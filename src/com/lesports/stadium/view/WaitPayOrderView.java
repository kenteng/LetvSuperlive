/**
 * 
 */
package com.lesports.stadium.view;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.AllOrderActivity;
import com.lesports.stadium.activity.BookthecarActivity;
import com.lesports.stadium.activity.OrderDetailActivity;
import com.lesports.stadium.activity.TicesWaitPayActivity;
import com.lesports.stadium.activity.WaitPayActivity;
import com.lesports.stadium.adapter.OrderExpandableListViewAdapter;
import com.lesports.stadium.bean.OrderListBean;
/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc :待支付订单页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@ResID(id = R.layout.layout_order_wait_pay_view)
public class WaitPayOrderView extends BaseView {
	/**
	 * 无数据的时候需要显示的布局
	 */
	@ResID(id = R.id.waitpay_order_layout_no_data)
	public RelativeLayout mLayoutNoData;
	/**
	 * 展示数据源的listview_全部
	 */
	@ResID(id = R.id.waitpay_all_ketuozhan_listview)
	public PullToRefreshExpandableListView mPullToRefreshExpandableListView;
	
	public ExpandableListView mListview;
	/**
	 * 数据源适配器
	 */
	private OrderExpandableListViewAdapter mAdapter;
	/**
	 * List<OrderListBean> list 全部的订单数据集合
	 */
	private List<OrderListBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	
	/**
	 * 刷新完成
	 */
	private final int REFRESHEXPANDABLELISTVIEW=100;
	private final int REFRESHEXPANDABLELISTVIEW2=200;
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case REFRESHEXPANDABLELISTVIEW2:
				mPullToRefreshExpandableListView.onRefreshComplete();
				break;
			case REFRESHEXPANDABLELISTVIEW:
				mPullToRefreshExpandableListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * @param context
	 */
	public WaitPayOrderView(final Context context) {
		super(context);
		this.mContext = context;
		initlistview();
	}

	/**
	 * 初始化可刷新的listview
	 */
	private void initlistview() {
		// TODO Auto-generated method stub
		mListview=mPullToRefreshExpandableListView.getRefreshableView();
		mPullToRefreshExpandableListView.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {


			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				// TODO Auto-generated method stub
				useWayRefrenshData_down();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				// TODO Auto-generated method stub
				useWayRefrenshData_up();
			}


		});
	}

	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData_down() {
		// TODO Auto-generated method stub
		//通知主页面进行数据更新
		/**
		 * 刷新数据的时候调用的方法
		 */
		if(AllOrderActivity.instance!=null){
			AllOrderActivity.instance.refrenshData(mPullToRefreshExpandableListView);
		}
			// TODO Auto-generated method stub
			Message message=new Message();
			message.arg1=200;
			handler.sendMessageDelayed(message, 1000);
	}
	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData_up() {
		// TODO Auto-generated method stub
		//通知主页面进行数据更新
		/**
		 * 刷新数据的时候调用的方法
		 */
			// TODO Auto-generated method stub
			Message message=new Message();
			message.arg1=100;
			handler.sendMessageDelayed(message, 1000);
	}

	/**
	 * 该方法用来通过本来对象调用来获取主界面传递过来的全部订单数据，适用于第一次加载数据的时候,加载的是全部的数据
	 */
	public void setFirstOrderData(List<OrderListBean> list) {
		this.mList = list;
		// 调用方法，来将数据加载到界面中,quanbu
		if (list != null && list.size() != 0) {
			UseWayAddDataToListview(list);
			mListview.setVisibility(View.VISIBLE);
			mLayoutNoData.setVisibility(View.GONE);
		} else {
			mLayoutNoData.setVisibility(View.VISIBLE);
			mListview.setVisibility(View.GONE);
		}

	}

	/**
	 * 该方法用来给代支付界面重新适配数据
	 */
	public void sencondData(final List<OrderListBean> list) {
		mAdapter.setList(list);
		mAdapter.notifyDataSetChanged();
		mListview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// 首先，这里需要做一个区分，先看该数据的类型是什么
				int ordertype = Integer.parseInt(list.get(groupPosition)
						.getOrdersType());
				switch (ordertype) {
				case 1:
					// 说明是商品
					int status = Integer.parseInt(list.get(groupPosition)
							.getStatus());// 判断状态
					String courser = list.get(groupPosition).getCourier();
					switch (status) {
					case 0:
						// 待付款
						Intent intent = new Intent(context,
								OrderDetailActivity.class);
						intent.putExtra("tag", 0);
						intent.putExtra("tags", "order");
						intent.putExtra("courser", courser);
						intent.putExtra("bean", list.get(groupPosition));
						context.startActivity(intent);
						break;

					default:
						break;
					}
					break;
				case 0:
					// 说明是餐饮
					int statuss = Integer.parseInt(list.get(groupPosition)
							.getStatus());// 判断状态
					String courser1 = list.get(groupPosition).getCourier();
					switch (statuss) {
					case 0:
						// 待付款
						Intent intent = new Intent(context,
								OrderDetailActivity.class);
						intent.putExtra("tag", 0);
						intent.putExtra("tags", "order");
						intent.putExtra("bean", list.get(groupPosition));
						intent.putExtra("courser", courser1);
						context.startActivity(intent);
						break;
					default:
						break;
					}
					break;
				case 3:
					// 说明是众筹
					int statusss = Integer.parseInt(list.get(groupPosition)
							.getStatus());
					switch (statusss) {
					case 0:
						// 待付款
						Intent inten = new Intent(mContext,
								WaitPayActivity.class);
						OrderListBean bean = list.get(groupPosition);
						inten.putExtra("bean", (Serializable) bean);
						inten.putExtra("tag", "0");
						inten.putExtra("other", "order");
						mContext.startActivity(inten);
						break;
					default:
						break;
					}
					break;
				case 2:
					//说明是用车
					Intent intent=new Intent(mContext,BookthecarActivity.class);
					intent.putExtra("orderid",list.get(groupPosition).getList_car().get(0).getCar_goodsName());
					mContext.startActivity(intent);
					break;
				case 4:
					//说明是购票
					int statusTices=Integer.parseInt(list.get(groupPosition).getStatus());
					switch (statusTices) {
					case 0:
						//待付款	
						Intent inten=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean bean=list.get(groupPosition);
						inten.putExtra("bean", (Serializable)bean);
						inten.putExtra("tag","0");
						inten.putExtra("other","tices");
						mContext.startActivity(inten);
						break;
					default:
						break;
					}
					break;

				default:
					break;
				}
				return false;
			}
		});

	}

	/**
	 * 该方法用来将数据加载到适配器中
	 * 
	 * @param mList2
	 */
	private void UseWayAddDataToListview(final List<OrderListBean> mList2) {
		if(mAdapter==null){
			mAdapter = new OrderExpandableListViewAdapter(mList2, context);
			mListview.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		for (int i = 0; i < mAdapter.getGroupCount(); i++) {
			mListview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}
		mListview.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				return true;
			}
		});
		mListview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Log.i("监听走了么", "子");
				// 首先，这里需要做一个区分，先看该数据的类型是什么
				int ordertype = Integer.parseInt(mList2.get(groupPosition)
						.getOrdersType());
				switch (ordertype) {
				case 1:
					// 说明是商品
					int status = Integer.parseInt(mList2.get(groupPosition)
							.getStatus());// 判断状态
					String courser = mList2.get(groupPosition).getCourier();
					switch (status) {
					case 0:
						// 待付款
						Intent intent = new Intent(context,
								OrderDetailActivity.class);
						intent.putExtra("tag", 0);
						intent.putExtra("tags", "order");
						intent.putExtra("courser", courser);
						intent.putExtra("bean", mList2.get(groupPosition));
						context.startActivity(intent);
						break;

					default:
						break;
					}
					break;
				case 0:
					// 说明是餐饮
					int statuss = Integer.parseInt(mList2.get(groupPosition)
							.getStatus());// 判断状态
					String courser1 = mList2.get(groupPosition).getCourier();
					switch (statuss) {
					case 0:
						// 待付款
						Intent intent = new Intent(context,
								OrderDetailActivity.class);
						intent.putExtra("tag", 0);
						intent.putExtra("tags", "order");
						intent.putExtra("bean", mList2.get(groupPosition));
						intent.putExtra("courser", courser1);
						context.startActivity(intent);
						break;
					default:
						break;
					}
					break;
				case 3:
					// 说明是众筹
					int statusss = Integer.parseInt(mList2.get(groupPosition)
							.getStatus());
					switch (statusss) {
					case 0:
						// 待付款
						Intent inten = new Intent(mContext,
								WaitPayActivity.class);
						OrderListBean bean = mList2.get(groupPosition);
						inten.putExtra("bean", (Serializable) bean);
						inten.putExtra("tag", "0");
						inten.putExtra("other", "order");
						mContext.startActivity(inten);
						break;
					default:
						break;
					}
					break;
				case 2:
					//说明是用车
					Intent intent=new Intent(mContext,BookthecarActivity.class);
					intent.putExtra("orderid",mList2.get(groupPosition).getList_car().get(0).getCar_goodsName());
					mContext.startActivity(intent);
					break;
				case 4:
					//说明是购票
					Log.i("购票","验证走了没有");
					int statusTices=Integer.parseInt(mList2.get(groupPosition).getStatus());
					switch (statusTices) {
					case 0:
						//待付款	
						Intent inten=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean bean=mList2.get(groupPosition);
						inten.putExtra("bean", (Serializable)bean);
						inten.putExtra("tag","0");
						inten.putExtra("other","tices");
						mContext.startActivity(inten);
						break;
					default:
						break;
					}
					break;


				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void initBaseData() {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {

	}

}
