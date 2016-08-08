package com.lesports.stadium.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CardAnim.DrawOffAnimListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class CardView extends FrameLayout {

    private static int DEFLAUT_CARDSIZE = 3;
    private static int DEFLAUT_ITEMSACE =5;
    // cache大小
    private static int DEFALUT_CARDCACHE = 0;
    private float mTouchSlop;
    private CardClickListener cardClickListener;

    public interface CardClickListener {
        public void onClick(View view, int pos);
    }

    public static int topPos = 0;

    public void setCardClickListener(CardClickListener cardClickListener) {
        this.cardClickListener = cardClickListener;
    }

    /**
     * 卡片显示的最大数量
     */
    private int mMaxCardSize = DEFLAUT_CARDSIZE;

    /**
     * 卡片的间隔
     */
    private int itemSpace = DensityUtil.dip2px(GlobalParams.context,DEFLAUT_ITEMSACE);


    /**
     * 数据适配器
     */
    private BaseAdapter adapter;


    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context) {
        super(context);
        init();
    }

    private void init() {
        ViewConfiguration con = android.view.ViewConfiguration.get(getContext());
        mTouchSlop = con.getScaledTouchSlop();
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        removeAllViews();
        fullView();
    }

    public int getmMaxCardSize() {
        return mMaxCardSize;
    }

    public void setmMaxCardSize(int mMaxCardSize) {
        this.mMaxCardSize = mMaxCardSize;
    }

    public int getItemSpace() {
        return itemSpace;
    }

    public void setItemSpace(int itemSpace) {
        this.itemSpace = itemSpace;
    }
    /**
     * 是用来放置缓存的View的
     */
    private ArrayList<View> cardPoll = new ArrayList<View>();

    private ArrayList<View> cacheCardPoll = new ArrayList<View>();

    private int mNextAdapterPosation = 0;
    private int allMargin;
    /**
     * 填充控件
     */
    private synchronized void fullView() {

        while (mNextAdapterPosation < adapter.getCount() && cardPoll.size() < mMaxCardSize + DEFALUT_CARDCACHE) {
            int index = mNextAdapterPosation % mMaxCardSize;
            View converView = cacheCardPoll.size() > 0 ? cacheCardPoll.get(0) : null;
            if (converView != null) {
                cacheCardPoll.remove(0);
            }
            final View card = adapter.getView(mNextAdapterPosation, converView, this);
            cardPoll.add(card);
            index = Math.min(mNextAdapterPosation, mMaxCardSize - 1);

            if (card.getWidth() == 0) {
                card.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                    

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {
                        card.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int count = cardPoll.size();
                        allMargin = (mMaxCardSize - 1 ) * itemSpace * 2;
                        for (int i = 0; i < count; i++) {
                            makeLayoutChild(cardPoll.get(i), i);
                        }
                    }

                });
            } else {
                android.util.Log.i("dfd", "dfd" + index);
                makeLayoutChild(card, index);
            }

            FrameLayout.LayoutParams params = (LayoutParams) card.getLayoutParams();

            if (params == null) {
                params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            }

            addViewInLayout(card, 0, params);

            mNextAdapterPosation += 1;

        }

    }


    public void makeLayoutChild(View view, int pos) {

        if (pos > mMaxCardSize - 1) {
            pos = mMaxCardSize - 1;
            view.setVisibility(View.GONE);
        }else{
        	view.setVisibility(View.VISIBLE);
        }
        

        int topMargin = (mMaxCardSize - 1 - pos) * itemSpace * 2;
//        float viewScalePresentX = 1 - (pos * itemSpace * 2) / (float) view.getWidth();
//        float viewScalePresentY = 1 - (pos * itemSpace * 2) / (float) view.getHeight();
        ViewHelper.setTranslationX(view, 0);
//        if (pos == 0) {
//            ViewPropertyAnimator.animate(view).translationX(topMargin).translationY(topMargin).setDuration(300).start();
//            //.scaleX(viewScalePresentX).scaleY(viewScalePresentY)
//        } else {
//            ViewHelper.setScaleX(view, viewScalePresentX);
//            ViewHelper.setScaleY(view, viewScalePresentY);
            ViewHelper.setTranslationY(view, -topMargin);
            ViewHelper.setTranslationX(view, topMargin);
            float i=pos;
 
  //          Log.e("SSSSSSSSSSSSSSSSSS", (float) (1.0-i/5)+"");
            //设置透明度
            ViewHelper.setAlpha(view, (float) (1.0-i/5));
   
 //       }

    }

    private float downX, downY;
    private CardAnim cardAnim;

    @SuppressLint("NewApi")
    @Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        float currentY = ev.getY();
        float currentX = ev.getX();
        switch (ev.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();

                View topView = cardPoll.get(0);
                topView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                topView.setPivotX(Math.abs(downX - topView.getLeft()));
                topView.setPivotY(Math.abs(downY) - topView.getTop() - (mMaxCardSize - 1) * itemSpace);

                if (vTracker == null) {
                    vTracker = VelocityTracker.obtain();
                } else {
                    vTracker.clear();
                }
                cardAnim = new CardAnim();

                break;
            case MotionEvent.ACTION_MOVE:
                float distanceY = Math.abs(currentY - downY);
                float distanceX = Math.abs(currentX - downX);
                if (distanceY > mTouchSlop || distanceX > mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    private VelocityTracker vTracker;
    private float upY, upX;
    private float xVelocity, yVelocity;
    private float upYTrans, upXTrans;
    private float xMove;
    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        // TODO Auto-generated method stub

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                vTracker.addMovement(event);
                float moveX = event.getX();
//                float moveY = event.getY();
                xMove= moveX - downX;
                if(xMove>0){
                	ViewHelper.setTranslationX(cardPoll.get(0), moveX - downX);
                }
//                ViewHelper.setTranslationY(cardPoll.get(0), moveY - downY);
//                cardAnim.runZN(cardPoll.get(0), downX, downY);
                break;
            case MotionEvent.ACTION_UP:
                upY = event.getY();
                upX = event.getX();
                vTracker.addMovement(event);
                vTracker.computeCurrentVelocity(1000);
                xVelocity = vTracker.getXVelocity();
                yVelocity = vTracker.getYVelocity();
                upYTrans = ViewHelper.getTranslationY(cardPoll.get(0));
                upXTrans = ViewHelper.getTranslationX(cardPoll.get(0));

                if (xVelocity > 500 ) {
                	//|| Math.abs(yVelocity) > 500
                    goDown();
                } else {
 //               	cardAnim.Stop();
//                	ViewHelper.setTranslationX(cardPoll.get(0),-xMove);

                    ViewPropertyAnimator.animate(cardPoll.get(0)).translationY(-allMargin)
                            .translationX(allMargin).setDuration(500).setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());

//  ViewPropertyAnimator.animate(cardPoll.get(0)).translationY((mMaxCardSize - 1) * itemSpace * 2)
 //                   .translationX(1).setDuration(500).setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
//                    float aleryRo = cardPoll.get(0).getRotation();
//                    if (aleryRo < 120) {
//                        ObjectAnimator.ofFloat(cardPoll.get(0), "rotation", aleryRo, 0).setDuration(500).start();
//                    } else {
//                        ObjectAnimator.ofFloat(cardPoll.get(0), "rotation", aleryRo, 360).setDuration(500).start();
//                    }

                }


            default:
                break;
        }

        return super.onTouchEvent(event);
    }
    /**
     * 掉落替换View
     */
    @SuppressLint("NewApi")
    private void goDown() {
        View topView = cardPoll.get(0);
        cardPoll.remove(topView);
        topPos++;
        fullView();

        for (int i = 0; i < cardPoll.size(); i++) {
            View view = cardPoll.get(i);
          
            view.setRotation(0);
            makeLayoutChild(view, i);
        }

        cardAnim.setDrawOffAnimListener(new DrawOffAnimListener() {

            @Override
            public void onff(View view) {
                cacheCardPoll.add(view);
//                float viewScalePresentX = 1 - ((mMaxCardSize - 1) * (itemSpace * 2) / (float) view.getWidth());
//                float viewScalePresentY = 1 - ((mMaxCardSize - 1) * (itemSpace * 2) / (float) view.getHeight());
//                ViewHelper.setScaleX(view, viewScalePresentX);
//                ViewHelper.setScaleY(view, viewScalePresentY);

                view.setPivotX(view.getWidth() / 2);
                view.setPivotY(view.getHeight() / 2);
//				 
                removeView(view);

            }
        });
        
//        ObjectAnimator obj1 = ObjectAnimator.ofFloat(topView, "translationX", 0f,2000);
//        obj1.start();
      cardAnim.runOff(topView, xVelocity, 0, downX, 0, 0, upX, upXTrans,0);

//		removeView(topView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(true, left, top, right, bottom);
//        if(0!=cardPoll.size()){
//	        cardPoll.get(0).setOnClickListener(new OnClickListener() {
//	            @Override
//	            public void onClick(View v) {
//	                cardClickListener.onClick(cardPoll.get(0), topPos);
//	            }
//	        });
//        }

    }
}
