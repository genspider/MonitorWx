import {resetParmas} from "../func/index"
const Mock = require("mockjs")
/**
 * 进程
 * @param req
 * @param res
 * @returns {*}
 */
function jincheng(req,res) {
    var _req = resetParmas(req)
    console.log(_req)
    var i = 0
    var _list = []
    for (i; i < 10; i++) {
        _list.push(
            Mock.mock({
                "%CPU":'@name',
                "%MEM":'@email',
                "COMMAND":/^1[385][1-9]\d{8}/,
                "PID":'@cname()',
                "RSS":1,
                "START":'@name',
                "STAT":'R',
                "TIME":/^1[385][1-9]\d{8}/,
                "TTY":'@cname()',
                "USER":1,
                "VSZ":'@name',
            })
        );
    }
    var result = {}
    result['data'] = _list;
    result['error'] = 0
    result['msg'] = "查询成功"
    return result;
}
//
function searchLog(req,res) {
    var _req = resetParmas(req)
    console.log(_req)
    var result = {}
    result['data'] = "11111111111111111111111111111111111111111111111111";
    result['error'] = 0
    result['msg'] = "查询成功"
    return result;
}
function queryLog(req,res) {
    var _req = resetParmas(req)
    console.log(_req)
    var result = {}
    result['data'] = "11111111111111111111111111111111111111111111111111";
    result['error'] = 0
    result['msg'] = "查询成功"
    return result;
}
function configList(req,res) {
    var _req = resetParmas(req)
    var obj ;
    if(_req.type == "url"){
        obj = [{
            value: '错误日志',
            label: '错误日志'
        }, {
            value: '选项2',
            label: '双皮奶'
        }, {
            value: '选项3',
            label: '蚵仔煎'
        }, {
            value: '选项4',
            label: '龙须面'
        }, {
            value: '选项5',
            label: '北京烤鸭'
        }]
    }
    if(_req.type == "implement"){
        obj = [{
            value: '商户管理平台',
            label: '商户管理平台'
        }, {
            value: '选项2',
            label: '双皮奶'
        }, {
            value: '选项3',
            label: '蚵仔煎'
        }, {
            value: '选项4',
            label: '龙须面'
        }, {
            value: '北京烤鸭',
            label: '北京烤鸭'
        }]
    }
    if(_req.type == "ip"){
        obj = [{
            value: '所有项目',
            label: 'all'
        }, {
            value: '综合管理平台',
            label: 'sys'
        }, {
            value: '店铺后台',
            label: 'back'
        }]
    }
    console.log(obj)
    var result = {}
    result['data'] = obj;
    result['error'] = 0
    result['msg'] = "查询成功"
    return result;
}
export {jincheng,searchLog,queryLog,configList}