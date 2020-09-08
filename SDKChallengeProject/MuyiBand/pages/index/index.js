//index.js
//获取应用实例
const { watch, computed } = require('../../utils/vuefy.js');
const RTMClient = require("../../utils/agora-rtm.js")
const { debounce } = require("../../utils/util.js")
const app = getApp()


Page({
  data: {
    openid:'',
    token: undefined,
    ident: "40A", //标识头，作为过滤体温手环的一个标识
    offset: 1, // 手和环境温度误差值
    physicals: [], //温度异常播报卡号ID集合
    physical: 0, //播报次数计算
    tableTips: "列表空", 
    searchFlag: false,
    devices: [], //搜索到的设备
    devicesAdvertis: [], //过滤后存放的设备
    connected: false,
    chs: [],
    interval: null,
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  onLoad: function () {
    var _this = this;
    this.getOpenid();
    this.rtm = new RTMClient()
    // sdk连接状态
    this.rtm.on('ConnectionStateChanged', (newState, reason) => {
      console.log('The connection status', newState)
      console.log('The reason for the state change', reason)
    })
    //rtm 赋值给全局
    app.globalData.agoraRtm = this.rtm
    watch(this, {//监听器
      physical: function (newVal) {
        console.log(newVal)
        if(_this.data.interval == null){
          var interval = setInterval(() => {
            if(_this.data.physical > 0){
              wx.playBackgroundAudio({
                dataUrl: app.globalData.WEBSITE_URL + '/tmp_error.mp3',
              });
            }
            _this.setData({physical : _this.data.physical -1 });
            if(_this.data.physical <= 0){
              clearInterval(_this.data.interval);
              _this.setData({interval : null});
            }
          }, 3000);
          _this.setData({interval : interval});
        }
      }
    })
  },
  getOpenid() {
    let that = this;
    wx.cloud.callFunction({
     name: 'getOpenid',
     complete: res => {
      console.log('云函数获取到的openid: ', res.result.openId)
      var openid = res.result.openId;
      that.setData({openid: openid })
      console.log( app.globalData.userInfo)
     }
    })
  },
  getUserInfo: function(e) {
    console.log(e)
    if (e.detail.userInfo){
      //用户按了允许授权按钮
      app.globalData.userInfo = e.detail.userInfo
      this.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      })
      if(this.rtm.isLogin) {
        console.log('already login')
        return
      }
      var time_id = new Date().getTime();
      this.rtm.login(this.data.token, this.data.openid).then(() => {
        console.log('login success')
        this.rtm.isLogin = true
        wx.navigateTo({
          url: `../message/message`
        });
      }).catch((err) => {
        console.log('login failed', err)
      })
    } else {
      //用户按了拒绝按钮
    }
   
  },
  navigateToStudent: function(e){ 
    var item = e.currentTarget.dataset.item;
    wx.navigateTo({ url: '/pages/student/student?cardId=' + item.cardNumber });
  },

  submitData: function(e){
    if(this.data.devicesAdvertis.length === 0) {
      wx.showToast({
        title: '列表为空，无法提交',
        icon: 'none',
        duration: 2000
      });
      return;
    }
    var physicalConditions = [];
    this.data.devicesAdvertis.forEach((v,i) => {
      var physicalCondition = {"cardId" : v.cardNumber, "temperature": v.handTemperature };
      physicalConditions.push(physicalCondition);
    });
    wx.request({
      url: app.globalData.WEBSITE_URL + '/app/screen/physical_condition/create_many',
      data: { "physicalConditions" : physicalConditions} ,
      method: 'POST',
      header: {'content-type': 'application/json'},
      success: function(res){
        console.log(res);
        wx.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 2000
        })
      },
      fail: function(res){
        console.log(res);
        wx.showToast({
          title: '提交失败，请联系相关开发人员',
          icon: 'none',
          duration: 2000
        });
      }
    });
  },
  startSearch: function() {
    var _this = this;
    app.checkSystemVersion(() => this.openBluetoothAdapter());
  },
  /******************蓝牙模块 */
  openBluetoothAdapter() {
    this.setData({searchFlag:true, tableTips: "开始搜索中..."});
    wx.openBluetoothAdapter({
      success: (res) => {
        console.log('openBluetoothAdapter success', res)
        this.startBluetoothDevicesDiscovery()
      },
      fail: (res) => {
        console.log(res)
        if (res.errCode === 10001) {
          wx.showModal({
            title: '温馨提示',
            content: '当前蓝牙适配器不可用,请打开系统蓝牙',
            showCancel:false,
          });
          this.setData({searchFlag:false, tableTips: "列表空"});
          wx.onBluetoothAdapterStateChange(res => {
            console.log('onBluetoothAdapterStateChange', res)
            if (res.available) {
              // this.startBluetoothDevicesDiscovery()
            }
          })
        }
      }
    })
  },
  getBluetoothAdapterState() {
    wx.getBluetoothAdapterState({
      success: (res) => {
        console.log('getBluetoothAdapterState', res)
        if (res.discovering) {
          this.onBluetoothDeviceFound()
        } else if (res.available) {
          this.startBluetoothDevicesDiscovery()
        }
      }
    })
  },
  startBluetoothDevicesDiscovery() {
    if (this._discoveryStarted) {
      return
    }
    this._discoveryStarted = true
    wx.startBluetoothDevicesDiscovery({
      powerLevel: "high",
      allowDuplicatesKey: true,
      success: (res) => {
        console.log('startBluetoothDevicesDiscovery success', res)
        this.onBluetoothDeviceFound()
      },
    })
  },
  stopBluetoothDevicesDiscovery() {
    this.setData({searchFlag:false, tableTips: "列表空"});
    wx.stopBluetoothDevicesDiscovery()
    this.closeBluetoothAdapter();
  },
  onBluetoothDeviceFound() {
    wx.onBluetoothDeviceFound((res) => {
      res.devices.forEach(device => {
        device.advertisDataString = app.buf2hex(device.advertisData);//二进制转字符串

        // device.advertisDataString = '107803E9002A0F02BC0117003F01233000080959543120324645';
        // var devicesAdvert = {//广播数据第一包解析，因安卓获取不到第一个包的数据因此废弃
        //   rawData: device.advertisDataString,
        //   deviceId: device.deviceId,
        //   name: device.name,
        //   rssi: device.RSSI,
        //   factoryID: device.advertisDataString.slice(0,8),
        //   time: parseInt(device.advertisDataString.slice(8,10),16) + ':' + parseInt(device.advertisDataString.slice(10,12), 16),
        //   stepCount: parseInt(device.advertisDataString.slice(14,16) + '' + device.advertisDataString.slice(12,14), 16),
        //   distance: parseInt(device.advertisDataString.slice(18,20) + '' + device.advertisDataString.slice(16,18), 16),
        //   calories: parseInt(device.advertisDataString.slice(22,24) + '' + device.advertisDataString.slice(20,22), 16),
        //   battery: parseInt(device.advertisDataString.slice(24,26), 16),
        //   sos: device.advertisDataString.slice(26,28),
        //   temperature: parseFloat(parseInt(device.advertisDataString.slice(28,30),16) + '.' + parseInt(device.advertisDataString.slice(30,32), 16)).toFixed(2) * 1,
        //   sleepingTime: device.advertisDataString.slice(32,34),
        //   fixedData: device.advertisDataString.slice(34,52)
        // }
        device.advertisDataString = '40A0FFFF000F647413210321513C000000000000000000000000';

        var devicesAdvert = {
          rawData: device.advertisDataString,
          deviceId: device.deviceId,
          name: device.name,
          localName: device.localName,
          rssi: device.RSSI,
          headerIdent: device.advertisDataString.slice(0,3),
          cardNumber: parseInt(device.advertisDataString.slice(8,16),16),
          mac: device.advertisDataString.slice(4,6)+ ":" + 
               device.advertisDataString.slice(6,8) + ":" + 
               device.advertisDataString.slice(8,10) + ":" + 
               device.advertisDataString.slice(10,12) + ":" + 
               device.advertisDataString.slice(12,14) + ":" + 
               device.advertisDataString.slice(14,16),
          handTemperature: parseFloat(parseInt(device.advertisDataString.slice(18,20),16) + '.' + parseInt(device.advertisDataString.slice(20,22), 16)).toFixed(1) * 1,
          envTemperature: parseInt(device.advertisDataString.slice(22,24),16),
          battery: parseInt(device.advertisDataString.slice(26,28),16),
          isUse: parseInt(device.advertisDataString.slice(30,32),16)
        }
        if (devicesAdvert.headerIdent != this.data.ident ) return;//如果不是40A标识头的过滤掉
        if((devicesAdvert.handTemperature - devicesAdvert.envTemperature) <= this.data.offset && (devicesAdvert.envTemperature - devicesAdvert.handTemperature) <= this.data.offset) devicesAdvert.isUse = 2;//如果手温和环境温度相差小于0.5度说明没戴手上
        if(devicesAdvert.isUse == 0 ){//如果佩戴了再进行修正
          devicesAdvert.handTemperature = app.temparatureFix(devicesAdvert.handTemperature, devicesAdvert.envTemperature);//手温修正
        } 
        const foundDevices = this.data.devices
        const physicals = this.data.physicals
        const idx = app.inArray(foundDevices, 'deviceId', device.deviceId)//是否已经存在该设备
        const pidx = app.inArray(physicals, 'deviceId', device.deviceId)//是否已经播报过的id
        const data = {}
        if (idx === -1) {
          data[`devices[${foundDevices.length}]`] = device;
          data[`devicesAdvertis[${foundDevices.length}]`] = devicesAdvert;
          if(devicesAdvert.handTemperature >= 37 ){//如果有体温大于37的加入语音播报队列
            data[`physicals[${physicals.length}]`] = {deviceId : device.deviceId };//是否已经播报过的id，播报过的会被录入该数组
            this.setData({physical: this.data.physical + 1 });//增加一次播报
          }
        } else {
          data[`devices[${idx}]`] = device;
          data[`devicesAdvertis[${idx}]`] = devicesAdvert;
          if(devicesAdvert.handTemperature >= 37 ){//如果有体温大于37的加入语音播报队列
            if (pidx === -1) { //如果当前设备前面有没有播报过语音则播报，否则不播报
              data[`physicals[${physicals.length}]`] = {deviceId : device.deviceId };//是否已经播报过的id，播报过的会被录入该数组
              this.setData({physical: this.data.physical + 1 });//增加一次播报
            }
          }
        }
        this.setData(data);
        console.log(data);
      })
    })
  },
  createBLEConnection(e) {
    const ds = e.currentTarget.dataset
    const deviceId = ds.deviceId
    const name = ds.name
    wx.createBLEConnection({
      deviceId,
      success: (res) => {
        this.setData({
          connected: true,
          name,
          deviceId,
        })
        this.getBLEDeviceServices(deviceId)
      },
      fail: (e) => {
        console.log(e)
      }
    })
    this.stopBluetoothDevicesDiscovery()
  },
  closeBLEConnection() {
    wx.closeBLEConnection({
      deviceId: this.data.deviceId
    })
    this.setData({
      connected: false,
      chs: [],
      canWrite: false,
    })
  },
  getBLEDeviceServices(deviceId) {
    console.log(deviceId);
    wx.getBLEDeviceServices({
      deviceId,
      success: (res) => {
        for (let i = 0; i < res.services.length; i++) {
          if (res.services[i].isPrimary) {
            this.getBLEDeviceCharacteristics(deviceId, res.services[i].uuid)
            return
          }
        }
      },
      fail: (e) => {
        console.log(e)
      }
    })
  },
  getBLEDeviceCharacteristics(deviceId, serviceId) {
    wx.getBLEDeviceCharacteristics({
      deviceId,
      serviceId,
      success: (res) => {
        console.log('getBLEDeviceCharacteristics success', res.characteristics)
        for (let i = 0; i < res.characteristics.length; i++) {
          let item = res.characteristics[i]
          if (item.properties.read) {
            wx.readBLECharacteristicValue({
              deviceId,
              serviceId,
              characteristicId: item.uuid,
            })
          }
          if (item.properties.write) {
            this.setData({
              canWrite: true
            })
            this._deviceId = deviceId
            this._serviceId = serviceId
            this._characteristicId = item.uuid
            this.writeBLECharacteristicValue()
          }
          if (item.properties.notify || item.properties.indicate) {
            wx.notifyBLECharacteristicValueChange({
              deviceId,
              serviceId,
              characteristicId: item.uuid,
              state: true,
            })
          }
        }
      },
      fail(res) {
        console.error('getBLEDeviceCharacteristics', res)
      }
    })
    // 操作之前先监听，保证第一时间获取数据
    wx.onBLECharacteristicValueChange((characteristic) => {
      const idx = app.inArray(this.data.chs, 'uuid', characteristic.characteristicId)
      const data = {}
      if (idx === -1) {
        data[`chs[${this.data.chs.length}]`] = {
          uuid: characteristic.characteristicId,
          value: ab2hex(characteristic.value)
        }
      } else {
        data[`chs[${idx}]`] = {
          uuid: characteristic.characteristicId,
          value: ab2hex(characteristic.value)
        }
      }
      // data[`chs[${this.data.chs.length}]`] = {
      //   uuid: characteristic.characteristicId,
      //   value: ab2hex(characteristic.value)
      // }
      this.setData(data)
    })
  },
  writeBLECharacteristicValue() {
    // 向蓝牙设备发送一个0x00的16进制数据
    let buffer = new ArrayBuffer(1)
    let dataView = new DataView(buffer)
    dataView.setUint8(0, Math.random() * 255 | 0)
    wx.writeBLECharacteristicValue({
      deviceId: this._deviceId,
      serviceId: this._deviceId,
      characteristicId: this._characteristicId,
      value: buffer,
    })
  },
  closeBluetoothAdapter() {
    wx.closeBluetoothAdapter()
    this._discoveryStarted = false
  },
})
