package com.lesports.stadium.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.IntegralDetailAdapter;
import com.lesports.stadium.bean.IntegralBean;
import com.lesports.stadium.bean.IntegralDataBean;
import com.lesports.stadium.bean.IntegralTimeBean;
import com.lesports.stadium.bean.LiveDetailHighlightBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.UIUtils;

/**
 * 
 * @ClassName: IntegralDetailActivity
 * 
 * @Description: 积分详情界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-19 下午5:27:42
 * 
 * 
 */
public class IntegralDetailActivity extends Activity implements OnClickListener {
	/**
	 * 返回键
	 */
	private ImageView ib_back;
	/**
	 * 上下拉的ListView
	 */
	private PullToRefreshListView prl_integral;
	/**
	 * ListView
	 */
	private ListView mListview;
	/**
	 * 积分详情集合
	 */
	IntegralBean integralDetailList = new IntegralBean();
	/**
	 * 积分详情时间集合
	 */
	List<IntegralTimeBean> integralDetailTimeList = new ArrayList<IntegralTimeBean>();
	/**
	 * 数据总页数
	 */
	int totalPage;
	/**
	 * 当前页数
	 */
	int currentPage = 1;
	/**
	 * 总数据当前页
	 */
	private int month;
	/**
	 * handle标记
	 */
	private final int USER_INTEGRAL_SUCCESS = 100;// 用户积分查询成功
	private final int USE_INTEGRAL_DETAIL = 200;// 用户积分详情查询
	private Handler integralHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USER_INTEGRAL_SUCCESS:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					useWayHanldeIntegralData(backdata);
				}
				break;
			case USE_INTEGRAL_DETAIL:
				String backdataMoth = (String) msg.obj;
				if (!TextUtils.isEmpty(backdataMoth)) {
					useWayHandleUserIntegralDetail(backdataMoth);
				}
				break;
			case 300:
				integralDetailAdapter.notifyDataSetChanged();
				break;
			default:

				break;
			}
		}

	};
	private IntegralDetailAdapter integralDetailAdapter;

	protected void addAdapter() {
		if (null != integralDetailList && null != integralDetailTimeList) {
			integralDetailAdapter = new IntegralDetailAdapter(
					IntegralDetailActivity.this, integralDetailList,
					integralDetailTimeList);
			mListview.setAdapter(integralDetailAdapter);
		}
	}

	/**
	 * 将获得的2个数据合并
	 */
	protected void beenAll() {

		for (int i = 1 + (currentPage - 1) * 20; i < integralDetailList
				.getData().size(); i++) {
			int before = new Integer(getChangeToTime(
					integralDetailList.getData().get(i - 1).getCreateTime())
					.substring(5, 7));
			int now = new Integer(getChangeToTime(
					integralDetailList.getData().get(i).getCreateTime())
					.substring(5, 7));
			Log.e("dfaddfasfa", before + "                        " + now);
			if (before != now) {
				--month;
				integralDetailList
						.getData()
						.get(i)
						.setMonth(
								integralDetailTimeList.get(month)
										.getCreateTime());
				integralDetailList
						.getData()
						.get(i)
						.setAllIntegral(
								integralDetailTimeList.get(month).getIntegral());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_detail);
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		ib_back.setOnClickListener(this);
		// tv_rule.setOnClickListener(this);

		prl_integral.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (UIUtils.isAvailable(IntegralDetailActivity.this)) {
					loadRefreshData(1, 1);

				} else {
					// 下拉刷新加载数据
					prl_integral.onPullDownRefreshComplete();

				}
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (UIUtils.isAvailable(IntegralDetailActivity.this)) {
					// loadRefreshData(0, 1);
					if (currentPage < totalPage) {
						++currentPage;
						Map<String, String> params = new HashMap<String, String>();
						params.put("pageNo", currentPage + ""); // 第几页
						params.put("rows", "20"); // 多少条数据
						GetDataFromInternet.getInStance().interViewNet(
								ConstantValue.USER_JIFEN, params,
								new GetDatas() {

									@Override
									public void getServerData(BackData data) {
										// TODO Auto-generated method stub
										if (data == null||integralHandler==null) {
											// 说明网络获取无数据
										} else {
											Object obj = data.getObject();
											if (obj == null) {
											} else {
												String backdata = obj
														.toString();
												if (backdata == null
														|| backdata.equals("")) {
												} else {
													IntegralBean integralDetailList2 = new IntegralBean();
													integralDetailList2 = JsonUtil
															.parseJsonToBean(
																	backdata,
																	IntegralBean.class);
													List<IntegralDataBean> integralDataBean = new ArrayList<IntegralDataBean>();
													integralDataBean = integralDetailList2
															.getData();
													for (int i = 0; i < integralDataBean
															.size(); i++) {
														integralDetailList
																.getData()
																.add(integralDataBean
																		.get(i));
													}
													beenAll();
													Message msg = new Message();
													msg.what = 300;
													integralHandler
															.sendMessage(msg);

													prl_integral
															.onPullUpRefreshComplete();
												}
											}
										}
									}
								}, false, false);
					} else {
						prl_integral.onPullUpRefreshComplete();
					}

				} else {
					// 上拉刷新加载数据

					prl_integral.onPullUpRefreshComplete();
				}
			}
		});

	}

	private void initData() {
		// TODO Auto-generated method stub
		requestIntegral();

	}

	private void initView() {
		ib_back = (ImageView) findViewById(R.id.ib_back);
		// tv_rule = (TextView) findViewById(R.id.tv_rule);
		prl_integral = (PullToRefreshListView) findViewById(R.id.prl_integral);
		mListview = prl_integral.getRefreshableView();
		prl_integral.setPullLoadEnabled(true);
		prl_integral.setPullRefreshEnabled(false);

	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 *            上拉或者下拉
	 * @param j
	 *            pager的页数
	 */
	protected void loadRefreshData(int i, int j) {
		// TODO Auto-generated method stub

	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}

	/**
	 * 获取积分详情内容
	 */
	private void requestIntegral() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "1"); // 第几页
		params.put("rows", "20"); // 多少条数据
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.USER_JIFEN, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Message msg = new Message();
									msg.what = USER_INTEGRAL_SUCCESS;
									msg.obj = backdata;
									integralHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);

	}

	/**
	 * 获取数据
	 */
	private void requestIntegralMonth() {
		Map<String, String> params = new HashMap<String, String>();
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.USER_JIFEN_MONTH, null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdataMoth = obj.toString();
								// Log.e("SSSSSSSSSSS", backdataMoth);
								if (backdataMoth == null
										|| backdataMoth.equals("")) {
								} else {
									Message msg = new Message();
									msg.what = USE_INTEGRAL_DETAIL;
									msg.obj = backdataMoth;
									integralHandler.sendMessage(msg);

								}
							}
						}
					}
				}, false, false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		// case R.id.tv_rule:
		// Intent intent = new Intent(this, IntegralRuleActivity.class);
		// startActivity(intent);
		// break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (integralDetailTimeList != null) {
			integralDetailTimeList.clear();
			integralDetailTimeList = null;
		}
		if(integralHandler!=null)
			integralHandler.removeCallbacksAndMessages(null);
		integralHandler=null;
		integralDetailList = null;
		integralDetailAdapter = null;
		mListview = null;
		
		super.onDestroy();
	}

	/**
	 * 处理用户积分数据
	 * 
	 * @param backdata
	 */
	private void useWayHanldeIntegralData(String backdata) {
		// TODO Auto-generated method stub
		integralDetailList = JsonUtil.parseJsonToBean(backdata,
				IntegralBean.class);
		if (integralDetailList == null
				|| integralDetailList.getTotalPage() == null)
			return;
		totalPage = new Integer(integralDetailList.getTotalPage());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				requestIntegralMonth();
			}
		});
		thread.start();

	};

	/**
	 * 用户积分详情结果处理
	 * 
	 * @param backdataMoth
	 */
	private void useWayHandleUserIntegralDetail(String backdataMoth) {
		// TODO Auto-generated method stub
		integralDetailTimeList = (ArrayList<IntegralTimeBean>) JsonUtil
				.parseJsonToList(backdataMoth,
						new TypeToken<List<IntegralTimeBean>>() {
						}.getType());

		if (integralDetailList.getData().size() > 0) {
			month = integralDetailTimeList.size() - 1;
			// TODO Auto-generated method stub
			integralDetailList
					.getData()
					.get(0)
					.setMonth(integralDetailTimeList.get(month).getCreateTime());
			integralDetailList
					.getData()
					.get(0)
					.setAllIntegral(
							integralDetailTimeList.get(month).getIntegral());
			beenAll();
			addAdapter();
		}

	}
}
