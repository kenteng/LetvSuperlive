package com.lesports.stadium.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LiveDetialActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.PowerActivity;
import com.lesports.stadium.activity.RechargeintegralActivity;
import com.lesports.stadium.activity.RecordActivity;
import com.lesports.stadium.activity.ShakeDetailActivity;
import com.lesports.stadium.adapter.FaceAdapter;
import com.lesports.stadium.adapter.FacePageAdeapter;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.bean.DiscussBean;
import com.lesports.stadium.bean.GiftBean;
import com.lesports.stadium.bean.TemMessage;
import com.lesports.stadium.bean.WenAnBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.FaceUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CircleImageView;
import com.lesports.stadium.view.CirclePageIndicator;
import com.lesports.stadium.view.ComposerLayout;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyLayout;
import com.lesports.stadium.view.MyLinearLayout;
import com.lesports.stadium.view.MyViewPager;
import com.lesports.stadium.view.flower.FllowerAnimation;
import com.lesports.stadium.view.heartview.HeartView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: AboutUsActivity
 * 
 * @Desc : 讨论 界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:29:57
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
public class DiscussFragment extends BaseV4Fragment {

	/**
	 * 评论输入框
	 */
	private EditText mDiscussInputEt;
	/**
	 * 发送评论
	 */
	private TextView mDiscussSendBt;
	/**
	 * 显示当前用户能量总值
	 */
	private TextView tvPower;

	/**
	 * 存储请求到的评论
	 */
	private LinkedList<DiscussBean> mDiscussList = new LinkedList<DiscussBean>();
	/**
	 * 发表评论时数据封装对象
	 */
	private DiscussBean tempDiscussBean = new DiscussBean();
	/**
	 * 表情对应的字符串数组
	 */
	private List<String> mFaceMapKeys;
	/**
	 * 存储view集合
	 */
	private List<View> lv;
	/**
	 * 表情适配器
	 */
	private FacePageAdeapter faceadapter;
	/**
	 * faceviewPager 对象
	 */
	private ViewPager mFaceViewPager;
	/**
	 * 当前ViewPager 界面
	 */
	private int mCurrentPage = 0;
	private CirclePageIndicator indicator;
	/**
	 * 软键盘管理类
	 */
	private InputMethodManager inputManager;
	/**
	 * 头像加载器
	 */
	private ImageLoader imageLoader = ImageLoader.getInstance();

	/**
	 * 定时器
	 */
	private Timer mTimer = new Timer();
	/**
	 * 提示送礼定时器
	 */
	private Timer sendGiftTimer = new Timer();
	/**
	 * 提示中奖 定时器
	 */
	private Timer yaoYaoTimer = new Timer();
	/**
	 * 我要尖叫 定时器
	 */
	private Timer jianJiaoTimer = new Timer();
	/**
	 * 表情是否显示
	 */
	private boolean isShowFace = false;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 当前触摸点相对于屏幕的坐标
	 */
	private int mCurrentInScreenX;
	private int mCurrentInScreenY;
	/**
	 * 当前界面的宽
	 */
	private int scrrenWidth;
	/**
	 * 当前界面的高
	 */
	private int scrrenHeight;

	private Long mCurrentClickTime;
	/**
	 * 长按与点击的分界点
	 */
	private static final long LONG_PRESS_TIME = 580;
	/**
	 * 将size2 转成px后的值
	 */
	private int size;
	private int size2 = 100;
	private OnTouchListener clickit;
	/**
	 * 飘花，票吻，票心 动画对象
	 */
	private FllowerAnimation fllowerAnimation, kissAnimation, heartAnimation,
			embraceAnimation, flowAnimation;
	private boolean resume = false;
	/**
	 * 是否显示自定义表情图片
	 */
	private boolean showFaceAndinputTag = false;
	/**
	 * RelativeLayout.LayoutParams 对象
	 */
	private android.widget.RelativeLayout.LayoutParams params;
	/**
	 * 存储飘吻 对象
	 */
	private ArrayList<FllowerAnimation> kissList = new ArrayList<>();
	/**
	 * 存储飘心 对象
	 */
	private ArrayList<FllowerAnimation> heartList = new ArrayList<>();
	/**
	 * 存储拥抱 对象
	 */
	private ArrayList<FllowerAnimation> embraceList = new ArrayList<>();
	/**
	 * 存储飘花 对象
	 */
	private ArrayList<FllowerAnimation> flowList = new ArrayList<>();
	/**
	 * 计算当前 吻对象 创建 数量 ，如果超过5个就去集合中获取
	 */
	int kisIndex = -1;
	/**
	 * 计算当前 心对象 创建 数量 ，如果超过5个就去集合中获取
	 */
	int heaIndex = -1;
	/**
	 * 计算当前 拥抱 对象 创建 数量 ，如果超过5个就去集合中获取
	 */
	int embIndex = -1;
	/**
	 * 计算当前 拥抱 对象 创建 数量 ，如果超过5个就去集合中获取
	 */
	int floIndex = -1;
	/*
	 * 显示淘宝菜单
	 */
	private final int SHOW_TAOMAO_MENU = 2;
	/*
	 * 打开淘宝菜单
	 */
	private final int OPEN_TAOMAO_MENU = 0;
	/*
	 * 评论成功
	 */
	private final int DISSCUSS_SUCCESS = 100;
	/*
	 * 成功获取评论内容
	 */
	private final int GET_DISSCUSS = 101;
	/**
	 * 长按期间显示的界面
	 */
	private final int LONG_PRESSING = 202;
	/**
	 * 动画稍后执行
	 */
	private final int START_ANIMATION = 303;
	/**
	 * 获取用户积分成功
	 */
	private final int SUCCESS_DATE_JIFEN = 606;
	/**
	 * 获取用户积分失败
	 */
	private final int FILUE_DATE_JIFEN = 600;
	/**
	 * 点击时有dis_ll 发送
	 */
	private final int DIS_LL_SEND = 999;
	/**
	 * 点击时有dis_ll 发送
	 */
	private final int SED_SUCCESS = 888;
	/**
	 * 是否是长按期间的标志
	 */
	private boolean isLongPress = true;

	private final int SUCCESS_DATE_WENAN = 6666;
	/**
	 * 获取礼物成功
	 */
	private final int SUCCESS_DATE_GIFT = 88888;
	/**
	 * 根据屏幕高度设置 滚动弹幕最大显示时间
	 */
	private final int SHOW_DISSCUSS_TIMER = 8888;
	/**
	 * 获取能量总和成功
	 */
	private final int SUCCESS_SUMENGER_GIFT = 88;
	/**
	 * 获取历史纪录成功
	 */
	private final int CHAT_HISTERY = 66;
	/**
	 * 网络异常
	 */
	private final int NO_NET = 99;
	/**
	 * composer_button对象
	 */
	private Drawable composer_button;
	private int composerbtgetMinimumHeight;
	/**
	 * 从网络获取讨论数据 开关
	 */
	private boolean timerTag = false;
	/**
	 * 要显示淘宝菜单朝向
	 */
	private byte orientation;
	/**
	 * imagview 对象
	 */
	private ImageView faceview;
	/**
	 * 自定义表情布局
	 */
	private RelativeLayout chat_more;
	/**
	 * 自定义LinearLayout 不处理事件
	 */
	private MyLinearLayout dis_ll;
	/**
	 * 线性布局
	 */
	private LinearLayout face_ll, selecView;
	/**
	 * 能量值布局
	 */
	private LinearLayout powerLayout;
	/**
	 * 自定义相对布局
	 */
	private MyLayout d2s_layout;
	/**
	 * 线性布局
	 */
	private LinearLayout discuss_input_rl;
	/**
	 * 自定义控件 显示 淘宝菜单
	 */
	private ComposerLayout clayout;
	/**
	 * 距离屏幕顶部距离
	 */
	private int scrrenTop;
	/**
	 * 时间转换格式
	 */
	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd H:mm:ss");
	/**
	 * 当前获取的id
	 */
	private String id;
	/**
	 * viewPager 对象
	 */
	private MyViewPager vp_pager;
	/**
	 * 当前活动标签
	 */
	private String lable;
	/**
	 * 当前聊天室roomid
	 */
	private String roomId;
	/**
	 * 每个礼物需要展示的积分
	 */
	private String[] giftsJifen;
	/**
	 * 仿淘宝菜单 半径
	 */
	private int radius;
	/**
	 * 条目显示的时间
	 */
	private int showTime2item = 5000;
	/**
	 * 默认点击x轴位置
	 */
	private float dis_ll_child_x = -400000;
	/**
	 * 默认点击y轴位置
	 */
	private float dis_ll_child_y = -400000;
	/**
	 * 宽度
	 */
	private int d2s_layout_hegith = 0;
	/**
	 * 文案数据封装
	 */
	private ArrayList<WenAnBean> wenAnList;
	/**
	 * 送礼数据对象集合
	 */
	private ArrayList<GiftBean> giftsLists;
	/**
	 * 封装从网络获取的送礼图片
	 */
	private ArrayList<Bitmap> bitms;
	/**
	 * 支持哪个球队
	 */
	private int agreeTemIndex;
	/**
	 * 球队信息
	 */
	private ArrayList<TemMessage> tems;

	/**
	 * 处理数据Handler
	 */
	private Handler discussHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 打开淘宝菜单
			case OPEN_TAOMAO_MENU:
				openTaobaoMenu();
				break;
			// 显示淘宝菜单
			case SHOW_TAOMAO_MENU:
				showTaobaoMenu();
				break;
			// 成功获取评论内容
			case GET_DISSCUSS:
				analsyeDisscussForNet((String) msg.obj);
				break;
			// 评论成功
			case DISSCUSS_SUCCESS:
				addPower("1","");
				sendDisscussSuccess();
				break;
			// 长按期间是否要显示淘宝菜单
			case LONG_PRESSING:
				checkShowTaobaoMenu();
				break;
			// 动画稍后执行
			case START_ANIMATION:
				setTimerTag(false);
				break;
			case SUCCESS_DATE_JIFEN:
				// 获取用户积分成功
				analyzeDate((String) msg.obj);
				break;
			case FILUE_DATE_JIFEN:
				break;
			// 根据屏幕高度设置 滚动弹幕最大显示时间
			case SHOW_DISSCUSS_TIMER:
				setShowTimerForDisscuss();
				break;
			case DIS_LL_SEND:
				clickShowHeart(msg);
				break;
			case SED_SUCCESS:
				anilySendGift(msg.obj);
				break;
			case SUCCESS_DATE_WENAN:
				anilyWenAn(msg.obj);
				break;
			case SUCCESS_DATE_GIFT:
				anilyGiftData(msg.obj);
				break;
			case NO_NET:
				// Toast.makeText(getActivity(), "网络异常",
				// Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS_SUMENGER_GIFT:
				analySumenger(msg.obj);
				break;
			case CHAT_HISTERY:
				analyseChatHistery((String) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 构造方法 传递当前活动id 和表情
	 * 
	 * @param ids
	 *            活动的id
	 * @param vp_pager
	 *            ViewPager对象
	 * @param lable
	 *            当前活动标签
	 */
	public DiscussFragment(String ids, MyViewPager vp_pager, String lable,String roomid) {
		this.id = ids;
		this.vp_pager = vp_pager;
		this.lable = lable;
		this.roomId = roomid;
	}

	/**
	 * 无惨构成方法
	 */
	public DiscussFragment() {
		super();
	}

	/**
	 * 初始化view
	 */
	@SuppressWarnings("deprecation")
	@Override
	public View initView(LayoutInflater inflater) {
		View discussView = inflater.inflate(R.layout.fragment_discuss2, null);
		discuss_input_rl = (LinearLayout) discussView
				.findViewById(R.id.discuss_input_rl);
		d2s_layout = (MyLayout) discussView.findViewById(R.id.d2s_layout);
		indicator = (CirclePageIndicator) discussView
				.findViewById(R.id.indicator);
		faceview = (ImageView) discussView.findViewById(R.id.faceview);
		chat_more = (RelativeLayout) discussView.findViewById(R.id.chat_more);
		dis_ll = (MyLinearLayout) discussView.findViewById(R.id.dis_ll);
		face_ll = (LinearLayout) discussView.findViewById(R.id.face_ll);
		selecView = (LinearLayout) discussView.findViewById(R.id.selecView);
		powerLayout = (LinearLayout) discussView.findViewById(R.id.ll_power);
		mFaceViewPager = (ViewPager) discussView.findViewById(R.id.face_pager);
		mDiscussInputEt = (EditText) discussView
				.findViewById(R.id.discuss_input_et);
		// 清楚评论焦点
		mDiscussInputEt.clearFocus();
		// 发送按钮
		mDiscussSendBt = (TextView) discussView.findViewById(R.id.sendview);
		// 能量总值按钮
		tvPower = (TextView) discussView.findViewById(R.id.tv_power);
		clayout = (ComposerLayout) discussView.findViewById(R.id.compslay);
		initFace();
		size = DensityUtil.dip2px(getActivity(), size2);
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		return discussView;
	}

	@Override
	public void onPause() {
		setTimerTag(true);
		super.onPause();
	}

	@Override
	public void onResume() {
		setTimerTag(false);
		if (!resume) {
			resume = true;
			popDiscuss();
		}
		if (GlobalParams.USER_INTEGRAL == 0)
			getUserJiFen();
		if (wenAnList == null)
			getSuggestion();
		super.onResume();
		discussHandler.sendEmptyMessageDelayed(8888, 160);
		if (!GlobalParams.IS_SPORT){
			getPower();
			getGiftMessage();
		}
	}

	private byte showLocation(int argx, int argh) {
		scrrenTop = GlobalParams.WIN_HIGTH - scrrenHeight
				- discuss_input_rl.getHeight();
		int top = 0;
		int left = 0;
		int right = 0;
		int bottom = 0;
		if (argx > size + composerbtgetMinimumHeight) {
			if (argh > scrrenTop + size + composerbtgetMinimumHeight) {
				// 右下角
				right = scrrenWidth - argx - composerbtgetMinimumHeight / 2;
				bottom = GlobalParams.WIN_HIGTH - discuss_input_rl.getHeight()
						- argh - composerbtgetMinimumHeight / 2;
				if (right < 0)
					right = 0;
				if (bottom < 0)
					bottom = 0;
				params.setMargins(0, 0, right, bottom);
				return ComposerLayout.RIGHTBOTTOM;
			} else {
				// 右上角
				top = argh - scrrenTop - composerbtgetMinimumHeight / 2;
				right = scrrenWidth - argx - composerbtgetMinimumHeight / 2;
				if (top < 0)
					top = 0;
				if (right < 0)
					right = 0;
				params.setMargins(0, top, right, 0);
				return ComposerLayout.RIGHTTOP;
			}
		} else {
			if (argh > scrrenTop + size + composerbtgetMinimumHeight) {
				// 左下角
				left = argx - composer_button.getMinimumWidth() / 2;
				bottom = GlobalParams.WIN_HIGTH - discuss_input_rl.getHeight()
						- argh - composerbtgetMinimumHeight / 2;
				if (left < 0)
					left = 0;
				if (bottom < 0)
					bottom = 0;
				params.setMargins(left, 0, 0, bottom);
				return ComposerLayout.LEFTBOTTOM;
			} else {
				// 左上角
				left = argx - composer_button.getMinimumWidth() / 2;
				top = argh - scrrenTop - composerbtgetMinimumHeight / 2;
				if (left < 0)
					left = 0;
				if (top < 0)
					top = 0;
				params.setMargins(left, top, 0, 0);
				return ComposerLayout.LEFTTOP;
			}
		}
	}

	private boolean isgetDisscuss = true;
	/**
	 * 显示送礼的中间图标
	 */
	private String centerImage;
	/**
	 * 能量总值
	 */
	private String energy;

	/**
	 * 定时弹出评论内容
	 */
	private void popDiscuss() {
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (mDiscussList != null && mDiscussList.size() < 1) {
					if (isgetDisscuss) {
						isgetDisscuss = false;
						requestAllDiscuss();
					}
				}
				if (mDiscussList != null && mDiscussList.size() == 0)
					try {
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				d2s_layout.post(new Runnable() {
					@Override
					public void run() {
						synchronized (DiscussFragment.class) {
							if (timerTag) {
								return;
							}
							if (mDiscussList == null
									|| mDiscussList.size() == 0)
								return;
							view = View
									.inflate(
											GlobalParams.context,
											R.layout.activity_listview_delete_left_chain_cell,
											null);
							TextView mDiscussContentTv = (TextView) view
									.findViewById(R.id.cell_name_textview);
							CircleImageView mUserImg = (CircleImageView) view
									.findViewById(R.id.discuss_item_userheadimg);
							DiscussBean bean = mDiscussList.removeFirst();
							view.setTag(bean);
							if (TextUtils.isEmpty(bean.getContent())) {
								return;
							}
							if (bean.getUserImage() != null) {
								if (bean.getUserImage().startsWith("http:")) {
									imageLoader.displayImage(
											bean.getUserImage(),
											mUserImg,
											MyFragment
													.setDefaultImageOptions(R.drawable.default_header));
								} else {
									imageLoader.displayImage(
											ConstantValue.BASE_IMAGE_URL
													+ bean.getUserImage()
													+ ConstantValue.IMAGE_END,
											mUserImg,
											MyFragment
													.setDefaultImageOptions(R.drawable.default_header));
								}
							} else {
								mUserImg.setImageResource(R.drawable.default_header);
							}
							CharSequence content = CommonUtils
									.convertNormalStringToSpannableString(
											getActivity(), bean.getContent(),
											true);
							mDiscussContentTv.setTextColor(0xffffffff);
							mDiscussContentTv.setText(content);
							mDiscussContentTv
									.setMovementMethod(LinkMovementMethod
											.getInstance());
							addview(view);
						}
					}
				});
			}
		}, 200, 950);

	}

	public class ViewHolder {
		public TextView contentText;
		public ImageView headView;
	}

	/**
	 * 设置响应事件
	 */
	@Override
	public void initListener() {
		powerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPowerActivity();
			}
		});

		clickit = new OnTouchListener() {
			int eventx = 0;
			int eventy = 0;
			int typeGift = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				String gfs = "";
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					eventx = (int) event.getX();
					eventy = (int) event.getY();
					// if (fllowerAnimation != null) {
					// d2s_layout.removeView(fllowerAnimation);
					// fllowerAnimation = null;
					// }
					if (v.getId() == 100 + 3) {
						typeGift = 3;
						clayout.clickItem(3, giftsLists.get(3)
								.getImage_selected());
						kisIndex++;
						if (kissList.size() < 5) {
							kissAnimation = new FllowerAnimation(getActivity(),
									bitms.get(3));
							kissList.add(kissAnimation);
							fllowerAnimation = kissAnimation;
						} else {
							fllowerAnimation = kissList.get(kisIndex % 5);
						}
						gfs = giftsLists.get(3).getIntergal();

					} else if (v.getId() == 100 + 2) {
						clayout.clickItem(2, giftsLists.get(2)
								.getImage_selected());
						typeGift = 2;
						heaIndex++;
						if (heartList.size() < 5) {
							heartAnimation = new FllowerAnimation(
									getActivity(), bitms.get(2));
							heartList.add(heartAnimation);
							fllowerAnimation = heartAnimation;
						} else {
							fllowerAnimation = heartList.get(heaIndex % 5);
						}
						gfs = giftsLists.get(2).getIntergal();

					} else if (v.getId() == 100 + 1) {
						typeGift = 1;
						clayout.clickItem(1, giftsLists.get(1)
								.getImage_selected());
						embIndex++;
						if (embraceList.size() < 5) {
							embraceAnimation = new FllowerAnimation(
									getActivity(), bitms.get(1));
							embraceList.add(embraceAnimation);
							fllowerAnimation = embraceAnimation;
						} else {
							fllowerAnimation = embraceList.get(embIndex % 5);
						}
						gfs = giftsLists.get(1).getIntergal();
					} else if (v.getId() == 100 + 0) {
						typeGift = 0;
						clayout.clickItem(0, giftsLists.get(0)
								.getImage_selected());
						floIndex++;
						if (flowList.size() < 5) {
							flowAnimation = new FllowerAnimation(getActivity(),
									bitms.get(0));
							fllowerAnimation = flowAnimation;
							flowList.add(flowAnimation);
						} else {
							fllowerAnimation = flowList.get(floIndex % 5);
						}
						gfs = giftsLists.get(0).getIntergal();
					}
					break;
				case MotionEvent.ACTION_UP:
					clayout.collapse();
					clayout.reset();
					if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
						clayout.reset();
						createDialog();
						exitDialog.setRemindTitle("温馨提示");
						exitDialog.setCancelTxt("取消");
						exitDialog.setConfirmTxt("立即登录");
						exitDialog.setRemindMessage("登录之后才能送礼哦~");
						exitDialog.show();
						return true;
					}
					if (giftsLists != null && giftsLists.size() > 0) {
						String integal = giftsLists.get(typeGift).getIntergal();
						if (GlobalParams.USER_INTEGRAL < Integer
								.parseInt(integal)) {
							fllowerAnimation = null;
							// 乐东不足
							createChongZhiDialog();
							return true;
						}
					} else {
						return true;
					}

					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT);
					fllowerAnimation.setLayoutParams(params);
					d2s_layout.removeView(fllowerAnimation);
					d2s_layout.addView(fllowerAnimation);
					fllowerAnimation.startAnimation();
					sendGifts(typeGift);
					break;

				default:
					break;
				}
				return true;
			}
		};

		mDiscussSendBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 处理发送事务
				String mDiscussStr = mDiscussInputEt.getText().toString()
						.trim();
				if (TextUtils.isEmpty(mDiscussStr)) {
					Toast.makeText(getActivity(), "请输入内容不能为空", 0).show();
					return;
				}
				if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
					createDialog();
					exitDialog.setRemindTitle("温馨提示");
					exitDialog.setCancelTxt("取消");
					exitDialog.setConfirmTxt("立即登录");
					exitDialog.setRemindMessage("登录之后才能评论哦~");
					exitDialog.show();
					return;
				}
				 if (mDiscussStr.length() > 70) {
					 Toast.makeText(getActivity(), "您发送的内容不能超过70字符",
					 Toast.LENGTH_SHORT).show();
					 return;
				 }
				requestSendDiscuss(mDiscussStr);
//				 sendChatMessage(mDiscussStr);
				inputManager.hideSoftInputFromWindow(getActivity().getWindow()
						.getDecorView().getWindowToken(), 0);
				showFaceAndinputTag = false;
			}
		});

		// 点击笑脸
		faceview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isShowFace) {
					inputManager.hideSoftInputFromWindow(getActivity()
							.getWindow().getDecorView().getWindowToken(), 0);
					chat_more.setVisibility(View.VISIBLE);
					face_ll.setVisibility(View.VISIBLE);
					selecView.setVisibility(View.VISIBLE);
					vp_pager.setnoScroll(true);
					showFaceAndinputTag = true;
					faceview.setImageResource(R.drawable.jianpan);
				} else {
					chat_more.setVisibility(View.GONE);
					selecView.setVisibility(View.GONE);
					vp_pager.setnoScroll(false);
					showFaceAndinputTag = false;
					faceview.setImageResource(R.drawable.fasong_biaoqing);
				}
				isShowFace = !isShowFace;

			}
		});
		mDiscussInputEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					chat_more.setVisibility(View.GONE);
					selecView.setVisibility(View.GONE);
					faceview.setImageResource(R.drawable.fasong_biaoqing);
					isShowFace = false;
					vp_pager.setnoScroll(false);
					showFaceAndinputTag = true;
				}

			}
		});
		mDiscussInputEt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showFaceAndinputTag = true;
				chat_more.setVisibility(View.GONE);
				selecView.setVisibility(View.GONE);
				faceview.setImageResource(R.drawable.fasong_biaoqing);
				vp_pager.setnoScroll(false);
				isShowFace = false;
			}
		});
		mDiscussInputEt.addTextChangedListener(new TextWatcher() {

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
				String content = mDiscussInputEt.getText().toString().trim();
				if (s != null && content.length() > 0) {
					// 发送按钮
					mDiscussSendBt.setVisibility(View.VISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mDiscussSendBt
							.getLayoutParams();
					// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					int top = DensityUtil.dip2px(getActivity(), 10);
					params.setMargins(0, top / 2, top, top / 2);
					params.width = DensityUtil.dip2px(getActivity(), 35);
					mDiscussSendBt.setLayoutParams(params);
					if (content.length() > 70) {
						Toast.makeText(getActivity(), "你输入的文字过多", 0).show();
						mDiscussInputEt.setText(s.subSequence(0, 70));
						mDiscussInputEt.setSelection(70);
					}
				} else {
					mDiscussSendBt.setVisibility(View.INVISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mDiscussSendBt
							.getLayoutParams();
					params.width = 0;
					params.setMargins(0, 0, 0, 0);
					mDiscussSendBt.setLayoutParams(params);
				}

			}
		});

		d2s_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isLongPress = true;
					mCurrentInScreenX = (int) event.getRawX();
					mCurrentInScreenY = (int) event.getRawY();
					mCurrentClickTime = Calendar.getInstance()
							.getTimeInMillis();
					// discussHandler.sendEmptyMessageDelayed(LONG_PRESSING,
					// LONG_PRESS_TIME);
					break;
				case MotionEvent.ACTION_MOVE:
					float y = event.getX();
					if (Math.abs(y - mCurrentInScreenX) > DensityUtil.dip2px(
							getActivity(), 8))
						isLongPress = false;
					break;
				case MotionEvent.ACTION_UP:
					// discussHandler.removeMessages(LONG_PRESSING);
					isLongPress = false;
					if (showFaceAndinputTag) {
						hideInputAndFace();
						showFaceAndinputTag = false;
						return false;
					}
					if (Calendar.getInstance().getTimeInMillis()
							- mCurrentClickTime < LONG_PRESS_TIME) {
						Message msg = new Message();
						msg.what = DIS_LL_SEND;
						msg.arg1 = (int) (event.getX() + 0.5);
						msg.arg2 = (int) (event.getY() + 0.5);
						discussHandler.sendMessage(msg);
						if (clayout != null && clayout.isShow()) {
							clayout.collapse();
							// clayout.removeAllViews();
							return false;
						}
						// float x = event.getX();
						// y = event.getY();
						// for (int i = 0; i < d2s_layout.getChildCount(); i++)
						// {
						// View child = d2s_layout.getChildAt(i);
						// if (child != null
						// && !(child instanceof FllowerAnimation)) {
						// if (containPoint(child, x, y)) {
						// HeartView heartView = new HeartView(
						// getActivity());
						// heartView.setColor(Color.RED);
						// d2s_layout.addHeartView(heartView, (int) x,
						// (int) y);
						// TextView tv = (TextView)
						// child.findViewById(R.id.cell_name_textview);
						// if(tv!=null)
						// tv.setTextColor(0xff339F78);
						// break;
						// }
						// }
						// }

						return false;
					}
					break;

				default:
					break;
				}
				return true;
			}
		});

		dis_ll.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					dis_ll_child_x = event.getX();
					dis_ll_child_y = event.getY();
					return false;
				}
				return false;
			}
		});

		d2s_layout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (d2s_layout == null)
							return;
						android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) d2s_layout
								.getLayoutParams();
						if (d2s_layout.getHeight() > d2s_layout_hegith) {
							d2s_layout_hegith = d2s_layout.getHeight();
						} else if (d2s_layout.getHeight() == d2s_layout_hegith) {
							setTimerTag(false);
						} else {
							setTimerTag(true);
						}
						// lp.height = d2s_layout_hegith;
						// d2s_layout.setLayoutParams(lp);
					}
				});

	}

	@Override
	public void initData(Bundle savedInstanceState) {
//		initChat();
		inputManager = (InputMethodManager) getActivity().getSystemService(
				"input_method");
		// createDialog();
		radius = DensityUtil.dip2px(getActivity(), 20);
		roomId = "1000845454";
//		getChatHistery(roomId);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onDestroy() {
		if (discussHandler != null) {
			discussHandler.removeCallbacksAndMessages(null);
			discussHandler = null;
		}
		sendGiftTimer.cancel();
		sendGiftTimer = null;
		mTimer.cancel();
		mTimer = null;
		yaoYaoTimer.cancel();
		yaoYaoTimer = null;
		jianJiaoTimer.cancel();
		jianJiaoTimer = null;
		if (kissList != null) {
			kissList.clear();
			kissList = null;
		}
		if (embraceList != null) {
			embraceList.clear();
			embraceList = null;
		}
		if (flowList != null) {
			flowList.clear();
			flowList = null;
		}
		if (heartList != null) {
			heartList.clear();
			heartList = null;
		}
		if (mDiscussList != null) {
			mDiscussList.clear();
			mDiscussList = null;
		}
		if (bitms != null) {
			bitms.clear();
			bitms = null;
		}
		if (giftsLists != null) {
			giftsLists.clear();
			giftsLists = null;
		}
		if (tems != null) {
			tems.clear();
			tems = null;
		}
		if (lv != null) {
			lv.clear();
			lv = null;
		}
		if (mFaceMapKeys != null) {
			mFaceMapKeys.clear();
			mFaceMapKeys = null;
		}
		fllowerAnimation = null;
		kissAnimation = null;
		heartAnimation = null;
		embraceAnimation = null;
		flowAnimation = null;
		imageLoader = null;
		faceadapter = null;
		indicator = null;
		clickit = null;
		System.gc();
		super.onDestroy();
	}

	/**
	 * 获取全部讨论内容
	 */
	private void requestAllDiscuss() {
		Map<String, String> paramss = new HashMap<String, String>();
		paramss.put("activityId", id); // 活动id
		paramss.put("dType", "2"); // 活动类型
		paramss.put("rows", "60");
		paramss.put("crowdfundId", "");
		paramss.put("fileId", "");
		paramss.put("action", "");
		paramss.put("releaseMillisecond", "");
		try {
			Date curDate = new Date(System.currentTimeMillis() - 60 * 1000);// 获取当前时间
			String str = formatter.format(curDate);
			paramss.put("startTime", str); // 时间
		} catch (Exception e) {
			e.printStackTrace();
		}

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (discussHandler == null)
							return;
						isgetDisscuss = true;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = GET_DISSCUSS;
							msg.obj = data.getObject();
							discussHandler.sendMessage(msg);
						} else {
							discussHandler.sendEmptyMessage(NO_NET);
						}
					}
				}, false, false);

	}

	/**
	 * 发送评论接口
	 */
	private void requestSendDiscuss(String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileId", "1"); // 宣传视屏id
		tempDiscussBean.setFileId("1");
		params.put("activityId", id); // 活动id
		tempDiscussBean.setActivityId(id);
		params.put("userId", GlobalParams.USER_ID); // 用户id
		tempDiscussBean.setUserId(GlobalParams.USER_ID);
		params.put("userName", GlobalParams.USER_NAME);// 用户昵称
		tempDiscussBean.setUserName(GlobalParams.USER_NAME);
		params.put("content", content);// 发表内容
		tempDiscussBean.setContent(content);
		params.put("dType", "2");
		tempDiscussBean.setDid("2");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SEND_DISCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (discussHandler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							discussHandler.sendEmptyMessage(DISSCUSS_SUCCESS);
						} else {
							Toast.makeText(getActivity(), "服务器繁忙，请稍后重试",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, false, false);

	}

	/**
	 * 初始化表情界面
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
		GridView gv = new GridView(getActivity());
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(getActivity(), i));
		gv.setOnTouchListener(forbidenScroll());

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == FaceUtils.NUM) {// 删除键的位置
					int selection = mDiscussInputEt.getSelectionStart();
					String text = mDiscussInputEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mDiscussInputEt.getText().delete(start, end);
							return;
						}
						mDiscussInputEt.getText().delete(selection - 1,
								selection);
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

							int newHeight = DensityUtil.dip2px(getActivity(),
									24);
							int newWidth = DensityUtil
									.dip2px(getActivity(), 24);
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,
									0, rawWidth, rawHeigh, matrix, true);
							ImageSpan imageSpan = new ImageSpan(getActivity(),
									newBitmap);
							String emojiStr = mFaceMapKeys.get(count);
							SpannableString spannableString = new SpannableString(
									emojiStr);
							spannableString.setSpan(imageSpan,
									emojiStr.indexOf('['),
									emojiStr.indexOf(']') + 1,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							mDiscussInputEt.append(spannableString);
						} else {
							String ori = mDiscussInputEt.getText().toString();
							int index = mDiscussInputEt.getSelectionStart();
							StringBuilder stringBuilder = new StringBuilder(ori);
							stringBuilder.insert(index, mFaceMapKeys.get(count));
							mDiscussInputEt.setText(stringBuilder.toString());
							mDiscussInputEt.setSelection(index
									+ mFaceMapKeys.get(count).length());
						}
					}
				}
			}
		});

		return gv;
	}

	/**
	 * 防止乱pageview乱滚动
	 * 
	 * @return
	 */
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

	/**
	 * 隐藏输入发或者是表情界面
	 */
	private void hideInputAndFace() {
		inputManager.hideSoftInputFromWindow(getActivity().getWindow()
				.getDecorView().getWindowToken(), 0);
		chat_more.setVisibility(View.GONE);
		selecView.setVisibility(View.GONE);
		faceview.setImageResource(R.drawable.fasong_biaoqing);
		isShowFace = false;
	}

	/**
	 * 当点击返回键时调用
	 */
	public boolean onBackPressed() {
		if (showFaceAndinputTag) {
			hideInputAndFace();
			showFaceAndinputTag = false;
			return true;
		}
		return false;
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
	}

	/**
	 * 创建充值提示框
	 */
	private void createChongZhiDialog() {
		exitDialog = new CustomDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						RechargeintegralActivity.class);
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
		exitDialog.setConfirmTxt("立即充值");
		exitDialog.setRemindMessage("乐豆不足，请去充值");
		exitDialog.show();
	}

	/**
	 * 界面看不到时设置为true 用于清除界面所有动画
	 * 
	 * @param timerTag
	 */
	public void setTimerTag(boolean timerTag) {
		this.timerTag = timerTag;
		if (d2s_layout != null && timerTag) {
			// d2s_layout.clearAnimation();
		}
	}

	/**
	 * 获取礼物颜色对象
	 * 
	 * @return
	 */
	private int[] getGifts() {
		int[] gifts = new int[] { R.drawable.gift_flower,
				R.drawable.gift_embrace, R.drawable.gift_heart,
				R.drawable.gift_kiss };
		return gifts;
	}

	/**
	 * 横竖屏切换执行时的方法
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setTimerTag(true);
		} else {
			setTimerTag(false);
		}
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 判断点击位置是否是view所在范围
	 * 
	 * @param view
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean containPoint(View view, float x, float y, int index) {
		Transformation trans = new Transformation();
		Animation anim = view.getAnimation();
		if (anim != null) {
			anim.getTransformation(view.getDrawingTime(), trans);
		}
		Matrix matrix = trans.getMatrix();

		int dx = view.getLeft();
		int dy = view.getTop();

		x -= dx;
		y -= dy;

		Matrix mat = new Matrix();
		if (matrix.invert(mat)) {
			float[] pointsSrc = new float[] { x, y };
			float[] pointsEnd = new float[] { 0, 0 };

			mat.mapPoints(pointsEnd, pointsSrc);

			x = pointsEnd[0] + dx;
			y = pointsEnd[1] + dy;
		}

		Rect rect = new Rect();
		view.getHitRect(rect);
		return rect.contains((int) x, (int) y);
	}

	/**
	 * // 1 鲜花 2 拥抱 3 爱心 4 吻
	 * 
	 * @param type
	 *            送花类型
	 */
	private void sendGifts(int typeGift) {
		GiftBean giftBean = giftsLists.get(typeGift);
		MobclickAgent.onEvent(getActivity(), "PresentGift");
		Map<String, String> params = new HashMap<String, String>();
		params.put("giftName", giftBean.getGiftName()); // 礼物名称
		params.put("activityId", id); // 当前活动id
		params.put("integral", giftBean.getIntergal()); // 礼物积分
		if (GlobalParams.IS_SPORT) {
			params.put("giveTo", tems.get(agreeTemIndex).teamName); // 送礼给谁
		} else {
			params.put("giveTo", lable); // 送礼给谁
		}
		addPower("0",giftBean.getIntergal());
		GetDataFromInternet.getInStance().interViewNet(ConstantValue.SEND_GIF,
				params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (discussHandler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							Message msg = new Message();
							msg.obj = obj;
							msg.what = SED_SUCCESS;
							discussHandler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 获取所有礼物积分
	 */
	private void getGiftsJIFen() {
		NetService netService = NetService.getInStance();
		netService.setHttpMethod(HttpSetting.POST_MODE);
		netService.setUrl(ConstantValue.GIFT_JIFEN);
		netService.setUseCache(false);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData data) {
				if (data != null && data.getNetResultCode() == 0) {
					Object obj = data.getObject();
					Log.i("wxn", "" + obj);
					try {
						JSONArray jsonArr = new JSONArray((String) obj);
						giftsJifen = new String[jsonArr.length()];
						for (int i = 0; i < jsonArr.length(); i++) {
							JSONObject temp = (JSONObject) jsonArr.get(i);
							if (temp.has("expendIntegral")) {
								giftsJifen[i] = temp
										.getString("expendIntegral");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

	/**
	 * 动态添加view条目
	 */
	private void addview(final View item) {
		LinearLayout.LayoutParams prams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		item.setLayoutParams(prams);

		// 判断scrollview中的控件大小与scrollview大小是否相同
		// 控件大小相同，隐藏新添加的item控件 以便后面执行动画
		item.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						if (dis_ll.getTop() != 0)
							item.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						dis_ll.setScrollY(-item.getHeight());
					}
				});
		dis_ll.addView(item);
		ObjectAnimator anim = ObjectAnimator.ofFloat(item, "scaleX", 0f, 1f)
				.setDuration(500);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				int sY = -item.getHeight()
						+ (int) (item.getHeight() * Float.parseFloat(animation
								.getAnimatedValue().toString()));
				if (sY != 0) {
					dis_ll.scrollTo(0, sY);
					if (dis_ll.getY() <= 0) {
						dis_ll.removeViewAt(0);
					}
				}
			}
		});
		anim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator animation) {
				new Thread() {
					public void run() {
						Activity activity = getActivity();
						if (activity != null)
							activity.runOnUiThread(new Runnable() {
								public void run() {
									AlphaAnimation anim = new AlphaAnimation(1,
											0);
									anim.setDuration(showTime2item);
									anim.setFillAfter(true);
									anim.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											View child = dis_ll.getChildAt(0);
											if (child != null) {
												dis_ll.getChildAt(0)
														.setVisibility(
																View.INVISIBLE);
												dis_ll.removeViewAt(0);
											}
										}
									});
									if (item != null) {
										item.startAnimation(anim);
									}
								}
							});
					};
				}.start();
			}
		});
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(item, "scaleY", 0f, 1f)
				.setDuration(500);
		AnimatorSet set = new AnimatorSet();
		set.play(anim).with(anim1);
		set.setTarget(item);
		item.setPivotX(0);
		item.setPivotY(item.getMeasuredHeight());
		item.setDrawingCacheEnabled(false);
		set.start();
	}

	/**
	 * 获取用户积分
	 */
	private void getUserJiFen() {
		if (!TextUtils.isEmpty(GlobalParams.USER_ID))
			GetDataFromInternet.getInStance().interViewNet(
					ConstantValue.GET_JIFEN, null, new GetDatas() {

						@Override
						public void getServerData(BackData data) {
							if (discussHandler == null)
								return;
							if (data != null && data.getNetResultCode() == 0) {
								Object obj = data.getObject();
								if (obj != null) {
									Message msg = new Message();
									msg.what = SUCCESS_DATE_JIFEN;
									msg.obj = obj;
									discussHandler.sendMessage(msg);
									return;
								}

							}
							Message msg = new Message();
							msg.what = FILUE_DATE_JIFEN;
							discussHandler.sendMessage(msg);
						}
					}, false, false);
	}

	/**
	 * 解析后台返回 的积分 并进行存储
	 * 
	 * @param obj
	 */
	private void analyzeDate(String obj) {
		try {
			if (TextUtils.isEmpty(obj)) {
				GlobalParams.USER_INTEGRAL = 0;
				SharedPreferencesUtils.saveData(getActivity(),
						"ls_user_message", "ingegral",
						GlobalParams.USER_INTEGRAL);
				return;
			}
			JSONObject jObj = new JSONObject(obj);
			if (jObj.has("integralBalance")) {
				GlobalParams.USER_INTEGRAL = jObj.getInt("integralBalance");
				SharedPreferencesUtils.saveData(getActivity(),
						"ls_user_message", "ingegral",
						GlobalParams.USER_INTEGRAL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	/**
	 * 解析后台返回的送礼数据
	 * 
	 * @param obj
	 */
	private void anilySendGift(Object obj) {
		if (obj == null)
			return;
		try {
			JSONObject jObj = new JSONObject((String) obj);
			if (jObj.has("content")) {
				DiscussBean tempDiscuss = new DiscussBean();
				tempDiscuss.setContent(jObj.getString("content"));
				tempDiscuss.setUserImage(GlobalParams.USER_HEADER);
				tempDiscuss.setUserName(GlobalParams.USER_NAME);
				tempDiscuss.setUserId(GlobalParams.USER_ID);
				mDiscussList.addFirst(tempDiscuss);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文案内容
	 */
	private void getSuggestion() {
		if (TextUtils.isEmpty(GlobalParams.USER_ID))
			return;
		Map<String, String> paramss = new HashMap<String, String>();
		paramss.put("activityId", id); // 活动id
		// paramss.put("type", "1"); // 活动类型
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.TISHI_WENAN, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (discussHandler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							if (obj != null) {
								Message msg = new Message();
								msg.what = SUCCESS_DATE_WENAN;
								msg.obj = obj;
								discussHandler.sendMessage(msg);
								return;
							}

						}
					}
				}, false, false);
	}

	/**
	 * 解析后台返回的送礼数据
	 * 
	 * @param obj
	 */
	private void anilyWenAn(Object obj) {
		if (obj == null)
			return;
		try {
			JSONArray jsArray = new JSONArray((String) obj);
			wenAnList = new ArrayList<>();
			for (int i = 0; i < jsArray.length(); i++) {
				WenAnBean bean = new WenAnBean();
				JSONObject orderObj = jsArray.getJSONObject(i);
				if (orderObj.has("promptDocumentContent"))
					bean.setPromptDocumentContent(orderObj
							.getString("promptDocumentContent"));
				if (orderObj.has("id"))
					bean.setId(orderObj.getString("id"));
				if (orderObj.has("duration"))
					bean.setDuration(orderObj.getString("duration"));
				String type = null;
				if (orderObj.has("type")) {
					type = orderObj.getString("type");
					bean.setType(type);
				}
				if (TextUtils.isEmpty(bean.getDuration())) {
					continue;
				}
				if ("1".equals(type) || "2".equals(type) || "3".equals(type))
					wenAnList.add(bean);
			}
			if (wenAnList != null && wenAnList.size() > 0) {
				for (WenAnBean bean : wenAnList)
					if ("1".equals(bean.getType())) {
						sendGiftTimer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {
								for (WenAnBean wenBean : wenAnList) {
									if ("1".equals(wenBean.getType())) {
										DiscussBean bean = new DiscussBean();
										bean.setWenan(true);
										bean.setWenAnId("1");
										bean.setUserName("系统提示");
										bean.setContent(wenBean
												.getPromptDocumentContent());
										mDiscussList.addFirst(bean);
										break;
									}
								}

							}
						}, 500, Integer.parseInt(bean.getDuration()) * 1000);
					} else if ("2".equals(bean.getType())) {
						yaoYaoTimer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {
								for (WenAnBean wenBean : wenAnList) {
									if ("2".equals(wenBean.getType())) {
										DiscussBean bean = new DiscussBean();
										bean.setWenan(true);
										bean.setWenAnId("2");
										bean.setUserName("系统提示");
										bean.setContent(wenBean
												.getPromptDocumentContent());
										mDiscussList.addFirst(bean);
										break;
									}
								}
							}
						}, 600, Integer.parseInt(bean.getDuration()) * 1000);

					} else if ("3".equals(bean.getType())) {
						jianJiaoTimer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {
								for (WenAnBean wenBean : wenAnList) {
									if ("3".equals(wenBean.getType())) {
										DiscussBean bean = new DiscussBean();
										bean.setWenan(true);
										bean.setWenAnId("3");
										bean.setUserName("系统提示");
										bean.setContent(wenBean
												.getPromptDocumentContent());
										mDiscussList.addFirst(bean);
										break;
									}
								}
							}
						}, 650, Integer.parseInt(bean.getDuration()) * 1000);
					}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示送礼 图标
	 */
	private void showSendGift() {
		composer_button = getActivity().getResources().getDrawable(
				R.drawable.composer_button);
		composerbtgetMinimumHeight = composer_button.getMinimumHeight();
		params = (android.widget.RelativeLayout.LayoutParams) clayout
				.getLayoutParams();
		params.setMargins(0, 0, DensityUtil.dip2px(getActivity(), 20),
				DensityUtil.dip2px(getActivity(), 20));
		orientation = ComposerLayout.RIGHTBOTTOM;
		MobclickAgent.onEvent(getActivity(), "ShowGiftButton");
		clayout.init(0, centerImage, orientation, size, radius, giftsLists);
		clayout.setLayoutParams(params);
		clayout.setButtonsOnClickListener(clickit);
	}

	/**
	 * 获取送礼信息
	 */
	public void getGiftMessage() {
		Map<String, String> paramss = new HashMap<String, String>();
		// 如果是比赛 则为球队id 音乐会 则是 活动id
		if (GlobalParams.IS_SPORT && tems != null) {
			paramss.put("teamId", tems.get(agreeTemIndex).teamId);
			// 1：比赛 2：音乐会
			paramss.put("type", "1");
		} else {
			paramss.put("teamId", id);
			paramss.put("type", "2");
		}
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_GIFT_MESSAGE, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (discussHandler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = SUCCESS_DATE_GIFT;
							msg.obj = data.getObject();
							discussHandler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 解析获取礼物 数据
	 * 
	 * @param data
	 */
	private void anilyGiftData(Object data) {
		if (data == null)
			return;
		String date = (String) data;
		try {
			JSONObject jsObj = new JSONObject(date);
			if (jsObj.has("data")) {
				String obj = jsObj.getString("data");
				jsObj = new JSONObject(obj);
				if (jsObj.has("centerImage")) {
					centerImage = jsObj.getString("centerImage");
				}
				if (jsObj.has("giftList")) {
					String dats = jsObj.getString("giftList");
					JSONArray jsArray = new JSONArray((String) dats);
					giftsLists = new ArrayList<>();
					bitms = new ArrayList<>();
					for (int i = 0; i < jsArray.length(); i++) {
						GiftBean bean = new GiftBean();
						JSONObject orderObj = jsArray.getJSONObject(i);
						if (orderObj.has("giftName")) {
							bean.setGiftName(orderObj.getString("giftName"));
						}
						if (orderObj.has("image_selected")) {
							bean.setImage_selected(orderObj
									.getString("image_selected"));
						}
						if (orderObj.has("snow")) {
							bean.setSnow(orderObj.getString("snow"));
						}
						if (orderObj.has("image_uselected")) {
							bean.setImage_uselected(orderObj
									.getString("image_uselected"));
						}
						if (orderObj.has("integral")) {
							bean.setIntergal(orderObj.getString("integral"));
						}
						giftsLists.add(bean);
						Log.i("wxn", "图片P：" + bean.getImage_selected());
					}
				}
				showSendGift();
				new Thread() {
					public void run() {
						for (GiftBean beans : giftsLists)
							bitms.add(imageLoader
									.loadImageSync(ConstantValue.BASE_IMAGE_URL
											+ beans.getSnow()));
					};
				}.start();
			}
		} catch (Exception e) {
			Log.i("wxn", e.getMessage());
		}

	}

	/**
	 * 根据球队信息 和 要支持的球队 去获取 所支持球队信息
	 * 
	 * @param tems
	 *            球队集合
	 * @param agreeIndex
	 *            支持哪个球队 在集合中的索引
	 */
	public void setLiveDetialActivity(ArrayList<TemMessage> tems, int agreeIndex) {
		this.tems = new ArrayList<>();
		this.tems.addAll(tems);
		powerLayout.setVisibility(View.GONE);
		this.agreeTemIndex = agreeIndex;
		if (agreeTemIndex != -1)
			getGiftMessage();
	}

	/**
	 * 根据支持的球队获取 礼物信息
	 * 
	 * @param agreeIndex
	 */
	public void setAgrreIndex(int agreeIndex) {
		this.agreeTemIndex = agreeIndex;
		powerLayout.setVisibility(View.GONE);
		if (agreeTemIndex != -1)
			getGiftMessage();
	}

	/**
	 * 打开淘宝菜单
	 */
	private void openTaobaoMenu() {
		clayout.expand();
		clayout.setButtonsOnClickListener(clickit);
	}

	/**
	 * 显示淘宝菜单
	 */
	private void showTaobaoMenu() {
		scrrenHeight = d2s_layout.getHeight();
		scrrenWidth = d2s_layout.getWidth();
		composer_button = getActivity().getResources().getDrawable(
				R.drawable.composer_button);
		composerbtgetMinimumHeight = composer_button.getMinimumHeight();
		params = (android.widget.RelativeLayout.LayoutParams) clayout
				.getLayoutParams();
		orientation = showLocation(mCurrentInScreenX, mCurrentInScreenY);
		if (giftsJifen == null || giftsJifen.length == 0) {
			Toast.makeText(getActivity(), "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		MobclickAgent.onEvent(getActivity(), "ShowGiftButton");
		// clayout.init(getGifts(), R.drawable.composer_button,
		// R.drawable.composer_button, orientation, size, radius,
		// giftsJifen);
		// discussHandler.sendEmptyMessageDelayed(OPEN_TAOMAO_MENU, 20);
		clayout.setLayoutParams(params);
	}

	/**
	 * 解析 获取的讨论数据
	 * 
	 * @param data
	 */
	private void analsyeDisscussForNet(String backdata) {
		d2s_layout.setStartPoint(d2s_layout.getBottom());
		String contents;
		if (!Utils.isNullOrEmpty(backdata)) {
			LiveDetialActivity activity = (LiveDetialActivity) getActivity();
			try {
				JSONArray jsonArr = new JSONArray(backdata);
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject temp = (JSONObject) jsonArr.get(i);
					DiscussBean tempDiscuss = new DiscussBean();
					if (temp.has("content")) {
						contents = temp.getString("content");
						tempDiscuss.setContent(contents);
						if (activity != null) {
							activity.setDanmu(contents, false);
						}
					}
					if (temp.has("likeCount"))
						tempDiscuss.setLikeCount(temp.getString("likeCount"));
					if (temp.has("releaseDate"))
						tempDiscuss.setReleaseDate(temp
								.getString("releaseDate"));
					if (temp.has("userId"))
						tempDiscuss.setUserId(temp.getString("userId"));
					if (temp.has("userImage"))
						tempDiscuss.setUserImage(temp.getString("userImage"));
					if (temp.has("userName"))
						tempDiscuss.setUserName(temp.getString("userName"));
					mDiscussList.add(tempDiscuss);
				}
				d2s_layout.setStartPoint(d2s_layout.getBottom());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送评论内容成功
	 */
	private void sendDisscussSuccess() {
		d2s_layout.setStartPoint(d2s_layout.getBottom());
		Activity at = getActivity();
		if (at == null)
			return;
		DiscussBean bean = new DiscussBean();
		String content = mDiscussInputEt.getText().toString().trim();
		bean.setContent(content);
		if (at != null && at instanceof LiveDetialActivity) {
			LiveDetialActivity lda = (LiveDetialActivity) at;
			lda.setDanmu(content, true);
		}
		bean.setUserImage(GlobalParams.USER_HEADER);
		bean.setUserName(GlobalParams.USER_NAME);
		bean.setUserId(GlobalParams.USER_ID);
		mDiscussList.addFirst(bean);
		mDiscussInputEt.setText("");
	}

	/**
	 * 长按界面 判断是否要弹出淘宝菜单界面
	 */
	private void checkShowTaobaoMenu() {
		if (!isLongPress)
			return;
		// clayout.removeAllViews();
		if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
			createDialog();
			exitDialog.setRemindTitle("温馨提示");
			exitDialog.setCancelTxt("取消");
			exitDialog.setConfirmTxt("立即登录");
			exitDialog.setRemindMessage("登录之后才能送礼哦~");
			exitDialog.show();
			return;
		}
		if (giftsJifen == null || giftsJifen.length == 0)
			getGiftsJIFen();
		discussHandler.sendEmptyMessage(SHOW_TAOMAO_MENU);
	}

	/**
	 * 根据屏幕高度设置滚动讨论显示的时间
	 */
	private void setShowTimerForDisscuss() {
		if (d2s_layout.getHeight() > 1556) {
			showTime2item = 7500;
		} else if (d2s_layout.getHeight() > 1456) {
			showTime2item = 7000;
		} else if (d2s_layout.getHeight() > 1156) {
			showTime2item = 6000;
		} else if (d2s_layout.getHeight() > 1000) {
			showTime2item = 5500;
		} else {
			showTime2item = 4500;
		}
	}

	/**
	 * 点击屏幕时判断是否是点击了讨论条目 如果是 判断
	 * 
	 * @param msg
	 */
	private void clickShowHeart(Message msg) {
		float x = msg.arg1;
		float y = msg.arg2;
		for (int i = 0; i < dis_ll.getChildCount(); i++) {
			View child = dis_ll.getChildAt(i);
			if (child != null && child.getVisibility() == View.VISIBLE
					&& !(child instanceof FllowerAnimation)) {
				if (containPoint(child, dis_ll_child_x, dis_ll_child_y, i)) {
					dis_ll_child_x = -40000;
					dis_ll_child_y = -40000;
					DiscussBean disBean = (DiscussBean) child.getTag();
					if (disBean == null)
						return;
					if (disBean != null) {
						if (disBean.isWenan()
								&& "2".equals(disBean.getWenAnId())) {
							if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
								createDialog();
								exitDialog.setRemindTitle("温馨提示");
								exitDialog.setCancelTxt("取消");
								exitDialog.setConfirmTxt("立即登录");
								exitDialog.setRemindMessage("登录之后才能操作哦~");
								exitDialog.show();
								return;
							} else {
								Intent shakeIntent = new Intent();
								shakeIntent.putExtra("id", id);
								shakeIntent.setClass(getActivity(),
										ShakeDetailActivity.class);
								startActivity(shakeIntent);
								return;
							}
						} else if (disBean.isWenan()
								&& "3".equals(disBean.getWenAnId())) {
							startRecordActivity();
							return;
						}
					}
					HeartView heartView = new HeartView(getActivity());
					heartView.setColor(Color.RED);
					d2s_layout.addHeartView(heartView, (int) x, (int) y);
					TextView tv = (TextView) child
							.findViewById(R.id.cell_name_textview);
					if (tv != null)
						tv.setTextColor(0xff339F78);
					break;
				}
			}
		}
		dis_ll_child_x = -40000;
		dis_ll_child_y = -40000;
	}

	/**
	 * 跳转到我要尖叫界面 获取liveDetialActivity数据
	 */
	private void startRecordActivity() {
		LiveDetialActivity liveActivity = (LiveDetialActivity) getActivity();
		boolean isScreamend = liveActivity.isScreamed();
		Intent recordIntent = new Intent();
		recordIntent.setClass(liveActivity, RecordActivity.class);
		if (!isScreamend)
			recordIntent.putExtra("hasScreamed", "hasScreamed");
		recordIntent.putExtra("tems", tems);
		recordIntent.putExtra("id", id);
		recordIntent.putExtra("roomId", roomId);
		recordIntent.putExtra("agreeTemIndex", agreeTemIndex);
		
		startActivity(recordIntent);
	}

	private void startPowerActivity() {
		Intent intent = new Intent(getActivity(), PowerActivity.class);
		intent.putExtra("id", id);
		if (tems != null)
			intent.putExtra("type", "0");
		else
			intent.putExtra("type", "1");
		intent.putExtra("power", energy);
		startActivity(intent);
	}

	/**
	 * 获取能量总值
	 */
	private void getPower() {
		if(TextUtils.isEmpty(GlobalParams.USER_ID)){
			powerLayout.setVisibility(View.GONE);
			return;
		}
		powerLayout.setVisibility(View.VISIBLE);
		Map<String, String> paramss = new HashMap<String, String>();
		// 如果是比赛 则为球队id 音乐会 则是 活动id
		if (GlobalParams.IS_SPORT && tems != null) {
			// 活动类型 0:比赛,1:活动
			paramss.put("resource_type", "0");
		} else {
			paramss.put("resource_type", "1");
		}
		paramss.put("resource_id", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_SUM_ENERGY, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "能量值：" + data.getObject());
						if (discussHandler == null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = SUCCESS_SUMENGER_GIFT;
							msg.obj = data.getObject();
							discussHandler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 解析获取能量总值字段
	 * 
	 * @param obj
	 */
	private void analySumenger(Object obj) {
		if (obj == null){
			energy = "0";
			tvPower.setText("能量值：" + energy);
			return;
		}
		try {
			JSONObject jObj = new JSONObject((String) obj);
			if (jObj.has("energy")) {
				energy = jObj.getString("energy");
				tvPower.setText("能量值：" + energy);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发表讨论或送礼调用该接口 计算能量总值
	 * @param type  （0：礼物，1：弹幕
	 * @param integral   礼物价值  如果是弹幕 为空
	 */
	private void addPower(String type ,String integral) {
		Map<String, String> paramss = new HashMap<String, String>();
		// 如果是比赛 则为球队id 音乐会 则是 活动id
		if (GlobalParams.IS_SPORT && tems != null) {
			// 活动类型 0:比赛,1:活动
			paramss.put("resource_type", "0");
		} else {
			paramss.put("resource_type", "1");
		}
		if("0".equals(type)){
			paramss.put("integral", integral);
		}else{
			paramss.put("barrage", "1");
		}
		paramss.put("resource_id", id);
		paramss.put("type", type);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_POWER, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						getPower();
					}
				}, false, false);
	}
	
	
	/**
	 * 获取聊天记录 从而 获取聊天室ip 和端口号
	 */
	private void getChatHistery(String roomId) {
		NetService netService = NetService.getInStance();
		netService.setParams("roomId", roomId);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_HISTORY);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				if (discussHandler == null)
					return;
				if (result != null && result.getNetResultCode() == 0) {
					Message msg = new Message();
					msg.what = CHAT_HISTERY;
					msg.obj = result.getObject();
					discussHandler.sendMessage(msg);
				}
			}
		});
	}

//	/**
//	 * 发送聊天消息
//	 */
//	private void sendChatMessage(String message) {
//		NetService netService = NetService.getInStance();
//		netService.setParams("roomId", roomId);
//		netService.setParams("message", message);
//		netService.setParams("vtkey", "");
//		netService.setHttpMethod(HttpSetting.GET_MODE);
//		netService.setUrl(ConstantValue.SEND_MESSAGE);
//		netService.loader(new NetLoadListener() {
//			
//			@Override
//			public void onloaderListener(BackData result) {
//				Log.i("wxn", "发送消息："+result.getNetResultCode()+"....."+result.getObject());
//				
//			}
//		});
//
//	}
//	/**
//	 * 解析获取到聊天记录信息
//	 * @param data
//	 */
	private void analyseChatHistery(String data) {
//		Log.i("wxn", "聊天servers:"+data);
//		if (TextUtils.isEmpty(data)) {
//			return;
//		}
//		try {
//			JSONObject objs = new JSONObject(data);
//			if (objs.has("data")) {
//				JSONObject datas = objs.getJSONObject("data");
//				if (datas.has("result")) {
//					JSONObject result = datas.getJSONObject("result");
//					if (result.has("server")) {
//						JSONObject ser = result.getJSONObject("server");
//						String server = ser.getString("server");
//						// http://10.154.252.32:8010
//						if (!TextUtils.isEmpty(server)) {
//							String servers = server.substring(7, 21);
//							String port = server.substring(22);
//							Log.i("wxn", "聊天servers:" + servers + " .... "
//									+ port);
//							connection(servers,Integer.parseInt(port),roomId);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//
//		}
	}
//	/**
//	 * 初始化乐视聊天室
//	 */
//	private void initChat(){
//		mChatCallback = new ChatCallback();
//        mChatManager = new LetvChatClientManager(mChatCallback);
//        mChatManager.enableDebugMode(true);
//	}
//	
//	private void connection(String testIp,int testPort,String testChatRoomId){
//		 try {
//             mChatManager.registerCallback(mChatCallback);
//         } catch (IllegalArgException e) {
//             e.printStackTrace();
//             Log.i("chat", "连接异常："+e.getMessage());
//         }
//         if (!mChatManager.isConnected()){
//             mChatManager.startConnectServer(testIp, testPort, testChatRoomId, ChatConstant.PHONE_TYPE, "V1.0");
//         }
//	}
//	
//	private LetvChatClientManager mChatManager;
//    private IChatIOCallback mChatCallback;
//	/**
//	 * 
//	* @ClassName: ChatCallback 
//	*
//	* @Description: 乐视聊天室回调函数 
//	*
//	* @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
//	*
//	* @author wangxinnian
//	* 
//	* @date 2016-8-1 下午3:59:39 
//	* 
//	*
//	 */
//  private class ChatCallback implements IChatIOCallback {
//
//        @Override
//        public void onJoinChat(int code, String message, String vtkey) {
//            Log.d("chat", "onJoinChat:code==" + code);
//        }
//
//        @Override
//        public void onExitJoinChat(boolean isExitChatSuccess) {
//            Log.d("chat", "onExitJoinChat:" + isExitChatSuccess);
//        }
//
//        @Override
//        public void onRecevieChatMsg(String json) {
//            Log.d("chat", "onRecevieChatMsg:" + json);
//        }
//        @Override
//        public void onReceiveBroadcastMsg() {
//        	 Log.d("chat", "onReceiveBroadcastMsg:");
//        }
//    }

}
