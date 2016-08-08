package com.lesports.stadium.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
/**
 * ***************************************************************
 * 
 * @Desc :用来控制控件显示和隐藏的时候上下移动的动画
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class AnimationUtil {

	 private static final String TAG = AnimationUtil.class.getSimpleName();
	 /**

	     * 从控件所在位置移动到控件的底部

	     *

	     * @return

	     */

	    public static TranslateAnimation moveToViewBottom() {

	        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,

	                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,

	                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

	        mHiddenAction.setDuration(200);

	        return mHiddenAction;

	    }
	    
	    
	    /**

	     * 从控件的底部移动到控件所在位置

	     *

	     * @return

	     */

	    public static TranslateAnimation moveToViewLocation() {

	        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,

	                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,

	                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

	        mHiddenAction.setDuration(500);

	        return mHiddenAction;

	    }

}
