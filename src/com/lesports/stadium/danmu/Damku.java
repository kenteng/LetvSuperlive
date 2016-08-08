package com.lesports.stadium.danmu;

public interface Damku {
	/**
	 * 开启弹幕
	 */
	void openDanmu();
	/**
	 * 关闭弹幕
	 */
	void closeDanmu();
	/**
	 * 获取弹幕状态
	 * @return
	 */
	boolean getDmStatus();
}
