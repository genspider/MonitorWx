<template>
    <div>
        <el-submenu v-if="!subroute.hidden && subroute.children && subroute.children.length > 0"
                    :index="genPath(fatherpath, subroute.path)">
            <template slot="title">

                <i :class="subroute.meta.icon"></i>
                &nbsp;&nbsp;
                <span slot="title">{{subroute.name}}</span>
            </template>
            <sidebarItem v-for="(submenu, subidx) in subroute.children"
                         :subroute="submenu"
                         :fatherpath="genPath(fatherpath, subroute.path)"
                         :barIdx="subidx"
                         :key="barIdx + '-' + subidx"
            />
        </el-submenu>
        <el-menu-item style="font-weight: 400" v-else-if="!subroute.hidden && subroute.trends != 1" :index="genPath(fatherpath, subroute.path)" active-text-color="#409EFF">&nbsp;&nbsp;
           {{subroute.name}}{{subroute.trends}}
        </el-menu-item>
        <el-menu-item style="font-weight: 400;display: none" v-else-if="subroute.trends == 1" :index="genPath(fatherpath, subroute.path)" active-text-color="#409EFF" >

        </el-menu-item>

        <el-menu-item style="font-weight: 400" v-else :index="genPath(fatherpath, subroute.path)">
            {{subroute.name}}{{subroute.trends}}
        </el-menu-item>
    </div>
</template>

<script>
    export default {
        name: "sidebarItem",
        props: {
            subroute: {
                type: Object
            },
            barIdx: {
                type: [String, Number]
            },
            fatherpath: {
                type: String
            }
        },
        computed: {
            // 默认激活的路由, 用来激活菜单选中状态
            defaultActive: function(){
                return this.$route.path
            },
        },
        methods: {
            // 生成侧边栏路由，格式: /a/b/c
            genPath: function(){
                let arr = [ ...arguments ]
                let path = arr.map(v => {
                    while (v[0] === '/'){
                        v = v.substring(1)
                    }
                    while(v[-1] === '/'){
                        v = v.substring(0, v.length)
                    }
                    return v
                }).join('/')
                path = path[0] === '/' ? path : '/'+path
                return path
            }
        },
        mounted: function(){

        }
    }
</script>

<style>

</style>