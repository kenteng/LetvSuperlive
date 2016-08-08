package com.lesports.stadium.utils;


import java.util.List;

import android.content.Context;

import com.lesports.stadium.bean.Address;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.letv.lepaysdk.R.string;

/**
 * 全局静态变量存储
 * 
 * @author Administrator
 * 
 */
public class GlobalParams {
	
	/**
	 * 屏幕宽度
	 */
	public static int WIN_WIDTH = 0;
	/**
	 * 屏幕高度
	 */
	public static int WIN_HIGTH = 0;
	
	/**
	 * 当前用户账号（id）
	 */
	public static String USER_ID ="175075642";
	
	/**
	 * 当前用户城市
	 */
	public static String USER_ADDRESS ="";
	
	/**
	 * 当前用户头像
	 */
	public static String USER_HEADER ="";
	/**
	 * 当前用户名称
	 */
	public static String USER_NAME ="";
	/**
	 * 当前用户性别
	 */
	public static String USER_GENDER ="";
	/**
	 * 当前用户昵称
	 */
	public static String NICK_NAME ="";
	/*
	 * 当前用户手机号
	 */
	public static String USER_PHONE ="";
	/**
	 * 当前用户邮箱
	 */
	public static String USER_EMIL ="";

	/**
	 * 用来记录的再次打开的mainactivity
	 */
	public static Context context = null;
	
	/**
	 * 判断互动跳转，我要上颜值
	 */
	public static boolean IS_SHOWFACE_HINT=false;
	/**
	 * 无网络时是否提示用户，默认提示一次后就不在提示
	 */
	public static boolean IS_SHOW_NOWORK = true;
	/**
	 * 分享weib连接
	 */
	public static String WEB_URL = "";
	/**
	 * 分享url
	 */
	public static String SHAR_IMAGE_URL = "";
	/**
	 * 分享内容
	 */
	public static String SHAR_CONTENT ="";
	/**
	 * 分享标题
	 */
	public static String SHAR_TITLE = "";
	/**
	 * 记住每次送礼物的索引 为了 展示上次送的是什么礼物
	 */
	public static int  GIFT_INDEX = 0;
	/**
	 * 用户积分
	 */
	public static int USER_INTEGRAL = 0;
	/**
	 * 获取当前位置
	 */
	public static Address MY_LOCATION = null;
	/**
	 * 餐饮商家地址
	 */
	public static String Online_Address="";
	/**
	 * 商品自取商家地址
	 */
	public static String Goods_Address="";
	/**
	 * 商品自取说明
	 */
	public static String Goods_Remark="";
	/**
	 * 餐饮商品自取商家名称
	 */
	public static String  Online_Name="";
	/**
	 * 餐饮商家电话号码
	 */
	public static String Online_Phone="";
	/**
	 * 商品自取商家名称
	 */
	public static String Goods_Store_Name="";
	/**
	 * 商品自取商家电话
	 */
	public static String Goods_Store_Phone="";
	/**
	 * 是否安装微信
	 */
	public static boolean ISINSTALL_WX = false;
	/**
	 * 是否安装QQ
	 */
	public static boolean ISINSTALL_QQ = false;
	/**
	 * 是否安装新浪微博
	 */
	public static boolean ISINSTALL_SN = false;
	
	/**
	 * 订单页面自取电话
	 */
	public static String ORDER_ZIQU_PHONE="";
	/**
	 * 订单页面自取姓名
	 */
	public static String ORDER_ZIQU_NAME="";
	/**
	 * 订单页面自取地址
	 */
	public static String ORDER_ZIQU_ADDRESS="";
	/**
	 * 订单页面送货电话
	 */
	public static String ORDER_SONGHUO_PHONE="";
	/**
	 * 订单页面送货地址
	 */
	public static String ORDER_SONGHUO_ADDRESS="";
	/**
	 * 订单送货名称
	 */
	public static String ORDER_SONGHUO_NAME="";
	/**
	 * 订单页面邮寄姓名
	 */
	public static String ORDER_YOUJI_NAME="";
	/**
	 * 订单页面邮寄地址
	 */
	public static String ORDER_YOUJI_ADDRESS="";
	/**
	 * 订单页面邮寄电话
	 */
	public static String ORDER_YOUJI_PHONE="";
	/**
	 * 餐饮支付页面的商家id
	 */
	public static String ONLINE_STORE_ID="";
	/**
	 * 数据库中该用户的商品集合
	 */
	public static List<RoundGoodsBean> SQL_USE_GOODS_LIST_YES=null;
	/**
	 * 数据库中的商品集合
	 */
	public static List<RoundGoodsBean> SQL_USE_GOODS_LIST_NO=null;
	/**
	 * 弹幕每个显示的时间
	 */
	public static long SHOW_TIMER = 0;
	/**
	 * 众筹详情图片地址
	 */
	public static String ZHONGCHOU_DETAIL_IMAGE="";
	/**
	 * 重酬订单图片地址
	 */
	public static String ZHONGCHOU_ORDER_IMAGE="";
	
	/**
	 * 保存七牛服务器上传文件 token值
	 */
	public static String QN_TOKEN = "";
	/**
	 * 用于修改是否给乐视测试，true为测试版本，false为开发版本
	 */
	public static final boolean CESHI_YIDAO=false;
	/**
	 * 用于网络请求时候的tooken
	 */
	public static String SSO_TOKEN="";
	
	public static boolean IS_REFRESH = true;
	/**
	 * 当前是否是体育类型 默认 false 在活动详情界面，我要尖叫界面 只有体育才能显示的功能
	 */
	public static boolean IS_SPORT = false;
	
	/**
	 * 当前城市id 默认是北京
	 */
	public static String CITY_ID = "110100";
	
	/**
	 * 当前弹幕开关状态
	 */
	public static boolean Switch = false;
	/**
	 * 当前用户选择的场馆id
	 */
	public static String VANUE_ID="";
	/**
	 * 最大音量
	 */
	public static int MAX_VOICE = 0;
	

}
