package com.jonly.frame.dao;

/**
 *
 * @author jonly
 * @version 1.00
 *
 */
public interface IDao<T> {
    
    public static enum DaoMethod {
        insert,
        insertSelective,
        deleteById,
        delete,
        updateById,
        updateByIdSelective,
        updateSelective,
        update,
        selectById,
        select,
        count
        ;
    }
}
