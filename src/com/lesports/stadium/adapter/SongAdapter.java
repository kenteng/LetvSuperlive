/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.LyricsListBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 歌曲列表adapter
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

public class SongAdapter extends BaseAdapter {

	private List<LyricsListBean> mList;
	private Context mContext;
	public SongAdapter(List<LyricsListBean> list,Context montext){
		mList=list;
		mContext=montext;
	}
	
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}


	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholder vh=null;
		if(arg1==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.song_listview_item,null);
			vh.mSong=(TextView) arg1.findViewById(R.id.song_listview_item_tv_name);
			vh.mNum=(TextView) arg1.findViewById(R.id.song_listview_item_num_111);
			vh.mAuthour=(TextView) arg1.findViewById(R.id.song_listview_item_tv_authoer);
			arg1.setTag(vh);
		}else{
			vh=(viewholder) arg1.getTag();
		}
			vh.mNum.setText((arg0+1)+"");
			vh.mSong.setText(mList.get(arg0).getMusicName());
			vh.mAuthour.setText(mList.get(arg0).getSinger());
		
		return arg1;
	}
	
	static class viewholder{
		/**
		 * 歌词显示的tv
		 */
		TextView mSong;
		/**
		 * 显示序号的tv
		 */
		TextView mNum;
		/**
		 * 演唱者
		 */
		TextView mAuthour;
	}

}
