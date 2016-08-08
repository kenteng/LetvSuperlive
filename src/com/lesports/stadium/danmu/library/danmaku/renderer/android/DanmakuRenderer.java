package com.lesports.stadium.danmu.library.danmaku.renderer.android;


import com.lesports.stadium.danmu.library.controller.DanmakuFilters;
import com.lesports.stadium.danmu.library.danmaku.model.BaseDanmaku;
import com.lesports.stadium.danmu.library.danmaku.model.DanmakuTimer;
import com.lesports.stadium.danmu.library.danmaku.model.IDanmakuIterator;
import com.lesports.stadium.danmu.library.danmaku.model.IDanmakus;
import com.lesports.stadium.danmu.library.danmaku.model.IDisplayer;
import com.lesports.stadium.danmu.library.danmaku.renderer.IRenderer;
import com.lesports.stadium.danmu.library.danmaku.renderer.IRenderer.RenderingState;
import com.lesports.stadium.danmu.library.danmaku.renderer.Renderer;


public class DanmakuRenderer extends Renderer {

    private final DanmakuTimer mStartTimer = new DanmakuTimer();
    private final RenderingState mRenderingState = new RenderingState();
    private DanmakusRetainer.Verifier mVerifier;
    private final DanmakusRetainer.Verifier verifier = new DanmakusRetainer.Verifier() {
        @Override
        public boolean skipLayout(BaseDanmaku danmaku, float fixedTop, int lines, boolean willHit) {
            if (danmaku.priority == 0 && DanmakuFilters.getDefault().filterSecondary(danmaku, lines, 0, mStartTimer, willHit)) {
                danmaku.setVisibility(false);
                return true;
            }
            return false;
        }
    };

    @Override
    public void clear() {
        DanmakusRetainer.clear();
        DanmakuFilters.getDefault().clear();
    }

    @Override
    public void release() {
        DanmakusRetainer.release();
        DanmakuFilters.getDefault().clear();
    }

    @Override
    public void setVerifierEnabled(boolean enabled) {
        mVerifier = (enabled ? verifier : null);
    }

    @Override
    public RenderingState draw(IDisplayer disp, IDanmakus danmakus, long startRenderTime) {
        int lastTotalDanmakuCount = mRenderingState.totalDanmakuCount;
        mRenderingState.reset();       
        IDanmakuIterator itr = danmakus.iterator();
        int orderInScreen = 0;        
        mStartTimer.update(System.currentTimeMillis());
        int sizeInScreen = danmakus.size();
        BaseDanmaku drawItem = null;
        while (itr.hasNext()) {

            drawItem = itr.next();
            
            if (drawItem.isLate()) {
                break;
            }

            if (!drawItem.hasPassedFilter()) {
                DanmakuFilters.getDefault().filter(drawItem, orderInScreen, sizeInScreen, mStartTimer, false);
            }

            if (drawItem.time < startRenderTime
                    || (drawItem.priority == 0 && drawItem.isFiltered())) {
                continue;
            }
            
            if (drawItem.getType() == BaseDanmaku.TYPE_SCROLL_RL){
                // 同屏弹幕密度只对滚动弹幕有效
                orderInScreen++;
            }

            // measure
            if (!drawItem.isMeasured()) {
                drawItem.measure(disp);
            }

            // layout
            DanmakusRetainer.fix(drawItem, disp, mVerifier);

            // draw
            if (!drawItem.isOutside() && drawItem.isShown()) {
                if (drawItem.lines == null && drawItem.getBottom() > disp.getHeight()) {
                    continue;    // skip bottom outside danmaku
                }
                int renderingType = drawItem.draw(disp);
                if(renderingType == IRenderer.CACHE_RENDERING) {
                    mRenderingState.cacheHitCount++;
                } else if(renderingType == IRenderer.TEXT_RENDERING) {
                    mRenderingState.cacheMissCount++;
                }
                mRenderingState.addCount(drawItem.getType(), 1);
                mRenderingState.addTotalCount(1);
            }

        }
        
        mRenderingState.nothingRendered = (mRenderingState.totalDanmakuCount == 0);
        mRenderingState.endTime = drawItem != null ? drawItem.time : RenderingState.UNKNOWN_TIME;
        if (mRenderingState.nothingRendered) {
            mRenderingState.beginTime = RenderingState.UNKNOWN_TIME;
        }
        mRenderingState.incrementCount = mRenderingState.totalDanmakuCount - lastTotalDanmakuCount;
        mRenderingState.consumingTime = mStartTimer.update(System.currentTimeMillis());
        return mRenderingState;
    }
    
}
