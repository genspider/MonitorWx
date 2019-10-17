<template>
    <div id="login">
        <el-container>
            <el-header height="150px">

            </el-header>
            <el-main class="login-div-main">
                <el-form class="login-page">
                    <p style="color: lightgrey;font-family:  'Arial','Microsoft YaHei','黑体','宋体',sans-serif;font-size: 25px">纵驰-微信加粉统计-管理平台</p>
                    <el-form-item>
                        <el-input type="text"
                                  auto-complete="off"
                                  placeholder="请输入手机号码"
                                  v-model="cellphone"
                                  @input="userChange"
                        >
                            <template slot="prepend">
                                <i class="el-icon-mobile-phone"></i>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input :type="pwdType"
                                  auto-complete="off"
                                  placeholder="请输入密码"
                                  v-model="password"
                                  @input="userChange"
                                  @keyup.enter.native="login"
                        >
                            <template slot="prepend"><span class="fa fa-lock fa-lg" style="width: 13px"></span></template>
                            <template slot="suffix"><span class="password-eye" @click="showPassword" :class="eyeType"style="margin-top: 15px;margin-left: -40px" ></span></template>
                        </el-input>
                    </el-form-item>
                    <el-checkbox
                            @change="changeRemember" v-model="rememberme" class="rememberme"
                    >
                        记住密码
                    </el-checkbox>
                    <el-form-item style="width:100%;">
                        <el-button type="primary" style="width:100%;"  @click="login" :disabled="isDisabled" v-loading="loading">登录</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
            <el-footer height="60px" class="login-div-foot">湘ICP备06014709号-3|Copyright© 云猴网 2013-2016，All Rights Reserved</el-footer>
        </el-container>
    </div>
</template>

<script>

    import {login} from '../../api/login'
    import {saveObj} from '../../utils/json'

    export default {
        name: "Login",
        data(){
            return{
                cellphone:'',
                password:'',
                isDisabled:true,
                loading:false,
                pwdType: 'password',
                eyeType: 'fa fa-eye-slash fa-lg eye',
                rememberme:false
            }
        },
        methods:{
            login(){
                var cellphone = this.cellphone;
                var password = this.password;
                this.loading = true
                if(cellphone == "admin" && password == "admin"){
                    saveObj('user',cellphone)
                    saveObj('token','111')
                    saveObj('power','1111')
                    // 登陆成功 保存帐号密码
                    if(this.rememberme){
                        this.setCookie(cellphone, password, 7)
                    }else{
                        this.deleteCookie()
                    }
                    this.$message({
                        message: '登陆成功',
                        type: 'success'
                    });
                    this.loading = false
                    this.$router.push({path:'/'})
                }else{
                    this.$message({
                        message: '用户名或密码错误',
                        type: 'error'
                    });
                    this.loading = false
                }
                /*this.loading = true
                login({
                    cellphone:cellphone,
                    password:password
                }).
                then(res=>{
                    if(res.error == 0){
                        console.log(res)
                        saveObj('user',cellphone)
                        saveObj('token',res.data.token)
                        this.$message({
                            message: '登录成功',
                            type: 'success'
                        });
                        if(this.rememberme){
                            this.setCookie(cellphone, password, 7)
                        }else{
                            this.deleteCookie()
                        }
                        this.$router.push({path:'/'})
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                    this.loading = false
                })
                    .catch(error=>{
                        this.loading = false
                    })*/
            },
            userChange(){
                this.cellphone== ''||this.password==''?this.isDisabled = true:this.isDisabled = false
            },
            showPassword() {
                if (this.pwdType === 'password') {
                    this.pwdType = ''
                    this.eyeType = 'fa fa-eye fa-lg eye'
                } else {
                    this.pwdType = 'password'
                    this.eyeType = 'fa fa-eye-slash fa-lg eye'
                }
            },
            setCookie(name, pass, days){
                let expire = new Date()
                expire.setDate(expire.getDate() + days)
                document.cookie = `C-username=${name};expires=${expire}`
                document.cookie = `C-password=${pass};expires=${expire}`
            },
            getCookie(){
                if(document.cookie.length){
                    let arr = document.cookie.split('; ')
                    for(let i=0; i<arr.length; i++){
                        let arr2 = arr[i].split('=')
                        if(arr2[0] === 'C-username'){
                            this.cellphone = arr2[1]
                        }else if(arr2[0] === 'C-password'){
                            this.password = arr2[1]
                            this.rememberme = true
                        }
                    }
                }
            },
            deleteCookie(){
                this.setCookie('', '', -1);
            },
            changeRemember() {
                console.log(this.rememberme);
                //this.rememberme == true ?this.rememberme =false:this.rememberme = true;
            },
        },mounted(){
            this.getCookie()
            this.userChange()
        }
    }
</script>

<style>
    #login{
        width: 100%;
        height: 100%;
        z-index: -5;
        background-color: gray;
    }
    .login-div-foot {
        color: white;
        text-align: center;
        line-height: 60px;
        border-top: 1px solid white;
    }

    .login-div-main {
        text-align: center;
        height: 700px;
    }
    .login-page{
        -webkit-border-radius: 5px;
        margin: 0px auto;
        width: 480px;
        padding: 65px 85px 85px;
        border: 1px solid white;
        background:white;
    }
    label.el-checkbox.rememberme {
        margin: 0px 0px 15px;
        margin-left: -350px;
        text-align: left;
    }
    .eye:hover{
        cursor: pointer;
    }
    .tozhuce:hover{
        cursor: pointer;
        color: cornflowerblue;
    }
    a{
        text-decoration: none;
    }

    .router-link-active {
        text-decoration: none;
    }

</style>