/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.GoodsDetailActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.OnlineCateringDetailActivity;
import com.lesports.stadium.activity.RegisterActivity;
import com.lesports.stadium.activity.ShoppingActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.ServiceGoodView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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

public class RoundsAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<RoundGoodsBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 用户是否登录
	 */
	private boolean isDenglu=false;
	private CustomDialog exitDialog;
	private int page_tag=0;
	
	public RoundsAdapter(List<RoundGoodsBean> mList,Context mContext,boolean isdenglu,int i){
		this.page_tag=i;
		this.isDenglu=isdenglu;
		this.mList=mList;
		this.mContext=mContext;
	}
	public void setlist(List<RoundGoodsBean> mList){
		this.mList=mList;
		notifyDataSetChanged();
	}
	public int getCount() {
		return mList==null?0:mList.size();
	}
	/**
	 * 刷新界面
	 */
	public void setBoolean(boolean is){
		this.isDenglu=is;
		notifyDataSetChanged();
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
			convertView.setTag(vh);
		}else{
			vh=(viewholder) convertView.getTag();
		}
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+mList.get(position).getSmallImg(), vh.mGoodsImage,
				R.drawable.zhoubianshangpin_zhanwei);
		vh.mGoodsName.setText(mList.get(position).getGoodsName());
		String prices = mList.get(position).getPrice();
		if(!TextUtils.isEmpty(prices)){
			if(Utils.isInteger(prices)){
				vh.mGoodsPrice.setText("￥"+prices);
			}
			double price=Utils.parseTwoNumber(prices);
			vh.mGoodsPrice.setText("￥"+price);
		}else{
			vh.mGoodsPrice.setText("￥"+0.00);
		}
		final ImageView im=vh.mGoodsAdd;
		im.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				// TODO Auto-generated method stub
				//需要先判断用户是否登录
				MobclickAgent.onEvent(mContext,"AddToCart");
				im.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ia_ball_shake));
				if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
						/**
						 * 调用方法，先查询，数据库中是否已经存在该商品
						 */
						boolean ishanve=checkTheGoodsIsHave(mList.get(position),GlobalParams.USER_ID);
						if(ishanve){
							//说明已经有了
							//调用方法，修改数量
							//调用方法，检测数据库存是否允许继续添加
							boolean ishaves=checkTheGoodsIsHaveStorck(mList.get(position),GlobalParams.USER_ID);
							if(ishaves){
								if(page_tag==0){
									
								}else if(page_tag==1){
									int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
					                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
					                ImageView ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
					                ball.setImageResource(R.drawable.gouwucheluoxiaanniu);// 设置buyImg的图片
					                if(ShoppingActivity.instance!=null){
					                	ShoppingActivity.instance.setAnim(ball, startLocation);// 开始执行动画
					                }else{
					                }
								}
								useWayChangeNum(mList.get(position));
								if(ShoppingActivity.instance!=null){
									ShoppingActivity.instance.setBuyNum();
								}
								int i=Integer.parseInt(mList.get(position).getStock().trim());
								i--;
								mList.get(position).setStock(i+"");
							}else{
								createDialogs();
							}
							
						}else{
							//说明还未存储，调用方法存入购物车数据库
							/**
							 * 调用方法将数据存入到数据库购物车表单中,
							 */
							String storc=mList.get(position).getStock();
							if(!TextUtils.isEmpty(storc)){
								int storst=Integer.parseInt(storc);
								if(storst>=1){
									if(page_tag==0){
										
									}else if(page_tag==1){
										
										int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
						                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
						                ImageView ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
						                ball.setImageResource(R.drawable.gouwucheluoxiaanniu);// 设置buyImg的图片
						                if(ShoppingActivity.instance!=null){
						                	ShoppingActivity.instance.setAnim(ball, startLocation);// 开始执行动画
						                }else{
						                }
									}
									RoundGoodsBean bean=mList.get(position);
									bean.setGoodsusenaem(GlobalParams.USER_ID);
									Object obj=insertDataToDB(bean);
									if(ShoppingActivity.instance!=null){
										ShoppingActivity.instance.setBuyNum();
										//同时，需要将这个商品的库存量减去1
									}
									if(!TextUtils.isEmpty(mList.get(position).getStock())){
										int i=Integer.parseInt(mList.get(position).getStock().trim());
										i--;
										mList.get(position).setStock(i+"");
									}
								}else{
									//库存不足
									createDialogs();
								}
							}else{
								//库存不足
								createDialogs();
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
		 * 商品价格d
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
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ? AND buy_mediumImg = ?", new String[]{uSER_ID,roundGoodsBean.getId(),roundGoodsBean.getSpaceid(),roundGoodsBean.getSeller()}, null);
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				RoundGoodsBean bean=list.get(i);
				if(bean.getId().equals(roundGoodsBean.getId())&&bean.getSpaceid().equals(roundGoodsBean.getSpaceid())){
					is=true;
					break;
				}else{
					is= false;
				}
			}
		}
		return is;
	}
	/**
	 * 判断该商品库存是否满足客户需求
	 * @param roundGoodsBean
	 * @param uSER_ID 
	 */
	private boolean checkTheGoodsIsHaveStorck(RoundGoodsBean roundGoodsBean, String uSER_ID) {
		boolean is=false;
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ? AND buy_mediumImg = ?", new String[]{uSER_ID,roundGoodsBean.getId(),roundGoodsBean.getSpaceid(),roundGoodsBean.getSeller()}, null);
		if(list!=null&&list.size()!=0){
			if(!TextUtils.isEmpty(roundGoodsBean.getStock())){
				double nums=Double.parseDouble(roundGoodsBean.getStock());
				double num_list=0;
				double num_sql=0;
				if(!TextUtils.isEmpty(list.get(0).getmNum())){
					num_sql=Double.parseDouble(list.get(0).getmNum());
					if(!TextUtils.isEmpty(roundGoodsBean.getmNum())){
						num_list=Double.parseDouble(roundGoodsBean.getmNum());
					}
					if(nums>(num_list+num_sql)){
						is=true;
					}else{
						is=false;
					}
				}
			}
		}
		return is;
		
	}
	
	private void useWayChangeNum(RoundGoodsBean roundGoodsBean) {
		// TODO Auto-generated method stub
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ? ", new String[]{GlobalParams.USER_ID,roundGoodsBean.getId(),roundGoodsBean.getSpaceid()}, null);
		RoundGoodsBean roundGoodsBeans=list.get(0);
		int count=Integer.parseInt(roundGoodsBeans.getmNum());
		count++;
		ContentValues values = new ContentValues();
		values.put("buy_mNum",count+"");
		int i=LApplication.dbBuyCar.update(values,
			"buy_mUsename = ? AND buy_id= ? AND buy_mSpaceid= ?", new String[] {
					GlobalParams.USER_ID, roundGoodsBean.getId(),roundGoodsBean.getSpaceid()});
	}
	

}
