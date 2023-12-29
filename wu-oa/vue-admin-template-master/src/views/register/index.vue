  <template>
  <div class="login-container">
    <article class="header">
      <header>
        <el-avatar icon="el-icon-user-solid" shape="circle" />
        <span class="login">
          <em class="bold">已有账号？</em>
          <a href="/login">
            <el-button type="primary" size="mini">登录</el-button>
          </a>
        </span>
      </header>
    </article>
    <section>
      <el-form ref="loginForm" :model="loginForm" class="login-form">

        <div class="title-container">
          <h3 class="title">注册</h3>
        </div>

        <!-- 邮箱 -->
        <el-form-item prop="email">
          <span class="svg-container">
            <svg-icon icon-class="email" />
          </span>
          <el-input
            ref="email"
            v-model="loginForm.email"
            placeholder="请输入邮箱"
            name="email"
            type="text"
            tabindex="5"
          />
        </el-form-item>

        <el-form-item prop="username1">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            type="text"
            tabindex="1"
            @blur="reload"
          />
        </el-form-item>

        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="password">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              :type="passwordType"
              placeholder="密码"
              tabindex="2"
              autocomplete="on"
              @keyup.native="checkCapslock"
              @blur="capsTooltip = false"
              @keyup.enter.native="handleLogin"
            />
            <span class="show-pwd" @click="showPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
        </el-tooltip>
        <el-tooltip v-model="recapsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="repassword">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
            <el-input
              :key="repasswordType"
              ref="repassword"
              v-model="loginForm.repassword"
              :type="repasswordType"
              placeholder="确认密码"
              tabindex="3"
              autocomplete="on"
              @keyup.native="recheckCapslock"
              @blur="recapsTooltip = false"
            />
            <span class="show-pwd" @click="reshowPwd">
              <svg-icon :icon-class="repasswordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
        </el-tooltip>

        <!-- 手机 -->
        <el-form-item prop="phone">
          <span class="svg-container">
            <svg-icon icon-class="wechat" />
          </span>
          <el-input
            ref="phone"
            v-model="loginForm.phone"
            placeholder="请输入手机号"
            name="phone"
            type="text"
            tabindex="4"
          />
        </el-form-item>

        <!-- 验证码 -->
        <!-- <el-form-item prop="verifycode">
          <span class="svg-container">
            <svg-icon icon-class="guide" />
          </span>
          <el-input
            ref="verifycode"
            v-model="loginForm.verifycode"
            placeholder="请输入验证码"
            name="verifycode"
            type="text"
            tabindex="6"
            style="width:70%;"
          />
          <img :src="verifycodeImg" alt="" @click="reload">
        </el-form-item> -->

        <el-button type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="userRegister">注册</el-button>

      </el-form>
    </section>
  </div>
</template>

<script>
import { register } from '@/api/user'
import axios from 'axios'
export default {
  data() {
    return {
      loginForm: {

      },
      capsTooltip: false,
      passwordType: 'password',
      recapsTooltip: false,
      repasswordType: 'password',
      verifycodeImg: ''
    }
  },
  methods: {

    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    reshowPwd() {
      if (this.repasswordType === 'password') {
        this.repasswordType = ''
      } else {
        this.repasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.repassword.focus()
      })
    },
    recheckCapslock(e) {
      const { key } = e
      this.recapsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    reload() {
      //  "路径?t=" + new Date() ，提供一个t变量，用于唯一标识每一次访问路径
      // this.verifycodeImg = `http://localhost:10010/v2/user-service/verifycode/${this.loginForm.username}?t=` + new Date().getTime()
      this.rerifycodeImg = `${process.env.VUE_APP_BASE_API}/user-service/verifycode/${this.loginForm.username}?t=${new Date().getTime()}`
    },
    async userRegister() {
      var that = this
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          const param = new URLSearchParams()
          param.append('username', values.username)
          param.append('email', values.email)
          param.append('phonenumber', values.phonenumber)
          param.append('password', values.password)
          axios.post('http://localhost:8080/admin/system/index/register', param).then(function(response) {
            // console.log(response.data)
            // if (response.data === 'index') {
            //   that.$emit('lisentcurrent', [response.data])
            // } else {
            //   alert(response.data)
            // }
          }).catch(function(error) {
            console.log(error)
          }).then(function() {

          })
          console.log('Received values of form: ', values)
        }
      })
      const { message } = await register(this.loginForm)
      this.$message.success(message)
      // 跳转到登录
      this.$router.push('/login')
    }

  }
}
</script>
 

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

// $bg:#283443;
$bg:#e6eaef;
$light_gray:#fff;
$cursor: rgb(19, 19, 19);
// $cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {

  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      // background: transparent;
      // border: 0px;
      // -webkit-appearance: none;
      // border-radius: 0px;
      // padding: 12px 5px 12px 15px;
      // // color: $light_gray;
      //  color: rgb(97, 60, 60);
      // height: 47px;
      // caret-color: $cursor;
       background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 25px;
      padding: 12px 5px 12px 15px;
      color: rgba(249, 245, 245, 0.308);
      height: 46px;
      caret-color: rgba(62,119,194,0.8);

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    // border: 1px solid rgba(255, 255, 255, 0.1);
    // background: rgba(0, 0, 0, 0.1);
     background: rgba(172,172,172,0.1);
    border-radius: 5px;
    // color: #454545;
      color: #6786880a;
  }
}
</style>

<style lang="scss" scoped>
// $bg:#2d3a4b;
// $dark_gray:#889aa4;
// $light_gray:#eee;
$bg:#514d4d;
$dark_gray:#889aa4;
$light_gray:#eee;
.login-container {
// min-height: 100%;
//   width: 100%;
//   background-color: $bg;
//   overflow: hidden;

   width: 100%;
   height: 100%;
   background-color:  rgb(55, 61, 65);
  // overflow: hidden;
 // background-image: url("../../assets/404_images/register.jpg");
   background-size:cover;
   background-position: center;
   position: center;
  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }
.header {
    border-bottom: 2px solid rgb(235, 232, 232);
    min-width: 980px;
    color: #666;

    header {
      margin: 0 auto;
      padding: 10px 0;
      width: 980px;

      .login {
        float: right;
      }

      .bold {
        font-style: normal;
        color: $light_gray;
      }
    }
  }

  > section {
    margin: 0 auto 30px;
    padding-bottom: 100px;
    width: 980px;
    min-height: 300px;
    box-sizing: border-box;

    .status {
      font-size: 12px;
      margin-left: 20px;
      color: #f9eff059;
    }

    .error {
      color: red;
    }
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
       font-size: 30px;
      color: #a9c5db;
      margin: -50px auto 0px auto;
      text-align: center;
      font-weight: bold;
    }
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

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}
</style>


