package com.lesports.stadium.bean;

/**
 * 场馆 信息
 * 
 * @author wangzn
 * 
 */
public class Venue {

	private String address;
	private String city;
	private String id;
	private String ip;
	private String latitude;
	private String longitude;
	private String province;
	private String remark;
	private String ssid;
	private String vStatus;
	private String venueName;
	private String buildId;
	private String supportGuide;
	
	

	public String getSupportGuide() {
		return supportGuide;
	}

	public void setSupportGuide(String supportGuide) {
		this.supportGuide = supportGuide;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getvStatus() {
		return vStatus;
	}

	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	@Override
	public String toString() {
		return "Venue [address=" + address + ", city=" + city + ", id=" + id
				+ ", ip=" + ip + ", latitude=" + latitude + ", longitude="
				+ longitude + ", province=" + province + ", remark=" + remark
				+ ", ssid=" + ssid + ", vStatus=" + vStatus + ", venueName="
				+ venueName + "]";
	}

}
