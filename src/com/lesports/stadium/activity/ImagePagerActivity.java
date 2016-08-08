package com.lesports.stadium.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.fragment.ImageDetailFragment;
import com.lesports.stadium.photoview.HackyViewPager;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.imageLoader.ImageFileCache;
import com.lesports.stadium.utils.imageLoader.ImageLoader;
import com.lesports.stadium.view.SharePopupWindow;
import com.letv.adlib.sdk.tasks.GetAdDataTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity implements OnClickListener, PlatformActionListener, Callback{
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	/**
	 * 活动实体类对象
	 */
	private SenceBean mSenceBean;
	
	/**
	 * 传递过来的集锦的id
	 */
	private String mId;
	/**
	 * 数据列表集合
	 */
	private List<HeightLightBean> list_data;
	/**
	 * 定时器
	 */
	private Timer refreshDiscussTimer = new Timer();
	/**
	 * 用于标记刷新界面的时候获取的时间数据
	 */
	private final int TIME_TAG = 2;
	/**
	 * 底部分享按钮
	 */
	private ImageView mBottomShared;
	/**
	 * 底部点赞按钮
	 */
	private ImageView mBottomZan;
	/**
	 * 底部下载按钮
	 */
	private ImageView mBottomDown;
	/**
	 * 当前展示的是第几张图片
	 */
	private int ImagePositon;
	/**
	 * 分享popuwindwo
	 */
	private SharePopupWindow share;
	/**
	 * 顶部返回键
	 */
	private ImageView mBack;
	/**
	 * 顶部分享
	 */
	private ImageView mSharedss;

	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TIME_TAG:
				String backdatatime = (String) msg.obj;
				// ActivityDetailBean beans=jsonParserData(backdatatime);
				// 调用方法，拿出时间数据，将至进行解析
				initDate(backdatatime);
				break;

			default:
				break;
			}

		}

	};
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		initview();
		initget();
		ArrayList<String> urls=new ArrayList<>();
		if(list_data!=null&&list_data.size()>0){
			for(int i=0;i<list_data.size();i++){
				urls.add(list_data.get(i).getFileUrl());
			}
			mPager = (HackyViewPager) findViewById(R.id.pager);
			ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
			mPager.setAdapter(mAdapter);
			indicator = (TextView) findViewById(R.id.indicator);

			CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
			indicator.setText(text);
			// 更新下标
			mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int arg0) {
					ImagePositon=arg0;
					CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
					indicator.setText(text);
				}

			});
			if (savedInstanceState != null) {
				pagerPosition = savedInstanceState.getInt(STATE_POSITION);
			}

			mPager.setCurrentItem(pagerPosition);
		}


	}

	/**
	 * 初始化view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mBottomShared=(ImageView) findViewById(R.id.image_liulan_fenxiang);
		mBottomShared.setOnClickListener(this);
		mBottomZan=(ImageView) findViewById(R.id.image_liulan_dianzan);
		mBottomZan.setOnClickListener(this);
		mBottomDown=(ImageView) findViewById(R.id.image_liulan_xiazai);
		mBottomDown.setOnClickListener(this);
		mBack=(ImageView) findViewById(R.id.new_picture_top_layout_back);
		mBack.setOnClickListener(this);
		mSharedss=(ImageView) findViewById(R.id.new_picture_top_layout_share);
		mSharedss.setOnClickListener(this);
	}

	/**
	 * 用来接收传递过来的数据
	 */
	private void initget() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		String tagss=intent.getStringExtra("isuse");
		if(!TextUtils.isEmpty(tagss)&&tagss.equals("yes")){
			mSenceBean=(SenceBean) intent.getSerializableExtra("bean");
			if(mSenceBean!=null){
				//说明是从为开始界面进入这个界面的，所以这里需要开启定时器，来进行时间计算
				refrenshUI();
			}else{
				
			}
		}
		String tag=intent.getStringExtra("tag");
		if(tag!=null&&tag.equals("height")){
			//说明是从集锦页面跳过的
			mId=intent.getStringExtra("id");
			list_data=(List<HeightLightBean>)intent.getSerializableExtra("list");
//			Log.i("id是多少",mId);
//			Log.i("现在有多少数据",list_data.size()+"");
//			mTextNumRighr.setText(list_data.size()+"");
			int index=0;
			for(int i=0;i<list_data.size();i++){
				if(list_data.get(i).getId().equals(mId)){
					index=i;
				}
			}
			HeightLightBean bean=new HeightLightBean();
			bean=list_data.get(0);
			list_data.set(0, list_data.get(index));
			list_data.set(index, bean);
			
		}
	}
	/**
	 * 该方法用来每隔一分钟刷新一次界面
	 */
	private void refrenshUI() {
		// TODO Auto-generated method stub
		refreshDiscussTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 调用方法，重新获取界面数据，拿出时间数据，进行重新适配加载到UI
				reGetTimeData();
			}
		}, 60000, 60000);
	}
	/**
	 * 重新获取时间数据，进行计时操作
	 */
	private void reGetTimeData() {
		// TODO Auto-generated method stub
		// userUtilsGetDataxstime(id);
		Message msg = new Message();
		msg.arg1 = TIME_TAG;
		msg.obj = mSenceBean.getStarttime();
		handler.sendMessage(msg);

	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
		}

	}
	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * 
	 * @param starttime
	 */
	private void initDate(String starttime) {
		// TODO Auto-generated method stub
		long starttimes = getChangeToTime(starttime);
		Date date = new Date();
		long currenttimes = date.getTime();
		Log.i("当前时间", currenttimes + "");
		Log.i("开始时间", starttimes + "");

		// 调用方法，计算两个时间之间差值
		calculationTime(starttimes, currenttimes);
	}
	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * 
	 * @param str
	 * @return
	 */
	public long getChangeToTime(String str) {
		if (TextUtils.isEmpty(str))
			return 0;
		long time = Long.parseLong(str);
		return time;

	}
	/**
	 * 计算时间差值
	 * 
	 * @param starttimes
	 * @param currenttimes
	 */
	private void calculationTime(long starttimes, long currenttimes) {
		// TODO Auto-generated method stub
		// SimpleDateFormat dfs = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long between = 0;
		try {
			// java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
			// java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
			// between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
			between = (starttimes - currenttimes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
				- min * 60 * 1000 - s * 1000);
		Log.i("当前计算后时间是", day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
				+ "毫秒");
		// System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
		// + "毫秒");
		/**
		 * 设置时间到控件上
		 */
		if(between<=0){
			Intent intent = new Intent(ImagePagerActivity.this,
					LiveDetialActivity.class);
			intent.putExtra("id", mSenceBean.getId());
			intent.putExtra("no", "1");
			intent.putExtra("lable", mSenceBean.getLabel());
			intent.putExtra("url", mSenceBean.getFileUrl());
			//直播源id 用于直播
			intent.putExtra("resourceId", mSenceBean.getResourceId());
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_liulan_dianzan:
			//点赞
			
			break;
		case R.id.image_liulan_fenxiang:
			
			break;
		case R.id.image_liulan_xiazai:
			//下载
			useWayDownLoadImages();
			break;
		case R.id.new_picture_top_layout_back:
			finish();
			break;
		case R.id.new_picture_top_layout_share:
			//分享
			showPop();
			break;

		default:
			break;
		}
	}
	/**
	 * 保存图片到本地
	 */
	private void useWayDownLoadImages() {
		// TODO Auto-generated method stub
		if(list_data!=null&&list_data.size()>0){
			String url=ConstantValue.BASE_IMAGE_URL+list_data.get(ImagePositon).getFileUrl()+ConstantValue.IMAGE_END;
			File file = new ImageFileCache(ImagePagerActivity.this).getFile(url);
			File newFile = new ImageFileCache(ImagePagerActivity.this).getNewFile(url);
			try {
				ImageLoader.CopyStream(new FileInputStream(file), new FileOutputStream(newFile));
				String urls = newFile.getAbsolutePath();
				saveImageToGallery(ImagePagerActivity.this,urls);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 该方法用来下载图片
	 */
	private void useWayDownLoadImage() {
		// TODO Auto-generated method stub
		try {
			if(list_data!=null&&list_data.size()>0){
				String url=ConstantValue.BASE_IMAGE_URL+list_data.get(ImagePositon).getFileUrl()+ConstantValue.IMAGE_END;
				File file = new ImageFileCache(ImagePagerActivity.this).getFile(url);
				File newFile = new ImageFileCache(ImagePagerActivity.this).getNewFile(url);
				ImageLoader.CopyStream(new FileInputStream(file), new FileOutputStream(newFile));
				url = newFile.getAbsolutePath();
				
				// 其次把文件插入到系统图库
			    try {
			        MediaStore.Images.Media.insertImage(this.getContentResolver(),
							file.getAbsolutePath(), newFile.getName(), null);
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    }
			    // 最后通知图库更新
			    this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + url)));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void showPop() {
		if(TextUtils.isEmpty(list_data.get(ImagePositon).getFileUrl())){
			Toast.makeText(getApplicationContext(), "该图片无null，无法分享", 0).show();
			return;
		}
		try{
			share = new SharePopupWindow(this);
			share.setPlatformActionListener(this);
			ShareModel model = new ShareModel();
			String imageurl = ConstantValue.BASE_IMAGE_URL
					+ list_data.get(ImagePositon).getFileUrl()+ ConstantValue.IMAGE_END;
			model.setImageUrl(imageurl);
			model.setUrl(imageurl);
			share.initShareParams(model);
			share.showShareWindow(false);
			// 显示窗口 (设置layout在PopupWindow中显示的位置)
			share.showAtLocation(this.findViewById(R.id.pb_main), Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}catch(Exception e){
		}

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
	/**
	 * 保存图片到本地
	 * @param context
	 * @param bmp
	 */
	public void saveImageToGallery(Context context,String url) {
		Bitmap bmp=BitmapFactory.decodeFile(url);
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "SuperLive");
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
	    Toast.makeText(ImagePagerActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onDestroy() {
		if(refreshDiscussTimer!=null)
			refreshDiscussTimer.cancel();
		if(list_data!=null)
			list_data.clear();
		list_data = null;
		super.onDestroy();
	}
}
