package com.lesports.stadium.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * 
 * @author Administrator
 * 
 */
/**
 * 
 * ***************************************************************
 * 
 * @ClassName: PrizeDetailActivity
 * 
 * @Desc : 奖品详情
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:29:57
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class PrizeDetailActivity extends Activity implements OnClickListener {
	// 收获人手机号
	private TextView tv_phone;
	// 收获人名称
	private TextView receiver_persion;
	// 图片
	private ImageView iv_prize_detail;
	// 选择收获地址按钮
	private ImageView select_location;
	// 奖品名称
	private TextView tv_prize_name;
	// 奖品价格
	private TextView tv_prize_pri;
	// 奖品有效期
	private TextView tv_prize_time;
	// 详情简介
	private TextView tv_detial;
	// 收获人地址
	private TextView tv_address;
	// 邮编
	private TextView tv_emil;
	// 获取奖品名称
	private String prizName;
	// 价格
	private String prce;
	// 有效期
	private String prizValide;
	// 状态 奖品是否领取 0未领取 1 已领取
	private String staty;
	// 领取按钮
	private TextView get_prize;
	// 奖品收货地址
	private String deliveryId;
	// 领取奖品时所要传入的id
	private String id;
	private String addressId;
	// 收货地址控件
	private TextView tv_receiverloaction;
	/**
	 * 加载图片
	 */
	protected ImageLoader imageLoader1;
	/**
	 * 图文分享界面  分享出去的图片路径
	 */
	private String shared_imager_url;
	/**
	 * 奖品id
	 */
	private String prize_id;
	private RelativeLayout rl_ares;
	private RelativeLayout rl_adres;
	private View view_unr;
	/**
	 * 获取数据成功
	 */
	private final int FILER = 110;
	/**
	 * 获取数据失败
	 */
	private final int SCUSS = 111;
	/**
	 * 请求地址请求码
	 */
	private final int ADRESS_CODE = 22;
	/**
	 * 领取奖品成功
	 */
	private final int RECEIVER_SCUSS = 222;
	/**
	 * 根据id获取地址 成功
	 */
	private final int ADRESS_SCUSS = 333;
	/**
	 * 根据用户id获取默认地址 成功
	 */
	private final int GET_DEFAULT_ADRESS_SCUSS = 666;
	/**
	 * 根据id获取地址
	 */
	private String detailedRemark;
	/**
	 * 图文详情
	 */
	private String infoUrl;
	/**
	 * 富文本
	 */
	private String detailInfo;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FILER:
				Toast.makeText(getApplicationContext(), "服务器繁忙请稍后重试", 0).show();
				break;
			case SCUSS:
				// 根据商品id 获取商品详情
				analyze((String) msg.obj);
				getAdress();
				break;
			case RECEIVER_SCUSS:
				// 领取奖品成功
				staty = "1";
				select_location.setVisibility(View.GONE);
				view_unr.setVisibility(View.GONE);
				rl_ares.setVisibility(View.VISIBLE);
				get_prize.setBackgroundResource(R.drawable.green_tx_background);
				get_prize.setText("已领取");
				get_prize.setClickable(false);
				MyPrizeActivity.instalce.modifyState(prize_id, id, addressId);
				break;
			case ADRESS_SCUSS:
				// 根据id获取商品地址成功
				analyAdressData((String) msg.obj);
				break;
			case GET_DEFAULT_ADRESS_SCUSS:
				// 解析默认地址
				analyDefaultAdressData((String) msg.obj);
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prizedetail);
		initDate();
		initView();
		initNetDate();
		initlistener();
	}

	// 获取网络数据
	private void initNetDate() {

		NetService netService = NetService.getInStance();
		netService.setParams("prizeId", prize_id);
		netService.setHttpMethod(HttpSetting.POST_MODE);
		netService.setUrl(ConstantValue.MY_PRIZE_DETAIL);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData data) {
				Message msg = new Message();
				if (data != null) {
					Object obj = data.getObject();
					if (obj != null) {
						msg.what = SCUSS;
						msg.obj = obj;
						handler.sendMessage(msg);
						return;
					}
				}
				msg.what = FILER;
				handler.sendMessage(msg);
			}
		});

	}

	private void getAdress() {
		// 如果该商品已领取 需要根据当前商品id获取 地址
		if ("1".equals(staty)) {
			NetService netServices = NetService.getInStance();
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(GlobalParams.USER_ID);
			if (isNum.matches())
				netServices.headers
						.setUid(Long.parseLong(GlobalParams.USER_ID));
			netServices.setParams("deliveryId", deliveryId);
			netServices.setHttpMethod(HttpSetting.POST_MODE);
			netServices.setUrl(ConstantValue.GET_ADRESS_ID);
			netServices.loader(new NetLoadListener() {
				@Override
				public void onloaderListener(BackData data) {
					Message msg = new Message();
					if (data != null) {
						Object obj = data.getObject();
						Log.i("wxn", deliveryId + "根据商品id获取地址：" + obj
								+ "  用户id：" + GlobalParams.USER_ID + " ... "
								+ data.getNetResultCode());
						if (obj != null) {
							msg.what = ADRESS_SCUSS;
							msg.obj = obj;
							handler.sendMessage(msg);

							return;
						}
					}
					msg.what = FILER;
					handler.sendMessage(msg);
				}
			});
		} else {
			// 如果该商品未领取，先获取默认地址
			requestDefaultAddress();
		}
	}

	// 获取传递过来的数据
	private void initDate() {
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(this));
		Intent intent = getIntent();
		Bundle bundler = intent.getBundleExtra("bundle");
		if (bundler != null) {
			prize_id = bundler.getString("prize_id");
			prizName = bundler.getString("prize_name");
			prce = bundler.getString("prize_price");
			prizValide = bundler.getString("prize_valide");
			staty = bundler.getString("staty");
			deliveryId = bundler.getString("deliveryId");
			id = bundler.getString("id");
		}
	}

	// 设置监听
	private void initlistener() {
		findViewById(R.id.iv_pic_text).setOnClickListener(this);
		findViewById(R.id.tv_pic_text).setOnClickListener(this);
		findViewById(R.id.select_location).setOnClickListener(this);
		findViewById(R.id.back_prize_detail).setOnClickListener(this);
		rl_adres.setOnClickListener(this);
		get_prize.setOnClickListener(this);

	}

	// 初始化view控件
	private void initView() {
		iv_prize_detail = (ImageView) findViewById(R.id.iv_prize_detail);
		select_location = (ImageView) findViewById(R.id.select_location);
		tv_prize_name = (TextView) findViewById(R.id.tv_prize_name);
		tv_receiverloaction = (TextView) findViewById(R.id.tv_receiverloaction);
		tv_prize_pri = (TextView) findViewById(R.id.tv_prize_pri);
		tv_prize_time = (TextView) findViewById(R.id.tv_prize_time);
		tv_detial = (TextView) findViewById(R.id.tv_detial);
		tv_emil = (TextView) findViewById(R.id.tv_emil);
		tv_address = (TextView) findViewById(R.id.tv_address);
		receiver_persion = (TextView) findViewById(R.id.receiver_persion);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		get_prize = (TextView) findViewById(R.id.get_prize);
		rl_ares = (RelativeLayout) findViewById(R.id.rl_ares);
		rl_adres = (RelativeLayout) findViewById(R.id.rl_adres);
		view_unr = findViewById(R.id.view_unr);
		if ("1".equals(staty)) {
			// 已领取 设置成灰色
			select_location.setVisibility(View.GONE);
			view_unr.setVisibility(View.GONE);
			rl_ares.setVisibility(View.VISIBLE);
			get_prize.setBackgroundResource(R.drawable.green_tx_background);
			get_prize.setText("已领取");
			get_prize.setClickable(false);
			rl_adres.setClickable(false);
			tv_receiverloaction.setText("收货地址");
		} else if ("0".equals(staty)) {
			// 未领取
			select_location.setVisibility(View.VISIBLE);
			view_unr.setVisibility(View.VISIBLE);
			rl_ares.setVisibility(View.GONE);
			get_prize.setBackgroundResource(R.drawable.recharge_tx_background);
			get_prize.setText("领取");
			get_prize.setClickable(true);
			rl_adres.setClickable(true);
			tv_receiverloaction.setText("选择收货地址");
		}
		tv_prize_name.setText(prizName);
		tv_prize_time.setText("使用期限：" + prizValide);
		tv_prize_pri.setText("￥" + prce);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_pic_text:
		case R.id.tv_pic_text:
			// 进入图文详情界面
			Intent intent = new Intent(getApplicationContext(),
					CrowdPictureDetailsActivity.class);
			intent.putExtra("url", detailInfo); //传递富文本
			intent.putExtra("share_rul", infoUrl); //连接 
			intent.putExtra("name", prizName);
			intent.putExtra("prizeid", prize_id);
			intent.putExtra("shared_imager_url", shared_imager_url);
			startActivity(intent);
			break;
		case R.id.get_prize:
			if ("1".equals(staty))
				return;
			if(TextUtils.isEmpty(addressId)){
				Toast.makeText(getApplicationContext(), "请选择收货地址", 0).show();
				return;
			}
			receiverPrize();
			// 领取
			break;
		case R.id.rl_adres:
			intent = new Intent(getApplicationContext(),
					SelectAddressActivity.class);
			if(!TextUtils.isEmpty(addressId)){
				intent.putExtra("id", addressId);
			}
			startActivityForResult(intent, ADRESS_CODE);
			break;
		case R.id.back_prize_detail:
			finish();
		default:
			break;
		}

	}

	private void analyze(String obj) {
		try {
			JSONArray jsonArr = new JSONArray(obj);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject temp = (JSONObject) jsonArr.get(i);
				if (temp.has("infoUrl"))
					infoUrl = temp.getString("infoUrl");
				if (temp.has("detailInfo"))
					detailInfo = temp.getString("detailInfo");
				if (temp.has("detailedImage")){
					shared_imager_url = ConstantValue.BASE_IMAGE_URL
							+ temp.getString("detailedImage") + ConstantValue.IMAGE_END;
					imageLoader1
							.displayImage(
									ConstantValue.BASE_IMAGE_URL
											+ temp.getString("detailedImage") + ConstantValue.IMAGE_END,
									iv_prize_detail,
									MyFragment
											.setDefaultImageOptions(R.drawable.canyinshangpin_zhanwei));
				}
				if (temp.has("description"))
					tv_detial.setText(temp.getString("description"));
				if (temp.has("detailedRemark"))
					detailedRemark = temp.getString("description");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ADRESS_CODE:
			if (data != null) {
				ShippingAddressBean selectBean = (ShippingAddressBean) data
						.getSerializableExtra("addressBean");
				if (selectBean != null
						&& !TextUtils.isEmpty(selectBean.getUserAddress())) {
					addressId = selectBean.getId();
					receiver_persion.setText("收货人：" + selectBean.getUserName());
					tv_phone.setText(selectBean.getUserPhone());
					tv_address.setText("收货地址：" + selectBean.getUserCity()+selectBean.getUserAddress());
					tv_emil.setText("邮政编码：" + selectBean.getPostcode());
					rl_ares.setVisibility(View.VISIBLE);
					view_unr.setVisibility(View.GONE);
				}
			}
			break;
		}
		
	}

	// 领取奖品
	private void receiverPrize() {
		HashMap<String, String> map = new HashMap<>();
		map.put("id", id);
		map.put("status", "1");
		map.put("deliveryId", addressId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.RECEIVER_PRIZE, map, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Message msg = new Message();
						if (data != null&&data.getNetResultCode()== 0) {
							Object obj = data.getObject();
							if (obj != null) {
								msg.what = RECEIVER_SCUSS;
								msg.obj = obj;
								handler.sendMessage(msg);
								return;
							}
						}
						msg.what = FILER;
						handler.sendMessage(msg);
					}
				}, false, false);

	}

	// analyAdressData
	private void analyAdressData(String obj) {
		String cityaddress = "";
		if (TextUtils.isEmpty(obj))
			return;
		try {
			JSONObject temp = new JSONObject(obj);
			if (temp.has("name"))
				receiver_persion.setText("收货人：" + temp.getString("name"));
			if (temp.has("cityAddress"))
				cityaddress = temp.getString("cityAddress");
			if (temp.has("address"))
				tv_address.setText("收货地址：" +cityaddress+ temp.getString("address"));
			if (temp.has("mobilePhone"))
				tv_phone.setText(temp.getString("mobilePhone"));
			if (temp.has("zipcode"))
				tv_emil.setText("邮政编码：" + temp.getString("zipcode"));
			rl_ares.setVisibility(View.VISIBLE);
			view_unr.setVisibility(View.GONE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	/**
	 * 获取默认收货地址
	 */
	private void requestDefaultAddress() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", GlobalParams.USER_ID); // userId
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_DEFAULT_ADDRESS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = GET_DEFAULT_ADRESS_SCUSS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	private void analyDefaultAdressData(String backdatassss) {
		if (!TextUtils.isEmpty(backdatassss)) {
			try {
				JSONObject temp = new JSONObject(backdatassss);
				// 使用方法，设置地址
				String cityaddrss = "";
				if (temp.has("name"))
					receiver_persion.setText("收货人：" + temp.getString("name"));
				if (temp.has("cityAddress"))
					cityaddrss = temp.getString("cityAddress");
				if (temp.has("address"))
					tv_address.setText("收货地址：" + cityaddrss+temp.getString("address"));
				if (temp.has("mobilePhone"))
					tv_phone.setText(temp.getString("mobilePhone"));
				if (temp.has("zipcode"))
					tv_emil.setText("邮政编码：" + temp.getString("zipcode"));
				if (temp.has("id"))
					addressId = temp.getString("id");
				rl_ares.setVisibility(View.VISIBLE);
				view_unr.setVisibility(View.GONE);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}
}
