package com.lesports.stadium.danmu.library.controller;

import com.lesports.stadium.danmu.library.danmaku.model.AbsDisplayer;
import com.lesports.stadium.danmu.library.danmaku.model.BaseDanmaku;
import com.lesports.stadium.danmu.library.danmaku.parser.BaseDanmakuParser;
import com.lesports.stadium.danmu.library.danmaku.renderer.IRenderer.RenderingState;

public interface IDrawTask {

    public void addDanmaku(BaseDanmaku item);

    public void removeAllDanmakus();

    public void removeAllLiveDanmakus();

    public void clearDanmakusOnScreen(long currMillis);

    public RenderingState draw(AbsDisplayer<?> displayer);

    public void reset();

    public void seek(long mills);

    public void start();

    public void quit();

    public void prepare();

    public void requestClear();

    public void setParser(BaseDanmakuParser parser);

    public interface TaskListener {
        public void ready();

        public void onDanmakuAdd(BaseDanmaku danmaku);

        public void onDanmakuConfigChanged();
    }

    public void requestHide();

}
