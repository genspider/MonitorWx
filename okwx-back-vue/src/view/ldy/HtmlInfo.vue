<template>
    <div>
        <el-container>
            <!--头部搜索条件-->
            <el-header class="user-header" height="300">
                别名:
                <el-input style="width: 300px" v-model="keyWords.alias">

                </el-input>
                标题:
                <el-input style="width: 300px" v-model="keyWords.title">

                </el-input>

                <el-button icon="el-icon-search" @click="search(1)" circle></el-button>
                <el-button type="danger" @click="showDialog({id:0})" plain>
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
                            prop="alias"
                            label="别名"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="title"
                            label="文章标题"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="pc_file_path"
                            label="pc端页面"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="mb_file_path"
                            label="移动端页面"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="mb_tpl"
                            label="移动端原始模板"
                            align="center"
                            width="200">
                    </el-table-column>

                    <el-table-column
                            prop="pc_tpl"
                            label="pc端原始模板"
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="mb_tpl"
                            label="移动端模板"
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
                            prop="hits"
                            label="浏览量"
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
                            width="150">
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
    import {ldyList,ldyDel} from '../../api/template'
    export default {
        name: "HtmlInfo",
        data() {
            return {

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

                keyWords:{
                    alias:"",
                    title:''
                },

                currentPage:1, //初始页
                pageSize:10,
                count:0,
                pageSum:0,
                tableLoading:false,
                pageLoading:false,
            }
        },
        methods:{
            //搜索
            search(currentPage) {
                this.tableLoading = true;
                this.pageLoading = true;
                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage;
                this.keyWords['page'] = this.currentPage
                this.keyWords['limit'] = this.pageSize
                ldyList(this.keyWords).then(res => {
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
                ldyDel({
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
                this.$router.push({ name:'创建落地页',params:row })
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