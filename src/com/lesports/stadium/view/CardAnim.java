package com.lesports.stadium.view;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;


import android.view.View;

public class CardAnim {
    private View view;


    private float xVelocity;
    private float yVelocity;

 
    @SuppressWarnings("unused")
	private float downX, downY, upY, upX;

 
    private float upYTrans, upXTrans;

    private ValueAnimator valueAnimator;

    public CardAnim() {
        super();
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isRunOffAnim) {
                    drawOffAnim();
                }

                if (isRunZN) {
                    drawZnAnim();
                }

                if (isRunRotaionAnim) {
                    drawRotaionAnim();
                }
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(3000);
    }

    private long drawRoteAnimTime = 0;

    protected void drawRotaionAnim() {
        long time = System.currentTimeMillis() - drawRoteAnimTime;
        drawRoteAnimTime = System.currentTimeMillis();
        if (Math.abs(vRote) > 2) {

            if (vRote > 0) {
                vRote = 2;
            } else {
                vRote = -2;
            }
        }

        double addDegree = time * Math.abs(vRote);

        if (vRote > 0) {
            vRote = vRote - 0.01;
        } else {
            addDegree = addDegree * -1;
            vRote = vRote + 0.01;
        }

        ViewHelper.setRotation(view, (float) (view.getRotation() + addDegree));

    }


    private double W = 7;
    private double B = 0.5;
    private double vRote;


    protected void drawZnAnim() {
        double time = (System.currentTimeMillis() - runZNStartTime) / 1000d;

        float ax = pivoftX, bx = view.getWidth() / 2, cx = pivoftX;
        float ay = pivoftY, by = view.getHeight() / 2, cy = view.getHeight() / 2;
        double ac = Math.abs(ay - cy);
        double bc = Math.abs(bx - cx);
        double tan = Math.atan(bc / ac);

        double A = Math.toDegrees(Math.abs(tan));

        double degrees = A * Math.exp(-1 * B * time) * Math.cos(Math.sqrt(W * time));

        if (ax < bx && ay < by) {
            ViewHelper.setRotation(view, (float) (A - degrees));
        } else if (ax > bx && ay < by) {
            ViewHelper.setRotation(view, (float) (-(A - degrees)));
        } else if (ax > bx && ay > by) {
            A = 90 + (90 - A);
            double degrees2 = A * Math.exp(-1 * B * time) * Math.cos(Math.sqrt(W * time));
            ViewHelper.setRotation(view, (float) -(A - degrees2));
        } else {
            A = 90 + (90 - A);
            double degrees2 = A * Math.exp(-1 * B * time) * Math.cos(Math.sqrt(W * time));
            ViewHelper.setRotation(view, (float) (A - degrees2));
        }

        W = W + 0.2;

        vRote = (view.getRotation() - cacheDegree) / (System.currentTimeMillis() - vTime);
        vTime = System.currentTimeMillis();
        cacheDegree = view.getRotation();

    }

    private float cacheDegree = 0;
    private long vTime = 0;

    private boolean isRunRotaionAnim = false;

    protected void drawOffAnim() {
        double time = (System.currentTimeMillis() - runOffAnimStartTime) / 1000d;
        float heaveY = upYTrans - getHeaveY(time, yVelocity * -1);
//        ViewHelper.setTranslationY(view, heaveY);

        float heavex = getHeavex(time, Math.abs(xVelocity));
        if (upX - downX > 0) {
            heavex = upXTrans + heavex;
        } else {
            heavex = upXTrans - heavex;
        }

        if (heavex > 2000 || heaveY > 2000) {
            valueAnimator.cancel();
            if (drawOffAnimListener != null) {
                drawOffAnimListener.onff(view);
            }
        } else {

        }

        ViewHelper.setTranslationX(view, heavex);
//		y 300 400 , 300 ..-200

    }

    private DrawOffAnimListener drawOffAnimListener;

    public void setDrawOffAnimListener(DrawOffAnimListener drawOffAnimListener) {
        this.drawOffAnimListener = drawOffAnimListener;
    }

    public interface DrawOffAnimListener {

        public void onff(View view);
    }

    private float getHeavex(double t, double v) {
        double a = -1000f;
        double s = 0 - v / a;
        if (t > s) {
            t = s;
        }
        //s = vt+at^2/2
        double xPoint = v * t + a * (t * t) / 2;

        return (float) xPoint;

    }

    private float getHeaveY(double t, double v) {
        double g = 5000d;
        double y = v * t - g * (t * t) / 2;
        return (float) y;
    }

    private long runOffAnimStartTime = 0;
    private boolean isRunOffAnim = false;



    public void runOff(View view, float xVelocity, float yVelocity, float downX, float downY, float upY, float upX,
                       float upxTrans, float upYTrans) {
        this.view = view;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.downX = downX;
        this.downY = downY;
        this.upX = upX;
        this.upY = upY;
        this.upXTrans = upxTrans;
        this.upYTrans = upYTrans;

        isRunZN = false;
        isRunRotaionAnim = true;
        drawRoteAnimTime = System.currentTimeMillis();
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);

        runOffAnimStartTime = System.currentTimeMillis();
        isRunOffAnim = true;

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    private long runZNStartTime = 0;
    private float pivoftX, pivoftY;
    private boolean isRunZN = false;


    public void runZN(View view, float downX, float downY) {

        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
            this.view = view;
            this.downX = downX;
            this.downY = downY;

            pivoftX = view.getPivotX();
            pivoftY = view.getPivotY();

            runZNStartTime = System.currentTimeMillis();
            isRunZN = true;
        }


    }


    public void Stop() {
        valueAnimator.cancel();
    }


}
