/**
 * 
 */
package com.lesports.stadium.bean;

import com.lesports.stadium.dao.Column;
import com.lesports.stadium.dao.TableName;

/**
 * ***************************************************************
 * 
 * @Desc : 菜单项内的报警界面
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
 * 首先得明白的一点是，在未点击加号的时候，该菜品的数量为0，也就是说，适配器张红在家再说护具的时候，首先判断伤商品数量
 * 来加载布局，当商品数量发生改变的时候，需要将数据进行重新统计
 */
@TableName("foodbuyCarBean")
public class ServiceCateringDetailBean {
	
	/**
	 * 首先明白，这里需要每一个用户在每一个商家中都有一个单独的购物车，所以这里添加商品的时候就需要添加一个用户id的标记，目的是为了做购物车区分
	 */
	/**
	 * 购物车用户标记
	 */
	@Column("foodbuy_usernametag")
	private String userNametag;
	public String getUserNametag() {
		return userNametag;
	}
	public void setUserNametag(String userNametag) {
		this.userNametag = userNametag;
	}
	public String getBigimg() {
		return bigimg;
	}
	public void setBigimg(String bigimg) {
		this.bigimg = bigimg;
	}
	/**
	 * 商品名称
	 */
	@Column("foodbuy_goodsname")
	private String goodsName;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsNames) {
		goodsName = goodsNames;
	}
	public int getGoodsNum() {
		return GoodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		GoodsNum = goodsNum;
	}
	public float getGoodsPrice() {
		return GoodsPrice;
	}
	public void setGoodsPrice(float goodsPrice) {
		GoodsPrice = goodsPrice;
	}
	/***
	 * 商品数量，初始值都为0
	 */
	@Column("foodbuy_goodsnum")
	private int GoodsNum=0;
	/**
	 * 商品价格 
	 */
	@Column("foodbuy_goodsprices")
	private float GoodsPrice;
	
	/**
	 * {
        "bigImg": "/upload/image019.png",
        "category": {
            "cId": 4956,
            "classicName": "服饰服装"
        },
        "classicId": 4956,
        "createTime": 1457155870000,
        "freight": 5,
        "gId": 94199,
        "goodsName": "鹿晗专辑",
        "mediumImg": "/upload/image019.png",
        "price": 99,
        "priceUnit": 1,
        "referprice": 99,
        "seller": 1570,
        "smallImg": "/upload/image019.png",
        "status": 1,
        "stock": 100
    },
	 */
	/**
	 * 图片url
	 */
	@Column("foodbuy_bigimgs")
	private String bigimg;
	public String getBigImg() {
		return bigimg;
	}
	public void setBigImg(String bigImg) {
		this.bigimg = bigImg;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
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
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
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
	/**
	 * 类别id
	 */
	@Column("foodbuy_cid")
	private String cId;
	/**
	 * 商品类名称
	 */
	@Column("foodbuy_classicname")
	private String classicName;
	/**
	 * 类id
	 */
	@Column("foodbuy_classicid")
	private String classicId;
	/**
	 * 创建时间
	 */
	@Column("foodbuy_createtTime")
	private String createTime;
	/**
	 * 
	 */
	@Column("foodbuy_freight")
	private String freight;
	/**
	 * 商品id
	 */
	@Column("foodbuy_gid")
	private String gId;
	/**
	 * 图片
	 */
	@Column("foodbuy_stock")
	private String stock;
	/**
	 * 图片
	 */
	@Column("foodbuy_mediumimg")
	private String mediumImg;
	@Column("foodbuy_price")
	private String price;
	@Column("foodbuy_priceunit")
	private String priceUnit;
	@Column("foodbuy_referprice")
	private String referprice;
	@Column("foodbuy_seller")
	private String seller;
	@Column("foodbuy_smallimg")
	private String smallImg;
	@Column("foodbuy_status")
	private String status;
	
	

}
