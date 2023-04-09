<template>
    <div class="return-requests-holder">
        <!-- <div style="height: 40px;"></div> -->
        <h3 style="margin-left: 20px;">
            <el-icon><DocumentDelete /></el-icon>
            退货申请
        </h3>
        <div class="table-holder">
            <el-table stripe :data="returnReqs" height="600">
                <el-table-column label="日期" prop="date" width="250" fixed/>
                <el-table-column label="主题" prop="title" width="250" fixed/>
                <el-table-column label="商品" prop="goods" width="200">
                    <template #default="scope">
                        {{ scope.row.goods.name }}
                    </template>                    
                </el-table-column>
                <el-table-column label="卖家" prop="seller" width="100">
                    <template #default="scope">
                        <el-tag>
                            {{ scope.row.seller.username }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="交易价格" prop="price" width="100"/>
                <el-table-column label="其他内容" prop="content" width="400"/>
                <el-table-column label="操作" prop="operations" width="250" fixed="right">
                    <template #default="scope">
                        <template v-if="scope.row.type === 6">
                            <el-button
                                size="small"
                                type="primary"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >确认</el-button
                            >                            
                        </template >                            
                        <template v-else-if="scope.row.type === 7">
                            <el-button
                                size="small"
                                type="primary"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >退货完成</el-button
                            > 
                        </template>

                    </template>
                </el-table-column>
            </el-table>
        </div>

    </div>

</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { get, post } from '@/requests/http'
export default {

    data(){
        return {
            returnReqs: []
        }
    },
    created(){
        this.getData()
    },
    methods: {
        getData(){
            /**
             * api: /trade/returning/requests
             * @method: get
             */
            get('/trade/returning/requests').then((res) => {
                if(res.success){
                    this.returnReqs = res.data
                }else{
                    ElMessage({message: res.message, type: 'error'})
                }
            }).catch(err => console.log(err))
        },
        opreate(associationId, from, to){

                let confirmInfo = '退货是否完成？'
                let message = '已完成退货，自动跳转交易记录。。。'
                ElMessageBox.confirm(
                    confirmInfo,
                    'Warning',
                    {
                        confirmButtonText: '确认',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }
                ).then(() => {

                    /**
                     *  api: /trade
                     *  @method: POST
                     *  @param: associationId, from, to
                     */
                     let params = {
                        associationId: associationId,
                        from: from,
                        to: to,
                        content: ''
                    }                    
                    post('/trade',params,false).then((res) => {
                        if(res.success){
                            if(to === 8){
                                ElMessage({type: 'success',message: message})
                                location.replace('/records') 
                            }else{
                                location.reload()
                            } 
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err))
                    
                }).catch(() => {})                   
            


        },
    }

}
</script>

<style>

    .return-requests-holder{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .table-holder{
        margin: 0px 20px;
    }
</style>