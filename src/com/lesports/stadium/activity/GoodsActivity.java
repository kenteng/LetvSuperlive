package com.lesports.stadium.activity;

import java.util.ArrayList;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.Goods;
import com.lesports.stadium.engine.GetGoods;
import com.lesports.stadium.engine.impl.GET_Shopping_goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
* @ClassName: GoodsActivity 
*
* @Description: 获取商品详情界面
*
* @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
*
* @author wangxinnian
* 
* @date 2016-7-19 下午5:18:45 
* 
*
 */
public class GoodsActivity extends Activity {
	private ListView goods_list;
	private TextView goods_no;
	private Myadapter adapter;
	private ArrayList<Goods> goods = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		initview();
		initDate();
	}

	/**
	 * 
	 * @Title: initDate   
	 * @Description: 初始化数据   
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private void initDate() {
		GET_Shopping_goods video = new GET_Shopping_goods();
		video.GetShoppGoods(new GetGoods() {
			
			@Override
			public void getAllGoods(ArrayList<Goods> goodls) {
				if(goodls!=null){
					goods_list.setVisibility(View.VISIBLE);
					goods_no.setVisibility(View.GONE);
					goods = goodls;
					adapter.notifyDataSetChanged();
				}else{
					goods_no.setVisibility(View.VISIBLE);
					goods_list.setVisibility(View.GONE);
				}
				
			}
		});
		
	}
	/**
	 * 
	 * @Title: initview   
	 * @Description: 初始化view  
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	private void initview() {
		goods_list = (ListView) findViewById(R.id.goods_list);
		goods_no = (TextView) findViewById(R.id.goods_no);
		goods_no.setText("正在获取商品。。。");
		adapter = new Myadapter();
		goods_list.setAdapter(adapter);
		
	}
	class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return goods==null?0:goods.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Goods good = goods.get(position);
			TextView tv = new TextView(getApplicationContext());
			tv.setText("商品："+good.getGoodsName()+"     价格："+good.getPrice());
			return tv;
		}
		
	}

}
