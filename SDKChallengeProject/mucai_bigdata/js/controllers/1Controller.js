var vm = new Vue({
    el: '#app',
    data() {
        this.lineDataZoom = false ? [{ startValue: '09.30' }, { type: 'slider', start: 0, end: 20 }] : [];
        this.lineExtend = { series: { label: { normal: { show: true } } } }
        this.chartLineSettings = { labelMap: { 'logs': '原木方量', 'broad': '板材方量' } }
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
            contract_data: { cy: "0", ly: "0", my: "0", ml: "0", qty: "0", volume: "0", amount: "0" },
            //折线图数据
            chartLine1Data: { columns: ['axisX', 'axisY'], rows: [] },
            //折线图数据
            chartLine2Data: { columns: ['axisX', 'logs', 'broad'], rows: [] },
            //饼图数据
            chartPieData: { columns: ['name', 'percent'], rows: [] }
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
                value.radius = '70%'; //大小
                value.center = ['50%', '45%']; //位置
                value.label = { //饼图图形上的文本标签
                        normal: {
                            show: true, //显示饼形图的线说明
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
            vm.Contract();
            vm.interval({ //定时任务
                Contract: { //每分钟刷新接口
                    exec: vm.Contract,
                    time: 1 * 1000 * 60
                }
            })
        },
        Contract: function() {
            var vm = this;
            indexModule.Contract({}, function(res) {
                // var result = {"code":1,"msg":"请求成功","data":{"cy":"215","ly":"564","my":"11","ml":"25","qty":"54569001","volume":"26709877.59","amount":"496306","cv":{"axisX":["2019-09","2019-10","2019-11","2019-12","2020-01","2020-02","2020-03","2020-04","2020-05","2020-06","2020-07","2020-08","2020-09"],"axisY":[45698,77082,170395,126155,49720,80113,139817,84015,104362,165523,116320,70829,63151]},"logs":{"axisX":["2019-10","2019-11","2019-12","2020-01","2020-02","2020-03","2020-04","2020-05","2020-06","2020-07","2020-08","2020-09"],"axisY":[[74793,157843,121565,46450,68492,139235,82938,100495,165523,105823,70829,61612],[2289,12552,4489,3270,11587,582,null,3867,0,10497,null,1540]]},"origin":{"新西兰":6170469,"加拿大":4193958,"美国":3599663,"所罗门群岛":1954133,"澳大利亚":1880758,"巴布亚新几内亚":1770876,"喀麦隆":1193930,"赤道几内亚共和国":746580,"乌克兰":544858,"其他":4654656}}}
                res = {
                    "cy": "216",
                    "ly": "564",
                    "my": "11",
                    "ml": "25",
                    "qty": "54569001",
                    "volume": "26709877.59",
                    "amount": "496306",
                    "cv": { "axisX": ["2019-09", "2019-10", "2019-11", "2019-12", "2020-01", "2020-02", "2020-03", "2020-04", "2020-05", "2020-06", "2020-07", "2020-08", "2020-09"], "axisY": [45698, 77082, 170395, 126155, 49720, 80113, 139817, 84015, 104362, 165523, 116320, 70829, 63151] },
                    "logs": {
                        "axisX": ["2019-10", "2019-11", "2019-12", "2020-01", "2020-02", "2020-03", "2020-04", "2020-05", "2020-06", "2020-07", "2020-08", "2020-09"],
                        "axisY": [
                            [74793, 157843, 121565, 46450, 68492, 139235, 82938, 100495, 165523, 105823, 70829, 61612],
                            [2289, 12552, 4489, 3270, 11587, 582, null, 3867, 0, 10497, null, 1540]
                        ]
                    },
                    "origin": { "新西兰": 6170469, "加拿大": 4193958, "美国": 3599663, "所罗门群岛": 1954133, "澳大利亚": 1880758, "巴布亚新几内亚": 1770876, "喀麦隆": 1193930, "赤道几内亚共和国": 746580, "乌克兰": 544858, "其他": 4654656 }
                }
                var contract_data = {}
                var lineResult1 = [];
                $.each(res, function(key, value) {
                    if (typeof value == 'string') {
                        contract_data[key] = String(parseInt(value));
                    }
                }); //格式化数字为字符串 

                vm.chartLine1Data.rows = col2row2(vm.line1Format(res.cv));
                vm.chartLine2Data.rows = col2row2(vm.line2Format(res.logs));
                vm.chartPieData.rows = vm.pieFormat(res.origin);

                vm.contract_data = contract_data;
            });
        },
        line1Format: function(array) {
            var result = [];
            $.each(array, function(key, value) {
                result.push({ field: key, data: value });
            });
            return result;
        },
        line2Format: function(array) {
            var result = [];
            array.logs = array.axisY[0];
            array.broad = array.axisY[1];
            delete array.axisY
            $.each(array, function(key, value) {
                result.push({ field: key, data: value });
            });
            return result;
        },
        pieFormat: function(array) {
            var result = [];
            $.each(array, function(key, value) {
                result.push({ name: key, percent: value });
            });
            return result;
        }
    },
    watch: {
        contract_data: function(val) { //渲染数字动画效果
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