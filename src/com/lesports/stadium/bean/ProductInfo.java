package com.lesports.stadium.bean;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 购物车内的商品信息
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
public class ProductInfo extends BaseInfo
{
	private String mGoodsSpaceShuoming;
	
	public String getmGoodsSpaceShuoming() {
		return mGoodsSpaceShuoming;
	}

	public void setmGoodsSpaceShuoming(String mGoodsSpaceShuoming) {
		this.mGoodsSpaceShuoming = mGoodsSpaceShuoming;
	}

	private String space_id;
	public String getSpace_id() {
		return space_id;
	}

	public void setSpace_id(String space_id) {
		this.space_id = space_id;
	}

	/**
	 * 自取商家地址
	 */
	private String selerAddress;
	public String getSelerAddress() {
		return selerAddress;
	}

	public void setSelerAddress(String selerAddress) {
		this.selerAddress = selerAddress;
	}

	public String getFrieight() {
		return frieight;
	}

	public void setFrieight(String frieight) {
		this.frieight = frieight;
	}

	/**
	 * 配送费
	 */
	private String frieight;
	/**
	 * 商品的图片UIL
	 */
	private String imageUrl;
	/**
	 * 商品描述，也就是商品名称
	 */
	private String desc;
	/**
	 * 商品价格
	 */
	private double price;
	/**
	 * 商品数量
	 */
	private int count;
	/**
	 * 商品的绝对位置，// 绝对位置，只在ListView构造的购物车中，在删除时有效
	 */
	private int position;
	/**
	 * 商品运费
	 */
	private double Yunfei;

	public double getYunfei() {
		return Yunfei;
	}

	public void setYunfei(double yunfei) {
		Yunfei = yunfei;
	}

	public ProductInfo()
	{
		super();
	}

	public ProductInfo(String id, String name, String imageUrl, String desc, double price, int count)
	{

		super.Id = id;
		super.name = name;
		this.imageUrl = imageUrl;
		this.desc = desc;
		this.price = price;
		this.count = count;
	
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}
	
	/**
	 * 商品所属商家
	 */
	private String seller;

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}
	/**
	 * 商品所支持的配送方式
	 */
	private String sendWay="0";

	public String getSendWay() {
		return sendWay;
	}

	public void setSendWay(String sendWay) {
		this.sendWay = sendWay;
	}
	/**
	 * 自取说明
	 */
	private String pickup_remark;

	public String getPickup_remark() {
		return pickup_remark;
	}

	public void setPickup_remark(String pickup_remark) {
		this.pickup_remark = pickup_remark;
	}
	
	

}
