import {list} from "../mock/func/list"
const Mock = require("mockjs")

Mock.mock('http://report.yunhou.com/api/navigation/list', "get",list)
