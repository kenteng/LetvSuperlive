package com.lesports.stadium.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MessageNotifyActivity;
import com.lesports.stadium.bean.SelfMessageBean;

public class MyMessageAdapter extends BaseAdapter {
	private int month;
	private Context context;
	//获取的时间
		int getTime;
	//布局文件
	//adapter_integral_detail
	List<SelfMessageBean> messageList;
	
	public MyMessageAdapter(Context context,List<SelfMessageBean> messageList){
		this.context=context;
		this.messageList=messageList;
	}

	@Override
	public int getCount() {
		if(null==messageList){
			return 0;
		}else{
			return messageList.size();
		}
		
	}

	@Override
	public Object getItem(int arg0) {
		return messageList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MessageInfoHolder messageInfoHolder=null;
		if(null==convertView){
			convertView = View.inflate(context,
					R.layout.adapter_message, null);
			messageInfoHolder=new MessageInfoHolder();
			messageInfoHolder.rl_top=(RelativeLayout) convertView.findViewById(R.id.rl_top);
			messageInfoHolder.iv_image=(ImageView) convertView.findViewById(R.id.iv_image);
			messageInfoHolder.iv_unread=(ImageView) convertView.findViewById(R.id.iv_unread);			
			messageInfoHolder.tv_time=(TextView) convertView.findViewById(R.id.tv_time);
			messageInfoHolder.tv_detail=(TextView) convertView.findViewById(R.id.tv_detail);
			messageInfoHolder.tv_about=(TextView) convertView.findViewById(R.id.tv_about);
			messageInfoHolder.tv_detail_top=(TextView) convertView.findViewById(R.id.tv_detail_top);
			messageInfoHolder.cb_isclick=(CheckBox) convertView.findViewById(R.id.cb_isclick);
			Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
			t.setToNow(); // 取得系统时间。  
			month = t.month+1;
			convertView.setTag(messageInfoHolder);
		}else{
			messageInfoHolder=(MessageInfoHolder) convertView.getTag();
		}
		messageInfoHolder.cb_isclick.setTag(messageList.get(position));
		messageInfoHolder.cb_isclick.setChecked(messageList.get(position).isCheck());
		messageInfoHolder.rl_top.setVisibility(View.GONE);
		String changeTime=getChangeToTime(messageList.get(position).getCreateTime());
	
		getTime=new Integer(changeTime.substring(5, 7));
		if(position==0){
			if(month==getTime){
				messageInfoHolder.rl_top.setVisibility(View.VISIBLE);
				messageInfoHolder.tv_detail_top.setText("本月");
			}else{
				messageInfoHolder.rl_top.setVisibility(View.VISIBLE);
				messageInfoHolder.tv_detail_top.setText(changeTime.substring(0, 4)+"-"+changeTime.substring(5, 7));
			}

//if(position!=0)
		}else if(position!=0) {
			String foreTime=getChangeToTime(messageList.get(position-1).getCreateTime());
			int fore=new Integer(foreTime.substring(5,7));
			int now=new Integer(changeTime.substring(5, 7));
			if(fore!=now){
				messageInfoHolder.rl_top.setVisibility(View.VISIBLE);
				messageInfoHolder.tv_detail_top.setText(changeTime.substring(0, 4)+"-"+changeTime.substring(5, 7));
			}
		}
		String time=parseDate(getChangeToTime(messageList.get(position).getCreateTime()));
		
		if(!"更早".equals(time)){
			messageInfoHolder.tv_time.setText(time+"  "+changeTime.substring(11,16));
		}else{
//			messageInfoHolder.tv_time.setText(changeTime.substring(20,22)
//					+"  "+changeTime.substring(5,7)+"-"+changeTime.substring(8,10)
//					);
			String timer;
			if(changeTime.contains("星"))
				timer = "周"+changeTime.substring(changeTime.length()-1)
						+"  "+changeTime.substring(5,7)+"-"+changeTime.substring(8,10);
			else
				timer = changeTime.substring(20,22)
						+"  "+changeTime.substring(5,7)+"-"+changeTime.substring(8,10);
			messageInfoHolder.tv_time.setText(timer);
		}
		
//		messageInfoHolder.tv_time.setText(DateUtil.getChangeToTime(messageList.get(position).getCreateTime()));
		messageInfoHolder.tv_detail.setText(messageList.get(position).getMessageRemark());
		messageInfoHolder.tv_about.setText(messageList.get(position).getMessageContent());
		switch (messageList.get(position).getMessageType()) {
		case "1":
			messageInfoHolder.iv_image.setBackgroundResource(R.drawable.message_award);
			break;
		case "2":
			messageInfoHolder.iv_image.setBackgroundResource(R.drawable.my_ka);
			break;
		case "3":
			messageInfoHolder.iv_image.setBackgroundResource(R.drawable.message_setting);
		break;

		default:
			break;
		}
		
		if("0".equals(messageList.get(position).getHasReaded())){
			messageInfoHolder.iv_unread.setVisibility(View.VISIBLE);
		}else{
			messageInfoHolder.iv_unread.setVisibility(View.GONE);
		}
		
		messageInfoHolder.cb_isclick.setVisibility(View.GONE);
		
		if(messageList.get(position).isEdit()){
			messageInfoHolder.cb_isclick.setVisibility(View.VISIBLE);
			messageInfoHolder.cb_isclick.setChecked(messageList.get(position).isCheck());  
		}
		
		final MessageInfoHolder holder=messageInfoHolder;
		
		messageInfoHolder.cb_isclick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SelfMessageBean  bean = (SelfMessageBean) buttonView.getTag();
				if(bean!=null){
					bean.setCheck(isChecked);
					//调用界面中的checkbox改变状态方法，刷新界面
					for(SelfMessageBean sfbean :messageList){
						if(sfbean.getId().equals(bean.getId())){
							sfbean.setCheck(isChecked);
							break;
						}
					}
					MessageNotifyActivity.instance.setRefrenshAllChoise(messageList);
				}else{
					Log.i("wxn", "zhegge");
				}
//				messageList.get(position).setCheck(isChecked);
			}
		});
		
		return convertView;
	}
	
	
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss E",
				Locale.getDefault());
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
	
	
	static class MessageInfoHolder {
		ImageView iv_image,iv_unread;
		TextView tv_time,tv_detail,tv_about,tv_detail_top;
		RelativeLayout rl_top;
		CheckBox cb_isclick;
	}
	

}
