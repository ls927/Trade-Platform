<template>
    <div class="buying-messages-holder">
        <!-- <div style="height: 40px;"></div> -->
        <h3 style="margin-left: 20px;">
            <el-icon><Goods /></el-icon>
            我的购买
        </h3>
        <div class="table-holder">
            <el-table stripe :data="buyInfos" height="600">
                <el-table-column label="日期" prop="date"  width="250" fixed/>
                <el-table-column label="商品" prop="goods" width="200" fixed>
                    <template #default="scope">
                        {{ scope.row.goods.name }}
                        &nbsp;
                        <el-tag effect="light" :type="scope.row.goods.status == 2 ? 'danger' : scope.row.goods.status == 1 ? 'warning' : 'success'">
                            <span>{{ scope.row.goods.status == 2 ? '锁定' : scope.row.goods.status == 1 ? '意向' : '架上'}}</span>
                        </el-tag>
                    </template>                    
                </el-table-column>
                <el-table-column label="主题" prop="title" width="250"/>
                <el-table-column label="卖家" prop="seller" width="100" >
                    <template #default="scope">
                        <el-tag>
                            {{ scope.row.seller.username }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="买家" prop="buyer" width="100">
                    <template #default="scope">
                        <el-tag>
                            {{ scope.row.buyer.username }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="交易价格" prop="price" width="100"/>
                <el-table-column label="其他内容" prop="content" width="400"/>
                <el-table-column label="操作" prop="operations"  width="250" fixed="right">
                    <template #default="scope">
                        <template v-if="scope.row.type === 0">
                            <template v-if="scope.row.goods.status === 2">
                                <el-button 
                                    size="small"    
                                    type="primary"
                                    @click="checkGoodsDetails(scope.row.goods.id)"
                                    >商品详情</el-button
                                >                                
                            </template>
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, cancel)"
                                >取消交易</el-button
                            >                            
                        </template>
                        <template v-else-if="scope.row.type === 1">
                            <el-button 
                                    size="small"    
                                    type="primary"
                                    @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                    >确认</el-button
                            > 
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, cancel)"
                                >取消交易</el-button
                            >                            
                        </template>
                        <template v-else-if="scope.row.type === 2">
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, cancel)"
                                >取消交易</el-button
                            >                            
                        </template>
                        <template v-else-if="scope.row.type === 3">
                            <el-button
                                size="small"
                                type="primary"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >交易完成</el-button
                            >
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, cancel)"
                                >取消交易</el-button
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
import { get, post } from '@/requests/http'
export default {

    data(){
        return {
            buyInfos: [],
            cancel: -1,
        }
    },
    created(){
        this.getData()
    },
    methods: {
        getData(){
            /**
             * api: /trade/buying
             * method: get
             */
            get('/trade/buying').then((res) => {
                if(res.success){
                    this.buyInfos = res.data
                }else{
                    ElMessage({message: res.message, type: 'error'})
                }
            }).catch(err => console.log(err))
            
        },
        opreate(associationId, from, to){

            let confirmInfo, message
            switch (to) {
                case this.cancel:
                    confirmInfo = '是否取消交易？若是请输入原因';
                    message =  '交易已取消！';
                    break;
                case 2:
                    confirmInfo = '是否接受邀约？'
                    message = '已接受邀约！'
                    break;
                case 4:
                    confirmInfo = '是否完成交易?';
                    message = '交易已完成！自动跳转至交易记录。。。';
                    break; 
            }

            if(to === this.cancel){
                ElMessageBox.prompt(confirmInfo, 'Tip', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                }).then(({ value }) => {
                    
                    /**
                     *  api: /trade
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
                            location.replace('/records')                         
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
                )
                .then(() => {

                    /**
                     *  action: /trade
                     *  method: POST
                     *  params: associationId, from, to
                     */
                    // console.log(associationId)
                    // console.log(from)
                    // console.log(to)
                    let params = {
                        associationId: associationId,
                        from: from,
                        to: to,
                        content: ''
                    }
                    post('/trade',params,false).then((res) => {
                        if(res.success){
                            ElMessage({type: 'success',message: message})
                            if(to === 4){
                                location.replace('/records')
                            }else{
                                location.reload()                    
                            }                            
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err))                    
                    

                })
                .catch(() => {})
            }
        },
        checkGoodsDetails(id){
            this.$router.push({name: 'GoodsDetails', params: {'goodsId': id}})
        }
    }

}
</script>

<style>

    .selling-messages-holder{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .table-holder{
        margin: 0px 20px;
    }
</style>