
import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
   // mode: 'history',
  //  base: '/home/',
    routes: [
      {
            path: '/',
            redirect: '/index',
            name: 'index',
            component: (resolve) => require(['@/view/index/index'], resolve),
            meta: { title: '主页', icon: 'guide', noCache: true},
            children: [
                {
                    path: 'index',
                    component:(resolve) => require(['@/view/index/HelloWorld'], resolve),
                    name: '首页',
                    meta: {title: '主页'},
                    top:true,
                    id:1,
                },
                {
                    path: 'config',
                    component:(resolve) => require(['@/view/config/index'], resolve),
                    redirect: '/config/facIndex',
                    name: '用户账号',
                    top:true,
                    id:1,
                    meta: {icon: 'el-icon-setting', noCache: true},
                    children: [
                        {
                            path: 'facIndex',
                            component: (resolve) => require(['@/view/config/false-alarm-config/config'], resolve),
                            name: '账号列表',
                            id:11,
                            meta: { icon: 'el-icon-bell',title: '账号列表', noCache: true },

                        },

                    ],
                },
                {
                    path: 'monitor',
                    component: (resolve) => require(['@/view/monitor/index'], resolve),
                    redirect: '/monitor/copy',
                    name: '粉丝',
                    top: true,
                    id: 1,
                    meta: {icon: 'el-icon-view', noCache: true},
                    children: [
                        {
                            path: 'copy',
                            component: (resolve) => require(['@/view/monitor/copy'], resolve),
                            name: '粉丝列表',
                            meta: { title: '粉丝列表', noCache: true },
                            id:31,
                        },
                        {
                            path: 'all',
                            component: (resolve) => require(['@/view/monitor/all'], resolve),
                            name: '粉丝量统计',
                            meta: { title: '粉丝量统计', noCache: true },
                            id:31,
                        },
                    ]
                },
                {
                    path: 'apk',
                    component: (resolve) => require(['@/view/apk/index'], resolve),
                    redirect: '/apk/config',
                    name: 'apk',
                    top: true,
                    id: 1,
                    meta: {icon: 'el-icon-upload', noCache: true},
                    children: [
                        {
                            path: 'config',
                            component: (resolve) => require(['@/view/apk/config'], resolve),
                            name: 'apk配置',
                            meta: { title: 'apk配置', noCache: true },
                            id:31,
                        },
                    ]
                },
            ]
        },
      {
          path: '/login',
          name: 'login',
          component: (resolve) => require(['@/view/login/login'], resolve),
          meta: { title: '登录', },
          hidden:true
      },
      {
          path: '/notfound',
          name: 'notfound',
          component: (resolve) => require(['@/components/404'], resolve),
      },
      {
          path:'*',
          hidden:true,
          redirect:'/notfound'
      }
  ]

})
