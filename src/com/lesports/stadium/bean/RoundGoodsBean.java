/**
 * 
 */
package com.lesports.stadium.bean;

import java.io.Serializable;

import com.lesports.stadium.dao.Column;
import com.lesports.stadium.dao.ID;
import com.lesports.stadium.dao.TableName;
/**
 * ***************************************************************
 * 
 * @Desc : 商品实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
@TableName("buyCarBean")
public class RoundGoodsBean implements Serializable{

	@Column("buy_space_image")
	private String space_image;
	public String getSpace_image() {
		return space_image;
	}
	public void setSpace_image(String space_image) {
		this.space_image = space_image;
	}
	/**
	 * 商品规格说明
	 */
	@Column("buy_space_shuoming")
	private String mGoodsSpecifications;
	public String getmGoodsSpecifications() {
		return mGoodsSpecifications;
	}
	public void setmGoodsSpecifications(String mGoodsSpecifications) {
		this.mGoodsSpecifications = mGoodsSpecifications;
	}
	/**
	 * 用户不同，购物车不同，这里添加的标记是为了记录到底是哪个用户添加进来的商品
	 */
	@Column("buy_mUsename")
	private String goodsusenaem;
	
	@Column("buy_mSpaceid")
	private String spaceid="111";
	public String getSpaceid() {
		return spaceid;
	}
	public void setSpaceid(String spaceid) {
		this.spaceid = spaceid;
	}
	public String getGoodsusenaem() {
		return goodsusenaem;
	}
	public void setGoodsusenaem(String goodsusenaem) {
		this.goodsusenaem = goodsusenaem;
	}
	/**
	 * 商品广告图片
	 */
	@Column("buy_bannerImage")
	private String bannerImage;
	public String getBannerImage() {
		return bannerImage;
	}
	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}
	/**
	 * 商品数量
	 */
	@Column("buy_mNum")
	private String mNum="1";
	public String getmNum() {
		return mNum;
	}
	public void setmNum(String mNum) {
		this.mNum = mNum;
	}
	/**
	 * 图片地址
	 */
	@Column("buy_ImageUrl")
	private String ImageUrl;
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getGoodsPrice() {
		return GoodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		GoodsPrice = goodsPrice;
	}
	public String getIsAdd() {
		return IsAdd;
	}
	public void setIsAdd(String isAdd) {
		IsAdd = isAdd;
	}
//	@Column("buy_gId")
//	private String gId;
//	public String getgId() {
//		return gId;
//	}
//	public void setgId(String gId) {
//		this.gId = gId;
//	}
	/**
	 * 商品自取地址
	 */
	@Column("buy_pickup_address")
	private String pickup_address;
	public String getPickup_address() {
		return pickup_address;
	}
	public void setPickup_address(String pickup_address) {
		this.pickup_address = pickup_address;
	}
	/**
	 * 商品价格
	 */
	@Column("buy_GoodsPrice")
	private String GoodsPrice;
	/**
	 * 是否已加入购物车,0代表未加入，1代表已加入
	 */
	@Column("buy_IsAdd")
	private String IsAdd;
	
	/**
	 *  "createTime": 1452069474000,
            "status": 1,
            "referPrice": 112,
            "classicId": 1,
            "label": "刘若英",
            "bigimg": "D:\\workspace\\src\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\letv_mgr\\upload/16cdd50ecbb2ab65645f6eac17144398.jpg",
            "smallImg": "D:\\workspace\\src\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\letv_mgr\\upload/16cdd50ecbb2ab65645f6eac17144398.jpg",
            "priceunit": 1,
            "classicName": "文具",
            "mediumImg": "D:\\workspace\\src\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\letv_mgr\\upload/16cdd50ecbb2ab65645f6eac17144398.jpg",
            "seller": 1,
            "id": 43,
            "parentId": 48,
            "price": 50,
            "stock": 11,
            "sales": 10000,
            "freight": 10,
            "cId": 1,
            "goodsName": "篮球"
            
            
            {
    "bigImg": "/upload/img/16d6f7747fc57966d98484a933c4fcf8.jpg",  ********
    "category": {
        "classicName": "文具",
        "parentId": 48
    },
    "classicId": 1,
    "createTime": 1451034468000,
    "createby": 1,
    "freight": 0,
    "gId": 34,
    "goodsName": "111",
    "label": "刘若英",
    "mediumImg": "/upload/img/16d6f7747fc57966d98484a933c4fcf8.jpg",
    "price": 111,
    "priceUnit": 1,
    "referprice": 1111,
    "sales": 10000,
    "seller": 1,
    "smallImg": "/upload/img/16d6f7747fc57966d98484a933c4fcf8.jpg",
    "status": 1,
    "stock": 1111
    
     * "address":"A区3002",
	"companyname":"普通商户测试账户（勿删）",
	"contact":"ceshi1111",
	"createTime":1461136896000,
	"id":94383,
	"imageUrl":"/upload/image/users/b07a8b03f0464d9d95d29dd2288eecc2.jpg",
	"loginname":"ceshi1111",
	"mobilephone":"13819231234",
	"password":"ceshi1111",
	"role":2,
	"telephone":"13819231234",
	"usertype":0,
	"zipcode":"313223"
}
            
            
            
         {
	"bigImg":"/upload/img/image009.png",
	"category":{
	"classicName":"家居美食"
	},
	"classicId":4959,
	"createTime":1457154717000,
	"createby":1570,
	"description":"走过路过不要错过",
	"freight":5,
	"gId":94100,
	"goodsName":"烤翅",
	"label":"篮球赛",
	"mediumImg":"/upload/img/image011.png",
	"price":10,
	"priceUnit":1,
	"referprice":10,
	"seller":1570,
	"sellerName":"棒约翰",
	"smallImg":"/upload/img/image013.png",
	"status":1,
	"stock":100
	
	
	 * "address":"A区3002",
	"companyname":"普通商户测试账户（勿删）",
	"contact":"ceshi1111",
	"createTime":1461136896000,
	"id":94383,
	"imageUrl":"/upload/image/users/b07a8b03f0464d9d95d29dd2288eecc2.jpg",
	"loginname":"ceshi1111",
	"mobilephone":"13819231234",
	"password":"ceshi1111",
	"role":2,
	"telephone":"13819231234",
	"usertype":0,
	"zipcode":"313223"
	
	
	"price":1,
	"priceUnit":1,
	"referprice":1,
	"seller":1604,
	"sellerAddress":"西区A2030",
	"sellerName":"必胜客",
	"smallImg":"/upload/image/goods/1f882cc8b6954932a1350cf2947c07b8.png",
	"status":1,
	"stock":1,
	"type":1
}   


	 */
	@Column("buy_sellerAddress")
	private String sellerAddress;
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	@Column("buy_sellerName")
	private String sellerName;
	
	@Column("buy_address")
	private String address;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@Column("buy_companyname")
	private String companyname;
	@Column("buy_mobilephone")
	private String mobilephone;
	@Column("buy_telephone")
	private String telephone;
	/**
	 * 创建时间
	 */
	@Column("buy_createTime")
	private String createTime;
	/**
	 * 商品描述
	 * @return
	 */
	@Column("buy_description")
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReferPrice() {
		return referPrice;
	}
	public void setReferPrice(String referPrice) {
		this.referPrice = referPrice;
	}
	public String getClassicId() {
		return classicId;
	}
	public void setClassicId(String classicId) {
		this.classicId = classicId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getBigimg() {
		return bigimg;
	}
	public void setBigimg(String bigimg) {
		this.bigimg = bigimg;
	}
	public String getSmallImg() {
		return smallImg;
	}
	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}
	public String getPriceunit() {
		return priceunit;
	}
	public void setPriceunit(String priceunit) {
		this.priceunit = priceunit;
	}
	public String getClassicName() {
		return classicName;
	}
	public void setClassicName(String classicName) {
		this.classicName = classicName;
	}
	public String getMediumImg() {
		return mediumImg;
	}
	public void setMediumImg(String mediumImg) {
		this.mediumImg = mediumImg;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	/**
	 * status 状态，是否已售完或者已下架
	 */
	@Column("buy_status")
	private String status;
	/**
	 * referPrice 原价
	 */
	@Column("buy_referPrice")
	private String referPrice;
	/**
	 * 所属类别id
	 */
	@Column("buy_classicId")
	private String classicId;
	/**
	 * 商品标签
	 */
	@Column("buy_label")
	private String label;
	/**
	 * 大图地址
	 */
	@Column("buy_bigimg")
	private String bigimg;
	/**
	 * 缩略图地址
	 */
	@Column("buy_smallImg")
	private String smallImg;
	/**
	 * 配送费
	 */
	@Column("buy_priceunit")
	private String priceunit;
	/**
	 * 类别名称
	 */
	@Column("buy_classicName")
	private String classicName;
	/**
	 * mediumImg
	 */
	@Column("buy_mediumImgs")
	private String mediumImg;
	/**
	 * 卖家
	 */
	@Column("buy_mediumImg")
	private String seller="1";
	/**
	 * 商品id
	 */
	@Column("buy_id")
	@ID(autoincrement = false, value = "buy_id")
	private String id;
	/**
	 * 商品所属父类id
	 */
	@Column("buy_parentId")
	private String parentId;
	/**
	 * 商品价格
	 */
	@Column("buy_price")
	private String price;
	/**
	 *  库存
	 */
	@Column("buy_stock")
	private String stock;
	/**
	 * 已经卖出
	 */
	@Column("buy_sales")
	private String sales;
	/**
	 * 运送费
	 */
	@Column("buy_freight")
	private String freight;
	/**
	 * 商品名称
	 */
	@Column("buy_goodsName")
	private String goodsName="";
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * 一个不知道做什么的id
	 */
	@Column("buy_cId")
	private String cId; 
	
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		RoundGoodsBean bean=(RoundGoodsBean) o;
		return super.equals(o);
	}
	@Column("buy_delivery_type")
	private String delivery_type="0";
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	@Column("buy_pickup_remark")
	private String pickup_remark;
	public String getPickup_remark() {
		return pickup_remark;
	}
	public void setPickup_remark(String pickup_remark) {
		this.pickup_remark = pickup_remark;
	}
	
	
	
}
