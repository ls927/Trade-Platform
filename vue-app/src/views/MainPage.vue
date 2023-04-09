<template>
  <div class="main-holder">
    <aside-bar></aside-bar>
    <router-view class="main-content"></router-view>
  </div>
  
</template>

<script>
import AsideBar from '@/components/AsideBar.vue';
export default {

    components:{
      AsideBar
    }

}
</script>

<style>
    .main-holder{
      display: flex;
      flex-direction: row;
      margin: 0;
    }
    .main-content{
      width: calc(100vw - 200px);
      height: 100%;
      margin: 0;
    }
</style>