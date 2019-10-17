import {get, post, put} from '../utils/http'

var api = process.env.API3

//增加微信号
export const insertPower = p => get(api+'/power/insert', p);
//微信号 列表
export const listPower = p => get(api+'/power/list', p);
//微信号 删除
export const deletePower = p => get(api+'/power/delete', p);
//上传二维码图片
export const uploadQr = p => put(api+'/okuser/img', p);
//域名 列表
export const listDomain = p => get(api+'/power/searchDomain',p);
//域名 增加
export const insertDomain = p => get(api+'/power/insertDomain',p);
//域名 删除
export const delDomain = p => get(api+'/power/delDomain',p);
//域名 下拉选
export const selectDomain = p => get(api+'/power/domainSelect',p);