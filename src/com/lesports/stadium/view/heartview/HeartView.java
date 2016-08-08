
package com.lesports.stadium.view.heartview;

import com.lesports.stadium.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * ***************************************************************
 * @ClassName:  HeartView
 * 
 * @Desc : 心型图形
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-2-2 上午11:53:09
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class HeartView extends ImageView {

    private static final Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int mHeartResId = R.drawable.lov1;
    private int mHeartBorderResId = R.drawable.heart_border;
    private static Bitmap sHeart;
    private static Bitmap sHeartBorder;
    private static final Canvas sCanvas = new Canvas();

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeartView(Context context) {
        super(context);
    }

    public void setColor(int color) {
        Bitmap heart = createHeart(color);
        setImageDrawable(new BitmapDrawable(getResources(), heart));
    }

    public void setColorAndDrawables(int color, int heartResId, int heartBorderResId) {
        if (heartResId != mHeartResId) {
            sHeart = null;
        }
        if (heartBorderResId != mHeartBorderResId) {
            sHeartBorder = null;
        }
        mHeartResId = heartResId;
        mHeartBorderResId = heartBorderResId;
        setColor(color);
    }

    public Bitmap createHeart(int color) {
        if (sHeart == null) {
            sHeart = BitmapFactory.decodeResource(getResources(), mHeartResId);
        }
        if (sHeartBorder == null) {
            sHeartBorder = BitmapFactory.decodeResource(getResources(), mHeartResId);
        }
        Bitmap heart = sHeart;
        Bitmap heartBorder = sHeartBorder;
        Bitmap bm = createBitmapSafely(heartBorder.getWidth(), heartBorder.getHeight());
        if (bm == null) {
            return null;
        }
        Canvas canvas = sCanvas;
        canvas.setBitmap(bm);
        Paint p = sPaint;
        canvas.drawBitmap(heartBorder, 0, 0, p);
        p.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        float dx = (heartBorder.getWidth() - heart.getWidth()) / 2f;
        float dy = (heartBorder.getHeight() - heart.getHeight()) / 2f;
        canvas.drawBitmap(heart, dx, dy, p);
        p.setColorFilter(null);
        canvas.setBitmap(null);
        return bm;
    }

    private static Bitmap createBitmapSafely(int width, int height) {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
    }

}
