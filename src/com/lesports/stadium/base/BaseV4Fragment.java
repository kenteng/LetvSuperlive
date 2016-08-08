/**
 * ***************************************************************
 * @ClassName:  BaseV4Fragment 
 * 
 * @Desc : TODO(这里用一句话描述这个类的作用)
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-1 上午11:58:22
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

package com.lesports.stadium.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author simon
 *
 */
public abstract class BaseV4Fragment extends Fragment implements OnItemClickListener{

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
