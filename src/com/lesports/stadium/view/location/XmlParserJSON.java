package com.lesports.stadium.view.location;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.lesports.stadium.view.location.model.CityModel;
import com.lesports.stadium.view.location.model.DistrictModel;
import com.lesports.stadium.view.location.model.ProvinceModel;

public class XmlParserJSON {

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
	public XmlParserJSON(Context context) {
		asertManger = context.getAssets();
		parserJson();
	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();
	private AssetManager asertManger;

	private void parserJson() {
		try { 
			InputStream is = asertManger.open("citycode.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String jsonstring = new String(buffer, "utf-8");
			is.close();
			JSONArray array = new JSONArray(jsonstring);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject objs = array.getJSONObject(i);
				if (objs.has("mergerName")) {
					String parentId = objs.getString("mergerName");
					if(!TextUtils.isEmpty(parentId)){
						String[] citys = parentId.split(",");
						if(citys.length==2){
							//省
							provinceModel = new ProvinceModel();
							provinceModel.setName(citys[1]);
							if(objs.has("ID")){
								String id = objs.getString("ID");
								provinceModel.setId(id);
							}
							//将市添加到省集合中
							List<CityModel> cityList = new ArrayList<CityModel>();
							provinceModel.setCityList(cityList);
							cityModel = null;
							provinceList.add(provinceModel);
						}else if(citys.length==3){ //市
							provinceModel = provinceList.get(provinceList.size()-1);
							//创建 市模型 
							cityModel = new CityModel();
							cityModel.setName(citys[2]);
							if(objs.has("ID")){
								String id = objs.getString("ID");
								cityModel.setId(id);
							}
							//将县添加到 市集合中
							List<DistrictModel> districtList = new ArrayList<DistrictModel>();
							cityModel.setDistrictList(districtList);
							provinceModel.getCityList().add(cityModel);
						}else{
							DistrictModel distrimodel = new DistrictModel();
							distrimodel.setName(citys[3]);
							if (objs.has("ID")){
								String id = objs.getString("ID");
								distrimodel.setId(id);
							}
							provinceModel = provinceList.get(provinceList.size()-1);
							List<CityModel> cityslst = provinceModel.getCityList();
							cityslst.get(cityslst.size()-1).getDistrictList().add(distrimodel);
						}
					}
				}
			}
			
		} catch (Exception e) {

		}
	}

}
