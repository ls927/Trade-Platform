<template>
    <div class="selling-messages-holder">
        <div class="back-up-nav">
            <el-link href="/selling" :underline="false" style="margin: 20px auto auto 20px;">
                <el-icon size="x-large"><Back/></el-icon>&nbsp;返回我的出售
            </el-link>
        </div>
        <h3 style="margin-left: 20px;">
            <el-icon><Sell /></el-icon>
            出售商品 
            <span>{{ goods.name }}</span>
            &nbsp;
            <el-tag effect="light" size="larger" :type="goods.status == 3 ? '' : goods.status == 2 ? 'danger' : goods.status == 1 ? 'warning' : 'success'">
                <span>{{ goods.status == 3 ? '已出售' : goods.status == 2 ? '锁定' : goods.status == 1 ? '意向' : '架上'}}</span>
            </el-tag>

        </h3>
        <div class="table-holder">
            <el-table stripe :data="sellInfos" height="600">
                <el-table-column label="日期" prop="date" width="250" fixed/>
                <el-table-column label="主题" prop="title" width="250" fixed/>
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
                        <template v-if="scope.row.type === 0">
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
                                >拒绝</el-button
                            >                            
                        </template>
                        <template v-else-if="scope.row.type === 1">
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
                                type="primary"
                                @click="opreate(scope.row.associationId, scope.row.type, scope.row.type + 1)"
                                >进行交易</el-button
                            >
                            <el-button
                                size="small"
                                type="danger"
                                @click="opreate(scope.row.associationId, scope.row.type, cancel)"
                                >取消交易</el-button
                            >                            
                        </template>
                        <template v-else-if="scope.row.type === 4">
                            <el-button
                                size="small"
                                type="primary"
                                @click="checkTradingRecord()"
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
import { get, post } from '@/requests/http'
export default {

    data(){
        return {
            cancel: -1,
            sellInfos: [],
            goods: {}
        }
    },
    created(){
        this.getData()
    },
    methods: {
        getData(){
            /**
             * api: /trade/selling/{goodsId}
             * @method: get
             * @param: goodsId
             */
            let url = '/trade/selling/' + this.$route.params.goodsId
            get(url).then((res) => {
                if(res.success){
                    this.goods = res.data
                    this.sellInfos = res.map.sellInfos
                }else{
                    ElMessage({type: 'error', message: res.message})
                }
            }).catch(err => console.log(err))

            
        },        
        opreate(associationId, from, to){
            
            let confirmInfo,message

            switch (to) {
                case this.cancel:
                    confirmInfo = from === 0 ? '是否拒绝该出价？' : '是否取消交易？若是请输入原因：';
                    message = from === 0 ? '已拒绝出价,交易已取消！' : '交易已取消！';
                    break;
                case 1:
                    confirmInfo = "请输入交易时间和地点。注意格式为： '时间：yyyy-MM-hh HH:mm 地点：xxx'"
                    message = "交易已邀约!"
                    break;
                case 3:
                    confirmInfo = '是否进行交易？';
                    message = '交易进行中。。。';
                    break; 
            }

            if(to === 1 || (from !== 0 && to === this.cancel)){
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
                            if(to === this.cancel){
                               location.replace('/records')   
                            }else{
                                location.reload()
                            }                    
                                                   
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err))                        

                }).catch(() => {})

            }
            else{
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
                            if(res.message){
                                ElMessage({type: 'success',message: message})                                
                            }

                            if(to === this.cancel){
                               location.replace('/records')   
                            }else{
                                location.reload()
                            }                    
                                                     
                        }else{
                            ElMessage({message: res.message, type: 'error'})
                        }
                    }).catch(err => console.log(err))  

                                      
                    
                }).catch(() => {})                
            }


        },
        checkTradingRecord(){
            location.replace('/records')
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

    .back-up-nav{
        height: 40px;
    }

    .table-holder{
        margin: 0px 20px;
    }
</style>