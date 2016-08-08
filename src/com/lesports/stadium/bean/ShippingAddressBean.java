package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * @ClassName:  ShippingAddressBean 
 * 
 * @Desc : 收货地址实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-16 下午4:25:10
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class ShippingAddressBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户昵称
	 */
	private String userName;
	/**
	 * 用户电话号
	 */
	private String userPhone;
	/**
	 * 邮政编码
	 */
	private String postcode;
	/**
	 * 省市区
	 */
	private String userCity;
	/**
	 * 街道地址
	 */
	private String userStreet;
	/**
	 * 详细地址
	 */
	private String userAddress;
	/**
	 * 是否选择默认地址
	 */
	private boolean userSelected;
	/**
	 * 地址id
	 */
	private String id;
	/**
	 * 区id
	 */
	private String areaId;
	/**
	 * 城市Id
	 */
	private String cityId;
	/**
	 * 省份
	 */
	private String provinceId;
	/**
	 * 座机号
	 */
	private String telphone;
	private String isDefault;
	
	public ShippingAddressBean(){}
	
	public ShippingAddressBean(String userName, String userPhone,
			String postcode, String userCity, String userStreet,
			String userAddress, boolean userSelected, String id, String areaId,
			String cityId, String provinceId, String telphone, String isDefault) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.postcode = postcode;
		this.userCity = userCity;
		this.userStreet = userStreet;
		this.userAddress = userAddress;
		this.userSelected = userSelected;
		this.id = id;
		this.areaId = areaId;
		this.cityId = cityId;
		this.provinceId = provinceId;
		this.telphone = telphone;
		this.isDefault = isDefault;
	}

	public ShippingAddressBean(String userName, String userPhone,
			String postcode, String userCity, String userStreet,
			String userAddress, boolean userSelected, String id, String areaId,
			String cityId, String provinceId, String telphone) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.postcode = postcode;
		this.userCity = userCity;
		this.userStreet = userStreet;
		this.userAddress = userAddress;
		this.userSelected = userSelected;
		this.id = id;
		this.areaId = areaId;
		this.cityId = cityId;
		this.provinceId = provinceId;
		this.telphone = telphone;
	}

	public ShippingAddressBean(String userName, String userPhone,
			String postcode, String userCity, String userStreet,
			String userAddress, boolean userSelected) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.postcode = postcode;
		this.userCity = userCity;
		this.userStreet = userStreet;
		this.userAddress = userAddress;
		this.userSelected = userSelected;
	}

	public ShippingAddressBean(String userName, String userPhone,
			String userAddress, boolean userSelected) {
		super();
		this.userName = userName;
		this.userPhone = userPhone;
		this.userAddress = userAddress;
		this.userSelected = userSelected;
	}
	
	
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getUserCity() {
		return userCity;
	}
	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}
	public String getUserStreet() {
		return userStreet;
	}
	public void setUserStreet(String userStreet) {
		this.userStreet = userStreet;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public boolean isUserSelected() {
		return userSelected;
	}
	public void setUserSelected(boolean userSelected) {
		this.userSelected = userSelected;
	}
}
