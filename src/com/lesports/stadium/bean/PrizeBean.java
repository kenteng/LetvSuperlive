package com.lesports.stadium.bean;
/**
 * 
* @ClassName: PrizeBean 
*
* @Description: 奖品实体类
*
* @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
*
* @author wangxinnian
* 
* @date 2016-8-1 下午7:44:51 
* 
*
 */
public class PrizeBean {
	// 奖品数量
	public String amount;
	// 奖品id
	public String prizeId;
	// 奖品状态 已领取
	public String status;
	// 奖品图片路径
	public String image;
	// 奖品名称
	public String name;
	// 奖品价格
	public String price;
	// 奖品备注
	public String remark;
	// 奖品开始使用时间
	public String startTime;
	// 奖品截止使用时间
	public String endTime;
	//1为实物 2 为 虚拟 3是乐豆奖品
	public String type;
	//奖品收货地址id
	public String deliveryId;
	//奖品与 人关联时生成的id ，在领取奖品时用这个id
	public String id;
	//虚拟奖品 在去使用 时根据这个id去查询 是到用车还是购票或 餐饮
	public String contentId;
	//如果是乐豆 奖品 该字段是乐豆奖励数
	public String integral;
	

}
