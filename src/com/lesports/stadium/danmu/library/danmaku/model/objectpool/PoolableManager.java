package com.lesports.stadium.danmu.library.danmaku.model.objectpool;


public interface PoolableManager<T extends Poolable<T>> {
    T newInstance();

    void onAcquired(T element);

    void onReleased(T element);
}
