package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CrowdfundingDetailZhuyiAdapter extends BaseAdapter {

	/**
	 * 注意事项数据集
	 */
	private List<String> mlist;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	public CrowdfundingDetailZhuyiAdapter(List<String> mlists,Context mContexts){
		this.mlist=mlists;
		this.mContext=mContexts;
	}
	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholderzy vh=null;
		if(arg1==null){
			vh=new viewholderzy();
			@SuppressWarnings("static-access")
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.item_crowdfunding_detail_zhuyishixian,null);
			vh.mTitle=(TextView) arg1.findViewById(R.id.cf_d_zy_lv_i);
			arg1.setTag(vh);
		}else{
			vh=(viewholderzy) arg1.getTag();
		}
		vh.mTitle.setText(mlist.get(arg0));
		return arg1;
	}
	
	static class viewholderzy{
		/**
		 *条目
		 */
		TextView mTitle;
	}

}
