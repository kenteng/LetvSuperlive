package com.lesports.stadium.lsyvideo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lesports.stadium.lsyvideo.ui.LetvUIListener;
import com.lesports.stadium.lsyvideo.ui.utils.ReUtils;



public class V4RightMiddleView extends RelativeLayout {

	/**
	 * lockFlag 为false 表示不加锁
	 */
	private boolean lockFlag = false;
	/**
	 * 锁屏控件
	 */
	private ImageView videoLock;
	/**
	 * 陀螺仪控件
	 */
	private V4ChangeModeBtn changeMode;
	/**
	 * 上下文
	 */
	private Context context;
	
	private LetvUIListener mLetvUIListener;

	public V4RightMiddleView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public V4RightMiddleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public V4RightMiddleView(Context context) {
		super(context);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		context = getContext();
		initView(context);
		initListener();
	}

	/**
	 * 判断是否枷锁
	 * 
	 * @return
	 */
	public boolean isLockFlag() {
		return lockFlag;
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	protected void initView(final Context context) {
		videoLock = (ImageView) findViewById(ReUtils.getId(context,
				"lock_scrren"));
		changeMode = (V4ChangeModeBtn) findViewById(ReUtils.getId(context,
				"vnew_change_mode"));
	}

	/**
	 * 设置监听
	 */
	private void initListener() {
		videoLock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				lockFlag = !lockFlag;

				if (lockFlag) {
					videoLock.setImageResource(ReUtils.getDrawableId(context,
							"letv_skin_v4_large_mult_live_action_lock"));
				} else {
					videoLock.setImageResource(ReUtils.getDrawableId(context,
							"letv_skin_v4_large_mult_live_action_unlock"));
				}
			}
		});

	}
	
	
	public void setLetvUIListener(LetvUIListener mLetvUIListener) {
		this.mLetvUIListener = mLetvUIListener;
		if (mLetvUIListener != null) {
			if (changeMode != null) {
				changeMode.setLetvUIListener(mLetvUIListener);
			}
		}
	}
	
    public void isPano(boolean pano){
        if (pano) {
        	changeMode.setVisibility(VISIBLE);
        }else {
        	changeMode.setVisibility(GONE);
        }
    }

}
