import {get,post} from '../utils/http'
//登录
var api = process.env.API1
//搜索 进程
export const jincheng = p => post(api+'/api/server/index', p);
//搜索 得到key
export const searchLog = p => post(api+'/api/navigation/journal', p);
//通过key + ip 搜索日志
export const queryLog = p => post(api+'/api/navigation/query', p);
//搜索select下拉选
export const configList = p => post(api+'/api/confing/list', p);
//错误日志
export const get_journal = p => post(api+'/api/api_journal/get_journal', p);
//api信息
export const journal_nature = p => post(api+'/api/api_journal/journal_nature', p);
//性能查询
export const pyinfoCnfig = p => post(api+'/api/server/pyinfoConfig', p);
//redis 信息
export const redisConfig = p => post(api+'/api/server/redisConfig', p);