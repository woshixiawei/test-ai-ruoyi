# 前端编码规范速查表（AI编码强制遵守）

> **本文件为AI编码时的强制速查参考，提取自完整编码规范文档。**
> **AI在编码前必须阅读本文件，编码过程中必须遵守所有规则。**
> 完整规范详见 `.qoder/skills/frontend-engineer/guides/` 目录。

---

## 1. 技术栈与语言（违反即拒绝）

| 规则 | 正确 | 禁止 |
|------|------|------|
| 前端框架 | Vue 3（`<script setup>` + Composition API） | Vue 2、React、Angular |
| 编程语言 | TypeScript（`.ts` / `.vue` 含 `lang="ts"`） | JavaScript（`.js` 文件一律禁止） |
| 路由管理 | Vue Router 4 | 其他路由方案 |
| 状态管理 | Pinia | Vuex |
| 组件库 | Element Plus 等开源组件库优先 | 自行开发基础组件 |

## 2. 代码风格（违反即拒绝）

| 规则 | 正确 | 禁止 |
|------|------|------|
| 缩进 | Tab 字符 | 空格缩进 |
| 引号 | 单引号 `''` | 双引号 `""`（HTML属性除外） |
| 分号 | 必须使用分号 | 省略分号 |
| 尾随逗号 | 多行对象/数组最后一个元素后加逗号 | 不加尾随逗号 |
| 行宽 | 不超过120字符 | 超长行不换行 |
| 变量声明 | `const` 优先，需要重赋值用 `let` | `var` |

## 3. TypeScript 规范（违反即拒绝）

```typescript
// ✅ 正确：明确类型、接口优先
interface UserInfo {
  id: string;
  name: string;
  age?: number;
}

// ❌ 禁止：使用 any
const user: any = {};  // 禁止！
// ❌ 禁止：使用 type 定义对象结构
type UserInfo = { id: string };  // 应使用 interface
```

### 强制规则清单
1. **禁止 `any`**：必须使用具体类型或 `unknown` + 类型守卫
2. **接口优先**：定义对象结构用 `interface`，联合类型用 `type`
3. **显式返回值**：函数必须声明返回值类型
4. **严格模式**：`tsconfig.json` 必须 `strict: true`
5. **禁止 `.js` 文件**：所有源文件必须为 `.ts` / `.vue`

## 4. 命名规范（违反即拒绝）

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件文件 | PascalCase | `UserProfile.vue` |
| 工具函数文件 | camelCase | `dateUtils.ts` |
| 常量/配置 | UPPER_SNAKE_CASE | `API_CONFIG.ts` |
| 类型定义 | kebab-case + `.types` | `user.types.ts` |
| 目录名 | kebab-case | `user-profile/` |
| 变量/函数 | camelCase | `getUserInfo()` |
| 常量 | UPPER_SNAKE_CASE | `const MAX_COUNT = 3;` |
| 布尔变量 | is/has/can 前缀 | `isLoading`, `hasPermission` |
| 组件/类/接口 | PascalCase | `UserService`, `UserProps` |
| 事件命名 | on + 动词 | `onClick`, `onSubmit` |
| CSS类名 | BEM规范 | `.user-card__avatar--active` |

## 5. 组件开发规范（违反即拒绝）

### 5.1 开源组件库优先（强制）
```typescript
// ✅ 正确：使用开源组件
<el-table :data="tableData">

// ❌ 禁止：自行开发基础组件
<my-custom-table :data="tableData">  // 禁止！Element Plus有Table组件
```

### 5.2 Props 设计规范
```typescript
// ✅ 正确：声明类型 + JSDoc注释 + 默认值
interface Props {
  /** 用户ID */
  userId: string;
  /** 是否可编辑 */
  editable?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  editable: false,
});

// ❌ 禁止：无类型Props
const props = defineProps(['userId', 'editable']);  // 禁止！
```

### 5.3 组件文件结构模板
```vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
/**
 * @file 组件名称
 * @description 组件功能描述
 */

// ========== Props & Emits ==========
// ========== State ==========
// ========== Computed ==========
// ========== Methods ==========
// ========== Lifecycle ==========
</script>

<style scoped lang="scss">
.component-name {
  /* 样式 */
}
</style>
```

### 5.4 组件通信规范
| 场景 | 方式 | 说明 |
|------|------|------|
| 父→子 | Props | 单向数据流，禁止子组件修改props |
| 子→父 | Emit | `defineEmits` 定义事件 |
| 跨多层 | Provide/Inject | 仅用于主题、国际化等 |
| 跨组件/跨页面 | Pinia | 全局状态管理 |

## 6. 样式规范（违反即拒绝）

| 规则 | 正确 | 禁止 |
|------|------|------|
| 颜色 | SCSS/CSS变量 | 硬编码颜色值 |
| z-index | 变量统一管理 | 随意写 `99999` |
| 选择器深度 | ≤3层 | 过深嵌套 |
| !important | 仅覆盖第三方库 | 正常样式使用 |
| 样式方案 | Scoped CSS / CSS Module | 全局污染 |
| 字体单位 | `rem` | `px`（字体大小场景） |

## 7. API调用规范（违反即拒绝）

```typescript
// ✅ 正确：统一请求封装 + 类型定义
import request from '@/http/axios';

async function fetchUsers(): Promise<ApiResponse<UserDTO[]>> {
  return request.get('/backend/system/user/list');
}

// ❌ 禁止：裸用axios
import axios from 'axios';
const res = await axios.get('/api/users');  // 禁止！必须使用统一封装

// ❌ 禁止：无类型定义
async function fetchUsers() {  // 缺少返回值类型，禁止！
  return request.get('/api/users');
}
```

### 强制规则
1. **统一请求封装**：必须使用项目 `@/http/axios.ts` 统一封装，禁止裸用 `axios`
2. **类型定义**：API函数必须声明参数和返回值类型
3. **错误处理**：统一在响应拦截器中处理401等通用错误
4. **请求前缀**：API路径必须按后端规范使用 `/backend/system/{module}/...`

## 8. 注释规范（违反即拒绝）

```typescript
// ✅ 正确：文件头注释
/**
 * @file UserService 用户服务
 * @description 处理用户相关的数据获取、状态管理和缓存
 */

// ✅ 正确：函数注释
/**
 * 获取用户详细信息
 * @param userId - 用户唯一标识
 * @returns 用户详细信息
 */

// ✅ 正确：行内注释解释"为什么"
// 因为后端 API 返回的时间戳是秒级，需要转为毫秒
const timestamp = serverTime * 1000;

// ❌ 禁止：重复代码含义的注释
// 设置用户名为张三
const userName = '张三';  // 禁止！
```

**注释语言：统一使用中文**

## 9. 测试规范

| 指标 | 要求 |
|------|------|
| 行覆盖率 | ≥ 80%（核心业务逻辑 ≥ 90%） |
| 分支覆盖率 | ≥ 70% |
| 函数覆盖率 | ≥ 80% |
| 展示组件 | 必须测试 |
| 工具函数 | 必须测试（边界条件、异常输入） |

### 测试命名规范
```typescript
describe('模块/组件名', () => {
  it('应[期望行为]当[条件]', () => {});
});
```

## 10. 完整页面组件代码模板

```vue
<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-form :model="queryForm" inline>
      <el-form-item label="名称">
        <el-input v-model="queryForm.name" placeholder="请输入名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <div class="table-toolbar">
      <el-button type="primary" @click="handleCreate">新增</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" v-loading="loading" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-popconfirm title="确认删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button link type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.current"
      v-model:page-size="pagination.size"
      :total="pagination.total"
      @current-change="fetchData"
      @size-change="fetchData"
    />
  </div>
</template>

<script setup lang="ts">
/**
 * @file Xxx管理页面
 * @description Xxx的增删改查功能
 */

import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { FormInstance } from 'element-plus';

// ========== 类型定义 ==========
interface XxxItem {
  id: number;
  name: string;
}

// ========== State ==========
const loading = ref(false);
const tableData = ref<XxxItem[]>([]);
const pagination = reactive({ current: 1, size: 10, total: 0 });
const queryForm = reactive({ name: '' });

// ========== Methods ==========
async function fetchData() {
  loading.value = true;
  try {
    // 调用API获取数据
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  pagination.current = 1;
  fetchData();
}

function handleReset() {
  Object.assign(queryForm, { name: '' });
  handleSearch();
}

function handleCreate() {
  // 打开新增对话框
}

function handleEdit(row: XxxItem) {
  // 打开编辑对话框
}

async function handleDelete(id: number) {
  // 调用删除API
  ElMessage.success('删除成功');
  fetchData();
}

// ========== Lifecycle ==========
onMounted(() => {
  fetchData();
});
</script>

<style scoped lang="scss">
.page-container {
  padding: 16px;
}

.table-toolbar {
  margin-bottom: 12px;
}
</style>
```
