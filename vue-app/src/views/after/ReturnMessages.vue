<template>
    <div class="return-messages-holder">
        <!-- <div style="height: 40px;"></div> -->
        <h3 style="margin-left: 20px;">
            <el-icon><Box /></el-icon>
            退货信息
        </h3>
        <div class="table-holder">
            <el-table stripe :data="returnMsgs" height="600">
                <el-table-column label="日期" prop="date" width="250" fixed/>
                <el-table-column label="主题" prop="title" width="250" fixed/>
                <el-table-column label="商品" prop="goods" width="200">
                    <template #default="scope">
                        {{ scope.row.goods.name }}
                    </template>                    
                </el-table-column>
                <!-- <el-table-column label="卖家" prop="seller" >
                    <template #default="scope">
                        <el-tag>
                            {{ scope.row.seller }}
                        </el-tag>
                    </template>
                </el-table-column> -->
                <el-table-column label="买家" prop="buyer" width="100">
                    <template #default="scope">
                        <el-tag>
                            {{ scope.row.buyer.username }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="交易价格" prop="price" width="100"/>
                <el-table-column label="其他内容" prop="content" width="400"/>
                <el-table-column label="操作" prop="operations" width="250" fixed="right">
                    <template #default="scope">
                        <template v-if="scope.row.type === 5">
                            <el-button
                                size="small"
                                type="primary"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >同意退货</el-button
                            >
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, this.cancel)"
                                >拒绝退货</el-button
                            >                             
                        </template>
                        <template v-if="scope.row.type === 8">
                            <el-button
                                size="small"
                                type="primary"
                                @click="checkTradingRecords()"
                                >查看交易记录</el-button
                            >                            
                        </template>                           
                        <template v-else>
                        </template>

                    </template>
                </el-table-column>
            </el-table>
        </div>

    </div>

</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus'
import { get , post} from '@/requests/http'
export default {

    data(){
        return {
            cancel: -1,
            returnMsgs:[],
        }
    },
    created(){
        this.getData()
    },
    methods: {
        getData(){
            /**
             * api: /trade/returning/messages
             * @method: get
             */
            get('/trade/returning/messages').then((res) => {
                if(res.success){
                    this.returnMsgs = res.data
                }else{
                    ElMessage({message: res.message, type: 'error'})
                }
            }).catch(err => console.log(err))

        },
        opreate(associationId, from, to){

            let confirmInfo, message 

            switch (to) {
                case this.cancel:
                    confirmInfo = '是否拒绝退货？';
                    message =  '已拒绝退货申请！';
                    break;
                case 6:
                    confirmInfo = "请输入退货时间和地点。注意格式为： '时间：yyyy-MM-hh HH:mm 地点：xxx'";
                    message = '已发起退货邀约！';
                    break; 
            }


            if(to === 6){
                ElMessageBox.prompt(confirmInfo, 'Tip', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                }).then(({ value }) => {
                    /**
                     *  action: /trade
                     *  method: POST
                     *  params: associationId, from, to, content(value)
                     */
                     let params = {
                        associationId: associationId,
                        from: from,
                        to: to,
                        content: value
                    }                    
                    post('/trade',params,false).then((res) => {
                        if(res.success){
                            ElMessage({type: 'success',message: message + `${value}`})
                            location.reload()
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err)) 

                }).catch(() => {})                
            }else{
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
                     *  action: /trade
                     *  method: POST
                     *  params: associationId, from, to
                     */
                     let params = {
                        associationId: associationId,
                        from: from,
                        to: to,
                        content: ''
                    }                    
                    post('/trade',params,false).then((res) => {
                        if(res.success){
                            ElMessage({type: 'success',message: message})
                            location.reload()
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err))                 

                }).catch(() => {})   
            }



        },
        checkTradingRecords(){
            location.replace('/records')
        }
    }

}
</script>

<style>

    .return-messages-holder{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .table-holder{
        margin: 0px 20px;
    }
</style>