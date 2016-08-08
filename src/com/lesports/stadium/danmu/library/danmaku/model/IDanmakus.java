package com.lesports.stadium.danmu.library.danmaku.model;


public interface IDanmakus {

    public boolean addItem(BaseDanmaku item);

    public boolean removeItem(BaseDanmaku item);
    
    public IDanmakus subnew(long startTime, long endTime);

    public IDanmakus sub(long startTime, long endTime);

    public int size();

    public void clear();
    
    public BaseDanmaku first();
    
    public BaseDanmaku last();
    
    public IDanmakuIterator iterator();
    
    public boolean contains(BaseDanmaku item);

    public boolean isEmpty();
    
    public void setSubItemsDuplicateMergingEnabled(boolean enable);

}
