//app.js
App({
  globalData: {
    userInfo: null,
    sysinfo: null,
    WEBSITE_URL: ""
  },
  onLaunch: function () {
    wx.cloud.init({env: 'prod-qh6bh',   traceUser: true});
    this.globalData.sysinfo = wx.getSystemInfoSync();
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //   }
    // })
    // // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           // 可以将 res 发送给后台解码出 unionId
    //           this.globalData.userInfo = res.userInfo

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (this.userInfoReadyCallback) {
    //             this.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }
    //   }
    // })
  },
  getModel: function () { //获取手机型号
      return this.globalData.sysinfo["model"]
  },
  getVersion: function () { //获取微信版本号
      return this.globalData.sysinfo["version"]
  },
  getSystem: function () { //获取操作系统版本
      return this.globalData.sysinfo["system"]
  },
  getPlatform: function () { //获取客户端平台
      return this.globalData.sysinfo["platform"]
  },
  getSDKVersion: function () { //获取客户端基础库版本
      return this.globalData.sysinfo["SDKVersion"]
  },
  getPreMonth: function (date) {   //获取上一个月日期
    // console.log(date)
    var year = date.getFullYear(); //获取当前日期的年份  
    var month = date.getMonth() + 1; //获取当前日期的月份  
    var day = date.getDate(); //获取当前日期的日  
    var days = new Date(year, month, 0);  
    days = days.getDate(); //获取当前日期中月的天数  
    var year2 = year;  
    var month2 = parseInt(month) - 1;  
    if (month2 == 0) {  
        year2 = parseInt(year2) - 1;  
        month2 = 12;  
    }  
    var day2 = day;  
    var days2 = new Date(year2, month2, 0);  
    days2 = days2.getDate();  
    if (day2 > days2) {  
        day2 = days2;  
    }  
    if (month2 < 10) {  
        month2 = '0' + month2;  
    }  
    var t2 = year2 + '-' + month2 + '-' + day2;  
    return t2;  
  },
  versionCompare: function (ver1, ver2) { //版本比较
    var version1pre = parseFloat(ver1)
    var version2pre = parseFloat(ver2)
    var version1next = parseInt(ver1.replace(version1pre + ".", ""))
    var version2next = parseInt(ver2.replace(version2pre + ".", ""))
    if (version1pre > version2pre)
        return true
    else if (version1pre < version2pre) 
        return false
    else {
        if (version1next > version2next)
            return true
        else
            return false
    }
  },
  checkSystemVersion: function(callback) {
    //判断系统版本、微信版本、定位服务等权限和信息
      //Android 从微信 6.5.7 开始支持，iOS 从微信 6.5.6 开始支持
      //第一项，如果手机是android系统，需要判断版本信息
      if (this.getPlatform() == "android" && this.versionCompare("6.5.7", this.getVersion())) {
        wx.showModal({
          title: '提示',
          content: '当前微信版本过低，请更新至最新版本',
          showCancel: false
        });
        return;
      }
      //第二项，如果手机是ios系统，需要判断版本信息
      if (this.getPlatform() == "ios" && this.versionCompare("6.5.6", this.getVersion())) {
        wx.showModal({
          title: '提示',
          content: '当前微信版本过低，请更新至最新版本',
          showCancel: false
        });
        return;
      }
      //版本  以及  平台校验完毕后   需要判断蓝牙的相关信息
      //微信小程序 android6.0手机需要开启位置服务，否则扫描不到设备
      console.log("当前系统版本：" + this.getSystem());//Android 8.1.0
      console.log("当前微信版本：" + this.getVersion());
      if (this.getPlatform() == "android") {
        console.log("android手机  当前系统版本号：" + this.getSystem().replace("Android", "").replace(" ", ""));
        //android版本高于6.0.0
        if (!this.versionCompare("6.0.0", this.getSystem().replace("Android","").replace(" ",""))) {
          console.log("当前系统版本高于6.0.0");
          //位置服务权限
          // wx.getSetting({
          //   success: function (res) {
          //     var statu = res.authSetting;
          //     //位置服务授权校验操作
          //     if (!statu['scope.userLocation']) {
          //       wx.showModal({
          //         title: '温馨提示',
          //         content: '请授予位置服务权限，以便更好的搜索周围设备',
          //         success: function (tip) {
          //           if (tip.confirm) {
          //             //点击确认  开始判断位置服务权限信息
          //             wx.openSetting({
          //               success: function (data) {
          //                 if (data.authSetting["scope.userLocation"] === true) {
          //                   wx.showToast({
          //                     title: '授权成功',
          //                     icon: 'success',
          //                     duration: 1000
          //                   })
          //                   //授权成功之后，调用自己封装的蓝牙各项操作
          //                   this.openBluetoothAdapter();
    
          //                 } else {
          //                   wx.showToast({
          //                     title: '授权失败',
          //                     icon: 'none',
          //                     duration: 1000
          //                   });
          //                 }
          //               }
          //             })
          //           }else{
          //             console.log("点击了取消操作");
          //           }
          //         }
          //       })
          //     }else {
          //       //存在权限，调用封装的蓝牙方式继续进行 
          //       this.openBluetoothAdapter();
          //     }
          //   },
          //   fail: function (res) {
          //     wx.showToast({
          //       title: '调用授权窗口失败',
          //       icon: 'success',
          //       duration: 1000
          //     });
          //   }
          // })
          callback();
          return;
        } else if (!this.versionCompare(this.getSystem().replace("Android", "").replace(" ", "")), "4.3.0") {
          //系统版本低于4.3的,使用不了ble蓝牙
          wx.showModal({
            title: '温馨提示',
            content: '您的手机系统版本较低，无法操作BLE蓝牙设备',
            showCancel:false,
          });
          return;
        }else {
          console.log("系统版本低于6.0.0但高于4.3.0");
          //除去android系统的手机(由于最开始过滤了能支持蓝牙的最低微信版本   所以此处无需再判断)
          callback();
          return;
        }
      }
    //除android以外的ios或者其他系统
    callback();
  },
  buf2hex: function (buffer) {
    return Array.prototype.map.call(new Uint8Array(buffer), x => ('00' + x.toString(16)).slice(-2)).join('').toUpperCase();
  },
  inArray: function(arr, key, val) {
    for (let i = 0; i < arr.length; i++) {
      if (arr[i][key] === val) {
        return i;
      }
    }
    return -1;
  },
  temparatureFix: function (hand,env) {//温度修正
    var a1 = 778, b1 = -485449, c1 = 111202587, a2 = 0, b2 = -10403, c2 = 3266904;
    return (( c1 + b1 * hand * 10 + a1 * hand * hand * 10 * 10 ) / 1000000 + ( c2 + b2 * env * 10) / 1000000) . toFixed(1);
  }
})



/**
app.json
"list": [{
  "pagePath": "pages/index/index",
  "iconPath": "image/icon_component.png",
  "selectedIconPath": "image/icon_component_HL.png",
  "text": "查询"
}, {
  "pagePath": "pages/info/info",
  "iconPath": "image/tabbar_icon_setting_default.png",
  "selectedIconPath": "image/tabbar_icon_setting_active.png",
  "text": "个人信息"
}]

 */