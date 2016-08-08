package com.lesports.stadium.bean;

import java.io.Serializable;

import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 活动首界面数据源实体对象
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

public class SenceBean implements Serializable{
	
	
	/**
	 * 购票需要用到
	 */
	private String productId;
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * 用户是否已经报名
	 */
	private boolean isBaoming=false;
	public boolean isBaoming() {
		return isBaoming;
	}

	public void setBaoming(boolean isBaoming) {
		this.isBaoming = isBaoming;
	}
	/**
	 * item中的大标题
	 */
	private String title;

	public String getmTitle() {
		return title;
	}

	public void setmTitle(String title) {
		this.title = title;
	}
	/**
	 * 热度，受欢迎程度
	 */
	private String cHeat;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getcHeat() {
		return cHeat;
	}

	public void setcHeat(String cHeat) {
		this.cHeat = cHeat;
	}

	public String getcStatus() {
		return cStatus;
	}

	public void setcStatus(String cStatus) {
		this.cStatus = cStatus;
	}

	public String getCamptype() {
		return camptype;
	}

	public void setCamptype(String camptype) {
		this.camptype = camptype;
	}

	public String getCreattime() {
		return creattime;
	}

	public void setCreattime(String creattime) {
		this.creattime = creattime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSelltimeend() {
		return selltimeend;
	}

	public void setSelltimeend(String selltimeend) {
		this.selltimeend = selltimeend;
	}

	public String getSelltimestart() {
		return selltimestart;
	}

	public void setSelltimestart(String selltimestart) {
		this.selltimestart = selltimestart;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	/**
	 * 活动状态 0未开始，1进行中，2一结束
	 */
	private String cStatus;
	/**
	 * 活动类型  0 比赛  1 音乐会
	 */
	private String camptype;
	/**
	 * 创建时间
	 */
	private String creattime;
	/**
	 * 开始时间
	 */
	private String starttime;
	/**
	 * 结束时间
	 */
	private String endtime;
	/**
	 * 活动图片id
	 */
	private String fileId;
	/**
	 * 该活动的id
	 */
	private String id;
	/**
	 * 订阅时间
	 */
	private String selltimeend;
	/**
	 * 开始订阅时间
	 */
	private String selltimestart;
	/**
	 * 
	 */
	private String tips;
	/**
	 * 
	 */
	private String venueId;
	/**
	 * 
	 */
	private String summary;
	/**
	 * 
	 * @return
	 */
	private String venueName;
	/**
	 * 字段0：表示没有尖叫过1：表示已经尖叫过
	 */
	private String hasScreamed;
	/**
	 * 球赛 0未支持；1已支持
	 */
	private String hasSupported;
	
	
	public String getHasSupported() {
		return hasSupported;
	}

	public void setHasSupported(String hasSupported) {
		this.hasSupported = hasSupported;
	}

	public String getHasScreamed() {
		return hasScreamed;
	}

	public void setHasScreamed(String hasScreamed) {
		this.hasScreamed = hasScreamed;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * 
	 */
	private String label;
	
	/**
	 * 活动图片的url
	 */
	private String fileUrl;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * 未开始界面中的背景图
	 */
	private String backgroudImageURL;
	public String getBackgroudImageURL() {
		return backgroudImageURL;
	}

	public void setBackgroudImageURL(String backgroudImageURL) {
		this.backgroudImageURL = backgroudImageURL;
	}
	/**
	 * 列表项图片url
	 */
	private String frontCoverImageURL;
	public String getFrontCoverImageURL() {
		return frontCoverImageURL;
	}

	public void setFrontCoverImageURL(String frontCoverImageURL) {
		this.frontCoverImageURL = frontCoverImageURL;
	}
	/**
	 * 主题
	 */
	private String subhead;
	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	
	/**
	 * 直播源resourceId
	 */
	private String resourceId;
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	

}
/**
 *  "data": [
        {
               "cHeat": 11,
            "cStatus": 1,
            "camptype": 1,
            "creattime": 1450078020000,
            "endtime": 1450886400000,
            "fileId": 9,
            "id": 9,
            "label": "9",
            "selltimeend": 1447603200000,
            "selltimestart": 1447430400000,
            "starttime": 1450022400000,
            "summary": "111",
            "tips": "55",
            "title": "9",
            "venueId": 1

        },
        
        
        
        
    "backgroudImageURL":"/upload/image/activity/U1817P28T52D1752F872DT20080222140857.jpg",
	"cHeat":0,
	"cStatus":0,
	"camptype":1,
	"creattime":1457156067000,
	"endtime":1459267200000,
	"fileId":40,
	"fileUrl":"",
	"frontCoverImageURL":"/upload/image/activity/thumb_1076_500_1447725191573.jpg",
	"id":31,
	"label":"演唱会",
	"selltimeend":1459008000000,
	"selltimestart":1458144000000,
	"starttime":1459267200000,
	"subhead":"演唱会",
	"summary":"刘若英演唱会",
	"tips":"1",
	"title":"刘若英",
	"venueId":2,
	"venueName":"鸟巢"

 */
