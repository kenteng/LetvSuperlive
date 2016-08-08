package com.lesports.stadium.bean;

public class SelfMessageBean {
	private String createTime;
	private String hasReaded;
	private String id;
	private String messageContent;
	private String messageRemark;
	private String messageType;
	private String userId;
	private boolean isCheck=false;
	private boolean isEdit=false;
	
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getHasReaded() {
		return hasReaded;
	}
	public void setHasReaded(String hasReaded) {
		this.hasReaded = hasReaded;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageRemark() {
		return messageRemark;
	}
	public void setMessageRemark(String messageRemark) {
		this.messageRemark = messageRemark;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
