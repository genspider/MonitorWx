import {get, post, put} from '../utils/http'

var api = process.env.API3

//账号列表
export const userList = p => get(api+'/okuser/userList', p);
//增加账号
export const addUser = p => get(api+'/okuser/addUser', p);
//粉丝列表
export const firendList = p => get(api+'/okuser/firendList', p);
//粉丝列表
export const firendAll = p => get(api+'/okuser/firendAll', p);
//分组
export const groups = p => get(api+'/okuser/groups', p);