<template>
    <div>
        <el-container>
            <el-dialog :title="title" :visible.sync="isShow" width="500px" @close="cleanForm">
                <el-form :model="ruleForm">
                    <el-form-item label="ip">
                        <el-input v-model="ruleForm.ip" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="服务器名称">
                        <el-input v-model="ruleForm.name" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="用户名">
                        <el-input v-model="ruleForm.username" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="ruleForm.password" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="网站根目录">
                        <el-input v-model="ruleForm.www_dir" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="端口">
                        <el-input v-model="ruleForm.port" style="width: 300px"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer" >
                    <el-button @click="cleanForm">取 消</el-button>
                    <el-button type="primary" @click="submitForm" v-loading="ruleLoading">确 定</el-button>
                </div>
            </el-dialog>
            <!--头部搜索条件-->
            <el-header class="user-header" height="300">
                ip:
                <el-input style="width: 300px" v-model="keyWords.ip">

                </el-input>
                服务器名称:
                <el-input style="width: 300px" v-model="keyWords.name">

                </el-input>

                <el-button icon="el-icon-search" @click="search(1)" circle></el-button>
                <el-button type="danger" @click="showDialog(0)" plain>
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
                            sortable
                            label="序号"
                            align="center"
                            width="50">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="id"
                            label="id"
                            align="center"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="ip"
                            label="ip"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="服务器名称"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="用户名"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="password"
                            label="密码"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="www_dir"
                            label="目录"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="port"
                            label="端口"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="create_time"
                            label="创建时间"
                            align="center"
                            width="130">
                    </el-table-column>
                    <el-table-column
                            prop="update_time"
                            label="修改时间"
                            align="center"
                            width="130">
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="250">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="showDialog(scope.row)">修改</el-button>
                            <el-button type="text" size="small" @click="deleteTpl(scope.row)">删除</el-button>
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
    import * as ftp from '../../api/template'
    export default {
        name: "ftp",
        data() {
            return {
                title:"",
                file:[],
                tableData: [

                ],
                ruleForm: {
                    ip:'',
                    name:'',
                    username:'',
                    password:'',
                    www_dir:'',
                    port:''
                },
                id:0,
                keyWords:{
                    ip:"",
                    name:''
                },
                isShow:false,
                isShow2:false,
                currentPage:1, //初始页
                pageSize:10,
                count:0,
                pageSum:0,
                tableLoading:false,
                pageLoading:false,
                ruleLoading:false,
                updateLoading:false,
                param:'',
            }
        },
        methods:{
            //添加用户 或者修改用户
            submitForm(){
                if(this.id == 0){
                    ftp.addFtp(this.ruleForm).then(res=>{
                        var error = res.code;
                        var msg = res.msg;
                        this.ruleLoading = false;

                        if(error == 0){
                            this.$message({
                                message: msg,
                                type: 'success'
                            });
                            this.cleanForm();
                            this.search(this.currentPage);
                            this.isShow = false;
                        }else{
                            this.$message({
                                message: msg,
                                type: 'error'
                            });
                        }})
                }else{
                    this.ruleForm['id'] = this.id;
                    ftp.updateFtp(this.ruleForm).then(res => {
                        var error = res.code;
                        var msg = res.msg;
                        this.ruleLoading = false;

                        if (error == 0) {
                            this.$message({
                                message: msg,
                                type: 'success'
                            });
                            this.cleanForm();
                            this.search(this.currentPage);
                            this.isShow = false;
                        } else {
                            this.$message({
                                message: msg,
                                type: 'error'
                            });
                        }
                    })
                }
            },
            //搜索
            search(currentPage) {
                this.tableLoading = true;
                this.pageLoading = true;
                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage;
                ftp.ftpList({
                    ip:this.keyWords.ip,
                    name:this.keyWords.name,
                    alias:this.keyWords.alias,
                    page: this.currentPage,
                    limit: this.pageSize,
                }).then(res => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                    this.tableData = res.data;
                    this.currentPage = this.currentPage;
                    this.pageSize = this.pageSize;
                    this.count = res.count;
                    this.pageSum = Math.ceil(this.count/this.pageSize);

                    if(res.code != 0){
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                })
            },
            //删除
            deleteTpl(row){
                ftp.deleteFtp({
                    id:row.id,
                }).then(res=>{
                    if(res.code == 0)
                    {
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
                    this.search(this.currentPage);
                })
            },
            //打开添加框
            showDialog(row){
                if(row == 0){
                    this.title = "添加ftp"
                }else{
                    this.id = row.id;
                    Object.assign(this.ruleForm,row)
                    this.title = "修改ftp"
                }
                this.isShow = true;
            },
            //重命名
            updateName(){
                this.updateLoading = true
                updateTpl({
                    id:this.id,
                    file_name:this.ruleForm.filename
                }).then(res=>{
                    var error = res.code
                    var msg = res.msg
                    this.updateLoading = false

                    if(error == 0){
                        this.$message({
                            message: msg,
                            type: 'success'
                        });
                        this.search(this.currentPage)
                        this.isShow2 = false
                    }else{
                        this.$message({
                            message: msg,
                            type: 'error'
                        });
                    }
                }).catch(error=>{
                    this.updateLoading = false
                })
            },
            //窗口关闭事件
            cleanForm(){
                Object.assign({},this.ruleForm)
                this.isShow = false
            },

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