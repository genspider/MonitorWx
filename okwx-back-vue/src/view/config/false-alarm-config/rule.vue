<template>
    <div>
        <jian-she></jian-she>
        <el-container>
            <el-header class="user-header">
                <!--新增-->
                <el-dialog title="新增规则" :visible.sync="dialogFormVisible">
                    <el-form>
                        <el-form-item label="关键词" :label-width="formLabelWidth">
                            <div style="width: 750px;">

                                <el-input autocomplete="off" style="width: 300px;margin-left: 5px;margin-top: 5px" v-for="(v,index) in input1" v-model="input1[index].value" :key="index">
                                    <template slot="suffix">
                                        <i class="el-icon-circle-close-outline" @click="delInput(index,input1)"></i>
                                    </template>
                                </el-input>

                                <i class="el-icon-plus" style="font-size: 25px;margin-left: 30px" @click="addInput"></i>
                            </div>
                        </el-form-item>
                        <el-form-item label="条件1" :label-width="formLabelWidth">
                            <div style="width: 750px;">
                                <el-radio v-model="where1" label="1" >请求参数</el-radio>
                                <el-radio v-model="where1" label="2" >结果</el-radio>
                            </div>
                        </el-form-item>
                        <el-form-item label="条件2" :label-width="formLabelWidth">
                            <div style="width: 750px;float: left">
                                <el-radio v-model="where2" label="1" >出现</el-radio>
                                <el-radio v-model="where2" label="2" >没有</el-radio>
                                关键词:<el-input autocomplete="off" style="width: 300px"></el-input>
                                <i class="el-icon-plus" style="font-size: 25px;margin-left: 30px"></i>
                            </div>
                        </el-form-item>
                        <el-form-item label="条件3" :label-width="formLabelWidth">
                            <div style="width: 750px;">
                                <el-radio v-model="where3" label="1"  >每</el-radio>
                                <el-input autocomplete="off" style="width: 300px"></el-input>
                                秒匹配
                                <el-input autocomplete="off" style="width: 300px"></el-input>
                                次
                                <el-radio v-model="where3" label="2" >每</el-radio>
                                <el-input autocomplete="off" style="width: 300px"></el-input>
                                秒少于
                                <el-input autocomplete="off" style="width: 300px"></el-input>
                                次
                            </div>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer" style="width: 200px;margin: 0 auto">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                    </div>
                </el-dialog>

                <!--编辑-->
                <div style="background: grey;width: 100%;height: 100%;z-index: 100;position: absolute;background-color:rgba(96,96,96,0.15);top: 0px;left: 0px" v-show="isShow">
                    <div style="width: 800px;height: 800px;background: white;margin: 0px auto;position: relative;top: 50px">
                        <i  :class="gbicon?'el-icon-circle-close-outline':'el-icon-circle-close'" style="font-size: 55px;position: absolute;top: 20px;right: 20px" @mouseover="gbicon1" @mouseout="gbicon2" @click="closeDialog('updateForm')"></i>
                        <span style="color: orangered;font-size: 20px;position: absolute;top: 30px;left: 30px">编辑</span>
                        <div style="width: 600px;height: 600px;position: absolute;top: 100px;left: 100px">
                            <el-form :model="updateForm" ref="updateForm" label-width="100px" class="demo-ruleForm">
                                <el-form-item label="规则ID" prop="phone">
                                    <el-input v-model="updateForm.phone"></el-input>
                                </el-form-item>
                                <el-form-item label="名称" prop="user">
                                    <el-input v-model="updateForm.user"></el-input>
                                </el-form-item>
                                <el-form-item label="规则" prop="role">
                                    <el-input v-model="updateForm.role"></el-input>
                                </el-form-item>
                                <el-form-item label="状态" prop="rolename">
                                    <el-input v-model="updateForm.rolename"></el-input>
                                </el-form-item>

                                <el-form-item>
                                    <el-button type="primary" @click="submitForm(updateForm)"v-loading="updateLoading">完成</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </div>
                </div>

                <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="primary"  @click="dialogFormVisible = true" >新增规则</el-button>
                <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="success"  @click="start">全部启动</el-button>
                <el-button plain style="float: left;margin-left: 50px;margin-top:6px;height: 50px" type="info"  @click="stop">全部停止</el-button>
            </el-header>
            <el-main class="user-main">
                <el-table
                        :data="tableData"
                        class="loading-area"
                        height="700"
                        border
                        style="width: 100%;text-align: center">
                    <el-table-column
                            fixed
                            label="序号"
                            align="center"
                            min-width="50">
                        <template slot-scope="scope">
                            {{scope.$index+1}}
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="phone"
                            label="规则ID"
                            align="center"
                            min-width="50">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="user"
                            label="名称"
                            align="center"
                            min-width="50">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="rolename"
                            label="规则"
                            align="center"
                            min-width="150">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="role"
                            label="状态"
                            align="center"
                            min-width="50">
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            align="center"
                            label="操作"
                            width="200">
                        <template slot-scope="scope">

                            <el-button type="text" size="small" @click="showDialog(scope.$index,scope.row)" >编辑</el-button>
                            <el-button type="text" size="small" style="color: red;" @click="delById">删除</el-button>
                            <el-button type="text" size="small" style="color: green;"@click="startById">启动</el-button>
                            <el-button type="text" size="small" style="color: gold;"@click="disById">停止</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import JianShe from "../../../components/jianShe";
    export default {
        name: "rule",
        components: {JianShe},
        data() {
            return {
                input1:[
                    {
                        id:0,
                        value:''
                    },
                ],
                isShow:false,
                input2Sum:1,
                where1:'1',
                where2:'1',
                where3:'1',
                where4:'1',
                dialogFormVisible: false,
                formLabelWidth: '120px',
                tableData: [
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        rolename: '上海',
                        role: '普陀区',
                    },
                ],
                updateForm:{
                    user: '',
                    phone: '',
                    rolename: '',
                    role: '',
                },
                id:0,
                isshow:false,
                updateLoading:false,
                gbicon:false,
            }
        },
        methods:{
            start(){
                this.$message({
                    message: "开始",
                    type: 'success'
                });
            },
            stop(){
                this.$message({
                    message: "结束",
                    type: 'success'
                });
            },
            delById(){
                this.$message({
                    message: "删除",
                    type: 'success'
                });
            },
            startById(){
                this.$message({
                    message: "开始",
                    type: 'success'
                });
            },
            disById(){
                this.$message({
                    message: "停止",
                    type: 'success'
                });
            },
            //关闭添加框
            closeDialog(list){
                this.isShow = false;
                this.$refs[list].resetFields()
            },
            //打开添加框
            showDialog(index,row){
                console.log(row)
                this.updateForm.user = row.user
                this.updateForm.phone = row.phone
                this.id = row.id
                this.updateForm.role = row.role
                this.updateForm.rolename = row.rolename
                console.log(this.updateForm)
                this.isShow = true;
            },
            //关闭按钮
            gbicon1(){
                this.gbicon = true
            },
            //关闭按钮
            gbicon2(){
                this.gbicon = false
            },
            addInput(){
                console.log(this.input1.length)
                if(this.input1.length <3){
                    var obj = {
                        'name':'',
                        'id':this.input1.length
                    }
                    this.input1.push(obj)
                    return
                }
                this.$message({
                    message: "文本框上限为3个",
                    type: 'error'
                });
            },
            delInput(index,input1){
               // var arr = this.input1;
                if(this.input1.length >1){
                    /*for(var i = this.input1.length-1;i>=0;i--){
                        input1.splice(i-1,1)
                    }*/
                    input1.splice(index,1)
                }else{
                    this.$message({
                        message: "文本框不能少于一个",
                        type: 'error'
                    });
                    return
                }
            }
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