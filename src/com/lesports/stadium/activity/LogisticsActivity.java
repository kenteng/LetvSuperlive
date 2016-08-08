package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.StatusExpandAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ChildStatusEntity;
import com.lesports.stadium.bean.GroupStatusEntity;
/**
 * 
 * ***************************************************************
 * @ClassName:  LogisticsActivity 
 * 
 * @Desc : 物流查询
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class LogisticsActivity extends BaseActivity implements OnClickListener{
	private ExpandableListView expandlistView;
	private StatusExpandAdapter statusAdapter;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_logistics, null);
		//添加布局
		addView(view);
		initView();
	}
	private void initView(){
		context = this;
		//添加标题
		hiddenTitleLayout(false);
		hiddenTittleContent(false, "查看物流");	
		getGoBack().setOnClickListener(this);
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		initExpandListView();
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_textview:
			finish();
			break;

		default:
			break;
		}
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
		String[] strArray = new String[] { "10月22日", "10月23日", "10月25日" };
		String[][] childTimeArray = new String[][] {
				{ "俯卧撑十次", "仰卧起坐二十次", "大喊我爱紫豪二十次", "每日赞紫豪一次" },
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
}
