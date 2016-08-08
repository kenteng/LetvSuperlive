package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.Address;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @ClassName: PoiSearchAdapter
 * 
 * @Desc : 下车地址Poi搜索框Adapter
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-29 下午6:34:59
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class PoiSearchAdapter extends ArrayAdapter<Address> {
	private int resourceId;  
	public PoiSearchAdapter(Context context, int resource,
			int textViewResourceId, List<Address> objects) {
		super(context, resource, textViewResourceId, objects);
		this.resourceId = resource; 
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Address user = getItem(position);  
	      LinearLayout userListItem = new LinearLayout(getContext());  
		        String inflater = Context.LAYOUT_INFLATER_SERVICE;   
		        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);   
		        vi.inflate(resourceId, userListItem, true);  
		        TextView tvUsername = (TextView)userListItem.findViewById(R.id.online_user_list_item_textview);  
		        tvUsername.setText(user.getAddress());  
		      
		        return userListItem;  

	}
}
