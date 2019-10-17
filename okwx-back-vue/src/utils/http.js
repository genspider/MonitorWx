import axios from 'axios';
import { Message } from 'element-ui';
import Vue from 'vue'
import QS from 'qs';
import router from '../router'
import {saveObj,getObj,removeObj} from '../utils/json'

/***
 * 封装 axios 拦截器
 * @type {number}
 */
axios.defaults.timeout = 240000;
//axios.defaults.baseURL ='http://report.yunhou.com';

// post请求头
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

let loading;
function startLoading() {
    loading = Vue.prototype.$loading({
        lock: true,
        text: "加载中",
        target: document.querySelector('.loading-area')//设置加载动画区域
    });
}
function endLoading() {
    loading.close();
}

//声明一个对象用于存储请求个数
let needLoadingRequestCount = 0;
function showFullScreenLoading() {
    if (needLoadingRequestCount === 0) {
        startLoading();
    }
    needLoadingRequestCount++;
};
function tryHideFullScreenLoading() {
    if (needLoadingRequestCount <= 0) return;
    needLoadingRequestCount--;
    if (needLoadingRequestCount === 0) {
        endLoading();
    }
};

//  请求拦截
axios.interceptors.request.use(config => {
    //showFullScreenLoading();
    const token = getObj('token');

    if(token){
       // config.params = {'token':token}
      /*  config.headers = {
            'token':token
        }*/
    }
    return config;
}, err => {
    //tryHideFullScreenLoading();
    Message({
        showClose: true,
        message: "请求超时",
        type: 'error'
    });
    return Promise.reject(err);
})

//http response 拦截器


axios.interceptors.response.use(
    response => {

        //console.log(router.history.current.name);
        console.log(response.data)

     //   if(response!=null){
    //        tryHideFullScreenLoading()
      //  }
        //没有传过来数据时
            switch (response.data.code) {
                /*case 0:
                  break;*/
                case 401:
                    Message({
                        showClose: true,
                        message: "登录失效，请重新登录",
                        type: 'error'
                    });
                    removeObj('token')
                    removeObj('user')
                    removeObj('power')
                    router.push('login')
                    break;
                /*default:
                    Message({
                        showClose: true,
                        message: response.data.msg,
                        type: 'error'
                    });
                    break;*/
            }

       // tryHideFullScreenLoading();
        console.log(response.data)
        return response.data;

    },
    error => {
        Message({
            showClose: true,
            message: '网络异常，请稍后重试',
            type: 'error'
        });
    //    tryHideFullScreenLoading()
        if (error && error.response) {
            console.log(error.response.status)
        }
        return Promise.reject(error)
    }
)





/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function get(url,params={}){
    return new Promise((resolve,reject) => {
        console.log(url)
        axios.get(url,{
            params:params
        })
            .then((response) => {
                resolve(response);
            })
            .catch((error) =>{
                reject(error)
            })
    })
}


/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post(url,data = {}){
    return new Promise((resolve,reject) => {
        axios.post(url,QS.stringify(data))
            .then((response) => {
                resolve(response);
            }).catch((error) => {
                reject(error)
            }
        )
    })
}
export function put(url,data = {}){
    return new Promise((resolve,reject) => {
        let config = {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        };
        axios.post(url,data,config)
            .then((response) => {
                resolve(response);
            }).catch((error) => {
                reject(error)
            }
        )
    })
}
