package com.lesports.stadium.danmu.library.danmaku.model.objectpool;


public interface Poolable<T> {
    void setNextPoolable(T element);

    T getNextPoolable();

    boolean isPooled();

    void setPooled(boolean isPooled);
}
