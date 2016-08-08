package com.lesports.stadium.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.Seat;
import com.lesports.stadium.bean.SeatInfo;
import com.lesports.stadium.view.OnSeatClickListener;
import com.lesports.stadium.view.SSThumView;
import com.lesports.stadium.view.SSView;


/**
 * 
 * ***************************************************************
 * @ClassName:  SelectSeatActivity 
 * 
 * @Desc : 选择座位，座位分布图
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
public class SelectSeatActivity extends BaseActivity implements OnClickListener{
	private static final int ROW = 20; // 列
	private static final int EACH_ROW_COUNT = 30; // 行
	/**自定义座位选择器*/
	private SSView mSSView;
	/**自定义座位选择器缩略图*/
	private SSThumView mSSThumView;
	/**座位列表*/
	private ArrayList<SeatInfo> list_seatInfos = new ArrayList<SeatInfo>();
	/**座位条件*/
	private ArrayList<ArrayList<Integer>> list_seat_conditions = new ArrayList<ArrayList<Integer>>();
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_selectseat, null);
		//添加布局
		addView(view);
		initView();
	}
	  private void initView(){
		  hiddenTitleLayout(false);
			hiddenTittleContent(false, "选择座位");
			getGoBack().setOnClickListener(this);
			
			//添加座位

			mSSView = (SSView) this.findViewById(R.id.mSSView);
			mSSThumView = (SSThumView) this.findViewById(R.id.ss_ssthumview);
			// mSSView.setXOffset(20);
			setSeatInfo();
			mSSView.init(EACH_ROW_COUNT, ROW, list_seatInfos, list_seat_conditions,
					mSSThumView, 5);
			mSSView.setOnSeatClickListener(new OnSeatClickListener() {

				@Override
				public boolean b(int column_num, int row_num, boolean paramBoolean) {
					String desc = "您选择了第" + (row_num + 1) + "排" + " 第"
							+ (column_num + 1) + "列";
					Toast.makeText(SelectSeatActivity.this, desc.toString(),
							Toast.LENGTH_SHORT).show();
					return false;
				}

				@Override
				public boolean a(int column_num, int row_num, boolean paramBoolean) {
					String desc = "您取消了第" + (row_num + 1) + "排" + " 第"
							+ (column_num + 1) + "列";
					Toast.makeText(SelectSeatActivity.this, desc.toString(),
							Toast.LENGTH_SHORT).show();
					return false;
				}

				@Override
				public void a() {
					// TODO Auto-generated method stub

				}
			});
		
			
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
	 * 设置座位条件
	 */
	private void setSeatInfo() {
		for (int i = 0; i < ROW; i++) {//8行
			SeatInfo mSeatInfo = new SeatInfo();
			ArrayList<Seat> mSeatList = new ArrayList<Seat>();
			ArrayList<Integer> mConditionList = new ArrayList<Integer>();
			for(int j=0;j<EACH_ROW_COUNT;j++){//每排20个座位 
				Seat mSeat = new Seat();
				if(j<0){
					mSeat.setN("Z");
					mConditionList.add(0);
				}else{
					mSeat.setN(String.valueOf(j-2));
					if(j<15&&j>10){
						mConditionList.add(2);
					}else if(j<19&&j>16){
						if(i>3&&i<7){
							mConditionList.add(0);
						}else{
							mConditionList.add(2);
						}
					}
						else{
						mConditionList.add(1);
					}
					
				}
				mSeat.setDamagedFlg("");
				mSeat.setLoveInd("0");
				mSeatList.add(mSeat);
			}
			mSeatInfo.setDesc(String.valueOf(i+1));
			mSeatInfo.setRow(String.valueOf(i+1));
			mSeatInfo.setSeatList(mSeatList);
			list_seatInfos.add(mSeatInfo);
			list_seat_conditions.add(mConditionList);
		}

	}
}
