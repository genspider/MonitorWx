<template>
    <div style="padding-left: 20px;padding-top: 20px">
        <el-container>
            <el-header height="30px"><span style="color: dodgerblue;">{{getTitle}}</span></el-header>
            <el-main>
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="文章标题">
                        <el-input v-model="form.title" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="文章内容">
                        <el-input
                                prefix="文章内容"
                                ref="textarea"
                                class="textarea"
                                type="textarea"
                                autosize
                                v-model="form.content">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="标题图片">
                        <el-upload
                                class="upload-demo"
                                action=""
                                :file-list="fileList"
                                :auto-upload=false
                                :on-change="onchangeImg"
                                :on-remove="handleRemoveImg"
                                :limit="1"
                                accept=".jpg"
                                list-type="picture">
                            <el-button size="small" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                        </el-upload>
                        <!--<el-input v-model="form.title_pic"></el-input>-->
                    </el-form-item>
                    <el-form-item label="pc端模板">
                        <el-select v-model="form.pc_tpl" filterable  clearable placeholder="请选择pc端模板">
                            <el-option
                                    v-for="item in tplSelect"
                                    :key="item.file_name"
                                    :label="item.file_name"
                                    :value="item.file_name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="m端模板">
                        <el-select v-model="form.mb_tpl" filterable  clearable placeholder="请选择pc端模板">
                            <el-option
                                    v-for="item in tplSelect"
                                    :key="item.file_name"
                                    :label="item.file_name"
                                    :value="item.file_name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="版权信息">
                        <el-input v-model="form.public_info" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item label="统计代码">
                        <el-input
                                prefix="文章内容"
                                ref="textarea"
                                class="textarea"
                                type="textarea"
                                autosize
                                v-model="form.code">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="浏览量">
                        <el-input v-model="form.hits" style="width: 100px"></el-input>
                    </el-form-item>
                    <el-form-item label="别名">
                        <el-input v-model="form.alias" style="width: 300px"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">立即创建</el-button>
                        <el-button @click="$router.back(-1)">返回</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import {ldySearchOne,ldyAdd,tplSelect} from "../../api/template"
    export default {
        data() {
            return {
                fileList: [
                    {name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'},
                    {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}
                    ],
                form: {
                    title:'',
                    content:'',
                    title_pic:'',
                    pc_tpl:'',
                    mb_tpl:'',
                    public_info:'',
                    code:'',
                    hits:'',
                    alias:'',
                },
                id:0,
                tplSelect:[],
                param:""
            }
        },
        computed:{
            getTitle(){
                if(this.id == 0){
                    return "增加落地页";
                }else{
                    return "编辑落地页"
                }
            }
        },
        methods: {
            onSubmit() {
                if(this.param == ""){
                    this.param = new FormData();
                }
                for(var k in this.form){
                    if(k != 'title_pic'){
                        console.log(k,this.form[k])
                        this.param.append(k,this.form[k])
                    }
                }
                if(this.id != 0){
                    this.param.append('id',this.id)
                }
                ldyAdd(this.param).then(res=>{
                    if(res.code == 0){
                        this.$message({
                            message: res.msg,
                            type: 'success'
                        })
                        //this.back()
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        })
                    }
                }).catch(err=>{})

            },
            searchDara(id){
                ldySearchOne({id:id}).then(res=>{
                    if(res.code == 0){
                        this.form = res.data.list
                        this.tplSelects();
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        })
                    }
                }).catch(err=>{})
            },
            back(){
                this.$router.go(-1);
            },
            tplSelects(){
                tplSelect().then(res=>{
                    if(res.code == 0){
                        this.tplSelect = res.data
                    }else{
                        this.$message({
                            message: res.msg,
                            type: 'error'
                        })
                    }
                }).catch(err=>{})
            },
            onchangeImg(file,filesList) {
                this.param = new FormData();
                this.param.append('title_pic', file.raw, file.name);
            },
            handleRemoveImg(){
                this.param.delete("title_pic")
                this.param = ""
            }
        },

        mounted() {
            this.id = this.$route.params.id
            if(this.id != 0){
                this.searchDara(this.id)
            }else{
                this.tplSelects();
            }
        }
    }
</script>
<style scoped>
    input{
        width: 300px !important;
    }
    .textarea >>> .el-textarea__inner{
        width: 70%;
        font-family:"Microsoft" !important;
    }
</style>