# 领域间交互约束规范

- 版本号: v4.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 领域隔离总览详见 [09-domain-isolation.md](./09-domain-isolation.md)
> 领域包分离规范详见 [09a-domain-package-structure.md](./09a-domain-package-structure.md)

---

## 1. 领域间交互核心原则

在共享层级架构下，应用层（AppService）是跨领域交互的编排中心：

- **AppService 可直接调用所有领域的 DomainService**：无需通过 Remote/Api 机制
- **调用链简洁**：`AppService → DomainService A + DomainService B + ...`
- **事务边界清晰**：跨领域操作在同一 AppService 方法中通过 `@Transactional` 统一控制

```
AppService（应用层编排）
    ├── 注入 DomainService A（领域 A 的服务）
    ├── 注入 DomainService B（领域 B 的服务）
    └── 协调多个领域服务完成业务用例
```

---

## 2. 允许的行为

| 场景 | 调用方式 | 说明 |
|------|---------|------|
| AppService 调用本领域 DomainService | 直接注入 | 常规调用 |
| AppService 调用其他领域 DomainService | 直接注入 | AppService 可跨域调用 |
| AppService 调用 Convertor | 直接注入 | Convertor 在 infrastructure 共享 |
| AppService 调用 Repository | **禁止** | 必须通过 DomainService 间接访问 |
| 微服务间调用 | 通过 OpenFeign | 跨服务调用仍使用 Feign Client |

---

## 3. 禁止的行为

| 禁止行为 | 说明 | 反例 |
|---------|------|------|
| AppService 直接调用 Mapper | Mapper 是基础设施层内部实现 | `authAppService` 中直接注入 `UserMapper` |
| AppService 直接调用 RepositoryImpl | Repository 实现属于基础设施层 | `authAppService` 中直接注入 `UserRepositoryImpl` |
| Controller 直接调用 DomainService | Controller 只能调用 AppService | `authController` 中直接注入 `UserDomainService` |
| Controller 直接操作 PO/DO | Controller 只能使用 Cmd/DTO/BO | `userController` 中直接 new `UserPO` |

---

## 4. 跨领域调用示例

### 4.1 AppService 跨域编排（推荐方式）

> **auth 领域说明**：auth 领域当前无独立 DomainService，认证逻辑（密码校验、Token 生成）直接在 AuthAppService 中完成，通过注入 UserDomainService 跨域查询用户数据。若后续认证逻辑复杂化，可抽取为 AuthDomainService。

```java
@Service
@RequiredArgsConstructor
public class AuthAppServiceImpl implements AuthAppService {

    // 直接注入用户领域服务（跨域调用）
    private final UserDomainService userDomainService;
    private final UserConvertor userConvertor;

    @Override
    public LoginResultBO login(LoginBO bo) {
        // 1. 直接调用用户领域服务查询用户
        UserDO user = userDomainService.findByUsername(bo.getUsername());

        // 2. 校验用户状态（AppService 层编排）
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已停用");
        }

        // 3. 校验密码（AppService 层编排）
        if (!bo.getPassword().equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 4. 登录并生成 Token（AppService 层编排）
        StpUtil.login(user.getId());

        // 5. 构建返回结果
        LoginResultBO result = new LoginResultBO();
        result.setToken(StpUtil.getTokenValue());
        result.setUserInfo(userConvertor.toUserBO(user));
        return result;
    }
}
```

### 4.2 Controller 只调用 AppService

```java
@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthAppService authAppService;
    private final UserConvertor userConvertor;

    @PostMapping("/login")
    public ApiResponse<LoginDTO> login(@RequestBody @Valid LoginCmd cmd) {
        return succ(userConvertor.toDTOFromLoginBO(authAppService.login(userConvertor.toLoginBO(cmd))));
    }
}
```

---

## 5. 跨服务调用（OpenFeign）

微服务架构下，不同服务之间的调用仍使用 **OpenFeign**，Feign Client 定义在 sdk 模块：

```java
@FeignClient(name = "user-service", path = "/backend/user",
    fallbackFactory = UserFeignClientFallback.class)
public interface UserFeignClient {
    @GetMapping("/{userId}")
    ApiResponse<UserDTO> getById(@PathVariable("userId") Long userId);
}
```

---

## 6. Remote 包定位

`infrastructure/remote/` 包仅用于**外部系统调用**（非领域间交互）：

| 用途 | 说明 | 示例 |
|------|------|------|
| 外部第三方服务 | 支付、短信、OSS 等 | `PayRemote.java`、`SmsRemote.java` |
| 外部 API 调用 | 使用 Hutool HttpUtil | `WechatRemote.java` |

**Remote 不再用于领域间交互**，领域间直接通过 AppService 编排。

---

## 7. 领域间交互检查清单

| 检查项 | 合格标准 |
|--------|---------|
| 包结构 | domain/ 下是否按业务领域分包？ |
| 跨域调用 | AppService 是否直接注入 DomainService？ |
| 禁止直接访问 | 是否存在 Controller 直接注入 DomainService/Repository 的情况？ |
| Convertor 位置 | Convertor 是否在 infrastructure/convertor/ 下？ |
| BO 位置 | BO 是否在对应领域 domain/{领域}/bo/ 下？ |
| 事务边界 | 跨域操作是否在 AppService 中统一事务控制？ |
| Remote 定位 | infrastructure/remote/ 是否仅用于外部系统调用？ |

---

> **交叉引用**：
> - 领域隔离总览详见 [09-domain-isolation.md](./09-domain-isolation.md)
> - 领域包分离规范详见 [09a-domain-package-structure.md](./09a-domain-package-structure.md)
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - OpenFeign 内部服务调用规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md) 第6节
