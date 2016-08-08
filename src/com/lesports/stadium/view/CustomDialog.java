package com.lesports.stadium.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;


/**
 * Dialog
 * @author shengzhigang
 *
 */
public class CustomDialog extends Dialog {
		/**
		 * 布局文件
		 */
        int layoutRes;
        /**
         * 确认按钮，取消按钮，提示信息，提示标题，确认关闭按钮
         */
        private TextView tv_confirm,tv_cancel,tv_remind_message,tv_title,tv_confirm_close;
        /**
         * 按钮区域
         */
        private LinearLayout llyt_confirm_cancel,llyt_confirm;
        /**
         * 监听事件
         */
        private android.view.View.OnClickListener confirmClick,cancelClick;
        /**
         * 提示信息，提示标题，确认按钮文字内容，取消按钮文字内容
         */
        private String remindMessage,remindTitle,confirmTxt,cancelTxt;
        
        /**
         * 中间的线
         */
        private View minddle_line;
       
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
		
		public CustomDialog() {
            super(LApplication.getTopActivity());
        }
        /**
         * 自定义布局的构造方法
         * @param context
         * @param resLayout
         */
        public CustomDialog(int resLayout){
            super(LApplication.getTopActivity());
            this.layoutRes=R.layout.remind_dialog;
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
        public CustomDialog(Context context,android.view.View.OnClickListener confirmClick,android.view.View.OnClickListener cancelClick){
            super(context, R.style.customDialog);
            this.layoutRes=R.layout.remind_dialog;
            this.confirmClick=confirmClick;
            this.cancelClick=cancelClick;
        }
        
        /**
         * 有标题，文字提示信息，确认按钮的dialog
         */
        public CustomDialog(android.view.View.OnClickListener confirmClick){
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
    	   //标题
    	   tv_title=(TextView)findViewById(R.id.tv_title);
    	   //文字提示内容
    	   tv_remind_message=(TextView)findViewById(R.id.tv_remind_message);
    	   //包含确认，取消按钮区域
    	   llyt_confirm_cancel=(LinearLayout)findViewById(R.id.llyt_confirm_cancel);
    	   //只有确认按钮区域
           llyt_confirm=(LinearLayout)findViewById(R.id.llyt_confirm);
           //确认按钮
    	   tv_confirm=(TextView)findViewById(R.id.tv_confirm);
    	   //取消按钮
    	   tv_cancel=(TextView)findViewById(R.id.tv_cancel);
    	   //确认按钮
    	   tv_confirm_close=(TextView)findViewById(R.id.tv_confirm_close);
    	   minddle_line= findViewById(R.id.minddle_line);
           
          //设置标题信息
    	   if(remindMessage!=null){
    		   tv_remind_message.setText(remindMessage); 
    	   }
    	   
    	   //判断是否需要展示标题
    	   if(remindTitle==null){
    		   tv_title.setVisibility(View.GONE);
    	   }else{
    		   tv_title.setText(remindTitle);
    	   }
    	   
    	   	//判断是否需要展示两个按钮
    	   if(cancelClick==null){
    		   tv_confirm_close.setOnClickListener(confirmClick);
    		   llyt_confirm.setVisibility(View.VISIBLE);
    		   llyt_confirm_cancel.setVisibility(View.GONE);
    		   minddle_line.setVisibility(View.GONE);
    		   
    		   if(confirmTxt!=null){
    			   tv_confirm_close.setText(confirmTxt);  
    		   }
    	   }else{
    		   tv_confirm.setOnClickListener(confirmClick);
        	   tv_cancel.setOnClickListener(cancelClick);
    		   llyt_confirm.setVisibility(View.GONE);
    		   llyt_confirm_cancel.setVisibility(View.VISIBLE);
    		   
    		   if(confirmTxt!=null){
        		   tv_confirm.setText(confirmTxt);
        	   }
        	   
        	   if(cancelTxt!=null){
        		   tv_cancel.setText(cancelTxt);
        	   }   
    	   }
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
