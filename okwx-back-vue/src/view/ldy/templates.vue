<template>
    <div>
        <el-container>
            <el-dialog title="上传模板" :visible.sync="isShow">
                <el-form :model="ruleForm">
                    <el-form-item label="模板名称">
                        <el-input v-model="ruleForm.filename" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="模板类型">
                        <el-select v-model="ruleForm.type" filterable  clearable placeholder="模板类型">
                            <el-option
                                    v-for="item in typeSelect"
                                    :key="item.name"
                                    :label="item.name"
                                    :value="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="模板文件">
                        <el-upload
                                style="float: left;margin-left: 50px;"
                                class="upload-demo"
                                action=""
                                :file-list="file"
                                :auto-upload=false
                                :on-change="onchangeHtml"
                                :on-remove="handleRemoveHtml"
                                :limit="1"
                                accept=".html"
                                ref="html">
                            <el-button plain size="small" type="primary" style="height: 50px">点击上传模板</el-button>
                            <div slot="tip" class="el-upload__tip" style="color: red;margin-left: 15px">只能上传html文件</div>
                        </el-upload>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="closeDialog">取 消</el-button>
                    <el-button type="primary" @click="submitForm" v-loading="ruleLoading">确 定</el-button>
                </div>
            </el-dialog>
            <el-dialog title="重命名" :visible.sync="isShow2">
                <el-form :model="ruleForm">
                    <el-form-item label="模板名称">
                        <el-input v-model="ruleForm.filename" style="width: 300px"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="closeDialog">取 消</el-button>
                    <el-button type="primary" @click="updateName" v-loading="updateLoading">确 定</el-button>
                </div>
            </el-dialog>
            <!--头部搜索条件-->
            <el-header class="user-header" height="300">
                模板ID:
                <el-input style="width: 300px" v-model="keyWords.id">

                </el-input>
                模板名称:
                <el-input style="width: 300px" v-model="keyWords.file_name">

                </el-input>
                模板类型:
                <el-select v-model="keyWords.type" filterable  clearable placeholder="模板类型">
                    <el-option
                            v-for="item in typeSelect"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                    </el-option>
                </el-select>
                <el-button icon="el-icon-search" @click="search(1)" circle></el-button>
                <el-button type="danger" @click="isShow = true" plain>
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
                            prop="id"
                            label="id"
                            align="center"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            label="模板名字"
                            align="center"
                            sortable
                            width="500">
                        <template slot-scope="scope">
                            <a :href="tableData[scope.$index].web_path" target="_blank">{{scope.row.file_name}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="file_path"
                            label="模板路径"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="type"
                            label="类型"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="create_time"
                            label="创建时间"
                            align="center"
                            width="350">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="update_time"
                            label="修改时间"
                            align="center"
                            width="350">
                    </el-table-column>



                    <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="250">
                        <template slot-scope="scope">
                            <el-button type="text" size="small" @click="showDialog(scope.$index,scope.row)">重命名</el-button>
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
    import {templateList,templateAdd,deleteTpl,updateTpl} from '../../api/template'
    export default {
        name: "templates",
        data() {
            return {
                file:[],
                typeSelect:[
                    {
                        name:'pc'
                    },
                    {
                        name:'mobile'
                    },
                ],
                tableData: [

                ],
                ruleForm: {
                    filename:'',
                    type:''
                },
                id:0,
                keyWords:{
                    file_name:"",
                    id:'',
                    type:''
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
                if(this.param == ""){
                    this.$message({
                        message: "请先选择文件",
                        type: 'error'
                    });
                    return false
                }
                this.ruleLoading = true
                this.param.append("filename",this.ruleForm.filename)
                this.param.append("type",this.ruleForm.type)
                templateAdd(this.param).then(res=>{
                    var error = res.code
                    var msg = res.msg
                    this.ruleLoading = false

                    if(error == 0){
                        this.$message({
                            message: msg,
                            type: 'success'
                        });
                        this.search(this.currentPage)
                        this.isShow = false
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
            search(currentPage) {
                this.tableLoading = true;
                this.pageLoading = true;
                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage
                this.keyWords['page'] = this.currentPage;
                this.keyWords['limit'] = this.pageSize;
                templateList(this.keyWords).then(res => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                    this.tableData = res.data
                    this.currentPage = this.currentPage
                    this.pageSize = this.pageSize
                    this.count = res.count
                    this.pageSum = Math.ceil(this.count/this.pageSize)
                    console.log(this.tableData)
                    if(res.code != 0){
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    console.log(error)
                    this.pageLoading = false;
                    this.tableLoading = false;
                })
            },
            //删除
            deleteTpl(row){
                deleteTpl({
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
                        this.search(this.currentPage)
                    })
            },
            //关闭添加框
            closeDialog(){
                this.isShow = false
                this.param = ""
                this.ruleForm.filename = ""
            },
            //关闭修改框
            closeDialog2(list){
                this.isShow2 = false;
                this.ruleForm.filename = ""
            },
            //打开添加框
            showDialog(index,row){
                this.id = row.id
                this.ruleForm.filename = row.file_name
                this.isShow2 = true;
            },
            //打开修改框
            showDialog2(){
                this.isShow2 = true;
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
            //添加模板
            onchangeHtml(file,filesList) {
                var fileArr = file.name.split(".");
                var houzhui = fileArr[fileArr.length-1];
                if(houzhui != "html" ){
                    this.$refs.html.clearFiles()
                    this.$message({
                        showClose: true,
                        message: "上传文件必须是html结尾的文件",
                        type: 'error'
                    });
                    return false
                }

                this.param = new FormData();

                this.param.append('file', file.raw, file.name);
            },
            handleRemoveHtml(){
                this.param.delete("file")
                this.param = ""
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