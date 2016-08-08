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
public class GoodsSpaceChildBean {

	/**
	 * {
	"goodsSpecsName":"黑色",
	"id":16,
	"parentId":15,
	"parentName":"颜色分类",
	"specified":0
	},
	 */
	private String goodsSpecsName;
	public String getGoodsSpecsName() {
		return goodsSpecsName;
	}
	public void setGoodsSpecsName(String goodsSpecsName) {
		this.goodsSpecsName = goodsSpecsName;
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getSpecified() {
		return specified;
	}
	public void setSpecified(String specified) {
		this.specified = specified;
	}
	private String id;
	private String parentId;
	private String parentName;
	private String specified;
}
