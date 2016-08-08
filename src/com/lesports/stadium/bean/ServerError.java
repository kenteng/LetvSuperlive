package com.lesports.stadium.bean;

import com.letv.component.core.http.bean.LetvBaseBean;

public class ServerError implements LetvBaseBean {
    
    private static final long serialVersionUID = 3926978732747166682L;
    
    /** 错误代码 */
    public int errorCode;
    
    /** 错误提示信息 (服务器返回)*/
    public String errorMessage;

}
