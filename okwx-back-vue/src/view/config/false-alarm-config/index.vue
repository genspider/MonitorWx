<template>
    <div>

                <template>
                    <transition name="fade" mode="out-in">
                        <router-view></router-view>
                    </transition>
                </template>

    </div>
</template>

<script>
    export default {
        name: "index",
        computed:{
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