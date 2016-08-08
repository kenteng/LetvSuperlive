package com.lesports.stadium.picture;

/**
 * Created by like on 2015/9/29.
 */
public class Bean {
	
	public Bean(String url,int items){
		this.imgResId=url;
		this.items=items;
	}
    private String imgResId;

	public String getImgResId() {
		return imgResId;
	}

	public void setImgResId(String imgResId) {
		this.imgResId = imgResId;
	}
	
	private int items;

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

}
