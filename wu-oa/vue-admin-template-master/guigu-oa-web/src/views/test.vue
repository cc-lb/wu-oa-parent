<template>
  <div>
    <div>账号切换</div>
     
    <button @click="admin()" type="default" size="mini">总管理员</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <button @click="zhangsan()" type="default" size="mini">张三</button>

    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    /*
    wjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNzczNDE211EqLU4t8kxRsjKCMPMSc1OBWsqzcpRqAcyDuLxAAAAA.pEyYE6lMiYOu_6r8t1d5J4hhOh_H4kaH7DASdosmkAtBStBAAfWl32NMWBKFvqdJMSWuO4SDQDh1Lt4Sl3EA5A'
    window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lrsjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNzczNLE00VEqLU4t8kxRsjKGMPMSc1OBWnKKirNylGoBJHVMp0IAAAA.fyT3D66bBQls7OHmWrEBWhRqqq_YCGLIltg5BUVWHBNQEieQUUVnUmBpiYoTdGlIosv714vJDb6czl1RfBZt3A'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNzczNDW21FEqLU4t8kxRsjKFMPMSc1OBWqqqsnKUagHL7DNWQQAAAA.0lI3he45UlfeX_xw83xY3elXRICK3bDzZdxe9UgtxirVTdMFq3maLJjF61rhNBWgFEC0CDk0YWfyClS8psMJDQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },
*/
    admin() {
      window.localStorage.setItem('token', '');
      let token ='eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcwsTAzMbUw1lEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQBMMDPHQgAAAA.ZSTpaqMZ4wd7AlON9MNLxS3eAbbCfvia7Dn4iFsBIsZVlDJthqNDyEV3cwnc8j4T3k0XulvwTHsB_KUnimregg'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token ='eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcwsTAzMTMy11EqLU4t8kwBiplC2HmJualAPTmZxZlKtQDWl9yoQgAAAA.Xjdwoj7JSllRw9Z00Rh0e7GiW5L7YlvHuLL686BHsf3SMN96YCB5xtZp3F93SBDvLnimQK6iJiG6Q7l1iFCdvA'
     window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },
    zhangsan(){
       window.localStorage.setItem('token', '');
           let token='eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcwsTAzMbOw0FEqLU4t8kwBiplA2HmJualAPVUZiXnpxYl5SrUA47En6kYAAAA.H_NqhCeo8HD0Y2-o0DB6C9uz_zrTj5nlXudXo4wggVfchUip0GDb_XA7kuadMxRNeoOJjpKyfJF3zGBE-XZNsA'
       window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
