# 基础设施层（Infrastructure Layer）规范（导航索引）

- 版本号: v2.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> **本文件为导航索引文档**，基础设施层的完整规范内容已拆分至独立子文档。

---

## 基础设施层职责边界

**职责边界**:
- 数据持久化（MyBatis-Plus Mapper + Repository）
- **Repository 接口**（继承 BaseRepository<T>，获得 IService CRUD 能力）
- **Repository 实现**（继承 BaseRepositoryImpl<M,T>，封装 Mapper 查询操作）
- 外部服务调用（HTTP Client / RPC Client）
- 缓存操作（Redisson）
- 消息队列发送（Kafka / Redis Stream）
- 文件存储、搜索引擎等技术实现
- **Convertor**（MapStruct 对象映射器）
- **Query 对象**（查询参数封装，当方法入参超过 4 个时使用）

---

## 规范文档索引

| 主题 | 文档 | 关键内容 |
|------|------|---------|
| Repository 与数据访问 | [03d1-repository.md](./03d1-repository.md) | PO定义、BaseRepository/BaseRepositoryImpl、Lambda查询、Query对象 |
| 外部服务调用 | [03d2-external-service.md](./03d2-external-service.md) | Hutool HttpUtil（外部第三方）、OpenFeign（内部微服务）、降级配置 |

---

## 基础设施层强制规范

1. **接口定义在 `infrastructure.repository` 包下**：Repository 接口在 `infrastructure.repository` 中定义，实现在 `infrastructure.repository.impl` 中
2. **所有 Repository 必须继承 BaseRepository/BaseRepositoryImpl**：获得 MyBatis-Plus IService/ServiceImpl 的通用 CRUD 能力
3. **数据转换**：Domain 对象（DO）↔ 持久化对象（PO）必须通过 Convertor 转换
4. **缓存统一封装**：禁止在业务代码中直接操作 RedisTemplate，使用封装好的 Cache 类
5. **外部服务调用使用 Hutool HttpUtil**：第三方外部服务调用必须使用 `HttpRequest` / `HttpUtil`，严禁使用 RestTemplate
6. **内部服务调用使用 OpenFeign**：微服务间远程调用必须使用 Feign Client，严禁使用 HttpUtil 调用内部服务
7. **外部调用必须有降级**：OpenFeign 使用 FallbackFactory，HttpUtil 使用 try-catch + 重试
8. **异常转换**：外部服务异常转换为领域层可理解的异常（SystemException / OpenFeignAccessException）
9. **自定义查询使用 Lambda 方式**：使用 `lambdaQuery()` 或 `Wrappers.lambdaQuery()` 构建查询条件，禁止手写 XML 映射文件

---

> **交叉引用**：
> - 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 适配层规范详见 [03a-adapter-layer.md](./03a-adapter-layer.md)
> - 应用层规范详见 [03b-application-layer.md](./03b-application-layer.md)
> - 领域层规范详见 [03c-domain-layer.md](./03c-domain-layer.md)
