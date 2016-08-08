package com.lesports.stadium.dao;

import com.lesports.stadium.bean.ServiceCateringDetailBean;

import android.content.ContentValues;
import android.content.Context;


public class FoodBuyCar extends DAOSupport<ServiceCateringDetailBean> {

	public FoodBuyCar(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @param whereClause 修改条件
	 * @param whereArgs  修图条件的值
	 */
	public int update(ContentValues values,String whereClause, String[] whereArgs ){
		if(values==null)
			return 0;
		return database.update(getTableName(), values, whereClause, whereArgs);
	}


}
