/**
 * 
 */
package com.lesports.stadium.bean;
/**
 * ***************************************************************
 * 
 * @Desc : 确认订单界面的收货地址或者是自提地址的实体类
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

public class OrderAddressBean {

	/**
	 * 商品id
	 */
	private String Id="1";
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	/**
	 * 商品名称
	 */
	private String name;
	/**
	 * 商品是否被选中
	 */
	private boolean isChoosed=false;
	
}
