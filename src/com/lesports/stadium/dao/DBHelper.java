package com.lesports.stadium.dao;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
	private static final String NAME = "chat.db";

	private static final int START_VERSION = 1;
	private static final int HISTORY_VERSION = 2;// 历史版本
	private static final int CURRENT_VERSION = 2;
	private static ArrayList<String> beanlist = new ArrayList<String>();

	public DBHelper(Context context) {
		super(context, NAME, null, CURRENT_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 数据库版本管理
		if(beanlist.size()>0){
			for(String name :beanlist)
			db.execSQL(getCreateTableSQL(name));
			onUpgrade(db, START_VERSION, CURRENT_VERSION);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
			case START_VERSION:
				
			case HISTORY_VERSION:
				break;
		}
	}
	/*
	 * 将需要创建表的对象加入集合
	 */
	public static void  addBean(String name){
		beanlist.add(name);
	}
	
	private String getCreateTableSQL(String name){
//		Log.i("22", "建表语句开始执行。。");
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		@SuppressWarnings("rawtypes")
		Class clazz = loadChatBeanPropters(name);
//		Log.i("22", "建表语句..："+clazz.getSimpleName());
		@SuppressWarnings("unchecked")
		TableName tableName  = (TableName) clazz.getAnnotation(TableName.class);		
		if (tableName != null) {
			sb.append(tableName.value()+" (");
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			ID id = field.getAnnotation(ID.class);
			if(id!=null){
				String key = id.value();
				boolean isAutoincrement = id.autoincrement();
				if(isAutoincrement){
				   sb.append(key+" INTEGER PRIMARY KEY,");
				}else{
					sb.append(key+" INTEGER,");
				}
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				String key = column.value();
				sb.append(key);
				if (field.getType() == int.class) {
					sb.append(" INTEGER,");
				} else if (field.getType() == long.class) {
					sb.append(" INTEGER,");
				} else if (field.getType() == String.class){
					sb.append(" varchar,");
				}else if(field.getType() == boolean.class){
					sb.append(" INTEGER,");
				}else if(field.getType() == double.class){
					sb.append(" DOUBLE,");
				}else if(field.getType()== float.class){
					sb.append(" FLOAT,");
				}
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" );");
//		Log.i("22", "建表语句：" +sb.toString());
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static Class loadChatBeanPropters(String classSimpleName) {
		Properties uiConfig = new Properties();
		InputStream is = DBHelper.class.getClassLoader().getResourceAsStream(
				"config_chat.properties");
		try {
			uiConfig.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String chatBean = uiConfig.getProperty(classSimpleName);
		try {
//			Log.i("lwc","chatBean :"+chatBean+" classSimpleName :"+classSimpleName);
			return Class.forName(chatBean);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

}
