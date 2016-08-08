/**
 * 
 */
package com.lesports.stadium.activity;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.Commants2Adapter;
import com.lesports.stadium.adapter.CrowdfundingDetailZhuyiAdapter;
import com.lesports.stadium.adapter.FaceAdapter;
import com.lesports.stadium.adapter.FacePageAdeapter;
import com.lesports.stadium.adapter.ReportbackExpandableListViewAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.bean.CrowReportBackBean;
import com.lesports.stadium.bean.CrowdDetailBean;
import com.lesports.stadium.bean.DiscussBean;
import com.lesports.stadium.bean.ReportBackChildBean;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.EditLengthOnclistener;
import com.lesports.stadium.utils.FaceUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CirclePageIndicator;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyProgressBar;
import com.lesports.stadium.view.MyScrollView;
import com.lesports.stadium.view.MyScrollView.OnScrollListener;
import com.lesports.stadium.view.Mylistview;
import com.lesports.stadium.view.SharePopupWindow;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 众筹详情页面
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc 
 * @data:
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */

public class CrowdFundingDetailActivity extends FragmentActivity implements
		OnScrollListener, OnClickListener, PlatformActionListener, Callback {

	/**
	 * fragment的导航栏的导航线
	 */
	private ImageView mLine;
	/**
	 * fragment的导航栏的导航线
	 */
	private ImageView mLine2;
	/**
	 * 该布局下方需要设置导航线
	 */
	private LinearLayout mLyoutLine;
	/**
	 * 存储着fragment的viewpager
	 */
	// private NoScrollViewPager mViewpager;
	/**
	 * 回报的点击布局
	 */
	private RelativeLayout mLayoutClickReportback;
	/**
	 * 评论的点击布局
	 */
	private RelativeLayout mLayoutClickCommant;

	/**
	 * 存储着viewpager数据的数组
	 */
	private int[] selectList;
	private ImageView[] textViewList;
	private int selectID = 0;

	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	private MyScrollView mScrollview;
	/**
	 * 需要悬停在屏幕顶端的布局
	 */
	private LinearLayout mLayoutXT;
	/**
	 * 当前悬停布局所在父布局
	 */
	private LinearLayout mXuanDangqian;
	/**
	 * 滑动后悬停布局所在布局
	 */
	private LinearLayout mXuanHuadonghou;
	/**
	 * 动态获取当前悬停布局的顶部位置
	 */
	private int searchLayoutTop;
	/**
	 * 展示评论数据的listview
	 */
	private Mylistview mListviewCommant;
	/**
	 * 列表项数据适配器
	 */
	private Commants2Adapter mAdapter;
	/**
	 * 展示回报数据的listview
	 */
	private ExpandableListView mListviewReportback;
	/**
	 * 回报的textview
	 */
	private TextView mReportBack;
	/**
	 * 评论textview
	 */
	private TextView mCommants;
	/**
	 * 底部需要隐藏的布局
	 */
	private LinearLayout mLayoutBottom;
	/**
	 * 展示注意事项的listview
	 */
	// private Mylistview mListview_zhuyishixiang;
	/**
	 * 展示注意事项的列表项的适配器
	 */
	private CrowdfundingDetailZhuyiAdapter mZyAdapter;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * // 组元素数据列表——回报
	 */
	private List<ReportBackGroupBean> groups = new ArrayList<ReportBackGroupBean>();
	/**
	 * // 子元素数据列表——回报
	 */
	private Map<String, List<ReportBackChildBean>> children = new HashMap<String, List<ReportBackChildBean>>();
	/**
	 * 展示回报数据的适配器
	 */
	private ReportbackExpandableListViewAdapter mReportbackAdapter;
	/**
	 * 评论部分输入内容布局
	 */
	private LinearLayout mLayoutInput;
	/**
	 * 界面当中最外部的视图view
	 */
	private RelativeLayout mRootLayout;
	/**
	 * 屏幕高度
	 */
	private int screenHeight = 0;
	/**
	 * 软件盘弹起后所占高度阀值
	 */
	private int keyHeight = 0;
	/**
	 * 输入框
	 */
	private EditText mCommantsEdittext;
	/**
	 * 返回键
	 */
	private ImageView mBack;
	/**
	 * 顶部布局
	 */
	private RelativeLayout mtoplayout;
	/**
	 * 弹出的输入框布局view
	 */
	private View popupView;
	/**
	 * window对象
	 */
	private PopupWindow pop;
	private RelativeLayout ll_popup;
	/**
	 * 传递过来的众筹id
	 */
	private AllChipsBean bean;
	/**
	 * 传递过来的众筹标记
	 */
	private String zhongchou_tag;
	/**
	 * 众筹状态
	 */
	private TextView mStatus;
	/**
	 * 众筹以达到数额
	 */
	private TextView mYidadao;
	/**
	 * 进度条
	 */
	private MyProgressBar progressbar;
	/**
	 * 金额数
	 */
	private TextView mJineshu;
	/**
	 * 剩余天数
	 */
	private TextView mShengyuTime;
	/**
	 * 名称
	 */
	private TextView mCrowdName;
	/**
	 * 众筹状态背景图片
	 */
	private ImageView mStatusBg;
	/**
	 * 参与人数
	 */
	private TextView mCanyurenshu;
	/**
	 * 目标金额
	 */
	private TextView mMubiaojine;
	/**
	 * 表情适配器
	 */
	private FacePageAdeapter faceadapter;
	/**
	 * 存放表情的viewpager
	 */
	private ViewPager mFaceViewPager;
	/**
	 * 当前表情页面页码
	 */
	private int mCurrentPage = 0;

	private CirclePageIndicator indicator;

	/**
	 * 表情对应的文字集合
	 */
	// 表情对应的字符串数组
	private List<String> mFaceMapKeys;
	/**
	 * 表情集合
	 */
	private List<View> lv_face;
	/**
	 * 发表评论的输入框
	 */
	private EditText mCommantInput;

	/**
	 * 表情选择输入按钮
	 */
	private ImageView faceview;
	/**
	 * 表情是否显示
	 */
	private boolean isShowFace = false;
	private InputMethodManager inputManager;// 软键盘管理类
	/**
	 * 表情布局
	 */
	private RelativeLayout chat_more;
	private TextView rl_send;
	private LinearLayout face_ll;
	/**
	 * 查看众筹详情
	 */
	private TextView mCheckCrowdDetail;
	/**
	 * 用于标记网络获取数据为空
	 */
	private final int NUMM_TAG = 1;
	/**
	 * 定义标记，用来接受图文连接，然后传递到图文详情界面
	 */
	private String URL_P_AND_W = "tuwenlianjie";
	/**
	 * 众筹详情顶部图片
	 */
	private ImageView mImageBackground;
	/**
	 * 当前展示回报的时候需要显示的布局
	 */
	private LinearLayout mLayoutReportBack;
	/**
	 * 众筹详情项目简介
	 */
	private TextView mProjectInfo;
	/**
	 * 用于记录当前评论数量
	 */
	private TextView mPinlunshuliang;
	/**
	 * 用于记录当前回报数量
	 */
	private TextView mHuibaoshuliang;
	/**
	 * 存储请求到的评论
	 */
	private LinkedList<DiscussBean> mDiscussList = new LinkedList<DiscussBean>();
	/**
	 * 用于标记网络数据不为空
	 */
	private final int DATA_ISHAVE = 2;
	/**
	 * 要发表的数据内容
	 */
	private String mDiscussStr;
	private SharePopupWindow share;
	private TextView mTitle;
	/**
	 * 分享点击的布局
	 */
	private RelativeLayout mLayoutShare;
	/**
	 * 本类对象
	 */
	public static CrowdFundingDetailActivity instance;
	/**
	 * 注意事项
	 */
	private TextView mZhuyishixiang;
	/**
	 * 演出地点
	 */
	private TextView mAddresss;
	/**
	 * 演出时间
	 */
	private TextView mShowTime;
	/**
	 * 用于标记评论不为空
	 */
	private final int DISCUSS_DATA = 3;
	/**
	 * 需要分享的界面url
	 */
	private String httpurl;
	/**
	 * 加载更多按钮
	 */
	private TextView mAddMoreButton;
	/**
	 * 加载更多进度条
	 */
	private ProgressBar mAddmoreProgress;
	/**
	 * 众筹评论最后一条数据的事件值
	 */
	private String END_TIME_PINLUN;
	/**
	 * 定义标注是否发表评论成功
	 */
	private int tag_is_yesorno=0;
	/**
	 * 众筹图文详情界面分享出去图片
	 */
	private String shared_imager_url;
	
	/**
	 * handler里面的标记
	 */
	private final int GET_DISSCUSS_CONTENT=6;//获取讨论内容
	private final int SEND_DISSCUSS_SUCCESS=8;//发表评论成功
	private final int ADD_MORE_DISSCUSS_SUCCESS=16;//添加更多品论成功
	private final int ADD_MORE_DISSCUSS_FAILED=166;//添加更多品论失败
	private final int ADD_MORE_DISSCUSS_SUCCESS_NODATA=116;//添加更多品论成功但没有数据
	private final int GET_DISSCUSS_SUCCESS=122;//获取评论个数成功
	/**
	 * 众筹详情实体类
	 */
	private CrowdDetailBean mDetailbeans;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case NUMM_TAG:
				Toast.makeText(CrowdFundingDetailActivity.this, "服务器异常，请稍后重试", 1)
						.show();
				break;
			case DATA_ISHAVE:
				String backdata = (String) msg.obj;
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
					//处理返回数据
					useWayuseTheData(backdata);
				}
				break;
			case GET_DISSCUSS_CONTENT:
				String backdata1 = (String) msg.obj;
				Log.e("DISCUSS", "这里是handler" + backdata1);
				if (!Utils.isNullOrEmpty(backdata1)) {
					// 调用方法，将数据加载到界面中
					useWayAddDataToUi(backdata1);
				}
				break;
			case SEND_DISSCUSS_SUCCESS:
				String backdata3 = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata3)) {
					if (backdata3.equals("success")) {
						// 说明成功了，先将数据合成一个实体类，追加到评论集合中
						tag_is_yesorno=1;
						mCommantInput.setText("");
						addData();
//						Toast.makeText(CrowdFundingDetailActivity.this, "发表成功",
//								0).show();
					}
				}
			case ADD_MORE_DISSCUSS_SUCCESS:
				//众筹品论加载更多加载成功
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);
				String backdata2 = (String) msg.obj;
				useWayAddDataToUi_addmore(backdata2);
				if(mDiscussList!=null&&mDiscussList.size()>=1){
					if(tag_is_yesorno==1){
						mDiscussList.removeFirst();
					}
					END_TIME_PINLUN=mDiscussList.get(mDiscussList.size()-1).getReleaseDate();
					if(mAdapter!=null){
						mAdapter.setmDiscussList(mDiscussList);
					}else{
						initdatas(mDiscussList);
					}
				}
			case ADD_MORE_DISSCUSS_SUCCESS_NODATA:
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);
				//众筹品论加载更多加载成功但无数据
				break;
			case ADD_MORE_DISSCUSS_FAILED:
				mAddMoreButton.setVisibility(View.VISIBLE);
				mAddmoreProgress.setVisibility(View.GONE);
				//众筹品论加载更多加载失败
				
				break;
			case GET_DISSCUSS_SUCCESS:
				//获取的评论数据个数
				String backdata22 = (String) msg.obj;
				if(!TextUtils.isEmpty(backdata22)){
					useWayHandlerDisscussNumData(backdata22);
				}
				break;
			case 123:
				
				break;

			default:
				break;
			}

		}

		
	};
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_crowdfunding_detail);
		MobclickAgent.onEvent(CrowdFundingDetailActivity.this,"CrowdFundingDetail");
		instance = this;
		Intent intent = getIntent();
		bean = (AllChipsBean) intent.getSerializableExtra("bean");
		zhongchou_tag = intent.getStringExtra("tag");
		initLayout();
		if (bean != null && !TextUtils.isEmpty(bean.getId())) {
			// 调用方法，初始化view数据
			userUtilsGetDataLIST(bean.getId());
			useWayInitViewData();
		}
		// 调用方法，获取众筹详情数据
		initData();
//		initpopuwindow();
		initFace();
	}

	/**
	 * 将详情数据加载到界面控件上
	 * 
	 * @param beans
	 */
	private void adddatatoviewsss(CrowdDetailBean beans) {
		// TODO Auto-generated method stub
		mZhuyishixiang.setText(beans.getRemark());
		mAddresss.setText(beans.getProjectAddress());
		// 调用方法，处理时间
		decodeTime(beans.getProjectTime());
	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 */
	private void decodeTime(String string) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日     HH:mm",
				Locale.getDefault());
		long time = Long.parseLong(string);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);
//		String timesss = timestring.substring(0, 10);
		mShowTime.setText(timestring);
	}

	/**
	 * 用来处理讨论数据
	 * 
	 * @param backdata1
	 */
	private void useWayAddDataToUi(String backdata1) {
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
			if (mDiscussList != null && mDiscussList.size() != 0) {
				END_TIME_PINLUN=mDiscussList.get(mDiscussList.size()-1).getReleaseDate();
				initdatas(mDiscussList);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * 调用方法，获取众筹评论个数
	 * @param id
	 */
	private void useWayGetDisscussNum(String id) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", id);
		params.put("activityId", "");
		params.put("fileId", "");
		params.put("startTime", "");
		params.put("dType", "3");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ZHONGCHOU_DISSCUSS_NUM, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = NUMM_TAG;
							handler.sendMessage(msg);

						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Message msg = new Message();
								msg.arg1 = NUMM_TAG;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if(data.getNetResultCode()==0){
									Message msg=new Message();
									msg.arg1=122;
									msg.obj=backdata;
									handler.sendMessage(msg);
								}else{
									Message msg=new Message();
									msg.arg1=123;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}
	/**
	 * 用来给处理详情限制字数
	 * 
	 * @param projectIntroduction
	 */
	private void addDataToView(String projectIntroduction) {
		// TODO Auto-generated method stub
		if (projectIntroduction != null
				&& !TextUtils.isEmpty(projectIntroduction)) {
			mProjectInfo.setText(projectIntroduction);
		}
	}
	/**
	 * 为listview追加数据
	 */
	private void addData() {
		// TODO Auto-generated method stub
		DiscussBean beanss = new DiscussBean();
		beanss.setFileId("1");
		beanss.setActivityId(bean.getId());
		beanss.setUserId(GlobalParams.USER_ID);
		beanss.setUserName(GlobalParams.USER_NAME);
		beanss.setUserImage(GlobalParams.USER_HEADER);
		beanss.setContent(mDiscussStr);
		Date date = new Date();
		long currenttimes = date.getTime();
		beanss.setReleaseDate(currenttimes + "");
		beanss.setDid("1");
		if (mDiscussList != null&&mDiscussList.size()!=0) {
			mDiscussList.addFirst(beanss);
			if(mAdapter!=null){
				mAdapter.setmDiscussList(mDiscussList);
				mAdapter.notifyDataSetChanged();
//				mPinlunshuliang.setText(mDiscussList.size() + "");
			}else{
				initdatas(mDiscussList);
			}
			
		}else{
			mDiscussList.addFirst(beanss);
			if(mAdapter!=null){
				mAdapter.setmDiscussList(mDiscussList);
				mAdapter.notifyDataSetChanged();
				mPinlunshuliang.setText(mDiscussList.size() + "");
			}else{
				mPinlunshuliang.setText(mDiscussList.size() + "");
				initdatas(mDiscussList);
			}
		}

	}

	/**
	 * 用来将回报数据加载到回报的listview中
	 * 
	 * @param list
	 * @param bean2
	 * @param beans
	 */
	private void useDataToReportBackListview(List<CrowReportBackBean> list,
			AllChipsBean bean2, CrowdDetailBean beans) {
		// TODO Auto-generated method stub
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				// 调用方法，分拣数据
				checkDataToGroupAndChild(list.get(i), i, bean2, beans);
			}
		}
	}

	/**
	 * 该方法用来将获取的汇报数据加载到回报的组以及子集合中
	 * 
	 * @param bean2
	 * @param i
	 * @param bean22
	 * @param beans2
	 */
	private void checkDataToGroupAndChild(CrowReportBackBean bean2, int i,
			AllChipsBean bean22, CrowdDetailBean beans2) {
		// TODO Auto-generated method stub
		if (bean2 != null) {
			ReportBackGroupBean groupbean = new ReportBackGroupBean();
			groupbean.setId(bean2.getId());
			groupbean.setBean(bean22);
			groupbean.setChildBean(bean2);
			groupbean.setCbean(beans2);
			groupbean.setPeopleNum(bean2.getTotalCount());
			groupbean.setPrice(bean2.getReturnPrice());
			groupbean.setNumLimit(bean2.getReturnLimit());
			groups.add(groupbean);
			String returnContent = bean2.getReturnContent();
			// 这里要对这个字符串进行分割处理，目前还不知道按照什么来进行分割
			List<ReportBackChildBean> products = new ArrayList<ReportBackChildBean>();
			ReportBackChildBean beans = new ReportBackChildBean();
			beans.setmTitle(returnContent);
			products.add(beans);
			// 调用方法，检查该实体在组集合中的下表
			int index = checkIndex(groups, bean2.getId());
			children.put(groups.get(index).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
		}

	}

	/**
	 * 检查组集合中该实体类组信息的下标
	 * 
	 * @param groups2
	 * @param id
	 */
	private int checkIndex(List<ReportBackGroupBean> groups2, String id) {
		// TODO Auto-generated method stub
		int num = 0;
		if (groups2 != null && groups2.size() != 0) {
			if (groups2.size() == 1) {
				num = 0;
			} else {
				for (int i = 0; i < groups2.size(); i++) {
					if (groups.get(i).getId().equals(id)) {
						num = i;
						break;
					}
				}
			}
		}
		return num;
	}

	/**
	 * 用来解析众筹详情数据
	 * 
	 * @param backdata
	 */
	private CrowdDetailBean jsonWaYdATA(String backdata) {
		// TODO Auto-generated method stub
		CrowdDetailBean bean = new CrowdDetailBean();
		try {
			List<CrowReportBackBean> list = new ArrayList<CrowReportBackBean>();
			JSONObject obj = new JSONObject(backdata);
			if (obj.has("evaluateCount")) {
				bean.setEvaluateCount(obj.getString("evaluateCount"));
			}
			if(obj.has("detailPicture")){
				bean.setDetailPicture(obj.getString("detailPicture"));
			}
			if(obj.has("orderPicture")){
				bean.setOrderPicture(obj.getString("orderPicture"));
			}
			if (obj.has("infoUrl")) {
				bean.setInfoUrl(obj.getString("infoUrl"));
			}
			if (obj.has("freight")) {
				bean.setFreight(obj.getString("freight"));
			}
			if (obj.has("id")) {
				bean.setId(obj.getString("id"));
			}
			if (obj.has("propagatePicture")) {
				bean.setPropagatePicture(obj.getString("propagatePicture"));
			}
			if (obj.has("projectTime")) {
				bean.setProjectTime(obj.getString("projectTime"));
			}
			if (obj.has("projectAddress")) {
				bean.setProjectAddress(obj.getString("projectAddress"));
			}
			if (obj.has("projectInfo")) {
				bean.setProjectInfo(obj.getString("projectInfo"));
			}
			if (obj.has("projectIntroduction")) {
				bean.setProjectIntroduction(obj
						.getString("projectIntroduction"));
			}
			if (obj.has("remark")) {
				bean.setRemark(obj.getString("remark"));
			}
			if (obj.has("returnCount")) {
				bean.setReturnCount(obj.getString("returnCount"));
			}
			if (obj.has("returns")) {
				// 说明存在汇报数据
				JSONArray array = obj.getJSONArray("returns");
				int count = array.length();
				for (int i = 0; i < count; i++) {
					JSONObject objs = array.getJSONObject(i);
					CrowReportBackBean beans = new CrowReportBackBean();
					if (objs.has("id")) {
						beans.setId(objs.getString("id"));
					}
					if (objs.has("returnContent")) {
						beans.setReturnContent(objs.getString("returnContent"));
					}
					if (objs.has("returnLimit")) {
						beans.setReturnLimit(objs.getString("returnLimit"));
					}
					if (objs.has("returnName")) {
						beans.setReturnName(objs.getString("returnName"));
					}
					if (objs.has("returnPrice")) {
						beans.setReturnPrice(objs.getString("returnPrice"));
					}
					if (objs.has("totalCount")) {
						beans.setTotalCount(objs.getString("totalCount"));
					}
					list.add(beans);
				}
			}
			bean.setList(list);
			return bean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

	private void userUtilsGetDataLIST(String string) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", string);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ZHONGCHOU_DETAILS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = NUMM_TAG;
							handler.sendMessage(msg);

						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Message msg = new Message();
								msg.arg1 = NUMM_TAG;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null
										&& !TextUtils.isEmpty(backdata)) {
									Message msg = new Message();
									msg.arg1 = NUMM_TAG;
									handler.sendMessage(msg);
								} else {
									Log.i("众筹",
											"走到这里了么？" + data.getNetResultCode());
									Log.i("众筹数据", backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = DATA_ISHAVE;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 主要是为了初始化
	 */
	private void useWayInitViewData() {
		// TODO Auto-generated method stub
		if (bean != null) {
			if (bean.getCrowdfundStatus().equals("2")) {
				mStatus.setText("未开始");
				mStatusBg.setImageBitmap(readBitMap(
						CrowdFundingDetailActivity.this,
						R.drawable.crowfundingndetial));
				// 调用方法，设置开始时间
				initDates("1",bean.getEndTime(),bean.getBeginTime(), mShengyuTime);
			} else if (bean.getCrowdfundStatus().equals("1")) {
				mStatus.setText("众筹中");
				mStatusBg.setImageBitmap(readBitMap(
						CrowdFundingDetailActivity.this,
						R.drawable.crowfundingdetial));
				// 调用方法，设置结束时间
				initDates("0",bean.getEndTime(),bean.getBeginTime(), mShengyuTime);
			} else if (bean.getCrowdfundStatus().equals("3")) {
				mStatus.setText("已结束");
				mStatusBg.setImageBitmap(readBitMap(
						CrowdFundingDetailActivity.this,
						R.drawable.crowfundingendetial));
				mShengyuTime.setText("0天0小时");
			}
			// 调用方法，处理众筹目标金额
			double targetmoney = checkAndChange(bean.getTargetMoney());
			mMubiaojine.setText("￥" + targetmoney);
			// //调用方法，计算已达到金额数，以及进度，以及百分比
			// countProgress(progressbar,targetmoney,bean.getHasMoney(),
			// mYidadao, mJineshu);
			// 调用方法，计算已达到金额数，以及进度，以及百分比
			countProgressed(progressbar,targetmoney, bean.getHasMoney(),
					mYidadao, mJineshu);
			String[] name = bean.getCrowdfundName().split("_");
			mCrowdName.setText(name[0]);
			mTitle.setText(name[1]);
			mCanyurenshu.setText(bean.getParticipation() + "人");
		}
	}

	/**
	 * 通过传入的已经达到的金额数和目标金额数来计算进度条占比
	 * @param mProgresssagebar
	 * @param targetmoney
	 * @param hasMoney
	 * @param mCompleatPnum 
	 * @param mComMonNum 
	 */
	private void countProgressed(MyProgressBar mProgresssagebar,
			double targetmoney, String hasMoney, TextView mCompleatPnum, TextView mComMonNum) {
		if(TextUtils.isEmpty(hasMoney))
			return;
		double hasmoney=Double.parseDouble(hasMoney);
		if(hasmoney>=0){
			double zhaanbi=(hasmoney*100)/(targetmoney*100);
			Log.i("目前占比是多少",zhaanbi+"");
			if(zhaanbi==0){
				mProgresssagebar.setProgress(0);
				mCompleatPnum.setText("0.00%");
			}else{
				mProgresssagebar.setProgress((int)(zhaanbi*100));
				double zhanbis=Utils.parseTwoNumber((zhaanbi*100)+"");
				mCompleatPnum.setText(zhanbis+"%");
			}
			double dangqianjine=Utils.parseTwoNumber(hasMoney);
			mComMonNum.setText("￥"+dangqianjine);
			mComMonNum.setText("￥"+hasmoney);
		}else{
			mProgresssagebar.setProgress(0);
			mCompleatPnum.setText("0.00%");
			mComMonNum.setText("￥0.00元");
		}
		
	}

	/**
	 * 用来处理数据，保证数据不被科学记数法写错
	 * 
	 * @param targetMoney
	 */
	private double checkAndChange(String targetMoney) {
		// TODO Auto-generated method stub
		BigDecimal bd = new BigDecimal(targetMoney);
		String str = bd.toPlainString();
		Double targetmoney = Double.parseDouble(str);
		return targetmoney;
	}

	/**
	 * 初始化表情数据
	 */
	private void initFace() {
		// 将表情map的key保存在数组中
		Set<String> keySet = FaceUtils.getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);

		lv_face = new ArrayList<View>();
		for (int i = 0; i < FaceUtils.NUM_PAGE; ++i)
			lv_face.add(getGridView(i));
		faceadapter = new FacePageAdeapter(lv_face);
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
	 * 初始化viewpager中的gridview的数据
	 * 
	 * @param i
	 * @return
	 */
	private GridView getGridView(int i) {
		GridView gv = new GridView(CrowdFundingDetailActivity.this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(CrowdFundingDetailActivity.this, i));
		gv.setOnTouchListener(forbidenScroll());

		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == FaceUtils.NUM) {// 删除键的位置
					int selection = mCommantInput.getSelectionStart();
					String text = mCommantInput.getText().toString().trim();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mCommantInput.getText().delete(start, end);
							return;
						}
						mCommantInput.getText()
								.delete(selection - 1, selection);
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
									CrowdFundingDetailActivity.this, 24);
							int newWidth = DensityUtil.dip2px(
									CrowdFundingDetailActivity.this, 24);
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,
									0, rawWidth, rawHeigh, matrix, true);
							ImageSpan imageSpan = new ImageSpan(
									CrowdFundingDetailActivity.this, newBitmap);
							String emojiStr = mFaceMapKeys.get(count);
							SpannableString spannableString = new SpannableString(
									emojiStr);
							spannableString.setSpan(imageSpan,
									emojiStr.indexOf('['),
									emojiStr.indexOf(']') + 1,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							mCommantInput.append(spannableString);
						} else {
							String ori = mCommantInput.getText().toString();
							int index = mCommantInput.getSelectionStart();
							StringBuilder stringBuilder = new StringBuilder(ori);
							stringBuilder.insert(index, mFaceMapKeys.get(count));
							mCommantInput.setText(stringBuilder.toString());
							mCommantInput.setSelection(index
									+ mFaceMapKeys.get(count).length());
						}
					}
				}
			}
		});

		return gv;
	}

	/**
	 * // 防止乱pageview乱滚动
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
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * 
	 * @param starttime 标记
	 * @param string 结束时间
	 * @param string2 开始时间
	 * @param mShengTime 控件
	 */
	private void initDates(String starttime, String string, String string2, TextView mShengTime) {
		// TODO Auto-generated method stub
		long starttimes = getChangeToTimes(string2);
		long endtime=getChangeToTimes(string);
		Date date = new Date();
		long currenttimes = date.getTime();
		Log.i("当前时间", currenttimes + "");
		Log.i("开始时间", starttimes + "");
		// 调用方法，计算两个时间之间差值
		if(starttime.equals("1")){
			String str = calculationTime(endtime, starttimes);
			mShengTime.setText(str);
		}else{
			String str = calculationTime(endtime, currenttimes);
			mShengTime.setText(str);
		}
		
	}

	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * 
	 * @param str
	 * @return
	 */
	public long getChangeToTimes(String str) {
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
	private String calculationTime(long starttimes, long currenttimes) {
		// TODO Auto-generated method stub
		// SimpleDateFormat dfs = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long between = 0;
		try {
			// java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
			// java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
			// between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
			between = Math.abs(starttimes - currenttimes);
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
		String times = day + "天" + hour + "小时";
		return times;
	}

	/**
	 * 计算金额占比
	 * 
	 * @param mProgresssagebar
	 * @param targetMoney
	 * @param hasMoney
	 * @param mCompleatPnum
	 * @param mComMonNum
	 */
	private void countProgress(MyProgressBar mProgresssagebar,
			long targetmoney, String hasMoney, TextView mCompleatPnum,
			TextView mComMonNum) {
		if (TextUtils.isEmpty(hasMoney))
			return;
		int hasmoney = Integer.parseInt(hasMoney);
		if (hasmoney >= 0) {
			float zhaanbi = (hasmoney * 100) / (targetmoney * 100);
			if (zhaanbi <= 1) {
				mProgresssagebar.setProgress(0);
				mCompleatPnum.setText("0%");
			} else {
				mProgresssagebar.setProgress((int) zhaanbi);
				mCompleatPnum.setText((int) zhaanbi + "%");
			}
			mComMonNum.setText("￥" + hasmoney + "元");
		} else {
			mProgresssagebar.setProgress(0);
			mCompleatPnum.setText("0%");
		}

	}

	/**
	 * 判断一个字符串是不是一个数值
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 初始化底部需要弹出的窗体布局
	 */
	private void initpopuwindow() {
		// TODO Auto-generated method stub
		popupView = View.inflate(CrowdFundingDetailActivity.this,
				R.layout.crowd_chat_input, null);
		pop = new PopupWindow(CrowdFundingDetailActivity.this);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		// 软键盘不会挡住popwindow
		pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		pop.setBackgroundDrawable(new BitmapDrawable());
//		pop.setFocusable(true);
		pop.setOutsideTouchable(false);
		pop.setFocusable(true);
		pop.setContentView(popupView);
//		pop.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				 TODO Auto-generated method stub
//				mLayoutInput.setVisibility(View.VISIBLE);
//			}
//		});
	
		
		
		TextView mFabiao = (TextView) popupView
				.findViewById(R.id.crowd_input_fasong);
		mFabiao.setOnClickListener(listener);
	}
	/**
	 * 初始化数据评论的数据
	 */
	private void initdatas(List<DiscussBean> listbean) {
		// TODO Auto-generated method stub
		mAdapter = new Commants2Adapter(listbean,
				CrowdFundingDetailActivity.this);
		mListviewCommant.setAdapter(mAdapter);
	}

	/**
	 * 初始化view
	 * 
	 * @2016-2-17下午1:40:51
	 */
	@SuppressLint("ResourceAsColor") private void initLayout() {
		mShowTime = (TextView) findViewById(R.id.reportback_lv_bottom_yanchuxinxi_time);
		mZhuyishixiang = (TextView) findViewById(R.id.crowdfunding_zhuyishixiang_content);
		mAddresss = (TextView) findViewById(R.id.reportback_lv_bottom_yanchuxinxi_address);
		mLayoutShare = (RelativeLayout) findViewById(R.id.fenxiang_layout);
		mLayoutShare.setOnClickListener(listener);
		mTitle = (TextView) findViewById(R.id.crowdfunding_detail_title);
		mHuibaoshuliang = (TextView) findViewById(R.id.crowdfunding_detail_huibaoNum);
		mPinlunshuliang = (TextView) findViewById(R.id.crowdfunding_detail_pinglunNum);
		mProjectInfo = (TextView) findViewById(R.id.crowdfunding_detail_jianjieContent);
		mLayoutReportBack = (LinearLayout) findViewById(R.id.zhongchou_huibaoshihouxuyaoxianshi);
		mLayoutReportBack.setVisibility(View.VISIBLE);
		mImageBackground = (ImageView) findViewById(R.id.crowdfunding_detail_top_image);
		mCheckCrowdDetail = (TextView) findViewById(R.id.crowdfunding_detail_chakantuwenxiangqing);
		mCheckCrowdDetail.setOnClickListener(listener);
		mMubiaojine = (TextView) findViewById(R.id.crowdfunding_detail_goalmoney);
		mCanyurenshu = (TextView) findViewById(R.id.crowdfunding_detail_attandpeople);
		mStatusBg = (ImageView) findViewById(R.id.crowdfunding_detail_righttop_bg);
		mShengyuTime = (TextView) findViewById(R.id.crowdfunding_detail_shengyutianshu);
		mCrowdName = (TextView) findViewById(R.id.crowdfunding_detail_name);
		mJineshu = (TextView) findViewById(R.id.crowdfunding_detail_yichoujine);
		progressbar = (MyProgressBar) findViewById(R.id.crowdfunding_detail_progressbar);
		mYidadao = (TextView) findViewById(R.id.crowdfunding_detail_yidadao);
		mStatus = (TextView) findViewById(R.id.crowdfunding_detail_righttop_status);
		mLayoutClickCommant = (RelativeLayout) findViewById(R.id.layout_tv_title_2);
		mLayoutClickReportback = (RelativeLayout) findViewById(R.id.layout_tv_title_1);
		mLine = (ImageView) findViewById(R.id.crowdfunding_detail_tv_line);
		mLine2 = (ImageView) findViewById(R.id.crowdfunding_detail_tv_line2);
		mLayoutXT = (LinearLayout) findViewById(R.id.ll_middle);
		mScrollview = (MyScrollView) findViewById(R.id.crowdfunding_scrollview);
		mScrollview.setOnScrollListener(this);
		mXuanDangqian = (LinearLayout) findViewById(R.id.crowdfunding_xuantingdangqian);
		mXuanHuadonghou = (LinearLayout) findViewById(R.id.search01);
		mListviewCommant = (Mylistview) findViewById(R.id.crowdfunding_listview_pinlun);
		mListviewCommant.setFocusable(false);
		initmListviewCommant(mListviewCommant);
		mListviewReportback = (ExpandableListView) findViewById(R.id.crowdfunding_listview_huibao);
		mListviewReportback.setFocusable(false);
		mReportBack = (TextView) findViewById(R.id.crowdfunding_detail_huibao);
		mCommants = (TextView) findViewById(R.id.crowdfunding_detail_pinglun);
		// mListview_zhuyishixiang=(Mylistview)
		// findViewById(R.id.crowdfunding_listview_zhuyishixiang);
		// mListview_zhuyishixiang.setFocusable(false);
		mLayoutInput = (LinearLayout) findViewById(R.id.crowdfunding_bottom_layout_input);
		mLayoutInput.setOnClickListener(listener);
		mCommantInput = (EditText)findViewById(R.id.discuss_input_et);
		mCommantInput.addTextChangedListener(new EditLengthOnclistener(70, mCommantInput,CrowdFundingDetailActivity.this));
		mCommantInput.setTextColor(Color.rgb(0,0,0));
		mCommantInput.setOnClickListener(listener);
		//新的布局
		RelativeLayout relayout_chart=(RelativeLayout) findViewById(R.id.chatrelative);
		relayout_chart.setBackgroundColor(Color.rgb(255,255,255));
		RelativeLayout relayout_top=(RelativeLayout) findViewById(R.id.layout_top_zhanwei);
		relayout_top.setBackgroundColor(Color.rgb(255,255,255));
		RelativeLayout relayout_bottom=(RelativeLayout) findViewById(R.id.layout_botttom_zhanwei);
		relayout_bottom.setBackgroundColor(Color.rgb(255,255,255));
		chat_more = (RelativeLayout)findViewById(R.id.chat_more);
		faceview = (ImageView)findViewById(R.id.faceview);
		faceview.setOnClickListener(listener);
		mFaceViewPager = (ViewPager)findViewById(R.id.face_pager);
		face_ll = (LinearLayout)findViewById(R.id.face_ll);
		rl_send = (TextView)findViewById(R.id.sendview);
		rl_send.setOnClickListener(listener);
		indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		mRootLayout = (RelativeLayout) findViewById(R.id.crowdfunding_root_layout);
		mRootLayout.setFocusableInTouchMode(true);
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		setCommantAndReprtListview();
		mBack = (ImageView) findViewById(R.id.crowdfunding_detail_top_back);
		mBack.setOnClickListener(listener);
		mtoplayout = (RelativeLayout) findViewById(R.id.crowdfunding_xuanting);
		inputManager = (InputMethodManager) CrowdFundingDetailActivity.this
				.getSystemService("input_method");
		int[] location = new int[2];
		mLayoutClickReportback.getLocationInWindow(location);
		int x = location[0];
		int y = location[1];
		Log.i("diyici目前的高度是", x + "" + y);
		findViewById(R.id.crowdfunding_detail_top_share).setOnClickListener(
				this);
		mCommantInput.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					chat_more.setVisibility(View.GONE);
					isShowFace = false;
				}

			}
		});
		mCommantInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = mCommantInput.getText().toString().trim();
				if (s != null && content.length() > 0) {
					// 显示
					rl_send.setVisibility(View.VISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) rl_send
							.getLayoutParams();
					// params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					int top = DensityUtil.dip2px(CrowdFundingDetailActivity.this, 10);
					params.setMargins(0, top / 2, top, top / 2);
					params.width = DensityUtil.dip2px(CrowdFundingDetailActivity.this, 35);
					rl_send.setLayoutParams(params);
				} else {
					// 隐藏
					rl_send.setVisibility(View.INVISIBLE);
					android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) rl_send
							.getLayoutParams();
					params.width = 0;
					params.setMargins(0, 0, 0, 0);
					rl_send.setLayoutParams(params);
				}

			}
		});

	}

	/**
	 * 初始化讨论获取接口数据
	 * @param mListviewCommant2
	 */
	private void initmListviewCommant(Mylistview mListviewCommant2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater) CrowdFundingDetailActivity.this.getSystemService
				(CrowdFundingDetailActivity.this.LAYOUT_INFLATER_SERVICE);
		View footview=inflater.inflate(R.layout.footview_zhongchou_taolun,null);
		mListviewCommant2.addFooterView(footview);
		mAddMoreButton=(TextView) footview.findViewById(R.id.textView_taolun_dibu_footview);
		mAddmoreProgress=(ProgressBar) findViewById(R.id.progressBar1textView_taolun_dibu_footview);
		mAddMoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAddMoreButton.setVisibility(View.GONE);
				mAddmoreProgress.setVisibility(View.VISIBLE);
				requestAllDiscuss_addmore(mDetailbeans.getId(),END_TIME_PINLUN);
			}
		});
	}

	/**
	 * 初始化该部分控件的显示
	 */
	private void setCommantAndReprtListview() {
		// TODO Auto-generated method stub
		// 程序默认进来的时候，先要显示回报部分
		mReportBack.setTextColor(Color.rgb(78, 220, 254));
		mHuibaoshuliang.setTextColor(Color.rgb(78, 220, 254));
		mCommants.setTextColor(Color.BLACK);
		mPinlunshuliang.setTextColor(Color.BLACK);
		mListviewCommant.setVisibility(View.GONE);
		mListviewReportback.setVisibility(View.VISIBLE);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			searchLayoutTop = mXuanDangqian.getTop();// 获取悬停布局的顶部位置
			Log.i("dierci当前高度", searchLayoutTop + "");
			int[] location = new int[2];
			mLayoutClickReportback.getLocationInWindow(location);
			int x = location[0];
			int y = location[1];
			Log.i("dierici目前的高度是", x + "" + y);
		}
	}

	/**
	 * 初始化列表项选择数据
	 */
	private void initData() {
		selectList = new int[] { 0, 1 };
		textViewList = new ImageView[] { mLine, mLine2 };
		mLayoutClickCommant.setOnClickListener(listener);
		mLayoutClickReportback.setOnClickListener(listener);

	}
	private OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_tv_title_1:
				Utils.hidenInputService(CrowdFundingDetailActivity.this,mLayoutClickReportback);
				if (selectID == 0) {
					return;
				} else {
					setSelectedTitle(0);
					mReportBack.setTextColor(Color.rgb(78, 220, 254));
					mHuibaoshuliang.setTextColor(Color.rgb(78, 220, 254));
					mCommants.setTextColor(Color.BLACK);
					mPinlunshuliang.setTextColor(Color.BLACK);
					mListviewCommant.setVisibility(View.GONE);
					mLayoutReportBack.setVisibility(View.VISIBLE);
					mLayoutInput.setVisibility(View.GONE);
//					pop.dismiss();
				}
				break;
			case R.id.layout_tv_title_2:
				if (selectID == 1) {
					return;
				} else {
					setSelectedTitle(1);
					mReportBack.setTextColor(Color.BLACK);
					mHuibaoshuliang.setTextColor(Color.BLACK);
					mCommants.setTextColor(Color.rgb(78, 220, 254));
					mPinlunshuliang.setTextColor(Color.rgb(78, 220, 254));
					mListviewCommant.setVisibility(View.VISIBLE);
					mLayoutReportBack.setVisibility(View.GONE);
					mLayoutInput.setVisibility(View.VISIBLE);
//					pop.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
				}
				break;
			case R.id.crowdfunding_detail_top_back:
				finish();
				break;
//			case R.id.crowdfunding_bottom_layout_input:
//				mLayoutInput.setVisibility(View.GONE);
//				pop.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
//				break;
			case R.id.faceview:
				if (!isShowFace) {
					 inputManager.hideSoftInputFromWindow(CrowdFundingDetailActivity.this
					 .getWindow().getDecorView().getWindowToken(), 0);
					chat_more.setVisibility(View.VISIBLE);
					face_ll.setVisibility(View.VISIBLE);
					faceview.setImageResource(R.drawable.jianpan);
				} else {
					chat_more.setVisibility(View.GONE);
					faceview.setImageResource(R.drawable.fasong_biaoqing);
				}
				isShowFace = !isShowFace;
				break;
			case R.id.discuss_input_et:
				chat_more.setVisibility(View.GONE);
				isShowFace = false;
				break;
			case R.id.crowdfunding_detail_chakantuwenxiangqing:
				Intent intent = new Intent(CrowdFundingDetailActivity.this,
						CrowdPictureDetailsActivity.class);
				intent.putExtra("url", URL_P_AND_W);
				intent.putExtra("shared_imager_url", shared_imager_url);
				intent.putExtra("share_rul", mDetailbeans!=null?mDetailbeans.getInfoUrl():"");
				intent.putExtra("id", bean.getId());
				if (bean != null && !TextUtils.isEmpty(bean.getCrowdfundName())) {
					intent.putExtra("name", bean.getCrowdfundName());
				}
				startActivity(intent);
				break;
			case R.id.sendview:
				// 先判断用户是否已经登陆
				if(zhongchou_tag.equals("3")){
					createDialogs("该众筹已结束，暂不能评论");
				}else{
					if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
						createDialog();
						exitDialog.show();
					} else {
						// 处理发送事务
						mDiscussStr = mCommantInput.getText().toString().trim();
						if (!TextUtils.isEmpty(mDiscussStr)) {
							// 调用方法，发送评论
							requestSendDiscuss(mDiscussStr);
						}
					}
				}
				
				break;
			case R.id.fenxiang_layout:
				// 点击时候需要做出的分享

				break;
			}
		}
	};
	/**
	 * 提示用户进行登录
	 */
	private void createDialogs(String str){
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("确定");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage(str);
		exitDialog.show();
	}

	/**
	 * 发送评论接口
	 */
	private void requestSendDiscuss(String content) {

		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", bean.getId()); // 活动id
		params.put("userId", GlobalParams.USER_ID); // 用户id
		params.put("userName", GlobalParams.USER_NAME);// 用户昵称
		params.put("content", content);// 发表内容
		params.put("dType", "3");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SEND_DISCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("DISCUSS",
											"走到这里了么？发送成功"
													+ data.getNetResultCode()
													+ backdata);
									if(data.getNetResultCode()==0){
										Message sendMessage = new Message();
										sendMessage.arg1 = 8;
										sendMessage.obj = backdata;
										handler.sendMessage(sendMessage);
									}else{
										Message sendMessage = new Message();
										sendMessage.arg1 = 81;
										handler.sendMessage(sendMessage);
									}
								}
							}

						}
					}
				}, false, false);

	}

	private void createDialog() {
		// 取消用车
		exitDialog = new CustomDialog(this,new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CrowdFundingDetailActivity.this,
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
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能进行评论哦~");
	}

	private void setSelectedTitle(int position) {
		for (int i = 0; i < selectList.length; i++) {
			if (selectList[i] == 0) {
				selectList[i] = 1;
				textViewList[i].setVisibility(View.INVISIBLE);
			}
		}
		selectList[position] = 0;
		textViewList[position].setVisibility(View.VISIBLE);
		selectID = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lesports.stadium.view.MyScrollView.OnScrollListener#onScroll(int)
	 */
	@Override
	public void onScroll(int scrollY) {
		// TODO Auto-generated method stub
		Log.i("当前高度", scrollY + "");
		if (scrollY >= 100) {
			mtoplayout.setVisibility(View.GONE);
		} else if (scrollY <= 10) {
			mtoplayout.setVisibility(View.VISIBLE);
		}
		if (scrollY >= searchLayoutTop) {
			if (mLayoutXT.getParent() != mXuanHuadonghou) {
				mXuanDangqian.removeView(mLayoutXT);
				mXuanHuadonghou.addView(mLayoutXT);
				// 显示底部布局
				// 先判断
				switch (selectID) {
				case 0:
					Log.i("当前是", "汇报");
					break;
				case 1:
					Log.i("当前是", "评论");
					mLayoutInput.setVisibility(View.VISIBLE);
//					pop.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
					break;
				default:
					break;
				}
			}
		} else {
			if (mLayoutXT.getParent() != mXuanDangqian) {
				mXuanHuadonghou.removeView(mLayoutXT);
				mXuanDangqian.addView(mLayoutXT);
				mtoplayout.setVisibility(View.VISIBLE);
				switch (selectID) {
				case 0:
					Log.i("当前是", "汇报2");
					break;
				case 1:
					Log.i("当前是", "评论2");
					mLayoutInput.setVisibility(View.GONE);
//					pop.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
//					pop.dismiss();
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * 
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * 
	 * @param resId
	 * 
	 * @return
	 */

	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;
		// 获取资源图片

		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.crowdfunding_detail_top_share:
			// 弹出分享界面
			showPop(false);
			break;

		default:
			break;
		}

	}

	/**
	 * 获取全部讨论内容
	 */
	private void requestAllDiscuss(String crowdfundId) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", crowdfundId); // 活动id
		params.put("dType", "3"); // 活动id
		params.put("rows","20");
		// params.put("userId", GlobalParams.USER_ID); //用户id
		// params.put("fileId", "1"); //宣传视屏id
		// Log.i("", "走到这里了么？");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(CrowdFundingDetailActivity.this,
									"网络错误", Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(CrowdFundingDetailActivity.this,
										"网络错误", Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(
											CrowdFundingDetailActivity.this,
											"网络异常", Toast.LENGTH_SHORT).show();
								} else {
									Log.i("DISCUSS",
											"好吧走到这里了么？"
													+ data.getNetResultCode()
													+ backdata);
									Message msg = new Message();
									msg.arg1 = 6;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}
	/**
	 * 获取全部讨论内容
	 */
	private void requestAllDiscuss_addmore(String crowdfundId,String time) {

		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", crowdfundId); // 活动id
		params.put("dType", "3"); // 活动id
		params.put("releaseMillisecond",time);
		params.put("action","2");
		params.put("rows", "20");
		// params.put("userId", GlobalParams.USER_ID); //用户id
		// params.put("fileId", "1"); //宣传视屏id
		// Log.i("", "走到这里了么？");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ALLDISSCUSS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(CrowdFundingDetailActivity.this,
									"网络错误", Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(CrowdFundingDetailActivity.this,
										"网络错误", Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(
											CrowdFundingDetailActivity.this,
											"网络异常", Toast.LENGTH_SHORT).show();
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
									if(data.getNetResultCode()==0){
										Message msg = new Message();
										msg.arg1 = 16;
										msg.obj = backdata;
										handler.sendMessage(msg);
									}else if(data.getNetResultCode()==600){
										Message msg = new Message();
										msg.arg1 = 116;
										handler.sendMessage(msg);
									}else{
										Message msg = new Message();
										msg.arg1 = 166;
										msg.obj = backdata;
										handler.sendMessage(msg);
									}
									
								}
							}

						}
					}
				}, false, false);

	}
	
	private void showPop(boolean isShow) {
		share = new SharePopupWindow(this);
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
//		 model.setImageUrl(httpurl);
		 model.setUrl(httpurl);
		 model.setText(bean.getCrowdfundName());
		 model.setTitle(bean.getCrowdfundName());
		share.initShareParams(model);
		share.setWebUrl(ConstantValue.SHARED_ZHONGCHOU+bean.getId());
		share.showShareWindow(false);
		if (isShow) {
			share.setTopShow("照片上传成功", true);
		} else {
			share.setTopShow("分享", false);
		}
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.crowdfunding_root_layout), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}
	@Override
	protected void onResume() {
		super.onResume();
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		instance = null;
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		if(mDiscussList!=null){
			mDiscussList.clear();
			mDiscussList = null;
		}
		if(lv_face!=null){
			lv_face.clear();
			lv_face = null;
		}
		if(mFaceMapKeys!=null){
			mFaceMapKeys.clear();
			mFaceMapKeys = null;
		}
		if(children!=null){
			children.clear();
			children = null;
		}
		if(groups!=null){
			groups.clear();
			groups = null;
		}
		super.onDestroy();
		ShareSDK.stopSDK(this);
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
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		if(isShowFace){
			isShowFace = false;
			chat_more.setVisibility(View.GONE);
			faceview.setImageResource(R.drawable.fasong_biaoqing);
			return;
		}
		super.onBackPressed();
	}
	

	/**
	 * 处理众筹详情数据，参数为网络请求下来的数据源
	 * @param backdata
	 */
	private void useWayuseTheData(String backdata) {
		// 调用方法，解析详情数据
		mDetailbeans= jsonWaYdATA(backdata);
		GlobalParams.ZHONGCHOU_DETAIL_IMAGE=mDetailbeans.getDetailPicture();
		if(!TextUtils.isEmpty(GlobalParams.ZHONGCHOU_DETAIL_IMAGE)){
			httpurl = ConstantValue.BASE_IMAGE_URL + GlobalParams.ZHONGCHOU_DETAIL_IMAGE;
			LApplication.loader.DisplayImage(
					ConstantValue.BASE_IMAGE_URL + GlobalParams.ZHONGCHOU_DETAIL_IMAGE+ ConstantValue.IMAGE_END
							, mImageBackground,
					R.drawable.zhongchouliebiao_zhanwei);
			shared_imager_url = ConstantValue.BASE_IMAGE_URL + GlobalParams.ZHONGCHOU_DETAIL_IMAGE+ ConstantValue.IMAGE_END;
		}
		GlobalParams.ZHONGCHOU_ORDER_IMAGE=mDetailbeans.getOrderPicture();
//		if (mDetailbeans != null && mDetailbeans.getProjectInfo() != null
//				&& !TextUtils.isEmpty(mDetailbeans.getProjectInfo())) {
			if (null!=mDetailbeans) {
			URL_P_AND_W = mDetailbeans.getProjectInfo();
			if (mDetailbeans.getProjectIntroduction() != null
					& !TextUtils.isEmpty(mDetailbeans
							.getProjectIntroduction())) {
				addDataToView(mDetailbeans.getProjectIntroduction());
				adddatatoviewsss(mDetailbeans);
			}
			//调用方法，获取该众筹评论数据个数
			useWayGetDisscussNum(mDetailbeans.getId());
			Log.e("DISCUSS", "这是众筹ID" + mDetailbeans.getId());
			// 调用方法，获取回报数据，将之加载到回报的listview中
			List<CrowReportBackBean> list = mDetailbeans.getList();
			if (list != null && list.size() != 0) {
				useDataToReportBackListview(list, bean, mDetailbeans);
				if (groups != null && groups.size() != 0
						&& children != null && children.size() != 0) {
					mHuibaoshuliang.setText(groups.size() + "");
					mReportbackAdapter = new ReportbackExpandableListViewAdapter(
							zhongchou_tag, groups, children,
							CrowdFundingDetailActivity.this);
					mListviewReportback
							.setAdapter(mReportbackAdapter);
					for (int j = 0; j < mReportbackAdapter
							.getGroupCount(); j++) {
						mListviewReportback.expandGroup(j);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
					}
					mListviewReportback
							.setOnGroupClickListener(new OnGroupClickListener() {

								@Override
								public boolean onGroupClick(
										ExpandableListView parent,
										View v, int groupPosition,
										long id) {

									return true;
								}
							});
				}
			}
		}
	}
	/**
	 * 用来处理获取评论个数成功的数据
	 * @param backdata22
	 */
	private void useWayHandlerDisscussNumData(String backdata22) {
		JSONObject obj;
		try {
			obj = new JSONObject(backdata22);
			String num=obj.getString("count");
			mPinlunshuliang.setText(num);
			requestAllDiscuss(mDetailbeans.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}
