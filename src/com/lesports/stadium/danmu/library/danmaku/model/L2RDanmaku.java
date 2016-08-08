package com.lesports.stadium.danmu.library.danmaku.model;


public class L2RDanmaku extends R2LDanmaku {

    public L2RDanmaku(Duration duration) {
        super(duration); 
    }
    
    @Override
    public void layout(IDisplayer displayer, float x, float y) {
        if (mTimer != null) {
            long currMS = mTimer.currMillisecond;
            long deltaDuration = currMS - time;
            if (deltaDuration > 0 && deltaDuration < duration.value) {
                this.x = getAccurateLeft(displayer, currMS);
                if (!this.isShown()) {
                    this.y = y;
                    this.setVisibility(true);
                }
                mLastTime = currMS;
                return;
            }
            mLastTime = currMS;
        }
        this.setVisibility(false);
    }
    
    @Override
    public float[] getRectAtTime(IDisplayer displayer, long time) {
        if (!isMeasured())
            return null;
        float left = getAccurateLeft(displayer, time);
        if (RECT == null) {
            RECT = new float[4];
        }
        RECT[0] = left;
        RECT[1] = y;
        RECT[2] = left + paintWidth;
        RECT[3] = y + paintHeight;
        return RECT;
    }

    protected float getAccurateLeft(IDisplayer displayer, long currTime) {
        long elapsedTime = currTime - time;
        if (elapsedTime >= duration.value) {
            return displayer.getWidth();
        }
        return mStepX * elapsedTime - paintWidth;
    }

    @Override
    public float getLeft() {
        return x;
    }

    @Override
    public float getTop() {
        return y;
    }

    @Override
    public float getRight() {
        return x + paintWidth;
    }

    @Override
    public float getBottom() {
        return y + paintHeight;
    }

    @Override
    public int getType() {
        return TYPE_SCROLL_LR;
    }

}
