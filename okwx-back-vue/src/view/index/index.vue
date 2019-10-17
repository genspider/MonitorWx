<template>
  <div class="app" >
    <el-container>
      <el-aside class="app-side app-side-left"
                :class="isCollapse ? 'app-side-collapsed' : 'app-side-expanded'">
        <sidebar :isCollapse="isCollapse" :routes="getList"/>
      </el-aside>

      <el-container>
        <el-header class="app-header">
          <div style="width: 60px; cursor: pointer;"
               @click.prevent="toggleSideBar">
            <i v-show="!isCollapse" class="el-icon-d-arrow-left"></i>
            <i v-show="isCollapse" class="el-icon-d-arrow-right"></i>
          </div>
            <div>

            </div>
          <!-- 我是样例菜单 -->

          <div class="app-header-userinfo">
              <el-menu default-active="/"
                       router
                       class="el-menu-demo tab-page"
                       mode="horizontal"
                       theme="dark"
                       active-text-color="#409EFF">

              </el-menu>
            <el-dropdown trigger="hover"
                         :hide-on-click="false" class="el-dropdown">
              <span class="el-dropdown-link" style="color: orangered">
                {{ username }}
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>我的消息</el-dropdown-item>
                <el-dropdown-item>设置</el-dropdown-item>
                <el-dropdown-item divided
                                  @click.native="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="app-body" style="background: whitesmoke">
          <template>
              <transition name="fade" mode="out-in">
                  <router-view v-cloak></router-view>
              </transition>
          </template>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import sidebar from '../layout/sidebar'
    import {saveObj,removeObj,getObj} from '../../utils/json'
    export default {
        name: 'Container',
        components:{
            sidebar
        },
        data() {
            return {
                username: '',
                isCollapse: false
            }
        },
        computed:{
            // 默认激活的路由, 用来激活菜单选中状态
            getName:function (){
                return this.$route.name
            },
            key() {
                return this.$route.name !== undefined? this.$route.name +new Date(): this.$route +new Date()
            },
            // 默认激活的路由, 用来激活菜单选中状态
            defaultActive: function(){
                return this.$route.path
            },
            getList:function () {
                console.log(getObj('list'))
                return antRouter;
            }
        },
        methods: {
            toggleSideBar() {
                this.isCollapse = !this.isCollapse
            },
            logout: function () {
                this.$confirm('确认退出?', '提示', {})
                    .then(() => {
                        removeObj('user')
                        removeObj('token')
                        removeObj('power')
                        removeObj('list')
                        this.$message({
                            showClose: true,
                            message: '注销成功',
                            type: 'success',
                        })
                        this.$router.push('/login');
                    })
                    .catch(() => { });
            },
        },
        mounted: function () {

            let user = getObj('user')
            if (user) {
                this.username = user;
            }
        },
        watch: {

        },
    }
</script>

<style>
    .el-dropdown:hover{
        cursor: pointer;
    }
    [v-cloak] {
        display:none !important;
    }
</style>