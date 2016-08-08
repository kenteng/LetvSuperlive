package com.lesports.stadium.bean;

/**
 * 支持城市类
 * @author 王宗宁
 *
 */
public class SupportCity {
	private String cityCode;
	/**
	 * 当前城市id
	 */
	private String id;
	private String lat;
	private String levelType;
	private String lng;
	private String mergerName;
	/**
	 * 当前城市 名称
	 */
	private String name;
	private String parentId;
	private String pinYin;
	private String shortName;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMergerName() {
		return mergerName;
	}

	public void setMergerName(String mergerName) {
		this.mergerName = mergerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return "SupportCity [cityCode=" + cityCode + ", id=" + id + ", lat="
				+ lat + ", levelType=" + levelType + ", lng=" + lng
				+ ", mergerName=" + mergerName + ", name=" + name
				+ ", parentId=" + parentId + ", pinYin=" + pinYin
				+ ", shortName=" + shortName + "]";
	}

	
}
