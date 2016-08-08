package com.lesports.stadium.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.GiftBean;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("ViewConstructor")
public class ComposerLayout extends RelativeLayout {

	public static byte RIGHTBOTTOM = 1, CENTERBOTTOM = 2, LEFTBOTTOM = 3,
			LEFTCENTER = 4, LEFTTOP = 5, CENTERTOP = 6, RIGHTTOP = 7,
			RIGHTCENTER = 8;
	private boolean hasInit = false; // 初始化咗未
	private boolean areButtonsShowing = false;// 有冇展開
	private Context mycontext;
	private CircleImageView cross; // 主按鈕中間嗰個十字
	private RelativeLayout rlButton;// 主按鈕
	private Animations myani; // 動畫類
	private LinearLayout[] llayouts; // 子按鈕集
	private int duretime = 300;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ArrayList<GiftBean> mGitfs;
	private int width;

	/**
	 * 構造函數 本來想喺構造函數度讀取參數嘅，但就要喺values下面搞個attr，同埋layout嘅xml又要加命名空間————
	 * 咁搞嘅話~好多人可能唔知點用，而且參數太多（例如N個子按鈕）處理起身亦比較羅嗦。
	 * 所以而家乾脆搞個init()函數，由java代碼調用，唔讀xml喇。 所以構造函數只記錄個context就算
	 */
	public ComposerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mycontext = context;
		imageLoader.init(ImageLoaderConfiguration.createDefault(mycontext));
		width =DensityUtil.dip2px(mycontext, 49);
	}

	public ComposerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mycontext = context;
		imageLoader.init(ImageLoaderConfiguration.createDefault(mycontext));
		width =DensityUtil.dip2px(mycontext, 49);
	}

	public ComposerLayout(Context context) {
		super(context);
		this.mycontext = context;
		imageLoader.init(ImageLoaderConfiguration.createDefault(mycontext));
		width =DensityUtil.dip2px(mycontext, 49);
	}

	/**
	 * 初始化
	 * 
	 * @param imgResId
	 *            子按钮图片集合
	 * @param showhideButtonId
	 *            主按钮图片背景id
	 * @param crossId
	 *            主按鈕上图片id
	 * @param pCode
	 *            位置代碼，例如“右上角”係ALIGN_PARENT_BOTTOM|ALIGN_PARENT_RIGHT
	 * @param radius
	 *            半徑
	 * @param durationMillis
	 *            動畫耗時
	 */
	public void init(int showhideButtonId, String centerImage,
			byte pCode, int radius, final int durationMillis,ArrayList<GiftBean> gitfs) {
		if(gitfs==null||gitfs.size()==0)
			return;
//		Log.i("wxn", gitfs.size()+">>>>>>>>");
		mGitfs = gitfs;
		duretime = durationMillis;
		// 處理pcode，將自定義嘅位置值改成align值
		int align1 = 12, align2 = 14;
		if (pCode == RIGHTBOTTOM) { // 右下角
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == CENTERBOTTOM) {// 中下
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == LEFTBOTTOM) { // 左下角
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (pCode == LEFTCENTER) { // 左中
			align1 = ALIGN_PARENT_LEFT;
			align2 = CENTER_VERTICAL;
		} else if (pCode == LEFTTOP) { // 左上角
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == CENTERTOP) { // 中上
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == RIGHTTOP) { // 右上角
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_TOP;
		} else if (pCode == RIGHTCENTER) { // 右中
			align1 = ALIGN_PARENT_RIGHT;
			align2 = CENTER_VERTICAL;
		}
		// 如果細過半徑就整大佢
		RelativeLayout.LayoutParams thislps = (LayoutParams) this
				.getLayoutParams();
		Bitmap mBottom = BitmapFactory.decodeResource(mycontext.getResources(),
				R.drawable.selected_flower);
//		Bitmap mBottom = imageLoader.loadImageSync(ConstantValue.BASE_IMAGE_URL+mGitfs.get(0).getImage_uselected());
		if (pCode == CENTERBOTTOM || pCode == CENTERTOP) {
			if (thislps.width != -1
					&& thislps.width != -2
					&& thislps.width < (radius + mBottom.getWidth() + radius * 0.1) * 2) {
				thislps.width = (int) ((radius * 1.1 + mBottom.getWidth()) * 2);
			}
		} else {
			if (thislps.width != -1
					&& thislps.width != -2
					&& thislps.width < radius + mBottom.getWidth() + radius
							* 0.1) { // -1係FILL_PARENT，-2係WRAP_CONTENT
				// 因為animation嘅setInterpolator設咗OvershootInterpolator，即系喐到目標之後仍然行多一段（超過目標位置）~然後再縮返到目標位置，所以父layout就要再放大少少。而因為呢個OvershootInterpolator接納嘅係一個彈力（浮點）值，佢經過一定算法計算出個時間……如果要根據呢個彈力轉換做距離數值，就比較麻煩，所以我只系求其加咗1/10個半徑。想追求完美嘅~可以自行研究下OvershootInterpolator類同Animation類，http://www.oschina.net可以揾倒android
				// sdk嘅源碼。
				thislps.width = (int) (radius * 1.1 + mBottom.getWidth());
			}
		}
		if (pCode == LEFTCENTER || pCode == RIGHTCENTER) {
			if (thislps.height != -1
					&& thislps.height != -2
					&& thislps.height < (radius + mBottom.getHeight() + radius * 0.1) * 2) {
				thislps.width = (int) ((radius * 1.1 + mBottom.getHeight()) * 2);
			}
		} else {
			if (thislps.height != -1
					&& thislps.height != -2
					&& thislps.height < radius + mBottom.getHeight() + radius
							* 0.1) {
				thislps.height = (int) (radius * 1.1 + mBottom.getHeight());
			}
		}
		this.setLayoutParams(thislps);
		// 兩個主要層
		RelativeLayout rl1 = new RelativeLayout(mycontext);// 包含若干子按鈕嘅層

		rlButton = new RelativeLayout(mycontext); // 主按扭
		llayouts = new LinearLayout[gitfs.size()];
		// N個子按鈕
		for (int i = 0; i < gitfs.size(); i++) {
			TextView tv = new TextView(mycontext);
			tv.setText(gitfs.get(i).getIntergal());	
			tv.setId(1000);
			tv.setVisibility(View.GONE);
			LinearLayout.LayoutParams llps1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			llps1.gravity = CENTER_HORIZONTAL;
			tv.setLayoutParams(llps1);
			tv.setGravity(CENTER_HORIZONTAL);
			ImageView img = new ImageView(mycontext);// 子按扭圖片
			imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+gitfs.get(i).getImage_uselected() + ConstantValue.IMAGE_END, img,MyFragment.setDefaultImageOptions(R.drawable.default_header));
			LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(
					width,
					width);

			img.setLayoutParams(llps);
			img.setId(11111);
			llayouts[i] = new LinearLayout(mycontext);// 子按鈕層
			llayouts[i].setId(100 + i);// 隨便設個id，方便onclick嘅時候識別返出嚟。呢個id值係求其設嘅，如果發現同其他控件沖突就自行改一下。
			llayouts[i].setOrientation(LinearLayout.VERTICAL);
			if(pCode==LEFTTOP||pCode==RIGHTTOP){
				llayouts[i].addView(img);
				llayouts[i].addView(tv);
				
			}else{
				llayouts[i].addView(tv);
				llayouts[i].addView(img);
			}
			

			RelativeLayout.LayoutParams rlps = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlps.alignWithParent = true;
			rlps.addRule(align1, RelativeLayout.TRUE);
			rlps.addRule(align2, RelativeLayout.TRUE);
			llayouts[i].setLayoutParams(rlps);
			llayouts[i].setVisibility(View.INVISIBLE);// 此处不能为GONE
			rl1.addView(llayouts[i]);
		}
		@SuppressWarnings("deprecation")
		RelativeLayout.LayoutParams rlps1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		rlps1.alignWithParent = true;
		rlps1.addRule(align1, RelativeLayout.TRUE);
		rlps1.addRule(align2, RelativeLayout.TRUE);
		rl1.setLayoutParams(rlps1);

		RelativeLayout.LayoutParams buttonlps = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonlps.alignWithParent = true;
		buttonlps.addRule(align1, RelativeLayout.TRUE);
		buttonlps.addRule(align2, RelativeLayout.TRUE);
		rlButton.setLayoutParams(buttonlps);
		if(showhideButtonId!=0)
			rlButton.setBackgroundResource(showhideButtonId);
//		cross = new ImageView(mycontext);
//		cross = new CircleImageView(mycontext);
		cross = (CircleImageView) View.inflate(mycontext, R.layout.tb_imgview, null);
		RelativeLayout.LayoutParams crosslps = new RelativeLayout.LayoutParams(
				width,
				width);
		crosslps.alignWithParent = true;
		crosslps.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		cross.setLayoutParams(crosslps);
//		cross.setImageResource(R.drawable.default_header);
//		LApplication.loader.DisplayImage(GlobalParams.USER_HEADER, cross, R.drawable.default_header);
		imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+centerImage + ConstantValue.IMAGE_END, cross,MyFragment.setDefaultImageOptions(R.drawable.default_header));
//			cross.setImageResource(R.drawable.gift);
		rlButton.addView(cross);
		myani = new Animations(rl1, pCode, radius+DensityUtil.dip2px(mycontext, 5));
		rlButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isShow()){
					collapse();
				}else{
					expand();
				}
				
			}
		});

//		cross.startAnimation(Animations.getRotateAnimation(0, 360, 200));
		this.addView(rl1);
		this.addView(rlButton);
		hasInit = true;

	}

	public Point[] getloaction(int radius) {
		Point[] loations = new Point[llayouts.length];
		for (int i = 0; i < llayouts.length; i++) {
			Point pint = new Point();
			pint.x = (int) llayouts[i].getX()+radius;
			pint.y = (int) llayouts[i].getY()+radius;
			loations[i] = pint;
			Log.i("wxn", i+"pint.x :"+pint.x+ "  pint.y:"+pint.y+"  radius:"+radius);
		}
		return loations;
	}

	/**
	 * 收埋
	 */
	public void collapse() {
		myani.startAnimationsOut(duretime);
//		cross.startAnimation(Animations.getRotateAnimation(-270, 0, duretime));
		areButtonsShowing = false;
	}

	/**
	 * 打開
	 */
	public void expand() {
		myani.startAnimationsIn(duretime);
//		cross.startAnimation(Animations.getRotateAnimation(0, -270, duretime));
		areButtonsShowing = true;
		
	}

	/**
	 * 初始化咗未（其實冇乜用，原來有就保留）
	 */
	public boolean isInit() {
		return hasInit;
	}

	/**
	 * 有冇展開（其實冇乜用，原來有就保留）
	 */
	public boolean isShow() {
		return areButtonsShowing;
	}
	
	/**
	 * 偏离原位置
	 * @param Orgition 当前组合的方向
	 * @param index 是第几个item
	 */
	public void setMargin(byte origition,int index){
		if(index>llayouts.length || index<0)
			return;
		for(int i=0;i<llayouts.length;i++)
		{
			if(i!=index){
				LayoutParams parmas = (LayoutParams) llayouts[index].getLayoutParams();
				parmas.setMargins(0, 0, 0, 0);
				llayouts[index].setLayoutParams(parmas);
			}
				
		}
		LayoutParams parmas = (LayoutParams) llayouts[index].getLayoutParams();
		int size = DensityUtil.dip2px(mycontext, 30);
		if(origition==RIGHTBOTTOM){
			//右下角
			if(index == 0){
				parmas.setMargins(0, 0, size, 0);
			}else if(index ==3){
				parmas.setMargins(0, 0, 0, size);
			}else{
				parmas.setMargins(0, 0, size, size);
			}
		}else if(origition==RIGHTTOP){
			//右顶角
			if(index == 0){
				parmas.setMargins(0, 0, size, 0);
			}else if(index ==3){
				parmas.setMargins(0, size, 0, 0);
			}else{
				parmas.setMargins(0, size, size, 0);
			}
		}else if(origition==LEFTBOTTOM){
			//左下角
			if(index == 0){
				parmas.setMargins(size, 0, 0, 0);
			}else if(index ==3){
				parmas.setMargins(0, 0, 0, size);
			}else{
				parmas.setMargins(size, 0, 0, size);
			}
		}else if(origition==LEFTTOP){
			//左顶角
			if(index == 0){
				parmas.setMargins(size, 0, 0, 0);
			}else if(index ==3){
				parmas.setMargins(0, size, 0, 0);
			}else{
				parmas.setMargins(size, size, 0, 0);
			}
		}
		llayouts[index].findViewById(1000).setVisibility(View.VISIBLE);
		llayouts[index].setLayoutParams(parmas);
	}
	
	public void resetMaing(int index){
		LayoutParams parmas = (LayoutParams) llayouts[index].getLayoutParams();
		parmas.setMargins(0, 0, 0, 0);
		llayouts[index].setLayoutParams(parmas);
		llayouts[index].findViewById(1000).setVisibility(View.GONE);
	}
	//恢复默认
	public void reset(){
		if(mGitfs==null)
			return;
		for(int i=0;i<llayouts.length;i++){
			llayouts[i].findViewById(1000).setVisibility(View.GONE);
			ImageView iv = (ImageView) llayouts[i].findViewById(11111);
			imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+mGitfs.get(i).getImage_uselected() + ConstantValue.IMAGE_END, iv,MyFragment.setDefaultImageOptions(R.drawable.default_header));
		}
	}
	
	public void clickItem(int index,String imgPath){
		llayouts[index].findViewById(1000).setVisibility(View.VISIBLE);
		ImageView iv = (ImageView) llayouts[index].findViewById(11111);
		imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL+imgPath + ConstantValue.IMAGE_END, iv,MyFragment.setDefaultImageOptions(R.drawable.default_header));
	}

	/**
	 * 設置各子按鈕嘅onclick事件
	 */
	public void setButtonsOnClickListener(final OnTouchListener l) {

		if (llayouts != null) {
			for (int i = 0; i < llayouts.length; i++) {
				if (llayouts[i] != null)
					llayouts[i].setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// 此处添加其他事件比如按钮增大或者缩回菜单
//							collapse();
							l.onTouch(v, event);
							return true;
						}
					});
			}
		}
	}

	public class Point {
		public int x;
		public int y;
		public void setX(int x){
			this.x =x;
		}
		public void setY(int y){
			this.y =y;
		}
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
	}
}
