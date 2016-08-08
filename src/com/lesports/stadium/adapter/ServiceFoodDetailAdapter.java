/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.List;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.OnlineCateringDetailActivity;
import com.lesports.stadium.activity.OrderActivity;
import com.lesports.stadium.activity.RegisterActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ServiceCateringDetailBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;

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
 * @Desc : 服务部分的餐饮详情界面的适配器
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

public class ServiceFoodDetailAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	public List<ServiceCateringDetailBean> mList;
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 是否处于营业时间内
	 */
	public boolean isHave=false;
	public ServiceFoodDetailAdapter(boolean is,List<ServiceCateringDetailBean> List,Context context){
		this.mList=List;
		this.mContext=context;
		this.isHave=is;
	}
	/**
	 * 动态给适配器重新是配数据
	 * @2016-2-16上午9:57:42
	 */
	public void setList(List<ServiceCateringDetailBean> list){
		this.mList=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList.size();
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		viewholder vh=null;
		if(arg1==null){
			vh=new viewholder();
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1=inflater.inflate(R.layout.catering_detail_listview_item,null);
			vh.mImage=(ImageView) arg1.findViewById(R.id.catering_detail_lv_item_image);
			vh.mTitle=(TextView) arg1.findViewById(R.id.catering_detail_lv_item_title);
			vh.mPrice=(TextView) arg1.findViewById(R.id.catering_detail_lv_item_price);
			vh.mNum=(TextView) arg1.findViewById(R.id.catering_detail_lv_item_num);
			vh.mAdd=(ImageView) arg1.findViewById(R.id.catering_detail_lv_item_add);
			vh.mJian=(ImageView) arg1.findViewById(R.id.catering_detail_lv_item_jianqu);
			arg1.setTag(vh);
		}else{
			vh=(viewholder) arg1.getTag();
		}
		//先判断已选择数量是否为0
		if(mList.get(arg0).getGoodsNum()==0){
			//说明是未添加
			vh.mNum.setVisibility(View.GONE);
			vh.mJian.setVisibility(View.GONE);
		}else{
			vh.mNum.setVisibility(View.VISIBLE);
			vh.mJian.setVisibility(View.VISIBLE);
			vh.mNum.setText(mList.get(arg0).getGoodsNum()+"");
		}
		Log.i("商品名称",mList.get(arg0).getGoodsName());
		vh.mTitle.setText(mList.get(arg0).getGoodsName());
		if(!TextUtils.isEmpty(mList.get(arg0).getPrice())){
			vh.mPrice.setText(Utils.parseTwoNumber(mList.get(arg0).getPrice())+"");
		}else{
			vh.mPrice.setText("0.00");
		}
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+mList.get(arg0).getBigImg() + ConstantValue.IMAGE_END, vh.mImage,R.drawable.canyinshangpin_zhanwei);
		final TextView mnum=vh.mNum;
		final ImageView mjian=vh.mJian;
		vh.mAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否在营业时间内
				if(isHave){
					//先判断用户是否登录，如果没有登录就提示
					MobclickAgent.onEvent(mContext,"FoodList");
					String userid=GlobalParams.USER_ID;
					if(!TextUtils.isEmpty(userid)){
		                //调用方法，检测该商品在数据库中是否存在
		                boolean ishave=checkTheFoodsIsHave(mList.get(arg0),userid);
		                if(ishave){
		                	//说明是存在的
		                	//调用方法，对数据库中的该商品的数量进行操作
		                	boolean is=checkTheFoodsIsHave_storck(mList.get(arg0));
		                	if(is){
		                		mnum.setVisibility(View.VISIBLE);
		    					mjian.setVisibility(View.VISIBLE);
		    					int i=mList.get(arg0).getGoodsNum();
		    					i++;
		    					mList.get(arg0).setGoodsNum(i);
		    					notifyDataSetChanged();
		                		int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
		    	                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
		    	                ImageView ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
		    	                ball.setImageResource(R.drawable.gouwucheluoxiaanniu);// 设置buyImg的图片
		    	                ((OnlineCateringDetailActivity)mContext).setAnim(ball, startLocation);// 开始执行动画
		                		makeDataNumOfDB(mList.get(arg0),userid);
			                	((OnlineCateringDetailActivity) mContext).setBuyNum(mList);
		                	}else{
		                		createDialogs();
		                	}
		                }else{
		                	boolean is=checkTheFoodsIsHave_storck(mList.get(arg0));
		                	if(is){
		                		mnum.setVisibility(View.VISIBLE);
								mjian.setVisibility(View.VISIBLE);
								int i=mList.get(arg0).getGoodsNum();
								i++;
								mList.get(arg0).setGoodsNum(i);
								notifyDataSetChanged();
			                	int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
				                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
				                ImageView ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
				                ball.setImageResource(R.drawable.gouwucheluoxiaanniu);// 设置buyImg的图片
				                ((OnlineCateringDetailActivity)mContext).setAnim(ball, startLocation);// 开始执行动画
			                	mList.get(arg0).setUserNametag(userid);
			                	LApplication.foodbuycar.insert(mList.get(arg0));
			                	((OnlineCateringDetailActivity) mContext).setBuyNum(mList);
		                	}else{
		                		createDialogs();
		                	}
		                	
		                }
					}else{
						createDialog();
					}
				}else{
					createDialog("抱歉","未曾营业");
				}

			}
		});
		mjian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
					int i=mList.get(arg0).getGoodsNum();
					if(i==1){
						//说明已经是最后一个，减去以后就成为0，需要将数据控件和减去符号隐藏
						mnum.setVisibility(View.GONE);
						mjian.setVisibility(View.GONE);
					}
					i--;
					mList.get(arg0).setGoodsNum(i);
					notifyDataSetChanged();
//					int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
//	                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
//	                ImageView ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
//	                ball.setImageResource(R.drawable.ic_launcher);// 设置buyImg的图片
//	                ((OnlineCateringDetailActivity)mContext).setAnim(ball, startLocation);// 开始执行动画
	                //判断数据库中是否存在
	                boolean ishave=checkTheFoodsIsHave(mList.get(arg0),GlobalParams.USER_ID);
	                if(ishave){
	                	//调用方法，进行减操作
	                	makeDataNumOfDBjian(mList.get(arg0),GlobalParams.USER_ID);
	                	((OnlineCateringDetailActivity) mContext).setBuyNum(mList);
	                }else{
	                	Log.i("没有数据","数据库减去的时候没有数据");
	                }
				}else{
					createDialog();
				}
				
			}
		});
		return arg1;
	}
	private void createDialog(String string, String string2) {
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setConfirmTxt("确认");
		exitDialog.setRemindTitle("温馨提示");
//		exitDialog.setRemindMessage("抱歉，店铺还未营业"+string+"-"+string2);
		exitDialog.setRemindMessage("抱歉，店铺还未营业");
		exitDialog.show();
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
	
	static class viewholder{
		ImageView mImage;
		TextView mTitle;
		TextView mPrice;
		TextView mNum;
		ImageView mAdd;
		ImageView mJian;
		
	}
	/**
	 * 在数据库中检测该商品是否存在
	 * @param serviceCateringDetailBean
	 * @return
	 */
	private boolean checkTheFoodsIsHave_storck(
			ServiceCateringDetailBean serviceCateringDetailBean) {
		// TODO Auto-generated method stub
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
		}else{
			if(!TextUtils.isEmpty(serviceCateringDetailBean.getStock())){
				double stork=Double.parseDouble(serviceCateringDetailBean.getStock());
				if(stork>=1){
					ishave=true;
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
	 * @param userid 
	 * @return
	 */
	private boolean checkTheFoodsIsHave(
			ServiceCateringDetailBean serviceCateringDetailBean, String userid) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		List<ServiceCateringDetailBean> list=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?", new String[]{userid,serviceCateringDetailBean.getSeller()}, null);
//		List<ServiceCateringDetailBean> list=LApplication.foodbuycar.findAll();
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
	 * 对数据库中指定商品的数量进行加1操作
	 * @param serviceCateringDetailBean
	 * @param userid 
	 */
	private void makeDataNumOfDB(
			ServiceCateringDetailBean serviceCateringDetailBean, String userid) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		String id=serviceCateringDetailBean.getgId();
		List<ServiceCateringDetailBean> list_new=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{uSER_ID,serviceCateringDetailBean.getSeller(),id}, null);
		if(list_new!=null&&list_new.size()!=0){
			ServiceCateringDetailBean bean=list_new.get(0);
			//调用方法，从数据库中删除
			LApplication.foodbuycar.delete("foodbuy_usernametag =? AND foodbuy_seller=? AND foodbuy_gid =?", new String[]{uSER_ID,serviceCateringDetailBean.getSeller(),id});
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



}
