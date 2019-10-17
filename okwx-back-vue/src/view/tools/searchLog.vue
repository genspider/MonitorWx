<template>
    <div>
        <el-container>
            <el-header class="user-header">
                <div style="width: 90%;margin: 0px auto">

                    <el-select v-model="input.implementId" placeholder="请选择请求类型">
                        <el-option
                                v-for="item in options1"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                    <el-select v-model="input.urlId" placeholder="请选择项目类型">
                        <el-option
                                v-for="item in options2"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                    <el-date-picker
                            v-model="input.time"
                            type="datetime"
                            format="yyyy-MM-dd"
                            value-format="timestamp"
                            placeholder="选择日期时间"
                            :picker-options="pickerOptions1">
                    </el-date-picker>
                    <el-input style="width: 300px" v-model="input.grep" placeholder="关键词">
                    </el-input>
                    选择
                    <el-select v-model="input.type" style="width: 100px">
                        <el-option
                                v-for="item in options4"
                                :key="item.id"
                                :label="item.confing"
                                :value="item.val">
                        </el-option>
                    </el-select>
                    <el-input style="width: 100px" v-model="input.sum" ></el-input>
                        条
                    &nbsp;&nbsp
                    <el-button v-loading="loading1"  icon="el-icon-search" @click="search" circle>

                    </el-button>
                    <el-button type="danger" @click="refresh" plain>
                        清空
                    </el-button>

                </div>
                <el-tabs v-model="ip" @tab-click="search2" style="margin-top: 30px">
                    <el-tab-pane
                                 v-for="item in options3"
                                 :key="item.confing"
                                 :label="item.name"
                                 :name="item.confing"
                    ></el-tab-pane>
                </el-tabs>
            </el-header>


            <el-main class="user-main" v-loading="loading2" style="margin-top: 100px">
                <div style="width: 90%;margin: 0px auto;" v-show="showLog">
                    <el-input
                            prefix="日志"
                            ref="textarea"
                            class="textarea"
                            style="width: 90%"
                            type="textarea"
                            id="log"
                            autosize
                            v-model="textarea"
                            disabled>
                    </el-input>
                </div>

            </el-main>
        </el-container>
    </div>
</template>

<script>
    import {searchLog,queryLog,configList} from '../../api/data-report'
    export default {
        name: "index",
        data() {
            return {
                pickerOptions1: {
                    disabledDate: (time) => {
                        return time.getTime() < this.input.time || time.getTime() > Date.now();
                    }
                },
                input:{
                    grep:'',
                    time:'',
                    implementId:'',
                    urlId:'',
                    type:'',
                    sum:'10'
                },

                options1: [],
                options2: [],
                options3:[],
                options4:[{
                    id:1,
                    confing:'前',
                    val:'head'
                },{
                    id:2,
                    confing:'后',
                    val:'tail'
                }],
                textarea:"",
                key:"",
                ip:'',
                setTimeId:'',
                loading1:false,
                loading2:false,
                showLog:false
            }
        },
        methods:{
            search(){
                if(this.input.grep == ''){
                    this.$message({
                        showClose: true,
                        message:"关键词不能为空" ,
                        type: 'error'
                    });
                    return
                }
                this.loading1 = true
                searchLog(this.input).then(res=>{
                    if(res.error == 0){
                        this.key = res.data.data;
                        this.loading1 = false
                        this.search2()
                    }else{
                        this.$message({
                            showClose: true,
                            message:res.msg ,
                            type: 'error'
                        });
                        this.loading1 = false
                    }
                }).catch(error=>{
                    this.loading1 = false
                })
            },
            search2(){
                clearInterval(this.setTimeId)
                this.loading2 = true
                this.setTimeId = setInterval(()=>{
                    queryLog({
                        key:this.key,
                        ip:this.ip
                    }).then(res=>{
                        if(res.error == 0){
                            if(res.data){
                                this.showLog = true
                                this.loading2 = false
                                this.textarea = res.data;
                                clearInterval(this.setTimeId)
                            }
                        }else{
                            this.$message({
                                showClose: true,
                                message:res.msg,
                                type: 'error'
                            });
                            this.showLog = false
                            this.loading2 = false
                            clearInterval(this.setTimeId)
                        }
                    }).catch(error=>{
                        this.showLog = false
                        this.loading2 = false
                        clearInterval(this.setTimeId)
                    })
                },5000)
            },
            refresh(){
                this.input.grep = ''
                this.input.time = ''
                this.input.implementId = ''
                this.input.urlId = ''
                this.input.type = ''
            },
            configTo(){
                configList({
                    type:'ip'
                }).then(res=>{
                    this.options3 = res.data.ip
                    console.log("ip="+ this.options3[0].confing)
                    this.ip = this.options3[0].confing
                })
                configList({
                    type:'url'
                }).then(res=>{
                    this.options2 = res.data.url
                })
                configList({
                    type:'implement'
                }).then(res=>{
                    this.options1 = res.data.implement
                    console.log(this.options1[0])
                })
            }
        },
        mounted(){
            this.configTo()
        },
        beforeDestroy () {

            clearInterval(this.setTimeId)
        },

    }
</script>

<style scoped>
    .user-header{
        line-height: 60px;
    }
    .user-main{
        height: 780px;
        margin-top: 50px;
    }
    .textarea >>> .el-textarea__inner{
         font-family:"Microsoft" !important;
         font-size:20px !important;
        color: indianred;
     }
</style>