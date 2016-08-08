package com.lesports.stadium.danmu.library.danmaku.model;


public class DanmakuTimer {
    public long currMillisecond;

    private long lastInterval;

    public long update(long curr) {
        lastInterval = curr - currMillisecond;
        currMillisecond = curr;
        return lastInterval;
    }

    public long add(long mills) {
        return update(currMillisecond + mills);
    }

    public long lastInterval() {
        return lastInterval;
    }

}
