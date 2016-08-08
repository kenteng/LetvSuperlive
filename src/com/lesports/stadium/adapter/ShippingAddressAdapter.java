package com.lesports.stadium.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.utils.Utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * ***************************************************************
 * @ClassName:  ShippingAddressAdapter 
 * 
 * @Desc : 收货地址适配器
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-16 下午3:31:17
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class ShippingAddressAdapter extends BaseAdapter{
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 存储收货地址
	 */
	private List<ShippingAddressBean> addressList=new ArrayList<ShippingAddressBean>();
	/**
	 * 讨论用户昵称颜色
	 */
	private ForegroundColorSpan blueSpan;
	public ShippingAddressAdapter(Context mContext,
			List<ShippingAddressBean> addressList) {
		super();
		blueSpan=new ForegroundColorSpan(0xff43b7e6);
		this.mContext = mContext;
		this.addressList = addressList;
	}

	public List<ShippingAddressBean> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<ShippingAddressBean> addressList) {
		this.addressList = addressList;
	}

	@Override
	public int getCount() {
		return Utils.isNullOrEmpty(addressList)?0:addressList.size();
	}

	@Override
	public Object getItem(int position) {
		return addressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddressHolder holder=null;
		if(convertView==null){
			holder=new AddressHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_shippingaddress, null);
			holder.nameTv=(TextView)convertView.findViewById(R.id.seleceaddress_name_tv);
			holder.phoneTv=(TextView)convertView.findViewById(R.id.seleceaddress_phone_tv);
			holder.addressTv=(TextView)convertView.findViewById(R.id.seleceaddress_address_tv);
			holder.selectedTb=(ToggleButton)convertView.findViewById(R.id.discuss_item_userselected_tb);
			holder.rightLl=(LinearLayout)convertView.findViewById(R.id.address_rightic_ll);
			convertView.setTag(holder);
		}else{
			holder=(AddressHolder) convertView.getTag();
		}
		
		holder.nameTv.setText(addressList.get(position).getUserName());
		holder.phoneTv.setText(addressList.get(position).getUserPhone());
		if(position==0&&addressList.get(position).getIsDefault().equals("1")){
			SpannableStringBuilder builder = new SpannableStringBuilder("［默认］"+addressList.get(position).getUserCity()+addressList.get(position).getUserAddress());
			builder.setSpan(blueSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.addressTv.setText(builder);
		}else{
			holder.addressTv.setText(addressList.get(position).getUserCity()+addressList.get(position).getUserAddress());
		}
		if(position==0){
			holder.selectedTb.setChecked(true);
		}
		holder.rightLl.setVisibility(View.GONE);
		return convertView;
	}
	class AddressHolder{
		TextView nameTv,phoneTv,addressTv;
		ToggleButton selectedTb;
		LinearLayout rightLl;
	}
}
