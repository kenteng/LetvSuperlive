package com.lesports.stadium.engine;

import java.io.File;
import java.util.List;

import com.lesports.stadium.utils.ConstantValue;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 上传文件到后台
 * 
 * @author Administrator
 * 
 */
public class UpLoadFile {
	/**
	 * 上传文件
	 * 
	 * @param filePath
	 *            本地文件路径
	 * @param callback
	 *            回调方法
	 */
	public static void upLoadFile(List<String> filePath,
			RequestCallBack<String> callback) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		for(int i=0;i<filePath.size();i++){
			File file = new File(filePath.get(i));
			params.addBodyParameter(i+"", file);
		}
		http.send(HttpRequest.HttpMethod.POST,ConstantValue.UPLOAD_INTERFACE, params, callback);
	}
}
