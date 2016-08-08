package com.lesports.stadium.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
	/**
	 * 对象非空判断
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			boolean empty = true;
			for (int i = 0; i < object.length; i++)
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			return empty;
		}
		return false;
	}
	/**
	 * 判断是否是数字并保留两位小数，doulbe型
	 */
	public static double parseTwoNumber(String number){
		String number2=number;
		if(isNumber(number)){
			if(isInteger(number)){
				number2=number+".00";
			}
			return roundForNumber(Double.parseDouble(number2), 3);
		}else{
			return Double.parseDouble(number2);
		}
	}
	 /**
     * 判断字符串是否是整数
     */
   public static boolean isInteger(String value) {
       try {
    	   Integer.parseInt(value);
    	   	return true;
	       } catch(NumberFormatException e) {
	           return false;
	       }
   }
    /**
        * 判断字符串是否是浮点数
        */
   public static boolean isDouble(String value) {
       try {
           Double.parseDouble(value);
           if (value.contains("."))
               return true;
           return false;
       } catch (NumberFormatException e) {
           return false;
       }
   }
   /**
    * 判断字符串是否是数字
    */
   public static boolean isNumber(String value) {
       return isInteger(value) || isDouble(value);
   }
   /**
    * 
    * 提供精确的小数位四舍五入处理。
    * 
    * @param v
    *            需要四舍五入的数字
    * 
    * @param scale
    *            小数点后保留几位
    * 
    * @return 四舍五入后的结果
    * 
    */

   public static double roundForNumber(double v,int scale){
       if(scale<0){
           throw new IllegalArgumentException("The scale must be a positive integer or zero");
       }
       BigDecimal b = new BigDecimal(Double.toString(v));
       BigDecimal one = new BigDecimal("1");
       return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
   }
   /**
	 * 强制隐藏输入法
	 */
	public static void hidenInputService(Context context,View view){
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(view.getWindowToken(),
		0);

	}
}
