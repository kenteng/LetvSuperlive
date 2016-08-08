package com.lesports.stadium.bean;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 现场演唱会周边商品数据实体类
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
public class GoodsBean {
	/**
	 * 列表大图地址
	 */
	private String bigImg;
	/**
	 * 分类对象
	 */
	private Category category;
	/**
	 * 所属分类id
	 */
	private int classicId;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 创建人id
	 */
	private int createby;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 商品id
	 */
	private int gId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 中图
	 */
	private String mediumImg;
	/**
	 * 列价格
	 */
	private float price;
	/**
	 * 1：rmb 2：积分
	 */
	private int priceUnit;
	/**
	 * 参考价格
	 */
	private float referprice;
	/**
	 * 商户id
	 */
	private int seller;
	/**
	 * 小图
	 */
	private String smallImg;
	/**
	 * 是否上架
	 */
	private int status;
	/**
	 * 库存
	 */
	private int stock;
	
	public String getBigImg() {
		return bigImg;
	}

	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getClassicId() {
		return classicId;
	}

	public void setClassicId(int classicId) {
		this.classicId = classicId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getCreateby() {
		return createby;
	}

	public void setCreateby(int createby) {
		this.createby = createby;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getgId() {
		return gId;
	}

	public void setgId(int gId) {
		this.gId = gId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getMediumImg() {
		return mediumImg;
	}

	public void setMediumImg(String mediumImg) {
		this.mediumImg = mediumImg;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(int priceUnit) {
		this.priceUnit = priceUnit;
	}

	public float getReferprice() {
		return referprice;
	}

	public void setReferprice(float referprice) {
		this.referprice = referprice;
	}

	public int getSeller() {
		return seller;
	}

	public void setSeller(int seller) {
		this.seller = seller;
	}

	public String getSmallImg() {
		return smallImg;
	}

	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public class Category{
		private int cId;
		private String classicName;
		private int parentId;
		public int getcId() {
			return cId;
		}
		public void setcId(int cId) {
			this.cId = cId;
		}
		public String getClassicName() {
			return classicName;
		}
		public void setClassicName(String classicName) {
			this.classicName = classicName;
		}
		public int getParentId() {
			return parentId;
		}
		public void setParentId(int parentId) {
			this.parentId = parentId;
		}
		
	}
}
