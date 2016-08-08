/**
 * 
 */
package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 集锦列表项的实体类
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

public class HeightLightBean implements Serializable{
	
	/**
	 * 标题
	 */
	private String mTitle;
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmImageUrl() {
		return mImageUrl;
	}
	public void setmImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}
	/**
	 * id
	 */
	private String mId;
	/**
	 * 图片url
	 */
	private String mImageUrl;
	/**
	 * [
    {
        "activityId": 31,
        "fileName": "1312940701912.jpg",
        "fileProfile": null,
        "fileSource": null,
        "fileType": 0,
        "fileUrl": "/upload/video/1312940701912.jpg",
        "id": 30,
        "resourceId": "11",
        "uploadDate": 1457684382000,
        "videoImageUrl": ""
        
        
        
         "activityId": 30,
        "fileClass": 1,
        "fileName": "12",
        "fileProfile": "北京VS新疆",
        "fileSource": "2015-2016中国男子篮球联赛第35轮，排名第八的北京队主场对阵远道而来的新疆队，马布里的发挥将会很大程度上影响本场比赛的走势，最终影响季后赛北京队的路能走多远。",
        "fileType": 0,
        "fileUrl": "/upload/img/5.jpg",
        "id": 12,
        "uploadDate": 1450682820000,
        "videoImageUrl": "12"
        
        {"count":1,"data":[{"activityId":30,"fileClass":1,"fileName":"测试",
        "fileProfile":"测试","fileSource":"1","fileType":1,
        "fileUrl":"767e3b9499","id":79,"resourceId":"767e3b9499",
        "supportFullView":null,"uploadDate":1459432727000,
        "videoImageUrl":"/upload/video/演唱会封面.png"}],"page":1,
        "param":null,"paramEntity":null,"paramList":null,"paramMap":null,
        "rows":6,"totalPage":1}


    }
]
	 */
	private String supportFullView;
	public String getSupportFullView() {
		return supportFullView;
	}
	public void setSupportFullView(String supportFullView) {
		this.supportFullView = supportFullView;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	private String uploadDate;
	private String activityId;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileProfile() {
		return fileProfile;
	}
	public void setFileProfile(String fileProfile) {
		this.fileProfile = fileProfile;
	}
	public String getFileSource() {
		return fileSource;
	}
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getVideoImageUrl() {
		return videoImageUrl;
	}
	public void setVideoImageUrl(String videoImageUrl) {
		this.videoImageUrl = videoImageUrl;
	}
	private String fileName;
	private String fileProfile;
	private String fileSource;
	private String fileType;
	private String fileUrl;
	private String id;
	private String resourceId;
	private String videoImageUrl;

}
