package com.jonly.frame.dao.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jonly.frame.dao.DaoException;
import com.jonly.frame.dao.GenericDao;
import com.jonly.frame.dao.IDao;
import com.jonly.frame.dao.IDaoRegistry;
import com.jonly.frame.dto.SqlParam;
import com.jonly.frame.util.pagination.Page;

/**
 *
 * @author jonly
 * @version 1.00
 *
 */
public class MyBatisDaoRegistry implements IDaoRegistry, ApplicationContextAware {
    private SqlSessionFactory sqlSessionFactory;
    private Map<Class<?>, Class<?>> registry;
    private ApplicationContext context;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<IDao<T>> getDaoClass(Class<T> clazz) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if (this.registry == null) {
            this.registry = new HashMap<Class<?>, Class<?>>();
            Collection<Class<?>> list = configuration.getMapperRegistry().getMappers();
            for (Class<?> c : list) {
                if (!IDao.class.isAssignableFrom(c)) {
                    continue;
                }
                Class<?> modelClass = (Class<?>) ((ParameterizedType) c.getGenericInterfaces()[0])
                        .getActualTypeArguments()[0];
                this.registry.put(modelClass, c);
            }
        }
        Class<IDao<T>> daoClass = (Class<IDao<T>>) this.registry.get(clazz);
        return daoClass;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public <T> GenericDao<T> getGenericDao(Class<T> modelClazz) {
        Class<IDao<T>> daoClass = this.getDaoClass(modelClazz);
        final Class<?> targetDaoClass = daoClass;
        final IDao<T> dao = context.getBean(daoClass);
        return new GenericDao<T>() {

            @Override
            public long count(SqlParam cond) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.count.name(), SqlParam.class);
                    return (long) m.invoke(dao, cond);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int delete(SqlParam cond) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.delete.name(), SqlParam.class);
                    return (int) m.invoke(dao, cond);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int deleteById(Long id) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.deleteById.name(), Long.class);
                    return (int) m.invoke(dao, id);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int insert(T record) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.insert.name(), record.getClass());
                    return (int) m.invoke(dao, record);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int insertSelective(T record) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.insertSelective.name(), record.getClass());
                    return (int) m.invoke(dao, record);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<T> select(SqlParam cond, Page page) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.select.name(), SqlParam.class,
                            Page.class);
                    return (List<T>) m.invoke(dao, cond, page);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public T selectById(Long id) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.selectById.name(), Long.class);
                    return (T) m.invoke(dao, id);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int updateSelective(T record, SqlParam cond) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.updateSelective.name(),
                            record.getClass(), SqlParam.class);
                    return (int) m.invoke(dao, record, cond);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int update(T record, SqlParam cond) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.update.name(), record.getClass(),
                            SqlParam.class);
                    return (int) m.invoke(dao, record, cond);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int updateByIdSelective(T record) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.updateByIdSelective.name(),
                            record.getClass());
                    return (int) m.invoke(dao, record);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

            @Override
            public int updateById(T record) throws DaoException {
                try {
                    Method m = targetDaoClass.getMethod(IDao.DaoMethod.updateById.name(), record.getClass());
                    return (int) m.invoke(dao, record);
                } catch (Exception e) {
                    throw new DaoException(e);
                }
            }

        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
