<template>  
    <CommonHeader class="header"></CommonHeader>
    <router-view class="content"></router-view>
</template>


<script>
import CommonHeader from './components/CommonHeader.vue'
export default {
  name: 'App',
  components: {
    CommonHeader
  }
}
</script>

<style>

    .header{
      width: 100%;
    }

    .content{
      height: calc(100vh - 60px);
    }

</style>
