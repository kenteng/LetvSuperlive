package com.lesports.stadium.danmu.library.danmaku.model;


/**
 * 顶部固定弹幕
 */
public class FTDanmaku extends BaseDanmaku {

    private float x = 0;

    protected float y = -1;

    private float[] RECT = null;

    private float mLastLeft;

    private float mLastPaintWidth;

    private int mLastDispWidth;

    public FTDanmaku(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void layout(IDisplayer displayer, float x, float y) {
        if (mTimer != null) {
            long deltaDuration = mTimer.currMillisecond - time;
            if (deltaDuration > 0 && deltaDuration < duration.value) {
                if (!this.isShown()) {
                    this.x = getLeft(displayer);
                    this.y = y;
                    this.setVisibility(true);
                }
                return;
            }

            this.setVisibility(false);
            this.y = -1;
            this.x = displayer.getWidth();
        }

    }

    protected float getLeft(IDisplayer displayer) {
        if (mLastDispWidth == displayer.getWidth() && mLastPaintWidth == paintWidth) {
            return mLastLeft;
        }
        float left = (displayer.getWidth() - paintWidth) / 2;
        mLastDispWidth = displayer.getWidth();
        mLastPaintWidth = paintWidth;
        mLastLeft = left;
        return left;
    }

    @Override
    public float[] getRectAtTime(IDisplayer displayer, long time) {
        if (!isMeasured())
            return null;
        float left = getLeft(displayer);
        if (RECT == null) {
            RECT = new float[4];
        }
        RECT[0] = left;
        RECT[1] = y;
        RECT[2] = left + paintWidth;
        RECT[3] = y + paintHeight;
        return RECT;
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
        return TYPE_FIX_TOP;
    }
}
