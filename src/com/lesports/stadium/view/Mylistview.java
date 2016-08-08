package com.lesports.stadium.view;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 为解决listview与scrollview的冲突的自定义的listview
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
public class Mylistview extends ListView {
	
	public Mylistview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Mylistview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	public Mylistview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	 @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                             MeasureSpec.AT_MOST);

             super.onMeasure(widthMeasureSpec, expandSpec);
     }
	
	
	

}
