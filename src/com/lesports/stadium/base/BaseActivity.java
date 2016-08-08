package com.lesports.stadium.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.utils.Utils;
import com.umeng.analytics.MobclickAgent;
/**
 * BaseActivity
 * @author dcc
 *	Activity基类,其中包含消息头的显示隐藏横竖屏控制等功能
 */
public  class BaseActivity extends Activity {
	/**子类布局*/
	private LinearLayout mViewContent;
	/**标题栏整体布局*/
	public View mLayoutTitle;
	/**标题栏返回键*/
	private TextView titleGoBackTv;
	/**标题内容*/
	private TextView titleContentTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		//控制横竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initView();
	}

	/**
	 * 初始化View
	 */
	private  void initView(){
		mViewContent=(LinearLayout)findViewById(R.id.layout_base_center);
		mLayoutTitle=findViewById(R.id.layout_basetitle);
		titleContentTv=(TextView)findViewById(R.id.base_titlecontent_tv);
		titleGoBackTv=(TextView)findViewById(R.id.base_back_textview);
	};
	
	/**
	 * 添加布局内容
	 * @param view
	 */
	public void addView(View view){
		if (view == null) return;
		if (mViewContent != null) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			mViewContent.addView(view,layoutParams);
		}
	}
	/**
	 * 隐藏标题布局
	 * 
	 * @param hidden  true隐藏；false显示。默认隐藏
	 */
	public void hiddenTitleLayout(boolean hidden) {
		if (hidden) {
			mLayoutTitle.setVisibility(View.GONE);
			return;
		}
		mLayoutTitle.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 隐藏标题并传值
	 * @param hidden
	 */
	public void hiddenTittleContent(boolean hidden,String title){
		if(hidden){
			titleContentTv.setVisibility(View.INVISIBLE);
			return;
		}
		titleContentTv.setVisibility(View.VISIBLE);
		if(!Utils.isNullOrEmpty(title)){
			titleContentTv.setText(title);
		}
	}
	/**
	 * 隐藏返回按键
	 * @param hidden
	 */
	public void hiddenGoBack(boolean hidden){
		if(hidden){
			titleGoBackTv.setVisibility(View.INVISIBLE);
			return;
		}
		titleGoBackTv.setVisibility(View.VISIBLE);
	}
	/**
	 * 获取标题返回控件
	 * @return 消息头返回键
	 */
	public TextView getGoBack(){
		return titleGoBackTv;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
