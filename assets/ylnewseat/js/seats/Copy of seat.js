/**
 * 在线选座核心实现 V2.0 for APP & Android
 * Created by wangzhihua on 2015-1-9.
 */
var Seat = (function(globle, undefined){
	//alert(navigator.userAgent);
    var _document   = globle.document,
        rootURL = "../",
        canvas,	 //画布对象
        context, //画布2d
        canvasX = 0, //初始坐标x
        canvasY = 0, //初始坐标y
        start1X,
        start1X,
        midX,
        midY,
        midXY = [0,0],
        startLen,  //2触点之间的距离
        scale = 1.5,  //缩放倍率
        canvasMid = true,  //画布初始化居中
        mouse1={
            x: 0,
            y:0
        },  //焦点函数鼠标
        mouse2,  //焦点函数鼠标
        isClick = true,  //判断是否点击
        move = false,	//是否拖动
        selArea = {},  //选中的区域
        timeout,  //防止多次画图
        seatImgsCache = [],  //存放缓存图片
        imgIsLoaded = false,  //判断图片是否加入缓存
        singleClick = true,  //是否单手指
        checkSeats = [],	 //选中座位
        pricesSeatMap = [],	 //座位Map
        drawNum = 1,  //区域还是座位步骤
        canvasSeatWid,  //座位信息整体宽度
        canvasSeatHeg,  //座位信息整体高度
        seatArr = [],
        seatnum = 6,  //正常票限购数量
        tpseatnum = 12,  //正常套票限购数量
        tppricesMap,  //套票集合规则
        zytppricesMap,  //自由套票规则
        csLens = [1000],  //存放zytp自由套票序号
        rects = [],  //存放座位坐标
        chromeMove = 0,   //chrome的move移动次数
        fangda = false,
        seatO = {		//座位属性对象
    		w	  : 10, //座位宽
    		h	  : 10  //座位高
    	},
        _cache      = {
            images0  : {
                seat_0 : ""	 //场次图片
            },
            images  : {
                seat_1 : rootURL + "images/seats/icon-seat-0.png", //舞台方向
                seat_2 : rootURL + "images/seats/icon-seat-1.png", //可选座位
                seat_3 : rootURL + "images/seats/icon-seat-2.png", //不可选座位
                seat_4 : rootURL + "images/seats/icon-seat-3.png", //锁住座位
                seat_5 : rootURL + "images/seats/icon-seat-4.png", //套票座位
                seat_6 : rootURL + "images/seats/icon-seat-5.png" //选中座位
            },
            data    : {
                pi  :   null,
                vi  :   null,
                vs  :   null,
                ri  :   null,
                rs  :   null
            }
        },
        seatUrl = '../online/seatInfo.html?',  //座位信息页地址
        seatNum = 6,  //普通座位限购6张
        tpSeatNum = 12,  //包含正常套票座位限购12张
        __move  = false,
        __tap   = false;
    
    //微信MQQ环境浏览器 - 去除头部多余的条的高度影响  MicroMessenger  NetType/WIFI
    var ua = navigator.userAgent;
    var android = ua.indexOf("Android") > -1 || ua.indexOf("Linux") > -1;  //android终端或者uc浏览器
    var MQQ = ua.indexOf("MicroMessenger") > -1 && android;  //腾讯mqq浏览器
    
    //根据seat.js获取商品id,sid和n
    var seatJs = Lib.getId("seat");
    
    var pid = Lib.scriptArg(seatJs,"pid");
    var sid = Lib.scriptArg(seatJs,"sid");
    var n = seatNum = Lib.scriptArg(seatJs,"n");
    //把选中的座位暴露给全局
    globle.checkSeats = checkSeats;

    // 场次函数
    function Session(){
        //var that = this;
        seatsDate.pi(pid,function(data){
        	_cache.data.pi = data;
	        var sessList = JSON.parse(_cache.data.pi.replace(/=/g,":"))["result"];
	        //console.log(sessList);
	        //多场次
	        if(sessList.length >1){ 
	            Lib.getId("sessions").style.visibility = '';
	            var tempHtml = '<li seatid="{0}" class="cl" onclick="SeatidFunc({0},this)"><a>{1}</a><span></span></li>';
	            var tempHtmls = "";
	            var nowDate = new Date();
                nowDate = nowDate.format("yyyy-MM-dd hh:mm");
                //console.log(sessList);
	            Lib.each(sessList,function(o,n){
	            	
	            	if(n.time > nowDate)    //1为场次可售，0为售完
	            	  {
	            		var date0 = n["time"].split(' ');
	                	var date1 = date0[0];
	                	var date2 = date1.split('-');
	                	var para1 = date2[0];
	                	var para2 = date2[1];
	                	var para3 = date2[2];
	                	var dayNo = Lib.getWeek(para1,para2,para3);
	                	var dayAll = date0[0] +' ' + dayNo  +' ' + date0[1];
	                	tempHtmls += tempHtml.format(n["id"],dayAll);
	            	  }
	            		
	            });
	            Lib.getId("areas").innerHTML = tempHtmls;
	            Lib.getId("areasAll").style.display = 'block';
	            return false;
	        }else{  //单场次
	            Lib.getId("sessions").style.visibility = '';
	            $('.head-m s').eq(0).html(sessList[0]["D"]);
	            areaDatas(sessList[0]["id"]);
	        }
        });  //获取场次信息
        
        //this.show = function(){

       //}
    }
    
    //多场次点击跳转
    globle.SeatidFunc = function(sid,t){
	    $('#areas li').find('span').removeClass();
	    $(t).find('span').addClass('on');
	    $('.head-m s').eq(0).html($(t).find('a').html());
	    $('.seat-cq a').html($(t).find('a').html());
	    $('#areasAll').slideUp();
	    areaDatas(sid); //直接跳转到场次
	 }
    
    //判断点击还是触摸
    function clickEvent(fn){
        var touchStartTime;
        //var touchStartLocation;
        var touchEndTime;
        //var touchEndLocation;
        var isClick = true;
        canvas.addEventListener('touchstart', function() {
            var d = new Date();
            touchStartTime = d.getTime();
            //touchStartLocation = mouse.location(x,y);
        });
        canvas.addEventListener('touchend', function() {
            var d = new Date();
            touchEndTime= d.getTime();
            //touchEndLocation= mouse.location(x,y);
            doTouchLogic();
        });
        //时间短默认为点击时间，时间长则是拖动
        function doTouchLogic() {
            //var distance = touchEndLocation - touchStartLocation;
            var duration = touchEndTime - touchStartTime;
            if (duration <= 200) {
                //执行点击事件
                fn();
            }
        }
    }

    //事件监听，触摸，拖动
    var Events ={
        init: function(n){
        	drawNum = n;
            Lib.addEvent(canvas,"touchstart", Events.start, false);	//手机触摸点击
            
        },
        start: function(event){
            event.preventDefault();	//不执行与事件关联的默认动作
            if (event.touches.length == 0) return;
            mouse1 = windowToCanvas(canvas,event);
            Lib.addEvent(canvas,"touchmove",  Events.move, false);	//手机触摸移动
            Lib.addEvent(canvas,"touchend",   Events.end, false);	//手机触摸结束
        	try{
		        var touch = event.touches[0];
		        startX = touch.pageX;
		        startY = touch.pageY;
		        //Lib.log(event.touches);
				if (event.touches.length == 2) {
		            var touch1 = event.touches[1];
		            start1X = touch1.pageX;
		            start1Y = touch1.pageY;
		            startLen = Math.sqrt((start1X - startX) * (start1X - startX) + (start1Y - startY) * (start1Y - startY));
				    og(startLen);
				}
        	}catch(e){}
        },
        move: function(event){
            event.preventDefault();	//不执行与事件关联的默认动作
            canvasMid = false;
            chromeMove++;
			//console.log(chromeMove);
			if(chromeMove <=10){  //解决android下chrome点击移动多次选不中问题
				return;
			}
            rects = [];
            move = true;
            var canvasW = window.innerWidth;
			var footHeight = (window.innerHeight - Lib.position(Lib.getId("onlineBom")).top);
			var canvasH  = window.innerHeight - footHeight - Lib.getWH(Lib.getId("header")).height;
			
			var sessionImgW = _cache.data.vi["img_width"];
		    var sessionImgH = _cache.data.vi["img_heigth"];
            if(singleClick && event.targetTouches.length==1 && fangda == false){
            	try {
            		midXY.length = 0;
                    mouse2 = windowToCanvas(canvas,event);
                    var x = mouse2.x - mouse1.x;
                    var y = mouse2.y - mouse1.y;
                    mouse1 = mouse2;
                    
                    //Lib.log(canvasX+"%"+canvasY);
                    if(drawNum == 1){
                        var CanvasWid = canvasW*scale;
                        var CanvasHig = canvasH*scale;
                        //Lib.log(canvasW + "--" + canvasH);
                        //移动边界处理
        		        if(canvasX + x > 240){  //向右移动
        		        	x = 240 - canvasX;
        		        }
        		        if(CanvasWid + canvasX + x < CanvasWid/2){  //向左移动
        		        	x = CanvasWid / 2 - (canvasX + CanvasWid);
        		        }
        		        if (canvasY + y > 220) {  //向下移动
                            y = 220 - canvasY;
                        }
        		        if (canvasY + CanvasHig + y < CanvasHig / 2) {  //向上移动
                            y = CanvasHig / 2 - (canvasY + CanvasHig);
                        }
                    }
    				
    				if(drawNum == 2){
    					var scaleWid = canvasSeatWid;
    					var scaleHeg = canvasSeatHeg;
    					 //移动边界处理
    	                var CanvasWid = scaleWid*(scale) + scaleWid/2;
    	                var CanvasHig = scaleHeg*(scale) + scaleHeg/2;
    	                
    	                //Lib.log(canvasX + x);
    			        if(canvasX + x > 50){  //向右移动
    			        	x = 50 - canvasX;
    			        }
    			        if(CanvasWid + canvasX + x < CanvasWid/2){  //向左移动
    			        	x = CanvasWid / 2 - (canvasX + CanvasWid);
    			        }
    			        if (canvasY + y > 50) {  //向下移动
    	                    y = 50 - canvasY;
    	                }
    			        if (canvasY + CanvasHig + y < CanvasHig / 2) {  //向上移动
    	                    y = CanvasHig / 2 - (canvasY + CanvasHig);
    	                }
    				}
                    
                    canvasX += x;
                    canvasY += y;
            	} catch (e) {
                    alert(e);
                }
            	
            }else{
            	
            	if(event.targetTouches.length==2){
            		fangda = true;
                	singleClick = false;
                	var touch1 = event.touches[0];
                    var touch2 = event.touches[1];
                    var tx1 = touch1.pageX;
                    var ty1 = touch1.pageY;
                    var tx2 = touch2.pageX;
                    var ty2 = touch2.pageY;
                    
                    //计算放大伸缩的比例 
                    nowLen = Math.sqrt((tx2 - tx1) * (tx2 - tx1) + (ty2 - ty1) * (ty2 - ty1)); //双指现在的距离
                    endLen = canvasW + nowLen - startLen;
                    nowScale = endLen / canvasW;
                    scale = (scale+(nowScale-1)).floors(2);
                    Lib.log(scale);
                    
                    if(scale<1.5){
    					scale = 1.5;
    				} 
    				if(scale>3){
    					scale = 3;
    				}
    				//Lib.log(midXY[0]+"##"+midXY[1]);
    				//偏移后的舞台中心为基本点缩放
    				//Lib.log(midXY[0] + "^^"+midXY[1]);
    				var scaleWid,scaleHeg;
    				if(drawNum == 1){
    					scaleWid = sessionImgW;
    					scaleHeg = sessionImgH;
    				}else if(drawNum == 2){
    					scaleWid = canvasSeatWid;
    					scaleHeg = canvasSeatHeg;
    				}
    				//Lib.log(canvasX+"#"+canvasY);
    				//Lib.log(midXY[1]);
    				
    				if(scale >=1.5 && scale <= 3){
    					if(midXY[0]==undefined || midXY[1]==undefined) return;  //修复移缩到左上角
                    	canvasX = (window.innerWidth - scaleWid * scale)/2 + midXY[0];
                        canvasY = (canvas.height - scaleHeg * scale)/2 + midXY[1];
                        //Lib.log(canvasX+"$$"+canvasY);
                    }
                   
                }
            }
            
            
            if(!timeout){
                timeout = window.setTimeout(function () {
                    timeout = null;
                    drawNum == 1 ? Areas() : Seats();
//                    if(drawNum == 1){
//                    	Areas();
//                    }else if(drawNum == 2){
//                    	Seats();
//                    }
                }, 30);
            }
        },
        end: function(event){
            event.preventDefault(); //不执行与事件关联的默认动作
            move = false;
            chromeMove= 0;
            Lib.delEvent(canvas,"touchmove", null);
            Lib.delEvent(canvas,"touchend", null);
            //控制双手指完全离开后
            if (event.targetTouches.length == 0) {
            	singleClick = true;
            	fangda = false;
            }
        }
    };
    
    //获取区域数据
    function areaDatas(vid){
        seatsDate.vi(vid,function(data){_cache.data.vi = JSON.parse(data).result;});
        seatsDate.vs(vid,function(data){_cache.data.vs = JSON.parse(data).result;});
        
        var linterParams = window.setInterval(function(){
    		if(_cache.data.vi!=null && _cache.data.vs!=null){
    			console.log("参数加载成功.");
    			window.clearInterval(linterParams);
    			Areas(vid);
    			AreaBomInfo();
    		}
    	}, 500);
    }
    
    //区域函数
    function Areas(vid){
        var that = this;
        var url;
        //areaDatas(vid);
         
        	var remain = _cache.data.vs;
            var areas = _cache.data.vi["areas"];
            //console.log(areas);
            //场次显示
            Lib.getId("sessions").style.display = "";

            var sessionImgW = _cache.data.vi["img_width"];
            var sessionImgH = _cache.data.vi["img_heigth"];
            //开始画图
         this.draw = function(fn){
            	
            	//清除画布重新绘画 
                context.clearRect(0,0,canvas.width,canvas.height);
                canvas.style.display = "none";
                canvas.offsetHeight;
                canvas.style.display = "block";
            	
                //重新计算区域画布的可视区域
                var footHeight = (window.innerHeight - Lib.position(Lib.getId("onlineBom")).top);
                canvas.height = window.innerHeight - Lib.getWH(Lib.getId("header")).height;
                if(canvasMid){
                    //画布区域图居中
                	canvasX = (window.innerWidth - sessionImgW * scale)/2;
                	canvasY = (canvas.height - sessionImgH * scale)/2;
                }
                //计算中心点的偏移量
                var nowx = canvasX-(window.innerWidth - sessionImgW * scale)/2;
                var nowy = canvasY-(canvas.height - sessionImgH * scale)/2;
                midXY.push(nowx);
                midXY.push(nowy);
                 
                try {
                    context.drawImage(seatImgsCache["seat_0"], canvasX, canvasY,sessionImgW * scale, sessionImgH * scale);
                } catch (e){}
                //Lib.log(areas.rect);
                
                if(areas.length == 1 && areas[0].rect == null){  //没有闭合线直接跳转到座位图
                	seatUrl = seatUrl+'pid='+pid+'&'+'vid='+vid+'&'+'area='+areas[0].id;
                    window.location.href = seatUrl;
                }else{
    	            //开始画闭合线
    	            for(i=areas.length;i--;)
    	            {
    	                var area = areas[i];
    	                var _rect = area["rect"];
    	                if(_rect=="" || _rect==null) continue; //为空则该区域无票;break为跳出循环
    	
    	                context.beginPath();  //画图开始
    	                var rects = _rect.split("|");
    	                for (r = rects.length; r--;) //遍历画图闭合线坐标
    	                {
    	                    var _rect = rects[r].split(","); //拆分点标
    	                    context.lineTo(_rect[0] * scale + canvasX, _rect[1] * scale + canvasY); //按点画闭合线
    	                }
    	                var _id = area.id;   //得到某区域的id
    	                var remainSeat = remain[i];
    	                for(var j in remain){
    	                    if(remain[j].id == _id) remainSeat = remain[j];
    	                }
    	                if(remainSeat.remain == 0) //无座位可售区域
    	                {
    	                    context.fillStyle = area["color"]; //默认半透明灰色
    	                    context.fill(); 	//闭合区域填充颜色
    	                }
    	                context.stroke(); //画图结束
    	                //检测焦点是否存在
    	                if(context.isPointInPath(mouse1.x, mouse1.y))
    	                {
    	                    //Lib.log("在有效范围内,有座位.");
    	                    selArea["area"] = area;
    	                    selArea["remain"] = remainSeat.remain;
    	                }
    	            }
                }
                if(fn!= undefined) fn();
            };
            
            _cache.images0.seat_0 = _cache.data.vi["img"];
            //console.log(_cache.data.vi["img"]);
            if(_cache.images0.seat_0 == null){  //没有区域图直接跳转到座位图
            	seatUrl = seatUrl+'pid='+pid+'&'+'vid='+vid+'&'+'area='+areas[0].id;
                window.location.href = seatUrl;
            }
            //判断图片是否已经预加载
            if(!imgIsLoaded){
            	Lib.loadImages(_cache.images0,function(images){
                	imgIsLoaded = true;
                    //加载场次背景图
                    for ( var image in images)
                    {
                        seatImgsCache[image] = images[image];
                    }
                    that.draw();
                });
            }else{
            	that.draw();
            }
            
            //监听事件
            Events.init(1);
            
            //模拟点击事件
            clickEvent(function(){
                draw(function(){
                    //点击判断有座位区域跳转
                    if(selArea["area"]!= null && selArea["remain"]!= 0){
                    	if(seatUrl.indexOf('pid') < 0)
                    	seatUrl = seatUrl+'pid='+pid+'&'+'vid='+vid+'&'+'area='+selArea["area"].id;
                        window.location.href = seatUrl;
                    }
                });
            });
        
    }
    
   
    //获取座位具体信息数据
    function seatInfoDatas(vid,areaid){
        seatsDate.ri(vid,areaid,function(data){_cache.data.ri = JSON.parse(data).result;});
        seatsDate.rs(vid,areaid,function(data){_cache.data.rs = JSON.parse(data).result;});
        seatsDate.vi(vid,function(data){_cache.data.vi = JSON.parse(data).result;});
        
        var linterParams = window.setInterval(function(){
    		if(_cache.data.ri!=null && _cache.data.rs!=null && _cache.data.vi!=null){
    			console.log("参数加载成功2.");
    			window.clearInterval(linterParams);
    			Seats();
    			SeatBomInfo();
    		}
    	}, 500);
        
    }
    
    //初始化画座位信息数据
    function InitSeat(){
    	//获取vid,areaid
    	var vid    = Lib.getParam('vid');
    	var areaid = Lib.getParam('area');
    	seatInfoDatas(vid,areaid);
    	
    }
    
    var nums = 0;
    //座位函数 --  移动时候直接调用draw方法，其他的公共变量缓存不重新计算优化 --
    function Seats(){
    	var that = this;
    	
    	var _seats  = _cache.data.ri;
        var _sales  = _cache.data.rs;
        var _prices = _cache.data.vi["prices"]; 
        
        var pricesMap = {} , pricesSalesMap = {};
        
        if(pricesMap.length == undefined || pricesMap.length == undefined){
        	for ( var num in _prices) {
        		var price = _prices[num];
        		pricesMap[price["id"]+""] = price;
        	}
        	for ( var num in _sales) {
        		var sales = _sales[num]; 
        		pricesSalesMap[sales["id"]+""] = sales;
        	}
        }
        
        var _width  = seatO.w*scale;
        var _height = seatO.h*scale;
        
        //获取座位x,y最小和最大值
		var ret  = [];
		var ret1  = [];
		for(i=_seats.length;i--;)
        {
			var _seatsResult  = _seats[i];
            ret.push(_seatsResult["x"]);
            ret1.push(_seatsResult["y"]);
        }
		var x_min = Math.min.apply(null,ret);
		var x_max = Math.max.apply(null,ret);
		var y_min = Math.min.apply(null,ret1);
		var y_max = Math.max.apply(null,ret1);
        
        //重新计算区域画布的可视区域
        var footHeight = (window.innerHeight - Lib.position(Lib.getId("onlineBom")).top);
        canvas.height = window.innerHeight - Lib.getWH(Lib.getId("header")).height;
        
        canvasSeatWid = (x_max - x_min)*_width + (x_max - x_min)*8;
        canvasSeatHeg = (y_max - y_min)*_width + (y_max - y_min)*8;
        //保存座位初始的宽度和高度
        if(seatArr[0] == undefined){
        	seatArr.push(canvasSeatWid);
            seatArr.push(canvasSeatHeg);
        }
        canvasSeatWid = seatArr[0];
        canvasSeatHeg = seatArr[1];
        
        if(canvasMid){
            //画布区域图居中
        	canvasX = (window.innerWidth - canvasSeatWid * scale)/2;
        	canvasY = (canvas.height - canvasSeatHeg * scale)/2;
        }
        //计算中心点的偏移量
        var nowx = canvasX-(window.innerWidth - canvasSeatWid * scale)/2;
        var nowy = canvasY-(canvas.height - canvasSeatHeg * scale)/2;
        //Lib.log(nowx+"#"+nowy);
        midXY.push(nowx);
        midXY.push(nowy);
        
        this.draw = function(fn){
        	//Lib.log(++nums);
        	//清除画布重新绘画 
            context.clearRect(0,0,canvas.width,canvas.height);
            canvas.style.display = "none";
            canvas.offsetHeight;
            canvas.style.display = "block";
            
            var _width  = seatO.w*scale;
            var _height = seatO.h*scale;
        	
        	for(i=_seats.length;i--;)
            {
                var _seatsResult  = _seats[i];
                var $price_id     = _seatsResult["price_id"];
                var $seatid       = _seatsResult["id"];
                
                var $seat_x = _seatsResult["x"]*(_width + 8) + canvasX;
                var $seat_y = _seatsResult["y"]*(_height + 8) + canvasY;
                
                $seat_x = (0.5 + $seat_x) << 0;	//x坐标取整
                $seat_y = (0.5 + $seat_y) << 0; //y坐标取整
                
                var $seat_w = _width;
                var $seat_h = _height;
                
                var on_sale = pricesSalesMap[$seatid]["on_sale"];
                
                if(on_sale==1){
                	context.fillStyle = "#"+pricesMap[$price_id]["color"];
                }else{
                	context.fillStyle = "#cccccc";
                }
                //判断隐藏不出售的座位
                if(on_sale!=2)  
                  context.fillRect($seat_x,$seat_y,$seat_w,$seat_h);
                
                _seatsResult["price"] = pricesMap[$price_id];
                _seatsResult["sale"] = pricesSalesMap[$seatid];
                
                var key = "{0}_{1}".format($seat_x,$seat_y);
                pricesSeatMap[key] = _seatsResult;
                //this.drawRect($seat_x,$seat_y);
                //存放座位的坐标范围
                if(rects.length <_seats.length)
                  rects.push([$seat_x, $seat_y, $seat_x + _width, $seat_y + _height]);
                
                var drawSeatImg = seatImgsCache["seat_2"]; //默认背景图片
                if(_seatsResult["tp"]==1 && on_sale==1){
                	drawSeatImg = seatImgsCache["seat_5"];
                }
                for(var s=checkSeats.length;s--;){
                	if(checkSeats[s]["id"] == $seatid){ //购买的座位
                		drawSeatImg = seatImgsCache["seat_6"];
                	}
                }
                //判断隐藏不出售的座位 
                if(on_sale!=2)
                  context.drawImage(drawSeatImg, $seat_x, $seat_y, seatO.w* scale, seatO.h* scale);
            }
        	if(fn!= undefined) fn();
        };
        
        //判断图片是否已经预加载
        if(!imgIsLoaded){
        	Lib.loadImages(_cache.images,function(images){
            	imgIsLoaded = true;
                //加载场次背景图
                for ( var image in images)
                {
                    seatImgsCache[image] = images[image];
                }
                that.draw();
            });
        }else{
        	that.draw();
        }
        
        //监听事件
        Events.init(2);
        
    }
    
    
    
    //var nums1 = 0;
    //判断选中的座位
    function clickSelect(){
    	var x = mouse1.x;
        var y = mouse1.y;
        //Lib.log(rects);
        //Lib.log(x +" * "+y);
        for (var i= rects.length; i-->0;) 
        {
            if(rects[i]==undefined) break;
            var x0= rects[i][0], y0= rects[i][1], x1= rects[i][2], y1= rects[i][3];
            if (x0<=x && x<x1 && y0<=y && y<y1) 
            {
                var key = "{0}_{1}".format(x0,y0);
                if(pricesSeatMap[key].sale.on_sale != 0 && pricesSeatMap[key].sale.on_sale != 2)  //灰色的不能选中
                  Success(pricesSeatMap[key]);
            }
        }
    }
    
    //计算正常套票按照tp_id删除后的集合
    function tpDelTpid(s){
    	//删除正常套票中相同的tp_id
    	for(var i = checkSeats.length -1; i >= 0; i--){
    		if(checkSeats[i].tp_id == s){ 
    			checkSeats.splice(i,1);
    		}
    	}
    	return checkSeats;
    }

    //计算正常套票按照price_id删除后的集合
    function delPricId(s){
    	//删除正常套票中相同的price_id
    	for(var i = checkSeats.length -1; i >= 0; i--){
    		if(checkSeats[i].price_id == s){ 
    			checkSeats.splice(i,1);
    		}
    	}
    	return checkSeats;
    }

    //删除数组里面包含的某个键值对后返回其他的
    function delByArg(a,b,c){
    	for(var i = a.length -1; i >= 0; i--){
    		if(a[i][b] == c){ 
    			a.splice(i,1);
    		}
    	}
    	return a;
    }

    //删除数组里面包含的某个键值对后返回其他的
    function delAllByArg(a,b,c){
    	for(var i = a.length -1; i >= 0; i--){
    		if(a[i][b] == c){ 
    			a.splice(i,1);
    		}
    	}
    	//删除数据源里面包含对应的属性
    	var seats = _cache.data.ri;
    	for(var i = seats.length;i--;){
    		if(seats[i].hasOwnProperty(b) && seats[i][b] === c){
    			delete seats[i][b];
    			delete seats[i]["zyPrice"];
    		}
    	}
    	return a;
    }
    
    //把选中的座位显示或取消在画布
    function Success(seatObj){
    	//Lib.log(checkSeats);
    	if(move) return; //添加移动冒泡事件
    	var flag = true;
    	var hasTp = false;  //是否包含套票
    	//再次点击选中的座位则取消选中
    	checkSeats.each(function(o,n){
    		if(o.id == seatObj.id){    //有相同的套票ID则不提交相关的套票
    			checkSeats.splice(n,1);
    			flag = false;
    			if(seatObj.tp == 1){  //判断是否为正常套票
    				checkSeats = tpDelTpid(seatObj.tp_id);
    				//checkSeats.length = 0;
    				//checkSeats = noTpLists;
    		    } 
    			//Lib.log(seatObj);
    			if(seatObj["zytp"] !== undefined){  //判断是否为自由套票
    				checkSeats = delAllByArg(checkSeats,'zytp',seatObj["zytp"]);
    		    } 
    		}
    		if(o.tp == 1) hasTp = true;
    	});
    	if(!hasTp && checkSeats.length >= seatnum){  //非套票最多购买6张
    		alert("最多只能选择{0}张票...".format(seatnum));
    		return false;
    	}else if(hasTp && checkSeats.length >= tpseatnum){  //包含套票最多能买12张
    		alert("最多只能选择{0}张票...".format(tpseatnum));
    		return false;
    	}
    	if(flag) {
    		var tplist = _cache.data.vi["tplist"];	//套票集合
    		var freetp = false;  //是否自由套票
    		tppricesMap = {}; //套票  [#price_id(_)#type_id] 集合套票规则
    		zytppricesMap= [];
    		for(var tp = tplist.length; tp--;){
    			var _tp = tplist[tp];
    			tppricesMap[_tp["price_id"]+"_"+_tp["type_id"]] = _tp;
    			//添加自由套票集合
    			if(_tp["is_free"] == 1){
    				zytppricesMap.push(_tp);
    			}
    		}
    		//console.log("--------------");
    		var seats = _cache.data.ri; //票价缓存
    		//判断选择的的票价组合里面是否符合自由套票规则
    		
    		//if(seatObj["tp"]==0 && freetp){  //自由套票
    			
    		//}else 
    		if(seatObj["tp"]==1){  //正常套票 
    			//console.log(seatObj);
    			var tpp;
    			//if(seatObj["tp_id"] == 0){  //特殊情况处理
    				tpp = seats.select('@tp_type_id={0}'.format(seatObj["tp_type_id"]));
    			//}else{
    				tpp = tpp.select('@tp_id={0}'.format(seatObj["tp_id"]));
    			//}
    			
    			//console.log("--------------");
    			var cur = new Array(tpp);
    			var isFalse = true;
    			console.log(tpp);
    			tplist.each(function(o,n){
    				if(o.is_free == 0 && o.num <= 3){
    					isFalse = false;
    				}
    			});
    			if(!isFalse && checkSeats.length + cur[0].length  > seatnum){  //包含套票2-2或3-3套票规则的最多6张
    				alert("最多只能选择{0}张票...".format(seatnum));
    				return false;
    			}
    			else if(isFalse && checkSeats.length + cur[0].length  > tpseatnum){  //包含套票最多能买12张
    				alert("最多只能选择{0}张票...".format(tpseatnum));
    				return false;
    			}
    			Array.prototype.push.apply(checkSeats,cur[0]);			
    			//checkSeats.concat(cur[0]);
    		}else{	//非套票
    			checkSeats.push(seatObj);
    		}
    	}
    	//选中座位在下面显示
    	FunCheckSeats();
    }
    
    //根据价格和数量判断套票类型
    function tpTypeNum(pr,num){
    	var n;
    	zytppricesMap.each(function(x,y){
			if(x['price_id'] == pr && x['num'] == num){
				n = x['type_id'];
			}
		});
    	return n;
    }
    
    //选中座位列表底部购物车展示
    globle.FunCheckSeats = function(num,t){
    	var chilePricesTem = '';
    	var showNub = [];  //存放正常票id
    	var showZyNub = [];  //存放自由套票tp_id
    	if(num !=undefined){
    		if(t == -1){  //判断是正常套票
    			checkSeats = tpDelTpid(num);
    		}if(t == -2){  //判断是自由套票
    			checkSeats = delAllByArg(checkSeats,'zytp',num);
    		}else if(t == undefined){
    			checkSeats.splice(num,1);
    		}
    	} 
    	function csLensFunc(){
    		var nubs = ++csLens[0];
    		csLens.length = 0;
    		csLens.push(nubs);
    	}
    	if(checkSeats.length > 1){  //判断数量大于1的时候检查是否包含自由套票
    		
    		zytppricesMap.each(function(o,n){
    			var zytpp = new Array(checkSeats.select('@price_id={0}'.format(o["price_id"])))[0]; 
    			//求price_id所包含的套票规则的数量 
    			var zytpn = [];
    			zytppricesMap.each(function(x,y){
    				if(x["price_id"] == o["price_id"]){
    					zytpn.push(x.num);
    				}
    			});
    			//zytpn = zytpn.sort(function(a,b){return a<b?1:-1});
    			//console.log(zytpn);
    			//2套-3套-(2套,2套)-(2套-3套)-(2套-2套-2套或3套-3套)
    			
    			if(zytpp.length == o.num){  //判断自由套票数量符合条件,不同票价的组合也可以配套  2套-3套
    				checkSeats = delPricId(o["price_id"]);
    				zytpp.each(function(x,y){
    					x["zytp"] = csLens[0];
    					x["tp_id"] = csLens[0];
    					x["tp_type_id"] = o["type_id"];
    				});
    				csLensFunc();
    				Array.prototype.push.apply(checkSeats,zytpp);
    				
    				//同一票价多次自由套票，去除已经选好的套票组，给下一组自由套票自动匹配
    				var zyttpOther = [];
    				for(var i = zytpp.length -1; i >= 0; i--){ //给自由套票排序，符合下一组自由套票的挨着放一起匹配
    					if(zytpp[i]['zytp'] !== undefined){ 
    						zyttpOther.push(zytpp[i]);
    						zytpp.splice(i,1);
    					}
    				}
    				if(zytpp.length == o.num){  
    					checkSeats = delPricId(o["price_id"]);
    					zytpp.each(function(x,y){
    						x["zytp"] = csLens[0];
    						x["tp_id"] = csLens[0];
    						x["tp_type_id"] = o["type_id"];
    					});
    					csLensFunc();
    					Array.prototype.push.apply(checkSeats,zytpp);
    					Array.prototype.push.apply(checkSeats,zyttpOther);
    				}
    				
    			}
    			else if(zytpp.length % o.num == 0 && zytpp.length / o.num > 1){ //组合(2套,2套)(3套,3套)(2套,2套,2套)
    				for(var j=0;j<zytpn.length;j++){
    					if(zytpn[j]*3 == zytpp.length){ //2,2,2 跟公网同步优先这种方式排列
    						for(var i =0;i<zytpn[j];i++){
    							zytpp[i]["zytp"] = csLens[0];
    							zytpp[i]["tp_id"] = csLens[0];
    							zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],zytpn[j]);
    						}
    						csLensFunc();
    						for(var i =zytpn[j];i<zytpn[j]*2;i++){
    							zytpp[i]["zytp"] = csLens[0];
    							zytpp[i]["tp_id"] = csLens[0];
    							zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],zytpn[j]);
    						}
    						csLensFunc();
    						for(var i =zytpn[j]*2;i<zytpp.length;i++){
    							zytpp[i]["zytp"] = csLens[0];
    							zytpp[i]["tp_id"] = csLens[0];
    							zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],zytpn[j]);
    						}
    						csLensFunc();
    						break;
    					}
    					if(zytpn[j]*2 == zytpp.length){  //2,2或3,3
    						for(var i =0;i<o.num;i++){
    							zytpp[i]["zytp"] = csLens[0];
    							zytpp[i]["tp_id"] = csLens[0];
    							zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],o.num);
    						}
    						csLensFunc();
    						for(var i =o.num;i<zytpp.length;i++){
    							zytpp[i]["zytp"] = csLens[0];
    							zytpp[i]["tp_id"] = csLens[0];
    							zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],zytpp.length-o.num);
    						}
    						csLensFunc();
    					}

    				}
    				//checkSeats = delPricId(o["price_id"]);
    				//Array.prototype.push.apply(checkSeats,zytpp);
    			}else if(zytpp.length / o.num > 1 && zytpn.contains(zytpp.length % o.num)){  //(2套,3套)
    				var numa = zytpp.length % o.num;
    				for(var i =0;i<numa;i++){
    					zytpp[i]["zytp"] = csLens[0];
    					zytpp[i]["tp_id"] = csLens[0];
    					zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],numa);
    				}
    				csLensFunc();
    				for(var i =numa;i<zytpp.length;i++){
    					zytpp[i]["zytp"] = csLens[0];
    					zytpp[i]["tp_id"] = csLens[0];
    					zytpp[i]["tp_type_id"] = tpTypeNum(o["price_id"],zytpp.length-numa);
    				}
    				csLensFunc();
    			}

    			//console.log(checkSeats);
    			zytpn = [];  //清空某个票价ID的里面对应的套票数量组合
    		});
    	}
    	//Lib.log(checkSeats);
    	//针对checkSeats相同票价的在一起  正常套票有问题
//    	checkSeats = checkSeats.sort(function(a){
//    		if(a["tp"] == 0)
//    		    return a["zytp"] == undefined ? 1:-1;
//    	});
    	checkSeats.each(function(o,n){
    		chilePricesTem += '<li class="cl">';
    		chilePricesTem += '<span class="show-sp1" style="background-color:#{0};"></span>';
    		chilePricesTem += '<span class="show-sp2">{1}元</span>';
    		chilePricesTem += '<span class="show-sp3">{2}</span>';
    		if(o.tp == 1 && !showNub.contains(o.tp_id)){  //正常套票,后面关闭按钮只出现一个
    			chilePricesTem += '<a class="show-sp4" onclick="FunCheckSeats('+o.tp_id+',-1)" ></a>';
    		}
    		else if(o.tp != 1 && o["zytp"] !== undefined && !showZyNub.contains(o["zytp"])){  //自由套票
    			var zitpnub = parseInt(o["zytp"]);
    			chilePricesTem += '<a class="show-sp4" onclick="FunCheckSeats('+zitpnub+',-2)" ></a>';
    		}
    		else if(o.tp != 1 && o["zytp"] === undefined){  //非套票
    			chilePricesTem += '<a class="show-sp4" onclick="FunCheckSeats({3})" ></a>';
    		}
    		chilePricesTem += '</li>';
    		chilePricesTem = chilePricesTem.format(o["price"]["color"],o["price"]["price"],o["pinfo"],n);
    		if(o["zytp"] !== undefined){
    			showZyNub.push(o.zytp);
    			showZyNub = showZyNub.unique();
    		}
    		showNub.push(o.tp_id);
    		showNub = showNub.unique();
    	});
    	Lib.getId("child_prices").innerHTML = chilePricesTem;
    	Lib.getId("priceNum").innerHTML = checkSeats.length;
    	
    	
    	//console.log("--------------");
    	//Lib.log(checkSeats);
    	
    	rects = [];
    	showNub= [];
    	if(checkSeats.length == 0){
    		csLens= [1000];  //清空自由套票序号
    	} 
    	Seats();
    };
    
    //座位信息显示绑定点击事件
    function SeatClick(){
		clickEvent(function(){
	        clickSelect();
	    });
    }  
    
    //底部显示票价信息
    function AreaBomInfo(){
   	 //areaDatas(vid);
        
        var prices = _cache.data.vi["prices"];
        var pricesTem = '';
        for ( var n = 0; n < prices.length; n++) {
            var ul = (n % 4 == 0);
            if(n % 4 == 0) pricesTem += '<ul class="online-ua cl">';
            pricesTem += '<li><span><s style="background-color:#{0};"></s> {1}元</span></li>'.format(prices[n]["color"],prices[n]["price"]);
            if(n % 4 == 3) pricesTem += '</ul>';
        }
        Lib.getId("prices1").innerHTML = pricesTem;
        Lib.getId("prices2").innerHTML = pricesTem;
        Lib.getId("araesShow").style.display = "";
        Lib.getId("seatsShow").style.display = "none";
        Lib.getId("onlineBom").style.bottom = 0;
   };
   
   
   //底部显示票价信息
   function SeatBomInfo(){
  	 //areaDatas(vid);
       
       var prices = _cache.data.vi["prices"];
       console.log(prices);
       var pricesTem = '';
       for ( var n = 0; n < prices.length; n++) {
           var ul = (n % 4 == 0);
           if(n % 4 == 0) pricesTem += '<ul class="online-ua cl">';
           pricesTem += '<li><span><s style="background-color:#{0};"></s> {1}元</span></li>'.format(prices[n]["color"],prices[n]["price"]);
           if(n % 4 == 3) pricesTem += '</ul>';
       }
       Lib.getId("prices2").innerHTML = pricesTem;
       Lib.getId("seatsShow").style.display = "";
       Lib.getId("onbuy").style.display = "";
       //Lib.getId("onlineBom").style.bottom = 0;
  };
  

    //焦点函数鼠标x,y轴
    function windowToCanvas(canvas,e){//x,y){
        //console.log(e);
        //console.log(e.touches);
        var pageEvent = {
            pageX : (e.touches && e.touches.length != 0) ? e.touches[0].pageX : e.pageX,
            pageY : (e.touches && e.touches.length != 0) ? e.touches[0].pageY : e.pageY
        };
        var x = pageEvent.pageX;
        var y = pageEvent.pageY;
        if(MQQ) y -= 30;
        var _canvas = canvas.getBoundingClientRect();
        return {
            x:x - _canvas.left - (_canvas.width - canvas.width) / 2,
            y:y - _canvas.top - (_canvas.height - canvas.height) / 2
        };
    }

    return function(_config){
        var that = this;
        that.config = _config || {};
        that.loading = that.config.loading || function(){};
        //...

        //初始化画布
        function init(fn){
            canvas	= Lib.getId("canvas");
            var footHeight = (window.innerHeight - Lib.position(Lib.getId("onlineBom")).top);
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight - footHeight;
            context	= canvas.getContext("2d");
        }
        init();
        //Session();  //场次显示
        
        return {
            session : Session,
            initSeat: InitSeat,
            seat    : Seats,
            area    : Areas,
            seatClick : SeatClick,
            areaBomInfo: AreaBomInfo,
            seatBomInfo: SeatBomInfo
        };
    };

})(window);


