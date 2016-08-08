package com.lesports.stadium.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.CrowdPayActivty;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.bean.ReportBackChildBean;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 回报数据的适配器
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
public class ReportbackExpandableListViewAdapter extends BaseExpandableListAdapter
{
	/**
	 * 存放商品信息的集合，这里是店铺以及店铺下的商品数据
	 */
	public List<ReportBackGroupBean> groups;
	/**
	 * 存放店铺下所有商品信息的集合
	 */
	public Map<String, List<ReportBackChildBean>> children;
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;
	private String tag;
	 /**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;

	/**
	 * 构造函数
	 * 
	 * @param groups
	 *            组元素列表
	 * @param children
	 *            子元素列表
	 * @param context
	 */
	public ReportbackExpandableListViewAdapter(String tags,List<ReportBackGroupBean> groups, Map<String, List<ReportBackChildBean>> children, Context context)
	{
		super();
		this.tag=tags;
		this.groups = groups;
		this.children = children;
		this.context = context;
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
		List<ReportBackChildBean> childs = children.get(groups.get(groupPosition).getId());

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
			convertView = View.inflate(context, R.layout.reportback_listview_item_group, null);
			gholder.mPeopleNum =(TextView) convertView.findViewById(R.id.reportback_lv_item_guigeyaoqiu);
			gholder.mPrice = (TextView) convertView.findViewById(R.id.reportback_lv_item_moneyNum);
			gholder.mZhiChi = (TextView) convertView.findViewById(R.id.reportback_lv_item_woyaozhichi);
			 convertView.setTag(gholder);
		} else
		{
			gholder = (GroupHolder) convertView.getTag();
		}
		final ReportBackGroupBean group = (ReportBackGroupBean) getGroup(groupPosition);
		if (group != null)
		{
			if(!TextUtils.isEmpty(group.getNumLimit())){
				if(group.getNumLimit().equals("0")){
					gholder.mPeopleNum.setText("不限");
				}else{
					gholder.mPeopleNum.setText(group.getNumLimit()+"人");
				}
			}else{
				gholder.mPeopleNum.setText("不限");
			}
			
			gholder.mPrice.setText(group.getPrice());
		}
		if(tag.equals("1")){
				gholder.mZhiChi.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
								int limit=Integer.parseInt(group.getNumLimit());
								int num=Integer.parseInt(group.getPeopleNum());
								if(num>=limit&&limit!=0){
									createDialogs("该支持人数已满，请选择其他回报进行支持");
								}else{
									Intent intent=new Intent(context, CrowdPayActivty.class);
									intent.putExtra("group",(Serializable)group);
									context.startActivity(intent);
								}
							
						}else{
							createDialog();
						}
						
					}
				});
			
		}else if(tag.equals("2")){
			gholder.mZhiChi.setTextColor(Color.GRAY);
			gholder.mZhiChi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createDialogs("该众筹暂未开始，敬请期待");
				}
			});
		}else if(tag.equals("3")){
			gholder.mZhiChi.setTextColor(Color.GRAY);
			gholder.mZhiChi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					createDialogs("很遗憾，该众筹已经结束，请选择其他支持");
				}
			});
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
			convertView = View.inflate(context, R.layout.reportback_listview_item_child, null);
			cholder.iv_decrease =(TextView) convertView.findViewById(R.id.report_item_child_title);

			convertView.setTag(cholder);
		} else
		{
			cholder = (ChildHolder) convertView.getTag();
		}
		final ReportBackChildBean product = (ReportBackChildBean) getChild(groupPosition, childPosition);

		if (product != null)
		{

			cholder.iv_decrease.setText(product.getmTitle());
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
		TextView mPrice;
		TextView mPeopleNum;
		TextView mZhiChi;
	}

	/**
	 * 子元素绑定器
	 * 
	 * 
	 */
	private class ChildHolder
	{
		TextView iv_decrease;
	}

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
	 * 提示用户进行登录
	 */
	private void createDialog(){
		exitDialog = new CustomDialog(context,new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context,
						LoginActivity.class);
				context.startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("去登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能支持哦  ~~");
		exitDialog.show();
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
	 * 
	 * 
	 */
	public interface ModifyCountInterface
	{
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
		public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

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
		public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
	}
	
	

}
