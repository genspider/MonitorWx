<template>
    <div>

        <el-container>
            <el-header class="user-header">
                <div style="position: relative;top: 10px;left: 150px;font-family:'Microsoft' ;font-size:20px">日志</div>
            </el-header>
            <div>

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
    import {get_journal} from '../../../api/data-report'
    export default {
        name: "falseReport",
        data(){
            return {

                logList:'',

                setTimeId:'',

                loading2:false
            }
        },
        methods:{

            lookLog(){
                this.loading2 = true

                this.setTimeId = setInterval(()=>{
                    get_journal()
                        .then(res=>{
                            if(res.error == 0){
                                if(res.data == null){
                                    res.data = ""
                                }
                                this.logList = res.data + this.logList
                                if(this.logList != ""){
                                    this.loading2 = false
                                }
                            }else{
                                this.$message({
                                    message: res.msg,
                                    type: 'error'
                                });
                                this.loading2 = false
                                clearInterval(this.setTimeId)
                            }

                        })
                    .catch(err=>{
                        this.loading2 = false
                        clearInterval(this.setTimeId)
                    })
                },1000)
            },
        },
        beforeDestroy () {
            clearInterval(this.setTimeId)
        },
        mounted(){
            this.lookLog()
        }

    }
</script>

<style scoped>
    .user-header{
        line-height: 60px;
    }
    .user-main{
        height: 780px;
        margin-top: 10px;
    }
    .textarea >>> .el-textarea__inner{
        font-family:"Microsoft" !important;
        font-size:20px !important;
        color: indianred;
    }
</style>