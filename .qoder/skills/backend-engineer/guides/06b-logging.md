# 后端日志管理规范

- 版本号: v1.0（从 06-exception-and-logging.md v2.1 拆分）
- 适用范围: 所有后端模块
- 约束力: **强制**

---

> 异常处理规范详见 [06a-exception.md](./06a-exception.md)
> 异常与日志总览详见 [06-exception-and-logging.md](./06-exception-and-logging.md)

---

## 1. 日志级别使用

| 级别 | 使用场景 |
|------|---------|
| `ERROR` | 系统异常、业务异常（需人工介入）、外部服务调用失败 |
| `WARN` | 业务异常（用户可理解）、潜在风险、非预期但可恢复的情况 |
| `INFO` | 业务流程关键节点、外部调用入口/出口、定时任务开始/结束 |
| `DEBUG` | 详细调试信息、SQL 语句、参数打印（仅开发环境） |
| `TRACE` | 最详细的跟踪信息（极少使用） |

---

## 2. 日志格式

```java
// Controller 层：请求入口/出口
log.info("[UserController] createUser request: {}", dto);
log.info("[UserController] createUser response: userId={}", userId);

// AppService 层：业务用例开始/结束
log.info("[UserAppService] 开始创建用户, email={}", dto.getEmail());
log.info("[UserAppService] 用户创建成功, userId={}", userId);

// DomainService 层：核心业务逻辑
log.info("[OrderDomainService] 提交订单, userId={}, itemCount={}", userId, items.size());

// 外部调用：入口/出口/异常
log.info("[PayServiceClient] 创建支付订单, orderNo={}, amount={}", orderNo, amount);
log.error("[PayServiceClient] 创建支付订单失败, orderNo={}", orderNo, e);
```

---

## 3. 敏感信息处理

```java
// 错误示例：日志中直接打印密码
log.info("用户登录, password={}", password); // ❌ 严禁

// 正确示例：敏感字段脱敏（使用 Hutool）
import cn.hutool.core.util.DesensitizedUtil;
log.info("用户登录, password={}", DesensitizedUtil.password(password)); // ✅ 输出: ********
log.info("用户登录, phone={}", DesensitizedUtil.mobilePhone(phone));     // ✅ 输出: 138****8888
log.info("用户登录, idCard={}", DesensitizedUtil.idCardNum(idCard));     // ✅ 输出: 110101********1234

// 正确示例：不打印敏感字段
log.info("用户登录, userName={}", userName); // ✅ 只打印非敏感信息
```

**敏感信息脱敏规则**:
| 字段类型 | 脱敏方式 | 示例 |
|---------|---------|------|
| 密码 | 全部替换为 `********` | `********` |
| 手机号 | 中间4位替换 | `138****8888` |
| 身份证号 | 中间8位替换 | `110101********1234` |
| 银行卡号 | 显示前4后4 | `6222****8888` |
| 邮箱 | 用户名部分脱敏 | `z***@example.com` |
| Token / 密钥 | 全部替换 | `********` |

---

> **交叉引用**：
> - 异常处理规范详见 [06a-exception.md](./06a-exception.md)
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 安全规范详见 [08-security-and-misc.md](./08-security-and-misc.md)
