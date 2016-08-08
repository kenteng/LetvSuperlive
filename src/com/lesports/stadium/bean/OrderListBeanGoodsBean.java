package com.lesports.stadium.bean;

import java.io.Serializable;

import com.ylpw.sdk.R.string;

/**
 * ***************************************************************
 * 
 * @Desc : 订单列表数据实体类条目内商品集合实体类
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
public class OrderListBeanGoodsBean implements Serializable{

	/**
	 *  "goods": {
                        "bigImg": "/upload/img/image005.png",
                        "category": {
                            "classicName": "餐饮美食"
                        },
                        "classicId": 4959,
                        "createTime": 1457155033000,
                        "createby": 1570,
                        "description": "",
                        "freight": 5,
                        "gId": 94188,
                        "goodsName": "果汁",
                        "label": "演唱会",
                        "mediumImg": "/upload/img/image005.png",
                        "price": 5,
                        "priceUnit": 1,
                        "referprice": 5,
                        "seller": 1571,
                        "smallImg": "/upload/img/image005.png",
                        "status": 1,
                        "stock": 44
                    },
                    if(objs.has("pickup_address")){
					moundgoodsbean.setPickup_address(objs.getString("pickup_address"));
				}
				if(objs.has("pickup_remark")){
					moundgoodsbean.setPickup_remark(objs.getString("pickup_remark"));
				}
                    "goodsId": 94188,
                    "id": 20160606,
                    "price": 560,
                    "wareNumber": 2

	 */
	private String pecificationName;// 规格
	
	
	public String getPecificationName() {
		return pecificationName;
	}
	public void setPecificationName(String pecificationName) {
		this.pecificationName = pecificationName;
	}
	private String pickup_address;
	public String getPickup_address() {
		return pickup_address;
	}
	public void setPickup_address(String pickup_address) {
		this.pickup_address = pickup_address;
	}
	public String getPickup_remark() {
		return pickup_remark;
	}
	public void setPickup_remark(String pickup_remark) {
		this.pickup_remark = pickup_remark;
	}
	private String pickup_remark;
	private String bigImg;
	public String getBigImg() {
		return bigImg;
	}
	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getClassicId() {
		return classicId;
	}
	public void setClassicId(String classicId) {
		this.classicId = classicId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMediumImg() {
		return mediumImg;
	}
	public void setMediumImg(String mediumImg) {
		this.mediumImg = mediumImg;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getReferprice() {
		return referprice;
	}
	public void setReferprice(String referprice) {
		this.referprice = referprice;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getSmallImg() {
		return smallImg;
	}
	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWareNumber() {
		return wareNumber;
	}
	public void setWareNumber(String wareNumber) {
		this.wareNumber = wareNumber;
	}
	private String classicName;
	private String classicId;
	private String createTime;
	private String createby;
	private String description;
	private String freight;
	private String gId;
	private String goodsName;
	private String label;
	private String mediumImg;
	private String price;
	private String priceUnit;
	private String referprice;
	private String seller;
	private String smallImg;
	private String status;
	private String stock;
	private String goodsId;
	private String id;
	private String wareNumber;
	
	
}
