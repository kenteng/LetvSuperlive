package com.lesports.stadium.danmu.library.danmaku.parser;


import com.lesports.stadium.danmu.library.danmaku.model.DanmakuTimer;
import com.lesports.stadium.danmu.library.danmaku.model.IDanmakus;
import com.lesports.stadium.danmu.library.danmaku.model.IDisplayer;

/**
 *
 */
public abstract class BaseDanmakuParser {
    protected IDataSource<?> mDataSource;

    protected DanmakuTimer mTimer;
    protected int mDispWidth;
    protected int mDispHeight;
    protected float mDispDensity;
    protected float mScaledDensity;

    private IDanmakus mDanmakus;

    protected IDisplayer mDisp;
    
    public BaseDanmakuParser setDisplayer(IDisplayer disp){
        mDisp = disp;
    	mDispWidth = disp.getWidth();
        mDispHeight = disp.getHeight();
        mDispDensity = disp.getDensity();
        mScaledDensity = disp.getScaledDensity();
        DanmakuFactory.updateViewportState(mDispWidth, mDispHeight, getViewportSizeFactor());
        DanmakuFactory.updateMaxDanmakuDuration();
        return this;
    }
    
    /**
     * decide the speed of scroll-danmakus
     * @return
     */
    protected float getViewportSizeFactor() {
        return 1 / (mDispDensity - 0.6f);
    }

    public IDisplayer getDisplayer(){
        return mDisp;
    }
    
    public BaseDanmakuParser load(IDataSource<?> source) {
        mDataSource = source;
        return this;
    }
    
    public BaseDanmakuParser setTimer(DanmakuTimer timer) {
        mTimer = timer;
        return this;
    }

    public DanmakuTimer getTimer() {
        return mTimer;
    }
    
    public IDanmakus getDanmakus() {
        if (mDanmakus != null)
            return mDanmakus;
        DanmakuFactory.resetDurationsData();
        mDanmakus = parse();
        releaseDataSource();
        DanmakuFactory.updateMaxDanmakuDuration();
        return mDanmakus;
    }
    
    protected void releaseDataSource() {
        if(mDataSource!=null)
            mDataSource.release();
        mDataSource = null;
    }

    protected abstract IDanmakus parse();

    public void release() {
        releaseDataSource();
    }

}
