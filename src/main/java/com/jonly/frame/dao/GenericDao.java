package com.jonly.frame.dao;

import java.util.List;

import com.jonly.frame.dto.SqlParam;
import com.jonly.frame.util.pagination.Page;

public abstract class GenericDao<T> {
    public abstract long count(SqlParam cond) throws DaoException;

    public abstract int delete(SqlParam cond) throws DaoException;

    public abstract int deleteById(Long id) throws DaoException;

    public abstract int insert(T record) throws DaoException;

    public abstract int insertSelective(T record) throws DaoException;

    public abstract List<T> select(SqlParam cond, Page page) throws DaoException;

    public abstract T selectById(Long id) throws DaoException;

    public abstract int updateSelective(T record, SqlParam cond) throws DaoException;

    public abstract int update(T record, SqlParam cond) throws DaoException;

    public abstract int updateByIdSelective(T record) throws DaoException;

    public abstract int updateById(T record) throws DaoException;
}
