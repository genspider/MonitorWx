<template>
    <div>
        <jian-she></jian-she>
        <el-container>
            <div style="background: grey;width: 100%;height: 100%;z-index: 100;position: absolute;background-color:rgba(96,96,96,0.15);top: 0px;left: 0px" v-show="isShow">
                <div style="width: 800px;height: 800px;background: white;margin: 0px auto;position: relative;top: 50px">

                    <i  :class="gbicon?'el-icon-circle-close-outline':'el-icon-circle-close'" style="font-size: 55px;position: absolute;top: 20px;right: 20px" @mouseover="gbicon1" @mouseout="gbicon2" @click="closeDialog('ruleForm')"></i>

                    <div style="width: 600px;height: 600px;position: absolute;top: 100px;left: 100px">
                        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                            <el-form-item label="用户名" prop="name">
                                <el-input v-model="ruleForm.name"></el-input>
                            </el-form-item>
                            <el-form-item label="角色" prop="user">
                                <el-input v-model="ruleForm.user"></el-input>
                            </el-form-item>
                            <el-form-item label="手机号码" prop="phone">
                                <el-input v-model="ruleForm.phone"></el-input>
                            </el-form-item>
                            <el-form-item label="邮箱" prop="email">
                                <el-input v-model="ruleForm.email"></el-input>
                            </el-form-item>
                            <el-form-item label="权限" prop="role">
                                <el-input v-model="ruleForm.role"></el-input>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                                <el-button @click="resetForm('ruleForm')">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
            <el-header class="user-header">
                账号:
                <el-input style="width: 300px">

                </el-input>
                手机号码:
                <el-input style="width: 300px">

                </el-input>
                邮箱:
                <el-input style="width: 300px">

                </el-input>
                &nbsp;&nbsp;
                <el-button icon="el-icon-search" circle></el-button>
                <el-button type="danger" @click="showDialog" plain>
                    新增
                </el-button>
            </el-header>
            <el-main class="user-main">
                <el-table
                        :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                        border

                        style="width: 100%;text-align: center">
                    <el-table-column
                            fixed
                            prop="id"
                            label="id"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            fixed
                            prop="user"
                            label="角色"
                            width="200">
                    </el-table-column>
                    <el-table-column
                            prop="phone"
                            label="电话号码"
                            width="400">
                    </el-table-column>
                    <el-table-column
                            prop="email"
                            label="邮箱"
                            width="400">
                    </el-table-column>
                    <el-table-column
                            prop="role"
                            label="权限"
                            width="200">
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="300">
                        <template slot-scope="scope">
                            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
                            <el-button type="text" size="small" @click="showDialog">编辑</el-button>
                            <el-button type="text" size="small">删除</el-button>
                            <el-button type="text" size="small">启动</el-button>
                            <el-button type="text" size="small">禁用</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div style="width: 60px;height: 30px;margin: 0px auto;position: absolute;">
                    共<span style="color:red">{{tableData.length}}</span>页
                </div>

                <el-pagination
                        style="margin-left: 1000px;margin-top: 20px;width: 30px"
                        background
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="pagesize"
                        layout="prev, pager, next"
                        :total="tableData.length">
                </el-pagination>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import JianShe from "../../../components/jianShe";
    export default {
        name: "index",
        components: {JianShe},
        data() {
            return {
                tableData: [
                    {
                        id:1,
                        user: '2016-05-1111103',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                    {
                        id:1,
                        user: '2016-05-03',
                        phone: '王小虎',
                        email: '上海',
                        role: '普陀区',
                    },
                ],
                ruleForm: {
                    name: '',
                    user:'',
                    phone:'',
                    email:'',
                    role:''
                },
                rules: {
                    name: [
                        { required: true, message: '请输入活动名称', trigger: 'blur' },
                        { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                    ],
                    region: [
                        { required: true, message: '请选择活动区域', trigger: 'change' }
                    ],
                    date1: [
                        { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                    ],
                    date2: [
                        { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
                    ],
                    type: [
                        { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
                    ],
                    resource: [
                        { required: true, message: '请选择活动资源', trigger: 'change' }
                    ],
                    desc: [
                        { required: true, message: '请填写活动形式', trigger: 'blur' }
                    ]
                },
                gbicon:false,
                isShow:false,
                currentPage:1, //初始页
                pagesize:10,
            }
        },
        methods:{
            closeDialog(list){
                this.isShow = false;
                this.$refs[list].resetFields()
            },
            showDialog(){
                this.isShow = true;
            },
            gbicon1(){
                this.gbicon = true
            },
            gbicon2(){
                this.gbicon = false
            },
            handleClick(row){
                console.log(row)
            },
            resetForm(list){
                this.$refs[list].resetFields();
            },
            handleSizeChange: function (size) {
                this.pagesize = size;
                console.log(this.pagesize)  //每页下拉显示数据
            },
            handleCurrentChange: function(currentPage){
                this.currentPage = currentPage;
                console.log(this.currentPage)  //点击第几页
            },
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