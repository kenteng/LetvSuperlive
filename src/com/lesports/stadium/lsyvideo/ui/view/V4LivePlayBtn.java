package com.lesports.stadium.lsyvideo.ui.view;


import com.lesports.stadium.R;
import com.lesports.stadium.lsyvideo.ui.base.BasePlayBtn;

import android.content.Context;
import android.util.AttributeSet;



/**
 * 播放按钮
 */
public  class V4LivePlayBtn extends BasePlayBtn {
	
	public V4LivePlayBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4LivePlayBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	public V4LivePlayBtn(Context context) {
		super(context);
	}

	@Override
	protected int getPauseResId() {
		return R.drawable.letv_skin_v4_btn_play;
	}

	@Override
	protected int getPlayResId() {
		return R.drawable.letv_skin_v4_btn_pause;
	}

	
   
}
