package com.lesports.stadium.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lesports.stadium.R;
import com.lesports.stadium.lsyvideo.VideoLayoutParams;
import com.lesports.stadium.lsyvideo.videoview.pano.live.UIPanoActionLiveVideoView;

public class PlayActivity extends Activity {
	private Bundle mBundle;
	private IMediaDataVideoView videoView;
	VideoViewListener mVideoViewListener = new VideoViewListener() {

		@Override
		public void onStateResult(int event, Bundle bundle) {
			handleVideoInfoEvent(event, bundle);// 处理视频信息事件
			handlePlayerEvent(event, bundle);// 处理播放器事件
			handleLiveEvent(event, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		loadDataFromIntent();// load data
		initview();
	}

	private void initview() {
		videoView = new UIPanoActionLiveVideoView(this);
		videoView.setVideoViewListener(mVideoViewListener);

		RelativeLayout videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
		videoContainer.addView((View) videoView,
				VideoLayoutParams.computeContainerSize(this, 16, 9));
		videoView.setDataSource(mBundle);
	}

	/**
	 * 获取传递过来的参数
	 */
	private void loadDataFromIntent() {
        mBundle = new Bundle();
        String mActiveId = "A2016073000001mz";
        mBundle.putInt(PlayerParams.KEY_PLAY_MODE, PlayerParams.VALUE_PLAYER_ACTION_LIVE);
        mBundle.putString(PlayerParams.KEY_PLAY_ACTIONID, mActiveId);
        mBundle.putBoolean(PlayerParams.KEY_PLAY_USEHLS, true);
        mBundle.putBoolean("pano", true);
        mBundle.putBoolean("hasSkin", true);;
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        videoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 处理播放器本身事件，具体事件可以参见IPlayer类
     */
    private void handlePlayerEvent(int state, Bundle bundle) {
        switch (state) {
            case PlayerEvent.PLAY_VIDEOSIZE_CHANGED:
                /**
                 * 获取到视频的宽高的时候，此时可以通过视频的宽高计算出比例，进而设置视频view的显示大小。
                 * 如果不按照视频的比例进行显示的话，(以surfaceView为例子)内容会填充整个surfaceView。
                 * 意味着你的surfaceView显示的内容有可能是拉伸的
                 */
                break;

            case PlayerEvent.PLAY_PREPARED:
                // 播放器准备完成，此刻调用start()就可以进行播放了
                if (videoView != null) {
                    videoView.onStart();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 处理直播类事件
     */
    private void handleLiveEvent(int state, Bundle bundle) {
    }

    /**
     * 处理视频信息类事件
     */
    private void handleVideoInfoEvent(int state, Bundle bundle) {
    }

}
