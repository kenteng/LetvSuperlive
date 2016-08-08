/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.OnlineCateringDetailActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ServiceCateringDetailBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
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

/**
 * ***************************************************************
 * 
 * @Desc : 餐饮界面弹出窗口的listview的适配器
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

public class OnlineCateringWindowAdapter extends BaseAdapter {

	public List<ServiceCateringDetailBean> mList;
	private Context mContext;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public OnlineCateringWindowAdapter(List<ServiceCateringDetailBean> msList,Context mCosntext){
		this.mContext=mCosntext;
		this.mList=msList;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("InflateParams") @SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.catering_settlraccount_listview_item,null);
			vh.mAdd=(ImageView) convertView.findViewById(R.id.catering_settleaccount_lv_item_add);
			vh.mImage=(ImageView) convertView.findViewById(R.id.catering_settleaccount_lv_item_image);
			vh.mJian=(ImageView) convertView.findViewById(R.id.catering_settleaccount_lv_item_jianqu);
			vh.mName=(TextView) convertView.findViewById(R.id.catering_settleaccount_lv_item_title);
			vh.mNum=(TextView) convertView.findViewById(R.id.catering_settleaccount_lv_item_num);
			vh.mPrice=(TextView) convertView.findViewById(R.id.catering_settleaccount_lv_item_price);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.mName.setText(mList.get(position).getGoodsName());
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mList.get(position).getBigImg() + ConstantValue.IMAGE_END, vh.mImage,R.drawable.zhoubianshangpin_zhanwei);
		vh.mNum.setText(mList.get(position).getGoodsNum()+"");
		if(!TextUtils.isEmpty(mList.get(position).getPrice())){
			vh.mPrice.setText(Utils.parseTwoNumber(mList.get(position).getPrice())+"");
		}else{
			vh.mPrice.setText("0.00");
		}
		vh.mAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String usename=GlobalParams.USER_ID;
				if(!TextUtils.isEmpty(usename)){
					//告诉数据库，数据库内的商品也要发生改变
					boolean ishave=checkTheFoodsIsHave(mList.get(position));
					if(ishave){
						//
						//检查商品库存是否满足
						boolean ishavesxuqiu=checkTheFoodsIsHave_storck(mList.get(position));
						if(ishavesxuqiu){
							int i=mList.get(position).getGoodsNum();
							i++;
							mList.get(position).setGoodsNum(i);
							notifyDataSetChanged();
							((OnlineCateringDetailActivity) mContext).setBuyNumWindow(mList);
							makeDataNumOfDB(mList.get(position),usename);
						}else{
							createDialogs();
						}
						
						
					}
				}else{
					createDialog();
				}
			
			}
		});
		vh.mJian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String usename=GlobalParams.USER_ID;
				if(!TextUtils.isEmpty(usename)){
					int i=mList.get(position).getGoodsNum();
					if(i==1){
						//说明已经是最后一个，减去以后就成为0，需要将数据从集合中删除
						if(mList!=null&&mList.size()!=0){
							boolean ishave=checkTheFoodsIsHave(mList.get(position));
							if(ishave){
								//调用方法，直接从数据库中删除该商品
								deleteFoodOfDB(mList.get(position));
								i--;
								mList.get(position).setGoodsNum(i);
								mList.remove(position);
								notifyDataSetChanged();
								((OnlineCateringDetailActivity) mContext).setBuyNumWindow(mList);
							}
						}
					}else{
						if(mList!=null&&mList.size()!=0){
							boolean ishave=checkTheFoodsIsHave(mList.get(position));
							if(ishave){
								//进行数据-1操作
								makeDataNumOfDBjian(mList.get(position),usename);
								i--;
								mList.get(position).setGoodsNum(i);
								notifyDataSetChanged();
								((OnlineCateringDetailActivity) mContext).setBuyNumWindow(mList);
							}
						}
					}
				}else{
					createDialog();
				}
				
			}
		});
		return convertView;
	}
	

	/**
	 * 提示用户进行登录
	 */
	private void createDialogs(){
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("确定");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("商品库存不足，请选择其他商品加入");
		exitDialog.show();
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
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能添加购物车哦  ~~");
		exitDialog.show();
	}
	static class ViewHolder{
		/**
		 * 商品图片
		 */
		ImageView mImage;
		/**
		 * 商品名称
		 */
		TextView mName;
		/**
		 * 商品价格
		 */
		TextView mPrice;
		/**
		 * 商品数量
		 */
		TextView mNum;
		/**
		 * 加号
		 */
		ImageView mAdd;
		/**
		 * 建号
		 */
		ImageView mJian;
	}
	/**
	 * 根据传入商品，从数据库中删除该商品
	 * @param serviceCateringDetailBean
	 */
	private void deleteFoodOfDB(
			ServiceCateringDetailBean serviceCateringDetailBean) {
		String id=serviceCateringDetailBean.getgId();
		LApplication.foodbuycar.delete("foodbuy_gid =?", new String[]{id});
		
	}
	/**
	 * 在数据库中检测该商品是否存在
	 * @param serviceCateringDetailBean
	 * @return
	 */
	private boolean checkTheFoodsIsHave(
			ServiceCateringDetailBean serviceCateringDetailBean) {
		boolean ishave=false;
		List<ServiceCateringDetailBean> list=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{GlobalParams.USER_ID,serviceCateringDetailBean.getSeller(),serviceCateringDetailBean.getgId()}, null);
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(serviceCateringDetailBean.getgId().equals(list.get(i).getgId())){
					ishave=true;
					break;
				}else{
					ishave=false;
				}
			}
		}
		return ishave;
	}
	/**
	 * 在数据库中检测该商品是否存在
	 * @param serviceCateringDetailBean
	 * @return
	 */
	private boolean checkTheFoodsIsHave_storck(
			ServiceCateringDetailBean serviceCateringDetailBean) {
		boolean ishave=false;
		List<ServiceCateringDetailBean> list=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{GlobalParams.USER_ID,serviceCateringDetailBean.getSeller(),serviceCateringDetailBean.getgId()}, null);
		if(list!=null&&list.size()!=0){
			double kucun=0;
			double shangpinshu=0;
			double shujukushu=0;
			if(!TextUtils.isEmpty(serviceCateringDetailBean.getGoodsNum()+"")){
				shangpinshu=serviceCateringDetailBean.getGoodsNum();
				if(!TextUtils.isEmpty(list.get(0).getGoodsNum()+"")){
					shujukushu=list.get(0).getGoodsNum();
					if(!TextUtils.isEmpty(serviceCateringDetailBean.getStock())){
						kucun=Double.parseDouble(serviceCateringDetailBean.getStock());
						if(kucun>(shangpinshu+shujukushu)){
							ishave=true;
						}else{
							ishave=false;
						}
					}
				}
			}
		}
		return ishave;
	}
	/**
	 * 对数据库中指定商品的数量进行加1操作
	 * @param serviceCateringDetailBean
	 * @param userid 
	 */
	private void makeDataNumOfDB(
			ServiceCateringDetailBean serviceCateringDetailBean, String userid) {
		String id=serviceCateringDetailBean.getgId();
		List<ServiceCateringDetailBean> list_new=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{userid,serviceCateringDetailBean.getSeller(),id}, null);
		if(list_new!=null&&list_new.size()!=0){
			ServiceCateringDetailBean bean=list_new.get(0);
			//调用方法，从数据库中删除
			LApplication.foodbuycar.delete("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{userid,serviceCateringDetailBean.getSeller(),id});
			//修改bean的num
			int goodsnum=bean.getGoodsNum();
			goodsnum++;
			bean.setGoodsNum(goodsnum);
			bean.setUserNametag(GlobalParams.USER_ID);
			LApplication.foodbuycar.insert(bean);
		}else{
			serviceCateringDetailBean.setUserNametag(GlobalParams.USER_ID);
			LApplication.foodbuycar.insert(serviceCateringDetailBean);
		}
		
	}
	/**
	 * 对数据库中指定商品的数量进行加1操作
	 * @param serviceCateringDetailBean
	 * @param uSER_ID 
	 */
	private void makeDataNumOfDBjian(
			ServiceCateringDetailBean serviceCateringDetailBean, String uSER_ID) {
		String id=serviceCateringDetailBean.getgId();
		List<ServiceCateringDetailBean> list_new=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{uSER_ID,serviceCateringDetailBean.getSeller(),id}, null);
		if(list_new!=null&&list_new.size()!=0){
			ServiceCateringDetailBean bean=list_new.get(0);
			//调用方法，从数据库中删除
			LApplication.foodbuycar.delete("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{uSER_ID,serviceCateringDetailBean.getSeller(),id});
			//修改bean的num
			int goodsnum=bean.getGoodsNum();
			goodsnum--;
			bean.setGoodsNum(goodsnum);
			if(goodsnum==0){
				
			}else{
				bean.setUserNametag(uSER_ID);
				LApplication.foodbuycar.insert(bean);
			}
		}else{
			serviceCateringDetailBean.setUserNametag(uSER_ID);
			LApplication.foodbuycar.insert(serviceCateringDetailBean);
		}
		
		
	}

}
