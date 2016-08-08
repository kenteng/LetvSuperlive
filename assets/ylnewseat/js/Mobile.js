/**
 * 手机通用网页形式回调对象（兼容IOS、Andriod）
 * Created by zjj on 2015/1/8.
 *
 * var phone = new Phone();
 * phone.Callback("onlineSeatBack","back"); // 后退
 * phone.Callback("onlineSeatOrderInfo","product_id=55288687&product_play_id=55288695&num=1&address_id=0"); // 订单详情
 */

function log( str ){ console.info( str )};
if (typeof String.prototype.format !== "function") {
    /* 字符串模板 */
    String.prototype.format = function () {
        var s = this, //字符串指针
            length = arguments.length; //参数长度
        while (--length >= 0){
            s = s.replace(new RegExp('\\{' + length + '\\}', 'g'), arguments[length]);
        }
        return s;
    };
}

var Phone = (function(global, doc){

    var _userAgent = global.navigator.userAgent,
        _isAndriod = /android/i.test(_userAgent),
        _isIOS     = /ip(ad|hone|od)/i.test(_userAgent),
        _isMobile  = /ip(ad|hone|od)|android/i.test(_userAgent);

    function MobileCallback(_params){

        var thi$    = this;

        thi$.params = _params || {};
        thi$.public = thi$.params.public || "ylpwMobile"; // 公共对象（此参数必须定义，Andriod对应名字，IOS对应加上（JavascriptHandler））
        thi$.method = thi$.params.method; // 调用方法
        thi$.param  = thi$.params.param;  // 回调参数

        if(_isAndriod){
            var AndriodTemplate = "window.{0}.{1}('{2}');"
                                    .format( thi$.public, thi$.method, thi$.param );
            log(AndriodTemplate);
            try {  // 兼容网页没有设置全局变量异常
                global.eval(AndriodTemplate);
            } catch (e) {}
        }
        if(_isIOS){
            var IOSTemplate = "bridge.callHandler('{0}{1}JavascriptHandler', '{2}', function(response) {  });"
                                .format( thi$.public, thi$.method, thi$.param );
            log(IOSTemplate);
            function connectWebViewJavascriptBridge(callback) {
                if (global.WebViewJavascriptBridge) {
                    callback(WebViewJavascriptBridge);
                } else {
                    doc.addEventListener('WebViewJavascriptBridgeReady', function() {callback(WebViewJavascriptBridge)}, false);
                }
            }
            connectWebViewJavascriptBridge(function(bridge) {
                global.eval(IOSTemplate);
            });
        }
    }

    // 公共方法
    var _method = {
        Callback   : function(method, param){
            MobileCallback({ method  : method, param   : param })
        }
    }

    // 全局定义
    if(_isMobile) { // 自动切换全局弹出为自定义传输弹出
        global.alert = function (msg) {
            _method.Callback("alert", msg);
        }
    }

    return function(_config){
        this.config = _config || {};

        return {
            // IOS、Android通用回调函数
            Callback  : _method.Callback
        }
    };

})(window, document);

