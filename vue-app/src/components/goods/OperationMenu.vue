<template>
    <div class="op-menu-holder">
        <el-button type="primary" class="op-menu-btn" @click="putupGoods" v-if="isLogin">上架商品</el-button>
        <div class="flex-grow"></div>
        
        <el-select v-model="category" clearable class="op-menu-select-1" placeholder="商品类别">
            <el-option
                v-for="item in categoryOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
        </el-select>

        <el-select v-model="order" clearable class="op-menu-select-2" placeholder="默认排序">
            <el-option
                v-for="item in orderOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
        </el-select>

        <el-input v-model="keywords" class="op-menu-input" placeholder="搜索" @change="search">
            <template #prefix>
                <el-icon><search /></el-icon>
            </template>
        </el-input>

        <put-up-goods ref="dialog" :categories-options="categoryOptions"></put-up-goods>


    </div>

    


</template>

<script>

import PutUpGoods from './PutUpGoods.vue'


const orderOptions = [
  {
    value: 1,
    label: '价格升序',
  },
  {
    value: 2,
    label: '价格降序',
  }
]

export default {
    
    props:{
        categoryOptions: [],
        isLogin: Boolean
    },
    data(){
        return {
            category: "",
            order: "",
            orderOptions,
            keywords:''
        }
    },
    components: { PutUpGoods },
    watch:{

        category(){
            this.$emit('getData', this.keywords, this.order, this.category)
        },
        order(){
            this.$emit('getData', this.keywords, this.order, this.category)
        }

    },
    methods: {

        search(){
            this.$emit('getData', this.keywords, this.order, this.category)
        },
        putupGoods(){
            this.$refs.dialog.drawerVisible = true
        }

    }

}
</script>

<style>
    .op-menu-holder{
        height: 80px;
        display: flex;
        flex-direction: row;
        align-items: center;
        margin: 0;
    }

    .op-menu-btn{
        margin: auto 40px;
        width: 100px;
        height: 30px;
    }

    .op-menu-select-1{
        height: 32px;
        width: 100px;
    }

    .op-menu-select-2{
        height: 32px;
        width: 100px;
        margin: auto 20px;
        
    }

    .op-menu-input{
        height: 32px;
        width: 250px;
        margin: auto 20px;
    }

</style>