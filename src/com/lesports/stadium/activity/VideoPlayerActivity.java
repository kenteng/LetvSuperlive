package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.Commants2Adapter;
import com.lesports.stadium.adapter.FaceAdapter;
import com.lesports.stadium.adapter.FacePageAdeapter;
import com.lesports.stadium.adapter.MoreVideoAdapter;
import com.lesports.stadium.adapter.NoStartActionAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.DiscussBean;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.danmu.Damku;
import com.lesports.stadium.danmu.library.danmaku.loader.ILoader;
import com.lesports.stadium.danmu.library.danmaku.loader.IllegalDataException;
import com.lesports.stadium.danmu.library.danmaku.loader.android.DanmakuLoaderFactory;
import com.lesports.stadium.danmu.library.danmaku.model.BaseDanmaku;
import com.lesports.stadium.danmu.library.danmaku.model.Duration;
import com.lesports.stadium.danmu.library.danmaku.model.android.DanmakuGlobalConfig;
import com.lesports.stadium.danmu.library.danmaku.model.android.Danmakus;
import com.lesports.stadium.danmu.library.danmaku.model.android.SpannedCacheStuffer;
import com.lesports.stadium.danmu.library.danmaku.parser.BaseDanmakuParser;
import com.lesports.stadium.danmu.library.danmaku.parser.DanmakuFactory;
import com.lesports.stadium.danmu.library.danmaku.parser.IDataSource;
import com.lesports.stadium.danmu.library.danmaku.parser.android.AcFunDanmakuParser;
import com.lesports.stadium.danmu.library.ui.widget.DanmakuView;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.DiscussFragment;
import com.lesports.stadium.lsyvideo.VideoLayoutParams;
import com.lesports.stadium.lsyvideo.videoview.pano.vod.UIPanoVodVideoView;
import com.lesports.stadium.lsyvideo.videoview.vod.UIVodVideoView;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.FaceUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CirclePageIndicator;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.HorizontalListView;
import com.lesports.stadium.view.Mylistview;
import com.lesports.stadium.view.SharePopupWindow;

public class VideoPlayerActivity extends BaseActivity implements
		OnClickListener, PlatformActionListener, Callback, Damku {

	/**
	 * 比赛简介标签1
	 */
	private TextView mLable1;
	/**
	 * 比赛简介标签2
	 */
	private TextView mLable2;
	/**
	 * 比赛简介内容
	 */
	private TextView mContent;
	/**
	 * 比赛简介更多详情
	 */
	private TextView mGengduo;
	/**
	 * 展示更多视频的横向的listview
	 */
	private HorizontalListView mHorizontallistview;
	/**
	 * 评论列表展示listview
	 */
	private Mylistview mListview;
	/**
	 * 评论输入框
	 */
	private EditText mInput;
	/**
	 * 评论表情选择按钮
	 */
	private ImageView mFaceButton;
	/**
	 * 更多视频的适配器
	 */
	private MoreVideoAdapter mAdapter;
	/**
	 * 底部评论数据适配器
	 */
	private Commants2Adapter mCommantAdapter;
	/**
	 * 返回按钮
	 */
	private ImageView mBack;
	/*
	 * 视频苏略图
	 */
	private ImageView videoplayer_video_qian_imagevideo;
	private ImageView videoplayer_video_qian_image;
	/**
	 * 视频简介的全部内容显示
	 */
	private TextView mContents;
	/**
	 * 视频播放参数
	 */
	private Bundle bundle;
	/**
	 * 数据列表集合
	 */
	private List<HeightLightBean> list_data;
	/**
	 * 传递过来的集锦的id
	 */
	private String mId;
	/**
	 * 用来标记是否需要显示更多详情
	 */
	private boolean isshow = false;
	/**
	 * 底部海报列表的适配器
	 */
	private NoStartActionAdapter mNoactionadapter;
	/**
	 * 是否是横屏
	 */
	private boolean isCross_Screen = false;
	/**
	 * 定时器
	 */
	private Timer mTimer = new Timer();
	/**
	 * 活动ID
	 */
	private String activityId;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	private RelativeLayout vidoplayer_layout_top;
	private LayoutParams params;
	private LinearLayout vidoplayer_bottom_layout_input;
	private RelativeLayout rl_vl_top;
	private ImageView videoplayer_video_qian_share;
	private SharePopupWindow share;
	private RelativeLayout chat_more;
	private List<String> mFaceMapKeys;
	private List<View> lv;
	private FacePageAdeapter faceadapter;
	private ViewPager mFaceViewPager;
	private int mCurrentPage = 0;
	private CirclePageIndicator indicator;
	private LinearLayout face_ll, selecView;
	private boolean isStart = true;
	/**
	 * 弹幕状态
	 */
	boolean danmuStatus = false;
	/**
	 * 表情是否显示
	 */
	private boolean isShowFace = false;
	/**
	 * 存储请求到的评论
	 */
	private LinkedList<DiscussBean> mDiscussList = new LinkedList<DiscussBean>();
	private BaseDanmakuParser mParser;
	// 当前是否需要重播
	private boolean isCircle = false;
	/**
	 * 活动实体类对象
	 */
	private SenceBean mSenceBean;
	/**
	 * 定时器
	 */
	private Timer refreshDiscussTimer = new Timer();
	/**
	 * 用于标记刷新界面的时候获取的时间数据
	 */
	private final int TIME_TAG = 22;
	private TextView mAddMoreButton;
	private ProgressBar mAddmoreProgress;
	/**
	 * 众筹评论最后一条数据的事件值
	 */
	private String END_TIME_PINLUN;
	/**
	 * 网络请求处理
	 */
	private final int HANDLE_TAG_2 = 2;
	private final int HANDLE_TAG_101 = 101;
	private final int HANDLE_TAG_16 = 16;
	private final int HANDLE_TAG_116 = 116;
	private final int HANDLE_TAG_166 = 166;
	/**
	 * 当前视频索罗图
	 */
	public String  thumbnail;
	/**
	 * 视频播放对象
	 */
	private IMediaDataVideoView videoView;
	/**
	 * 视频播放监听器
	 */
    VideoViewListener mVideoViewListener = new VideoViewListener() {


        @Override
        public void onStateResult(int event, Bundle bundle) {
            handleVideoInfoEvent(event, bundle);// 处理视频信息事件
            handlePlayerEvent(event, bundle);// 处理播放器事件
            handleLiveEvent(event, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调

        }
    };
	private Handler dataHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_TAG_2:
				/*
				 * 错误代码：999 系统运行异常
				 */
				String backdata = (String) msg.obj;
				if (!Utils.isNullOrEmpty(backdata)) {
					useWayGetDidsssData(backdata);
				}
				break;
			case HANDLE_TAG_101:
				Toast.makeText(getApplicationContext(), "评论成功",
						Toast.LENGTH_SHORT).show();
				useWayAddDataToList();
				break;
			case TIME_TAG:
				String backdatatime = (String) msg.obj;
				// ActivityDetailBean beans=jsonParserData(backdatatime);
				// 调用方法，拿出时间数据，将至进行解析
				initDate(backdatatime);
				break;
			case HANDLE_TAG_16:
				// 众筹品论加载更多加载成功
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);

				String backdata2 = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata2)) {
					useWayHandleAddMoreSuccess(backdata2);
				}
				break;
			case HANDLE_TAG_116:
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);
				// 众筹品论加载更多加载成功但无数据
				break;
			case HANDLE_TAG_166:
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);
				// 众筹品论加载更多加载失败

				break;

			default:
				break;
			}
		}

	};
	// private String content;
	private ScrollView vidoplayer_scrollview;
	private InputMethodManager inputManager;// 软键盘管理类
	/**
	 * 弹幕显示对象
	 */
	private DanmakuView sv_danmaku;
	private FrameLayout video_flv;
	/**
	 * 是否开始播放视频
	 *
	 */
	private boolean isStartPlay = false;
	/**
	 * 是否开启关闭弹幕的开关
	 */
	private MyReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_vidoplayer);
		ShareSDK.initSDK(this);
		receiver = new MyReceiver();
		// 调用方法，接受传递过来的数据集合
		initget();
		initviews();
		initDate();
		// 调用方法，初始化视频简介数据
		initdataofintroduction();
		// 调用方法，初始化更多精彩视频数据
		if (list_data != null && list_data.size() != 0) {
			initdatatolistview(list_data);

		}
		loadDisscuss();
		// initmorevideo();
		// 初始化底部评论数据
		requestAllDiscuss();
		initBottomCommant();
		initFace();
		createDialog();
	}

	private void initdatas(List<DiscussBean> listbean) {
		// TODO Auto-generated method stub
		mCommantAdapter = new Commants2Adapter(listbean,
				VideoPlayerActivity.this);
		mListview.setAdapter(mCommantAdapter);
	}

	/**
	 * 初始化底部的横向的listview，并且加载数据
	 * 
	 * @param mHorizonListview2
	 * @param list2
	 */
	private void initdatatolistview(final List<HeightLightBean> list2) {
		final List<HeightLightBean> list = new ArrayList<HeightLightBean>();
		if (list2 != null && list2.size() != 0) {
			for (int i = 0; i < list2.size(); i++) {
				if (list2.get(i).getFileType().equals("1")) {
					list.add(list2.get(i));
				}
				if (list2.get(i).getId().equals(mId)) {
					thumbnail = list2.get(i).getVideoImageUrl();
					LApplication.loader.DisplayImage(
							ConstantValue.BASE_IMAGE_URL
									+ thumbnail
									+ ConstantValue.IMAGE_END,
							videoplayer_video_qian_image);
					shared_url = ConstantValue.BASE_IMAGE_URL
							+ list2.get(i).getVideoImageUrl()
							+ ConstantValue.IMAGE_END;
				}
			}
		}
		if (list != null && list.size() != 0) {
			mNoactionadapter = new NoStartActionAdapter(list,
					VideoPlayerActivity.this);
			mHorizontallistview.setAdapter(mNoactionadapter);
			mHorizontallistview
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// 需要先判断，是不是视频，也就是说需要判断标记
							final HeightLightBean bean = list.get(arg2);
							if (bean.getFileType().equals("1")) {
								mId = bean.getId();
								initdataofintroduction();
								GlobalParams.WEB_URL = ConstantValue.SHARED_VIDEOPLSY
										+ mId;
								videoplayer_video_qian_imagevideo
										.setVisibility(View.GONE);
								videoplayer_video_qian_image
										.setVisibility(View.GONE);
								isCircle = false;
								audioId = bean.getResourceId();
								isStartPlay = true;
								thumbnail = bean.getVideoImageUrl();
								fileSoure = bean.getFileSource();
								videoView.stopAndRelease();
								bundle = createBundleLive("1".equals(fileSoure), audioId);
								if(!TextUtils.isEmpty(supprotfull)){
									if(!supprotfull.equals(bean.getSupportFullView())){
										videoContainer.removeView((View)videoView);
										supprotfull = bean.getSupportFullView();
										videoView = "1".equals(supprotfull)?new UIPanoVodVideoView(VideoPlayerActivity.this):new UIVodVideoView(VideoPlayerActivity.this);
										videoView.setVideoViewListener(mVideoViewListener);
								        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(VideoPlayerActivity.this, 16, 9));
									}
								}
								if ("1".equals(fileSoure)) {
									 videoView.setDataSource(bundle);
						        } else {
						        	 videoView.setDataSource(audioId);
						        }
								requestAllDiscuss();
							};
						}
					});
		}
	}

	/**
	 * 用来接收传递过来的数据
	 */
	private void initget() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String tag = intent.getStringExtra("tag");
		String tagss = intent.getStringExtra("isuse");
		if (!TextUtils.isEmpty(tagss) && tagss.equals("yes")) {
			mSenceBean = (SenceBean) intent.getSerializableExtra("bean");
			if (mSenceBean != null) {
				// 说明是从为开始界面进入这个界面的，所以这里需要开启定时器，来进行时间计算
				refrenshUI();
			} else {
			}
		}
		mId = intent.getStringExtra("id");
		GlobalParams.WEB_URL = ConstantValue.SHARED_VIDEOPLSY + mId;
		heightLigtBean = (HeightLightBean) intent.getSerializableExtra("heightlightbean");
		if(heightLigtBean!=null)
			audioId = heightLigtBean.getResourceId();
		Log.i("wxn", "audioI:"+audioId);
		if (tag != null && tag.equals("height")) {
			// 说明是从集锦页面跳过的
			// int selection = intent.getIntExtra("selection",0);
			list_data = (List<HeightLightBean>) intent
					.getSerializableExtra("list");
			activityId = intent.getStringExtra("activityId");
			// list_data.get(selection);
			Log.i("123", mId);
			Log.i("123", list_data.size() + "");
		}

	}

	/**
	 * 该方法用来每隔一分钟刷新一次界面
	 */
	private void refrenshUI() {
		// TODO Auto-generated method stub
		refreshDiscussTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 调用方法，重新获取界面数据，拿出时间数据，进行重新适配加载到UI
				reGetTimeData();
			}
		}, 60000, 60000);
	}

	/**
	 * 重新获取时间数据，进行计时操作
	 */
	private void reGetTimeData() {
		// TODO Auto-generated method stub
		// userUtilsGetDataxstime(id);
		Message msg = new Message();
		msg.what = TIME_TAG;
		msg.obj = mSenceBean.getStarttime();
		dataHandler.sendMessage(msg);

	}

	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * 
	 * @param starttime
	 */
	private void initDate(String starttime) {
		// TODO Auto-generated method stub
		long starttimes = getChangeToTime(starttime);
		Date date = new Date();
		long currenttimes = date.getTime();
		Log.i("当前时间", currenttimes + "");
		Log.i("开始时间", starttimes + "");

		// 调用方法，计算两个时间之间差值
		calculationTime(starttimes, currenttimes);
	}

	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * 
	 * @param str
	 * @return
	 */
	public long getChangeToTime(String str) {
		if (TextUtils.isEmpty(str))
			return 0;
		long time = Long.parseLong(str);
		return time;

	}

	/**
	 * 计算时间差值
	 * 
	 * @param starttimes
	 * @param currenttimes
	 */
	private void calculationTime(long starttimes, long currenttimes) {
		// TODO Auto-generated method stub
		// SimpleDateFormat dfs = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long between = 0;
		try {
			// java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
			// java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
			// between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
			between = (starttimes - currenttimes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
				- min * 60 * 1000 - s * 1000);
		Log.i("当前计算后时间是", day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
				+ "毫秒");
		// System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
		// + "毫秒");
		/**
		 * 设置时间到控件上
		 */
		if (between <= 0) {
			Intent intent = new Intent(VideoPlayerActivity.this,
					LiveDetialActivity.class);
			intent.putExtra("id", mSenceBean.getId());
			intent.putExtra("no", "1");
			intent.putExtra("lable", mSenceBean.getLabel());
			intent.putExtra("url", mSenceBean.getFileUrl());
			// 直播源id 用于直播
			intent.putExtra("resourceId", mSenceBean.getResourceId());
			startActivity(intent);
		}

	}

	// 获取传递过来的数据
	private void initDate() {
		Intent intent = getIntent();
		String id = intent.getStringExtra("playId");
		inputManager = (InputMethodManager) this
				.getSystemService("input_method");
		HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
		overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
		overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
		DanmakuGlobalConfig.DEFAULT
				.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN, 3)
				.setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f)
				.setScaleTextSize(1.2f)
				.setCacheStuffer(new SpannedCacheStuffer())// 图文混排使用SpannedCacheStuffer
				.preventOverlapping(overlappingEnablePair);
		if (sv_danmaku != null) {
			sv_danmaku.showFPS(false);
			sv_danmaku.enableDanmakuDrawingCache(true);
		}

	}

	/**
	 * 初始化底部评论数据
	 */
	private void initBottomCommant() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			String name = "姓名" + i;
			String content = "这里是内容，评论的内容" + i;
			list.add(content);
		}
		mCommantAdapter = new Commants2Adapter(mDiscussList,
				VideoPlayerActivity.this);
		mListview.setAdapter(mCommantAdapter);

	}

	/**
	 * 用来初始化视频简介部分的数据的方法
	 */
	private void initdataofintroduction() {
		// TODO Auto-generated method stub
		if (list_data != null && list_data.size() != 0) {
			HeightLightBean bean = null;
			for (int i = 0; i < list_data.size(); i++) {
				if (mId.equals(list_data.get(i).getId())) {
					bean = list_data.get(i);
					break;
				}
			}
			if (bean != null) {
				mLable1.setText(bean.getFileName());
				String soucre = bean.getFileSource();
				if("1".equals(soucre))
					soucre = "乐视云";
				else 
					soucre = "其它来源";
				mLable2.setText(soucre);
				shared_content = bean.getFileProfile();
				GlobalParams.SHAR_CONTENT = shared_content;
				GlobalParams.SHAR_TITLE = shared_content;
				if (bean.getFileProfile() != null
						&& !TextUtils.isEmpty(bean.getFileProfile())) {
					addDataToView(bean.getFileProfile());
				}
				mContents.setText(bean.getFileProfile());
			}
		}
	}

	/**
	 * 用来给限制字数
	 * 
	 * @param projectIntroduction
	 */
	private void addDataToView(String projectIntroduction) {

		if (projectIntroduction != null
				&& !TextUtils.isEmpty(projectIntroduction)) {
			int length = projectIntroduction.length();
			if (length >= 30) {
				String newString = projectIntroduction.substring(0, 29);
				String newsstring = newString + "...";
				mContent.setText(newsstring);
			} else {
				mContent.setText(projectIntroduction);
				mGengduo.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 初始化view
	 */
	private void initviews() {
		supprotfull = heightLigtBean.getSupportFullView();
		fileSoure = heightLigtBean.getFileSource();
		bundle = createBundleLive("1".equals(fileSoure),audioId);
		videoView = "1".equals(supprotfull)?new UIPanoVodVideoView(this):new UIVodVideoView(this);
		videoView.setVideoViewListener(mVideoViewListener);
		videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));
//		if ("1".equals(fileSoure)) {
//			 videoView.setDataSource(bundle);
//        } else {
//        	 videoView.setDataSource(audioId);
//        }
		vidoplayer_scrollview = (ScrollView) findViewById(R.id.vidoplayer_scrollview);
		mLable1 = (TextView) findViewById(R.id.vidoplayer_bisaijianjie_lable1);
		mLable2 = (TextView) findViewById(R.id.vidoplayer_bisaijianjie_lable2);
		mContent = (TextView) findViewById(R.id.vidoplayer_bisaijianjie_content);
		mGengduo = (TextView) findViewById(R.id.vidoplayer_bisaijianjie_gengduoxiangqing);
		mGengduo.setOnClickListener(this);
		videoplayer_video_qian_imagevideo = (ImageView) findViewById(R.id.videoplayer_video_qian_imagevideo);
		videoplayer_video_qian_image = (ImageView) findViewById(R.id.videoplayer_video_qian_image);
		mHorizontallistview = (HorizontalListView) findViewById(R.id.vidoplayer_haibaolv);
		vidoplayer_layout_top = (RelativeLayout) findViewById(R.id.vidoplayer_layout_top);
		rl_vl_top = (RelativeLayout) findViewById(R.id.rl_vl_top);
		vidoplayer_bottom_layout_input = (LinearLayout) findViewById(R.id.vidoplayer_bottom_layout_input);
		mListview = (Mylistview) findViewById(R.id.vidoplayer_pinglun_lv);
		mListview.setFocusable(false);
		initmListviewCommant(mListview);
		mInput = (EditText) findViewById(R.id.discuss_input_et);
		mInput.clearFocus();
		mFaceButton = (ImageView) findViewById(R.id.faceview);
		mFaceButton.setOnClickListener(this);
		videoplayer_video_qian_share = (ImageView) findViewById(R.id.videoplayer_video_qian_share);
		mBack = (ImageView) findViewById(R.id.videoplayer_video_qian_back);
		mBack.setOnClickListener(this);
		videoplayer_video_qian_share.setOnClickListener(this);
		videoplayer_video_qian_imagevideo.setOnClickListener(this);
		mContents = (TextView) findViewById(R.id.vidoplayer_bisaijianjie_contents);
		mContents.setVisibility(View.GONE);
		sendview = (TextView) findViewById(R.id.sendview);
		sendview.setOnClickListener(this);
		mFaceViewPager = (ViewPager) findViewById(R.id.face_pager);
		chat_more = (RelativeLayout) findViewById(R.id.chat_more);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		face_ll = (LinearLayout) findViewById(R.id.face_ll);
		selecView = (LinearLayout) findViewById(R.id.selecView);
		mInput.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					chat_more.setVisibility(View.GONE);
					selecView.setVisibility(View.GONE);
					isShowFace = false;
					mFaceButton.setImageResource(R.drawable.fasong_biaoqing);
				}

			}
		});
		mInput.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				chat_more.setVisibility(View.GONE);
				selecView.setVisibility(View.GONE);
				mFaceButton.setImageResource(R.drawable.fasong_biaoqing);
				isShowFace = false;
			}
		});
		mInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = mInput.getText().toString().trim();
				if (s != null && content.length() > 0) {
					// 发送按钮
					sendview.setVisibility(View.VISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) sendview
							.getLayoutParams();
					// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					int top = DensityUtil.dip2px(VideoPlayerActivity.this, 10);
					params.setMargins(0, top / 2, top, top / 2);
					params.width = DensityUtil.dip2px(VideoPlayerActivity.this,
							35);
					sendview.setLayoutParams(params);
				} else {
					sendview.setVisibility(View.INVISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) sendview
							.getLayoutParams();
					params.width = 0;
					params.setMargins(0, 0, 0, 0);
					sendview.setLayoutParams(params);
				}

			}
		});
		// video_flv = (FrameLayout) findViewById(R.id.video_flv);
		sv_danmaku = (DanmakuView) findViewById(R.id.sv_danmaku);
		vidoplayer_scrollview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				inputManager.hideSoftInputFromWindow(VideoPlayerActivity.this
						.getWindow().getDecorView().getWindowToken(), 0);
				chat_more.setVisibility(View.GONE);
				selecView.setVisibility(View.GONE);
				return false;
			}
		});

	}

	/**
	 * 初始化讨论获取接口数据
	 * 
	 * @param mListviewCommant2
	 */
	private void initmListviewCommant(Mylistview mListviewCommant2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) VideoPlayerActivity.this
				.getSystemService(VideoPlayerActivity.this.LAYOUT_INFLATER_SERVICE);
		View footview = inflater.inflate(R.layout.footview_zhongchou_taolun,
				null);
		mListviewCommant2.addFooterView(footview);
		mAddMoreButton = (TextView) footview
				.findViewById(R.id.textView_taolun_dibu_footview);
		mAddmoreProgress = (ProgressBar) findViewById(R.id.progressBar1textView_taolun_dibu_footview);
		mAddMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAddMoreButton.setVisibility(View.GONE);
				mAddmoreProgress.setVisibility(View.VISIBLE);
				requestAllDiscuss_addmore(END_TIME_PINLUN);
			}
		});
	}

	/**
	 * 获取全部讨论内容
	 */
	private void requestAllDiscuss_addmore(String time) {

		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		if (!TextUtils.isEmpty(mId)) {
			params.put("fileId", mId); // 活动id
		} else {
			params.put("fileId", activityId);
		}
		params.put("dType", "1"); // 活动id
		params.put("releaseMillisecond", time);
		params.put("action", "2");
		params.put("rows", "20");
		// params.put("userId", GlobalParams.USER_ID); //用户id
		// params.put("fileId", "1"); //宣传视屏id
		// Log.i("", "走到这里了么？");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(dataHandler==null)
							return;
						if (data == null) {
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("DISCUSS",
											"好吧走到这里了么？"
													+ data.getNetResultCode()
													+ backdata);
									/*
									 * Log.i(getActivity().getClass().getName(),
									 * backdata);
									 */
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.what = HANDLE_TAG_16;
										msg.obj = backdata;
										dataHandler.sendMessage(msg);
									} else if (data.getNetResultCode() == 600) {
										Message msg = new Message();
										msg.what = HANDLE_TAG_116;
										dataHandler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.what = HANDLE_TAG_166;
										msg.obj = backdata;
										dataHandler.sendMessage(msg);
									}

								}
							}

						}
					}
				}, false, false);

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.videoplayer_video_qian_back:
			// 返回按钮
			onBackPressed();
			break;
		case R.id.videoplayer_video_qian_imagevideo:
			// 视频播放
			videoView.stopAndRelease();
			if ("1".equals(fileSoure)) {
				 videoView.setDataSource(bundle);
	        } else {
	        	 videoView.setDataSource(audioId);
	        }
			videoplayer_video_qian_imagevideo.setVisibility(View.GONE);
			videoplayer_video_qian_image.setVisibility(View.GONE);
			break;
		case R.id.videoplayer_video_qian_share:
			// 分享
			showPop();
			break;
		case R.id.vidoplayer_bisaijianjie_gengduoxiangqing:
			if (isshow) {
				// 说明当前显示缩略的,需要显示全部
				mGengduo.setVisibility(View.GONE);
				mContent.setVisibility(View.GONE);
				mContents.setVisibility(View.VISIBLE);
				isshow = !isshow;
			} else {
				mContent.setVisibility(View.VISIBLE);
				mContents.setVisibility(View.GONE);
				isshow = !isshow;
			}
			break;
		case R.id.sendview:
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				exitDialog.show();
				return;
			}
			String content = mInput.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(getApplicationContext(), "请输入内容", 1).show();
				return;
			} else if (content.length() > 70) {
				Toast.makeText(getApplicationContext(), "您发送的内容不能超过30字符",
						Toast.LENGTH_SHORT).show();
				return;
			}
			requestSendDiscuss(content);

			break;

		case R.id.faceview:
			// 发送表情
			if (!isShowFace) {
				inputManager.hideSoftInputFromWindow(this.getWindow()
						.getDecorView().getWindowToken(), 0);
				chat_more.setVisibility(View.VISIBLE);
				selecView.setVisibility(View.VISIBLE);
				face_ll.setVisibility(View.VISIBLE);
				mFaceButton.setImageResource(R.drawable.jianpan);
			} else {
				chat_more.setVisibility(View.GONE);
				selecView.setVisibility(View.GONE);
				mFaceButton.setImageResource(R.drawable.fasong_biaoqing);
			}
			isShowFace = !isShowFace;
			break;
		default:
			break;
		}
	}

	/**
	 * 发送评论接口
	 */
	private void requestSendDiscuss(String content) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileId", mId); // 宣传视屏id
		if (!TextUtils.isEmpty(mId)) {
			params.put("activityId", mId); // 活动id
		} else {
			params.put("activityId", activityId); // 活动id
		}
		params.put("userId", GlobalParams.USER_ID); // 用户id
		params.put("userName", GlobalParams.USER_NAME);// 用户昵称
		params.put("content", content);// 发表内容
		params.put("dType", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SEND_DISCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(getApplicationContext(), "网络错误", 1)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(getApplicationContext(), "网络错误",
										1).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(getApplicationContext(),
											"网络异常", 1).show();
								} else {
									Log.i("DISCUSS",
											"走到这里了么？发送成功"
													+ data.getNetResultCode()
													+ backdata);
									/*
									 * Log.i(getActivity().getClass().getName(),
									 * backdata);
									 */
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if (backdata.equals("success")) {
										Message sendMessage = new Message();
										sendMessage.what = HANDLE_TAG_101;
										sendMessage.obj = backdata;
										dataHandler.sendMessage(sendMessage);
									}
								}
							}

						}
					}
				}, false, false);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig != null)
			super.onConfigurationChanged(newConfig);
		if (!isCross_Screen) {
			// 横屏
			isCross_Screen = true;
			params = (LayoutParams) vidoplayer_layout_top.getLayoutParams();
			params.height = LayoutParams.MATCH_PARENT;
			vidoplayer_bottom_layout_input.setVisibility(View.GONE);
			rl_vl_top.setVisibility(View.GONE);
		} else {
			isCross_Screen = false;
			params.height = DensityUtil.dip2px(getApplicationContext(), 180);
			vidoplayer_bottom_layout_input.setVisibility(View.VISIBLE);
			rl_vl_top.setVisibility(View.VISIBLE);
		}
		vidoplayer_layout_top.setLayoutParams(params);
		addview();
		if (videoView != null) {
			videoView.onConfigurationChanged(newConfig);
		}

	}

	private void addview() {
		// vidoplayer_layout_top.removeView(sv_danmaku);
		// int height = skin.getHeight();
		// int width = skin.getWidth();
		// Log.i("wxn", "height :" + height + "width:" + width);
		// LayoutParams params = new LayoutParams(width, height);
		// sv_danmaku.setLayoutParams(params);
		// vidoplayer_layout_top.addView(sv_danmaku);
	}

	@Override
	public void onBackPressed() {
		if (isShowFace) {
			isShowFace = false;
			chat_more.setVisibility(View.GONE);
			selecView.setVisibility(View.GONE);
			mFaceButton.setImageResource(R.drawable.fasong_biaoqing);
			return;
		}
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			// player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT)
			// ILetvPlayerController controller =
			// playBoard.getPlayerController();
			// if(controller!=null)
			// controller.getIsPlayerController().setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
			return;
		}
		// skin = null;
		// if (playBoard != null) {
		// playBoard.onPause();
		// playBoard.onDestroy();
		// }
		bundle = null;
		params = null;
		mCommantAdapter = null;
		mAdapter = null;
		mListview = null;
		if (share != null) {
			share.dismiss();
			share = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		System.gc();
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter= new IntentFilter(DanmAction);
		 registerReceiver(receiver , filter); 
		 if (videoView != null) {
			 videoView.onResume();
		 }
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		 if (videoView != null) {
			 videoView.onPause();
		 }
	}

	@Override
	protected void onDestroy() {
		if (dataHandler != null)
			dataHandler.removeCallbacksAndMessages(null);
		dataHandler = null;
		share = null;
		super.onDestroy();
		GlobalParams.Switch = false;
		GlobalParams.SHOW_TIMER = 0;
		 if (videoView != null) {
			 videoView.onDestroy();
		 }
		if (mFaceMapKeys != null) {
			mFaceMapKeys.clear();
			mFaceMapKeys = null;
		}
		if (lv != null) {
			lv.clear();
			lv = null;
		}
		ShareSDK.stopSDK(this);
	}

	/**
	 * 分享网址
	 */
	private String shared_url;
	/*
	 * 分享内容
	 */
	private String shared_content;

	/**
	 * 显示分享选择界面
	 */
	private void showPop() {
		Log.i("wxn", "shared_url:" + shared_url + "   shared_content:"
				+ shared_content);
		// WEB_URL = ConstantValue.SHARED_VIDEOPLSY+mId;
		if (share == null) {
			share = new SharePopupWindow(this);
			share.setPlatformActionListener(this);
		}
		ShareModel model = new ShareModel();
		// model.setImageUrl(shared_url);
		model.setText(shared_content);
		model.setTitle(shared_content);
		model.setUrl(shared_url);
		share.initShareParams(model);
		share.setWebUrl(ConstantValue.SHARED_VIDEOPLSY + mId);
		share.showShareWindow(false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.main_share), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		if (what == 1) {
			Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		} else if (what == 2) {

		}

		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	/**
	 * 获取全部讨论内容
	 */
	private void requestAllDiscuss() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		if (!TextUtils.isEmpty(mId)) {
			params.put("fileId", mId); // 活动id
		} else {
			params.put("fileId", activityId);
		}
		params.put("dType", "1");
		params.put("rows", "20");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(dataHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(VideoPlayerActivity.this, "网络错误", 1)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(VideoPlayerActivity.this,
										"网络错误", 1).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(VideoPlayerActivity.this,
											"网络异常", 1).show();
								} else {
									Log.i("DISCUSS", "走到这里了么？" + activityId
											+ "===" + data.getNetResultCode()
											+ backdata);
									/*
									 * Log.i(getActivity().getClass().getName(),
									 * backdata);
									 */
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.what = HANDLE_TAG_2;
									msg.obj = backdata;
									dataHandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}

	/**
	 * 初始化表情面板
	 */
	private void initFace() {
		// 将表情map的key保存在数组中
		Set<String> keySet = FaceUtils.getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);

		lv = new ArrayList<View>();
		for (int i = 0; i < FaceUtils.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		faceadapter = new FacePageAdeapter(lv);
		mFaceViewPager.setAdapter(faceadapter);
		mFaceViewPager.setCurrentItem(mCurrentPage);
		indicator.setViewPager(mFaceViewPager);
		faceadapter.notifyDataSetChanged();
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	/**
	 * 初始化表情
	 * 
	 * @param i
	 * @return
	 */
	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == FaceUtils.NUM) {// 删除键的位置
					int selection = mInput.getSelectionStart();
					String text = mInput.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mInput.getText().delete(start, end);
							return;
						}
						mInput.getText().delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * FaceUtils.NUM + arg2;
					if (count <= FaceUtils.getFaceMap().size()) {

						// 下面这部分，在EditText中显示表情
						Bitmap bitmap = BitmapFactory
								.decodeResource(getResources(),
										(Integer) FaceUtils.getFaceMap()
												.values().toArray()[count]);
						if (bitmap != null) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();

							int newHeight = DensityUtil.dip2px(
									VideoPlayerActivity.this, 24);
							int newWidth = DensityUtil.dip2px(
									VideoPlayerActivity.this, 24);
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,
									0, rawWidth, rawHeigh, matrix, true);
							ImageSpan imageSpan = new ImageSpan(
									VideoPlayerActivity.this, newBitmap);
							String emojiStr = mFaceMapKeys.get(count);
							SpannableString spannableString = new SpannableString(
									emojiStr);
							spannableString.setSpan(imageSpan,
									emojiStr.indexOf('['),
									emojiStr.indexOf(']') + 1,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							mInput.append(spannableString);
						} else {
							String ori = mInput.getText().toString();
							int index = mInput.getSelectionStart();
							StringBuilder stringBuilder = new StringBuilder(ori);
							stringBuilder.insert(index, mFaceMapKeys.get(count));
							mInput.setText(stringBuilder.toString());
							mInput.setSelection(index
									+ mFaceMapKeys.get(count).length());
						}
					}
				}
			}
		});

		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public void closeDanmu() {
		Log.i("wxn", "close danmu////");
		if (danmuStatus) {
			mTimer.cancel();
		}
		sv_danmaku.hide();
		danmuStatus = false;
	}

	@Override
	public void openDanmu() {
		sv_danmaku.setVisibility(View.VISIBLE);
		danmuStatus = true;
		if (isStart) {
			sv_danmaku.start();
			isStart = false;
			getDiscussForTimer();
			sv_danmaku.show();
		} else {
			sv_danmaku.show();
		}
	}

	/**
	 * 获取当前 弹幕 开关状态
	 */
	public boolean getDmStatus() {
		return danmuStatus;
	}

	private BaseDanmakuParser createParser(String stream) {

		if (stream == null) {
			return new BaseDanmakuParser() {

				@Override
				protected Danmakus parse() {
					return new Danmakus();
				}
			};
		}

		ILoader loader = DanmakuLoaderFactory
				.create(DanmakuLoaderFactory.TAG_ACFUN);

		try {
			loader.load(stream);
		} catch (IllegalDataException e) {
			e.printStackTrace();
		}
		BaseDanmakuParser parser = new AcFunDanmakuParser();
		IDataSource<?> dataSource = loader.getDataSource();
		parser.load(dataSource);
		return parser;

	}

	/**
	 * 加载讨论数据
	 */
	private void loadDisscuss() {
		Map<String, String> map = new HashMap<String, String>();
		if (!TextUtils.isEmpty(mId)) {
			map.put("fileId", mId); // 活动id
		} else {
			map.put("fileId", activityId);
		}
		map.put("dType", "1");
		map.put("rows", "100");
		try {
			Date curDate = new Date(System.currentTimeMillis() - 60 * 1000);// 获取当前时间
			String str = DiscussFragment.formatter.format(curDate);
			map.put("startTime", str); // 时间
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, map, new GetDatas() {

					@Override
					public void getServerData(BackData result) {
						if (result != null) {
							Object obj = result.getObject();
							if (obj != null) {
								String content = (String) obj;
								mParser = createParser(content);
								sv_danmaku.prepare(mParser);
							}
						}

					}
				}, false, false);
	}

	/**
	 * 定时获取讨论数据
	 */
	private void getDiscussForTimer() {
		mTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				getDiscuss();
			}
		}, 1000 * 60, 3000 * 60);
	}

	private void getDiscuss() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fileId", mId);
		map.put("dType", "1");
		map.put("rows", "100");
		try {
			Date curDate = new Date(System.currentTimeMillis() - 60 * 1000);// 获取当前时间
			String str = DiscussFragment.formatter.format(curDate);
			map.put("startTime", str); // 时间
		} catch (Exception e) {
			e.printStackTrace();
		}
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, map, new GetDatas() {

					@Override
					public void getServerData(BackData result) {
						if (result != null) {
							Object obj = result.getObject();
							if (obj != null) {
								String contents = (String) obj;
								JSONArray jsonArr;
								try {
									jsonArr = new JSONArray(contents);
									for (int i = 0; i < jsonArr.length(); i++) {
										JSONObject temp = (JSONObject) jsonArr
												.get(i);
										getDanmu(temp.getString("content"),
												false);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

					}
				}, false, false);
	}

	private Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	private String audioId;
	private TextView sendview;
	private RelativeLayout videoContainer;

	private void getDanmu(String content, boolean isSend) {
		boolean iscontentFace = false;
		try {
			BaseDanmaku item = DanmakuFactory
					.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
			if (content.startsWith("[") && content.endsWith("]"))
				content = content + " ";
			SpannableStringBuilder value = SpannableStringBuilder
					.valueOf(content);
			Matcher localMatcher = EMOTION_URL.matcher(value);
			while (localMatcher.find()) {
				String str2 = localMatcher.group(0);
				int k = localMatcher.start();
				int m = localMatcher.end();
				if (m - k < 8) {
					if (FaceUtils.getFaceMap().containsKey(str2)) {
						iscontentFace = true;
						int face = FaceUtils.getFaceMap().get(str2);
						Bitmap bitmap = BitmapFactory.decodeResource(
								GlobalParams.context.getResources(), face);
						Log.i("wxn", "....bitmap" + bitmap);
						if (bitmap != null) {
							if (true) {
								int rawHeigh = bitmap.getHeight();
								int rawWidth = bitmap.getHeight();
								int newHeight = DensityUtil.dip2px(
										getApplicationContext(), 24);
								int newWidth = DensityUtil.dip2px(
										getApplicationContext(), 24);
								// 计算缩放因子
								float heightScale = ((float) newHeight)
										/ rawHeigh;
								float widthScale = ((float) newWidth)
										/ rawWidth;
								// 新建立矩阵
								Matrix matrix = new Matrix();
								matrix.postScale(heightScale, widthScale);
								// 压缩后图片的宽和高以及kB大小均会变化
								bitmap = Bitmap.createBitmap(bitmap, 0, 0,
										rawWidth, rawHeigh, matrix, true);
							}
							ImageSpan localImageSpan = new ImageSpan(
									GlobalParams.context, bitmap,
									ImageSpan.ALIGN_BASELINE);
							value.setSpan(localImageSpan, k, m,
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
			if (iscontentFace) {
				// 包含表情
				item.text = value;
				item.textShadowColor = 0;
				item.underlineColor = 0;
				// item.textColor = Color.RED;
				item.padding = 5;
				item.priority = 1;
			} else {
				item.text = content;
			}
			item.textSize = 18f * (mParser.getDisplayer().getDensity() - 0.6f);
			item.duration = new Duration(8 * 1000);
			item.priority = 1;
			item.textShadowColor = Color.WHITE;
			item.textColor = Color.WHITE;
			GlobalParams.SHOW_TIMER += 50;
			if (isSend) {
				item.time = sv_danmaku.getCurrentTime() + 800;
			} else {
				item.time = GlobalParams.SHOW_TIMER;
			}
			sv_danmaku.addDanmaku(item);
		} catch (Exception e) {

		}
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(VideoPlayerActivity.this,
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent(VideoPlayerActivity.this,
								LoginActivity.class);
						startActivity(intent);
						exitDialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						exitDialog.dismiss();
					}
				});
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindMessage("登录之后才能评论哦~");
	}

	/**
	 * 加载更多的时候刷新数据
	 */
	private void useWayAddDataToUi_addmore(String backdata1) {
		// TODO Auto-generated method stub
		try {
			JSONArray jsonArr = new JSONArray(backdata1);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject temp = jsonArr.getJSONObject(i);
				DiscussBean tempDiscuss = new DiscussBean();
				tempDiscuss.setContent(temp.getString("content"));
				tempDiscuss.setLikeCount(temp.getString("likeCount"));
				tempDiscuss.setReleaseDate(temp.getString("releaseDate"));
				tempDiscuss.setUserId(temp.getString("userId"));
				tempDiscuss.setUserImage(temp.getString("userImage"));
				tempDiscuss.setUserName(temp.getString("userName"));
				mDiscussList.add(tempDiscuss);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 处理获取下来的品论数据
	 * 
	 * @param backdata
	 */
	private void useWayGetDidsssData(String backdata) {
		try {
			JSONArray jsonArr = new JSONArray(backdata);
			for (int i = 0; i < jsonArr.length(); i++) {

				JSONObject temp = (JSONObject) jsonArr.get(i);
				DiscussBean tempDiscuss = new DiscussBean();
				tempDiscuss.setContent(temp.getString("content"));
				tempDiscuss.setLikeCount(temp.getString("likeCount"));
				tempDiscuss.setReleaseDate(temp.getString("releaseDate"));
				tempDiscuss.setUserId(temp.getString("userId"));
				tempDiscuss.setUserImage(temp.getString("userImage"));
				tempDiscuss.setUserName(temp.getString("userName"));
				mDiscussList.add(tempDiscuss);
			}
			END_TIME_PINLUN = mDiscussList.get(mDiscussList.size() - 1)
					.getReleaseDate();
			mCommantAdapter.setmDiscussList(mDiscussList);
			mCommantAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * 添加一条数据到列表项中
	 */
	private void useWayAddDataToList() {
		// TODO Auto-generated method stub
		DiscussBean bean = new DiscussBean();
		bean.setContent(mInput.getText().toString().trim());
		bean.setUserImage(GlobalParams.USER_HEADER);
		bean.setUserName(TextUtils.isEmpty(GlobalParams.NICK_NAME) ? GlobalParams.USER_NAME
				: GlobalParams.NICK_NAME);
		Date date = new Date();
		long currenttimes = date.getTime();
		bean.setReleaseDate(currenttimes + "");
		mDiscussList.addFirst(bean);
		mCommantAdapter.notifyDataSetChanged();
		mListview.setSelection(0);
		getDanmu(mInput.getText().toString(), true);
		mInput.setText("");
	}

	/**
	 * 众筹评论加载更多成功处理
	 * 
	 * @param backdata2
	 */
	private void useWayHandleAddMoreSuccess(String backdata2) {
		useWayAddDataToUi_addmore(backdata2);
		if (mDiscussList != null && mDiscussList.size() >= 1) {
			END_TIME_PINLUN = mDiscussList.get(mDiscussList.size() - 1)
					.getReleaseDate();
			if (mCommantAdapter != null) {
				mCommantAdapter.setmDiscussList(mDiscussList);
			} else {
				initdatas(mDiscussList);
			}
		}
	}

	/**
	 * 创建点播用的bundle
	 * 
	 * @param isVuid
	 *            播放是否有vuid 如果有为true，如果以路径播放 为false
	 * @param path
	 *            如果是以路径播放 ，播放视频路径值
	 * @param vuid
	 *            如果是以vuid播放 vuid值
	 */
	private Bundle createBundleLive(boolean isVuid,String vuid) {
		Bundle bundle = new Bundle();
		bundle.putInt(PlayerParams.KEY_PLAY_MODE, PlayerParams.VALUE_PLAYER_VOD);
		if (isVuid) {
			bundle.putString(PlayerParams.KEY_PLAY_UUID, ConstantValue.LS_UUID);
			bundle.putString(PlayerParams.KEY_PLAY_VUID, vuid);
			bundle.putString(PlayerParams.KEY_PLAY_CHECK_CODE, "");
			bundle.putString(PlayerParams.KEY_PLAY_PAYNAME, "0");
			bundle.putString(PlayerParams.KEY_PLAY_USERKEY,
					ConstantValue.LS_USKY);
			bundle.putString(PlayerParams.KEY_PLAY_BUSINESSLINE, "120");
		} else {
			bundle.putString("path", vuid);
		}
		bundle.putBoolean("pano", true);
		bundle.putBoolean("hasSkin", true);
		return bundle;
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
            case PlayerEvent.PLAY_COMPLETION:
            	//播放完成
            	LApplication.loader.DisplayImage(
						 ConstantValue.BASE_IMAGE_URL
						 + thumbnail +
						 ConstantValue.IMAGE_END,
						 videoplayer_video_qian_image);
            	videoplayer_video_qian_imagevideo.setImageResource(R.drawable.video_receiver);
            	videoplayer_video_qian_imagevideo.setVisibility(View.VISIBLE);
				videoplayer_video_qian_image.setVisibility(View.VISIBLE);
//				skin.setVisibility(View.GONE);
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
    
    public static String DanmAction = "receiver_danmu_receiver";
    /**
     * 视频播放对象
     */
	private HeightLightBean heightLigtBean;
	/**
	 * 1:全景视频  
	 */
	private String supprotfull;
	/**
	 * 1：乐视云2：其他
	 */
	private String fileSoure;
    /**
     * 弹幕开启关闭
     */
    public class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			Log.i("wxn", "接收到广播。。。");
			if (intent.getAction().equals(DanmAction)) {
				String status = intent.getStringExtra("status");
				if("1".equals(status)){
					openDanmu();
				}else{
					closeDanmu();
				}
			}
		}
	};

}
