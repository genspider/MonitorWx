// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
/* jshint esversion: 6 */
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/scss/font-awesome.scss'
import echarts from 'echarts'
import '../src/assets/css/index.scss'
import { Message } from 'element-ui';
import {saveObj,removeObj,getObj} from '@/utils/json'
//process.env.MOCK && require('../src/mock')  打开mock数据
Vue.prototype.$echarts = echarts

Vue.use(ElementUI)

Vue.prototype.router = router;

Vue.prototype.API1 = process.env.API1;

Vue.prototype.API2 = process.env.API2;

Vue.config.productionTip = false
import '@/vendor/Export2Excel.js'
import '@/vendor/Blob.js'
import axios from "axios";
import {router_list} from '@/config/routerlist'

var list
router.beforeEach((to, from, next) => {
    //设置title
    window.document.title = to.meta.title;
    var user = getObj('user')
    if(!user && to.path !== '/login'&& to.path !== '/regist'){
        removeObj("list")
        Message({
            showClose: true,
            message: '请先登录',
            type: 'error'
        });
        window.document.title = '登录';
        next({
            path: '/login'
        })
    }else{
        if (!list) {//不加这个判断，路由会陷入死循环
            if (!getObj('list')) {
                list = router_list;
                    saveObj('list', list) //存储路由到localStorage
                    routerGo(to, next)//执行路由跳转方法
            } else {//从localStorage拿到了路由
                list = getObj('list')//拿到路由
                routerGo(to, next)
            }
        } else {
            next();
        }
    }
})
function routerGo(to, next) {
    global.antRouter = list //将路由数据传递给全局变量，做侧边栏菜单渲染工作
    next({ ...to, replace: true })
}

new Vue({
  el: '#app',
  router,
  render:h=>h(App),
})
