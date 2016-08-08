package com.lesports.stadium.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseV4Fragment;
/**
 * 视频导航页,第一个fragment
 * @author simon
 *
 */
public class GuideFragment1 extends BaseV4Fragment {
	
//	public static final String VIDEO_NAME = "guide_one.mp4";
//	private MyVideoView mVideoView1;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=View.inflate(getActivity(), R.layout.guide_fragment1, null);
//		mVideoView1 = (MyVideoView) view.findViewById(R.id.videoView1);
//		 File videoFile = getActivity().getFileStreamPath(VIDEO_NAME);
//	        if (!videoFile.exists()) {
//	            videoFile = copyVideoFile(1);
//	        }
//
//	        playVideo(videoFile);
		return view;
	}
//	
//	  private void playVideo(File videoFile) {
//	        mVideoView1.setVideoPath(videoFile.getPath());
//	        mVideoView1.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//	        mVideoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//				@Override
//	            public void onPrepared(MediaPlayer mediaPlayer) {
//	               mediaPlayer.setLooping(true);
//	                mediaPlayer.start();
//	            }
//	        });
//	    }
//	  
	   @Override
	public void onDestroy() {
	        super.onDestroy();
//	        mVideoView1.stopPlayback();
	    }
	 

//	    private File copyVideoFile(int i) {
//	        File videoFile;
//	        FileOutputStream fos;
//	        InputStream in;
//	        try {
//        		fos = getActivity().openFileOutput(VIDEO_NAME, getActivity().MODE_PRIVATE);
//        		 in = getResources().openRawResource(R.raw.guide_one);
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
//	        videoFile = getActivity().getFileStreamPath(VIDEO_NAME);
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
