import {resetParmas} from "../func/index"
const Mock = require("mockjs")

/***
 * 权限
 * @param req
 */
function powerModifyState(req) {
    var _req = resetParmas(req)

}
function powerList(req) {
    var _req = resetParmas(req)

}
function powerAdd(req) {
    var _req = resetParmas(req)

}
function adminModifyState(req) {
    var _req = resetParmas(req)

}
function adminList(req) {
    var _req = resetParmas(req)
    console.log(_req)
    var currentPage = _req.currentPage
    var pageSize = _req.pageSize
    var name = _req.name
    var i = 0
    var _list = []
    var _data = {}
    if(name == "1"){
        var len =96
    }else{
        var len =50
    }
    //var len = (100 - pageSize * (currentPage - 1)) < pageSize ? (100 - pageSize * (currentPage - 1)) : pageSize;
    for (i; i < len; i++) {
        _list.push(
            Mock.mock({
                "id": (currentPage - 1) * pageSize + (i + 1),
                "cellphone":'@name',
                "mailbox":'@email',
                "phone":/^1[385][1-9]\d{8}/,
                "rolename":'@cname()',
                "timeType":1,
            })
        );
    }
    _data['count'] = _list.length
    _data['currentPage'] = Number(currentPage)
    _data['list'] = _list.slice((currentPage-1)*pageSize,currentPage*pageSize)
    _data['pageSize'] = Number(pageSize)
    _data['pageSum'] = _list.length/Number(pageSize)
    var result = {}
    result['data'] = _data;
    result['error'] = 0
    result['msg'] = "查询成功"
    console.log(result)
    return result;

}
function adminAdd(req) {
    console.log(req)
    var _req = resetParmas(req)
    console.log(_req)
    var result = {
        'data':null,
        'error':0,
        'msg':'修改成功'
    }
    return result
}

export {powerAdd,powerList,powerModifyState,adminAdd,adminList,adminModifyState}