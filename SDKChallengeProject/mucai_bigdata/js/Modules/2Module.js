var index2Module = (function(mod) {
    mod.AccVolum = function(post, callback) {
        callback();
        // sw_ajax("GET","/Chart/AccVolum", post, function(res){
        // 	return callback(res.data);
        // });
    }
    mod.GetUser = function(post, callback) {
        callback();
        // sw_ajax("POST", "/Chart/User", post, function(res) {
        //     return callback(res.data);
        // });
    }
    mod.WarehouseDist = function(post, callback) {
        callback();
        // sw_ajax("POST", "/Chart/WarehouseDist", post, function(res) {
        //     return callback(res.data);
        // });
    }
    mod.StorageTrend = function(post, callback) {
        callback();
        // sw_ajax("POST", "/Chart/StorageTrend", post, function(res) {
        //     return callback(res.data);
        // });
    }

    return mod;
})(window.index2Module || {});