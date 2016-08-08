
package com.lesports.stadium.view;

import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;


public class PathAnimator extends AbstractPathAnimator {
    private final AtomicInteger mCounter = new AtomicInteger(0);
    private Handler mHandler;

    public PathAnimator(int startpoint) {
        mHandler = new Handler(Looper.getMainLooper());
        
    }
    private FloatAnimation anim;

    @Override
    public void start(final View child, final ViewGroup parent) {
    	anim = new FloatAnimation(createPath(), parent, child,false);
        anim.setDuration(6000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(child);
                    }
                });
                mCounter.decrementAndGet();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
                mCounter.incrementAndGet();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
    	
    	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	int magrinleft = DensityUtil.dip2px(GlobalParams.context, 10);
    	params.setMargins(magrinleft, 0, magrinleft, 0);
        parent.addView(child,params);
        ScaleAnimation sanim=new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
		sanim.setDuration(500);
		child.startAnimation(sanim);

		sanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationRepeat(Animation animation) {
				
			}
			public void onAnimationEnd(Animation animation) {
				child.startAnimation(anim);
			}
		});
        
    }

    static class FloatAnimation extends Animation {
        private PathMeasure mPm;
        private View mView;
        private float mDistance;
        private boolean isHeart;

        public FloatAnimation(Path path, View parent, View child,boolean isHeart) {
            mPm = new PathMeasure(path, false);
            mDistance = mPm.getLength();
            mView = child;
            this.isHeart = isHeart;
            parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        protected void applyTransformation(float factor, Transformation transformation) {
            Matrix matrix = transformation.getMatrix();
            mPm.getMatrix(mDistance * factor, matrix, PathMeasure.POSITION_MATRIX_FLAG);
//            mView.setRotation(mRotation * factor);
            float scale = 1F;
            if (3000.0F * factor < 200.0F) {
                scale = scale(factor, 0.0D, 0.06666667014360428D, 0.20000000298023224D, 1.100000023841858D);
            } else if (3000.0F * factor < 300.0F) {
                scale = scale(factor, 0.06666667014360428D, 0.10000000149011612D, 1.100000023841858D, 1.0D);
            }
            if(isHeart){
	            mView.setScaleX(scale);
	            mView.setScaleY(scale);
            }
            transformation.setAlpha(1.0F - factor);
        }
    }

    private static float scale(double a, double b, double c, double d, double e) {
        return (float) ((a - b) / (c - b) * (e - d) + d);
    }


	@Override
	public void startHeart(final View child, final ViewGroup parent,int pointX,int pointY) {
		   parent.addView(child);
	        FloatAnimation anim = new FloatAnimation(createHeartPath(pointX, pointY), parent, child,true);
	        anim.setDuration(4000);
	        anim.setInterpolator(new LinearInterpolator());
	        anim.setAnimationListener(new Animation.AnimationListener() {
	            @Override
	            public void onAnimationEnd(Animation animation) {
	                mHandler.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        parent.removeView(child);
	                    }
	                });
	                mCounter.decrementAndGet();
	            }

	            @Override
	            public void onAnimationRepeat(Animation animation) {

	            }

	            @Override
	            public void onAnimationStart(Animation animation) {
	                mCounter.incrementAndGet();
	            }
	        });
	        anim.setInterpolator(new LinearInterpolator());
	        child.startAnimation(anim);
		
	}
}

