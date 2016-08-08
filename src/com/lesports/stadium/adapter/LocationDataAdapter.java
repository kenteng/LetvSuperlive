package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.bean.SupportCity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocationDataAdapter extends BaseAdapter {

	private List<SupportCity> mListLocationData;
	private Context mContext;

	public LocationDataAdapter(List<SupportCity> list, Context context) {
		this.mContext = context;
		this.mListLocationData = list;
	}

	public void setlist(List<SupportCity> list) {

		this.mListLocationData = list;
	}

	@Override
	public int getCount() {
		return mListLocationData == null ? 0 : mListLocationData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mListLocationData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholder vh = null;
		if (arg1 == null) {
			vh = new viewholder();
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.locationadapter_item, null);
			vh.mName = (TextView) arg1.findViewById(R.id.location_name);
			arg1.setTag(vh);

		} else {
			vh = (viewholder) arg1.getTag();
		}
		vh.mName.setTag(mListLocationData.get(arg0));
		vh.mName.setText(mListLocationData.get(arg0).getShortName());
		vh.mName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)mContext).disMiss((SupportCity) v.getTag());
			}
		});
		return arg1;
	}

	static class viewholder {
		TextView mName;
	}

}
