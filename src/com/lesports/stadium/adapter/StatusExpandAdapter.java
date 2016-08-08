package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.ChildStatusEntity;
import com.lesports.stadium.bean.GroupStatusEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class StatusExpandAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater = null;
	private List<GroupStatusEntity> groupList;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param oneList
	 */
	public StatusExpandAdapter(Context context,
			List<GroupStatusEntity> group_list) {
		this.groupList = group_list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 返回一级Item总数
	 */
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	/**
	 * 返回二级Item总数
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupList.get(groupPosition).getChildList() == null) {
			return 0;
		} else {
			return groupList.get(groupPosition).getChildList().size();
		}
	}

	/**
	 * 获取一级Item内容
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	/**
	 * 获取二级Item内容
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getChildList().get(childPosition);
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		GroupViewHolder holder = new GroupViewHolder();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.time_group_status_item, null);
		}
		holder.groupName = (TextView) convertView
				.findViewById(R.id.time_one_status_name);
		holder.groupName.setText(groupList.get(groupPosition).getGroupName());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder = null;
		ChildStatusEntity entity = (ChildStatusEntity) getChild(groupPosition,
				childPosition);
		if (convertView != null) {
			viewHolder = (ChildViewHolder) convertView.getTag();
		} else {
			viewHolder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.time_child_status_item, null);
			viewHolder.twoStatusTime = (TextView) convertView
					.findViewById(R.id.time_two_complete_time);
		}
		viewHolder.twoStatusTime.setText(entity.getCompleteTime());

		convertView.setTag(viewHolder);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private class GroupViewHolder {
		TextView groupName;
	}

	private class ChildViewHolder {
		public TextView twoStatusTime;
	}

}
