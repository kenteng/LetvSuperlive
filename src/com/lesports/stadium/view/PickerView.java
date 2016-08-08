package com.lesports.stadium.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.lesports.stadium.R;




public class PickerView extends View {

    public static final String TAG = "PickerView";
 //控制字符的间距
    public static final float MARGIN_ALPHA = 2.0f;
 
    public static final float SPEED = 2;

    private List<String> mDataList;

    private int mCurrentSelected;
    private Paint mPaint, nPaint;

    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mColorText = 0x333333;
    @SuppressWarnings("unused")
	private int nColorText = 0x666666;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    @SuppressLint("HandlerLeak")
	Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            invalidate();
        }

    };
    private boolean canScroll = true;

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    public void setData(List<String> datas) {
        mDataList = datas;
        mCurrentSelected = datas.size() / 4;
        invalidate();
    }

 
    public void setSelected(int selected) {
        mCurrentSelected = selected;
        int distance = mDataList.size() / 2 - mCurrentSelected;
        if (distance < 0)
            for (int i = 0; i < -distance; i++) {
                moveHeadToTail();
                mCurrentSelected--;
            }
        else if (distance > 0)
            for (int i = 0; i < distance; i++) {
                moveTailToHead();
                mCurrentSelected++;
            }
        invalidate();
    }


    public void setSelected(String mSelectItem) {
        for (int i = 0; i < mDataList.size(); i++)
            if (mDataList.get(i).equals(mSelectItem)) {
                setSelected(i);
                break;
            }
    }


    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        //字体大小控制
        mMaxTextSize = mViewHeight /10.0f;
        mMinTextSize = mMaxTextSize / 1.0f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setStyle(Style.FILL);
        nPaint.setTextAlign(Align.CENTER);
        nPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit)
            drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen-mViewHeight/5);
        FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }

    }


    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        nPaint.setTextSize(size);
        nPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        //位置控制
        float y = (float) (mViewHeight / 2.0 + type * d-mViewHeight/5);
        FontMetricsInt fmi = nPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, nPaint);
    }


    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {

        mMoveLen += (event.getY() - mLastDownY);

        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }

        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    public interface onSelectListener {
        void onSelect(String text);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (canScroll)
            return super.dispatchTouchEvent(event);
        else
            return false;
    }
}
