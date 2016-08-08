package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: TemMessage
 * 
 * @Description: 封装球队信息
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-15 上午11:29:22
 * 
 * 
 */
public class TemMessage implements Serializable {
	/**
	 * 球队id
	 */
	public String teamId;
	/**
	 * 球队名称
	 */
	public String teamName;
	/**
	 * 球队logo
	 */
	public String logoUrl;
	/**
	 * 当前球队支持人数
	 */
	public String supportCount;
	/**
	 * 比赛id
	 */
	public String id;
	/**
	 * 该队 颜色
	 */
	public String temColor;
	/**
	 * 盾牌
	 */
	public String backgrounding;
	/**
	 * 支持该队的图标
	 */
	public String supportImg;

	/**
	 * 该球队之前是否支持过 0是没有支持，1是支持过了
	 */
	public String hasSupported;

	/**
	 * 当前球队尖叫值是多少
	 */
	public String screamDB;

}
