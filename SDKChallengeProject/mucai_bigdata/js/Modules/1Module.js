var indexModule = (function(mod) {
    mod.Contract = function(post, callback) { //采购大数据
        callback();
        // sw_ajax("POST","Chart/Contract", post, function(res){
        // 	return callback(res.data);
        // });
    }
    return mod;
})(window.indexModule || {});