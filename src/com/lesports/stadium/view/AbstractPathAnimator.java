package com.lesports.stadium.view;

import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractPathAnimator {
	private int point;
	
	/**
	 * 条目上传路径
	 * @return
	 */
	public Path createPath() {
		Path p = new Path();
		p.moveTo(0, 0);
		p.cubicTo(0, 0, 0, -point/2, 0, -point);
		return p;
	}
	/**
	 * 心 飘动路径
	 * @param pointX
	 * @param pointY
	 * @return
	 */
	public Path createHeartPath(int pointX,int pointY) {
		Path p = new Path();
        p.moveTo(pointX, pointY);
        p.cubicTo(pointX+80, pointY-200, pointX+100, pointY-point/2, pointX-65, pointY-point);
		return p;
	}
	public abstract void start(View child, ViewGroup parent);
	public abstract void startHeart(View child, ViewGroup parent,int pointX,int pointY);
	public void setPoint(int point){
		if(point>this.point)
			this.point = point-120;
		
	}

}
