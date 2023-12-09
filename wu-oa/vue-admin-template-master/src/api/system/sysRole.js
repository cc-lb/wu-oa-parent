import request from '@/utils/request'
//  @/utils/request这里面是做

const api_name='admin/system/sysRole'
export default{
    getPageList(page,limit,searchObj){
        return request({
            url:`${api_name}/${page}/${limit}`,
            method:'get',
            params:searchObj
        })
    },
    removeById(id){
        return request({
            url:`${api_name}/remove/${id}`,
            method:'delete',
            
        })
    },
    save(role){
        return request({
        url:`${api_name}/save`,
        method:'post',
        data:role

    })
    },
    getById(id) {
        return request({
          url: `${api_name}/get/${id}`,
          method: 'get'
        })
      },
      
      updateById(role) {
        return request({
          url: `${api_name}/update`,
          method: 'put',
          data: role
        })
      },
      batchRemove(idList){
        return request({
          url:`${api_name}/batchRemove`,
          method:'delete',
          data:idList
        })

      },
      
      getRoles(adminId) {
        return request({
          url: `${api_name}/toAssign/${adminId}`,
          method: 'get'
        })
      },
      
      assignRoles(assginRoleVo) {
        return request({
          url: `${api_name}/doAssign`,
          method: 'post',
          data: assginRoleVo
        })
      }
}