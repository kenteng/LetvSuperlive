package com.lesports.stadium.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.writeDateToSdCard;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 退款界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class TuiKuanActivity extends BaseActivity implements OnClickListener {

	/**
	 * 选择照片的点击布局
	 */
	private LinearLayout mLayoutChoiseImage;
	private Uri originalUri;
	private byte[] mContent;
	private String cachePath;
	private File file, file2, file3;
	private Bitmap myBitmap = null;
	private Bitmap myBitmap2 = null;
	private Bitmap myBitmap3 = null;
	/**
	 * 定义变量，该变量用来标记是第几次选择图片
	 */
	private int ChoiseNum = 1;
	/**
	 * 选择图片的按钮
	 */
	private ImageView mChoiseImage;
	/**
	 * 需要加载照片的imageview
	 */
	private ImageView mImageAdd1, mImageAdd2, mImageAdd3;
	/**
	 * 三个减号按钮
	 */
	private ImageView mJianhao1, mJianhao2, mJianhao3;
	/**
	 * 上传图片说明
	 */
	private TextView mShangchuan;
	/**
	 * 退款原因
	 */
	private EditText mTuikuanYuanyin;
	/**
	 * 退款金额
	 */
	private EditText mTuikuanJine;
	/**
	 * 退款说明
	 */
	private EditText mTuikuanShuoming;
	/**
	 * 退款返回键
	 */
	private ImageView mBack;
	/**
	 * 订单id
	 */
	private OrderListBean orderBean;;
	/**
	 * 提交订单的点击布局
	 */
	private RelativeLayout mLayout_goto;
	/**
	 * 用来标记第一张图片的路径
	 */
	private String filePath1;
	/**
	 * 第二张图片路径
	 */
	private String filePath2;
	/**
	 * 第三张图片路径
	 */
	private String filePath3;
	/**
	 * 该标记用来标注是十分钟内还是十分钟之后
	 */
	private boolean is_SHIFENZHONG = false;
	/**
	 * 退款说明
	 */
	private TextView mTuikuanshuomingss;
	/**
	 * 本类对象
	 */
	public static TuiKuanActivity instance;
	/**
	 * 退款进度布局
	 */
	private RelativeLayout mTuikuanjindu;
	/**
	 * 退款最大金额
	 */
	private float price = 0;
	/**
	 * 处理数据的handler；
	 */
	private final int HANDLE_TAG_5 = 5;
	private final int HANDLE_TAG_1 = 1;
	private final int HANDLE_TAG_15 = 15;
	private final int HANDLE_TAG_55 = 55;
	private final int HANDLE_TAG_115 = 115;
	private final int HANDLE_TAG_1151 = 1151;
	private final int HANDLE_TAG_116 = 116;
	private final int HANDLE_TAG_1161 = 1161;
	private final int HANDLE_TAG_117 = 117;
	private final int HANDLE_TAG_118 = 118;
	private final int HANDLE_TAG_155 = 155;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case HANDLE_TAG_1:
				//
				Toast.makeText(TuiKuanActivity.this, "网络请求超时", 0).show();
				break;
			case HANDLE_TAG_5:
				String backdatass = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatass)) {
					if (backdatass.equals("SUCCESS")) {
						useWayHandleTuikuan10HOU(backdatass);
					}
				}
				break;
			case HANDLE_TAG_15:
				Toast.makeText(TuiKuanActivity.this, "申请失败", 0).show();
				break;
			case HANDLE_TAG_55:
				String timestring = (String) msg.obj;
				if (!TextUtils.isEmpty(timestring)) {
					useWayHandletuikuan(timestring);
				}
				break;
			case HANDLE_TAG_115:
				String backdatasss = (String) msg.obj;
				mTuikuanjindu.setVisibility(View.GONE);
				if (!TextUtils.isEmpty(backdatasss)) {
					useWayHandleTuikuan10nei(backdatasss);
				}
				break;
			case HANDLE_TAG_1151:
				// 说明是十分钟外的
				String base = (String) msg.obj;
				if (!TextUtils.isEmpty(base)) {
					mTuikuanjindu.setVisibility(View.GONE);
					useWayHandleTuikuan10wai(base);
				}
				break;
			case HANDLE_TAG_116:
				String baseee = (String) msg.obj;
				if (!TextUtils.isEmpty(baseee)) {
					// 说明是十分钟内的
					// 支付宝的退款状态
					mTuikuanjindu.setVisibility(View.GONE);
					useWayHandleALIPAY10nei(baseee);
				}
				break;
			case HANDLE_TAG_1161:
				String baseees = (String) msg.obj;
				if (!TextUtils.isEmpty(baseees)) {
					// 说明是十分钟内的
					mTuikuanjindu.setVisibility(View.GONE);
					useWayHandleALIPAY10neis(baseees);
				}
				break;
			case HANDLE_TAG_117:
				mTuikuanjindu.setVisibility(View.GONE);
				Toast.makeText(TuiKuanActivity.this, "退款失败", 0).show();
				break;
			case HANDLE_TAG_118:
				mTuikuanjindu.setVisibility(View.GONE);
				Toast.makeText(TuiKuanActivity.this, "网络异常", 0).show();
				break;
			case HANDLE_TAG_155:
				mTuikuanjindu.setVisibility(View.VISIBLE);
				Toast.makeText(TuiKuanActivity.this, "退款失败", 0).show();
				break;

			default:
				break;
			}

		}


	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuikuan);
		instance = this;
		cachePath = CacheManager.getInstance().getCachePath(
				CacheType.CACHE_IMAGE);
		initviews();
		initDtatas();
	}

	/**
	 * 根据传入的时间值，截取分钟数
	 * 
	 * @param str
	 * @return
	 */
	public String getHourAndMinute(String str) {
		String hm = str.substring(3, 5);
		return hm;
	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}

	/**
	 * 获取上个界面传递过来的订单数据
	 */
	private void initDtatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		orderBean = (OrderListBean) intent.getSerializableExtra("order");
		price = Float.parseFloat(orderBean.getAmount());
		mTuikuanshuomingss.setText("最多" + price + "元");
	}

	/**
	 * 初始化界面
	 */
	private void initviews() {
		// TODO Auto-generated method stub
		mTuikuanjindu = (RelativeLayout) findViewById(R.id.layout_tuikuan_jindu);
		mTuikuanshuomingss = (TextView) findViewById(R.id.tuikuan_tuikuanjine_shuoming);
		mLayout_goto = (RelativeLayout) findViewById(R.id.textView1_tijiaoshenqing_llayout);
		mLayout_goto.setOnClickListener(this);
		mBack = (ImageView) findViewById(R.id.tuikuan_back);
		mBack.setOnClickListener(this);
		mTuikuanShuoming = (EditText) findViewById(R.id.tuikuan_tuikuanshuoming_input);
		mTuikuanShuoming.clearFocus();
		mTuikuanJine = (EditText) findViewById(R.id.tuikuan_tuikuanjine_input);
		mTuikuanJine.clearFocus();
		mTuikuanYuanyin = (EditText) findViewById(R.id.tuikuan_tuikuanleixing_tuikuanyuanyin);
		mTuikuanYuanyin.clearFocus();
		mShangchuan = (TextView) findViewById(R.id.shangchuanpingzheng_shuoming);
		mJianhao1 = (ImageView) findViewById(R.id.layout_yonghuxuanze_jianhao_1);
		mJianhao1.setOnClickListener(this);
		mJianhao2 = (ImageView) findViewById(R.id.layout_yonghuxuanze_jianhao_2);
		mJianhao2.setOnClickListener(this);
		mJianhao3 = (ImageView) findViewById(R.id.layout_yonghuxuanze_jianhao_3);
		mJianhao3.setOnClickListener(this);
		mChoiseImage = (ImageView) findViewById(R.id.shangchuanpingzheng_shuomingtupian);
		mChoiseImage.setOnClickListener(this);
		mImageAdd1 = (ImageView) findViewById(R.id.layout_yonhuxuanze_tupian_1);
		mImageAdd1.setOnClickListener(this);
		mImageAdd2 = (ImageView) findViewById(R.id.layout_yonhuxuanze_tupian_2);
		mImageAdd2.setOnClickListener(this);
		mImageAdd3 = (ImageView) findViewById(R.id.layout_yonhuxuanze_tupian_3);
		mImageAdd3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shangchuanpingzheng_shuomingtupian:
			if (mJianhao1.getVisibility() == View.GONE
					&& mJianhao3.getVisibility() == View.GONE
					&& mJianhao3.getVisibility() == View.GONE) {
				ChoiseNum = 1;
			}
			if (ChoiseNum < 4) {
				final CharSequence[] items1 = { "相册", "拍照" };
				AlertDialog dlg1 = new AlertDialog.Builder(TuiKuanActivity.this)
						.setTitle("选择图片")
						.setItems(items1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										// 这里item是根据选择的方式，
										// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
										if (item == 1) {
											Intent getImageByCamera = new Intent(
													"android.media.action.IMAGE_CAPTURE");
											startActivityForResult(
													getImageByCamera, 1);
										} else {
											Intent getImage = new Intent(
													Intent.ACTION_GET_CONTENT);
											getImage.addCategory(Intent.CATEGORY_OPENABLE);
											getImage.setType("image/jpeg");
											startActivityForResult(getImage, 11);
										}
									}
								}).create();
				dlg1.show();
			} else {
				Toast.makeText(TuiKuanActivity.this, "图片最多能上传三张", 0).show();
			}

			break;
		case R.id.layout_yonghuxuanze_jianhao_1:
			filePath1 = null;
			mJianhao1.setVisibility(View.GONE);
			mImageAdd1.setVisibility(View.GONE);
			ChoiseNum = 1;
			break;
		case R.id.layout_yonghuxuanze_jianhao_2:
			filePath2 = null;
			mJianhao2.setVisibility(View.GONE);
			mImageAdd2.setVisibility(View.GONE);
			ChoiseNum = 2;
			break;
		case R.id.layout_yonghuxuanze_jianhao_3:
			filePath3 = null;
			mJianhao3.setVisibility(View.GONE);
			mImageAdd3.setVisibility(View.GONE);
			ChoiseNum = 3;
			break;
		case R.id.textView1_tijiaoshenqing_llayout:
			Log.i("退款", "是否监听到了");
			if (!TextUtils.isEmpty(orderBean.getId())) {
				Log.i("退款", "是否监听到了");
				mTuikuanjindu.setVisibility(View.VISIBLE);
				// 先调用方法，判断该订单是否在十分钟之内
				Tuikuanshijian(orderBean.getId());
			}
			break;
		case R.id.tuikuan_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void Tuikuanshijian(String orderid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderid);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.TUIKUAN_TIME_PANDUAN, params, new GetDatas() {
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
									Log.i("创建的时候预约列表数据", backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_55;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_155;
										handler.sendMessage(msg);
									}

								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void getTuikuan(String orderid, String tuikuanleixing,
			String tuikuanyuanyin, String tuikuanjine, String tuikuanshuoming) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderid);
		params.put("refundType", tuikuanleixing);
		params.put("refundReason", tuikuanyuanyin);
		params.put("refundFee", tuikuanjine);
		params.put("refundRemark", tuikuanshuoming);
		params.put("attachments", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SHENGQING_TUIKUAN, params, new GetDatas() {
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
									Log.i("创建的时候预约列表数据", backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if (data.getNetResultCode() == 0) {
										// 十分钟外
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_1151;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else if (data.getNetResultCode() == 666) {

										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_1161;
										msg.obj = backdata;
										handler.sendMessage(msg);
										// 支付宝处于异步回掉状态

									} else if (data.getNetResultCode() == 999) {
										// 处于退款失败中
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_117;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_117;
										handler.sendMessage(msg);
									}

								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void getTuikuan_quxiao(final String orderid, String tuikuanleixing,
			String tuikuanyuanyin, String tuikuanjine, String tuikuanshuoming) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", orderid);
		params.put("refundType", tuikuanleixing);
		params.put("refundReason", tuikuanyuanyin);
		params.put("refundFee", tuikuanjine);
		params.put("refundRemark", tuikuanshuoming);
		params.put("attachments", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.QUXIAO_ORDER, params, new GetDatas() {
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
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Log.i("lwc",
											"目前退款状态是" + data.getNetResultCode()
													+ " backdata :" + backdata
													+ "orderid:" + orderid);
									if (data.getNetResultCode() == 0) {
										// 微信的退款成功
										// 说明是十分钟内
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_115;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else if (data.getNetResultCode() == 666) {
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_116;
										msg.obj = backdata;
										handler.sendMessage(msg);
										// 支付宝处于异步回掉状态

									} else if (data.getNetResultCode() == 999) {
										// 处于退款失败中
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_117;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = HANDLE_TAG_118;
										handler.sendMessage(msg);
									}

								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * 用来判断用户输入的金额是否合法
	 * 
	 * @param orderMoneyNum
	 */
	private boolean decodeTheNum(String orderMoneyNum) {
		// TODO Auto-generated method stub
		boolean isUse = false;
		if (!TextUtils.isEmpty(orderMoneyNum)) {
			// 先判断是否存在。号
			if (orderMoneyNum.contains(".")) {
				float money = Float.parseFloat(orderMoneyNum);
				Log.i("可退",price+"");
				Log.i("实退",money+"");
				if (money > price) {
					isUse = false;
				} else {
					isUse = true;
				}
			} else {
				String one = orderMoneyNum.substring(0,1);
				int ones = Integer.parseInt(one);
				if (ones != 0) {
					int money = Integer.parseInt(orderMoneyNum);
					if (money > price) {
						isUse = false;
					} else {
						isUse = true;
					}
				} else {
					isUse = false;
				}
			}

		} else {
			isUse = false;
		}
		return isUse;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		ContentResolver resolver = getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法， 这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		if (requestCode == 11) {
			try {
				if (data != null) {
					// 获得图片的uri
					originalUri = data.getData();
					if (originalUri == null) {

					} else {

						switch (ChoiseNum) {
						case 1:
							// 将图片内容解析成字节数组
							mContent = readStream(resolver.openInputStream(Uri
									.parse(originalUri.toString())));
							boolean b = writeDateToSdCard.writeDateTosdcard(
									cachePath, "123456.jpg", mContent);
							file = new File(cachePath + "/" + "123456.jpg");
							// 将字节数组转换为ImageView可调用的Bitmap对象
							// // //把得到的图片绑定在控件上显示
							myBitmap = decodeFile(file);
							if (myBitmap == null) {
								Toast.makeText(TuiKuanActivity.this, "1没有图片", 0)
										.show();
							} else {
								filePath1 = cachePath + "/" + "123456.jpg";
								mShangchuan.setVisibility(View.GONE);
								mImageAdd1.setImageBitmap(myBitmap);
								mImageAdd1.setVisibility(View.VISIBLE);
								mJianhao1.setVisibility(View.VISIBLE);
								ChoiseNum = 2;
							}
							break;
						case 2:
							// 将图片内容解析成字节数组
							mContent = readStream(resolver.openInputStream(Uri
									.parse(originalUri.toString())));
							writeDateToSdCard.writeDateTosdcard(cachePath,
									"1234567.jpg", mContent);
							file = new File(cachePath + "/" + "1234567.jpg");
							// 将字节数组转换为ImageView可调用的Bitmap对象
							// // //把得到的图片绑定在控件上显示
							myBitmap2 = decodeFile(file);
							if (myBitmap2 == null) {
								Toast.makeText(TuiKuanActivity.this, "1没有图片", 0)
										.show();
							} else {
								filePath2 = cachePath + "/" + "1234567.jpg";
								mImageAdd2.setImageBitmap(myBitmap2);
								mImageAdd2.setVisibility(View.VISIBLE);
								mJianhao2.setVisibility(View.VISIBLE);
								ChoiseNum = 3;
							}
							break;
						case 3:
							// 将图片内容解析成字节数组
							mContent = readStream(resolver.openInputStream(Uri
									.parse(originalUri.toString())));
							writeDateToSdCard.writeDateTosdcard(cachePath,
									"12345678.jpg", mContent);
							file = new File(cachePath + "/" + "12345678.jpg");
							// 将字节数组转换为ImageView可调用的Bitmap对象
							// // //把得到的图片绑定在控件上显示
							myBitmap3 = decodeFile(file);
							if (myBitmap3 == null) {
								Toast.makeText(TuiKuanActivity.this, "3没有图片", 0)
										.show();
							} else {
								filePath3 = cachePath + "/" + "12345678.jpg";
								mImageAdd3.setImageBitmap(myBitmap3);
								mImageAdd3.setVisibility(View.VISIBLE);
								mJianhao3.setVisibility(View.VISIBLE);
								ChoiseNum = 4;
							}
							break;

						default:
							break;
						}
					}
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else if (requestCode == 1) {
			try {

				super.onActivityResult(requestCode, resultCode, data);
				if (data != null) {
					Bundle extras = data.getExtras();
					myBitmap = (Bitmap) extras.get("data");
					if (myBitmap == null) {

					} else {
						switch (ChoiseNum) {
						case 1:
							// 将图片内容解析成字节数组
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							myBitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									baos);
							mContent = baos.toByteArray();
							boolean b = writeDateToSdCard.writeDateTosdcard(
									cachePath, "12345612.jpg", mContent);
							file = new File(cachePath + "/" + "12345612.jpg");
							// 把得到的图片绑定在控件上显示
							myBitmap = decodeFile(file);
							if (myBitmap == null) {
								Toast.makeText(TuiKuanActivity.this, "1没有图片", 0)
										.show();
							} else {
								filePath1 = cachePath + "/" + "12345612.jpg";
								mShangchuan.setVisibility(View.GONE);
								mImageAdd1.setImageBitmap(myBitmap);
								mImageAdd1.setVisibility(View.VISIBLE);
								mJianhao1.setVisibility(View.VISIBLE);
								ChoiseNum = 2;
							}
							break;
						case 2:
							// 将图片内容解析成字节数组
							ByteArrayOutputStream baosw = new ByteArrayOutputStream();
							myBitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									baosw);
							mContent = baosw.toByteArray();
							writeDateToSdCard.writeDateTosdcard(cachePath,
									"123456123.jpg", mContent);
							file = new File(cachePath + "/" + "123456123.jpg");
							// 把得到的图片绑定在控件上显示
							myBitmap2 = decodeFile(file);
							if (myBitmap2 == null) {
								Toast.makeText(TuiKuanActivity.this, "1没有图片", 0)
										.show();
							} else {
								filePath2 = cachePath + "/" + "123456123.jpg";
								mImageAdd2.setImageBitmap(myBitmap2);
								mImageAdd2.setVisibility(View.VISIBLE);
								mJianhao2.setVisibility(View.VISIBLE);
								ChoiseNum = 3;
							}
							break;
						case 3:
							// 将图片内容解析成字节数组
							ByteArrayOutputStream baosw3 = new ByteArrayOutputStream();
							myBitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									baosw3);
							mContent = baosw3.toByteArray();
							writeDateToSdCard.writeDateTosdcard(cachePath,
									"1234561234.jpg", mContent);
							file = new File(cachePath + "/" + "1234561234.jpg");
							// 把得到的图片绑定在控件上显示
							myBitmap3 = decodeFile(file);
							if (myBitmap3 == null) {
								Toast.makeText(TuiKuanActivity.this, "3没有图片", 0)
										.show();
							} else {
								filePath3 = cachePath + "/" + "1234561234.jpg";
								mImageAdd3.setImageBitmap(myBitmap3);
								mImageAdd3.setVisibility(View.VISIBLE);
								mJianhao3.setVisibility(View.VISIBLE);
								ChoiseNum = 4;
							}
							break;

						default:
							break;
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	private Bitmap decodeFile(File f) {

		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// The new size we want to scale to
			final int REQUIRED_HEIGHT = 800;
			final int REQUIRED_WIDTH = 480;
			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			System.out.println(width_tmp + "  " + height_tmp);
			Log.w("===", (width_tmp + "  " + height_tmp));
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_WIDTH
						&& height_tmp / 2 < REQUIRED_HEIGHT)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
				Log.w("===", scale + "''" + width_tmp + "  " + height_tmp);
			}
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	/**
	 * 处理十分钟之后的退款
	 * @param backdatass
	 */
	private void useWayHandleTuikuan10HOU(String backdatass) {
		// TODO Auto-generated method stub
		Intent intents = new Intent(TuiKuanActivity.this,
				TuikuanOrderDetailActivity.class);
		intents.putExtra("jine", mTuikuanJine.getText()
				.toString());
		intents.putExtra("order", orderBean);
		intents.putExtra("tag", "8");
		intents.putExtra("page", "three");
		startActivity(intents);
		TuiKuanActivity.this.finish();
	}
	/**
	 * 处理退款时间，看到底是十分钟内还是十分钟后
	 * @param timestring
	 */
	private void useWayHandletuikuan(String timestring) {
		// TODO Auto-generated method stub
		String str;
		try {
			JSONObject obj = new JSONObject(timestring);
			if (obj.has("timeDifferent")) {
				str = obj.getString("timeDifferent");
				String time = getChangeToTime(str);
				String mini = getHourAndMinute(time);
				int mimiis = Integer.parseInt(mini);
				if (mimiis > 10) {
					// 进行十分钟后的退款操作
					is_SHIFENZHONG = false;
					String orderMoneyNum = mTuikuanJine.getText()
							.toString();
					// 调用方法，判断金额是否合法
					boolean isuse = decodeTheNum(orderMoneyNum);
					if (isuse) {
						String orderType = "1";// 默认是我要退货
						String orderYuanyin = mTuikuanYuanyin
								.getText().toString();
						String orderShuoming = mTuikuanShuoming
								.getText().toString();
						getTuikuan(orderBean.getId(), orderType,
								orderYuanyin, orderMoneyNum,
								orderShuoming);
					} else {
						mTuikuanjindu.setVisibility(View.GONE);
						Toast.makeText(TuiKuanActivity.this,
								"请输入合法的退款金额", 0).show();
					}
				} else {
					// 进行十分钟内的退款操作
					is_SHIFENZHONG = true;
					String orderMoneyNum = mTuikuanJine.getText()
							.toString();
					// 调用方法，判断金额是否合法
					boolean isuse = decodeTheNum(orderMoneyNum);
					if (isuse) {
						String orderType = "1";// 默认是我要退货
						String orderYuanyin = mTuikuanYuanyin
								.getText().toString();
						String orderShuoming = mTuikuanShuoming
								.getText().toString();
						getTuikuan_quxiao(orderBean.getId(),
								orderType, orderYuanyin,
								orderMoneyNum, orderShuoming);
					} else {
						Toast.makeText(TuiKuanActivity.this,
								"请输入合法的退款金额", 0).show();
						mTuikuanjindu.setVisibility(View.GONE);
					}
				}
			} else {
				Toast.makeText(TuiKuanActivity.this, "网络异常", 0)
						.show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 十分钟内的退款操作
	 * @param backdatasss
	 */
	private void useWayHandleTuikuan10nei(String backdatasss) {
		// TODO Auto-generated method stub
		Log.i("十分钟内", backdatasss);
		Intent intent = new Intent(TuiKuanActivity.this,
				TuikuanSuccessActivity.class);
		intent.putExtra("yuanyin", mTuikuanYuanyin.getText()
				.toString());
		intent.putExtra("jine", mTuikuanJine.getText().toString());
		intent.putExtra("shuoming", mTuikuanShuoming.getText()
				.toString());
		intent.putExtra("order", orderBean);
		startActivity(intent);
	}
	/**
	 * 十分钟之外的退款操作
	 * @param base
	 */
	private void useWayHandleTuikuan10wai(String base) {
		// TODO Auto-generated method stub
		Intent intentss = new Intent(TuiKuanActivity.this,
				TuikuanOrderDetailActivity.class);
		intentss.putExtra("jine", mTuikuanJine.getText().toString());
		intentss.putExtra("order", orderBean);
		intentss.putExtra("tag", "8");
		intentss.putExtra("page", "two");
		startActivity(intentss);
		TuiKuanActivity.this.finish();
	}
	/**
	 * 十分钟内的支付宝退款操作
	 * @param baseee
	 */
	private void useWayHandleALIPAY10nei(String baseee) {
		// TODO Auto-generated method stub
		Intent intents = new Intent(TuiKuanActivity.this,
				TuikuanOrderDetailActivity.class);
		intents.putExtra("jine", mTuikuanJine.getText().toString());
		intents.putExtra("order", orderBean);
		intents.putExtra("tag", "8");
		intents.putExtra("page", "two");
		startActivity(intents);
		TuiKuanActivity.this.finish();
	}
	private void useWayHandleALIPAY10neis(String baseee) {
		// TODO Auto-generated method stub
		Intent intents = new Intent(TuiKuanActivity.this,
				TuikuanOrderDetailActivity.class);
		intents.putExtra("jine", mTuikuanJine.getText().toString());
		intents.putExtra("order", orderBean);
		intents.putExtra("tag", "8");
		intents.putExtra("page", "two");
		startActivity(intents);
		TuiKuanActivity.this.finish();
	}



}
