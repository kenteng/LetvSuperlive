package com.lesports.stadium.view;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 显示在中间容器的界面的基类
 * 可以使用注解加载布局 @ResID(id = R.layout.q_main_pager) 
 * 代替findViewByid    @ResID(id=R.id.q_message_fc)
 * 如果不使用注解 请在init()方法中初始化 布局和控件
 *   使用showInMiddle 用来接收显示的布局
 * 
 * 
 * @author Administrator
 * 
 */
public abstract class BaseView extends Activity implements View.OnClickListener {
	protected Context context;
	protected Bundle bundle;
	protected ViewGroup showInMiddle;
	protected ImageLoader imageLoader1;

	public BaseView(Context context, Bundle bunlde) {
		super();
		this.context = context;
		this.bundle = bunlde;
		showInMiddle = initField(this,context);
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(context));
		initBaseData();
		initData();
        initView();
        initListener();
	}
	public BaseView(Context context) {
		super();
		this.context = context;
		showInMiddle = initField(this,context);
		imageLoader1 = ImageLoader.getInstance();
		imageLoader1.init(ImageLoaderConfiguration.createDefault(context));
		initBaseData();	
        initView();
        initData();
        initListener();
	}

	/**
	 * 初始化基础数据
	 */
	public abstract void initBaseData();
	
	/**
	 * 初始化View
	 */
	public abstract void initView();
	
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	
	
	/**
	 * 初始化监听
	 */
	public abstract void initListener();

	/**
	 * 为中间容器提供需要显示的内容
	 * 
	 * @return
	 */
	public View getView() {

		if (showInMiddle.getLayoutParams() == null) {
			showInMiddle.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		}
		return showInMiddle;
	}


	/**
	 * 界面显示时调用
	 */
	public void onResume() {
	}

	/**
	 * 即将推出界面时调用 
	 */
	public void onPause() {
	}

	@Override
	public void onClick(View v) {
	}

	public View findViewById(int resId) {
		return showInMiddle.findViewById(resId);
	}

	
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}


	
	/**
	 * 注解实现findViewById和setContentView
	 */
	/**
	 * 核心：利用反射，给指定变量设置值。
	 * 
	 * @param ui
	 * @param context
	 * @return
	 */
	private static ViewGroup initField(BaseView ui,Context context) {
		ViewGroup showview;
		try {
			// 获取标记在BaseUI上的类型为ResID的注解，如果BaseUI上没有注解，抛异常
			ResID resID = ui.getClass().getAnnotation(ResID.class);
			if (resID != null) {
				// 解析布局文件
				showview = (ViewGroup) View.inflate(context, resID.id(), null);
			} else {
				throw new RuntimeException("需要指定布局文件对应的资源ID");
			}
			// 反射获取BaseUI类中所有全局变量
			Field[] fields = ui.getClass().getDeclaredFields();
			for (Field item : fields) {
				// 暴力反射，获取私有变量
				item.setAccessible(true);
				// 获取变量上类型为ResID的注解
				ResID annotation = item.getAnnotation(ResID.class);
				if (annotation != null) {
					// 获取id属性
					int id = annotation.id();
					// 给有注解的属性设置值
					item.set(ui, showview.findViewById(id));
				}
			}
			return showview;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
