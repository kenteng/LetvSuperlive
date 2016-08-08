package com.lesports.stadium.lsyvideo.videoview.live;

import java.util.ArrayList;
import java.util.List;

import com.lecloud.sdk.api.md.entity.live.LiveInfo;
import com.lecloud.sdk.api.md.entity.live.Stream;
import com.lecloud.sdk.api.md.entity.vod.cloud.MediaEntity;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.constant.StatusCode;
import com.lecloud.sdk.player.IPlayer;
import com.lecloud.sdk.videoview.live.LiveVideoView;
import com.lesports.stadium.lsyvideo.ui.ILetvLiveUICon;
import com.lesports.stadium.lsyvideo.ui.ILetvVodUICon;
import com.lesports.stadium.lsyvideo.ui.LetvLiveUIListener;
import com.lesports.stadium.lsyvideo.ui.impl.LetvLiveUICon;
import com.lesports.stadium.lsyvideo.ui.utils.ScreenUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

/**
 */
public class UILiveVideoView extends LiveVideoView {
	protected ILetvLiveUICon letvLiveUICon;
	protected List<MediaEntity> medialists;
	protected int width = -1;
	protected int height = -1;
	private boolean isSeeking = false;

	public UILiveVideoView(Context context) {
		super(context);
		initUICon(context);
	}

	private void initUICon(final Context context) {
		letvLiveUICon = new LetvLiveUICon(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(letvLiveUICon.getView(), params);
		letvLiveUICon.setLetvLiveUIListener(new LetvLiveUIListener() {

			@Override
			public void setRequestedOrientation(int requestedOrientation) {
				if (context instanceof Activity) {
					((Activity) context)
							.setRequestedOrientation(requestedOrientation);
				}
			}

			@Override
			public void resetPlay() {
				// LeLog.dPrint(TAG, "--------resetPlay");
			}

			@Override
			public void onSetDefination(int type) {
				// ((IMediaDataPlayer)
				// player).setDataSourceByRate(medialists.get(type).getVtype());
			}

			@Override
			public void onSeekTo(float sec) {
				// long msec = (long) (sec * player.getDuration());
				if (player != null) {
					player.seekTo((long) sec);
				}

			}

			@Override
			public void onClickPlay() {
				Log.i("wxn", "pause.222..");
				letvLiveUICon.setPlayState(!player.isPlaying());
				Log.i("wxm", "....zanti....");
				if (player.isPlaying()) {
					player.stop();
					player.reset();
					player.release();
					// if(player instanceof IMediaDataActionPlayer){
					// ((IMediaDataActionPlayer) player).stopTimeShift();
					// }
					letvLiveUICon.showController(false);
					enablePanoGesture(false);
				} else if (isComplete()) {
					// player.reset();
				} else {
					player.retry();
					// if(player instanceof IMediaDataActionPlayer){
					// ((IMediaDataActionPlayer) player).startTimeShift();
					// }
					letvLiveUICon.showController(true);
					enablePanoGesture(false);
				}

			}

			@Override
			public void onUIEvent(int event, Bundle bundle) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSwitchPanoVideoMode(int mode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(int progress) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartSeek() {
				// TODO Auto-generated method stub
				isSeeking = true;
			}
			

		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (width == -1 || height == -1) {
			width = UILiveVideoView.this.getLayoutParams().width;
			height = UILiveVideoView.this.getLayoutParams().height;
		}
		if (ScreenUtils.getOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
			UILiveVideoView.this.getLayoutParams().height = height;
			UILiveVideoView.this.getLayoutParams().width = width;
			letvLiveUICon
					.setRequestedOrientation(ILetvVodUICon.SCREEN_PORTRAIT);
		} else {
			UILiveVideoView.this.getLayoutParams().height = ScreenUtils
					.getHeight(getContext());
			UILiveVideoView.this.getLayoutParams().width = ScreenUtils
					.getWight(getContext());
			letvLiveUICon
					.setRequestedOrientation(ILetvVodUICon.SCREEN_LANDSCAPE);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onInterceptMediaDataSuccess(int event, Bundle bundle) {
		super.onInterceptMediaDataSuccess(event, bundle);
		switch (event) {
		case PlayerEvent.MEDIADATA_LIVE:
			List<String> ratetypes = new ArrayList<String>();
			LiveInfo liveInfo = bundle
					.getParcelable(PlayerParams.KEY_RESULT_DATA);
			List<Stream> mStreams = liveInfo.getStreams();
			String definition = liveInfo.getStreams().get(0).getRateName();
			for (Stream stream : mStreams) {
				ratetypes.add(stream.getRateName());
			}
			letvLiveUICon.setRateTypeItems(ratetypes, definition);
			break;

		default:
			break;
		}
	}

	@Override
	protected void notifyPlayerEvent(int state, Bundle bundle) {
		super.notifyPlayerEvent(state, bundle);
		switch (state) {
		case PlayerEvent.PLAY_COMPLETION:
			break;
		case PlayerEvent.PLAY_INFO:
			int code = bundle.getInt(PlayerParams.KEY_RESULT_STATUS_CODE);
			if (code == StatusCode.PLAY_INFO_VIDEO_RENDERING_START) {

			}
			break;
		case PlayerEvent.PLAY_SEEK_COMPLETE: {// 209
			isSeeking = false;
			break;
		}
		default:
			break;
		}
	}

	protected void enablePanoGesture(boolean enable) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean isComplete() {
		// TODO
		return player != null
				&& player.getStatus() == IPlayer.PLAYER_STATUS_EOS;
	}

	@Override
	public void onResume() {
		super.onResume();
		letvLiveUICon.showController(true);
		// if(player instanceof IMediaDataActionPlayer){
		// ((IMediaDataActionPlayer) player).startTimeShift();
		// }
		enablePanoGesture(true);
	}
}
