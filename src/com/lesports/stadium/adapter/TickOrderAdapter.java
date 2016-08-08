package com.lesports.stadium.adapter;

import java.util.ArrayList;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.TickOrderBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TickOrderAdapter extends BaseAdapter {

	private ArrayList<TickOrderBean> listbean;
	private Context context;
	public TickOrderAdapter(Context context){
		this.context = context;
	}

	public void setDate(ArrayList<TickOrderBean> listbean) {
		this.listbean = listbean;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return listbean == null ? 0 : listbean.size();
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
		ViewHolder holder = null;
		TickOrderBean bean = listbean.get(position);
		if(null==convertView){
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_tick_order, null);
			holder.iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
			holder.tick_price = (TextView) convertView.findViewById(R.id.tick_price);
			holder.instruction = (TextView) convertView.findViewById(R.id.instruction);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		LApplication.loader
			.DisplayImage(bean.getPicUrl(), holder.iv_tick, R.drawable.zhongchouliebiao_zhanwei);
		holder.tick_price.setText("价格：" + bean.getPrice()+" X "+bean.getWareNumber() + "￥");
		holder.instruction.setText(bean.getSeatNumber());
		return convertView;
	}
	class ViewHolder{
		public ImageView iv_tick;
		public TextView tick_price;
		public TextView instruction;
		
	}

}
