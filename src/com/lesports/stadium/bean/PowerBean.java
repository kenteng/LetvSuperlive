package com.lesports.stadium.bean;

public class PowerBean {
private String energy;
private String nickname;
private String picture;
public PowerBean(){}

public PowerBean(String energy, String nickname, String picture) {
	super();
	this.energy = energy;
	this.nickname = nickname;
	this.picture = picture;
}

public String getEnergy() {
	return energy;
}
public void setEnergy(String energy) {
	this.energy = energy;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getPicture() {
	return picture;
}
public void setPicture(String picture) {
	this.picture = picture;
}

}
