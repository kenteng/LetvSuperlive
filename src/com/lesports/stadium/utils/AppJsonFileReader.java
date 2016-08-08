package com.lesports.stadium.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.bean.CityBean;

import android.content.Context;
import android.content.res.AssetManager;

public class AppJsonFileReader {
	public static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	public static List<CityBean> setData(String str) {
		try {
			List<CityBean> data = new ArrayList<CityBean>();
			JSONArray array = new JSONArray(str);
			int len = array.length();
			CityBean map;
			for (int i = 0; i < len; i++) {
				JSONObject object = array.getJSONObject(i);
				map = new CityBean();
				map.setID(object.getString("ID"));
				map.setLevelType(object.getString("levelType"));
				map.setName(object.getString("name"));
				data.add(map);
			}
			return data;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<Map<String, String>> setListData(String str) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {

			JSONArray array = new JSONArray(str);
			int len = array.length();
			Map<String, String> map;
			for (int i = 0; i < len; i++) {
				JSONObject object = array.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("imageId", object.getString("imageId"));
				map.put("title", object.getString("title"));
				map.put("subTitle", object.getString("subTitle"));
				map.put("type", object.getString("type"));
				data.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;

	}
}
