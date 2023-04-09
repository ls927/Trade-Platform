import { createStore } from "vuex";

import { get } from "@/requests/http";

const store = createStore({
    state:{
        isLogin: false,
        userInfo: {}
    },
    getters: {

        isLogin: (state) => state.isLogin,

        username: (state) => state.userInfo.username,

        id: (state) => state.userInfo.id,

        buyingOpsCount: (state) => state.userInfo.buyingOpsCount,

        sellingOpsCount: (state) => state.userInfo.sellingOpsCount,

        returningReqsCount: (state) => state.userInfo.returningReqsCount,

        returningMsgsCount: (state) => state.userInfo.returningMsgsCount

    },
    mutations:{

        changeIsLogin(state, data){
            state.isLogin = data
        },
        changeUserInfo(state, data){
            state.userInfo = Object.assign({}, data)
        }

    },
    actions: {
        
        getUserInfo(context){
            
            get('/user/info').then((res) => {

                if(res.success){
                    context.commit('changeIsLogin',true)
                    context.commit('changeUserInfo',res.data)
                }else{
                    context.commit('changeIsLogin',false)
                }

            })

        }

    }
})

export default store