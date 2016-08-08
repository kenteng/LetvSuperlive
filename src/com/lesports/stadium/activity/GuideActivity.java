package com.lesports.stadium.activity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lesports.stadium.R;
import com.lesports.stadium.fragment.GuideFragment1;
import com.lesports.stadium.fragment.GuideFragment2;
import com.lesports.stadium.fragment.GuideFragment3;
import com.lesports.stadium.fragment.GuideFragment4;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.view.MyVideoView;
/**
 * 
* @ClassName: GuideActivity 
*
* @Description: 引导界面 
*
* @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
*
* @author wangxinnian
* 
* @date 2016-7-19 下午5:19:06 
* 
*
 */
public class GuideActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mWelcomeVp;
    
	private ImageView[] tips;
	
	public static final String VIDEO_NAME = "all_video.mp4";


	private CategoryPageAdapter categoryAdapter;
	
	private ViewGroup group;

	private ImageView iv_skip;

	private MyVideoView videoView;

//	private boolean isFirstIn;
	


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//    	SharedPreferences preferences = getSharedPreferences(
//				"guideFirst", MODE_PRIVATE);
//
//		isFirstIn = preferences.getBoolean("isFirstIn", true);
//		
//		if(!isFirstIn){
//			Intent intent=new Intent();
//				intent.setClass(GuideActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//	
//		}else{
        
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	            Window window = getWindow();
	            window.setFlags(
	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	        }
	        setContentView(R.layout.activity_guide_first);
	//        getSupportActionBar().hide();
	
	        findView();
	
	        initView();
//		}


    }

    private void findView() {

   
    }

    private void initView() {
    	
    	group = (ViewGroup) findViewById(R.id.viewGroup);
    	
    	iv_skip = (ImageView) findViewById(R.id.iv_skip);
    	iv_skip.setOnClickListener(this);
    	
    	videoView = (MyVideoView) findViewById(R.id.videoView);
    	
    	File videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists()) {
            videoFile = copyVideoFile(1);
        }

        playVideo(videoFile);
    	
    	mWelcomeVp = (ViewPager) findViewById(R.id.welcomevp);
    	int size1=DensityUtil.dip2px(this, 10);
    	int size2=DensityUtil.dip2px(this, 10);
    	tips = new ImageView[4];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageview = new ImageView(this);
			
			LayoutParams imageParams = new LayoutParams(size1, size1);
			imageParams.setMargins(size2, 0, size2, 0);
			imageview.setLayoutParams(imageParams);
			tips[i] = imageview;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.dot_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.dot_normal);
			}

			group.addView(imageview);
		}
    	
    	categoryAdapter = new CategoryPageAdapter(getSupportFragmentManager());
    	
        mWelcomeVp.setOnPageChangeListener(new OnPageChangeListener() {

    		@Override
    		public void onPageSelected(int position) {
    			if(position<3){
    				setImageBackground(position);
    			}
   				
    			if(position==3){
    				setGuided();
//    				Intent intent=new Intent();
//	   				intent.setClass(GuideActivity.this, MainActivity.class);
//	   				startActivity(intent);
	   				
    				finish();	
    				videoView.stopPlayback();
   				}

    		}

    		@Override
    		public void onPageScrolled(int arg0, float arg1, int arg2) {
    			if(arg0==2){
   					if(arg1>0.05f){
//   						setGuided();
   	   					
   	   					
   					}
   				}
    		}

    		@Override
    		public void onPageScrollStateChanged(int arg0) {
//    			if(arg0==3){
//   					finish();
//   					Intent intent=new Intent();
//   					intent.setClass(GuideActivity.this, MainActivity.class);
//   					startActivity(intent);
//   				}
    			
    		}
    	});
        
        mWelcomeVp.setAdapter(categoryAdapter);
    
    }
    
    private void playVideo(File videoFile) {
    	videoView.setVideoPath(videoFile.getPath());
    	videoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    	videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
            public void onPrepared(MediaPlayer mediaPlayer) {
               mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }
  

 

    private File copyVideoFile(int i) {
        File videoFile;
        FileOutputStream fos;
        InputStream in;
        try {
    		fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
    		 in = getResources().openRawResource(R.raw.all_video);

            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
        return videoFile;
    }
    
	private void setGuided() {
		SharedPreferences preferences = getSharedPreferences("guideFirst", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}

	


	
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.dot_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.dot_normal);
			}
		}
	}


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(isFirstIn){
        	videoView.stopPlayback();
//        }
//        mVideoView.stopPlayback();
    }

    @SuppressLint("NewApi")
	@Override
    public void onClick(View view) {
       switch (view.getId()) {
       case R.id.iv_skip:
//    	   Intent intent=new Intent();
//    	   intent.setClass(GuideActivity.this, MainActivity.class);
//    	   startActivity(intent);
//    	   setGuided();
    	   finish();
    	   videoView.stopPlayback();
			
    	   break;
	default:
		break;
	}
    }

    enum InputType {
        NONE, LOGIN, SIGN_UP;
    }
    private class CategoryPageAdapter extends FragmentStatePagerAdapter {

	
		public CategoryPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int num) {
			Fragment fragment = null;
			switch (num) {
			case 0:
				fragment = new GuideFragment3();
				break;
			case 1:
				fragment = new GuideFragment2();
				break;
			case 2:
				fragment = new GuideFragment1();
				break;
			case 3:
				fragment = new GuideFragment4();
				break;

			default:
				fragment = new GuideFragment1();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 4;
		}

	}




}
