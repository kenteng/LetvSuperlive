package com.lesports.stadium.bean;

/**
 * ***************************************************************
 * @ClassName:  DrivierBean 
 * 
 * @Desc : DriverBean
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-4-28 下午12:03:35
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class DrivierBean {
	/**
	 * 司机姓名
	 */
	private String name;
	/**
	 * 司机性别
	 */
	private String gender;
	/**
	 * 驾龄
	 */
	private String driving_years;
	/**
	 * 司机星级
	 */
	private String star_level;
	/**
	 * 车型
	 */
	private String brand;
	/**
	 * 牌照
	 */
	private String vehicle_number;
	/**
	 * 
	 */
	private String car_setup;
	private String car_company_name;
	/**
	 * 司机公司
	 */
	private String driver_company_name;
	private String is_default_photo;
	/**
	 * 头像链接
	 */
	private String photo_url;
	/**
	 * 电话号
	 */
	private String cellphone;
	private String unittime_complete_count;
	public DrivierBean(){}
	public DrivierBean(String name, String gender, String driving_years,
			String star_level, String brand, String vehicle_number,
			String car_setup, String car_company_name,
			String driver_company_name, String is_default_photo,
			String photo_url, String cellphone, String unittime_complete_count) {
		super();
		this.name = name;
		this.gender = gender;
		this.driving_years = driving_years;
		this.star_level = star_level;
		this.brand = brand;
		this.vehicle_number = vehicle_number;
		this.car_setup = car_setup;
		this.car_company_name = car_company_name;
		this.driver_company_name = driver_company_name;
		this.is_default_photo = is_default_photo;
		this.photo_url = photo_url;
		this.cellphone = cellphone;
		this.unittime_complete_count = unittime_complete_count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDriving_years() {
		return driving_years;
	}
	public void setDriving_years(String driving_years) {
		this.driving_years = driving_years;
	}
	public String getStar_level() {
		return star_level;
	}
	public void setStar_level(String star_level) {
		this.star_level = star_level;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getVehicle_number() {
		return vehicle_number;
	}
	public void setVehicle_number(String vehicle_number) {
		this.vehicle_number = vehicle_number;
	}
	public String getCar_setup() {
		return car_setup;
	}
	public void setCar_setup(String car_setup) {
		this.car_setup = car_setup;
	}
	public String getCar_company_name() {
		return car_company_name;
	}
	public void setCar_company_name(String car_company_name) {
		this.car_company_name = car_company_name;
	}
	public String getDriver_company_name() {
		return driver_company_name;
	}
	public void setDriver_company_name(String driver_company_name) {
		this.driver_company_name = driver_company_name;
	}
	public String getIs_default_photo() {
		return is_default_photo;
	}
	public void setIs_default_photo(String is_default_photo) {
		this.is_default_photo = is_default_photo;
	}
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getUnittime_complete_count() {
		return unittime_complete_count;
	}
	public void setUnittime_complete_count(String unittime_complete_count) {
		this.unittime_complete_count = unittime_complete_count;
	}
	
}
