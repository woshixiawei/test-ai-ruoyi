# 架构师设计规范文档（导航）

- 版本号: v1.1
- 适用范围: 所有项目技术设计与数据库设计
- 状态: 待评审

---

## 核心规范摘要（AI速查）

> 以下为架构师设计时必须遵守的核心规则，完整规范详见下方 guides/ 链接。

### 数据库设计红线
| 规则 | 正确 | 禁止 |
|------|------|------|
| 主键 | `bigint unsigned AUTO_INCREMENT` | 自增int、UUID |
| 时间字段 | `datetime NOT NULL DEFAULT CURRENT_TIMESTAMP` | timestamp、varchar存时间 |
| 软删除字段 | `is_deleted bigint(20) DEFAULT 0`，删除时值为id | tinyint(1) 0/1 |
| 唯一索引 | `UNIQUE KEY uk_xxx_deleted(业务字段, is_deleted)` 联合唯一 | 仅对业务字段建唯一索引 |
| 表注释 | 每张表必须有COMMENT | 无注释 |
| DDL输出 | 可直接执行的 `CREATE TABLE` 语句 | 纯表格描述 |

### 设计一致性要求
1. 技术选型必须与后端编码规范中声明的强制技术栈一致（MyBatis-Plus、Redisson、MapStruct等）
2. 四层架构分层必须与后端规范中的分层一致
3. API接口设计必须遵循Cmd/DTO命名和转换规范
4. 异常体系和统一响应封装必须遵循ApiResponse和错误码规范

---

> **本文件为导航索引文档**，完整规范内容已拆分至 `guides/` 目录下的独立子文档。请根据需要查阅对应专题。

---

## 前后端编码规范（技术设计必读）

> **【强制】** 架构师在进行技术设计时，**必须**先查阅以下前后端编码规范导航文件，确保技术设计方案与编码规范保持一致。

| 规范类别 | 导航文件路径 | 说明 |
|---------|------------|------|
| **后端编码规范** | `.qoder/skills/backend-engineer/STANDARDS.md` | Java 21技术栈、三模块架构、四层架构、Repository、MapStruct、异常体系、日志、领域隔离等完整后端规范索引 |
| **前端编码规范** | `.qoder/skills/frontend-engineer/STANDARDS.md` | Vue 3 + TypeScript技术栈、项目结构、代码风格、TypeScript规范、测试与性能等完整前端规范索引 |

---

## 规范文档索引

| 序号 | 文档 | 主题 | 关键内容 |
|------|------|------|--------|
| 1 | [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) | MySQL 数据库设计规范 | 命名规范、数据类型、索引设计、表结构、SQL编写、分区分表、安全规范 |

---

## 快速导航

### 数据库设计
- 库名/表名/字段名命名规则 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第1节
- 数据类型选择 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第2节
- 索引设计原则 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第3节
- 表结构设计模板 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第4节
- SQL 编写标准 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第5节
- 分区分表策略 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第6节
- 安全规范 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第7节
- 技术设计文档模板 → [01-mysql-design-spec.md](./guides/01-mysql-design-spec.md) 第8节

---

## 变更记录

| 版本 | 变更内容 | 变更日期 |
|------|---------|---------|
| v1.1 | 新增前后端编码规范引用：架构师进行技术设计时必须参考后端STANDARDS.md和前端STANDARDS.md，确保设计与实现一致 | 2026-05-04 |
