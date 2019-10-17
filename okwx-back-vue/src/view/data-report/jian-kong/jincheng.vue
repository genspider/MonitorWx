<template>
    <div>
        <el-container>
            <el-header class="user-header">
                <el-select v-model="input.type" placeholder="请选择服务器" @change="search">
                    <el-option
                            v-for="item in options1"
                            :key="item.confing"
                            :label="item.name"
                            :value="item.confing">
                    </el-option>
                </el-select>
            </el-header>
            <el-main class="user-main">
                <el-table
                        :row-class-name="tableRowClassName"
                        v-loading = "tableLoading"
                        :data="tableData"
                        height="700"
                        class="loading-area"
                        border
                        style="width: 100%;text-align: center">
                    <el-table-column
                            fixed
                            label="id"
                            align="center"
                            width="50">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="USER"
                            label="用户"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="PID"
                            label="进程ID"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="%CPU"
                            label="CPU"
                            align="center"
                            width="100">
                    </el-table-column>

                    <el-table-column
                            prop="%MEM"
                            label="内存"
                            align="center"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="VSZ"
                            label="虚拟内存"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="RSS"
                            label="常驻内存"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="TTY"
                            label="终端位置"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="STAT"
                            label="状态"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="START"
                            label="启动时间"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="TIME"
                            label="使用的Cpu时间"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="COMMAND"
                            label="命令"
                            align="center"
                            width="150">
                    </el-table-column>

                  <!--  <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="250">
                        <template slot-scope="scope">

                            <el-button type="text" size="small" @click="showDialog(scope.$index,scope.row)">编辑</el-button>
                            <el-button type="text" size="small">删除</el-button>
                            <el-button type="text" size="small">启动</el-button>
                            <el-button type="text" size="small">禁用< /el-button>
                        </template>
                    </el-table-column>-->
                </el-table>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import {jincheng,configList} from '../../../api/data-report'
    export default {
        name: "jingcheng",
        data() {
            return {
                tableData: [

                ],

                currentPage:1, //初始页
                pageSize:10,
                count:0,
                pageSum:0,

                options1: [],
                input:{
                    type:''
                },
                tableLoading:false,
                setTimeId:''
            }
        },
        methods:{
            search(){
                this.tableLoading = true
                this.setTimeId = setInterval(
                    ()=>{
                    jincheng({
                        ip:this.input.type
                    }).
                    then(res=>{
                        if(res.error == 0){
                            if(res.data.length>0){
                                this.tableData = this.stateTo(res.data);
                            }
                            this.tableLoading = false
                        }else{
                            this.$message({
                                "message":res.msg,
                                "type":"error"
                            })
                            clearInterval(this.setTimeId)
                            this.tableLoading = false
                        }
                    })
                        .catch(err=>{
                        clearInterval(this.setTimeId)
                    })
                },2000)
            },

            stateTo(arr){
                for(var i=0,count = arr.length;i<count;i++){
                    var str =''
                    if(arr[i].STAT == "[]"){
                        str = this.zhuanhuan(arr[i].STAT)
                    }else{
                        for(var j = 0,len = arr[i].STAT.length;j<len;j++){
                            str = this.zhuanhuan(arr[i].STAT.substr(j,1))+"\n"+ str
                        }
                    }
                    arr[i].STAT = str
                }
                return arr
            },
            zhuanhuan(stat){
                switch (stat) {
                    case 'R':
                        stat = "runing运行态"
                        break;
                    case 'S':
                        stat= "可中断睡眠态"
                        break;
                    case 'D':
                        stat= "不可中断睡眠态"
                        break;
                    case 'T':
                        stat = "停止态"
                        break;
                    case 'Z':
                        stat = "僵尸态"
                        break;
                    case 's':
                        stat = "领导者进程"
                        break;
                    case '+':
                        stat = "前台进程"
                        break;
                    case 'l':
                        stat= "多线程进程"
                        break;
                    case 'N':
                        stat = "低优先级进程"
                        break;
                    case '<':
                        stat= "高优先级进程"
                        break;
                    case '[]':
                        stat= "内核线程"
                        break;
                }
                return stat
            },
            tableRowClassName({row, rowIndex}) {
                /*if (row === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';*/
                for(var i = 0;i<row.STAT.split('\n').length;i++){
                    if(row.STAT.split('\n')[i] == "僵尸态"){
                        return 'warning-row';
                    }else{
                        return 'success-row'
                    }
                }
                return '';
            },
            handleClick(row){

            },
            configTo(){
                configList({
                    type:'ip'
                }).then(res=>{

                    this.options1 = res.data.ip
                })
            }
        },
        mounted(){
            this.configTo()
            //this.search()
        },
        beforeDestroy() {
            clearInterval(this.setTimeId)
        },

    }
</script>

<style>
    .el-table .warning-row {
        background: oldlace;
    }
    .el-table .success-row {
        background: #f0f9eb;
    }
    .user-header{
        line-height: 60px;
    }
    .user-main{
        height: 780px;
    }
</style>