/**
 * 
 */
package com.lesports.stadium.adapter;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.sharedpreference.tipprogress.RopeProgressBar;
import com.lesports.stadium.sharedpreference.tipprogress.RopeProgressBarend;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.MyProgressBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 众筹首页展示数据的列表项的适配器
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

@SuppressLint("InflateParams") public class CrowdFundingAdapter extends BaseAdapter {

	/**
	 * 数据源集合
	 */
	private List<AllChipsBean> mList;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	/**
	 * 该标记用来决定，listview当中展示哪一种类别的
	 *默认为3.以所有类型的形式展示数据
	 */
	public CrowdFundingAdapter(List<AllChipsBean> mLists,Context mContexts){
		this.mList=mLists;
		this.mContext=mContexts;
	}
	/**
	 * 提供给界面中用于动态改变适配器内数据以及类别标记
	 * @2016-2-26上午10:24:56
	 */
	public void setTagandData(List<AllChipsBean> list){
		this.mList=list;
		notifyDataSetChanged();
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
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {
		viewholders vh=null;
		if(convertView==null){
			vh=new viewholders();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.crowd_listview_item,null);
			vh.mBGimage=(ImageView) convertView.findViewById(R.id.image_crowd_listview_item);
			vh.mStatusImage=(ImageView) convertView.findViewById(R.id.tv_crowd_listview_item_status_image);
			vh.mStatus=(TextView) convertView.findViewById(R.id.tv_crowd_listview_item_status);
			vh.mTitle=(TextView) convertView.findViewById(R.id.tv_crowd_listview_item_status_title);
			vh.mYear=(TextView) convertView.findViewById(R.id.tv_crowd_listview_item_status_start_time);
			/**
			 * 下面开始获取未开始的布局
			 */
			vh.mLayout_noaction=(RelativeLayout) convertView.findViewById(R.id.layout_crowd_listview_weikaishi);
			vh.mStartMonth=(TextView) convertView.findViewById(R.id.tv_crowd_lv_kaishishijian_weikaishi_yuefen);
			vh.mStartHour=(TextView) convertView.findViewById(R.id.tv_crowd_lv_kaishishijian_weikaishi);
			vh.mMubiaoMoney=(TextView) convertView.findViewById(R.id.tv_crowd_lv_weikaishi_mubiaojine);
			
			vh.mProgresssagebar_weikaishi=(RopeProgressBar) convertView.findViewById(R.id.crowd_ropeProgressBar_weikaishi);
			/**
			 * 下面获取进行中的布局
			 */
			vh.mLayout_ing=(RelativeLayout) convertView.findViewById(R.id.layout_crowd_listview_jinxingzhong);
			vh.mYichouJine=(TextView) convertView.findViewById(R.id.tv_crowd_lv_yichoujine);
			vh.mInPersonNum=(TextView) convertView.findViewById(R.id.tv_crowd_lv_canjiarenshu_num);
			vh.mShengYuTime=(TextView) convertView.findViewById(R.id.tv_crowd_lv_shengyushijian);
			vh.mProgresssagebar_zhongchouzhong=(RopeProgressBar) convertView.findViewById(R.id.crowd_ropeProgressBar_jinxingzhong);
			/**
			 * 已结束但是成功的布局
			 */
			vh.mLayout_success=(RelativeLayout) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_chenggong);
			vh.mTV_success_yichoujine=(TextView) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_chenggong_yichoujine);
			vh.mTv_success_canjiarenshu=(TextView) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_chenggong_canjiarenshus);
			vh.mProgresssagebar_success=(RopeProgressBarend) convertView.findViewById(R.id.crowd_ropeProgressBar_yijieshu_chenggong);
			/**
			 * 众筹结束，但是失败的布局
			 */
			vh.mTV_faild_canjiarenshu=(TextView) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_shibai_canjiarenshus);
			vh.mTV_faild_yichoujine=(TextView) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_shibai_yichoujine);
			vh.mLayoutFaild=(RelativeLayout) convertView.findViewById(R.id.layout_crowd_listview_yijieshu_shibai);
			vh.mProgresssagebar_faild=(RopeProgressBarend) convertView.findViewById(R.id.crowd_ropeProgressBar_yijieshu_shibai);
			convertView.setTag(vh);
		}else{
			vh=(viewholders) convertView.getTag();
		}
		String status=mList.get(position).getCrowdfundStatus();
		if(!TextUtils.isEmpty(status)){
			if(status.equals("2")){
				//未开始
				vh.mStatus.setText("未开始");
				vh.mStatus.setTextColor(Color.rgb(70,202,253));
				vh.mStatusImage.setImageBitmap(readBitMap(mContext, R.drawable.zhongchou_weikaishi));
				vh.mLayout_noaction.setVisibility(View.VISIBLE);
				vh.mLayout_ing.setVisibility(View.GONE);
				vh.mLayout_success.setVisibility(View.GONE);
				vh.mLayoutFaild.setVisibility(View.GONE);
				useWayCountProgress(vh.mProgresssagebar_weikaishi,mList.get(position));
				vh.mMubiaoMoney.setText("￥"+mList.get(position).getTargetMoney());
				useWayCountTime(vh.mStartMonth,vh.mStartHour,mList.get(position).getBeginTime());
			}else if(status.equals("1")){
				//进行中
				vh.mStatus.setText("众筹中");
				vh.mStatus.setTextColor(Color.rgb(255,219,13));
				vh.mStatusImage.setImageBitmap(readBitMap(mContext, R.drawable.seconde_zhongchouzhong));
				vh.mLayout_noaction.setVisibility(View.GONE);
				vh.mLayout_ing.setVisibility(View.VISIBLE);
				vh.mLayout_success.setVisibility(View.GONE);
				vh.mLayoutFaild.setVisibility(View.GONE);
				useWayCountProgress(vh.mProgresssagebar_zhongchouzhong,mList.get(position));
				vh.mYichouJine.setText("￥"+mList.get(position).getHasMoney());
				vh.mInPersonNum.setText(mList.get(position).getParticipation()+"人");
				useWayCountTimeIng(vh.mShengYuTime,mList.get(position).getEndTime());
			}else if(status.equals("3")){
				//已结束
				vh.mStatus.setText("已结束"); 
				vh.mStatus.setTextColor(Color.rgb(114,114,114));
				vh.mStatusImage.setImageBitmap(readBitMap(mContext, R.drawable.zhongchou_yijieshu));
				//需要先计算，已筹金额和目标金额的大小，判断该众筹失败还是成功了
				if(!TextUtils.isEmpty(mList.get(position).getHasMoney())&&!TextUtils.isEmpty(mList.get(position).getTargetMoney())){
					int havemoney=(int)Double.parseDouble(mList.get(position).getHasMoney());
					int targetmoney=(int)Double.parseDouble(mList.get(position).getTargetMoney());
					if(havemoney>=targetmoney){
						//说明众筹成功了
						vh.mLayout_noaction.setVisibility(View.GONE);
						vh.mLayout_ing.setVisibility(View.GONE);
						vh.mLayout_success.setVisibility(View.VISIBLE);
						vh.mLayoutFaild.setVisibility(View.GONE);
						useWayCountProgressend(vh.mProgresssagebar_success,mList.get(position));
						vh.mTv_success_canjiarenshu.setText(mList.get(position).getParticipation()+"人");
						vh.mTV_success_yichoujine.setText("￥"+mList.get(position).getParticipation());
					}else{
						//说明众筹失败了
						vh.mLayout_noaction.setVisibility(View.GONE);
						vh.mLayout_ing.setVisibility(View.GONE);
						vh.mLayout_success.setVisibility(View.GONE);
						vh.mLayoutFaild.setVisibility(View.VISIBLE);
						useWayCountProgressend(vh.mProgresssagebar_success,mList.get(position));
						vh.mTV_faild_canjiarenshu.setText(mList.get(position).getParticipation()+"人");
						vh.mTV_faild_yichoujine.setText("￥"+mList.get(position).getHasMoney());
					}
				}
				
				
			}
		}
		vh.mTitle.setText(mList.get(position).getCrowdfundName());
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+ mList.get(position).getPropagatePicture() + ConstantValue.IMAGE_END, vh.mBGimage,
				R.drawable.zhongchouliebiao_zhanwei);
		return convertView;
	}
	
	/**
	 * 处理进行中的剩余时间
	 * @param mShengYuTime
	 * @param endTime
	 */
	private void useWayCountTimeIng(TextView mShengYuTime, String endTime) {
		Date date = new Date();
		long currenttimes = date.getTime();
		long endtime=Long.parseLong(endTime);
		 long between = 0;
         try {
//             java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
//             java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
//             between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        	 between =Math.abs(endtime - currenttimes);
        } catch (Exception ex) {
             ex.printStackTrace();
         }
         long day = between / (24 * 60 * 60 * 1000);
         long hour = (between / (60 * 60 * 1000) - day * 24);
         mShengYuTime.setText(day+"天"+hour+"小时");
	}
	/**
	 * 处理未开始的众筹的距离开始的时间
	 * @param mStartMonth
	 * @param mStartHour
	 * @param endTime
	 */
	private void useWayCountTime(TextView mStartMonth, TextView mStartHour,
			String endTime) {
		String time=getChangeToTime(endTime);
		//计算截取月日以及小时数
		String month=getMonthAndDay(time);
		String hours=getHour(time);
		mStartMonth.setText(month);
        mStartHour.setText(hours);
        
        
         
         
	}
	/**
	 * 截取小时数
	 * @param time
	 * @return
	 */
	private String getHour(String time) {
		 String md=time.substring(5,10);
		return md;
	}
	/**
	 * 根据传入数据来计算进度条的进度
	 * @param mProgresssagebar_weikaishi
	 * @param allChipsBean
	 * @param i 
	 */
	private void useWayCountProgress(
			RopeProgressBar mProgresssagebar_weikaishi,
			AllChipsBean allChipsBean) {
		String targertmoney=allChipsBean.getTargetMoney();
		String havemoney=allChipsBean.getHasMoney();
		if(!TextUtils.isEmpty(targertmoney)&&!TextUtils.isEmpty(havemoney)){
			double tart=Double.parseDouble(targertmoney);
			double have=Double.parseDouble(havemoney);
			mProgresssagebar_weikaishi.setMax((int)tart);
			mProgresssagebar_weikaishi.setProgress((int)have);
			mProgresssagebar_weikaishi.setDynamicLayout(false);
			mProgresssagebar_weikaishi.setStrokeWidth(20);
			mProgresssagebar_weikaishi.setPrimaryColor(Color.rgb(70, 202, 253));
			mProgresssagebar_weikaishi.setSecondaryColor(Color.rgb(17, 26, 50));
		}else{
			mProgresssagebar_weikaishi.setMax(0);
			mProgresssagebar_weikaishi.setProgress(0);
			mProgresssagebar_weikaishi.setDynamicLayout(false);
			mProgresssagebar_weikaishi.setStrokeWidth(20);
			mProgresssagebar_weikaishi.setPrimaryColor(Color.rgb(70, 202, 253));
			mProgresssagebar_weikaishi.setSecondaryColor(Color.rgb(17, 26, 50));
		}
	}
	/**
	 * 根据传入数据来计算进度条的进度
	 * @param mProgresssagebar_weikaishi
	 * @param allChipsBean
	 * @param i 
	 */
	private void useWayCountProgressend(
			RopeProgressBarend mProgresssagebar_weikaishi,
			AllChipsBean allChipsBean) {
		String targertmoney=allChipsBean.getTargetMoney();
		String havemoney=allChipsBean.getHasMoney();
		if(!TextUtils.isEmpty(targertmoney)&&!TextUtils.isEmpty(havemoney)){
			int tart=(int)Double.parseDouble(targertmoney);
			int have=(int)Double.parseDouble(havemoney);
			mProgresssagebar_weikaishi.setMax(tart);
			mProgresssagebar_weikaishi.setProgress(have);
			mProgresssagebar_weikaishi.setDynamicLayout(false);
			mProgresssagebar_weikaishi.setStrokeWidth(20);
			mProgresssagebar_weikaishi.setPrimaryColor(Color.rgb(114, 114, 114));
			mProgresssagebar_weikaishi.setSecondaryColor(Color.rgb(17, 26, 50));
		}else{
			mProgresssagebar_weikaishi.setMax(0);
			mProgresssagebar_weikaishi.setProgress(0);
			mProgresssagebar_weikaishi.setDynamicLayout(false);
			mProgresssagebar_weikaishi.setStrokeWidth(20);
			mProgresssagebar_weikaishi.setPrimaryColor(Color.rgb(114, 114, 114));
			mProgresssagebar_weikaishi.setSecondaryColor(Color.rgb(17, 26, 50));
		}
	}
	/**
	 * 用来处理数据，保证数据不被科学记数法写错
	 * @param targetMoney
	 */
	private double checkAndChange(String targetMoney) {
		// TODO Auto-generated method stub
		BigDecimal bd = new BigDecimal(targetMoney); 
		String str = bd.toPlainString();
		double targetmoney=Double.parseDouble(str);
		return targetmoney;
	}
	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * @param starttime
	 * @param mShengTime 
	 * @param endtime 
	 * @param tag 
	 */
	private void initDate(String starttime, TextView mShengTime, String tag, String endtime) {
		// TODO Auto-generated method stub
		long starttimes= getChangeToTimes(starttime);
		long endtimes=getChangeToTimes(endtime);
		Date date = new Date();
		long currenttimes=date.getTime();
		//调用方法，计算两个时间之间差值
		if(tag.equals("1")){
			//说明是为开始的
			String str=calculationTime(endtimes,starttimes);
			mShengTime.setText(str);
		}else{
			String str=calculationTime(endtimes,currenttimes);
			mShengTime.setText(str);
		}
		
	}
	/**
	 * 计算时间差值
	 * @param starttimes
	 * @param currenttimes
	 */
	private String calculationTime(long starttimes, long currenttimes) {
//		  SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	         long between = 0;
	         try {
//	             java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
//	             java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
//	             between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
	        	 between =Math.abs(starttimes - currenttimes);
	        } catch (Exception ex) {
	             ex.printStackTrace();
	         }
	         long day = between / (24 * 60 * 60 * 1000);
	         long hour = (between / (60 * 60 * 1000) - day * 24);
	         long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
	         long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	         long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
	                 - min * 60 * 1000 - s * 1000);
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
	 * @param targetmoney
	 * @param hasMoney
	 * @param mCompleatPnum 
	 * @param mComMonNum 
	 */
	private void countProgress(MyProgressBar mProgresssagebar,
			long targetmoney, String hasMoney, TextView mCompleatPnum, TextView mComMonNum) {
		if(TextUtils.isEmpty(hasMoney))
			return;
		float hasmoney=Float.parseFloat(hasMoney);
		if(hasmoney>=0){
			float zhaanbi=(hasmoney*100)/(targetmoney*100);
			if(zhaanbi<=1){
				mProgresssagebar.setProgress(0);
				mCompleatPnum.setText("0%");
			}else{
				mProgresssagebar.setProgress((int)zhaanbi);
				mCompleatPnum.setText((int)zhaanbi+"%");
			}
			mComMonNum.setText("￥"+hasmoney+"元");
		}else{
			mProgresssagebar.setProgress(0);
			mCompleatPnum.setText("0%");
		}
		
	}
	
	/**
	 * 通过传入的已经达到的金额数和目标金额数来计算进度条占比
	 * @param mProgresssagebar
	 * @param targetmoney
	 * @param hasMoney
	 * @param mCompleatPnum 
	 * @param mComMonNum 
	 */
	private void countProgressed(MyProgressBar mProgresssagebar,
			double targetmoney, String hasMoney, TextView mCompleatPnum, TextView mComMonNum) {
		if(TextUtils.isEmpty(hasMoney))
			return;
		double hasmoney=Double.parseDouble(hasMoney);
		if(hasmoney>=0){
			double zhaanbi=(hasmoney*100)/(targetmoney*100);
			Log.i("目前占比是多少",zhaanbi+"");
			if(zhaanbi==0){
				mProgresssagebar.setProgress(0);
				mCompleatPnum.setText("0.00%");
			}else{
				mProgresssagebar.setProgress((int)(zhaanbi*100));
				double zhanbis=Utils.parseTwoNumber((zhaanbi*100)+"");
				mCompleatPnum.setText(zhanbis+"%");
			}
			double dangqianjine=Utils.parseTwoNumber(hasMoney);
			mComMonNum.setText("￥"+dangqianjine);
		}else{
			mProgresssagebar.setProgress(0);
			mCompleatPnum.setText("0.00%");
			mComMonNum.setText("￥0.00");
		}
		
	}
	public static boolean isNumeric(String str){
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
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
	static class viewholders{
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
		 * 状态的图片
		 */
		ImageView mStatusImage;
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
		 * 灰度背景
		 */
		RelativeLayout mLayout;
		/**
		 * 未开始的底部布局
		 */
		RelativeLayout mLayout_noaction;
		/**
		 * 开启月份,开启小时数。目标金额，进度图条
		 */
		TextView mStartMonth;
		TextView mStartHour;
		TextView mMubiaoMoney;
		RopeProgressBar mProgresssagebar_weikaishi;
		/**
		 * 进行中的底部布局
		 */
		RelativeLayout mLayout_ing;
		/**
		 * 已筹金额,参加人数，剩余时间，当前进度
		 */
		TextView mYichouJine;
		TextView mInPersonNum;
		TextView mShengYuTime;
		RopeProgressBar mProgresssagebar_zhongchouzhong;
		/**
		 * 已结束的底部布局---众筹成功
		 */
		RelativeLayout mLayout_success;
		TextView mTV_success_yichoujine;
		TextView mTv_success_canjiarenshu;
		RopeProgressBarend mProgresssagebar_success;
		/**
		 * 已结束的地步布局——————众筹失败
		 */
		RelativeLayout mLayoutFaild;
		TextView mTV_faild_yichoujine;
		TextView mTV_faild_canjiarenshu;
		RopeProgressBarend mProgresssagebar_faild;
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
