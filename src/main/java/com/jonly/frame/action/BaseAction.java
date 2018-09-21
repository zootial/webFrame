package com.jonly.frame.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jonly.frame.service.GenericService;

@Controller
public class BaseAction {
	
//	protected Map<String, Class<?>> modelMap = new HashMap<String, Class<?>>();
//
//	@Resource
//	private GenericService<?> service;

//	@RequestMapping(method=RequestMethod.GET, value="/domain/{type}/{id}",
//			produces="application/json")
//	@ResponseBody
//	public Object getDomainObject(@PathVariable String type, @PathVariable String id) {
//		Object e = service.getById(Integer.valueOf(id), this.getDomainClass(type));
//		return e;
//	}
	
//	@RequestMapping(method=RequestMethod.GET, value="/domain/{type}",
//			produces="application/json")
//	@ResponseBody
//	public List<Test> queryDomainObject(@PathVariable String type){
//		TestService service = (TestService)this.getService(type);
//		Page<Test> page = new Page<Test>();
//		page.setPageNo(1);
//		page.setPageSize(1);
//		List<Test> list = service.query(1, page);
//		return list;
//	}
	
//	@RequestMapping(method=RequestMethod.PUT, value="/domain/{type}",
//			produces="text/plain", consumes={"application/json"})
//	@ResponseBody
//	public Object modifyDomainObject(@PathVariable String type, @RequestBody String content) {
//		@SuppressWarnings("unchecked")
//		Object model = JSONObject.parseObject(content, this.getDomainClass(type));
//		service.modify(model);
//		return model;
//	}
//	
//	
//	@RequestMapping(method=RequestMethod.POST, value="/domain/{type}",
//			produces="application/json", consumes={"application/json"})
//	@ResponseBody
//	public Object addDomainObject(@PathVariable String type, @RequestBody String content) {
//		Object model = JSONObject.parseObject(content, this.getDomainClass(type));
//		service.save(model);
//		return model;
//	}
//	
//	@RequestMapping(method=RequestMethod.DELETE, value="/domain/{type}/{id}",
//			produces="text/plain")
//	@ResponseBody
//	public Object removeDomainObject(@PathVariable String type, @PathVariable String id) {
//		service.removeById(Integer.valueOf(id));
//		return e;
//	}
//
//	
//	private Class<?> getDomainClass(String typeName){
//		return modelMap.get(typeName);
//	}
//	
//	protected void registerDomainClass(String typeName, Class<?> clazz){
//		modelMap.put(typeName, clazz);
//	}
}
