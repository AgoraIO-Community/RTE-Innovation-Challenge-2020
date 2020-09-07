var vm = new Vue({
    el: '#app',
    data() {
        this.lineDataZoom = false ? [{ startValue: '09.30' }, { type: 'slider', start: 0, end: 20 }] : [];
        this.lineExtend = { series: { label: { normal: { show: true } } } }
        this.chartLineSettings = { labelMap: { 'axisY': '方量' } }
        this.chartPieColors = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
        this.chartPieSettings = {
                title: '',
                dimension: 'name',
                metrics: 'percent' //抬头,维度,指标
            }
            //		this.dataZoom: [{
            //          startValue: '2014-06-01'
            //      	}, {
            //          type: 'inside'
            //      }],
        return {
            vehicle_data: { onway_truck: "0", onway_volume: "0", truck: "0", y_truck: "0", y_volume: "0" },
            //进库分析饼图数据
            chartLine1Data: { columns: ['axisX', 'axisY'], rows: [] },
            //出库分析饼图数据
            chartLine2Data: { columns: ['axisX', 'axisY'], rows: [] },
            //进库分析饼图数据
            chartPie1Data: { columns: ['name', 'percent'], rows: [] },
            //出库分析饼图数据
            chartPie2Data: { columns: ['name', 'percent'], rows: [] }
        }
    },
    created: function() {
        var vm = this;
        vm.initData();
    },
    methods: {
        content_company() {
            var iWidth = 720; //弹出窗口的宽度; 
            var iHeight = 600; //弹出窗口的高度; 
            //获得窗口的垂直位置 
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
            //获得窗口的水平位置 
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
            window.open("content.html", "联系客服", `height=800, width=500, top=${iTop}, left=${iLeft}, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no`)
        },
        afterLineConfig(options) {
            console.log(options)
            var legend = options.legend; //维度指标调整
            var series = options.series; //对象调整  折线图
            var xAxis = options.xAxis; //圆饼调整
            var yAxis = options.yAxis; //圆饼调整

            legend.show = false; //隐藏标签lebel
            legend.type = 'scroll';
            legend.textStyle = { color: '#FFF' }; //方块按钮文字颜色
            xAxis.forEach(function(value) { value.axisLine = { lineStyle: { color: '#FFF' } }; });
            yAxis.forEach(function(value) { value.axisLine = { lineStyle: { color: '#FFF' } }; });

            options.legend = legend;
            return options;
        },
        afterPieConfig(options) {
            var legend = options.legend; //维度指标调整
            var series = options.series; //对象调整  圆饼

            legend.show = false;
            legend.type = 'scroll';
            legend.textStyle = { color: '#FFF' } //方块按钮文字颜色

            series.forEach(function(value) {
                value.name = '用户数据';
                value.radius = '75%'; //大小
                value.center = ['50%', '55%']; //位置
                value.label = { //饼图图形上的文本标签
                        normal: {
                            show: true,
                            //                      position:'inner', //标签的位置 inner饼内
                            formatter: '{b}-{d}%',
                            //文字的字体大小
                            textStyle: { fontWeight: 300, fontSize: 16 }
                        }
                    }
                    //饼图图形上的鼠标悬浮标签
                value.tooltip = { formatter: '{b}-{d}%' }
            });

            options.legend = legend;
            options.series = series;
            return options;
        },
        interval: function(options) {
            $.each(options, function(key, value) {
                setInterval(value.exec, value.time);
            });
        },
        initData: function() {
            var vm = this;
            vm.AnalysisInout();
            vm.PnameInout();
            vm.Vehicle();
            vm.interval({ //定时任务
                AnalysisInout: { //每分钟刷新接口 出入库折线图数据
                    exec: vm.AnalysisInout,
                    time: 60 * 1000 * 60
                },
                PnameInout: { //每分钟刷新接口 出入库饼形图数据
                    exec: vm.PnameInout,
                    time: 60 * 1000 * 60
                },
                Vehicle: { //每分钟刷新接口 车辆和在途信息
                    exec: vm.Vehicle,
                    time: 20 * 1000
                }
            })
        },
        Vehicle: function() {
            var vm = this;
            indexModule.Vehicle({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"truck":39805,"onway_truck":416,"onway_volume":14237,"y_truck":141767,"y_volume":6229554}}
                res = { "truck": 39805, "onway_truck": 416, "onway_volume": 14237, "y_truck": 141767, "y_volume": 6229554 }
                $.each(res, function(key, value) {
                    res[key] = String(parseInt(value));
                })
                vm.vehicle_data = res;
            });
        },
        AnalysisInout: function() {
            var vm = this;
            indexModule.AnalysisInout({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"in":{"axisX":["2020-08-21","2020-08-22","2020-08-23","2020-08-24","2020-08-25","2020-08-26","2020-08-27","2020-08-28","2020-08-29","2020-08-30","2020-08-31","2020-09-01","2020-09-02","2020-09-03","2020-09-04"],"axisY":[8770,24921,9564,8946,7849,9223,7688,12631,8162,8759,7477,5863,8454,9118,9539]},"out":{"axisX":["2020-08-21","2020-08-22","2020-08-23","2020-08-24","2020-08-25","2020-08-26","2020-08-27","2020-08-28","2020-08-29","2020-08-30","2020-08-31","2020-09-01","2020-09-02","2020-09-03","2020-09-04"],"axisY":[8959,9317,7605,9325,9457,8840,8649,11495,8200,8522,10808,11360,9897,10688,10744]}}}
                res = { "in": { "axisX": ["2020-08-21", "2020-08-22", "2020-08-23", "2020-08-24", "2020-08-25", "2020-08-26", "2020-08-27", "2020-08-28", "2020-08-29", "2020-08-30", "2020-08-31", "2020-09-01", "2020-09-02", "2020-09-03", "2020-09-04"], "axisY": [8770, 24921, 9564, 8946, 7849, 9223, 7688, 12631, 8162, 8759, 7477, 5863, 8454, 9118, 9539] }, "out": { "axisX": ["2020-08-21", "2020-08-22", "2020-08-23", "2020-08-24", "2020-08-25", "2020-08-26", "2020-08-27", "2020-08-28", "2020-08-29", "2020-08-30", "2020-08-31", "2020-09-01", "2020-09-02", "2020-09-03", "2020-09-04"], "axisY": [8959, 9317, 7605, 9325, 9457, 8840, 8649, 11495, 8200, 8522, 10808, 11360, 9897, 10688, 10744] } }
                $.each(res, function(key, value) {
                    var result = [];
                    console.log(value)
                    $.each(value['axisX'], function(index, val) {
                        value['axisX'][index] = vm.$options.filters.formatDate(val, "MM.dd");
                    });
                    $.each(value, function(index, val) {
                        result.push({ field: index, data: val });
                    });
                    res[key] = col2row2(result);
                });
                vm.chartLine1Data.rows = res.out;
                vm.chartLine2Data.rows = res.in;
            });
        },
        PnameInout: function() {
            var vm = this;
            indexModule.PnameInout({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"in":[{"name":"云杉","percent":657728},{"name":"樟子松","percent":288165},{"name":"加松","percent":132888},{"name":"SPF","percent":119936},{"name":"其他","percent":542021}],"out":[{"name":"云杉","percent":575151},{"name":"樟子松","percent":222912},{"name":"加松","percent":127929},{"name":"SPF","percent":100896},{"name":"其他","percent":446820}]}}
                res = { "in": [{ "name": "云杉", "percent": 657728 }, { "name": "樟子松", "percent": 288165 }, { "name": "加松", "percent": 132888 }, { "name": "SPF", "percent": 119936 }, { "name": "其他", "percent": 542021 }], "out": [{ "name": "云杉", "percent": 575151 }, { "name": "樟子松", "percent": 222912 }, { "name": "加松", "percent": 127929 }, { "name": "SPF", "percent": 100896 }, { "name": "其他", "percent": 446820 }] }
                vm.chartPie1Data.rows = res.out;
                vm.chartPie2Data.rows = res.in;
            });
        },
    },
    watch: {
        vehicle_data: function(val) { //渲染数字动画效果
            var vm = this;
            setTimeout(function() {
                $.each(val, function(key, value) {
                    $.each(vm.$refs[String(key)], function(index, item) {
                        var num = String(value).charAt(index);
                        var y = -parseInt(num) * 58;
                        $(item).animate({ backgroundPosition: '(0 ' + String(y) + 'px)' }, 'slow', 'swing', function() {});
                    });
                });
            }, 500);
        }
    }
});