package com.lesports.stadium.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.DiscussBean;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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
public class Commants2Adapter extends BaseAdapter {


	/**
	 * 数据源个数
	 */
	private int DataNum=0;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	/**
	 * 存储请求到的评论
	 */
	private List<DiscussBean> mDiscussList = new ArrayList<DiscussBean>();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	public List<DiscussBean> getmDiscussList() {
		return mDiscussList;
	}
	public void setmDiscussList(List<DiscussBean> mDiscussList) {
		this.mDiscussList = mDiscussList;
	}
	public Commants2Adapter(List<DiscussBean> list,Context context){
		this.mContext=context;
		this.mDiscussList=list;
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	@Override
	public int getCount() {
		DataNum=mDiscussList.size();
		return mDiscussList==null?0:mDiscussList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDiscussList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholdesssss vh=null;
		DiscussBean tempDiscuss=new DiscussBean();
		tempDiscuss=mDiscussList.get(arg0);
		if(arg1==null){
			vh=new viewholdesssss();
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
		vh.mName.setText(tempDiscuss.getUserName());
		CharSequence content = CommonUtils
				.convertNormalStringToSpannableString(
				mContext, tempDiscuss.getContent(),
				true);
		vh.mConntent.setText(content);
		if(!TextUtils.isEmpty(tempDiscuss.getUserImage())){
			if(tempDiscuss.getUserImage().startsWith("http:")){
				imageLoader.displayImage(tempDiscuss.getUserImage(), vh.mImage,MyFragment.setDefaultImageOptions(R.drawable.default_header));
			}else{
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+tempDiscuss.getUserImage() + ConstantValue.IMAGE_END, vh.mImage,MyFragment.setDefaultImageOptions(R.drawable.default_header));
			}
		}else{
			vh.mImage.setImageResource(R.drawable.default_header);
		}
		initDate(tempDiscuss.getReleaseDate(), vh.mTime);
		return arg1;
	}
	
	
	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * @param starttime
	 */
	private void initDate(String starttime,TextView time) {
		// TODO Auto-generated method stub
		long starttimes= getChangeToTime(starttime);
		Date date = new Date();
		long currenttimes=date.getTime();
		Log.i("当前时间",currenttimes+"");
		Log.i("开始时间",starttimes+"");
		
		//调用方法，计算两个时间之间差值
		calculationTime(starttimes,currenttimes,time);
	}
	/**
	 * 计算时间差值
	 * @param starttimes
	 * @param currenttimes
	 * @param time 
	 */
	private void calculationTime(long starttimes, long currenttimes, TextView time) {
		// TODO Auto-generated method stub
//		  SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	         long between = 0;
	         try {
	        	 between = (starttimes - currenttimes);
	        	 
	        } catch (Exception ex) {
	             ex.printStackTrace();
	         }
	         long day = between / (24 * 60 * 60 * 1000);
	         long hour = (between / (60 * 60 * 1000) - day * 24);
	         long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	         long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	         long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
	                 - min * 60 * 1000 - s * 1000);
	         Log.i("当前计算后时间是", day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
	                 + "毫秒");
	         /**
	          * 设置时间到控件上
	          */
	        
	        	 int days=(int) Math.abs(day);
	        	 if(days==1){
	        		 time.setText("昨天"); 
	        	 }else if(days==2){
	        		 time.setText("前天");
	        	 }else if(days>2&&days<7){
	        		 time.setText(days+"天前");
	        	 }else if(days>=7&&days<30){
	        		 time.setText("一周前");
	        	 }else if(days>30){
	        		 time.setText("一个月前");
	        	 }else if(days<1){
	        		 int hours=(int) Math.abs(hour);
	        		 if(hours<23&&hours>1){
	        			 time.setText(hours+"小时前");
	        		 }else{
	        			 int mins=(int) Math.abs(min);
	        	         if(mins<=3){
	        	        	 time.setText("刚刚"); 
	        	         }else{
	        	        	 time.setText(mins+"分钟前");
	        	         }
	        	         }
	        		 }
	}
	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * @param str
	 * @return
	 */
	public long getChangeToTime(String str){
		if(TextUtils.isEmpty(str))
			return 0;
		long time=Long.parseLong(str);
		return time;
		
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
		/**
		 * RelativeLayout 
		 */
		RelativeLayout mLayout_addmore;
		
	}

}
