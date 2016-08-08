package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.MyMessageAdapter;
import com.lesports.stadium.bean.IntegralTimeBean;
import com.lesports.stadium.bean.SelfMessageBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.ACache;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.UIUtils;

/**
 * 消息通知界面
 * 
 * @author Administrator
 * 
 */
public class MessageNotifyActivity extends Activity implements OnClickListener {
	/**
	 * 编辑按钮
	 */
	private TextView tv_edit;
	/**
	 * 下部编辑栏
	 */
	private RelativeLayout rl_button;
	/**
	 * 标记为未读
	 */
	private RelativeLayout rl_unread;
	/**
	 * 删除
	 */
	private TextView tv_delete;
	/**
	 * 删除(图片)
	 */
	private ImageView iv_delete;
	/**
	 * 全选
	 */
	private CheckBox cb_isclickall;

	private PullToRefreshListView prl_message;
	private ListView mListview;
	private boolean isEdit = false;
	// 适配
	private MyMessageAdapter myMessageAdapter;

	// 总数据
	private List<SelfMessageBean> selfMessage = null;
	// json解析后的网络数据
	private List<SelfMessageBean> selfMessage0;

	private StringBuffer hasReadId = new StringBuffer();

	public static MessageNotifyActivity instance;

	private boolean isbreak = false;

	/**
	 * handle 里面所用的标记
	 */
	private final int SPEED_REQUEST_SUCCESS = 100;
	private final int DELETE_MESSAGE = 400;
	private final int UP_REFRENSH_DATA = 500;
	private final int DOWN_REFRENSH_DATA = 600;
	private final int CHANGE_WEIDU = 200;
	private final int CHANGE_YIDU = 300;
	@SuppressLint("HandlerLeak")
	private Handler messageHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SPEED_REQUEST_SUCCESS:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					useWayHandleMessage(backdata);
				}
				break;
			// 标记为未读
			case CHANGE_WEIDU:
				List<String> noReadid = (List<String>) msg.obj;
				int i;
				for (String tmp : noReadid) {
					i = new Integer(tmp);
					selfMessage.get(i).setHasReaded("0");
				}
				myMessageAdapter.notifyDataSetChanged();
				break;
			// 标记已读
			case CHANGE_YIDU:
				String location = (String) msg.obj;
				int num = new Integer(location);
				selfMessage.get(num).setHasReaded("1");
				myMessageAdapter.notifyDataSetChanged();
				break;
			// 删除
			case DELETE_MESSAGE:
				String deleteId = (String) msg.obj;
				if (!TextUtils.isEmpty(deleteId)) {
					useWayHandleDeleteMessage(deleteId);
				}
				break;
			// 上拉加载
			case UP_REFRENSH_DATA:
				String dataU = (String) msg.obj;
				if (!TextUtils.isEmpty(dataU)) {
					useWayHandleUpLoadData(dataU);
				}
				break;
			// 下拉刷新
			case DOWN_REFRENSH_DATA:
				String dataD = (String) msg.obj;
				if (!TextUtils.isEmpty(dataD)) {
					useWayHandleDownRefrenshData(dataD);
				}
				break;
			default:

				break;
			}
		}

		/**
		 * 处理下拉刷新
		 * 
		 * @param dataD
		 */
		private void useWayHandleDownRefrenshData(String dataD) {
			// TODO Auto-generated method stub
			selfMessage0 = (ArrayList<SelfMessageBean>) JsonUtil
					.parseJsonToList(dataD,
							new TypeToken<List<SelfMessageBean>>() {
							}.getType());
			if (selfMessage.addAll(0, selfMessage0)) {
				myMessageAdapter.notifyDataSetChanged();
			}
			prl_message.onPullDownRefreshComplete();
		}

		/**
		 * 上拉加载数据
		 * 
		 * @param dataU
		 */
		private void useWayHandleUpLoadData(String dataU) {
			// TODO Auto-generated method stub
			selfMessage0 = (ArrayList<SelfMessageBean>) JsonUtil
					.parseJsonToList(dataU,
							new TypeToken<List<SelfMessageBean>>() {
							}.getType());
			for (int x = 0; x < selfMessage0.size(); x++) {
				selfMessage.add(selfMessage0.get(x));
			}
			prl_message.onPullUpRefreshComplete();
			myMessageAdapter.notifyDataSetChanged();
		}

		/**
		 * 用来删除消息
		 * 
		 * @param deleteId
		 */
		private void useWayHandleDeleteMessage(String deleteId) {
			// TODO Auto-generated method stub
			String[] idArray = deleteId.split(",");
			for (int j = 0; j < selfMessage.size(); j++) {
				for (int z = 0; z < idArray.length; z++) {
					if (j >= selfMessage.size() || selfMessage.size() <= 0) {

					} else if (selfMessage.get(j).getId().equals(idArray[z])) {
						selfMessage.remove(j);
						j--;
						break;
					}
				}
			}
			myMessageAdapter.notifyDataSetChanged();
		}

		/**
		 * 处理请求下来的消息数据
		 * 
		 * @param backdata
		 */
		@SuppressWarnings("unchecked")
		private void useWayHandleMessage(String backdata) {
			// TODO Auto-generated method stub
			// selfMessage0=null;
			selfMessage = (ArrayList<SelfMessageBean>) JsonUtil
					.parseJsonToList(backdata,
							new TypeToken<List<SelfMessageBean>>() {
							}.getType());
			myMessageAdapter = new MyMessageAdapter(MessageNotifyActivity.this,
					selfMessage);
			if (mListview != null)
				mListview.setAdapter(myMessageAdapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagenotify);
		instance = this;
		initview();
		initData();
		initListener();
	}

	private void initListener() {
		findViewById(R.id.back).setOnClickListener(this);
		tv_edit.setOnClickListener(this);
		rl_unread.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		cb_isclickall.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isbreak)
					return;
				for (int i = 0; i < selfMessage.size(); i++) {
					selfMessage.get(i).setCheck(isChecked);
				}
				myMessageAdapter.notifyDataSetChanged();
			}
		});

		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				notifyHasRead(arg2);
			}
		});

		prl_message.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(MessageNotifyActivity.this)) {
					prl_message.onPullDownRefreshComplete();
				} else {
					// 下拉刷新加载数据
					if (selfMessage.size() > 0) {
						String messageId = selfMessage.get(0).getId();
						loadRefreshData(1, messageId);
					} else {
						prl_message.onPullDownRefreshComplete();
					}
				}
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(MessageNotifyActivity.this)) {
					prl_message.onPullUpRefreshComplete();
				} else {
					// 上拉刷新加载数据
					if (selfMessage.size() > 0) {
						String messageId = selfMessage.get(
								selfMessage.size() - 1).getId();
						loadRefreshData(0, messageId);
					}
				}
			}
		});

	}

	protected void loadRefreshData(final int i, String id) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		if (i == 0) {
			params.put("flag", "U");
		} else {
			params.put("flag", "D");
		}
		params.put("messageId", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MESSAGE_ALL, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据

						} else {
							Object obj = data.getObject();
							if (obj == null) {
								if (i == 0) {
									prl_message.onPullUpRefreshComplete();
								} else {
									prl_message.onPullDownRefreshComplete();
								}
							} else {
								String backdata = obj.toString();
								if (backdata == null || backdata.equals("")) {
									if (i == 0) {
										prl_message.onPullUpRefreshComplete();
									} else {
										prl_message.onPullDownRefreshComplete();
									}
								} else {
									// Log.e("MessageNotify", backdata);
									if (i == 0) {
										Message msg = new Message();
										msg.what = UP_REFRENSH_DATA;
										msg.obj = backdata;
										messageHandler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.what = DOWN_REFRENSH_DATA;
										msg.obj = backdata;
										messageHandler.sendMessage(msg);
									}
								}
							}
						}
					}
				}, false, false);

	}

	private void initData() {
		// TODO Auto-generated method stub
		requestMessage();

	}

	/**
	 * 该方法用来在消息全部选定的情况下，置换全选按钮的状态
	 */
	public void setRefrenshAllChoise(List<SelfMessageBean> messageList) {
		boolean ischeck = false;
		if (messageList != null && messageList.size() > 0) {
			for (int i = 0; i < messageList.size(); i++) {
				if (messageList.get(i).isCheck()) {
					ischeck = true;
				} else {
					ischeck = false;
					break;
				}
			}
		}
		isbreak = true;
		cb_isclickall.setChecked(ischeck);
		isbreak = false;
	}

	private void initview() {

		prl_message = (PullToRefreshListView) findViewById(R.id.prl_message);
		prl_message.setPullLoadEnabled(true);
		prl_message.setPullRefreshEnabled(true);
		mListview = prl_message.getRefreshableView();
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		tv_delete = (TextView) findViewById(R.id.tv_delete);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		rl_button = (RelativeLayout) findViewById(R.id.rl_button);
		rl_unread = (RelativeLayout) findViewById(R.id.rl_unread);
		cb_isclickall = (CheckBox) findViewById(R.id.cb_isclickall);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			finish();
			break;
		case R.id.tv_delete:
		case R.id.iv_delete:
			notifyDelete();
			break;
		case R.id.tv_edit:
			if (isEdit) {
				for (int i = 0; i < selfMessage.size(); i++) {
					selfMessage.get(i).setEdit(!isEdit);
				}
				tv_edit.setText("编辑");
				rl_button.setVisibility(View.GONE);

			} else {
				for (int i = 0; i < selfMessage.size(); i++) {
					selfMessage.get(i).setEdit(!isEdit);
				}
				tv_edit.setText("取消");
				rl_button.setVisibility(View.VISIBLE);
			}
			isEdit = !isEdit;
			myMessageAdapter.notifyDataSetChanged();
			break;
		case R.id.rl_unread:
			notifyNoRead();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	/**
	 * 第一次请求数据
	 */
	private void requestMessage() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("flag", "U"); // 多条读取后的id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MESSAGE_ALL, params, new GetDatas() {
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
								if (backdata == null || backdata.equals("")) {
								} else {
									// Log.e("MessageNotify", backdata);
									Message msg = new Message();
									msg.what = 100;
									msg.obj = backdata;
									messageHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * 将消息标记为已读
	 */
	private void notifyHasRead(final int i) {
		// TODO Auto-generated method stub
		String str = hasReadId.toString();
		Map<String, String> params = new HashMap<String, String>();
		params.put("messageId", selfMessage.get(i).getId()); // 多条读取后的id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MESSAGE_HAS_READ, params, new GetDatas() {
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
								if (backdata == null || backdata.equals("")) {
								} else {
									// Toast.makeText(MessageNotifyActivity.this,
									// "success", 0).show();
									Log.e("notifyHasReadnotifyHasRead",
											backdata);
									if ("success".equals(backdata)) {
										Message msg = new Message();
										msg.what = CHANGE_YIDU;
										msg.obj = i + "";
										messageHandler.sendMessage(msg);
									}
								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * 将标记的消息设置为未读
	 */
	private void notifyNoRead() {
		Map<String, String> params = new HashMap<String, String>();
		int firstNoRead = 0;
		StringBuffer noReadStr = new StringBuffer();
		List<String> listLocation = new ArrayList<String>();
		for (int i = 0; i < selfMessage.size(); i++) {
			if (selfMessage.get(i).isCheck()) {
				if (firstNoRead == 0) {
					noReadStr.append(selfMessage.get(i).getId());
					listLocation.add(i + "");
					++firstNoRead;
				} else {
					noReadStr.append("," + selfMessage.get(i).getId());
					listLocation.add(i + "");
				}
			}
		}
		final List<String> listLocationD = listLocation;
		String par = noReadStr.toString();
		Log.e("DDDDDDDDDDDDDDDDDDD", par);
		params.put("messageId", par); // 多条读取后的id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MESSAGE_TO_UNREAD, params, new GetDatas() {

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
								if (backdata == null || backdata.equals("")) {
								} else {
									// Toast.makeText(MessageNotifyActivity.this,
									// "success", 0).show();
									Log.e("MessageNotify", backdata);
									// 不管以前是否已读,都返回 success
									if ("success".equals(backdata)) {
										Message msg = new Message();
										msg.what = CHANGE_WEIDU;
										msg.obj = listLocationD;
										messageHandler.sendMessage(msg);
									}
								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * 将标记的消息删除
	 */
	private void notifyDelete() {
		Map<String, String> params = new HashMap<String, String>();
		int firstNoRead = 0;
		StringBuffer noReadStr = new StringBuffer();
		for (int i = 0; i < selfMessage.size(); i++) {
			if (selfMessage.get(i).isCheck()) {
				if (firstNoRead == 0) {
					noReadStr.append(selfMessage.get(i).getId());
					++firstNoRead;
				} else {
					noReadStr.append("," + selfMessage.get(i).getId());
				}
			}
		}
		final String par = noReadStr.toString();
		params.put("idString", par); // 多条读取后的id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MESSAGE_DELETE, params, new GetDatas() {

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
								Log.e("MessageNotify", backdata);
								if (backdata == null || backdata.equals("")) {
								} else {
									// Toast.makeText(MessageNotifyActivity.this,
									// "success", 0).show();

									if ("success".equals(backdata)) {
										Message msg = new Message();
										msg.what = DELETE_MESSAGE;
										msg.obj = par;
										messageHandler.sendMessage(msg);
									}
								}
							}
						}
					}
				}, false, false);
	}

	@Override
	protected void onDestroy() {
		instance = null;
		if (messageHandler != null) {
			messageHandler.removeCallbacksAndMessages(null);
		}
		if (selfMessage != null) {
			selfMessage.clear();
			selfMessage = null;
		}
		if (selfMessage0 != null) {
			selfMessage0.clear();
			selfMessage0 = null;
		}
		hasReadId = null;
		myMessageAdapter = null;
		mListview = null;
		super.onDestroy();
	}

}
