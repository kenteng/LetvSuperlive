package com.lesports.stadium.danmu.library.danmaku.model;


public class FBDanmaku extends FTDanmaku {

    public FBDanmaku(Duration duration) {
        super(duration);
    }

    @Override
    public int getType() {
        return TYPE_FIX_BOTTOM;
    }

}
