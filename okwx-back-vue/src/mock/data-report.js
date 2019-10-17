import {jincheng,searchLog,queryLog,configList} from "../mock/func/data-report"
const Mock = require("mockjs")

Mock.mock('http://report.yunhou.com/api/server/index', "post",jincheng)

Mock.mock('http://report.yunhou.com/api/navigation/journal', "post",searchLog)

Mock.mock('http://report.yunhou.com/api/navigation/query', "post",queryLog)

Mock.mock('http://report.yunhou.com/api/confing/list', "post",configList)