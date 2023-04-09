<template>
  <el-menu
    :default-active="activeIndex"
    mode="horizontal"
    :ellipsis="false"
    router
    @select="menuItemClick"
  >
    
    <div class="title" @click="this.$router.push('/home')">We Trade</div>
    <div class="flex-grow" />

    <template v-if="isLogin">
      <el-menu-item index="/goods">
        <el-icon :size="20"><UserFilled /></el-icon>
        {{username}}
      </el-menu-item>
      <el-menu-item @click="logout" index="/logout">退出</el-menu-item>
    </template>
    <template v-else>
      <el-menu-item index="/login">登录</el-menu-item>
      <el-menu-item index="/register">注册</el-menu-item>
    </template>


  </el-menu>


</template>

<script>
import { mapGetters } from 'vuex'
import { get } from '@/requests/http'
import { ElMessage } from 'element-plus'
export default {

    computed: {
      ...mapGetters([
        'isLogin',
        'username'
      ])
    },
    methods:{
      logout(){
        /**
         * api: /user/logout
         * method: get
         * params: -
         */
        get('/user/logout').then((res) => {
          if(res.success){
            if(res.message){
              ElMessage({message: res.message, type: 'success'})
            }
            location.replace('/')
          }else{
            // do nothing
          }
        }).catch(err => { console.log(err) })
        
      }
    }
}
</script>
 
<style>


  .title{
    display: inline-block;
    margin-top: 8px;
    margin-left: 30px;
    text-align: center;
    font-family: Impact, Haettenschweiler, 'Arial Narrow Bold', sans-serif;
    font-size: xx-large;
  }

  .flex-grow {
    flex-grow: 1;
  }

</style>