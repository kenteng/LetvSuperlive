package com.lesports.stadium.adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.ActivityFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 首界面listview的数据适配器
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
 *         ***************************************************************
 */

public class SenceFragmentListviewAdapter extends BaseAdapter {

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 数据源
	 */
	private List<SenceBean> mList;
	/**
	 * 该标记用来决定，listview当中展示哪一种类别的 默认为0.以所有类型的形式展示数据
	 */
	private int SHOW_TAG = 3;
	/**
	 * 用于标示全部类型的标记
	 */
	private final int ALL_TAG = 3;
	/**
	 * 用于标示进行中的标记
	 */
	private final int ING_TAG = 1;
	/**
	 * 用于标示未开始的标记
	 */
	private final int NOSTATRT_TAG = 0;
	/**
	 * 用于表示已结束的标记
	 */
	private final int EDDED_TAG = 2;
	/**
	 * 用于标示演唱会的标记
	 */
	private final int SONG_TAG = 4;
	/**
	 * 用于标示比赛的标记
	 */
	private final int GAME_TAG = 5;
	/**
	 * 保存着预约列表的数据集合
	 */
	private List<YuYueActivityBean> mYuyuelist=new ArrayList<YuYueActivityBean>();
	/**
	 * 该变量用来存储用户点击的活动的id
	 */
	private String activityid=null;
	/**
	 * 用来记录被点击的或者是当前item的下表
	 */
	private int position=0;
	/**
	 * String bid=null;
	 */
	private String bid=null;
	/**
	 * 预约数据的集合
	 */
	private List<YuYueActivityBean> list_yuyue; 
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1:
				// 这里是处理获取是否预约
				String backdataid = (String) msg.obj;
				Log.i("是否预约的数据", backdataid);
				if (backdataid != null && !TextUtils.isEmpty(backdataid)
						&& backdataid.length() != 0) {
					// 调用方法，解析数据
					jsonISyuyue(backdataid);
				}
				break;
			case 2:
				//处理取消预约
				String bacastring=(String) msg.obj;
				Log.i("取消预约数据",bacastring);
				if(bacastring!=null&&!TextUtils.isEmpty(bacastring)){
					if(bacastring.equals("success")){
						if(mList!=null&&mList.size()!=0){
							if(activityid!=null&&!TextUtils.isEmpty(activityid)){
								for(int i=0;i<mList.size();i++){
									if(mList.get(i).getId().equals(activityid)){
										mList.get(i).setBaoming(false);
										int num=Integer.parseInt(mList.get(i).getcHeat());
										if(num>=0){
											num=0;
										}else{
											num--;
										}
										mList.get(i).setcHeat(num+"");
										notifyDataSetChanged();
										for(int j=0;j<list_yuyue.size();j++){
											if(activityid.equals(list_yuyue.get(j).getActivityId())){
												list_yuyue.remove(j);
											}
										}
									}
								}
							}
						}
					}
					
				}
				break;
			case 3:
				// 这里是进行报名的
				String backdata = (String) msg.obj;
				Log.i("报名获取的数据是多少", backdata);
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
//					String status = jsonBaomingData(backdata);
					List<YuYueActivityBean> list=jsonway(backdata);
					if(list_yuyue!=null&&list_yuyue.size()!=0&&list==null&&list.size()!=0){
						list_yuyue.addAll(list);
						if(mList!=null&&mList.size()!=0){
							if(activityid!=null&&!TextUtils.isEmpty(activityid)){
								for(int i=0;i<mList.size();i++){
									if(mList.get(i).getId().equals(activityid)){
										mList.get(i).setBaoming(false);
										int num=Integer.parseInt(mList.get(i).getcHeat());
										num++;
										mList.get(i).setcHeat(num+"");
										notifyDataSetChanged();
									}
								}
							}
						}
					}else{
						
					}
				}
				break;
			case 6:
				//获取bid
				String biddata=(String) msg.obj;
				if (biddata != null && !TextUtils.isEmpty(biddata)
						&& biddata.length() != 0) {
					// 调用方法，解析数据
					jsonISyuyueID(biddata);
				}
				break;
			default:
				break;
			}

		}


	};
	

	/**
	 * 用来获取活动的bid
	 */
	private void jsonISyuyueID(String backdataid) {
		// TODO Auto-generated method stub
		try {
			JSONArray array=new JSONArray(backdataid);
			for(int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				if(obj.has("bid")){
					bid=obj.getString("bid");
				}
				
			}
			if(bid!=null&&!TextUtils.isEmpty(bid)){
				userUtilsGetDataquxiao(bid);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 用来查询用户是否已经预约该活动
	 * [
    {
        "activityId": 31,
        "bStatus": 1,
        "bid": 292,
        "userId": 171828113
    }
]
	 * @param backdataid
	 */
	private void jsonISyuyue(String backdataid) {
		// TODO Auto-generated method stub
		String activityid=null;
		String status=null;
		String userid=null;
		try {
			JSONArray array=new JSONArray(backdataid);
			for(int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				if(obj.has("bid")){
					bid=obj.getString("bid");
				}
				if(obj.has("activityId")){
					activityid=obj.getString("activityId");
				}
				if(obj.has("bStatus")){
					status=obj.getString("bStatus");
				}
				if(obj.has("userId")){
					userid=obj.getString("userId");
				}
				
			}
			//先判断状态
			if(status.equals("0")){
				//说明未报名
				if(mList!=null&&mList.size()!=0){
					mList.get(position).setBaoming(true);
				}else{
					mList.get(position).setBaoming(false);
				}
				notifyDataSetChanged();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/**
	 * 处理活动列表数据
	 * 
	 * @param backdataid
	 */
	private List<YuYueActivityBean> jsonway(String backdataid) {
		// TODO Auto-generated method stub
		List<YuYueActivityBean> list = new ArrayList<YuYueActivityBean>();
		try {
			JSONArray array = new JSONArray(backdataid);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				YuYueActivityBean bean = new YuYueActivityBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("bid")) {
					bean.setBid(obj.getString("bid"));
				}
				if (obj.has("bStatus")) {
					bean.setbStatus(obj.getString("bStatus"));
				}
				if (obj.has("userId")) {
					bean.setUserId(obj.getString("userId"));
				}
				list.add(bean);

			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	};


	/**
	 * @param context
	 * @param list
	 */
	public SenceFragmentListviewAdapter(Context context, List<SenceBean> list) {
		this.mContext = context;
		this.mList = list;
		
	}

	/**
	 * 提供给界面中用于动态改变适配器内数据以及类别标记
	 * 
	 * @2016-2-26上午10:24:56
	 */
	public void setTagandData(int tag, List<SenceBean> list) {
		this.mList = list;
		this.SHOW_TAG = tag;
		notifyDataSetChanged();
	}
	/**
	 * 提供给界面中用于动态改变适配器内数据以及类别标记
	 * 
	 * @2016-2-26上午10:24:56
	 */
	public void setData(List<SenceBean> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		viewHolder vh = null;
		if (arg1 == null) {
			vh = new viewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater
					.inflate(R.layout.sence_fragment_listview_item, null);
			vh.mBigtitle = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_bittitle);
			vh.mExplain = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_explain);
			vh.mLocation = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_locationinfo);
			vh.mPersonNum = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_personnum);
			vh.mStatus = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_state_text);
			vh.mStatusBg = (ImageView) arg1
					.findViewById(R.id.sence_fragment_listview_state_image);
			vh.mTimeType = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_time_shangwuhaishixiawu);
			vh.mTimeStart = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_time_starttime);
			vh.mTimeEnd = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_time_endtime);
			vh.mTitle = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_title);
			vh.mTimeGl = (TextView) arg1
					.findViewById(R.id.sence_fragment_listview_time_ganggang);
			vh.mBgImage = (ImageView) arg1
					.findViewById(R.id.sence_frament_listview_image);
			vh.isBaoming = (ImageView) arg1.findViewById(R.id.icon_hot_no);
			arg1.setTag(vh);
		} else {
			vh = (viewHolder) arg1.getTag();
		}

		if (SHOW_TAG == ALL_TAG) {
			// 说明是要展示所有数据
			if(mList.size()!=0){
				int status = Integer.parseInt(mList.get(arg0).getcStatus());
				if (status == NOSTATRT_TAG) {
					// 说明这里是未开始
					vh.mTimeGl.setVisibility(View.VISIBLE);
					vh.mTimeEnd.setVisibility(View.VISIBLE);
					vh.mTimeStart.setVisibility(View.VISIBLE);
					vh.mTimeType.setVisibility(View.VISIBLE);
					// 设置最大标题为月日时间
					setTimeToBigTitle(vh.mBigtitle, mList.get(arg0).getStarttime());
					vh.mBigtitle.setTextColor(Color.WHITE);
					// 计算当前时间是上午还是下午，并且将至设置到时间类型控件
					countTimesType(vh.mTimeType, mList.get(arg0).getStarttime());
					// 将开始时间的小时分钟数设置到控件上
					setStartTimeToUI(vh.mTimeStart, mList.get(arg0).getStarttime());
					// 将结束时间设置到控件上
					setEndTimeToUI(vh.mTimeEnd, mList.get(arg0).getEndtime());
					vh.mStatusBg.setImageBitmap(readBitMap(mContext,
							R.drawable.activity_nostart));
					vh.mStatus.setText("未开始");
					vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人想参加");
					vh.mExplain.setText(mList.get(arg0).getTitle());
				} else if (status == ING_TAG) {
					// 说明是正在进行中
					vh.mBigtitle.setText("NOW");
					vh.mTimeGl.setVisibility(View.GONE);
					vh.mTimeEnd.setVisibility(View.GONE);
					vh.mTimeStart.setVisibility(View.GONE);
					vh.mTimeType.setVisibility(View.GONE);
					vh.mStatusBg.setImageBitmap(readBitMap(mContext,
							R.drawable.button_underway));
					vh.mStatus.setText("进行中");
					vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
					vh.mExplain.setText(mList.get(arg0).getTitle());
				} else if (status == EDDED_TAG) {
					vh.mStatusBg.setImageBitmap(readBitMap(mContext,
							R.drawable.activity_ended));
					vh.mStatus.setText("已结束");
					vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
					vh.mExplain.setText(mList.get(arg0).getTitle());
				}
			}
			
		} else if (SHOW_TAG == NOSTATRT_TAG) {
			// 说明要展示未开始
			vh.mTimeGl.setVisibility(View.VISIBLE);
			vh.mTimeEnd.setVisibility(View.VISIBLE);
			vh.mTimeStart.setVisibility(View.VISIBLE);
			vh.mTimeType.setVisibility(View.VISIBLE);
			// 设置最大标题为月日时间
			setTimeToBigTitle(vh.mBigtitle, mList.get(arg0).getStarttime());
			// 计算当前时间是上午还是下午，并且将至设置到时间类型控件
			countTimesType(vh.mTimeType, mList.get(arg0).getStarttime());
			// 将开始时间的小时分钟数设置到控件上
			setStartTimeToUI(vh.mTimeStart, mList.get(arg0).getStarttime());
			// 将结束时间设置到控件上
			setEndTimeToUI(vh.mTimeEnd, mList.get(arg0).getEndtime());
			vh.mStatusBg.setImageBitmap(readBitMap(mContext,
					R.drawable.activity_nostart));
			vh.mStatus.setText("未开始");
			vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人已报名");
			vh.mExplain.setText(mList.get(arg0).getLabel());
			vh.mTitle.setText(mList.get(arg0).getVenueName());
		} else if (SHOW_TAG == ING_TAG) {
			// 要展示进行中的所有数据
			vh.mBigtitle.setText("NOW");
			vh.mTimeGl.setVisibility(View.GONE);
			vh.mTimeEnd.setVisibility(View.GONE);
			vh.mTimeStart.setVisibility(View.GONE);
			vh.mTimeType.setVisibility(View.GONE);
			vh.mStatusBg.setImageBitmap(readBitMap(mContext,
					R.drawable.button_underway));
			vh.mStatus.setText("进行中");
			vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人已报名");
			vh.mExplain.setText(mList.get(arg0).getLabel());
		} else if (SHOW_TAG == EDDED_TAG) {
			// 要展示已结束的所有数据
			vh.mStatusBg.setImageBitmap(readBitMap(mContext,
					R.drawable.activity_ended));
			vh.mStatus.setText("已结束");
			vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人已报名");
			vh.mExplain.setText(mList.get(arg0).getLabel());
		} else if (SHOW_TAG == SONG_TAG) {
			// 说明是要展示演唱会的所有数据，这里对演唱会不做进行开始结束区分，全部展示就好
			int status = Integer.parseInt(mList.get(arg0).getcStatus());
			if (status == NOSTATRT_TAG) {
				// 说明这里是未开始
				vh.mTimeGl.setVisibility(View.VISIBLE);
				vh.mTimeEnd.setVisibility(View.VISIBLE);
				vh.mTimeStart.setVisibility(View.VISIBLE);
				vh.mTimeType.setVisibility(View.VISIBLE);
				// 设置最大标题为月日时间
				setTimeToBigTitle(vh.mBigtitle, mList.get(arg0).getStarttime());
				// 计算当前时间是上午还是下午，并且将至设置到时间类型控件
				countTimesType(vh.mTimeType, mList.get(arg0).getStarttime());
				// 将开始时间的小时分钟数设置到控件上
				setStartTimeToUI(vh.mTimeStart, mList.get(arg0).getStarttime());
				// 将结束时间设置到控件上
				setEndTimeToUI(vh.mTimeEnd, mList.get(arg0).getEndtime());
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.activity_nostart));
				vh.mStatus.setText("未开始");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人想参加");
				vh.mExplain.setText(mList.get(arg0).getLabel());
			} else if (status == ING_TAG) {
				// 说明是正在进行中
				// 最大标题需要显示now，并且该下面时间需要隐藏
				vh.mBigtitle.setText("NOW");
				vh.mTimeGl.setVisibility(View.GONE);
				vh.mTimeEnd.setVisibility(View.GONE);
				vh.mTimeStart.setVisibility(View.GONE);
				vh.mTimeType.setVisibility(View.GONE);
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.button_underway));
				vh.mStatus.setText("进行中");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
			} else if (status == EDDED_TAG) {
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.activity_ended));
				// vh.mBgImage.setBackground(mContext.getResources().getDrawable(R.drawable.tiyuhaibao));
				vh.mStatus.setText("已结束");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
				vh.mExplain.setText(mList.get(arg0).getLabel());
			}
		} else if (SHOW_TAG == GAME_TAG) {
			// 说明是要展示比赛的所有数据，这里对演唱会不做进行开始结束区分，全部展示就好
			int status = Integer.parseInt(mList.get(arg0).getcStatus());
			if (status == NOSTATRT_TAG) {
				// 说明这里是未开始
				vh.mTimeGl.setVisibility(View.VISIBLE);
				vh.mTimeEnd.setVisibility(View.VISIBLE);
				vh.mTimeStart.setVisibility(View.VISIBLE);
				vh.mTimeType.setVisibility(View.VISIBLE);
				// 设置最大标题为月日时间
				setTimeToBigTitle(vh.mBigtitle, mList.get(arg0).getStarttime());
				// 计算当前时间是上午还是下午，并且将至设置到时间类型控件
				countTimesType(vh.mTimeType, mList.get(arg0).getStarttime());
				// 将开始时间的小时分钟数设置到控件上
				setStartTimeToUI(vh.mTimeStart, mList.get(arg0).getStarttime());
				// 将结束时间设置到控件上
				setEndTimeToUI(vh.mTimeEnd, mList.get(arg0).getEndtime());
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.activity_nostart));
				// vh.mBgImage.setBackground(mContext.getResources().getDrawable(R.drawable.lyric_bg));
				vh.mStatus.setText("未开始");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人想参加");
				vh.mExplain.setText(mList.get(arg0).getLabel());
			} else if (status == ING_TAG) {
				// 说明是正在进行中
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.button_underway));
				// vh.mBgImage.setBackground(mContext.getResources().getDrawable(R.drawable.testimagesss));
				vh.mStatus.setText("进行中");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
			} else if (status == EDDED_TAG) {
				vh.mStatusBg.setImageBitmap(readBitMap(mContext,
						R.drawable.activity_ended));
				// vh.mBgImage.setBackground(mContext.getResources().getDrawable(R.drawable.tiyuhaibao));

				vh.mStatus.setText("已结束");
				vh.mPersonNum.setText(mList.get(arg0).getcHeat() + "人参加");
				vh.mExplain.setText(mList.get(arg0).getLabel());
			}
		}
		vh.mLocation.setText(mList.get(arg0).getVenueName());
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+ mList.get(arg0).getFrontCoverImageURL() + ConstantValue.IMAGE_END, vh.mBgImage,
				R.drawable.huodongshouye_zhanwei);
		vh.mTitle.setText(mList.get(arg0).getSubhead());
		initStartTime(vh.mTitle, mList.get(arg0).getStarttime());
		if(mList.get(arg0).isBaoming()){
			vh.isBaoming.setImageBitmap(readBitMap(mContext,
					R.drawable.icon_yijingbaoming));
			vh.mPersonNum.setTextColor(Color.RED);
		}else{
			vh.isBaoming.setImageBitmap(readBitMap(mContext,
					R.drawable.huodong_weicanjia));
			vh.mPersonNum.setTextColor(Color.rgb(68,195, 244));
		}
		//先判断活动类型
		int status=Integer.parseInt(mList.get(arg0).getcStatus());
		useWayAddOnclick(status,vh.isBaoming,mList.get(arg0).getId(),mList.get(arg0).isBaoming());
		return arg1;
	}
	/**
	 * 处理点击事件
	 * @param status
	 * @param isBaoming
	 * @param string 
	 * @param b 
	 */
	private void useWayAddOnclick(int status, final ImageView isBaoming, final String string, final boolean b) {
		// TODO Auto-generated method stub
		switch (status) {
		case 0:
			isBaoming.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("未开始", "测试点击事件");
					if(b){
						//拿着活动id去quxiao报名，
						ActivityFragment.instance.IngBaomingQuxiao(string);
//						isBaoming.setImageBitmap(readBitMap(mContext,
//								R.drawable.icon_hot_no));
					}else{
						//baoming 
						ActivityFragment.instance.IngBaoming(string);
//						isBaoming.setImageBitmap(readBitMap(mContext,
//								R.drawable.icon_yijingbaoming));
					}
				}
			});
			break;
		case 1:
			isBaoming.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("进行中", "测试点击事件");
				}
			});
			break;
		case 2:
			isBaoming.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("已结束", "测试点击事件");
				}
			});
	
			break;

		default:
			break;
		}
	}

	/**
	 * 用来检查用户是否预约该活动
	 * @param id
	 * @return
	 */
	private boolean checkActivityIsYuyue(String id) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		if(list_yuyue!=null&&list_yuyue.size()!=0){
			for(int i=0;i<list_yuyue.size();i++){
				if(id.equals(list_yuyue.get(i).getActivityId())){
					ishave=true;
					break;
				}else{
					ishave=false;
				}
			}
		}
		return ishave;
	}

	/**
	 * 根据传入的活动id，用户来获取相对应的bid
	 * @param id
	 */
	private String getBid(String id) {
		// TODO Auto-generated method stub
		String bid=null;
		for(int i=0;i<mYuyuelist.size();i++){
			YuYueActivityBean bean=mYuyuelist.get(i);
			if(bean.getActivityId().equals(id)){
				bid=bean.getBid();
				Log.i("bid是多少",bid);
				break;
			}
		}
		return bid;
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao 
	 */
	private void huoquyonghuyuyueliebiao() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YONGHUYUYUE_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {

							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 10;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}
				}, false,false);
	}
	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetDataxsbaoming(String huodongid, String userid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", huodongid);
		params.put("userId", userid);
		params.put("bStatus", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {

							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 3;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}
				}, false,false);
	}
	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetDataquxiao(String bid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("bid", bid);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.QUXIAOYUYUE_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {

							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {

								} else {
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 2;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}
				}, false,false);
	}

	/**
	 * 用来获取该活动的bid，也就是该用户对于该活动的bis
	 */
	public void getYuyueBid(String userid,String activity) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userid);
		params.put("activityId",activity);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACIVITY_IS_YUYUE, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									Log.i(mContext.getClass().getName(),
											backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 6;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false,false);
	}
	/**
	 *用来判断用户有没有预约该活动
	 */
	public void getYuyueActivity(String userid,String activity) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userid);
		params.put("activityId",activity);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACIVITY_IS_YUYUE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									Log.i(mContext.getClass().getName(),
											backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 1;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false,false);
	}

	/**
	 * 将结束时间转换后设置到控件上
	 * 
	 * @param mTimeEnd
	 * @param endtime
	 */
	private void setEndTimeToUI(TextView mTimeEnd, String endtime) {
		// TODO Auto-generated method stub
		String str = getChangeToTime(endtime);
		String hm = getHourAndMinute(str);
		mTimeEnd.setText(hm);
	}

	/**
	 * //根据传入开始时间，截取小时数，来进行设置
	 * 
	 * @param mTimeStart
	 * @param starttime
	 */
	private void setStartTimeToUI(TextView mTimeStart, String starttime) {
		// TODO Auto-generated method stub
		String str = getChangeToTime(starttime);
		String hm = getHourAndMinute(str);
		mTimeStart.setText(hm);
	}

	/**
	 * 根据当前传入时间，来计算当前时间是pm还是am
	 * 
	 * @param mTimeType
	 * @param starttime
	 */
	private void countTimesType(TextView mTimeType, String starttime) {
		// TODO Auto-generated method stub
		String str = getChangeToTime(starttime);
		String hm = getHourAndMinute(str);
		/**
		 * 截取小时数
		 */
		String hour = hm.substring(0, 2);
		int hours = Integer.parseInt(hour);
		if (hours < 12) {
			// 说明是上午
			mTimeType.setText("AM");
		} else {
			mTimeType.setText("PM");
		}
	}

	/**
	 * 根据传入控件和时间，将时间数值设置到空间上
	 * 
	 * @param mBigtitle
	 * @param starttime
	 */
	private void setTimeToBigTitle(TextView mBigtitle, String starttime) {
		// TODO Auto-generated method stub
		String str = getChangeToTime(starttime);
		String md = getMonthAndDay(str);
		mBigtitle.setText(md);
		// Typeface
		// face=Typeface.createFromAsset(mContext.getAssets(),"ratchicons.ttf");
		// mBigtitle.setTypeface(face);
	}

	/**
	 * 将传入的时间字符串，转换成相应的时间，并且将之设置到控件上
	 * 
	 * @param mTitle
	 * @param string
	 */
	private void initStartTime(TextView mTitle, String string) {
		// TODO Auto-generated method stub
		String str = getChangeToTime(string);
		Log.i("得到的开始时间是多少", str);
		// 截取月日 2015.12.14.00.00.00
		String md = getMonthAndDay(str);
		Log.i("现在的月日是多少", md);
		String hm = getHourAndMinute(str);
		Log.i("现在获取的小时分钟数是", hm);
	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}

	/**
	 * 根据传入的字符串时间值，截取当前月日时间字符串
	 * 
	 * @param str
	 * @return
	 */
	public String getMonthAndDay(String str) {
		String md = str.substring(5, 10);
		return md;

	}

	/**
	 * 根据传入的时间值，截取小时和分钟数
	 * 
	 * @param str
	 * @return
	 */
	public String getHourAndMinute(String str) {
		String hm = str.substring(11, 16);
		return hm;
	}

	static class viewHolder {
		/**
		 * 大标题
		 */
		TextView mBigtitle;
		/**
		 * 说明
		 */
		TextView mExplain;
		/**
		 * 标题
		 */
		TextView mTitle;
		/**
		 * time类型，上午还是下午，只用在未开始的item中，开始或者结束的需要隐藏
		 */
		TextView mTimeType;
		/**
		 * 开始时间
		 */
		TextView mTimeStart;
		/**
		 * 结束时间
		 */
		TextView mTimeEnd;
		/**
		 * 时间隔离符号
		 */
		TextView mTimeGl;
		/**
		 * 地理位置信息
		 */
		TextView mLocation;
		/**
		 * 参加人数
		 */
		TextView mPersonNum;
		/**
		 * 活动的状态，分为三种，比赛中，未开始，已结束
		 */
		TextView mStatus;
		/**
		 * 活动状态的背景图片
		 */
		ImageView mStatusBg;
		/**
		 * 展示背景的image
		 */
		ImageView mBgImage;
		/**
		 * 用户是否报名
		 */
		ImageView isBaoming;
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
