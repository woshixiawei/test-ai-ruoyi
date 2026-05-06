# 前端样式、测试、性能与 Git 规范

- 版本号: v1.1
- 适用范围: 所有前端项目
- 约束力: **强制**

---

## 1. 样式管理规范

### 1.1 样式方案选择优先级
1. **CSS Module**（推荐用于大型项目）
2. **Scoped CSS**（Vue 项目默认推荐）
3. **Tailwind CSS**（需要原子化 CSS 时）
4. **CSS-in-JS**（React 项目可选）

### 1.2 通用样式规范
- **单位规范**: 
  - 字体大小: 使用 `rem`（基准 16px）
  - 布局尺寸: 使用 `px` 或 `%`
  - 响应式断点: 使用 `px`
- **颜色管理**: 所有颜色必须使用 SCSS/CSS 变量，禁止硬编码
```scss
// variables.scss
:root {
  --color-primary: #1890ff;
  --color-primary-hover: #40a9ff;
  --color-text-primary: #262626;
  --color-text-secondary: #595959;
  --color-border: #d9d9d9;
  --color-bg-base: #f5f5f5;
  --color-success: #52c41a;
  --color-warning: #faad14;
  --color-error: #f5222d;
}
```
- **z-index 管理**: 使用变量统一管理，禁止随意写 `99999`
```scss
$z-index-dropdown: 100;
$z-index-modal: 500;
$z-index-popover: 600;
$z-index-tooltip: 700;
$z-index-toast: 800;
```

### 1.3 样式编写规范
- **选择器深度**: 最多嵌套 3 层，超过应重构
- **避免 `!important`**: 仅在覆盖第三方库样式时使用，且必须注释原因
- **响应式设计**: 使用移动优先（Mobile First）策略
```scss
// 正确：移动优先
.card {
  width: 100%;
  
  @media (min-width: 768px) {
    width: 50%;
  }
}
```
- **动画规范**: 过渡动画时长统一使用 `200ms` / `300ms`，使用 `ease-in-out`

---

## 2. 测试规范

### 2.1 测试文件组织
```
src/
├── components/
│   └── Button/
│       ├── Button.vue
│       └── Button.spec.ts    # 同目录测试文件
├── utils/
│   ├── format.ts
│   └── __tests__/            # 或单独测试目录
│       └── format.spec.ts
```

### 2.2 测试编写规范
```typescript
// Button.spec.ts
import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import Button from './Button.vue';

describe('Button 组件', () => {
  // ========== 渲染测试 ==========
  it('应正确渲染默认按钮', () => {
    const wrapper = mount(Button, {
      props: { label: '点击我' },
    });
    
    expect(wrapper.text()).toBe('点击我');
    expect(wrapper.classes()).toContain('btn--default');
  });
  
  // ========== Props 测试 ==========
  it('应根据 type prop 应用不同样式', () => {
    const wrapper = mount(Button, {
      props: { label: '提交', type: 'primary' },
    });
    
    expect(wrapper.classes()).toContain('btn--primary');
  });
  
  // ========== 事件测试 ==========
  it('点击时应触发 click 事件', async () => {
    const onClick = vi.fn();
    const wrapper = mount(Button, {
      props: { label: '点击', onClick },
    });
    
    await wrapper.find('button').trigger('click');
    expect(onClick).toHaveBeenCalledTimes(1);
  });
  
  // ========== 边界条件测试 ==========
  it('禁用状态下不应触发点击事件', async () => {
    const onClick = vi.fn();
    const wrapper = mount(Button, {
      props: { label: '禁用', disabled: true, onClick },
    });
    
    await wrapper.find('button').trigger('click');
    expect(onClick).not.toHaveBeenCalled();
  });
  
  it('空 label 时应渲染为空或显示占位符', () => {
    const wrapper = mount(Button, {
      props: { label: '' },
    });
    
    expect(wrapper.text()).toBe('');
  });
  
  // ========== 快照测试（可选） ==========
  it('应匹配快照', () => {
    const wrapper = mount(Button, {
      props: { label: '快照测试' },
    });
    
    expect(wrapper.html()).toMatchSnapshot();
  });
});
```

### 2.3 测试要求
| 类型 | 覆盖率要求 | 说明 |
|------|-----------|------|
| 行覆盖率 | ≥ 80% | 核心业务逻辑 ≥ 90% |
| 分支覆盖率 | ≥ 70% | |
| 函数覆盖率 | ≥ 80% | |
| 展示组件 | 必须测试 | 渲染、props、事件 |
| 工具函数 | 必须测试 | 边界条件、异常输入 |
| 容器组件 | 建议测试 | 数据获取、状态变化 |
| API 层 | 建议 Mock 测试 | 响应处理、错误处理 |

### 2.4 测试命名规范
```typescript
// 测试套件: describe('模块/组件名', () => {})
describe('用户服务 (UserService)', () => {
  // 测试用例: it('应[期望行为]当[条件]', () => {})
  it('应返回用户信息当用户ID有效时', () => {});
  it('应抛出错误当用户ID为空字符串时', () => {});
  it('应返回null当用户不存在时', () => {});
  it('应缓存结果当同一用户被多次查询时', () => {});
});
```

---

## 3. 性能优化规范

### 3.1 代码层面
- **避免不必要的重渲染**: 使用 `computed`/`watch` 优化计算属性和侦听
- **列表渲染使用 key**: 必须使用稳定且唯一的 `key`
- **懒加载**: 路由和大型组件使用 `defineAsyncComponent`
- **图片优化**: 使用 WebP 格式，提供 `srcset`，懒加载非首屏图片

### 3.2 网络层面
- **接口防抖**: 搜索等输入场景使用防抖（debounce）
- **接口节流**: 按钮点击等场景使用节流（throttle）
- **缓存策略**: 合理使用 HTTP 缓存和本地缓存

### 3.3 构建层面
- **代码分割**: 按路由和模块分割 chunk
- **Tree Shaking**: 确保引入方式支持 Tree Shaking
- **Gzip/Brotli**: 生产环境开启压缩

---

## 4. Git 提交规范

### 4.1 分支命名
| 类型 | 命名格式 | 示例 |
|------|---------|------|
| 功能开发 | `feature/[功能名]` | `feature/user-profile` |
| 缺陷修复 | `fix/[bug描述]` | `fix/login-error` |
| 热修复 | `hotfix/[问题描述]` | `hotfix/security-patch` |
| 技术债务 | `refactor/[重构内容]` | `refactor/api-layer` |

### 4.2 提交信息格式
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 说明**:
| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复 bug |
| `docs` | 文档更新 |
| `style` | 代码格式调整（不影响功能） |
| `refactor` | 重构 |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `chore` | 构建/工具相关 |

**示例**:
```
feat(user): 添加用户资料编辑功能

- 实现头像上传和裁剪
- 添加表单验证
- 集成用户更新 API

Closes #123
```

---

> **交叉引用**：
> - 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)
> - 代码风格与注释详见 [02-code-style.md](./02-code-style.md)
> - 组件设计原则详见 [03-project-structure.md](./03-project-structure.md)
> - TypeScript 编码标准详见 [04-typescript.md](./04-typescript.md)
