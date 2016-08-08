/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 歌词适配器
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

public class LyricAdapter extends BaseAdapter {

	private List<String> mList;
	private Context mContext;
	public LyricAdapter(List<String> list,Context montext){
		mList=list;
		mContext=montext;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override  
	public boolean isEnabled(int position) {  
	    return false;  
	}  


	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholder vh=null;
		if(arg1==null){
			vh=new viewholder();
			@SuppressWarnings("static-access")
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.lyric_listview_item,null);
			vh.mLyric=(TextView) arg1.findViewById(R.id.lyric_listview_item_tv);
			arg1.setTag(vh);
		}else{
			vh=(viewholder) arg1.getTag();
		}
			vh.mLyric.setText(mList.get(arg0));
		
		return arg1;
	}
	
	static class viewholder{
		/**
		 * 歌词显示的tv
		 */
		TextView mLyric;
	}

}
