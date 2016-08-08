/**
 * 
 */
package com.lesports.stadium.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ***************************************************************
 * 
 * @Desc : 为解决scrollview嵌套冲突问题而新建的gridview
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
 *         ***************************************************************
 */

public class MyGridView extends GridView {
	public MyGridView(Context mcoonte) {
		super(mcoonte);
	}
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 int expandSpec = MeasureSpec.makeMeasureSpec( 
			                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
				     super.onMeasure(widthMeasureSpec, expandSpec); 

	}
}
