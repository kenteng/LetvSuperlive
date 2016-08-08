package com.lesports.stadium.bean;
/**
 * ***************************************************************
 * 
 * @Desc : 收支记录里面的子实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwk
 * 
 * @data: 2016-6-17下午2:06:03
 * 
 * @Version : 版本号
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class RecordBean {
	private String cid;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	private String time;
	private String num;

}
