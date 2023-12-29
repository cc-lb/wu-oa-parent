// 解决内网穿透，添加该文件
module.exports = {
  devServer: {
    /*
    proxy: {
      '/api': { // 匹配所有以 '/dev-api'开头的请求路径
        target: 'http://localhost:8800',//将浏览器中请求的路径（axios中的路径）改为target中的路径
        changeOrigin: true, // 支持跨域
        pathRewrite: { // 重写路径: 去掉路径中开头的'/dev-api'
          '^/api': '/'
        }
      }
    },
    */
    proxy: {
      '/api': { // 匹配所有以 '/dev-api'开头的请求路径
        target: 'http://localhost:8800',//将浏览器中请求的路径（axios中的路径）改为target中的路径
        changeOrigin: true, // 支持跨域
        pathRewrite: { // 重写路径: 去掉路径中开头的'/dev-api'
          '^/api': '/'
        }
      }
    },
    host: '0.0.0.0',
    port: 9090,
    hot: true,
    disableHostCheck: true,
     
  }
  

}
