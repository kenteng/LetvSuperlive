package com.lesports.stadium.danmu.library.danmaku.loader.android;



import java.io.InputStream;

import com.lesports.stadium.danmu.library.danmaku.loader.ILoader;
import com.lesports.stadium.danmu.library.danmaku.loader.IllegalDataException;
import com.lesports.stadium.danmu.library.danmaku.parser.android.AndroidFileSource;

public class BiliDanmakuLoader implements ILoader {

    private static BiliDanmakuLoader _instance;

    private AndroidFileSource dataSource;

    private BiliDanmakuLoader() {

    }

    public static BiliDanmakuLoader instance() {
        if (_instance == null) {
            _instance = new BiliDanmakuLoader();
        }
        return _instance;
    }

    public void load(String uri) throws IllegalDataException {
        try {            
            dataSource = new AndroidFileSource(uri);
        } catch (Exception e) {
        	throw new IllegalDataException(e);
        }
    }

    public void load(InputStream stream) {
        dataSource = new AndroidFileSource(stream);
    }

    @Override
    public AndroidFileSource getDataSource() {
        return dataSource;
    }
}
