# 领域隔离与跨领域交互规范（导航索引）

- 版本号: v4.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> **本文件为导航索引文档**，领域隔离的完整规范内容已拆分至独立子文档。请根据需要查阅对应专题。

---

## 领域隔离概览

```
┌──────────────────────────────────────────────────────────────────────┐
│                        server 模块                                    │
│                                                                      │
│  adapter/controller/          ← 共享适配层（所有领域 Controller）      │
│  application/service/         ← 共享应用层（AppService 可跨域编排）    │
│                                                                      │
│  domain/                      ← 领域层（按业务分包）                  │
│    ├── auth/                  ← 认证领域（仅BO，认证逻辑由AuthAppService编排）   │
│    ├── user/                  ← 用户领域（DO/BO/DomainService/Repo）   │
│    ├── config/                ← 配置领域                              │
│    └── ...                                                          │
│                                                                      │
│  infrastructure/              ← 共享基础设施层                        │
│    ├── po/ mapper/ convertor/ ← 数据访问                              │
│    ├── repository/impl/       ← 仓储实现                              │
│    └── remote/                ← 外部系统调用（非领域间交互）            │
│                                                                      │
│  config/  contract/  helper/  ← 公共包                                │
│                                                                      │
│  领域间交互：AppService 直接调用各领域 DomainService                   │
│  禁止：Controller 直接注入 DomainService/Repository                  │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 规范文档索引

| 主题 | 文档 | 关键内容 |
|------|------|---------|
| 领域包分离 | [09a-domain-package-structure.md](./09a-domain-package-structure.md) | 共享层级+领域分包结构、命名规范、强制规范 |
| 领域间交互约束 | [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md) | 允许/禁止行为、跨域调用示例、Remote包定位、Code Review检查清单 |

---

## 快速导航

### 按场景
- 新建业务领域如何分包 → [09a-domain-package-structure.md](./09a-domain-package-structure.md)
- AppService 跨域调用 → [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md) 第4节
- Code Review 检查 → [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md) 第7节

---

> **交叉引用**：
> - 三模块架构与项目结构详见 [02-module-architecture.md](./02-module-architecture.md)
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 代码风格与命名约定详见 [04-code-style.md](./04-code-style.md)
> - OpenFeign 内部服务调用规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) 第6节
