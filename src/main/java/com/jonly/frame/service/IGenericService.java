package com.jonly.frame.service;

import java.util.List;

import com.jonly.frame.dto.SqlParam;
import com.jonly.frame.util.pagination.Page;

public interface IGenericService<T> {
    long total(SqlParam cond, Class<T> clazz);
    
	public int save(T model);
	
	public int saveSelective(T model);

	public int removeById(Long id, Class<T> clazz);
	
	public int remove(SqlParam cond, Class<T> clazz);
	
	public int modify(T model);
	
	public int modifySelective(T model);
	
	public int modify(SqlParam cond, T model);
	
	public int modifySelective(SqlParam cond, T model);
	
	public T getById(Long id, Class<T> clazz);
	
	public List<T> query(SqlParam cond, Page page, Class<T> clazz);
}
