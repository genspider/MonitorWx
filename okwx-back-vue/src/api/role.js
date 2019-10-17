import {get,post} from '../utils/http'
var api = process.env.API1
/***
 * 权限
 * @param p
 * @returns {Promise}
 */
//修改
export const powerModifyState = p => post(api+'/api/role/modify_state', p);
//查询
export const powerList = p => post(api+'/api/role/list', p);
//添加
export const powerAdd = p => post(api+'/api/role/add', p);
/***
 * 管理员
 * @param p
 * @returns {Promise}
 */
//修改
export const adminModifyState = p => post(api+'/api/admin/modify_state', p);
//查询
export const adminList = p => post(api+'/api/admin/list', p);
//添加
export const adminAdd = p => post(api+'/api/admin/add', p);