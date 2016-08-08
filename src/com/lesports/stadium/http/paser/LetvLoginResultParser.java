package com.lesports.stadium.http.paser;

import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.letv.component.utils.DebugLog;

public class LetvLoginResultParser {
	private static LoginUserInfo user;

	public static LoginResultData parseReponseToLoginUserInfo(String data) {
		DebugLog.log("AuthListener", "response=" + data);
		String picture = null;
		JSONObject beanJson = null;
		JSONObject o = null;
		if (data == null) {
			return null;
		}
		try {
			o = new JSONObject(data);
			if (o.getString("status") != null) {
				LoginResultData res = new LoginResultData();
				res.setErrorCode(o.getString("errorCode"));
				res.setStatus(o.getString("status"));
				res.setMessage(o.getString("message"));
				if (o.has("tv_token")) {
					res.setTv_token(o.getString("tv_token"));
				}
				if (o.has("sso_tk")) {
					res.setSso_tk(o.getString("sso_tk"));
				}

				if (o.has("bean") && null != o.getString("bean")
						&& !"".equals(o.getString("bean"))
						&& !"[]".equals(o.getString("bean"))) {
					beanJson = o.getJSONObject("bean");
					user = new LoginUserInfo();
					if (beanJson.has("uid")) {
						user.uid = beanJson.getString("uid");
					}
					if (beanJson.has("username")) {
						user.username = beanJson.getString("username");
					}
					if (beanJson.has("status")) {
						user.status = beanJson.getInt("status");
					}
					if (beanJson.has("nickname")) {
						user.nickname = beanJson.getString("nickname");
					}
					if (beanJson.has("qq")) {
						user.qq = beanJson.getString("qq");
					}
					if (beanJson.has("registIp")) {
						user.registIp = beanJson.getString("registIp");
					}
					if (beanJson.has("registTime")) {
						user.registTime = beanJson.getString("registTime");
					}
					if (beanJson.has("birthday")) {
						user.birthday = beanJson.getString("birthday");
					}
					if (beanJson.has("email")) {
						user.email = beanJson.getString("email");
					}
					if (beanJson.has("province")) {
						user.province = beanJson.getString("province");
					}
					if (beanJson.has("city")) {
						user.city = beanJson.getString("city");
					}
					if (beanJson.has("address")) {
						user.address = beanJson.getString("address");
					}
					if (beanJson.has("mobile")) {
						user.mobile = beanJson.getString("mobile");
					}
					if (beanJson.has("gender")) {
						user.gender = beanJson.getString("gender");
					}
					if (beanJson.has("user_connect")) {
						JSONObject user_connect = beanJson
								.getJSONObject("user_connect");
						if (user_connect.has("avatar")) {
							String pic = user_connect.getString("avatar");
							user.picArray = new String[] { pic };
						}
					} else {
						if (beanJson.has("picture")) {
							picture = beanJson.getString("picture");
							String[] picArray = picture.split(",");
							if (picArray.length > 0) {
								user.picArray = picArray;
							}
						}
					}

					if (user.avatar == null || user.avatar.equals("")) {
						if (user.picArray != null && user.picArray.length > 0) {
							if (user.picArray.length > 1) {
								user.avatar = user.picArray[1];
							} else {
								user.avatar = user.picArray[0];
							}
							user.avatar = user.picArray[0];
						}
					}
					res.setBean(user);
				} else {
					return null;
				}
				return res;
			} else {
				return null;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
