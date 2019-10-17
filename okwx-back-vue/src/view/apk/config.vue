<template>
    <div>
        <el-container>
            <el-dialog :title="title" :visible.sync="isShow" width="500px" @close="cleanForm">
                <el-form :model="ruleForm">
                    <el-form-item label="软件名字">
                        <el-input v-model="ruleForm.name" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="版本">
                        <el-input v-model="ruleForm.version" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="下载地址">
                        <el-input v-model="ruleForm.apk_url" style="width: 300px"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer" >
                    <el-button @click="cleanForm">取 消</el-button>
                    <el-button type="primary" @click="submitForm" v-loading="ruleLoading">确 定</el-button>
                </div>
            </el-dialog>
            <!--头部搜索条件-->
            <el-header class="user-header" height="300">
                软件名称:
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
                            prop="name"
                            label="软件名字"
                            align="center"
                            width="300">
                    </el-table-column>
                    <el-table-column
                            prop="apk_url"
                            label="下载地址"
                            align="center"
                            width="500">
                    </el-table-column>
                    <el-table-column
                            prop="version"
                            label="版本"
                            align="center"
                            width="200">
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="250">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="showDialog(scope.row)">修改</el-button>
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
    import * as apk from '../../api/apk'
    export default {
        name: "ftp",
        data() {
            return {
                title:"",
                file:[],
                tableData: [

                ],
                ruleForm: {
                    version:'',
                    name:'',
                    apk_url:'',
                },
                id:0,
                keyWords:{
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
                    apk.insert(this.ruleForm).then(res=>{
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
                    apk.update(this.ruleForm).then(res => {
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
                var obj = this.keyWords;
                obj['page'] = this.currentPage;
                obj['limit'] = this.pageSize;
                apk.list(obj).then(res => {
                    console.log(res)
                    this.pageLoading = false;
                    this.tableLoading = false;
                    this.tableData = res.data.list;
                    this.currentPage = this.currentPage;
                    this.pageSize = this.pageSize;
                    this.count = res.count;
                    this.pageSum = Math.ceil(this.count/this.pageSize);
                    console.log(res.data)
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

            //打开添加框
            showDialog(row){
                if(row == 0){
                    this.title = "添加apk配置"
                }else{
                    this.id = row.id;
                    Object.assign(this.ruleForm,row)
                    this.title = "修改apk配置"
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