package com.lesports.stadium.receiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.BookthecarActivity;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.utils.ConstantValue;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	// 震动
	private Vibrator vibrator;
	/**
	 * 易道广播action
	 */
	public static final String ACTION_YIDAO_CAR = "com.broadcast.yidao.car";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		// Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() +
		// ", extras: " + printBundle(bundle));
		// Log.e("YIDAOtui", printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			// TODO 可以通过从Bundle中拿到的extra的值来进行不同的操作 打开不同的activity
			// 打开Activity
			// Log.d("YongChe", bundle.toString());
			String tag = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject yidaoObj = new JSONObject(tag);
				String type = yidaoObj.getString("type");
				if (type.equals("001")) {
					Intent i = new Intent(context, BookthecarActivity.class);
					i.putExtras(bundle);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);
				}else{
					if(!isTopActivity(context)){
						Intent i = new Intent(context, MainActivity.class);
						context.startActivity(i);
					}
				}
				JPushInterface.clearAllNotifications(context);
				JPushInterface.clearLocalNotifications(context);
			} catch (Exception e) {
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
			// bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			// Log.w(TAG, "[MyReceiver]" + intent.getAction()
			// +" connected state change to "+connected);
		} else {
			// Log.d(TAG, "[MyReceiver] Unhandled intent - " +
			// intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String tag = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String type = "";
		String orderId = "";
		try {
			Log.i("dcc", "后台推送：" + tag);
			JSONObject yidaoObj = new JSONObject(tag);
			type = yidaoObj.getString("type");
			orderId = yidaoObj.getString("subject");
		} catch (JSONException e) {

			e.printStackTrace();
		}
		// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		// String[] msg=message.split("_");
		// Log.e("YIDAOtui",printBundle(bundle)+"===="+tag);
		// Log.d("YIDAOtui",type+"===="+orderId+"==="+message);
		Log.i("wxn", "type:" + type);
		Log.i("wxn", "message:" + message + "   orderId : " + orderId);
		// 判断是否用车推送
		if (type.equals("001")) {
			// 用车推送
			Log.e("dcc", "type:" + type + message);
			if (message.equals("无人接单")) {
				// Toast.makeText(context, "无人接单", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", "无人接单");
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals("司机已确认")) {
				// Toast.makeText(context, "司机已确认", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", "司机已确认");
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals("司机已到达")) {
				// Toast.makeText(context, "司机已到达", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", message);
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals("服务开始")) {
				// Toast.makeText(context, "服务开始", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", message);
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals("服务完成")) {
				// Toast.makeText(context, "服务完成", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", message);
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals("账单确认")) {
				// Toast.makeText(context, "账单确认", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", message);
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			} else if (message.equals(R.string.yidao_end)) {
				// Toast.makeText(context, "交易结束", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("msg", "交易结束");
				intent.setAction(ACTION_YIDAO_CAR);
				context.sendBroadcast(intent);
			}
		} else if (type.equals("007")) {
			Log.i("lwc", "退款推送的结果数据是" + tag);
		} else if (type.equals("008")) {
			if (!isTopActivity(context))
				return;
			LApplication alip = (LApplication) context.getApplicationContext();
			alip.showGuanggaoView(ConstantValue.BASE_IMAGE_URL + orderId
					+ ConstantValue.IMAGE_END);
		}
	}

	public void showLocalNotification(Context context, String message) {
		JPushLocalNotification ln = new JPushLocalNotification();
		ln.setBuilderId(0);
		ln.setTitle("Soul");
		ln.setContent(message);
		ln.setNotificationId(new Random().nextInt());
		ln.setBroadcastTime(System.currentTimeMillis());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "jpush");
		map.put("test", "111");
		JSONObject json = new JSONObject(map);
		ln.setExtras(json.toString());
		JPushInterface.addLocalNotification(context, ln);
	}

	protected static boolean isTopActivity(Context context) {
		String packageName = context.getPackageName();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
