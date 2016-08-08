package com.lesports.stadium.bean;

import java.io.Serializable;
import java.util.List;

/**
 * ***************************************************************
 * 
 * @Desc : 订单列表数据实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class OrderListBean implements Serializable{

	/**
	 *   {
            "amount": 560,
            "companies": "顺丰",
            "courier": 1,
            "createBy": 1,
            "createTime": 1458526592000,
            "freight": 5,
            "handler": {},
            "id": 88643,
            "orderAmount": 555,
            "orderDetailList": [
                {
                    "goods": {
                        "bigImg": "/upload/img/image005.png",
                        "category": {
                            "classicName": "餐饮美食"
                        },
                        "classicId": 4959,
                        "createTime": 1457155033000,
                        "createby": 1570,
                        "description": "",
                        "freight": 5,
                        "gId": 94188,
                        "goodsName": "果汁",
                        "label": "演唱会",
                        "mediumImg": "/upload/img/image005.png",
                        "price": 5,
                        "priceUnit": 1,
                        "referprice": 5,
                        "seller": 1571,
                        "smallImg": "/upload/img/image005.png",
                        "status": 1,
                        "stock": 44
                    },
                    "goodsId": 94188,
                    "id": 20160606,
                    "price": 560,
                    "wareNumber": 2
                }
            ],
            "orderNumber": "2016032100001",
            "orderType": 0,
            "ordersType": 0,
            "payMent": 0,
            "payStatus": 0,
            "position": "五排19号",
            "remark": "顺丰",
            "status": 1
            telePhone
            payTime
        },
        
        "car_Brand":"2",
	"car_Type":"2",
	"car_Type_Id":"1",
	"city":"北京",
	"create_Time":1458057600000,
	"deleteFlag":0,
	"driver_Id":"43",
	"driver_Name":"胡巴",
	"driver_Phone":"13565498756",
	"end_Address":"的就是垃圾的",
	"end_Latitude":43431,
	"end_Lognitude":434,
	"end_Position":"几点开垃圾",
	"expect_Start_Time":1458057600000,
	"flight_Number":"1",
	"id":9,
	"insure_Company":"晶朝",
	"insure_Number":"3",
	"msg":"431431",
	"order_Id":"2016031600006",
	"passenger_Name":"阿凡达",
	"passenger_Number":1,
	"passenger_Phone":"13521830527",
	"payStatus":1,
	"regulatepan_Reson":"2",
	"start_Address":"麦克风是垃圾的速度就",
	"start_Latitude":4214,
	"start_Lognitude":32431,
	"start_Position":"hi多撒谎发多少",
	"time_Length":12,
	"total_Amount":44,
	"type":"1",
	"user_Id":1,
	"vehicle_Number":"3"
        
        
        
                {
            "amount": 59,
            "courier": 0,
            "createBy": 188514499,
            "createTime": 1462242040000,
            "freight": 0,
            "handler": {
                
            },
            "id": 890315,
            "orderAmount": 59,
            "orderDetailList": [
                {
                    "goods": {
                        "car_Type": "经济车型",
                        "city": "bj",
                        "deleteFlag": 0,
                        "end_Address": "五道口基地",
                        "end_Latitude": 40,
                        "end_Lognitude": 116,
                        "end_Position": "五道口",
                        "expect_Start_Time": 1462204800000,
                        "id": 321,
                        "order_Id": "2005789418",
                        "passenger_Name": "晶朝",
                        "passenger_Number": 1,
                        "passenger_Phone": "18210987923",
                        "payStatus": 8,
                        "start_Address": "晶朝科技12D",
                        "start_Latitude": 40,
                        "start_Lognitude": 116,
                        "start_Position": "晶朝科技",
                        "time_Length": 1,
                        "total_Amount": 59,
                        "type": "1",
                        "user_Id": 188514499
                    },
                    "goodsId": 321,
                    "goodsName": "2005789418",
                    "id": 20163708,
                    "price": 59,
                    "wareNumber": 1
                }
            ],
            "orderNumber": "20160503022040",
            "orderType": 1,
            "ordersType": 2,
            "payMent": 1,
            "payStatus": 4,
            "remark": "用车订单",
            "seller": {
                
            },
            "sellerId": 0,
            "status": 23,
            "telePhone": "18210987923"
        },
        
	*/
	

	
	
	private String companyname;
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	private String privilege;
	
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	private String telePhone;
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	private String payTime;
	private String amount;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCompanies() {
		return companies;
	}
	public void setCompanies(String companies) {
		this.companies = companies;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public List<OrderListBeanGoodsBean> getList() {
		return list;
	}
	public void setList(List<OrderListBeanGoodsBean> list) {
		this.list = list;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrdersType() {
		return ordersType;
	}
	public void setOrdersType(String ordersType) {
		this.ordersType = ordersType;
	}
	public String getPayMent() {
		return payMent;
	}
	public void setPayMent(String payMent) {
		this.payMent = payMent;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String companies;
	private String courier;
	private String createBy;
	private String createTime;
	private String freight;
	private String id;
	private String orderAmount;
	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	private List<OrderListBeanGoodsBean> list;
	private List<OrderListBeanZhongchouBean> list_zhong;
	private List<OrderListBeanCar> list_car;
	public List<OrderListBeanCar> getList_car() {
		return list_car;
	}
	public void setList_car(List<OrderListBeanCar> list_car) {
		this.list_car = list_car;
	}
	public List<OrderListBeanZhongchouBean> getList_zhong() {
		return list_zhong;
	}
	public void setList_zhong(List<OrderListBeanZhongchouBean> list_zhong) {
		this.list_zhong = list_zhong;
	}
	private String orderNumber;
	private String orderType;
	private String ordersType;
	private String payMent;
	private String payStatus;
	private String position;
	private String remark;
	private String status;
	private List<OrderListBeanTices> listtices;
	public List<OrderListBeanTices> getListtices() {
		return listtices;
	}
	public void setListtices(List<OrderListBeanTices> listtices) {
		this.listtices = listtices;
	}
	
	private String address;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCityAddress() {
		return cityAddress;
	}
	public void setCityAddress(String cityAddress) {
		this.cityAddress = cityAddress;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String cityAddress;
	private String mobilePhone;
	private String name;
	
	
	
}
