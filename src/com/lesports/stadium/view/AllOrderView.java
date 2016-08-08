/**
 * 
 */
package com.lesports.stadium.view;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.lesports.stadium.activity.OrderDetailWaitRepecipt;
import com.lesports.stadium.activity.TicesWaitPayActivity;
import com.lesports.stadium.activity.WaitPayActivity;
import com.lesports.stadium.activity.WaitTakeGoodsActivity;
import com.lesports.stadium.adapter.OrderExpandableListViewAdapter;
import com.lesports.stadium.bean.OrderListBean;


/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 所有订单页面
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
@ResID(id = R.layout.layout_allorderview)
public class AllOrderView extends BaseView {
	/**
	 * List<OrderListBean> list   全部的订单数据集合
	 */
	@SuppressWarnings("unused")
	private List<OrderListBean> mList;
	/**
	 * 无数据的时候需要显示的布局
	 */
	@ResID(id=R.id.all_order_layout_no_data)
	public RelativeLayout mLayoutNoData;
	/**
	 * 展示数据源的listview_全部
	 */
	@ResID(id=R.id.order_all_ketuozhan_listview)
	public PullToRefreshExpandableListView mPullToRefreshExpandableListView;
	
	private ExpandableListView mListview;
	/**
	 * 数据源适配器
	 */
 	private OrderExpandableListViewAdapter  mAdapter;
 	/**
	 */
 	@SuppressWarnings("unused")
	private CustomDialog exitDialog;
 	/**
 	 * 当前点击位置
 	 */
 	private int currentIndex;
 	/**
 	 * 上下文
 	 */
 	private Context mContext;
 	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 200:
				mPullToRefreshExpandableListView.onRefreshComplete();
				break;
			case 100:
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
	public AllOrderView(final Context context) {
		super(context);
		this.mContext=context;
		initviews();
	}

	/**
	 * 初始化界面控件
	 */
	private void initviews() {
		mListview=mPullToRefreshExpandableListView.getRefreshableView();
		mPullToRefreshExpandableListView.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {


			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				useWayRefrenshData_down();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ExpandableListView> refreshView) {
				useWayRefrenshData_up();
			}


		});
	}
	
	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData_down() {
		//通知主页面进行数据更新
		/**
		 * 刷新数据的时候调用的方法
		 */
		if(AllOrderActivity.instance!=null){
			AllOrderActivity.instance.refrenshData(mPullToRefreshExpandableListView);
		}
			Message message=new Message();
			message.arg1=200;
			handler.sendMessageDelayed(message, 1000);
	}
	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData_up() {
		//通知主页面进行数据更新
		/**
		 * 刷新数据的时候调用的方法
		 */
			Message message=new Message();
			message.arg1=100;
			handler.sendMessageDelayed(message, 1000);
	}

	public void setSecondData(final List<OrderListBean> list){
		mAdapter.setList(list);
		mAdapter.notifyDataSetChanged();
		Log.i("221","currentIndex..11."+currentIndex);
		mListview.setSelection(currentIndex);
		for (int i = 0; i < mAdapter.getGroupCount(); i++)
		{
			mListview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}
		mListview.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.i("监听走了么","子");
				currentIndex = groupPosition;
				//首先，这里需要做一个区分，先看该数据的类型是什么````````````````																													
				int ordertype=Integer.parseInt(list.get(groupPosition).getOrdersType());
				switch (ordertype) {
				case 1:
					//说明是商品
					int status=Integer.parseInt(list.get(groupPosition).getStatus());//判断状态
					String courser=list.get(groupPosition).getCourier();
					switch (status) {
					case 0:
						//待付款
						Intent intent=new Intent(context,OrderDetailActivity.class);
						intent.putExtra("tag",0);
						intent.putExtra("tags","order");
						intent.putExtra("courser",courser);
						intent.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent);
						break;
					case 1:
						//待发货
						Intent intent1=new Intent(context,OrderDetailActivity.class);
						intent1.putExtra("tag",1);
						intent1.putExtra("tags","order");
						intent1.putExtra("courser",courser);
						intent1.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent1);
						break;
					case 2:
						//待收货
						Intent intent2=new Intent(context,OrderDetailActivity.class);
						intent2.putExtra("tag",2);
						intent2.putExtra("tags","order");
						intent2.putExtra("courser",courser);
						intent2.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent2);
						break;
					case 3:
						//送货完成状态，送货的最后一个状态值
						Intent intent3=new Intent(context,OrderDetailActivity.class);
						intent3.putExtra("tag",3);
						intent3.putExtra("tags","order");
						intent3.putExtra("courser",courser);
						intent3.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent3);
						break;
					case 4:
						//自取完成，最后一个状态
						Intent intent4=new Intent(context,OrderDetailActivity.class);
						intent4.putExtra("tag",4);
						intent4.putExtra("tags","order");
						intent4.putExtra("courser",courser);
						intent4.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent4);
						break;
						
					default:
						break;
					}
					break;
				case 0:
					//说明是餐饮
					int statuss=Integer.parseInt(list.get(groupPosition).getStatus());//判断状态
					String courser1=list.get(groupPosition).getCourier();
					switch (statuss) {
					case 0:
						//待付款
						Intent intent=new Intent(context,OrderDetailActivity.class);
						intent.putExtra("tag",0);
						intent.putExtra("tags","order");
						intent.putExtra("bean",list.get(groupPosition));
						intent.putExtra("courser",courser1);
						context.startActivity(intent);
						break;
					case 1:
						//待发货
						Intent intent1=new Intent(context,OrderDetailActivity.class);
						intent1.putExtra("tag",1);
						intent1.putExtra("tags","order");
						intent1.putExtra("courser",courser1);
						intent1.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent1);
						break;
					case 2:
						//待收货
						Intent intent2=new Intent(context,OrderDetailActivity.class);
						intent2.putExtra("tag",2);
						intent2.putExtra("tags","order");
						intent2.putExtra("courser",courser1);
						intent2.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent2);
						break;
					case 3:
						Intent intent12=new Intent(context,OrderDetailActivity.class);
						intent12.putExtra("tag",3);
						intent12.putExtra("tags","order");
						intent12.putExtra("courser",courser1);
						intent12.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent12);
				        break;
				    case 4:
				    	Intent intent112=new Intent(context,OrderDetailActivity.class);
						intent112.putExtra("tag",4);
						intent112.putExtra("tags","order");
						intent112.putExtra("courser",courser1);
						intent112.putExtra("bean",list.get(groupPosition));
						context.startActivity(intent112);
			            break;
			            
			            /*退款状态*
			             * dysdys
			             * 
			             * */
				    case 5:
				    	//系统取消订单，也就是退款成功
				    	Intent intent5=new Intent(context, OrderDetailActivity.class);
				    	intent5.putExtra("tag",5);
				    	intent5.putExtra("tags","order");
				    	intent5.putExtra("courser",courser1);
				    	intent5.putExtra("tuikuan","xys");
				    	intent5.putExtra("bean",list.get(groupPosition));
				    	context.startActivity(intent5);
				    	break;
				    case 6:
				    	//卖家取消订单
				    	Intent intent6=new Intent(context, OrderDetailActivity.class);
				    	intent6.putExtra("tag",6);
				    	intent6.putExtra("tags","order");
				    	intent6.putExtra("courser",courser1);
				    	intent6.putExtra("tuikuan","xys");
				    	intent6.putExtra("bean",list.get(groupPosition));
				    	context.startActivity(intent6);
				    	break;
			        case 7 ://买家取消订单 10中之内退款,并且是已经退款成功
			        	Intent intent72=new Intent(context,OrderDetailActivity.class);
						intent72.putExtra("tags","order");
						intent72.putExtra("tag",7);
						intent72.putExtra("bean",list.get(groupPosition));
						intent72.putExtra("tuikuan","xys");
						intent72.putExtra("courser",courser1);
						context.startActivity(intent72);
			            break;        
					case 8:
						//退款申请，这是属于大于十分钟的退款ding'dan
						Intent intent82=new Intent(context,OrderDetailActivity.class);
						intent82.putExtra("tags","order");
						intent82.putExtra("tag",8);
						intent82.putExtra("bean",list.get(groupPosition));
						intent82.putExtra("tuikuan","dys");
						intent82.putExtra("courser",courser1);
						context.startActivity(intent82);
						break;
					case 9:
						Intent intent92=new Intent(context,OrderDetailActivity.class);
						intent92.putExtra("tags","order");
						intent92.putExtra("tag",9);
						intent92.putExtra("bean",list.get(groupPosition));
						intent92.putExtra("tuikuan","dys");
						intent92.putExtra("courser",courser1);
						context.startActivity(intent92);
						break;
					case 10:
						Intent intent1112=new Intent(context,OrderDetailActivity.class);
						intent1112.putExtra("tags","order");
						intent1112.putExtra("tag",10);
						intent1112.putExtra("bean",list.get(groupPosition));
						intent1112.putExtra("tuikuan","dys");
						intent1112.putExtra("courser",courser1);
						context.startActivity(intent1112);
						break;
					case 11:
						Intent intent1222=new Intent(context,OrderDetailActivity.class);
						intent1222.putExtra("tags","order");
						intent1222.putExtra("tag",11);
						intent1222.putExtra("bean",list.get(groupPosition));
						intent1222.putExtra("tuikuan","dys");
						intent1222.putExtra("courser",courser1);
						context.startActivity(intent1222);
						break;

					default:
						break;
					}
					break;
				case 3:
					//说明是众筹
					int statusss=Integer.parseInt(list.get(groupPosition).getStatus());
					switch (statusss) {
					case 0:
						//待付款
						Intent inten=new Intent(mContext,WaitPayActivity.class);
						OrderListBean bean=list.get(groupPosition);
						inten.putExtra("bean", (Serializable)bean);
						inten.putExtra("tag","0");
						inten.putExtra("other","order");
						mContext.startActivity(inten);
						break;
					case 1:
						//待发货
						Intent intens=new Intent(mContext,OrderDetailWaitRepecipt.class);
						OrderListBean beans=list.get(groupPosition);
						intens.putExtra("bean", (Serializable)beans);
						intens.putExtra("tag","1");
						intens.putExtra("other","order");
						mContext.startActivity(intens);
						break;
					case 2:
						//待收货
						Intent intenss=new Intent(mContext,WaitTakeGoodsActivity.class);
						OrderListBean beanss=list.get(groupPosition);
						intenss.putExtra("bean", (Serializable)beanss);
						intenss.putExtra("tag","2");
						intenss.putExtra("other","order");
						mContext.startActivity(intenss);
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
					Log.i("购票","验证走了没有");
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
					case 1:
						//待发货
						Intent intens=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean beans=list.get(groupPosition);
						intens.putExtra("bean", (Serializable)beans);
						intens.putExtra("tag","1");
						intens.putExtra("other","tices");
						mContext.startActivity(intens);
						break;
					case 2:
						//待收货
						Intent intenss=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean beanss=list.get(groupPosition);
						intenss.putExtra("bean", (Serializable)beanss);
						intenss.putExtra("tag","2");
						intenss.putExtra("other","tices");
						mContext.startActivity(intenss);
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
	 * 该方法用来通过本来对象调用来获取主界面传递过来的全部订单数据，适用于第一次加载数据的时候,加载的是全部的数据
	 */
	public void setFirstOrderData(List<OrderListBean> list){
		this.mList=list;
			//调用方法，来将数据加载到界面中,quanbu
			if(list!=null&&list.size()!=0){
				UseWayAddDataToListview(list);
				mListview.setVisibility(View.VISIBLE);
			}

		
	}
	/**
	 * 该方法用来将数据加载到适配器中
	 * @param mList2
	 */
	private void UseWayAddDataToListview(final List<OrderListBean> mList2) {
		if(mAdapter==null){
			mAdapter=new OrderExpandableListViewAdapter(mList2, context);
			mListview.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		Log.i("221","currentIndex..."+currentIndex);
//		mListview.setSelection(currentIndex);
		for (int i = 0; i < mAdapter.getGroupCount(); i++)
		{
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
//				Log.i("监听走了么","子");
				currentIndex = groupPosition;
				//首先，这里需要做一个区分，先看该数据的类型是什么````````````````																													
				int ordertype=Integer.parseInt(mList2.get(groupPosition).getOrdersType());
				switch (ordertype) {
				case 1:
					//说明是商品
					int status=Integer.parseInt(mList2.get(groupPosition).getStatus());//判断状态
					String courser=mList2.get(groupPosition).getCourier();
					switch (status) {
					case 0:
						//待付款
						Intent intent=new Intent(context,OrderDetailActivity.class);
						intent.putExtra("tag",0);
						intent.putExtra("tags","order");
						intent.putExtra("courser",courser);
						intent.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent);
						break;
					case 1:
						//待发货
						Intent intent1=new Intent(context,OrderDetailActivity.class);
						intent1.putExtra("tag",1);
						intent1.putExtra("tags","order");
						intent1.putExtra("courser",courser);
						intent1.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent1);
						break;
					case 2:
						//待收货
						Intent intent2=new Intent(context,OrderDetailActivity.class);
						intent2.putExtra("tag",2);
						intent2.putExtra("tags","order");
						intent2.putExtra("courser",courser);
						intent2.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent2);
						break;
					case 3:
						//送货完成状态，送货的最后一个状态值
						Intent intent3=new Intent(context,OrderDetailActivity.class);
						intent3.putExtra("tag",3);
						intent3.putExtra("tags","order");
						intent3.putExtra("courser",courser);
						intent3.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent3);
						break;
					case 4:
						//自取完成，最后一个状态
						Intent intent4=new Intent(context,OrderDetailActivity.class);
						intent4.putExtra("tag",4);
						intent4.putExtra("tags","order");
						intent4.putExtra("courser",courser);
						intent4.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent4);
						break;
					default:
						break;
					}
					break;
				case 0:
					//说明是餐饮
					int statuss=Integer.parseInt(mList2.get(groupPosition).getStatus());//判断状态
					String courser1=mList2.get(groupPosition).getCourier();
					switch (statuss) {
					case 0:
						//待付款
						Intent intent=new Intent(context,OrderDetailActivity.class);
						intent.putExtra("tag",0);
						intent.putExtra("tags","order");
						intent.putExtra("bean",mList2.get(groupPosition));
						intent.putExtra("courser",courser1);
						context.startActivity(intent);
						break;
					case 1:
						//待发货
						Intent intent1=new Intent(context,OrderDetailActivity.class);
						intent1.putExtra("tag",1);
						intent1.putExtra("tags","order");
						intent1.putExtra("courser",courser1);
						intent1.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent1);
						break;
					case 2:
						//待收货
						Intent intent2=new Intent(context,OrderDetailActivity.class);
						intent2.putExtra("tag",2);
						intent2.putExtra("tags","order");
						intent2.putExtra("courser",courser1);
						intent2.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent2);
						break;
					case 3:
						Intent intent12=new Intent(context,OrderDetailActivity.class);
						intent12.putExtra("tag",3);
						intent12.putExtra("tags","order");
						intent12.putExtra("courser",courser1);
						intent12.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent12);
				        break;
				    case 4:
				    	Intent intent112=new Intent(context,OrderDetailActivity.class);
						intent112.putExtra("tag",4);
						intent112.putExtra("tags","order");
						intent112.putExtra("courser",courser1);
						intent112.putExtra("bean",mList2.get(groupPosition));
						context.startActivity(intent112);
			            break;
			            /*退款状态*/
			            
				    case 5:
				    	//系统取消订单，也就是退款成功
				    	Intent intent5=new Intent(context, OrderDetailActivity.class);
				    	intent5.putExtra("tag",5);
				    	intent5.putExtra("tags","order");
				    	intent5.putExtra("courser",courser1);
				    	intent5.putExtra("tuikuan","xys");
				    	intent5.putExtra("bean",mList2.get(groupPosition));
				    	context.startActivity(intent5);
				    	break;
				    case 6:
				    	//卖家取消订单
				    	Intent intent6=new Intent(context, OrderDetailActivity.class);
				    	intent6.putExtra("tag",6);
				    	intent6.putExtra("tags","order");
				    	intent6.putExtra("courser",courser1);
				    	intent6.putExtra("tuikuan","xys");
				    	intent6.putExtra("bean",mList2.get(groupPosition));
				    	context.startActivity(intent6);
				    	break;
			        case 7 ://买家取消订单 10中之内退款,并且是已经退款成功
			        	Intent intent72=new Intent(context,OrderDetailActivity.class);
						intent72.putExtra("tags","order");
						intent72.putExtra("tag",7);
						intent72.putExtra("bean",mList2.get(groupPosition));
						intent72.putExtra("tuikuan","xys");
						intent72.putExtra("courser",courser1);
						context.startActivity(intent72);
			            break;        
					case 8:
						//退款申请，这是属于大于十分钟的退款ding'dan
						Intent intent82=new Intent(context,OrderDetailActivity.class);
						intent82.putExtra("tags","order");
						intent82.putExtra("tag",8);
						intent82.putExtra("bean",mList2.get(groupPosition));
						intent82.putExtra("tuikuan","dys");
						intent82.putExtra("courser",courser1);
						context.startActivity(intent82);
						break;
					case 9:
						Intent intent92=new Intent(context,OrderDetailActivity.class);
						intent92.putExtra("tags","order");
						intent92.putExtra("tag",9);
						intent92.putExtra("bean",mList2.get(groupPosition));
						intent92.putExtra("tuikuan","dys");
						intent92.putExtra("courser",courser1);
						context.startActivity(intent92);
						break;
					case 10:
						Intent intent1112=new Intent(context,OrderDetailActivity.class);
						intent1112.putExtra("tags","order");
						intent1112.putExtra("tag",10);
						intent1112.putExtra("bean",mList2.get(groupPosition));
						intent1112.putExtra("tuikuan","dys");
						intent1112.putExtra("courser",courser1);
						context.startActivity(intent1112);
						break;
					case 11:
						Intent intent1222=new Intent(context,OrderDetailActivity.class);
						intent1222.putExtra("tags","order");
						intent1222.putExtra("tag",11);
						intent1222.putExtra("bean",mList2.get(groupPosition));
						intent1222.putExtra("tuikuan","dys");
						intent1222.putExtra("courser",courser1);
						context.startActivity(intent1222);
						break;

					default:
						break;
					}
					break;
				case 3:
					//说明是众筹
					int statusss=Integer.parseInt(mList2.get(groupPosition).getStatus());
					switch (statusss) {
					case 0:
						//待付款
						Intent inten=new Intent(mContext,WaitPayActivity.class);
						OrderListBean bean=mList2.get(groupPosition);
						inten.putExtra("bean", (Serializable)bean);
						inten.putExtra("tag","0");
						inten.putExtra("other","order");
						mContext.startActivity(inten);
						break;
					case 1:
						//待发货
						Intent intens=new Intent(mContext,OrderDetailWaitRepecipt.class);
						OrderListBean beans=mList2.get(groupPosition);
						intens.putExtra("bean", (Serializable)beans);
						intens.putExtra("tag","1");
						intens.putExtra("other","order");
						mContext.startActivity(intens);
						break;
					case 2:
						//待收货
						Intent intenss=new Intent(mContext,WaitTakeGoodsActivity.class);
						OrderListBean beanss=mList2.get(groupPosition);
						intenss.putExtra("bean", (Serializable)beanss);
						intenss.putExtra("tag","2");
						intenss.putExtra("other","order");
						mContext.startActivity(intenss);
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
					case 1:
						//待发货
						Intent intens=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean beans=mList2.get(groupPosition);
						intens.putExtra("bean", (Serializable)beans);
						intens.putExtra("tag","1");
						intens.putExtra("other","tices");
						mContext.startActivity(intens);
						break;
					case 2:
						//待收货
						Intent intenss=new Intent(mContext,TicesWaitPayActivity.class);
						OrderListBean beanss=mList2.get(groupPosition);
						intenss.putExtra("bean", (Serializable)beanss);
						intenss.putExtra("tag","2");
						intenss.putExtra("other","tices");
						mContext.startActivity(intenss);
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
