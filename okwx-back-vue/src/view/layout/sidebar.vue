<template>
    <div>
        <div class="app-side-logo">
            <div style="line-height: 60px;" v-if="!isCollapse">
                纵驰-微信加粉统计-管理平台
            </div>
            <div style="line-height: 60px;" v-if="isCollapse">
                纵驰
            </div>
        </div>
        <div>
            <!-- 我是样例菜单 -->
            <el-menu class="el-menu-vertical-demo tab-page"
                     :default-active="defaultActive"
                     router

                     :collapse="isCollapse">

                <sidbarItem v-for="(item, idx) in routes"
                            :subroute="item"
                            :fatherpath="fatherPath"
                            :barIdx="idx" :key="idx"/>
            </el-menu>
        </div>
    </div>
</template>

<script>
    import sidbarItem from '../layout/sidebarItem'
    export default {
        name: "sidebar",
        components: {
            sidbarItem
        },
        props: {
            isCollapse: {
                type: Boolean
            },
            routes: {
                type: Array
            }
        },
        computed: {
            // 首次进入页面时展开当前页面所属的菜单
            defaultActive(){
                return this.$route.path
            },
            fatherPath(){
                // 这里直接获取路由配置的 '/' 项
                return this.$router.options.routes[0].path
            }
        },
        mounted(){
        }
    }
</script>

<style>

</style>