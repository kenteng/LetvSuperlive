package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.view.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * ***************************************************************
 * 
 * @Desc : 众筹详情页面评论fragment的列表项适配器
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
@SuppressLint("InflateParams") public class CommantsAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<String> mList;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	public CommantsAdapter(List<String> list,Context context){
		this.mContext=context;
		this.mList=list;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholdesssss vh=null;
		if(arg1==null){
			vh=new viewholdesssss();
			@SuppressWarnings("static-access")
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_crowdfunding_taolun, null);
			vh.mConntent=(TextView) arg1.findViewById(R.id.crowdfunding_taolun_neirong);
			vh.mImage=(CircleImageView) arg1.findViewById(R.id.crowdfunding_taolun_lv_headerview);
			vh.mName=(TextView) arg1.findViewById(R.id.crowdfunding_taolun_name);
			vh.mTime=(TextView) arg1.findViewById(R.id.crowdfunding_taolun_time);
			arg1.setTag(vh);
		}else{
			vh=(viewholdesssss) arg1.getTag();
		}
		
		return arg1;
	}
	static class viewholdesssss{
		/**
		 * 圆形头像
		 */
		CircleImageView mImage;
		/**
		 * 用户名称
		 */
		TextView mName;
		/**
		 * 时间
		 */
		TextView mTime;
		/**
		 * 评论内容
		 */
		TextView mConntent;
		
	}

}
