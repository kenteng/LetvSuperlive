package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: GuanggaoActivitys
 * 
 * @Description: 首页广告解析 活动数据
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-19 上午11:42:38
 * 
 * 
 */
public class GuanggaoActivitys implements Serializable {
	private String backgroudImageURL;
	private String cHeat;
	private String camptype;
	private String cStatus;
	private String copyright;
	private String creattime;
	private String endtime;
	private String frontCoverImageURL;
	private String id;
	private String label;
	private String productId;
	private String selltimeend;
	private String selltimestart;
	private String starttime;
	private String subhead;
	private String summary;
	private String tips;
	private String title;
	private String venueId;
	// hasScreamed字段0：表示没有尖叫过1：表示已经尖叫过
	private String hasScreamed;
	// 球队 hasSupported：0未支持；1已支持
	private String hasSupported;
	//
	private String resourceId;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getBackgroudImageURL() {
		return backgroudImageURL;
	}

	public String getcHeat() {
		return cHeat;
	}

	public String getCamptype() {
		return camptype;
	}

	public String getcStatus() {
		return cStatus;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getCreattime() {
		return creattime;
	}

	public String getEndtime() {
		return endtime;
	}

	public String getFrontCoverImageURL() {
		return frontCoverImageURL;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getProductId() {
		return productId;
	}

	public String getSelltimeend() {
		return selltimeend;
	}

	public String getSelltimestart() {
		return selltimestart;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getSubhead() {
		return subhead;
	}

	public String getSummary() {
		return summary;
	}

	public String getTips() {
		return tips;
	}

	public String getTitle() {
		return title;
	}

	public String getVenueId() {
		return venueId;
	}

	public String getHasScreamed() {
		return hasScreamed;
	}

	public String getHasSupported() {
		return hasSupported;
	}

	public void setBackgroudImageURL(String backgroudImageURL) {
		this.backgroudImageURL = backgroudImageURL;
	}

	public void setcHeat(String cHeat) {
		this.cHeat = cHeat;
	}

	public void setCamptype(String camptype) {
		this.camptype = camptype;
	}

	public void setcStatus(String cStatus) {
		this.cStatus = cStatus;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public void setFrontCoverImageURL(String frontCoverImageURL) {
		this.frontCoverImageURL = frontCoverImageURL;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setSelltimeend(String selltimeend) {
		this.selltimeend = selltimeend;
	}

	public void setSelltimestart(String selltimestart) {
		this.selltimestart = selltimestart;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public void setHasScreamed(String hasScreamed) {
		this.hasScreamed = hasScreamed;
	}

	public void setHasSupported(String hasSupported) {
		this.hasSupported = hasSupported;
	}

}
