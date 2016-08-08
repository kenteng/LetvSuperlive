package com.lesports.stadium.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.LyricAdapter;
import com.lesports.stadium.adapter.SongAdapter;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.bean.LyricsListBean;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.Mylistview;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 用于展示歌词的fragment
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class LyricsFragment extends BaseV4Fragment implements OnClickListener {

	/**
	 * 需要点击以后弹出歌曲选择列表的layout
	 */
	private RelativeLayout mLayoutSongList;
	/**
	 * 需要动态切换的背景图片
	 */
	private LinearLayout mLayoutBackground;
	/**
	 * 歌曲列表项内容
	 */
	private List<String> mlistSong;
	/**
	 * 歌词内容
	 */
	private List<String> mListLyric = new ArrayList<String>();
	/**
	 * 活动的id，目的是为了获取该活动下面的歌单列表
	 */
	private String id;
	/**
	 * 歌曲列表布局
	 */
	private RelativeLayout mLayout_songlist;
	/**
	 * 歌曲列表的列表项
	 */
	private ListView mSongListviw;
	/**
	 * 是否显示
	 */
	private boolean isShow = false;
	/**
	 * 存储解析数据的集合
	 */
	/**
	 * 歌词内容的listview
	 */
	private Mylistview mCustListview;
	/**
	 * 展示歌词数据的scrollview
	 */
	private ScrollView mScrollview;
	/**
	 * 歌曲名称
	 */
	private TextView mSongName;
	/**
	 * 歌曲演唱者
	 */
	private TextView mSongAuther;
	/**
	 * 获取歌曲内容
	 */
	private final int CONTENT = 2;
	/**
	 * 获取歌单成功
	 */
	private final int LYRICS_LIST = 1;

	/**
	 * 处理数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case LYRICS_LIST:
				String backdata = (String) msg.obj;
				if (backdata != null && backdata.length() != 0
						&& !TextUtils.isEmpty(backdata)) {
					JsonParserDataToAdapter(backdata);
				} else {

				}

				break;
			case CONTENT:
				// 歌词内容
				String backdatas = (String) msg.obj;
				if (backdatas != null && backdatas.length() != 0
						&& !TextUtils.isEmpty(backdatas)) {
					JsonParserDataToAdapterLyric(backdatas);
				}
			default:
				break;
			}

		}
	};

	/**
	 * 构成方法 传递 当前活动id
	 * 
	 * @param ids
	 */
	public LyricsFragment(String ids) {
		this.id = ids;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_lyric, null);
		initViews(view);
		// popFilter = new PopupWindow(getActivity());
		// View viewFilter = inflater.inflate(R.layout.window_grade,
		// null);
		// ll_popul_filter = (LinearLayout) viewFilter
		// .findViewById(R.id.layout_window);
		// mListviewSong=(ListView) viewFilter.findViewById(R.id.flow_listview);
		// int ScreenHeight=getScreenHeight(getActivity());
		// popFilter.setWidth(LayoutParams.MATCH_PARENT);
		// popFilter.setHeight(LayoutParams.MATCH_PARENT);
		// popFilter.setBackgroundDrawable(new BitmapDrawable());
		// popFilter.setFocusable(true);
		// popFilter.setOutsideTouchable(true);
		// popFilter.setContentView(viewFilter);
		userUtilsGetData(id);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		// userUtilsGetData(id);
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetLyricData(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.LYRIC_CONTENT, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Object obj = data.getObject();
						String backdata = obj.toString();
						Message msg = new Message();
						msg.arg1 = CONTENT;
						msg.obj = backdata;
						handler.sendMessage(msg);
					}

				}, false, true);
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetData(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.LYRICS_LIST_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							Message msg = new Message();
							msg.arg1 = LYRICS_LIST;
							msg.obj = backdata;
							handler.sendMessage(msg);
						}
					}

				}, false, true);
	}

	/**
	 * 解析获取到的歌曲列表数据，并且将加载到适配器里面
	 * 
	 */
	private void JsonParserDataToAdapter(String backdata) {
		final List<LyricsListBean> listList = new ArrayList<LyricsListBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				LyricsListBean bean = new LyricsListBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("embraceCount")) {
					bean.setEmbraceCount(obj.getString("embraceCount"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("flowerCount")) {
					bean.setFlowerCount(obj.getString("flowerCount"));
				}
				if (obj.has("kissCount")) {
					bean.setKissCount(obj.getString("kissCount"));
				}
				if (obj.has("loveCount")) {
					bean.setLoveCount(obj.getString("loveCount"));
				}
				if (obj.has("musicName")) {
					bean.setMusicName(obj.getString("musicName"));
				}
				if (obj.has("singer")) {
					bean.setSinger(obj.getString("singer"));
				}
				if (obj.has("lyric")) {
					bean.setLyric(obj.getString("lyric"));
				}
				if (obj.has("musicsort")) {
					bean.setMusicsort(obj.getString("musicsort"));
				}
				listList.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		SongAdapter adapters = new SongAdapter(listList, getActivity());
		mSongListviw.setAdapter(adapters);
		// //首先需要将默认数据加载上
		// if(listList!=null&&listList.size()!=0){
		// userUtilsGetLyricData(listList.get(0).getId());
		// }
		mSongListviw.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 调用方法，将该item中对应的歌词数据传递过去，进行解析适配
				userUtilsGetLyricData(listList.get(arg2).getId());
				// popFilter.dismiss();
				// if(!isShow){
				// isShow=!isShow;
				// mLayout_songlist.setVisibility(View.VISIBLE);
				// }else{
				isShow = !isShow;
				mLayout_songlist.setVisibility(View.GONE);
				// }
			}
		});
	}

	/**
	 * 解析获取到的歌曲列表数据，并且将加载到适配器里面
	 */
	private void JsonParserDataToAdapterLyric(String backdata) {
		LyricsListBean bean = new LyricsListBean();
		try {
			JSONObject obj = new JSONObject(backdata);

			if (obj.has("activityId")) {
				bean.setActivityId(obj.getString("activityId"));
			}
			if (obj.has("embraceCount")) {
				bean.setEmbraceCount(obj.getString("embraceCount"));
			}
			if (obj.has("id")) {
				bean.setId(obj.getString("id"));
			}
			if (obj.has("flowerCount")) {
				bean.setFlowerCount(obj.getString("flowerCount"));
			}
			if (obj.has("kissCount")) {
				bean.setKissCount(obj.getString("kissCount"));
			}
			if (obj.has("loveCount")) {
				bean.setLoveCount(obj.getString("loveCount"));
			}
			if (obj.has("musicName")) {
				bean.setMusicName(obj.getString("musicName"));
			}
			if (obj.has("singer")) {
				bean.setSinger(obj.getString("singer"));
			}
			if (obj.has("lyric")) {
				bean.setLyric(obj.getString("lyric"));
			}
			if (obj.has("musicsort")) {
				bean.setMusicsort(obj.getString("musicsort"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		addLyricData(bean);
	}

	/**
	 * 将得到的歌词数据进行解析，并且将至进行加载
	 * 
	 * @param bean
	 */
	private void addLyricData(LyricsListBean bean) {
		// 调用方法，获取歌词数据
		mSongName.setText(bean.getMusicName());
		mSongAuther.setText(bean.getSinger());
		// 调用方法，进行解析数据
		mListLyric.clear();
		mListLyric = anynaricData(bean);
		if (mListLyric != null && mListLyric.size() != 0) {
			LyricAdapter adapter = new LyricAdapter(mListLyric, getActivity());
			mCustListview.setAdapter(adapter);
		}
	}

	/**
	 * 解析提炼歌词数据
	 * 
	 * @param bean
	 * @return
	 */
	private List<String> anynaricData(LyricsListBean bean) {
		List<String> list = new ArrayList<String>();
		if (!TextUtils.isEmpty(bean.getLyric())) {
			String[] str = bean.getLyric().split("/r/n");
			int length = str.length;
			// list.add(bean.getMusicName());
			// list.add("作者："+bean.getSinger());
			for (int i = 0; i < length; i++) {
				list.add(str[i]);
			}
		}
		return list;

	}

	/**
	 * @param view
	 * @2016-2-1下午3:20:11 初始化控件
	 */
	private void initViews(View view) {
		// TODO Auto-generated method stub
		mSongAuther = (TextView) view
				.findViewById(R.id.lyric_scrollview_zuozhe);
		mSongName = (TextView) view
				.findViewById(R.id.lyric_scrollview_gequmingcheng);
		mScrollview = (ScrollView) view
				.findViewById(R.id.lyric_scrollview_geci);
		mScrollview.clearFocus();
		mCustListview = (Mylistview) view
				.findViewById(R.id.lyric_scrollview_neirong);
		mCustListview.clearFocus();
		mLayoutSongList = (RelativeLayout) view
				.findViewById(R.id.lyric_layout_list);
		mLayoutSongList.setOnClickListener(this);
		mLayoutBackground = (LinearLayout) view
				.findViewById(R.id.lyric_layout_bg);
		mLayout_songlist = (RelativeLayout) view
				.findViewById(R.id.layout_lyric_song_list);
		mSongListviw = (ListView) view.findViewById(R.id.lyric_song_new_list);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lyric_layout_list:
			// 弹出window，进行歌曲选择
			// 筛选
			// ll_popul_filter.startAnimation(AnimationUtils.loadAnimation(
			// getActivity(), R.anim.activity_translate_in));
			// popFilter.showAsDropDown(mLayoutSongList);
			if (!isShow) {
				isShow = !isShow;
				mLayout_songlist.setVisibility(View.VISIBLE);
			} else {
				isShow = !isShow;
				mLayout_songlist.setVisibility(View.GONE);
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		if (mListLyric != null) {
			mListLyric.clear();
			mListLyric = null;
		}
		if (mlistSong != null) {
			mlistSong.clear();
			mlistSong = null;
		}
		super.onDestroy();
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
