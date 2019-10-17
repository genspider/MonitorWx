import {get, post, put} from '../utils/http'

var api = process.env.API3

//模板列表
export const templateList = p => get(api+'/template/list', p);
//模板下拉选typeSelect
export const tplSelect = p => get(api+'/template/tplSelect', p);
//模板添加
export const templateAdd= p => put(api+'/template/upload', p);
//模板删除
export const deleteTpl= p => get(api+'/template/deleteTpl', p);
//模板修改
export const updateTpl= p => get(api+'/template/updateTpl', p);
//标签删除
export const delTagsById= p => get(api+'/template/delTagsById', p);
//标签增加
export const addTags= p => get(api+'/template/addTags', p);
//标签修改
export const updateTags= p => get(api+'/template/updateTags', p);
//标签查询 id
export const searchTagsById= p => get(api+'/template/searchTagsById', p);
//标签查询 不分页
export const getAllTags= p => get(api+'/template/getAllTags', p);
//标签查询
export const searchTags= p => get(api+'/template/searchTags', p);
//ftp查询
export const ftpList= p => get(api+'/template/ftpList', p);
//ftp查询
export const addFtp= p => get(api+'/template/addFtp', p);
//ftp查询
export const deleteFtp= p => get(api+'/template/deleteFtp', p);
//ftp查询
export const updateFtp= p => get(api+'/template/updateFtp', p);
//落地页查询
export const ldyList= p => get(api+'/template/ldyList', p);
//落地页查询 id
export const ldySearchOne= p => get(api+'/template/ldySearchOne', p);
//落地页添加
export const ldyAdd= p => put(api+'/template/ldyAdd', p);
//落地页添加
export const ldyDel= p => get(api+'/template/ldyDel', p);