package com.lesports.stadium.bean;

/**
 * 
 * @ClassName: GiftBean
 * 
 * @Description: 封装礼物的对象
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-15 下午1:12:35
 * 
 * 
 */
public class GiftBean {
	/**
	 * 礼物名称
	 */
	private String giftName;
	/**
	 * 点击时图片
	 */
	private String image_selected;
	/**
	 * 未点击时图片
	 */
	private String image_uselected;
	/**
	 * 飘落的图片
	 */
	private String snow;
	/**
	 * 积分
	 */
	private String intergal;

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getImage_selected() {
		return image_selected;
	}

	public void setImage_selected(String image_selected) {
		this.image_selected = image_selected;
	}

	public String getImage_uselected() {
		return image_uselected;
	}

	public void setImage_uselected(String image_uselected) {
		this.image_uselected = image_uselected;
	}

	public String getSnow() {
		return snow;
	}

	public void setSnow(String snow) {
		this.snow = snow;
	}

	public String getIntergal() {
		return intergal;
	}

	public void setIntergal(String intergal) {
		this.intergal = intergal;
	}

}
