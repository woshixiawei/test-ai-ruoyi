# 前端 TypeScript 编码标准与 API 调用规范

- 版本号: v1.1
- 适用范围: 所有前端项目
- 约束力: **强制**

---

> **重要：本规范完全基于 TypeScript，JavaScript 已被全面禁用。**
> 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)

---

## 1. TypeScript 强制要求
- **所有源文件必须使用 TypeScript**（`.ts` / `.vue` 且必须包含 `lang="ts"`）
- **严禁创建 `.js` 文件**（第三方库和构建配置文件除外）
- **禁止使用 `any`**，必须使用具体类型或使用 `unknown` + 类型守卫
- **接口优先于类型别名**: 定义对象结构时使用 `interface`，联合类型使用 `type`
- **显式返回值**: 函数必须声明返回值类型（除非可由 TS 完全推断）
- **严格模式**: 项目 `tsconfig.json` 必须开启 `strict: true`

## 2. 代码规范
```typescript
// ========== 变量声明 ==========
// 优先使用 const，需要重新赋值才用 let
const API_BASE_URL = 'https://api.example.com';
let currentUser: User | null = null;

// 禁止使用 var
// ❌ var count = 0;

// ========== 函数编写 ==========
// 优先使用箭头函数和函数表达式
const calculateTotal = (items: CartItem[]): number => {
  return items.reduce((sum, item) => sum + item.price * item.quantity, 0);
};

// 单参数可省略括号
const formatDate = (date: Date): string => {
  return date.toISOString().split('T')[0];
};

// 简单函数可单行
const double = (n: number): number => n * 2;

// ========== 对象与数组 ==========
// 对象属性简写
const userName = '张三';
const user = { userName, age: 25 }; // ✅

// 解构赋值
const { name, email } = user;
const [first, second] = list;

// 展开运算符复制（不修改原对象）
const newUser = { ...user, age: 26 };
const newList = [...list, newItem];

// ========== 空值处理 ==========
// 使用可选链和空值合并
const city = user?.address?.city ?? '未知城市';

// 避免深层嵌套 if，使用提前返回
function processOrder(order: Order | null): Result {
  if (!order) {
    return { success: false, error: '订单不存在' };
  }
  
  if (order.status !== 'pending') {
    return { success: false, error: '订单状态不正确' };
  }
  
  // 处理逻辑
  return { success: true };
}

// ========== 异步编程 ==========
// 优先使用 async/await，避免 Promise 链
async function fetchUserData(userId: string): Promise<User> {
  try {
    const response = await fetch(`/api/users/${userId}`);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }
    
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('获取用户数据失败:', error);
    throw error;
  }
}

// 并行请求使用 Promise.all
const [users, orders] = await Promise.all([
  fetchUsers(),
  fetchOrders(),
]);

// ========== 错误处理 ==========
// 自定义错误类
class BusinessError extends Error {
  constructor(
    message: string,
    public code: string,
    public statusCode: number = 400,
  ) {
    super(message);
    this.name = 'BusinessError';
  }
}

// 统一错误处理
function handleError(error: unknown): ErrorResponse {
  if (error instanceof BusinessError) {
    return { code: error.code, message: error.message };
  }
  
  if (error instanceof Error) {
    return { code: 'UNKNOWN_ERROR', message: error.message };
  }
  
  return { code: 'UNKNOWN_ERROR', message: '发生未知错误' };
}
```

---

## 3. API 调用规范
```typescript
// api/request.ts - 统一请求封装
import axios, { AxiosInstance, AxiosRequestConfig, AxiosError } from 'axios';

const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// 响应拦截器
request.interceptors.response.use(
  (response) => response.data,
  (error: AxiosError<ErrorResponse>) => {
    if (error.response?.status === 401) {
      // 统一处理登录失效
      window.location.href = '/login';
      return Promise.reject(new BusinessError('登录已过期', 'UNAUTHORIZED', 401));
    }
    
    const message = error.response?.data?.message || '网络请求失败';
    return Promise.reject(new BusinessError(message, 'REQUEST_ERROR'));
  },
);

export default request;
```

---

> **交叉引用**：
> - 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)
> - 代码风格与命名规范详见 [02-code-style.md](./02-code-style.md)
> - 组件设计原则详见 [03-project-structure.md](./03-project-structure.md)
