package com.lesports.stadium.adapter;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : (视频播放界面底部评论列表的数据实体类)
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
public class VideoBottomCommantBean {

	/**
	 * 头像url
	 */
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 评论者名称
	 */
	private String name;
	/**
	 * 评论内容
	 */
	private String content;
}
