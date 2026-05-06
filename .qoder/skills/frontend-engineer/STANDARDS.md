# 前端编码规范文档（导航）

- 版本号: v1.1
- 适用范围: 所有前端项目
- 状态: 待评审

---

## 核心规范摘要（AI速查）

> 以下为前端编码时必须遵守的核心规则，详细规范详见 guides/ 目录。
> **核心规范速查见 `QUICK_REF.md`（编码前必读）。**

### 前端编码红线
| 规则 | 正确 | 禁止 |
|------|------|------|
| 框架 | Vue 3 + `<script setup>` + Composition API | Vue 2、React |
| 语言 | TypeScript（`.ts`/`.vue` 含 `lang="ts"`） | `.js`文件 |
| 类型 | 明确类型，interface优先 | `any` |
| 组件库 | Element Plus等开源组件库优先 | 自行开发基础组件 |
| 状态管理 | Pinia | Vuex |
| 缩进 | Tab字符 | 空格 |
| 注释语言 | 中文 | 英文注释 |
| API调用 | 项目统一axios封装 | 裸用axios |

---

> **本文件为导航索引文档**，完整规范内容已拆分至 `guides/` 目录下的独立子文档。请根据需要查阅对应专题。

---

## 规范文档索引

| 序号 | 文档 | 主题 | 关键内容 |
|------|------|------|---------|
| 1 | [01-tech-stack.md](./guides/01-tech-stack.md) | 技术栈强制要求 | Vue 3、TypeScript、开源优先原则 |
| 2 | [02-code-style.md](./guides/02-code-style.md) | 代码风格与注释 | 缩进（Tab）、命名规范、BEM、JSDoc、ESLint/Prettier |
| 3 | [03-project-structure.md](./guides/03-project-structure.md) | 项目结构与组件设计 | 目录结构、组件分类、Props 设计、组件通信 |
| 4 | [04-typescript.md](./guides/04-typescript.md) | TypeScript 编码标准 | TS 强制要求、编码规范、API 调用规范 |
| 5 | [05-testing-and-perf.md](./guides/05-testing-and-perf.md) | 样式/测试/性能/Git | 样式管理、测试规范、性能优化、Git 提交 |

---

## 快速导航

### 新项目启动
1. 先读 [01-tech-stack.md](./guides/01-tech-stack.md) 确认技术选型
2. 再读 [03-project-structure.md](./guides/03-project-structure.md) 搭建项目结构
3. 配置 [02-code-style.md](./guides/02-code-style.md) 中的 ESLint/Prettier

### 日常编码
- 命名和格式 → [02-code-style.md](./guides/02-code-style.md)
- TypeScript 编码 → [04-typescript.md](./guides/04-typescript.md)
- 组件开发 → [03-project-structure.md](./guides/03-project-structure.md)

### 质量保障
- 样式规范 → [05-testing-and-perf.md](./guides/05-testing-and-perf.md) 第1节
- 测试规范 → [05-testing-and-perf.md](./guides/05-testing-and-perf.md) 第2节
- 性能优化 → [05-testing-and-perf.md](./guides/05-testing-and-perf.md) 第3节
- Git 规范 → [05-testing-and-perf.md](./guides/05-testing-and-perf.md) 第4节

---

## 评审记录

| 评审人 | 评审意见 | 评审结果 |
|--------|---------|---------|
| | | |

## 变更记录

| 版本 | 变更内容 | 变更日期 |
|------|---------|---------|
| v1.0 | 初稿 | 2026-05-04 |
| v1.1 | 新增技术栈强制要求；缩进改为Tab；组件开发强化开源优先；目录结构补充第三方模板说明；全面禁用JavaScript | 2026-05-04 |
