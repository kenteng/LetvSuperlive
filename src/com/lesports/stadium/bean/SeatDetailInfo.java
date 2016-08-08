package com.lesports.stadium.bean;

public  class SeatDetailInfo {
	
	public SeatDetailInfo() {
		super();
	}
	public static String floor="首层";
	public static String channel="101";
	public static String row="1排";
	public static String seat="1座";
	public static String getFloor() {
		return floor;
	}
	public static void setFloor(String floor) {
		SeatDetailInfo.floor = floor;
	}
	public static String getChannel() {
		return channel;
	}
	public static void setChannel(String channel) {
		SeatDetailInfo.channel = channel;
	}
	public static String getRow() {
		return row;
	}
	public static void setRow(String row) {
		SeatDetailInfo.row = row;
	}
	public static String getSeat() {
		return seat;
	}
	public static void setSeat(String seat) {
		SeatDetailInfo.seat = seat;
	}
	
	
}
