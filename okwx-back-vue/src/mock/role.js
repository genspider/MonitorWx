const Mock = require("mockjs")
import {powerModifyState,powerList,powerAdd,adminModifyState,adminList,adminAdd} from '../mock/func/role'
Mock.mock('http://report.yunhou.com/api/role/modify_state?token=111', "post", {
    "error": 0,
    "data": {
        "token":123,
        "power":'admin'
    },
    "msg": "登录成功"
})
Mock.mock('http://report.yunhou.com/api/role/list?token=111', "post", {
    "error": 0,
    "data": {
        "token":123,
        "power":'admin'
    },
    "msg": "登录成功"
})
Mock.mock('http://report.yunhou.com/api/role/add?token=111', "post", {
    "error": 0,
    "data": {
        "token":123,
        "power":'admin'
    },
    "msg": "登录成功"
})
Mock.mock('http://report.yunhou.com/api/admin/modify_state?token=111', "post", {
    "error": 0,
    "data": {
        "token":123,
        "power":'admin'
    },
    "msg": "登录成功"
})
//管理员 查询功能
Mock.mock('http://report.yunhou.com/api/admin/list', "post", adminList)
//管理员 增加 修改功能
Mock.mock('http://report.yunhou.com/api/admin/add', "post",adminAdd)