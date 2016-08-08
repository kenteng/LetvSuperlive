package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 购物车内商品实体类的基类
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
public class BaseInfo implements Serializable
{
	/**
	 * 商品id
	 */
	protected String Id;
	/**
	 * 商品名称
	 */
	protected String name;
	/**
	 * 商品是否被选中
	 */
	protected boolean isChoosed;

	public BaseInfo()
	{
		super();
	}

	public BaseInfo(String id, String name)
	{
		super();
		Id = id;
		this.name = name;

	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isChoosed()
	{
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed)
	{
		this.isChoosed = isChoosed;
	}

}
