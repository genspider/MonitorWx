<template>
    <div>
        <!--增加弹框-->
        <el-dialog title="新增域名" :visible.sync="dialogFormVisible">
            <el-form :model="domainForm" ref="domainForm" label-width="100px" class="demo-ruleForm">
                <el-form-item
                        label="备注"
                        prop="name"
                        :rules="[
      { required: true, message: '所填写备注不能为空'},
    ]"
                >
                    <el-input type="name" v-model.number="domainForm.alias" style="width: 300px" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item
                        label="域名"
                        prop="name"
                        :rules="[
      { required: true, message: '所填写域名不能为空'},
    ]"
                >
                    <el-input type="name" v-model.number="domainForm.name" style="width: 300px" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('domainForm')">提交</el-button>
                    <el-button @click="resetForm('domainForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!---修改弹框--->
        <el-dialog title="微信号绑定域名" :visible.sync="dialogFormVisibleUpdate">
            <div style="text-align: center">
                <el-transfer
                        style="text-align: left; display: inline-block"
                        v-model="wxNoArr"
                        filterable
                        :left-default-checked="[2, 3]"
                        :right-default-checked="[1]"
                        :titles="['微信号列表', '域名绑定微信号列表']"
                        :button-texts="['取消绑定', '绑定该域名']"
                        :format="{
        noChecked: '${total}',
        hasChecked: '${checked}/${total}'
      }"
                        @change="handleChange"
                        :data="data">
                    <span slot-scope="{ option }">{{ option.key }} - {{ option.label }}</span>
                    <el-button class="transfer-footer" slot="left-footer" size="small" style="display: none">操作</el-button>
                    <el-button class="transfer-footer" slot="right-footer" size="small">操作</el-button>
                </el-transfer>
            </div>
        </el-dialog>
        <el-header>
            <el-button type="danger" @click="batchDel">批量删除</el-button>
            <el-button @click="clearSelected">取消全选</el-button>
            <el-button @click="dialogFormVisible = true">新增</el-button>
        </el-header>
        <el-main class="user-main">
            <el-table
                    max-height = "800px"
                    ref="table"
                    :data="tableData"
                    v-loading="tableLoading"
                    border
                    @selection-change="handleSelectionChange"
                    style="width: 80%;text-align: center;">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        fixed
                        label="序号"
                        align="center"
                        width="70">
                    <template slot-scope="scope">
                        {{scope.$index+1}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="alias"
                        label="备注"
                        sortable
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="域名"
                        sortable
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="wx_no"
                        label="微信号"
                        sortable
                        align="center"
                        width="900">
                </el-table-column>

                <el-table-column
                        fixed="right"
                        align="center"
                        label="操作"
                        width="100">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" style="color: orangered;" @click="wxDel(scope.row)">删除</el-button>
                        <!--<el-button type="text" size="small" style="color: orangered;" @click="bandWx(scope.row)">绑定微信号</el-button>-->
                    </template>
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
    </div>
</template>

<script>
    import {listDomain,insertDomain,delDomain} from '../../api/power'
    import Editor from "../../components/domain/editor";
    export default {
        name: "config",
        components: {Editor},
        data() {
            const generateData = _ => {
                const data = [];
                for (let i = 1; i <= 15; i++) {
                    data.push({
                        key: i,
                        label: `微信号 ${ i }`,
                    });
                }
                return data;
            };
            return {
                tableData: [],
                currentPage: 1, //初始页
                pageSize: 10,
                count: 0,
                pageSum: 0,
                tableLoading: false,
                pageLoading: false,
                ids:[],
                dialogFormVisible:false,
                domainForm:{
                    name:'',
                    alias:''
                },
                dialogFormVisibleUpdate:false,

                data: generateData(),
                wxNoArr: [1,2,3],
                renderFunc(h, option) {
                    return '<span>{ option.key } - { option.label }</span>';
                }
            }
        },
        computed:{

        },
        methods: {
            //搜索
            search(currentPage) {
                this.tableLoading = true;
                this.pageLoading = true;
                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage
                listDomain({
                    page: this.currentPage,
                    limit: this.pageSize,
                }).then(res => {
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
                }).catch(error => {
                    this.pageLoading = false;
                    this.tableLoading = false;
                })
            },
            wxDel(data){
                var msg;
                if(data.name instanceof Array){

                    let name = ''

                    for(var k in data.name){
                        name = name + data.name[k]+","
                    }
                    msg = '【批量删除】此操作将删除 '+name+'域名, 是否继续?'
                }else{
                    msg = '此操作将删除 '+data.name+'域名, 是否继续?'
                }

                this.$confirm(msg, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    delDomain({id:data.id}).then(res => {
                        if(res.code == 0){
                            this.$message({
                                message: res.msg,
                                type: 'success'
                            });
                            this.search();
                        }else{
                            this.$message({
                                message: res.msg,
                                type: 'error'
                            });
                        }
                    }).catch(error => {})
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    })
                })
            },
            batchDel(){
                var delIds = [];
                var delName = [];
                var ids = this.ids;
                for(var k in ids){
                    delIds.push(ids[k]['id']);
                    delName.push(ids[k]['name']);
                }
                var data = {
                    id:delIds,
                    name:delName,
                }
                this.wxDel(data)
            },
            handleSelectionChange(val){
                this.ids = val
            },
            clearSelected(){
                this.$refs.table.clearSelection()
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        insertDomain(this.domainForm).then(res=>{
                            if(res.code == 0){
                                this.$message({
                                    message: res.msg,
                                    type: 'success'
                                });
                                this.dialogFormVisible = false
                                this.resetForm('domainForm');
                                this.search();
                            }else{
                                this.$message({
                                    message: res.msg,
                                    type: 'error'
                                });
                            }
                        }).catch(err=>{

                        })
                    } else {
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            bandWx(data){
                console.log(data)
                this.dialogFormVisibleUpdate = true
            },
            handleChange(value, direction, movedKeys) {
                console.log(value, direction, movedKeys);
            }
        },
        //搜索
        mounted() {
            this.search()
        }
    }
</script>
<style scoped>
    .user-main{
        height: 780px;
    }
    .transfer-footer {
        margin-left: 20px;
        padding: 6px 5px;
    }
</style>