package com.lesports.stadium.bean;

import java.util.ArrayList;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PowerAdapter extends BaseAdapter{
	private Context context;
	/**
	 * 数据存储
	 */
	private List<PowerBean> powerList=new ArrayList<PowerBean>();
	public PowerAdapter(){}
	
	public PowerAdapter(Context context, List<PowerBean> powerList) {
		super();
		this.context = context;
		this.powerList = powerList;
	}

	public List<PowerBean> getPowerList() {
		return powerList;
	}

	public void setPowerList(List<PowerBean> powerList) {
		this.powerList = powerList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return powerList.size()>3?powerList.size()-3:0;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder=null;
		if(convertView==null){
			mHolder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_power, null);
			mHolder.positionTv=(TextView) convertView.findViewById(R.id.power_item_position_tv);
			mHolder.headCimg=(CircleImageView)convertView.findViewById(R.id.power_item_head_civ);
			mHolder.nameTv=(TextView)convertView.findViewById(R.id.power_item_name_tv);
			mHolder.numberTv=(TextView)convertView.findViewById(R.id.power_item_number_tv);
			convertView.setTag(mHolder);
		}else{
			mHolder=(Holder) convertView.getTag();
		}
		mHolder.positionTv.setText(""+(position+4));
		mHolder.nameTv.setText(powerList.get(position+3).getNickname());
		mHolder.numberTv.setText(powerList.get(position+3).getEnergy());
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+ powerList.get(position+3).getPicture()
				+ ConstantValue.IMAGE_END, mHolder.headCimg,
				R.drawable.default_header);
		return convertView;
	}
class Holder{
	private TextView positionTv;
	private CircleImageView headCimg;
	private TextView nameTv;
	private TextView numberTv;
}
}
