# 后端技术栈与架构强制要求

- 版本号: v2.1
- 技术栈: Java 21 + Maven
- 模块架构: 三模块（base / sdk / server）
- 架构模式: 四层架构（贫血模型）
- 适用范围: 所有后端模块
- 约束力: **强制**

---

**本章节为强制性要求，所有后端开发必须严格遵守。**

### 0.1 技术栈（强制）
- **JDK**: Java 21（LTS），充分利用新特性（Record、Pattern Matching、Sealed Classes、Virtual Threads 等）
- **构建工具**: Maven 3.9+
- **框架**: Spring Boot 3.x + Spring Framework 6.x
- **数据库**: MySQL 8.0+（**强制，严禁使用 PostgreSQL 或其他数据库**）
- **数据访问**: MyBatis-Plus（**强制，严禁使用原生 MyBatis、JDBC 或 Spring Data JPA**）
- **缓存**: Redis（**Redisson 客户端，强制，严禁使用 Jedis、Lettuce 直连或其他 Redis 客户端**）
- **消息队列**:
  - 低并发场景：Redis List / Redis Stream
  - 高并发场景：Kafka（**强制，严禁使用 RabbitMQ、RocketMQ 或其他消息中间件**）
- **API 风格**: RESTful + JSON
- **代码简化**: Lombok（**强制**）
- **对象映射**: MapStruct（**强制，严禁手写 Convertor**）
- **工具类**: Hutool（**优先使用，严禁重复造轮子**）
- **定时任务**: XXL-JOB（**强制，严禁使用 Spring @Scheduled 进行生产环境定时任务调度**）
- **内部服务调用**: OpenFeign（**微服务间远程调用强制使用，严禁用 HttpUtil 调用内部服务**）
- **外部服务调用**: Hutool HttpUtil（**第三方外部服务调用强制使用，严禁使用 RestTemplate**）

### 0.2 四层架构（强制）

本项目严格遵循**四层架构**（适配层 → 应用层 → 领域层 → 基础设施层），各层职责边界清晰，严禁跨层调用。

```
┌─────────────────────────────────────────┐
│           适配层 (Adapter Layer)          │
│  Controller / API / Cron / MQ Consumer   │
│  职责: 协议适配、参数校验、响应封装          │
├─────────────────────────────────────────┤
│         应用层 (Application Layer)        │
│        AppService / AppServiceImpl       │
│  职责: 应用编排、事务控制、用例协调          │
├─────────────────────────────────────────┤
│          领域层 (Domain Layer)            │
│   DomainService / DO / ValueObject       │
│  职责: 核心业务逻辑、领域规则、业务计算       │
├─────────────────────────────────────────┤
│       基础设施层 (Infrastructure Layer)   │
│     Mapper/Repository / Client / Cache   │
│  职责: 数据持久化、外部调用、技术实现          │
└─────────────────────────────────────────┘
```

**调用规则**（严格单向，不可逆）：
- 适配层 → 应用层 → 领域层 → 基础设施层
- 领域层 **不允许** 调用应用层或适配层
- 应用层 **不允许** 调用适配层
- 基础设施层 **不允许** 调用领域层、应用层、适配层

### 0.3 三模块架构（强制）

本项目采用 **三模块架构**，通过 Maven 多模块管理，各模块职责边界清晰，严禁跨模块乱依赖。

```
┌──────────────────────────────────────────────────────────┐
│                      parent (pom)                        │
│             版本管理 + 依赖统一声明                        │
├──────────┬──────────────────┬────────────────────────────┤
│          │                  │                            │
│  ${project}-base   │   ${project}-sdk   │      ${project}-server       │
│  公共基础模块   │   对外接口定义模块  │      服务实现模块            │
│          │                  │                            │
│  · 基础类/抽象类  │  · 入参（ReqDTO/Cmd）│  · Controller             │
│  · 统一响应封装   │  · 出参（RespDTO）  │  · AppService / DomainService │
│  · 异常体系     │  · 业务枚举       │  · Repository / Mapper       │
│  · 通用工具类    │  · API 接口定义    │  · Config / Client          │
│  · 分页/分查询基类 │                  │  · Spring Boot 启动类        │
│          │                  │                            │
└──────────┴──────────────────┴────────────────────────────┘
```

**模块依赖关系**（严格单向，不可循环）:
```
server  ──依赖──>  sdk  ──依赖──>  base
```

| 模块 | 职责 | 可依赖 | 不可依赖 | 打包方式 |
|------|------|--------|---------|--------|
| **base** | 公共基础：基础类、异常体系、统一响应、工具类、分页基类 | 第三方库 | sdk、server | jar |
| **sdk** | 对外接口定义：入参（ReqDTO/Cmd）、出参（RespDTO）、业务枚举、API接口 | base | server | jar |
| **server** | 服务实现：完整四层架构、业务逻辑、Spring Boot 启动 | sdk（自动引入 base） | 无 | jar（可执行） |

**强制规则**:
1. **base 模块零业务**：base 模块不包含任何业务逻辑，只提供公共基础设施
2. **sdk 模块零实现**：sdk 模块只定义数据契约（DTO/枚举/API接口），不包含任何业务实现
3. **server 是唯一可运行模块**：只有 server 模块包含 Spring Boot 启动类和完整业务实现
4. **依赖方向严格单向**：server → sdk → base，严禁反向依赖或循环依赖
5. **接口版本化管理**：sdk 模块发布到 Maven 仓库供外部消费，需遵循语义化版本

---

> **交叉引用**：
> - 三模块项目结构详见 [02-module-architecture.md](./02-module-architecture.md)
> - 四层架构各层规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 代码风格约定详见 [04-code-style.md](./04-code-style.md)
