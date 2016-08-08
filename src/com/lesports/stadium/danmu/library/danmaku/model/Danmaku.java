package com.lesports.stadium.danmu.library.danmaku.model;


import com.lesports.stadium.danmu.library.danmaku.parser.DanmakuFactory;

public class Danmaku extends BaseDanmaku {

    public Danmaku(String text) {
        DanmakuFactory.fillText(this, text);
    }

    @Override
    public boolean isShown() {
        return false;
    }

    @Override
    public void layout(IDisplayer displayer, float x, float y) {

    }

    @Override
    public float[] getRectAtTime(IDisplayer displayer, long time) {
        return null;
    }

    @Override
    public float getLeft() {
        return 0;
    }

    @Override
    public float getTop() {
        return 0;
    }

    @Override
    public float getRight() {
        return 0;
    }

    @Override
    public float getBottom() {
        return 0;
    }

    @Override
    public int getType() {
        return 0;
    }
}
