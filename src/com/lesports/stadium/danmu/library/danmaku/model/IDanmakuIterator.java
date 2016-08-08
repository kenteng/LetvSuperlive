package com.lesports.stadium.danmu.library.danmaku.model;


public interface IDanmakuIterator {

    public BaseDanmaku next();
    
    public boolean hasNext();
    
    public void reset();

    public void remove();
    
}
