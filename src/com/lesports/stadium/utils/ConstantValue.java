package com.lesports.stadium.utils;


/**
 * 
 * @ClassName:  ConstantValue   
 * @Description:定义一些全局常量 比如网络地址端口   
 * @author: 王新年 
 * @date:   2015-12-28 下午5:55:30   
 *
 */
public interface ConstantValue {
	/*
	 * http://interface.jingzhaokeji.com:8070/letv_mgr/musicale/getMusicList.do
	 * 访问网络的基址 http://interface.jingzhaokeji.com:8070/letv_mgr/
	 */
//	String BASE_URL = "http://interface1.jingzhaokeji.com:8070/";
//	String BASE_URL = "http://202.85.218.52:8070/";
//	String BASE_URL = "http://10.11.145.81:8080/";  //乐视内侧环境
	String BASE_URL = "http://gene.lesports.com/api/smartgym/";
	/**
	 * 图片访问基地址     7牛云服务器地址    预上线环境
	 */
//	String BASE_IMAGE_URL = "http://o7mjouz4i.bkt.clouddn.com/";
	/**
	 * 图片访问基地址     7牛云服务器地址    生产环境
	 */
	String BASE_IMAGE_URL = "http://o963rsgf9.bkt.clouddn.com/";
	
	/**
	 *通知永乐 服务器   购票成功的地址
	 */
	String BY_TICKOUS_SUCCESS = "http://api.228.cn//appLM/payNotify";
	/**
	 * 去支付 之前先 查询 购票订单是否有效  无效则不支付
	 */
	String IS_EFECTIVE = "http://api.228.cn//appLM/queryOrdersStayus";
	/**
	 * 易到用车 基质  预上线环境
	 */
//	String YIDAO_BASE_URL = "http://sandbox.open.yongche.org/";
	/**
	 * 易到用车 基质 生产环境
	 */
	String YIDAO_BASE_URL = "https://yop.yongche.com/";
	/**
	 * 校验token是否有效。如果无效需要重新登录
	 */
	String SSO_TOKEN = "https://sso.letv.com/sdk/checkTk";
	/**
	 * 加载七牛服务器图片 时 结尾数据
	 */
	String IMAGE_END = "?attname=image";
	/**
	 * 未开始界面分享
	 */
	String SHARED_NOSTART= BASE_URL + "letv_mgr/pages/jsp/sharePage/activity.jsp?id=";
	/**
	 * 视频播放界面分享
	 */
	String SHARED_VIDEOPLSY = BASE_URL + "letv_mgr/pages/jsp/sharePage/video.jsp?id=";
	/**
	 * 众筹详情界面分享
	 */
	String SHARED_ZHONGCHOU = BASE_URL + "letv_mgr/pages/jsp/sharePage/crowdfundDetail.html?crowdfundId=";
	
	/**
	 * 众筹图文详情页分享
	 */
	String SHARED_ZHONGPICTURE = BASE_URL + "letv_mgr/pages/jsp/sharePage/crowdfundinfo.jsp?crowdfundId=";
	/**
	 * 商品分享
	 */
	String SHARED_SHANGPING = BASE_URL + "letv_mgr/pages/jsp/sharePage/goodsdetail.jsp?goodsId=";
	/**
	 * 奖品图文详情分享页
	 */
	String SHARED_JIANGPINGPICTURE = BASE_URL + "letv_mgr/pages/jsp/sharePage/prizeDetail.jsp?prizeId=";
	
	/**
	 * 登录
	 */
	String LOGIN = BASE_URL+"/login";
	/**
	 * 商城
	 */
	String SHOPP = BASE_URL+"letv_mgr/goods/getGoodsList.do?";
	/**
	 * 商品详情
	 */
	String SHOPP_DETAIL=BASE_URL+"letv_mgr/goods/getGoodsDetail.do?";
	/**
	 * 获取收获地址
	 */
	String RECEIPT_ADDRESS=BASE_URL+"letv_mgr/delivery/getDeliveries.do";
	/**
	 * 获取活动界面首页数据
	 */
	String ACTIVITY_DATA=BASE_URL+"letv_mgr/activity/getActivityWithPagination.do";
	/**
	 * 获取歌曲列表的接口
	 */
	String LYRICS_LIST_DATA=BASE_URL+"letv_mgr/musicale/getMusicList.do";
	/**
	 * 歌曲内容获取接口
	 */
	String LYRIC_CONTENT=BASE_URL+"letv_mgr/musicale/getLyricsByMusicaleId.do";
	/**
	 * 获取周边商品的接口
	 */
	String SROUNDING_LIST_DATA=BASE_URL+"letv_mgr/goods/getSurrounding.do";
	/**
	 * 活动详情界面数据接口
	 */
	String ACTIVITY_DETAIL=BASE_URL+"letv_mgr/activity/getActivityById.do";
	/**
	 * 未开始和进行中的活动详情
	 */
	String ACTIVITY_DETAIL_NOACTION=BASE_URL+"letv_mgr/activity/selectGoingActivityById.do";
	/**
	 * 商品详情数据接口
	 */
	String GOODS_DETAIL=BASE_URL+"letv_mgr/goods/getGoodsDetail.do";
/**
	 * 获取所有讨论
	 */
	String ALLDISSCUSS=BASE_URL+"letv_mgr/discuss/selectDiscuss.do";
	/**
	 * 发送讨论内容
	 */
	String SEND_DISCUSS=BASE_URL+"letv_mgr/discuss/addDiscuss.do";
	/**
	 * 获取用车地址信息
	 */
	String RIDECAR_ADDRESS=BASE_URL+"letv_mgr/carAddress/queryCarAddress.do";
	/**
	 * 生成支付订单
	 */
	String YIDAO_PAY_ORDER=BASE_URL+"letv_mgr/payMent/createCarOrder.do";
	
	/**
	 * 添加用户常用地址
	 */
	String SEND_COMMON_ADDRESS=BASE_URL+"letv_mgr/carAddress/addCarAddress.do";
	/**
	 * 获取用车估价
	 */
	String GET_CAR_PRICE=YIDAO_BASE_URL+"v2/cost/estimated";
	/**
	 * 添加收货地址
	 */
	String ADD_SHIIPPING_ADDRESS=BASE_URL+"letv_mgr/delivery/addDelivery.do";
	/**
	 * 修改收货地址
	 */
	String CHANGE_SHIIPPING_ADDRESS=BASE_URL+"letv_mgr/delivery/updateDelivery.do";
	/**
	 * 获取默认收货地址
	 */
	String GET_DEFAULT_ADDRESS=BASE_URL+"letv_mgr/delivery/getDefaultDelivery.do";
	/**
	 * 获取所有收货地址
	 */
	String GET_ALL_ADDRESS=BASE_URL+"letv_mgr/delivery/getDeliveries.do";
	/**
	 * 删除收获地址
	 */
	String DELETE_ADDRESS=BASE_URL+"letv_mgr/delivery/deleteDeliveries.do";
	/**
	 * 获取单一订单信息  http://sandbox.open.yongche.org/
	 */
	String GET_ONLY_ORDER= YIDAO_BASE_URL+"v2/order";
	/**
	 * 获取司机详细信息
	 */
	String GET_DRIVER_DETEAL=YIDAO_BASE_URL+"v2/driver/info";
	/**
	 * 获取订单费用
	 */
	String GET_ORDER_FEE=YIDAO_BASE_URL+"v2/order/calculate/";
	/**
	 * 支付成功易道回调
	 */
	String PAY_SUCCESS_YIDAO=YIDAO_BASE_URL+"v2/payCash";
	/**
	 * 取消订单
	 */
	String CANCLE_ORDER=YIDAO_BASE_URL+"v2/order/";
	/**
	 * 删除订单
	 */
	String DELETE_ORDER=BASE_URL+"letv_mgr/carOrder/deleteCarOrder.do";
	/**
	 * 获取司机位置
	 */
	String GET_DRIVER_LOCATION=YIDAO_BASE_URL + "v2/driver/location";
	/**
	 * 获取司机列表，暂无用
	 */
	String GET_DRIVER_LIST=YIDAO_BASE_URL+"v2/driver/getSelectDriver";
	/**
	 * 订单
	 */
	String GET_CAR_ORDER=YIDAO_BASE_URL+"v2/order";
	/**
	 * 创建用车订单
	 */
	String CREAT_CAR_ORDER=BASE_URL+"letv_mgr/carOrder/addCarOrder.do";
	/**
	 * 易道用车TOKEN  预上线
	 */
//	String CAR_TOKEN="psTBKnSYPotjmpj0rRillUPhDdJ2S7CQEDRlyiKJ";
	/**
	 * 易道用车TOKEN  生产环境
	 */
	String CAR_TOKEN="nkQGZqjP6k4vEaACf96v5gmYeQoz69aos6LEgtwQ";
	/**
	 * 服务商品列表
	 */
	String SERVICE_GOODSLIST=BASE_URL+"letv_mgr/goods/getGoodsBySeller.do";
	String SERVICE_OODLIST_DATA=BASE_URL+"letv_mgr/goods/getGoodsList.do";
	String NEW_SERVICE_GOODS_LIST_DATA=BASE_URL+"letv_mgr/goods/getGoodsList.do";
	/**
	 * 服务餐饮商家列表获取
	 */
	String SERVICE_FOODDIANPU_LIST=BASE_URL+"letv_mgr/users/getUsersByUserType.do";
	/**
	 * 获取商家所有的商品
	 */
	String SERVICE_FOODDIANPU_GOODS_LIST=BASE_URL+"letv_mgr/goods/getGoodsBySeller.do";
	/**
	 * 预约报名参加活动
	 */
	String ADD_ACTIVITY=BASE_URL+"letv_mgr/bespeak/addBespeak.do";
	/**
	 *上传文件
	 */
	String UPLOAD_INTERFACE=BASE_URL+"letv_mgr/UploadServlet";
	/**
	 * 集锦数据获取
	 */
	String HIGHT_LEIGHT=BASE_URL+"letv_mgr/campaign/highlights.do";
	/**
	 * 众筹获取数据列表
	 */
	String ZHONGCHOU_LIST=BASE_URL+"letv_mgr/crowdfund/getCrowdfundList.do";
	/**
	 * 众筹详情获取
	 */
	String ZHONGCHOU_DETAILS=BASE_URL+"letv_mgr/crowdfund/getCrowdfundDetail.do";
	/**
	 * 获取众筹评论数量
	 */
	String ZHONGCHOU_DISSCUSS_NUM=BASE_URL+"letv_mgr/discuss/countDiscuss.do";
	/**
	 * 获取用户预约列表
	 */
	String YONGHUYUYUE_LIST=BASE_URL+"letv_mgr/bespeak/queryBespeak.do";
	/**
	 * 取消预约
	 */
	String QUXIAOYUYUE_ACTIVITY=BASE_URL+"letv_mgr/bespeak/deleteBespeak.do";
	/**
	 *查询该活动是否被用户预约
	 */
	String ACIVITY_IS_YUYUE=BASE_URL+"letv_mgr/bespeak/selectBespeakByUserIdAndActivityId.do";
	/**
	 * 生成订单
	 */
	String MAKE_A_ORDER_NUM=BASE_URL+"letv_mgr/order/addOrder.do";
	/**
	 * 获取支付参数
	 */
	String GET_PAY_DATA=BASE_URL+"letv_mgr/payMent/createPayOrder.do";
	/**
	 * 用户积分详情查询
	 */
	String USER_JIFEN=BASE_URL+"letv_mgr/integral/getIntegralDetail.do";
	/**
	 * 消息标记为已读
	 */
	String MESSAGE_HAS_READ=BASE_URL+"letv_mgr/message/readMessage.do";	
	/**
	 * 我的全部消息
	 */
	String MESSAGE_ALL=BASE_URL+"letv_mgr/message/getAllMessage.do";	
	/**
	 * 我的消息_删除
	 */
	String MESSAGE_DELETE=BASE_URL+"letv_mgr/message/deleteMessage.do";	
	/**
	 * 消息标记为未读
	 */
	String MESSAGE_TO_UNREAD=BASE_URL+"letv_mgr/message/unReadMessage.do";	
	/**
	 * 用户积分详情查询每月汇总
	 */
	String USER_JIFEN_MONTH=BASE_URL+"letv_mgr/integral/totalIntegral.do";
	/**
	 * 订单列表获取
	 */
	String ORDER_LIST=BASE_URL+"letv_mgr/order/getUserOrders.do";
	/**
	 * 积分充值比例
	 */
	String GETINTEGRAL=BASE_URL+"letv_mgr/integralRecharge/getIntegralRechargeList.do";
	
	/**
	 * 添加积分充值订单
	 */
	String ADD_JIFEN_ORDER=BASE_URL+"letv_mgr/integralOrder/addIntegralOrder.do";
	/**
	 * 生成积分充值支付订单
	 */
	String ADD_JIFEN_ORDER_NEXT=BASE_URL+"letv_mgr/payMent/createIntegralPayOrder.do";
	/**
	 * 积分规则
	 */
	String INTEGRALRULE=BASE_URL+"letv_mgr/integralRule/getInUsingRule.do";
	/**
	 * 摇一摇获取奖品
	 */
	String SHAKE_PRICE=BASE_URL+"letv_mgr/activity/shakeShake.do";
	/**
	 * 摇一摇次数
	 */
	String SHAKE_NUMBER=BASE_URL+"letv_mgr/lotteryRule/queryLotteryRule.do";
	
	/**
	 * 获取广告列表
	 */
	String GUANGGAO_LIST=BASE_URL+"letv_mgr/advertisement/getAdvertisements.do";

	/**
	 * 摇一摇规则
	 */
	String SHAKE_RULE=BASE_URL+"letv_mgr/lotteryRule/queryLotteryRule.do";
	/**
	 * 获取广告列表
	 */
	String FEEDBACK=BASE_URL+"letv_mgr/feedBack/addFeedBack.do";
	/**
	 * 获取广告列表
	 */
	String SELFMESSAGE=BASE_URL+"letv_mgr/message/selectMyMessage.do";
	/**";
	
	
	 * 项目名称
	 */
	String APP_NAME = "Superlive";
	/**
	 * 乐视云 uuid
	 */
	String LS_UUID = "6iyjcurece";
	/**
	 * 乐视云userkey
	 */
	String LS_USKY = "56311cf7c41551a3a928ee3765a59a65";
	/**
	 * 获取优惠列表
	 */
	String YOUHUIQUAN_LIST=BASE_URL+"letv_mgr/coupon/getCouponListByUserId.do";
	/**
	 * 获取商品规格参数
	 */
	String GOODS_GUIGE_DATA=BASE_URL+"letv_mgr/goodsSpecifications/getGoodsSpecificationsListByGoodsId.do";
	/**
	 * 申请退款
	 */
	String SHENGQING_TUIKUAN=BASE_URL+"letv_mgr/order/applyRefund.do";
	/**
	 * 取消订单
	 */
	String QUXIAO_ORDER=BASE_URL+"letv_mgr/order/cansoleOrder.do";
	/**
	 * 奖品列表获取
	 */
	String PRICE_LIST=BASE_URL+"letv_mgr/prize/queryMyPrize.do";
	/**
	 * 判断退款时间是否在十分钟之内
	 */
	String TUIKUAN_TIME_PANDUAN=BASE_URL+"letv_mgr/order/getCancelTime.do";
	/**
	 * 送礼物
	 */
	String SEND_GIF=BASE_URL+"letv_mgr/gift/giveGift.do";
	/**
	 * 查询用户积分
	 */
	String GET_JIFEN=BASE_URL+"letv_mgr/integral/getIntegral.do";
	/**
	 * 查询用户消息数量
	 */
	String GET_MESSAGE_NUMBER=BASE_URL+"letv_mgr/message/selectMyMessageCount.do";
	/**
	 * 查询每个礼品锁扣积分
	 */
	String GIFT_JIFEN=BASE_URL+"letv_mgr/gift/giveAllGiftIntegral.do";
	/**
	 * 将乐视信息同步到后台
	 */
	String LS_MESSAGE=BASE_URL+"letv_mgr/letvuser/addLetvUser.do";
	/**
	 * 获取后台个人消息
	 */
	String PERSION_MESSAGE=BASE_URL+"letv_mgr/letvuser/queryLetvUser.do";
	/**
	 * 保存个人消息
	 */
	String SAVE_PERSION_MESSAGE=BASE_URL+"letv_mgr/letvuser/updateLetvUser.do";
	/**
	 * 我的奖品详情
	 */
	String MY_PRIZE_DETAIL=BASE_URL+"letv_mgr/prize/queryPrizeDetail.do";
	/**
	 * 领取奖品
	 */
	String RECEIVER_PRIZE=BASE_URL+"letv_mgr/prize/configDeliveryIdForMyPrize.do";
	/**
	 * 检查商品是否库存不足
	 */
	String CHECK_GOODS_KUCUN=BASE_URL+"letv_mgr/order/checkStock.do";
	/**
	 * 根据奖品id 去查询要跳转到哪个界面
	 */
	String CHECK_PRIZE_ID=BASE_URL+"letv_mgr/coupon/getCouponById.do";
	/**
	 * 根据奖品id 查询收货地址
	 */
	String GET_ADRESS_ID=BASE_URL+"letv_mgr/delivery/getDeliveryById.do";
	/**
	 * 购票生成支付订单
	 */
	String TICKEORDER=BASE_URL+"letv_mgr/payMent/createTicketPaymentOrder.do";
	/**
	 * 根据订单号获取订单
	 */
	String ORDER_MESSAGE_NUMBER=BASE_URL+"letv_mgr/order/getOrderByOrderNumber.do";
	/**
	 * 提醒发货接口
	 */
	String TIXING_SEND_GOODS=BASE_URL+"letv_mgr/order/sendOrderNotice.do";
	/**
	 * 意见反馈增加
	 */
	String SUGGESTION_ADD = BASE_URL+"letv_mgr/feedBack/addFeedBack.do";
	/**
	 * 文件上传到七牛上 获取token
	 */
	String GET_TOKEN = BASE_URL+"letv_mgr/qiniu/getToken.do";
	
	String JIFENG_GUIZE = BASE_URL+"letv_mgr/activity/shakeShakeCount.do";
	/**
	 * 提示文案
	 */
	String TISHI_WENAN = BASE_URL+"letv_mgr/promptDocument/querypromptDocument.do";
	/**
	 * 支持球队
	 */
	String SUPPORTTEAM = BASE_URL+"letv_mgr/teamSupport/supportTeam.do";
	/**
	 * 根据活动id 获取球队信息
	 */
	String GET_TEM_MESSAGE = BASE_URL+"letv_mgr/campaign/getTeamInfoByActivity.do";
	/**
	 * 讨论送礼 获取礼物信息
	 */
	String GET_GIFT_MESSAGE = BASE_URL+"letv_mgr/gifts/getGiftsByTeamId.do";
	
	/**
	 * 获取支持的城市列表
	 */
	String SUPPORT_CITY = BASE_URL + "letv_mgr/region/getSupportedCities.do";
	/**
	 * 上传我要尖叫数据
	 */
	String ADD_SCREAM = BASE_URL + "letv_mgr/scream/addScream.do";
	/**
	 * 我要尖叫界面获取 尖叫数据
	 */
	String GET_SCREAM = BASE_URL + "letv_mgr/scream/getScreamData.do";
	/**
	 * 获取尖叫名次
	 */
	String GET_SCREAM_SEQUENCE = BASE_URL + "/letv_mgr/scream/getUserSequenceForThirdService";
	
	/**
	 * 获取场馆列表
	 */
	String GET_VENUE = BASE_URL + "/letv_mgr/venue/getVenueList.do";
	/**
	 * 获取能量总和
	 */
	String GET_SUM_ENERGY = BASE_URL + "/letv_mgr/energy/getSumEnergy.do";
	/**
	 * 获取能量排行
	 */
	String GET_ENERGY_LIST = BASE_URL + "/letv_mgr/energy/getEnergyTopTen.do";
	/**
	 * 获取聊天记录
	 */
	String GET_HISTORY = "http://api.my.letv.com/chat/history";
	/**
	 * 发送聊天
	 */
	String SEND_MESSAGE = "http://api.my.letv.com/chat/message";
	/**
	 * 活动详情获取
	 */
	String ACTIVITY_MESSAGE = BASE_URL + "/letv_mgr/activity/selectGoingActivityById.do";
	/**
	 * 根据活动id获取视频详情
	 */
	String VIDEO_MESSAGE = BASE_URL + "/letv_mgr/activity/selectResourceIdByAId.do";
	/**
	 * 根据活动id获取回顾视频和图片接口
	 */
	String GET_RETURN_VIDEO_IMAGE=BASE_URL+"/letv_mgr/campaign/getVideoAndImage.do";
	/**
	 * 添加能量值
	 */
	String ADD_POWER = BASE_URL+"/letv_mgr/energy/addEnergy.do";

}
