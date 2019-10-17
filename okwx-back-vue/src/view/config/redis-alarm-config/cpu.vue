<template>
    <div>
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="微信号">
                <el-input type="textarea"  v-model="form.wxNo" class="textarea" style="" placeholder="各个微信号之间请用英文逗号隔开"></el-input>
            </el-form-item>
            <el-form-item label="倍率">
                <el-input v-model="form.beilv" filterable  style="width: 7rem"></el-input>
                <el-select v-model="form.domain" placeholder="请选择域名">
                    <el-option
                            v-for="item in domainSelect"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                    </el-option>
                </el-select>
                <el-button type="primary" @click="onSubmit" v-loading = "form.loading">增加</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import {insertPower,selectDomain} from "../../../api/power"
    export default {
        name:'cpu',
        data() {
            return {
                form: {
                    wxNo: '',
                    beilv:1,
                    domain:'',
                    loading:false
                },
                domainSelect:[],

            }
        },
        methods: {
            onSubmit() {
                if(this.form.beilv == null ||this.form.beilv == ''){
                    this.form.beilv = 0
                    return
                }
                if(this.form.wxNo == null ||this.form.wxNo == ''){
                    this.$message({
                        message: "请填写微信号",
                        type: 'error'
                    });
                    return
                }
                this.form.loading = true
                insertPower(this.form).then(res=>{
                    if(res.code == 0){
                        this.$message({
                            message: res.msg,
                            type: 'success'
                        });
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                    this.form.loading = false
                }).catch(err=>{
                    this.form.loading = false
                })
            },
            searchDomain(){
                selectDomain().then(res=>{
                    if(res.code == 0){
                        this.domainSelect = res.data.list;
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        });
                    }
                }).catch(err=>{

                })
            }
        },
        mounted() {
            this.searchDomain();
        }
    }
</script>

<style scoped>
    .textarea >>> .el-textarea__inner{
        font-family:"Microsoft" !important;
        font-size:14px !important;
        color: indianred;
        height: 15rem;
        width: 50rem;
    }
</style>