<template>
    <div class="selling-goods-holder">
        <div style="height: 50px;"></div>
        <message-cards 
            :sellItems="sellItems"
        ></message-cards>
        <div class="goods-pager-holder">
            <el-pagination 
                layout="prev, pager, next" 
                :total="total" 
                v-model:page-size="pageSize" 
                v-model:current-page="currentPage"
            />    
        </div>
    </div>
</template>

<script>
import MessageCards from '@/components/trade/MessagesGrid.vue'
import { get } from '@/requests/http'
import { ElMessage } from 'element-plus'
export default {

    data(){
        return {
            sellItems: [],
            pageSize: 10,
            total: 0,
            currentPage: 1
        }
    },
    components: { MessageCards },
    created(){
        this.getData()
    },
    watch: {
        currentPage(){
            this.getData()
        }
    },
    methods: {

        getData(){
            /**
            `* api: /trade/selling
             * @method: get
             * @param: currentPage, pageSize
             * @returns
             */
            let params = {
                currentPage: this.currentPage,
                pageSize: this.pageSize
            }
            get('/trade/selling',params).then((res) => {
                if(res.success){
                    this.sellItems = res.data
                    this.total = res.map.total         
                }else{
                    ElMessage({message: res.message, type: 'error'})
                }

            }).catch(err => console.log(err))


        }

    }

}
</script>

<style>
    .selling-goods-holder{
        display: flex;
        flex-direction: column;
    }
    .goods-pager-holder{
        display: flex;
        justify-content: center;
        margin-top: 20px;
    }

</style>