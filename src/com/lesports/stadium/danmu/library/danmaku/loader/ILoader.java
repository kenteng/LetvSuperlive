package com.lesports.stadium.danmu.library.danmaku.loader;


import java.io.InputStream;

import com.lesports.stadium.danmu.library.danmaku.parser.IDataSource;

public interface ILoader {
    /**
     * @return data source
     */
    IDataSource<?> getDataSource();
    /**
     * @param uri 弹幕文件地址(http:// file://)
     */
    void load(String uri) throws IllegalDataException;
    /**
     * 
     * @param in stream from Internet or local file
     */
    void load(InputStream in) throws IllegalDataException;
}
