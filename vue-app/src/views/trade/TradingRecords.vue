<template>
    <div class="trading-records-holder">
        <!-- <div style="height: 40px;"></div> -->
        <h3 style="margin-left: 20px;">
            <el-icon><Tickets /></el-icon>
            交易记录
        </h3>
        <div class="table-holder">
            <el-table stripe :data="records" height="600">
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
                        <template v-if="scope.row.type === 4 && scope.row.buyer.id === id">
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >申请退货</el-button
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
import { mapGetters } from 'vuex'
import { get, post } from '@/requests/http'
export default {

    data(){
        return {
            records:[]
        }
    },
    created(){
        this.getData()
    },
    computed: {
        ...mapGetters(['id'])
    },
    methods: {
        getData(){
            /**
             * api: /trade/records
             * @method get
             * 
             */
            get('/trade/records').then((res) => {
                if(res.success){
                    this.records = res.data
                }else{
                    ElMessage({type: 'error', message: res.message})
                }
            })
        },
        opreate(associationId, from, to){

            let confirmInfo = '是否申请退货？若是请填写原因'
            let message = '已申请退货，自动跳转退货申请。。。'
            ElMessageBox.prompt(confirmInfo, 'Tip', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
            }).then(({ value }) => {

                /**
                 *  api: /trade
                 *  @method: POST
                 *  @param: associationId, from, to, content(value)
                 */
                let params = {
                        associationId: associationId,
                        from: from,
                        to: to,
                        content: value
                }
                post('/trade',params,false).then((res) => {
                    if(res.success){
                        ElMessage({
                            type: 'success',
                            message: message + {value},
                        })
                        location.replace('/returning/requests')
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

    .trading-records-holder{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }

    .table-holder{
        margin: 0px 20px;
    }
</style>