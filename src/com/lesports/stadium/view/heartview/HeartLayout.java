
package com.lesports.stadium.view.heartview;

import com.lesports.stadium.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * ***************************************************************
 * @ClassName:  HeartLayout
 * 
 * @Desc : 心型布局
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
public class HeartLayout extends RelativeLayout {

    private AbstractPathAnimator mAnimator;
    private AbstractPathAnimatorgift mGiftAnimator;

    public HeartLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);
        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a));
        mGiftAnimator= new PathAnimatorGift(AbstractPathAnimatorgift.Config.fromTypeArray(a));
        a.recycle();
    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }
    public AbstractPathAnimatorgift getAnimatorGift(){
    	return mGiftAnimator;
    }
    public void setAnimatro(AbstractPathAnimatorgift animator){
    	clearAnimation();
    	mGiftAnimator=animator;
    }
    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void addHeart(int color) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColor(color);
        mAnimator.start(heartView, this);
    }

    public void addHeart(int color, int heartResId, int heartBorderResId) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
        mAnimator.start(heartView, this);
    }
    public void addGiftHeart(int color) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColor(color);
        mGiftAnimator.start(heartView, this);
    }

    public void addGiftHeart(int color, int heartResId, int heartBorderResId) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
        mGiftAnimator.start(heartView, this);
    }

}
