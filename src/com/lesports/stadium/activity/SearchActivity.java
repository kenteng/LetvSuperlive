package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.SecondSenceFragmentListviewAdapter;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;

/**
 * 
 * @ClassName: SearchActivity
 * 
 * @Description: 搜索活动界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author siming
 * 
 * @date 2016-7-19 下午6:32:52
 * 
 * 
 */
public class SearchActivity extends Activity implements OnClickListener {
	/**
	 * 返回键
	 */
	private ImageView ib_back;
	/**
	 * 搜索按钮
	 */
	private TextView tv_search;
	/**
	 * 输入的文本
	 */
	private EditText et_search;
	/**
	 * 没有搜索结果显示的布局
	 */
	private RelativeLayout rl_no_result;
	/**
	 * 页面404显示的布局
	 */
	private RelativeLayout rl_404;
	/**
	 * 展示的Listview
	 */
	private ListView lv_show_search;
	/**
	 * 列表项的数据源集合
	 */
	private List<SenceBean> mList = new ArrayList<SenceBean>();
	/**
	 * 需要传递的活动id的标记
	 */
	private final String ID_TAG = "id";
	/**
	 * 列表项数据控件listview的适配器
	 */
	private SecondSenceFragmentListviewAdapter mAdapter;
	/**
	 * 搜索成功
	 */
	private final int SUCCESS = 2;
	/**
	 * 搜索失败
	 */
	private final int FILERE = 3;
	/**
	 * 软键盘管理类
	 */
	private InputMethodManager inputManager;
	/**
	 * 搜索时进度条
	 */
	private RelativeLayout pgrogress;

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case SUCCESS:
				tv_search.setClickable(true);
				tv_search.setEnabled(true);
				pgrogress.setVisibility(View.GONE);
				analyseDtae((String) msg.obj);
				break;
			case FILERE:
				tv_search.setClickable(true);
				tv_search.setEnabled(true);
				pgrogress.setVisibility(View.GONE);
				rl_no_result.setVisibility(View.VISIBLE);
				lv_show_search.setVisibility(View.GONE);
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_detail);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		inputManager = (InputMethodManager) this
				.getSystemService("input_method");
		InitView();
		InitListener();
	}

	/**
	 * 初始化监听
	 */
	private void InitListener() {
		ib_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		// 事实查询
		et_search.addTextChangedListener(new TextWatcher() {

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
				search();
			}
		});
	}

	/**
	 * 初始化view
	 */
	private void InitView() {
		ib_back = (ImageView) findViewById(R.id.ib_back);
		tv_search = (TextView) findViewById(R.id.tv_search);
		et_search = (EditText) findViewById(R.id.et_search);
		rl_no_result = (RelativeLayout) findViewById(R.id.rl_no_result);
		pgrogress = (RelativeLayout) findViewById(R.id.layout_progress);
		rl_404 = (RelativeLayout) findViewById(R.id.rl_404);
		lv_show_search = (ListView) findViewById(R.id.lv_show_search);
		et_search.requestFocus();
		et_search.setFocusable(true);
	}

	/**
	 * 获取 输入框内容进行查询
	 */
	private void search() {
		String seacrhContent = et_search.getText().toString().trim();
		if(!TextUtils.isEmpty(seacrhContent))
			userUtilsGetData(seacrhContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.tv_search:
			String content = et_search.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(getApplicationContext(), "请输入内容",
						Toast.LENGTH_SHORT).show();
				return;
			}
			inputManager.hideSoftInputFromWindow(SearchActivity.this
					.getWindow().getDecorView().getWindowToken(), 0);
			pgrogress.setVisibility(View.VISIBLE);
			tv_search.setClickable(false);
			tv_search.setEnabled(false);
			userUtilsGetData(content);
			break;

		default:
			break;
		}
	}

	/**
	 * 搜索
	 * 
	 * @param str
	 */
	private void userUtilsGetData(String str) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0,1,2");
		params.put("action", "2");
		params.put("rows", "20");
		params.put("name", str);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null&&handler!=null) {
							// 说明网络获取无数据
							String backdata = data.getObject().toString();
							Log.i("wxn", data.getNetResultCode() + "搜索。。。"
									+ backdata);
							if (!TextUtils.isEmpty(backdata)) {
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = SUCCESS;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.arg1 = FILERE;
								handler.sendMessage(msg);
							}
						}
					}
				}, false, false);
	}

	/**
	 * 用来解析该界面从网络获取到的数据
	 * 
	 * @2016-2-18下午4:45:18
	 */
	private List<SenceBean> jsonParserData(String jsonstring) {
		Log.i("wxn", "首页数据" + jsonstring);
		List<SenceBean> list = new ArrayList<SenceBean>();
		try {
			JSONArray array = new JSONArray(jsonstring);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				SenceBean bean = new SenceBean();
				JSONObject objs = array.getJSONObject(i);
				if (objs.has("camptype")) {
					bean.setCamptype(objs.getString("camptype"));
				}
				if (objs.has("cHeat")) {
					bean.setcHeat(objs.getString("cHeat"));
				}
				if (objs.has("id")) {
					bean.setId(objs.getString("id"));
				}
				if (objs.has("creattime")) {
					bean.setCreattime(objs.getString("creattime"));
				}
				if (objs.has("cStatus")) {
					bean.setcStatus(objs.getString("cStatus"));
				}
				if (objs.has("frontCoverImageURL")) {
					bean.setFrontCoverImageURL(objs
							.getString("frontCoverImageURL"));
				}
				if (objs.has("subhead")) {
					bean.setSubhead(objs.getString("subhead"));
				}
				if (objs.has("endtime")) {
					bean.setEndtime(objs.getString("endtime"));
				}
				if (objs.has("fileId")) {
					bean.setFileId(objs.getString("fileId"));
				}
				if (objs.has("title")) {
					bean.setmTitle(objs.getString("title"));
				}
				if (objs.has("selltimeend")) {
					bean.setSelltimeend(objs.getString("selltimeend"));
				}
				if (objs.has("starttime")) {
					bean.setStarttime(objs.getString("starttime"));
				}
				if (objs.has("tips")) {
					bean.setTips(objs.getString("tips"));
				}
				if (objs.has("venueId")) {
					bean.setVenueId(objs.getString("venueId"));
				}
				if (objs.has("label")) {
					bean.setLabel(objs.getString("label"));
				}
				if (objs.has("summary")) {
					bean.setSummary(objs.getString("summary"));
				}
				if (objs.has("fileUrl")) {
					bean.setFileUrl(objs.getString("fileUrl"));
				}
				if (objs.has("venueName")) {
					bean.setVenueName(objs.getString("venueName"));
				}
				if (objs.has("backgroudImageURL")) {
					bean.setBackgroudImageURL(objs
							.getString("backgroudImageURL"));
				}
				if (objs.has("resourceId")) {
					bean.setResourceId(objs.getString("resourceId"));
				}
				if (objs.has("productId")) {
					Log.i("wxn", "productId:" + objs.getString("productId"));
					bean.setProductId(objs.getString("productId"));
				}

				list.add(bean);
			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * 将网络获取的数据加载到界面当中的listview的方法
	 * 
	 * @2016-2-19上午9:35:33
	 */
	private void addDataToListview(final List<SenceBean> list,
			ListView mListview2) {

		mAdapter = new SecondSenceFragmentListviewAdapter(SearchActivity.this,
				list);
		mListview2.setAdapter(mAdapter);
		// if(isShowLast){
		// mListview2.setSelection(shouCount);
		// }
		mListview2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SenceBean bean = list.get(arg2);
				String status = bean.getcStatus();
				String id = bean.getId();
				if (status.equals("0")) {
					// 说明是未开始
					Intent intents = new Intent(SearchActivity.this,
							NoActionActivity.class);
					intents.putExtra("bean", list.get(arg2));
//					intents.putExtra("list", list.get(arg2));
//					intents.putExtra("tag", "activity");
//					intents.putExtra("productId", list.get(arg2).getProductId());
//					intents.putExtra(ID_TAG, list.get(arg2).getId());
					startActivity(intents);
				} else if (status.equals("1")) {
					Intent intent = new Intent(SearchActivity.this,
							LiveDetialActivity.class);
					intent.putExtra("id", bean.getId());
					intent.putExtra("no", "1");
					intent.putExtra("lable", bean.getLabel());
					intent.putExtra("url", bean.getFileUrl());
					// 区别音乐会和球赛 0.比赛1.音乐会
					intent.putExtra("camptype", bean.getCamptype());
					// 直播源id 用于直播
					intent.putExtra("resourceId", bean.getResourceId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(SearchActivity.this,
							LiveDetialActivity.class);
					intent.putExtra("id", bean.getId());
					intent.putExtra("no", "1");
					intent.putExtra("lable", bean.getLabel());
					intent.putExtra("url", bean.getFileUrl());
					// 区别音乐会和球赛 0.比赛1.音乐会
					intent.putExtra("camptype", bean.getCamptype());
					intent.putExtra("end", "end");
					// 直播源id 用于直播
					intent.putExtra("resourceId", bean.getResourceId());
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 解析返回来的数据
	 * 
	 * @param backdata
	 */
	@SuppressWarnings("unchecked")
	private void analyseDtae(String backdata) {
		if (!TextUtils.isEmpty(backdata)) {
			Log.i("数据", backdata);
			mList = jsonParserData(backdata);
			// 对齐排序 排序按照 进行中、未开始、已结束 顺序
			if (mList.size() > 1) {
				Collections.sort(mList, new Comparator() {
					@SuppressLint("UseValueOf")
					@Override
					public int compare(Object o1, Object o2) {
						SenceBean senceBean1 = (SenceBean) o1;
						SenceBean senceBean2 = (SenceBean) o2;
						Integer max1 = 0;
						Integer max2 = 0;
						if (senceBean1.getcStatus().equals("1")) {
							max1 = 3;
						} else if (senceBean1.getcStatus().equals("0")) {
							max1 = 2;
						} else if (senceBean1.getcStatus().equals("2")) {
							max1 = 1;
						}
						if (senceBean2.getcStatus().equals("1")) {
							max2 = 3;
						} else if (senceBean2.getcStatus().equals("0")) {
							max2 = 2;
						} else if (senceBean2.getcStatus().equals("2")) {
							max2 = 1;
						}
						//2、进行中、未开始活动，按照时间顺序排序

						//3、已结束活动，按照时间倒序排序
						if(0==max1.compareTo(max2)){
							if(max1!=2){
								return senceBean1.getStarttime().compareTo(senceBean2.getStarttime());
										
							}else{
								return senceBean2.getStarttime().compareTo(senceBean1.getStarttime());
							}
						}else{
							return max2.compareTo(max1);
						}
						
					}
				});
			}
			rl_no_result.setVisibility(View.GONE);
			lv_show_search.setVisibility(View.VISIBLE);
			addDataToListview(mList, lv_show_search);
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mList!=null)
			mList.clear();
		mList = null;
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		handler =null;
		
		super.onDestroy();
	}

}
