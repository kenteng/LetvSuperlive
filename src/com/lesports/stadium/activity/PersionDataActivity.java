package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.UpLoadFile;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.CitySelector;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.view.CircleImageView;
import com.lesports.stadium.view.location.XmlParserJSON;
import com.lesports.stadium.view.location.model.CityModel;
import com.lesports.stadium.view.location.model.DistrictModel;
import com.lesports.stadium.view.location.model.ProvinceModel;
import com.lesports.stadium.view.location.widget.OnWheelChangedListener;
import com.lesports.stadium.view.location.widget.WheelView;
import com.lesports.stadium.view.location.widget.adapter.ArrayWheelAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * 个人资料界面
 * 
 * @author Administrator
 * 
 */
public class PersionDataActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	/**
	 * 调用系统相册获取图片
	 */
	private static final int GET_PHOTO = 2;
	/**
	 * 圆形头像控件
	 */
	private CircleImageView iv_self_photo;
	/**
	 * 昵称
	 */
	private EditText tv_nick_name;
	/**
	 * 手机号
	 */
	private TextView phone_tv;
	/**
	 * 邮箱
	 */
	private EditText emil_tv;
	/**
	 * 上传头像成功
	 */
	private final int SUCCESS = 10;
	/**
	 * 访问网络失败
	 */
	private final int FILE = 11;
	/**
	 * 成功获取token
	 */
	private final int SUCCESS_TOKEN = 666;
	/**
	 * 获取token失败
	 */
	private final int FILUE_TOKEN = 444;
	private ImageView dow_img;

	/**
	 * 左右移动三个点
	 */
	private ImageView iv_1, iv_2, iv_3;
	/**
	 * 加载动画
	 */
	private RelativeLayout loding_bg, animation_p;
	/**
	 * 城市选择器
	 */
	public CitySelector timeSelector;
	/**
	 * 位移动画
	 */
	private TranslateAnimation anim;
	/**
	 * 位移动画
	 */
	private TranslateAnimation anim2;
	/**
	 * 位移动画
	 */
	private TranslateAnimation anim3;
	/**
	 * 位移动画 时间
	 */
	private int time = 500;
	/**
	 * 移动动画标志 1
	 */
	private static final int ON_LEFT = 1;
	/**
	 * 移动动画标志 2
	 */
	private static final int ON_CENTER = 2;
	/**
	 * 移动动画标志 3
	 */
	private static final int ON_RIGHT = 3;
	/**
	 * 红点1 的位置
	 */
	private int iv1_position = ON_LEFT;
	/**
	 * 红点2 的位置
	 */
	private int iv2_position = ON_CENTER;
	/**
	 * 红点3 的位置
	 */
	private int iv3_position = ON_RIGHT;
	/**
	 * 动画宽度
	 */
	private int width;
	private boolean tag = true;
	/**
	 * 省份 控件
	 */
	private WheelView mViewProvince;
	/**
	 * 市控件
	 */
	private WheelView mViewCity;
	/**
	 * 区控件
	 */
	private WheelView mViewDistrict;
	/**
	 * 性别选择控件
	 */
	private WheelView mViewSex;
	/**
	 * 城市选择popupwindwo
	 */
	private PopupWindow window;
	/**
	 * 性别 选择popupwindwo
	 */
	private PopupWindow sexWindow;
	private final int TAG_SAVE_DATA=100;
	/**
	 * 头像加载器
	 */
	private ImageLoader imageLoader = ImageLoader.getInstance();
	// 临时头像路径
	private String fileUrlUUID;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tag = false;
			switch (msg.what) {
			case SUCCESS:
				// 上传头像成功
				Log.i("wxn", "fileUrlUUID:"+fileUrlUUID);
//				 LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL+fileUrlUUID,
//				 iv_self_photo, R.drawable.default_header);
//				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+fileUrlUUID, iv_self_photo,MyFragment.setDefaultImageOptions(R.drawable.default_header));
				imageLoader.displayImage(imgPath, dow_img,MyFragment.setDefaultImageOptions(R.drawable.default_header));
				ImageLoader.getInstance().displayImage("file:///"+imgPath, iv_self_photo);
				
//				ImageLoader
//						.getInstance()
//						.displayImage(
//								ConstantValue.BASE_IMAGE_URL+fileUrlUUID,
//								iv_self_photo,
//								MyFragment
//										.setDefaultImageOptions(R.drawable.default_header));
				break;
			case FILE:
				// 上传头像失败
				Toast.makeText(PersionDataActivity.this, "网络异常，请稍候重试!", 0)
						.show();
				break;
			case TAG_SAVE_DATA:
//				Toast.makeText(PersionDataActivity.this, "保存成功!", 0).show();
				// 保存数据
				useWayHandleSaveData();
				break;
			case SUCCESS_TOKEN:
				//获取token成功
				anilyseToken((String)msg.obj);
				break;
			case FILUE_TOKEN:
				//获取token失败
				break;
			default:
				break;
			}
		}
		
	};
	/**
	 * 城市 
	 */
	private TextView city_tv;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";
	/**
	 * 性别选择
	 */
	private String[] sex = new String[] { "女", "男" };
	/**
	 * 性别控件
	 */
	private TextView sex_tv;
	/**
	 * 软键盘管理类
	 */
	private InputMethodManager inputManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persiondata);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		initview();
		showPopwindow();
		showPopwindowForSex();
		setUpData();
		showDate();
		getToken();
		inputManager = (InputMethodManager) this.getSystemService(
				"input_method");
	}
	
	/**
	 * 获取token
	 */
	private void getToken() {
		if (TextUtils.isEmpty(GlobalParams.QN_TOKEN)) {
			GetDataFromInternet.getInStance().interViewNet(ConstantValue.GET_TOKEN,
					null, new GetDatas() {

						@Override
						public void getServerData(BackData data) {
							Log.i("wxn", "token"+data.getObject());
							if(data!=null){
								Object obj = data.getObject();
								if(obj!=null){
									Message msg = new Message();
									msg.what = SUCCESS_TOKEN;
									msg.obj =obj;
									handler.sendMessage(msg);
								}
								
							}
							Message msg = new Message();
							msg.what = FILUE_TOKEN;
							handler.sendMessage(msg);
						}
					}, false, false);
		}

	}
	/**
	 * 解析token数据获取
	 * @param data 后台返回的数据
	 */
	private void anilyseToken(String data){
		if(TextUtils.isEmpty(data))
			return;
		try {
			JSONObject jObj = new JSONObject(data);
			if(jObj.has("uptoken")){
				GlobalParams.QN_TOKEN = jObj.getString("uptoken");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 界面个控件展示
	 */
	private void showDate() {
		fileUrlUUID = GlobalParams.USER_HEADER;
		if (!TextUtils.isEmpty(GlobalParams.NICK_NAME)) {
			tv_nick_name.setText(GlobalParams.NICK_NAME);
		} else {
			tv_nick_name.setText(GlobalParams.USER_NAME);
		}
		phone_tv.setText(GlobalParams.USER_PHONE);
		emil_tv.setText(GlobalParams.USER_EMIL);
		if (TextUtils.isEmpty(GlobalParams.USER_GENDER)){
			sex_tv.setText("女");
		}else{
			if(GlobalParams.USER_GENDER.contains("女")||GlobalParams.USER_GENDER.contains("男")){
				sex_tv.setText(GlobalParams.USER_GENDER);
			}else{
				sex_tv.setText("女");
			}
		}

		if (!TextUtils.isEmpty(GlobalParams.USER_ADDRESS)
				&& !"null".equals(GlobalParams.USER_ADDRESS))
			city_tv.setText(GlobalParams.USER_ADDRESS);

	}

	@Override
	protected void onResume() {
		if (!TextUtils.isEmpty(fileUrlUUID)){
			if(fileUrlUUID.startsWith("http:")){
				imageLoader.displayImage(fileUrlUUID, iv_self_photo, MyFragment
						.setDefaultImageOptions(R.drawable.default_header));
			}else{
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+fileUrlUUID + ConstantValue.IMAGE_END, iv_self_photo, MyFragment
						.setDefaultImageOptions(R.drawable.default_header));
			}
		}
		width = animation_p.getLayoutParams().width;
		super.onResume();
	}
	
	/**
	 * 初始化view控件
	 */
	private void initview() {
		city_tv = (TextView) findViewById(R.id.city_tv);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		loding_bg = (RelativeLayout) findViewById(R.id.loding_bg);
		animation_p = (RelativeLayout) findViewById(R.id.animation_p);
		tv_nick_name = (EditText) findViewById(R.id.tv_nick_name);
		phone_tv = (TextView) findViewById(R.id.phone_tv);
		emil_tv = (EditText) findViewById(R.id.emil_tv);
		sex_tv = (TextView) findViewById(R.id.sex_tv);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.modify_photo).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		findViewById(R.id.sex_rl).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		findViewById(R.id.city_rl).setOnClickListener(this);
		iv_self_photo = (CircleImageView) findViewById(R.id.iv_self_photo);
		dow_img = (ImageView) findViewById(R.id.dow_img);
		iv_self_photo.setOnClickListener(this);
		emil_tv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.modify_photo:
		case R.id.iv_self_photo:
			// 修改头像
			Intent pxc = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pxc, GET_PHOTO);
			break;
		case R.id.save:

			// 判断手机号是否正确
			// if ("".equals(phone_tv.getText().toString().trim())) {
			// Toast.makeText(PersionDataActivity.this, "手机号不能为空", 0).show();
			// } else if ("".equals(emil_tv.getText().toString().trim())) {
			// Toast.makeText(PersionDataActivity.this, "邮箱不能为空", 0).show();
			// } else if
			// (!CommonUtils.isRegexPhone(phone_tv.getText().toString())) {
			// Toast.makeText(PersionDataActivity.this, "请输入正确的手机号", 0).show();
			//
			// } else if (!CommonUtils.isEmail(emil_tv.getText().toString())) {
			// Toast.makeText(PersionDataActivity.this, "请输入正确的邮箱", 0).show();
			//
			// } else if ("".equals(city_tv.getText().toString())) {
			// Toast.makeText(PersionDataActivity.this, "请输入您所在的城市", 0).show();
			//
			// } else
			if ("".equals(tv_nick_name.getText().toString())) {
				Toast.makeText(PersionDataActivity.this, "请输昵称", 0).show();
			} else {
				String emilcontent = emil_tv.getText().toString().trim();
				if (!TextUtils.isEmpty(emilcontent)
						&& !CommonUtils.isEmail(emilcontent)) {
					Toast.makeText(PersionDataActivity.this, "请输入正确的邮箱", 0)
							.show();
					return;
				}
				requestPersion();
			}

			break;
		case R.id.sex_rl:
			inputManager.hideSoftInputFromWindow(this.getWindow()
					.getDecorView().getWindowToken(), 0);
			showSexWindow();
			break;
		case R.id.city_rl:
			// 修改城市
			// timeSelector = new CitySelector(PersionDataActivity.this,
			// new CitySelector.ResultHandler() {
			// @Override
			// public void handle(String time) {
			// Toast.makeText(getApplicationContext(), time,
			// Toast.LENGTH_LONG).show();
			// }
			// }, "2015-10-27 09:33", "2016-11-29 21:54", instance);
			//
			// timeSelector.setScrollUnit(CitySelector.SCROLLTYPE.HOUR,
			// CitySelector.SCROLLTYPE.MINUTE);
			// timeSelector.show();
			inputManager.hideSoftInputFromWindow(PersionDataActivity.this.getWindow()
					.getDecorView().getWindowToken(), 0);
			show();
			break;
		case R.id.emil_tv:
			// 修改邮箱
			break;

		default:
			break;
		}

	}

	/**
	 * 保存个人信息
	 */
	private void requestPersion() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("picture", fileUrlUUID); // 头像
		params.put("nickname", tv_nick_name.getText().toString()); // 昵称
		params.put("gender", sex_tv.getText().toString()); // 性别
		params.put("city", city_tv.getText().toString()); // 地址
		params.put("mobile", phone_tv.getText().toString()); // 手机号
		params.put("email", emil_tv.getText().toString()); // 邮箱
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SAVE_PERSION_MESSAGE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									Message msg = new Message();
									msg.what = TAG_SAVE_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);
	}

	public void setCity(String s) {
		city_tv.setText(s);
	}

	private String imgPath;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GET_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Cursor cursor = PersionDataActivity.this.getContentResolver()
						.query(uri, null, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					imgPath = cursor.getString(1); // 图片文件路径
					cursor.close();
				} else {
					imgPath = uri.getPath();
				}
				loding_bg.setVisibility(View.VISIBLE);
				loding_bg.setAlpha(0.8f);
				resetAnim();
//				upload_headerfile(imgPath);
				uploadImage(imgPath);
//				List<String> list = new ArrayList<String>();
//				list.add(imgPath);
//				UpLoadFile.upLoadFile(list, new RequestCallBack<String>() {
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						// 上传成功
//						fileUrlUUID = ConstantValue.BASE_IMAGE_URL
//								+ responseInfo.result;
//						handler.sendEmptyMessage(SUCCESS);
//						Log.i("wxn", "resutl :" + fileUrlUUID);
//
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						// 上传失败
//						Log.i("wxn", "上传失败：" + arg1);
//						handler.sendEmptyMessage(FILE);
//
//					}
//				});

			}
			break;

		default:
			break;
		}
	}

	private void resetAnim() {
		// 第一个球在最左边时执行的动画（最左移动到最右）
		if (iv1_position == ON_LEFT) {
			anim = new TranslateAnimation(0, width - iv_1.getWidth(), 0, 0);
			anim.setDuration(time);
			anim.setFillAfter(true);
			iv_1.startAnimation(anim);
			iv1_position = ON_RIGHT;
			// 第一个球在右边时执行的动画（最右移动到中间）
		} else if (iv1_position == ON_RIGHT) {
			anim = new TranslateAnimation(width - iv_1.getWidth(), width / 2
					- iv_1.getWidth() / 2, 0, 0);
			anim.setDuration(time);
			anim.setFillAfter(true);
			iv_1.startAnimation(anim);
			iv1_position = ON_CENTER;
			// 第一个求在中间时执行的动画（中间移动到最左）
		} else {
			iv1_position = ON_LEFT;
			anim = new TranslateAnimation(width / 2 - iv_1.getWidth() / 2, 0,
					0, 0);
			anim.setDuration(time);
			anim.setFillAfter(true);
			iv_1.startAnimation(anim);
		}

		if (iv2_position == ON_CENTER) {
			anim2 = new TranslateAnimation(0, -width / 2 + iv_1.getWidth() / 2,
					0, 0);
			anim2.setDuration(time);
			anim2.setFillAfter(true);
			iv_2.startAnimation(anim2);
			iv2_position = ON_LEFT;
		} else if (iv2_position == ON_RIGHT) {
			anim2 = new TranslateAnimation(width / 2 - iv_1.getWidth() / 2, 0,
					0, 0);
			anim2.setDuration(time);
			anim2.setFillAfter(true);
			iv_2.startAnimation(anim2);
			iv2_position = ON_CENTER;
		} else {
			anim2 = new TranslateAnimation(-width / 2 + iv_1.getWidth() / 2,
					width / 2 - iv_1.getWidth() / 2, 0, 0);
			anim2.setDuration(time);
			anim2.setFillAfter(true);
			iv_2.startAnimation(anim2);
			iv2_position = ON_RIGHT;
		}

		if (iv3_position == ON_CENTER) {
			anim3 = new TranslateAnimation(-width / 2 + iv_1.getWidth() / 2,
					-width + iv_3.getWidth(), 0, 0);
			anim3.setDuration(time);
			anim3.setFillAfter(true);
			iv_3.startAnimation(anim3);
			iv3_position = ON_LEFT;
		} else if (iv3_position == ON_RIGHT) {
			anim3 = new TranslateAnimation(0, -width / 2 + iv_1.getWidth() / 2,
					0, 0);
			anim3.setDuration(time);
			anim3.setFillAfter(true);
			iv_3.startAnimation(anim3);
			iv3_position = ON_CENTER;
		} else {
			anim3 = new TranslateAnimation(-width + iv_3.getWidth(), 0, 0, 0);
			anim3.setDuration(time);
			anim3.setFillAfter(true);
			iv_3.startAnimation(anim3);
			iv3_position = ON_RIGHT;
		}

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (tag)
					resetAnim();
			}
		});
	}

	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_location, null);

		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);

		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});

		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);

	}
	
	/**
	 * 显示 城市选择器
	 */
	private void show() {
		// 在底部显示
		window.showAtLocation(this.findViewById(R.id.city_iv), Gravity.BOTTOM,
				0, 0);
	}
	
	/**
	 * 显示 性别选择器
	 */
	private void showSexWindow() {
		// 在底部显示
		sexWindow.showAtLocation(this.findViewById(R.id.city_iv),
				Gravity.BOTTOM, 0, 0);
	}
	
	/**
	 * 设置 城市选择器
	 */
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		mViewSex.setViewAdapter(new ArrayWheelAdapter<String>(this, sex));
		mViewSex.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (sexWindow.isShowing()) {
			sex_tv.setText(sex[newValue]);
		}
		if (window.isShowing()) {
			if (wheel == mViewProvince) {
				updateCities();
			} else if (wheel == mViewCity) {
				updateAreas();
			} else if (wheel == mViewDistrict) {
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			}

			if (mDistrictDatasMap.get(mCurrentCityName) != null
					&& mDistrictDatasMap.get(mCurrentCityName).length > 0) {
				mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
				city_tv.setText(mCurrentProviceName + " " + mCurrentCityName
						+ " " + mCurrentDistrictName);
			} else {
				city_tv.setText(mCurrentProviceName + " " + mCurrentCityName);
			}
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		if (mDistrictDatasMap.get(mCurrentCityName) != null
				&& mDistrictDatasMap.get(mCurrentCityName).length > 0)
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		else
			mCurrentDistrictName = "";

	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		try {
			XmlParserJSON json = new XmlParserJSON(getApplicationContext());
			provinceList = json.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getId());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 显示popupWindow
	 */
	private void showPopwindowForSex() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_sex, null);

		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		sexWindow = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		sexWindow.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		sexWindow.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		sexWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

		// popWindow消失监听方法
		sexWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

		mViewSex = (WheelView) view.findViewById(R.id.id_sex_whell);
		// 添加change事件
		mViewSex.addChangingListener(this);

	}

	private UploadManager uploadManager = new UploadManager();
	/**
	 * 上传图片到 7牛云服务器
	 * @param filePath
	 */
	public void uploadImage(String filePath) {
//		fileUrlUUID = getFileUrlUUID() ;
		uploadManager.put(filePath, null, GlobalParams.QN_TOKEN, new UpCompletionHandler() {
			public void complete(String key, com.qiniu.android.http.ResponseInfo info, JSONObject response) {
				loding_bg.setVisibility(View.GONE);
				try{
				if(response!=null){
					if(response.has("key")){
						fileUrlUUID = response.getString("key");
						handler.sendEmptyMessage(SUCCESS);
						return;
					}
				}
				}catch (Exception e){
					
				}
				handler.sendEmptyMessage(FILE);
//				if (info != null && info.statusCode == 200) {// 上传成功
//					String fileRealUrl = getRealUrl(fileUrlUUID);
//					System.out.println("debug:fileRealUrl = " + fileRealUrl);
//					
//				} else {
//					
//				}
			}

			
		}, new UploadOptions(null, null, false, new UpProgressHandler() {
			public void progress(String key, double percent) {
				
			}
		}, null));

	}
	
	/**
	 * 生成远程文件路径（全局唯一）
	 * 
	 * @return
	 */
	private String getFileUrlUUID() {
		String filePath = android.os.Build.MODEL + "__" + System.currentTimeMillis() + "__" + (new Random().nextInt(500000))
				+ "_" + (new Random().nextInt(10000));
		return filePath.replace(".", "0");
	}
	
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}
	
	/**
	 * 保存数据
	 */
	private void useWayHandleSaveData() {
		// TODO Auto-generated method stub
		GlobalParams.USER_NAME = tv_nick_name.getText().toString();
		GlobalParams.USER_PHONE = phone_tv.getText().toString();
		GlobalParams.USER_EMIL = emil_tv.getText().toString();
		GlobalParams.USER_GENDER = sex_tv.getText().toString();
		GlobalParams.USER_ADDRESS = city_tv.getText().toString();
		GlobalParams.USER_HEADER = fileUrlUUID;
		SharedPreferencesUtils.saveData(GlobalParams.context,
				"ls_user_message", "mobile", GlobalParams.USER_PHONE);
		SharedPreferencesUtils.saveData(GlobalParams.context,
				"ls_user_message", "nickname", GlobalParams.USER_NAME);
		SharedPreferencesUtils.saveData(GlobalParams.context,
				"ls_user_message", "email", GlobalParams.USER_EMIL);
		SharedPreferencesUtils.saveData(GlobalParams.context,
				"ls_user_message", "picture", GlobalParams.USER_HEADER);
		SharedPreferencesUtils.saveData(GlobalParams.context,
				"ls_user_message", "gender", GlobalParams.USER_GENDER);
		SharedPreferencesUtils
				.saveData(GlobalParams.context, "ls_user_message",
						"address", GlobalParams.USER_ADDRESS);
		finish();
	};
	
	

}
