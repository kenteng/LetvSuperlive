/**
 * 
 */
package com.lesports.stadium.view;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.UseCounponActivity;
import com.lesports.stadium.adapter.CounponAdapter;
import com.lesports.stadium.bean.YouHuiBean;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 已经失效的优惠券优惠券
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@ResID(id = R.layout.layout_bukeyishiyong)
public class InvalindView extends BaseView {
	@ResID(id = R.id.bukeyishiyong_all_prize_lv)
	private ListView all_prize_lv;
	@SuppressWarnings("unused")
	private List<String> list;

	/**
	 * 上下文
	 */
	private UseCounponActivity mContext;
	/**
	 * 数据源集合
	 */
	private List<YouHuiBean> mList;
	@SuppressWarnings("unused")
	private String goodpricd;
	/**
	 * @param context
	 */
	public InvalindView(final Context context) {
		super(context);
		this.mContext=(UseCounponActivity)context;
	}
	/**
	 * 从主界面中将优惠券数据传递过来
	 * @param list
	 * @param goodsprice 
	 */
	public void setList(List<YouHuiBean> list, String goodsprice){
		this.mList=list;
		this.goodpricd=goodsprice;
		//这里需要调用方法从服务器获取数据，目前先模拟假数据
		if(mList!=null&&mList.size()!=0){
			initListviewData(mList);
		}
		
	}
	/**
	 * 该方法用来初始化界面列表项的假数据
	 */
	private void initListviewData(final List<YouHuiBean> list) {
		final CounponAdapter adapter=new CounponAdapter(list, mContext);
		all_prize_lv.setAdapter(adapter);
//		all_prize_lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				mResultBean=list.get(arg2);
//				for(int i=0;i<list.size();i++){
//					if(arg2==i){
//						list.get(i).setChoise(true);
//					}else{
//						list.get(i).setChoise(false);
//					}
//				}
//				adapter.refreshUI(list);
//			}
//		});
		all_prize_lv.setEmptyView(findViewById(R.id.youhuiquanbukeyong_meishuju));
		
	}

	public InvalindView(final Context context, Bundle bunlde) {
		super(context, bunlde);
	}

	@Override
	public void initBaseData() {

	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {

	}


}
