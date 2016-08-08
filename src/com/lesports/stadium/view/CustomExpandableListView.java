/**
 * 
 */
package com.lesports.stadium.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * ***************************************************************
 * 
 * @Desc : 自定义的为了解决可拓展listview与scrollview的冲突的expanablelistview
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

public class CustomExpandableListView extends ExpandableListView {

	/**
	 * @param context
	 */
	public CustomExpandableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public CustomExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}


	
	

}
