package com.lesports.stadium.lsyvideo.videoview.pano.live;


import android.content.Context;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnTouchListener;

import com.lecloud.sdk.surfaceview.ISurfaceView;
import com.lesports.stadium.lsyvideo.ui.base.BaseChangeModeBtn;
import com.lesports.stadium.lsyvideo.ui.impl.LetvLiveUICon;
import com.lesports.stadium.lsyvideo.videoview.live.UIActionLiveVideoView;
import com.lesports.stadium.lsyvideo.videoview.pano.base.BasePanoSurfaceView;
import com.letv.pano.ISurfaceListener;
import com.letv.pano.OnPanoViewTapUpListener;

/**
 * Created by heyuekuai on 16/5/31.
 */
public class UIPanoActionLiveVideoView extends UIActionLiveVideoView {
    ISurfaceView surfaceView;
    BasePanoSurfaceView.PanoControllMode panoControllMode;
    private int panoVideoMode;
    private int lastPanoVideoMode = -1;
    public UIPanoActionLiveVideoView(Context context) {
        super(context);
    }

    @Override
    protected void prepareVideoSurface() {
        surfaceView = new BasePanoSurfaceView(context);
        if (panoControllMode != null) {
            ((BasePanoSurfaceView) surfaceView).switchControllMode(panoControllMode);
        }
        panoControllMode = ((BasePanoSurfaceView) surfaceView).getPanoMode();
        setVideoView(surfaceView);
        ((BasePanoSurfaceView) surfaceView).registerSurfacelistener(new ISurfaceListener() {
            @Override
            public void setSurface(Surface surface) {
                player.setDisplay(surface);
            }
        });
        ((BasePanoSurfaceView) surfaceView).setTapUpListener(new OnPanoViewTapUpListener() {
            @Override
            public void onSingleTapUp(MotionEvent e) {
            	letvLiveUICon.performClick();
            }
        });

        ((LetvLiveUICon) letvLiveUICon).setOnTouchListener(new OnTouchListener() {
        	@Override
        	public boolean onTouch(View v, MotionEvent event) {
            	if(player !=null && player.isPlaying()){
            		((BasePanoSurfaceView) surfaceView).onPanoTouch(v, event);
            		return true;
            	}else{
            		return false;
            	}
        	}
        });
        letvLiveUICon.isPano(true);
    }

    protected void switchPanoVideoMode(int mode) {
    	panoVideoMode = mode;
        if (mode == BaseChangeModeBtn.MODE_MOVE) {
            panoControllMode = BasePanoSurfaceView.PanoControllMode.GESTURE_AND_GYRO;
        } else {
            panoControllMode = BasePanoSurfaceView.PanoControllMode.GESTURE;
        }
        ((BasePanoSurfaceView) surfaceView).switchControllMode(panoControllMode);
    }
    
    protected void enablePanoGesture(boolean enable){
    	if(enable){
    		if(lastPanoVideoMode != -1){
    			switchPanoVideoMode(lastPanoVideoMode);
    		}
    	}else{
    		if(panoVideoMode == BaseChangeModeBtn.MODE_MOVE){
	    		lastPanoVideoMode = panoVideoMode;
	    		switchPanoVideoMode(BaseChangeModeBtn.MODE_TOUCH);
    		}
    	}
    }
}
