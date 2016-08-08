package com.lesports.stadium.bean;

import java.io.Serializable;

public class Address implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 地址,addressTitle
	 */
	private String address;
	
	/**
	 * 具体位置,address
	 */
	private String specificLocation;
	
	/**
	 * 是否是当前位置
	 */
	private boolean isCurrent;
	/**
	 * 地址类型
	 */
	private String addressType;
	/**
	 * 用车类型
	 */
	private String useCarType;
	/**
	 * 地址经度
	 */
	private double longitude;
	/**
	 * 地址纬度
	 */
	private double latitude;
	/**
	 * 地址所在城市
	 */
	private String city;
	/**
	 * 地图类型
	 */
	private String mapType;
	private String earaCode;
	public Address(){}
	
	public Address(String address, String specificLocation, boolean isCurrent,
			String addressType, String useCarType, double longitude,
			double latitude, String city, String mapType, String earaCode) {
		super();
		this.address = address;
		this.specificLocation = specificLocation;
		this.isCurrent = isCurrent;
		this.addressType = addressType;
		this.useCarType = useCarType;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.mapType = mapType;
		this.earaCode = earaCode;
	}

	public String getEaraCode() {
		return earaCode;
	}

	public void setEaraCode(String earaCode) {
		this.earaCode = earaCode;
	}

	public Address(String address, String specificLocation, boolean isCurrent,
			String addressType, String useCarType, double longitude,
			double latitude, String city, String mapType) {
		super();
		this.address = address;
		this.specificLocation = specificLocation;
		this.isCurrent = isCurrent;
		this.addressType = addressType;
		this.useCarType = useCarType;
		this.longitude = longitude;
		this.latitude = latitude;
		this.city = city;
		this.mapType = mapType;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getUseCarType() {
		return useCarType;
	}

	public void setUseCarType(String useCarType) {
		this.useCarType = useCarType;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpecificLocation() {
		return specificLocation;
	}

	public void setSpecificLocation(String specificLocation) {
		this.specificLocation = specificLocation;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
}
