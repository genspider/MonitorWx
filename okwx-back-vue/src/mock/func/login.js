import {resetParmas} from "../func/index"

/**
 * 登录
 * @param req
 * @param res
 * @returns {*}
 */
function login(req,res) {
        var _req = resetParmas(req)
        if(_req.cellphone != 1){
            return {
                "error": 500,
                "data": null,
                "msg": "密码错误"
            }
        }
        return {
            "error": 0,
            "data": {
                "token":111,
                "power":'admin'
            },
            "msg": "登录成功"
        }
}
export {login}