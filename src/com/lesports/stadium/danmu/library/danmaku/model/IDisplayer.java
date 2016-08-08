package com.lesports.stadium.danmu.library.danmaku.model;



public interface IDisplayer {

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract float getDensity();

    public abstract int getDensityDpi();

    public abstract int draw(BaseDanmaku danmaku);

    public abstract float getScaledDensity();

    public abstract int getSlopPixel();

    public abstract void measure(BaseDanmaku danmaku);

    public abstract float getStrokeWidth();

    public abstract void setHardwareAccelerated(boolean enable);

    public abstract boolean isHardwareAccelerated();

    public abstract int getMaximumCacheWidth();

    public abstract int getMaximumCacheHeight();


    ////////////////// setter ///////////////////////////

    public abstract void resetSlopPixel(float factor);

    public abstract void setDensities(float density, int densityDpi, float scaledDensity);

    public abstract void setSize(int width, int height);

}
