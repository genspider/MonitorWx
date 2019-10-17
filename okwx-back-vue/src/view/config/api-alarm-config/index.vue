<template>
    <div>
        <el-container>
            <el-header class="rai-header">
                <el-menu :default-active="$route.path"
                         class="el-menu-demo tab-page"
                         mode="horizontal"
                         router
                         active-text-color="#409EFF">
                    <el-menu-item index="/config/api-alarm-config/setAlarmParam">设置报警参数</el-menu-item>
                    <el-menu-item index="/config/api-alarm-config/setAlarmMail">设置报警邮箱</el-menu-item>
                    <el-menu-item index="/config/api-alarm-config/setAlarmApi">设置报警接口</el-menu-item>
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