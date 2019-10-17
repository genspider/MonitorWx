'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',
    MOCK: 'true',
    API1:'"http://report.yunhou.com"',
    API2:'"http://10.12.25.59:9090"',
   // API3:'"http://www.local.yaf.com/index.php"',
    API3:'"http://wx.zcjian.cn/index.php"',
})
