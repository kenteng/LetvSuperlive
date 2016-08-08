package com.lesports.stadium.activity;

import com.lesports.stadium.R;
//ceshi
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: AboutUsActivity
 * 
 * @Desc : 关于我们界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:29:57
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class AboutUsActivity extends Activity implements OnClickListener {
	/**
	 * 显示当前版本号 的textview
	 */
	private TextView version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		String versionName = getVersionName();
		initView();
		if (TextUtils.isEmpty(versionName))
			versionName = "V 1.0.0";
		version.setText("V " + versionName);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		version = (TextView) findViewById(R.id.version);

	}

	/**
	 * 获取版本号信息
	 * 
	 * @return
	 */
	private String getVersionName() {
		String version = "";
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

}
