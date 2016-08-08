package com.lesports.stadium.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.PayParametric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class PayUtils {
	
	//public static  String signKey="a75ce74bbac731b7ee87b4d68c612592";
	
	//public static  String marchantBusinessId="4";
	//
//	public static  String signKey="89889d7db08c1a58741f220ff93be3865";
	public static  String signKey="b2c0bf6dfcaac9501ec3846d377e4cfa";
	
    public static  String marchantBusinessId="13";
	
	public static void setSignKey(String signKey) {
		PayUtils.signKey = signKey;
	}
	
	//获取时间戳
	@SuppressLint("SimpleDateFormat") 
	public static String  getTimestamp(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String dateStr = df.format(new Date());
		Log.i("lepaytest", "timestamp=" + dateStr);
		return dateStr;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getTradeNum(){
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		int rand =random.nextInt();
		if(rand>0){
			return rand;
		}else{
			return (-rand);
		}
	}
	
	public  static String getSign(PayParametric item) throws Exception{
		//signKey=mSignKey.getText().toString().trim();
		String sourceUrlStr = getAscParamsUrlStr(item); // 按参数字母升序排列参数原串
		String signStr = CommonUtils.md5(sourceUrlStr + "&key="+ signKey);
		return signStr;
	}
	
	
	public static String getAscParamsUrlStr(PayParametric item){		
		List<NameValuePair> paramslist = new LinkedList<NameValuePair>();
		paramslist.add(new BasicNameValuePair("version",item.getVersion().trim()));
		paramslist.add(new BasicNameValuePair("merchant_business_id",item.getMerchant_business_id().trim()));
		paramslist.add(new BasicNameValuePair("user_id", item.getUser_id().trim()));
		paramslist.add(new BasicNameValuePair("user_name",item.getUser_name().trim()));
		paramslist.add(new BasicNameValuePair("notify_url",item.getNotify_url().trim()));
        //paramslist.add(new BasicNameValuePair("call_back_url", mNotifyUrl.getText().toString().trim()));
		paramslist.add(new BasicNameValuePair("merchant_no",item.getMerchant_no().trim()));
		paramslist.add(new BasicNameValuePair("out_trade_no", item.getOut_trade_no().trim()));
		paramslist.add(new BasicNameValuePair("price",item.getPrice().trim()));
		paramslist.add(new BasicNameValuePair("currency", item.getCurrency().trim()));
		paramslist.add(new BasicNameValuePair("pay_expire",item.getPay_expire().trim()));
		paramslist.add(new BasicNameValuePair("product_id", item.getProduct_id().trim()));
		paramslist.add(new BasicNameValuePair("product_name", item.getProduct_name().trim()));
		paramslist.add(new BasicNameValuePair("product_desc",item.getProduct_desc().trim()));
		paramslist.add(new BasicNameValuePair("product_urls",item.getProduct_urls().trim()));
		paramslist.add(new BasicNameValuePair("timestamp",getTimestamp()));
		paramslist.add(new BasicNameValuePair("key_index", item.getKey_index().trim()));
		paramslist.add(new BasicNameValuePair("input_charset",item.getInput_charset().trim()));
		paramslist.add(new BasicNameValuePair("sign_type", item.getSign_type().trim()));
        paramslist.add(new BasicNameValuePair("ip", getLocalIpAddress()));
        paramslist.add(new BasicNameValuePair("service", item.getService().trim()));

		String urlStr =getUrlParams(paramslist);
		Log.i("lepaytest", "urlString:" + urlStr);
		return urlStr;
	}
	
	private static String getUrlParams(List<NameValuePair> paramslist) {
		if (paramslist == null || paramslist.size() == 0) {
			return "";
		}
		URLEncodedUtils.format(paramslist, "UTF-8");
		Collections.sort(paramslist, DICCOMPARATOR);
		StringBuffer sb = new StringBuffer();
		final int count = paramslist.size();
		for (int i = 0; i < count; i++) {
			if (i != 0) {
				sb.append("&");
			}
			sb.append(paramslist.get(i).getName());
			sb.append("=");
			sb.append(paramslist.get(i).getValue());
		}
		return sb.toString();
	}
	
	private final static Comparator<NameValuePair> DICCOMPARATOR = new Comparator<NameValuePair>() {
		@Override
		public int compare(NameValuePair lhs, NameValuePair rhs) {
			if (lhs.getName().equals(rhs.getName())) {
				return 0;
			}
			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};
	
	
	
	/**
	 * 
	 * @param item
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getTradeInfo(PayParametric item) throws UnsupportedEncodingException {
		 StringBuffer params = new StringBuffer("");
	     params.append("version").append("=").append(item.getVersion().toString().trim());
         params.append("&").append("service").append("=").append("lepay.app.api.show.cashier");
	      params.append("&").append("merchant_business_id").append("=").append(item.getMerchant_business_id().trim());
	      params.append("&").append("user_id").append("=").append(item.getUser_id());
	      params.append("&").append("user_name").append("=").append(item.getUser_name().toString().trim());
	      //params.append("&").append("letv_user_id").append("=").append(mLetvUserId.getText().toString().trim());
	      params.append("&").append("notify_url").append("=").append(item.getNotify_url().toString().trim());
         //params.append("&").append("call_back_url").append("=").append(mNotifyUrl.getText().toString().trim());
	      params.append("&").append("merchant_no").append("=").append(item.getMerchant_no().toString().trim());
	      params.append("&").append("out_trade_no").append("=").append(item.getOut_trade_no().toString().trim());
	      params.append("&").append("price").append("=").append(item.getPrice().toString().trim());
	      params.append("&").append("currency").append("=").append(item.getCurrency().toString().trim());
	      params.append("&").append("pay_expire").append("=").append(item.getPay_expire().toString().trim());
	      params.append("&").append("product_id").append("=").append(item.getProduct_id().toString().trim());
	      params.append("&").append("product_name").append("=").append(item.getProduct_name().toString().trim());
	      params.append("&").append("product_desc").append("=").append(item.getProduct_desc().toString().trim());
	      params.append("&").append("product_urls").append("=").append(item.getProduct_urls().toString().trim());
	      params.append("&").append("timestamp").append("=").append(getTimestamp());
	      params.append("&").append("key_index").append("=").append(item.getKey_index());
	      params.append("&").append("input_charset").append("=").append(item.getInput_charset());
         params.append("&").append("ip").append("=").append(getLocalIpAddress());
         
         /**
          * sign 为安全保证，需商户服务器进行签名，签名秘钥不暴露给客户端；
          */
	      try {
			params.append("&").append("sign").append("=").append(getSign(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
	      params.append("&").append("sign_type").append("=").append(item.getSign_type().toString().trim());
	      return params.toString();
	}
	
	   /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) LApplication.getContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return "undefind";
        }
    }
    
    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
	
}
