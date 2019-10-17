<template>
    <div>
        <el-container>
            <!--头部搜索条件-->
            <el-header class="user-header" height="300">
                账号:
                <el-input style="width: 300px" v-model="keyWords.user_no"></el-input>
                <el-select v-model="keyWords.groups" filterable  clearable @change="search(1)"  placeholder="请选择分组">
                    <el-option
                            v-for="item in domainSelect"
                            :key="item.groups"
                            :label="item.groups"
                            :value="item.groups">
                    </el-option>
                </el-select>

                时间区间:
                <el-date-picker
                        v-model="keyWords.startTime"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        value-format="timestamp"
                        placeholder="选择日期时间"
                        :picker-options="pickerOptions0">
                </el-date-picker>
                至
                <el-date-picker
                        v-model="keyWords.endTime"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        value-format="timestamp"
                        placeholder="选择日期时间"
                        :picker-options="pickerOptions1">
                </el-date-picker>
                <el-select v-model="pageSize" @change="search(1)">
                    <el-option
                            v-for="item in pageSelect"
                            :key="item.pageSize"
                            :label="item.pageSize"
                            :value="item.pageSize">
                    </el-option>
                </el-select>
                <el-button icon="el-icon-search" @click="search(1)" circle></el-button>
                <el-button type="danger" v-loading = "Load" @click="excel" plain>
                    导出数据
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
                            sortable
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="groups"
                            label="分组"
                            sortable
                            align="center"
                            width="200">
                    </el-table-column>
                    <el-table-column
                           prop="user_no"
                            label="微信账号"
                            align="center"
                            width="300">
                    </el-table-column>

                    <el-table-column
                            prop="fans_name"
                            sortable
                            label="粉丝名字"
                            align="center"
                            width="300">
                    </el-table-column>
                    <el-table-column
                            prop="create_time"
                            sortable
                            label="创建时间"
                            align="center"
                            width="500">
                    </el-table-column>
                </el-table>
                <div style="width: 90px;height: 30px;">
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
    import {firendList,groups} from "../../api/fans";
    export default {
        name: "tags",
        data() {
            return {
                pageSelect:[{
                    pageSize:10
                },{
                    pageSize:20
                },{
                    pageSize:30
                },{
                    pageSize:40
                },{
                    pageSize:50
                }
                ],
                pickerOptions0: {
                    disabledDate: (time) => {
                        if (this.keyWords.endTime != "") {
                            return time.getTime() > Date.now() || time.getTime() > this.keyWords.endTime;
                        } else {
                            return time.getTime() > Date.now();
                        }
                    }
                },
                pickerOptions1: {
                    disabledDate: (time) => {
                        return time.getTime() < this.keyWords.startTime || time.getTime() > Date.now();
                    }
                },
                Load:false,
                domainSelect:[],

                tableData: [

                ],
                keyWords:{
                    user_no:'',
                    startTime:'',
                    endTime:''
                },
                currentPage:1, //初始页
                pageSize:10,
                count:0,
                pageSum:0,
                tableLoading:false,
                pageLoading:false,
                key:{
                    user_no:'',
                    startTime:'',
                    endTime:''
                },
                excelData:[]
            }
        },
        methods:{
            searchDomain(){
                groups().then(res=>{
                    if(res.code == 0){
                        this.domainSelect = res.data.list;
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(err=>{})
            },
            //搜索
            search(currentPage) {
                this.tableLoading = true;
                this.pageLoading = true;

                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage;
                this.keyWords['page'] = this.currentPage
                this.keyWords['limit'] = this.pageSize

                firendList(this.keyWords).then(res => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                    this.tableData = res.data.list
                    this.currentPage = this.currentPage
                    this.pageSize = this.pageSize
                    this.count = res.count
                    this.pageSum = Math.ceil(this.count/this.pageSize)
                    if(res.code != 0){
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                    this.searchDomain();
                }).
                catch(error => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                })

            },
            excel(){
                this.Load = true;

                var flag = true;
                for(var k in this.key){
                    if(this.key[k] != this.keyWords[k]){
                        flag = false
                    }
                }
                if(this.excelData.length>0 && flag){
                    this.Load = false;
                    require.ensure([], () => {
                        const { export_json_to_excel } = require('../../vendor/Export2Excel'); //引入文件　　　　　　
                        const tHeader = ['id', '分组','微信号码', '粉丝名字', '创建时间']; //将对应的属性名转换成中文

                        const filterVal = ['id', 'groups','user_no', 'fans_name','create_time'];
                        const list = this.excelData;
                        //把data里的tableData存到list
                        const data = this.formatJson(filterVal, list);
                        export_json_to_excel(tHeader, data, '监控粉丝详情列表');
                    })
                }
                else{
                    this.keyWords['excel'] = 1
                    firendList(this.keyWords).
                    then(res=>{
                        if(res.code == 0){
                            this.Load = false;
                            require.ensure([], () => {
                                const { export_json_to_excel } = require('../../vendor/Export2Excel'); //引入文件　　　　　　

                                const tHeader = ['id', '分组','微信号码', '粉丝名字', '创建时间']; //将对应的属性名转换成中文

                                const filterVal = ['id', 'groups','user_no', 'fans_name','create_time'];
                                const list = this.excelData;
                                //把data里的tableData存到list
                                const data = this.formatJson(filterVal, list);
                                export_json_to_excel(tHeader, data, '监控粉丝详情列表');
                            })
                        }else{
                            this.$message({
                                message: res.msg,
                                type: 'error'
                            });
                        }
                        this.excelData = res.data.list
                        Object.assign(this.key,this.keyWords)
                    }).
                    catch(err=>{
                        this.Load = true;
                    })
                }
            },
            formatJson(filterVal, jsonData) {
                return jsonData.map(v => filterVal.map(j => v[j]))
            }
        },
        //搜索
        mounted(){
            this.search()
            this.searchDomain()
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