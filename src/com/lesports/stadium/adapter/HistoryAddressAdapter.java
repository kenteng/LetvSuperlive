package com.lesports.stadium.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ***************************************************************
 * @ClassName:  HistoryAddressAdapter 
 * 
 * @Desc :下车地址适配器
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-22 下午4:55:22
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class HistoryAddressAdapter extends BaseAdapter {
	private Context context;
	private List<Address> addressList=new ArrayList<Address>();
	
	public HistoryAddressAdapter(Context context, List<Address> addressList) {
		super();
		this.context = context;
		this.addressList = addressList;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<Address> addressList) {
		this.addressList = addressList;
	}

	@Override
	public int getCount() {
		return addressList.size();
	}

	@Override
	public Object getItem(int position) {
		return Utils.isNullOrEmpty(addressList)?null:addressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		Address address=new Address();
		address=addressList.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_history_address, null);
			holder.addressTv=(TextView)convertView.findViewById(R.id.tv_address);
			holder.detailAddressTv=(TextView)convertView.findViewById(R.id.tv_location);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.addressTv.setText(address.getAddress());
		holder.detailAddressTv.setText(address.getSpecificLocation());
		return convertView;
	}
	class ViewHolder{
		TextView addressTv;
		TextView detailAddressTv;
	}
}
