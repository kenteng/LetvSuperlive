package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.utils.ConstantValue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NoStartActionAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<HeightLightBean> mlist;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	public NoStartActionAdapter(List<HeightLightBean> mlists,Context mContexts){
		this.mlist=mlists;
		this.mContext=mContexts;
	}
	@Override
	public int getCount() {
		return mlist==null? 0:mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams") @SuppressWarnings("static-access")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholderss vh=null;
		if(arg1==null){
			vh=new viewholderss();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_noostart_lv_spadnhb, null);
			vh.m_bg=(ImageView) arg1.findViewById(R.id.nostart_lv_image_bg);
			vh.m_bj=(ImageView) arg1.findViewById(R.id.nostart_lv_image_biaoji);
			arg1.setTag(vh);
		}else{
			vh=(viewholderss) arg1.getTag();
		}
		if("1".equals(mlist.get(arg0).getFileType())){
			//说明是视频
			vh.m_bj.setVisibility(View.VISIBLE);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mlist.get(arg0).getVideoImageUrl() + ConstantValue.IMAGE_END, vh.m_bg,R.drawable.default_image);
		}else {
			//说明是海报
			vh.m_bj.setVisibility(View.GONE);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mlist.get(arg0).getFileUrl() + ConstantValue.IMAGE_END, vh.m_bg,R.drawable.default_image);
		}
		return arg1;
	}
	static class viewholderss{
		/**
		 * 背景图片
		 */
		ImageView m_bg;
		/**
		 * 播放标记
		 */
		ImageView m_bj;
	}

}
