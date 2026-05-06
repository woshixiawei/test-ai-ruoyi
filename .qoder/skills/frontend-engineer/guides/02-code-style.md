# 前端代码风格与注释规范

- 版本号: v1.1
- 适用范围: 所有前端项目
- 约束力: **强制**

---

## 1. 代码风格约定

### 1.1 缩进与格式化
- **缩进**: 使用 **Tab 字符** 进行缩进，禁止使用空格
- **换行**: 使用 Unix 风格换行符 (`\n`)
- **文件末尾**: 文件末尾必须保留一个空行
- **最大行宽**: 单行代码不超过 **120个字符**，超出应换行
- **分号**: JavaScript/TypeScript 中**必须**使用分号结束语句
- **引号**: 字符串统一使用 **单引号** (`'`)，HTML 属性使用双引号 (`"`)
- **尾随逗号**: 多行对象和数组的最后一个元素后添加尾随逗号（便于 diff）

### 1.2 命名规范

#### 文件命名
| 类型 | 命名规范 | 示例 |
|------|---------|------|
| 组件文件 | PascalCase | `UserProfile.vue`, `NavBar.tsx` |
| 工具函数文件 | camelCase | `dateUtils.ts`, `formatCurrency.ts` |
| 常量/配置文件 | UPPER_SNAKE_CASE | `API_CONFIG.ts`, `ROUTES.ts` |
| 样式文件 | 与组件同名 + `.module` | `UserProfile.module.css` |
| 类型定义文件 | kebab-case + `.types` | `user.types.ts` |
| 目录名 | kebab-case | `user-profile/`, `api-services/` |

#### 变量命名
| 类型 | 命名规范 | 示例 |
|------|---------|------|
| 常量 | UPPER_SNAKE_CASE | `const MAX_RETRY_COUNT = 3;` |
| 普通变量/函数 | camelCase | `const userName = '';`, `function getUserInfo() {}` |
| 类/组件/接口 | PascalCase | `class UserService {}`, `interface UserProps {}` |
| 布尔值变量 | 使用 `is`/`has`/`can` 前缀 | `isLoading`, `hasPermission`, `canEdit` |
| 枚举 | PascalCase | `enum OrderStatus { Pending = 'pending' }` |
| 私有属性 | 下划线前缀（仅类中） | `_internalCache` |

#### CSS 类名命名
- 使用 **BEM (Block Element Modifier)** 命名规范
```css
/* Block */
.user-card { }

/* Element */
.user-card__avatar { }
.user-card__name { }

/* Modifier */
.user-card--active { }
.user-card__name--highlight { }
```
- 在 CSS Module 中使用 camelCase：`.userCardAvatar`

---

## 2. 注释与文档规范

### 2.1 文件头注释
```typescript
/**
 * @file UserService 用户服务
 * @description 处理用户相关的数据获取、状态管理和缓存
 * @author 后端工程师
 * @date 2026-05-04
 */
```

### 2.2 函数/方法注释
```typescript
/**
 * 获取用户详细信息
 * @param userId - 用户唯一标识
 * @param options - 可选配置
 * @returns 用户详细信息，未找到返回 null
 * @throws BusinessError 当用户ID格式无效时
 * @example
 * const user = await getUserDetail('12345');
 * console.log(user?.name);
 */
async function getUserDetail(
  userId: string,
  options?: { includeOrders?: boolean },
): Promise<UserDetail | null> {
  // ...
}
```

### 2.3 行内注释
```typescript
// 正确：解释"为什么"而非"做什么"
// 因为后端 API 返回的时间戳是秒级，需要乘以 1000 转为毫秒
const timestamp = serverTime * 1000;

// 错误：重复代码本身已说明的内容
// 设置用户名为张三
const userName = '张三';
```

---

## 3. ESLint / Prettier 配置建议

### 3.1 推荐配置
```javascript
// .eslintrc.cjs
module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: [
    'eslint:recommended',
    '@typescript-eslint/recommended',
    'plugin:vue/vue3-recommended',
    'prettier',
  ],
  parser: 'vue-eslint-parser',
  parserOptions: {
    parser: '@typescript-eslint/parser',
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  rules: {
    // 强制规则
    '@typescript-eslint/no-explicit-any': 'error',
    '@typescript-eslint/explicit-function-return-type': 'warn',
    'vue/multi-word-component-names': 'off',
    'no-console': ['warn', { allow: ['error', 'warn'] }],
    'prefer-const': 'error',
    'no-var': 'error',
    // 禁止 JavaScript 文件
    'import/no-restricted-paths': ['error', { zones: [{ target: './src', from: './src/**/*.js' }] }],
  },
};
```

```javascript
// .prettierrc
{
  "semi": true,
  "singleQuote": true,
  "useTabs": true,
  "trailingComma": "all",
  "printWidth": 120,
  "endOfLine": "lf",
  "arrowParens": "always"
}
```

---

> **交叉引用**：
> - TypeScript 编码标准详见 [04-typescript.md](./04-typescript.md)
> - 组件设计原则详见 [03-project-structure.md](./03-project-structure.md)
