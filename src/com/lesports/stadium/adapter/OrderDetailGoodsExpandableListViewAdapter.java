package com.lesports.stadium.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.SeeLogiestActivity;
import com.lesports.stadium.activity.TuiKuanActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;
import com.lesports.stadium.bean.ReportBackChildBean;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.utils.ConstantValue;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 订单数据的适配器
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
public class OrderDetailGoodsExpandableListViewAdapter extends BaseExpandableListAdapter {
	/**
	 * 组的数据源集合
	 */
	public List<ReportBackGroupBean> groups;
	/**
	 * 子的数据源集合
	 */
	public Map<String, List<ReportBackChildBean>> children;
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;

	private List<OrderListBean> mList;
	private String tag=null;

	/**
	 * 构造函数
	 * 
	 * @param groups
	 *            组元素列表t
	 * @param children
	 *            子元素列表
	 * @param context
	 */
	public OrderDetailGoodsExpandableListViewAdapter(List<OrderListBean> mList,
			Context context) {
		super();
		this.context = context;
		this.mList = mList;
	}

	public void setList(List<OrderListBean> list) {
		this.mList = null;
		this.mList = list;
	}

	public void setCheckInterface(CheckInterface checkInterface) {
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(
			ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
	}

	@Override
	public int getGroupCount() {
		Log.i("wxn", "mList.zise()"+mList.size());
		return mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;
		String groupStringTypes = mList.get(groupPosition).getOrdersType();
		if (groupStringTypes != null && !TextUtils.isEmpty(groupStringTypes)) {
			if (groupStringTypes.equals("0")) {
				// 说明现在所属的集合是商品的
				count = mList.get(groupPosition).getList().size();
			} else if (groupStringTypes.equals("1")) {
				// 说明现在所属商品是餐饮的
				count = mList.get(groupPosition).getList().size();
			} else if (groupStringTypes.equals("3")) {
				// 说明是众筹的
				count = mList.get(groupPosition).getList_zhong().size();
			}
		}
		return count;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String groupStringTypes = mList.get(groupPosition).getOrdersType();
		if (groupStringTypes != null && !TextUtils.isEmpty(groupStringTypes)) {
			if (groupStringTypes.equals("1")) {
				// 说明现在所属的集合是商品的
				return mList.get(groupPosition).getList().get(childPosition);
			} else if (groupStringTypes.equals("0")) {
				// 说明现在所属商品是餐饮的
				return mList.get(groupPosition).getList().get(childPosition);
			} else if (groupStringTypes.equals("3")) {
				// 说明是众筹的
				return mList.get(groupPosition).getList_zhong()
						.get(childPosition);
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		GroupHolder gholder;
			gholder = new GroupHolder();
			convertView = View
					.inflate(context, R.layout.item_order_group, null);
			gholder.mName = (TextView) convertView
					.findViewById(R.id.order_item_group_name);
			final OrderListBean Ordergroup = (OrderListBean) getGroup(groupPosition);
			if (Ordergroup != null) {
				if(Ordergroup.getOrdersType().equals("0")){
					gholder.mName.setText(Ordergroup.getCompanies());
				}else if(Ordergroup.getOrdersType().equals("1")){
					gholder.mName.setText(Ordergroup.getCompanies());
				}else if(Ordergroup.getOrdersType().equals("3")){
					gholder.mName.setText(mList.get(groupPosition).getList_zhong().get(0).getCrowdfundName());
				}
			}
		return convertView;
	}
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView=null;
		String orderDataType = mList.get(groupPosition).getOrdersType();
		if (orderDataType != null && !TextUtils.isEmpty(orderDataType)) {
			if (orderDataType.equals("1")) {
				// 说明是商品，所以这里需要加载商品的布局
				final ChildHolder cholder;
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_shangpin_canyin, null);
				cholder.mGoodsGuide=(TextView) convertView.findViewById(R.id.order_child_goods_shuliang_guige);
				cholder.mGoodsNum = (TextView) convertView
						.findViewById(R.id.order_child_goods_shuliang_s);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_goods_image_s);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_child_goods_jiage_s);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.order_child_goods_shouhuodizhi_s);
				final OrderListBeanGoodsBean product = (OrderListBeanGoodsBean) getChild(
						groupPosition, childPosition);

				if (product != null) {
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
					useWanHanderGuide(cholder.mGoodsGuide,mList.get(groupPosition).getList().get(childPosition).getPecificationName());
					LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
							+ product.getSmallImg() + ConstantValue.IMAGE_END, cholder.mImage,
							R.drawable.zhoubianshangpin_zhanwei);
					cholder.mGoodsNum.setText("X"+product.getWareNumber());
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
					cholder.mPrice.setText(mList.get(groupPosition).getList().get(childPosition).getGoodsName());
					cholder.mZuowei.setText("￥"+mList.get(groupPosition).getList().get(childPosition).getPrice());
					//调用方法，计算商品总价格
					double price = useWayCountGoodsPrice(mList.get(groupPosition).getList());
					double feight=Double.parseDouble(mList.get(groupPosition).getFreight());
					int status=Integer.parseInt(mList.get(groupPosition).getStatus());
					String courier=mList.get(groupPosition).getCourier();
				}
				return convertView;
			} else if (orderDataType.equals("0")) {
				// 说明是餐饮，所以这里加载餐饮的布局
				final ChildHolder cholder;
				// if (convertView == null)
				// {
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_shangpin_canyin, null);
				cholder.mGoodsGuide=(TextView) convertView.findViewById(R.id.order_child_goods_shuliang_guige);
				cholder.mGoodsNum = (TextView) convertView
						.findViewById(R.id.order_child_goods_shuliang_s);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_goods_image_s);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_child_goods_jiage_s);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.order_child_goods_shouhuodizhi_s);
				final OrderListBeanGoodsBean product = (OrderListBeanGoodsBean) getChild(
						groupPosition, childPosition);

				if (product != null) {
					LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
							+ product.getSmallImg() + ConstantValue.IMAGE_END, cholder.mImage,
							R.drawable.zhoubianshangpin_zhanwei);
					cholder.mGoodsNum.setText("X"+product.getWareNumber());
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
					cholder.mZuowei.setText("￥"+mList.get(groupPosition).getList().get(childPosition).getPrice());
					cholder.mPrice.setText(mList.get(groupPosition).getList().get(childPosition).getGoodsName());
					//调用方法，计算商品总价格
					double price = useWayCountGoodsPrice(mList.get(groupPosition).getList());
					double feight=Double.parseDouble(mList.get(groupPosition).getFreight());
					int status=Integer.parseInt(mList.get(groupPosition).getStatus());
					String courier=mList.get(groupPosition).getCourier();
				}
				//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
				return convertView;
		}
		}

		return null;
	}

	/**
	 * 处理规格数据
	 * @param mGuigename
	 * @param pecificationName
	 */
	private void useWanHanderGuide(TextView mGuigename, String pecificationName) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(pecificationName)){
			String[] str=pecificationName.split(",");
			String color=str[0];
			String chicun=str[1];
			mGuigename.setText("颜色："+color+"  尺寸："+chicun);
		}else{
			mGuigename.setText("颜色：默认  尺寸：默认");
		}
	}
	/**
	 * 根据集合来计算商品总价
	 * @param list
	 */
	private double useWayCountGoodsPrice(List<OrderListBeanGoodsBean> list) {
		// TODO Auto-generated method stub
		double price=0;
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(!TextUtils.isEmpty(list.get(i).getPrice())){
					double prices=Double.parseDouble(list.get(i).getPrice());
					price=price+prices;
				}else{
					price=price+0;
				}
			}
		}else{
			return price;
		}
		return price;
	}
	/**
	 * 用来处理按钮点击事件
	 * @param i
	 * @param orderListBean
	 * @param mCheckWuliu
	 */
	private void useWayChangeButton(int i, final OrderListBean orderListBean,
			TextView mCheckWuliu) {
		switch (i) {
		case 0:
			
			break;
		case 1:
			//说明是退款
			mCheckWuliu.setText("退款");
			mCheckWuliu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, TuiKuanActivity.class);
					intent.putExtra("bean",(Serializable)orderListBean);
					context.startActivity(intent);
				}
			});
			break;
		case 2:
			//说明是查看物流
			mCheckWuliu.setText("查看物流");
			mCheckWuliu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, SeeLogiestActivity.class);
					intent.putExtra("bean",(Serializable)orderListBean);
					context.startActivity(intent);
				}
			});

		default:
			break;
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 组元素绑定器
	 * 
	 * 
	 */
	private class GroupHolder {
		TextView mName;
	}

	/**
	 * 子元素绑定器
	 * 
	 * 
	 */
	private class ChildHolder {
		TextView mPrice;
		ImageView mImage;
		TextView mZuowei;
		TextView mGoodsNum;
		TextView mGoodsGuide;
	}

	/**
	 * 复选框接口
	 * 
	 * 
	 */
	public interface CheckInterface {
		/**
		 * 组选框状态改变触发的事件
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param isChecked
		 *            组元素选中与否
		 */
		public void checkGroup(int groupPosition, boolean isChecked);

		/**
		 * 子选框状态改变时触发的事件
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void checkChild(int groupPosition, int childPosition,
				boolean isChecked);
	}

	/**
	 * 改变数量的接口
	 * 
	 * 
	 */
	public interface ModifyCountInterface {
		/**
		 * 增加操作
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param showCountView
		 *            用于展示变化后数量的View
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void doIncrease(int groupPosition, int childPosition,
				View showCountView, boolean isChecked);

		/**
		 * 删减操作
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param showCountView
		 *            用于展示变化后数量的View
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void doDecrease(int groupPosition, int childPosition,
				View showCountView, boolean isChecked);
	}

}
