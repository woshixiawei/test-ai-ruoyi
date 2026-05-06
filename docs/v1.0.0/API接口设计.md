# 技术设计文档 - API接口设计

- 版本号: v1.0.0
- 关联主文档: docs/v1.0.0/技术设计文档.md
- 本文件为技术设计文档第5节的独立拆分

---

## 5.1 接口列表

| 模块 | 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|------|
| 认证 | POST | /backend/auth/login | 用户登录 | 否 |
| 认证 | POST | /backend/auth/logout | 用户登出 | 是 |
| 认证 | GET | /backend/auth/info | 获取当前用户信息 | 是 |
| 认证 | GET | /backend/auth/routes | 获取当前用户路由/菜单 | 是 |
| 认证 | GET | /backend/auth/permissions | 获取当前用户权限标识 | 是 |
| 用户 | GET | /backend/system/user/list | 用户列表 | 是 |
| 用户 | GET | /backend/system/user/{id} | 用户详情 | 是 |
| 用户 | POST | /backend/system/user | 新增用户 | 是 |
| 用户 | PUT | /backend/system/user | 修改用户 | 是 |
| 用户 | DELETE | /backend/system/user/{id} | 删除用户 | 是 |
| 用户 | PUT | /backend/system/user/{id}/password/reset | 重置密码 | 是 |
| 用户 | PUT | /backend/system/user/{id}/status | 修改用户状态 | 是 |
| 角色 | GET | /backend/system/role/list | 角色列表 | 是 |
| 角色 | GET | /backend/system/role/{id} | 角色详情 | 是 |
| 角色 | POST | /backend/system/role | 新增角色 | 是 |
| 角色 | PUT | /backend/system/role | 修改角色 | 是 |
| 角色 | DELETE | /backend/system/role/{id} | 删除角色 | 是 |
| 角色 | GET | /backend/system/role/{id}/menus | 获取角色已分配菜单 | 是 |
| 角色 | PUT | /backend/system/role/{id}/menus | 分配角色菜单权限 | 是 |
| 菜单 | GET | /backend/system/menu/list | 菜单列表 | 是 |
| 菜单 | GET | /backend/system/menu/tree | 菜单树 | 是 |
| 菜单 | GET | /backend/system/menu/{id} | 菜单详情 | 是 |
| 菜单 | POST | /backend/system/menu | 新增菜单 | 是 |
| 菜单 | PUT | /backend/system/menu | 修改菜单 | 是 |
| 菜单 | DELETE | /backend/system/menu/{id} | 删除菜单 | 是 |
| 部门 | GET | /backend/system/dept/list | 部门列表 | 是 |
| 部门 | GET | /backend/system/dept/tree | 部门树 | 是 |
| 部门 | GET | /backend/system/dept/{id} | 部门详情 | 是 |
| 部门 | POST | /backend/system/dept | 新增部门 | 是 |
| 部门 | PUT | /backend/system/dept | 修改部门 | 是 |
| 部门 | DELETE | /backend/system/dept/{id} | 删除部门 | 是 |
| 岗位 | GET | /backend/system/post/list | 岗位列表 | 是 |
| 岗位 | GET | /backend/system/post/{id} | 岗位详情 | 是 |
| 岗位 | POST | /backend/system/post | 新增岗位 | 是 |
| 岗位 | PUT | /backend/system/post | 修改岗位 | 是 |
| 岗位 | DELETE | /backend/system/post/{id} | 删除岗位 | 是 |
| 字典类型 | GET | /backend/system/dict/type/list | 字典类型列表 | 是 |
| 字典类型 | GET | /backend/system/dict/type/{id} | 字典类型详情 | 是 |
| 字典类型 | POST | /backend/system/dict/type | 新增字典类型 | 是 |
| 字典类型 | PUT | /backend/system/dict/type | 修改字典类型 | 是 |
| 字典类型 | DELETE | /backend/system/dict/type/{id} | 删除字典类型 | 是 |
| 字典数据 | GET | /backend/system/dict/data/list | 字典数据列表 | 是 |
| 字典数据 | GET | /backend/system/dict/data/{id} | 字典数据详情 | 是 |
| 字典数据 | POST | /backend/system/dict/data | 新增字典数据 | 是 |
| 字典数据 | PUT | /backend/system/dict/data | 修改字典数据 | 是 |
| 字典数据 | DELETE | /backend/system/dict/data/{id} | 删除字典数据 | 是 |
| 字典数据 | GET | /backend/system/dict/data/type/{dictType} | 按类型查字典 | 是/否 |
| 参数配置 | GET | /backend/system/config/list | 参数列表 | 是 |
| 参数配置 | GET | /backend/system/config/{id} | 参数详情 | 是 |
| 参数配置 | POST | /backend/system/config | 新增参数 | 是 |
| 参数配置 | PUT | /backend/system/config | 修改参数 | 是 |
| 参数配置 | DELETE | /backend/system/config/{id} | 删除参数 | 是 |
| 参数配置 | GET | /backend/system/config/key/{configKey} | 按键查参数 | 是/否 |
| 通知公告 | GET | /backend/system/notice/list | 公告列表 | 是 |
| 通知公告 | GET | /backend/system/notice/{id} | 公告详情 | 是 |
| 通知公告 | POST | /backend/system/notice | 新增公告 | 是 |
| 通知公告 | PUT | /backend/system/notice | 修改公告 | 是 |
| 通知公告 | DELETE | /backend/system/notice/{id} | 删除公告 | 是 |
| 通知公告 | PUT | /backend/system/notice/{id}/publish | 发布公告 | 是 |
| 通知公告 | PUT | /backend/system/notice/{id}/withdraw | 撤回公告 | 是 |
| 通知公告 | GET | /backend/system/notice/unread | 未读公告列表 | 是 |
| 通知公告 | PUT | /backend/system/notice/{id}/read | 标记已读 | 是 |
| 操作日志 | GET | /backend/system/operLog/list | 操作日志列表 | 是 |
| 操作日志 | DELETE | /backend/system/operLog/clean | 清理日志 | 是 |
| 操作日志 | GET | /backend/system/operLog/export | 导出日志 | 是 |
| 登录日志 | GET | /backend/system/loginLog/list | 登录日志列表 | 是 |
| 登录日志 | DELETE | /backend/system/loginLog/clean | 清理日志 | 是 |
| 登录日志 | GET | /backend/system/loginLog/export | 导出日志 | 是 |
| 文件 | POST | /backend/system/file/upload | 文件上传 | 是 |
| 文件 | GET | /backend/system/file/list | 文件列表 | 是 |
| 文件 | GET | /backend/system/file/{id} | 文件详情 | 是 |
| 文件 | DELETE | /backend/system/file/{id} | 删除文件 | 是 |
| 文件 | GET | /backend/system/file/download/{id} | 下载文件 | 是 |
| 个人中心 | GET | /backend/system/profile | 获取个人信息 | 是 |
| 个人中心 | PUT | /backend/system/profile | 修改个人信息 | 是 |
| 个人中心 | PUT | /backend/system/profile/password | 修改密码 | 是 |
| 个人中心 | POST | /backend/system/profile/avatar | 上传头像 | 是 |
| 个人中心 | GET | /backend/system/profile/loginLog | 个人登录历史 | 是 |
| 个人中心 | GET | /backend/system/profile/messages | 个人消息列表 | 是 |

> **免认证接口说明**：表中标记为"是/否"的接口（如按类型查字典、按键查参数）在登录页等未登录场景下允许匿名访问，通过 sa-token 全局拦截器的白名单配置放行，无需携带 Token。

## 5.2 接口详情（核心接口示例）

#### 用户登录
- 请求方式: POST
- 请求路径: /backend/auth/login
- 请求参数:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| captcha | string | 否 | 验证码（V1.0.0可选） |
- 响应格式:
```json
{
  "code": 0,
  "msg": null,
  "data": {
    "token": "satoken-xxxxx",
    "tokenName": "satoken"
  },
  "traceId": "a1b2c3d4e5f6"
}
```

#### 获取当前用户信息
- 请求方式: GET
- 请求路径: /backend/auth/info
- 响应格式:
```json
{
  "code": 0,
  "msg": null,
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "https://xxx/avatar.png",
    "roles": ["admin"],
    "permissions": ["system:user:list", "system:user:add"]
  },
  "traceId": "a1b2c3d4e5f6"
}
```

#### 获取用户路由/菜单
- 请求方式: GET
- 请求路径: /backend/auth/routes
- 响应格式:
```json
{
  "code": 0,
  "msg": null,
  "data": [
    {
      "id": 1,
      "menuName": "系统管理",
      "menuType": 0,
      "path": "/system",
      "icon": "Setting",
      "children": [
        {
          "id": 2,
          "menuName": "用户管理",
          "menuType": 1,
          "path": "/system/user",
          "component": "system/user/index",
          "permission": "system:user:list"
        }
      ]
    }
  ],
  "traceId": "a1b2c3d4e5f6"
}
```

#### 通用分页响应格式
```json
{
  "code": 0,
  "msg": null,
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  },
  "traceId": "a1b2c3d4e5f6"
}
```
