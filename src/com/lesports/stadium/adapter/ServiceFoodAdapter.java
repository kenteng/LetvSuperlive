/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ServiceFoodBean;
import com.lesports.stadium.utils.ConstantValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 服务部分的餐饮部分的适配器
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

public class ServiceFoodAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<ServiceFoodBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	public ServiceFoodAdapter(List<ServiceFoodBean> List,Context context){
		this.mList=List;
		this.mContext=context;
	}
	public void setList(List<ServiceFoodBean> list){
		this.mList=list;
		notifyDataSetChanged();
	}
	public void addList(List<ServiceFoodBean> list){
		this.mList.addAll(list);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList ==null?0:mList.size();
	}
	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		viewholder vh=null;
		if(arg1==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.catering_fragment_listview_item,null);
			vh.mImage=(ImageView) arg1.findViewById(R.id.catering_fragment_listview_item_image);
			vh.mTitle=(TextView) arg1.findViewById(R.id.catering_fragment_listview_item_title);
			vh.mAddrrss=(TextView) arg1.findViewById(R.id.catering_fragment_listview_item_location);
			vh.mTime=(TextView) arg1.findViewById(R.id.catering_fragment_listview_item_time);
			arg1.setTag(vh);
		}else{
			vh=(viewholder) arg1.getTag();
		}
		String time=mList.get(arg0).getStartTime()+"-"+mList.get(arg0).getEndTime();
		vh.mTime.setText(time);
		vh.mTitle.setText(mList.get(arg0).getCompanyname());
		vh.mAddrrss.setText(mList.get(arg0).getAddress());
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mList.get(arg0).getImageUrl() + ConstantValue.IMAGE_END, vh.mImage,R.drawable.canyinliebiao_zhanwei);
		return arg1;
	}
	static class viewholder{
		ImageView mImage;
		TextView mTitle;
		TextView mAddrrss;
		TextView mTime;
	}

}
