/**
 * 
 */
package com.lesports.stadium.adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.bean.OrderCrowdfundingBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.MyProgressBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 我的众筹首页展示数据的列表项的适配器
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

public class OrderCrowdFundingAdapter extends BaseAdapter {

	/**
	 * 数据源集合
	 */
	private List<OrderCrowdfundingBean> mList;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	public OrderCrowdFundingAdapter(List<OrderCrowdfundingBean> mLists,Context mContexts){
		this.mList=mLists;
		this.mContext=mContexts;
	}
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {
		viewholdersorrder vh=null;
		if(convertView==null){
			vh=new viewholdersorrder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.ordercrowdfunding_listview_item,null);
			vh.mBGimage=(ImageView) convertView.findViewById(R.id.order_crowdfunding_lv_item_bg);
			vh.mComMonNum=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_yichoujine);
			vh.mCompleatPnum=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_yidadao);
			vh.mCurruntPnum=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_attandpeople);
			vh.mJoalNum=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_goalmoney);
			vh.mMonAndDay=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_time_monthandday);
			vh.mName=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_name);
			vh.mProgresssagebar=(MyProgressBar) convertView.findViewById(R.id.order_crowdfunding_lv_item_progress_bg);
			vh.mShengTime=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_shengyutianshu);
			vh.mStatus=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_righttop_status);
			vh.mStatusBG=(ImageView) convertView.findViewById(R.id.order_crowdfunding_lv_item_righttop_bg);
			vh.mTimesBG=(ImageView) convertView.findViewById(R.id.order_crowdfunding_lv_item_lefttop);
			vh.mTitle=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_title);
			vh.mYear=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_item_time_year);
			vh.mProgresssagebar_yijieshu=(MyProgressBar) convertView.findViewById(R.id.order_crowdfunding_lv_item_progress_bg_weikaishi);
			vh.mPrice=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_price);
			vh.mDate=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_date);
			vh.mDaifahuo=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_daifahuo);
			vh.mDaiPay=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_daizhifu);
			vh.mDaishouhuo=(TextView) convertView.findViewById(R.id.order_crowdfunding_lv_daishouhuo);
			convertView.setTag(vh);
		}else{
			vh=(viewholdersorrder) convertView.getTag();
		}
		if(mList.get(position).getStatus()==0){
			//说明是待支付
			vh.mDaiPay.setVisibility(View.VISIBLE);
			vh.mDaifahuo.setVisibility(View.GONE);
			vh.mDaishouhuo.setVisibility(View.GONE);
		}else if(mList.get(position).getStatus()==1){
			//说明是待发货
			vh.mDaiPay.setVisibility(View.GONE);
			vh.mDaifahuo.setVisibility(View.VISIBLE);
			vh.mDaishouhuo.setVisibility(View.GONE);
			
		}else if(mList.get(position).getStatus()==2){
			//说明是待收货
			vh.mDaiPay.setVisibility(View.GONE);
			vh.mDaifahuo.setVisibility(View.GONE);
			vh.mDaishouhuo.setVisibility(View.VISIBLE);
			
		}
		vh.mStatusBG.setImageBitmap(readBitMap(mContext, R.drawable.button_underway));
		return convertView;
	}
	
	/**
	 * 这里是处理剩余时间的方法
	 * @param beginTime
	 * @param mShengTime
	 */
	private void dateTimeshengyu(String beginTime, TextView mShengTime) {
		// TODO Auto-generated method stub
		initDate(beginTime,mShengTime);
	}
	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * @param starttime
	 * @param mShengTime 
	 */
	private void initDate(String starttime, TextView mShengTime) {
		// TODO Auto-generated method stub
		long starttimes= getChangeToTimes(starttime);
		Date date = new Date();
		long currenttimes=date.getTime();
		Log.i("当前时间",currenttimes+"");
		Log.i("开始时间",starttimes+"");
		//调用方法，计算两个时间之间差值
		String str=calculationTime(starttimes,currenttimes);
		mShengTime.setText(str);
	}
	/**
	 * 计算时间差值
	 * @param starttimes
	 * @param currenttimes
	 */
	private String calculationTime(long starttimes, long currenttimes) {
		// TODO Auto-generated method stub
//		  SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	         long between = 0;
	         try {
//	             java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
//	             java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
//	             between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
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
//	         System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
//	                 + "毫秒"); 
	         /**
	          * 设置时间到控件上
	          */
	         String times=day+"天"+hour+"小时";
			return times;
	}
	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * @param str
	 * @return
	 */
	public long getChangeToTimes(String str){
		if(TextUtils.isEmpty(str))
			return 0;
		long time=Long.parseLong(str);
		return time;
		
	}
	/**
	 * 通过传入的已经达到的金额数和目标金额数来计算进度条占比
	 * @param mProgresssagebar
	 * @param targetMoney
	 * @param hasMoney
	 * @param mCompleatPnum 
	 * @param mComMonNum 
	 */
	private void countProgress(MyProgressBar mProgresssagebar,
			String targetMoney, String hasMoney, TextView mCompleatPnum, TextView mComMonNum) {
		// TODO Auto-generated method stub
		int hasmoney=Integer.parseInt(hasMoney);
		int target=Integer.parseInt(targetMoney);
		float zhaanbi=hasmoney*100/target*100;
		if(zhaanbi<=1){
			mProgresssagebar.setProgress(0);
			mCompleatPnum.setText("0%");
		}else{
			mProgresssagebar.setProgress((int)zhaanbi);
			mCompleatPnum.setText((int)zhaanbi+"%");
		}
		mComMonNum.setText("￥"+hasmoney+"元");
	}
	/**
	 * 因为是未开始，所以这里需要展示开始时间
	 * @param mYear
	 * @param beginTime
	 * @param mMonAndDay 
	 */
	private void dateAddStart(TextView mYear, String beginTime, TextView mMonAndDay) {
		// TODO Auto-generated method stub
		String times=getChangeToTime(beginTime);
		String year=getYear(times);
		mYear.setText(year);
		String monthandday=getMonthAndDay(times);
		mMonAndDay.setText(monthandday);
	}
	static class viewholdersorrder{
		/**
		 * 背景图片
		 */
		ImageView mBGimage;
		/**
		 * 时间中的年
		 */
		TextView mYear;
		/**
		 * 时间中的月日
		 */
		TextView mMonAndDay;
		/**
		 * 该动态的状态
		 */
		TextView mStatus;
		/**
		 * 状态的背景图片
		 */
		ImageView mStatusBG;
		/**
		 * 年月日的背景图片
		 */
		ImageView mTimesBG;
		/**
		 * 姓名
		 */
		TextView mName;
		/**
		 * 该条报道的标题
		 */
		TextView mTitle;
		/**
		 * 当前参与人数
		 */
		TextView mCurruntPnum;
		/**
		 * 目标金额
		 */
		TextView mJoalNum;
		/**
		 * 当前进度条进度
		 */
		MyProgressBar mProgresssagebar;
		/**
		 * 当前进度条进度_已结束的
		 */
		MyProgressBar mProgresssagebar_yijieshu;
		/**
		 * 已达到人数
		 */
		TextView mCompleatPnum;
		/**
		 * 已筹到金额
		 */
		TextView mComMonNum;
		/**
		 * 剩余时间
		 */
		TextView mShengTime;
		/**
		 * 价格
		 */
		TextView mPrice;
		/**
		 * 日期
		 */
		TextView mDate;
		/**
		 * 待发货
		 */
		TextView mDaifahuo;
		
		/**
		 * 待收货
		 */
		TextView mDaishouhuo;
		/**
		 * 待支付
		 */
		TextView mDaiPay;
	}
	 /**

     *  以最省内存的方式读取本地资源的图片

     *  @param  context

     *  @param  resId

     *  @return 

      */

    public  static  Bitmap readBitMap(Context  context, int resId){ 

         BitmapFactory.Options opt = new  BitmapFactory.Options();

         opt.inPreferredConfig =  Bitmap.Config.RGB_565;

         opt.inPurgeable = true;

         opt.inInputShareable = true;

         //  获取资源图片

        InputStream is =  context.getResources().openRawResource(resId);

         return  BitmapFactory.decodeStream(is, null, opt);

         }

    /**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss", Locale.getDefault());
		long time=Long.parseLong(str);
		Date date = new Date(); 
		date.setTime(time);
		String timestring = sdf.format(date);
		
		return timestring;
		
	}
	/**
	 * 根据传入的字符串时间值，截取当前年时间字符串
	 * @param str
	 * @return
	 */
	public String getYear(String str){
		 String md=str.substring(0,4);
		return md;
		
	}
	/**
	 * 根据传入的字符串时间值，截取当前月日时间字符串
	 * @param str
	 * @return
	 */
	public String getMonthAndDay(String str){
		 String md=str.substring(5,10);
		return md;
		
	}

}
