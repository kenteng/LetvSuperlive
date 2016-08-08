package com.lesports.stadium.danmu.library.danmaku.model.objectpool;


public interface Pool<T extends Poolable<T>> {
    T acquire();

    void release(T element);
}
