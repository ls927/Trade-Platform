<template>
  <div class="goods-grid-holder">
    <div 
        v-for="item in goodsItems" 
        :key="item.id" 
        @click="clickItem(item.id)" 
        class="goods-item"
    >
        <img class="goods-item-img" :src="item.previewImgSrc"/>
        <div class="goods-item-title">
            <el-tag effect="light" size="small" :type="item.status == 2 ? 'danger' : item.status == 1 ? 'warning' : 'success'">
                <span>{{ item.status == 2 ? '锁定' : item.status == 1 ? '意向' : '架上'}}</span>
            </el-tag>
            {{ item.name.concat(item.description).substring(0,28) }}
        </div>
        <div class="goods-item-price-holder">
            <span class="goods-item-cny-symbol">￥</span>
            <span class="goods-item-price">{{ item.price }}</span> 
        </div>
    </div>
  </div>
</template>

<script>


export default {

    props: {
        goodsItems: [] 
    },
    methods: {
        clickItem(id){
            this.$router.push({name: 'GoodsDetails',params: {goodsId : id}})
        }
    }

}
</script>

<style>

    .goods-grid-holder{
        display: grid;
        grid-template-columns: repeat(5, 1fr);
        grid-template-rows: repeat(2, 255px);
        grid-column-gap: 45px;
        grid-row-gap: 30px;
        margin: 10px 50px 0px 70px;
    }

    .goods-item{
        background-color: rgb(250,250,250);
        display: flex;
        flex-direction: column;
        border-radius: 15px;
    }

    .goods-item:hover{
        background-color:whitesmoke;
    }

    .goods-item-img{
        margin: 25px 35px 10px 35px;
        width: 131.96px;
        height: calc(202px - 2*35px);
    }

    .goods-item-title{
        margin: 10px 5px 0px 5px;
        height: 36px;
        font-size: x-small;
    }

    .goods-item-price-holder{
        margin: 10px auto auto 5px ;
    }

    .goods-item-cny-symbol{
        color: red;
        font-size: small;
        font-weight: bolder;
    }

    .goods-item-price{
        color: red;
        font-size: large;
        font-weight: bolder;
    }


</style>