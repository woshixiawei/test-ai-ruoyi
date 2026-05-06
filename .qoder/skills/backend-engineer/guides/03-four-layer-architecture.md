# 后端四层架构详细规范（导航索引）

- 版本号: v5.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> **本文件为导航索引文档**，四层架构各层的完整规范内容已拆分至独立子文档。请根据需要查阅对应层级。

---

## 四层架构概览

```
请求 → 适配层（Adapter） → 应用层（Application） → 领域层（Domain） → 基础设施层（Infrastructure）
         Controller              AppService            DomainService         Repository
         Scheduler               事务控制               业务逻辑              Mapper
         MQ Consumer             编排                    PO↔DO转换            Cache
         参数校验                BO↔DO转换              异常抛出              Client
```

**调用方向**：适配层 → 应用层 → 领域层 → 基础设施层（**严禁反向调用和跨层调用**）

---

## 规范文档索引

| 层级 | 文档 | 主题 | 关键内容 |
|------|------|------|---------|
| 适配层 | [03a-adapter-layer.md](./03a-adapter-layer.md) | Controller / Scheduler / MQ Consumer | Cmd→BO→AppService、ApiResponse封装、XXL-JOB定时任务、Kafka消费者 |
| 应用层 | [03b-application-layer.md](./03b-application-layer.md) | AppService 接口与实现 | BO入参出参、事务控制、领域服务编排、调用链示例、**参数校验（基本格式）** |
| 领域层 | [03c-domain-layer.md](./03c-domain-layer.md) | DomainService / DO / 值对象 | 贫血模型、PO↔DO转换、业务逻辑、Repository调用、**参数校验（业务规则）** |
| 基础设施层 | [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) | Repository / Mapper / Client / Cache | BaseRepository继承、Lambda查询、Hutool HttpUtil、OpenFeign、**软删除实现** |

---

## 快速导航

### 按编码场景
- 写 Controller → [03a-adapter-layer.md](./03a-adapter-layer.md)
- 写 AppService → [03b-application-layer.md](./03b-application-layer.md)
- 写 DomainService → [03c-domain-layer.md](./03c-domain-layer.md)
- 写 Repository / Mapper → [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
- 对象映射（MapStruct） → [05-object-mapping.md](./05-object-mapping.md)
- 领域隔离与跨领域调用 → [09-domain-isolation.md](./09-domain-isolation.md)

### 按关注点
- Cmd→BO→DO→PO 转换链 → [03b-application-layer.md](./03b-application-layer.md) + [03c-domain-layer.md](./03c-domain-layer.md)
- Repository 基础接口与实现 → [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) 第3节
- 外部服务调用（Hutool） → [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) 第5节
- 内部服务调用（OpenFeign） → [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) 第6节
- 参数校验（应用层格式/领域层业务规则） → [03b-application-layer.md](./03b-application-layer.md) 第5节 + [03c-domain-layer.md](./03c-domain-layer.md) 第6节
- 软删除实现（is_deleted = id） → [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) → [03d1-repository.md](./03d1-repository.md) 第4节

---

> **交叉引用**：
> - 项目结构与模块概述详见 [02-module-architecture.md](./02-module-architecture.md)
> - 技术栈与架构概述详见 [01-tech-stack.md](./01-tech-stack.md)
> - 代码风格与命名约定详见 [04-code-style.md](./04-code-style.md)
> - MapStruct 对象映射详见 [05-object-mapping.md](./05-object-mapping.md)
> - 异常体系与日志详见 [06-exception-and-logging.md](./06-exception-and-logging.md)
