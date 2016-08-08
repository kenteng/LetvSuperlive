package com.lesports.stadium.bean;

public class PayParametric {
	
	private String version;
	
    private String service;
    
    private String merchant_business_id;
    
    private String user_id;
    
    private String user_name;
    
    //private String letv_user_id").append("=").append(mLetvUserId.getText().toString().trim());
    private String notify_url;
    
    //private String call_back_url").append("=").append(mNotifyUrl.getText().toString().trim());
    private String merchant_no;
    
    private String out_trade_no;
    
    private String price;
    
    private String currency;
    
    private String pay_expire;
    
    private String product_id;
    
    private String product_name;
    
    private String product_desc;
    
    private String product_urls;
    
    private String timestamp;
    
    private String key_index;
    
    private String input_charset;
    
    private String ip;
    
    /**
     * sign 为安全保证，需商户服务器进行签名，签名秘钥不暴露给客户端；
     */
    private String sign;
    
    private String sign_type;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMerchant_business_id() {
		return merchant_business_id;
	}

	public void setMerchant_business_id(String merchant_business_id) {
		this.merchant_business_id = merchant_business_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPay_expire() {
		return pay_expire;
	}

	public void setPay_expire(String pay_expire) {
		this.pay_expire = pay_expire;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}

	public String getProduct_urls() {
		return product_urls;
	}

	public void setProduct_urls(String product_urls) {
		this.product_urls = product_urls;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getKey_index() {
		return key_index;
	}

	public void setKey_index(String key_index) {
		this.key_index = key_index;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
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

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
    
    
    
}
