package com.lesports.stadium.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.view.CustomDialog;

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
public class FaceScoreActivity extends Activity implements OnClickListener,
		SurfaceHolder.Callback {

	private static final String TAG = "FaceScoreActivity";
	private static final int MEDIA_TYPE_IMAGE = 1;
	private ImageView switchCameraBtn, captureBtn;
	private SurfaceView surfaceSv;

	private SurfaceHolder mHolder;
	private Camera mCamera;
	// 0表示后置，1表示前置
	private int cameraPosition = 1;
	public static int result;
	/**
	 * 调用系统相册获取图片
	 */
	private static final int GET_PHOTO = 2;
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
	private Dialog dialog;
	private View dialogView;
	private int currendid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 不显示标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_face_score);

		findById();
		initData();
		createDialogView();
	}

	/**
	 * 初始化view
	 */
	private void findById() {
		switchCameraBtn = (ImageView) this
				.findViewById(R.id.bnToggleCamera);
		captureBtn = (ImageView) this.findViewById(R.id.bnCapture);
		surfaceSv = (SurfaceView) this.findViewById(R.id.cameraPreview);

		switchCameraBtn.setOnClickListener(this);
		captureBtn.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.bnSelectPicture).setOnClickListener(this);
		flashlight = (ImageView) findViewById(R.id.flashlight);
		flashlight.setOnClickListener(this);
	}

	/**
	 * 初始化相关data
	 */
	private void initData() {
		// 获得句柄
		mHolder = surfaceSv.getHolder();
		// 添加回调
		mHolder.addCallback(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (this.checkCameraHardware(this) && (mCamera == null)) {
			// 打开camera
			mCamera = getCamera();
			if(mCamera==null){
				return;
			}
			setCameraDisplayOrientation(this, 1, mCamera);
			if (mHolder != null) {
				setStartPreview(mCamera, mHolder);
			}
		}
	}

	private Camera getCamera() {
		Camera camera = null;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			camera = null;
		}
		return camera;
	}

	@Override
	public void onPause() {
		super.onPause();
		/**
		 * 记得释放camera，方便其他应用调用
		 */
		releaseCamera();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 释放mCamera
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();// 停掉原来摄像头的预览
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bnToggleCamera:
			// 切换前后摄像头
			int cameraCount = 0;
			CameraInfo cameraInfo = new CameraInfo();
			cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
				if (cameraPosition == 1) {
					// 现在是后置，变更为前置
					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
						/**
						 * 记得释放camera，方便其他应用调用
						 */
						releaseCamera();
						// 打开当前选中的摄像头
						mCamera = Camera.open(i);
						if(mCamera==null){
							if(exitDialog!=null&&!exitDialog.isShowing())
								exitDialog.show();
							return;
						}
						setCameraDisplayOrientation(FaceScoreActivity.this, i,
								mCamera);
						// 通过surfaceview显示取景画面
						setStartPreview(mCamera, mHolder);
						cameraPosition = 0;
						currendid = 1;
						flashlight.setImageResource(R.drawable.flash_light_close);
						break;
					}
				} else {
					// 现在是前置， 变更为后置
					if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
						/**
						 * 记得释放camera，方便其他应用调用
						 */
						releaseCamera();
						mCamera = Camera.open(i);
						if(mCamera==null){
							if(exitDialog!=null&&!exitDialog.isShowing())
								exitDialog.show();
							return;
						}
						setCameraDisplayOrientation(FaceScoreActivity.this, i,
								mCamera);
						setStartPreview(mCamera, mHolder);
						cameraPosition = 1;
						currendid = 0;
						flashlight.setImageResource(R.drawable.flash_light_close);
						break;
					}
				}

			}
			break;
		case R.id.bnCapture:
			// 拍照,设置相关参数
			flashlight.setImageResource(R.drawable.flash_light_close);
			Camera.Parameters params = mCamera.getParameters();
//			params.setPictureFormat(ImageFormat.JPEG);
//			params.setPreviewSize(800, 400);
//			int widnt = 0;
//			int height =0;
//			List<Size> list = params.getSupportedPreviewSizes();
//			if(list!=null&&list.size()>0){
//				for(Size size:list){
//					if(size.width>widnt){
//						widnt = size.width;
//						height = size.height;
//					}
//				}
//				params.setPreviewSize(widnt, height);
//			}
				
			// 自动对焦
			params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
//			mCamera.setParameters(params);
			mCamera.takePicture(null, null, picture);
			break;
		case R.id.bnSelectPicture:
			// 打开本地相册
			Intent pxc = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(pxc, GET_PHOTO);
			break;
		case R.id.flashlight:
			if(currendid ==1)
				return;
			Camera.Parameters camParmeters = mCamera.getParameters();
			camParmeters.setFlashMode(Parameters.FLASH_MODE_ON);
			mCamera.setParameters(camParmeters);
			flashlight.setImageResource(R.drawable.flash_light);
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
		}
	}
	@Override
	protected void onResume() {
		currendid = 0;
		super.onResume();
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
	
	private void releaseMemmorry() {
		releaseCamera();
		finish();
		System.gc();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(mCamera==null){
			if(exitDialog==null)
				 createDialog();
		}
		setStartPreview(mCamera, mHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
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

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		setStartPreview(mCamera, mHolder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 当surfaceview关闭时，关闭预览并释放资源
		/**
		 * 记得释放camera，方便其他应用调用
		 */
		releaseCamera();
		holder = null;
		surfaceSv = null;
	}

	/**
	 * 创建png图片回调数据对象
	 */
	PictureCallback picture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
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
				if (currendid != 0)
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
	private ImageView flashlight;

	/**
	 * Create a File for saving an image or video
	 */
	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".png");
		} else {
			return null;
		}
		return mediaFile;
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/**
	 * activity返回式返回拍照图片路径
	 * 
	 * @param mediaFile
	 */
	private void returnResult(File mediaFile) {
		Intent intent = new Intent();
		intent.setData(Uri.fromFile(mediaFile));
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	/**
	 * 设置camera显示取景画面,并预览
	 * 
	 * @param camera
	 */
	private void setStartPreview(Camera camera, SurfaceHolder holder) {
		if(camera==null||holder==null)
			return;
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
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
		camera.setDisplayOrientation(result);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		currendid = 0;
		switch (requestCode) {
		case GET_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				Cursor cursor = FaceScoreActivity.this.getContentResolver()
						.query(uri, null, null, null, null);
				cursor.moveToFirst();
				final String imgPath = cursor.getString(1); // 图片文件路径
				cursor.close();
				Intent intent = new Intent(getApplicationContext(),
						ShareFaceScoreActivity.class);
				intent.putExtra(ShareFaceScoreActivity.filePath, imgPath);
				startActivity(intent);
//				releaseCamera();
			}
			break;

		default:
			break;
		}
	}
	private CustomDialog exitDialog;
	private void createDialog() {
		if(exitDialog!=null)
			return;
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
				finish();
			}
		});
		exitDialog.setConfirmTxt("确定");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("拍照权限被拒绝，请为该应用打开拍照权限");
		exitDialog.show();
	}
}
