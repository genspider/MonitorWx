<template>
    <div>
        <el-dialog title="添加配置" :visible.sync="dialogFormVisible">
            <el-form :model="form">
                <el-form-item label="项目名" :label-width="formLabelWidth">
                    <el-select v-model="form.id" placeholder="请选择项目名" >
                        <el-option
                                v-for="item in options1"
                                :key="item.name"
                                :label="item.name"
                                :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="日志类型" :label-width="formLabelWidth">
                    <el-select v-model="form.name" placeholder="请选择日志类型">
                        <el-option
                                v-for="item in options2"
                                :key="item.name"
                                :label="item.name"
                                :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="addConfig">确 定</el-button>
            </div>
        </el-dialog>

        <el-table
                :data="tableData"
                v-loading="tableLoading"
                border
                style="width: 70%;text-align: center;margin: 0 auto;">
            <el-table-column
                    fixed
                    prop="id"
                    label="项目名"
                    align="center"
                    width="480">
            </el-table-column>

            <el-table-column
                    fixed
                    prop="name"
                    label="日志类型"
                    align="center"
                    width="480">
            </el-table-column>

            <el-table-column
                    fixed="right"
                    align="center"
                    label="操作"
                    width="150">
                <template slot="header" slot-scope="scope">
                    <div  v-if="state">
                        <el-button type= "text" size="small" @click="state = false" >编辑</el-button>
                    </div>
                    <div v-else="!state">
                        <el-button type= "text" size="small" @click="saveConfig">保存</el-button>
                        <el-button type= "text" size="small" @click="state = true">取消</el-button>
                    </div>
                </template>

                <template slot-scope="scope">
                    <div v-if="!state">
                        <el-button type= "text" size="small" @click="delConfig(scope.$index)">删除</el-button>
                    </div>
                </template>
            </el-table-column>

        </el-table>
        <div  v-if="!state">
            <i class="el-icon-plus" style="font-size: 25px;margin-left: 1280px" @click="dialogFormVisible = true"></i>
        </div>
    </div>
</template>

<script>
    export default {
        name: "listenerLogType",
        data(){
            return {
                tableData: [
                    {
                        name:111111,
                        id:111111,
                    },
                    {
                        name:11111,
                        id:11111,
                    },
                    {
                        name:11111,
                        id:1111111,
                    },
                    {
                        name:11111,
                        id:111111,
                    },
                ],
                tableLoading:false,
                state:true,
                dialogFormVisible: false,
                form: {
                    name:'',
                    id:''
                },
                options1:[
                    {name:1},
                    {
                        name:2
                    },
                    {
                        name:3
                    }
                ],
                options2:[{
                    name:1
                },{
                    name:4
                }],
                formLabelWidth: '120px',
            }
        },
        methods:{
            showDialog(){
                this.state = false
                this.dialogFormVisible = true;
            },
            addConfig(){
                var obj = {
                    name:this.form.name,
                    id:this.form.id
                }
                this.tableData.push(obj)
                this.dialogFormVisible = false
            },
            delConfig(index){
                this.tableData.splice(index,1)
            },
            saveConfig(){
                this.$message({
                    "message":"保存成功",
                    "type":"success"
                })
                this.state = true
            }
        }
    }
</script>

<style >

</style>