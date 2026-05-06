# 后端异常处理规范

- 版本号: v1.0（从 06-exception-and-logging.md v2.1 拆分）
- 适用范围: 所有后端模块
- 约束力: **强制**

---

> 日志管理规范详见 [06b-logging.md](./06b-logging.md)
> 异常与日志总览详见 [06-exception-and-logging.md](./06-exception-and-logging.md)

---

## 1. 异常体系（base 模块）

> 异常体系定义在 **base 模块**，所有模块共用。

```
CommException (抽象基类，含 code + isFatal)        ← base 模块
├── BusinessException          # 业务异常（用户可理解，如"库存不足"）    isFatal=false
├── ParamException             # 参数校验异常                            isFatal=false
├── SystemException            # 系统异常（数据库连接失败等）              isFatal=true
├── NotLoginException          # 未登录异常                              isFatal=false
├── NoPermissionException      # 无权限异常                              isFatal=false
└── OpenFeignAccessException   # 内部服务远程调用异常                     isFatal=true
```

**`isFatal()` 含义**：
- `true`：致命异常，需要人工介入，全局异常处理器会调用 `errRecordDomainService.reportException()` 记录到异常表
- `false`：非致命异常，属于正常业务流程（如用户输入错误、库存不足），仅记录日志不告警

**CommException 抽象基类**（base 模块）:
```java
/**
 * 项目公共异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CommException extends RuntimeException {

	/** 异常对应的错误码 */
	private Integer code;

	public CommException(Integer code) {
		this.code = code;
	}

	public CommException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public CommException(String message, Throwable cause, Integer code) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * 判断当前异常是否为致命异常
	 * @return true表示致命异常，false表示可恢复异常
	 */
	public abstract boolean isFatal();
}
```

---

## 2. 全局异常处理器（标准实现）

> 参考 shenji 项目的实际实现，全局异常处理器采用 **单入口 + `instanceof` 分发** 模式。

```java
package com.example.project.server.config.handler;

import com.example.project.base.dto.ApiResponse;
import com.example.project.base.constant.ErrConstant;
import com.example.project.base.exceptions.CommException;
import com.example.project.base.exceptions.NotLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>采用单入口 + instanceof 分发模式，所有异常统一进入 exceptionHandler 方法。</p>
 * <p>通过 CommException 的 isFatal() 判断是否为致命异常，致命异常记录到异常表并告警。</p>
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final ErrRecordDomainService errRecordDomainService;

	@ExceptionHandler(Exception.class)
	public ApiResponse<Void> exceptionHandler(Exception exception) {
		String traceId = MDC.get("traceId");

		// 1. CommException 体系异常
		if (exception instanceof CommException commException) {
			if (commException.isFatal()) {
				log.error("发生致命异常", exception);
				errRecordDomainService.reportException(exception);
			} else {
				log.warn("发生非致命异常", exception);
			}
			return ApiResponse.<Void>builder()
					.code(commException.getCode())
					.msg(commException.getMessage())
					.traceId(traceId)
					.build();
		}

		// 2. Sa-Token 未登录异常
		if (exception instanceof NotLoginException notLoginException) {
			log.warn("未登录异常", exception);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.NOT_LOGIN_ERR_CODE)
					.msg("请先登录")
					.traceId(traceId)
					.build();
		}

		// 3. 参数校验异常（@Valid / @Validated 触发）
		if (exception instanceof MethodArgumentNotValidException validException) {
			String message = validException.getBindingResult().getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage())
					.collect(Collectors.joining("; "));
			log.warn("[ValidationException] {}", message);
			return ApiResponse.<Void>builder()
					.code(ErrConstant.PARAM_ERR_CODE)
					.msg(message)
					.traceId(traceId)
					.build();
		}

		// 4. 兜底：未知异常
		log.error("发生未知异常", exception);
		errRecordDomainService.reportException(exception);
		return ApiResponse.<Void>builder()
				.code(ErrConstant.SYSTEM_ERR_CODE)
				.msg("系统繁忙，请稍后重试")
				.traceId(traceId)
				.build();
	}
}
```

**全局异常处理器强制规范**:
1. **单入口模式**：只定义一个 `@ExceptionHandler(Exception.class)` 方法，通过 `instanceof` 分发处理
2. **必须注入 ErrRecordDomainService**：用于记录致命异常和未知异常到异常表
3. **必须返回 traceId**：每个异常响应必须携带链路追踪 ID
4. **致命异常告警**：`isFatal() == true` 的异常必须调用 `errRecordDomainService.reportException()` 记录
5. **非致命异常仅日志**：`isFatal() == false` 的异常仅 `log.warn`，不记录到异常表
6. **未知异常兜底**：所有未预期的 Exception 必须记录到异常表并返回通用错误消息
7. **禁止泄漏堆栈信息**：面向用户的 msg 不能包含 SQL、堆栈、内部类名等技术细节

---

## 3. 错误码定义（base 模块）

> 错误码常量定义在 **base 模块** 的 `ErrConstant` 类中。

```java
package com.example.project.base.constant;

/**
 * 错误码常量
 */
public class ErrConstant {

	public static final int SUCCESS_CODE = 0;
	public static final int PARAM_ERR_CODE = 50001;
	public static final int BUSINESS_ERR_CODE = 50002;
	public static final int SYSTEM_ERR_CODE = 50004;
	public static final int NOT_LOGIN_ERR_CODE = 50005;
	public static final int NO_PERMISSION_CODE = 50006;
	public static final int OPEN_FEIGN_ACCESS_ERR_CODE = 50007;
}
```

---

## 4. 统一响应封装（base 模块）

> 统一响应类 `ApiResponse` 定义在 **base 模块**，所有模块共用。

```java
package com.example.project.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * 统一 API 响应封装
 */
@Data
@Builder
@Schema(description = "API 请求返回值类")
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

	@Schema(description = "错误码，成功为 0")
	@Builder.Default
	private int code = 0;

	@Schema(description = "错误消息提示")
	@Builder.Default
	private String msg = null;

	@Schema(description = "返回数据")
	@Builder.Default
	private @Nullable T data = null;

	@Schema(description = "链路追踪ID")
	@Builder.Default
	private String traceId = null;

	public static <T> ApiResponse<T> succ(T data) {
		return ApiResponse.<T>builder().code(0).data(data).build();
	}

	public static ApiResponse<Void> succ() {
		return ApiResponse.<Void>builder().code(0).build();
	}

	public static <T> ApiResponse<T> fail(int code, String msg) {
		return ApiResponse.<T>builder().code(code).msg(msg).build();
	}
}
```

---

> **交叉引用**：
> - 日志管理规范详见 [06b-logging.md](./06b-logging.md)
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 安全规范详见 [08-security-and-misc.md](./08-security-and-misc.md)
