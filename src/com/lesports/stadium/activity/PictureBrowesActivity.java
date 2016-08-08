package com.lesports.stadium.activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import com.lesports.stadium.R;
import com.lesports.stadium.bean.ActivityDetailBean;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.coverflow.CoverFlowAdapter;
import com.lesports.stadium.coverflow.FeatureCoverFlow;
import com.lesports.stadium.coverflow.GameEntity;
import com.lesports.stadium.fragment.ActivityFragment;
import com.lesports.stadium.picture.BaseAdapterHelper;
import com.lesports.stadium.picture.Bean;
import com.lesports.stadium.picture.Gallery;
import com.lesports.stadium.picture.QuickPagerAdapter;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.ImageUtils;
import com.lesports.stadium.utils.imageLoader.ImageFileCache;
import com.lesports.stadium.utils.imageLoader.ImageLoader;
import com.lesports.stadium.view.SharePopupWindow;
import com.loc.r;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 图片预览界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
@SuppressLint("NewApi")
public class PictureBrowesActivity extends Activity implements OnClickListener, PlatformActionListener, Callback{
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部标题
	 */
	private TextView mTitleNum;
	/**
	 * 数据列表集合
	 */
	private List<HeightLightBean> list_data;
	/**
	 * 传递过来的集锦的id
	 */
	private String mId;
	/**
	 * 分享popuwindwo
	 */
	private SharePopupWindow share;
	
	private List<View> dots; // 图片标题正文的那些点
	private Gallery gallery;
	 private LinearLayout group;
	 QuickPagerAdapter<Bean> mQuickPagerAdapter;
	    List<Bean> data;
	private int currentIndex;
	public static PictureBrowesActivity instance;
	/**
	 * 左边数字
	 */
	public TextView mTextNumleft;
	/**
	 * 右边数字
	 */
	private TextView mTextNumRighr;
	/**
	 * 活动实体类对象
	 */
	private SenceBean mSenceBean;
	/**
	 * 定时器
	 */
	private Timer refreshDiscussTimer = new Timer();
	/**
	 * 用于标记刷新界面的时候获取的时间数据
	 */
	private final int TIME_TAG = 2;
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_picturebrowes);
		ShareSDK.initSDK(this);
		instance=this;
		initview();
		initget();
		//这里开始准备数据
		if(list_data!=null&&list_data.size()!=0){
			 data = new ArrayList<Bean>();
			for(int i=0;i<list_data.size();i++){
				HeightLightBean bean=list_data.get(i);
				data.add(new Bean(bean.getFileUrl(),i));
			}
		}
		mQuickPagerAdapter = new QuickPagerAdapter<Bean>(this, R.layout.gallery_item_layout, data) {

			@Override
			protected void convertView(BaseAdapterHelper helper, final Bean item,
					int positions) {
				// TODO Auto-generated method stub
				 helper.setImageResource(R.id.leshi_tupianyulan_imageview, item.getImgResId());
//	                helper.setText(R.id.textview, getString(item.getStrResId()));
//	                helper.setImageOnClickListener(R.id.leshi_tupianyulan_imageview, new View.OnClickListener() {
//	                    @Override
//	                    public void onClick(View v) {
//	                        Toast.makeText(getBaseContext(), "测试", Toast.LENGTH_SHORT).show();
//	                    }
//	                });
	                Log.i("下表是",positions+"");
//	                changeDots(item.getItems());
			}
        };
        gallery.setAdapter(mQuickPagerAdapter);
      //加载底部的点点
        dots = new ArrayList<View>();// 计算需要生成的表示点数量
        group=(LinearLayout) findViewById(R.id.tupian_goods_layout_addview);
        //循环往该布局中添加控件
        for (int i = 0; i < data.size(); i++) {
			TextView tv=new TextView(this);
	         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25,25); 
	         lp.setMargins(5,0, 5,0);       
	         tv.setLayoutParams(lp);
	         if(i==0){
	        	 tv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.page_indicator_focused));
	         }else{
	        	 tv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.page_indicator_unfocused)); 
	         }
	        currentIndex=0;
			group.addView(tv);
			dots.add(tv);
		}
	}
	
	public void changeDots(int positions) {
		Log.i("wxn", "当前选中的是："+positions);
		currentIndex = positions;
        for(int i=0;i<dots.size();i++){
        	TextView tv=(TextView) dots.get(i);
        	if(i==positions){
        		tv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.page_indicator_focused));
        	}else{
        		tv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.page_indicator_unfocused));
        	}
        }
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
			Log.i("id是多少",mId);
			Log.i("现在有多少数据",list_data.size()+"");
			mTextNumRighr.setText(list_data.size()+"");
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
			Intent intent = new Intent(PictureBrowesActivity.this,
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


	/**
	 * 初始化view'
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mTextNumleft=(TextView) findViewById(R.id.picture_top_layout_num_left);
		mTextNumRighr=(TextView) findViewById(R.id.picture_top_layout_num_right);
		gallery = (Gallery) findViewById(R.id.leshi_tupianyulan_gallery);
		mBack=(ImageView) findViewById(R.id.picture_top_layout_back);
		mBack.setOnClickListener(this);
		mTitleNum=(TextView) findViewById(R.id.picture_top_layout_num);
		findViewById(R.id.picture_top_layout_share).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.picture_top_layout_back:
			finish();
			break;
		case R.id.picture_top_layout_share:
			showPop();
		default:
			break;
		}
	}
	
	private void showPop() {
		if(TextUtils.isEmpty(data.get(currentIndex).getImgResId())){
			Toast.makeText(getApplicationContext(), "该图片无null，无法分享", 0).show();
			return;
		}
		try{
			share = new SharePopupWindow(this);
			share.setPlatformActionListener(this);
			ShareModel model = new ShareModel();
			String imageurl = ConstantValue.BASE_IMAGE_URL
					+ data.get(currentIndex).getImgResId() + ConstantValue.IMAGE_END;
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
	protected void onDestroy() {
		instance = null;
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		if(list_data!=null)
		{
			list_data.clear();
			list_data = null;
		}
		if(dots!=null){
			dots.clear();
			dots = null;
		}
		if(data!=null){
			data.clear();
			data = null;
		}
		if(refreshDiscussTimer!=null){
			refreshDiscussTimer.cancel();
		}
		share = null;
		mSenceBean = null;
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

}
