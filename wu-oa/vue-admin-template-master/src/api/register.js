import request from '@/utils/request'
export function register(user) { // 注册
    //  return axios.post('/admin/system/index/register', user)
    return request({
      url:'/admin/system/index/register',
      method:'post',
      params:{user}
    })
    }