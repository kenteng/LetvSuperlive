package com.lesports.stadium.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.IntegralBean;
import com.lesports.stadium.bean.IntegralTimeBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat") public class IntegralDetailAdapter extends BaseAdapter {
	/**
	 *积分详情集合
	 */
	IntegralBean integralDetailList = new IntegralBean();
	/**
	 *积分详情时间集合
	 */
	List<IntegralTimeBean> integralDetailTimeList = new ArrayList<IntegralTimeBean>();
	/**
	 *上下文
	 */
	private Context context;
	//布局文件
	//adapter_integral_detail
	//系统当前的时间
	private int month;
	//获取的时间
	int getTime;
	

	
	boolean isFirst=true;
	
	public IntegralDetailAdapter(Context context,IntegralBean integralDetailList,List<IntegralTimeBean> integralDetailTimeList){
		this.context=context;
		this.integralDetailList=integralDetailList;
		this.integralDetailTimeList=integralDetailTimeList;
	}

	@Override
	public int getCount() {
		if(null==integralDetailList){
			return 0;
		}else{
			return integralDetailList.getData().size();
		}

		
	}

	@Override
	public Object getItem(int arg0) {
		return integralDetailList.getData().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	

	@SuppressLint("UseValueOf") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IntegralInfoHolder integralInfoHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
				R.layout.adapter_integral_detail, null);
			integralInfoHolder=new IntegralInfoHolder();
			integralInfoHolder.tv_detail=(TextView) convertView.findViewById(R.id.tv_detail);
			integralInfoHolder.tv_detail_top=(TextView) convertView.findViewById(R.id.tv_detail_top);
			integralInfoHolder.tv_sum=(TextView) convertView.findViewById(R.id.tv_sum);
			integralInfoHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			integralInfoHolder.tv_money=(TextView) convertView.findViewById(R.id.tv_money);
			integralInfoHolder.iv_image=(ImageView) convertView.findViewById(R.id.iv_image);
			integralInfoHolder.rl_top=(RelativeLayout) convertView.findViewById(R.id.rl_top);
		
			convertView.setTag(integralInfoHolder);
		} else {
			integralInfoHolder = (IntegralInfoHolder) convertView.getTag();
		}
		Log.e("position", position+"");
		integralInfoHolder.rl_top.setVisibility(View.GONE);
		int topSum=0;
		if(null!=integralDetailList.getData().get(position).getMonth()){
			Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
			t.setToNow(); // 取得系统时间。  
			month = t.month+1;
			integralInfoHolder.rl_top.setVisibility(View.VISIBLE);
			String time=getChangeToTime(integralDetailList.getData().get(position).getMonth().toString());
			getTime=new Integer(time.substring(5, 7));
			topSum=new Integer(integralDetailList.getData().get(position).getAllIntegral());
			if(month==getTime){
				integralInfoHolder.rl_top.setVisibility(View.VISIBLE);
				if(topSum>0){
					integralInfoHolder.tv_sum.setText("+"+integralDetailList.getData().get(position).getAllIntegral());
				}else{
					integralInfoHolder.tv_sum.setText(integralDetailList.getData().get(position).getAllIntegral());
				}
				integralInfoHolder.tv_detail_top.setText("本月合计");
			}else{
				integralInfoHolder.rl_top.setVisibility(View.VISIBLE);
				if(topSum>0){
					integralInfoHolder.tv_sum.setText("+"+integralDetailList.getData().get(position).getAllIntegral());
				}else{
					integralInfoHolder.tv_sum.setText(integralDetailList.getData().get(position).getAllIntegral());
				}
//				integralInfoHolder.tv_sum.setText(integralDetailList.getData().get(position).getAllIntegral());
				integralInfoHolder.tv_detail_top.setText(getTime+"月合计");
		}
		}
		
		integralInfoHolder.tv_detail.setText(integralDetailList.getData().get(position).getRemark());
		String time=parseDate(getChangeToTime(integralDetailList.getData().get(position).getCreateTime()));
		String changeTime=getChangeToTime(integralDetailList.getData().get(position).getCreateTime());
		Log.e("wxn", changeTime+"........"+time);
		if(!"更早".equals(time)){
			integralInfoHolder.tv_time.setText(time+"  "+changeTime.substring(11,16));
		}else{
			String timer;
			if(changeTime.contains("星"))
				timer = "周"+changeTime.substring(changeTime.length()-1)
						+"  "+changeTime.substring(5,7)+"-"+changeTime.substring(8,10);
			else
				timer = changeTime.substring(20,22)
						+"  "+changeTime.substring(5,7)+"-"+changeTime.substring(8,10);
			integralInfoHolder.tv_time.setText(timer);
		}
		int everyNumber=new Integer(integralDetailList.getData().get(position).getIntegral());
		if(everyNumber>0){
			integralInfoHolder.tv_money.setText("+"+integralDetailList.getData().get(position).getIntegral());
		}else{
			integralInfoHolder.tv_money.setText(integralDetailList.getData().get(position).getIntegral());
		}
		
		if("0".equals(integralDetailList.getData().get(position).getGetFrom())){
			integralInfoHolder.iv_image.setBackgroundResource(R.drawable.my_ka);
		}else{
			integralInfoHolder.iv_image.setBackgroundResource(R.drawable.my_award);
		}
		return convertView;
	}
	
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss E");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);
		return timestring;

	}
	
	

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	private String parseDate(String createTime){    
        try {
            String ret = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss E");
            long create = sdf.parse(createTime).getTime();
            Calendar now = Calendar.getInstance();
            long ms  = 1000*(now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND));//毫秒数
            long ms_now = now.getTimeInMillis();
            if(ms_now-create<ms){
                ret = "今天";
            }else if(ms_now-create<(ms+24*3600*1000)){
                ret = "昨天";
            }else if(ms_now-create<(ms+24*3600*1000*2)){
                ret = "前天";
            }else{
                ret= "更早";
            }
            return ret;
            } catch (Exception e) {
            e.printStackTrace();
            }
        return null;
    }
	
	static class IntegralInfoHolder {
		ImageView iv_image;
		TextView tv_detail_top,tv_sum,tv_time,tv_detail,tv_money;
		RelativeLayout rl_top;

	}

}
