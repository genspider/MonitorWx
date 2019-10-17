import {get,post} from '../utils/http'

var api = process.env.API1
//修改配置
export const updateConfig = p => post(api+'/api/confing/add', p);
//修改配置
export const deleteConfig = p => post(api+'/api/confing/modify_state', p);
