package com.lesports.stadium.bean;

import java.io.Serializable;

import com.lesports.stadium.utils.PinyinUtil;

/**
 * 通讯录手机联系人实体类
 * 
 * @author 盛之刚
 * 
 */
public class PhoneInfo implements Serializable,Comparable<PhoneInfo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phoneName;
	private String phone;
	private String pinyin;

	public PhoneInfo(String phoneName, String phone) {
		super();
		try {
			this.phoneName = phoneName;
			this.phone = phone;
			this.setPinyin(PinyinUtil.getPinyin(phoneName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PhoneInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof PhoneInfo)){
			return false;
		}
		PhoneInfo phoneInfo = (PhoneInfo) o;
		return phone.equals(phoneInfo.phone);
	}

	@Override
	public String toString() {
		return "PhoneInfo [phoneName=" + phoneName + ",pinyin=" + pinyin +", phone=" + phone + "]";
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return phoneName.hashCode();
	}
	
	@Override
	public int compareTo(PhoneInfo another) {
		return pinyin.compareTo(another.pinyin);
	}

}
