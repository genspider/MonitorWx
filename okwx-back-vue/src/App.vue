<template>
    <transition name="fade"
                mode="out-in">
        <router-view v-cloak></router-view>
    </transition>
</template>

<script>
    export default {
        name: 'app',
        components: {
        }
    }

</script>

<style lang="scss">
    [v-cloak] {
        display:none !important;
    }
    body {
        margin: 0px;
        padding: 0px;
        font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
        font-size: 14px;
        -webkit-font-smoothing: antialiased;
    }

    #app {
        position: absolute;
        top: 0px;
        bottom: 0px;
        width: 100%;
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