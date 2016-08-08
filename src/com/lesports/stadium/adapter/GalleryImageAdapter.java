package com.lesports.stadium.adapter;

import java.util.ArrayList;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.Wonderful_Video_bean;
import com.lesports.stadium.utils.imageLoader.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 现场  精彩视频展示 adapter
 * @author Administrator
 *
 */
@SuppressLint("ViewHolder") public class GalleryImageAdapter extends BaseAdapter {
	
	private ArrayList<Wonderful_Video_bean> arrayListIndexFlash ;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	
	public GalleryImageAdapter(Context context,  ArrayList<Wonderful_Video_bean> arrayListIndexFlash) {
		super();
		this.arrayListIndexFlash = arrayListIndexFlash;
		this.mInflater = LayoutInflater.from(context); 
	    this.imageLoader = new ImageLoader(context.getApplicationContext(),R.drawable.bg_default);
	 
	}
	


	public int getCount() {
		if (this.arrayListIndexFlash != null && this.arrayListIndexFlash.size() > 0)
			return Integer.MAX_VALUE;
		return 0;
	}

	public Object getItem(int position) {
		try{
			int index = 0;
			if (arrayListIndexFlash != null && arrayListIndexFlash.size() > 0) {
				if (position < 0) {
					position = position + arrayListIndexFlash.size();
				}
				index = position % arrayListIndexFlash.size();
				return arrayListIndexFlash.get(index);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try{
			int index = position % this.arrayListIndexFlash.size();
			Wonderful_Video_bean voide = (Wonderful_Video_bean) getItem(index);
			convertView = mInflater.inflate(R.layout.item_feature, null);
			final ImageView i = (ImageView) convertView
					.findViewById(R.id.imageView1);
			final ProgressBar mProgressLoading = (ProgressBar) convertView
					.findViewById(R.id.progressLoading);
			
			//add by xj
			i.setScaleType(ImageView.ScaleType.FIT_XY);
//			i.setScaleType(ImageView.ScaleType.MATRIX);
			convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
			imageLoader.DisplayImage(voide.getImg_url(), i, mProgressLoading,640*832);
			
			return convertView;
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	 
}