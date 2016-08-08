/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.utils.ConstantValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 未开始页面视频适配器
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

public class NoactionVideoAdapter extends BaseAdapter {

	private List<HeightLightBean> mList;
	private Context mContext;
	public NoactionVideoAdapter(List<HeightLightBean> list,Context montext){
		mList=list;
		mContext=montext;
	}
	public void setList(List<HeightLightBean> list){
		mList=list;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mList==null?0:mList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		return mList==null?0:mList.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholder vh=null;
		if(arg1==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_lv_noactionvideo,null);
			vh.mImage=(ImageView) arg1.findViewById(R.id.noactionvideo_hrightlight_lv_item_image);
			arg1.setTag(vh);
		}else{
			vh=(viewholder) arg1.getTag();
		}
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mList.get(arg0).getVideoImageUrl() + ConstantValue.IMAGE_END, vh.mImage,R.drawable.default_image);
		return arg1;
	}
	
	static class viewholder{
		/**
		 * 歌词显示的tv
		 */
		TextView mtitle;
		/**
		 * 背景图片
		 */
		ImageView mImage;
		/**
		 * 集锦内容
		 */
		TextView mContent;
	}

}
