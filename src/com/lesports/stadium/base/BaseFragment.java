package com.lesports.stadium.base;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @ClassName:  BaseFragment   
 * @Description:fragment的基类  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:21:23   
 *
 */
@SuppressLint("NewApi") public abstract class BaseFragment extends Fragment implements OnItemClickListener {
	/**
	 * 需要展示的view
	 */
	public View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		initListener();
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 初始化布局
	 * @param inflater
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
	/**
	 * 初始化监听
	 */
	public abstract void initListener();
	/**
	 * 初始化数据
	 * @param savedInstanceState
	 */
	public abstract void initData(Bundle savedInstanceState);
}
