package com.lesports.stadium.net;

public class Code {
	
	/**
	 * 广播Code
	 * @author shengzg
	 *
	 */
	public class BroadcastCode{
		/**
		 * 广场修改
		 */
		public static final String SHARE_BROADCAST_CODE="com.jzkj.cpp.share";
		
		/**
		 * 修改头像
		 */
		public static final String HEADER_BROADCAST_CODE="com.jzkj.cpp.header";
	}
	
  
  /**
   * 服务端code
   * @author shengzg
   *
   */
  public class ServiceCode{
      /**
       * 成功
       */
      public static final int SUCCESS=0;
      
      /**
       * Session 为空
       */
      public static final int SESSION_NULL=900;
      
      /**
       * Session错误或过期
       */
      public static final int SESSION_NOT_FOUND=901;
      
      /**
       * Header信息错误
       */
      public static final int HEADER_ERROR=910;
      
      /**
       * 鉴权码错误
       */
      public static final int SAFE_CODE_ERROR=920;
      
      /**
       * 系统错误
       */
      public static final int SYSTEM_ERROR=999;
      
      /**
       * 缺少参数
       */
      public static final int PARAMETER_LOST=800;
      
      /**
       * 用户已经注册
       */
      public static final int USER_REGISTED=100;
      /**
       * 用户名或密码错误
       */
      public static final int USER_WRONG = 700;
      
      /**
       * 用户尚未注册
       */
      public static final int USER_TNO_REGIS=101;
      
      /**
       * 不能频繁发送验证码
       */
      public static final int VERIFY_CODE_TIME_INTERVAL=102;

      /**
       * 验证码错误
       */
      public static final int VERIFY_CODE_ERROR=103;

      /**
       * 旧密码错误
       */
      public static final int OLD_PASSWORD_ERROR=104;
      
      /**
       * 网络异常
       */
      public static final int NETWORK_ERROR=10001;
      
      /**
       * 缓存解析失败
       */
      public static final int  RESOLVE_ERROR=10002;
  }
  
	
  /**
   * 数据返回类型
   */
   public class StructureType{
       /**
        * 服务端不返回任何内容
        */
       public static final int FMT_BLANK=0;
       
       /** 
        * 普通字符串，若有换行，换行符为\n
        */
       public static final int FMT_PLAIN=1;
       
       /**
        * JSON格式，换行符\n
        */
       public static final int FMT_JSON=2;
       
       /**
        *XML格式，换行符\n
        */
       public static final int FMT_XML=3;
       
       /**
        * 布尔值（保留）
        */
       public static final int FMT_BOOL=4;

       /**
        * 32位整数值（保留）
        */
       public static final int FMT_INT=5;
       
       /**
       64位长整形（保留）
       *
       */
       public static final int FMT_LONG=6;
       
       /**
       *GZIP压缩格式（位域）
       *
       */
       public static final int FMT_GZIP=16;
       
       /**
        *  数据内容已加密（位域）
        */
       public static final int  FMT_CRYPT=32;  

   }
   
   /**
    * 接口加载方式
    * @author shengzg
    *
    */
   public class LoadType{
	   /**
	    * 上拉加载
	    */
	   public static final String UP_LOAD="U";
	   
	   /**
	    * 下拉刷新
	    */
	   public static final String DOWN_LOAD="D";
	   
	   /**
	    * 正常
	    */
	   public static final String NORMAL_LOAD="N";
	   
	 
   }
  
    /**
     *Http请求设置 
     */
    public class HttpSetting{
        /**
         * 设置字符集
         */
        public static final String CHARSET="utf-8";
        
        /**
         * POST请求方式
         */
        public  static final String POST_MODE="POST";
        
        /**
         * GET请求方式
         */
        public static final String GET_MODE="GET";
        
        /**
         * 请求暗号
         */
        public static final String CIPHER="qinqingbao";
        
    }
    
   /**
    * 返回数据源类型 
    */
    public class DataResourceType{
    	
    	/**
    	 * 缓存返回
    	 */
    	public static final int CACHE_DATA=1;
    	
    	/**
    	 * 接口返回
    	 */
    	public static final int INTERFACE_DATA=2;
    }
    
    
	/**
	 * 输入框Code
	 * @author shengzg
	 *
	 */
	public class inputCode{
		/**
		 * 无
		 */
		public static final int NO_INPUT=0;
		
		/**
		 * 手机号输入框
		 */
		public static final int PHONE_CODE=1;
		
		/**
		 * 图片验证码输入框
		 */
		public static final int IMAGE_SECURITY_CODE=2;
		
		/**
		 * 短信验证码输入框
		 */
		public static final int SMS_SECURITY_CODE=3;
		
		/**
		 * 密码输入框
		 */
		public static final int PASSWORD_CODE=4;
		
		/**
		 * 用户名
		 */
		public static final int USER_NAME_CODE=5;
		/**
		 * 新密码输入框
		 */
		public static final int NEW_PASSWORD_CODE=6;
		/**
		 * 新密码再次输入框
		 */
		public static final int NEW_PASSWORD_AGAIN_CODE=7;
	}
	
	
    
    /**
     * HttpHeader
     **产品ID 
     **版本号
     **配置版本
     **渠道ID 
     **用户ID 
     **设备型号ID 
     **设备ID 
     **会话ID
     */
    public class HeaderMsg{
        public static final int APPID=100;
        public static final int VERSION_CODE=1;
        public static final int OTA_VERSION=1;
        public static final int PID=1;
        public static final int UID=0;   //10241  10159
        public static final int MID=1;
        public static final String DID="1";
        public static final String SID="1";
    }
    
    /**
	 * 设置内容类型
	 * @author shengzg
	 *
	 */
    public class SettingType{
		/**
		 * 只有左边有文字
		 */
		public static final int TEXT_LEFT=0;
		
		/**
		 * 两边都有文字
		 */
		public static final int TXT_LEFT_RIGHT=1;
		/**
		 * 文字按钮
		 */
		public static final int TEXT_BUTTON=2;
		
		/**
		 * 按钮
		 */
		public static final int BUTTON=3;
	}
    
    
    /**
	 * hanlderCode
	 * @author Administrator
	 *
	 */
	public class HandlerCode{
		/**
		 * 成功
		 */
		public static final int SUCCESS=0;
		
		/**
		 * 评论
		 */
		public static final int COMMENT=1;
		
		/**
		 * 视频播放
		 */
		public static final int PLAY_VIDEO=2;
		
		/**
		 * 发送广播
		 */
		public static final int SEND_BROADCAST=3;
		
		/**
		 * 展示软键盘
		 */
		public static final int SHOW_SOFT_INPUT=4;
		
		/**
		 * 刷新
		 */
		public static final int REFRESH=5;
		
		/**
		 * 删除评论
		 */
		public static final int COMMENT_DELETE=6;
		
		
		/**
		 * 失败
		 */
		public static final int ERROR=-1001;
		
		/**
		 * 网络异常
		 */
		public static final int NETWORK_ERROR=-1002;
		
		
		/**
		 * 用户不存在
		 */
		public static final int USER_NO_EXIST=-1003;
		
		/**
		 * 密码错误
		 */
		public static final int PASSWORD_ERROR=-1004;
		
		/**
		 * 密码错误
		 */
		public static final int USER_LOCKED=-1005;
		
		/**
		 * 密码为空
		 */
		public static final int PASSWORD_EMPTY=-1006;
		
		/**
		 * 手机号为空
		 */
		public static final int PHONE_EMPTY=-1007;
	}
	
	/**
	 * 网络状态
	 * @author Administrator
	 *
	 */
	public class NetworkStatus{
		/**
		 * 没有网络
		 */
		public static final int NO_NET=0;
		
		/**
		 * Wifi
		 */
		public static final int WIFI=1;
		
		/**
		 * 2G网
		 */
		public static final int TWO_G=2;
		
		/*
		 * 3G网
		 */
		public static final int THREE_G=3;
		
		/**
		 * 4G网
		 */
		public static final  int FOUR_G=4;
	}
	
	
	/**
	 * 图片存贮Code
	 * @author Administrator
	 *
	 */
	public class ImageDealWithCode{
		/**
		 * 成功
		 */
		public static final int SUCCESS=0;
		
		/**
		 *文件不存在
		 */
		public static final int FILE_NO_EXIST=-1;
		
		/**
		 * 内存溢出
		 */
		public static final int OUT_MEMORY=-2;
		
		/**
		 * IO错误
		 */
		public static final int IO_ERROR=-3;
	}
	
	/**
	 * 接口返回数据类型
	 * @author Administrator
	 *
	 */
	public class ServerDataType{
		/**
		 * 文本内容
		 */
		public static final int TEXT=1;
		
		/**
		 * json
		 */
		public static final int JSON=2;
		
	}
	/**
	 * 版本更新信息
	 * @author Administrator
	 *
	 */
	public class UpdateCode{
		/**
		 * 软件包升级(可选)
		 */
		public static final int CHOICE_UPDATE=1;
		
		/**
		 * 软件包强制升级
		 */
		public static final int FORCIBLY_UPDATE =2;
		
		
		/**
		 * 配置升级
		 */
       public static final int  CONFIG_UPDATE=3;
	}

}
