package com.jonly.frame.model;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {

	private static final long serialVersionUID = -7898555931893554095L;

	private Integer id;
	
	private Date createTime;
	
	private Date lastUpdateTime;

	private Integer dataVersion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Integer dataVersion) {
		this.dataVersion = dataVersion;
	}
	
	
}