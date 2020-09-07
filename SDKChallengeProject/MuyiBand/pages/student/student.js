var wxCharts = require('../wxcharts/wxcharts.js');
var app = getApp();
var lineChart = null;
Page({
    data: {
        cardId: "",
        startTime: "",
        endTime: ""
    },
    touchHandler: function (e) {
      lineChart.scrollStart(e);
    },
    moveHandler: function (e) {
        lineChart.scroll(e);
    },
    touchEndHandler: function (e) {
        lineChart.scrollEnd(e);
        lineChart.showToolTip(e, {
            format: function (item, category) {
                return category + ' ' + item.name + ':' + item.data 
            }
        });        
    },
    createSimulationData: function (mode) {
        var _this = this;
        var currentDate = new Date();
        var categories = [];
        var data = [];
        var getCondition = function(url,callback) {
            wx.request({
                url: app.globalData.WEBSITE_URL + url,
                data: { 
                    "cardId" : _this.data.cardId, 
                    "startTime" : _this.data.startTime,
                    "endTime": _this.data.endTime
                } ,
                method: 'POST',
                header: {'content-type': 'application/json'},
                success: function(res){
                  console.log(res);
                  if(res.data.msg == "SUCCESS"){
                    callback(res.data);
                  }else{
                    wx.showToast({
                        title: res.data.msg,
                        icon: 'none',
                        duration: 2000
                    });
                  }
                  
                },
                fail: function(res){
                  console.log(res);
                  wx.showToast({
                    title: '查询失败，请联系相关开发人员',
                    icon: 'none',
                    duration: 2000
                  });
                }
              });
        }
        if(mode == 'today' | mode == null){
            for (var i = 8; i <= 18; i++) {
                categories.push(i+":00");
            } 
            _this.setData({
                startTime: new Date(new Date().toLocaleDateString()).getTime() + 8 * 60 * 60 * 1000 + "", 
                endTime: new Date(new Date().toLocaleDateString()).getTime() + 18 * 60 * 60 * 1000 + ""
            })
            var res = {list:[{createDate:1590045009868 , temperature : 23}]}
                categories.forEach((v)=>{
                    res.list.some(item => {
                        new Date(item.createDate).getHours() == v.split(':')[0]
                    }) 
                    // if( ){
                    //     data.push(23);
                    // }else{
                    //     data.push(null);
                    // }
                })
                console.log(data)
            // getCondition('/app/screen/physical_condition/find',function(res){
            //     categories.forEach((v)=>{
            //         if( res.list.some(item => new Date(item.createDate).getHours() === v.split(':')[0]) ){
            //             data.push(item.temperature);
            //         }else{
            //             data.push('');
            //         }
            //     })
            // })
        }else if(mode == 'week') {
            var startTimeTmp = new Date(new Date(currentDate.toLocaleDateString()).getTime() - 6 * 24 * 60 * 60 * 1000);
            while(startTimeTmp.getTime() < currentDate.getTime()){
                categories.push('周' + '日一二三四五六'.charAt(startTimeTmp.getDay()) + "(" + startTimeTmp.getDate() + ")");
                // categories.push('周' + '日一二三四五六'.charAt(startTimeTmp.getDay()) + "(" + (startTimeTmp.getMonth() + 1) + "/" + startTimeTmp.getDate() + ")");
                startTimeTmp = new Date(startTimeTmp.setDate(startTimeTmp.getDate() + 1 ));
            }
            console.log(categories);
            _this.setData({
                startTime: new Date(currentDate.toLocaleDateString()).getTime() - 6 * 24 * 60 * 60 * 1000 + "", 
                endTime: currentDate.getTime() + ""
            })
            getCondition('/app/screen/physical_condition_data/find',function(res){
                res.list.forEach((v,i) => data.push(v.temperature));
            })
        }else if(mode == 'month'){
            var startTimeTmp = new Date(app.getPreMonth(currentDate));
            while(startTimeTmp.getTime() < currentDate.getTime()){
                categories.push(startTimeTmp.getMonth() + 1 + "-" + startTimeTmp.getDate());
                startTimeTmp = new Date(startTimeTmp.setDate(startTimeTmp.getDate() + 1 ));
            }
            console.log(categories);
            _this.setData({
                startTime: new Date(app.getPreMonth(currentDate)).getTime() + "", 
                endTime: currentDate.getTime() + ""
            })
            getCondition('/app/screen/physical_condition_data/find',function(res){
                var resData = [];
                res.list.forEach((v,i) => {
                    var startTimeTmp = new Date(app.getPreMonth(currentDate));
                    while(startTimeTmp.getTime() < currentDate.getTime()){



                        categories.push(startTimeTmp.getMonth() + 1 + "-" + startTimeTmp.getDate());
                        startTimeTmp = new Date(startTimeTmp.setDate(startTimeTmp.getDate() + 1 ));
                    }
                });
                data.push(v.temperature);
                data = resData;
            })
        }


        // if(mode == 'day'){
        //   for (var i = 0; i < 24; i++) {
        //     categories.push(i);
        //     data.push(Math.random()*(42-32+1)+32);
        //   } 
        // }else if(mode == 'week'){
        //   for (var i = 0; i < 7; i++) {
        //     categories.push(i+1);
        //     data.push(Math.random()*(42-32+1)+32);
        //   }
        // }else {
        //   for (var i = 0; i < 30; i++) {
        //     categories.push(i + 1);
        //     data.push(Math.random()*(42-32+1)+32);
        //   }
        // }
        // data[4] = null;
        return {
            categories: categories,
            data: data
        }
    },
    updateData: function (e) {
        var simulationData = this.createSimulationData(e.currentTarget.dataset.item);
        var series = [{
            name: '体温',
            data: simulationData.data,
            format: function (val, name) {
                return val.toFixed(2) + '°C';
            }
        }];
        lineChart.updateData({
            categories: simulationData.categories,
            series: series
        });
    },
    onLoad: function (options) {
        this.setData({
            cardId: options.cardId, 
            startTime: new Date(new Date().toLocaleDateString()).getTime() + 8 * 60 * 60 * 1000 + "", 
            endTime: new Date(new Date().toLocaleDateString()).getTime() + 18 * 60 * 60 * 1000 + ""
        });
        
        console.log(this.data)
        var windowWidth = 320;
        try {
            var res = wx.getSystemInfoSync();
            windowWidth = res.windowWidth;
        } catch (e) {
            console.error('getSystemInfoSync failed!');
        }
        
        var simulationData = this.createSimulationData("today");
        lineChart = new wxCharts({
            canvasId: 'lineCanvas',
            type: 'line',
            categories: simulationData.categories,
            animation: true,
            enableScroll: true,
            // background: '#f5f5f5',
            series: [{
                name: '体温',
                data: simulationData.data,
                format: function (val, name) {
                    return val.toFixed(2) + '°C';
                }
            }],
            xAxis: {
                disableGrid: true,
                //gridColor //String 例如#7cb5ec default #cccccc //X轴网格颜色
                //fontColor //String 例如#7cb5ec default #666666 X轴数据点颜色
                //disableGrid //Boolean default false 不绘制X轴网格
                //type //String 可选值calibration(刻度) 默认为包含样式
            },
            yAxis: {
                title: '体温曲线图(°C)',
                format: function (val) { // 自定义Y轴文案显示
                    return val.toFixed(0);
                },
                max: 40, //Y轴起始值
                min: 35 //Y轴终止值
                //gridColor //String 例如#7cb5ec default #cccccc //X轴网格颜色
                //fontColor //String 例如#7cb5ec default #666666 X轴数据点颜色
                //disableGrid //Boolean default false 不绘制X轴网格
                //type //String 可选值calibration(刻度) 默认为包含样式
            },
            width: windowWidth,
            height: 200,
            dataLabel: true,//是否在图表中显示数据内容值
            dataPointShape: true,//是否在图表中显示数据点图形标识
            extra: {
                lineStyle: 'curve'
            }
        });
    }
});