import type * as Business from "./type"
import { request } from "@/http/axios"

// ========== 认证模块 ==========
/** 登出 */
export function logoutApi() {
  return request<ApiResponseData<void>>({
    url: "auth/logout",
    method: "post"
  })
}

/** 获取当前用户路由 */
export function getRoutesApi() {
  return request<ApiResponseData<Business.MenuDTO[]>>({
    url: "auth/routes",
    method: "get"
  })
}

/** 获取当前用户权限标识 */
export function getPermissionsApi() {
  return request<ApiResponseData<string[]>>({
    url: "auth/permissions",
    method: "get"
  })
}

// ========== 用户管理 ==========
export function getUserListApi(params: { current: number, size: number, keyword?: string, status?: number }) {
  return request<ApiResponseData<Business.PageResult<Business.UserDTO>>>({
    url: "system/user/list",
    method: "get",
    params
  })
}

export function getUserByIdApi(id: number) {
  return request<ApiResponseData<Business.UserDTO>>({
    url: `system/user/${id}`,
    method: "get"
  })
}

export function createUserApi(data: Business.UserCreateCmd) {
  return request<ApiResponseData<number>>({
    url: "system/user",
    method: "post",
    data
  })
}

export function updateUserApi(id: number, data: Business.UserUpdateCmd) {
  return request<ApiResponseData<void>>({
    url: `system/user/${id}`,
    method: "put",
    data
  })
}

export function deleteUserApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/user/${id}`,
    method: "delete"
  })
}

export function resetPasswordApi(id: number, data: Business.ResetPasswordCmd) {
  return request<ApiResponseData<void>>({
    url: `system/user/${id}/password/reset`,
    method: "put",
    data
  })
}

export function updateUserStatusApi(id: number, data: Business.UpdateStatusCmd) {
  return request<ApiResponseData<void>>({
    url: `system/user/${id}/status`,
    method: "put",
    data
  })
}

// ========== 角色管理 ==========
export function getRoleListApi(params: { current: number, size: number, keyword?: string, status?: number }) {
  return request<ApiResponseData<Business.PageResult<Business.RoleDTO>>>({
    url: "system/role/list",
    method: "get",
    params
  })
}

export function getRoleByIdApi(id: number) {
  return request<ApiResponseData<Business.RoleDTO>>({
    url: `system/role/${id}`,
    method: "get"
  })
}

export function createRoleApi(data: Business.RoleCmd) {
  return request<ApiResponseData<number>>({
    url: "system/role",
    method: "post",
    data
  })
}

export function updateRoleApi(data: Business.RoleCmd) {
  return request<ApiResponseData<void>>({
    url: "system/role",
    method: "put",
    data
  })
}

export function deleteRoleApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/role/${id}`,
    method: "delete"
  })
}

export function getRoleMenusApi(id: number) {
  return request<ApiResponseData<number[]>>({
    url: `system/role/${id}/menus`,
    method: "get"
  })
}

export function assignRoleMenusApi(id: number, menuIds: number[]) {
  return request<ApiResponseData<void>>({
    url: `system/role/${id}/menus`,
    method: "put",
    data: menuIds
  })
}

// ========== 菜单管理 ==========
export function getMenuListApi() {
  return request<ApiResponseData<Business.MenuDTO[]>>({
    url: "system/menu/list",
    method: "get"
  })
}

export function getMenuTreeApi() {
  return request<ApiResponseData<Business.MenuDTO[]>>({
    url: "system/menu/tree",
    method: "get"
  })
}

export function getMenuByIdApi(id: number) {
  return request<ApiResponseData<Business.MenuDTO>>({
    url: `system/menu/${id}`,
    method: "get"
  })
}

export function createMenuApi(data: Business.MenuCmd) {
  return request<ApiResponseData<number>>({
    url: "system/menu",
    method: "post",
    data
  })
}

export function updateMenuApi(data: Business.MenuCmd) {
  return request<ApiResponseData<void>>({
    url: "system/menu",
    method: "put",
    data
  })
}

export function deleteMenuApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/menu/${id}`,
    method: "delete"
  })
}

// ========== 部门管理 ==========
export function getDeptListApi() {
  return request<ApiResponseData<Business.DeptDTO[]>>({
    url: "system/dept/list",
    method: "get"
  })
}

export function getDeptTreeApi() {
  return request<ApiResponseData<Business.DeptDTO[]>>({
    url: "system/dept/tree",
    method: "get"
  })
}

export function getDeptByIdApi(id: number) {
  return request<ApiResponseData<Business.DeptDTO>>({
    url: `system/dept/${id}`,
    method: "get"
  })
}

export function createDeptApi(data: Business.DeptCmd) {
  return request<ApiResponseData<number>>({
    url: "system/dept",
    method: "post",
    data
  })
}

export function updateDeptApi(data: Business.DeptCmd) {
  return request<ApiResponseData<void>>({
    url: "system/dept",
    method: "put",
    data
  })
}

export function deleteDeptApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/dept/${id}`,
    method: "delete"
  })
}

// ========== 岗位管理 ==========
export function getPostListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.PostDTO>>>({
    url: "system/post/list",
    method: "get",
    params
  })
}

export function getPostByIdApi(id: number) {
  return request<ApiResponseData<Business.PostDTO>>({
    url: `system/post/${id}`,
    method: "get"
  })
}

export function createPostApi(data: Business.PostCmd) {
  return request<ApiResponseData<number>>({
    url: "system/post",
    method: "post",
    data
  })
}

export function updatePostApi(data: Business.PostCmd) {
  return request<ApiResponseData<void>>({
    url: "system/post",
    method: "put",
    data
  })
}

export function deletePostApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/post/${id}`,
    method: "delete"
  })
}

// ========== 字典类型管理 ==========
export function getDictTypeListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.DictTypeDTO>>>({
    url: "system/dict/type/list",
    method: "get",
    params
  })
}

export function getDictTypeByIdApi(id: number) {
  return request<ApiResponseData<Business.DictTypeDTO>>({
    url: `system/dict/type/${id}`,
    method: "get"
  })
}

export function createDictTypeApi(data: Business.DictTypeCmd) {
  return request<ApiResponseData<number>>({
    url: "system/dict/type",
    method: "post",
    data
  })
}

export function updateDictTypeApi(data: Business.DictTypeCmd) {
  return request<ApiResponseData<void>>({
    url: "system/dict/type",
    method: "put",
    data
  })
}

export function deleteDictTypeApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/dict/type/${id}`,
    method: "delete"
  })
}

// ========== 字典数据管理 ==========
export function getDictDataListApi(params: { current: number, size: number, dictTypeId?: number }) {
  return request<ApiResponseData<Business.PageResult<Business.DictDataDTO>>>({
    url: "system/dict/data/list",
    method: "get",
    params
  })
}

export function getDictDataByTypeApi(dictType: string) {
  return request<ApiResponseData<Business.DictDataDTO[]>>({
    url: `system/dict/data/type/${dictType}`,
    method: "get"
  })
}

export function getDictDataByIdApi(id: number) {
  return request<ApiResponseData<Business.DictDataDTO>>({
    url: `system/dict/data/${id}`,
    method: "get"
  })
}

export function createDictDataApi(data: Business.DictDataCmd) {
  return request<ApiResponseData<number>>({
    url: "system/dict/data",
    method: "post",
    data
  })
}

export function updateDictDataApi(data: Business.DictDataCmd) {
  return request<ApiResponseData<void>>({
    url: "system/dict/data",
    method: "put",
    data
  })
}

export function deleteDictDataApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/dict/data/${id}`,
    method: "delete"
  })
}

// ========== 参数配置管理 ==========
export function getConfigListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.ConfigDTO>>>({
    url: "system/config/list",
    method: "get",
    params
  })
}

export function getConfigByIdApi(id: number) {
  return request<ApiResponseData<Business.ConfigDTO>>({
    url: `system/config/${id}`,
    method: "get"
  })
}

export function getConfigByKeyApi(configKey: string) {
  return request<ApiResponseData<Business.ConfigDTO>>({
    url: `system/config/key/${configKey}`,
    method: "get"
  })
}

export function createConfigApi(data: Business.ConfigCmd) {
  return request<ApiResponseData<number>>({
    url: "system/config",
    method: "post",
    data
  })
}

export function updateConfigApi(data: Business.ConfigCmd) {
  return request<ApiResponseData<void>>({
    url: "system/config",
    method: "put",
    data
  })
}

export function deleteConfigApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/config/${id}`,
    method: "delete"
  })
}

// ========== 通知公告管理 ==========
export function getNoticeListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.NoticeDTO>>>({
    url: "system/notice/list",
    method: "get",
    params
  })
}

export function getNoticeByIdApi(id: number) {
  return request<ApiResponseData<Business.NoticeDTO>>({
    url: `system/notice/${id}`,
    method: "get"
  })
}

export function createNoticeApi(data: Business.NoticeCmd) {
  return request<ApiResponseData<number>>({
    url: "system/notice",
    method: "post",
    data
  })
}

export function updateNoticeApi(data: Business.NoticeCmd) {
  return request<ApiResponseData<void>>({
    url: "system/notice",
    method: "put",
    data
  })
}

export function deleteNoticeApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/notice/${id}`,
    method: "delete"
  })
}

export function publishNoticeApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/notice/${id}/publish`,
    method: "put"
  })
}

export function withdrawNoticeApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/notice/${id}/withdraw`,
    method: "put"
  })
}

export function getUnreadNoticesApi() {
  return request<ApiResponseData<Business.NoticeDTO[]>>({
    url: "system/notice/unread",
    method: "get"
  })
}

export function markNoticeReadApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/notice/${id}/read`,
    method: "put"
  })
}

// ========== 操作日志 ==========
export function getOperLogListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.OperLogDTO>>>({
    url: "system/operLog/list",
    method: "get",
    params
  })
}

export function cleanOperLogApi() {
  return request<ApiResponseData<void>>({
    url: "system/operLog/clean",
    method: "delete"
  })
}

// ========== 登录日志 ==========
export function getLoginLogListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.LoginLogDTO>>>({
    url: "system/loginLog/list",
    method: "get",
    params
  })
}

export function cleanLoginLogApi() {
  return request<ApiResponseData<void>>({
    url: "system/loginLog/clean",
    method: "delete"
  })
}

// ========== 文件管理 ==========
export function getFileListApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.FileDTO>>>({
    url: "system/file/list",
    method: "get",
    params
  })
}

export function getFileByIdApi(id: number) {
  return request<ApiResponseData<Business.FileDTO>>({
    url: `system/file/${id}`,
    method: "get"
  })
}

export function uploadFileApi(file: File) {
  const formData = new FormData()
  formData.append("file", file)
  return request<ApiResponseData<Business.FileDTO>>({
    url: "system/file/upload",
    method: "post",
    data: formData,
    headers: { "Content-Type": "multipart/form-data" }
  })
}

export function deleteFileApi(id: number) {
  return request<ApiResponseData<void>>({
    url: `system/file/${id}`,
    method: "delete"
  })
}

// ========== 个人中心 ==========
export function getProfileApi() {
  return request<ApiResponseData<Business.UserDTO>>({
    url: "system/profile",
    method: "get"
  })
}

export function updateProfileApi(data: Business.ProfileUpdateCmd) {
  return request<ApiResponseData<void>>({
    url: "system/profile",
    method: "put",
    data
  })
}

export function changePasswordApi(data: Business.PasswordChangeCmd) {
  return request<ApiResponseData<void>>({
    url: "system/profile/password",
    method: "put",
    data
  })
}

export function getProfileLoginLogApi(params: { current: number, size: number }) {
  return request<ApiResponseData<Business.PageResult<Business.LoginLogDTO>>>({
    url: "system/profile/loginLog",
    method: "get",
    params
  })
}

export function getProfileMessagesApi() {
  return request<ApiResponseData<Business.NoticeDTO[]>>({
    url: "system/profile/messages",
    method: "get"
  })
}
