<template>
  <div class="login-holder">
    <img src="../assets/we-trade2.png" style="height: 60px;width: auto;margin-bottom: 20px;"/>
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      status-icon
      :rules="rules"
      style="width: 250px"
    >
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" autocomplete="off">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" autocomplete="off">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" class="btn">登录</el-button>
      </el-form-item>        

    </el-form>
  </div>


</template>

<script>
import { post } from '@/requests/http'
import { ElMessage } from 'element-plus'
export default {
    data(){
      return {
        loginForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [
            { required: true, message: '请输入昵称', trigger: 'blur' },
            { pattern: /^[a-zA-Z\u4E00-\u9FA5][a-zA-Z0-9_\-\u4E00-\u9FA5]*$/, message: "昵称格式不正确"},
            { min: 2, max: 10, message: "用户名长度只能是 2 到 10 之间"}
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 8, max: 16, message: "密码长度在 8 到 16 之间"}
          ]
        }
      } 
    },
    methods: {
      submitForm(){
        this.$refs.loginFormRef.validate(valid => {
          if(valid){
            // 提交请求
            /**
             * api: /user/login
             * method: post
             * params: formData:{username,password}
             */
            let data = {
              username: this.loginForm.username,
              password: this.loginForm.password
            }
            post("/user/login",data,false)
            .then((res) => {
              if(res.success){
                if(res.message){
                  ElMessage({
                    message: res.message,
                    type: 'success'
                  })
                }
                location.replace("/")
              }else{
                if(res.message){
                  ElMessage({
                    message: res.message,
                    type: 'error'
                  })
                }
              }
            })
            .catch(err => {console.log(err)})            
            
          }
        })
      }
    }

}
</script>

<style>
  
  .login-holder {
    height: 400px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }


  .flex-grow{
    flex-grow: 1;
  }
  .btn{
    width: 250px;
  }

</style>