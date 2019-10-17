import {resetParmas} from "../func/index"

/**
 * 登录
 * @param req
 * @param res
 * @returns {*}
 */
function list(req,res) {

    return {
        "error": 0,
        "data":[
            {
                path: 'config',
                component:(resolve) => require(['@/view/config/index'], resolve),
                redirect: '/config/facIndex',
                name: '配置',
                top:true,
                id:1,
                meta: {icon: 'el-icon-setting', noCache: true},
                children: [
                    {
                        path: 'facIndex',
                        component: (resolve) => require(['@/view/config/false-alarm-config/index'], resolve),
                        name: '错误报警配置',
                        hidden:true,
                        id:11,
                        meta: { title: '错误报警配置', noCache: true }
                    },
                    {
                        path: 'redis-alarm-config',
                        component: (resolve) => require(['@/view/config/redis-alarm-config/index'], resolve),
                        name: 'Redis报警配置',
                        meta: {icon: 'el-icon-bell', noCache: true },
                        redirect: '/config/redis-alarm-config/healthStatus',
                        hidden:true,
                        id:12,
                        children: [
                            {
                                path: 'healthStatus',
                                component: (resolve) => require(['@/view/config/redis-alarm-config/healthStatus'], resolve),
                                name: '健康情况',
                                meta: { title: '健康情况', noCache: true},
                                id:121,
                            },
                            {
                                path: 'linkNumber',
                                component: (resolve) => require(['@/view/config/redis-alarm-config/linkNumber'], resolve),
                                name: '链接个数',
                                meta: { title: '链接个数', noCache: true},
                                id:122,
                            },
                            {
                                path: 'internalStorage',
                                component: (resolve) => require(['@/view/config/redis-alarm-config/internalStorage'], resolve),
                                name: '内存',
                                meta: { title: '内存', noCache: true},
                                id:123,
                            },
                            {
                                path: 'cpu',
                                component: (resolve) => require(['@/view/config/redis-alarm-config/cpu'], resolve),
                                name: 'cpu',
                                meta: { title: 'cpu', noCache: true },
                                id:124,
                            },
                            {
                                path: 'redisLaterTime',
                                component: (resolve) => require(['@/view/config/redis-alarm-config/redisLaterTime'], resolve),
                                name: 'redis延迟时间',
                                meta: { title: 'redis延迟时间', noCache: true },
                                id:125,
                            },
                        ],
                    },
                    {
                        path: 'api-alarm-config',
                        component: (resolve) => require(['@/view/config/api-alarm-config/index'], resolve),
                        name: 'API报警配置',
                        meta: {  icon: 'el-icon-phone-outline', noCache: true },
                        redirect: '/config/api-alarm-config/setAlarmParam',
                        hidden:true,
                        id:13,
                        children: [
                            {
                                path: 'setAlarmParam',
                                component: (resolve) => require(['@/view/config/api-alarm-config/setAlarmParam'], resolve),
                                name: '设置报警参数',
                                meta: { title: '设置报警参数', noCache: true },
                                id:131,
                            },
                            {
                                path: 'setAlarmMail',
                                component: (resolve) => require(['@/view/config/api-alarm-config/setAlarmMail'], resolve),
                                name: '设置报警邮箱',
                                meta: { title: '设置报警邮箱', noCache: true },
                                id:132,
                            },
                            {
                                path: 'setAlarmApi',
                                component: (resolve) => require(['@/view/config/api-alarm-config/setAlarmApi'], resolve),
                                name: '设置报警接口',
                                meta: { title: '设置报警接口', noCache: true },
                                id:133,
                            },
                        ],
                    },
                    {
                        path: 'listener-log-config',
                        component: (resolve) => require(['@/view/config/listener-log-config/index'], resolve),
                        name: '监听日志设置',
                        meta: {  icon: 'el-icon-time', noCache: true},
                        redirect: '/config/listener-log-config/listenerLogType',
                        hidden:true,
                        id:14,
                        children: [
                            {
                                path: 'listenerLogType',
                                component: (resolve) => require(['@/view/config/listener-log-config/listenerLogType'], resolve),
                                name: '监听日志目录',
                                meta: { title: '监听日志目录', noCache: true },
                                id:141,
                            },
                            {
                                path: 'listenerRedisMsg',
                                component: (resolve) => require(['@/view/config/listener-log-config/listenerRedisMsg'], resolve),
                                name: '监听redis信息',
                                meta: { title: '监听redis信息', noCache: true },
                                id:143,
                            },
                        ],
                    }
                ],
            },
            {
                path: 'role',
                component: (resolve) => require(['@/view/role/index'], resolve),
                name: '管理',
                redirect: '/role/rsIndex',
                top:true,
                id:3,
                meta: {icon: 'el-icon-edit', noCache: true },
                children: [
                    {
                        path: 'power',
                        component: (resolve) => require(['@/view/role/power'], resolve),
                        name: '权限管理',
                        meta: { title: '权限管理', noCache: true },
                        id:31,
                    },
                    {
                        path: 'user',
                        component: (resolve) => require(['@/view/role/user'], resolve),
                        name: '用户管理',
                        meta: { title: '用户管理', noCache: true },
                        id:32,
                    },
                    {
                        path: 'router',
                        component: (resolve) => require(['@/view/role/routerRole'], resolve),
                        name: '路由管理',
                        meta: { title: '路由管理', noCache: true},
                        id:33,
                    },
                ],
            },
            {
                path: 'tools',
                component: (resolve) => require(['@/view/tools/index'], resolve),
                name: '工具箱',
                redirect: '/tools/upload',
                top:true,
                id:4,
                meta: {icon: 'el-icon-goods', noCache: true },
                children: [
                    {
                        path: 'upload',
                        component: (resolve) => require(['@/view/tools/upload'], resolve),
                        name: '上传文件',
                        meta: { title: '上传文件', noCache: true },
                        id:41,
                    },
                ],
            },
            {
                path: 'data-report',
                component: (resolve) => require(['@/view/data-report/index'], resolve),
                name: '数据报告',
                top:true,
                id:2,
                meta: {icon: 'el-icon-printer', noCache: true },
                children: [
                    {
                        path: 'rsIndex',
                        component: (resolve) => require(['@/view/data-report/redis-status/index'], resolve),
                        name: 'Redis状态',
                        meta: { title: 'Redis状态', noCache: true },
                        id:21,
                    },

                    {
                        path: 'index',
                        component: (resolve) => require(['@/view/data-report/api-status/index'], resolve),
                        name: 'API状态',
                        meta: { title: 'API状态', icon: 'el-icon-rank', noCache: true },
                        top:true,
                        id:5,
                        children: [
                            {
                                path: 'ReturnErrorMsg',
                                component: (resolve) => require(['@/view/data-report/api-status/ReturnErrorMsg'], resolve),
                                name: 'API返回错误信息',
                                meta: { title: 'API返回错误信息', noCache: true },
                                id:51,
                            },
                            {
                                path: 'apiRequestMsg',
                                component: (resolve) => require(['@/view/data-report/api-status/apiRequestMsg'], resolve),
                                name: 'API请求信息',
                                meta: { title: 'API请求信息', noCache: true },
                                id:52,
                            },
                        ],
                    },
                    {
                        path: 'psIndex',
                        component: (resolve) => require(['@/view/data-report/php-status/index'], resolve),
                        name: 'PHP状态',
                        meta: { title: 'PHP状态', noCache: true },
                        id:22,
                    },
                    {
                        path: 'slIndex',
                        component: (resolve) => require(['@/view/data-report/search-log/index'], resolve),
                        name: '查看日志',
                        meta: { title: '查看日志', noCache: true },
                        id:23,
                    },
                    {
                        path: 'jian-kong',
                        component: (resolve) => require(['@/view/data-report/jian-kong/index'], resolve),
                        name: '监控',
                        meta: {  noCache: true},
                        hidden:true,
                        redirect: '/data-report/jian-kong/jincheng',
                        id:14,
                        children: [
                            {
                                path: 'jincheng',
                                component: (resolve) => require(['@/view/data-report/jian-kong/jincheng'], resolve),
                                name: '监听进程',
                                meta: { title: '监听进程', noCache: true },
                                id:141,
                            },
                            {
                                path: 'xingneng',
                                component: (resolve) => require(['@/view/data-report/jian-kong/xingneng'], resolve),
                                name: '监听性能',
                                meta: { title: '监听性能', noCache: true },
                                id:142,
                            },
                        ],
                    },
                ],
            },
        ],
        "msg": ""
    }
}
export {list}