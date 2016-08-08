/**
 * 
 */
package com.lesports.stadium.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MyPrizeActivity;
import com.lesports.stadium.activity.PrizeDetailActivity;
import com.lesports.stadium.bean.PrizeBean;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.utils.ConstantValue;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 所有订单页面
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
@ResID(id = R.layout.layout_allprizeview)
public class UnReceiverPrizeView extends BaseView {

	@ResID(id = R.id.all_prize_lv)
	private ListView all_prize_lv;
	@ResID(id = R.id.no_all_prize)
	private TextView no_all_prize;
	private List<PrizeBean> list;
	private Myadapter adapter;
	private PrizeBean bean;
	private Intent intent;

	/**
	 * @param context
	 */
	public UnReceiverPrizeView(final Context context) {
		super(context);
	}

	public UnReceiverPrizeView(final Context context, Bundle bunlde) {
		super(context, bunlde);
	}

	@Override
	public void initBaseData() {

	}

	@Override
	public void initView() {
		adapter = new Myadapter();
		all_prize_lv.setAdapter(adapter);
	}
	
	public void setDate(List<PrizeBean> list){
		this.list = list;
		if (list == null || list.size() == 0) {
			all_prize_lv.setVisibility(View.GONE);
			no_all_prize.setVisibility(View.VISIBLE);
		} else {
			no_all_prize.setVisibility(View.GONE);
			all_prize_lv.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void initData() {

	}

	@Override
	public void initListener() {

	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
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
			final ViewHolderFlight holder;
			bean = list.get(position);
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.item_prizeview,
						null);
				holder = new ViewHolderFlight();
				holder.prize_iv = (ImageView) convertView
						.findViewById(R.id.prize_iv);
				holder.prize_name = (TextView) convertView
						.findViewById(R.id.prize_name);
				holder.prize_price_tv = (TextView) convertView
						.findViewById(R.id.prize_price_tv);
				holder.vidality_timer_tv = (TextView) convertView
						.findViewById(R.id.vidality_timer_tv);
				holder.prize_stats = (TextView) convertView
						.findViewById(R.id.prize_stats);
				holder.to_user = (TextView) convertView
						.findViewById(R.id.to_user);
				holder.priz_hold = (RelativeLayout) convertView
						.findViewById(R.id.priz_hold);
				holder.line_price = convertView
						.findViewById(R.id.line_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolderFlight) convertView.getTag();
			}
			holder.to_user.setTag(bean);
			if(position==list.size()-1)
				holder.line_price.setVisibility(View.GONE);
			else
				holder.line_price.setVisibility(View.VISIBLE);	
//			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
//					+ bean.getImage(), holder.prize_iv,R.drawable.app_logo1);
			imageLoader1.displayImage(ConstantValue.BASE_IMAGE_URL+ bean.image + ConstantValue.IMAGE_END, holder.prize_iv,MyFragment.setDefaultImageOptions(R.drawable.zhoubianshangpin_zhanwei));
			holder.prize_name.setText(bean.name);
			String starttime = MyPrizeActivity.getChangeToTime(bean
					.startTime);
			String endtime = MyPrizeActivity.getChangeToTime(bean.endTime);
			holder.vidality_timer_tv.setText(starttime + "-" + endtime);
			String amount = bean.amount;
			if (TextUtils.isEmpty(amount))
				amount = "1";
			holder.prize_price_tv.setText(bean.price + " x " + amount);
			String status = bean.status;
			// 1为实物，2为虚拟
			if ("1".equals(bean.type)) { 
				if ("0".equals(status)) {
					holder.prize_stats.setText("未领取");
					holder.to_user.setText("去领取");
				}else{
					//实物去查看详情
					holder.to_user.setText("查看详情");
					holder.prize_stats.setText("已领取");
				}
			}else{
				//虚拟的优惠劵 可以去使用
				holder.to_user.setText("去使用");
				holder.prize_stats.setText("已领取");
			}
			holder.to_user.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PrizeBean prizeBean = (PrizeBean) v.getTag();
					if(prizeBean==null){
						return;
					}
					String status = prizeBean.status;
					//去领取 
					intent = new Intent(context,PrizeDetailActivity.class);
					Bundle bundler = new Bundle();
					bundler.putString("prize_id", prizeBean.prizeId);
					bundler.putString("prize_name", prizeBean.name);
					bundler.putString("deliveryId", prizeBean.deliveryId);
					bundler.putString("id", prizeBean.id);
					String amount = prizeBean.amount;
					if (TextUtils.isEmpty(amount))
						amount = "1";
					bundler.putString("prize_price", prizeBean.price + " x " + amount);
					String starttime = MyPrizeActivity.getChangeToTime(bean
							.startTime);
					String endtime = MyPrizeActivity.getChangeToTime(bean.endTime);
					bundler.putString("prize_valide", starttime + "-" + endtime);
					bundler.putString("staty", prizeBean.status);
					intent.putExtra("bundle", bundler);
					context.startActivity(intent);
				}
			});
			return convertView;
		}

	}

	class ViewHolderFlight {
		// 图片
		public ImageView prize_iv;
		// 名称
		public TextView prize_name;
		// 价格
		public TextView prize_price_tv;
		// 有效期
		public TextView vidality_timer_tv;
		// 状态 已领取，未领取
		public TextView prize_stats;
		// 状态 去领取，去使用
		public TextView to_user;
		// 整个条目
		public RelativeLayout priz_hold;
		//底部线条
		public View line_price;
	}

}
