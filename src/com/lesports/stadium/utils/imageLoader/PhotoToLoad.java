package com.lesports.stadium.utils.imageLoader;


import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 
 * @ClassName:  PhotoToLoad   
 * @Description:存储图片的bean对象   
 * @author: 王新年 
 * @date:   2015-12-28 下午5:40:03   
 *
 */
public class PhotoToLoad {
	public String url;
	public ImageView imageView;
	public ProgressBar progressBar;
	public int imageWidthHightPixels;
	public int bgImageResId;

	public PhotoToLoad(String u, ImageView i) {
		url = u;
		imageView = i;
	}

	public PhotoToLoad(String u, ImageView i, int resId) {
		url = u;
		imageView = i;
		bgImageResId = resId;
	}

	public PhotoToLoad(String u, ImageView i, ProgressBar pb, int widthHightPixels) {
		url = u;
		imageView = i;
		progressBar = pb;
		imageWidthHightPixels = widthHightPixels;
	}

	public PhotoToLoad(String u, ImageView i, ProgressBar pb, int widthHightPixels, int resId) {
		url = u;
		imageView = i;
		progressBar = pb;
		imageWidthHightPixels = widthHightPixels;
		bgImageResId = resId;
	}
}
