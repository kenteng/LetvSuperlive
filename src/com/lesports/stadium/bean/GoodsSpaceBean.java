package com.lesports.stadium.bean;
/**
 * ***************************************************************
 * 
 * @Desc : 商品详情规格参数实体类
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
public class GoodsSpaceBean {
	/**
	 * [
    {
        "goodsPicture": "/upload/image/goodsSpecifications/132e7147a4ec40bd81e3ca6b563dd156.png",
        "goodsSpecifications": "红色",
        "goodsStock": 50,
        "id": 1,
        "price": 10,
        "wareID": 94194
    },
    {
        "goodsPicture": "/upload/image/goodsSpecifications/c5a93ff3b6e140bebbda06aeb50b5ccb.png",
        "goodsSpecifications": "黄色",
        
        "goodsStock": 100,
        "id": 2,
        "price": 10,
        "wareID": 94194
        
        
        "goodsPicture": "/upload/image/goodsSpecs/20a4a695619645fd8d27f6c0541a6fc4.png",
      "goodsSpecifications": "黑色,大",
      "goodsSpecsCategoryIds": "16,19",
      "goodsStock": 1,
      "id": 178,
      "price": 80,
      "wareID": 94383

    }
]
	 */
	private String goodsSpecsCategoryIds;
	
	public String getGoodsSpecsCategoryIds() {
		return goodsSpecsCategoryIds;
	}
	public void setGoodsSpecsCategoryIds(String goodsSpecsCategoryIds) {
		this.goodsSpecsCategoryIds = goodsSpecsCategoryIds;
	}
	private String goodsPicture;
	public String getGoodsPicture() {
		return goodsPicture;
	}
	public void setGoodsPicture(String goodsPicture) {
		this.goodsPicture = goodsPicture;
	}
	public String getGoodsSpecifications() {
		return goodsSpecifications;
	}
	public void setGoodsSpecifications(String goodsSpecifications) {
		this.goodsSpecifications = goodsSpecifications;
	}
	public String getGoodsStock() {
		return goodsStock;
	}
	public void setGoodsStock(String goodsStock) {
		this.goodsStock = goodsStock;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getWareID() {
		return wareID;
	}
	public void setWareID(String wareID) {
		this.wareID = wareID;
	}
	private String goodsSpecifications;
	private String goodsStock;
	private String id;
	private String price;
	private String wareID;

}
