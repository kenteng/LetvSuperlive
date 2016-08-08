package com.lesports.stadium.bean;

import com.letv.component.core.http.bean.LetvBaseBean;

/**
 * 用户实体类
 * @author fjh
 */
public class LoginUserInfo implements LetvBaseBean{

	private static final long serialVersionUID = 5617255728396890266L;
	public int status;
	/**用户id*/
	public String uid;
	/**用户名*/
	public String username;
	/**昵称*/
	public String nickname;
	/**UC返回的token*/
	public String sso_tk;
	public String message;
	/**登陆类型*/
	public String act;
	/**新浪token*/
	public String  sinaToken;
	/**新浪uid*/
	public String  sinaUid;
	/**头像(乐视网)*/
	public String[] picArray;
	/**头像(第三方登陆账户头像，如果是乐视网的默认是picArray中的第一张图片)*/
	public String avatar;
	/**性别*/
	public String gender;
	/**错误码*/
	public int errorCode;
	/**qq*/
	public String qq;
	/**email*/
	public String email;
	/**生日*/
	public String birthday;
	/**上次登录IP*/
	public String registIp;
	/**上次登录时间*/
	public String registTime;
	/**mobile*/
	public String mobile;
	/**省份*/
	public String province;
	/**城市*/
	public String city;
	/**地址*/
	public String address;
	/**tv_token*/
	public String tv_token;
//	public String getQq() {
//		return qq;
//	}
//	public void setQq(String qq) {
//		this.qq = qq;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getBirthday() {
//		return birthday;
//	}
//	public void setBirthday(String birthday) {
//		this.birthday = birthday;
//	}
//	public String getRegistIp() {
//		return registIp;
//	}
//	public void setRegistIp(String registIp) {
//		this.registIp = registIp;
//	}
//	public String getRegistTime() {
//		return registTime;
//	}
//	public void setRegistTime(String registTime) {
//		this.registTime = registTime;
//	}
//	public String getMobile() {
//		return mobile;
//	}
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//	public String getProvince() {
//		return province;
//	}
//	public void setProvince(String province) {
//		this.province = province;
//	}
//	public String getCity() {
//		return city;
//	}
//	public void setCity(String city) {
//		this.city = city;
//	}
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
//	public String getTv_token() {
//		return tv_token;
//	}
//	public void setTv_token(String tv_token) {
//		this.tv_token = tv_token;
//	}
//	public int getStatus() {
//		return status;
//	}
//	public void setStatus(int status) {
//		this.status = status;
//	}
//	public String getUid() {
//		return uid;
//	}
//	public void setUid(String uid) {
//		this.uid = uid;
//	}
//	public String getUsername() {
//		return username;
//	}
//	public void setUsername(String username) {
//		this.username = username;
//	}
//	public String getNickname() {
//		return nickname;
//	}
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
//	public String getSso_tk() {
//		return sso_tk;
//	}
//	public void setSso_tk(String sso_tk) {
//		this.sso_tk = sso_tk;
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	public String getAct() {
//		return act;
//	}
//	public void setAct(String act) {
//		this.act = act;
//	}
//	public String getSinaToken() {
//		return sinaToken;
//	}
//	public void setSinaToken(String sinaToken) {
//		this.sinaToken = sinaToken;
//	}
//	public String getSinaUid() {
//		return sinaUid;
//	}
//	public void setSinaUid(String sinaUid) {
//		this.sinaUid = sinaUid;
//	}
//	public String[] getPicArray() {
//		return picArray;
//	}
//	public void setPicArray(String[] picArray) {
//		this.picArray = picArray;
//	}
//	public String getAvatar() {
//		return avatar;
//	}
//	public void setAvatar(String avatar) {
//		this.avatar = avatar;
//	}
//	public String getGender() {
//		return gender;
//	}
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//	public int getErrorCode() {
//		return errorCode;
//	}
//	public void setErrorCode(int errorCode) {
//		this.errorCode = errorCode;
//	}
	public String toString() {
		return "status="+status+"&uid="+uid+"&uname="+username+"&act="+act+"&nickname="+nickname+"&gender="+gender+"&ssotoken="+sso_tk;
	}
}
