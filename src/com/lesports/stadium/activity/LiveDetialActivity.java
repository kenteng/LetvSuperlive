package com.lesports.stadium.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lecloud.sdk.constant.PlayerEvent;
import com.lecloud.sdk.constant.PlayerParams;
import com.lecloud.sdk.videoview.IMediaDataVideoView;
import com.lecloud.sdk.videoview.VideoViewListener;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.VideoPlayerActivity.MyReceiver;
import com.lesports.stadium.adapter.CardAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.TemMessage;
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
import com.lesports.stadium.fragment.HighlightsFragment;
import com.lesports.stadium.fragment.ImageFragment;
import com.lesports.stadium.fragment.InteractiveFragment;
import com.lesports.stadium.fragment.LyricsFragment;
import com.lesports.stadium.fragment.SurroundingFragment;
import com.lesports.stadium.fragment.VideoFragment;
import com.lesports.stadium.lsyvideo.VideoLayoutParams;
import com.lesports.stadium.lsyvideo.videoview.live.UIActionLiveVideoView;
import com.lesports.stadium.lsyvideo.videoview.pano.live.UIPanoActionLiveVideoView;
import com.lesports.stadium.lsyvideo.videoview.pano.vod.UIPanoVodVideoView;
import com.lesports.stadium.lsyvideo.videoview.vod.UIVodVideoView;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.FaceUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CardView;
import com.lesports.stadium.view.CardView.CardClickListener;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyViewPager;
import com.lesports.stadium.view.SpringProgressView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ***************************************************************
 * @ClassName:  LiveDetialActivity 
 * 
 * @Desc : live点击后的详情页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-1-29 下午4:45:04
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

/**
 * @author simon
 * 
 */
public class LiveDetialActivity extends FragmentActivity implements
		OnClickListener, Damku {
	/**
	 * 返回键
	 */
	private ImageView iv_detail_back;
	/**
	 * viewpage下划线
	 */
	private ImageView iv_line;

	/**
	 * viewpage
	 */
	private MyViewPager vp_pager;
	/**
	 * viewpage的适配
	 */
	private CategoryPageAdapter categoryAdapter;
	/**
	 * 记录前一页的信息
	 */
	private int lastPager = 0;
	/**
	 * 导航栏的线性布局
	 */
	private LinearLayout ll_middle;

	/**
	 * 活动id
	 */
	private String id;
	/**
	 * 活动数据页码
	 */
	private String no;
	/**
	 * 活动标签
	 */
	private String lable;
	/*
	 * 视频id
	 */
	private String videoId;
	/**
	 * 当前视频类型   1：乐视云2：其他
	 */
	private String resourceType;
	/**
	 * 聊天室
	 */
	private String roomId;
	/*
	 * 视频播放结束后提示语
	 */
	private TextView thanks_tv;
	
	/**
	 * 传入乐视云薄情参数
	 */
	private Bundle bundle;
	/*
	 * 解析本地弹幕数据
	 */
	private BaseDanmakuParser mParser;
	/*
	 * 包括视频播放器和弹幕的容器
	 */
	private RelativeLayout video_flv;
	/*
	 * 弹幕控件
	 */
	private DanmakuView mDanmakuView;
	/*
	 * 当前弹幕状态 开启关闭
	 */
	public boolean danmuStatus = false;
	/**
	 * 变量控制弹幕开启一次
	 */
	private boolean isStart = true;
	/**
	 * 标题
	 */
	private String titles[];
	/**
	 * 存储fragmentid
	 */
	private int fragmentid[];
	/**
	 * 讨论id
	 */
	private final int DISS_ID = 1000;
	/**
	 * 歌词id
	 */
	private final int SONG_ID = 1001;
	/**
	 * 集锦id
	 */
	private final int SPECIMENS_ID = 1002;
	/**
	 * 周边id
	 */
	private final int ARROUND_ID = 1003;
	/**
	 * 互动id
	 */
	private final int INTERACT_ID = 1004;
	/**
	 * 视频id
	 */
	private final int VIDEO_ID = 1005;
	/**
	 * 图片id
	 */
	private final int PICTURE_ID = 1006;
	/**
	 * 存储中部导航栏
	 */
	private ArrayList<TextView> titlsevs = new ArrayList<TextView>();

	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * 头部集锦标题
	 */
	private TextView tv_title;
	// private AntiAliasImageView aaiv_4;
	/**
	 * 头部集锦说明
	 */
	private TextView tv_next;
	/**
	 * 头部集锦详情
	 */
	private TextView tv_detail;
	/**
	 * 控制导航页点击次数
	 */
	private int count = 0;
	// onResume 方法只执行一次
	private boolean onlyOne = true;

	CardView cardView;
	MyCardView adapter;
	List<String> data;
	private final String SHAREDPREFERENCES_NAME = "LS_APP";
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	List<HeightLightBean> liveDetailList = new ArrayList<HeightLightBean>();
	/**
	 * 图片传入list
	 */
	List<HeightLightBean> picturelList = new ArrayList<HeightLightBean>();
	/**
	 * 比赛提示框
	 */
	private Dialog dialog;
	/**
	 * 比赛提示框显示的view
	 */
	private View dialogView;
	/**
	 * 根据id获取球队信息成功
	 */
	private final int SUCCESS_TEMMESSAGE = 66666;
	/**
	 * 获取活动详情成功
	 */
	private final int SUCCESS_ACTIVE_MESSAGE = 333;
	/**
	 * 视频详情成功
	 */
	private final int SUCCESS_VIDEO_MESSAGE = 33;
	/**
	 * 回顾获取视频数据
	 */
	private final int FIRST_VIDEO = 3;
	/**
	 * 支持球队成功
	 */
	private final int AGREE_SUCCESS = 88888;
	/**
	 * 获取网络数据异常
	 */
	private final int GETFILER = 4444;
	/**
	 * 支持异常
	 */
	private final int AGREE_ERROR = 444;
	/**
	 * 存储球队信息
	 */
	private ArrayList<TemMessage> tems;
	/**
	 * 支持那支球队
	 */
	private int agreeTemIndex = -1;
	/**
	 * 当前类实例对象
	 */
	public static LiveDetialActivity instance;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 讨论对象
	 */
	private DiscussFragment discussfg;
	/**
	 * 是否开启关闭弹幕的开关
	 */
	private MyReceiver receiver;
	/*
	 * 乐视云播放控件
	 */
	private IMediaDataVideoView videoView;
	/*
	 * 乐视云播放回调
	 */
	VideoViewListener mVideoViewListener = new VideoViewListener() {

		@Override
		public void onStateResult(int event, Bundle bundle) {
			handleVideoInfoEvent(event, bundle);// 处理视频信息事件
			handlePlayerEvent(event, bundle);// 处理播放器事件
			handleLiveEvent(event, bundle);// 处理直播类事件,如果是点播，则这些事件不会回调

		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				break;
			case TAG_DATA:
				// 调用方法，解析数据
				analyseData((String) msg.obj);
				break;
			case SUCCESS_TEMMESSAGE:
				analyseTemMessage((String) msg.obj);
				break;
			case AGREE_SUCCESS:
				agreeTem(msg.arg2);
				break;
			case GETFILER:
				Toast.makeText(getApplicationContext(), "网络异常",
						Toast.LENGTH_SHORT).show();
				break;

			case AGREE_ERROR:
				Toast.makeText(getApplicationContext(), "服务器异常，请稍后重试",
						Toast.LENGTH_SHORT);
				break;
			case SUCCESS_ACTIVE_MESSAGE:
				analyseActiveMessage((String) msg.obj);
				break;
			case SUCCESS_VIDEO_MESSAGE:
				analyseVideoMessage((String) msg.obj);
				break;
			case FIRST_VIDEO:
				analyseFirstVideo((String) msg.obj);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_live_detail);
		receiver = new MyReceiver();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initDate();
		//根据 传入值 判断是 1进行中 2 回顾
		//如果是进行中 根据活动id获取活动详情
		//如果是 回顾  顶部是点播
		if(TextUtils.isEmpty(end)){
			//进行中
			getActiviteMessage();
			getVideoMessage();
			// 初始化比赛弹出框view
			initDialogView();
			// 当有直播源时才 加载弹幕内容
			if (!TextUtils.isEmpty(videoId))
				loadDisscuss();
		}else{
			getdataFrom();
			//回顾 仅有视频 图片和商城
		}
		
	}
	
	private void initviewDate(){
		initFragmentDate();
		// 初始化view
		initView();
		inittiems();
		// 初始监听
		initListener();
	}
	
	private void initFragmentDate(){
		//判断是否是回顾界面进来的
		if (!TextUtils.isEmpty(end)) {
			titles = new String[] { "视频","图片","商城" };
			fragmentid = new int[] { VIDEO_ID, PICTURE_ID,ARROUND_ID };
		}else{
			if (TextUtils.isEmpty(videoId)){
				titles = new String[] { "讨论", "商城", "互动" };
				fragmentid = new int[] { DISS_ID, ARROUND_ID, INTERACT_ID };
			}else{
				titles = new String[] { "讨论", "集锦", "商城", "互动" };
				fragmentid = new int[] { DISS_ID, SPECIMENS_ID, ARROUND_ID,
						INTERACT_ID };
			}
		}
	}

	/**
	 * 如果是球赛初始化球队信息
	 */
	private void inittiems() {
		if (!TextUtils.isEmpty(end)){
			//说明是从回顾进来的
			return;
		}
		// 判断是否是球赛 0 是球赛 获取球队信息
		if (!"1".equals(camptype)) {
			GlobalParams.IS_SPORT = true;
			getTemSMessages();
			createDialog();
		} else {
			GlobalParams.IS_SPORT = false;
			if (discussfg == null) {
				Fragment discussfgs =  categoryAdapter
				.getItem(0);
				if(discussfgs instanceof DiscussFragment){
					discussfg = (DiscussFragment)discussfgs;
				}
			}
//			discussfg.getGiftMessage();
		}
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		instance = this;
		Intent intent = getIntent();
		// 活动id
		id = intent.getStringExtra("id");
		hasSupported = intent.getStringExtra("hasSupported");
		hasScreamed = intent.getStringExtra("hasScreamed");
		camptype = intent.getStringExtra("camptype");
		// 该比赛是否结束 如果有值 则以结束 已结束不显示互动
		end = intent.getStringExtra("end");
		no = intent.getStringExtra("no");
		lable = intent.getStringExtra("lable");
		// 直播源id
		videoId = intent.getStringExtra("resourceId");
	}
	
	/**
	 * 创建直播参数bundle
	 */
	private void createBundleForLive( String mActiveId) {
		bundle = new Bundle();
        bundle.putInt(PlayerParams.KEY_PLAY_MODE, PlayerParams.VALUE_PLAYER_ACTION_LIVE);
        bundle.putString(PlayerParams.KEY_PLAY_ACTIONID, mActiveId);
        bundle.putBoolean(PlayerParams.KEY_PLAY_USEHLS, true);
        bundle.putBoolean("pano", true);
        bundle.putBoolean("hasSkin", true);;
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
	private void createBundleLive(boolean isVuid,String vuid) {
		bundle = new Bundle();
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
	}

	/**
	 * 事件监听
	 */
	private void initListener() {
		if(startImage!=null)
			startImage.setOnClickListener(this);
		iv_detail_back.setOnClickListener(this);
		for (int i = 0; i < titlsevs.size(); i++) {
			titlsevs.get(i).setTag(i);
			titlsevs.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (int) v.getTag();
					vp_pager.setCurrentItem(index);
				}
			});
		}
		vp_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// position：当前页
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / titles.length, position
						* GlobalParams.WIN_WIDTH / titles.length, 0, 0);
				animation.setDuration(300);
				animation.setFillAfter(true);
				iv_line.startAnimation(animation);
				lastPager = position;
				rl_sprots.setVisibility(View.GONE);
				for (TextView tv : titlsevs)
					tv.setTextColor(getResources().getColor(R.color.word_gray));
				switch (position) {
				case 0:
					if(GlobalParams.IS_SPORT&& tems!=null)
						rl_sprots.setVisibility(View.VISIBLE);
					titlsevs.get(0)
							.setTextColor(
									getResources().getColor(
											R.color.service_select_txt));
					break;
				case 1:
					Utils.hidenInputService(LiveDetialActivity.this, vp_pager);
					titlsevs.get(1)
							.setTextColor(
									getResources().getColor(
											R.color.service_select_txt));
					break;
				case 2:
					Utils.hidenInputService(LiveDetialActivity.this, vp_pager);
					titlsevs.get(2)
							.setTextColor(
									getResources().getColor(
											R.color.service_select_txt));
					break;
				case 3:
					Utils.hidenInputService(LiveDetialActivity.this, vp_pager);
					titlsevs.get(3)
							.setTextColor(
									getResources().getColor(
											R.color.service_select_txt));
					break;
				case 4:
					Utils.hidenInputService(LiveDetialActivity.this, vp_pager);
					titlsevs.get(4)
							.setTextColor(
									getResources().getColor(
											R.color.service_select_txt));
					break;
				}

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
	 * 初始化布局
	 */
	private void initView() {
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		boolean isFirstIn = preferences.getBoolean("isFirstIn_live", true);
		if (isFirstIn && TextUtils.isEmpty(end)) {
			preferences.edit().putBoolean("isFirstIn_live", false).commit();
			guide_page_live = (RelativeLayout) findViewById(R.id.guide_page_live);
			TextView syas = (TextView) findViewById(R.id.syas);
			if("1".equals(camptype)){
				syas.setText(getResources().getString(R.string.music_syas));
			}else{
				syas.setText(getResources().getString(R.string.tems_syas));
			}
			guide_page_live.setVisibility(View.VISIBLE);
			guide_item1 = (RelativeLayout) findViewById(R.id.guide_item1);
			guide_item2 = (RelativeLayout) findViewById(R.id.guide_item2);
			guide_page_live.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (count == 0) {
						guide_item1.setVisibility(View.GONE);
						guide_item2.setVisibility(View.VISIBLE);
					} else {
						guide_page_live.setVisibility(View.GONE);
					}
					count++;
				}
			});
		}
		// 只有有直播源id时才初始化播放器，弹幕这些控件
		if (!TextUtils.isEmpty(videoId)||!TextUtils.isEmpty(end)) {
			// 直播 控件  支持 全景视频 ,不支持全景视频
			videoContainer = (RelativeLayout) findViewById(R.id.videoContainer);
			if(TextUtils.isEmpty(end)){
				videoView = "1".equals(supportFullView)?new UIPanoActionLiveVideoView(this):new UIActionLiveVideoView(this);
				videoView.setVideoViewListener(mVideoViewListener);
				videoContainer.addView((View) videoView,
						VideoLayoutParams.computeContainerSize(this, 16, 9));
				createBundleForLive(videoId);
				if("1".equals(fileSource))
					videoView.setDataSource(bundle);
				else
					videoView.setDataSource(videoId);
				videoContainer.setVisibility(View.VISIBLE);
			}else{
				shortImage = (ImageView) findViewById(R.id.iv_short_image);
				startImage = (ImageView) findViewById(R.id.iv_start_image);
				LApplication.loader.DisplayImage(
						 ConstantValue.BASE_IMAGE_URL
						 + videoImageUrl +
						 ConstantValue.IMAGE_END,
						 shortImage);
				videoView = "1".equals(supportFullView)?new UIPanoVodVideoView(this):new UIVodVideoView(this);
				videoView.setVideoViewListener(mVideoViewListener);
				videoContainer.addView((View) videoView,
						VideoLayoutParams.computeContainerSize(this, 16, 9));
				createBundleLive("1".equals(fileSource), videoId);
				videoContainer.setVisibility(View.VISIBLE);
				shortImage.setVisibility(View.VISIBLE);
				startImage.setVisibility(View.VISIBLE);
			}
			
			// 弹幕
			mDanmakuView = (DanmakuView) findViewById(R.id.sv_danmaku);
			mDanmakuView.setVisibility(View.VISIBLE);
			HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
			overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
			overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
			DanmakuGlobalConfig.DEFAULT
					.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN,
							3).setDuplicateMergingEnabled(false)
					.setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
					.setCacheStuffer(new SpannedCacheStuffer())// 图文混排使用SpannedCacheStuffer
					.preventOverlapping(overlappingEnablePair);
			if (mDanmakuView != null) {
				mDanmakuView.showFPS(false);
				mDanmakuView.enableDanmakuDrawingCache(true);
			}
		} else {
			getdataFromServiceheight();

		}
		thanks_tv = (TextView) findViewById(R.id.thanks_tv);
		cardView = (CardView) this.findViewById(R.id.cardview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		// 顶部RelativeLayout
		video_flv = (RelativeLayout) findViewById(R.id.video_flv);
		// 菜单栏父控件
		ll_middle = (LinearLayout) findViewById(R.id.ll_middle);
		rl_sprots = (RelativeLayout) findViewById(R.id.rl_sprots);
		mBlueLogo = (ImageView) rl_sprots.findViewById(R.id.bule_logo);
		mRedLogo = (ImageView) rl_sprots.findViewById(R.id.red_logo);
		mBlueNum = (TextView) rl_sprots.findViewById(R.id.blue_num);
		mRedNum = (TextView) rl_sprots.findViewById(R.id.red_num);
		mProjress = (SpringProgressView) rl_sprots
				.findViewById(R.id.projress);
		titlsevs.clear();
		// 菜单栏父控件 添加view
		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			TextView tv = new TextView(this);
			tv.setText(title);
			tv.setId(1000 + i * 10);
			tv.setTextSize(15);
			tv.setTextColor(getResources().getColor(R.color.word_gray));
			tv.setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1);
			titlsevs.add(tv);
			ll_middle.addView(tv, params);
		}
		titlsevs.get(0).setTextColor(
				getResources().getColor(R.color.service_select_txt));
		titlsevs.get(0).requestFocus();
		// 返回按钮
		iv_detail_back = (ImageView) findViewById(R.id.iv_detail_back);
		// 设置导航栏的导航线
		iv_line = (ImageView) findViewById(R.id.iv_line);
		LayoutParams layoutParams = new LayoutParams(GlobalParams.WIN_WIDTH
				/ titles.length, DensityUtil.dip2px(this, 2));
		// layoutParams.addRule(RelativeLayout.BELOW, R.id.ll_middle);
		iv_line.setLayoutParams(layoutParams);
		// ViewPager
		vp_pager = (MyViewPager) findViewById(R.id.vp_pager);
		vp_pager.setnoScroll(false);
		categoryAdapter = new CategoryPageAdapter(getSupportFragmentManager());
		vp_pager.setAdapter(categoryAdapter);
		vp_pager.setOffscreenPageLimit(5);
		if (TextUtils.isEmpty(videoId)&&TextUtils.isEmpty(end)) {
			video_flv.setBackgroundResource(R.drawable.livedetail_bg);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_detail_back:
			onBackPressed();
			break;
		case R.id.bule_logo:
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				exitDialog.show();
				return;
			}
			supprotTem(0);
			// 支持蓝队
			break;
		case R.id.red_logo:
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				exitDialog.show();
				return;
			}
			supprotTem(1);
			// 支持红队
			break;
		case R.id.iv_start_image:
			if("1".equals(fileSource))
				videoView.setDataSource(bundle);
			else
				videoView.setDataSource(videoId);
			startImage.setVisibility(View.GONE);
			shortImage.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	private class CategoryPageAdapter extends FragmentStatePagerAdapter {

		/**
		 * 构造函数，传入FragmentManager
		 */
		public CategoryPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int num) {
			return getFragment(num);
		}

		@Override
		public int getCount() {
			return titles == null ? 0 : titles.length;
		}

	}

	private Fragment getFragment(int num) {
		Fragment fragment;
		int fId = fragmentid[num];
		Log.i("123", "fragment:" + num + "  ..." + fId);
		switch (fId) {
		case DISS_ID:
			discussfg = new DiscussFragment(id, vp_pager,lable,roomId);
			fragment = discussfg;
			break;
		case SONG_ID:
			fragment = new LyricsFragment(id);
			break;
		case SPECIMENS_ID:
			fragment = new HighlightsFragment(id);
			break;
		case ARROUND_ID:
			fragment = new SurroundingFragment(id, no, lable);
			break;
		case INTERACT_ID:
			fragment =InteractiveFragment.netInstance(id);
			break;
		case VIDEO_ID:
			fragment = new VideoFragment(id);
			break;
		case PICTURE_ID:
			fragment = new ImageFragment(id);
			break;

		default:
			discussfg = new DiscussFragment(id, vp_pager,lable,roomId);
			fragment = discussfg;
			break;
		}
		return fragment;
	}

	/**
	 * 修改fragment内容
	 */
	public void changeItem(int itemId) {
		vp_pager.setCurrentItem(itemId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter= new IntentFilter(VideoPlayerActivity.DanmAction);
		registerReceiver(receiver , filter); 
		if(videoView!=null)
			videoView.onResume();
		if (dialog != null && dialog.isShowing()) {
			getTemSMessages();
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
		instance = null;
		discussfg = null;
		
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		 if (videoView != null) {
	            videoView.onDestroy();
	        }
		GlobalParams.SHOW_TIMER = 0;
		cardView.topPos = 0;
		if (data != null) {
			data.clear();
			data = null;
		}
		if (titlsevs != null) {
			titlsevs.clear();
			titlsevs = null;
		}
		if(tems!=null){
			tems.clear();
			tems = null;
		}
		if(fragmentid!=null)
			fragmentid = null;
		instance = null;
		dialogView = null;
		imageLoader = null;
		cardView = null;
		adapter = null;
		mDanmakuView = null;
		video_flv = null;
		mParser = null;
		bundle = null;
		dialog= null;
		
		vp_pager = null;
		categoryAdapter = null;
		tv_detail = null;
		tv_next = null;
		tv_title = null;
		System.gc();
		super.onDestroy();
	}

	private boolean isCross_Screen;
	private LayoutParams params;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		params = (LayoutParams) video_flv.getLayoutParams();

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 横屏
			Log.i("wxn", "横屏");
			isCross_Screen = true;
			params.height = LayoutParams.MATCH_PARENT;
			iv_line.setVisibility(View.GONE);
			vp_pager.setVisibility(View.GONE);
			ll_middle.setVisibility(View.GONE);
			iv_detail_back.setVisibility(View.GONE);
		} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i("wxn", "s竖屏屏");
			isCross_Screen = false;
			params.height = DensityUtil.dip2px(getApplicationContext(), 180);
			iv_line.setVisibility(View.VISIBLE);
			vp_pager.setVisibility(View.VISIBLE);
			ll_middle.setVisibility(View.VISIBLE);
			iv_detail_back.setVisibility(View.VISIBLE);
		}
		video_flv.setLayoutParams(params);
		if (videoView != null) {
            videoView.onConfigurationChanged(newConfig);
        }
		// addview();
		super.onConfigurationChanged(newConfig);
	}

	private void addview() {
//		video_flv.removeView(mDanmakuView);
//		int height = skin.getHeight();
//		int width = skin.getWidth();
//		LayoutParams params = new LayoutParams(width, height);
//		mDanmakuView.setLayoutParams(params);
//		video_flv.addView(mDanmakuView);
	}

	// 关闭弹幕
	public void closeDanmu() {
		mDanmakuView.hide();
		danmuStatus = false;
	}

	// 开启弹幕
	public void openDanmu() {
		danmuStatus = true;
		if (isStart) {
			mDanmakuView.start();
			isStart = false;
			// getDiscussForTimer();
		} else {
			mDanmakuView.show();
		}
	}

	/*
	 * 读取弹幕内容
	 */
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
	 * 加载数据
	 */
	private void loadDisscuss() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", id);
		map.put("dType", "2");
		map.put("rows", "60");
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
								mDanmakuView.prepare(mParser);
							}
						}

					}
				}, false, false);
	}

	@Override
	public void onBackPressed() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			finish();
			return;
		}
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			// player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT)
//			ILetvPlayerController controller = playBoard.getPlayerController();
//			if (controller != null)
//				controller.getIsPlayerController().setScreenResolution(
//						ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
			return;
		}
		
		if (titlsevs != null) {
			titlsevs.clear();
			titlsevs = null;
		}
		if (liveDetailList != null) {
			liveDetailList.clear();
			liveDetailList = null;
		}
		if (picturelList != null) {
			picturelList.clear();
			picturelList = null;
		}
		categoryAdapter = null;
		vp_pager = null;
		cardView = null;
		ll_middle = null;
		mDanmakuView = null;
		mParser = null;
		discussfg = null;

		finish();
		super.onBackPressed();
	}

	/**
	 * 获取弹幕状态
	 */
	@Override
	public boolean getDmStatus() {
		return danmuStatus;
	}

	/**
	 * 定时器
	 */
	private Timer mTimer = new Timer();

	// 定时获取数据
	// private void getDiscussForTimer(){
	// mTimer.scheduleAtFixedRate(new TimerTask() {
	//
	// @Override
	// public void run() {
	// getDiscuss();
	// }
	// }, 1000*60, 5000*60);
	// }

	private void getDiscuss() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", id);
		map.put("dType", "2");
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
										setDanmu(temp.getString("content"),
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

	// 匹配规则 判断是否含有表情
	private Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	private RelativeLayout guide_page_live;
	private RelativeLayout guide_item1;
	private RelativeLayout guide_item2;

	// content 发送内容 isSend 为true 时能 在界面上立刻弹出该弹幕
	public void setDanmu(String content, boolean isSend) {
		try {
			BaseDanmaku item = DanmakuFactory
					.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
			if (content.startsWith("[") && content.endsWith("]")) {
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
							int face = FaceUtils.getFaceMap().get(str2);
							Bitmap bitmap = BitmapFactory.decodeResource(
									GlobalParams.context.getResources(), face);
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
					item.text = value;
					item.textShadowColor = 0;
					item.underlineColor = 0;
					item.textColor = Color.RED;
					item.padding = 5;
				}
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
				item.time = mDanmakuView.getCurrentTime() + 800;
			} else {
				item.time = GlobalParams.SHOW_TIMER;
			}
			mDanmakuView.addDanmaku(item);
		} catch (Exception e) {

		}
	}

	private void getdataFromServiceheight() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id);
		params.put("fileClass", "0");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.HIGHT_LEIGHT, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = TAG_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}

				}, false, false);
	}

	int trynumber = 1;

	public class MyCardView extends CardAdapter<String> {
		private android.view.LayoutInflater inflater;

		public MyCardView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			inflater = inflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public View getCardView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.card_item, null);
			}

			final ImageView iv_background = (ImageView) convertView
					.findViewById(R.id.iv_background);
			ImageView iv_play = (ImageView) convertView
					.findViewById(R.id.iv_play);
			final int i = position;
			iv_background.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("1".equals(liveDetailList
							.get(i % liveDetailList.size()).getFileType())) {
						Intent intent = new Intent(LiveDetialActivity.this,
								VideoPlayerActivity.class);
						intent.putExtra("tag", "height");
						intent.putExtra("id",
								liveDetailList.get(i % liveDetailList.size())
										.getId());
						intent.putExtra("heightlightbean",
								liveDetailList.get(i % liveDetailList.size())
										);
						intent.putExtra("list", (Serializable) liveDetailList);
						// Toast.makeText(LiveDetialActivity.this,liveDetailList.get(i
						// % liveDetailList.size()).getFileName() , 0).show();
						startActivity(intent);
					} else {
						// 跳转到图片浏览的界面
						Intent intent = new Intent(LiveDetialActivity.this,
								ImagePagerActivity.class);
						intent.putExtra("tag", "height");
						intent.putExtra("id",
								liveDetailList.get(i % liveDetailList.size())
										.getId());
						// intent.putExtra("audioId",liveDetailList.get(i %
						// liveDetailList.size()).getResourceId());

						intent.putExtra("list", (Serializable) picturelList);
						// Toast.makeText(LiveDetialActivity.this,liveDetailList.get(i
						// % liveDetailList.size()).getFileName() , 0).show();
						startActivity(intent);
					}

				}
			});
			// Log.e("SSSSSSSSSSSSSS", liveDetailList.get(i %
			// liveDetailList.size()).getFileType());
			if ("1".equals(liveDetailList.get(i % liveDetailList.size())
					.getFileType())) {
				iv_play.setVisibility(View.VISIBLE);
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL
						+ liveDetailList.get(position % liveDetailList.size())
								.getVideoImageUrl() + ConstantValue.IMAGE_END,
						iv_background, list_options);
			} else {
				iv_play.setVisibility(View.GONE);
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL
						+ liveDetailList.get(position % liveDetailList.size())
								.getFileUrl() + ConstantValue.IMAGE_END,
						iv_background, list_options);
			}
			// des.setText(data.get(position % data.size()));
			// imageLoader.displayImage(ConstantValue.DownIMage+liveDetailList.get(position
			// % liveDetailList.size()).getFileUrl()+ConstantValue.Type,
			// iv_background, list_options);

			// ImageLoader.getInstance().loadImage(ConstantValue.DownIMage+liveDetailList.get(position
			// %
			// liveDetailList.size()).getFileUrl()+ConstantValue.Type,list_options,
			// new ImageLoadingListener() {
			// @Override
			// public void onLoadingStarted(String arg0, View arg1) {
			// iv_background.setImageResource(R.drawable.default_image);
			// }
			//
			// @Override
			// public void onLoadingFailed(String arg0, View arg1, FailReason
			// arg2) {
			// // Log.e(this.getClass().getName(),"failed");
			// iv_background.setImageResource(R.drawable.default_image);
			// }
			//
			// @Override
			// public void onLoadingComplete(String arg0, View arg1, Bitmap
			// arg2) {
			// // Log.e(this.getClass().getName(),
			// arg2.getByteCount()/1024+"==load=="+arg0+"=="+arg1);
			// iv_background.setImageBitmap(arg2);
			//
			// }
			//
			// @Override
			// public void onLoadingCancelled(String arg0, View arg1) {
			// }
			// });
			// LApplication.loader.DisplayImage(ConstantValue.DownIMage+liveDetailList.get(position
			// %
			// liveDetailList.size()).getFileUrl()+ConstantValue.Type,iv_background);
			// Log.e("urlurl                 urlurl",
			// "-----------"+(trynumber++)+"+++++++++++++++"+position+"size---------"+liveDetailList.size());
			tv_title.setText(liveDetailList.get(
					cardView.topPos % liveDetailList.size()).getFileName());
			tv_next.setText(liveDetailList.get(
					cardView.topPos % liveDetailList.size()).getFileSource());
			tv_detail.setText(liveDetailList.get(
					cardView.topPos % liveDetailList.size()).getFileProfile());

			return convertView;
		}

	}

	private DisplayImageOptions list_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.b240_160) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.b240_160)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.b240_160) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheOnDisc(true).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.displayer(new FadeInBitmapDisplayer(500)).build();// 构建完成
	/**
	 * 蓝队logo
	 */
	private ImageView blueLogo;
	/**
	 * 红队logo
	 */
	private ImageView redLogo;
	/**
	 * 蓝队名称
	 */
	private TextView blueName;
	/**
	 * 红队名称
	 */
	private TextView redName;
	/**
	 * 蓝队支持人数
	 */
	private TextView agreeBlueNume;
	/**
	 * 红队支持人数
	 */
	private TextView agreeRedNume;
	/**
	 * 设置进度
	 */
	private SpringProgressView proGressView;
	private RelativeLayout rl_sprots;
	/**
	 * 改活动是否已结束，如果结束 不显示互动
	 */
	private String end;
	/**
	 * 球赛是否支持过 0未支持；1已支持
	 */
	private String hasSupported;
	/**
	 * 球赛是否尖叫过 0：表示没有尖叫过1：表示已经尖叫过
	 */
	private String hasScreamed;
	/**
	 * 支持蓝队 的图标
	 */
	private ImageView ivArgreeBlue;
	/**
	 * 支持红队的图标
	 */
	private ImageView ivArgreeRed;
	/**
	 * 蓝队图标
	 */
	private ImageView mBlueLogo;
	/**
	 * 红队图标
	 */
	private ImageView mRedLogo;
	/**
	 * 蓝队支持人数
	 */
	private TextView mBlueNum;
	/**
	 * 红队支持人数
	 */
	private TextView mRedNum;
	/**
	 * 进度条
	 */
	private SpringProgressView mProjress;
	/**
	 * 红队支持人数
	 */
	private int redCount;
	/**
	 * 蓝队支持人数
	 */
	private int blueCount;
	/**
	 * 当前活动类型 0 是篮球比赛 2是足球比赛  1是音乐会
	 */
	private String camptype;
	/**
	 * 值为1 时 支持视频全景
	 */
	private String supportFullView;
	/**
	 * 如果是回顾进入的 则是当前视频缩略图
	 */
	private String videoImageUrl;
	/**
	 * 值为1乐视云使用 乐视云id ，其他为播放地址
	 * 
	 */
	private String fileSource;
	/**
	 * 点播视频缩略图
	 */
	private ImageView shortImage;
	/**
	 * 点播视频开关
	 */
	private ImageView startImage;
	private RelativeLayout videoContainer;

	/**
	 * 弹出界面
	 */
	private void setDialog() {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.Theme_Light_Dialo);
			// 获得dialog的window窗口
			Window window = dialog.getWindow();
			// 设置dialog在屏幕底部
			window.setGravity(Gravity.CENTER);
			// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
			// window.setWindowAnimations(R.style.dialogStyle);
			int lor = DensityUtil.dip2px(getApplicationContext(), 20);
			int top = DensityUtil.dip2px(getApplicationContext(), 15);
			int bot = DensityUtil.dip2px(getApplicationContext(), 5);
			// 获得window窗口的属性
			android.view.WindowManager.LayoutParams lp = window.getAttributes();
			// 设置窗口宽度为充满全屏
			// lp.width = DensityUtil.dip2px(RecordActivity.this, 300);
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			// 设置窗口高度为包裹内容
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			// 将设置好的属性set回去

			window.setAttributes(lp);
			window.getDecorView().setPadding(0, top, 0, bot);
			// 将自定义布局加载到dialog上
			dialog.setContentView(dialogView);
			dialog.setCanceledOnTouchOutside(false);
//			dialog.setCancelable(false);
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK){
						dialog.dismiss();
						LiveDetialActivity.this.finish();
					}
				return false;
			}});
		}
		if (!dialog.isShowing())
			dialog.show();
	}

	private void initDialogView() {
		dialogView = LayoutInflater.from(this).inflate(
				R.layout.dialog_livedetial_layout, null);
		ivArgreeBlue = (ImageView) dialogView.findViewById(R.id.iv_agrre_bule);
		ivArgreeRed = (ImageView) dialogView.findViewById(R.id.iv_agrre_red);
		blueLogo = (ImageView) dialogView.findViewById(R.id.bule_logo);
		redLogo = (ImageView) dialogView.findViewById(R.id.red_logo);
		blueName = (TextView) dialogView.findViewById(R.id.bule_name);
		redName = (TextView) dialogView.findViewById(R.id.red_name);
		agreeBlueNume = (TextView) dialogView
				.findViewById(R.id.agree_bule_nume);
		agreeRedNume = (TextView) dialogView.findViewById(R.id.agree_red_nume);
		proGressView = (SpringProgressView) dialogView
				.findViewById(R.id.sp_progress);
		blueLogo.setOnClickListener(this);
		redLogo.setOnClickListener(this);
	}

	/**
	 * 支持哪个球队
	 */
	private void supprotTem(final int index) {
		TemMessage tem = tems.get(index);
		Map<String, String> params = new HashMap<String, String>();
		String temId = tem.teamId;
		params.put("gameId", tem.id); // 比赛id
		Log.i("wxn", "id:" + tem.id + "teamId:" + temId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SUPPORTTEAM, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = AGREE_SUCCESS;
							msg.arg2 = index;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = AGREE_ERROR;
							msg.arg2 = data.getNetResultCode();
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 如果是比赛 获取 两支球队信息
	 */
	private void getTemSMessages() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id); // 活动id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_TEM_MESSAGE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn",
								data.getNetResultCode() + "球队信息："
										+ data.getObject());
						if (handler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.obj = data.getObject();
							msg.arg1 = SUCCESS_TEMMESSAGE;
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(GETFILER);
						}
					}
				}, false, false);

	}

	/**
	 * 解析 球队信息
	 * 
	 * @param data
	 */
	private void analyseTemMessage(String data) {
		if (TextUtils.isEmpty(data)) {
			return;
		}
		try {
			JSONArray array = new JSONArray(data);
			int count = array.length();
			tems = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				TemMessage bean = new TemMessage();
				JSONObject objs = array.getJSONObject(i);
				// 设置比赛id
				if (objs.has("id")) {
					bean.id = (objs.getString("id"));
				}

				// 设置支持人数
				if (objs.has("supportCount")) {
					bean.supportCount = (objs.getString("supportCount"));
				}

				// 设置是否支持过
				if (objs.has("hasSupported")) {
					String spoort = objs.getString("hasSupported");
					bean.hasSupported = spoort;
					if (!TextUtils.isEmpty(GlobalParams.USER_ID)&&"1".equals(spoort)) {
						agreeTemIndex = i;
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				}

				if (objs.has("team")) {
					String team = objs.getString("team");
					JSONObject teams = new JSONObject(team);
					// 设置盾牌
					if (teams.has("backgroundimg")) {
						bean.backgrounding = (teams.getString("backgroundimg"));
					}
					// 设置支持图标
					if (teams.has("support_img")) {
						bean.supportImg = (teams.getString("support_img"));
					}
					// 设置该队logourl
					if (teams.has("url")) {
						bean.logoUrl = (teams.getString("url"));
					}
					// 设置该队id
					if (teams.has("id")) {
						bean.teamId = (teams.getString("id"));
					}
					// 设置该队颜色
					if (teams.has("team_color")) {
						bean.temColor = (teams.getString("team_color"));
					}
					// 设置该队队名
					if (teams.has("teamname")) {
						bean.teamName = (teams.getString("teamname"));
					}
				}
				tems.add(bean);
			}
			if (tems == null || tems.size() < 2) {
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			initDialgoViewData();
			Log.i("wxn", ""+agreeTemIndex +" end :"+end);
			if (agreeTemIndex == -1 && TextUtils.isEmpty(end))
				setDialog();
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("wxn", "yic "+e.getMessage());
		}

	}

	private void initDialgoViewData() {
		rl_sprots.setVisibility(View.VISIBLE);
		TemMessage blueTem = tems.get(0);
		TemMessage redTem = tems.get(1);
		Log.i("wxn", "blueTem :"+blueTem.supportCount);
		Log.i("wxn", "redTem :"+redTem.supportCount);
		blueName.setText(blueTem.teamName);

		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + blueTem.logoUrl
						+ ConstantValue.IMAGE_END, blueLogo, list_options);
		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + blueTem.logoUrl
						+ ConstantValue.IMAGE_END, mBlueLogo, list_options);
		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + blueTem.supportImg
						+ ConstantValue.IMAGE_END, ivArgreeBlue, list_options);

		redName.setText(redTem.teamName);

		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + redTem.logoUrl
						+ ConstantValue.IMAGE_END, redLogo, list_options);
		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + redTem.logoUrl
						+ ConstantValue.IMAGE_END, mRedLogo, list_options);
		imageLoader.displayImage(
				ConstantValue.BASE_IMAGE_URL + redTem.supportImg
						+ ConstantValue.IMAGE_END, ivArgreeRed, list_options);
		redCount = TextUtils.isEmpty(redTem.supportCount) ? 0 : Integer
				.parseInt(redTem.supportCount);
		blueCount = TextUtils.isEmpty(blueTem.supportCount) ? 0 : Integer
				.parseInt(blueTem.supportCount);
		mRedNum.setText(redCount + "");
		mBlueNum.setText(blueCount + "");
		agreeBlueNume.setText(blueCount + "");
		agreeRedNume.setText(redCount + "");

		int blueColor = Color.parseColor("#" + blueTem.temColor);
		int redColor = Color.parseColor("#" + redTem.temColor);
		agreeBlueNume.setTextColor(blueColor);
		agreeRedNume.setTextColor(redColor);
		mBlueNum.setBackgroundColor(blueColor);
		mRedNum.setBackgroundColor(redColor);
		int[] colors = new int[] { blueColor, redColor };
		proGressView.setColors(colors);
		proGressView.setMaxCount(redCount + blueCount);
		proGressView.setCurrentCount(blueCount);

		mProjress.setColors(colors);
		mProjress.setMaxCount(redCount + blueCount);
		mProjress.setCurrentCount(blueCount);
		Log.i("wxn", "fragment:"+discussfg+"   tems:"+tems);
		if (discussfg != null && TextUtils.isEmpty(end)) {
			discussfg.setLiveDetialActivity(tems, agreeTemIndex);
		}

	}

	private void analyseData(String backdata) {
		if (backdata != null && backdata.length() != 0
				&& !TextUtils.isEmpty(backdata)) {
			// 调用方法，解析数据
			// LiveDetailHighlightBean
			ArrayList<HeightLightBean> lists = new ArrayList<>();

			liveDetailList = (ArrayList<HeightLightBean>) JsonUtil
					.parseJsonToList(backdata,
							new TypeToken<List<HeightLightBean>>() {
							}.getType());
			for (int i = 0; i < liveDetailList.size(); i++) {
				String type = liveDetailList.get(i).getFileType();
				if ("1".equals(type) || "0".equals(type)) {
					lists.add(liveDetailList.get(i));
					if ("0".equals(type)) {
						picturelList.add(liveDetailList.get(i));
					}
				}
			}
			liveDetailList = lists;
			if (liveDetailList.size() > 0) {
				// for(int i=0;i<liveDetailList.size();i++){
				// if(!"1".equals(liveDetailList.get(i).getFileType())){
				// picturelList.add(liveDetailList.get(i));
				// }
				// }
				tv_title.setVisibility(View.VISIBLE);
				// tv_next.setVisibility(View.VISIBLE);
				tv_detail.setVisibility(View.VISIBLE);

				cardView.setVisibility(View.VISIBLE);
				cardView.setmMaxCardSize(4);
				adapter = new MyCardView(getApplicationContext());
				adapter.setData(liveDetailList);
				// adapter.setData(initData());
				cardView.setAdapter(adapter);

				cardView.setCardClickListener(new CardClickListener() {
					@Override
					public void onClick(View view, int pos) {
						android.widget.Toast.makeText(getApplicationContext(),
								pos + "", 1).show();

					}
				});
			}

		}

	}

	/**
	 * 支持某个球队后 取消dialog展示
	 * 
	 * @param index
	 */
	private void agreeTem(int index) {
		agreeTemIndex = index;
		if (discussfg != null) {
			discussfg.setAgrreIndex(agreeTemIndex);
		}
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
		int count;
		mProjress.setMaxCount(redCount + blueCount + 1);
		if (index == 0) {
			// 蓝队
			count = blueCount + 1;
			mBlueNum.setText(count + "");
			mProjress.setCurrentCount(count);
		} else {
			// 红队
			count = redCount + 1;
			mRedNum.setText(count + "");
			mProjress.setCurrentCount(blueCount);
		}
		tems.get(index).supportCount = (count + "");
	}

	/**
	 * 是否尖叫过
	 * 
	 * @return
	 */
	public boolean isScreamed() {
		return "1".equals(hasScreamed);
	}

	/**
	 * 获取球队信息
	 */
	public ArrayList<TemMessage> getTemsMessage() {
		return tems;
	}

	/**
	 * 获取支持的是哪个球队
	 * 
	 * @return
	 */
	public int getAgreeTem() {
		return agreeTemIndex;
	}

	/**
	 * 活动标签
	 * 
	 * @return
	 */
	public String getLayble() {
		return lable;
	}

	/**
	 * 设置尖叫过
	 */
	public void sethasScreamed() {
		this.hasScreamed = "1";
	}

	/**
	 * 获取活动id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 * 获取聊天室id roomId
	 * @return
	 */
	public String getRoomId(){
		return roomId;
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LiveDetialActivity.this,
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
		exitDialog.setRemindMessage("登录之后才能支持哦~");
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
                	if(startImage!=null)
                		startImage.setVisibility(View.GONE);
                	if(shortImage!=null)
                		shortImage.setVisibility(View.GONE);
                    videoView.onStart();
                }
                break;
            case PlayerEvent.PLAY_COMPLETION:
            	if(TextUtils.isEmpty(end)){
            		thanks_tv.setVisibility(View.VISIBLE);
            	}else{
            		LApplication.loader.DisplayImage(
   						 ConstantValue.BASE_IMAGE_URL
   						 + videoImageUrl +
   						 ConstantValue.IMAGE_END,
   						 shortImage);
            		startImage.setImageResource(R.drawable.video_receiver);
            		startImage.setVisibility(View.VISIBLE);
        			shortImage.setVisibility(View.VISIBLE);
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
    
    /**
     * 根据活动id 获取轰动详情接口
     */
    private void getActiviteMessage(){
    	Map<String, String> params = new HashMap<String, String>();
		params.put("id", id); // 活动id
		params.put("type", "1"); // 类型（0：未开始，1：进行中
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_MESSAGE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn",
								data.getNetResultCode() + "活动详情信息："
										+ data.getObject());
						if (handler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.obj = data.getObject();
							msg.arg1 = SUCCESS_ACTIVE_MESSAGE;
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(GETFILER);
						}
					}
				}, false, false);
    }
    /**
     * 解析活动详情数据
     * @param data
     */
    private void analyseActiveMessage(String data){
    	if(TextUtils.isEmpty(data)){
    		return;
    	}
    	try{
    		JSONObject objs = new JSONObject(data);
    		if(objs.has("label")){
    			lable = objs.getString("label");
    		}
    		if(objs.has("resourceId")){
    			videoId = objs.getString("resourceId");
    		}
    		if(objs.has("resourceType")){
    			resourceType = objs.getString("resourceType");
    		}
    		if(objs.has("roomId")){
    			roomId = objs.getString("roomId");
    		}
    		if(objs.has("hasScreamed")){
    			hasScreamed = objs.getString("hasScreamed");
    		}
    		if(objs.has("hasSupported")){
    			hasSupported = objs.getString("hasSupported");
    		}
    		if(objs.has("camptype")){
    			camptype = objs.getString("camptype");
    		}
    	}catch(Exception e){
    		
    	}
		if("null".equals(videoId)){
			videoId = "";
		}
    }
    
    private void getVideoMessage(){
    	Map<String, String> params = new HashMap<String, String>();
		params.put("id", id); // 活动id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.VIDEO_MESSAGE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn",
								data.getNetResultCode() + "视频详情信息："
										+ data.getObject());
						if (handler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.obj = data.getObject();
							msg.arg1 = SUCCESS_VIDEO_MESSAGE;
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(GETFILER);
						}
					}
				}, false, false);
    }
    
    private void analyseVideoMessage(String date){
    	if(TextUtils.isEmpty(date)){
    		return;
    	}
    	try{
    		JSONObject dates = new JSONObject(date);
    		if(dates.has("supportFullView")){
    			supportFullView = dates.getString("supportFullView");
    		}
    		if(dates.has("fileSource")){
    			fileSource = dates.getString("fileSource");
    		}
    		if(dates.has("resourceId")){
    			videoId = dates.getString("resourceId");
    		}
    		initviewDate();
    	}catch(Exception e){}
    }
    
    
	/**
	 * 已结束 获取集锦中第一个视频数据
	 * 
	 * @param context
	 */
	private void getdataFrom() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id);
		params.put("fileClass", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.HIGHT_LEIGHT, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = FIRST_VIDEO;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
	}
	
	private void analyseFirstVideo(String date){
		String fileType = "";
		if(TextUtils.isEmpty(date))
			return;
		try{
			JSONArray datesArray = new JSONArray(date);
			for(int i =0;i<datesArray.length();i++){
				JSONObject dates = datesArray.getJSONObject(i);
				Log.i("wxn", "回顾收条数据："+dates);
				if(dates.has("fileType"))
					fileType = dates.getString("fileType");
				if(!"1".equals(fileType)){
					continue;
				}
				if(dates.has("resourceId")){
					videoId = dates.getString("resourceId");
				}
				if(dates.has("fileSource")){
					fileSource = dates.getString("fileSource");
				}
				if(dates.has("supportFullView")){
					supportFullView = dates.getString("supportFullView");
				}
				if(dates.has("videoImageUrl")){
					videoImageUrl = dates.getString("videoImageUrl");
				}
				break;
			}
			initviewDate();
		}catch(Exception e){
			
		}
	}
	
    /**
     * 弹幕开启关闭
     */
    public class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			Log.i("wxn", "接收到广播。。。");
			if (intent.getAction().equals(VideoPlayerActivity.DanmAction)) {
				String status = intent.getStringExtra("status");
				if("1".equals(status)){
					openDanmu();
				}else{
					closeDanmu();
				}
			}
		}
	};
	
	/**
	 * 回顾切换 顶部点播视频
	 * @param bean
	 */
	public void changeVideo(HeightLightBean bean){
		videoId = bean.getResourceId();
		videoImageUrl = bean.getVideoImageUrl();
		fileSource = bean.getFileSource();
		videoView.stopAndRelease();
		Log.i("wxn", ""+videoId+" .."+fileSource+" " );
		createBundleLive("1".equals(fileSource),videoId);
		if(!supportFullView.equals(bean.getSupportFullView())){
			videoContainer.removeView((View)videoView);
			supportFullView = bean.getSupportFullView();
			videoView = "1".equals(supportFullView)?new UIPanoVodVideoView(this):new UIVodVideoView(this);
			videoView.setVideoViewListener(mVideoViewListener);
	        videoContainer.addView((View) videoView, VideoLayoutParams.computeContainerSize(this, 16, 9));
		}
		if ("1".equals(fileSource)) {
			 videoView.setDataSource(bundle);
        } else {
        	 videoView.setDataSource(videoId);
        }
	}
	

}
