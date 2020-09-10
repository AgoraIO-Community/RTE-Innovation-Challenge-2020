module.exports = {
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: []
    }
  },
  devServer: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:9501',
        changeOrigin: true
      }
    },
  }
}
