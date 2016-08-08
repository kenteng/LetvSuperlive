package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 支付参数实体类
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
public class PayBean implements Serializable{

	/**
	 * {
    "product_urls": "http://interface.jingzhaokeji.com:8070/letv_mgr/upload/img/image011.png",
    "merchant_no": "88893",
    "product_id": "94195",
    "sign_type": "MD5",
    "product_name": "演唱会",
    "input_charset": "UTF-8",
    "notify_url": "http://192.168.1.203:8070/letv_mgr/notifyUrl.do",
    "product_desc": "可乐",
    "pay_expire": "60",
    "out_trade_no": "2016032400055",
    "version": "2.0",
    "currency": "CNY",
    "ip": "192.168.1.51",
    "sign": "4a4bc3a449ea7d79fa6b390a00c21507",
    "key_index": "1",
    "timestamp": "2016-03-24 19:21:12",
    "user_name": "letv_56c28303960e450",
    "price": "5.0",
    "service": "lepay.app.api.show.cashier",
    "user_id": "171828113",
    "merchant_business_id": "13"
    
    
    
    {
    "merchant_no": "97",
    "product_id": "13",
    "sign_type": "MD5",
    "product_name": "10元充值100积分",
    "input_charset": "UTF-8",
    "notify_url": "http://interface.jingzhaokeji.com:8070/letv_mgr/notify/integralNotifyUrl.do",
    "product_desc": "10元充值100积分",
    "pay_expire": "45",
    "out_trade_no": "20160425104826",
    "version": "2.0",
    "currency": "CNY",
    "ip": "192.168.1.84",
    "sign": "4d6bfa24c9e9215e97c963d25fdc5d16",
    "key_index": "1",
    "timestamp": "2016-04-25 10:48:26",
    "user_name": "137xxxxx459_674",
    "price": "0.01",
    "service": "lepay.app.api.show.cashier",
    "user_id": "1",
    "merchant_business_id": "13"
}
}
	 */
	
	private String product_urls;
	public String getProduct_urls() {
		return product_urls;
	}
	public void setProduct_urls(String product_urls) {
		this.product_urls = product_urls;
	}
	public String getMerchant_no() {
		return merchant_no;
	}
	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getInput_charset() {
		return input_charset;
	}
	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getProduct_desc() {
		return product_desc;
	}
	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}
	public String getPay_expire() {
		return pay_expire;
	}
	public void setPay_expire(String pay_expire) {
		this.pay_expire = pay_expire;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getKey_index() {
		return key_index;
	}
	public void setKey_index(String key_index) {
		this.key_index = key_index;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getMerchant_business_id() {
		return merchant_business_id;
	}
	public void setMerchant_business_id(String merchant_business_id) {
		this.merchant_business_id = merchant_business_id;
	}
	private String merchant_no;
	private String product_id;
	private String sign_type;
	private String product_name;
	private String input_charset;
	private String notify_url;
	private String product_desc;
	private String pay_expire;
	private String out_trade_no;
	private String version;
	private String currency;
	private String ip;
	private String sign;
	private String key_index;
	private String timestamp;
	private String user_name;
	private String price;
	private String service;
	private String user_id;
	private String merchant_business_id;
}
