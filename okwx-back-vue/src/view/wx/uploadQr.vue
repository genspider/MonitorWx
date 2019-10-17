<template>
    <div>

        <el-container>
            <el-header class="user-header">
                <div style="width: 90%;margin: 0px auto">
                    
                    <el-upload
                            style="float: left;margin-left: 50px"
                            class="upload-demo"
                            action=""
                            :auto-upload=false
                            :on-change="onchangeImg"
                            :on-remove="handleRemoveImg"
                            :multiple = true
                            accept=".jpg"
                            ref="img">
                        <el-button plain size="small" type="primary" style="height: 50px">点击上传图片</el-button>
                        <div slot="tip" class="el-upload__tip" style="color: red;margin-left: 15px">只能上传后缀为.jpg的文件</div>
                    </el-upload>
          
                    <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="primary" @click="sub" v-loading="loading" >提交</el-button>
                    <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="primary" @click="clean" >清空</el-button>
                    <!--          <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="info" @click="lookLog" >查看执行日志</el-button>-->
                </div>
            </el-header>

        </el-container>

    </div>
</template>

<script>

    import { Message } from 'element-ui';
    import {uploadQr}from '../../api/power'
    export default {
        name: "upload",
        data(){
            return {
                param:"",
                loading:false,
                img:false,
                imgList:[],
            }
        },
        methods:{
            sub(){
                if(!this.img){
                    Message({
                        showClose: true,
                        message: "请选择二维码图片",
                        type: 'error'
                    });
                    return
                }
                if(this.param != ""){
                    this.param.delete("file[]");
                    var imgList = this.imgList;
                    for(var k in imgList){
                        this.param.append('file[]', imgList[k].row, imgList[k].name);
                    }
                }


                this.loading = true
                uploadQr(this.param).then((res) =>{
                    this.loading = false
                    if(res.code == 0){
                        Message({
                            showClose: true,
                            message:res.msg,
                            type: 'success'
                        });
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
            onchangeImg(file,filesList) {

                if(this.param == ""){
                    this.param = new FormData();
                }
                this.img = true;
                this.imgList.push({row:file.raw,name:file.name});
            },
            handleRemoveImg(file,filesList){
                var imgList = this.imgList;

                for(var k in imgList){
                    if(file.name == imgList[k].name){
                        imgList.splice(k,1);
                    }
                }
                if(imgList == []){
                    this.img = false;
                }
            },
            clean(){
                this.$refs.img.clearFiles()
                this.imgList = [];
                this.img = false;
            },
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
    
</style>