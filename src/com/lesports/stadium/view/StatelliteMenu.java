package com.lesports.stadium.view;




import com.lesports.stadium.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
/**
 * 
 * 描述:自定义卫星菜单
 * 项目名称:statellitemenu<br/>
 * 类名称:StateMenu<br/>
 *
 * @author:邓聪聪
 *
 */
//思路：1.先定义半径和位置，需要自定义属性
//		 　2.实现父类的onMeasure方法，动态计算view的宽高及其子view的个数
public class StatelliteMenu extends ViewGroup implements View.OnClickListener{
private  int mRadius=50;//菜单的半径
private  Position mPosition=Position.MIDDLE_BOTTOM;//定义卫星菜单的初始位置
private View mButton;//卫星菜单主键按钮
private OnItemOnClickListener onItemClickListener;//子菜单按钮监听事件
private Status mCurrentStatus=Status.MENU_HIDDE;
	public StatelliteMenu(Context context) {
		this(context,null);
	}
	public StatelliteMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public StatelliteMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//初始化自定义属性
		mRadius=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mRadius, getResources().getDisplayMetrics());
		TypedArray typeArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.StatelliteMenu, defStyle, 0);
		int n=typeArray.getIndexCount();//获取属性数组值的长度
		for (int i = 0; i <n; i++) {
			int attr=typeArray.getIndex(i);//取出每一个属性
			switch (attr) {
			case R.styleable.StatelliteMenu_position:
				//判断是否是位置属性
				int  attrValue=typeArray.getInt(attr, 0);//取出位置属性的值
				switch (attrValue) {
				case 0:
					mPosition=Position.LEFT_TOP;
					break;
				case 1:
					mPosition=Position.LEFT_BOTTOM;
					break;
				case 2:
					mPosition=Position.MIDDLE_TOP;
					break;
				case 3:
					mPosition=Position.MIDDLE_BOTTOM;
					break;
				case 4:
					mPosition=Position.RIGHT_TOP;
					break;
				case 5:
					mPosition=Position.RIGHT_BOTTOM;
					break;
				}
				break;

			case R.styleable.StatelliteMenu_gift_radius:
				mRadius=typeArray.getDimensionPixelSize(attr,(int)
						TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100f,getResources().getDisplayMetrics()));
				break;
			   }
		    }
		typeArray.recycle();//释放资源
	}
	@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			for (int i = 0; i <getChildCount(); i++) {
			getChildAt( i ).measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);//动态测量子view的大小
			//	getChildAt( i ).measure(20,20);//动态测量子view的大小
			}
			super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
          if (changed) {
			setCurrentButton();//设置主键view的位置
			int count=getChildCount()-1;//获取除主按钮之外的其他子view的个数
			switch (mPosition) {
			case LEFT_TOP:
				getChildPosition(count);
				break;
			case LEFT_BOTTOM:
				getChildPosition(count);
				break;
			case MIDDLE_TOP:
				getChildMiddlePosition(count);
				break;
			case MIDDLE_BOTTOM:
				getChildMiddlePosition(count);
				break;
			case RIGHT_TOP:
				getChildPosition(count);
				break;
			case RIGHT_BOTTOM:
				getChildPosition(count);
				break;
			}
		}
	}
	/**
	 * 
	 * 功能:当主键按钮在屏幕在左上，左下，右上、右下时其他子view的
	 * @param count 
	 * @author: 邓聪聪
	 * 
	 */
	private void getChildPosition(int count){
		for (int i =0; i <count; i++) {
			View child=getChildAt(i+1);//获取其中某一个子view
			child.setVisibility(View.GONE);
			/*int cWidth=child.getMeasuredWidth();//获取子view的宽度
			int cHeight=child.getMeasuredHeight();//获取子view的高度
*/			int cWidth=90;//获取子view的宽度
			int cHeight=90;
			double sumAngle=Math.PI/2;
			int cl=(int) (mRadius*Math.sin(sumAngle/(count-1)*i));//子view到父容器左边的距离
			int ct=(int) (mRadius*Math.cos(sumAngle/(count-1)*i));//子view到父容器上边的距离
			if (mPosition==Position.LEFT_BOTTOM||mPosition==Position.RIGHT_BOTTOM) {
				ct=getMeasuredHeight()-ct-cHeight;
			}
			if (mPosition==Position.RIGHT_TOP||mPosition==Position.RIGHT_BOTTOM) {
				cl=getMeasuredWidth()-cl-cWidth;
			}
			child.layout(cl, ct, cl+cWidth,ct+cHeight);
		}
	}
	/**
	 * 除主键按钮之外的其他子view的位置
	 */
	private void getChildMiddlePosition(int count){
		for (int i =0; i <count; i++) {
			View child=getChildAt(i+1);//获取其中某一个子view
			child.setVisibility(View.GONE);
			int cWidth=child.getMeasuredWidth();//获取子view的宽度
			int cHeight=child.getMeasuredHeight();//获取子view的高度
			double sumAngle=Math.PI;
			int cl=(int) (mRadius*Math.cos(sumAngle/(count-1)*i));//子view到父容器左边的距离
			int ct=(int) (mRadius*Math.sin(sumAngle/(count-1)*i));//子view到父容器上边的距离
			if (mPosition==Position.MIDDLE_TOP) {
				cl=getMeasuredWidth()/2-cl-cWidth;
				child.layout(cl+cWidth/2, ct, cl+3*cWidth/2,ct+cHeight);//为了让子view居中显示给其左边距离多加子view的一半
			}
			if (mPosition==Position.MIDDLE_BOTTOM) {
				cl=getMeasuredWidth()/2+cl;
				ct=getMeasuredHeight()-ct-cHeight;
				child.layout(cl-cWidth/2, ct, cl+cWidth/2,ct+cHeight);//为了让子view居中显示给其左边距离多 减去子view的一半
			}
			
		}
	}
	/***
	 * 
	 * 功能:  第一个子元素为主按钮，为按钮布局且初始化点击事件
	 * @author: 邓聪聪
	 * 
	 */
	private void setCurrentButton() {
	  View	currentButton=getChildAt(0);
	  currentButton.setOnClickListener(this);//给主键按钮设置事件监听
	  int leftDis=0;//设置主键按钮相对于父容器左上角的x轴距离
	  int topDis=0;//设置主键按钮到父容器左上角y轴距离
	  int width=currentButton.getMeasuredWidth();//得到主键按钮自身的宽度
	  int height=currentButton.getMeasuredHeight();//得到主键按钮自身的高度
	  switch (mPosition) {
	  case LEFT_TOP:
		  leftDis=0;
		  topDis=0;
		break;
	  case LEFT_BOTTOM:
		  leftDis=0;
		  topDis=getMeasuredHeight()-height;
		  break;
	  case MIDDLE_TOP:
		  leftDis=getMeasuredWidth()/2-width/2;
		  topDis=0;
		  break;
	  case MIDDLE_BOTTOM:
		  leftDis=getMeasuredWidth()/2-width/2;
		  topDis=getMeasuredHeight()-height;
		  break;
	  case RIGHT_TOP:
		  leftDis=getMeasuredWidth()-width;
		   topDis=0;
		  break;
	  case RIGHT_BOTTOM:
		  leftDis=getMeasuredWidth()-width;
		  topDis=getMeasuredHeight()-height;
		  break;
	   }
	  currentButton.layout(leftDis, topDis, leftDis+width, topDis+height);
	}
	@Override
	public void onClick(View v) {
	//	mButton=findViewById(R.id.id_button);
		if (mButton==null) {
			mButton=getChildAt(0);
		}
		rotateView(mButton,0,720,300);
		toggleMenu(300);
	}
	/**
	 * 点击主按钮时菜单切换动画
	 * 功能:
	 * @param durationMills 
	 * @author: 邓聪聪
	 * 
	 */
	private void toggleMenu(int durationMills) {
		int count=getChildCount()-1;
		for (int i = 0; i <count; i++) {
			final View childView=getChildAt(i+1);
			childView.setVisibility(View.VISIBLE);
			int xFlag=1;//子view滑动时X轴的偏移量
			int  yFlag=1;//子view滑动时Y轴的偏移量
			if (mPosition==Position.LEFT_TOP||mPosition==Position.LEFT_BOTTOM||mPosition==Position.MIDDLE_BOTTOM||mPosition == Position.LEFT_BOTTOM) {
				xFlag=-1;
			}
			if (mPosition==Position.LEFT_TOP||mPosition==Position.RIGHT_TOP||mPosition==Position.MIDDLE_TOP) {
				yFlag=-1;
			}
			int cl=0;
			int ct=0;
			if (mPosition==Position.LEFT_TOP||mPosition==Position.LEFT_BOTTOM||mPosition==Position.RIGHT_TOP||mPosition==Position.RIGHT_BOTTOM) {
				cl=(int) (mRadius * Math.sin(Math.PI / 2 / (count - 1) * i));
			    ct=(int) (mRadius*Math.cos(Math.PI / 2 /(count-1)*i));//子view到父容器上边的距离
			}
			if (mPosition==Position.MIDDLE_TOP||mPosition==Position.MIDDLE_BOTTOM) {
				cl=(int) (mRadius * Math.cos(Math.PI / (count - 1) * i));
			    ct=(int) (mRadius*Math.sin(Math.PI /(count-1)*i));//子view到父容器上边的距离
			}
			AnimationSet animationSet=new AnimationSet(true);
			Animation translate = null;
			if (mCurrentStatus==Status.MENU_HIDDE) {//如果当前状态的子view是隐藏状态就去打开
				animationSet.setInterpolator(new OvershootInterpolator(2f));
				translate=new TranslateAnimation(xFlag*cl, 0, yFlag*ct, 0);//添加移动动画
				childView.setClickable(true);
				childView.setFocusable(true);
			}else{//如果当前状态是打开的就隐藏
				translate=new TranslateAnimation( 0,xFlag*cl, 0,yFlag*ct);
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			translate.setAnimationListener(new AnimationListener()
			{
				public void onAnimationStart(Animation animation)
				{
				}

				public void onAnimationRepeat(Animation animation)
				{
				}

				public void onAnimationEnd(Animation animation)
				{
					if (mCurrentStatus == Status.MENU_HIDDE)
						childView.setVisibility(View.GONE);
				}
			});
			translate.setDuration(durationMills);
			translate.setFillAfter(true);
			// 为动画设置一个开始延迟时间，纯属好看，可以不设
			translate.setStartOffset((i * 100) / (count - 1));
			RotateAnimation rotate=new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(durationMills);
			rotate.setFillAfter(true);
			animationSet.addAnimation(rotate);//旋转动画必须在移动动画之前添加，否则会影响展示效果
			animationSet.addAnimation(translate);
			childView.startAnimation(animationSet);
			final int index=i;
			childView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
			       if (onItemClickListener!=null) {
			    	   onItemClickListener.onItemClick(childView, index);//给主按钮以外每个itemview添加点击事件
			    	   //给子view添加点击之后的动画效果
			    	   toggleChildView(index);
			    	   changeStatus();
				  }
				}
			});
			
		}
		changeStatus();
	}
	/**
	 * 
	 * 功能: 开始菜单动画，点击的MenuItem放大消失，其他的缩小消失
	 * @author: 邓聪聪
	 * 
	 */
	protected void toggleChildView(int index) {
		for (int i = 0; i < getChildCount()-1; i++) {
			View childView=getChildAt(i+1);
			if (i==index) {
				childView.startAnimation(scaleBigAnimation(300));
			}else{
				childView.startAnimation(scaleSmallAnimation(300));
			}
			childView.setClickable(false);
			childView.setFocusable(false);
		}
	}
	/***
	 * 
	 * 功能:先放大后消失
	 * @param i 
	 * @author: 邓聪聪
	 * 
	 */
	private Animation scaleBigAnimation(int durationMillis) {
		AnimationSet animSet=new AnimationSet(true);
		Animation scale=new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		AlphaAnimation  alpha=new AlphaAnimation(1.0f, 0.0f);
		animSet.setDuration(durationMillis);
		animSet.setFillAfter(true);
		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		return animSet;
	}
	/***
	 * 
	 * 功能:缩小后消失
	 * @param i 
	 * @author: 邓聪聪
	 * 
	 */
	private Animation scaleSmallAnimation(int durationMillis) {
		Animation scale=new ScaleAnimation(1.0f, 0f, 1.0f, 0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(durationMillis);
		scale.setFillAfter(true);
		return scale;
	}
	/**
	 * 
	 * 功能: 菜单的闭合状态
	 * @author: 邓聪聪
	 * 
	 */
	private void changeStatus()
	{
		mCurrentStatus = (mCurrentStatus == Status.MENU_HIDDE ? Status.MENU_SHOW
				: Status.MENU_HIDDE);
	}
	/**
	 * 
	 * 功能:主键按钮的旋转动画
	 * @param v
	 * @param fromDegrees
	 * @param toDegrees
	 * @param durationMills 
	 * @author: 邓聪聪
	 * 
	 */
	private void rotateView(View v,int fromDegrees,int toDegrees,int durationMills) {
		RotateAnimation rotate=new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(300);
		rotate.setFillAfter(true);//是否可以跟其他动画一起使用
		v.startAnimation(rotate);
	}
	/***
	 * 
	 * 描述:此枚举类用来确定菜单主按钮的位置
	 * 项目名称:statellitemenu<br/>
	 * 类名称:Position<br/>
	 *
	 * @author:邓聪聪
	 * 
	 */
	    public enum Position{
	    	LEFT_TOP,LEFT_BOTTOM,MIDDLE_TOP,MIDDLE_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	    }
	    /**
	     * 
	     * 描述:此枚举类 用来展示子view在点击后是隐藏还是显示状态
	     * 项目名称:statellitemenu<br/>
	     * 类名称:Status<br/>
	     *
	     * @author:邓聪聪
	     *
	     */
	    public enum Status{
	    	MENU_SHOW,MENU_HIDDE
	    }
	/**
	 * 
	 * 描述:给子菜单按钮提供外部回调的接口
	 * 项目名称:statellitemenu<br/>
	 * 类名称:OnItemOnClickListener<br/>
	 *
	 * @author:邓聪聪
	 * 
	 */
	public interface OnItemOnClickListener{
		void onItemClick(View v,int pos);
	}
	public void setOnItemOnClickListener(OnItemOnClickListener listener){
		this.onItemClickListener=listener;
	}
	public OnItemOnClickListener getOnItemClickListener(){
		return onItemClickListener;
	}
	/**
	 * 设置礼物位置
	 */
	public void setPosition(Position position){
		mPosition=position;
	}
}
