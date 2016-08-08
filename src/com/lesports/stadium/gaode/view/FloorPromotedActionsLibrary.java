package com.lesports.stadium.gaode.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;


import com.lesports.stadium.R;
import com.lesports.stadium.utils.DensityUtil;


public class FloorPromotedActionsLibrary {

    Context context;

    FrameLayout frameLayout;

    Button mainImageButton;

    RotateAnimation rotateOpenAnimation;

    RotateAnimation rotateCloseAnimation;

    ArrayList<Button> promotedActions;

    ObjectAnimator objectAnimator[];

    private int px;

    private static final int ANIMATION_TIME = 50;

    private boolean isMenuOpened;

	private Button button;
	

    public void setup(Context activityContext, FrameLayout layout) {
        context = activityContext;
        promotedActions = new ArrayList<Button>();
        frameLayout = layout;
        //定义高度
        px =40;
        openRotation();
        closeRotation();
    }

    
    public Button addMainItem(Drawable drawable,View.OnClickListener onClickListener) {

        button = (Button) LayoutInflater.from(context).inflate(R.layout.floor_main_promoted_action_button, frameLayout, false);


        
        button.setOnClickListener(onClickListener);

        frameLayout.addView(button);

        mainImageButton = button;

        return button;
    }
    
//    public void setMainButtonText(String s){
//    	button.setText(s);
//    }
    
    public void setDisvisibility(){
    	button.setVisibility(View.GONE);
    }
    public void setVisibility(){
    	button.setVisibility(View.VISIBLE);
    }

    public void addItem(String text, View.OnClickListener onClickListener) {

        Button button = (Button) LayoutInflater.from(context).inflate(R.layout.floor_promoted_action_button, frameLayout, false);

    //    button.setImageDrawable(drawable);
        button.setText(text);
//        button.setScaleType(ScaleType.CENTER);


        button.setOnClickListener(onClickListener);

        promotedActions.add(button);

        frameLayout.addView(button);

        return;
    }
    
    public void clearButton(){
    	if(null!=promotedActions){
    		promotedActions.clear();
    	}
//    	if(null!=frameLayout.getChildAt(0)){
    		frameLayout.removeAllViews();
//    	}
    }


    public AnimatorSet closePromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }

        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setCloseAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainImageButton.startAnimation(rotateCloseAnimation);
                mainImageButton.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable(true);
                hidePromotedActions();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        return animation;
    }

    public AnimatorSet openPromotedActions() {

        if (objectAnimator == null){
            objectAnimatorSetup();
        }



        AnimatorSet animation = new AnimatorSet();

        for (int i = 0; i < promotedActions.size(); i++) {

            objectAnimator[i] = setOpenAnimation(promotedActions.get(i), i);
        }

        if (objectAnimator.length == 0) {
            objectAnimator = null;
        }

        animation.playTogether(objectAnimator);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mainImageButton.startAnimation(rotateOpenAnimation);
                mainImageButton.setClickable(false);
                showPromotedActions();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mainImageButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });


        return animation;
    }

    private void objectAnimatorSetup() {

        objectAnimator = new ObjectAnimator[promotedActions.size()];
    }


    /**
     * Set close animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    private ObjectAnimator setCloseAnimation(Button promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, DensityUtil.dip2px(context, -px * (promotedActions.size() - position)), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X,DensityUtil.dip2px(context,  -px * (promotedActions.size() - position)), 0f);
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    /**
     * Set open animation for single button
     *
     * @param promotedAction
     * @param position
     * @return objectAnimator
     */
    private ObjectAnimator setOpenAnimation(Button promotedAction, int position) {

        ObjectAnimator objectAnimator;

        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_Y, 0f, DensityUtil.dip2px(context, -px * (promotedActions.size() - position)));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));

        } else {
            objectAnimator = ObjectAnimator.ofFloat(promotedAction, View.TRANSLATION_X, 0f, DensityUtil.dip2px(context, -px * (promotedActions.size() - position)));
            objectAnimator.setRepeatCount(0);
            objectAnimator.setDuration(ANIMATION_TIME * (promotedActions.size() - position));
        }

        return objectAnimator;
    }

    private void hidePromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.GONE);
        }
    }

    private void showPromotedActions() {

        for (int i = 0; i < promotedActions.size(); i++) {
            promotedActions.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void openRotation() {

        rotateOpenAnimation = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateOpenAnimation.setFillAfter(true);
        rotateOpenAnimation.setFillEnabled(true);
        rotateOpenAnimation.setDuration(ANIMATION_TIME);
    }

    private void closeRotation() {

        rotateCloseAnimation = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateCloseAnimation.setFillAfter(true);
        rotateCloseAnimation.setFillEnabled(true);
        rotateCloseAnimation.setDuration(ANIMATION_TIME);
    }


}
