<template>
    <div>
        <el-container>
            <el-header class="user-header">
                时间区间:
                <el-date-picker
                        v-model="input.startTime"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        value-format="timestamp"
                        placeholder="选择日期时间"
                        :picker-options="pickerOptions0">
                </el-date-picker>
                至
                <el-date-picker
                        v-model="input.endTime"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        value-format="timestamp"
                        placeholder="选择日期时间"
                        :picker-options="pickerOptions1">
                </el-date-picker>
                接口名:
                <el-input style="width: 300px" v-model="input.apiName">

                </el-input>
                &nbsp;&nbsp;
                <el-button icon="el-icon-search" @click="search(1)" circle></el-button>

            </el-header>
            <el-main class="user-main">
                <el-tabs v-model="input.appName" @tab-click="search(1)" style="margin-top: 30px">
                    <el-tab-pane
                            v-for="item in options2"
                            :key="item.id"
                            :label="item.name"
                            :name="item.name"
                    ></el-tab-pane>
                </el-tabs>

                <el-table
                        :data="tableData"
                        v-loading="tableLoading"
                        stripe
                        style="width: 100%">
                    <el-table-column
                            prop="app_id"
                            label="app_id"
                            align="center"
                            min-width="30%">
                    </el-table-column>
                    <el-table-column
                            prop="api_name"
                            label="接口名"
                            align="center"
                            min-width="30%">
                    </el-table-column>
                    <el-table-column
                            prop="total"
                            label="接口请求次数"
                            align="center"
                            min-width="40%">
                    </el-table-column>
                    <el-table-column
                            width="500px"
                            prop="time"
                            label="接口响应时间"
                            align="center"
                            min-width="30%">
                    </el-table-column>
                </el-table>
                <div style="width: 150px;height: 30px;margin: 0px auto;position: absolute;">
                    共<span style="color:red">{{pageSum}}</span>页|共<span style="color:red">{{count}}</span>条
                </div>

                <el-pagination

                        style="margin-left: 1000px;margin-top: 20px;width: 30px"
                        background
                        @current-change="search"
                        :current-page="input.currentPage"
                        :page-size="input.pageSize"
                        layout="prev, pager, next"
                        :total="count">
                </el-pagination>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import {journal_nature,configList} from '../../../api/data-report'
    export default {
        name: "apiRequestMsg",
        data() {
            return {
                activeName: 'sys',
                pickerOptions0: {
                    disabledDate: (time) => {
                        if (this.input.endTime != "") {
                            return time.getTime() > Date.now() || time.getTime() > this.input.endTime;
                        } else {
                            return time.getTime() > Date.now();
                        }
                    }
                },
                pickerOptions1: {
                    disabledDate: (time) => {
                        return time.getTime() < this.input.startTime || time.getTime() > Date.now();
                    }
                },
                options2:[],
                tableData: [

                ],
                count:0,
                pageSum:0,
                input:{
                    apiName:'',
                    startTime:'',
                    endTime:'',
                    appName:'',
                    currentPage:1, //初始页
                    pageSize:10,
                },
                tableLoading:false,
            }
        },
        methods:{
            search(currentPage){
                console.log(currentPage)
                console.log(this.input)
                var start_time = this.input.startTime
                var end_time = this.input.endTime
                !currentPage?this.input.currentPage = 1:this.input.currentPage = currentPage
                this.tableLoading = true
                journal_nature({
                    apiName:this.input.apiName,
                    startTime:Math.round(start_time / 1000),
                    endTime:Math.round(end_time / 1000),
                    appName:this.input.appName,
                    currentPage:this.input.currentPage, //初始页
                    pageSize:this.input.pageSize,
                }).then(res=>{
                    if(res.error == 0){
                        var data = res.data
                        this.tableData = data.list
                        this.input.currentPage = Number(data.currentPage)
                        this.input.pageSize = Number(data.pageSize)
                        this.count = Number(data.count)
                        this.pageSum = data.pageSum
                    }else{
                        this.$message({
                            message:res.msg,
                            type: 'error'
                        });
                    }
                    this.tableLoading = false
                }).catch(error =>{
                    this.tableLoading = false
                })
            },
            configTo(){
                configList({
                    type:'url'
                }).then(res=> {
                    this.options2 = res.data.url
                    this.input.appName = res.data.url[0].name
                })
            }
        },
        mounted(){
            this.configTo()
            //this.search()
        },

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