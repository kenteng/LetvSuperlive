package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.OrderWindowBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WindowMenuAdapter extends BaseAdapter {

	private List<OrderWindowBean> mList;
	private Context mContext;
	public WindowMenuAdapter(List<OrderWindowBean> mLists,Context mContexts){
		this.mList=mLists;
		this.mContext=mContexts;
	}
	public void setList(List<OrderWindowBean> mLists){
		this.mList=mLists;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		windoworder vh=null;
		if(convertView==null){
			vh=new windoworder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.item_order_window_menu,null);
			vh.mIschoise=(ImageView) convertView.findViewById(R.id.order_window_menu_image);
			vh.mTitle=(TextView) convertView.findViewById(R.id.order_window_menu_title);
			convertView.setTag(vh);
		}else{
			vh=(windoworder) convertView.getTag();
		}
		if(mList.get(position).isChoise()){
			vh.mIschoise.setVisibility(View.VISIBLE);
		}else{
			vh.mIschoise.setVisibility(View.GONE);
		}
		vh.mTitle.setText(mList.get(position).getName());
		return convertView;
	}
	
	static class windoworder{
		TextView mTitle;
		ImageView mIschoise;
	}

}
