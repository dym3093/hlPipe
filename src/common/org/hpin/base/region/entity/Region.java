package org.hpin.base.region.entity;

import org.hpin.common.core.orm.BaseEntity;

public class Region extends BaseEntity {
	
	/*
	 * 编号
	 */
	private String id;
	
	/*
	 * 行政区划
	 */
	private String regionName;
	
	/*
	 * 父节点
	 */
	private String parentId;
	
	/*
	 * 拼音码
	 */
	private String pym;
	
	/**
	 * 深度
	 */
	private Integer deep;
	
	/**
	 * 上级行政区划名称
	 */
	private String parentName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPym() {
		return pym;
	}

	public void setPym(String pym) {
		this.pym = pym;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getDeep() {
		return deep;
	}

	public void setDeep(Integer deep) {
		this.deep = deep;
	}

}
