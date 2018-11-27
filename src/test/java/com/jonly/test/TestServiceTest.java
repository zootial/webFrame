package com.jonly.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:conf/test-spring-context.xml", "classpath:conf/test-spring-mybatis.xml"})
public class TestServiceTest/* extends AbstractJUnit4SpringContextTests*/ {

//	@Resource
//	private TestService testService;
	
	@Test
	public void test(){

//		insert();
//		query();
//	    testService.update(2, "123");
	}
	
//	void insert(){
//		com.jonly.test.model.Test test = new com.jonly.test.model.Test();
//		test.setName("123");
//		test.setStatus(1);
//		testService.save(test);
//		System.out.println(test);
//	}
	
	void query(){
//		Page page = new Page();
//		List<com.jonly.test.model.Test> list = testService.query(1, page);
//		System.out.println(page.getTotalRecord());
//		System.out.println(list);
//		testService.update(2, "123");
	}
}
