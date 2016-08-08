package com.lesports.stadium.utils;


import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @ClassName:  BeanFactory   
 * @Description:工厂模式 加载业务   
 * @author: 王新年 
 * @date:   2015-12-28 下午5:43:43   
 *
 */
public class BeanFactory {
	private static Properties properties;
	static {
		properties = new Properties();
		try {
			properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取配置文件中指定的实例对象
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T>  T getImpl(Class<T> clazz) {
//		String name = properties.getProperty("UserEngine");
		String name = properties.getProperty(clazz.getSimpleName());

		try {
			return (T) Class.forName(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
