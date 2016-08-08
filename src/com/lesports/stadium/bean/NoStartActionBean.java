package com.lesports.stadium.bean;

public class NoStartActionBean {
	
	/**
	 * 海报或者视频id
	 */
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIsTag() {
		return isTag;
	}
	public void setIsTag(String isTag) {
		this.isTag = isTag;
	}
	/**
	 * imageURL
	 */
	private String url;
	/**
	 * 标记，是海报还是视频
	 */
	private String isTag;

}
