package com.lesports.stadium.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.engine.CaptureSensorsObserver;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CameraCropBorderView;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: ActivityFaceScore
 * 
 * @Desc : 我要上颜值 界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-14 下午2:13:46
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class FaceScoreActivity1 extends Activity implements
		View.OnClickListener, CaptureSensorsObserver.RefocuseListener {
	/*
	 * 切换前后摄像头控件
	 */
	private ImageView bnToggleCamera;
	/*
	 * 按钮控件
	 */
	private ImageView bnCapture, bnSelectPicture;
	/*
	 * 针布局控件
	 */
	private FrameLayout framelayoutPreview;
	/*
	 * 显示视图控件
	 */
	private CameraPreview preview;
	/*
	 * 自定义边框控件
	 */
	private CameraCropBorderView cropBorderView;
	/*
	 * Camera对象
	 */
	private Camera camera;
	/*
	 * 回调对象
	 */
	private PictureCallback pictureCallBack;
	/*
	 * 监听事件传感器
	 */
	private CaptureSensorsObserver observer;
	/*
	 * 当前摄像头
	 */
	private int currentCameraId;
	/*
	 * 当前手机摄像头数
	 */
	private int frontCameraId;
	/*
	 * 是否开始拍照
	 */
	private boolean _isCapturing;
	/*
	 * 相机旋转监听的类
	 */
	private CaptureOrientationEventListener _orientationEventListener;
	/*
	 * 图像旋转度
	 */
	private int _rotation;
	/*
	 * 图片最大尺寸
	 */
	public static final int kPhotoMaxSaveSideLen = 2600;
	/**
	 * 调用系统相册获取图片
	 */
	private static final int GET_PHOTO = 2;
	/*
	 * log标识
	 */
	final static String TAG = "capture";
	/*
	 * 是否开启闪灯
	 */
	private boolean flashStart = false;
	private ImageView flashlight;
	private static int result;
	private Dialog dialog;
	private View dialogView;
	private int type;
	/**
	 * 参与规则
	 */
	private final int TAKE_PART_RULE = 100;
	/**
	 * 中奖
	 */
	private final int WIN_PRIZE = 110;
	/**
	 * 未中奖
	 */
	private final int NO_WIN_PRIZE = 120;
	/**
	 * 没有机会
	 */
	private final int NO_CHANCE = 130;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face_score);
		observer = new CaptureSensorsObserver(this);
		_orientationEventListener = new CaptureOrientationEventListener(this);
		getViews();
		setListeners();
		setupDevice();
		createDialogView();
//		setDialog();
	}

	private void createDialogView() {
		switch (type) {
		case TAKE_PART_RULE:
			//参与规则界面
			dialogView = LayoutInflater.from(this).inflate(
					R.layout.pop_facehint, null);
			dialogView.findViewById(R.id.delte).setOnClickListener(this);
			dialogView.findViewById(R.id.takepart).setOnClickListener(this);
			break;
		case WIN_PRIZE:
			//中奖界面
			dialogView = LayoutInflater.from(this).inflate(
					R.layout.pop_win_prize, null);
			break;
		case NO_WIN_PRIZE:
			//未中奖界面
			break;
		case NO_CHANCE:
			//没有机会界面
			break;

		default:
			break;
		}
	}

	private void setDialog() {
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
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		// 将设置好的属性set回去
		window.setAttributes(lp);
		window.getDecorView().setPadding(lor, top, lor, bot);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			releaseMemmorry();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if (null != observer) {
			observer.setRefocuseListener(null);
			observer = null;
		}
		_orientationEventListener = null;

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera(); // release the camera immediately on pause event

		observer.stop();
		_orientationEventListener.disable();
		System.gc();
	}

	@Override
	protected void onResume() {
		// 提示参与规则的dialog界面
		if (GlobalParams.IS_SHOWFACE_HINT && dialog != null
				&& !dialog.isShowing()) {
			dialog.show();
			GlobalParams.IS_SHOWFACE_HINT = false;
		}
		openCamera();
		super.onResume();
	}

	private void releaseMemmorry() {
		releaseCamera();
		finish();
		System.gc();
	}

	/*
	 * 释放Camer资源
	 */
	private void releaseCamera() {
		if (camera != null) {
			camera.release(); // release the camera for other applications
			camera = null;
		}

		if (null != preview) {
			framelayoutPreview.removeAllViews();
			preview = null;
		}
	}

	/*
	 * 查找控件 打开摄像头
	 */
	protected void getViews() {
		bnToggleCamera = (ImageView) findViewById(R.id.bnToggleCamera);
		flashlight = (ImageView) findViewById(R.id.flashlight);
		bnCapture = (ImageView) findViewById(R.id.bnCapture);
		bnSelectPicture = (ImageView) findViewById(R.id.bnSelectPicture);
		framelayoutPreview = (FrameLayout) findViewById(R.id.cameraPreview);
	}

	/*
	 * 设置监听
	 */
	protected void setListeners() {
		findViewById(R.id.back).setOnClickListener(this);
		bnToggleCamera.setOnClickListener(this);
		flashlight.setOnClickListener(this);
		bnCapture.setOnClickListener(this);
		bnSelectPicture.setOnClickListener(this);
		observer.setRefocuseListener(this);
		pictureCallBack = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				_isCapturing = false;
				String imageSt = CacheManager.getInstance().getCachePath(
						CacheType.CACHE_IMAGE);
				File photoFile = new File(imageSt + System.currentTimeMillis()
						+ ".png");
				try {
					FileOutputStream outputStream = new FileOutputStream(
							photoFile);
					outputStream.write(data);
					outputStream.close();
					data = null;
					Intent intent = new Intent(getApplicationContext(),
							ShareFaceScoreActivity.class);
					intent.putExtra(ShareFaceScoreActivity.filePath,
							photoFile.getAbsolutePath());
					if (currentCameraId != 0)
						result = 360 - result;
					intent.putExtra("rotateResult", result + "");
					startActivity(intent);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
				data = null;
				Toast.makeText(getApplicationContext(), "内存不足，请稍后拍摄", 0).show();

			}
		};

	}

	/*
	 * 设置摄像头设备
	 */
	private void setupDevice() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			int cameraCount = Camera.getNumberOfCameras();

			if (cameraCount < 1) {
				Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else if (cameraCount == 1) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			} else {
				bnToggleCamera.setVisibility(View.VISIBLE);
			}

			currentCameraId = 0;
			frontCameraId = findFrontFacingCamera();
			if (-1 == frontCameraId) {
				bnToggleCamera.setVisibility(View.INVISIBLE);
			}
		}
	}

	/*
	 * 打开摄像头
	 */
	private void openCamera() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			try {
				camera = Camera.open(currentCameraId);
			} catch (Exception e) {
				Toast.makeText(this, R.string.camera_open_fail,
						Toast.LENGTH_SHORT).show();
				finish();
				return;
			}

		} else {
			try {
				camera = Camera.open();
			} catch (Exception e) {
				Toast.makeText(this, R.string.camera_open_fail,
						Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
		}
		// camera.setDisplayOrientation(90);
		setCameraDisplayOrientation(this, currentCameraId, camera);
		Camera.Parameters camParmeters = camera.getParameters();
		camParmeters.setFocusMode("macro");
		camera.setParameters(camParmeters);
//		List<Size> sizes = camParmeters.getSupportedPreviewSizes();
		preview = new CameraPreview(this, camera);
		// cropBorderView = new CameraCropBorderView(this);
		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
		// FrameLayout.LayoutParams.MATCH_PARENT,
		// FrameLayout.LayoutParams.MATCH_PARENT);
		params1.gravity = Gravity.CENTER;
		framelayoutPreview.addView(preview, params1);
		// framelayoutPreview.addView(cropBorderView, params2);
		observer.start();
		_orientationEventListener.enable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bnToggleCamera:
			switchCamera();
			break;
		case R.id.bnCapture:
			bnCaptureClicked();
			break;
		case R.id.bnSelectPicture:
			// 打开本地相册
			Intent pxc = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pxc, GET_PHOTO);
			break;
		case R.id.flashlight:
			Camera.Parameters camParmeters = camera.getParameters();
			camParmeters.setFlashMode(Parameters.FLASH_MODE_ON);
			camera.setParameters(camParmeters);
			break;
		case R.id.back:
			releaseMemmorry();
			break;
		case R.id.delte:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			releaseMemmorry();
			break;
		case R.id.takepart:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			break;
		default:
			break;
		}
	}

	/*
	 * 切换前后摄像头
	 */
	private void switchCamera() {
		if (currentCameraId == 0) {
			currentCameraId = frontCameraId;
		} else {
			currentCameraId = 0;
		}
		releaseCamera();
		openCamera();
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GET_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				releaseCamera();
				Uri uri = data.getData();
				Cursor cursor = FaceScoreActivity1.this.getContentResolver()
						.query(uri, null, null, null, null);
				cursor.moveToFirst();
				final String imgPath = cursor.getString(1); // 图片文件路径
				cursor.close();
				Intent intent = new Intent(getApplicationContext(),
						ShareFaceScoreActivity.class);
				intent.putExtra(ShareFaceScoreActivity.filePath, imgPath);
				startActivity(intent);
			}
			break;

		default:
			break;
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
			final double ASPECT_TOLERANCE = 0.05;

			if (sizes == null)
				return null;

			Size optimalSize = null;
			int optimalSideLen = 0;
			double optimalDiffRatio = Double.MAX_VALUE;

			for (Size size : sizes) {

				int sideLen = Math.max(size.width, size.height);
				boolean select = false;
				if (sideLen < kPhotoMaxSaveSideLen) {
					if (0 == optimalSideLen || sideLen > optimalSideLen) {
						select = true;
					}
				} else {
					if (kPhotoMaxSaveSideLen > optimalSideLen) {
						select = true;
					} else {
						double diffRatio = Math.abs((double) size.width
								/ size.height - targetRatio);
						if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
							select = true;
						} else if (diffRatio < optimalDiffRatio
								+ ASPECT_TOLERANCE
								&& sideLen < optimalSideLen) {
							select = true;
						}
					}
				}

				if (select) {
					optimalSize = size;
					optimalSideLen = sideLen;
					optimalDiffRatio = Math.abs((double) size.width
							/ size.height - targetRatio);
				}
			}

			return optimalSize;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

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
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
			}
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
				cameraId = i;
				break;
			}
		}
		return cameraId;
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
		camera.setDisplayOrientation(result);
	}

	@Override
	public void needFocuse() {

		if (null == camera || _isCapturing) {
			return;
		}

		camera.cancelAutoFocus();
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
}
