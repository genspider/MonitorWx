<template>
    <div>
        <el-container>
            <el-header class="rai-header">
                <el-menu :default-active="$route.path"
                         class="el-menu-demo tab-page"
                         mode="horizontal"
                         router
                         active-text-color="#409EFF">
                    <el-menu-item index="/config/redis-alarm-config/healthStatus">健康状态</el-menu-item>
                    <el-menu-item index="/config/redis-alarm-config/linkNumber">链接个数</el-menu-item>
                    <el-menu-item index="/config/redis-alarm-config/internalStorage">内存状态</el-menu-item>
                    <el-menu-item index="/config/redis-alarm-config/cpu">cpu状态</el-menu-item>
                    <el-menu-item index="/config/redis-alarm-config/redisLaterTime">redis延迟时间</el-menu-item>
                </el-menu>
            </el-header>
            <el-main>
                <template>
                    <transition name="fade" mode="out-in">
                        <router-view></router-view>
                    </transition>
                </template>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "index",
        computed:{
            // 默认激活的路由, 用来激活菜单选中状态
            getName:function (){
                return this.$route.name
            },
            key() {
                return this.$route.name !== undefined? this.$route.name +new Date(): this.$route +new Date()
            },
        },
    }
</script>

<style>
    body {
        margin: 0px;
        padding: 0px;
        /*background: url(assets/bg1.jpg) center !important;
            background-size: cover;*/
    // background: #1F2D3D;
        font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
        font-size: 14px;
        -webkit-font-smoothing: antialiased;
    }
    .rai-header{
        color: #333;
        text-align: center;
        line-height: 60px;
    }
    .fade-enter-active,
    .fade-leave-active {
        transition: all .2s ease;
    }

    .fade-enter,
    .fade-leave-active {
        opacity: 0;
    }
</style>