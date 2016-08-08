package com.lesports.stadium.adapter;

import java.util.HashMap;
import java.util.List;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.PhoneInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 手机联系人Adapter
 * @author 盛之刚
 *
 */
public class PhoneAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<PhoneInfo> list;
	@SuppressWarnings("unused")
	private HashMap<String, Integer> alphaIndexer;
	@SuppressWarnings("unused")
	private Context context;
	private int flag;

	public PhoneAdapter(Context con, List<PhoneInfo> list,int flag) {
		if (con != null) {
			this.context = con;
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.flag = flag;
			alphaIndexer = new HashMap<String, Integer>();
		}
	}


	@Override
	public int getCount() {
		if (this.context != null) {
			return list.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sharefriend_ditail,
					parent, false);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (list.get(position).getPhoneName() != null) {
			holder.name.setText(list.get(position).getPhoneName());
		}
		if (flag == 1) {
			PhoneInfo phoneInfo = list.get(position);

			String currentLetter = String.valueOf(phoneInfo.getPinyin().charAt(
					0));
			String indexLetter = null;
			if (position == 0) {
				indexLetter = currentLetter;
			} else {
				String preLetter = String.valueOf(list.get(position - 1)
						.getPinyin().charAt(0));
				if (!currentLetter.equals(preLetter)) {
					indexLetter = currentLetter;
				}
			}
			holder.alpha.setVisibility(indexLetter == null ? View.GONE
					: View.VISIBLE);
			if (holder.alpha.getVisibility() == View.VISIBLE) {
				holder.alpha.setText(indexLetter);
			}
		}else {
			holder.alpha.setVisibility(View.GONE);
		}
		holder.phone.setText(list.get(position).getPhone());
		

		return convertView;

	}

	class ViewHolder {
		public TextView alpha;
		public TextView name;
		public TextView phone;
	}
}
