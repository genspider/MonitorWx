// 将所有的mock文件引入
require('../mock/login')
require('../mock/role')
require('../mock/list')
require('../mock/data-report')

// 在这里可以做一些通用的配置
const Mock = require("mockjs")
// 设置所有ajax请求的超时时间，模拟网络传输耗时
Mock.setup({
    timeout: 1000
})
