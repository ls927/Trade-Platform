<template>
    <el-drawer
        v-model="drawerVisible"
        title="商品上架"
        direction="rtl"
        show-close="false"
    >
        <el-form
            ref="formRef"
            :model="form"
            :rules="formRules"
            label-position="top"
        >
            <el-form-item label="商品名称" prop="name">
                <el-input v-model="form.name" style="width: 300px"/>
            </el-form-item>
            <el-form-item label="商品描述" prop="description">
                <el-input v-model="form.description" type="textarea" rows="2" style="width: 400px"></el-input>
            </el-form-item>
            
            
            <el-form-item label="商品类别" prop="category">
                <el-select v-model="form.category" style="width: 100px;">
                    <el-option
                        v-for="item in categoriesOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="商品定价" prop="price">
                <el-input v-model="form.price" style="width: 100px;">
                    <template #prefix>
                        <span style="font-weight: bold;">￥</span>
                    </template>
                </el-input>
            </el-form-item>                         
        </el-form>

        <div style="margin-bottom: 8px;">商品图片</div>
        <el-upload 
            action="#" 
            list-type="picture-card" 
            :auto-upload="false" 
            limit="3"
            v-model:file-list="fileList"
            :on-exceed="handleExceed"
            ref="uploadRef"
        >

            <template #tip>
                <div class="el-upload__tip text-red">
                    最多上传3张图片，否则新图片会覆盖旧图片
                </div>
            </template>

            <el-icon><Plus /></el-icon>

            <!-- 展示上传图片的缩略图 -->
            <template #file="{ file }">
                <div>
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                    <span class="el-upload-list__item-actions">
                    <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)"
                    >
                        <el-icon><zoom-in /></el-icon>
                    </span>
                    <!-- <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleDownload(file)"
                    >
                        <el-icon><Download /></el-icon>
                    </span> -->
                    <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove(file)"
                    >
                        <el-icon><Delete /></el-icon>
                    </span>
                    </span>
                </div>
            </template>
        </el-upload>

        <!-- 预览图片：全图 -->
        <el-dialog v-model="previewVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>

        <div class="put-up-goods-btn-holder">
            <el-button type="primary" @click="putup()">
                确认上架
            </el-button>
            <div style="flex-grow: 1;"></div>
            <el-button type="info" @click="cancelPutup()">
                取消上架
            </el-button>                
        </div>

    </el-drawer>

</template>

<script>
import { ElMessage } from 'element-plus'
import { post } from '@/requests/http'
export default {
    
    props:{
        categoriesOptions: []
    },
    
    data(){
        return{
            drawerVisible: false,
            form: {
                name: '',
                description: '',
                category: '',
                price: ''
            },
            formRules: {
                name: [
                    { required: true, message: '请输入商品名称', trigger: 'blur' },
                    { pattern: /^[a-zA-Z\u4E00-\u9FA5][a-zA-Z0-9_\-\u4E00-\u9FA5]*$/, message: "名称格式不正确"},
                    { max: 20, message: "名称长度不大于 20 字符"}
                ],
                description:[
                    { required: true, message: '请输入商品详情', trigger: 'blur' },
                    { pattern: /^.{1,300}$/, message: '商品详情字符数在 1 到 300 之间'}
                ],
                category: [
                    { required: true, message: '请选择类别', trigger: 'blur' },
                    { validator: this.checkCategory }
                ],
                price: [
                    { required: true, message: '请输入价格', trigger: 'blur' },
                    { pattern: /^[0-9]+(\.[0-9]+)?$/, message: '价格格式不正确！' }
                ]
            },
            fileList: [],
            dialogImageUrl: '',
            previewVisible: false,
            disabled: false
        }
        
    },
    methods: {

        /**
         * @param files 超过限制文件数量的文件
         * @param uploadFiles 限制数量内的文件列表
         * 
         * 另外, uploadRef 有如下方法:
         *   abort	取消上传请求	(file: UploadFile) => void
         *   submit	手动上传文件列表	() => void
         *   clearFiles	清空已上传的文件列表（该方法不支持在 before-upload 中调用）
         *      (status?: 
         *          Array<"ready" | "uploading" | "success" | "fail">
         *       ) => void
         *   handleStart	手动选择文件    (file: UploadFile) => void
         *   handleRemove	手动移除文件    (file: UploadFile) => void
         * 
         */
        handleExceed(files, uploadFiles){
            this.$refs.uploadRef.handleRemove(uploadFiles[0])
            this.$refs.uploadRef.handleStart(files[0])
        },
        handlePictureCardPreview(file){
            this.previewVisible = true
            this.dialogImageUrl = file.url
        },
        handleRemove(file){
            this.$refs.uploadRef.handleRemove(file)
        },

        checkCategory(rule, value, callback){
            if(value == ''){
                callback(new Error('请选择类别！'))
            }else{
                callback()
            }
        },


        putup(){
            // 校验表单和图片
            // 提交表单和图片,成功提交时回调函数
            // 清空表单和图片并关闭抽屉
            this.$refs.formRef.validate(valid => {
                if(valid){
                    if(this.fileList.length > 0){
                        /**
                        * api: /goods/putup
                        * method: post
                        * @param: formData 
                        * @return: goodsId
                        */
                        let data = new FormData()
                        data.append('name',this.form.name)
                        data.append('description',this.form.description)
                        data.append('category',this.form.category)
                        data.append('price',this.form.price)
                        for(let i in this.fileList){
                           data.append('images',this.fileList[i].raw) 
                        }

                        post('/goods/putup', data, true)
                        .then((res) => {
                            if(res.success){
                                if(res.message){
                                    ElMessage({message: res.message, type: 'success'})
                                }
                                let url = '/goods/details/' + res.data
                                location.replace(url)
                            }else{
                                if(res.message){
                                    ElMessage({message: res.message, type: 'error'})
                                }                                
                            }
                        })
                        .catch(err => console.log(err))
    
                    }else{
                        return ElMessage({
                            message: '请上传商品图片！',
                            type: 'warning',
                        })
                    }
                }
            })             

        },
        cancelPutup(){
            // 清空表单和图片并关闭抽屉
            this.form = []
            this.$refs.uploadRef.clearFiles()
            this.drawerVisible = false
        }

    }


}
</script>

<style>
    .put-up-goods-btn-holder{
        display: flex; 
        flex-direction: row; 
        margin-top: 80px; 
        width: 400px;
    }

</style>