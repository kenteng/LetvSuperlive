package com.lesports.stadium.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.OrderListBeanTices;
import com.lesports.stadium.bean.TicesDetailBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderTicesAdapter extends BaseAdapter {

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 数据源
	 */
	private List<OrderListBeanTices> mList;
	public OrderTicesAdapter(Context context,List<OrderListBeanTices> list){
		this.mContext=context;
		this.mList=list;
	}
	@Override
	public int getCount() {
		if(mList!=null&&mList.size()!=0){
			return mList.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewholder vh=null;
		if(convertView==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.item_order_child_goupiao_xiangqing,null);
			vh.mImage=(ImageView) convertView.findViewById(R.id.order_child_goupiao_image_s);
			vh.mNum=(TextView) convertView.findViewById(R.id.order_child_goupiao_shuliang_s);
			vh.mPositon=(TextView) convertView.findViewById(R.id.goupiao_order_child_goods_shouhuodizhi_s);
			vh.mPrice=(TextView) convertView.findViewById(R.id.order_child_goupiao_jiage_s);
			convertView.setTag(vh);
		}
		else{
			vh=(viewholder) convertView.getTag();
		}
		//调用方法，转换实体对象
		TicesDetailBean bean=GetDataFromDetail(mList.get(position).getPinfo());
		vh.mNum.setText("X"+mList.get(position).getWareNumber());
		float price=0;
		if(!TextUtils.isEmpty(mList.get(position).getPrice())){
			price=Float.parseFloat(mList.get(position).getPrice());
		}else{
			price=0;
		}
		LApplication.loader
		.DisplayImage(bean.getPicture(), vh.mImage, R.drawable.zhongchouliebiao_zhanwei);
		vh.mPrice.setText("￥:"+price+" ("+bean.getPriceTag()+")");
		vh.mPositon.setText(bean.getSeatNumber());
		return convertView;
	}
	/**
	 * 获取详细数据对象
	 * @param pinfo
	 * @return
	 */
	private TicesDetailBean GetDataFromDetail(String pinfo) {
		TicesDetailBean bean=new TicesDetailBean();
		if(!TextUtils.isEmpty(pinfo)){
			try {
				JSONObject obj=new JSONObject(pinfo);
				if(obj.has("productName")){
					bean.setPicture(obj.getString("productName"));
				}
				if(obj.has("priceTag")){
					bean.setPriceTag(obj.getString("priceTag"));
				}
				if(obj.has("productName")){
					bean.setProductName(obj.getString("productName"));
				}
				if(obj.has("seatNumber")){
					bean.setSeatNumber(obj.getString("seatNumber"));
				}
				if(obj.has("picture")){
					bean.setPicture(obj.getString("picture"));
				}
			return bean;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	static class viewholder{
		ImageView mImage;
		TextView mPrice;
		TextView mPositon;
		TextView mNum;
	}

}
