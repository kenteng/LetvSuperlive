package com.lesports.stadium.bean;

/**
 * ***************************************************************
 * @ClassName:  CityBean 
 * 
 * @Desc : 收货地址，选择省市区，area实体
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-4-12 下午2:44:44
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class CityBean {
	private String area;
	private String code;
	private String levelType;
	private String name;
	private String ID;
	public CityBean(){}
	public CityBean(String area, String code) {
		super();
		this.area = area;
		this.code = code;
	}
	
	public CityBean(String area, String code, String levelType, String name,
			String iD) {
		super();
		this.area = area;
		this.code = code;
		this.levelType = levelType;
		this.name = name;
		ID = iD;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	
}
