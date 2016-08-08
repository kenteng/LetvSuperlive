package com.lesports.stadium.bean;

/**
 * ***************************************************************
 * 
 * @ClassName: DiscussBean
 * 
 * @Desc : 讨论实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-8 下午6:43:41
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class DiscussBean {
	/**
	 * 发布人名字
	 */
	private String userName;
	/**
	 * 发布人头像
	 */
	private String userImage;
	/**
	 * 发布内容
	 */
	private String content;
	/**
	 * 发布时间
	 */
	private String releaseDate;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 点赞数
	 */
	private String likeCount;
	/**
	 * 视屏id
	 */
	private String fileId;
	/**
	 * 活动id
	 */
	private String activityId;
	private String dType;
	private String did;
	/**
	 * 是否是文案
	 */
	private boolean isWenan;
	/**
	 * 文案id
	 */
	private String wenAnId;

	public String getWenAnId() {
		return wenAnId;
	}

	public void setWenAnId(String wenAnId) {
		this.wenAnId = wenAnId;
	}

	public DiscussBean() {
	}

	public DiscussBean(String userName, String userImage, String content,
			String releaseDate, String userId, String likeCount, String fileId,
			String activityId, String dType, String did) {
		super();
		this.userName = userName;
		this.userImage = userImage;
		this.content = content;
		this.releaseDate = releaseDate;
		this.userId = userId;
		this.likeCount = likeCount;
		this.fileId = fileId;
		this.activityId = activityId;
		this.dType = dType;
		this.did = did;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getdType() {
		return dType;
	}

	public void setdType(String dType) {
		this.dType = dType;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public boolean isWenan() {
		return isWenan;
	}

	public void setWenan(boolean isWenan) {
		this.isWenan = isWenan;
	}

}
