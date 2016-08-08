package com.lesports.stadium.view;


import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;


/**
 * Dialog
 * @author shengzhigang
 *
 */
public class CustomDialogNavigation extends Dialog {
		/**
		 * 布局文件
		 */
        int layoutRes;
   
        /**
         * 按钮区域
         */
        @SuppressWarnings("unused")
		private LinearLayout llyt_confirm_cancel,llyt_confirm;
        /**
         * 监听事件
         */
        @SuppressWarnings("unused")
		private android.view.View.OnClickListener confirmClick,cancelClick;
        /**
         * 提示信息，提示标题，确认按钮文字内容，取消按钮文字内容
         */
        @SuppressWarnings("unused")
		private String remindMessage,remindTitle,confirmTxt,cancelTxt;
       
        /**
         * 设置提示信息
         * @param remindMessage
         */
		public void setRemindMessage(String remindMessage) {
			this.remindMessage = remindMessage;
		}
		
		/**
		 * 设置提示标题
		 * @param remindTitle
		 */
		public void setRemindTitle(String remindTitle) {
			this.remindTitle = remindTitle;
		}
		
		/**
		 * 设置确认按钮文字信息
		 * @param confirmTxt
		 */
		public void setConfirmTxt(String confirmTxt) {
			this.confirmTxt = confirmTxt;
		}
		
		/**
		 * 设置取消按钮文字信息
		 * @param cancelTxt
		 */
		public void setCancelTxt(String cancelTxt) {
			this.cancelTxt = cancelTxt;
		}
		
		public CustomDialogNavigation() {
            super(LApplication.getTopActivity());
        }
        /**
         * 自定义布局的构造方法
         * @param context
         * @param resLayout
         */
        public CustomDialogNavigation(int resLayout){
            super(LApplication.getTopActivity());
            this.layoutRes=R.layout.remind_dialog_navigation;
//            this.layoutRes=resLayout;
        }
        
        /**
         * 有标题，文字提示信息，取消 确认按钮的dialog
         * @param context
         * @param theme
         * @param resLayout
         * @param confirmClick 确定按钮点击
         * @param cancelClick  取消按钮点击
         */
        public CustomDialogNavigation(android.view.View.OnClickListener confirmClick,android.view.View.OnClickListener cancelClick){
            super(LApplication.getTopActivity(), R.style.customDialog);
            this.layoutRes=R.layout.remind_dialog;
            this.confirmClick=confirmClick;
            this.cancelClick=cancelClick;
        }
        
        /**
         * 有标题，文字提示信息，确认按钮的dialog
         */
        public CustomDialogNavigation(android.view.View.OnClickListener confirmClick){
            super(LApplication.getTopActivity(), R.style.customDialog);
            this.layoutRes=R.layout.remind_dialog;
            this.confirmClick=confirmClick;
        }
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(layoutRes);
            
            initView();
            
        }
        
        /**
         * 初始化View
         */
       private void initView(){

    	   //包含确认，取消按钮区域
    	   llyt_confirm_cancel=(LinearLayout)findViewById(R.id.llyt_confirm_cancel);
    	   //只有确认按钮区域
           llyt_confirm=(LinearLayout)findViewById(R.id.llyt_confirm);

       }
       
       @Override
    public void dismiss() {
	    super.dismiss();
	    this.confirmClick=null;
	    this.cancelClick=null;
        this.remindMessage=null;
        this.remindTitle=null;
        this.confirmTxt=null;
        this.cancelTxt=null;
	    
	}
 }
