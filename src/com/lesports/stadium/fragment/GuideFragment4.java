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
 * 视频导航页,第四个fragment
 * @author simon
 *
 */
public class GuideFragment4 extends BaseV4Fragment {
	
//	public static final String VIDEO_NAME3 = "guide_three.mp4";
//	private MyVideoView mVideoView3;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=View.inflate(getActivity(), R.layout.guide_fragment4, null);
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
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

}
