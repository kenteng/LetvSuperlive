package com.lesports.stadium.app;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap; 
import android.os.Bundle;
import android.os.IBinder;
//import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lecloud.sdk.config.LeCloudPlayerConfig;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.activity.UserCarActivity;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.FileUtil;
import com.lesports.stadium.crash.CrashHandler;
import com.lesports.stadium.dao.DBBuyCar;
import com.lesports.stadium.dao.DBHelper;
import com.lesports.stadium.dao.FoodBuyCar;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.service.AppStatusService;
import com.lesports.stadium.sharedpreference.LoginSpManager;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.utils.imageLoader.ImageLoader;

/**
 * 
 * @ClassName: LApplication
 * @Description:项目运行时初始化一些全局变量 ，服务
 * @author: 王新年
 * @date: 2015-12-28 下午5:19:01
 * MultiDexApplication
 */
public class LApplication extends Application {
	/**
	 * application的实例
	 */
	public static LApplication instance;
	/**
	 * 存放的Activity的集合
	 */
	private List<Activity> mList = new LinkedList<Activity>();
	/**
	 * 当前的Activity
	 */
	private Activity topActivity;
	/**
	 * 上下文
	 */
	public static Context context;
	/**
	 * 加载图片
	 */
	public static ImageLoader loader;
	/**
	 * 购物车数据库对象
	 */
	public static DBBuyCar dbBuyCar;
	/**
	 * 餐饮购物车数据库对象
	 */
	public static FoodBuyCar foodbuycar;
	/**
	 * 用车订单
	 */
	public static String yidaoOrderId = "";
	/**
	 * windowManger 对象
	 */
	private WindowManager mWindowManager;
	/**
	 * 参数
	 */
	private WindowManager.LayoutParams param;
	/**
	 * 显示广告的布局
	 */
	private View guangGaoView;
	/**
	 * 显示广告图片
	 */
	private String guangGaoUrl;
	private Intent service;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		instance = this;
		LeCloudPlayerConfig.init(getApplicationContext());
		DBHelper.addBean("RoundGoodsBean");
		DBHelper.addBean("ServiceCateringDetailBean");
		dbBuyCar = new DBBuyCar(getApplicationContext());
		foodbuycar = new FoodBuyCar(getApplicationContext());
		cleanCacheFile();
		initDate();
		initService();
		startService();
		checkIsEfective();
	}
	//判断tokkens是否有效
	private void checkIsEfective() {
		if(TextUtils.isEmpty(GlobalParams.SSO_TOKEN))
			return;
		Map<String, String> params = new HashMap<String, String>();
		params.put("tk",GlobalParams.SSO_TOKEN);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SSO_TOKEN, params, new GetDatas() {
					
					@Override
					public void getServerData(BackData data) {
						if(data!=null&&data.getNetResultCode()==0){
							Log.i("wxn", "token ..."+data.getObject());
							try{
								JSONObject obj=new JSONObject((String)data.getObject());
								if(obj.has("errorCode")){
									String errorCode = obj.getString("errorCode");
									if("0".equals(errorCode)){
										return;
									}
								}
							}catch (Exception e){
							}
						}
						clearData();
					}
				},false,false);
	}
	/**
	 * 清空数据
	 */
	private  void clearData(){
		GlobalParams.NICK_NAME = "";
		GlobalParams.USER_GENDER = "";
		GlobalParams.USER_HEADER = "";
		GlobalParams.USER_NAME = "";
		GlobalParams.USER_ID = "";
		GlobalParams.USER_PHONE = "";
		GlobalParams.USER_INTEGRAL = 0;
		GlobalParams.SSO_TOKEN = "";
		SharedPreferencesUtils.clear(this, "ls_user_message");
		LoginSpManager.logout(this);
	}

	/**
	 * 启动监听用来判断当前应用是否在前后运行
	 */
	private void startService(){
		service = new Intent(this, AppStatusService.class);
		startService(service);
	}


	/**
	 * 
	 * @Title: initDate
	 * @Description: 初始化公共数据
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initDate() {
		// 获取屏幕宽高
		WindowManager manager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		GlobalParams.WIN_WIDTH = dm.widthPixels;
		GlobalParams.WIN_HIGTH = dm.heightPixels;
		GlobalParams.context = this;
		// 初始化数据库集合
		// DBHelper.dbList.add("ChatMessageItem");
		// 初始化 缓存目录
		CacheManager.getInstance().initCacheDirectory();
		// 添加异常扑捉工具
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		loader = new ImageLoader(this);
		initUser();

	}
	//读取配置文件中用户信息
	private void initUser() {
		GlobalParams.USER_ADDRESS = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "address", "");
		GlobalParams.USER_ID = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "id", "");
		GlobalParams.USER_GENDER = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "gender", "");
		GlobalParams.USER_HEADER = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "picture", "");
		GlobalParams.USER_EMIL = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "email", "");
		GlobalParams.USER_NAME = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "nickname", "");
		GlobalParams.USER_PHONE = SharedPreferencesUtils.getData(GlobalParams.context, "ls_user_message", "mobile", "");
		GlobalParams.USER_INTEGRAL = SharedPreferencesUtils.getData(context, "ls_user_message", "ingegral", 0);
		GlobalParams.SSO_TOKEN = SharedPreferencesUtils.getData(context, "ls_user_message", "sso_token", "");
	}
	
	public static Context getContext() {
		return context;
	}

	/**
	 * 
	 * @Title: initService
	 * @Description:初始化服务及监听
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initService() {
		// 注册Activity生命周期监听
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityStopped(Activity activity) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onActivityStarted(Activity activity) {
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onActivityResumed(Activity activity) {
				// 给当前Activity赋值
				LApplication.this.topActivity = activity;
				// 将Activity放入到Activity集合
				mList.add(activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
			}

			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
			}
		});
	}

	/**
	 * 关闭所有的activity
	 */
	public void exit() {
		if(service!=null)
			stopService(service);
		try {
			for (Activity activity : mList) {
				if (activity != null){
					activity.finish();
					activity = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mNotificationManager.cancelAll();
		}
	}

	/**
	 * 移除某个activity
	 * 
	 * @param name
	 *            activity的名称
	 */
	public void RemoveActivity(String name) {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					if (activity.getLocalClassName().contains(name)) {
						activity.finish();
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取某个activity
	 * 
	 * @param name
	 *            activity的名称
	 */
	public Activity getActivity(String name) {
		Activity act = null;
		try {
			for (Activity activity : mList) {
				if (activity != null)
					if (activity.getLocalClassName().contains(name)) {
						act = activity;
						break;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return act;
	}

	/**
	 * 获取当前的Activity
	 * 
	 * @return
	 */
	public static Activity getTopActivity() {
		return instance.topActivity;
	}
	/**
	 * 获取包名
	 * @param cxt
	 * @param pid
	 * @return
	 */
	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps != null) {
			for (RunningAppProcessInfo procInfo : runningApps) {
				if (procInfo.pid == pid) {
					return procInfo.processName;
				}
			}
		}
		return null;
	}
	/**
	 * 清空缓存
	 */
	private void cleanCacheFile() {
		ExecutorService clearCacheService = Executors.newSingleThreadExecutor();
		String root = FileUtil
				.getCacheRootDirPath(context);
		clearCacheService.execute(new ClearCacheFile(root));
	}

	/**
	 * 开启一个单线程用来根据缓存大小来清理最近没有使用过的缓存图片文件
	 * 
	 * @author donggx
	 * 
	 */
	class ClearCacheFile implements Runnable {
		private String cacheRoot;

		public ClearCacheFile(String cacheRoot) {
			
			this.cacheRoot = cacheRoot;
		}

		@Override
		public void run() {
			if (TextUtils.isEmpty(cacheRoot))
				return;
			File cacheDirectory = new File(cacheRoot);
			FileUtil.clear(cacheDirectory);
		}
	}
	
	/**
	 * 显示广告
	 */
	public void showGuanggaoView(String url) {
		synchronized (LApplication.class) {
			if(url==null|| TextUtils.isEmpty(url))
				return;
			if(guangGaoView!=null)
				return;
			guangGaoUrl = url;
			guangGaoView = LayoutInflater.from(this).inflate(R.layout.guanggao_layout, null);
			ImageView guangGao = (ImageView) guangGaoView.findViewById(R.id.pic_guanggao);
			com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, guangGao);
	//		.DisplayImage(url, guangGao, R.drawable.huodongshouye_zhanwei);
			guangGaoView.findViewById(R.id.dilte_guanggao).setOnClickListener(
					new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							if(guangGaoView!=null){
								mWindowManager.removeView(guangGaoView);
								guangGaoView = null;
								guangGaoUrl = null;
							}
						}
					});
			guangGao.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(guangGaoView!=null){
								for(int i= mList.size()-1;i>0;i--){
									if(!mList.get(i).getClass().equals(MainActivity.class)){
										mList.get(i).finish();
									}else{
										break;
									}
								}
//								MainActivity.instance.tag_view=2;
//								MainActivity.instance.initIntentData("yongche");
								Intent intent=new Intent(LApplication.this,UserCarActivity.class);
								LApplication.this.startActivity(intent);
								mWindowManager.removeView(guangGaoView);
								guangGaoView = null;
								guangGaoUrl = null;
							}
						}
					});
			// 获取WindowManager
			mWindowManager = (WindowManager) getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			// 设置LayoutParams(全局变量）相关参数
			param = getMywmParams();
	
			param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // 系统提示类型,重要
			param.format = 1;
			param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
			param.flags = param.flags
					| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
			param.flags = param.flags
					| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
	
			param.alpha = 1.0f;
	
			param.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
			// 以屏幕左上角为原点，设置x、y初始值
			param.x = 0;
			param.y = 0;
	
			// 设置悬浮窗口长宽数据
			param.width = WindowManager.LayoutParams.FILL_PARENT;
			param.height = DensityUtil.dip2px(this, 200);
	
			// 显示myFloatView图像
			mWindowManager.addView(guangGaoView, param);
		
		}

	}
	/**
	 * 删除广告条
	 */
	public void removeView() {
		if (guangGaoView != null) {
			mWindowManager.removeView(guangGaoView);
			guangGaoView = null;
		}
	}
	/**
	 * 获取广告图片url
	 * @return
	 */
	public String getGuangGaoUrl(){
		return guangGaoUrl;
	}
	/**
	 * windowManager LayoutParams  用于设置view
	 */
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	/**
	 *  windowManager LayoutParams
	 * @return
	 */
	private WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}
	/**
	 * 返回存放的Activity的集合
	 * @return
	 */
	public List<Activity> getActivityList(){
		return mList;
	}
}
