import {login} from "../mock/func/login"
const Mock = require("mockjs")

Mock.mock('http://report.yunhou.com/api/member/login?token=111', "post", login)
