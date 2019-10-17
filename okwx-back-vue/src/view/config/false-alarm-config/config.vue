<template>
    <div>
        <el-header>
            <!--<el-button type="danger" @click="batchDel">批量下线</el-button>
            <el-button @click="clearSelected">取消全选</el-button>-->
            <el-dialog
                    title="增加用户"
                    :visible.sync="isShow"
                    width="500px"
                    @close="cleanForm">

                <el-form :model="ruleForm">
                    <el-form-item label="类型：">
                        <el-select v-model="ruleForm.power" filterable  >
                            <el-option
                                    v-for="item in powerSelect"
                                    :key="item.power"
                                    :label="item.text"
                                    :value="item.power">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分组：">
                        <el-input v-model="ruleForm.groups" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="账号名称：">
                        <el-input v-model="ruleForm.user_no" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码：">
                        <el-input v-model="ruleForm.cellphone" style="width: 300px"></el-input>
                    </el-form-item>
                </el-form>

                <div slot="footer" class="dialog-footer" >
                    <el-button @click="cleanForm">取 消</el-button>
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                </div>
            </el-dialog>
            微信号:
            <el-input style="width: 300px" v-model="keyWords.user_no"></el-input>

            <el-select v-model="keyWords.groups" filterable  clearable  @change="search(1)" placeholder="请选择分组">
                <el-option
                        v-for="item in domainSelect"
                        :key="item.groups"
                        :label="item.groups"
                        :value="item.groups">
                </el-option>
            </el-select>
            <el-select v-model="pageSize" @change="search(1)" placeholder="每页条数">
                <el-option
                        v-for="item in pageSelect"
                        :key="item.pageSize"
                        :label="item.pageSize"
                        :value="item.pageSize">
                </el-option>
            </el-select>
            <el-select v-model="keyWords.is_online" clearable @change="search(1)"placeholder="在线状态">
                <el-option
                        v-for="item in onlineSelect"
                        :key="item.online"
                        :label="item.text"
                        :value="item.online">
                </el-option>
            </el-select>
            <el-button icon="el-icon-search" @click="search(1)" circle></el-button>
            <el-button type="danger" @click="showDialog()" plain>
                新增
            </el-button>
        </el-header>
        <el-main class="user-main">
            <el-table
                    max-height = "800px"
                    ref="table"
                    :data="tableData"
                    v-loading="tableLoading"
                    border
                    style="width: 100%;text-align: center;">

                <el-table-column
                        fixed
                        label="序号"
                        align="center"
                        width="200">
                    <template slot-scope="scope">
                        {{scope.$index+1}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="groups"
                        label="分组"
                        sortable
                        align="center"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="user_no"
                        label="账号"
                        sortable
                        align="center"
                        width="500">
                </el-table-column>
                <el-table-column
                        label="在线状态"
                        align="center"
                        width="100">
                    <template slot-scope="scope">
                        <!--<a :href="tableData[scope.$index].wwwqrcode" target="_blank">{{scope.row.wwwqrcode}}</a>-->
                        <!--is_online-->
                        <div v-if="tableData[scope.$index].is_online == 0" style="width: 10px;height: 10px;background: #F56C6C;border-radius: 5px;margin: 0px auto">

                        </div>
                        <div v-else="tableData[scope.$index].is_online == 1" style="width: 10px;height: 10px;background: #67C23A;border-radius: 5px;margin: 0px auto">

                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="last_login_time"
                        label="最近一次登录时间"
                        align="center"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="last_out_time"
                        label="最近一次退出登录时间"
                        align="center"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="cellphone"
                        label="手机号码"
                        align="center"
                        width="300">
                </el-table-column>
                <el-table-column
                        prop="ip"
                        label="ip"
                        align="center"
                        width="200">
                </el-table-column>

            </el-table>
            <div style="width: 90px;height: 30px;">
                共<span style="color:#F56C6C">{{pageSum}}</span>页|共<span style="color:#F56C6C">{{count}}</span>条
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
    import {listPower,deletePower,selectDomain,insertPower} from '../../../api/power'
    import {userList,addUser,groups} from '../../../api/fans'
    export default {
        name: "config",
        data() {
            return {
                pageSelect:[{
                    pageSize:10
                },{
                    pageSize:20
                },{
                    pageSize:30
                },{
                    pageSize:40
                },{
                    pageSize:50
                }
                ],
                onlineSelect:[{
                    online:0,
                    text:"下线"
                },{
                    online:1,
                    text:"在线"
                },
                ],
                powerSelect:[{
                    power:0,
                    text:"员工"
                }/*,{
                    power:1,
                    text:"老大"
                },*/
                ],
                keyWords:{
                    user_no:'',
                    groups:'',
                    is_online:''
                },
                domainSelect:[

                ],
                tableData: [],
                currentPage: 1, //初始页
                pageSize: 10,
                count: 0,
                pageSum: 0,
                tableLoading: false,
                pageLoading: false,
                ids:[],
                ruleForm: {
                    user_no:'',
                    groups:'',
                    cellphone:'',
                    power:0
                },
                isShow:false,
            }
        },

        methods: {
            searchDomain(){
                groups().then(res=>{
                    if(res.code == 0){
                        this.domainSelect = res.data.list;
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(err=>{})
            },
            //搜索
            search(currentPage) {

                !currentPage ? this.currentPage = 1 : this.currentPage = currentPage
                this.tableLoading = true;
                this.pageLoading = true;

                var obj = {
                    page: this.currentPage,
                    limit: this.pageSize,
                    user_no:this.keyWords.user_no,
                    groups:this.keyWords.groups,
                    is_online:this.keyWords.is_online
                }
                userList(obj).then(res => {
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
                if(data.wx_no instanceof Array){
                    let wx = ''

                    for(var k in data.wx_no){
                        wx = wx + data.wx_no[k]+","
                    }

                    msg = '【批量下线】此操作将下线 微信号为'+wx+'的用户, 是否继续?'
                }else{
                    msg = '此操作将下线 微信号为'+data.wx_no+'的用户, 是否继续?'
                }

                this.$confirm(msg, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deletePower({id:data.id,qrcode:data.qrcode}).then(res => {
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
                        message: '已取消下线'
                    })
                })
            },
            batchDel(){
                var delIds = [];
                var delWxNo = [];

                var qrcode = [];
                var ids = this.ids;
                for(var k in ids){
                    delIds.push(ids[k]['id']);
                    delWxNo.push(ids[k]['wx_no']);
                    qrcode.push(ids[k]['qrcode']);
                }
                var data = {
                    id:delIds,
                    wx_no:delWxNo,
                    qrcode:qrcode
                }
                this.wxDel(data)
            },
            handleSelectionChange(val){
                this.ids = val
            },
            clearSelected(){
                this.$refs.table.clearSelection()
            },
            handleEdit:function(index,row){
                //遍历数组改变editeFlag
                console.log(row)
                console.log(index);
                this.tableData[index].editeFlag=true;
            },
            handleSave:function(index,row){
                //保存数据，向后台取数据
                console.log(row)
                console.log(index);
                var obj = {
                    wxNo: row.wx_no,
                    beilv:row.multiplying_power,
                    domain:row.domain,
                }
                insertPower(obj).then(res=>{
                    if(res.code == 0){
                        this.$message({
                            message: "修改成功",
                            type: 'success'
                        });
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(err=>{

                })
                this.tableData[index].editeFlag=false;
            },
            submitForm(){
                addUser(this.ruleForm).then(res=>{
                    var error = res.code
                    var msg = res.msg
                    this.ruleLoading = false
                    if(error == 0){
                        this.$message({
                            message: msg,
                            type: 'success'
                        });
                        this.search(this.currentPage)
                        this.isShow = false
                    }else{
                        this.$message({
                            message: msg,
                            type: 'error'
                        });
                    }})
            },
            showDialog(){
                this.isShow = true;
            },
            //窗口关闭事件
            cleanForm(){
                this.ruleForm.user_no = ""
                this.ruleForm.groups = ""
                this.isShow = false
            },
        },
        //搜索
        mounted() {
            this.search()
            this.searchDomain()
        }
    }
</script>
<style scoped>
    .user-main{
        height: 780px;
    }
</style>