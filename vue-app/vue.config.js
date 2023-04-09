const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    host: '0.0.0.0',
    port: 8081,  // 前端
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
})
