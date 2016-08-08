package com.lesports.stadium.bean;

public class GoodsDetail {
	private String bigImg;
	/**
	 * 所属分类
	 */
	private int classicId;
	private String createTime;
	private int createby;
	private String description;
	/**
	 * 商品id
	 */
	private int gId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	private String mediumImg;
	/**
	 * 价格
	 */
	private float price;
	private int priceUnit;
	/**
	 * 参考价格
	 */
	private float referprice;
	private int seller;
	private String smallImg;
	private int status;
	private int stock;
	
	
	public String getBigImg() {
		return bigImg;
	}


	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
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
		private String classicName;
		private int parentId;
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
