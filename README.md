# AI-RuoYi — 100% AI 生成的通用后台管理系统

> **声明：本项目的每一行代码、每一份文档、每一个测试用例，均由 AI 自动生成。作者从未手动编写过一行代码，也从未手动执行过任何测试，仅做最终验收。**

## 这是什么

这是一个**完整复刻若依（RuoYi）核心功能**的通用后台管理系统，从需求采集、产品设计、技术设计、编码、测试到最终交付，全流程由 AI 独立完成。

使用的 AI Coding 工具：[Qoder](https://qoder.com)

## AI 全流程覆盖

| 阶段 | 执行者 | AI 完成内容 | 产出物 |
|------|--------|------------|--------|
| 需求采集 | AI 需求采集专员 | 与用户对话、澄清需求、结构化输出 | 需求采集文档 |
| 产品设计 | AI 产品经理 | 竞品分析、功能设计、PRD 编写 | PRD 文档 |
| 技术设计 | AI 架构师 | 技术选型、数据库设计、API 设计 | 技术设计文档、API 接口设计、数据库设计 |
| 测试用例 | AI 测试工程师 | 单元测试用例 + 集成测试用例编写 | 测试用例文档 |
| 编码 | AI 前端工程师 + AI 后端工程师 | 前后端全部代码生成 | 后端 Java 代码 + 前端 Vue 代码 |
| Code Review | AI 架构师 + AI 测试工程师 | 代码评审、规范检查 | 评审意见 |
| 单元测试 | AI 测试工程师 | 自动编写并执行 81 个单元测试 | 单元测试报告（100% 通过） |
| 集成测试 | AI 集成测试工程师 | E2E 浏览器交互测试 + API 联调测试 | 集成测试报告（86 + 75 用例，100% 通过） |
| 交付 | AI 工作流总指挥 | 交付物汇总 | v1.0.0 版本交付 |

**作者的角色：仅做最终验收，确认 AI 产出是否符合预期。**

## 功能概览

### 系统管理
- **用户管理** — 用户增删改查、密码重置、状态切换、角色分配
- **角色管理** — 角色增删改查、菜单权限分配
- **菜单管理** — 目录/菜单/按钮三级管理，支持按钮级权限控制
- **部门管理** — 树形结构部门管理
- **岗位管理** — 岗位编码与名称管理

### 系统工具
- **字典管理** — 字典类型与字典数据维护
- **参数配置** — 系统参数键值对管理
- **通知公告** — 公告发布、撤回

### 系统监控
- **操作日志** — 系统操作行为记录
- **登录日志** — 用户登录记录

### 文件管理
- **文件上传/管理** — 附件上传与列表管理

### 个人中心
- 基本资料、修改密码、登录日志、站内消息

### 权限控制
- **菜单级权限** — 根据角色动态渲染侧边栏菜单
- **按钮级权限** — 根据权限标识控制页面按钮的显示与隐藏
- **路由守卫** — 前端路由级别的权限拦截

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.2 | 核心框架 |
| MyBatis-Plus | 3.5.9 | ORM 框架 |
| sa-token | 1.39.0 | 认证授权（替代 Shiro + JWT） |
| Redisson | 3.27.0 | Redis 客户端 / 分布式会话 |
| MapStruct | 1.5.5 | 对象映射 |
| Hutool | 5.8.29 | 工具库 |
| Knife4j | 4.5.0 | API 文档 |
| Java | 21 | 运行时 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| TypeScript | 5.x | 类型安全 |
| Vite | 7.x | 构建工具 |
| Element Plus | 2.x | UI 组件库 |
| Pinia | 3.x | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| V3 Admin Vite | 5.x | 后台模板 |

### 基础设施
| 技术 | 版本 | 说明 |
|------|------|------|
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.x | 缓存 / 会话存储 |

## 后端架构

采用 **三模块 + 四层架构** 设计：

```
testruoyi/
├── base/          # 公共组件、基础工具类、通用配置
├── sdk/           # 对外 API 接口定义、Cmd/DTO、常量定义
└── server/        # 服务实现层（四层架构）
    ├── adapter/       # 适配层：Controller、拦截器、AOP
    ├── application/   # 应用服务层：业务流程编排、事务控制
    ├── domain/        # 领域服务层：核心业务逻辑、规则校验
    └── infrastructure/ # 基础设施层：Mapper/PO、文件存储、缓存
```

**模块依赖方向**：`server` → `sdk` → `base`

**领域划分**：auth / user / config / log / file / profile

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Node.js 18+ / pnpm
- MySQL 8.0+
- Redis 7.x+

### 1. 克隆项目

```bash
git clone https://github.com/your-username/testruoyi.git
cd testruoyi
```

### 2. 初始化数据库

创建 MySQL 数据库：

```sql
CREATE DATABASE test_ruoyi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

执行建表 SQL（位于 [docs/v1.0.0/数据库设计.md](docs/v1.0.0/数据库设计.md) 中的 DDL 语句）。

> 首次启动后端时，`DataInitializer` 会自动插入初始数据（管理员账号、角色、菜单、按钮权限等），无需手动导入。

### 3. 配置后端

修改 `server/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/test_ruoyi?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false&rewriteBatchedStatements=true
    username: root
    password: YOUR_DB_PASSWORD        # ← 改为你的 MySQL 密码
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: YOUR_REDIS_PASSWORD   # ← 改为你的 Redis 密码（无密码则删除此行）
```

### 4. 启动后端

```bash
cd server
mvn spring-boot:run
```

后端启动在 `http://localhost:8080`

API 文档：`http://localhost:8080/doc.html`

### 5. 启动前端

```bash
cd web
pnpm install
pnpm dev
```

前端启动在 `http://localhost:3333`

### 6. 登录系统

| 账号 | 密码 | 角色 | 权限范围 |
|------|------|------|---------|
| admin | admin123 | 超级管理员 | 全部菜单 + 全部按钮权限 |
| zhangsan | 123456 | 普通角色 | 有限菜单 + 无按钮权限（只读） |

## 项目结构

```
testruoyi/
├── base/                          # 公共模块
│   └── src/main/java/.../base/
│       ├── basecls/               # 基础类（Controller/Repository）
│       ├── constant/              # 常量定义
│       ├── dto/                   # 通用 DTO（ApiResponse/PageDTO）
│       ├── enums/                 # 枚举
│       ├── exceptions/            # 异常体系
│       ├── model/                 # 分页模型
│       └── util/                  # 工具类
├── sdk/                           # SDK 模块
│   └── src/main/java/.../sdk/
│       ├── cmd/                   # 命令对象（按领域分包）
│       ├── contract/enums/        # 契约枚举
│       └── dto/                   # 数据传输对象（按领域分包）
├── server/                        # 服务端模块
│   └── src/main/java/.../server/
│       ├── adapter/controller/    # 适配层 - Controller
│       ├── application/           # 应用服务层
│       │   ├── bo/                #   业务对象（按领域分包）
│       │   └── service/impl/      #   应用服务实现
│       ├── config/                # 配置类（DataInitializer 等）
│       ├── domain/                # 领域服务层
│       │   └── {auth,user,config,log,file,profile}/
│       │       ├── do_/           #   领域对象
│       │       ├── service/       #   领域服务
│       │       └── repository/    #   仓储接口
│       ├── infrastructure/        # 基础设施层
│       │   ├── po/                #   持久化对象
│       │   ├── mapper/            #   MyBatis-Plus Mapper
│       │   ├── convertor/         #   MapStruct 转换器
│       │   └── repository/impl/   #   仓储实现
│       └── helper/                # 辅助工具
├── web/                           # 前端项目
│   ├── src/
│   │   ├── common/                # 通用资源
│   │   │   ├── apis/              #   API 请求
│   │   │   ├── assets/            #   静态资源
│   │   │   ├── components/        #   公共组件
│   │   │   ├── composables/       #   组合式函数（usePermission 等）
│   │   │   ├── constants/         #   常量
│   │   │   └── utils/             #   工具函数
│   │   ├── layouts/               # 布局组件
│   │   ├── pages/                 # 页面组件
│   │   │   ├── system/            #   系统管理（user/role/menu/dept/post/dict/config/notice）
│   │   │   ├── log/               #   日志管理（operLog/loginLog）
│   │   │   ├── file/              #   文件管理
│   │   │   ├── profile/           #   个人中心
│   │   │   └── dashboard/         #   首页
│   │   ├── plugins/               #   插件（v-permission 指令等）
│   │   ├── pinia/                 #   状态管理
│   │   └── router/                #   路由配置
│   └── tests/                     # 前端单元测试
├── docs/                          # 项目文档
│   ├── 项目概要.md                #   项目概要（当前状态快照）
│   └── v1.0.0/                    #   v1.0.0 版本交付物
│       ├── 需求采集文档.md
│       ├── PRD文档.md
│       ├── 技术设计文档.md
│       ├── API接口设计.md
│       ├── 数据库设计.md
│       ├── 单元测试用例文档.md
│       ├── 单元测试报告.md
│       ├── 集成测试用例文档.md
│       ├── 集成测试报告.md
│       ├── E2E集成测试用例文档.md
│       └── E2E集成测试报告.md
└── pom.xml                        # Maven 父 POM
```

## 权限控制机制

### 菜单级权限
用户登录后，后端根据角色关联的菜单列表返回可访问的路由，前端动态生成侧边栏菜单。无权限的页面不会出现在导航中。

### 按钮级权限
每个页面的操作按钮（新增/编辑/删除等）通过 `v-permission` 指令和 `usePermission` 组合式函数控制可见性：

```vue
<!-- 指令方式 -->
<el-button v-permission="['system:user:add']">新增</el-button>

<!-- 组合式函数方式 -->
<el-button v-if="hasPermission('system:user:edit')">编辑</el-button>
```

权限标识格式：`{模块}:{资源}:{操作}`，如 `system:user:add`、`system:role:edit`、`system:notice:publish`。

超级管理员（admin）拥有通配符权限 `*`，所有按钮均可见。

## 核心特性

- **RBAC 双级权限控制** — 菜单级 + 按钮级，精确到页面操作
- **ID 型软删除** — 删除时 `is_deleted = id`，支持唯一索引正确工作
- **sa-token 认证** — UUID Token + Redis 分布式会话，替代传统 Shiro + JWT
- **自动数据初始化** — `DataInitializer` 首次启动自动创建管理员、角色、菜单和权限数据
- **四层领域架构** — 适配层 / 应用服务层 / 领域服务层 / 基础设施层，职责清晰
- **API 文档** — Knife4j (Swagger) 自动生成，访问 `/doc.html`
- **操作日志** — AOP 自动记录接口调用日志
- **登录日志** — 记录每次登录的时间和 IP

## 与若依的差异

| 对比项 | 若依（RuoYi） | 本项目 |
|--------|--------------|--------|
| 后端框架 | Spring Boot 2.x | Spring Boot 3.3.2 |
| Java 版本 | Java 8/11 | Java 21 |
| 安全框架 | Shiro + JWT | sa-token |
| ORM | MyBatis（XML 映射） | MyBatis-Plus（注解为主） |
| 前端框架 | Vue 2 / Vue 3 (Element UI/Plus) | Vue 3.5 + TypeScript 5.x |
| 构建工具 | Webpack / Vite | Vite 7.x |
| 缓存客户端 | Jedis / Lettuce | Redisson |
| 软删除策略 | 固定值 1/0 | ID 型软删除（is_deleted = id） |
| 架构风格 | 传统三层 | 四层领域架构 |
| 代码生成 | 有 | 无（AI 全量生成） |

## 许可证

[MIT License](LICENSE)

---

**本项目是 AI Coding 能力的一个完整验证 — 从需求到交付，零人工编码。**
