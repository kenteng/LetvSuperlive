package com.lesports.stadium.lsyvideo.ui.base;


import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lesports.stadium.lsyvideo.ui.LetvUIListener;
import com.lesports.stadium.lsyvideo.ui.orientation.OrientationSensorUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.WiperSwitch;

public abstract class BaseMediaController extends RelativeLayout{
	
	protected BasePlayBtn mBasePlayBtn;
	
	protected BaseChgScreenBtn mBaseChgScreenBtn;
	/**切换码率按钮*/
	protected BaseRateTypeBtn mBaseRateTypeBtn;

	protected LetvUIListener mLetvUIListener;
	/**切换全景陀螺仪检测方式按钮*/
	protected BaseChangeModeBtn mBaseChangeModeBtn;
	/**
	 * 弹幕开关
	 */
	protected WiperSwitch wiperSwitch;
	
	protected Context context;

    public BaseMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

	public BaseMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseMediaController(Context context) {
        super(context);
    }

    @Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.context = getContext();
		onInitView();
	}
    

    protected abstract void onInitView();
    
    public abstract void setLetvUIListener(LetvUIListener mLetvUIListener);
    
    public abstract void setPlayState(boolean isPlayState);
    
    public abstract void setRateTypeItems(List<String> ratetypes,String definition);
    
    public abstract void setCurrentPosition(long position);
    
    public abstract void setDuration(long duration);
	
    public abstract void setBufferPercentage(long bufferPercentage);
    
    public void isPano(boolean pano){
        if (pano) {
            mBaseChangeModeBtn.setVisibility(VISIBLE);
        }else {
            mBaseChangeModeBtn.setVisibility(GONE);
        }
    }
    
    public void setOrientationSensorUtils(OrientationSensorUtils mOrientationSensorUtils) {
        mBaseChgScreenBtn.setOrientationSensorUtils(mOrientationSensorUtils);
    }
    
    public void setSwiperStater(){
	    if(wiperSwitch!=null){
	    	wiperSwitch.setChecked(GlobalParams.Switch);
	    }
    }

}
