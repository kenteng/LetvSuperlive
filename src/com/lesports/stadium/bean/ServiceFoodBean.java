package com.lesports.stadium.bean;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 服务餐饮部分数据实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class ServiceFoodBean {

	/**
	 *  "address": "西区A2018",
        "companyname": "棒约翰",
        "contact": "李先生",
        "id": 1570,
        "mobilephone": "18210362250",
        "telephone": "18210362250",
        "zipcode": "412200"
        "imageUrl":"/upload/image001.png",


"address":"西区A2019",
	"companyname":"满记甜品",
	"contact":"王先生",
	"endbusinesstime":"20:00",
	"id":1571,
	"loginname":"manjitianpin",
	"mobilephone":"18210362251",
	"startbusinesstime":"都是反复",
	"telephone":"18210362250",
	"zipcode":"412200"
	 */
	/**
	 * 地址
	 */
	/**
	 * 开店时间
	 */
	private String startbusinesstime;
	public String getStartTime() {
		return startbusinesstime;
	}
	public void setStartTime(String startTime) {
		this.startbusinesstime = startTime;
	}
	public String getEndTime() {
		return endbusinesstime;
	}
	public void setEndTime(String endTime) {
		this.endbusinesstime = endTime;
	}
	/**
	 * 关门时间
	 */
	private String endbusinesstime;
	private String address;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	/**
	 * 店铺名称
	 */
	private String companyname;
	/**
	 * 店主
	 */
	private String contact;
	/**
	 * 店铺id
	 */
	private String id;
	/**
	 * 手机电话
	 */
	private String mobilephone;
	/**
	 *座机电话
	 */
	private String telephone;
	/**
	 * 
	 */
	private String zipcode;
	/**
	 * 图片url
	 */
	private String imageUrl;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
