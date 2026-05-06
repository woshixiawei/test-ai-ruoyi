# 外部服务调用规范（Hutool HttpUtil + OpenFeign）

- 版本号: v1.0（从 03d-infrastructure-layer.md v1.0 拆分）
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 基础设施层总览详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
> Repository 与数据访问规范详见 [03d1-repository.md](./03d1-repository.md)

---

## 1. 外部第三方服务调用（Hutool HttpUtil）

> **强制规定**：外部第三方服务调用（如支付、短信、OSS 等）**必须使用 Hutool 的 `HttpUtil`**，**严禁使用 `RestTemplate`、`WebClient`、`OkHttp` 或 `HttpClient`**。

**为什么选择 Hutool HttpUtil**：
- 项目已强制引入 Hutool，无需额外依赖
- API 简洁，一行代码完成 HTTP 调用
- 内建连接池、超时控制、重试机制
- 统一团队 HTTP 调用风格

```java
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

/**
 * 支付服务客户端（外部第三方服务）
 *
 * <p>外部服务 Client 放在 infrastructure/client 包下。</p>
 * <p>使用 Hutool HttpRequest 进行 HTTP 调用。</p>
 */
@Component
@Slf4j
public class PayServiceClient {

	private static final String BASE_URL = "https://pay.example.com/api";

	/**
	 * 创建支付订单
	 */
	public PayOrderResponse createOrder(BigDecimal amount, String orderNo) {
		PayOrderRequest request = new PayOrderRequest(amount, orderNo);
		String url = BASE_URL + "/orders";

		log.info("[PayServiceClient] 创建支付订单, orderNo={}, amount={}", orderNo, amount);
		try {
			HttpResponse httpResponse = HttpRequest.post(url)
					.timeout(5000)
					.header("Content-Type", "application/json")
					.body(JSONUtil.toJsonStr(request))
					.execute();

			if (!httpResponse.isOk()) {
				log.error("[PayServiceClient] 创建支付订单失败, httpStatus={}, body={}",
						httpResponse.getStatus(), httpResponse.body());
				throw new SystemException("支付服务调用失败, httpStatus=" + httpResponse.getStatus());
			}

			return JSONUtil.toBean(httpResponse.body(), PayOrderResponse.class);
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			log.error("[PayServiceClient] 创建支付订单异常, orderNo={}", orderNo, e);
			throw new SystemException("支付服务暂时不可用，请稍后重试");
		}
	}

	/**
	 * 查询支付结果（GET 请求示例）
	 */
	public PayOrderResponse queryOrder(String orderNo) {
		String url = BASE_URL + "/orders/" + orderNo;
		try {
			String body = cn.hutool.http.HttpUtil.get(url, 5000);
			return JSONUtil.toBean(body, PayOrderResponse.class);
		} catch (Exception e) {
			log.error("[PayServiceClient] 查询支付结果异常, orderNo={}", orderNo, e);
			throw new SystemException("支付服务暂时不可用，请稍后重试");
		}
	}
}
```

**Hutool HTTP 调用速查**:
| 场景 | 方法 | 示例 |
|------|------|------|
| 简单 GET | `HttpUtil.get(url, timeout)` | `HttpUtil.get("https://api.com/users", 5000)` |
| 简单 POST | `HttpUtil.post(url, body)` | `HttpUtil.post("https://api.com/orders", jsonString)` |
| 复杂请求 | `HttpRequest.post(url).header(...).body(...).execute()` | 见上方完整示例 |
| JSON 响应解析 | `JSONUtil.toBean(json, Class)` | `JSONUtil.toBean(body, UserDTO.class)` |

---

## 2. 内部微服务调用（OpenFeign）

> **强制规定**：微服务架构下，内部服务间远程调用**必须使用 OpenFeign**，**严禁使用 Hutool HttpUtil 调用内部服务**。

**外部服务 vs 内部服务的区分**:
| 类型 | 调用方式 | 典型场景 |
|------|---------|---------|
| 外部第三方服务 | Hutool HttpUtil | 支付网关、短信服务、第三方 OSS |
| 内部微服务 | OpenFeign | 用户服务调用订单服务、权限校验服务 |

**Feign Client 接口定义**（sdk 模块）:
```java
package com.example.project.sdk.api;

import com.example.project.base.dto.ApiResponse;
import com.example.project.sdk.dto.order.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 订单服务远程调用接口
 */
@FeignClient(
		name = "order-service",
		path = "/backend/order",
		fallbackFactory = OrderFeignClientFallback.class
)
public interface OrderFeignClient {

	@GetMapping("/{orderId}")
	ApiResponse<OrderDTO> getById(@PathVariable("orderId") Long orderId);

	@GetMapping("/list")
	ApiResponse<List<OrderDTO>> listByUserId(@RequestParam("userId") Long userId);
}
```

**Feign 降级工厂**（server 模块）:
```java
@Slf4j
@Component
public class OrderFeignClientFallback implements FallbackFactory<OrderFeignClient> {

	@Override
	public OrderFeignClient create(Throwable cause) {
		log.error("[OrderFeignClient] 远程调用失败", cause);

		return new OrderFeignClient() {
			@Override
			public ApiResponse<OrderDTO> getById(Long orderId) {
				return ApiResponse.fail(ErrConstant.OPEN_FEIGN_ACCESS_ERR_CODE,
						"订单服务不可用: " + cause.getMessage());
			}

			@Override
			public ApiResponse<List<OrderDTO>> listByUserId(Long userId) {
				return ApiResponse.fail(ErrConstant.OPEN_FEIGN_ACCESS_ERR_CODE,
						"订单服务不可用: " + cause.getMessage());
			}
		};
	}
}
```

**OpenFeign 配置**（`application.yml`）:
```yaml
feign:
  client:
    config:
      default:
        connect-timeout: 3000
        read-timeout: 5000
        logger-level: BASIC
  sentinel:
    enabled: true

spring:
  main:
    allow-circular-references: true
```

**启动类注解**:
```java
@EnableFeignClients(basePackages = "com.example.project.sdk.api")
@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
```

**OpenFeign 强制规范**:
1. **接口定义在 sdk 模块**：Feign Client 接口放在 sdk 模块的 `api` 包，供跨服务引用
2. **降级实现在 server 模块**：FallbackFactory 放在 server 模块的 `adapter.api` 包
3. **必须配置降级**：每个 Feign Client 必须提供 FallbackFactory，禁止裸调用
4. **禁止 Feign 调用外部服务**：Feign 仅用于内部微服务调用
5. **超时配置**：connect-timeout ≤ 3s，read-timeout ≤ 5s
6. **日志级别**：生产环境使用 `BASIC`，仅记录请求方法和 URL

---

> **交叉引用**：
> - 基础设施层总览详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
> - Repository 与数据访问规范详见 [03d1-repository.md](./03d1-repository.md)
> - 领域间跨服务调用示例详见 [09b-domain-interaction-rules.md](./09b-domain-interaction-rules.md)
