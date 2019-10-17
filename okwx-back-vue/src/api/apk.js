import {get,post} from '../utils/http'

var api = process.env.API3
//修改配置
export const insert = p => get(api+'/apk/insert', p);
export const list = p => get(api+'/apk/list', p);
export const update = p => get(api+'/apk/update', p);
export const findone = p => get(api+'/apk/findone', p);