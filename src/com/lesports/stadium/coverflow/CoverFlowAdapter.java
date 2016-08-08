package com.lesports.stadium.coverflow;

import java.util.ArrayList;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.utils.ConstantValue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoverFlowAdapter extends BaseAdapter {
	
	private ArrayList<GameEntity> mData = new ArrayList<GameEntity>(0);
	private Context mContext;

	public CoverFlowAdapter(Context context) {
		mContext = context;
	}
	
	public void setData(ArrayList<GameEntity> data) {
		mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int pos) {
		return mData.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_coverflow, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.label);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
//可以将数据实体类对象的int型图片资源修改为image的url，在这里调用工具类来下载图片
//        holder.image.setImageResource(mData.get(position).imageResId);
        LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+ mData.get(position).imageResId + ConstantValue.IMAGE_END, holder.image,
				R.drawable.default_image);
        holder.text.setText(mData.get(position).titleResId);

		return rowView;
	}


    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
}
