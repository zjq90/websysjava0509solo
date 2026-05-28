const path = require('path')

module.exports = {
  transpileDependencies: [],
  devServer: {
    allowedHosts: 'all'
  },
  chainWebpack: config => {
    config.module
      .rule('vue')
      .use('fix-recyclableRender')
      .loader(path.resolve(__dirname, 'webpack-fix-recyclableRender.js'))
      .after('@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/templateLoader.js')
  }
}
