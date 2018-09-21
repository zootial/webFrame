package com.jonly.frame.util.pagination.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonly.frame.util.pagination.Page;
import com.jonly.frame.util.pagination.mybatis.parser.AbstractParser;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisPagePlugin implements Interceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MybatisPagePlugin.class);
    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Page page = null;
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
            MetaObject mo = SystemMetaObject.forObject(statementHandler);

            Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
            if (parameterObject == null) {
                return invocation.proceed();
            }
            page = findPageObject(parameterObject);
            if (page == null) {
                return invocation.proceed();
            }

            MappedStatement mappedStatement = (MappedStatement) mo.getValue("delegate.mappedStatement");
            Configuration configuration = mappedStatement.getConfiguration();
            Connection connection = (Connection) invocation.getArgs()[0];

            guessDialect(connection);
            AbstractParser parser = AbstractParser.newParser(dialect);

            BoundSql boundSql = statementHandler.getBoundSql();
            if (page.getTotalPage() < 0) {
                String sql = boundSql.getSql();
                String countSql = parser.getCountSql(sql);
                PreparedStatement countStmt = connection.prepareStatement(countSql);
                BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                        boundSql.getParameterMappings(), parameterObject);
                mo = SystemMetaObject.forObject(boundSql);
                @SuppressWarnings("unchecked")
                Map<String, Object> additionalParam = (Map<String, Object>) mo.getValue("additionalParameters");
                for (String key : additionalParam.keySet()) {
                    countBS.setAdditionalParameter(key, additionalParam.get(key));
                }
                setParameters(countStmt, mappedStatement, countBS, parameterObject);
                ResultSet rs = countStmt.executeQuery();
                if (rs.next()) {
                    page.setTotalRecord(rs.getInt(1));
                }
                rs.close();
                countStmt.close();
            }

            parser.preparePageSql(configuration, statementHandler.getParameterHandler(), mappedStatement, boundSql,
                    page);
        }

        return invocation.proceed();
    }

    protected Page findPageObject(Object parameterObj) {
        Page page = null;
        if (parameterObj instanceof Page) {
            page = (Page) parameterObj;
            return page;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof Page) {
                    page = (Page) val;
                    return page;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialect = properties.getProperty("dialect");
        if (dialect != null) {
            this.dialect = Dialect.of(dialect);
        }
    }

    protected void guessDialect(Connection connection) throws SQLException {
        try {
            if (this.dialect == null) {
                String productName = connection.getMetaData().getDatabaseProductName();
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Database productName: " + productName);
                }
                this.dialect = Dialect.of(productName.toLowerCase());
            }
        } catch (SQLException e) {

        }
    }

    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
            Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value = null;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
                            && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value)
                                    .getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        try {
                            value = metaObject == null ? null : metaObject.getValue(propertyName);
                        } catch (Exception e) {
                        }
                    }
                    @SuppressWarnings("unchecked")
                    TypeHandler<Object> typeHandler = (TypeHandler<Object>) parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
                                + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value,
                            parameterMapping.getJdbcType() == null ? JdbcType.VARCHAR : parameterMapping.getJdbcType());
                }
            }
        }
    }

}
