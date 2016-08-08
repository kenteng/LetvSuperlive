package com.lesports.stadium.adapter;

import java.io.Serializable;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.CrowdPayActivty;
import com.lesports.stadium.bean.CrowReportBackBean;
import com.lesports.stadium.utils.GlobalParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportBackAdapter extends BaseAdapter {

	private Context mContext;
	private List<CrowReportBackBean> list;
	private String tag;
	public ReportBackAdapter(Context mconContext,List<CrowReportBackBean> list,String tag){
		this.mContext=mconContext;
		this.list=list;
		this.tag=tag;
	}
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewho vh=null;
		if(convertView!=null){
			vh=new viewho();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.reportback_item_view,null);
			vh.mPrice=(TextView) convertView.findViewById(R.id.new_reportback_lv_item_moneyNum);
			vh.mLimit=(TextView) convertView.findViewById(R.id.new_reportback_lv_item_guigeyaoqiu);
			vh.mZhichi=(TextView) convertView.findViewById(R.id.new_reportback_lv_item_woyaozhichi);
			vh.mHuibaoneirong=(TextView) convertView.findViewById(R.id.new_reportback_lv_item_huibaoneirong);
			convertView.setTag(vh);
			
		}else{
			vh=(viewho) convertView.getTag();
		}
		vh.mHuibaoneirong.setText(list.get(position).getReturnContent());
		if(tag.equals("yes")){
			//说明是已结束的
			vh.mZhichi.setTextColor(Color.GRAY);
		}else{
			if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
				Intent intent=new Intent(mContext, CrowdPayActivty.class);
				intent.putExtra("group",(Serializable)list);
				mContext.startActivity(intent);
			}else{
			}
		}
		return null;
	}
	
	static class viewho{
		TextView mPrice;
		TextView mLimit;
		TextView mZhichi;
		TextView mHuibaoneirong;
	}

}
