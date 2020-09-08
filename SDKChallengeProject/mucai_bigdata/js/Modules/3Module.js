var indexModule = (function(mod) {
    mod.Vehicle = function(post, callback) { //车辆统计
        callback();
        // sw_ajax("POST","Chart/Vehicle", post, function(res){
        // 	return callback(res.data);
        // });
    }
    mod.AnalysisInout = function(post, callback) { //出入库分析(折线图表)
        callback();
        // sw_ajax("POST","Chart/AnalysisInout", post, function(res){
        // 	return callback(res.data);
        // });
    }
    mod.PnameInout = function(post, callback) { //出入库分析(饼形图表)
        callback();
        // sw_ajax("POST","Chart/PnameInout", post, function(res){
        // 	return callback(res.data);
        // });
    }

    return mod;
})(window.indexModule || {});