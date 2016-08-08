package com.lesports.stadium.bean;

/**
 * ***************************************************************
 * 
 * @ClassName: YongCheBean
 * 
 * @Desc : 车型对象
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-24 下午4:50:02
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class YongCheBean {
	/**
	 * 车类型id
	 */
	private String car_type_id;
	/**
	 * 车型名称
	 */
	private String name;
	/**
	 * 车型简介
	 */
	private String brand;
	/**
	 * 可乘人数
	 */
	private String person_number;
	/**
	 * 车型图片
	 */
	private String pic;
	/**
	 * 最小起租时长(单位:秒)
	 */
	private String min_time_length;
	/**
	 * 最小起租费用（单位：分)
	 */
	private String min_fee;
	/**
	 * 每公里钱数（单位：分）
	 */
	private String fee_per_kilometer;
	/**
	 * 每分钟钱数（单位：分）
	 */
	private String fee_per_hour;
	/**
	 * 夜间服务费（单位：分）
	 */
	private String night_service_fee;
	/**
	 * 空驶距离，超过该距离，加收空驶费（单位：米）
	 */
	private String kongshi_distance;
	/**
	 * 机场服务费（单位：分）
	 */
	private String airport_service_fee;
	/**
	 * 最小响应时长（单位：秒）
	 */
	private String min_response_time;
	public YongCheBean(){}
	public YongCheBean(String car_type_id, String name, String brand,
			String person_number, String pic, String min_time_length,
			String min_fee, String fee_per_kilometer, String fee_per_hour,
			String night_service_fee, String kongshi_distance,
			String airport_service_fee, String min_response_time) {
		super();
		this.car_type_id = car_type_id;
		this.name = name;
		this.brand = brand;
		this.person_number = person_number;
		this.pic = pic;
		this.min_time_length = min_time_length;
		this.min_fee = min_fee;
		this.fee_per_kilometer = fee_per_kilometer;
		this.fee_per_hour = fee_per_hour;
		this.night_service_fee = night_service_fee;
		this.kongshi_distance = kongshi_distance;
		this.airport_service_fee = airport_service_fee;
		this.min_response_time = min_response_time;
	}
	public String getCar_type_id() {
		return car_type_id;
	}
	public void setCar_type_id(String car_type_id) {
		this.car_type_id = car_type_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPerson_number() {
		return person_number;
	}
	public void setPerson_number(String person_number) {
		this.person_number = person_number;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getMin_time_length() {
		return min_time_length;
	}
	public void setMin_time_length(String min_time_length) {
		this.min_time_length = min_time_length;
	}
	public String getMin_fee() {
		return min_fee;
	}
	public void setMin_fee(String min_fee) {
		this.min_fee = min_fee;
	}
	public String getFee_per_kilometer() {
		return fee_per_kilometer;
	}
	public void setFee_per_kilometer(String fee_per_kilometer) {
		this.fee_per_kilometer = fee_per_kilometer;
	}
	public String getFee_per_hour() {
		return fee_per_hour;
	}
	public void setFee_per_hour(String fee_per_hour) {
		this.fee_per_hour = fee_per_hour;
	}
	public String getNight_service_fee() {
		return night_service_fee;
	}
	public void setNight_service_fee(String night_service_fee) {
		this.night_service_fee = night_service_fee;
	}
	public String getKongshi_distance() {
		return kongshi_distance;
	}
	public void setKongshi_distance(String kongshi_distance) {
		this.kongshi_distance = kongshi_distance;
	}
	public String getAirport_service_fee() {
		return airport_service_fee;
	}
	public void setAirport_service_fee(String airport_service_fee) {
		this.airport_service_fee = airport_service_fee;
	}
	public String getMin_response_time() {
		return min_response_time;
	}
	public void setMin_response_time(String min_response_time) {
		this.min_response_time = min_response_time;
	}
	
}
