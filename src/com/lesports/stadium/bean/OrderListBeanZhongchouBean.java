package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 我的订单界面中类型为众筹的数据源实体对象
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
public class OrderListBeanZhongchouBean  implements Serializable{

	
	
/**
 *         {
            "amount": 560,
            "companies": "顺丰",
            "courier": 1,
            "createBy": 1,
            "createTime": 1458887940000,
            "freight": 5,
            "handler": {
                
            },
            "id": 88916,
            "orderAmount": 555,
            "orderDetailList": [
                {
                    "goods": {
                        "crowdfund": {
                            "beginTime": 1458835200000,
                            "crowdfundName": "黄义达十周年演唱会",
                            "crowdfundStatus": 1,
                            "endTime": 1458835200000,
                            "evaluateCount": 0,
                            "hasMoney": 0,
                            "id": 39,
                            "participation": 0,
                            "projectAddress": "乐视体育生态中心",
                            "projectInfo": "
一个在音乐世界里从未妥协的苦行僧。他细腻，遭受过精神的极度磨砺，这些经历让他的每个音符都直击你的心底，撩拨起你心灵的共鸣——他叫黄义达！
",
                            "projectIntroduction": "一个在音乐世界里从未妥协的苦行僧。他细腻，遭受过精神的极度磨砺，这些经历让他的每个音符都直击你的心底，撩拨起你心灵的共鸣——他叫黄义达！",
                            "projectTime": 1458878608000,
                            "propagatePicture": "upload/crowdfund/20160324/b25b0133542f4fdd997eedd68fe5e423.png",
                            "remark": "无",
                            "returnCount": 0,
                            "targetMoney": 1.0E7
                        },
                        "handler": {
                            
                        },
                        "id": 46,
                        "returnContent": "1.价值277元演唱会门票一张\r\n2.I’M YIDA 十年纪念演唱会海报一张",
                        "returnLimit": 0,
                        "returnName": "支持100",
                        "returnPrice": 100,
                        "totalCount": 1
                    },
                    "goodsId": 46,
                    "id": 20160905,
                    "price": 560,
                    "wareNumber": 1
                }
            ],
            "orderNumber": "2016032500004",
            "orderType": 0,
            "ordersType": 3,
            "payMent": 0,
            "payStatus": 0,
            "position": "五排19号",
            "remark": "顺丰",
            "status": 1
        },
 */
	
	private String deliveryStatus;
	
	public String getDeliveryStatus() {
	return deliveryStatus;
}
public void setDeliveryStatus(String deliveryStatus) {
	this.deliveryStatus = deliveryStatus;
}
	private String beginTime;
	public String getBeginTime() {
	return beginTime;
}
public void setBeginTime(String beginTime) {
	this.beginTime = beginTime;
}
public String getCrowdfundName() {
	return crowdfundName;
}
public void setCrowdfundName(String crowdfundName) {
	this.crowdfundName = crowdfundName;
}
public String getCrowdfundStatus() {
	return crowdfundStatus;
}
public void setCrowdfundStatus(String crowdfundStatus) {
	this.crowdfundStatus = crowdfundStatus;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getEvaluateCount() {
	return evaluateCount;
}
public void setEvaluateCount(String evaluateCount) {
	this.evaluateCount = evaluateCount;
}
public String getHasMoney() {
	return hasMoney;
}
public void setHasMoney(String hasMoney) {
	this.hasMoney = hasMoney;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getParticipation() {
	return participation;
}
public void setParticipation(String participation) {
	this.participation = participation;
}
public String getProjectAddress() {
	return projectAddress;
}
public void setProjectAddress(String projectAddress) {
	this.projectAddress = projectAddress;
}
public String getProjectInfo() {
	return projectInfo;
}
public void setProjectInfo(String projectInfo) {
	this.projectInfo = projectInfo;
}
public String getProjectIntroduction() {
	return projectIntroduction;
}
public void setProjectIntroduction(String projectIntroduction) {
	this.projectIntroduction = projectIntroduction;
}
public String getProjectTime() {
	return projectTime;
}
public void setProjectTime(String projectTime) {
	this.projectTime = projectTime;
}
public String getPropagatePicture() {
	return propagatePicture;
}
public void setPropagatePicture(String propagatePicture) {
	this.propagatePicture = propagatePicture;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getReturnCount() {
	return returnCount;
}
public void setReturnCount(String returnCount) {
	this.returnCount = returnCount;
}
public String getTargetMoney() {
	return targetMoney;
}
public void setTargetMoney(String targetMoney) {
	this.targetMoney = targetMoney;
}
public String getReturnContent() {
	return returnContent;
}
public void setReturnContent(String returnContent) {
	this.returnContent = returnContent;
}
public String getGoodssssid() {
	return goodssssid;
}
public void setGoodssssid(String goodssssid) {
	this.goodssssid = goodssssid;
}
public String getReturnLimit() {
	return returnLimit;
}
public void setReturnLimit(String returnLimit) {
	this.returnLimit = returnLimit;
}
public String getReturnName() {
	return returnName;
}
public void setReturnName(String returnName) {
	this.returnName = returnName;
}
public String getReturnPrice() {
	return returnPrice;
}
public void setReturnPrice(String returnPrice) {
	this.returnPrice = returnPrice;
}
public String getTotalCount() {
	return totalCount;
}
public void setTotalCount(String totalCount) {
	this.totalCount = totalCount;
}
public String getGoodsId() {
	return goodsId;
}
public void setGoodsId(String goodsId) {
	this.goodsId = goodsId;
}
public String getBeanid() {
	return beanid;
}
public void setBeanid(String beanid) {
	this.beanid = beanid;
}
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
}
public String getWareNumber() {
	return wareNumber;
}
public void setWareNumber(String wareNumber) {
	this.wareNumber = wareNumber;
}
	private String crowdfundName;
	private String crowdfundStatus;
	private String endTime;
	private String evaluateCount;
	private String hasMoney;
	private String id;
	private String participation;
	private String projectAddress;
	private String projectInfo;
	private String projectIntroduction;
	private String projectTime;
	private String propagatePicture;
	private String remark;
	private String returnCount;
	private String targetMoney;
	private String returnContent;
	private String goodssssid;
	private String returnLimit;
	private String returnName;
	private String returnPrice;
	private String totalCount;
	private String goodsId;
	private String beanid;
	private String price;
	private String wareNumber;
	
}
