import {get,post} from '../utils/http'
//登录
var api = process.env.API1
export const login = p => post(api+'/api/member/login', p);