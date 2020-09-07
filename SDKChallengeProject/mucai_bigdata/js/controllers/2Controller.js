var vm = new Vue({
    el: '#app',
    data() {
        this.lineExtend = {
            series: {
                label: {
                    normal: {
                        show: true
                    }
                }
            }
        }
        this.chartLineSettings = {
            labelMap: {
                'axisY': '绿色',
                'red': '红色'
            },
            min: [0.7],
            max: [1.2]
        }
        this.chartPieSettings = {
            dimension: 'name', //维度
            metrics: 'percent' //指标
        }

        this.chartMapTitle = { //标题样式
            text: '',
            left: 'center',
            textStyle: {
                color: 'white',
                fontSize: 16
            }
        }
        this.chartMapSettings = {
            position: 'china',
            dimension: 'name',
            metrics: ['name'],
            selectData: true,
            roam: true,
            selectedMode: 'multiple',
        }
        this.chartMapEvents = {
            click: (v) => {
                this.cityName = v.name
            }
        }

        return {
            mapWidth: document.body.clientWidth * 0.38,
            mapHeight: document.body.clientWidth * 0.38 * 0.9,
            dateTime: null,
            cityName: '',
            acc_volum: { day_in: "0", day_out: "0", day_stock: "0.0", year_in: "0", year_out: "0" },
            mapData: {},
            chartMapData: {
                columns: ['name', 'total'],
                rows: []
            },
            chartLineData: { //进库分析折线数据
                columns: ['axisX', 'axisY', '仓储趋势分析', 'red'],
                rows: []
            },
            chartPieData: {
                columns: ['name', 'percent'],
                rows: []
            },
            Member_num: null,
            all_percent: 0
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
            var visualMap = options.visualMap;
            options.tooltip = { //图形上的鼠标悬浮标签
                trigger: "item",
                formatter: '{b}'
            }
            legend.show = false; //显示红色，绿色标签
            legend.type = 'scroll';
            legend.data = ["绿色", "红色"];
            legend.bottom = '10px'; //放底部
            legend.textStyle = { color: '#FFF' }; //方块按钮文字颜色
            xAxis.forEach(function(value) {
                value.axisLine = { lineStyle: { color: '#FFF' } };
            });
            yAxis.forEach(function(value) {
                value.type = 'value';
                value.axisLine = { lineStyle: { color: '#FFF' } };
                value.interval = 0.1;
            });

            visualMap = {
                show: false,
                dimension: 1,
                type: 'piecewise',
                pieces: [{
                    gte: 0.9, //大于0.9
                    color: 'red' //显示红色
                }, {
                    gte: 0,
                    lte: 0.9, //小于0.9
                    color: 'green' //显示绿色
                }]
            }
            options.visualMap = visualMap;
            return options;
        },
        afterPieConfig(options) {
            var legend = options.legend; //维度指标调整
            var series = options.series; //对象调整  圆饼

            //			legend.show = false;
            legend.type = 'scroll';
            legend.bottom = '10px';
            legend.textStyle = { color: '#FFF' } //方块按钮文字颜色

            series.forEach(function(value) {
                value.name = '用户数据';
                value.radius = '60%'; //大小
                value.center = ['50%', '50%']; //位置
                value.label = { //饼图图形上的文本标签
                        normal: {
                            show: true,
                            //                      position:'inner', //标签的位置 inner饼内
                            formatter: '{d}%',
                            textStyle: {
                                fontWeight: 300,
                                fontSize: 16 //文字的字体大小
                            }
                        }
                    },
                    value.tooltip = { //饼图图形上的鼠标悬浮标签
                        formatter: '{b}-{d}%',
                    }
            });

            options.legend = legend;
            options.series = series;
            return options;
        },
        afterMapConfig: function(options) {
            var legend = options.legend; //维度指标调整
            var series = options.series; //维度指标调整

            legend.show = false;
            legend.type = 'scroll';
            legend.textStyle = { color: '#FFF' } //方块按钮文字颜色

            series.forEach(function(value) {
                value.itemStyle = { //背景颜色等样式
                        normal: { //未选中状态
                            //	                    borderWidth:2,//边框大小
                            //	                    borderColor:'lightgreen',//边框颜色
                            areaColor: '#bbbbbb', //多边形块状区域颜色
                            label: { //标签文字
                                show: true, //显示名称
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        },
                        emphasis: { // 选中样式
                            borderWidth: 1,
                            borderColor: '#fff', //边框颜色
                            areaColor: 'red', //多边形块状区域颜色
                            label: { //标签文字
                                show: true,
                                textStyle: {
                                    color: '#fff'
                                }
                            }
                        }
                    },
                    value.tooltip = { //饼图图形上的鼠标悬浮标签
                        formatter: '{b}',
                    }
            });

            options.legend = legend;
            return options;
        },
        interval: function(options) {
            $.each(options, function(key, value) {
                setInterval(value.exec, value.time);
            });
        },
        initData: function() {
            this.AccVolum();
            this.WarehouseDist();
            this.GetUser();
            this.StorageTrend();
            this.interval({ //定时任务
                refreshDateTime: { //每秒刷新时间
                    exec: this.refreshDateTime,
                    time: 1000
                },
                WarehouseDist: { //每分钟刷新接口
                    exec: this.WarehouseDist,
                    time: 60 * 1000 * 60 * 12
                },
                AccVolum: { //每分钟刷新接口
                    exec: this.AccVolum,
                    time: 20 * 1000
                },
                GetUser: { //每分钟刷新接口
                    exec: this.GetUser,
                    time: 60 * 1000 * 60 * 12
                },
                StorageTrend: { //每分钟刷新接口
                    exec: this.StorageTrend,
                    time: 60 * 1000 * 60 * 12
                }
            })
        },
        refreshDateTime: function() {
            this.dateTime = Date.now();
        },
        WarehouseDist: function() {
            var vm = this;
            index2Module.WarehouseDist({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"list":[{"name":"上海","total":"1"},{"name":"四川省","total":"4"},{"name":"天津","total":"2"},{"name":"山东省","total":"5"},{"name":"广东省","total":"4"},{"name":"江苏省","total":"24"},{"name":"江西省","total":"1"},{"name":"福建省","total":"1"}],"title":"覆盖8个省市，50个仓储网点","total_city":8,"total_dot":50,"total_days":1254,"total_system":18}}
                res = { "list": [{ "name": "上海", "total": "1" }, { "name": "四川省", "total": "4" }, { "name": "天津", "total": "2" }, { "name": "山东省", "total": "5" }, { "name": "广东省", "total": "4" }, { "name": "江苏省", "total": "24" }, { "name": "江西省", "total": "1" }, { "name": "福建省", "total": "1" }], "title": "覆盖8个省市，50个仓储网点", "total_city": 8, "total_dot": 50, "total_days": 1254, "total_system": 18 }
                $.each(res.list, function(key, value) {
                    value.name = String(value.name).replace("省", "");
                })
                vm.chartMapData.rows = res.list;
                vm.mapData = res;
                //				vm.chartMapTitle.text = res.title;
            });
        },
        GetUser: function() {
            var vm = this;
            index2Module.GetUser({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":[{"title":"仓储公司","percent":27},{"title":"货主公司","percent":1081},{"title":"开证公司","percent":157},{"title":"司机","percent":14377},{"title":"仓库","percent":49}]}
                res = [{ "title": "仓储公司", "percent": 27 }, { "title": "货主公司", "percent": 1081 }, { "title": "开证公司", "percent": 157 }, { "title": "司机", "percent": 14377 }, { "title": "仓库", "percent": 49 }]
                for (var value of res) {
                    vm.all_percent += value.percent;
                }
                vm.Member_num = res;
                var result = [];
                $.each(res, function(key, value) {
                    result.push({ name: value.title, percent: value.percent });
                });
                vm.chartPieData.rows = result;
                console.log(vm.chartPieData.rows)
            });
        },
        AccVolum: function() {
            var vm = this;
            index2Module.AccVolum({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"day_in":"3628.3751","day_out":"10261.8954","day_stock":"1055379","year_in":"3249389","year_out":"2961862"}}
                res = { "day_in": "3668.3751", "day_out": "10261.8954", "day_stock": "1055379", "year_in": "3249389", "year_out": "2961862" }
                $.each(res, function(key, value) {
                    res[key] = String(parseInt(value));
                })
                vm.acc_volum = res;
            });
        },
        StorageTrend: function() {
            var vm = this;
            index2Module.StorageTrend({}, function(res) {
                // var mock_result = {"code":1,"msg":"请求成功","data":{"axisX":["2020-08-21","2020-08-22","2020-08-23","2020-08-24","2020-08-25","2020-08-26","2020-08-27","2020-08-28","2020-08-29","2020-08-30","2020-08-31","2020-09-01","2020-09-02","2020-09-03","2020-09-04"],"axisY":[1.01,1.01,1,0.98999999999999999,0.97999999999999998,0.97999999999999998,0.95999999999999996,0.94999999999999996,0.94999999999999996,0.93999999999999995,0.93000000000000005,0.92000000000000004,0.91000000000000003,0.91000000000000003,0.91000000000000003]}}
                res = { "axisX": ["2020-08-21", "2020-08-22", "2020-08-23", "2020-08-24", "2020-08-25", "2020-08-26", "2020-08-27", "2020-08-28", "2020-08-29", "2020-08-30", "2020-08-31", "2020-09-01", "2020-09-02", "2020-09-03", "2020-09-04"], "axisY": [1.01, 1.01, 1, 0.98999999999999999, 0.97999999999999998, 0.97999999999999998, 0.95999999999999996, 0.94999999999999996, 0.94999999999999996, 0.93999999999999995, 0.93000000000000005, 0.92000000000000004, 0.91000000000000003, 0.91000000000000003, 0.91000000000000003] }
                var result = [];
                $.each(res['axisX'], function(key, value) {
                    res['axisX'][key] = vm.$options.filters.formatDate(value, "MM.dd");
                });
                $.each(res, function(key, value) {
                    result.push({ field: key, data: value });
                });

                vm.chartLineData.rows = col2row2(result);
                console.log(vm.chartLineData.rows)
            });
        },
    },
    filters: {
        animateNumbak: function(value, e) { //暂时作废
            console.log(value)
            console.log(e)
            obj.animate({
                backgroundPosition: '(0 ' + String(-parseInt(value) * 58) + 'px)'
            }, 'slow', 'swing', function() {});
        },
        userInfoProportion: function(value, all_count) {
            var vm = this;
            if (!value) return '';
            value = parseInt(value);
            return ((value / all_count) * 100).toFixed(2);
        }
    },
    watch: {
        acc_volum: function(val) {
            var vm = this;
            setTimeout(function() {
                $.each(val, function(key, value) {
                    $.each(vm.$refs[String(key)], function(index, item) { //渲染数字动画效果
                        var num = String(value).charAt(index);
                        var y = -parseInt(num) * 58;
                        $(item).animate({ backgroundPosition: '(0 ' + String(y) + 'px)' }, 'slow', 'swing', function() {});
                    });
                });
            }, 500);

        }
    }
});