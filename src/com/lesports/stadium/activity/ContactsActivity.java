package com.lesports.stadium.activity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.PhoneAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.PhoneInfo;
import com.lesports.stadium.ui.IndexSideBar;
import com.lesports.stadium.ui.IndexSideBar.OnLetterChangeListener;
import com.lesports.stadium.utils.PinyinUtil;
import com.lesports.stadium.utils.Utils;

/**
 * ***************************************************************
 * 
 * @ClassName: ContactsActivity
 * 
 * @Desc : 所有联系人
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-17 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *  ***************************************************************
 */
public class ContactsActivity extends BaseActivity implements OnClickListener {
	/**
	 * 上下文
	 */
	private static Context mContext;
	
	/**
     * 右侧展示内容
     */
	private static final String[] LETTERS = new String[] { "#", "@", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	/** 
	 * 获取库Phone表字段 
	 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID,
			Phone.SORT_KEY_PRIMARY };
	/**
	 *  联系人显示名称
	**/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** 
	 * 电话号码
	**/
	private static final int PHONES_NUMBER_INDEX = 1;
	
	/**
	 * 手机号正则表达式
	 */
	private String res = "^((\\+861)|(1))[3578]((\\d)|(\\d\\s))\\d{4}((\\d{4})|(\\s\\d{4}))*$";
	
	/**
	 * 手机联系人集合
	 */
	private ArrayList<PhoneInfo> list = new ArrayList<PhoneInfo>();

	/**
	 * 适配器
	 */
	private PhoneAdapter adapter;
	/**
	 * 返回
	 */
	private ImageView ivBack;
	/**
	 * 标题
	 */
	private TextView tvTitle;
	/**
	 * listView
	 */
	private ListView mListView;

	/**
	 * 右侧展示View
	 */
	@SuppressLint("HandlerLeak")
	private IndexSideBar isb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		mContext = ContactsActivity.this;
		initView();
		initData();
		initListener();
	}
	
	/**
	 * 初始化View
	 */
	private void initView() {
		ivBack = (ImageView)findViewById(R.id.title_left_iv);
		tvTitle=(TextView)findViewById(R.id.tv_title);
		mListView = (ListView) findViewById(R.id.contact_list);
		isb = (IndexSideBar) findViewById(R.id.isb_sharefriend);
		tvTitle.setText(this.getString(R.string.all_contacts));
		isb.setLetters(LETTERS);
	}
	
	  /**
     * 初始化数据
     */
	private void initData() {
		getPhoneContacts();
		if (list.size() > 0) {
			Collections.sort(list);
			adapter = new PhoneAdapter(ContactsActivity.this, list,1);
			mListView.setAdapter(adapter);
		}
	}



	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		//返回
		ivBack.setOnClickListener(this);
	    //首字母检索
		isb.setOnLetterChangeListener(new OnLetterChangeListener() {
			@Override
			public void onLetterChange(String letter) {
				for (int i = 0; i < list.size(); i++) {
					PhoneInfo phoneInfo = list.get(i);
					String s = String.valueOf(phoneInfo.getPinyin().charAt(0));
					if (TextUtils.equals(s, letter)) {
						mListView.setSelection(i);
						break;
					}
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhoneInfo phontInfo=list.get(position);
				Intent intent=new Intent();
				intent.putExtra("phoneInfo", phontInfo);
				setResult(0, intent);
				finish();
			}
		});
		
	}
 

	/**
	 * 得到手机通讯录联系人信息
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	private void getPhoneContacts() {
		String phoneBrand = android.os.Build.BRAND;
		ContentResolver resolver = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = null;
		if ("meizu".equalsIgnoreCase(phoneBrand)
				|| "lge".equalsIgnoreCase(phoneBrand)) {
			phoneCursor = resolver.query(Phone.CONTENT_URI, new String[] {
					Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,
					Phone.CONTACT_ID, Phone.SORT_KEY_PRIMARY,
					"phonebook_label_alt" }, null, null, Phone.DISPLAY_NAME);
		} else {
			phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION,
					null, null, Phone.DISPLAY_NAME);
		}
		String str_ = null;
		String requestStr = "";
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				PhoneInfo phone_info = new PhoneInfo();
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (Utils.isNullOrEmpty(phoneNumber)) {
					continue;
				} else {
					phoneNumber = phoneNumber.replaceAll("-|\\s", "");
					if (!phoneNumber.matches(res))
						continue;
					
					try {
						phone_info.setPhone(phoneNumber);
						phone_info.setPhoneName(contactName);
						phone_info.setPinyin(PinyinUtil.getPinyin(contactName));
					} catch (Exception e) {
						e.printStackTrace();
					}
					list.add(phone_info);
				}
			}
		}
		if (phoneCursor != null && str_ != null) {
		}

		phoneCursor.close();

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			finish();
		break;
		default:
			break;
		}
	}
}
