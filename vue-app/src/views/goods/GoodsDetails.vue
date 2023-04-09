<template>
    <div class="goods-details-holder">
        <el-row class="back-up-nav">
            <el-col :span="24" >
                <el-link href="/goods" :underline="false" style="margin: 20px auto auto 20px;">
                    <el-icon size="x-large"><Back/></el-icon>&nbsp;返回商店
                </el-link>
            </el-col>
        </el-row>
        <el-row style="height: 65%;" v-if="this.goodsDetails.name">
            <el-col :span="1"></el-col>
            <el-col :span="8">
                <el-carousel 
                    indicator-position="outside" 
                    pause-on-hover="true"
                    height="380px" 
                    class="goods-details-img-carousel"
                >
                    <el-carousel-item v-for="(item,index) in goodsDetails.imgSrcs" :key="index">
                        <img :src="item" class="goods-details-img"/>
                    </el-carousel-item>
                </el-carousel>
            </el-col>
            <el-col :span="14" >
                <div style="height: 100%;display: flex;flex-direction: column;margin-left: 50px;">
                    <div class="goods-details-title">{{ goodsDetails.name }}</div>
                    <div class="goods-details-items"><span>参考价：<span style="color:red">￥{{ goodsDetails.price }}</span></span></div>
                    <div class="goods-details-items">
                        <span>
                            卖家：
                            <el-tag effect="light">
                                <span>{{ goodsDetails.seller }}</span>
                            </el-tag>
                        </span>
                    </div>
                    <div class="goods-details-items">上架时间：{{ goodsDetails.putUpTime }}</div>
                    <div class="goods-details-items">
                        <span>
                            商品状态：
                            <el-tag effect="light"
                                    :type="goodsDetails.status == 2 ? 'danger' :
                                     goodsDetails.status == 1 ? 'warning' : 'success'">
                                    <span>{{ goodsDetails.status == 2 ? '锁定' : goodsDetails.status == 1 ? '意向' : '架上'}}</span>
                            </el-tag>
                        </span>
                    </div>
                    <div class="goods-details-status">
                        <el-scrollbar >
                            <div style="display:flex;">
                                <div v-for="item in goodsDetails.associations" :key="item.buyer" class="goods-details-association-item">
                                    <span style="font-weight: bold;">{{ item.buyer }}</span> 
                                        出价 
                                    <span style="color: red;font-weight: bolder;">￥{{ item.price }}</span>
                                </div>
                            </div>
                        </el-scrollbar>

                    </div>
                        <div class="goods-details-items">
                            <template v-if="id !== goodsDetails.sellerId">
                                <div style="display: inline-flex;">
                                    <el-input v-model="offerPrice" style="width: 100px;" >
                                        <template #prefix>
                                            <span style="color: red">￥</span>
                                        </template>
                                    </el-input>
                                    <div style="width: 5px"></div>
                                    <el-button @click="offer" type="primary" style="width: 60px;height: 34px;">出价</el-button>
                                </div>                                
                            </template>
                            <template v-else>
                                <div style="display: inline-flex;">
                                    <el-button @click="checkMessage" type="primary" style="width: 100px;height: 34px;">查看信息</el-button>
                                    <div style="width: 20px"></div>
                                    <el-button @click="takeDownGoods" type="danger" style="width: 100px;height: 34px;">下架商品</el-button>
                                </div>     
                            </template>
                        </div>
                    </div>
            </el-col>
        </el-row>
        <el-row style="height: 30%;margin-left:40px" v-if="this.goodsDetails.name">
            <el-col :span="1"></el-col>
            <el-col :span="22">
                <div style="font-size: larger;font-weight: bold;">商品描述</div>
                <div style="height: 10px;"></div>
                <div>{{ goodsDetails.description }}</div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { ElMessageBox } from 'element-plus'
import {ElMessage} from 'element-plus'
import {get, post} from '@/requests/http'
export default {

    data(){
        return {
            goodsDetails: {},
            offerPrice: '',
        }
    },
    created(){
        this.getData()
    },
    computed:{
        ...mapGetters(['id'])
    },
    methods: {

        getData(){
            /**
             * api: /goods/details/{goodsId}
             * @method: post
             * @param: goodsId
             * @returns: goodsDetails
             */
            let url = '/goods/details/' + this.$route.params.goodsId
            get(url).then((res) => {
                if(res.success){
                    this.goodsDetails = res.data
                }else{
                    ElMessage({message: res.message, type: 'error'})
                }
            }).catch(err => console.log(err))
        },

        offer(){

            if(!/^\d{1,3}(\.\d{2})?$/.test(this.offerPrice)){
                ElMessage({
                    type: 'error',
                    message: '价格格式错误！'
                })
            }else{
                /**
                 * api: /goods/bid
                 * @method post
                 * @param goodsId, offerPrice
                 */
                let params = {
                    goodsId: this.$route.params.goodsId,
                    offerPrice: this.offerPrice
                }
                post('/goods/bid',params,false).then((res) => {
                    if(res.success){
                        ElMessage({
                            type: 'success',
                            message: res.message
                        })
                        location.replace('/buying/messages') 
                    }else{
                        ElMessage({message: res.message, type: 'error'})
                    }
                }).catch(err => console.log(err))                
                              
            }

            
        },

        checkMessage(){
            location.href = '/selling/' + this.goodsDetails.id + '/messages'
        },
        
        takeDownGoods(){
            ElMessageBox.confirm(
                '确定下架商品',
                'Warning',
                {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            ).then(() => {
                /**
                *  api: /goods/takedown/{goodsId}
                *  @method: POST
                *  @param: goodsId
                */
                let url = '/goods/takedown/' + this.goodsDetails.id
                post(url).then((res) => {
                    if(res.success){
                        ElMessage({
                            type: 'success',
                            message: res.message
                        }) 
                        location.replace('/')                       
                    }else{
                        ElMessage({
                            type: 'error',
                            message: res.message
                        })  
                    }
                }).catch(err => console.log(err))

            }).catch()
        }
    }

}
</script>

<style>
    .goods-details-holder{
        width: 100%;
        height: 100%;
        margin: 0;
        
    }
    .back-up-nav{
        height: 5%;
        align-items: center;
    }
    .goods-details-img-carousel{
        margin: 32px 40px auto 40px;
        width: calc(100% - 2*40px);
        border: 2px solid whitesmoke;
        border-radius: 8px;
    }
    .goods-details-img{
        height: 361.93px; /* 保持图片是正方形 */
        width: 100%;
    }

    .goods-details-title{
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        font-size: larger;
        font-weight: bolder;
    }

    .goods-details-items{
        flex-grow: 0.5;
        display: flex;
        flex-direction: column;
        justify-content: center;
        font-size: medium;
        font-weight: bold;
    }

    .goods-details-offer{
        flex: 0.5;
        display: flex;
        flex-direction: column;
        justify-content: center;
        font-size: medium;
        font-weight: bold;
    }

    .goods-details-status{
        flex: 2.5;
        border: 1.5px solid rgb(220,223,230);
        border-radius: 8px;
    }

    .goods-details-association-item{
        flex-shrink: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 160px;
        height: 120px;
        margin: 10px;
        text-align: center;
        border-radius: 4px;
        background: var(--el-color-primary-light-9);
        color: var(--el-color-primary);
    }



</style>