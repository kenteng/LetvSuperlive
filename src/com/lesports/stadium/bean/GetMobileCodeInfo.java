package com.lesports.stadium.bean;

import com.letv.component.core.http.bean.LetvBaseBean;

/**
 * 下发手机激活码bean
 * @author fjh
 */
public class GetMobileCodeInfo implements LetvBaseBean{
	
	private static final long serialVersionUID = -2175423527367966638L;
	private String action;
	private int status;
	private int errorCode;
	private String message;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
}
