<template>
    <div>

        <el-container>
            <el-header class="user-header">
                <div style="width: 90%;margin: 0px auto">
                    <el-input style="width: 200px;float: left;margin-left: 50px" v-model="appcode" placeholder="appcode">
                    </el-input>
                    <el-input style="width: 200px;float: left;margin-left: 50px" v-model="usercode" placeholder="usercode">
                    </el-input>
                    <el-upload
                            style="float: left;margin-left: 50px"
                            class="upload-demo"
                            action=""
                            :auto-upload=false
                            :on-change="onchangeExcel"
                            :on-remove="handleRemoveExcel"
                            :limit="1"
                            accept=".xls,.xlsx"
                            ref="excel">
                        <el-button plain size="small" type="primary" style="height: 50px">点击上传excel</el-button>
                        <div slot="tip" class="el-upload__tip" style="color: red;margin-left: 15px">只能上传后缀为.xls,.xlsx的文件</div>
                    </el-upload>
                    <el-upload
                        style="float: left;margin-left: 50px;"
                        class="upload-demo"
                        action=""
                        :file-list="zips"
                        :auto-upload=false
                        :on-change="onchangeZip"
                        :on-remove="handleRemoveZip"
                        :limit="1"
                        accept=".zip"
                        ref="zip">
                    <el-button plain size="small" type="primary" style="height: 50px">点击上传压缩包</el-button>
                    <div slot="tip" class="el-upload__tip" style="color: red;margin-left: 15px">只能上传zip文件</div>
                </el-upload>
                    <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="primary" @click="sub" v-loading="loading" >提交</el-button>
                    <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="success" @click="clean" >清空</el-button>
                    <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="info" @click="output2" >下载模版</el-button>
          <!--          <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="info" @click="lookLog" >查看执行日志</el-button>-->
                </div>
            </el-header>
            <div v-show="show" >
                <div style="position: relative;top: 100px;left: 150px;font-family:'Microsoft' ;font-size:20px">日志</div>

                <el-main class="user-main" v-loading="loading2">
                    <div style="width: 90%;margin: 0px auto">
                        <el-input
                                prefix="日志"
                                ref="textarea"
                                class="textarea"
                                style="width: 90%"
                                type="textarea"
                                id="log"
                                autosize
                                v-model="logList"
                                disabled>
                        </el-input>
                    </div>
                </el-main>
            </div>
        </el-container>

    </div>
</template>

<script>

    import { Message } from 'element-ui';
    import {invoice,invoicelog}from '../../api/tools'
    export default {
        name: "upload",
        data(){
            return {
                zips:[],
                excels:[],
                param:"",
                loading:false,
                logList:'',
                appcode:'',
                usercode:'',
                excel:false,
                zip:false,
                show:false,
                setTimeId:'',
                id:'',
                loading2:false
            }
        },
        methods:{
            sub(){
                if(!this.excel){
                    Message({
                        showClose: true,
                        message: "请选择excel文件",
                        type: 'error'
                    });
                    return
                }else if(!this.zip){
                    Message({
                        showClose: true,
                        message: "请选择zip文件",
                        type: 'error'
                    });
                    return
                }

                if(this.usercode == ""){
                    Message({
                        showClose: true,
                        message: "请填写usercode",
                        type: 'error'
                    });
                    return
                }
                if(this.appcode == ""){
                    Message({
                        showClose: true,
                        message: "请填写appcode",
                        type: 'error'
                    });
                    return
                }
                this.loading = true
               // if(process.env.NODE_ENV=="production"){
                  //  var api = "http://10.200.52.183:9090/invoice"
                //}
              //  if(process.env.NODE_ENV=="development"){
          //          var api = "http://10.12.25.59:9090/invoice"
             //   }
              /*  let config = {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                };*/
                this.id = this.usercode
                this.param.append("usercode",this.usercode)
                this.param.append("appcode",this.appcode)
                invoice(this.param).then((res) =>{
                    this.loading = false
                    // console.log(response)
                    if(res.error == 0){
                        Message({
                            showClose: true,
                            message:res.msg,
                            type: 'success'
                        });
                        setTimeout(()=>{
                            this.lookLog()
                        },2000)
                    }else{
                        Message({
                            showClose: true,
                            message:res.msg,
                            type: 'error'
                        });
                    }

                }).catch((error)=>{
                    this.loading = false
                })
            },
            onchangeExcel(file,filesList) {

                var houzhui = file.name.split(".")[1];
                if(houzhui != "xlsx" && houzhui != "xls" ){
                    this.$refs.excel.clearFiles()
                    Message({
                        showClose: true,
                        message: "上传文件必须是xlsx 或者 xls结尾的文件",
                        type: 'error'
                    });
                    return false
                }
                if(this.param == ""){
                    this.param = new FormData();
                }
                this.excel = true;
                this.param.append('excelfilename', file.raw, file.name);

            },
            handleRemoveExcel(file,filesList){
                this.param.delete('excelfilename')
                this.excel = false;
                //this.param = ""
            },
            onchangeZip(file,filesList) {
                var houzhui = file.name.split(".")[1];
                if(houzhui != "zip"){
                    this.$refs.zip.clearFiles()
                    Message({
                        showClose: true,
                        message: "上传文件必须是zip结尾的文件",
                        type: 'error'
                    });
                    return false
                }

                //重新写一个表单上传的方法
                if(this.param == ""){
                    this.param = new FormData();
                }
                this.zip = true
                this.param.append('zipfilename', file.raw, file.name);
            },
            handleRemoveZip(file,filesList){
                this.zip = false
                this.param.delete('zipfilename')
            },
            clean(){
                this.$refs.zip.clearFiles()
                this.$refs.excel.clearFiles()
                this.appcode = "";
                this.usercode ="";
                this.zip = false;
                this.excel = false;
            },
            output2(){
                require.ensure([], () => {

                    const { export_json_to_excel } = require('../../vendor/Export2Excel'); //引入文件　　　　　　

                    const tHeader = ['发票名称', '发票代码', '发票号码', '开票日期', '校验码', '合计金额']; //将对应的属性名转换成中文

                    const data = [];

                    export_json_to_excel(tHeader, data, '发票模板');
                })
            },
            lookLog(){

                this.show = true
                this.loading2 = true

                this.setTimeId = setInterval(()=>{
                    invoicelog({
                        "usercode":this.usercode
                    }).then(
                        (res)=>{

                            if(res.error ==0){
                                var str = '';
                                var arr =  JSON.parse(res.data);
                                for(var i =0 ;i<arr.length;i++){
                                    if(arr[i]){
                                        console.log(typeof arr[i]);
                                        str = str + arr[i] + '\n'
                                    }
                                }
                                this.logList = str;
                                this.loading2 = false
                            }

                        }
                    ).catch((error)=>{
                        this.show = false
                        this.loading2 = false
                        clearInterval(this.setTimeId)
                        console.log(error);
                    })
                },2000)
            }
        },
        beforeDestroy () {
            clearInterval(this.setTimeId)
        },
        mounted(){

        }

    }
</script>

<style scoped>
    .user-header{
        line-height: 60px;
    }
    .user-main{
        height: 780px;
        margin-top: 100px;
    }
    .textarea >>> .el-textarea__inner{
        font-family:"Microsoft" !important;
        font-size:20px !important;
        color: indianred;
    }
</style>