package com.lesports.stadium.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.TemMessage;
import com.lesports.stadium.bean.postMessage;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.engine.ClsOscilloscope;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.ImageUtils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyVisualizerView;
import com.lesports.stadium.view.SpringProgressView;
import com.lesports.stadium.view.VisualizerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: RecordActivity
 * 
 * @Desc : 现场_演唱会_互动 -测分贝
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-2 下午2:28:58
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class RecordActivity extends Activity implements OnClickListener {
	/**
	 * log 标识
	 */
	private static final String TAG = "RecordActivity";
	/**
	 * 布局中两个Button控件用于拍照选相册背景图片
	 */
	ImageView startButton, stopButton;
	/**
	 * 布局中两个TextView 控件 用于显示音量分贝值
	 */
	private TextView rel_voice_size, rel_voice_text;
	/**
	 * 当前位置
	 */
	private TextView location_tv_re;
	/**
	 * 布局中两个RelativeLayout 控件 用于添加自定义控件
	 */
	private RelativeLayout rel_voice_view, energy;
	/**
	 * 音柱动画
	 */
	private VisualizerView mBaseVisualizerView;
	/**
	 * 分贝值动画
	 */
	private MyVisualizerView myviesu;

	/**
	 * ClsOscilloscope 实例
	 */
	ClsOscilloscope clsOscilloscope;

	/**
	 * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	 */
	static final int frequency = 44100;
	/*
	 * 设置双声道
	 */
	static final int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
	/*
	 * 音频数据格式：每个样本16位
	 */
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	/*
	 * // 录音最小buffer大小
	 */
	int recBufSize;
	/**
	 * 录音对象AudioRecord
	 */
	AudioRecord audioRecord;
	/**
	 * 调用系统相册获取图片
	 */
	private static final int GET_PHOTO = 2;
	/**
	 * 相机对象Camera
	 */
	private Camera camera;
	private int currentCameraId;
	private CameraPreview preview;
	private FrameLayout cameraPreview;
	public static final int kPhotoMaxSaveSideLen = 2600;
	private FrameLayout.LayoutParams params1;
	private FrameLayout.LayoutParams params2;
	private PictureCallback pictureCallBack;
	private boolean _isCapturing;
	/**
	 * 判断当前图片是否来源于图库 true 是
	 */
	private boolean isComing = false;
	private boolean getLocation = false;
	private boolean onlayseSuggetsion = true;
	/*
	 * 相机旋转监听的类
	 */
	private CaptureOrientationEventListener _orientationEventListener;
	/*
	 * 图像旋转度
	 */
	private int _rotation;
	/**
	 * onResume 方法只执行一次
	 */
	private boolean ends = true;
	private Dialog dialog;
	private View dialogView;
	public static RecordActivity instace;
	private static int result;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions list_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_dupai) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.default_dupai)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.default_dupai) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheOnDisc(true).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.displayer(new FadeInBitmapDisplayer(500)).build();// 构建完成
	private boolean isNoe = false;
	/**
	 * 截屏成功
	 */
	private final int CART_SCREEN = 10;
	/**
	 * 上传尖叫数据成功
	 */
	private final int SUCCESS_ADD = 88;
	/**
	 * 获取尖叫数据成功
	 */
	private final int SUCCESS_GET_SCREEN = 8888;
	/**
	 * 获取聊天室记录成功
	 */
	private final int CHAT_HISTERY = 6666;
	/**
	 * 接收到数据
	 */
	private final int RECEIVER_SCCESS = 66666;
	/**
	 * 封装后台推送的消息
	 */
	private ArrayList<postMessage> temsMessage = new ArrayList<>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CART_SCREEN:
				cartScreen();
				break;
			case SUCCESS_GET_SCREEN:
				analyseGetScreen((String) msg.obj);
				break;
			case SUCCESS_ADD:
				analyseResultAddScreen((String) msg.obj);
				break;
			case CHAT_HISTERY:
				analyseChatHistery((String)msg.obj);
				break;
			case RECEIVER_SCCESS:
				analyseReceiverMessage((String)msg.obj);
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_orientationEventListener = new CaptureOrientationEventListener(this);
		super.onCreate(savedInstanceState);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		setContentView(R.layout.activity_record);
		instace = this;
		initData();
		setupDevice();
		if (openCamera()) {
			return;
		}
		cameraPreview = (FrameLayout) findViewById(R.id.cameraPreview);
		stopButton = (ImageView) this.findViewById(R.id.stop);
		startButton = (ImageView) this.findViewById(R.id.start);
		bt_record = (RelativeLayout) this.findViewById(R.id.bt_record);
		params1 = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		params2 = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		cameraPreview.addView(preview, params1);

		upView = View.inflate(getApplicationContext(),
				R.layout.layout_record_up, null);
		agreeLogo = (ImageView) upView.findViewById(R.id.iv_agree_logo);
		redLogo = (ImageView) upView.findViewById(R.id.red_logo);
		buleLogo = (ImageView) upView.findViewById(R.id.bule_logo);
		blueNum = (TextView) upView.findViewById(R.id.blue_num);
		redNum = (TextView) upView.findViewById(R.id.red_num);
		agreNum = (TextView) upView.findViewById(R.id.tv_agre_num);
		projress = (SpringProgressView) upView.findViewById(R.id.projress);
		rlSports = (RelativeLayout) upView.findViewById(R.id.rl_sprots);
		rlSports.setVisibility(View.GONE);
		agreeLogo.setVisibility(View.GONE);
		agreNum.setVisibility(View.GONE);
		rel_voice_size = (TextView) upView.findViewById(R.id.rel_voice_size);
		rel_voice_text = (TextView) upView.findViewById(R.id.rel_voice_text);
		location_tv_re = (TextView) upView.findViewById(R.id.location_tv_re);
		location_tv_re.setMaxWidth(GlobalParams.WIN_WIDTH * 2 / 3);
		record_back = (ImageView) findViewById(R.id.record_back);
		energy = (RelativeLayout) upView.findViewById(R.id.music_record);
		rel_voice_view = (RelativeLayout) upView
				.findViewById(R.id.rel_voice_view);
		// 添加到
		int bottom = DensityUtil.dip2px(getApplicationContext(), 117);
		params2.setMargins(0, 0, 0, bottom);
		cameraPreview.addView(upView, params2);

		mBaseVisualizerView = new VisualizerView(this);
		LayoutParams pars = new LayoutParams(LayoutParams.FILL_PARENT,// 宽度
				DensityUtil.dip2px(this, 180)// 高度
		);
		pars.addRule(RelativeLayout.ALIGN_BOTTOM);
		mBaseVisualizerView.setLayoutParams(pars);
		rel_voice_view.addView(mBaseVisualizerView);

		myviesu = new MyVisualizerView(this);
		// myviesu.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,//
		// 宽度
		// (int) (200f * getResources().getDisplayMetrics().density)// 高度
		// ));
		android.widget.RelativeLayout.LayoutParams parmss = new LayoutParams(
				LayoutParams.FILL_PARENT,// 宽度
				LayoutParams.WRAP_CONTENT);
		myviesu.setLayoutParams(parmss);

		energy.addView(myviesu);

		stopButton.setOnClickListener(this);
		startButton.setOnClickListener(this);
		record_back.setOnClickListener(this);
		location_tv_re.setOnClickListener(this);
		upView.findViewById(R.id.location_re).setOnClickListener(this);

		// 录音组件
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize);
		clsOscilloscope = new ClsOscilloscope();
		pictureCallBack = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				releaseCamera();
				_isCapturing = false;
				Bitmap bitmap = null;
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					// 此处就把图片压缩了
					options.inSampleSize = 2;
					options.inPurgeable = true;
					options.inInputShareable = true;
					bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length, options);
					if (null == bitmap) {
						options.inSampleSize = 5;
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length, options);
					}
				} catch (Throwable e) {
				}
				if (null == bitmap) {
					Toast.makeText(RecordActivity.this, "内存不足，保存照片失败！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Matrix matrix = new Matrix();
				matrix.setRotate(result);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);
				if (cameraPreview != null)
					cameraPreview.removeAllViews();
				ImageView showPicture = new ImageView(getApplicationContext());
				// showPicture.setImageBitmap(bitmap);
				Drawable background = new BitmapDrawable(bitmap);
				showPicture.setBackgroundDrawable(background);
				cameraPreview.addView(showPicture, params1);
				cameraPreview.addView(upView, params2);
				if(handler!=null)
					handler.sendEmptyMessage(CART_SCREEN);
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle(); // 回收图片所占的内存
					bitmap = null;
					System.gc(); // 提醒系统及时回收
				}
			}
		};
	}

	@Override
	protected void onResume() {
		if (camera != null && audioRecord != null) {
			startRecording();
		}
		if (isComing) {
			startRecording();
		}
		super.onResume();
		if (ends) {
			ends = false;
			if (camera == null && !isComing) {
				createDialog(1);
			} else if ((audioRecord == null || recBufSize == 0) && !isComing) {
				createDialog(2);
			}
		}
		if (location_tv_re != null) { // location_tv_re ==null说明当前摄像头没哟权限
			if(GlobalParams.MY_LOCATION!=null){							// onCreate 方法没有执行完
				location_tv_re.setText(GlobalParams.MY_LOCATION.getAddress());
			}else{
				location_tv_re.setText("");
			}
			if (getLocation) {
				getLocation = false;
				cameraPreview.removeAllViews();
				addView();
				startRecording();
			}
		}
		if (location_tv_re != null && !isComing && !ends && !isNoe) {
			if (camera == null) {
				cameraPreview.removeAllViews();
				addView();
				startRecording();
			}
		}
		isNoe = false;
		if(GlobalParams.IS_SPORT){
			getScream();
		}
		
//		addScream("80");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 跳转到分享界面
		case R.id.start:
			if ((camera == null || stoprecord) && !isComing)
				return;
			Log.i("wxn", "点击时间：" + time());
			stopRecording();
			bnCaptureClicked();
			break;
		// 获取系统图片
		case R.id.stop:
			// stopRecording();
			Intent pxc = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pxc, GET_PHOTO);
			isNoe = true;
			break;
		case R.id.record_back:
			relaseMemorry();
			break;
		case R.id.location_tv_re:
		case R.id.location_re:
			getLocation = true;
			Intent intent = new Intent(RecordActivity.this,
					BoardingActivity.class);
			intent.putExtra("tag", "地址");
			startActivity(intent);
			// 跳转到地图界面；
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: startRecording
	 * @Description: 启动录音
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void startRecording() {
		clsOscilloscope.baseLine = energy.getHeight() / 2;
		clsOscilloscope.Start(audioRecord, recBufSize, mBaseVisualizerView,
				this, myviesu, rel_voice_size, rel_voice_text);
	}

	/**
	 * 
	 * @Title: stopRecording
	 * @Description:停止录音
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void stopRecording() {
		if (clsOscilloscope != null)
			clsOscilloscope.Stop();
	}

	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		stopRecording();
		releaseCamera();
		instace = null;
		if(tems!=null){
			tems.clear();
			tems = null;
		}
		imageLoader = null;
		GlobalParams.MAX_VOICE = 0;
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		stopRecording();
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		relaseMemorry();
		super.onBackPressed();
	}

	private void relaseMemorry() {
		releaseCamera();
		stopRecording();
		finish();
		if (bmp != null && bmp.isRecycled()) {
			bmp.recycle();
		}
		bmp = null;
		System.gc();
	}

	private ImageView showPicture;
	private FrameLayout.LayoutParams paramsss;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GET_PHOTO:
			cameraPreview.removeAllViews();
			if (resultCode == Activity.RESULT_OK) {
				isComing = true;
				releaseCamera();
				Uri uri = data.getData();
				Cursor cursor = RecordActivity.this.getContentResolver().query(
						uri, null, null, null, null);
				String imgPath;
				if (cursor != null) {
					cursor.moveToFirst();
					// 图片文件路径
					imgPath = cursor.getString(1);
					cursor.close();
				} else {
					imgPath = uri.getPath();
				}
				try {
					imgPath = URLDecoder.decode(imgPath, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				showPicture = new ImageView(getApplicationContext());
				showPicture.setScaleType(ScaleType.CENTER_CROP);
				// Bitmap bitmap = BitmapUtil.loadBitmap(imgPath);
				int degree = ImageUtils.getBitmapDegree(imgPath);
				Bitmap bitmap = readBitMap(imgPath, cameraPreview.getWidth(),
						cameraPreview.getHeight());
				if (degree != 0)
					bitmap = ImageUtils.rotateBitmapByDegree(bitmap, degree);
				Log.i("wxn", "height: " + bitmap.getHeight() + "  "
						+ GlobalParams.WIN_HIGTH);
				showPicture.setImageBitmap(bitmap);
				paramsss = new FrameLayout.LayoutParams(GlobalParams.WIN_WIDTH,
						GlobalParams.WIN_HIGTH);
				// paramsss.setMargins(-(GlobalParams.WIN_HIGTH-GlobalParams.WIN_WIDTH)/2,
				// 0, 0, 0);
				cameraPreview.addView(showPicture, paramsss);
				cameraPreview.addView(upView, params2);
			} else {
				// addView();
				isComing = false;
				if (showPicture != null && paramsss != null) {
					cameraPreview.addView(showPicture, paramsss);
					cameraPreview.addView(upView, params2);
				} else {
					isNoe = false;
				}
				startRecording();
			}
			// bt_record
			break;

		default:
			break;
		}
	}

	private void setupDevice() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			int cameraCount = Camera.getNumberOfCameras();

			if (cameraCount < 1) {
				Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			currentCameraId = 0;
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d(TAG, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	private boolean openCamera() {
		try {
			camera = Camera.open();
			if (camera == null) {
				Log.i("wxn", "空指针。。。");
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		setCameraDisplayOrientation(this, 0, camera);
		preview = new CameraPreview(this, camera);
		_orientationEventListener.enable();
		return false;
	}

	/*
	 * 相机旋转监听的类，最后保存图片时用到
	 */
	private class CaptureOrientationEventListener extends
			OrientationEventListener {
		public CaptureOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			if (null == camera)
				return;
			if (orientation == ORIENTATION_UNKNOWN)
				return;

			orientation = (orientation + 45) / 90 * 90;
			if (android.os.Build.VERSION.SDK_INT <= 8) {
				_rotation = (90 + orientation) % 360;
				return;
			}

			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(currentCameraId, info);

			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				_rotation = (info.orientation - orientation + 360) % 360;
			} else { // back-facing camera
				_rotation = (info.orientation + orientation) % 360;
			}
		}
	}

	private static void setCameraDisplayOrientation(Activity activity,
			int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}

		// LogEx.i("result: " + result);
		camera.setDisplayOrientation(result);
	}

	private View upView;
	private ImageView record_back;
	private RelativeLayout bt_record;
	private Bitmap bmp;

	private void addView() {
		setupDevice();
		openCamera();
		cameraPreview.addView(preview, params1);
		cameraPreview.addView(upView, params2);
	}

	private void releaseCamera() {
		if (camera != null) {
			camera.release(); // release the camera for other applications
			camera = null;
		}

		// if (null != cameraPreview) {
		// cameraPreview.removeAllViews();
		// cameraPreview = null;
		// }
	}

	private void bnCaptureClicked() {
		if (_isCapturing) {
			return;
		}
		_isCapturing = true;
		try {
			camera.takePicture(null, null, pictureCallBack);
		} catch (RuntimeException e) {
			e.printStackTrace();
			_isCapturing = false;
		}
		if (isComing) {
			cartScreen();
		} else {
			// handler.sendEmptyMessageDelayed(0, 500);
		}
	}

	/**
	 * A basic Camera preview class
	 */
	public class CameraPreview extends SurfaceView implements
			SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		@SuppressWarnings("deprecation")
		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, now tell the camera where to draw
			// the preview.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your
			// activity.
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();// 停止预览
				camera.release(); // 释放摄像头资源
				camera = null;
			}
		}

		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
			final double ASPECT_TOLERANCE = 0.05;
			double targetRatio = (double) w / h;
			if (sizes == null)
				return null;

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = h;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
					continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the
			// requirement
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}

			return optimalSize;
		}

		private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
			// final double ASPECT_TOLERANCE = 0.05;
			//
			// if (sizes == null)
			// return null;
			//
			// Size optimalSize = null;
			// int optimalSideLen = 0;
			// double optimalDiffRatio = Double.MAX_VALUE;
			//
			// for (Size size : sizes) {
			//
			// int sideLen = Math.max(size.width, size.height);
			// // LogEx.i("size.width: " + size.width + ", size.height: " +
			// // size.height);
			// boolean select = false;
			// if (sideLen < kPhotoMaxSaveSideLen) {
			// if (0 == optimalSideLen || sideLen > optimalSideLen) {
			// select = true;
			// }
			// } else {
			// if (kPhotoMaxSaveSideLen > optimalSideLen) {
			// select = true;
			// } else {
			// double diffRatio = Math.abs((double) size.width
			// / size.height - targetRatio);
			// if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
			// select = true;
			// } else if (diffRatio < optimalDiffRatio
			// + ASPECT_TOLERANCE
			// && sideLen < optimalSideLen) {
			// select = true;
			// }
			// }
			// }
			//
			// if (select) {
			// optimalSize = size;
			// optimalSideLen = sideLen;
			// optimalDiffRatio = Math.abs((double) size.width
			// / size.height - targetRatio);
			// }
			// }
			if (sizes == null)
				return null;
			int height = 0;
			int index = 0;
			for (int i = 0; i < sizes.size(); i++) {
				if (sizes.get(i).height > height) {
					height = sizes.get(i).height;
					index = i;
				}
			}
			return sizes.get(index);
			// return optimalSize;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			Log.i(TAG, "surfaceChanged format:" + format + ", w:" + w + ", h:"
					+ h);
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			try {
				Camera.Parameters parameters = mCamera.getParameters();

				List<Size> sizes = parameters.getSupportedPreviewSizes();
				Size optimalSize = getOptimalPreviewSize(sizes, w, h);
				parameters
						.setPreviewSize(optimalSize.width, optimalSize.height);
				double targetRatio = (double) w / h;
				sizes = parameters.getSupportedPictureSizes();
				optimalSize = getOptimalPictureSize(sizes, targetRatio);
				parameters
						.setPictureSize(optimalSize.width, optimalSize.height);
				parameters.setRotation(0);
				mCamera.setParameters(parameters);
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
				Log.i(TAG, "Error starting camera preview: " + e.getMessage());
			}
		}
	}

	/**
	 * 获取和保存当前屏幕的截图
	 */
	private void cartScreen() {
		cameraPreview.buildDrawingCache();
		// 允许当前窗口保存缓存信息
		cameraPreview.setDrawingCacheEnabled(true);
		bmp = cameraPreview.getDrawingCache();

		String filepath = CacheManager.getInstance().getCachePath(
				CacheType.CACHE_IMAGE)
				+ System.currentTimeMillis() + ".jpg";
		// 3.保存Bitmap
		try {
			File file = new File(filepath);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 96, fos);
				fos.flush();
				fos.close();
			}
			cameraPreview.destroyDrawingCache();
			addScream();
			Intent intent = new Intent(getApplicationContext(),
					ShareFaceScoreActivity.class);
			intent.putExtra(ShareFaceScoreActivity.filePath, filepath);
			intent.putExtra("type", "record");
			startActivityForResult(intent, GET_PHOTO);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(String path, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// opt.outHeight = height;
		// opt.outWidth = width;
		opt.inSampleSize = 2;
		// 获取资源图片
		InputStream is;
		try {
			is = new FileInputStream(path);
			Bitmap map = BitmapFactory.decodeStream(is, null, opt);
			is.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CustomDialog exitDialog;
	private boolean stoprecord = false;
	private int type;
	/**
	 * 如果是比赛，则暂时 所支持的logo
	 */
	private ImageView agreeLogo;
	/**
	 * 红方标志
	 */
	private ImageView redLogo;
	/**
	 * 蓝方标志
	 */
	private ImageView buleLogo;
	/**
	 * 蓝方支持人数
	 */
	private TextView blueNum;
	/**
	 * 红方支持人数
	 */
	private TextView redNum;
	/**
	 * 进度条
	 */
	private SpringProgressView projress;
	/**
	 * 体育比赛 显示的布局
	 */
	private RelativeLayout rlSports;
	/**
	 * 第多少名支持
	 */
	private TextView agreNum;
	/**
	 * 球队信息
	 */
	private ArrayList<TemMessage> tems;
	/**
	 * 是否尖叫过 为空 则尖叫过
	 */
	private String hasScreamed;
	/**
	 * 自己支持哪个球队
	 */
	private int agreeTemIndex;
	/**
	 * 当前聊天室roomid
	 */
	private String roomId;
	/**
	 * 活动id
	 */
	private String id;

	public void createDialog(int type) {
		this.type = type;
		if (exitDialog != null)
			return;
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
				finish();
			}
		});
		exitDialog.setConfirmTxt("确定");
		if (type == 1) {
			exitDialog.setRemindMessage("拍照权限被拒绝,请打开拍照权限");
		} else {
			stoprecord = true;
			exitDialog.setRemindMessage("录音权限被拒绝,请打开录音权限");
		}
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCanceledOnTouchOutside(false);
		exitDialog.show();
	}

	public void dissmissDialog() {
		if (exitDialog != null && exitDialog.isShowing() && type != 1)
			exitDialog.dismiss();
	}

	public String time() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	private void setDialog() {
		dialogView = LayoutInflater.from(this).inflate(
				R.layout.dialog_record_layout, null);
		dialogView.findViewById(R.id.delte).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog != null && dialog.isShowing())
							dialog.dismiss();
					}
				});
		dialog = new Dialog(this, R.style.Theme_Light_Dialo);
		// 获得dialog的window窗口
		Window window = dialog.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		int lor = DensityUtil.dip2px(getApplicationContext(), 20);
		int top = DensityUtil.dip2px(getApplicationContext(), 15);
		int bot = DensityUtil.dip2px(getApplicationContext(), 5);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		// lp.width = DensityUtil.dip2px(RecordActivity.this, 300);
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去

		window.setAttributes(lp);
		window.getDecorView().setPadding(0, top, 0, bot);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 获取传递过来的数据
	 */
	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			id = intent.getStringExtra("id");
			if (GlobalParams.IS_SPORT) {
				initChat();
				hasScreamed = intent.getStringExtra("hasScreamed");
				agreeTemIndex = intent.getIntExtra("agreeTemIndex", 0);
				roomId = intent.getStringExtra("roomId");
				tems = (ArrayList<TemMessage>) intent
						.getSerializableExtra("tems");
				getChatHistery(roomId);
			}
		}
	}

	/**
	 * 上传当前音量
	 * 
	 * @param data
	 */
	private void addScream() {
		Map<String, String> paramss = new HashMap<String, String>();
		// 如果是比赛 则为球队id 音乐会 则是 活动id
		if (GlobalParams.IS_SPORT && tems != null) {
			paramss.put("acId", tems.get(agreeTemIndex).id); // 比赛id
			// 0：比赛1：音乐会
			paramss.put("type", "0");
			Log.i("wxn", "jianjiaoid :。。。");
		} else {
			paramss.put("acId", ""); 
			paramss.put("type", "1");
			paramss.put("aId", id); // 音乐为活动id
			Log.i("wxn", "jianjiaoid :aaaaaaa");
		}
		paramss.put("aId", id); // 音乐为活动id
		paramss.put("screamDB", GlobalParams.MAX_VOICE+"");
		Log.i("wxn", "jianjiaoid :"+id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_SCREAM, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "jianjiaoid :"+id+" "+data.getNetResultCode());
//						if(handler==null)
//							return;
//						if (data != null && data.getNetResultCode() == 0) {
//							getScreamSequence();
//						}
					}
				}, false, false);
	}

	/**
	 * 获取比赛 时 尖叫数据
	 */
	private void getScream() {
		Map<String, String> paramss = new HashMap<String, String>();
		paramss.put("activityId", id);
		paramss.put("campainId", tems.get(agreeTemIndex).id+""); // 音乐为活动id
		paramss.put("type", "0"); // type=1时为活动id，type=0时为比赛id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_SCREAM, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null){
							return;
						}
						Log.i("wxn", "尖叫说："+data.getObject()+"...."+data.getNetResultCode());
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = SUCCESS_GET_SCREEN;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 解析 获取比赛 尖叫数据
	 * 
	 * @param data
	 */
	private void analyseGetScreen(String data) {
		hasScreamed = null;
		LiveDetialActivity.instance.sethasScreamed();
		if (TextUtils.isEmpty(data)) {
			return;
		}
		try {
			JSONObject obj = new JSONObject(data);
			if (obj.has("sequence")) {
				String sequence = obj.getString("sequence");
				agreNum.setText("No." + sequence);
				agreeLogo.setVisibility(View.VISIBLE);
			}
			if (obj.has("teamData")) {
				String datas = obj.getString("teamData");
				JSONArray arrays = new JSONArray(datas);
				for (int i = 0; i < arrays.length(); i++) {
					String arrs = arrays.getString(i);
					JSONObject arrsObj = new JSONObject(arrs);
					if (arrsObj.has("teamId")) {
						String temId = arrsObj.getString("teamId");
						if (tems != null) {
							for (TemMessage tem : tems) {
								if (tem.teamId.equals(temId)) {
									String screamDB = arrsObj
											.getString("screamDB");
									tem.screamDB = (screamDB);
									break;
								}
							}
						}
					}
				}
			}
			if (tems == null || tems.size() < 2||agreeLogo==null)
				return;
			TemMessage blueTem = tems.get(0);
			TemMessage redTem = tems.get(1);
			imageLoader.displayImage(
					ConstantValue.BASE_IMAGE_URL
							+ tems.get(agreeTemIndex).backgrounding
							+ ConstantValue.IMAGE_END, agreeLogo, list_options);
			imageLoader.displayImage(
					ConstantValue.BASE_IMAGE_URL + redTem.logoUrl
							+ ConstantValue.IMAGE_END, redLogo, list_options);

			imageLoader.displayImage(
					ConstantValue.BASE_IMAGE_URL + blueTem.logoUrl
							+ ConstantValue.IMAGE_END, buleLogo, list_options);

			int blueColor = Color.parseColor("#" + blueTem.temColor);
			int redColor = Color.parseColor("#" + redTem.temColor);
			int[] colors = new int[] { blueColor, redColor };
			projress.setColors(colors);
			int redScreanmDb = TextUtils.isEmpty(redTem.screamDB) ? 0
					: Integer.parseInt(redTem.screamDB);
			int blueScreanmDb = TextUtils.isEmpty(blueTem.screamDB) ? 0
					: Integer.parseInt(blueTem.screamDB);
			projress.setMaxCount(redScreanmDb + blueScreanmDb);
			projress.setCurrentCount(blueScreanmDb);
			blueNum.setText(blueScreanmDb + "dB");
			redNum.setText(redScreanmDb + "dB");
			blueNum.setBackgroundColor(blueColor);
			redNum.setBackgroundColor(redColor);
			rlSports.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(hasScreamed)&&onlayseSuggetsion) {
				if(dialog==null||!dialog.isShowing())
					setDialog();
				onlayseSuggetsion = false;
			} else {
				agreeLogo.setVisibility(View.VISIBLE);
				agreNum.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 解析上传当前音量返回的数据
	 * 
	 * @param obj
	 */
	private void analyseResultAddScreen(String obj) {
		if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(hasScreamed)) {
			return;
		}
		try {
			JSONObject objs = new JSONObject(obj);
			if (objs.has("sequence")) {
				// 将未尖叫的变量置为已尖叫
				hasScreamed = null;
				LiveDetialActivity.instance.sethasScreamed();
				String sequence = objs.getString("sequence");
				agreeLogo.setVisibility(View.VISIBLE);
				agreNum.setVisibility(View.VISIBLE);
				agreNum.setText("No." + sequence);
				LiveDetialActivity.instance.sethasScreamed();
			}
		} catch (Exception e) {
		}
	};
	
	/**
	 * 获取比赛 时 尖叫数据
	 */
	private void getScreamSequence() {
		Map<String, String> paramss = new HashMap<String, String>();
		if(tems!=null&&GlobalParams.IS_SPORT){
			paramss.put("campaignId", tems.get(agreeTemIndex).id+""); // 所支持球队id
			paramss.put("type", "0"); // 0比赛 1 普通活动
		}
		else{
			paramss.put("activityId", id); // 音乐为活动id
			paramss.put("type", "1"); // 0比赛 1 普通活动
		}
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_SCREAM_SEQUENCE, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "获取到判明："+data.getObject()+"结果吗："+data.getNetResultCode());
						if(handler==null){
							return;
						}
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.what = SUCCESS_ADD;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}
	
	
/*	private LetvChatClientManager mChatManager;
    private IChatIOCallback mChatCallback;
	*//**
	 * 
	* @ClassName: ChatCallback 
	*
	* @Description: 乐视聊天室回调函数 
	*
	* @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
	*
	* @author wangxinnian
	* 
	* @date 2016-8-1 下午3:59:39 
	* 
	*
	 *//*
  private class ChatCallback implements IChatIOCallback {

        @Override
        public void onJoinChat(int code, String message, String vtkey) {
            Log.d("chat", "onJoinChat:code==" + code);
        }

        @Override
        public void onExitJoinChat(boolean isExitChatSuccess) {
            Log.d("chat", "onExitJoinChat:" + isExitChatSuccess);
        }

        @Override
        public void onRecevieChatMsg(String json) {
            Log.d("chat", "onRecevieChatMsg:" + json);
            if (handler == null)
				return;
            Message msg = new Message();
			msg.what = RECEIVER_SCCESS;
			msg.obj = json;
			handler.sendMessage(msg);
        }
        @Override
        public void onReceiveBroadcastMsg() {
        	 Log.d("chat", "onReceiveBroadcastMsg:");
        }
    }*/
  
	/**
	 * 解析获取到聊天记录信息
	 * @param data
	 */
	private void analyseChatHistery(String data) {
		if (TextUtils.isEmpty(data)) {
			return;
		}
		try {
			JSONObject objs = new JSONObject(data);
			if (objs.has("data")) {
				JSONObject datas = objs.getJSONObject("data");
				if (datas.has("result")) {
					JSONObject result = datas.getJSONObject("result");
					if (result.has("server")) {
						JSONObject ser = result.getJSONObject("server");
						String server = ser.getString("server");
						// http://10.154.252.32:8010
						if (!TextUtils.isEmpty(server)) {
							String servers = server.substring(7, 21);
							String port = server.substring(22);
							Log.i("wxn", "聊天servers:" + servers + " .... "
									+ port);
							connection(servers,Integer.parseInt(port),roomId);
						}
					}
				}
			}
		} catch (Exception e) {

		}
	}
	/**
	 * 初始化乐视聊天室
	 */
	private void initChat(){
		/*mChatCallback = new ChatCallback();
        mChatManager = new LetvChatClientManager(mChatCallback);
        mChatManager.enableDebugMode(true);*/
	}
	
	private void connection(String testIp,int testPort,String testChatRoomId){
/*		 try {
           mChatManager.registerCallback(mChatCallback);
       } catch (IllegalArgException e) {
           e.printStackTrace();
           Log.i("chat", "连接异常："+e.getMessage());
       }
       if (!mChatManager.isConnected()){
           mChatManager.startConnectServer(testIp, testPort, testChatRoomId, ChatConstant.PHONE_TYPE, "V1.0");
       }*/
	}
	
	/**
	 * 获取聊天记录 从而 获取聊天室ip 和端口号
	 */
	private void getChatHistery(String roomId) {
		NetService netService = NetService.getInStance();
		netService.setParams("roomId", roomId);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_HISTORY);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				if (handler == null)
					return;
				if (result != null && result.getNetResultCode() == 0) {
					Message msg = new Message();
					msg.what = CHAT_HISTERY;
					msg.obj = result.getObject();
					handler.sendMessage(msg);
				}
			}
		});
	}
	/**
	 * 解析接收到的消息
	 * @param message
	 */
	private void analyseReceiverMessage(String message){
		if(TextUtils.isEmpty(message)){
			return;
		}
		try {
			JSONObject obj = new JSONObject(message);
			if(obj.has("message")){
				temsMessage.clear();
				String chatMess = obj.getString("");
				if(!TextUtils.isEmpty(chatMess)&&chatMess.contains("quot;")){
					String newMessage = chatMess.replaceAll("quot;", "\"");
					JSONArray temss = new JSONArray(newMessage);
					for(int i =0;i<temss.length();i++){
						postMessage messbean = new postMessage();
						JSONObject tems = temss.getJSONObject(i);
						if(tems.has("teamId")){
							messbean.temId = tems.getString("teamId");
						}
						if(tems.has("screamDB")){
							messbean.screamDB = tems.getString("screamDB");
						}
						temsMessage.add(messbean);
					}
					//设置界面
					if(tems!=null&&tems.size()>1 && blueNum!=null && redNum!=null){
						TemMessage blueTem = tems.get(0);
						postMessage postm = temsMessage.get(0);
						String blueTemScreen;
						String redTemScreen;
						if(blueTem.teamId.equals(postm.temId)){
							blueTemScreen = postm.screamDB;
							redTemScreen = temsMessage.get(1).screamDB;
						}else{
							blueTemScreen = temsMessage.get(1).screamDB;
							redTemScreen = postm.screamDB;
						}
						
						int redScreanmDb = TextUtils.isEmpty(redTemScreen) ? 0
								: Integer.parseInt(redTemScreen);
						int blueScreanmDb = TextUtils.isEmpty(blueTemScreen) ? 0
								: Integer.parseInt(blueTemScreen);
						projress.setMaxCount(redScreanmDb + blueScreanmDb);
						projress.setCurrentCount(blueScreanmDb);
						blueNum.setText(blueScreanmDb + "dB");
						redNum.setText(redScreanmDb + "dB");
					}
					
					
				}
			}
		} catch (Exception e) {
		}
		
	}
	
	

}
