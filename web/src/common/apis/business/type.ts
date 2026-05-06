/** 后端统一响应格式 */
export interface ApiResponseData<T = any> {
  code: number
  msg: string
  data: T
}

/** 分页结果 */
export interface PageResult<T> {
  current: number
  size: number
  total: number
  records: T[]
}

// ========== 用户管理 ==========
export interface UserDTO {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  avatar: string
  status: number
  deptId: number
  postId: number
  remark: string
  roleIds: number[]
  createTime: string
  updateTime: string
}

export interface UserCreateCmd {
  username: string
  nickname: string
  password: string
  email?: string
  phone?: string
  status?: number
  deptId?: number
  postId?: number
  remark?: string
  roleIds?: number[]
}

export interface UserUpdateCmd extends UserCreateCmd {}

export interface ResetPasswordCmd {
  newPassword: string
}

export interface UpdateStatusCmd {
  status: number
}

// ========== 角色管理 ==========
export interface RoleDTO {
  id: number
  roleName: string
  roleCode: string
  sort: number
  status: number
  remark: string
  menuIds: number[]
  createTime: string
  updateTime: string
}

export interface RoleCmd {
  id?: number
  roleName: string
  roleCode: string
  sort?: number
  status?: number
  remark?: string
}

// ========== 菜单管理 ==========
export interface MenuDTO {
  id: number
  menuName: string
  parentId: number
  sort: number
  path: string
  component: string
  permission: string
  menuType: number
  icon: string
  status: number
  createTime: string
  updateTime: string
  children?: MenuDTO[]
}

export interface MenuCmd {
  id?: number
  menuName: string
  parentId?: number
  sort?: number
  path?: string
  component?: string
  permission?: string
  menuType: number
  icon?: string
  status?: number
}

// ========== 部门管理 ==========
export interface DeptDTO {
  id: number
  deptName: string
  parentId: number
  sort: number
  leader: string
  phone: string
  email: string
  status: number
  createTime: string
  updateTime: string
  children?: DeptDTO[]
}

export interface DeptCmd {
  id?: number
  deptName: string
  parentId?: number
  sort?: number
  leader?: string
  phone?: string
  email?: string
  status?: number
}

// ========== 岗位管理 ==========
export interface PostDTO {
  id: number
  postCode: string
  postName: string
  sort: number
  status: number
  remark: string
  createTime: string
  updateTime: string
}

export interface PostCmd {
  id?: number
  postCode: string
  postName: string
  sort?: number
  status?: number
  remark?: string
}

// ========== 字典管理 ==========
export interface DictTypeDTO {
  id: number
  dictName: string
  dictType: string
  status: number
  remark: string
  createTime: string
  updateTime: string
}

export interface DictTypeCmd {
  id?: number
  dictName: string
  dictType: string
  status?: number
  remark?: string
}

export interface DictDataDTO {
  id: number
  dictTypeId: number
  dictLabel: string
  dictValue: string
  sort: number
  status: number
  remark: string
  createTime: string
  updateTime: string
}

export interface DictDataCmd {
  id?: number
  dictTypeId: number
  dictLabel: string
  dictValue: string
  sort?: number
  status?: number
  remark?: string
}

// ========== 参数配置 ==========
export interface ConfigDTO {
  id: number
  configName: string
  configKey: string
  configValue: string
  isSystem: number
  remark: string
  createTime: string
  updateTime: string
}

export interface ConfigCmd {
  id?: number
  configName: string
  configKey: string
  configValue: string
  isSystem?: number
  remark?: string
}

// ========== 通知公告 ==========
export interface NoticeDTO {
  id: number
  title: string
  content: string
  noticeType: number
  status: number
  publisherId: number
  publishTime: string
  createTime: string
  updateTime: string
}

export interface NoticeCmd {
  id?: number
  title: string
  content: string
  noticeType: number
  status?: number
}

// ========== 操作日志 ==========
export interface OperLogDTO {
  id: number
  title: string
  operType: number
  method: string
  requestMethod: string
  operUrl: string
  operIp: string
  operLocation: string
  operParam: string
  jsonResult: string
  status: number
  errorMsg: string
  operTime: string
  costTime: number
  createTime: string
  updateTime: string
}

// ========== 登录日志 ==========
export interface LoginLogDTO {
  id: number
  username: string
  ip: string
  location: string
  browser: string
  os: string
  status: number
  msg: string
  loginTime: string
  createTime: string
  updateTime: string
}

// ========== 文件管理 ==========
export interface FileDTO {
  id: number
  fileName: string
  filePath: string
  fileSize: number
  fileType: string
  url: string
  createBy: number
  createTime: string
  updateTime: string
}

// ========== 个人中心 ==========
export interface ProfileUpdateCmd {
  nickname: string
  email?: string
  phone?: string
}

export interface PasswordChangeCmd {
  oldPassword: string
  newPassword: string
}
