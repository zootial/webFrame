package com.jonly.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonly.frame.dao.GenericDao;
import com.jonly.frame.service.GenericService;
import com.jonly.frame.util.pagination.Page;
import com.jonly.test.dao.mapper.TestMapper;
import com.jonly.test.dto.TestExample;
import com.jonly.test.model.Test;

@Service
public class TestService extends GenericService<Test>{

//	@Resource 
//	private TestDao testDao;
	
	@Resource 
	private TestMapper testMapper;
	

	@Transactional
	public List<Test> query(Integer status, Page page){
//	    TestExample ex = new TestExample();
//	    ex.createCriteria().andStatusEqualTo(status);
//	    GenericDao<Test> dao = getDao();
//		return dao.selectByExample(ex, page);
	    return null;
	}
	
	@Transactional
	public int update(Integer status, String name) {
//	    Test record = new Test();
//	    record.setStatus(status);
//	    TestExample ex = new TestExample();
//        ex.createCriteria().andNameEqualTo(name);
//        return getDao().updateByExampleSelective(record, ex);
	    return 1;
	}
}
