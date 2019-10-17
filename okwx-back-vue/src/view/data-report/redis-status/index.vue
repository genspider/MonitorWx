<template>
    <div>
        <el-container>
            <el-header class="user-header">
                <div style="position: relative;top: 10px;left: 150px;font-family:'Microsoft' ;font-size:20px">redis状态</div>
            </el-header>
            <div  >

                <el-main class="user-main" v-loading="loading2">
                    <div style="width: 90%;margin: 0px auto">
                        <el-input
                                prefix="redis状态"
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

    import JianShe from "../../../components/jianShe";
    import {configList,pyinfoCnfig,redisConfig} from '../../../api/data-report'
    import {redis_info} from '../../../config/redis'
    export default {
        name: "index",
        components: {JianShe},
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
                    redisConfig().then(res=>{
                        this.loading2 = false
                        if(res.error == 0){
                            /* var str = this.swapJSON(res.data)
                             this.logList = str*/

                            var str = ""
                            str = this.swapJSON(res.data)
                            this.logList = str
                            /* console.log( res.data)
                             var temps = res.data.split("#")

                             var tempa =[]

                             for(var i=0,len = temps.length;i<len;i++){
                                 var arr = temps[i].split("\n");
                                 str = str + arr[0]+"\n"
                             }
                             console.log(str)*/
                            /*console.log(1)
                            var temps = res.data.split("#")
                           // console.log(temps)
                            var tempa =[]
                            for(var i=0,len = temps.length;i<len;i++){
                                console.log(i)
                                console.log(temps[i].split('\n'))
                           //     tempa[i].push()
                            }
                            console.log(tempa)*/
                            /*var  temp ={}
                             var temp = res.data.split('\n')
                            // console.log(temp)
                             var obj ={}
                             for(var v of temp){
                                 var temp2 = v.split(":")
                                 obj[temp2[0]] =  temp2[1]
                             }
                             var o ={

                             }
                             for(var k in obj){
                                 var k2 = redis_info[k]
                                 var v2 = obj[k]

                                 o[k2] = v2

                             }

                             var str = '';
                             for(var ok in o){
                                 if(ok != "undefined")
                                 {
                                     str = str + ok + "   :    " + o[ok] + "\n"
                                 }

                             }
                             this.logList = str*/

                        }else{
                            this.$message({
                                message: res.msg,
                                type: 'error'
                            });
                            this.loading2 = false
                            clearInterval(this.setTimeId)
                        }
                    }).
                    catch(err=>{
                        this.loading2 = false
                        clearInterval(this.setTimeId)
                    })
                },2000)

            },
            swapJSON(json) {
                var  temp ={}
                var temp = json.split('\n')
                // console.log(temp)
                var obj ={}
                for(var v of temp){
                    var temp2 = v.split(":")
                    obj[temp2[0]] =  temp2[1]
                }
                var o ={

                }
                for(var k in obj){
                    var k2 = redis_info[k]
                    var v2 = obj[k]
                    o[k2] = v2
                }

                var str = '';
                for(var ok in o){
                    if(ok != "undefined")
                    {
                        str = str + ok + "   :    " + o[ok] + "\n"
                    }
                }
                return str
            }
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
        font-size:15px !important;
        color: black !important;
    }
</style>