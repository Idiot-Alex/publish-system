<template>
  <div class="login-container">
    <el-form ref="login-form" :model="temp" :rules="rules" class="login-form" auto-complete="on" label-position="left">
      <h3 class="title">信息发布系统</h3>
      <el-form-item prop="userName">
        <el-input v-model="temp.userName" placeholder="userName" type="text" auto-complete="on" />
      </el-form-item>
      <el-form-item prop="userPassword">
        <el-input
          v-model="temp.userPassword"
          placeholder="password"
          type="password"
          auto-complete="on"
          @keyup.enter.native="login"
        />
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="login">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
const CODE_OK = 0
export default {
  data() {
    return {
      temp: {
        userName: '',
        userPassword: ''
      },
      loading: false,
      rules: {
        userName: [{ required: true, trigger: 'blur', message: 'User Name is required' }],
        userPassword: [{ required: true, trigger: 'blur', message: 'Password is required' }]
      }
    }
  },
  methods: {
    login() {
      this.$refs['login-form'].validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('Login', this.temp).then(res => {
            this.loading = false
            // 登录成功 跳转 首页
            if (res.code === CODE_OK) {
              this.$router.push({ path: '/' })
            } else {
              this.$notify.error({
                title: 'Failed',
                message: res.msg
              })
            }
          })
        } else {
          console.log('error submit for login!!')
          return false
        }
      })
    },
  }
}
</script>
<style lang="scss">
$bg:#2d3a4b;
$light_gray:#eee;
/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 5px 5px 5px 20px;
      color: $light_gray;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>
<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: $light_gray;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
