/**
 * 
 */
package com.lesports.stadium.adapter;

import java.util.HashMap;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.OrderAddressBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @Desc : 购物车适配器
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

public class OrderAddressAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<OrderAddressBean> mList;
	/**
	 * 上下文对象
	 */
	private Context mContext;
	/**
	 * 用来以键值对的形式存储数据是否被选中
	 */
	private static HashMap<Integer, Boolean> isSelected;
	/**
	 * 
	 * @param mliList
	 * @param mcoonContext
	 */

	public OrderAddressAdapter(List<OrderAddressBean> mliList, Context mcoonContext) {
		this.mList = mliList;
		this.mContext = mcoonContext;
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	/**
	 * 初始化被选定的数据
	 * 
	 * @2016-2-22上午11:33:41
	 */
	private void initDate() {
		for (int i = 0; i < mList.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		OrderAddressAdapter.isSelected = isSelected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolderBuy vh = null;
		if (arg1 == null) {
			vh = new ViewHolderBuy();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.item_order_shouhuofangshi, null);
			vh.mGoodsName=(TextView) arg1.findViewById(R.id.order_shouhuo_address_name);
			vh.mCheckbox=(CheckBox) arg1.findViewById(R.id.order_shouhuo_address_isuse);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolderBuy) arg1.getTag();
		}
		vh.mGoodsName.setText(mList.get(arg0).getName());
		vh.mCheckbox.setChecked(mList.get(arg0).isChoosed());
		return arg1;
	}

	/**
	 * 
	 * ***************************************************************
	 * @Modify : null
	 * checkbox的监听方法
	 * ***************************************************************
	 */
	private final class CheckBoxChangedListener implements
			OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton cb, boolean flag) {
			int position = (Integer) cb.getTag();
			getIsSelected().put(position, flag);
		    mList.get(position).setChoosed(flag);
		}

	}
	static class ViewHolderBuy {
		/**
		 * 是否选定
		 */
		CheckBox mCheckbox;
		/**
		 * 商品名称
		 */
		TextView mGoodsName;
	}

}
