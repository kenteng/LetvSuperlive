package com.lesports.stadium.adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MyCouponActivity;
import com.lesports.stadium.activity.OnlineContainActivity;
import com.lesports.stadium.activity.ShoppingActivity;
import com.lesports.stadium.activity.UserCarActivity;
import com.lesports.stadium.bean.YouHuiBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 优惠券适配器
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MyCounponAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<YouHuiBean> mList;
	/**
	 * 该标记用来标注是否失效
	 */
	/**
	 * 上下文
	 */
	private Context mContext;
	public MyCounponAdapter(List<YouHuiBean> list,Context context){
		this.mList=list;
		this.mContext=context;
	}
	public void refreshUI(List<YouHuiBean> list){
		this.mList=list;
		notifyDataSetChanged();
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

	@SuppressLint("InflateParams") @Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		viewholder_coupon vh=null;
		if(arg1==null){
			vh=new viewholder_coupon();
			@SuppressWarnings("static-access")
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_coupon_listview_item_my,null);
			vh.mClass=(ImageView) arg1.findViewById(R.id.my_youhuiquan_item_zhongleitupian);
			vh.mDate=(TextView) arg1.findViewById(R.id.my_youhuiquan_item_youxiaoriqi_riqi);
			vh.mIschoise=(ImageView) arg1.findViewById(R.id.my_youhuiquan_item_shifouxuanzhongkuang);
			vh.mName=(TextView) arg1.findViewById(R.id.my_youhuiquan_item_name);
			vh.mShuoming=(TextView) arg1.findViewById(R.id.my_youhuiquan_item_youhuiquanshiyongshuoming);
			vh.mPrice=(TextView) arg1.findViewById(R.id.my_youhuiquan_item_jinqianshu);
			vh.mLayout_huidu=(RelativeLayout) arg1.findViewById(R.id.my_layout_youhuiquan_yijingshixiao);
			vh.mGotuUse=(TextView) arg1.findViewById(R.id.my_youhuiquan_item_qushiyong);
			arg1.setTag(vh);
		}else{
			vh=(viewholder_coupon) arg1.getTag();
		}
		if(mList.get(arg0).getStatus().equals("2")){
			vh.mLayout_huidu.setVisibility(View.VISIBLE);
			vh.mPrice.setText("￥"+mList.get(arg0).getCouponPrice());
			vh.mPrice.setTextColor(Color.GRAY);
			vh.mGotuUse.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.usecoupon_style));
		}else{
			vh.mLayout_huidu.setVisibility(View.GONE);
			vh.mPrice.setText("￥"+mList.get(arg0).getCouponPrice());
		}
		//这里需要判断数据类型来填写名称
		String type=mList.get(arg0).getCouponType();
		if(!TextUtils.isEmpty(type)){
			if(type.equals("1")){
				vh.mName.setText("餐饮券");
				if(mList.get(arg0).getStatus().equals("1")&&mList.get(arg0).getInusing().equals("0")){
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.canyinquan));
				}else{
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.canyin_bukeyong));
				}
			}else if(type.equals("4")){
				vh.mName.setText("商品券");
				if(mList.get(arg0).getStatus().equals("1")&&mList.get(arg0).getInusing().equals("0")){
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.shangpinquan));
				}else{
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.shangpin_bukeyong));
				}
			}else if(type.equals("2")){
				vh.mName.setText("用车券");
				if(mList.get(arg0).getStatus().equals("1")&&mList.get(arg0).getInusing().equals("0")){
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.yongchequan));
				}else{
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.yongche_bukeyong));
				}
			}else if(type.equals("3")){
				vh.mName.setText("购票券");
				if(mList.get(arg0).getStatus().equals("1")&&mList.get(arg0).getInusing().equals("0")){
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.goupiaoquan));
				}else{
					vh.mClass.setImageBitmap(readBitMap(mContext,
							R.drawable.goupiao_bukeyong));
				}
			}
		}
		//判断是否被选中
		if(mList.get(arg0).isChoise()){
			vh.mIschoise.setVisibility(View.VISIBLE);
		}else{
			vh.mIschoise.setVisibility(View.INVISIBLE);
		}
		//调用方法，处理时间
		useWayTime(vh.mDate,mList.get(arg0).getBeginTime(),mList.get(arg0).getEndTime());
		vh.mShuoming.setText("订单满"+mList.get(arg0).getCouponCondition()+"元使用");
		vh.mGotuUse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent;
				if(mList.get(arg0).getStatus().equals("1")){
					// 先判断类型
					if (mList.get(arg0).getCouponType().equals("1")) {
						// 说是餐饮
						// 需要跳转到兽界面，然后进行餐饮选择
//						MainActivity.instance.tag_view=0;
//						MainActivity.instance.initIntentData("canyin");
						intent=new Intent(mContext,OnlineContainActivity.class);
						mContext.startActivity(intent);
						MyCouponActivity.intance.finish();
					} else if (mList.get(arg0).getCouponType().equals("4")) {
						//商品
//						MainActivity.instance.tag_view=1;
//						MainActivity.instance.initIntentData("shangpin");
						intent=new Intent(mContext,ShoppingActivity.class);
						mContext.startActivity(intent);
						MyCouponActivity.intance.finish();
					}else if(mList.get(arg0).getCouponType().equals("2")){
						//用车
//						MainActivity.instance.tag_view=2;
//						MainActivity.instance.initIntentData("yongche");
						intent=new Intent(mContext,UserCarActivity.class);
						mContext.startActivity(intent);
						MyCouponActivity.intance.finish();
					}else if(mList.get(arg0).getCouponType().equals("3")){
						//购票
						MyCouponActivity.intance.finish();
//						MainActivity.instance.initIntentData("goupiao");
					}
				}else{
					Toast.makeText(mContext, "对不起，该优惠券已经失效", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return arg1;
	}
	
	/**
	 * 用来处理优惠券有效期
	 * @param mDate
	 * @param beginTime
	 * @param endTime
	 */
	private void useWayTime(TextView mDate, String beginTime, String endTime) {
		String starttime=getChangeToTime(beginTime);
		String endtime=getChangeToTime(endTime);
		String start=getMonthAndDay(starttime);
		String end=getMonthAndDay(endtime);
		mDate.setText(start+"—"+end);
	}


	static class viewholder_coupon{
		/**
		 * 优惠券名称
		 */
		TextView mName;
		/**
		 * 优惠券说明
		 */
		TextView mShuoming;
		/**
		 * 优惠券日期
		 */
		TextView mDate;
		/**
		 * 优惠券种类图片
		 */
		ImageView mClass;
		/**
		 * 优惠券是否被选中
		 */
		ImageView mIschoise;
		/**
		 * 优惠券价格
		 */
		TextView mPrice;
		/**
		 * 灰度背景
		 */
		RelativeLayout mLayout_huidu;
		/**
		 * 去使用
		 */
		private TextView mGotuUse;
	}
	/**
	 * 根据传入的字符串时间值，截取当前年月日时间字符串
	 * 
	 * @param str
	 * @return
	 */
	public String getMonthAndDay(String str) {
		String md = str.substring(0, 10);
		return md;

	}
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}
	/**
	 * 用来处理图片，目的是为了节省内存
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		// 获取资源图片

		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);

	}


}
