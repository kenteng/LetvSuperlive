package com.lesports.stadium.fragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.view.MyVideoView;
/**
 * 视频导航页,第三个fragment
 * @author simon
 *
 */
public class GuideFragment3 extends BaseV4Fragment {
	
//	public static final String VIDEO_NAME3 = "guide_three.mp4";
//	private MyVideoView mVideoView3;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=View.inflate(getActivity(), R.layout.guide_fragment3, null);
//		mVideoView3 = (MyVideoView) view.findViewById(R.id.videoView3);
//		 File videoFile = getActivity().getFileStreamPath(VIDEO_NAME3);
//	        if (!videoFile.exists()) {
//	            videoFile = copyVideoFile(1);
//	        }
//
//	        playVideo(videoFile);
		return view;
	}
	
	   @Override
		public void onDestroy() {
		        super.onDestroy();
//		        mVideoView3.stopPlayback();
		    }
	
//	  private void playVideo(File videoFile) {
//	        mVideoView3.setVideoPath(videoFile.getPath());
//	        mVideoView3.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//	        mVideoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//				@Override
//	            public void onPrepared(MediaPlayer mediaPlayer) {
//	               mediaPlayer.setLooping(true);
//	                mediaPlayer.start();
//	            }
//	        });
//	    }

	 

//	    private File copyVideoFile(int i) {
//	        File videoFile;
//	        FileOutputStream fos;
//	        InputStream in;
//	        try {
//        		fos = getActivity().openFileOutput(VIDEO_NAME3, getActivity().MODE_PRIVATE);
//        		 in = getResources().openRawResource(R.raw.guide_three);
//
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
//	        videoFile = getActivity().getFileStreamPath(VIDEO_NAME3);
//	        if (!videoFile.exists())
//	            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
//	        return videoFile;
//	    }

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
