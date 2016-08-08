package com.lesports.stadium.net;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.net.Code.DataResourceType;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.Code.LoadType;
import com.lesports.stadium.net.Code.NetworkStatus;
import com.lesports.stadium.net.Code.ServiceCode;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.SignUtil;
import com.lesports.stadium.utils.ThreadManager;
import com.lesports.stadium.view.CustomDialog;


/**
 * 接口访问
 * @author Administrator
 *
 */
public class NetService {

    private static final String TAG = "NetService";

    /**
     * 连接超时，连接主机超时（单位是毫秒）
     */
    private static final int CONNECT_TIME_OUT = 5000;

    /**
     * 设置从主机读取数据超时（单位是毫秒）
     */
    private static final int READ_TIME_OUT = 5000;

    /**
     * Server对象
     */
    private static NetService service;


    //设置相关
    /**
     * 连接超时,不设置就用默认值
     */
    private int connectTimeOut;

    /**
     * 响应超时，不设置就用默认值
     */
    private int readTimeOut;

    // 参数相关
    /**
     * 接口地址
     */
    private String url;
    
    /**
     * 头信息
     */
    public RequestHeader headers = RequestHeader.getInstance();

    /**
     * 参数
     */
    private Map<String, String> params = new HashMap<String, String>();

    //缓存相关
    /**
     * 是否需要缓存
     */
    private boolean isUseCache = false;
    
    /**
     * 缓存管理
     */
    private static CacheManager cacheManager;

    /**
     * 缓存key,如果不调用key的生成方法则自动生成
     */
    private String cacheKey;

    /**
     * 缓存类型
     */
    private int cacheType;

    /**
     * 缓存目录
     */
    private String cachePath;
    

    /**
     * 缓存临时文件的目录
     */
    private String tempPath;

    /**
     * 网络请求方式
     */
    private String httpMethod;

    /**
     * 接口回调
     */
    private ServiceContentHandler serviceContentHandler;

    /**
     * 返回数据
     */
    private BackData backData;

    /**
     * 接口回调
     */
    private NetLoadListener netLoadListener;

    /**
     * 网络接口返回内容长度
     */
    private long contentLength;

    /**
     * 是否使用安全吗字符串
     * 默认值为false
     */
    private boolean isUsedSec;
  
    
    /**
     * 加载方式  D下拉  U 上拉  N正常（默认）
     */
    private String loadType=LoadType.NORMAL_LOAD;
    
    /**
     * 是否是分页，如果是分页  需要关闭页面的时候
     */
    private boolean isPaging=false;
    
    /**
     * 单例模式
     *
     * @return 返回网络访问的一个实例
     */
    public static NetService getInStance() {
//        if (service == null) {
//        	
//        }
        service = new NetService();
        cacheManager=CacheManager.getInstance();
        return service;
    }

    /**
     * 构造函数
     */
    public NetService() {
        // 初始化变量
        this.readTimeOut = READ_TIME_OUT;
        this.connectTimeOut = CONNECT_TIME_OUT;

        this.httpMethod = HttpSetting.POST_MODE;
        this.backData = new BackData();

        this.isUsedSec = false;
        this.cacheType = CacheType.CACHE_NETWORK;
        this.cacheKey=null;
    }

    /**
     * 设置连接超时时间，如果不设置用默认值
     *
     * @param connectTimeOut 连接超时时间
     */
    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    /**
     * 设置从主机读取数据超时时间，如果不设置用默认值
     *
     * @param readTimeOut 超时时间
     */
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    /**
     * 设置请求接口地址
     *
     * @param url 请求接口地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 设置参数
     *
     * @param key   健
     * @param value 值
     */
    public void setParams(String key, String value) {
        this.params.put(key, value);
    }

    /**
     * 设置是否使用缓存
     *
     * @param isUseCache 是否使用缓存
     */
    public void setUseCache(boolean isUseCache) {
        this.isUseCache = isUseCache;
    }


    /**
     * 设置请求的方式，默认post
     *
     * @param httpMethod 设置http请求方法
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * 设置serviceContentHandler
     *
     * @param serviceContentHandler 设置serviceContentHandler
     */
    public void setSrviceContentHandler(ServiceContentHandler serviceContentHandler) {
        this.serviceContentHandler = serviceContentHandler;
    }

    
    /**
     * 手动生成cacheKey
     *
     * @param keyFlg 缓存key
     */
    public void SetCacheKey(String keyFlg) {
        String params_str = "";
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                //解析Map传递的参数
                if (params_str.equals("")) {
                    params_str = entry.getKey() + "=" + entry.getValue();
                } else {
                    params_str += "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        String key_str = url + "?" + params_str;
        this.cacheKey = keyFlg + CommonUtils.md5(key_str);
    }


    /**
     * 设置是否使用安全码字符串  默认值为false
     *
     * @param isUsedSec 是否使用安全码
     */
    public void setUsedSec(boolean isUsedSec) {
        this.isUsedSec = isUsedSec;
    }
    
    /**
     * 设置加载方式   加载方式  D下拉  U 上拉  N正常（默认）
     * @param loadType
     */
    public void setLoadType(String loadType) {
		this.loadType = loadType;
	}
    
    public void setIsPaging(boolean isPaging){
    	this.isPaging=isPaging;
    }

    /**
     * 自动生成cacheKey
     */
    public void creatCacheKey() {
        String params_str = "";
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                //解析Map传递的参数
                if (params_str.equals("")) {
                    params_str = entry.getKey() + "=" + entry.getValue();
                } else {
                    params_str += "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        String key_str = url + "?" + params_str+"?userId="+headers.getUid();
        this.cacheKey = CommonUtils.md5(key_str);
    }

    /**
     * 获取缓存或者请求网络
     *
     * @param netLoadListener 网络加载监听函数
     */
    public void loader(NetLoadListener netLoadListener) {
        this.netLoadListener = netLoadListener;
        this.loader(netLoadListener, null);
    }

    /**
     * 获取缓存，请求网络
     *
     * @param netLoadListener       网络加载监听函数
     * @param serviceContentHandler 接口回调函数
     */
    public void loader(NetLoadListener netLoadListener, ServiceContentHandler serviceContentHandler) {
        this.netLoadListener = netLoadListener;
        this.serviceContentHandler = serviceContentHandler;
        params.put("requestTime", ""+Calendar.getInstance().getTimeInMillis());
        params.put("sso_token",GlobalParams.SSO_TOKEN);
		String sings = SignUtil.createSign(params);
		params.put("sign", sings);
        //创建访问网络的key
        creatCacheKey();
        //获取当前访问缓存路径
        cachePath = cacheManager.getCachePath(cacheKey, cacheType);
        //判断网络
        if(CommonUtils.getAPNType(GlobalParams.context)==NetworkStatus.NO_NET){
        	//无网络 弹出提示，并获取本地数据
        	if(GlobalParams.IS_SHOW_NOWORK){
        		showNoNetDialog();
        	}
        	getDataByCache();
        }else{
        	//当要求使用缓存，且缓存文件存在时 读取缓存文件
        	File file = new File(cachePath);
        	if(isUseCache && file.exists()){
        		//加载缓存中内容
        		getDataByCache();
        	}else{
        	   //网络请求
        		getDataByNet();
        	}
        }
    }

    /**
     * 从缓存中获取网络请求数据
     */
    public void getDataByCache() {
        //读取缓存内容
        String cache_content;
		try {
			cache_content = CacheUtils.getCacheData(cachePath);
			if(!TextUtils.isEmpty(cache_content)&&!cache_content.equals("[]")){
	        	 backData.setObject(cache_content);
	        	 backData.setNetResultCode(ServiceCode.SUCCESS);
	             backData.setDataResourceType(DataResourceType.CACHE_DATA);
	             backData.setLoadType(this.loadType);
	             
	             //返回数据
	             if (netLoadListener != null) {
	                 netLoadListener.onloaderListener(backData);
	             }
	        }else if(TextUtils.isEmpty(cache_content)){
	        	if (netLoadListener != null) {
                	backData.setNetResultCode(ServiceCode.NETWORK_ERROR);
                	backData.setObject("");
                    netLoadListener.onloaderListener(backData);
                }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }

    /**
     * 向网络请求数据
     */
    private void getDataByNet() {
    	ThreadManager threadManager = ThreadManager.getInstance();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //实例化一个默认的Http客户端
                URL uRL;
                HttpURLConnection httpURLConnection;
                Map<String, String> list = new HashMap<String, String>();
                //参数字符串
                String params_str = "";
                try {
                    //判断请求方式
                    if (httpMethod.equals(HttpSetting.GET_MODE)) {
                        if (params != null && !params.isEmpty()) {
                            for (Map.Entry<String, String> entry : params.entrySet()) {
                                //解析Map传递的参数
                                if (params_str.equals("")) {
                                    params_str = entry.getKey() + "=" + entry.getValue();
                                } else {
                                    params_str += "&" + entry.getKey() + "=" + entry.getValue();
                                }
                            }
                        }
                        //判断url地址，组装请求地址
                        if (url.contains("?")) {
                            url = url + "&" + params_str;
                        } else {
                            url = url + "?" + params_str;
                        }
                        //实例化
                        uRL = new URL(url.trim());
                        Log.i("wxn", "网络请求地址："+url.trim());
                        httpURLConnection = (HttpURLConnection) uRL.openConnection();
                        httpURLConnection.setRequestMethod(HttpSetting.GET_MODE);
                        //设置头信息
                        httpURLConnection.addRequestProperty("x-letv-req", headers.getHeaders());
                        //设置头信息安全码
                        if (isUsedSec) {
                            httpURLConnection.addRequestProperty("x-letv-sec", getSec());
                        }
              
                    } else {
                        StringBuffer paramsBuffer = new StringBuffer();
                        // 组织请求参数
                        if (params != null && !params.isEmpty()) {
                            for (Map.Entry<String, String> entry : params.entrySet()) {
                                //解析Map传递的参数，使用一个键值对对象BasicNameValuePair保存。
                                list.put(entry.getKey(), entry.getValue());
                            }
                        }
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry element = (Map.Entry) it.next();
                            paramsBuffer.append(element.getKey());
                            paramsBuffer.append("=");
                            paramsBuffer.append(element.getValue());
                            paramsBuffer.append("&");
                        }
                        if (paramsBuffer.length() > 0) {
                            paramsBuffer.deleteCharAt(paramsBuffer.length() - 1);
                        }
                        uRL = new URL(url);
                        httpURLConnection = (HttpURLConnection) uRL.openConnection();
            
                        //设置头信息
                        httpURLConnection.addRequestProperty("x-letv-req", headers.getHeaders());
                        //设置头信息安全码
                        if (isUsedSec) {
                            httpURLConnection.addRequestProperty("x-letv-sec", getSec());
                        }
                        
                        byte[]outbytes = paramsBuffer.toString().getBytes("UTF-8");
                        httpURLConnection.setRequestProperty("Content-Length", String
                                .valueOf(outbytes.length));
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);

                        // 获取URLConnection对象对应的输出流
                        PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                        // 发送请求参数
                        printWriter.write(paramsBuffer.toString());
                        // flush输出流的缓冲
                        printWriter.flush();
                    }
                    httpURLConnection.connect();
                    //判断是否请求成功，为时表示成功，其他均问有问题。
                    int responseCode = httpURLConnection.getResponseCode();  //返回值
                    if (responseCode >= 200 && responseCode < 300) {
                        //接口返回数据流
                        InputStream inputStream = httpURLConnection.getInputStream();
                        if (inputStream != null) {
                            contentLength = httpURLConnection.getContentLength();
                            //从头信息中获取服务器的应答
                            String requestHeaders = httpURLConnection.getHeaderField("x-letv-res");
                            if (requestHeaders != null) {
                                //解析信息获得接口返回数据类型
                                backData.setServiceResult(requestHeaders);
                            }
                            //解析流数据
                           // analyseStream(inputStream);  
                           //判断是否需要缓存
                            if (isUseCache&&!isPaging) {
                                //创建缓存临时文件
                                File file = createTempFile();
                                //自定义文件输入流
                                CachedInputStream cacheInputStream;
                                try {
                                    //初始话
                                    cacheInputStream = new CachedInputStream(inputStream, file);
                                    //解析流数据
                                    analyseStream(cacheInputStream);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //解析流数据
                                analyseStream(inputStream);
                            }
                        }
                    }else{
                    	//接口访问失败
                        //返回数据
                        if (netLoadListener != null) {
                        	backData.setNetResultCode(responseCode);
                        	backData.setObject("");
                            netLoadListener.onloaderListener(backData);
                        }
                    }
                } catch (IOException e) {
                	if (netLoadListener != null) {
                    	backData.setNetResultCode(ServiceCode.NETWORK_ERROR);
                    	backData.setObject("");
                        netLoadListener.onloaderListener(backData);
                    }
                }
            }
        };

        //添加到线程池管理者中
        threadManager.addTask(task);
    }


  /**
   * 解析数据
   * @param inputStream 
   * @param dataType 数据类型 1 缓存返回，2 服务器返回
   */
    private void analyseStream(InputStream inputStream) {
        // 获取返回数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer content = new StringBuffer();
        String lineTxt;
        try {
            while ((lineTxt = reader.readLine()) != null) {
            	content.append(lineTxt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                if (isUseCache&&!isPaging) {
                    File tempFile = new File(tempPath);
                    long tempLength = tempFile.length();
                    //如果返回的内容的长度缓存临时文件的内容长度一样则临时文件移到缓存目录里面
                    if (contentLength == tempLength) {
                        File file = new File(cachePath);
                        tempFile.renameTo(file);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        backData.setObject(content.toString());
        backData.setLoadType(this.loadType);
        backData.setDataResourceType(DataResourceType.INTERFACE_DATA);

        //返回数据
        if (netLoadListener != null) {
            netLoadListener.onloaderListener(backData);
        }
    }
    
    /**
     * 创建缓存的临时文件
     */
    private File createTempFile() {
        //生成零时文件
        tempPath = cacheManager.getCachePath(cacheKey,CacheType.CACHE_TMP)+ ".temp";
        //实例化缓存临时文件
        File file = new File(tempPath);
        //判断缓存文件是否存在
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 写缓存数据
     * @param object
     * @throws IOException
     */
    public void writeCache(Object object) throws IOException{
    	File tempFile=createTempFile();
        if(!tempFile.exists()){
       	 try {
       		tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        DataOutputStream out=null;        
        try {
          out=new DataOutputStream(  
                   new BufferedOutputStream(  
                   new FileOutputStream(cachePath,false)));  
          out.writeBytes(URLEncoder.encode(object.toString(), "UTF-8"));
          //out.writeBytes(new String(object.toString().getBytes("UTF-8"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
       }finally{
           try {
               out.flush();
               out.close();
               //如果返回的内容的长度缓存临时文件的内容长度一样则临时文件移到缓存目录里面
               if (contentLength == tempFile.length()) {
                   File cacheFile = new File(cachePath);
                   tempFile.renameTo(cacheFile);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    /**
     * 生成安全码字符串
     */
    private String getSec() {
        long time = System.currentTimeMillis();
        return "0" + time + CommonUtils.md5(time + HttpSetting.CIPHER + getParamsCode());
    }

    /**
     * 根据参数生成安全码字符串
     *
     * @return 安全码字符串
     */
    private String getParamsCode() {
        String result = "";
        if (params != null && !params.isEmpty()) {
            List<String> dateStringList = new ArrayList<String>();
            dateStringList.addAll(params.keySet());
            for (String str : dateStringList) {
                result += str + params.get(str);
            }
        }
        return result;
    }
    private CustomDialog exitDialog;
	/**
	 * 无网络提示框
	 */
	private void showNoNetDialog() {
		final Activity activity = LApplication.getTopActivity();
		if(activity==null)
			return;
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				GlobalParams.IS_SHOW_NOWORK = false;
				exitDialog = new CustomDialog(activity,new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
						LApplication.getTopActivity().startActivity(intent);
						exitDialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						exitDialog.dismiss();
					}
				});
				exitDialog.setRemindTitle("温馨提示");
				exitDialog.setCancelTxt("知道了");
				exitDialog.setConfirmTxt("设置");
				exitDialog.setRemindMessage("亲，当前无网络哦~");
				exitDialog.show();
			}
		});
	}
}