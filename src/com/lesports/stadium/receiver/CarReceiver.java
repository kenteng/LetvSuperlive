package com.lesports.stadium.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * ***************************************************************
 * @ClassName:  CarReceiver 
 * 
 * @Desc :用车广播
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-4-27 下午5:02:15
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class CarReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		 if(intent.getAction().equals("com.broadcast.yidao.car")){
			    Toast.makeText(context, "收到广播", 0).show();
			}
	}
}
