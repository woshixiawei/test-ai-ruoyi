# 适配层（Adapter Layer）规范

- 版本号: v1.0（从 03-four-layer-architecture.md v4.0 拆分）
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> 应用层规范详见 [03b-application-layer.md](./03b-application-layer.md)
> 领域层规范详见 [03c-domain-layer.md](./03c-domain-layer.md)
> 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)

---

## 1. 适配层职责边界

> **模块归属**：适配层位于 **server 模块**，但 Controller 的入参类（Cmd）和出参类（DTO）定义在 **sdk 模块**。

**职责边界**:
- 接收外部请求（HTTP / RPC / MQ / Cron）
- 参数校验（@Valid / @Validated）
- 接收 Cmd（来自 sdk 模块），通过 Convertor 转为 BO 后传给 AppService
- 从 AppService 获取 BO，通过 Convertor 转为 DTO 后返回
- 调用应用层，不直接处理业务逻辑
- 统一响应封装（ApiResponse，来自 base 模块）

---

## 2. Controller 规范

```java
package com.example.project.server.adapter.controller.backend;

import com.example.project.base.basecls.BaseController;       // base 模块
import com.example.project.base.dto.ApiResponse;                 // base 模块
import com.example.project.base.dto.PageDTO;                     // base 模块
import com.example.project.sdk.cmd.user.UserCreateCmd;            // sdk 模块
import com.example.project.sdk.cmd.user.UserUpdateCmd;            // sdk 模块
import com.example.project.sdk.dto.user.UserDTO;                 // sdk 模块
import com.example.project.server.application.service.UserAppService; // server 模块
import com.example.project.server.application.bo.user.UserBO;      // server 模块
import com.example.project.server.application.bo.user.UserCreateBO; // server 模块
import com.example.project.server.infrastructure.convertor.UserConvertor; // server 模块

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/backend/user")
@Tag(name = "后端接口-用户接口", description = "提供后端用户管理接口")
public class UserController extends BaseController {

	private final UserAppService userAppService;
	private final UserConvertor userConvertor;

	/**
	 * 创建用户
	 */
	@PostMapping
	@Operation(summary = "创建用户")
	public ApiResponse<Long> create(@RequestBody @Valid UserCreateCmd cmd) {
		Long id = userAppService.createUser(userConvertor.toCreateBO(cmd));
		return succ(id);
	}

	/**
	 * 更新用户
	 */
	@PutMapping("/{id}")
	@Operation(summary = "更新用户")
	public ApiResponse<Void> update(@PathVariable Long id, @RequestBody @Valid UserUpdateCmd cmd) {
		userAppService.updateUser(id, userConvertor.toCreateBOFromUpdateCmd(cmd));
		return succ();
	}

	/**
	 * 根据ID查询用户详情
	 */
	@GetMapping("/{id}")
	@Operation(summary = "查询用户详情")
	public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
		return succ(userConvertor.toDTOFromBO(userAppService.getUserById(id)));
	}

	/**
	 * 分页查询用户列表
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询用户列表")
	public ApiResponse<PageDTO<UserDTO>> page(
			@RequestParam(defaultValue = "1") long current,
			@RequestParam(defaultValue = "10") long size,
			@RequestParam(required = false) String keyword) {
		PageBO<UserBO> boPage = userAppService.pageUsers(current, size, keyword);
		PageDTO<UserDTO> result = new PageDTO<>();
		result.setCurrent(boPage.getCurrent());
		result.setSize(boPage.getSize());
		result.setTotal(boPage.getTotal());
		result.setRecords(userConvertor.toDTOFromBOList(boPage.getRecords()));
		return succ(result);
	}
}
```

**Controller 强制规范**:
1. **继承 BaseController**：所有 Controller 必须继承 base 模块的 `BaseController`，使用统一的 `succ()` / `succ(data)` 方法返回
2. **只做四件事**：接收请求 → 参数校验 → 调用 AppService → 返回 ApiResponse
3. **禁止在 Controller 中写业务逻辑**：if/else 业务判断、数据计算、数据库操作一律不允许
4. **【最严格】禁止跨层调用**：Controller **只能**调用 AppService 和 Convertor，**严禁**直接注入或调用 DomainService、Repository、Mapper
5. **统一返回 ApiResponse**：所有接口必须返回 `ApiResponse<T>`（来自 base 模块）
6. **入参来自 sdk 模块**：Cmd 类定义在 sdk 模块，Controller 通过 import 引用
7. **出参来自 sdk 模块**：DTO 类定义在 sdk 模块，Controller 通过 import 引用
8. **入参转换 Cmd→BO**：通过 Convertor 将 Cmd 转为 BO 后传给 AppService，**禁止**将 Cmd 直接传给 AppService
9. **出参转换 BO→DTO**：AppService 返回 BO，Controller 通过 Convertor 转为 DTO 后返回
10. **路径规范**：后台接口 `/backend/` 前缀，前台接口 `/front/` 前缀，RESTful 风格

---

## 3. XXL-JOB 分布式定时任务规范（强制）

> **定时任务框架选型**：项目使用 **XXL-JOB** 作为分布式定时任务调度平台，**严禁使用 Spring `@Scheduled`** 进行生产环境定时任务调度。

**为什么禁用 @Scheduled**：
- `@Scheduled` 不支持集群分布式调度，多实例会重复执行
- 无法动态调整 cron 表达式，修改需重新部署
- 缺少执行日志、告警、重试等运维能力

**XXL-JOB 配置**（`application.yml`）:
```yaml
xxl:
  job:
    admin:
      addresses: http://xxl-job-admin:8080/xxl-job-admin
    accessToken: default_token
    executor:
      appname: ${spring.application.name}
      port: 9999
      logretentiondays: 30
```

**任务定义规范**:
```java
package com.example.project.server.adapter.scheduler;

import com.example.project.server.application.service.OrderAppService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单相关定时任务
 *
 * <p>所有定时任务必须放在 adapter.scheduler 包下，遵循适配层职责边界。</p>
 * <p>定时任务只能调用 AppService，禁止直接调用 DomainService 或 Mapper。</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderJobHandler {

	private final OrderAppService orderAppService;

	/**
	 * 取消超时未支付的订单
	 *
	 * <p>调度策略：每5分钟执行一次，由 XXL-JOB 控制台配置 cron。</p>
	 */
	@XxlJob("cancelTimeoutOrderJob")
	public void cancelTimeoutOrderJob() {
		log.info("[OrderJobHandler] 开始执行订单超时取消任务");
		try {
			int count = orderAppService.cancelTimeoutOrders();
			log.info("[OrderJobHandler] 成功取消 {} 个超时订单", count);
		} catch (Exception e) {
			log.error("[OrderJobHandler] 订单超时取消任务执行失败", e);
			throw e; // 抛出异常，XXL-JOB 会标记为执行失败并触发告警
		}
	}

	/**
	 * 自动确认收货（发货超过7天）
	 */
	@XxlJob("autoConfirmReceiveJob")
	public void autoConfirmReceiveJob() {
		log.info("[OrderJobHandler] 开始执行自动确认收货任务");
		try {
			int count = orderAppService.autoConfirmReceive();
			log.info("[OrderJobHandler] 自动确认收货 {} 笔订单", count);
		} catch (Exception e) {
			log.error("[OrderJobHandler] 自动确认收货任务执行失败", e);
			throw e;
		}
	}
}
```

**XXL-JOB 强制规范**:
1. **任务类放在 `adapter.scheduler` 包**：属于适配层，只做调度触发
2. **JobHandler 方法名即任务标识**：`@XxlJob("jobHandlerName")` 的值必须与 XXL-JOB 控制台配置一致
3. **只调用 AppService**：定时任务禁止直接操作 Mapper 或 DomainService
4. **异常必须抛出**：让 XXL-JOB 标记执行失败并触发告警，禁止吞掉异常
5. **日志必须打印**：任务开始、结束、处理数量必须记录
6. **cron 由控制台管理**：不在代码中硬编码 cron 表达式，通过 XXL-JOB 控制台动态配置
7. **禁止使用 `@Scheduled`**：生产环境统一使用 XXL-JOB

---

## 4. MQ Consumer 规范

```java
/**
 * 订单支付成功消息消费者
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPaySuccessConsumer {

	private final OrderAppService orderAppService;

	@KafkaListener(topics = "order.pay.success", groupId = "order-service")
	public void onMessage(@Payload OrderPaySuccessMessage message) {
		log.info("[OrderPaySuccessConsumer] 接收到订单支付成功消息, orderId={}", message.getOrderId());
		try {
			orderAppService.handlePaySuccess(message);
		} catch (Exception e) {
			log.error("[OrderPaySuccessConsumer] 处理订单支付成功消息失败, orderId={}", message.getOrderId(), e);
			// 根据业务决定：重试 / 死信队列 / 告警
			throw e; // 抛出异常触发重试或进入死信队列
		}
	}
}
```

---

> **交叉引用**：
> - 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 应用层规范详见 [03b-application-layer.md](./03b-application-layer.md)
> - 领域层规范详见 [03c-domain-layer.md](./03c-domain-layer.md)
> - 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
