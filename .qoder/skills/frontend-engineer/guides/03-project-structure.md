# 前端项目结构与组件设计规范

- 版本号: v1.1
- 适用范围: 所有前端项目
- 约束力: **强制**

---

## 1. 文件组织结构

> **适用范围说明**：以下目录结构规范仅在**未采用第三方代码模板/脚手架**（如 V3 Admin Vite、Vue-Vben-Admin 等现成解决方案）的情况下适用。若项目基于现成的第三方模板初始化，应遵循该模板自身的目录结构规范，同时确保组件开发、编码风格、TypeScript 使用等仍符合本文档的其他章节要求。

### 1.1 目录结构规范
```
src/
├── assets/           # 静态资源（图片、字体、图标）
│   ├── images/
│   ├── fonts/
│   └── icons/
├── components/       # 公共组件
│   ├── common/       # 通用基础组件（Button, Input, Modal 等）
│   ├── layout/       # 布局组件（Header, Sidebar, Footer 等）
│   └── business/     # 业务组件（业务强相关）
├── composables/      # Vue3 Composition 函数
│   └── useXXX.ts
├── views/            # 页面级组件（路由对应）
│   ├── home/
│   │   ├── index.vue
│   │   ├── components/     # 页面私有组件
│   │   └── styles/
│   └── user/
│       └── ...
├── router/           # 路由配置
│   ├── index.ts
│   └── routes/
├── stores/           # 状态管理
│   ├── index.ts
│   └── modules/
├── api/              # API 接口层
│   ├── index.ts
│   ├── request.ts    # 请求拦截器
│   └── modules/      # 按业务模块组织
├── utils/            # 工具函数
│   ├── index.ts
│   ├── format.ts
│   └── validate.ts
├── types/            # 全局类型定义
│   ├── global.d.ts
│   └── api.types.ts
├── styles/           # 全局样式
│   ├── index.scss
│   ├── variables.scss   # SCSS 变量
│   ├── mixins.scss      # SCSS Mixins
│   └── normalize.scss   # 样式重置
├── plugins/          # 插件配置
└── App.vue
```

### 1.2 组件组织原则
- **就近原则**: 组件相关的样式、子组件、类型定义放在同一目录下
- **扁平优先**: 组件层级不超过 3 层，过深时应考虑重构
- **单一职责**: 一个文件只包含一个组件（大型页面可例外）

---

## 2. 组件设计原则

### 2.0 开源组件库优先原则（强制）

在开始任何组件开发前，必须执行以下检查：

1. **调研开源方案**：在 Element Plus、Ant Design Vue、Vuetify、Naive UI 等主流 Vue 3 组件库中查找是否有满足需求的现成组件
2. **评估适用性**：确认开源组件的功能、样式、可定制性是否能满足当前需求
3. **决策记录**：
   - 若存在满足需求的开源组件 → **必须使用**，禁止自行开发
   - 若开源组件功能接近但不完全满足 → **优先扩展/二次封装**开源组件
   - 仅在**经过充分论证**确认没有任何开源方案能满足需求时，才允许自定义开发
4. **造轮子审批**：自定义组件必须在文档中记录无法使用开源方案的理由

**严禁在存在成熟开源方案的情况下自行开发基础组件**（如 Button、Table、Form、Modal、DatePicker 等）。

### 2.1 组件分类
| 类型 | 职责 | 特点 | 示例 |
|------|------|------|------|
| 展示组件 (Presentational) | 仅负责 UI 展示 | 无业务逻辑，接收 props | Button, Card, Badge |
| 容器组件 (Container) | 负责数据获取和业务逻辑 | 组合展示组件，处理状态 | UserList, OrderTable |
| 布局组件 (Layout) | 负责页面布局 | 决定内容区域结构 | Sidebar, Header |
| 高阶组件 (HOC) | 为组件提供额外能力 | 复用逻辑 | withAuth, withLoading |

### 2.2 Props 设计规范
```typescript
// Vue 3 示例
interface ButtonProps {
  /** 按钮类型 */
  type?: 'primary' | 'secondary' | 'danger' | 'default';
  /** 按钮尺寸 */
  size?: 'small' | 'medium' | 'large';
  /** 是否禁用 */
  disabled?: boolean;
  /** 点击事件回调 */
  onClick?: () => void;
}
```
- **必须声明类型**: 所有 props 必须有明确的 TypeScript 类型
- **提供默认值**: 可选 props 必须提供合理的默认值
- **单向数据流**: 禁止在子组件中直接修改 props
- **事件命名**: 使用 `on` + 动词/事件名的格式（`onClick`, `onSubmit`）
- **文档注释**: 每个 prop 必须添加 JSDoc 注释说明用途

### 2.3 组件文件结构
```vue
<!-- Vue 3 单文件组件示例 -->
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
/**
 * @file UserProfile 组件
 * @description 用户资料展示组件，用于显示用户基本信息
 */

import { ref, computed, onMounted } from 'vue';
import type { UserInfo } from './types';

// ========== Props & Emits ==========
interface Props {
  /** 用户ID */
  userId: string;
  /** 是否可编辑 */
  editable?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  editable: false,
});

const emit = defineEmits<{
  (e: 'update', user: UserInfo): void;
  (e: 'delete', id: string): void;
}>();

// ========== State ==========
const loading = ref(false);
const userInfo = ref<UserInfo | null>(null);
const errorMessage = ref('');

// ========== Computed ==========
const displayName = computed(() => {
  return userInfo.value?.nickname || userInfo.value?.username || '未知用户';
});

// ========== Methods ==========
async function fetchUserInfo() {
  loading.value = true;
  try {
    // ...
  } finally {
    loading.value = false;
  }
}

function handleUpdate() {
  if (userInfo.value) {
    emit('update', userInfo.value);
  }
}

// ========== Lifecycle ==========
onMounted(() => {
  fetchUserInfo();
});
</script>

<style scoped lang="scss">
.user-profile {
  /* 样式 */
}
</style>
```

### 2.4 组件通信规范
- **Props Down**: 父组件通过 props 向子组件传递数据
- **Events Up**: 子组件通过 `$emit` / `onChange` 向父组件通知事件
- **Provide/Inject**: 仅用于跨多层组件共享（如主题、国际化）
- **状态管理**: 跨组件/跨页面的状态必须使用 Pinia（Vue 3 推荐）或 Vuex
- **避免滥用 ref**: 尽量使用 props + emit，只有在必要（如第三方库封装）时才使用 `ref`/`useImperativeHandle`

---

> **交叉引用**：
> - 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)
> - 代码风格与注释详见 [02-code-style.md](./02-code-style.md)
> - TypeScript 编码标准详见 [04-typescript.md](./04-typescript.md)
> - 样式管理详见 [05-testing-and-perf.md](./05-testing-and-perf.md)
