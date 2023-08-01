const webpack = require("webpack");

module.exports = {
    publicPath: './',
    outputDir: 'dist',
    assetsDir: 'assets',
    lintOnSave: false,
    devServer: {
        open: true,
        port: 9011,
        https: false,
        hotOnly: false,
    },
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $: 'jquery',
                jQuery: 'jquery',
                'window.jQuery': 'jquery'
            })
        ]
    },
   // 配置 axios 代理请求
   devServer: {
    proxy: {
        '/api': {
            // 本地服务器
            target: 'http://127.0.0.1:8003',

            // 测试服务器
            // target: 'https://test-wms.foodsup.com/api',
            
            changeOrigin: true,
            ws: true,
            pathRewrite: {
                '/api': '',
            },
        },
    }
} 
};
