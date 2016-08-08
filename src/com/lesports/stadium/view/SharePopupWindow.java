package com.lesports.stadium.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.utils.AccessTokenKeeper;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.ImageUtils;
import com.lesports.stadium.utils.imageLoader.ImageFileCache;
import com.lesports.stadium.utils.imageLoader.ImageLoader;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class SharePopupWindow extends PopupWindow implements OnClickListener {

	private Context context;
	private PlatformActionListener platformActionListener;
	private ShareParams shareParams;
	private TextView top_sh;
	private IWXAPI api;
	private Bitmap shardBitmap;
	private String weburl;
	private ImageFileCache fileCache;
	private IWeiboShareAPI  mWeiboShareAPI = null;
	private RelativeLayout shar_rl;
	public  Tencent mTencent;
	public SharePopupWindow(Context cx) {
		this.context = cx;
		// 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,
       		 Constants.getSINA_APP_ID(context), false);
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, APP_KEY);
        
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        api = WXAPIFactory.createWXAPI(context,  Constants.APP_ID);
        fileCache = new ImageFileCache(context);
        mTencent = Tencent.createInstance("1105122629", context);
        isInstallMQ();
        
	}

	public PlatformActionListener getPlatformActionListener() {
		return platformActionListener;
	}

	public void setPlatformActionListener(
			PlatformActionListener platformActionListener) {
		this.platformActionListener = platformActionListener;
	}

	public void showShareWindow(boolean isCenter) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.shared_layout, null);
		shar_rl = (RelativeLayout) view
				.findViewById(R.id.shar_rl);
		if (isCenter) {
			shar_rl.setGravity(Gravity.CENTER);
		}
		shar_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharePopupWindow.this.dismiss();
			}
		});
		view.setAlpha(0.9f);
		top_sh = (TextView) view.findViewById(R.id.top_sh);
		view.findViewById(R.id.cancel_sh).setOnClickListener(this);
		view.findViewById(R.id.iv_share_wx).setOnClickListener(this);
		view.findViewById(R.id.iv_share_friend).setOnClickListener(this);
		view.findViewById(R.id.iv_share_wb).setOnClickListener(this);
		view.findViewById(R.id.iv_share_qq).setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		// this.setBackgroundDrawable(dw);

	}
	public void showShareWindow1(boolean isCenter) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.shared_layout_hengping, null);
		view.setAlpha(0.9f);
		top_sh = (TextView) view.findViewById(R.id.top_sh);
		view.findViewById(R.id.cancel_sh).setOnClickListener(this);
		view.findViewById(R.id.iv_share_wx).setOnClickListener(this);
		view.findViewById(R.id.iv_share_friend).setOnClickListener(this);
		view.findViewById(R.id.iv_share_wb).setOnClickListener(this);
		view.findViewById(R.id.iv_share_qq).setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		// this.setBackgroundDrawable(dw);
		
	}

	/**
	 * 设置显示的内容和是否显示左边图标
	 * 
	 * @param topContent
	 * @param isShowDrableLeft
	 */
	public void setTopShow(String topContent, boolean isShowDrableLeft) {
		top_sh.setText(topContent);
		if (isShowDrableLeft) {
			Resources res = context.getResources();
			Drawable img_off = res.getDrawable(R.drawable.re_check_true);
			img_off.setBounds(0, 0, img_off.getMinimumWidth(),
					img_off.getMinimumHeight());
			top_sh.setCompoundDrawables(img_off, null, null, null);
		}

	}

	/**
	 * 分享
	 * 
	 * @param position
	 */
	private void share(int position) {

		Platform plat = null;
		plat = ShareSDK.getPlatform(context, getPlatform(position));
		if (platformActionListener != null) {
			plat.setPlatformActionListener(platformActionListener);
		}
		plat.share(shareParams);
	}

	/**
	 * 初始化分享参数
	 * 
	 * @param shareModel
	 */
	public void initShareParams(ShareModel shareModel) {
		if (shareModel != null) {
			ShareParams sp = new ShareParams();
			sp.setShareType(Platform.SHARE_TEXT);
			sp.setShareType(Platform.SHARE_IMAGE);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setTitle(shareModel.getTitle());
			sp.setText(shareModel.getText());
			sp.setUrl(shareModel.getUrl());
			sp.setImageUrl(shareModel.getImageUrl());
			sp.setImagePath(shareModel.getImagPath());
			shareParams = sp;
		}
	}

	/**
	 * 获取平台
	 * 
	 * @param position
	 * @return
	 */
	private String getPlatform(int position) {
		String platform = "";
		switch (position) {
		case 0:
			platform = "Wechat";
			break;
		case 1:
			platform = "WechatMoments";
			break;
		case 2:
			platform = "SinaWeibo";
			break;
		}
		return platform;
	}

	private void qq() {
		ShareParams sp = new ShareParams();
		String title = shareParams.getTitle();
		if(TextUtils.isEmpty(title))
			sp.setTitle("　　");
		else
		   sp.setTitle(title);
		sp.setTitleUrl(shareParams.getImageUrl()); // 标题的超链接
		String text = shareParams.getText();
		if(TextUtils.isEmpty(text))
			sp.setText("　　");
		else 
			sp.setText(text);
		
		 final String imageurl = shareParams.getImageUrl();
		 String imagePath = shareParams.getImagePath();
		 if(!TextUtils.isEmpty(imagePath)){
			 qqImage(imagePath);
			 return;
		 }
		 
		if(!TextUtils.isEmpty(imageurl)){
			new Thread(){public void run() {
				qqImage(imageurl);
			};}.start();
			return;
		}
		 
//		sp.setComment("我对此分享内容的评论");
//		sp.setSite(shareParams.getTitle());
//		sp.setSiteUrl(shareParams.getImageUrl());
		if(!TextUtils.isEmpty(weburl)){
			sp.setText(weburl);
			sp.setShareType(Platform.SHARE_WEBPAGE);
		}
		Platform qq = ShareSDK.getPlatform(context, "QQ");
		qq.setPlatformActionListener(platformActionListener);
		qq.share(sp);
	}
	
	private void qqImage(String url){
		try{
			if(url.startsWith("http:")){
				File file = new ImageFileCache(context).getFile(url);
				File newFile = new ImageFileCache(context).getNewFile(url);
				ImageLoader.CopyStream(new FileInputStream(file), new FileOutputStream(newFile));
				url = newFile.getAbsolutePath();
			}
			final Bundle params = new Bundle();
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, url);
			params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
					QQShare.SHARE_TO_QQ_TYPE_IMAGE);
			doShareToQQ(params);
		}catch (Exception e){
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_sh:
			dismiss();
			break;
		case R.id.iv_share_wx:
			if(GlobalParams.ISINSTALL_WX){
				if(!TextUtils.isEmpty(weburl)){
					sharedWebViewWx(0,weburl);
				}else{
					final String imageurl = shareParams.getUrl();
					if(null!=shardBitmap){
						shareWx(0,shardBitmap); 
					}else if(!TextUtils.isEmpty(imageurl)){
							new Thread(){
								@Override
								public void run() {
									Bitmap bitmap = ImageUtils.getBitMap(fileCache, imageurl,true);
									shareWx(0,bitmap);
									super.run();
								}
							}.start();
					 }else{
						 share(0); 
					 }
				}
			}else{
				Toast.makeText(context, "您尚未安装微信客户端，无法分享给好友哦！", 0).show();
			}
			dismiss();
			break;
		case R.id.iv_share_friend:
			if(GlobalParams.ISINSTALL_WX)
			{ 
				final String imageurl = shareParams.getUrl();
				if(!TextUtils.isEmpty(weburl)){
					sharedWebViewWx(1,weburl);
				}else if(null!=shardBitmap){
					shareWx(1,shardBitmap);
				}else if(!TextUtils.isEmpty(imageurl)){
					new Thread(){
						@Override
						public void run() {
							Bitmap bitmap = ImageUtils.getBitMap(fileCache, imageurl,true);
							shareWx(1,bitmap);
							super.run();
						}
					}.start();
				}else{
					sharedTextWx(1,shareParams.getText());
				}
			}else{
				Toast.makeText(context, "您尚未安装微信客户端，无法分享到朋友圈哦！", 0).show();
			}
			dismiss();
			break;
		case R.id.iv_share_wb:
			if(GlobalParams.ISINSTALL_SN){
				new Thread(){
					public void run() {
						sinaWeibo();
					};
				}.start();
			}else{
				Toast.makeText(context, "您尚未安装新浪微博客户端，无法分享微博哦！", 0).show();
			}
			dismiss();
			break;
		case R.id.iv_share_qq:
			if(GlobalParams.ISINSTALL_QQ)
				qq1();
			else 
				Toast.makeText(context, "您尚未安装QQ客户端，无法分享给QQ好友", 0).show();
			dismiss();
			break;

		default:
			break;
		}
	}
	/***
	 * 微博分享
	 */
	private void sinaWeibo(){
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		String text = shareParams.getText();
		if(!TextUtils.isEmpty(text))
			weiboMessage.textObject = getTextObj();
		if(!TextUtils.isEmpty(weburl)){
			weiboMessage.mediaObject = getWebpageObj(weburl);
		}
		String imageurl = shareParams.getUrl();
		if(!TextUtils.isEmpty(imageurl))
			weiboMessage.imageObject = getImageObj(imageurl);
		 // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
//        AuthInfo authInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL, SCOPE);
        AuthInfo authInfo = new AuthInfo(context,
				Constants.getSINA_APP_ID(context), Constants.URL_REDIRECT,Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
//        if(GlobalParams.ISINSTALL_SN){
//        	 mWeiboShareAPI.sendRequest((Activity)context, request);
//        }else
        Log.i("wxn", "token..."+token);
        mWeiboShareAPI.sendRequest((Activity)context, request, authInfo, token, new WeiboAuthListener() {
            
            @Override
            public void onWeiboException( WeiboException arg0 ) {
            	 Log.i("wxn", "arg0..."+arg0.getMessage());
            }
            
            @Override
            public void onComplete( Bundle bundle ) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);
                Toast.makeText(context, "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
            }
            
            @Override
            public void onCancel() {
            }
        });
		
		
	}
	
    /**
     * 微博分享文字
     * 创建文本消息对象。
     * 
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        String text = shareParams.getText();
        if(text!=null&&text.length()>60){
        	text = text.substring(0, 60);
        }
        textObject.text =text;
        return textObject;
    }
    
    /**
     * 微博分享图片
     * 创建图片消息对象。
     * 
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(String imageurl) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo);
//        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imageurl);
//        bitmap =  ImageUtils.compressImage(bitmap, 32);
        Bitmap bitmap;
        if(null!=shardBitmap){
        	imageObject.setImageObject(shardBitmap);
        	bitmap = Bitmap.createScaledBitmap(shardBitmap, 150, 150, true);
        }else{
        	bitmap = ImageUtils.getBitMap(fileCache, imageurl,true);
        	imageObject.setImageObject(bitmap);
        	bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        }
        imageObject.setThumbImage(bitmap);
        return imageObject;
    }
//	
    /**
     * 微博
     * 创建多媒体（网页）消息对象。
     * 
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String wburl) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareParams.getTitle();
        String des = shareParams.getText();
        if(des!=null&&des.length()>60){
        	des = des.substring(0, 60);
        }
        mediaObject.description = des;
        String url = shareParams.getUrl();
		Bitmap bitmap = null;
		if(!TextUtils.isEmpty(url)){
			bitmap = ImageUtils.getBitMap(fileCache, url,true);
			if(bitmap!=null)
				bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
		}
		if(null==bitmap)
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shar_log);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = wburl;
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }
    
    /**
     * 微信分享图片
     * @param type type 1 分享到空间  其它分享到好友
     * @param bmp
     */
    private void shareWx(int type,Bitmap bmp ){
//    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
		WXImageObject imgObj = new WXImageObject(bmp);
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
//		bmp.recycle();
		msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		req.scene = type==1 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
    }
    
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	public void setSharedBitmap(Bitmap shardBitmap){
		this.shardBitmap = shardBitmap;
	}
	
	
	/**
	 * 分享微信 文字
	 * @param type type = 1 发送到朋友圈  ，其它 是分享到微信好友
	 * @param text 分享的文字
	 */
	private void sharedTextWx(int type,String text){
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		// msg.title = "Will be ignored";
		msg.description = text;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = type==1 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		
		// 调用api接口发送数据到微信
		api.sendReq(req);
	}
	
	/**
	 *  微信 分享web界面
	 * @param type  type == 1 微信朋友圈 ，其它 是分享到微信好友
	 * @param wburl 地址
	 */
	private void sharedWebViewWx(int type ,String wburl){
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = wburl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = shareParams.getTitle();
		String description = shareParams.getText();
		if(!TextUtils.isEmpty(description)&&description.length()>120){
			description = description.substring(0, 120);
		}
		msg.description =  description;
//		msg.title = "WebPage Title";
//		msg.description =  "WebPage Description WebPage Description";
		String url = shareParams.getUrl();
		Bitmap thumb = null;
		if(!TextUtils.isEmpty(url)){
			thumb = ImageUtils.getBitMap(fileCache, url,false);
			
		}
		if(null== thumb)
			thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.shar_log);
		msg.thumbData = bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = type == 1 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
	
	
	
	public void setWebUrl(String weburl){
//		this.weburl = "https://www.baidu.com/";
		this.weburl = weburl;
	}
	
	/**
	 * 判断是否安装微信 和ＱＱ
	 */
	private void isInstallMQ(){
		List<ResolveInfo> sharApps = CommonUtils.getShareApps(context);
		if(sharApps!=null){
			GlobalParams.ISINSTALL_WX = false;
			GlobalParams.ISINSTALL_QQ = false;
			GlobalParams.ISINSTALL_SN = false;
			for(ResolveInfo apps:sharApps){
				if("com.tencent.mm".equals(apps.activityInfo.packageName)){
					//已安装微信
					GlobalParams.ISINSTALL_WX = true;
				}else if("com.tencent.mobileqq".equals(apps.activityInfo.packageName)){
					//已安装QQ
					GlobalParams.ISINSTALL_QQ = true;
				}else if("com.sina.weibo".equals(apps.activityInfo.packageName)){
					//已安装新浪
					GlobalParams.ISINSTALL_SN = true;
				}
			}
		}
	}
	
	private int mExtarFlag = 0x00;
	private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
	private void qq1(){
		final Bundle params = new Bundle();
		String title = shareParams.getTitle();
		if(TextUtils.isEmpty(title))
			title = "   ";
		String summary = shareParams.getText();
		if(TextUtils.isEmpty(summary))
			summary = "   ";
		
		 final String imageurl = shareParams.getImageUrl();
		 String imagePath = shareParams.getImagePath();
		 if(!TextUtils.isEmpty(imagePath)){
			 qqImage(imagePath);
			 return;
		 }
		 
		if(!TextUtils.isEmpty(imageurl)){
			new Thread(){public void run() {
				qqImage(imageurl);
			};}.start();
			return;
		}
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, weburl);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareParams.getImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getResources().getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
        doShareToQQ(params);
	}
	
	private void doShareToQQ(final Bundle params){
		 new Handler(Looper.getMainLooper()).post(new Runnable() {
	            @Override
	            public void run() {
	                if (null != mTencent) {
	                    mTencent.shareToQQ((Activity) context, params, new IUiListener(){

							@Override
							public void onCancel() {
							}

							@Override
							public void onComplete(Object arg0) {
							}

							@Override
							public void onError(UiError arg0) {
							}}); 
	                }
	            }
	        });
	}
	
	
	
}
