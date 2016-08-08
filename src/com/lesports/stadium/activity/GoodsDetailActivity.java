/**
 * 
 */
package com.lesports.stadium.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.GoodsDetailBean;
import com.lesports.stadium.bean.GoodsSpaceBean;
import com.lesports.stadium.bean.GoodsSpaceChildBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.lsyvideo.ui.utils.ScreenUtils;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyScrollView;
import com.lesports.stadium.view.MyScrollView.OnScrollListener;
import com.lesports.stadium.view.SharePopupWindow;
import com.lesports.stadium.view.XCFlowLayout;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 商品详情界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:  
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */

public class GoodsDetailActivity extends Activity implements OnScrollListener,
		OnClickListener, PlatformActionListener, Callback {
	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	private MyScrollView mScrollview;
	/**
	 * 需要悬停在屏幕顶端的布局
	 */
	private RelativeLayout mLayoutXT;
	/**
	 * 当前悬停布局所在父布局
	 */
	private RelativeLayout mXuanDangqian;
	/**
	 * 滑动后悬停布局所在布局
	 */
	private LinearLayout mXuanHuadonghou;
	/**
	 * 动态获取当前悬停布局的顶部位置
	 */
	private int searchLayoutTop;
	/**
	 * 用于展示广告轮播的viewpager
	 */
	private ViewPager mViewPager; // android-support-v4中的滑动组件

	private List<View> dots; // 图片标题正文的那些点
	/**
	 * 顶部的购物车按钮
	 */
	private RelativeLayout mBuyCart;
	/**
	 * 顶部返回按钮
	 */
	private RelativeLayout mBack;
	/**
	 * 顶部购物车数量
	 */
	private TextView mBuyCatNum;
	/**
	 * 添加到购物车
	 */
	private TextView mAddBuyCar;
	/**
	 * 商品价格
	 */
	private TextView mGoodsPrice;
	/**
	 * 商品名称
	 */
	private TextView mGoodsName;
	/**
	 * 商品说明，也就是标签
	 */
	private TextView mGoodsLable;
	/**
	 * 点击以后弹出选择颜色与分类的布局
	 */
	private RelativeLayout mLayoutYanse;
	/**
	 * 显示颜色与分类的布局
	 */
	private LinearLayout mLayoutYCfenlei;
	/**
	 * 用来标记选择颜色与尺寸的部分是否显示
	 */
	private boolean IsShow = false;
	/**
	 * 用于选择颜色的flawlayout
	 */
	private XCFlowLayout mXCFlayout;
	private CustomDialog exitDialog;
	/**
	 * 存储被选择的view的map
	 */
	private Map<Integer, View> maps;
	/**
	 * 存储被选择的view的map
	 */
	private Map<Integer, View> mapsss;
	/**
	 * 被选中的标签上所对应的id
	 */
	private String dimesion = "11";
	/**
	 * 被选中的标签上的文字
	 */
	private String color_yanse = "11";
	/**
	 * 被选中的分类标签上的文字
	 */
	private String classss = null;
	/**
	 * 定义标签，用于记录当前被选中的颜色与分类的默认值
	 */
	private final String CHOICE_TAG = "ISCHOISE";
	/**
	 * 该变量用于记录被选中 颜色与分类
	 */
	private String ColorAndClass = CHOICE_TAG;
	/**
	 * 立即购买按钮
	 */
	private Button mNowBuy;
	/**
	 * 本类实例对象
	 */
	public static GoodsDetailActivity instance;
	/**
	 * 返回键
	 */
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	// /**
	// * 进行直接购买的时候需要携带的数据源
	// */
	// private RoundGoodsBean mRoundgoodsbeans=new RoundGoodsBean();
	/**
	 * 标记组
	 */
	private final String GROUP_TAG = "group";
	/**
	 * 标记子
	 */
	private final String CHILD_TAG = "child";
	/**
	 * 轮播页面数据源
	 */
	private String[] image_data;
	/**
	 * 商品描述内容展示
	 */
	private TextView mDescribeGoods;
	/**
	 * 商品详情内的加号按钮
	 */
	private ImageView mAdd;
	/**
	 * 减号按钮
	 */
	private ImageView mJianqu;
	/**
	 * 商品数量
	 */
	private TextView mGoodsNum;
	private int currentItem = 0; // 当前图片的索引号
	private List<ImageView> imageViews; // 滑动的图片集合
	private LinearLayout layout_addview;
	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * 选择颜色的布局
	 */
	private LinearLayout mLayout_color;
	/**
	 * 选择尺寸的布局
	 */
	private LinearLayout mLayout_chicun;
	/**
	 * 添加尺寸的标签布局
	 */
	private XCFlowLayout mXCLayout_chicun;

	/**
	 * 由那个界面进入这个界面的标记
	 */
	private String tag;
	/**
	 * 定义一个标记，用来标注到底有没有获取下来商品尺寸规格参数
	 */
	private boolean isHave = false;
	/**
	 * 该变量用来标记，是否需要显示颜色选择部分
	 */
	private boolean isShowColor = false;
	/**
	 * 该变量用来标记，是否需要显示尺寸选择部分
	 */
	private boolean isShowChicun = false;
	/**
	 * 子规格集合
	 */
	private List<GoodsSpaceChildBean> mlist_child;
	/**
	 * 规格集合
	 */
	private List<GoodsSpaceBean> mlist_parent;
	/**
	 * 商品详情实体类
	 */
	private GoodsDetailBean mBeanDetail;
	/**
	 * 商品的规格id
	 */
	private String mSpaceID = "111";
	/**
	 * 商品规格所对应的图片url
	 */
	private String mSpaceImage = "zanwu";
	/**
	 * 配送费
	 */
	private TextView mPeisongfei;
	/**
	 * 顶部商品总数
	 * 
	 */
	/**
	 * 顶部分享按钮
	 */
	private ImageView mShareImage;
	private SharePopupWindow share;
	private String imageurl = "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
	private String text = "我在xxx网上看到 智能场馆app ，我们一起来玩吧";
	private String title = "智能场馆";
	/**
	 * 商品id
	 */
	private String mGoodsID;
	/**
	 * 上个界面传递过来的商品实体类
	 */
	private RoundGoodsBean mRoundBean;
	/**
	 * 商品在选定规格以后的库存数量
	 */
	private String mGoodsSpaceNum = "0";
	/**
	 * 商品规格参数
	 */
	private String mGoodsSpecifications;
	/**
	 * 商品在选定了规格以后的价格是多少
	 */
	private String mMorenGoodsGuidePrice;
	/**
	 * handle里面所需要的标记
	 */
	private final int GOODSDETAIL_SUCCESS = 1;
	private final int GOODSDETAIL_FALIED = 10;
	/**
	 * 商品规格标记
	 */
	private final String GOODS_TAG_11 = "11";
	private final String GOODS_TAG_111 = "111";
	private final String GOODS_TAG_1111 = "1111";
	/**
	 * 处理网络数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GOODSDETAIL_SUCCESS:
				String backdata = (String) msg.obj;
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
					useWayHandleGoodsDetail(backdata);
				} else {
				}
				break;
			case GOODSDETAIL_FALIED:
				Toast.makeText(GoodsDetailActivity.this, "商品详情获取失败", 0).show();
				break;
			case 5:
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
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
		instance = this;
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_goodsdetails);
		MobclickAgent.onEvent(GoodsDetailActivity.this, "GoodsDetail");
		Intent intent = getIntent();
		if (null != intent) {
			mGoodsID = intent.getStringExtra("id");
			mRoundBean = (RoundGoodsBean) intent.getSerializableExtra("bean");
			tag = intent.getStringExtra("tag");
			if (null != mRoundBean) {
				text = mRoundBean.getGoodsName();
				title = mRoundBean.getGoodsName();
			}
		}
		initview();
		if (!TextUtils.isEmpty(mGoodsID)) {
			getdataFromService(mGoodsID);
		}
	}

	/**
	 * 将数据加载到控件上
	 * 
	 * @param msRoundgoodsbean
	 */
	private void userDataToView_new(GoodsDetailBean mBeanDetail) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(mBeanDetail.getPrice())) {
			if (mBeanDetail.getPrice().contains(".")) {
				mGoodsPrice
						.setText(Utils.parseTwoNumber(mBeanDetail.getPrice())
								+ "");
			} else {
				double price = Utils.parseTwoNumber(mBeanDetail.getPrice());
				mGoodsPrice.setText(price + "");
			}
		} else {
			mGoodsPrice.setText("0.00");
		}
		mPeisongfei
				.setText(Utils.parseTwoNumber(mBeanDetail.getFreight()) + "");
		mGoodsLable.setText(mBeanDetail.getLabel());
		mGoodsName.setText(mBeanDetail.getGoodsName());
		mDescribeGoods.setText(mBeanDetail.getDescription());
	}

	/**
	 * 分拣出存储颜色的数据
	 * 
	 * @param list_child
	 */
	private List<GoodsSpaceChildBean> useWayFenjianData_color(
			List<GoodsSpaceChildBean> list_child) {
		// TODO Auto-generated method stub
		List<GoodsSpaceChildBean> list_color = new ArrayList<GoodsSpaceChildBean>();
		if (list_child != null && list_child.size() != 0) {
			String parientid = "";
			for (int i = 0; i < list_child.size(); i++) {
				if (list_child.get(i).getParentId().equals("0")) {
					// 取出标题
					if (list_child.get(i).getGoodsSpecsName().contains("颜色")) {
						parientid = list_child.get(i).getId();
					}
				}
			}
			for (int i = 0; i < list_child.size(); i++) {
				if (!TextUtils.isEmpty(parientid)
						&& parientid.equals(list_child.get(i).getParentId())) {
					list_color.add(list_child.get(i));
				}
			}
		}
		return list_color;
	}

	/**
	 * 分拣出存储颜色和尺寸的数据
	 * 
	 * @param list_child
	 */
	private List<GoodsSpaceChildBean> useWayFenjianData_chicun(
			List<GoodsSpaceChildBean> list_child) {
		// TODO Auto-generated method stub
		List<GoodsSpaceChildBean> list_space = new ArrayList<GoodsSpaceChildBean>();
		if (list_child != null && list_child.size() != 0) {
			String parient = "";
			for (int i = 0; i < list_child.size(); i++) {
				if (list_child.get(i).getParentId().equals("0")) {
					// 取出标题
					if (list_child.get(i).getGoodsSpecsName().contains("尺寸")) {
						parient = list_child.get(i).getId();
					}
				}
			}
			for (int i = 0; i < list_child.size(); i++) {
				if (!TextUtils.isEmpty(parient)
						&& parient.equals(list_child.get(i).getParentId())) {
					list_space.add(list_child.get(i));
				}
			}
			if (null != list_space) {
			}
		} else {

		}
		return list_space;
	}

	/**
	 * 将商品详情各个规格所对应的图片展示出来
	 * 
	 * @param string
	 * @param list_parent
	 * @param mlist_child2
	 */
	private void useWayAddPicture(String string,
			List<GoodsSpaceBean> list_parent,
			List<GoodsSpaceChildBean> mlist_child2) {
		// TODO Auto-generated method stub
		if (list_parent != null && list_parent.size() != 0) {
			mLayoutYanse.setVisibility(View.VISIBLE);
			isHave = true;
			// 从规格集合内部读取出颜色集合和尺寸集合
			List<GoodsSpaceChildBean> list_color = useWayFenjianData_color(mlist_child2);
			List<GoodsSpaceChildBean> list_chicun = useWayFenjianData_chicun(mlist_child2);
			if (list_color != null && list_color.size() != 0
					&& list_chicun != null && list_chicun.size() != 0) {
				// 需要将颜色选择的显示出来
				isShowColor = true;
				// 将尺寸规格加入到界面
				userDataToFlayout(list_color);
				isShowChicun = true;
				userDataToFlayout_chicun(list_chicun);
				if (list_color != null && list_color.size() != 0) {
					// 首先需要明白，要根据颜色去规格集合中取出对应的图片
					if (list_parent != null && list_parent.size() != 0) {
						Log.i("查找", list_parent.size() + "");
						List<GoodsSpaceBean> list_image = new ArrayList<GoodsSpaceBean>();
						for (int i = 0; i < list_color.size(); i++) {
							for (int j = 0; j < list_parent.size(); j++) {
								if (list_parent.get(j)
										.getGoodsSpecsCategoryIds()
										.contains(list_color.get(i).getId())) {
									list_image.add(list_parent.get(j));
									break;
								}
							}
						}
						// 循环结束
						if (list_image != null && list_image.size() != 0) {
							Log.i("查找到的数据有多少", list_image.size() + "");
						} else {
							ColorAndClass = "nohave";
							mLayoutYanse.setVisibility(View.GONE);
							isHave = false;
							image_data = new String[1];
							image_data[0] = mBeanDetail.getSmallImg();
						}
					} else {
						ColorAndClass = "nohave";
						mLayoutYanse.setVisibility(View.GONE);
						isHave = false;
						image_data = new String[1];
						image_data[0] = mBeanDetail.getSmallImg();
					}
				} else {
					ColorAndClass = "nohave";
					mLayoutYanse.setVisibility(View.GONE);
					isHave = false;
					image_data = new String[1];
					image_data[0] = mBeanDetail.getSmallImg();
					// if(image_data!=null&&image_data.length!=0){
					// initViewpagerData(GoodsDetailActivity.this);
					// initAutoScrollviewViewpager(GoodsDetailActivity.this);
					// }
				}
			} else {
				mLayoutYanse.setVisibility(View.VISIBLE);
				isHave = true;
				isShowChicun = true;
				isShowColor = true;
				ColorAndClass = "nohave";
				image_data = new String[1];
				image_data[0] = mBeanDetail.getSmallImg();
				// if(image_data!=null&&image_data.length!=0){
				// initViewpagerData(GoodsDetailActivity.this);
				// initAutoScrollviewViewpager(GoodsDetailActivity.this);
				// }
				List<GoodsSpaceChildBean> list_space = new ArrayList<GoodsSpaceChildBean>();
				GoodsSpaceChildBean bean = new GoodsSpaceChildBean();
				bean.setGoodsSpecsName("默认");
				bean.setId(GOODS_TAG_111);
				list_space.add(bean);
				userDataToFlayout_chicun(list_space);
				List<GoodsSpaceChildBean> list_colors = new ArrayList<GoodsSpaceChildBean>();
				GoodsSpaceChildBean beans = new GoodsSpaceChildBean();
				beans.setGoodsSpecsName("默认");
				beans.setId(GOODS_TAG_111);
				list_colors.add(bean);
				userDataToFlayout(list_colors);
			}

		} else {
			mLayoutYanse.setVisibility(View.VISIBLE);
			isHave = true;
			isShowChicun = true;
			isShowColor = true;
			ColorAndClass = "nohave";
			image_data = new String[1];
			image_data[0] = mBeanDetail.getSmallImg();
			// if(image_data!=null&&image_data.length!=0){
			// initViewpagerData(GoodsDetailActivity.this);
			// initAutoScrollviewViewpager(GoodsDetailActivity.this);
			// }
			List<GoodsSpaceChildBean> list_space = new ArrayList<GoodsSpaceChildBean>();
			GoodsSpaceChildBean bean = new GoodsSpaceChildBean();
			bean.setGoodsSpecsName("默认");
			bean.setId(GOODS_TAG_111);
			list_space.add(bean);
			userDataToFlayout_chicun(list_space);
			List<GoodsSpaceChildBean> list_color = new ArrayList<GoodsSpaceChildBean>();
			GoodsSpaceChildBean beans = new GoodsSpaceChildBean();
			beans.setGoodsSpecsName("默认");
			beans.setId(GOODS_TAG_111);
			list_color.add(bean);
			userDataToFlayout(list_color);

		}
		String goodsSpecsDefaultImages = mBeanDetail
				.getGoodsSpecsDefaultImages();
		if (!TextUtils.isEmpty(goodsSpecsDefaultImages)) {
			if (goodsSpecsDefaultImages.contains(",")) {
				image_data = goodsSpecsDefaultImages.split(",");
			} else {
				image_data = new String[] { goodsSpecsDefaultImages };
			}
		}

		if (image_data != null && image_data.length != 0) {
			initViewpagerData(GoodsDetailActivity.this);
			initAutoScrollviewViewpager(GoodsDetailActivity.this);
		}
	}

	/**
	 * 读取出尺寸规格数据
	 * 
	 * @param list
	 * @return
	 */
	private List<String> getdatafromGuide_chicun(List<GoodsSpaceBean> list) {
		// TODO Auto-generated method stub
		List<String> list_chicun = new ArrayList<String>();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getGoodsSpecifications() != null) {
					list_chicun.add(list.get(i).getGoodsSpecifications());
				}
			}
		}
		return list_chicun;
	}

	/**
	 * 从传入的规格参数集合内部读取出颜色数据
	 * 
	 * @param list
	 * @return
	 */
	private List<String> getdatafromGuide(List<GoodsSpaceBean> list) {
		// TODO Auto-generated method stub
		List<String> lists = new ArrayList<String>();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getGoodsSpecifications() != null) {
					lists.add(list.get(i).getGoodsSpecifications());
				}
			}
		}
		return lists;
	}

	/**
	 * 添加数据到标签选择器上面
	 * 
	 * @param msRoundgoodsbean
	 */
	private void userDataToFlayout_chicun(
			final List<GoodsSpaceChildBean> list_chicun) {
		// TODO Auto-generated method stub
		final MarginLayoutParams ml = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ml.leftMargin = 5;
		ml.rightMargin = 5;
		ml.topMargin = 5;
		ml.bottomMargin = 5;
		mapsss = new HashMap<Integer, View>();
		// //模拟假数据
		// final List<String> list=new ArrayList<String>();
		// list.add("红");
		// list.add("绿");
		// list.add("蓝");
		final int size = list_chicun.size();
		for (int i = 0; i < list_chicun.size(); i++) {
			final TextView view = new TextView(GoodsDetailActivity.this);
			view.setText(list_chicun.get(i).getGoodsSpecsName());
			view.setTextColor(Color.WHITE);
			int width = GlobalParams.WIN_WIDTH;
			view.setWidth(width / 4);
			view.setHeight(width / 12);
			view.setGravity(Gravity.CENTER);
			view.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_maintextchange));
			mapsss.put(i, view);
			final int currunt = i;
			view.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(GoodsDetailActivity.this,view.getText(),Toast.LENGTH_SHORT).show();
					// mViewPager.setCurrentItem(currunt);// 切换当前显示的图片
					// 实时获取以及刷新选择的标签
					ColorAndClass = view.getText().toString();
					// 先将标记只为被点击的下表
					changeimagebg(mXCLayout_chicun, size);
					addview(mXCLayout_chicun, mapsss, size, view.getText()
							.toString(), ml);
					// 获取id
					for (int j = 0; j < list_chicun.size(); j++) {
						if (list_chicun.get(j).getGoodsSpecsName()
								.equals(view.getText().toString())) {
							color_yanse = list_chicun.get(j).getId();
						}
					}
					// 先判断尺寸有没有选择
					if (dimesion.equals(GOODS_TAG_11)) {
						// 说明还没有选择，所以不做操作
					} else if (dimesion.equals(GOODS_TAG_111)) {
						// 说明是显示默认的
						mSpaceID = "111";
						mSpaceImage = mBeanDetail.getBigImg();
						double price = Utils.parseTwoNumber(mBeanDetail
								.getPrice());
						mGoodsPrice.setText(price + "");
						mMorenGoodsGuidePrice = mBeanDetail.getPrice();
					} else {
						useWayChuliJiage(dimesion, color_yanse);
					}
				}
			});
			mXCLayout_chicun.addView(view, ml);
		}
	}

	/**
	 * 根据选择
	 * 
	 * @param dimesion
	 * @param color_yanse
	 */
	private void useWayChuliJiage(String dimesion, String color_yanse) {
		// TODO Auto-generated method stub
		String str = dimesion + "," + color_yanse;
		if (mlist_parent != null && mlist_parent.size() != 0) {
			for (int i = 0; i < mlist_parent.size(); i++) {
				if (mlist_parent.get(i).getGoodsSpecsCategoryIds().equals(str)) {
					double price = Utils.parseTwoNumber(mlist_parent.get(i)
							.getPrice());
					mGoodsPrice.setText(price + "");
					mSpaceID = mlist_parent.get(i).getId();
					mSpaceImage = mlist_parent.get(i).getGoodsPicture();
					mGoodsSpaceNum = mlist_parent.get(i).getGoodsStock();
					mGoodsSpecifications = mlist_parent.get(i)
							.getGoodsSpecifications();
					mMorenGoodsGuidePrice = mlist_parent.get(i).getPrice();
				}
			}
		} else {
			double price = Utils.parseTwoNumber(mBeanDetail.getPrice());
			mGoodsPrice.setText(price + "");
		}
	}

	/**
	 * 添加数据到标签选择器上面
	 * 
	 * @param msRoundgoodsbean
	 */
	private void userDataToFlayout(final List<GoodsSpaceChildBean> list_color) {
		// TODO Auto-generated method stub
		final MarginLayoutParams ml = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ml.leftMargin = 5;
		ml.rightMargin = 5;
		ml.topMargin = 5;
		ml.bottomMargin = 5;
		maps = new HashMap<Integer, View>();
		// //模拟假数据
		// final List<String> list=new ArrayList<String>();
		// list.add("红");
		// list.add("绿");
		// list.add("蓝");
		final int size = list_color.size();
		for (int i = 0; i < list_color.size(); i++) {
			final TextView view = new TextView(GoodsDetailActivity.this);
			view.setText(list_color.get(i).getGoodsSpecsName());
			view.setTextColor(Color.WHITE);
			int width = GlobalParams.WIN_WIDTH;
			view.setWidth(width / 4);
			view.setHeight(width / 12);
			view.setGravity(Gravity.CENTER);
			view.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_maintextchange));
			maps.put(i, view);
			final int currunt = i;
			view.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(GoodsDetailActivity.this,view.getText(),Toast.LENGTH_SHORT).show();
					mViewPager.setCurrentItem(currunt);// 切换当前显示的图片
					// 实时获取以及刷新选择的标签
					ColorAndClass = view.getText().toString();
					// 先将标记只为被点击的下表
					changeimagebg(mXCFlayout, size);
					addview(mXCFlayout, maps, size, view.getText().toString(),
							ml);
					// 获取id
					for (int j = 0; j < list_color.size(); j++) {
						if (list_color.get(j).getGoodsSpecsName()
								.equals(view.getText().toString())) {
							dimesion = list_color.get(j).getId();
						}
					}
					// 先判断尺寸有没有选择
					if (color_yanse.equals(GOODS_TAG_11)) {
						// 说明还没有选择，所以不做操作
					} else if (color_yanse.equals(GOODS_TAG_111)) {
						// 说明是显示默认的
						mSpaceID = "111";
						mSpaceImage = mBeanDetail.getBigImg();
						double price = Utils.parseTwoNumber(mBeanDetail
								.getPrice());
						mGoodsPrice.setText(price + "");
						mMorenGoodsGuidePrice = mBeanDetail.getPrice();
					} else {
						useWayChuliJiage(dimesion, color_yanse);
					}
				}
			});
			mXCFlayout.addView(view, ml);
		}
	}

	/**
	 * @author JZKJ-LWC
	 * @date : 2015-12-26 下午6:49:48 用来给taglayout动态的添加标签布局以及设置标签颜色
	 */
	public void addview(XCFlowLayout layout, Map<Integer, View> map, int size,
			String tag, MarginLayoutParams ml) {
		layout.removeAllViews();
		for (int i = 0; i < size; i++) {
			TextView view = (TextView) map.get(i);
			if (view.getText().toString().equals(tag)) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_maintext));
				view.setTextColor(Color.rgb(0, 113, 236));
				layout.addView(view);
			} else {
				view.setTextColor(Color.rgb(255, 255, 255));
				layout.addView(view);
			}

		}

	}

	/**
	 * @author JZKJ-LWC
	 * @date : 2015-12-27 下午2:35:28 用来还原xlayout中的子view的标签背景
	 */
	public void changeimagebg(XCFlowLayout layout, int size) {

		for (int i = 0; i < size; i++) {
			layout.getChildAt(i).setBackgroundDrawable(
					getResources().getDrawable(R.drawable.bg_maintextchange));
		}
	}

	/**
	 * 该方法用来将用户选定的这个商品添加到本地的购物车数据库中
	 * 
	 * @param roundGoodsBean
	 */
	private Object insertDataToDB(RoundGoodsBean roundGoodsBean) {
		Log.i("插入数据库的时候商品数量是多少", roundGoodsBean.getmNum());
		roundGoodsBean.setGoodsusenaem(GlobalParams.USER_ID);
		Object obj = LApplication.dbBuyCar.insert(roundGoodsBean);
		return obj;
	}

	private void createDialog(String message) {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GoodsDetailActivity.this,
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
		exitDialog.setRemindMessage(message);
		exitDialog.show();
	}
	/**
	 * 初始化添加按钮
	 * 
	 * @param msRoundgoodsbean
	 */
	private void initAddview(final RoundGoodsBean msRoundgoodsbean) {
		// TODO Auto-generated method stub
		// 检查商品库存是否允许添加
		boolean is = checkTheGoodsIsHaveStorck(msRoundgoodsbean,
				GlobalParams.USER_ID);
		if (is) {
			double mgoodsspacenums = Double.parseDouble(mGoodsSpaceNum);
			mgoodsspacenums--;
			mGoodsSpaceNum = mgoodsspacenums + "";
			String ids = msRoundgoodsbean.getId();
			Log.i("这里的id是多少", ids);
			// 判断在购物车内是否存在
			boolean ishave = checkTheGoodsIsHave(ids,
					msRoundgoodsbean.getSpaceid());
			if (ishave){
				useWayChangeNum(msRoundgoodsbean);
				// Toast.makeText(getApplicationContext(), "添加成功", 0).show();
				getdataFromSQL();
			} else {
				if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
					msRoundgoodsbean.setSpaceid(mSpaceID);
					msRoundgoodsbean.setGoodsusenaem(GlobalParams.USER_ID);
					String goodsnum = mGoodsNum.getText().toString();
					msRoundgoodsbean.setmNum(goodsnum);
					insertDataToDB(msRoundgoodsbean);
					// Toast.makeText(getApplicationContext(), "添加成功",
					// 0).show();
					if (mBuyCatNum.getVisibility() == View.VISIBLE) {
					} else {
						mBuyCatNum.setVisibility(View.VISIBLE);
					}
					getdataFromSQL();
				} else {
					createDialog("登录以后才能添加哦~");
				}

			}
		} else {
			createDialogs();
		}
	}

	/**
	 * 判断该商品库存是否满足客户需求
	 * 
	 * @param roundGoodsBean
	 * @param uSER_ID
	 */
	private boolean checkTheGoodsIsHaveStorck(RoundGoodsBean roundGoodsBean,
			String uSER_ID) {
		boolean is = false;
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?",
				new String[] { uSER_ID, roundGoodsBean.getId(),
						roundGoodsBean.getSpaceid() }, null);
		if (list != null && list.size() != 0) {
			if (!TextUtils.isEmpty(roundGoodsBean.getStock())) {
				double nums = Double.parseDouble(roundGoodsBean.getStock());
				double num_list = 0;
				double num_sql = 0;
				if (!TextUtils.isEmpty(list.get(0).getmNum())) {
					num_sql = Double.parseDouble(list.get(0).getmNum());
					if (!TextUtils.isEmpty(roundGoodsBean.getmNum())) {
						String numss = mGoodsNum.getText().toString();
						num_list = Double.parseDouble(numss);
					}
					if (nums > (num_list + num_sql)) {
						is = true;
					} else {
						is = false;
					}
				}
			}
		} else {
			// 第一次点击的时候，肯定没有添加进去
			// 在添加的时候，肯定是在判断了选定规格的情况下
			// 先判断商品本身的库存量
			if (TextUtils.isEmpty(mGoodsSpaceNum)) {
				createDialogs();
				return false;
			} else {
				double uinum = Double.parseDouble(mGoodsSpaceNum);
				double num = Double.parseDouble(mGoodsNum.getText().toString());
				if (uinum >= num) {
					is = true;

				} else {
					is = false;
					createDialogs();
				}

			}

			// double goodsnum=Double.parseDouble(mRoundBean.getStock());

		}
		return is;

	}

	private void useWayChangeNum(RoundGoodsBean roundGoodsBean) {
		// TODO Auto-generated method stub
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?",
				new String[] { GlobalParams.USER_ID, roundGoodsBean.getId(),
						roundGoodsBean.getSpaceid() }, null);
		Log.i("lwc", "修改的时候查询出来了多少数据" + list.size());
		RoundGoodsBean roundGoodsBeans = list.get(0);
		int count = Integer.parseInt(roundGoodsBeans.getmNum());
		Log.i("lwc", "数据库个数" + count);
		String nums = mGoodsNum.getText().toString();
		int numss = Integer.parseInt(nums);
		Log.i("lwc", "页面个数" + numss);
		int counts = (count + numss);
		ContentValues values = new ContentValues();
		values.put("buy_mNum", counts + "");
		int i = LApplication.dbBuyCar.update(values,
				"buy_mUsename = ? AND buy_id= ? AND buy_mSpaceid= ?",
				new String[] { GlobalParams.USER_ID, roundGoodsBean.getId(),
						roundGoodsBean.getSpaceid() });
		if (i == 1) {
			if (mBuyCatNum.getVisibility() == View.VISIBLE) {
			} else {
				mBuyCatNum.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 调用方法，获取商品详情数据
	 * 
	 * @param context
	 */
	private void getdataFromService(String id) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("gId", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GOODS_DETAIL, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "商品详情界面：" + data.getObject());
						if (data == null||handler==null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								Log.i("", "走到这里了么？" + data.getNetResultCode());
								// 调用方法，解析数据
								if (data.getNetResultCode() == 0) {
									Log.i("LWC", backdata);
									// 调用方法，将数据加载到适配器中
									Message msg = new Message();
									msg.arg1 = GOODSDETAIL_SUCCESS;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = GOODSDETAIL_FALIED;
									handler.sendMessage(msg);
								}

							}
						}
					}

				}, false, false);
	}

	/**
	 * 解析商品详情如下数据，并且将至存储到集合当中
	 * 
	 * @param backdata
	 * @param context
	 */
	private GoodsDetailBean jsonDataGoodsDetail(String backdata) {
		GoodsDetailBean moundgoodsbean = new GoodsDetailBean();
		try {
			JSONObject objs = new JSONObject(backdata);
			if (objs.has("bigImg")) {
				moundgoodsbean.setBigImg(objs.getString("bigImg"));
			}
			if (objs.has("classicId")) {
				moundgoodsbean.setClassicId(objs.getString("classicId"));
			}
			if (objs.has("category")) {
				// 子实体类部分
				JSONObject objz = objs.getJSONObject("category");
				if (objz.has("cId")) {
					moundgoodsbean.setClassicId(objz.getString("cId"));
				}
				if (objz.has("classicName")) {
					moundgoodsbean
							.setClassicName(objz.getString("classicName"));
				}
			}
			if (objs.has("description")) {
				moundgoodsbean.setDescription(objs.getString("description"));
			}
			if (objs.has("createTime")) {
				moundgoodsbean.setCreateTime(objs.getString("createTime"));
			}
			if (objs.has("createTime")) {
				moundgoodsbean.setCreateTime(objs.getString("createTime"));
			}
			if (objs.has("freight")) {
				moundgoodsbean.setFreight(objs.getString("freight"));
			}
			if (objs.has("gId")) {
				moundgoodsbean.setId(objs.getString("gId"));
			}
			if (objs.has("label")) {
				moundgoodsbean.setLabel(objs.getString("label"));
			}
			if (objs.has("mediumImg")) {
				moundgoodsbean.setMediumImg(objs.getString("mediumImg"));
			}
			if (objs.has("price")) {
				moundgoodsbean.setPrice(objs.getString("price"));
			}
			if (objs.has("goodsName")) {
				moundgoodsbean.setGoodsName(objs.getString("goodsName"));
			}
			if (objs.has("referPrice")) {
				moundgoodsbean.setReferprice(objs.getString("referprice"));
			}
			if (objs.has("seller")) {
				moundgoodsbean.setSeller(objs.getString("seller"));
			}
			if (objs.has("smallImg")) {
				moundgoodsbean.setSmallImg(objs.getString("smallImg"));
			}
			if (objs.has("status")) {
				moundgoodsbean.setStatus(objs.getString("status"));
			}
			if (objs.has("stock")) {
				moundgoodsbean.setStock(objs.getString("stock"));
			}
			if (objs.has("goodsSpecifications")) {
				JSONArray array = objs.getJSONArray("goodsSpecifications");
				List<GoodsSpaceBean> list = new ArrayList<GoodsSpaceBean>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject objsss = array.getJSONObject(i);
					GoodsSpaceBean beans = new GoodsSpaceBean();
					if (objsss.has("goodsPicture")) {
						beans.setGoodsPicture(objsss.getString("goodsPicture"));
					}
					if (objsss.has("goodsSpecifications")) {
						beans.setGoodsSpecifications(objsss
								.getString("goodsSpecifications"));
					}
					if (objsss.has("goodsSpecsCategoryIds")) {
						beans.setGoodsSpecsCategoryIds(objsss
								.getString("goodsSpecsCategoryIds"));
					}
					if (objsss.has("goodsStock")) {
						beans.setGoodsStock(objsss.getString("goodsStock"));
					}
					if (objsss.has("id")) {
						beans.setId(objsss.getString("id"));
					}
					if (objsss.has("price")) {
						beans.setPrice(objsss.getString("price"));
					}
					if (objsss.has("wareID")) {
						beans.setWareID(objsss.getString("wareID"));
					}
					list.add(beans);
				}
				moundgoodsbean.setList_parent(list);
			}
			if (objs.has("goodsSpecsCategoryList")) {
				JSONArray array = objs.getJSONArray("goodsSpecsCategoryList");
				List<GoodsSpaceChildBean> list_child = new ArrayList<GoodsSpaceChildBean>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject objz = array.getJSONObject(i);
					GoodsSpaceChildBean bean = new GoodsSpaceChildBean();
					if (objz.has("goodsSpecsName")) {
						bean.setGoodsSpecsName(objz.getString("goodsSpecsName"));
					}
					if (objz.has("id")) {
						bean.setId(objz.getString("id"));
					}
					if (objz.has("parentId")) {
						bean.setParentId(objz.getString("parentId"));
					}
					if (objz.has("parentName")) {
						bean.setParentName(objz.getString("parentName"));
					}
					if (objz.has("specified")) {
						bean.setSpecified(objz.getString("specified"));
					}
					list_child.add(bean);
				}
				moundgoodsbean.setList_child(list_child);
			}
			if (objs.has("goodsSpecsDefaultImages")) {
				moundgoodsbean.setGoodsSpecsDefaultImages(objs
						.getString("goodsSpecsDefaultImages"));
			}
			if (objs.has("address")) {
				moundgoodsbean.setAddress(objs.getString("address"));
			}
			if (objs.has("companyname")) {
				moundgoodsbean.setCompanyname(objs.getString("companyname"));
			}
			if (objs.has("contact")) {
				moundgoodsbean.setContact(objs.getString("contact"));
			}
			if (objs.has("id")) {
				moundgoodsbean.setId(objs.getString("id"));
			}
			if (objs.has("imageUrl")) {
				moundgoodsbean.setImageUrl(objs.getString("imageUrl"));
			}
			if (objs.has("loginname")) {
				moundgoodsbean.setLoginname(objs.getString("loginname"));
			}
			if (objs.has("mobilephone")) {
				moundgoodsbean.setMobilephone(objs.getString("mobilephone"));
			}
			if (objs.has("telephone")) {
				moundgoodsbean.setTelephone(objs.getString("telephone"));
			}
			if(objs.has("pickup_address")){
				moundgoodsbean.setPickup_address(objs.getString("pickup_address"));
			}
			if(objs.has("pickup_remark")){
				moundgoodsbean.setPickup_remark(objs.getString("pickup_remark"));
			}
			return moundgoodsbean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return moundgoodsbean;

	}

	/**
	 * 查询数据库中是否已经存在该条目
	 * 
	 * @param string
	 * @param roundGoodsBean
	 */
	private boolean checkTheGoodsIsHave(String id, String string) {
		boolean is = false;
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?",
				new String[] { GlobalParams.USER_ID, id, string }, null);
		Log.i("lwc", "查询出来了多少数据" + list.size());
		if (list != null && list.size() != 0) {
			RoundGoodsBean bean = list.get(0);
			if (bean.getId().equals(id) && bean.getSpaceid().equals(string)) {
				is = true;
			} else {
				is = false;
			}
		}
		return is;

	}

	/**
	 * 从数据库当中获取数据，目的是为了计算数量，将之显示出来
	 */
	private void getdataFromSQL() {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
			int countt = 0;
			List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
					"buy_mUsename= ?", new String[] { GlobalParams.USER_ID },
					null);
			Log.i("lwc", "界面刷新的时候查询出来了多少数据" + list.size());
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					int num = Integer.parseInt(list.get(i).getmNum());
					countt = countt + num;
				}
				if (countt >= 99) {
					mBuyCatNum.setText(countt + "");
				} else {
					mBuyCatNum.setText(countt + "");
				}

			} else {
				mBuyCatNum.setVisibility(View.GONE);
			}

		} else {
			mBuyCatNum.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getdataFromSQL();
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			searchLayoutTop = mXuanDangqian.getTop();// 获取悬停布局的顶部位置
		}
	}

	/**
	 * 初始化控件view
	 * 
	 * @2016-2-19下午1:15:29
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mShareImage = (ImageView) findViewById(R.id.goods_detail_share_im);
		mShareImage.setOnClickListener(this);
		mPeisongfei = (TextView) findViewById(R.id.goodsdetail_peisonfei_price);
		mLayout_chicun = (LinearLayout) findViewById(R.id.goodsdetail_layout_chicunyuyanse_xuanzechicun);
		mLayout_color = (LinearLayout) findViewById(R.id.goodsdetail_layout_chicunyuyanse_xuanzeyanse);
		mXCLayout_chicun = (XCFlowLayout) findViewById(R.id.proii_xcf_chi_chicun);
		layout_addview = (LinearLayout) findViewById(R.id.layout_addview);
		mScrollview = (MyScrollView) findViewById(R.id.goodsdetails_scrollview);
		mScrollview.setOnScrollListener(this);
		mLayoutXT = (RelativeLayout) findViewById(R.id.goodsdetail_layout_topbar);
		mXuanDangqian = (RelativeLayout) findViewById(R.id.goodsdetail_layout_top);
		mXuanHuadonghou = (LinearLayout) findViewById(R.id.goodsdetail_search01);
		mViewPager = (ViewPager) findViewById(R.id.vp_goods_top_vp);
		mBuyCart = (RelativeLayout) findViewById(R.id.goodsdetail_layout_topgoutuche);
		mBuyCart.setOnClickListener(this);
		mBack = (RelativeLayout) findViewById(R.id.goodsdetail_layout_topback);
		mBack.setOnClickListener(this);
		mBuyCatNum = (TextView) findViewById(R.id.goodsdetail_tv_topgoutuchenum);
		mAddBuyCar = (TextView) findViewById(R.id.goodsdetail_button_addcar);
		mAddBuyCar.setOnClickListener(this);
		mGoodsPrice = (TextView) findViewById(R.id.goodsdetail_price);
		mGoodsLable = (TextView) findViewById(R.id.goodsdetail_shuoming);
		mGoodsLable.setVisibility(View.GONE);
		mGoodsName = (TextView) findViewById(R.id.goodsdetail_name);
		mLayoutYanse = (RelativeLayout) findViewById(R.id.goodsdetail_layout_xuanzeyansefenlei);
		mLayoutYanse.setOnClickListener(this);
		mLayoutYCfenlei = (LinearLayout) findViewById(R.id.goodsdetail_layout_chicunxuanze);
		mXCFlayout = (XCFlowLayout) findViewById(R.id.proii_xcf_chi);
		mNowBuy = (Button) findViewById(R.id.goodsdetail_button_buy);
		mNowBuy.setOnClickListener(this);
		mDescribeGoods = (TextView) findViewById(R.id.goodsdetail_layout_shangpinmiaoshuneirong);
		mAdd = (ImageView) findViewById(R.id.shangpinxiangqing_add);
		mAdd.setOnClickListener(this);
		mJianqu = (ImageView) findViewById(R.id.sahngpinxiangqing_jianqu);
		mJianqu.setVisibility(View.GONE);
		mJianqu.setOnClickListener(this);
		mGoodsNum = (TextView) findViewById(R.id.shangpinxiangqing_num);
	}

	/**
	 * 初始化轮播控件
	 * 
	 * @2016-2-26下午1:41:15
	 */
	private void initAutoScrollviewViewpager(Context context) {
		// TODO Auto-generated method stub

		mViewPager.setAdapter(new MyAdapter());
		// 设置一个监听器，当ViewPager中的页面改变时调用
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				Message msg = new Message();
				msg.arg1 = 5;
				if(handler!=null)
					handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.page_indicator_unfocused);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return image_data.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 用来初始化顶部viewpager的数据源
	 * 
	 * @param context
	 * @2016-2-26下午1:35:25
	 */
	@SuppressLint("NewApi")
	private void initViewpagerData(Context context) {
		// TODO Auto-generated method stub
		// 这里需要从网络获取轮播图片，但是这个时候先用假数据
		// int[]
		// images={R.drawable.lyric_bg,R.drawable.tiyuhaibao,R.drawable.testimagesss};
		// 到时候只需要获取到这个images，就可以直接展示轮播
		int count = image_data.length;
		dots = new ArrayList<View>();// 计算需要生成的表示点数量
		layout_addview.removeAllViews();
		for (int i = 0; i < count; i++) {
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25, 25);
			lp.setMargins(5, 0, 5, 0);
			tv.setLayoutParams(lp);
			if (i == 0) {
				tv.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.page_indicator_focused));
			} else {
				tv.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
			}

			layout_addview.addView(tv);
			dots.add(tv);
		}
		imageViews = new ArrayList<ImageView>();
		for (int H = 0; H < count; H++) {
			ImageView img = new ImageView(context);
			Log.i("加载图片的时候", image_data[H]);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+ image_data[H] + ConstantValue.IMAGE_END, img,
					R.drawable.huodongshouye_zhanwei);
			// img.setBackground(context.getResources().getDrawable(images[H]));
			img.setScaleType(ScaleType.FIT_XY);
			imageViews.add(img);
			// mImageViews[H].setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // String aString = v.getTag().toString();
			// }
			// });
		}
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
		if (scrollY >= searchLayoutTop) {
			if (mLayoutXT.getParent() != mXuanHuadonghou) {
				mXuanDangqian.removeView(mLayoutXT);
				mXuanHuadonghou.addView(mLayoutXT);
			}
		} else {
			if (mLayoutXT.getParent() != mXuanDangqian) {
				mXuanHuadonghou.removeView(mLayoutXT);
				mXuanDangqian.addView(mLayoutXT);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goodsdetail_layout_topgoutuche:
			MobclickAgent.onEvent(GoodsDetailActivity.this, "ShowCart");
			if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
				Intent intent = new Intent(GoodsDetailActivity.this,
						BuyCarActivity.class);
				intent.putExtra("tag", tag);
				startActivity(intent);
			} else {
				createDialog("只有登录以后才能查看购物车哦~");
			}

			break;
		case R.id.goodsdetail_layout_topback:
			// if(ServiceFragment.instence!=null){
			// ServiceFragment.instence.tag_views=2;
			// }
			finish();
			break;
		case R.id.goodsdetail_layout_xuanzeyansefenlei:
			if (isHave) {
				if (IsShow) {
					mLayoutYCfenlei.setVisibility(View.GONE);
					IsShow = !IsShow;
					// //判断需要哪一部分
				} else {
					// 说明是未显示状态
					mLayoutYCfenlei.setVisibility(View.VISIBLE);
					IsShow = !IsShow;
					if (isShowChicun) {
						// 说明尺寸是可以显示的
						mLayout_chicun.setVisibility(View.VISIBLE);
					} else {
						mLayout_chicun.setVisibility(View.GONE);
					}
					if (isShowColor) {
						mLayout_color.setVisibility(View.VISIBLE);
					} else {
						mLayout_color.setVisibility(View.GONE);
					}
				}
			} else {
				Toast.makeText(GoodsDetailActivity.this, "暂无商品规格参数", 0).show();
			}
			break;
		case R.id.goodsdetail_button_buy:
			// 先判断是否登陆
			MobclickAgent.onEvent(GoodsDetailActivity.this, "buy");
			if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
				// 携带商品信息，进入订单确认界面
				// 说明未存在，
				// 先判断是否选择颜色与分类
				if (dimesion.equals(GOODS_TAG_11)
						&& color_yanse.equals(GOODS_TAG_11)) {
					Toast.makeText(GoodsDetailActivity.this, "请选择颜色与分类", 0)
							.show();
				} else if (dimesion.equals(GOODS_TAG_11)
						|| color_yanse.equals(GOODS_TAG_1111)) {
					Toast.makeText(GoodsDetailActivity.this, "请选择颜色", 0).show();
				} else if (dimesion.equals(GOODS_TAG_1111)
						|| color_yanse.equals(GOODS_TAG_11)) {
					Toast.makeText(GoodsDetailActivity.this, "请选择尺寸", 0).show();
				} else {
					if (!TextUtils.isEmpty(mGoodsSpaceNum)) {
						if (mSpaceID.equals(GOODS_TAG_111)) {
							mGoodsSpaceNum = mBeanDetail.getStock();
						}
						if (TextUtils.isEmpty(mGoodsSpaceNum)) {
							createDialogs();
							return;
						}
						double nums = Double.parseDouble(mGoodsSpaceNum);
						if (nums >= 1) {
							groups.clear();
							children.clear();
							// 调用方法，转换对象
							if (mRoundBean != null) {
								mRoundBean.setSpace_image(mSpaceImage);
								mRoundBean.setSpaceid(mSpaceID);
								mRoundBean
										.setGoodsusenaem(GlobalParams.USER_ID);
								mRoundBean
										.setmGoodsSpecifications(mGoodsSpecifications);
								GroupInfo info = new GroupInfo();
								info.setId(mRoundBean.getSeller());
								info.setName(mRoundBean.getSellerName());
								info.setSelerAddress(mRoundBean
										.getPickup_address());
								info.setmFeright(mRoundBean.getFreight());
								groups.add(info);
								ProductInfo infos = new ProductInfo();
								infos.setCount(Integer.parseInt(mGoodsNum
										.getText().toString()));
								infos.setDesc(mRoundBean.getGoodsName());
								infos.setmGoodsSpaceShuoming(mGoodsSpecifications);
								infos.setSpace_id(mSpaceID);
								infos.setId(mRoundBean.getId());
								infos.setPickup_remark(mRoundBean.getPickup_remark());
								infos.setImageUrl(mRoundBean.getSpace_image());
								infos.setName(mRoundBean.getGoodsName());
								infos.setSendWay(mRoundBean.getDelivery_type());
								infos.setSeller(mRoundBean.getSeller());
								infos.setFrieight(mRoundBean.getFreight());
								if (!TextUtils.isEmpty(mMorenGoodsGuidePrice)) {
									infos.setPrice(Double
											.parseDouble(mMorenGoodsGuidePrice));
								} else {
									infos.setPrice(1);
								}
								List<ProductInfo> list = new ArrayList<ProductInfo>();
								list.add(infos);
								children.put(mRoundBean.getSeller(), list);
								if (groups != null && groups.size() != 0
										&& children != null
										&& children.size() != 0) {
									Intent intentss = new Intent(
											GoodsDetailActivity.this,
											OrderActivity.class);
									intentss.putExtra(GROUP_TAG,
											(Serializable) groups);
									intentss.putExtra(CHILD_TAG,
											(Serializable) children);
									intentss.putExtra("yunfei",
											mBeanDetail.getFreight());
									startActivity(intentss);
									groups.clear();
									children.clear();
								}
							}
						} else {
							createDialogs();
						}
					} else {
						createDialogs();
					}

				}

			} else {
				createDialog("登录以后才能购买哦~");
			}
			break;
		case R.id.shangpinxiangqing_add:
			int num = Integer.parseInt(mGoodsNum.getText().toString());
			num++;
			mGoodsNum.setText(num + "");
			if (num >= 2) {
				mJianqu.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.sahngpinxiangqing_jianqu:
			int nums = Integer.parseInt(mGoodsNum.getText().toString());
			nums--;
			mGoodsNum.setText(nums + "");
			if (nums <= 1) {
				mJianqu.setVisibility(View.GONE);
			}
			break;
		case R.id.goodsdetail_button_addcar:
			MobclickAgent.onEvent(GoodsDetailActivity.this, "AddToCart");
			// 先判断是否选择颜色与分类
			if (dimesion.equals(GOODS_TAG_11)
					&& color_yanse.equals(GOODS_TAG_11)) {
				Toast.makeText(GoodsDetailActivity.this, "请选择颜色与分类", 0).show();
			} else if (dimesion.equals(GOODS_TAG_11)
					|| color_yanse.equals(GOODS_TAG_1111)) {
				Toast.makeText(GoodsDetailActivity.this, "请选择颜色", 0).show();
			} else if (dimesion.equals(GOODS_TAG_1111)
					|| color_yanse.equals(GOODS_TAG_11)) {
				Toast.makeText(GoodsDetailActivity.this, "请选择尺寸", 0).show();
			} else {
				if (mSpaceID.equals(GOODS_TAG_111)) {
					mGoodsSpaceNum = mBeanDetail.getStock();
				}
				if (TextUtils.isEmpty(mGoodsSpaceNum)) {
					createDialogs();
					return;
				}
				// 调用方法，转换对象
				mRoundBean.setSpace_image(mSpaceImage);
				mRoundBean.setSpaceid(mSpaceID);
				mRoundBean.setGoodsusenaem(GlobalParams.USER_ID);
				mRoundBean.setmGoodsSpecifications(mGoodsSpecifications);
				initAddview(mRoundBean);
			}
			break;
		case R.id.goods_detail_share_im:
			showPop();
			break;

		default:
			break;
		}
	}

	private void showPop() {
		if (image_data.length > 0)
			imageurl = ConstantValue.BASE_IMAGE_URL + image_data[0]
					+ ConstantValue.IMAGE_END;
		share = new SharePopupWindow(this);
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
		// model.setImageUrl(imageurl);
		model.setText(text);
		model.setTitle(title);
		model.setUrl(imageurl);
		share.initShareParams(model);
		share.setWebUrl(ConstantValue.SHARED_SHANGPING + mGoodsID);
		share.showShareWindow(false);
		share.setTopShow("分享", false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(android.R.id.content),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@Override
	protected void onDestroy() {
		if (scheduledExecutorService != null)
			scheduledExecutorService.shutdown();
		if (dots != null) {
			dots.clear();
			dots = null;
		}
		if (mapsss != null) {
			mapsss.clear();
			mapsss = null;
		}
		if (maps != null) {
			maps.clear();
			maps = null;
		}
		if (groups != null) {
			groups.clear();
			groups = null;
		}
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		handler = null;
		instance = null;
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

	private void createDialogs() {
		if (exitDialog == null)
			exitDialog = new CustomDialog(new OnClickListener() {

				@Override
				public void onClick(View v) {
					exitDialog.dismiss();
				}
			});
		exitDialog.setCancelTxt("确定");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("商品库存不足，请选择其他商品加入");
		exitDialog.show();
	}

	/**
	 * 处理商品详情数据
	 * 
	 * @param backdata
	 */
	private void useWayHandleGoodsDetail(String backdata) {
		// TODO Auto-generated method stub
		mBeanDetail = jsonDataGoodsDetail(backdata);
		userDataToView_new(mBeanDetail);
		if (mBeanDetail != null) {
			mlist_child = mBeanDetail.getList_child();
			mlist_parent = mBeanDetail.getList_parent();
			useWayAddPicture(mBeanDetail.getGoodsSpecsDefaultImages(),
					mlist_parent, mlist_child);
		}
	}

}
