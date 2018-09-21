package com.jonly.frame.util.pagination.mybatis.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import com.jonly.frame.util.pagination.Page;
import com.jonly.frame.util.pagination.mybatis.Constant;
import com.jonly.frame.util.pagination.mybatis.Dialect;

public abstract class AbstractParser implements Constant {

    public static AbstractParser newParser(Dialect dialect) {
        AbstractParser parser = null;
        switch (dialect) {
        case mysql:
            parser = new MysqlParser();
            break;
        case oracle:
            parser = new OracleParser();
            break;
        default:
            throw new RuntimeException("database dialect '" + dialect + "' is not supported by pagination plugin!");
        }
        return parser;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map<String, Object> processParameter(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> paramMappings = boundSql.getParameterMappings();
        Map<String, Object> paramMap = null;
        if (parameterObject instanceof Map) {
            paramMap = new HashMap<String, Object>();
            paramMap.putAll((Map) parameterObject);
        } else if (parameterObject == null) {
            paramMap = new HashMap<String, Object>();
        } else {
            paramMap = new HashMap<String, Object>();
            boolean hasTypeHandler = configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
            MetaObject metaObject = SystemMetaObject.forObject(parameterObject);
            if (!hasTypeHandler) {
                for (String name : metaObject.getGetterNames()) {
                    paramMap.put(name, metaObject.getValue(name));
                }
            }
            for (ParameterMapping parameterMapping : paramMappings) {
                String name = parameterMapping.getProperty();
                if (PAGEPARAMETER_FIRST.equals(name) || PAGEPARAMETER_SECOND.equals(name)
                        || paramMap.get(name) != null) {
                    continue;
                }
                if (hasTypeHandler || parameterMapping.getJavaType().equals(parameterObject.getClass())) {
                    paramMap.put(name, parameterObject);
                    break;
                }
            }
        }
        return paramMap;
    }

    public String getCountSql(final String sql) {
        isSupportedSql(sql);
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(0) from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    public void isSupportedSql(String sql) {
        if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new RuntimeException("unsupported 'for update' sql statement");
        }
    }

    public void preparePageSql(Configuration configuration, ParameterHandler paramHandler, MappedStatement ms,
            BoundSql boundSql, Page page) {
        String pageSql = getPageSql(boundSql.getSql());
        MetaObject boudsqlmo = SystemMetaObject.forObject(boundSql);
        boudsqlmo.setValue("sql", pageSql);
        boudsqlmo.setValue("parameterMappings", getPageParameterMapping(configuration, boundSql));
        Map<String, Object> paramMap = processParameter(configuration, boundSql);
        paramMap = buildPageParameter(paramMap, page);
        boudsqlmo.setValue("parameterObject", paramMap);
        SystemMetaObject.forObject(paramHandler).setValue("parameterObject", paramMap);
    }

    protected abstract String getPageSql(String sql);

    public List<ParameterMapping> getPageParameterMapping(Configuration configuration, BoundSql boundSql) {
        List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
        if (boundSql != null && boundSql.getParameterMappings() != null) {
            newParameterMappings.addAll(boundSql.getParameterMappings());
        }
        newParameterMappings
                .add(new ParameterMapping.Builder(configuration, PAGEPARAMETER_FIRST, Integer.class).build());
        newParameterMappings
                .add(new ParameterMapping.Builder(configuration, PAGEPARAMETER_SECOND, Integer.class).build());
        return newParameterMappings;
    }

    protected abstract Map<String, Object> buildPageParameter(Map<String, Object> paramMap, Page page);
}
