package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.StatusExpandAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ChildStatusEntity;
import com.lesports.stadium.bean.GroupStatusEntity;
import com.lesports.stadium.view.CustomExpandableListView;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 查看物流界面
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
public class SeeLogiestActivity extends BaseActivity implements OnClickListener{
	
	/**
	 * 展示物流数据的listview
	 */
	private ExpandableListView expandlistView;
	/**
	 * 适配器
	 */
	private StatusExpandAdapter statusAdapter;
	/**
	 * 上下问对象
	 */
	private Context context;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 图片
	 */
	private ImageView mImage;
	/**
	 * 物流状态
	 */
	private TextView mStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_logiest);
		context = this;
		initview();
		initExpandListView();
	}
	/**
	 *初始化界面view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist_shiguangzhou);
		mBack=(ImageView) findViewById(R.id.chakanwuliu_top_back);
		mBack.setOnClickListener(this);
		mImage=(ImageView) findViewById(R.id.chakanwuliu_dingbu_tupian_image);
		mStatus=(TextView) findViewById(R.id.chakanwuliu_dingbu_tupian_wuliuzhuangtai_ing);
	}
	/**
	 * 初始化可拓展列表
	 */
	private void initExpandListView() {
		statusAdapter = new StatusExpandAdapter(context, getListData());
		expandlistView.setAdapter(statusAdapter);
		expandlistView.setGroupIndicator(null); // 去掉默认带的箭头
		expandlistView.setSelection(0);// 设置默认选中项

		// 遍历所有group,将所有项设置成默认展开
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);
		}

		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}
	private List<GroupStatusEntity> getListData() {
		List<GroupStatusEntity> groupList;
		String[] strArray = new String[] {"10月22日","10月22日","10月22日", "10月23日", "10月25日" };
		String[][] childTimeArray = new String[][] {
				{ "俯卧撑十次", "仰卧起坐二十次", "大喊我爱紫豪二十次", "每日赞紫豪一次" },{ "俯卧撑十次", "仰卧起坐二十次", "大喊我爱紫豪二十次", "每日赞紫豪一次" },{ "俯卧撑十次", "仰卧起坐二十次", "大喊我爱紫豪二十次", "每日赞紫豪一次" },
				{ "亲，快快滴点赞哦~" }, { "没有赞的，赶紧去赞哦~" } };
		groupList = new ArrayList<GroupStatusEntity>();
		for (int i = 0; i < strArray.length; i++) {
			GroupStatusEntity groupStatusEntity = new GroupStatusEntity();
			groupStatusEntity.setGroupName(strArray[i]);

			List<ChildStatusEntity> childList = new ArrayList<ChildStatusEntity>();

			for (int j = 0; j < childTimeArray[i].length; j++) {
				ChildStatusEntity childStatusEntity = new ChildStatusEntity();
				childStatusEntity.setCompleteTime(childTimeArray[i][j]);
				childStatusEntity.setIsfinished(true);
				childList.add(childStatusEntity);
			}

			groupStatusEntity.setChildList(childList);
			groupList.add(groupStatusEntity);
		}
		return groupList;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.chakanwuliu_top_back:
			finish();
			break;

		default:
			break;
		}
	}

}
