<template>
    <div class="goods-holder">
        <operation-menu 
            :categoryOptions="categories"
            :isLogin="this.$store.getters.isLogin"
            @getData="getData"
        ></operation-menu>
        
        <goods-grid :goodsItems="goodsItems"></goods-grid>
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
import GoodsGrid from '@/components/goods/GoodsGrid.vue';
import OperationMenu from '@/components/goods/OperationMenu.vue'
import {get} from '@/requests/http'
export default {
    components: { OperationMenu , GoodsGrid},
    data(){
        return {
            goodsItems: [],
            pageSize: 10,
            currentPage: 1,
            total: 0,
            categories: [],
            keywords: '', 
            order: '', 
            category: ''     
        }
    },
    created(){
        this.getData()
        this.getCategories()
    },
    watch: {
        currentPage(){
            this.getData(this.keywords,this.order,this.category)
        }
    },
    methods: {
        getData(keywords, order, category){
            /**
             * api:  '/goods/show'
             * method: get
             * params: currentPage, pageSize, keywords, order, category
             */
            // console.log(keywords)
            // console.log(order)
            // console.log(category)
            if(!keywords) keywords = ''
            if(!order) order = 0
            if(!category) category = 0

            this.keywords = keywords
            this.order = order
            this.category = category
            
            let params = {
                currentPage: this.currentPage,
                pageSize: this.pageSize,
                keywords: keywords,
                order: order,
                category: category
            }
            get('/goods/show',params)
            .then((res)=>{
                if(res.success){
                    this.goodsItems = res.data
                    this.total = res.map.total
                }
            })
            .catch((err) => {console.log(err)})
        },
        getCategories(){
            /**
             * api:  '/goods/categories'
             * method: get
             */

            get('/goods/categories')
            .then((res) => {
                if(res.success){
                    this.categories = res.data
                }
            })
            .catch((err) => {console.log(err)})
        }
    }


}
</script>

<style>
    .goods-holder{
        display: flex;
        flex-direction: column;
        width: 100%;
        height: 100%;
        margin: 0;
    }
    .goods-pager-holder{
        display: flex;
        justify-content: center;
        margin-top: 20px;
    }
</style>