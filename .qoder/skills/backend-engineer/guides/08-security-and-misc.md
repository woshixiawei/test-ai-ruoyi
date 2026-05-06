# 后端安全规范与事务/分页/并发

- 版本号: v2.0
- 适用范围: 所有后端模块
- 约束力: **强制**

---

> 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)
> 异常体系与统一响应详见 [06-exception-and-logging.md](./06-exception-and-logging.md)

---

## 1. 安全规范

### 1.1 输入校验
```java
// Controller 层：使用 @Valid 进行校验
@PostMapping
public ApiResponse<UserCreateRespDTO> createUser(@RequestBody @Valid UserCreateReqDTO reqDTO) { }

// ReqDTO 校验注解
@Data
public class UserCreateReqDTO {

	@NotBlank(message = "用户名不能为空")
	@Size(min = 2, max = 20, message = "用户名长度2-20字符")
	private String userName;

	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式不正确")
	private String email;

	@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
	private String phone;

	@Min(value = 1, message = "年龄必须大于0")
	@Max(value = 150, message = "年龄不能超过150")
	private Integer age;
}
```

### 1.2 SQL 注入防护
```java
// 正确：使用 MyBatis-Plus 条件构造器（参数化查询）
Wrappers.<UserPO>lambdaQuery()
		.eq(UserPO::getUserName, userName);  // ✅

// 正确：使用 #{} 占位符
@Select("SELECT * FROM user WHERE user_name = #{userName}")
UserPO selectByName(@Param("userName") String userName);  // ✅

// 错误：使用 ${} 拼接（禁止）
@Select("SELECT * FROM user WHERE user_name = '${userName}'")  // ❌ 严禁
```

### 1.3 XSS 防护
```java
// 方案1：使用 Spring 自动 HTML 转义（配置 Jackson）
// 方案2：DTO 层使用 @SafeHtml（Hibernate Validator）
// 方案3：输出时转义
public static String escapeHtml(String input) {
	if (input == null) return null;
	return input
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#x27;");
}
```

### 1.4 其他安全要求
| 安全项 | 要求 |
|--------|------|
| 接口鉴权 | 所有业务接口必须校验登录状态，敏感操作校验权限 |
| 密码存储 | 必须使用 BCrypt 等强哈希算法，禁止明文存储 |
| 文件上传 | 限制文件类型、大小，存储路径不暴露 |
| 接口防刷 | 关键接口（登录、注册、支付）使用限流（Rate Limiting）|
| 敏感操作 | 修改密码、支付等操作需二次验证（短信/邮箱验证码）|
| HTTPS | 生产环境强制 HTTPS |
| CORS | 严格配置允许的 Origin，禁止 `*` |

---

## 2. 事务管理

```java
// 应用层写操作必须加事务
@Override
@Transactional(rollbackFor = Exception.class)
public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
	// 转账逻辑
}

// 只读操作可加 readOnly 优化性能
@Override
@Transactional(readOnly = true)
public UserDetailBO getUserById(Long userId) {
	// 查询逻辑
}

// 跨服务调用：事务边界在应用层，远程调用放在事务外
@Override
public void createOrderWithPayment(OrderCreateBO bo) {
	// 1. 本地事务：创建订单
	Long orderId = transactionTemplate.execute(status -> {
		return orderAppService.createOrder(bo);
	});

	// 2. 远程调用：创建支付（不在事务内）
	payServiceClient.createOrder(bo.getAmount(), orderId.toString());
}
```

---

## 3. 分页规范

> 分页基础类定义在 **base 模块**，sdk 模块可继承使用。

```java
// base 模块：分页命令基类
@Data
@Schema(description = "分页抽象类")
public abstract class PageCmd {

	@Schema(description = "当前页码")
	private long current = 1;

	@Schema(description = "每页大小")
	private long size = 15;

	/**
	 * 检查分页参数
	 */
	public abstract void checkParams();

	public void reInitParams() {
		if (current < 1) current = 1;
		if (size < 1) size = 15;
		if (size > 100) size = 100;
		checkParams();
	}
}

// base 模块：分页响应 DTO
@Data
@Schema(description = "分页数据DTO")
public class PageDTO<T> {
	private List<T> records;
	private long total;
	private long size;
	private long current;

	public static <T> PageDTO<T> empty(long size, long current) {
		PageDTO<T> dto = new PageDTO<>();
		dto.setRecords(List.of());
		dto.setTotal(0);
		dto.setSize(size);
		dto.setCurrent(current);
		return dto;
	}
}
```

---

## 4. 并发控制

```java
// 乐观锁：使用版本号（MyBatis-Plus）
@Data
public class UserAccountPO {
	@Version
	private Long version;
	private BigDecimal balance;
}

// 分布式锁：Redisson
@RequiredArgsConstructor
public class OrderService {
	private final RedissonClient redissonClient;

	public void processOrder(String orderNo) {
		RLock lock = redissonClient.getLock("order:" + orderNo);
		try {
			boolean locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
			if (!locked) {
				throw new BusinessException("订单处理中，请稍后重试");
			}
			// 处理订单
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new SystemException("获取锁被中断");
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
```

---

> **交叉引用**：
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 异常体系与日志详见 [06-exception-and-logging.md](./06-exception-and-logging.md)
> - 测试规范详见 [07-testing-spec.md](./07-testing-spec.md)
