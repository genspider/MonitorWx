<template>
    <div>
        <jian-she></jian-she>
        <el-container>
            <!--添加用户-->
            <div style="background: grey;width: 100%;height: 100%;z-index: 100;position: absolute;background-color:rgba(96,96,96,0.15);top: 0px;left: 0px" v-show="isShow">
                <div style="width: 800px;height: 800px;background: white;margin: 0px auto;position: relative;top: 50px">
                    <i  :class="gbicon?'el-icon-circle-close-outline':'el-icon-circle-close'" style="font-size: 55px;position: absolute;top: 20px;right: 20px" @mouseover="gbicon1" @mouseout="gbicon2" @click="closeDialog('ruleForm')"></i>
                    <span style="color: orangered;font-size: 20px;position: absolute;top: 30px;left: 30px">编辑</span>
                    <div style="width: 600px;height: 600px;position: absolute;top: 100px;left: 100px">
                        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                            <el-form-item label="角色名" prop="cellphone">
                                <el-input v-model="ruleForm.username"></el-input>
                            </el-form-item>
                            <el-form-item label="权限分支" prop="mailbox">
                                <el-input v-model="ruleForm.username"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="submitForm(ruleForm)"v-loading="ruleLoading">完成</el-button>
                                <el-button @click="addresetForm1()">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
            <!--修改用户-->
            <div style="background: grey;width: 100%;height: 100%;z-index: 100;position: absolute;background-color:rgba(96,96,96,0.15);top: 0px;left: 0px" v-show="isShow2">
                <div style="width: 800px;height: 800px;background: white;margin: 0px auto;position: relative;top: 50px">
                    <i  :class="gbicon?'el-icon-circle-close-outline':'el-icon-circle-close'" style="font-size: 55px;position: absolute;top: 20px;right: 20px" @mouseover="gbicon1" @mouseout="gbicon2" @click="closeDialog2('ruleForm')"></i>
                    <span style="color: orangered;font-size: 20px;position: absolute;top: 30px;left: 30px">添加</span>
                    <div style="width: 600px;height: 600px;position: absolute;top: 100px;left: 100px">
                        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                            <el-form-item label="角色名" prop="cellphone">
                                <el-input v-model="ruleForm.username"></el-input>
                            </el-form-item>
                            <el-form-item label="权限分支" prop="mailbox">
                                <el-input v-model="ruleForm.username"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="submitForm(ruleForm)" v-loading="ruleLoading">完成</el-button>
                                <el-button @click="updresetForm()">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
            <!--头部搜索条件-->
            <el-header class="user-header">
                账号:
                <el-input style="width: 300px" v-model="keyWords.name">

                </el-input>

                &nbsp;&nbsp;
                <el-button icon="el-icon-search" @click="search()" circle></el-button>
                <el-button type="danger" @click="showDialog2" plain>
                    新增
                </el-button>
            </el-header>
            <!---表格-->
            <el-main class="user-main">
                <el-table
                        :data="tableData"
                        v-loading="tableLoading"
                        border
                        style="width: 100%;text-align: center">
                    <el-table-column
                            fixed
                            label="序号"
                            align="center"
                            width="50">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="id"
                            label="id"
                            align="center"
                            width="70">
                    </el-table-column>

                    <el-table-column
                            fixed
                            prop="username"
                            label="用户名"
                            align="center"
                            width="600">
                    </el-table-column>

                    <el-table-column
                            prop="timeType"
                            label="权限"
                            align="center"
                            width="600">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="lookRole(scope.row)">查看</el-button>
                        </template>
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="250">
                        <template slot-scope="scope">

                            <el-button type="text" size="small" @click="showDialog(scope.$index,scope.row)">编辑</el-button>
                            <el-button type="text" size="small" @click="adminDel(scope.row,'del')">删除</el-button>
                            <el-button type="text" size="small">启动</el-button>
                            <el-button type="text" size="small">禁用</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div style="width: 90px;height: 30px;margin: 0px auto;position: absolute;">
                    共<span style="color:red">{{pageSum}}</span>页|共<span style="color:red">{{count}}</span>条
                </div>

                <el-pagination

                        style="margin-left: 1000px;margin-top: 20px;width: 30px"
                        background
                        @current-change="search"
                        :current-page="currentPage"
                        :page-size="pageSize"
                        layout="prev, pager, next"
                        :total="count">
                </el-pagination>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    //引入api方法
    import {powerList} from '../../api/role'
    import JianShe from "../../components/jianShe";

    export default {
        name: "power",
        components: {JianShe},
        data() {
            return {
                tableData: [

                ],
                ruleForm: {
                    username:'',
                    json_role:'',
                },
                id:0,
                rules: {
                    /*role: [
                        { required: true, message: '请输入role', trigger: 'change' },
                    ],
                    rolename: [
                        { required: true, message: '请输入rolename', trigger: 'change' }
                    ],
                    phone: [
                        { required: true, message: '请输入phone', trigger: 'change' }
                    ],
                    password: [
                        { required: true, message: '请输入password', trigger: 'change' }
                    ],
                    cellphone: [
                        {  required: true, message: '请输入cellphone', trigger: 'change' }
                    ],
                    mailbox: [
                        { required: true, message: '请输入mailbox', trigger: 'change' }
                    ],*/
                },
                keyWords:{
                    name:'',
                },
                gbicon:false,
                isShow:false,
                isShow2:false,
                currentPage:1, //初始页
                pageSize:10,
                count:0,
                pageSum:0,
                tableLoading:false,
                pageLoading:false,
                ruleLoading:false
            }
        },
        methods:{
            //添加用户 或者修改用户
            submitForm(form){
                if(this.id == 0){
                    var data = form
                }else{
                    var data = {
                        'cellphone':form.cellphone,
                        'mailbox':form.mailbox,
                        'password':form.password,
                        'phone':form.phone,
                        'role':form.role,
                        'rolename':form.rolename,
                        'id':this.id,
                    }
                }
                this.ruleLoading = true
                adminAdd(data).then(res=>{
                    var error = res.error
                    var msg = res.msg
                    this.ruleLoading = false
                    if(error == 0){
                        this.$message({
                            message: msg,
                            type: 'success'
                        });
                        this.search()
                    }else{
                        this.$message({
                            message: msg,
                            type: 'error'
                        });
                    }
                }).catch(error=>{
                    this.ruleLoading = false
                })
            },
            //搜索
            search(currentPage){
                this.tableLoading = true;
                this.pageLoading = true;
                !currentPage?this.currentPage = 1:this.currentPage = currentPage
                powerList(
                    {
                        name:this.keyWords.name,
                        currentPage:this.currentPage,
                        pageSize:this.pageSize
                    }
                    ).then(res=>{
                    this.pageLoading = false;
                    this.tableLoading = false;
                    res = res.data
                    console.log(res)
                    this.tableData = res.list
                    this.currentPage = Number(res.currentPage)
                    this.pageSize = Number(res.pageSize)
                    this.count = Number(res.count)
                    this.pageSum = res.pageSum
                }).catch(error=>{
                    this.pageLoading = false;
                    this.tableLoading = false;
                })

            },
            //删除
            adminDel(row,type){
                console.log(type)
                adminModifyState({
                    id:row.id,
                    name:type
                })
                    .then(res=>{
                        if(res.error == 0){
                            this.$message({
                                message: res.msg,
                                type: 'success'
                            });
                        }else{
                            this.$message({
                                message: res.msg,
                                type: 'error'
                            });
                        }
                    })
            },
            //关闭添加框
            closeDialog(list){
                this.isShow = false;
                this.addresetForm1()
            },
            //关闭修改框
            closeDialog2(list){
                this.isShow2 = false;
                this.updresetForm()
            },
            //打开添加框
            showDialog(index,row){
                this.ruleForm.username = row.username
                this.ruleForm.json_role = row.json_role
                this.id = row.id
                this.isShow = true;
            },
            //打开修改框
            showDialog2(){
                this.isShow2 = true;
            },
            //关闭按钮
            gbicon1(){
                this.gbicon = true
            },
            //关闭按钮
            gbicon2(){
                this.gbicon = false
            },
            handleClick(row){
                console.log(row)
            },
            //重置
            updresetForm(){
                this.ruleForm.username = ""
                this.ruleForm.json_role = ""
                this.id = 0
            },
            //重置
            addresetForm1(){
                this.ruleForm.username = ""
                this.ruleForm.json_role = ""
            },
            lookRole(row){
                this.$message({
                    message: "功能暂未开放",
                    type: 'success'
                });
            }

        },
        //搜索
        mounted(){
            this.search()
        }

    }
</script>

<style>
    .user-header{
        line-height: 60px;
    }
    .user-main{
        height: 780px;
    }
</style>