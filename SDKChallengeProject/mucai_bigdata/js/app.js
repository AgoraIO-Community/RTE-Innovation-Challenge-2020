//Ajax关闭异步和缓存
//$.ajaxSetup({async: false});// $().ready( function(){ $.ajaxSetup({ cache:false,async:false }); } ); 
//动画效果继承到jQuery中
$.fn.extend({
    animateCss: function(animationName, callback) {
        var _this = this;
        var animationEnd = (function(el) {
            var animations = {
                animation: 'animationend',
                OAnimation: 'oAnimationEnd',
                MozAnimation: 'mozAnimationEnd',
                WebkitAnimation: 'webkitAnimationEnd',
            };

            for (var t in animations) {
                if (el.style[t] !== undefined) {
                    return animations[t];
                }
            }
        })(document.createElement('div'));
        this.addClass('animated ' + animationName).one(animationEnd, function() {
            $(_this).removeClass('animated ' + animationName);
            if (typeof callback === 'function') callback();
        });
        return this;
    }
});

//设常量值
var DEBUG = true;
//var website = location.protocol + "//"+location.hostname
//if(location.port) website += ":"+location.port;
var website = '';
var apiurl = website + '/api';
var galleryurl = website + '/gallery';
var apikey = "ff238a2833cc473f18c49fdb3c26180e";

//浏览器兼容,判断当前浏览器版本
var browser = {
    versions: function() {
        var u = navigator.userAgent,
            app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
            iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            weixin: u.indexOf('MicroMessenger') > -1, //是否微信 （2015-01-22新增）
            qq: u.match(/\sQQ/i) == " qq" //是否QQ
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
}

//VUE时间格式化过滤器 yyyy-MM-dd/hh：mm：ss/pp/ww 2018-01-01-12/12：00：00/PM/周一
Vue.filter('formatDate', function(value, fmt) {
    fmt = fmt || "yyyy-MM-dd";
    var date = new Date(value);
    var weeks = ['日', '一', '二', '三', '四', '五', '六'];

    function padLeftZero(key, str) {
        switch (key) {
            case 'w+':
                return '周' + str;
            case 'p+':
                return str;
            default:
                return ('00' + str).substring(str.length);
        }
    }

    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    //  let o = {
    //      'M+': (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1),
    //      'd+': date.getDate() < 10 ? '0' + date.getDate() : date.getDate(),
    //      'w+': getWeek(date.getDay()),
    //      'h+': date.getHours() < 10 ? '0' + date.getHours() : date.getHours(),
    //      'm+': date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes(),
    //      's+': date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
    //  };
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'w+': weeks[date.getDay()],
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds(),
        'p+': date.getHours() > 11 ? 'PM' : 'AM'
    };

    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + '';
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(k, str));
        }
    }
    return fmt;
})

//VUE数值千分位过滤
Vue.filter('formatThousand', function(value, fmt) {
    if (!value) return '0.00';
    value = value.toString();
    value = value.replace(/,/g, "");
    fmt = fmt || "0,0";
    return numeral(value).format(fmt);
})

//获取浏览器参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
    var context = "";
    if (r != null) {
        context = r[2];
    }
    reg = null;
    r = null;
    return context == null || context == "" || context == "undefined" ? "" : context;
}

//打印Ajax请求Debug参数
function print_request_log(method, url, data, callback) {
    var request_log = {};
    request_log.method = method;
    request_log.url = url;
    request_log.param = data;
    request_log.callback = callback.toString();
    console.log(request_log);
}

//Ajax请求
function sw_ajax(method, url, data, callback) {
    //	data.apikey = apikey;
    DEBUG && print_request_log(method, apiurl + url, data, callback);
    switch (method) {
        case "POST":
            $.post(apiurl + url, data, function(res) {
                var res = $.parseJSON(res);
                if (res.code == 1) {
                    callback(res);
                } else {

                }
            });
            break;
        case "GET":
            $.get(apiurl + url, data, function(res) {
                var res = $.parseJSON(res);
                if (res.code == 1) {
                    callback(res);
                } else {

                }
            });
            break;
        default:
            $.post(apiurl + url, data, function(res) {
                var res = $.parseJSON(res);
                if (res.code == 1) {
                    callback(res);
                } else {

                }
            });
            break;
    }
}

//转JSON对象
function parseQuery(str) {
    if (str == null)
        str = location.search;
    var i, all, key, obj = {};
    if (str.tagName == 'FORM') {
        var all = str.elements;
        for (i = 0; i < all.length; i++) {
            var el = all[i];
            if (!el.name) continue;
            key = el.type.toLowerCase();
            if ((key == "radio" || key == "checkbox") &&
                !el.checked) continue;
            if (str.realValue && typeof el.realValue != 'undefined')
                obj[el.name] = el.realValue;
            else
                obj[el.name] = el.value;
        }
    } else {
        all = str.split("&");
        for (i in all) {
            key = all[i].split("=");
            obj[key[0]] = key[1];
        }
    }
    return obj;
}