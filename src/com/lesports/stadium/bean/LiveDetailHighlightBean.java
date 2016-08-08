package com.lesports.stadium.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LiveDetailHighlightBean implements Serializable{
	private String activityId;
	private String fileClass;
	private String fileName;
	private String fileProfile;
	private String fileSource;
	private String fileType;
	private String fileUrl;
	private String id;
	private String resourceId;
	private String uploadDate;
	private String videoImageUrl;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getFileClass() {
		return fileClass;
	}
	public void setFileClass(String fileClass) {
		this.fileClass = fileClass;
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
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getVideoImageUrl() {
		return videoImageUrl;
	}
	public void setVideoImageUrl(String videoImageUrl) {
		this.videoImageUrl = videoImageUrl;
	}
	
}
