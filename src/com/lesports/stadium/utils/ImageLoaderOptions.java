package com.lesports.stadium.utils;

import android.graphics.Bitmap;

import com.lesports.stadium.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderOptions {
// ViewPager options
	public static DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.app_logo)
			.showImageOnFail(R.drawable.app_logo)
			.resetViewBeforeLoading(true)//在加载图片之前情况ImageView中的图片
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)//设置缩放类型，会按照ImageView真实的宽高进行缩放
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的rgb显示模式，会让图片显示比较高清，而且占用内存较小
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(500)).build();
	
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions IM_IMAGE_OPTIONS= new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_error)
	.showImageForEmptyUri(R.drawable.ic_error)
	.showImageOnFail(R.drawable.ic_error)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.imageScaleType(ImageScaleType.EXACTLY_STRETCHED) 
	.displayer(new RoundedBitmapDisplayer(5)) // default 可以设置动画，比如圆角或者渐�?
	.cacheInMemory(true)
	.cacheOnDisc(false)
	.build();
}
