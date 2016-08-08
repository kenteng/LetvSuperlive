package com.lesports.stadium.danmu.library.danmaku.parser.android;



import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import com.lesports.stadium.danmu.library.danmaku.model.BaseDanmaku;
import com.lesports.stadium.danmu.library.danmaku.model.Duration;
import com.lesports.stadium.danmu.library.danmaku.model.android.Danmakus;
import com.lesports.stadium.danmu.library.danmaku.parser.BaseDanmakuParser;
import com.lesports.stadium.danmu.library.danmaku.parser.DanmakuFactory;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.FaceUtils;
import com.lesports.stadium.utils.GlobalParams;

public class AcFunDanmakuParser extends BaseDanmakuParser {

	@Override
	public Danmakus parse() {
		if (mDataSource != null && mDataSource instanceof JSONSource) {
			JSONSource jsonSource = (JSONSource) mDataSource;
			return doParse(jsonSource.data());
		}
		return new Danmakus();
	}

	/**
	 * @param danmakuListData
	 *            弹幕数据 传入的数组内包含普通弹幕，会员弹幕，锁定弹幕。
	 * @return 转换后的Danmakus
	 */
	private Danmakus doParse(JSONArray danmakuListData) {
		Danmakus danmakus = new Danmakus();
		if (danmakuListData == null || danmakuListData.length() == 0) {
			return danmakus;
		}
		Log.i("wxn", "获取数据："+danmakuListData.length());
		for (int i = 0; i < danmakuListData.length(); i++) {
			try {
				JSONObject danmakuArray = danmakuListData.getJSONObject(i);
				if (danmakuArray != null) {
					danmakus = _parse(danmakuArray, danmakus);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return danmakus;
	}

	private  Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public  Danmakus _parse(JSONObject jsonObject, Danmakus danmakus) {
		boolean isContentFace = false;
		if (danmakus == null) {
			danmakus = new Danmakus();
		}
		if (jsonObject == null || jsonObject.length() == 0) {
			return danmakus;
		}
		try {
			JSONObject obj = jsonObject;
			String content = obj.getString("content");
			BaseDanmaku item = DanmakuFactory
					.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
			if (content.startsWith("[") && content.endsWith("]"))
				content = content + " ";
			SpannableStringBuilder value = SpannableStringBuilder
					.valueOf(content);
			Matcher localMatcher = EMOTION_URL.matcher(value);
			while (localMatcher.find()) {
				String str2 = localMatcher.group(0);
				int k = localMatcher.start();
				int m = localMatcher.end();
				if (m - k < 8) {
					if (FaceUtils.getFaceMap().containsKey(str2)) {
						isContentFace = true;
						int face = FaceUtils.getFaceMap().get(str2);
						Bitmap bitmap = BitmapFactory.decodeResource(
								GlobalParams.context.getResources(), face);
						Log.i("wxn", "....bitmap"+bitmap);
						if (bitmap != null) {
							 if (true) {
								 int rawHeigh = bitmap.getHeight();
								 int rawWidth = bitmap.getHeight();
								 int newHeight =
								 DensityUtil.dip2px(GlobalParams.context,
								 24);
								 int newWidth =
								 DensityUtil.dip2px(GlobalParams.context,
								 24);
								 // 计算缩放因子
								 float heightScale = ((float) newHeight) /
								 rawHeigh;
								 float widthScale = ((float) newWidth) /
								 rawWidth;
								 // 新建立矩阵
								 Matrix matrix = new Matrix();
								 matrix.postScale(heightScale, widthScale);
								 bitmap = Bitmap.createBitmap(bitmap, 0, 0,
								 rawWidth, rawHeigh, matrix, true);
							 }
							ImageSpan localImageSpan = new ImageSpan(
									GlobalParams.context, bitmap,
									ImageSpan.ALIGN_BASELINE);
							value.setSpan(localImageSpan, k, m,
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
			if(isContentFace){
				//包含表情
				if (item != null) {
					if(danmakus.size()==0){
						item.time = 0;
						GlobalParams.SHOW_TIMER = 0;
					}else {
						GlobalParams.SHOW_TIMER +=50;
						item.time = (GlobalParams.SHOW_TIMER) * 10;
					}
					item.textShadowColor = Color.WHITE;
					item.index = danmakus.size();
					item.setTimer(mTimer);
					item.textSize = 18f * (mDispDensity - 0.6f);
					item.textSize = 10;
					item.duration = new Duration(8* 1000);
					item.text = value;
					item.priority = 1;
					item.textShadowColor = 0;
					item.underlineColor = 0;
					item.textColor = Color.WHITE;
					item.padding = 5;
					danmakus.addItem(item);
				}
			} else {
				int type = 1; // 弹幕类型
				item = DanmakuFactory.createDanmaku(type, mDisp);
				if (item != null) {
					if(danmakus.size()==0){
						item.time = 0;
						GlobalParams.SHOW_TIMER = 0;
					}else {
						GlobalParams.SHOW_TIMER +=50;
						item.time = (GlobalParams.SHOW_TIMER) * 10;
					}
					item.index = danmakus.size();
					item.setTimer(mTimer);
					item.textShadowColor = Color.WHITE;
					item.textColor = Color.WHITE;
					item.textSize = 18f * (mDispDensity - 0.6f);
					item.duration = new Duration(8 * 1000);
					item.text = content;
					item.priority = 1;
					danmakus.addItem(item);
				}
			}
		} catch (Exception e) {
		} 
		return danmakus;
	}

}
