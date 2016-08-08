/**
 * 基础方法公用包
 * Created by wangzhihua on 15-1-9.
 */

(function(win,undefined){

    var _document = win.document,
        _u = win.navigator.userAgent,
        _app = navigator.appVersion;

    Lib = win.Lib = {};

    //扩展obj对象的方法
    Lib.expand = function(obj,fn,args){
        for(var i in fn){
            obj[i] = fn[i];
        }
        return args ? win.Lib.expand(obj,args) : obj;
    };

    //扩展数组方法
    Lib.expand(Array.prototype,{
    	/* 数组循环方法 */
    	each: function (fn) { 
    		for (var i = 0; i < this.length; i ++)
    			if (fn (this[i], i) === false) 
    				break;
    		return this;
    	}, 
        //数组是否包含某个字符串
        contains: function(s){
        	if(this.length != undefined){
				for(var i = 0,k = this.length; i < k; i++){    //k放前面计算长度，js效率
					if(this[i] == s) return true;
				}
			}
		    return false;
        },
        //对数组删除某个元素的扩展
        del: function(n){
            if(n < 0)
                return this;
            else
                return this.slice(0,n).concat(this.slice(n+1,this.length));
        },
        //数组去除重复
        unique: function(){
            if(typeof this === "string"){   //字符串去重复
                var a = {},b = "",c;    //a为hash表
                for(var i =0,k = this.length; i < k; i++){
                    c = this.charAt(i);
                    if(!a[c]) a[c] = true;
                }
                for(var m in a){
                    b += m;
                }
                return b;
            }
            if(this instanceof Array){    //数组去重复
                var a = {},b = [],c;
                for(var i =0,k = this.length; i < k; i++){
                    c = this[i];
                    if(!a[c]) a[c] = true;
                }
                for(var m in a){
                    b.push(m);
                }
                return b.sort();   //IE和火狐排序不一致
            }
            return "";
        },
        //获取数组里元素最大值
        getMax: function(){
            return Math.max.apply(null,this);
        },
        //获取数组里元素最小值
        getMin: function(){
            return Math.min.apply(null,this);
        }
    });

    //扩展字符串方法
    Lib.expand(String.prototype,{
        /* 格式化字符串 */
        /* 'Hello, {0} ! '.format('world');  */
        format: function () {
            var s = this, length = arguments.length;
            while (--length >= 0)  s = s.replace(new RegExp('\\{' + length + '\\}', 'g'), arguments[length]);
            return s;
        }
    });

    //扩展数值方法
    Lib.expand(Number.prototype,{
        //截取小数点后面的位数
        floors: function(length){
            var num;
            length = length ? parseInt(length) : 0;
            if (length <= 0) return Math.floor(this);
            num = Math.floor(this * Math.pow(10, length)) / Math.pow(10, length);
            return num;
        }
    })

    /*
     * 对时间进行格式化,对原生时间类扩展
     * var date = new Date();
     * date.format("yyyy-MM-dd hh:mm:ss")
     */
    Lib.expand(Date.prototype,{
        format: function(format)
        {
            var o =
            {
                "M+" : this.getMonth()+1, //month
                "d+" : this.getDate(),    //day
                "h+" : this.getHours(),   //hour
                "m+" : this.getMinutes(), //minute
                "s+" : this.getSeconds(), //second
                "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
                "S" : this.getMilliseconds() //millisecond
            };
            if(/(y+)/.test(format))
                format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
            for(var k in o)
                if(new RegExp("("+ k +")").test(format))
                    format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
            return format;
        }
    })

    Lib = {
        // ajax 函数
        Ajax : function (config){
            var that = this;
            // 公共参数
            that.config = config || {};
            that.url = that.config.url || "";
            that.method = that.config.method || "GET";
            that.async = true;
            that.timeout = 8000;

            if(that.config.async == false) that.async = false;

            // 发送请求函数
            var xmlhttp;
            if (window.XMLHttpRequest){
                xmlhttp=new XMLHttpRequest();
            }else{
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            //记录请求是否成功完成；
            var requestDone = false;
            //初始化一个N秒后执行的回调函数，用于当N秒后未完成的话取消请求；
            setTimeout(function(){ requestDone = true; },that.timeout);

            var urls = jsonToParams(config);
            //console.log(urls);
            if(that.method == "GET"){
                xmlhttp.open(that.method,urls,that.async);
                xmlhttp.onreadystatechange = readyStateChange;		//建立回调函数；
                xmlhttp.send();
            }else if(that.method == "POST"){
                xmlhttp.open(that.method,urls,that.async);
                xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
                xmlhttp.onreadystatechange = readyStateChange;		//建立回调函数；
                xmlhttp.send();
            }

            function readyStateChange(){
                if(xmlhttp.readyState==4 && !requestDone){
                    if(xmlhttp.status >= 200 && xmlhttp.status < 300){
                        that.config.success(xmlhttp.responseText);
                    }
                }
            }

            // json数据转义&参数拼接
            function jsonToParams(params){
                var url = params.url || that.url;
                var datas = params.data;
                var urlAnd = "";
                for(var p in datas){
                    urlAnd = urlAnd + p +'='+ datas[p] + '&';
                }
                url = (url + '?' + urlAnd).replace(/&$/,'')+"&rm="+(new Date).getTime();
                return url;
            }

//        var util = {
//            get : function(params){
//                var urls = jsonToParams(params);
//                var async = params.async || that.async;
//                var method =  that.method || params.method;
//                send(method,urls,async);
//
//            },
//            post : function(params){
//                var urls = jsonToParams(params);
//            }
//        };

//        return {
//            get : util.get,
//            post : util.post
//        }

        },
        //判断设备内核和浏览器厂商
        browser:{
            trident: _u.indexOf('Trident') > -1, //IE内核
            presto: _u.indexOf('Presto') > -1, //opera内核
            webKit: _u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: _u.indexOf('Gecko') > -1 && _u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!_u.match(/AppleWebKit.*Mobile.*/)||!!_u.match(/AppleWebKit/), //是否为移动终端
            ios: !!_u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: _u.indexOf('Android') > -1 || _u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: _u.indexOf('iPhone') > -1 || _u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
            iPad: _u.indexOf('iPad') > -1, //是否iPad
            webApp: _u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            is360: _u.indexOf('360') > -1, //360浏览器
            isUC: _u.indexOf('UCBrowser') > -1  //uc浏览器
        },
        getId: function(s){
            return document.getElementById(s);
        },
        log:function(s){
            return console.log(s);
        },
        //对象遍历
        each: function(obj,fn){
            if ( obj.length == undefined ) {
                for ( var i in obj )
                    fn.call( obj[i], i, obj[i] );
            }else{
                for(var i = 0, l = obj.length; i < l; i++){
                    var t = obj[i];
                    if(t !== false) fn.call(t,i,t)
                }
            }
            return obj;
        },
        /*
         * 获取元素当前样式
         * @param element 元素节点
         * @param string  字符串
         * 方法: var s=$("#ele")  Moo.attr(s,"width"); 获取元素宽度
         */
        attr: function(dom,name){
            if(dom.currentStyle){   //ie
                return dom.currentStyle[name];
            }else if(_document.getComputedStyle){  //其他
                return _document.getComputedStyle(dom,null)[name];
            }
        },
        /**
         * 获取URL参数
         * @param script script对象
         * @param key    参数key
         * @returns	对应key的vlaue
         */
        scriptArg: function(script, key){
            var src = script.src;
            return (src.match(new RegExp("(?:\\?|&)"+key+"=(.*?)(?=&|$)"))||['',null])[1];
        },
        //注册事件兼容IE和火狐
        addEvent: function(obj,events,fn){    //注册事件
            if(obj.attachEvent){
                obj.attachEvent("on"+events,fn)
            }else{
                obj.addEventListener(events,fn,false)}
        },
        delEvent: function(obj,events,fn){    //销毁事件
            if(obj.detachEvent){
                obj.detachEvent("on"+events,fn)
            }else{
                obj.removeEventListener(events,fn,false)
            }
        },
        //阻止冒泡事件和浏览器默认的行为
        stopEvent: function(e){
            var e = e || WID.event;
            if(e.stopPropagation){
                e.stopPropagation();
            }else{
                e.cancelBubble = true;
            }
            if(e.preventDefault){
                e.preventDefault();
            }else{
                e.returnValue = false;  //阻止Event返回值
            }
        },
        //多图片的预加载函数
        loadImages: function(sources, callback){
            var count = 0, images ={}, imgNum = 0;
            for(src in sources){ imgNum++; }
            for(src in sources){
                images[src] = new Image();
                images[src].src = sources[src];
                if(images[src].complete){  //是否有缓冲存在
                    if(++count >= imgNum){
                        callback(images); //满足数量直接返回
                    }
                    continue;
                }
                images[src].onload = function(){ // 图片加载完成
                    if(++count >= imgNum){
                        callback(images);
                    }
                };
            }
        },
        //取元素坐标
        position: function(node){
            var left = 0, top = 0;
            do{
                left += node.offsetLeft;
                top  += node.offsetTop;
            }while((node = node.offsetParent))

            return {"left":left,"top":top};
        },
        //获取元素大小
        getWH: function(obj){
            var w = 0 , h = 0;
            w = obj.offsetWidth;
            h = obj.offsetHeight;
            return {"width" : w,"height" : h};
        },
        /**
         * 多图片的预加载
         * @param {Object} sources  图片数组
         * @param {Object} callback 回调函数
         */
         loadImages : function (sources, callback){
            var count = 0, images ={}, imgNum = 0;
            for(src in sources){
                if(sources[src] == "") continue; //为空则跳出
                imgNum++;
            }
            for(src in sources){
                if(sources[src] == "") continue; //为空则跳出
                images[src] = new Image();
                images[src].src = sources[src];
                if(images[src].complete){  //是否有缓冲存在
                    if(++count >= imgNum){
                        callback(images); //满足数量直接返回
                    }
                    continue;
                }
                images[src].onload = function(){
                    if(++count >= imgNum){
                        callback(images);
                    }
                };
            }
        },
        getParam: function(paramName, pageSrc) {
            var reg = new RegExp("(^|/?|&)"+ paramName+"=([^&]*)(/s|&|$)", "i");
            // 2014-12-16 L.cm
            if (typeof pageSrc === 'undefined') pageSrc = location.href;
            if (reg.test(pageSrc)) //test为script ID 
                return RegExp.$2; 
            else
                return ""; 
        },
         /*
		 * 	获得指定日期的星期数，1-6为星期一到星期六，0为星期天
		 *	@y 年份
		 *	@m 月份
		 *	@d 日
		 */
		  getWeek: function(y, m, d) {
			var _int = parseInt,
				c = _int(y/100),
			    wseng = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];
			y = parseInt(y);
			m = parseInt(m);
			d = parseInt(d);
			y = y.toString().substring(2, 4);
			y = _int(y, 10);
			if(m === 1) {
				m = 13;
				y--;
			} else if(m === 2) {
				m = 14;
				y--;
			};
		 
			var w = y + _int(y/4) + _int(c/4) - 2*c + _int(26*(m+1)/10) + d - 1;
			w = w%7;
		 
			w = w >= 0 ? w : w+7;
			return wseng[w];
		},
        
        /* JSON数据绑定 */
		jsonSelect: function(){
			var __proto = Array.prototype;
		    var __tmpl = function(_list) {
		        var _ret = [];
		        var _i = -1;
		        for (var _k in _list) {
		            var _e = _list[_k];
		            if (_e && _e != __proto[_k]) {
		                if ($C) {
		                    _ret[++_i] = _e;
		                }
		            }
		        }
		        return _ret;
		    }.toString();

		    function len(s) {return s.length;}
		    function left(s, n) {return s.substr(0, n);}
		    function right(s, n) {return s.substr(s.length - n);}
		    function mid(s, n, m) {return s.substr(n + 1, m);}
		    function instr(s, find) {return s.indexOf(find) + 1;}
		    
		    var __alias = [/@/g, "_e.", /<>/g, "!=", /AND/gi, "&&", /OR/gi, "||", /NOT/gi, "!", /([^=<>])=([^=]|$)/g, "$1==$2"];
		    var __rQuote = /""/g;
		    var __rQuoteTmp = /!~/g;
		    function __interpret(exp) {
		        exp = exp.replace(__rQuote, "!~");
		        var arr = exp.split("\"");
		        var i, n = arr.length;
		        var k = __alias.length;
		        for (i = 0; i < n; i += 2) {
		            var s = arr[i];
		            for (var j = 0; j < k; j += 2) {
		                s = s.replace(__alias[j], __alias[j + 1]);
		            }
		            arr[i] = s;
		        }
		        for (i = 1; i < n; i += 2) {
		            arr[i] = arr[i].replace(__rQuoteTmp, "\\\"");
		        }
		        return arr.join("\"");
		    }
		    function __compile() {
		        return eval("0," + arguments[0]);
		    }

		    var __cache = {};
		    __proto.select = function(exp) {
		        if (!exp) {
		            return [];
		        }
		        var fn = __cache[exp];
		        try {
		            if (!fn) {
		                var code = __interpret(exp);
		                code = __tmpl.replace("$C", code);
		                fn = __cache[exp] = __compile(code);
		            }
		            return fn(this);
		        } catch(e) {
		            return [];
		        }
		    }
		}

    }
  
    //初始化函数
	Lib.jsonSelect();  

})(window,undefined)

var onlineSeatUrl = Lib.getParam("onlineSeatUrl");
var maxseatNum = Lib.getParam("maxseatNum");
console.log(maxseatNum);
var maxtpseatnum = 12;
if (onlineSeatUrl == null || onlineSeatUrl == undefined || onlineSeatUrl == "" || onlineSeatUrl =="null" || onlineSeatUrl=="(null)") {
	onlineSeatUrl = "/appLM/load.jsonp";	
}
if (maxseatNum == null || maxseatNum == undefined || maxseatNum == "" || maxseatNum =="null" || maxseatNum=="(null)") {
	maxseatNum = 6;	
}else{
	maxtpseatnum = maxseatNum;
}
