package com.lesports.stadium.http.paser;

import org.json.JSONObject;

import com.letv.component.core.http.parse.LetvMainParser;

public abstract class LetvParser extends LetvMainParser{

	public LetvParser() {
		super(0);
	}
	
	public LetvParser(int from) {
		super(from);
	}
	
	@Override
	protected boolean canParse(String data) {
		
		return true;
	}

	@Override
	protected Object getData(String data) throws Exception {
		JSONObject object = new JSONObject(data);
		return object;
	}

	/**
	 * 加载本地数据，需要缓存数据的解析器需要实现
	 * */
	protected abstract String getLocationData();

}
