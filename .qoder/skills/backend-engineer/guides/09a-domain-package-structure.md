# 领域包分离规范

- 版本号: v3.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> **TL;DR 核心要点**：
> 1. **共享层级+领域分包**：adapter/application/infrastructure 共享，domain 按业务分包
> 2. **DO在 `domain/{领域}/do_/`**（注意 do_ 下划线，Java保留字限制）
> 3. **BO在 `application/bo/{领域}/`**（BO属于应用层，按领域分包）
> 4. **Repository接口在 `domain/{领域}/repository/`**，实现在 `infrastructure/repository/impl/`
> 5. **AppService可直接调用所有DomainService**（共享层级架构），禁止DomainService互调
> 6. **Remote仅用于外部第三方调用**，不用于领域间交互

---

> 领域隔离总览详见 [09-domain-isolation.md](./09-domain-isolation.md)
> 领域间交互约束详见 [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md)

---

## 1. 核心原则

server 模块采用**共享层级 + 领域分包**模式：

- **适配层（adapter）、应用层（application）、基础设施层（infrastructure）** 在所有领域间共享，直接位于 server 根包下
- **领域层（domain）** 按业务领域分包，每个领域拥有独立的子包（如 `domain/auth/`、`domain/user/`）
- **公共代码**（config / helper / contract）直接放在 server 根包下

应用层（AppService）可以直接调用所有领域层（DomainService）的服务，无需通过 Remote/Api 机制进行领域间交互。

---

## 2. 包结构规范

```
com.example.project.server/
├── ServerApplication.java                    # Spring Boot 启动类
│
├── adapter/                                  # ================== 共享适配层 ==================
│   └── controller/
│       └── backend/
│           ├── AuthController.java           #   认证接口
│           ├── UserController.java           #   用户管理接口
│           ├── RoleController.java           #   角色管理接口
│           └── ...
│
├── application/                              # ================== 共享应用层 ==================
│   └── service/
│       ├── AuthAppService.java               #   认证应用服务接口
│       ├── UserAppService.java               #   用户应用服务接口
│       └── impl/
│           ├── AuthAppServiceImpl.java       #   认证应用服务实现
│           └── UserAppServiceImpl.java       #   用户应用服务实现
│
├── application/                              # ================== 共享应用层 ==================
│   ├── bo/                                   #   BO（应用层业务对象，按领域分包）
│   │   ├── auth/                             #     auth 领域 BO
│   │   │   ├── LoginBO.java                  #       登录业务对象
│   │   │   └── LoginResultBO.java            #       登录结果
│   │   ├── user/                             #     user 领域 BO
│   │   │   ├── UserBO.java                   #       用户查询结果
│   │   │   └── UserCreateBO.java             #       用户创建参数
│   │   ├── dept/                             #     dept 领域 BO
│   │   ├── menu/                             #     menu 领域 BO
│   │   ├── role/                             #     role 领域 BO
│   │   ├── post/                             #     post 领域 BO
│   │   └── dict/                             #     dict 领域 BO（DictType + DictData）
│   └── service/
│       ├── AuthAppService.java               #   认证应用服务接口
│       ├── UserAppService.java               #   用户应用服务接口
│       └── impl/
│           ├── AuthAppServiceImpl.java       #   认证应用服务实现
│           └── UserAppServiceImpl.java       #   用户应用服务实现
│
├── domain/                                   # ================== 领域层（按业务分包） ==================
│   ├── user/                                 #   user 领域
│   │   ├── do_/                                #     DO（因 Java 保留字 do 限制，使用 do_ 替代）
│   │   │   └── UserDO.java                   #     用户领域对象
│   │   ├── service/
│   │   │   └── UserDomainService.java        #     用户领域服务
│   │   └── repository/
│   │       └── UserRepository.java           #     仓储接口
│   │
│   ├── config/                               #   config 领域（字典/参数/公告）
│   │   └── ...
│   ├── log/                                  #   log 领域（操作日志/登录日志）
│   │   └── ...
│   ├── file/                                 #   file 领域（文件管理）
│   │   └── ...
│   └── profile/                              #   profile 领域（个人中心）
│       └── ...
│
├── infrastructure/                           # ================== 共享基础设施层 ==================
│   ├── repository/
│   │   └── impl/
│   │       └── UserRepositoryImpl.java       #   仓储实现
│   ├── po/
│   │   └── UserPO.java                       #   持久化对象
│   ├── mapper/
│   │   └── UserMapper.java                   #   MyBatis-Plus Mapper
│   ├── convertor/
│   │   └── UserConvertor.java                #   MapStruct 对象转换器
│   ├── query/
│   │   └── UserQuery.java                    #   查询参数对象
│   └── remote/                               #   外部系统调用（非领域间交互）
│       └── XxxRemote.java                    #   调用外部第三方服务
│
├── config/                                   # ================== 公共配置 ==================
│   ├── handler/                              #   全局异常处理器
│   ├── interceptor/                          #   拦截器（鉴权、权限等）
│   └── filter/                               #   过滤器
│
├── contract/                                 # ================== 服务内部契约 ==================
│
└── helper/                                   # ================== 辅助工具 ==================
```

---

## 3. 包命名规范

| 规则 | 说明 | 示例 |
|------|------|------|
| 领域子包使用业务英文小写 | 采用领域标识的小写形式 | `domain/auth/`、`domain/user/`、`domain/config/` |
| 领域标识去掉连字符 | 与模块命名一致 | `system-auth` → `auth`（在 domain 包内无需 system 前缀） |
| 共享层直接在 server 根包下 | adapter / application / infrastructure | 所有领域共用 |
| 公共包与共享层平级 | config / helper / contract 等放在 server 根包下 | 不属于任何特定领域 |
| BO 放在应用层 `application/bo/{领域}/` | BO 属于应用层，按领域分包 | `application/bo/user/UserBO.java` |

---

## 4. 包分离强制规范

1. **适配层、应用层、基础设施层必须共享**：所有领域共用 adapter / application / infrastructure 三层，禁止按领域拆分这三层
2. **领域层必须按业务分包**：不同业务领域的 DomainService、DO、Repository 接口必须放在各自的 `domain/{领域}/` 子包下；BO 放在应用层 `application/bo/{领域}/` 下
3. **领域内部保持内聚**：同一领域的 DomainService、DO、Repository 接口必须放在该领域的子包路径下；BO 按领域归入 `application/bo/{领域}/`
4. **应用层可直接调用所有领域服务**：AppService 可直接注入任何 DomainService，无需通过 Remote/Api 机制
5. **基础设施层按类型分包**：PO、Mapper、Convertor、RepositoryImpl 等按类型统一放在 infrastructure 各子包下
6. **Remote 仅用于外部系统调用**：`infrastructure/remote/` 仅封装对外部第三方服务的调用，不用于领域间交互

---

## 5. 领域子包结构详解

每个领域在 `domain/` 下拥有独立的子包，包含该领域的核心业务元素：

| 包路径 | 职责 | 典型内容 |
|--------|------|---------|
| `domain/{领域}/service/` | 领域服务 | DomainService 接口及实现 |
| `domain/{领域}/do_/` | 领域数据对象 | DO（贫血模型），因 Java 保留字 `do` 限制使用 `do_` |
| `application/bo/{领域}/` | 业务对象 | BO（应用层使用的业务参数/结果），按领域分包 |
| `domain/{领域}/repository/` | 仓储接口 | Repository 接口（实现类在 infrastructure） |
| `domain/{领域}/event/` | 领域事件 | 领域事件定义 |

**示例**：以 `user` 领域为例

```
# 应用层 BO（按领域分包，每个领域独立的 BO 包）
com.example.project.server.application.bo.auth/
├── LoginBO.java
└── LoginResultBO.java

com.example.project.server.application.bo.user/
├── UserBO.java
├── UserCreateBO.java
└── UserQueryBO.java

com.example.project.server.application.bo.dept/
├── DeptBO.java
└── DeptCreateBO.java

com.example.project.server.application.bo.menu/
├── MenuBO.java
└── MenuCreateBO.java

com.example.project.server.application.bo.role/
├── RoleBO.java
└── RoleCreateBO.java

com.example.project.server.application.bo.post/
├── PostBO.java
└── PostCreateBO.java

com.example.project.server.application.bo.dict/
├── DictTypeBO.java
├── DictTypeCreateBO.java
├── DictDataBO.java
└── DictDataCreateBO.java

# 领域层（不含 BO）
com.example.project.server.domain.user/
├── do_/                                 # 因 Java 保留字 do 限制，使用 do_ 替代
│   └── UserDO.java
├── service/
│   └── UserDomainService.java
├── repository/
│   └── UserRepository.java
└── event/
    └── UserCreatedEvent.java
```

---

## 6. 共享层职责说明

| 层级 | 路径 | 职责 |
|------|------|------|
| 适配层 | `adapter/controller/` | HTTP Controller、Scheduler、MQ Consumer |
| 应用层 | `application/service/` | AppService 接口 + impl，业务流程编排，事务控制 |
| 基础设施层 | `infrastructure/` | RepositoryImpl、PO、Mapper、Convertor、Remote（外部调用）、Cache |

### 公共包说明

| 公共包 | 用途 | 典型内容 |
|--------|------|---------|
| `config/` | 全局配置 | 全局异常处理器、拦截器、过滤器、MybatisPlus 配置、Redis 配置 |
| `contract/` | 服务内部契约 | 跨领域共享的常量、枚举 |
| `helper/` | 辅助工具 | 跨领域复用的工具类 |

> **判断原则**：如果一个类被 2 个及以上领域使用，则应放入公共包；如果仅被 1 个领域使用，则放入该领域的包内。

---

> **交叉引用**：
> - 领域隔离总览详见 [09-domain-isolation.md](./09-domain-isolation.md)
> - 领域间交互约束详见 [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md)
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
