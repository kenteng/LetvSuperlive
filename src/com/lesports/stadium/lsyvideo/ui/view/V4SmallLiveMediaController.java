package com.lesports.stadium.lsyvideo.ui.view;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.VideoPlayerActivity;
import com.lesports.stadium.lsyvideo.ui.base.BaseChgScreenBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseLiveSeekBar;
import com.lesports.stadium.lsyvideo.ui.base.BasePlayBtn;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;

public class V4SmallLiveMediaController extends V4LargeLiveMediaController {
	
	
    public V4SmallLiveMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4SmallLiveMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4SmallLiveMediaController(Context context) {
        super(context);
    }
	
	@Override
	protected void onInitView() {
		mBasePlayBtn = (BasePlayBtn) findViewById(R.id.vnew_play_btn);
		mBaseChgScreenBtn = (BaseChgScreenBtn) findViewById(R.id.vnew_chg_btn);
		mBaseLiveSeekBar = (BaseLiveSeekBar) findViewById(R.id.vnew_seekbar);
		wiperSwitch = (WiperSwitch) findViewById(R.id.wi_swi);
		if(wiperSwitch!=null)
			wiperSwitch.setOnChangedListener(new OnChangedListener() {
						
						@Override
						public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
							//发送广播 通知 弹幕开启关闭
							Intent intent = new Intent(VideoPlayerActivity.DanmAction);
							if(checkState){
								intent.putExtra("status", "1");
							}else{
								intent.putExtra("status", "0");
							}
							context.sendBroadcast(intent);
							Log.i("wxn", "发送了。。。");
						}
					});
	}
	
	@Override
	public void setRateTypeItems(List<String> ratetypes,String definition) {
		
	}
}
