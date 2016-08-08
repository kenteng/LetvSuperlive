package com.lesports.stadium.listener;

import com.tencent.mm.sdk.modelpay.PayReq;

public interface WechatPayListener {
	void onPayListener(PayReq req );
}
