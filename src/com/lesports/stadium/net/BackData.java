package com.lesports.stadium.net;

import android.text.TextUtils;
import android.util.Log;

import com.lesports.stadium.net.Code.LoadType;


/**
 * 
 * @ClassName:  BackData   
 * @Description:用来保存，解析网络请求服务器端返回的数据  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:53:26   
 *
 */
public class BackData {

    /**
     *     x-ennew-res = resCode/uid/did/sid/format 共5种结果
     */
    private static final int RESPONSE_MESSAGE_NUM = 5;

    /**
     * 服务器code
     */
    private int netResultCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 服务器返回数据的格式：有plain text ，有json等
     */
    private int dataType;
    
    /**
     * 服务端返回的对象
     */
    private Object object; 
    
    /**
     * 加载方式  D下拉  U 上拉  N正常（默认）
     */
    private String loadType=LoadType.NORMAL_LOAD;
    
    /**
     * 返回数据是否是缓存
     * true 是  false 不是
     */
    private int dataResourceType;
    
    /**
     * 根据服务器返回的response string 进行信息解析
     * x-ennew-res = resCode/uid/did/sid/format
     *  resCode 结果代码
     *  uid 用户ID
     *  did 设备ID
     *  sid 会话ID
     *  format 格式声明
     * @param resultString  服务器返回的数据
     */
    public void setServiceResult(String resultString) {
        String[] str_list = resultString.split("/");
        if (str_list.length == RESPONSE_MESSAGE_NUM) {
            // 设置网络返回结果
            if(!TextUtils.isEmpty(str_list[0]))
            	setNetResultCode(Integer.valueOf(str_list[0]));
            // 设置返回数据格式
            if(!TextUtils.isEmpty(str_list[4]))
            	setDataType(Integer.valueOf(str_list[4]));

            RequestHeader requestHeader = RequestHeader.getInstance();
            //头信息，设置用户id --uid
            if(!TextUtils.isEmpty(str_list[1]))
            	requestHeader.setUid(Long.valueOf(str_list[1]));
            //头信息 ,设置did ,string类型
            requestHeader.setDid(str_list[2]);
            //头信息，设置会话id --sid
            requestHeader.setSid(str_list[3]);
        }
    }

    public int getNetResultCode() {
        return netResultCode;
    }
    public void setNetResultCode(int netResultCode) {
        this.netResultCode = netResultCode;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public int getDataType() {
        return dataType;
    }
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    
	public String getLoadType() {
		return loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}

	public int getDataResourceType() {
		return dataResourceType;
	}

	public void setDataResourceType(int dataResourceType) {
		this.dataResourceType = dataResourceType;
	}
}
