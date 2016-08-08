package com.lesports.stadium.danmu.library.danmaku.parser;


public interface IDataSource<T> {
    String SCHEME_HTTP_TAG = "http";
    String SCHEME_HTTPS_TAG = "https";
    String SCHEME_FILE_TAG = "file";
    
	public T data();
	
    public void release();

}
