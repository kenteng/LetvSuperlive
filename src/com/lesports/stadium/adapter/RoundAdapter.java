/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ***************************************************************
 * 
 * @Desc : 演唱会周边数据源适配器
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

public class RoundAdapter extends BaseAdapter {

	
	/**
	 * 数据源
	 */
	private List<RoundGoodsBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	private CustomDialog exitDialog;
	
	public RoundAdapter(List<RoundGoodsBean> mList,Context mContext){
		this.mList=mList;
		this.mContext=mContext;
	}
	public void setlist(List<RoundGoodsBean> mList){
		this.mList=mList;
	}
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList==null?0:mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewholder vh=null;
		if(convertView==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.round_gridview_item,null);
			vh.mGoodsAdd=(ImageView) convertView.findViewById(R.id.round_gridview_item_adds);
			vh.mGoodsImage=(ImageView) convertView.findViewById(R.id.round_gridview_item_iamges);
			vh.mGoodsName=(TextView) convertView.findViewById(R.id.round_gridview_item_title);
			vh.mGoodsPrice=(TextView) convertView.findViewById(R.id.round_gridview_item_price);
//			vh.mLable=(TextView) convertView.findViewById(R.id.round_gridview_item_lable);
			convertView.setTag(vh);
		}else{
			vh=(viewholder) convertView.getTag();
		}
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mList.get(position).getSmallImg() + ConstantValue.IMAGE_END, vh.mGoodsImage,R.drawable.app_logo1);
		vh.mGoodsName.setText(mList.get(position).getGoodsName());
		vh.mGoodsPrice.setText("￥"+mList.get(position).getPrice());
		if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
			//先检查，该商品是否已经存在在购物车内
			boolean ishavess=checkTheGoodsIsHave(mList.get(position),GlobalParams.USER_ID);
			if(ishavess){
				//说明之前就已经添加进去了，所以这里需要将图标直接切换成已经添加的状态，并且修改状态
				vh.mGoodsAdd.setBackground(mContext.getResources().getDrawable(R.drawable.icon_shopping_yes));
				mList.get(position).setIsAdd("1");
				notifyDataSetChanged();
			}else{
				vh.mGoodsAdd.setBackground(mContext.getResources().getDrawable(R.drawable.icon_shopping_no));
			}
		}else{
			vh.mGoodsAdd.setBackground(mContext.getResources().getDrawable(R.drawable.icon_shopping_no));
		}
		final ImageView im=vh.mGoodsAdd;
		im.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
					//首先，需要判断，该商品是否已经加入购物车，如果已加入，则提示用户已在购物车内，如果未加入，则变换图标，调用
					//方法，将该商品加入购物车当中
					String isAdd=mList.get(position).getIsAdd();
					if("1".equals(isAdd)){
						Toast.makeText(mContext, "亲，该商品已经在购物车了", Toast.LENGTH_SHORT).show();
					}else{
						im.setBackground(mContext.getResources().getDrawable(R.drawable.icon_shopping_yes));
						//调用方法，加入购物车
						//同时将该数据的isadd置为false
						mList.get(position).setIsAdd("1");
						notifyDataSetChanged();
						/**
						 * 调用方法，先查询，数据库中是否已经存在该商品
						 */
						boolean ishanve=checkTheGoodsIsHave(mList.get(position),GlobalParams.USER_ID);
						if(ishanve){
							//说明已经有了
						}else{
							//说明还未存储，调用方法存入购物车数据库
							/**
							 * 调用方法将数据存入到数据库购物车表单中,
							 */
							Object obj=insertDataToDB(mList.get(position));
						}
						
					}
				}else{
					createDialog();
				}
				
			}
			
		});
		return convertView;
	}
	
	private void createDialog(){
		exitDialog = new CustomDialog(mContext,new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(mContext,
						LoginActivity.class);
				mContext.startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("去登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能添加购物车哦  ~~");
		exitDialog.show();
	}
	static class viewholder{
		/**
		 * 商品图片
		 */
		ImageView mGoodsImage;
		/**
		 * 商品名称
		 */
		TextView mGoodsName;
		/**
		 * 商品价格
		 */
		TextView mGoodsPrice;
		/**
		 * 点击加入购物车
		 */
		ImageView mGoodsAdd;
		/**
		 * 商品说明
		 */
		TextView mLable;
	}
	/**
	 * 该方法用来将用户选定的这个商品添加到本地的购物车数据库中
	 * @param roundGoodsBean
	 */
	private Object insertDataToDB(RoundGoodsBean roundGoodsBean) {
		roundGoodsBean.setGoodsusenaem(GlobalParams.USER_ID);
		Object obj=LApplication.dbBuyCar.insert(roundGoodsBean);
		return obj;
	}
	/**
	 * 查询数据库中是否已经存在该条目
	 * @param roundGoodsBean
	 * @param uSER_ID 
	 */
	private boolean checkTheGoodsIsHave(RoundGoodsBean roundGoodsBean, String uSER_ID) {
		boolean is=false;
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ?", new String[]{uSER_ID}, null);
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				RoundGoodsBean bean=list.get(i);
				if(bean.getId().equals(roundGoodsBean.getId())){
					is=true;
					break;
				}else{
					is= false;
				}
			}
		}
		return is;
		
	}
	

}
