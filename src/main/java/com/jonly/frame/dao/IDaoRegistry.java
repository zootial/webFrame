package com.jonly.frame.dao;

/**
 * @author jonly
 * @version 1.00
 *
 */
public interface IDaoRegistry {

    public <T> Class<IDao<T>> getDaoClass(Class<T> clazz);
    public <T> GenericDao<T> getGenericDao(Class<T> modelClazz);
}
