package com.jonly.frame.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jonly.frame.dao.GenericDao;
import com.jonly.frame.dao.IDaoRegistry;
import com.jonly.frame.dto.SqlParam;
import com.jonly.frame.util.pagination.Page;

@Service
public class GenericService<T> implements IGenericService<T> {

    @Resource
    protected IDaoRegistry daoRegistry;

    @SuppressWarnings("unchecked")
    @Override
    public int save(T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).insert(model);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int saveSelective(T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).insertSelective(model);
    }

    @Override
    public int removeById(Long id, Class<T> clazz) {
        return daoRegistry.getGenericDao(clazz).deleteById(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int modify(T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).updateById(model);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int modifySelective(T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).updateByIdSelective(model);
    }

    @Override
    public T getById(Long id, Class<T> clazz) {
        T obj = daoRegistry.getGenericDao(clazz).selectById(id);
        return obj;
    }
    
    @Override
    public long total(SqlParam cond, Class<T> clazz) {
        return daoRegistry.getGenericDao(clazz).count(cond);
    }

    @Override
    public int remove(SqlParam cond, Class<T> clazz) {
        return daoRegistry.getGenericDao(clazz).delete(cond);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int modify(SqlParam cond, T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).update(model, cond);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int modifySelective(SqlParam cond, T model) {
        return daoRegistry.getGenericDao((Class<T>) model.getClass()).updateSelective(model, cond);
    }

    @Override
    public List<T> query(SqlParam cond, Page page, Class<T> clazz) {
        return daoRegistry.getGenericDao(clazz).select(cond, page);
    }

    protected GenericDao<T> getDao() {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return daoRegistry.getGenericDao(clazz);
    }

    public IDaoRegistry getDaoRegistry() {
        return daoRegistry;
    }

    public void setDaoRegistry(IDaoRegistry daoRegistry) {
        this.daoRegistry = daoRegistry;
    }

   

}
