/** 当前登录用户信息（对应后端 UserDTO） */
export interface CurrentUserData {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  avatar: string
  status: number
  deptId: number
  roleIds: number[]
  roles: string[]
  permissions: string[]
  createTime: string
  updateTime: string
}

export type CurrentUserResponseData = ApiResponseData<CurrentUserData>
