# 后端三模块架构与项目结构

- 版本号: v3.0
- 适用范围: 所有后端模块
- 约束力: **强制**

---

> **TL;DR 核心要点**：
> 1. 三模块架构：**base**（公共基础设施）/ **sdk**（对外接口契约）/ **server**（业务实现）
> 2. base模块：BaseController、异常体系、ApiResponse、工具类、PageBO/PageDTO
> 3. sdk模块：Cmd（入参）、RespDTO（出参）、业务枚举、Feign Client
> 4. server模块：四层架构（adapter/application/domain/infrastructure）
> 5. 依赖方向：server → sdk → base，**禁止反向依赖**
> 6. Controller继承BaseController，返回统一ApiResponse

---

> 技术栈与三模块架构概述详见 [01-tech-stack.md](./01-tech-stack.md)

---

## 1. 项目整体结构规范

```
project-parent/                          # Maven 父工程 (pom)
├── pom.xml                              # 版本管理 + 依赖统一声明
├── project-base/                        # ===================== base 模块 =====================
│   ├── pom.xml
│   └── src/main/java/com/example/project/base/
│       ├── basecls/                     # 基础抽象类
│       │   ├── BaseApi.java             #   API 基类（统一 succ/fail 响应）
│       │   ├── BaseController.java      #   Controller 基类（继承 BaseApi）
│       │   ├── BaseEnum.java            #   枚举基类接口（MyBatis-Plus IEnum）
│       │   ├── BaseRepository.java      #   Repository 基础接口（继承 IService<T>）
│       │   ├── BaseRepositoryImpl.java  #   Repository 基础实现类（继承 ServiceImpl<M,T>）
│       │   ├── EnumTypeHandler.java     #   枚举类型转换器
│       │   └── *ConverterFactory.java   #   枚举转换工厂（Spring MVC 参数绑定）
│       ├── cmd/                         # 命令基类
│       │   └── PageCmd.java            #   分页命令抽象类
│       ├── constant/                    # 常量
│       │   └── ErrConstant.java        #   错误码常量
│       ├── dto/                         # 基础 DTO
│       │   ├── ApiResponse.java        #   统一响应封装（code/msg/data/traceId）
│       │   ├── PageDTO.java            #   分页响应 DTO
│       │   └── ValueLabelDTO.java      #   值标签 DTO（下拉框等）
│       ├── enums/                       # 基础枚举
│       ├── exceptions/                  # 异常体系
│       │   ├── CommException.java      #   公共异常抽象基类（含 code + isFatal）
│       │   ├── BusinessException.java  #   业务异常
│       │   ├── ParamException.java     #   参数异常
│       │   ├── SystemException.java    #   系统异常
│       │   ├── NotLoginException.java  #   未登录异常
│       │   └── NoPermissionException.java # 无权限异常
│       ├── model/                       # 基础模型
│       │   ├── PageDO.java             #   分页领域对象（DomainService → AppService）
│       │   ├── PageBO.java             #   分页业务对象（AppService → Controller）
│       │   └── PageSearchDO.java       #   分页查询对象
│       ├── query/                       # 查询基类
│       │   └── PageQuery.java          #   分页查询抽象类
│       ├── typecollection/              #   类型集合（MyBatis-Plus JSON 字段类型）
│       │   ├── IntList.java
│       │   ├── LongList.java
│       │   ├── StrList.java
│       │   └── ...
│       └── util/                        # 通用工具类
│           ├── JacksonUtil.java         #   JSON 序列化工具（Jackson 封装）
│           ├── CommUtil.java            #   公共工具（密码加密等）
│           ├── ObjUtil.java             #   对象属性复制工具
│           └── ...
│
├── project-sdk/                         # ===================== sdk 模块 =====================
│   ├── pom.xml                          #   依赖 base 模块
│   └── src/main/java/com/example/project/sdk/
│       ├── api/                         # 对外 API 接口定义（供远程调用）
│       ├── cmd/                         # 命令（Controller 入参）
│       │   ├── user/                    #   用户模块命令
│       │   │   ├── UserCreateCmd.java
│       │   │   ├── UserUpdateCmd.java
│       │   │   └── LoginCmd.java
│       │   ├── order/                   #   订单模块命令
│       │   └── ...
│       ├── contract/                    # 契约定义
│       │   └── enums/                   #   业务枚举
│       ├── dto/                         # 响应 DTO（Controller 出参）
│       │   ├── user/                    #   用户模块 DTO
│       │   ├── order/                   #   订单模块 DTO
│       │   └── ...
│       └── typecollection/              #   类型集合（特定业务类型）
│
├── project-server/                      # ===================== server 模块 =====================
│   ├── pom.xml                          #   依赖 sdk 模块（自动引入 base）
│   └── src/main/java/com/example/project/server/
│       ├── ServerApplication.java       # Spring Boot 启动类
│       │
│       ├── adapter/                     #   共享适配层
│       │   └── controller/backend/
│       ├── application/                 #   共享应用层
│       │   └── service/
│       ├── domain/                      #   领域层（按业务分包）
│       │   ├── auth/
│       │   ├── user/
│       │   ├── config/
│       │   ├── log/
│       │   ├── file/
│       │   └── profile/
│       ├── infrastructure/              #   共享基础设施层
│       │   ├── repository/impl/
│       │   ├── po/
│       │   ├── mapper/
│       │   ├── convertor/
│       │   ├── query/
│       │   └── remote/                  #   外部系统调用
│       │
│       ├── config/                      #   公共全局配置
│       │   ├── handler/
│       │   ├── interceptor/
│       │   └── filter/
│       ├── contract/                    #   服务内部契约
│       └── helper/                      #   辅助工具
```

---

## 2. server 模块包结构规范（共享层级 + 领域分包）

server 模块采用**共享层级 + 领域分包**模式：适配层、应用层、基础设施层在所有领域间共享，领域层按业务领域分包。AppService 可直接调用所有领域的 DomainService。

> **领域分包详细规范** 详见 [09a-domain-package-structure.md](./09a-domain-package-structure.md)

### 2.1 server 包结构示例

```
src/main/java/com/example/project/server/
├── ServerApplication.java
├── adapter/                              # 共享适配层
│   └── controller/backend/
│       ├── AuthController.java
│       └── UserController.java
├── application/                          # 共享应用层
│   └── service/
│       ├── AuthAppService.java
│       ├── UserAppService.java
│       └── impl/
│           ├── AuthAppServiceImpl.java
│           └── UserAppServiceImpl.java
├── domain/                               # 领域层（按业务分包）
│   ├── auth/                             #   auth 领域（仅 BO，认证逻辑由 AuthAppService 编排）
│   │   └── bo/                           #     BO（LoginBO、LoginResultBO）
│   └── user/                             #   user 领域
│       ├── do_/                          #     DO
│       ├── service/                      #     DomainService
│       ├── repository/                   #     仓储接口
│       └── bo/                           #     BO
├── infrastructure/                       # 共享基础设施层
│   ├── repository/impl/                  #   仓储实现
│   ├── po/                               #   PO
│   ├── mapper/                           #   Mapper
│   ├── convertor/                        #   Convertor
│   ├── query/                            #   查询参数
│   └── remote/                           #   外部系统调用
├── config/                               # 公共配置
│   ├── handler/
│   ├── interceptor/
│   └── filter/
├── contract/                             # 公共契约
└── helper/                               # 公共工具
```

### 2.2 领域子包命名规则

| 领域标识 | domain 子包名 | 说明 |
|---------|--------------|------|
| system-auth | `domain/auth/` | 去掉 system 前缀 |
| system-user | `domain/user/` | 去掉 system 前缀 |
| system-config | `domain/config/` | 去掉 system 前缀 |
| system-log | `domain/log/` | 去掉 system 前缀 |
| system-file | `domain/file/` | 去掉 system 前缀 |
| system-profile | `domain/profile/` | 去掉 system 前缀 |

---

> **交叉引用**：
> - 四层架构各层详细规范（适配层/应用层/领域层/基础设施层）详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - **领域分包规范（强制）** 详见 [09a-domain-package-structure.md](./09a-domain-package-structure.md)
> - 领域间交互约束与 API 接口详见 [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md)
> - 代码风格与命名约定详见 [04-code-style.md](./04-code-style.md)
> - 异常体系与统一响应详见 [06-exception-and-logging.md](./06-exception-and-logging.md)
