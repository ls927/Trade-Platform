<template>
  <!-- prop 用来区分不同项，以便于生成各项中 v-model 的校验器，本例中，校验器全在 rules 里 -->
  <div class="register-holder">
    <img src="../assets/we-trade2.png" style="height: 60px;width: auto;margin-bottom: 20px;"/>
    <el-form
      ref="registerFormRef"
      :model="registerForm"
      status-icon
      :rules="rules"
      style="width: 250px"
    >
      <el-form-item prop="username">
        <el-input v-model="registerForm.username" autocomplete="off">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="registerForm.password" type="password" autocomplete="off">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input v-model="registerForm.confirmPassword" type="password" autocomplete="off" placeholder="请输入确认密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm" class="btn">注册</el-button>
      </el-form-item>

      <el-form-item>
        <el-button @click="resetForm" class="btn">重置</el-button>
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
      registerForm:{
        username: '',
        password: '',
        confirmPassword: ''        
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
        ],
        confirmPassword: [
          { required: true, message: '请输入确认密码', trigger: 'blur' },
          { validator: this.checkConfirmPassword}
        ]        
      }
    }
  },
  methods: {
    checkConfirmPassword(rule, value, callback){
      if(value === ''){
        callback(new Error("请输入确认密码"))
      }else if(value !== this.registerForm.password){
        callback(new Error("两次输入密码不匹配"))
      }else{
        callback()
      }
    },
    submitForm(){
      this.$refs.registerFormRef.validate(valid => {
        if(valid){
          
          // 提交表单
          /**
           * api: /user/register
           * method: post
           * params: formData {usename,password}
           */
          let data = {
            username: this.registerForm.username,
            password: this.registerForm.password
          }
          post("/user/register",data,false)
          .then((res) => {
              if(res.success){
                ElMessage({message: res.message,type: 'success'})
                location.replace("/login")
              }else{
                ElMessage({message: res.message,type: 'error'})
              }
          })
          .catch(err => {console.log(err)})    
          
          
        }
      })      
    },
    resetForm(){
      this.$refs.registerFormRef.resetFields()
    }
  }


}
</script>

<style>
  .register-holder {
    height: 400px;
    margin-top: 80px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .btn{
    width: 250px;
  }
</style>