package com.lesports.stadium.lsyvideo.ui.view;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;


import java.util.List;
import com.lesports.stadium.R;

import com.lesports.stadium.activity.VideoPlayerActivity;
import com.lesports.stadium.lsyvideo.ui.LetvUIListener;
import com.lesports.stadium.lsyvideo.ui.base.BaseChangeModeBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseChgScreenBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseLiveSeekBar;
import com.lesports.stadium.lsyvideo.ui.base.BaseLiveSeekBar.OnSeekChangeListener;
import com.lesports.stadium.lsyvideo.ui.base.BaseMediaController;
import com.lesports.stadium.lsyvideo.ui.base.BasePlayBtn;
import com.lesports.stadium.lsyvideo.ui.base.BaseRateTypeBtn;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;

public class V4LargeLiveMediaController extends BaseMediaController {
	protected BaseLiveSeekBar mBaseLiveSeekBar;
	protected View mBackToLive;
			
	/**上次是否显示了seekbar，用于隐藏后再次显示seekbar*/
	private boolean isLastShowSeekBar = false;
	/**上次是否显示了rateTypeBtn，用于隐藏后再次显示rateTypeBtn*/
	private boolean isLastShowRateTypeBtn = false;
	/**上次是否显示了ChangeModeBtn，用于隐藏后再次显示ChangeModeBtn*/
	private boolean isLastShowChangeModeBtn = false;
	
    public V4LargeLiveMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4LargeLiveMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4LargeLiveMediaController(Context context) {
        super(context);
    }

	@Override
	protected void onInitView() {
		mBasePlayBtn = (BasePlayBtn) findViewById(R.id.vnew_play_btn);
		mBaseChgScreenBtn = (BaseChgScreenBtn) findViewById(R.id.vnew_chg_btn);
		mBaseRateTypeBtn = (BaseRateTypeBtn) findViewById(R.id.vnew_rate_btn);
		mBaseLiveSeekBar = (BaseLiveSeekBar) findViewById(R.id.vnew_seekbar);
		mBaseChangeModeBtn = (BaseChangeModeBtn) findViewById(R.id.vnew_change_mode);
		wiperSwitch = (WiperSwitch) findViewById(R.id.wi_swi);
		mBackToLive = findViewById(R.id.vnew_back_to_live);
		mBackToLive.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mLetvUIListener.resetPlay();
            }
        });
		mBaseLiveSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                
            }

            @Override
            public void onShowBackToLive(boolean show) {
                mBackToLive.setVisibility(show?VISIBLE:INVISIBLE);
            }
        });
		if(wiperSwitch!=null)
			wiperSwitch.setOnChangedListener(new OnChangedListener() {
				
				@Override
				public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
					//发送广播 通知 弹幕开启关闭
					Log.i("wxn", "发送了。。。");
					Intent intent = new Intent(VideoPlayerActivity.DanmAction);
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
	
	@Override
	public void setLetvUIListener(LetvUIListener mLetvUIListener) {
		this.mLetvUIListener = mLetvUIListener;
		if (mLetvUIListener != null) {
			if (mBasePlayBtn != null) {
				mBasePlayBtn.setLetvUIListener(mLetvUIListener);
			}
			if (mBaseChgScreenBtn != null) {
				mBaseChgScreenBtn.setLetvUIListener(mLetvUIListener);
			}
			if (mBaseRateTypeBtn != null) {
				mBaseRateTypeBtn.setLetvUIListener(mLetvUIListener);
			}
			if (mBaseLiveSeekBar != null) {
			    mBaseLiveSeekBar.setLetvUIListener(mLetvUIListener);
			}
			if (mBaseChangeModeBtn != null) {
				mBaseChangeModeBtn.setLetvUIListener(mLetvUIListener);
			}
		}
	}
	
	@Override
	public void setRateTypeItems(List<String> ratetypes,String definition) {
		if (mBaseRateTypeBtn != null) {
			mBaseRateTypeBtn.setRateTypeItems(ratetypes, definition);
		}
	}

	@Override
	public void setPlayState(boolean isPlayState) {
		if (mBasePlayBtn != null) {
			mBasePlayBtn.setPlayState(isPlayState);
		}
	}

	@Override
	public void setCurrentPosition(long position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDuration(long duration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBufferPercentage(long bufferPercentage) {
		
		
	}
	
    public void setTimeShiftChange(long serverTime, long currentTime, long begin) {
        if (mBaseLiveSeekBar != null) {
        	if(!isLastShowSeekBar){
        		mBaseLiveSeekBar.setVisibility(VISIBLE);
        	}
            mBaseLiveSeekBar.setTimeShiftChange(serverTime, currentTime, begin);
        }
    }
    
    public BaseLiveSeekBar getSeekbar() {
        return mBaseLiveSeekBar;
    }

    
    public void showController(boolean isShow){
    	if(isShow){
            if (mBaseLiveSeekBar != null && isLastShowSeekBar) {
                mBaseLiveSeekBar.setVisibility(VISIBLE);
                mBaseLiveSeekBar.reset();
            }
            if(mBaseRateTypeBtn != null && isLastShowRateTypeBtn){
                mBaseRateTypeBtn.setVisibility(VISIBLE);
            }
            if(mBaseChangeModeBtn != null && isLastShowChangeModeBtn){
            	mBaseChangeModeBtn.setVisibility(VISIBLE);
            }
    	}else{
    		if (mBaseLiveSeekBar != null) {
    			if(mBaseLiveSeekBar.getVisibility() == View.VISIBLE){
    				isLastShowSeekBar = true;
    			}else{
    				isLastShowSeekBar = false;
    			}
                mBaseLiveSeekBar.setVisibility(GONE);
            }
            if(mBaseRateTypeBtn != null){
            	if(mBaseRateTypeBtn.getVisibility() == View.VISIBLE){
            		isLastShowRateTypeBtn = true;
    			}else{
    				isLastShowRateTypeBtn = false;
    			}
                mBaseRateTypeBtn.setVisibility(INVISIBLE);
            }
            if(mBaseChangeModeBtn != null){
            	if(mBaseChangeModeBtn.getVisibility() == View.VISIBLE){
            		isLastShowChangeModeBtn = true;
    			}else{
    				isLastShowChangeModeBtn = false;
    			}
            	mBaseChangeModeBtn.setVisibility(GONE);
            }
            if(mBackToLive != null){
            	mBackToLive.setVisibility(INVISIBLE);
            }
    	}
    }
    
}
