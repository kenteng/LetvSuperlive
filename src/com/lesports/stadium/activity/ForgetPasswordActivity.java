package com.lesports.stadium.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lesports.stadium.R;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.utils.LoginUtil;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


/**
 * ***************************************************************
 * 
 * @ClassName: RegisterActivity
 * 
 * @Desc : 忘记密码
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-23 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *  ***************************************************************
 */
public class ForgetPasswordActivity extends Activity implements OnClickListener {

	/**
	 * 返回
	 */
	private ImageView ivBack;
	
	/**
	 * 获取验证码
	 */
	private TextView tvGetCodes;
	
	/**
	 * 手机号输入框
	 */
	private EditText edtPhone;
	
	/**
	 * 验证码输入框
	 */
	private EditText edtCodes;
	
	/**
	 * 新密码输入框
	 */
	private EditText edtPassword;
	
	/**
	 * 完成
	 */
	private TextView tvChange;
	
	/**
	 * videoView
	 */
//	private VideoView mVideoView;
	
	public static final String VIDEO_NAME = "welcome_video.mp4";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpassword);
		initView();
		initListener();
	}

	private void initView() {
//		mVideoView = (VideoView) findViewById(R.id.videoView);
		
		ivBack=(ImageView)findViewById(R.id.iv_back);
		edtPhone=(EditText)findViewById(R.id.edt_phone);
		edtCodes=(EditText)findViewById(R.id.edt_activation_code);
		edtPassword=(EditText)findViewById(R.id.edt_password);
		tvGetCodes=(TextView)findViewById(R.id.tv_send_code);
		tvChange=(TextView)findViewById(R.id.tv_change);
//		
//		File videoFile = getFileStreamPath(VIDEO_NAME);
//        if (!videoFile.exists()) {
//            videoFile = copyVideoFile();
//        }
//
//        playVideo(videoFile);
	}
	
//	 private File copyVideoFile() {
//	        File videoFile;
//	        try {
//	            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
//	            InputStream in = getResources().openRawResource(R.raw.welcome_video);
//	            byte[] buff = new byte[1024];
//	            int len = 0;
//	            while ((len = in.read(buff)) != -1) {
//	                fos.write(buff, 0, len);
//	            }
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        videoFile = getFileStreamPath(VIDEO_NAME);
//	        if (!videoFile.exists())
//	            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
//	        return videoFile;
//	    }
//	  
//	    private void playVideo(File videoFile) {
//	        mVideoView.setVideoPath(videoFile.getPath());
//	        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//	        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//	            @Override
//	            public void onPrepared(MediaPlayer mediaPlayer) {
//	                mediaPlayer.setLooping(true);
//	                mediaPlayer.start();
//	            }
//	        });
//	    }
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
//	        mVideoView.stopPlayback();
	    }


	
	private void initListener(){
		ivBack.setOnClickListener(this);
		tvGetCodes.setOnClickListener(this);
		tvChange.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_send_code:
			
			break;
		case R.id.tv_change:
			//发送短信找回密码
			LoginUtil.retrievePwdBySMS(ForgetPasswordActivity.this,
					Constants.retrievePwdPhoneNum);
			ForgetPasswordActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
