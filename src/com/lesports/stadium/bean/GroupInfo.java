package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 购物车内的店铺信息
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
public class GroupInfo extends BaseInfo implements Serializable
{
	public GroupInfo()
	{
		super();
	}

	public GroupInfo(String id, String name)
	{
		super(id, name);
		// TODO Auto-generated constructor stub
	}
	private String selerAddress;
	public String getSelerAddress() {
		return selerAddress;
	}

	public void setSelerAddress(String selerAddress) {
		this.selerAddress = selerAddress;
	}
	private String mFeright;
	public String getmFeright() {
		return mFeright;
	}

	public void setmFeright(String mFeright) {
		this.mFeright = mFeright;
	}

}
