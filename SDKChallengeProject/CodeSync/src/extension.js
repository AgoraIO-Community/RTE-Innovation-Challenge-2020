const vscode = require('vscode');

/**
 * 插件被激活时触发，所有代码总入口
 * @param {*} context 插件上下文
 */
exports.activate = function(context) {
    console.log('codesync已被激活');
    console.log(vscode);
    require('./codesync')(context);
};

exports.deactivate = function() {
    console.log('codesync已被释放')
};