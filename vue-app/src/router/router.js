import { createRouter,createWebHistory } from "vue-router";
import store from "@/store/store";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/LoginPage.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/RegisterPage.vue')
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('../views/HomePage.vue')
    },
    {
        path: '/', /*注意在嵌套路由时，外层路由必须以 / 结尾*/
        component: () => import('../views/MainPage.vue'),
        children: [
            {
                path: '',
                redirect: '/goods'
            },
            {
                path:'goods',
                name: 'Goods',
                component: () => import('../views/goods/FilteredGoods.vue')
            },
            {
                path: 'goods/details/:goodsId',
                name: 'GoodsDetails',
                component: () => import('../views/goods/GoodsDetails.vue')  
            },
            {
                path: 'selling',
                name: 'SellingGoods',
                component: () => import('../views/trade/SellingGoods.vue')
            },
            {
                path: 'selling/:goodsId/messages',
                name: 'SellingMessages',
                component: () => import("../views/trade/SellingMessages.vue")
            },
            {
                path: 'buying/messages',
                name: 'BuyingMessages',
                component: () => import("../views/trade/BuyingMessages.vue")
            },
            {
                path: '/records',
                name: 'TradingRecords',
                component: () => import("../views/trade/TradingRecords.vue")
            },
            {
                path: '/returning/requests',
                name: 'ReturnRequests',
                component: () => import("../views/after/ReturnRequests.vue")
            },
            {
                path: '/returning/messages',
                name: 'ReturnMessages',
                component: () => import("../views/after/ReturnMessages.vue")
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes: routes
})

// 路由全局前置守卫
router.beforeEach(() => {
    store.dispatch('getUserInfo')
    return true;
})

export default router