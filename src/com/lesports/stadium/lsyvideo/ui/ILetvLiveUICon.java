package com.lesports.stadium.lsyvideo.ui;

public interface ILetvLiveUICon extends ILetvUICon {
	void setLetvLiveUIListener(LetvLiveUIListener mLetvLiveUIListener);

	void setTimeShiftChange(long serverTime, long currentTime, long begin);

	void showController(boolean b);

}
