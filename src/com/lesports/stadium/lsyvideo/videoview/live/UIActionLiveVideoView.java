package com.lesports.stadium.lsyvideo.videoview.live;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lecloud.sdk.api.md.entity.action.ActionInfo;
import com.lecloud.sdk.api.md.entity.live.LiveInfo;
import com.lecloud.sdk.api.md.entity.live.Stream;
import com.lecloud.sdk.api.status.ActionStatus;
import com.lecloud.sdk.api.timeshift.ItimeShiftListener;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.player.IAdPlayer;
import com.lecloud.sdk.player.IMediaDataActionPlayer;
import com.lecloud.sdk.player.IMediaDataPlayer;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.player.base.BaseMediaDataPlayer;
import com.lecloud.sdk.player.live.LivePlayer;
import com.lecloud.sdk.utils.LeLog;
import com.lecloud.sdk.videoview.live.ActionLiveVideoView;
import com.lesports.stadium.lsyvideo.ui.ILetvLiveUICon;
import com.lesports.stadium.lsyvideo.ui.ILetvVodUICon;
import com.lesports.stadium.lsyvideo.ui.LetvLiveUIListener;
import com.lesports.stadium.lsyvideo.ui.impl.LetvLiveUICon;
import com.lesports.stadium.lsyvideo.ui.utils.NetworkUtils;
import com.lesports.stadium.lsyvideo.ui.utils.ScreenUtils;
import com.lesports.stadium.lsyvideo.ui.view.V4MultLiveRightView;
import com.lesports.stadium.lsyvideo.ui.view.V4MultLiveRightView.SwitchMultLiveCallback;
import com.lesports.stadium.lsyvideo.ui.view.VideoNoticeView.IReplayListener;


import java.util.ArrayList;
import java.util.List;

/**
 */
public class UIActionLiveVideoView extends ActionLiveVideoView {
	public static final String TAG = "UIActionLiveVideoView";

	protected ILetvLiveUICon letvLiveUICon;
	protected List<String> vtypList = new ArrayList<String>();
	protected int width = -1;
	protected int height = -1;
	protected V4MultLiveRightView mV4MultLiveRightView;
	protected TextView timeTextView;
	protected long serverTime;
	/** 记录上次是否显示多机位图标 */
	private boolean isShowMultLiveBtnStatus = false;
	private boolean isSeeking = false;

	public UIActionLiveVideoView(Context context) {
		super(context);
		initUICon(context);
		initTimeShiftListener();
	}

	private void initTimeShiftListener() {
		((LivePlayer) player).setTimeShiftListener(new ItimeShiftListener() {

			@Override
			public void onChange(long serverTime, long currentTime, long begin) {
				UIActionLiveVideoView.this.serverTime = serverTime;
				if (letvLiveUICon != null && !isSeeking) {
					letvLiveUICon.setTimeShiftChange(serverTime, currentTime, begin);
				}
			}
		});
	}

	@Override
	protected void processActionStatus(int state) {
		super.processActionStatus(state);
		letvLiveUICon.hideLoading();
		letvLiveUICon.hideWaterMark();
		letvLiveUICon.processActionStatus(state);
		if (state == ActionStatus.STATUS_END) {
			removeRight();
		}
	}

	private void removeRight() {
		if (mV4MultLiveRightView != null) {
			removeView(mV4MultLiveRightView);
			mV4MultLiveRightView = null;
		}
	}

	@Override
	protected void processLiveStatus(int state) {
		super.processLiveStatus(state);
		letvLiveUICon.hideLoading();
		letvLiveUICon.hideWaterMark();
		letvLiveUICon.processLiveStatus(state);
		if (state == com.lecloud.sdk.api.md.entity.action.LiveInfo.STATUS_END) {
			removeRight();
		}
	}

	private void initUICon(final Context context) {
		letvLiveUICon = new LetvLiveUICon(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(letvLiveUICon.getView(), params);
		letvLiveUICon.setRePlayListener(new IReplayListener() {

			@Override
			public Bundle getReportParams() {
				return ((BaseMediaDataPlayer) player).getReportParams();
			}

			@Override
			public void onRePlay() {
				player.retry();
			}
		});
		letvLiveUICon.setLetvLiveUIListener(new LetvLiveUIListener() {

			@Override
			public void setRequestedOrientation(int requestedOrientation) {
				if (context instanceof Activity) {
					((Activity) context).setRequestedOrientation(requestedOrientation);
				}
			}

			@Override
			public void resetPlay() {
				player.retry();
			}

			@Override
			public void onSetDefination(int positon) {
				((IMediaDataPlayer) player).setDataSourceByRate(vtypList.get(positon));
			}

			@Override
			public void onSeekTo(float sec) {
				// long msec = (long) (sec * player.getDuration());
				isSeeking = false;
				LeLog.iPrint(TAG, "------sec:" + (long) sec);
				if (player != null) {
					letvLiveUICon.showLoadingProgress();
					seekTimeShift((long) sec);
				}
			}

			@Override
			public void onClickPlay() {
				Log.i("wxn", "pause.3333..");
				letvLiveUICon.setPlayState(!player.isPlaying());
				if (player.isPlaying()) {
					player.stop();
					player.reset();
					player.release();
					letvLiveUICon.showController(false);
					letvLiveUICon.canGesture(false);
					showMultLiveBtn(false);
					enablePanoGesture(false);
				} else if (isComplete()) {
					// player.reset();
				} else {
					player.retry();
					letvLiveUICon.showController(true);
					if (letvLiveUICon.isFullScreen()) {
						// letvLiveUICon.setRequestedOrientation(ScreenUtils.getOrientation(context));
						letvLiveUICon.canGesture(true);
						showMultLiveBtn(true);
					}
					enablePanoGesture(true);
				}
			}

			@Override
			public void onUIEvent(int event, Bundle bundle) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSwitchPanoVideoMode(int mode) {
				switchPanoVideoMode(mode);
			}

			@Override
			public void onProgressChanged(int progress) {
				// TODO Auto-generated method stub
				((LetvLiveUICon) letvLiveUICon).syncSeekProgress(progress);
			}

			@Override
			public void onStartSeek() {
				// TODO Auto-generated method stub
				isSeeking = true;
			}

		});
	}

	private void showMultLiveBtn(boolean isShow) {
		if (mV4MultLiveRightView == null || mV4MultLiveRightView.mMultLiveBtn == null) {
			return;
		}
		if (isShow) {
			if (isShowMultLiveBtnStatus) {
				mV4MultLiveRightView.setVisibility(View.VISIBLE);
			}
		} else {
			// if(mV4MultLiveRightView.mMultLiveBtn.getVisibility() ==
			// View.VISIBLE){
			// isShowMultLiveBtnStatus = true;
			// mV4MultLiveRightView.mMultLiveBtn.setVisibility(View.GONE);
			// }else{
			// isShowMultLiveBtnStatus = false;
			// }
			if (mV4MultLiveRightView.getVisibility() == View.VISIBLE) {
				isShowMultLiveBtnStatus = true;
				if (mV4MultLiveRightView.isShowSubLiveView) {
					mV4MultLiveRightView.toggleSubMultLiveView();
				}
				mV4MultLiveRightView.stopAndRelease();
				mV4MultLiveRightView.setVisibility(GONE);
			} else {
				isShowMultLiveBtnStatus = false;
			}
		}
	}

	protected void switchPanoVideoMode(int mode) {

	}

	protected void enablePanoGesture(boolean enable) {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (width == -1 || height == -1) {
			width = UIActionLiveVideoView.this.getLayoutParams().width;
			height = UIActionLiveVideoView.this.getLayoutParams().height;
		}
		if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
			UIActionLiveVideoView.this.getLayoutParams().height = height;
			UIActionLiveVideoView.this.getLayoutParams().width = width;
			letvLiveUICon.setRequestedOrientation(ILetvVodUICon.SCREEN_PORTRAIT);
			if (mV4MultLiveRightView != null) {
				if (mV4MultLiveRightView.isShowSubLiveView) {
					mV4MultLiveRightView.toggleSubMultLiveView();
				}
				mV4MultLiveRightView.stopAndRelease();
				mV4MultLiveRightView.setVisibility(GONE);
			}
		} else {
			UIActionLiveVideoView.this.getLayoutParams().height = ScreenUtils.getHeight(getContext());
			UIActionLiveVideoView.this.getLayoutParams().width = ScreenUtils.getWight(getContext());
			letvLiveUICon.setRequestedOrientation(ILetvVodUICon.SCREEN_LANDSCAPE);
			if (mV4MultLiveRightView != null) {
				mV4MultLiveRightView.setVisibility(VISIBLE);
			}
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onInterceptActionMediaDataSuccess(int event, Bundle bundle) {
		ActionInfo actionInfo = bundle.getParcelable(PlayerParams.KEY_RESULT_DATA);
		if (actionInfo != null && actionInfo.getActivityName() != null) {
			letvLiveUICon.setTitle(actionInfo.getActivityName());
		}
		List<com.lecloud.sdk.api.md.entity.action.LiveInfo> liveInfos = actionInfo.getLiveInfos();
		if (liveInfos.size() > 1) {
			initSubVideoView(liveInfos);
		}
		letvLiveUICon.showWaterMark(((ActionInfo) bundle.getParcelable(PlayerParams.KEY_RESULT_DATA)).getCoverConfig());
		super.onInterceptActionMediaDataSuccess(event, bundle);
	}

	@Override
	protected void onInterceptLiveMediaDataSuccess(int event, Bundle bundle) {
		List<String> ratetypes = new ArrayList<String>();
		LiveInfo liveInfo = bundle.getParcelable(PlayerParams.KEY_RESULT_DATA);
		if (mV4MultLiveRightView != null) {
			mV4MultLiveRightView.setCurrentMultLive(liveInfo.getLiveId());
		}
		List<Stream> mStreams = liveInfo.getStreams();
		vtypList.clear();
		for (Stream stream : mStreams) {
			ratetypes.add(stream.getRateName());
			vtypList.add(stream.getRateType());
		}
		String lastRate = ((IMediaDataPlayer) player).getLastRate();
		int index = -1;
		for (int i = 0; i < vtypList.size(); i++) {
			if (vtypList.get(i).equals(lastRate)) {
				index = i;
				break;
			}
		}

		if (!TextUtils.isEmpty(lastRate)) {
			String definition = ratetypes.get(index);
			letvLiveUICon.setRateTypeItems(ratetypes, definition);
		} else {
			String definition = liveInfo.getStreams().get(0).getRateName();
			letvLiveUICon.setRateTypeItems(ratetypes, definition);
		}

		super.onInterceptLiveMediaDataSuccess(event, bundle);
	}

	@Override
	protected void onInterceptMediaDataError(int event, Bundle bundle) {
		super.onInterceptMediaDataError(event, bundle);
		letvLiveUICon.hideLoading();
		letvLiveUICon.hideWaterMark();
		letvLiveUICon.processMediaState(event, bundle);
	}

	private void initSubVideoView(List<com.lecloud.sdk.api.md.entity.action.LiveInfo> liveInfos) {

		// LayoutInflater mLayoutInflater = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.CENTER_VERTICAL);
		if (mV4MultLiveRightView == null) {
			mV4MultLiveRightView = new V4MultLiveRightView(context);
			addView(mV4MultLiveRightView, lp);
		}
		if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
			mV4MultLiveRightView.setVisibility(VISIBLE);
		} else {
			mV4MultLiveRightView.setVisibility(GONE);
		}
		mV4MultLiveRightView.setActionInfoDone();
		mV4MultLiveRightView.setStreams(liveInfos);
		mV4MultLiveRightView.setSwitchMultLiveCallbackk(new SwitchMultLiveCallback() {

			@Override
			public void switchMultLive(String liveId) {
				// initSubVideoView(liveInfos);
				if (player != null) {
					setDataSourceByLiveId(liveId);
				}
			}
		});
	}

	@Override
	protected void notifyPlayerEvent(int event, Bundle bundle) {
		super.notifyPlayerEvent(event, bundle);
		letvLiveUICon.processPlayerState(event, bundle);
		switch (event) {
		case PlayerEvent.PLAY_PREPARED:
			letvLiveUICon.setPlayState(true);
			if (NetworkUtils.hasConnect(context) && !letvLiveUICon.isShowLoading()) {
				letvLiveUICon.showLoadingProgress();
			}
			break;
		case PlayerEvent.PLAY_INFO:
			int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
			if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {

			}

			switch (code) {
			case StatusCode.PLAY_INFO_BUFFERING_START:// 500004
				if (NetworkUtils.hasConnect(context) && !letvLiveUICon.isShowLoading()) {
					letvLiveUICon.showLoadingProgress();
				}
				break;
			case StatusCode.PLAY_INFO_BUFFERING_END:// 500005
				letvLiveUICon.hideLoading();
				break;
			case StatusCode.PLAY_INFO_VIDEO_RENDERING_START:// 500006
				letvLiveUICon.showWaterMark();
				;
				letvLiveUICon.hideLoading();
				break;
			default:
				break;
			}
			break;
		case PlayerEvent.PLAY_ERROR:// 205
			letvLiveUICon.hideLoading();
			break;
        case PlayerEvent.PLAY_SEEK_COMPLETE: {//209
            isSeeking = false;
            break;
        }
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		if (timeTextView != null) {
			removeView(timeTextView);
		}
		super.onPause();
	}

	@Override
	protected void onInterceptAdEvent(int code, Bundle bundle) {
		switch (code) {
		case PlayerEvent.AD_START:
			letvLiveUICon.getView().setVisibility(GONE);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			timeTextView = new TextView(context);
			timeTextView.setBackgroundColor(Color.BLACK);
			bringChildToFront(timeTextView);
			timeTextView.setPadding(20, 20, 20, 20);
			addView(timeTextView, lp);
			letvLiveUICon.hideLoading();
			break;
		case IAdPlayer.AD_PLAY_ERROR:
		case PlayerEvent.AD_COMPLETE:
			removeView(timeTextView);
			letvLiveUICon.getView().setVisibility(VISIBLE);
			break;
		case PlayerEvent.AD_PROGRESS:
			timeTextView.setText("广告 " + bundle.getInt(IAdPlayer.AD_TIME) + "s");
			break;

		default:
			break;
		}
		super.onInterceptAdEvent(code, bundle);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean isComplete() {
		// TODO
		return player != null && player.getStatus() == IPlayer.PLAYER_STATUS_EOS;
	}

	@Override
	public void onResume() {
		super.onResume();
		letvLiveUICon.showController(true);
		letvLiveUICon.canGesture(true);
		showMultLiveBtn(true);
		// if(player instanceof IMediaDataActionPlayer){
		// ((IMediaDataActionPlayer) player).startTimeShift();
		// }
		enablePanoGesture(true);
	}
}
