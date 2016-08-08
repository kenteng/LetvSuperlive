package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.view.RoundImageView;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
 * @Desc : (视频播放界面底部评论数据的适配器)
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
public class VideoBottomCommantAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<VideoBottomCommantBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 为了处理文字缩进效果
	 */
	private ForegroundColorSpan blueSpan;
	public VideoBottomCommantAdapter(List<VideoBottomCommantBean> mLists,Context mContexst){
		blueSpan=new ForegroundColorSpan(0xff43b7e6);
		this.mList=mLists;
		this.mContext=mContexst;
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
		viewholderbottom vh=null;
		if(arg1==null){
			vh=new viewholderbottom();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_video_commant,null);
			vh.mContent=(TextView) arg1.findViewById(R.id.video_bottom_comment_content);
			vh.mHeader=(RoundImageView) arg1.findViewById(R.id.video_bottom_comment_image);
			arg1.setTag(vh);
		}else{
			vh=(viewholderbottom) arg1.getTag();
		}
		//首先获取名称，计算长度
		String name=mList.get(arg0).getName();
		int length=name.length();
		SpannableStringBuilder builder = new SpannableStringBuilder(name+":"+mList.get(arg0).getContent());
		builder.setSpan(blueSpan, 0,length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		vh.mContent.setText(builder);
		return arg1;
	}
	
	static class viewholderbottom{
		/**
		 * 头像image
		 */
		RoundImageView mHeader;
		/**
		 * 内容
		 */
		TextView mContent;
	}

}
