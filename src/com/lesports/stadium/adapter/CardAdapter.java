package com.lesports.stadium.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lesports.stadium.bean.HeightLightBean;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

public abstract class CardAdapter<T> extends BaseAdapter {

    private List<HeightLightBean> data;
    private Context context;

    public CardAdapter(Context context) {
        super();
        this.context = context;
        data = new  ArrayList<HeightLightBean>();
    }

    public CardAdapter( List<HeightLightBean> data, Context context) {
        super();
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout contain = (FrameLayout) convertView;
        View cardView;
        View coverView;
        if (contain == null) {
            contain = new FrameLayout(context);
 //           contain.setBackgroundResource(R.drawable.meet_white_bg);
            cardView = getCardView(position, convertView, parent);
            contain.addView(cardView);
        } else {
            cardView = contain.getChildAt(0);
            coverView = getCardView(position, cardView, contain);
            contain.removeView(cardView);
            contain.addView(coverView);
        }

        return contain;
    }

    public abstract View getCardView(int position, View convertView, ViewGroup parent);

    public  List<HeightLightBean> getData() {
        return data;
    }

    public void setData( List<HeightLightBean> data) {
        this.data = data;
    }


}
