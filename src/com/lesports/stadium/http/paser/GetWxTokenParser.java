package com.lesports.stadium.http.paser;

import com.google.gson.Gson;
import com.lesports.stadium.bean.WxTokenInfo;


public class GetWxTokenParser extends LetvParser {
	@Override
	protected String getLocationData() {
		return null;
	}

	@Override
	public Object parse(Object data) throws Exception {
		WxTokenInfo wxTokenInfo = new WxTokenInfo();
		Gson gson=new Gson();
		wxTokenInfo=gson.fromJson(String.valueOf(data), wxTokenInfo.getClass());
	    if (wxTokenInfo == null) {
	        return null;
	    }
		return wxTokenInfo;
	}

}
