<template>
  <div>
    <div>账号切换</div>
    <button @click="wjl()" type="default" size="mini">王经理</button>
    <button @click="lrsjl()" type="default" size="mini">李人事经理</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
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

    lisi() {
      window.localStorage.setItem('token', '');
      let token ='eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwtzAxtTA301EqLU4t8kxRsjKBMPMSc1OBWnIyizOVagGaasq_QQAAAA.NYtvPV_oFnwHL0VAQemvJ0OiIyzdnlXNGX1iyY_mDyy-FIQhwIDcbJnD33XDVGsJn9_KmrRzpJJAIgN3uISKSA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },
    zhangsan(){
       window.localStorage.setItem('token', '');
           let token='eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_6tWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwNzczNDa11FEqLU4t8kwBihlD2HmJualAPVUZiXnpxYl5SrUAmPY8mkYAAAA.cpuJfoOSOU28D08NEng_Ymwvff04xuN58EoKzLUnkX2Po0eM9QHVLGk0IjJlH5-4FIDTjfqWDwKRbApYFnJZaA'
       window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
