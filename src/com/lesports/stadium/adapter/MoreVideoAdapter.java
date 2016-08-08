package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.MoreVideoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : (视频播放界面更多视频的适配器)
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
public class MoreVideoAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<MoreVideoBean> mlist;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	public MoreVideoAdapter(List<MoreVideoBean> mlists,Context mContexts){
		this.mlist=mlists;
		this.mContext=mContexts;
	}
	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholderss vh=null;
		if(arg1==null){
			vh=new viewholderss();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_video_lv_sp, null);
			vh.m_bg=(ImageView) arg1.findViewById(R.id.video_lv_image_bg);
			vh.title=(TextView) arg1.findViewById(R.id.video_lv_tv_biaoti);
			vh.time=(TextView) arg1.findViewById(R.id.video_lv_tv_shijian);
			arg1.setTag(vh);
		}else{
			vh=(viewholderss) arg1.getTag();
		}
		vh.title.setText(mlist.get(arg0).getTitle());
		return arg1;
	}
	static class viewholderss{
		/**
		 * 背景图片
		 */
		ImageView m_bg;
		/**
		 * 标题
		 */
		TextView title;
		/**
		 * 时间
		 */
		TextView time;
	}

}
