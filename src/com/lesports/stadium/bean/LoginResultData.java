package com.lesports.stadium.bean;

import com.letv.component.core.http.bean.LetvBaseBean;

public class LoginResultData implements LetvBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7689300022614362631L;
	private LoginUserInfo bean;
	private String status;
	private String errorCode;
	private String message;
	private String tv_token;
	private String sso_tk;
	public String getTv_token() {
		return tv_token;
	}
	public void setTv_token(String tv_token) {
		this.tv_token = tv_token;
	}
	public String getSso_tk() {
		return sso_tk;
	}
	public void setSso_tk(String sso_tk) {
		this.sso_tk = sso_tk;
	}
	public LoginUserInfo getBean() {
		return bean;
	}
	public void setBean(LoginUserInfo bean) {
		this.bean = bean;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
