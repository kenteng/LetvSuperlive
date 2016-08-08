package com.lesports.stadium.lsyvideo.ui.view;


import com.lesports.stadium.R;
import com.lesports.stadium.activity.VideoPlayerActivity;
import com.lesports.stadium.lsyvideo.ui.base.BaseChangeModeBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseChgScreenBtn;
import com.lesports.stadium.lsyvideo.ui.base.BasePlayBtn;
import com.lesports.stadium.lsyvideo.ui.base.BasePlayerSeekBar;
import com.lesports.stadium.lsyvideo.ui.base.BaseRateTypeBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseVodMediaController;
import com.lesports.stadium.lsyvideo.ui.base.TextTimerView;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;



public class V4LargeMediaController extends BaseVodMediaController {
	
	
    public V4LargeMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4LargeMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4LargeMediaController(Context context) {
        super(context);
    }
    
	
	@Override
	protected void onInitView() {
		mBasePlayBtn = (BasePlayBtn) findViewById(R.id.vnew_play_btn);
		mBaseChgScreenBtn = (BaseChgScreenBtn) findViewById(R.id.vnew_chg_btn);
		mBaseRateTypeBtn = (BaseRateTypeBtn) findViewById(R.id.vnew_rate_btn);
		mBasePlayerSeekBar = (BasePlayerSeekBar) findViewById(R.id.vnew_seekbar);
		mBaseChangeModeBtn = (BaseChangeModeBtn) findViewById(R.id.vnew_change_mode);
		mTextTimerView = (TextTimerView) findViewById(R.id.vnew_text_duration_ref);
		wiperSwitch = (WiperSwitch) findViewById(R.id.wi_swi);
		wiperSwitch.setChecked(GlobalParams.Switch);
		wiperSwitch.setOnChangedListener(new OnChangedListener() {
					
					@Override
					public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
						//发送广播 通知 弹幕开启关闭
						Log.i("wxn", "发送了。。。");
						Intent intent = new Intent();
						intent.setAction(VideoPlayerActivity.DanmAction);
						if(checkState){
							intent.putExtra("status", "1");
						}else{
							intent.putExtra("status", "0");
						}
						context.sendBroadcast(intent);
					}
		});
		
		mBaseChgScreenBtn.showZoomOutState();
	}

}
