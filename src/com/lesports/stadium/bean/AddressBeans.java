package com.lesports.stadium.bean;
/**
 * 购票订单的地址数据
 * @author JZKJ-LWC
 *"{\"userName\":\"用户名\",\"city\":\"北京市|北京市|东城区\",\"telePhone\":\"12345678901\",\"address\":\"街道名称\",\"area\":\"五环外\"}",

 */
public class AddressBeans {
	
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	private String city;
	private String telePhone;
	private String address;
	private String area;

}
