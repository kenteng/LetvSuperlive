package com.lesports.stadium.bean;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : (视频播放详情更多视频的实体类)
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
public class MoreVideoBean {
	
	/**
	 * 视频id
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 图片url
	 */
	private String url;
	/**
	 * 视频名称
	 */
	private String title;
	/**
	 * 视频时间
	 */
	private String time;

}
