package com.lesports.stadium.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.AllOrderActivity;
import com.lesports.stadium.activity.IntegralDetailActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.ManageShippingAddressActivity;
import com.lesports.stadium.activity.MessageNotifyActivity;
import com.lesports.stadium.activity.MyCrowdActivity;
import com.lesports.stadium.activity.MyCouponActivity;
import com.lesports.stadium.activity.MyPrizeActivity;
import com.lesports.stadium.activity.MySettingActivity;
import com.lesports.stadium.activity.MySubscribeActivity;
import com.lesports.stadium.activity.PersionDataActivity;
import com.lesports.stadium.activity.RechargeintegralActivity;
import com.lesports.stadium.activity.RegisterActivity;
import com.lesports.stadium.activity.SelectAddressActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.bean.IntegralRuleBean;
import com.lesports.stadium.bean.MessageNumerBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.utils.TextUtil;
import com.lesports.stadium.view.CircleImageView;
import com.lesports.stadium.view.CustomDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: MineFragment
 * 
 * @Desc : 我 的 界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint("InflateParams")
public class MyFragment extends BaseFragment implements OnClickListener {
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 显示当前总乐豆提示按钮
	 */
	private CustomDialog msgDialog;
	/**
	 * 未读消息总数
	 */
	private TextView unmessage_size;
	/**
	 * 用户昵称
	 */
	private TextView name;
	/**
	 * 我的积分
	 */
	private TextView my_score;
	/**
	 * 圆形头像
	 */
	private CircleImageView iv_self_photo;
	/**
	 * 获取积分成功
	 */
	private final int SUCCESS_DATE = 100;
	/**
	 * 获取未读消息数量失败
	 */
	private final int FILUE_DATE = 110;
	private final int DILTE_SHOW = 101;
	/**
	 * 获取未读消息数量成功
	 */
	private final int SUCCESS_MESSAGE = 200;
	/**
	 * 消息实体类
	 */
	MessageNumerBean messageNumerBean=new MessageNumerBean();
	/**
	 * 图片加载器
	 */
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_DATE:
				analyzeDate((String)msg.obj);
				break;
			case FILUE_DATE:
				getUserJiFen();
				break;
			case SUCCESS_MESSAGE:
				//消息数量获取成功
				analyseMessageDate((String) msg.obj);
				break;
			case DILTE_SHOW:
				setUserMessage();
			default:
				break;
			}
		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		View view = inflater.inflate(R.layout.fragment_my, null);
		view.findViewById(R.id.rl_my_appointment).setOnClickListener(this);
		view.findViewById(R.id.rl_my_award).setOnClickListener(this);
		view.findViewById(R.id.rl_my_crowd).setOnClickListener(this);
		view.findViewById(R.id.rl_my_coupon).setOnClickListener(this);
		view.findViewById(R.id.rl_my_order).setOnClickListener(this);
		view.findViewById(R.id.rl_address_receiver).setOnClickListener(this);
		view.findViewById(R.id.rl_settings).setOnClickListener(this);
		iv_self_photo = (CircleImageView) view.findViewById(R.id.iv_self_photo);
		iv_self_photo.setOnClickListener(this);
		view.findViewById(R.id.t_integral_recharge).setOnClickListener(this);
		view.findViewById(R.id.iv_integral_recharge).setOnClickListener(this);
		unmessage_size = (TextView) view.findViewById(R.id.unmessage_size);
		unmessage_size.setOnClickListener(this);
		view.findViewById(R.id.unrendmessage).setOnClickListener(this);
		view.findViewById(R.id.iv_integral_detail).setOnClickListener(this);
		view.findViewById(R.id.t_integral_detail).setOnClickListener(this);
		name = (TextView) view.findViewById(R.id.name);
		my_score = (TextView) view.findViewById(R.id.my_score);
		return view;
	}

	@Override
	public void onResume() {
		handler.sendEmptyMessageDelayed(DILTE_SHOW, 100);
		super.onResume();
	}

	@Override
	public void initListener() {
		my_score.setOnClickListener(this);
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		createDialog();
	}

	@Override
	public void onClick(View v) {
		if(TextUtils.isEmpty(GlobalParams.USER_ID)&&v.getId()!= R.id.iv_self_photo&&v.getId()!= R.id.rl_settings){
			exitDialog.show();
			return;
		}
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_my_appointment:
			// 跳转到 我的预约界面
			intent = new Intent(getActivity(), MySubscribeActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_my_award:
			// 跳转到 我的奖品界面
			intent = new Intent(getActivity(), MyPrizeActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_my_crowd:
			// 跳转到 我的众筹界面
			intent = new Intent(getActivity(), MyCrowdActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_my_coupon:
			// 跳转到 我的优惠券
			intent = new Intent(getActivity(), MyCouponActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_my_order:
			// 跳转到我的 订单界面
			intent = new Intent(getActivity(), AllOrderActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_address_receiver:
			// 跳转到收货地址界面
			intent = new Intent(getActivity(),
					ManageShippingAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_settings:
			// 跳转到设置界面
			intent = new Intent(getActivity(), MySettingActivity.class);
			startActivity(intent);
			break;
		case R.id.my_score:
			// 显示当前乐豆总额
			if(GlobalParams.USER_INTEGRAL>99999)
				Toast.makeText(getActivity(), ""+GlobalParams.USER_INTEGRAL, 1).show();
			break;
		case R.id.iv_self_photo:
			// 跳转个人资料界面
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				intent = new Intent(getActivity(), LoginActivity.class);
			} else {
				intent = new Intent(getActivity(), PersionDataActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.iv_integral_recharge:
		case R.id.t_integral_recharge:
			// 跳转积分明细界面
			intent = new Intent(getActivity(), IntegralDetailActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_integral_detail:
		case R.id.t_integral_detail:
			// 跳转积分充值界面
			intent = new Intent(getActivity(), RechargeintegralActivity.class);
			startActivity(intent);
			break;
		case R.id.unmessage_size:
		case R.id.unrendmessage:
			// 跳转消息通知界面
			unmessage_size.setVisibility(View.GONE);
			intent = new Intent(getActivity(), MessageNotifyActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	

	/**
	 * 获取用户消息数量
	 */
	private void getUserMessageNumber() {
		GetDataFromInternet.getInStance().interViewNet(ConstantValue.GET_MESSAGE_NUMBER,
				null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(data!=null){
							Object obj = data.getObject();
							if(obj!=null){
								Message msg = new Message();
								msg.what = SUCCESS_MESSAGE;
								msg.obj =obj;
								handler.sendMessage(msg);
								return;
							}
						}
						Message msg = new Message();
						msg.what = FILUE_DATE;
						handler.sendMessage(msg);
					}
				}, false, false);
	}
	

	/**
	 * 获取用户积分
	 */
	private void getUserJiFen() {
		GetDataFromInternet.getInStance().interViewNet(ConstantValue.GET_JIFEN,
				null, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if(data!=null){
							Object obj = data.getObject();
							if(obj!=null){
								Message msg = new Message();
								msg.what = SUCCESS_DATE;
								msg.obj =obj;
								handler.sendMessage(msg);
								return;
							}
							
						}
					}
				}, false, false);
	}
	
	/**
	 * 设置用户信息
	 */
	private void setUserMessage(){
		if(TextUtils.isEmpty(GlobalParams.USER_ID)){
			name.setText("点击登录");
			my_score.setVisibility(View.INVISIBLE);
			unmessage_size.setVisibility(View.GONE);
			iv_self_photo.setImageResource(R.drawable.default_header);
		}else{
			getUserMessageNumber();
		}
	}
	
	/**
	 * 解析 获取未读消息成功是的数据
	 */
	private void analyseMessageDate(String data){
		getUserJiFen();
		messageNumerBean = JsonUtil.parseJsonToBean(data,
				MessageNumerBean.class);
		if( null!= messageNumerBean&& null!= messageNumerBean.getCount()){
			if(!"0".equals(messageNumerBean.getCount())){
				unmessage_size.setVisibility(View.VISIBLE);
				unmessage_size.setText(messageNumerBean.getCount());
			}else{
				unmessage_size.setVisibility(View.GONE);
			}
		}
	}
	/**
	 * 解析用户积分数据
	 * @param obj
	 */
	private void analyzeDate(String obj) {
		if(!TextUtils.isEmpty(GlobalParams.USER_HEADER)){
			if(GlobalParams.USER_HEADER.startsWith("http:")){
				imageLoader.displayImage(GlobalParams.USER_HEADER, iv_self_photo,setDefaultImageOptions(R.drawable.default_header));
			}else{
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+GlobalParams.USER_HEADER + ConstantValue.IMAGE_END, iv_self_photo,setDefaultImageOptions(R.drawable.default_header));
			}
		}else{
			iv_self_photo.setImageResource(R.drawable.default_header);
		}
		name.setText(GlobalParams.USER_NAME);
		my_score.setVisibility(View.VISIBLE);
		try {
			if(TextUtils.isEmpty(obj)){
				GlobalParams.USER_INTEGRAL = 0;
				my_score.setText(GlobalParams.USER_INTEGRAL+"");
				SharedPreferencesUtils.saveData(getActivity(), "ls_user_message", "ingegral", GlobalParams.USER_INTEGRAL);
				return;
			}
			JSONObject jObj = new JSONObject(obj);
			if(jObj.has("integralBalance")){
				GlobalParams.USER_INTEGRAL= jObj.getInt("integralBalance");
				if(GlobalParams.USER_INTEGRAL>99999){
					my_score.setText("100000+");
				}else{
					my_score.setText(GlobalParams.USER_INTEGRAL+"");
				}
				my_score.setVisibility(View.VISIBLE);
				SharedPreferencesUtils.saveData(getActivity(), "ls_user_message", "ingegral", GlobalParams.USER_INTEGRAL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
	
	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(getActivity(),new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindMessage("您还没有登录哦");
	}
	
	/**
	 * 提示当前乐豆总额
	 * 
	 * @param msg
	 */
	private void showDiaLog() {
		msgDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msgDialog.dismiss();
			}
		});
		msgDialog.setRemindTitle("温馨提示");
		msgDialog.setConfirmTxt("确认");
		msgDialog.setRemindMessage("当前乐豆"+GlobalParams.USER_INTEGRAL);
		msgDialog.show();
	}
	
	/**
	 * 设置加载图片时 加载中和加载失败 显示的图片
	 * @param defaultImag
	 * @return
	 */
	public static DisplayImageOptions setDefaultImageOptions(int defaultImag){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(defaultImag) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(defaultImag)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(defaultImag) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheOnDisc(true).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
		.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
		.displayer(new FadeInBitmapDisplayer(500)).build();// 构建完成
		return options;
	}
	
	@Override
	public void onDestroy() {
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		iv_self_photo = null;
		exitDialog = null;
		msgDialog = null;
		messageNumerBean = null;
		super.onDestroy();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
}
