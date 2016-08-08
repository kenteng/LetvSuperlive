package com.lesports.stadium.adapter;

import java.util.List;
import java.util.Map;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 购物车数据的适配器
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
 * ***************************************************************
 */
public class ShopcartExpandableListViewAdapter extends BaseExpandableListAdapter
{
	/**
	 * 存放商品信息的集合，这里是店铺以及店铺下的商品数据
	 */
	public List<GroupInfo> groups;
	/**
	 * 存放店铺下所有商品信息的集合
	 */
	public Map<String, List<ProductInfo>> children;
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;

	/**
	 * 用来动态判断，界面当中的增减按钮是否需要显示
	 */
	private boolean isShow=false;
	/**
	 * 构造函数
	 * 
	 * @param groups
	 *            组元素列表
	 * @param children
	 *            子元素列表
	 * @param context
	 */
	public ShopcartExpandableListViewAdapter(boolean b,List<GroupInfo> groups, Map<String, List<ProductInfo>> children, Context context)
	{
		super();
		isShow=b;
		this.groups = groups;
		this.children = children;
		this.context = context;
	}
	/**
	 * 当用户在界面当中点击了编辑按钮的时候调用的方法
	 * @2016-2-23下午2:55:11
	 */
	public void setIsShow(boolean tag){
		this.isShow=tag;
		notifyDataSetChanged();
	}
	
	public void notifyData(List<GroupInfo> groups, Map<String, List<ProductInfo>> children){
		this.groups=groups;
		this.children = children;
	}

	public void setCheckInterface(CheckInterface checkInterface)
	{
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(ModifyCountInterface modifyCountInterface)
	{
		this.modifyCountInterface = modifyCountInterface;
	}

	@Override
	public int getGroupCount()
	{
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		String groupId = groups.get(groupPosition).getId();
		return children.get(groupId).size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return groups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		List<ProductInfo> childs = children.get(groups.get(groupPosition).getId());

		return childs.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{

		GroupHolder gholder;
		if (convertView == null)
		{
			gholder = new GroupHolder();
			convertView = View.inflate(context, R.layout.item_shopcart_group, null);
			gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
			gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
			 convertView.setTag(gholder);
		} else
		{
			gholder = (GroupHolder) convertView.getTag();
		}
		final GroupInfo group = (GroupInfo) getGroup(groupPosition);
		if (group != null)
		{
			gholder.tv_group_name.setText(group.getName());
			if(isShow){
				gholder.cb_check.setVisibility(View.VISIBLE);
			}else{
				gholder.cb_check.setVisibility(View.GONE);
			}
			gholder.cb_check.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)

				{
					group.setChoosed(((CheckBox) v).isChecked());
					checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
				}
			});
			gholder.cb_check.setChecked(group.isChoosed());
		}
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		final ChildHolder cholder;
		if (convertView == null)
		{
			cholder = new ChildHolder();
			convertView = View.inflate(context, R.layout.item_shopcart_product, null);
			cholder.cb_check = (CheckBox) convertView.findViewById(R.id.check_box);
			cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_intro);
			cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
			cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_add);
			cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_reduce);
			cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);
			cholder.tv_countnum=(TextView) convertView.findViewById(R.id.tv_countnum);
			cholder.im_product_pic=(ImageView) convertView.findViewById(R.id.iv_adapter_list_pic);
			cholder.tv_color=(TextView) convertView.findViewById(R.id.tv_type_color);
			cholder.tv_spacename=(TextView) convertView.findViewById(R.id.tv_shangpin_chicunguige);
			convertView.setTag(cholder);
		} else
		{
			cholder = (ChildHolder) convertView.getTag();
		}
		final ProductInfo product = (ProductInfo) getChild(groupPosition, childPosition);

		if (product != null)
		{
			if(!TextUtils.isEmpty(product.getSpace_id())&&!product.getSpace_id().equals("111")){
				//说明商品时有规格的
				if(!TextUtils.isEmpty(product.getmGoodsSpaceShuoming())){
					String string[]=product.getmGoodsSpaceShuoming().split(",");
					cholder.tv_spacename.setText("颜色："+string[0]+" 尺寸："+string[1]);
				}
			}
			cholder.tv_color.setText(product.getDesc());
			cholder.tv_product_desc.setText(product.getDesc());
			if(!TextUtils.isEmpty(product.getPrice()+"")){
				double price=Utils.parseTwoNumber(product.getPrice()+"");
				cholder.tv_price.setText("￥"+price+"");
			}else{
				cholder.tv_price.setText("￥"+"0.00");
			}
			cholder.tv_count.setText(product.getCount() + "");
			cholder.tv_countnum.setText("X"+product.getCount() + "");
			cholder.cb_check.setChecked(product.isChoosed());
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+product.getImageUrl() + ConstantValue.IMAGE_END, cholder.im_product_pic,
					R.drawable.huodongshouye_zhanwei);
			if(isShow){
				cholder.cb_check.setVisibility(View.VISIBLE);
				cholder.iv_increase.setVisibility(View.VISIBLE);
				cholder.iv_decrease.setVisibility(View.VISIBLE);
				cholder.tv_count.setVisibility(View.VISIBLE);
				cholder.tv_countnum.setVisibility(View.GONE);
			}else{
				cholder.cb_check.setVisibility(View.GONE);
				cholder.iv_increase.setVisibility(View.GONE);
				cholder.iv_decrease.setVisibility(View.GONE);
				cholder.tv_count.setVisibility(View.GONE);
				cholder.tv_countnum.setVisibility(View.VISIBLE);
			}
			cholder.cb_check.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					children.get(groups.get(groupPosition).getId()).get(childPosition).setChoosed(((CheckBox) v).isChecked());
					cholder.cb_check.setChecked(((CheckBox) v).isChecked());
					checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
				}
			});
			cholder.iv_increase.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					modifyCountInterface.doIncrease(children.get(groups.get(groupPosition).getId()).get(childPosition).getId(),groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
				}
			});
			cholder.iv_decrease.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					modifyCountInterface.doDecrease(children.get(groups.get(groupPosition).getId()).get(childPosition).getId(),groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
				}
			});
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}

	/**
	 * 组元素绑定器
	 * 
	 * 
	 */
	private class GroupHolder
	{
		CheckBox cb_check;
		TextView tv_group_name;
	}

	/**
	 * 子元素绑定器
	 * 
	 * 
	 */
	private class ChildHolder
	{
		CheckBox cb_check;

		TextView tv_spacename;
		ImageView im_product_pic;
		TextView tv_product_name;
		TextView tv_product_desc;
		TextView tv_price;
		TextView iv_increase;
		TextView tv_count;
		TextView iv_decrease;
		TextView tv_countnum;
		TextView tv_color;
	}

	/**
	 * 复选框接口
	 * 
	 * 
	 */
	public interface CheckInterface
	{
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
		public void checkChild(int groupPosition, int childPosition, boolean isChecked);
	}

	/**
	 * 改变数量的接口
	 */
	public interface ModifyCountInterface
	{
		/**
		 * 增加操作
		 * @param string 
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
		public void doIncrease(String string, int groupPosition, int childPosition, View showCountView, boolean isChecked);

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
		public void doDecrease(String string,int groupPosition, int childPosition, View showCountView, boolean isChecked);
	}
	
	

}
