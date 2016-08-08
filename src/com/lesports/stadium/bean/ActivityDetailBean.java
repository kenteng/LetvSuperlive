package com.lesports.stadium.bean;

import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 活动详情页面界面数据源实体对象
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

public class ActivityDetailBean {
	
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
	
	private String productId;
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	private String roomId;
	private String subhead;
	private String venueName;
	private String backgroudImageURL;

	public String getBackgroudImageURL() {
		return backgroudImageURL;
	}

	public void setBackgroudImageURL(String backgroudImageURL) {
		this.backgroudImageURL = backgroudImageURL;
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
            
            
             "backgroudImageURL": "510eef860b9a4ca3b8f015ce5c39c068.png",
    "cHeat": 569,
    "endtime": 1469935888000,
    "id": 146,
    "productId": "117447830",
    "roomId": "1000821880",
    "starttime": 1469071886000,
    "subhead": "MADE [V.I.P] TOUR",
    "summary": "他们是掀起韩流热潮的超人气天团\r\n他们令全世界粉丝都为之疯狂\r\n他们用超凡的音乐实力侵入所有乐迷的心\r\n他们是权志龙、崔胜贤、东永裴、姜大声、李胜贤\r\n他们是\r\nBIGBANG\r\n2016年7月16日、7月17日\r\nBIGBANG再度空降北京\r\n给VIP们带来全新的音乐狂潮！\r\n\r\n在数万VIP粉丝的热切期盼下，BIGBANG的“2016 BIGBANG MADE 「V.I.P」TOUR北京站”，将于2016年7月16日与7月17日连续两天，在“北京乐视体育中心(原北京万事达中心)”开唱。\r\n\r\n今年开年，BIGBANG就与北京的VIP共渡浪漫跨年之夜。当晚，BIGBANG除了为粉丝带来回归新曲及众多热门金曲外，更是悉心为粉丝制造惊喜。给VIP们留下了浪漫而幸福的回忆。据悉此次的北京见面会，成员们为了感谢歌迷朋友们的支持，献唱多首热门曲目当然是必不可少，也在见面会中精心准备了遊戲互动环节及‘Today’s Big Fan’活動，与粉丝近距离接触相信会让大家感受到BIGBANG与平时不一样的魅力。怎么互动，跟谁互动这些都为此次的北京演唱会增加了神秘感，想必BIGBANG也会不负众望，精心准备之后会把更好的舞台带给广大歌迷。\r\n\r\nBIGBANG大事记\r\n2006年8月19日，借由YG Family世界巡回演唱会正式出道。\r\n2007年5月，开始全国巡回演唱会BIGBANG Live Concert Tour“Want You”。\r\n2011年，成为历史上第一个获得MTV 欧洲音乐大奖“Worldwide Act”的亚洲组合。\r\n2012年上半年，登上美国格莱美官方网页，成为首个被格莱美介绍的韩国歌手。\r\n2012年7月，首度到中国进行巡演。\r\n2013年，五位成员各自进行solo音乐活动。\r\n2015年，以团体形式回归，并在5-9月分别发行《MADE》系列数字单曲专辑。\r\n2016年6月24日，BIGBANG将携全新巡演2016 BIGBANG MADE [V.I.P] TOUR IN HARBIN，High翻哈尔滨会展中心体育场！\r\n这是场特别为“VIP”定制的专属巡回演出！这是回馈给“VIP”的特别福利！\r\nVIP们还在等什么？赶快行动起来吧！\r\n与BIGBANG一起创造难忘的回忆，让现场再次被皇冠海点亮！",
    "title": "BIGBANG",
    "venueId": 24,
    "venueName": "乐视体育生态中心"


        },

 */
