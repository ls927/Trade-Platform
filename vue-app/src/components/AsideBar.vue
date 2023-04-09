<template>
    <el-menu
        class='bar'
        ref="menuRef"
        router=true
        :default-active="active"
    >
        <el-menu-item index="/">
            <template #title>
                <el-icon><Shop /></el-icon>
                <span>商店</span>
            </template>
        </el-menu-item>
        
        <el-sub-menu index="2" v-if="isLogin">
            <template #title>
                <el-icon><Message /></el-icon>
                <span>交易信息</span>
                <el-badge :value="tradingOpsCount" class="badge" v-if="tradingOpsCount"></el-badge>                     
            </template>
            <el-menu-item index="/buying/messages">
                <template #title>
                    <span>我的购买</span>
                    <el-badge :value="buyingOpsCount" class="badge" v-if="buyingOpsCount"></el-badge>
                </template>    
            </el-menu-item>
            <el-menu-item index="/selling">
                <template #title>
                    <span>我的出售</span>
                    <el-badge :value="sellingOpsCount" class="badge" v-if="sellingOpsCount"></el-badge>
                </template>
            </el-menu-item>
            <el-menu-item index="/records">
                <template #title>               
                    <span>交易记录</span>   
                </template>
            </el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="3" v-if="isLogin">
            <template #title>
                <el-icon><Promotion /></el-icon>
                <span>售后信息</span>
                <el-badge :value="returningOpsCount" class="badge" v-if="returningOpsCount"></el-badge>  
            </template>
            <el-menu-item index="/returning/requests">
                <template #title>
                    <span>退货申请</span>
                    <el-badge :value="returningReqsCount" class="badge" v-if="returningReqsCount"></el-badge>  
                </template>                
            </el-menu-item>
            <el-menu-item index="/returning/messages">
                <template #title>
                    <span>退货信息</span>
                    <el-badge :value="returningMsgsCount" class="badge" v-if="returningMsgsCount"></el-badge>
                </template>                 
            </el-menu-item>
        </el-sub-menu>        
    </el-menu>  
</template>

<script>
import { mapGetters } from 'vuex'
export default {
    data(){
        return {
        }
    },
    created(){
        this.$store.dispatch('getUserInfo')
    },
    computed: {
        ...mapGetters([
            'isLogin',
            'buyingOpsCount',
            'sellingOpsCount',
            'returningReqsCount',
            'returningMsgsCount',
        ]),
        tradingOpsCount(){
            return this.buyingOpsCount + this.sellingOpsCount
        },
        returningOpsCount(){
            return this.returningReqsCount + this.returningMsgsCount
        },
        active(){

            let routeName = this.$route.name
            if(routeName == 'Goods' || routeName == 'GoodsDetails'){
                return '/'
            }else if(routeName == 'SellingGoods' || routeName == 'SellingMessages'){
                return '/selling'
            }else{
                return this.$route.path
            }
        }
    },
    methods: {
        
    }

}
</script>

<style>
  .bar{
    width: 200px;
    height: 100%;
    margin: 0;
  }

  .badge{
    margin-bottom: 50px;
  }

</style>