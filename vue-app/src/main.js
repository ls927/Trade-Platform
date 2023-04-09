import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router/router.js'
import store from './store/store.js'

/**
 * Note: 
 * app.use 用于在 Vue 应用程序中安装插件。插件可以添加全局级别的功能或实用程序，并可能会影响整个应用程序。
 * 例如，你可以使用 app.use 安装 Vuex 状态管理库，从而使其在应用程序的所有组件中都可用。
 * 
 * app.component 用于定义新的 Vue 组件。组件是应用程序中基本的构建块，并允许开发人员将用户界面划分为独立、可重复使用的部分。
 * 通过使用 app.component 方法，你可以定义组件的选项，包括模板、数据、计算属性和生命周期钩子函数等。
 */
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}    

app.use(ElementPlus)
    .use(router)
    .use(store)
    .mount('#app')