/**
 * ***************************************************************
 * @ClassName:  LEDActivity 
 * 
 * @Desc : LED的activity
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-1 下午7:03:17
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lesports.stadium.R;

/**
 * @author simon
 * 
 */
public class LEDActivity extends Activity {
	/**
	 * seekbar 的color
	 */
	private SeekBar colorBar;
	/**
	 * seekbar 的speed
	 */
	private SeekBar speedBar;
	/**
	 * 自己输入的显示文字
	 */
	private TextView tv_edittext_show;
	/**
	 * 显示推荐的Listview
	 */
	private ListView lv_recommend;
	/**
	 * 输入的编辑文字
	 */
	private EditText et_input;
	/**
	 * 速度
	 */
	private int speed = 100;
	/**
	 * 颜色
	 */
	private int color=Color.RED ;
	/**
	 * 文字
	 */
	private String text = "I love you";
	/**
	 * 推荐
	 */
	private List<String> recommendList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led);
		initView();
		initData();
		initListener();
	}

	/**
	 * 
	 */
	private void initListener() {

		bt_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str=et_input.getText().toString();
				if("".equals(str.trim())){
					
				}else{
					recommendList.add(et_input.getText() + "");
					recommendAdapter.notifyDataSetChanged();
					et_input.setText("");
				}
			}
		});

		tv_edittext_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent ledTextShowIntent=new Intent();
				// ledTextShowIntent.setClass(LEDActivity.this,
				// LEDTextShowActivity.class);
				// ledTextShowIntent.putExtra("color", color);
				// ledTextShowIntent.putExtra("speed", speed);
				// ledTextShowIntent.putExtra("text", text);
				// startActivity(ledTextShowIntent);Intent mintent = new
				// Intent();
				Intent mintent = new Intent();
				mintent.setClass(LEDActivity.this, LEDShowActivity.class);
				mintent.putExtra("hanziorientation", true);
				mintent.putExtra("moveorientation", true);
				mintent.putExtra("hanzicolor",color);
				mintent.putExtra("hanziheight", 16);
				mintent.putExtra("speed", speed);
				mintent.putExtra("LEDshape", false);
				mintent.putExtra("mstring", text);
				LEDActivity.this.startActivity(mintent);
			}
		});

		colorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * 拖动条停止拖动的时候调用
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			/**
			 * 拖动条开始拖动的时候调用
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			/**
			 * 拖动条进度改变的时候调用
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// if(progress<30){
				// // ShapeDrawable bgShape =
				// (ShapeDrawable)tv_edittext_show.getBackground();
				// // bgShape.getPaint().setColor(Color.RED);
				// color="red";
				// tv_edittext_show.setTextColor(Color.RED);
				// // GradientDrawable p = (GradientDrawable)
				// tv_edittext_show.getBackground();
				// // p.setStroke(2, Color.RED);
				// }else if(progress>=30&&progress<60){
				// color="green";
				// tv_edittext_show.setTextColor(Color.GREEN);
				// // GradientDrawable p = (GradientDrawable)
				// tv_edittext_show.getBackground();
				// // p.setStroke(2, Color.GREEN);
				// }else{
				// color="yellow";
				// tv_edittext_show.setTextColor(Color.YELLOW);
				// // GradientDrawable p = (GradientDrawable)
				// tv_edittext_show.getBackground();
				// // p.setStroke(2, Color.YELLOW);
				// }
				 GradientDrawable p = (GradientDrawable)
						 colorBar.getThumb();
				switch (progress) {
				case 0:
					p.setColor(Color.RED);
		
					
					 color= Color.RED;
					 tv_edittext_show.setTextColor(Color.RED);
					break;
				case 1:
			//		p.setStroke(2, Color.GREEN);
					p.setColor(Color.GREEN);
					 color= Color.GREEN;
					 tv_edittext_show.setTextColor(Color.GREEN);
					break;
				case 2:
					p.setColor(Color.BLUE);
					 color= Color.BLUE;
					 tv_edittext_show.setTextColor(Color.BLUE);
					break;
				case 3:
					p.setColor(Color.YELLOW);
					 color= Color.YELLOW;
					 tv_edittext_show.setTextColor(Color.YELLOW);
					break;
				case 4:
					p.setColor(Color.CYAN);
					 color= Color.CYAN;
					 tv_edittext_show.setTextColor(Color.CYAN);
					break;
				case 5:
					p.setColor(Color.MAGENTA);
					 color= Color.MAGENTA;
					 tv_edittext_show.setTextColor(Color.MAGENTA);
					break;
				case 6:
					p.setColor(Color.WHITE);
					 color= Color.WHITE;
					 tv_edittext_show.setTextColor(Color.WHITE);
					break;

				default:
					break;
				}
			}
		});

		speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			/**
			 * 拖动条停止拖动的时候调用
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			/**
			 * 拖动条开始拖动的时候调用
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			/**
			 * 拖动条进度改变的时候调用
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
//				if (progress < 30) {
//					speed = "1";
//					marqueeView.mSpeed = 15;
//				} else if (progress >= 30 && progress < 60) {
//					speed = "2";
//					marqueeView.mSpeed = 8;
//				} else {
//					speed = "3";
//					marqueeView.mSpeed = 3;
//				}
				if(progress<10){
					marqueeView.mSpeed = 22;
					speedBar.setProgress(0);
				}else if(progress<20){
					marqueeView.mSpeed = 20;
					speedBar.setProgress(10);
				}else if(progress<30){
					marqueeView.mSpeed = 18;
					speedBar.setProgress(20);
				}else if(progress<40){
					marqueeView.mSpeed = 16;
					speedBar.setProgress(30);
				}else if(progress<50){
					marqueeView.mSpeed = 14;
					speedBar.setProgress(40);
				}else if(progress<60){
					marqueeView.mSpeed = 12;
					speedBar.setProgress(50);
				}else if(progress<70){
					marqueeView.mSpeed = 10;
					speedBar.setProgress(60);
				}else if(progress<80){
					marqueeView.mSpeed = 8;
					speedBar.setProgress(70);
				}else if(progress<90){
					marqueeView.mSpeed = 6;
					speedBar.setProgress(80);
				}else if(progress<100){
					marqueeView.mSpeed = 4;
					speedBar.setProgress(90);
				}else{
					marqueeView.mSpeed = 2;
					speedBar.setProgress(100);
				}
				speed = 100-speedBar.getProgress();
				marqueeView.reStart();
			}
		});

		et_input.addTextChangedListener(textWatcher);

	}

	/**
	 * 文字输入的监听
	 */
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			tv_edittext_show.setText(s);
			text = s.toString();
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	/*
	 * 发送按钮
	 */
	private TextView bt_send;
	private RecommendAdapter recommendAdapter;
	private com.lesports.stadium.view.MarqueeView marqueeView;
	private ImageButton ib_back;
	private ImageView iv_background;

	/**
	 * 
	 */
	private void initView() {

		colorBar = (SeekBar) findViewById(R.id.seekBar1);
		speedBar = (SeekBar) findViewById(R.id.seekBar2);
		tv_edittext_show = (TextView) findViewById(R.id.tv_edittext_show);
		lv_recommend = (ListView) findViewById(R.id.lv_recommend);
		et_input = (EditText) findViewById(R.id.discuss_input_et);
		bt_send = (TextView) findViewById(R.id.sendview);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		iv_background = (ImageView) findViewById(R.id.iv_background);
		
//		iv_background.setBackground(LiveDetialActivity.blurBackground);
		
		ib_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		marqueeView = (com.lesports.stadium.view.MarqueeView) findViewById(R.id.marqueeView);
		
		
		
		
		recommendList.add("Happy birthday!");
		recommendList.add("Merry christmas!");

		recommendAdapter = new RecommendAdapter();
		lv_recommend.setAdapter(recommendAdapter);

		lv_recommend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				tv_edittext_show.setText(recommendList.get(arg2));
				text = recommendList.get(arg2);
			}
		});
	}

	public class RecommendAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return recommendList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return recommendList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = View.inflate(LEDActivity.this,
					R.layout.led_recommend_item, null);
			TextView item = (TextView) convertView.findViewById(R.id.tv_item);
			item.setText(recommendList.get(position));

			return convertView;
		}

	}

	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub

	}

}
