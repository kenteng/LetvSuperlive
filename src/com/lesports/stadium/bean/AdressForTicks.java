package com.lesports.stadium.bean;

public class AdressForTicks {
	// 收货地址
	private String addressName;
	// 城市 北京市|北京市|西城区
	private String allCityDetail;
	// 用户名称
	private String userName;
	// 用户手机号
	private String userPhoneNumber;
	// 是否是默认地址 true 是
	private boolean isDefalut;

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAllCityDetail() {
		return allCityDetail;
	}

	public void setAllCityDetail(String allCityDetail) {
		this.allCityDetail = allCityDetail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public boolean isDefalut() {
		return isDefalut;
	}

	public void setDefalut(boolean isDefalut) {
		this.isDefalut = isDefalut;
	}

}
