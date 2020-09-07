const UglifyJsPlugin = require('uglifyjs-webpack-plugin')
module.exports = {
  devServer: {
    port: 80,
    disableHostCheck: true // That solved it
  },
  configureWebpack: config => {
    // 入口文件
    // config.entry.app = ['babel-polyfill', './src/main.js']
    // 删除console插件
    const plugins = [
      new UglifyJsPlugin({
        uglifyOptions: {
          compress: {
            warnings: false,
            drop_console: true,
            drop_debugger: true
          },
          output: {
            // 去掉注释内容
            comments: false
          }
        },
        sourceMap: false,
        parallel: true
      })
    ]
    // 只有打包生产环境才需要将console删除
    // if (process.env.VUE_APP_build_type == 'production') {
    config.plugins = [...config.plugins, ...plugins]
    // }
  }
}
