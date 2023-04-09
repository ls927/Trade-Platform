<template>
  <div class="goods-grid-holder">
      <div v-for="item in sellItems" :key="item.id" class="goods-item">
          <img class="goods-item-img" :src="item.previewImgSrc"/>
          <div class="goods-item-title">
              <el-tag effect="light" size="small" :type="item.status == 2 ? 'danger' : item.status == 1 ? 'warning' : 'success'">
                  <span>{{ item.status == 2 ? '锁定' : item.status == 1 ? '意向' : '架上'}}</span>
              </el-tag>
              {{ item.name.concat(item.description).substring(0,28) }}
          </div>
          <div class="goods-item-btn-holder">
            <el-button 
                @click="checkGoodsDetails(item.id)" 
                class="goods-item-btn"
            >   
                查看详情
            </el-button>
            
            <el-badge :value="item.opsCount" :hidden = "item.opsCount == 0">
                <el-button 
                    @click="checkTradeProcess(item.id)" 
                    class="goods-item-btn"
                >
                    进度
                </el-button>
            </el-badge>

            <!-- <el-badge is-dot style="margin-bottom:12px;"></el-badge> -->
          </div>
      </div>    
  </div>

</template>

<script>
export default {

    props:{
        sellItems: [],
        itemOpsCount: {}
    },
    methods: {
      checkGoodsDetails(id){
        this.$router.push({ name: 'GoodsDetails',params: { goodsId : id } })
      },
      checkTradeProcess(id){
        this.$router.push({ name: 'SellingMessages',params: { goodsId : id } })
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

    .goods-item-btn-holder{
        width: 192px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin: 10px 5px auto 5px ;
    }

    .goods-item-btn{
        height: 24px;
        margin: 0;
        font-size: xx-small;
    }
</style>