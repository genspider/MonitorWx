import {get, post, put} from '../utils/http'

var api = process.env.API3

//监听
export const monitorList = p => get(api+'/monitor/list', p);
//所有的数据
export const monitorAll = p => get(api+'/monitor/all', p);
