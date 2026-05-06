# 后端 Java 编码规范文档（导航）

- 版本号: v3.6
- 技术栈: Java 21 + Maven
- 模块架构: 三模块（base / sdk / server）
- 架构模式: 四层架构（贫血模型）
- 适用范围: 所有后端模块
- 状态: 待评审

---

> **本文件为导航索引文档**，完整规范内容已拆分至 `guides/` 目录下的独立子文档。请根据需要查阅对应专题。

---

## 规范文档索引

| 序号 | 文档 | 主题 | 关键内容 |
|------|------|------|---------|
| 1 | [01-tech-stack.md](./guides/01-tech-stack.md) | 技术栈与架构强制要求 | Java 21、Maven、Spring Boot 3.x、四层架构、三模块架构 |
| 2 | [02-module-architecture.md](./guides/02-module-architecture.md) | 三模块架构与项目结构 | base/sdk/server 模块目录树、server 包结构 |
| 3 | [03-four-layer-architecture.md](./guides/03-four-layer-architecture.md) | 四层架构导航索引 | 适配层/应用层/领域层/基础设施层 → 各自独立子文档 |
| 3a | [03a-adapter-layer.md](./guides/03a-adapter-layer.md) | 适配层规范 | Controller、XXL-JOB定时任务、MQ Consumer |
| 3b | [03b-application-layer.md](./guides/03b-application-layer.md) | 应用层规范 | AppService接口与实现、BO入参出参、事务控制、调用链示例、**参数校验（基本格式）** |
| 3c | [03c-domain-layer.md](./guides/03c-domain-layer.md) | 领域层规范 | DomainService、贫血模型DO、值对象、PO↔DO转换、**参数校验（业务规则）** |
| 3d | [03d-infrastructure-layer.md](./guides/03d-infrastructure-layer.md) | 基础设施层导航索引 | Repository数据访问/外部服务调用 → 各自独立子文档 |
| 3d1 | [03d1-repository.md](./guides/03d1-repository.md) | Repository与数据访问 | PO定义、BaseRepository/BaseRepositoryImpl、Lambda查询、Query对象、**软删除实现（is_deleted=id）** |
| 3d2 | [03d2-external-service.md](./guides/03d2-external-service.md) | 外部服务调用 | Hutool HttpUtil（外部第三方）、OpenFeign（内部微服务）、降级配置 |
| 4 | [04-code-style.md](./guides/04-code-style.md) | 代码风格约定 | 缩进、命名规范、Javadoc、参数限制、注释 |
| 5 | [05-object-mapping.md](./guides/05-object-mapping.md) | MapStruct 对象映射 | Convertor 接口定义、使用规范 |
| 6 | [06-exception-and-logging.md](./guides/06-exception-and-logging.md) | 异常与日志导航索引 | 异常体系/日志管理 → 各自独立子文档 |
| 6a | [06a-exception.md](./guides/06a-exception.md) | 异常处理规范 | CommException体系、全局异常处理器、错误码、ApiResponse |
| 6b | [06b-logging.md](./guides/06b-logging.md) | 日志管理规范 | 日志级别、日志格式、敏感信息脱敏 |
| 7 | [07-testing-spec.md](./guides/07-testing-spec.md) | 测试规范 | 单元测试、集成测试、覆盖率要求 |
| 8 | [08-security-and-misc.md](./guides/08-security-and-misc.md) | 安全/事务/分页/并发 | 输入校验、SQL注入防护、事务管理、分页、并发控制 |
| 9 | [09-domain-isolation.md](./guides/09-domain-isolation.md) | 领域隔离导航索引 | 领域包分离 → 交互约束 → 各自独立子文档 |
| 9a | [09a-domain-package-structure.md](./guides/09a-domain-package-structure.md) | 领域包分离规范 | 包结构规范、命名规范、分包强制规范 |
| 9b | [09b-domain-interaction-rules.md](./guides/09b-domain-interaction-rules.md) | 领域间交互约束与API接口 | 禁止行为、本地/远程调用示例、API接口契约、Code Review检查清单 |

---

## 快速导航

### 新项目启动
1. 先读 [01-tech-stack.md](./guides/01-tech-stack.md) 确认技术选型和架构模式
2. 再读 [02-module-architecture.md](./guides/02-module-architecture.md) 搭建三模块项目结构
3. 按 [03-four-layer-architecture.md](./guides/03-four-layer-architecture.md) 组织四层架构代码
4. 配置 [06a-exception.md](./guides/06a-exception.md) 中的异常体系和全局异常处理器

### 日常编码
- 命名和格式 → [04-code-style.md](./guides/04-code-style.md)
- 写 Controller → [03a-adapter-layer.md](./guides/03a-adapter-layer.md)
- 写 AppService → [03b-application-layer.md](./guides/03b-application-layer.md)（含参数校验规范）
- 写 DomainService → [03c-domain-layer.md](./guides/03c-domain-layer.md)（含业务校验规范）
- 写 Repository → [03d1-repository.md](./guides/03d1-repository.md)
- 外部服务调用 → [03d2-external-service.md](./guides/03d2-external-service.md)
- 对象映射（MapStruct） → [05-object-mapping.md](./guides/05-object-mapping.md)
- 时间类型规范（Date + @JsonFormat + @DateTimeFormat） → [04-code-style.md](./guides/04-code-style.md) 第9节
- 字段注释规范 → [04-code-style.md](./guides/04-code-style.md) 第6节
- 软删除实现（is_deleted = id） → [03d1-repository.md](./guides/03d1-repository.md) 第4节
- 参数校验（应用层格式/领域层业务规则） → [03b-application-layer.md](./guides/03b-application-layer.md) 第5节 + [03c-domain-layer.md](./guides/03c-domain-layer.md) 第6节
- 领域包分离 → [09a-domain-package-structure.md](./guides/09a-domain-package-structure.md)
- 领域间交互与API接口 → [09b-domain-interaction-rules.md](./guides/09b-domain-interaction-rules.md)

### 质量保障
- 安全规范 → [08-security-and-misc.md](./guides/08-security-and-misc.md)
- 事务/分页/并发 → [08-security-and-misc.md](./guides/08-security-and-misc.md)
- 测试规范 → [07-testing-spec.md](./guides/07-testing-spec.md)
- 异常处理 → [06a-exception.md](./guides/06a-exception.md)
- 日志管理 → [06b-logging.md](./guides/06b-logging.md)

---

## 评审记录

| 评审人 | 评审意见 | 评审结果 |
|--------|---------|---------|
| | | |

## 变更记录

| 版本 | 变更内容 | 变更日期 |
|------|---------|---------|
| v1.0 | 初稿 | 2026-05-04 |
| v1.1 | 技术选型强制化（MySQL/MyBatis-Plus/Redisson/Kafka/Hutool/Lombok/MapStruct）；AppService层强制使用BO后缀；方法参数限制≤4个；MapStruct替代手写Convertor | 2026-05-04 |
| v1.2 | 领域层架构从充血模型改为贫血模型；增加PO包存放持久化对象；Domain层对象统一使用DO后缀；MapStruct转换路径更新为BO↔DO↔PO | 2026-05-04 |
| v1.3 | 适配层删除VO概念；入参统一使用ReqDTO后缀；出参统一使用RespDTO后缀；Controller通过Convertor进行ReqDTO↔BO↔RespDTO转换 | 2026-05-04 |
| v2.0 | 新增三模块架构（base/sdk/server）；base模块定义公共基础设施（基础类、异常体系、统一响应、工具类）；sdk模块定义对外接口契约（入参Cmd/ReqDTO、出参RespDTO、业务枚举）；server模块承载四层架构和业务实现；Controller继承BaseController并引用sdk模块DTO；统一响应从Result改为ApiResponse；更新所有代码示例 | 2026-05-04 |
| v2.1 | 新增XXL-JOB分布式定时任务规范（禁用Spring @Scheduled）；外部服务调用强制使用Hutool HttpUtil（禁用RestTemplate）；新增OpenFeign内部服务调用规范（Feign Client定义在sdk模块）；异常体系新增OpenFeignAccessException；全局异常处理器改为单入口+instanceof分发模式（参考shenji项目）；新增isFatal致命异常分类机制；新增ErrRecordDomainService异常记录服务；错误码新增OPEN_FEIGN_ACCESS_ERR_CODE | 2026-05-04 |
| v2.2 | 新增领域隔离与跨领域交互规范：强制按业务领域分包（user/order/inventory等）；禁止直接访问其他领域内部实现（Mapper/RepositoryImpl/DomainService）；同一服务内跨领域调用必须通过AppService接口；跨服务调用必须使用OpenFeign；新增领域隔离Code Review检查清单 | 2026-05-04 |
| v2.4 | 规范文档拆分重构：03-four-layer-architecture.md（890行→33行导航+4个独立子文档03a/03b/03c/03d）、09-domain-isolation.md（415行→35行导航+2个独立子文档09a/09b）；每个子文档主题聚焦，原文档改为导航索引；更新STANDARDS.md导航链接 | 2026-05-04 |
| v2.5 | 进一步拆分大型子文档：03d-infrastructure-layer.md（432行→导航索引+03d1-repository+03d2-external-service）、06-exception-and-logging.md（328行→导航索引+06a-exception+06b-logging）；消除重复描述、精简代码示例 | 2026-05-04 |
| v3.0 | 架构模式重大调整：从"领域根包模式"（每个领域独立四层）改为"共享层级+领域分包"模式（adapter/application/infrastructure共享，domain按业务分包）；移除Remote→Api跨域调用机制，改为AppService直接调用DomainService；Remote包移入infrastructure/下用于外部系统调用；更新09a/09b/09/02四个规范文档 | 2026-05-05 |
| v3.1 | 新增参数校验规范（03b第5节应用层格式校验+03c第6节领域层业务校验）；新增软删除实现规范（03d1第4节：is_deleted bigint(20)类型，0=未删除，记录ID=已删除，BaseRepositoryImpl覆写removeById）；更新01-mysql-design-spec.md软删除字段定义和删除策略 | 2026-05-05 |
| v3.2 | 软删除实现改为MyBatis-Plus原生方案：logic-delete-value从1改为id（MySQL列引用）、@TableLogic delval从1改为id；移除BaseRepositoryImpl.removeById/removeByIds手动覆写；removeByIds()由逐条更新优化为单条SQL批量更新；MySQL规范DDL模板唯一索引改为联合唯一索引 | 2026-05-05 |
| v3.4 | 新增日期时间类型规范（04-code-style.md 第9节）：禁止使用LocalDateTime强制统一使用java.util.Date；所有Date字段必须添加@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)；CMD和DTO类Date字段必须额外添加@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN) | 2026-05-05 |
| v3.5 | 参数校验规范升级：领域层从“禁止做格式校验”改为“必须做参数格式校验（与应用层双重保障）”；明确校验异常分类：参数格式校验不通过抛ParamException，业务规则校验不通过抛BusinessException；校验顺序：应用层先格式校验→领域层格式校验+业务规则校验；更新03b/03c规范文档 | 2026-05-05 |
| v3.6 | MapStruct使用规范强化：所有Convertor必须定义updateDomainFromBO方法（@MappingTarget更新场景）；分页对象转换强制使用PageDO.from/PageBO.from/PageDTO.from工厂方法；明确允许手动setter的6种例外场景；DeptConvertor/MenuConvertor/PostConvertor新增updateDomainFromBO方法；DeptAppServiceImpl/MenuAppServiceImpl/PostAppServiceImpl重构为MapStruct更新模式；3个AppServiceImpl+3个Controller+3个DomainService的分页转换重构为工厂方法 | 2026-05-05 |
