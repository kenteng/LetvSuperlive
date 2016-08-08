package com.lesports.stadium.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.SharePopupWindow;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * ***************************************************************
 * 
 * @ClassName: ShareFaceScoreActivity
 * 
 * @Desc : 我要上颜值 分享界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-15 下午3:31:51
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ShareFaceScoreActivity extends Activity implements
		OnClickListener, PlatformActionListener, Callback {
	/*
	 * 上传控件
	 */
	private ImageView upload_image;
	/*
	 * 返回控件
	 */
	private ImageView back_image;
	/*
	 * 显示图片控件
	 */
	private ImageView show_image;
	/**
	 * 左右移动三个点
	 */
	private ImageView iv_1, iv_2, iv_3;
	/**
	 * 加载动画
	 */
	private RelativeLayout loding_bg, animation_p;
	private TranslateAnimation anim;
	private TranslateAnimation anim2;
	private TranslateAnimation anim3;
	private int time = 500;
	private static final int ON_LEFT = 1;
	private static final int ON_CENTER = 2;
	private static final int ON_RIGHT = 3;
	private static final int SUCCESS_TOKEN = 666;
	private int iv1_position = ON_LEFT;
	private int iv2_position = ON_CENTER;
	private int iv3_position = ON_RIGHT;
	private int width;
	private boolean tag = true;
	public static final String filePath = "path";

	private SharePopupWindow share;
	// private String text = "这是我的分享测试数据！~我只是来酱油的！~请不要在意 好不好？？？？？";
	// private String imageurl =
	// "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
	private String imageurl;
	// private String title = "拍拍搜";
	// private String url = "www.baidu.com";
	private String fromType;
	private String path;
	private String fileUrlUUID;
	/**
	 * handle里面需要用的标记
	 */
	private final int HANDLE_TAG_0=0;
	private final int HANDLE_TAG_1=1;
	private final int HANDLE_TAG_3=3;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loding_bg.setVisibility(View.GONE);
			tag = false;
			switch (msg.what) {
			case HANDLE_TAG_0:
				showPop(false);
				break;
			case HANDLE_TAG_1:
				Toast.makeText(getApplicationContext(), "服务器繁忙，请稍后重试", 0)
						.show();
				break;
			case HANDLE_TAG_3:
				showPop(true);
				// createDialog();
				// exitDialog.show();
				break;
			case SUCCESS_TOKEN:
				anilyseToken((String)msg.obj);
				break;

			default:
				break;
			}

		};
	};
	private CustomDialog exitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharefacescore);
		ShareSDK.initSDK(this);
		initView();
		initdate();
		initListerner();
		// createDialog();
		getToken();
	}

	/**
	 * @Title: initListerner
	 * @Description: 初始化监听
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initListerner() {
		upload_image.setOnClickListener(this);
		back_image.setOnClickListener(this);

	}

	/**
	 * @Title: initdate
	 * @Description: 初始化view
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initView() {
		upload_image = (ImageView) findViewById(R.id.upload_image);
		back_image = (ImageView) findViewById(R.id.back_image);
		show_image = (ImageView) findViewById(R.id.show_image);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		loding_bg = (RelativeLayout) findViewById(R.id.loding_bg);
		animation_p = (RelativeLayout) findViewById(R.id.animation_p);

	}

	/**
	 * @Title: initView
	 * @Description:初始化数据
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initdate() {
		Intent intent = getIntent();
		path = intent.getStringExtra(filePath);
		Log.i("wxn", "path"+path);
		String rotateResult = intent.getStringExtra("rotateResult");
		fromType = intent.getStringExtra("type");
		if (TextUtils.isEmpty(fromType)) {
			upload_image.setImageResource(R.drawable.up_pic);
		}
		if (!TextUtils.isEmpty(path)) {
			Bitmap cameraBitmap = RecordActivity.readBitMap(path,
					show_image.getWidth(), show_image.getHeight());
			if (!TextUtils.isEmpty(rotateResult)) {
				int result = Integer.parseInt(rotateResult);
				Matrix matrix = new Matrix();
				matrix.setRotate(result);
				cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
						cameraBitmap.getWidth(), cameraBitmap.getHeight(),
						matrix, true);
			}
			System.gc();
			show_image.setImageBitmap(cameraBitmap);
		}

	}

	@Override
	public void onClick(View v) {
		int resId = v.getId();
		switch (resId) {
		case R.id.upload_image:
			showPop(false);
//			if(!TextUtils.isEmpty(fileUrlUUID)){
//				handler.sendEmptyMessage(0);
//				return;
//			}
//			// 上传文件
//			resetAnim();
//			loding_bg.setVisibility(View.VISIBLE);
//			loding_bg.setAlpha(0.8f);
//			//上传图片到7牛服务器
//			uploadImage(path);
			break;
		case R.id.back_image:
			relaseMemorry();
			break;
		default:
			break;
		}

	}

	private void showPop(boolean isShow) {
		share = new SharePopupWindow(this);
		share.setSharedBitmap(((BitmapDrawable)(show_image.getDrawable())).getBitmap());
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
		imageurl = ConstantValue.BASE_IMAGE_URL+fileUrlUUID +ConstantValue.IMAGE_END;
		Log.i("wxn", "上showPop成功：" + imageurl);
		model.setImageUrl(imageurl);
		model.setImagePath(path);
//		 model.setText(text);
//		 model.setTitle(title);
		model.setUrl(imageurl);
		
		share.initShareParams(model);
		share.showShareWindow(false);
		if (isShow) {
			share.setTopShow("照片上传成功", true);
		} else {
			share.setTopShow("分享", false);
		}
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.main_share), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@Override
	public void onBackPressed() {
		relaseMemorry();
		super.onBackPressed();
	}

	public void relaseMemorry() {
		show_image = null;
		share = null;
		System.gc();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		width = animation_p.getLayoutParams().width;
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		if (what == 1) {
			Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	private void createDialog() {
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
				relaseMemorry();
			}
		});
		exitDialog.setConfirmTxt("完成");
		exitDialog.setRemindMessage("上传成功！");
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
	private UploadManager uploadManager = new UploadManager();
	public void uploadImage(String filePath) {
//		fileUrlUUID = getFileUrlUUID() ;
//		long nowLongTime = System.currentTimeMillis();
		String uuid = java.util.UUID.randomUUID().toString();
		uuid = uuid.replace("-", "_");
		uploadManager.put(filePath, uuid+".png", GlobalParams.QN_TOKEN, new UpCompletionHandler() {
			public void complete(String key, com.qiniu.android.http.ResponseInfo info, JSONObject response) {
				loding_bg.setVisibility(View.GONE);
				Log.i("wxn", "response:"+response);
				try{
				if(response!=null){
					if(response.has("key")){
						fileUrlUUID = response.getString("key");
						handler.sendEmptyMessage(3);
						return;
					}
				}
				}catch (Exception e){
					
				}
				handler.sendEmptyMessage(1);
			}

			
		}, new UploadOptions(null, null, false, new UpProgressHandler() {
			public void progress(String key, double percent) {
				
			}
		}, null));

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
						}
					}, false, false);
		}

	}
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

}
