# 应用层（Application Layer）规范

- 版本号: v1.0（从 03-four-layer-architecture.md v4.0 拆分）
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> 适配层规范详见 [03a-adapter-layer.md](./03a-adapter-layer.md)
> 领域层规范详见 [03c-domain-layer.md](./03c-domain-layer.md)
> 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)

---

## 1. 应用层职责边界

**职责边界**:
- 编排领域服务，完成完整业务用例
- 事务控制（`@Transactional`）
- 协调多个领域服务或外部服务
- BO → DO 转换（通过 Convertor）
- DO → BO 转换（通过 Convertor）
- 权限检查（粗粒度）
- **入参和出参必须都是 BO 类型，禁止暴露 Cmd/DTO**
- **不包含核心业务逻辑**

---

## 2. AppService 命名与结构

```java
/**
 * 用户应用服务
 *
 * <p>AppService 层的输入/输出对象必须使用 BO（Business Object）后缀。</p>
 * <p>禁止在接口签名中暴露 Cmd、DTO、DO 等其他类型。</p>
 */
public interface UserAppService {

	/**
	 * 创建用户
	 */
	Long createUser(UserCreateBO bo);

	/**
	 * 更新用户
	 */
	void updateUser(Long id, UserCreateBO bo);

	/**
	 * 删除用户
	 */
	void deleteUser(Long id);

	/**
	 * 根据ID查询用户
	 */
	UserBO getUserById(Long userId);

	/**
	 * 分页查询用户列表
	 */
	PageBO<UserBO> pageUsers(long current, long size, String keyword);
}

/**
 * 用户应用服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAppServiceImpl implements UserAppService {

	private final UserDomainService userDomainService;
	private final UserConvertor userConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createUser(UserCreateBO bo) {
		// 1. 校验用户名唯一（调用领域服务）
		userDomainService.validateUsernameNotExists(bo.getUsername());

		// 2. BO → DO（MapStruct 转换）
		UserDO user = userConvertor.toDomainFromCreateBO(bo);
		user.setStatus(1);

		// 3. 保存用户（调用领域服务）
		userDomainService.save(user);

		log.info("[UserAppService] 用户创建成功, userId={}, username={}", user.getId(), bo.getUsername());
		return user.getId();
	}

	@Override
	public UserBO getUserById(Long userId) {
		UserDO user = userDomainService.getById(userId);
		return userConvertor.toUserBO(user);
	}

	@Override
	public PageBO<UserBO> pageUsers(long current, long size, String keyword) {
		PageDO<UserDO> doPage = userDomainService.findPage(current, size, keyword);

		List<UserBO> boList = userConvertor.toUserBOList(doPage.getRecords());

		PageBO<UserBO> result = new PageBO<>();
		result.setRecords(boList);
		result.setTotal(doPage.getTotal());
		result.setSize(doPage.getSize());
		result.setCurrent(doPage.getCurrent());
		return result;
	}
}
```

---

## 3. AppService 强制规范

1. **必须有接口 + 实现类**：接口定义契约，实现类完成编排
2. **事务控制**：写操作必须加 `@Transactional(rollbackFor = Exception.class)`
3. **只做编排，不做业务计算**：业务规则交给 DomainService
4. **【最严格】禁止直接操作 Repository/Mapper**：AppService **只能**通过 DomainService 间接访问数据层，**严禁**直接注入或调用 Repository、Mapper
5. **【强制】入参必须是 BO 类型**：AppService 接口的所有对象参数必须以 BO 结尾，**严禁**使用 Cmd、ReqDTO 等非 BO 类型
6. **【强制】出参必须是 BO 类型**：AppService 接口的所有对象返回值必须以 BO 结尾，**严禁**返回 DTO 类型
7. **对象转换通过 Convertor**：BO → DO 和 DO → BO 转换通过 MapStruct Convertor 实现
8. **日志记录**：关键操作入口记录 info 级别日志

---

## 4. 四层架构调用链示例（严格遵守）

```java
// ✅ 正确调用链：Controller → AppService → DomainService → Repository
@RestController
public class UserController extends BaseController {
    private final UserAppService userAppService;  // 注入 AppService
    private final UserConvertor userConvertor;     // 注入 Convertor（Cmd→BO、BO→DTO）
}

@Service
public class UserAppServiceImpl implements UserAppService {
    private final UserDomainService userDomainService;  // 只注入 DomainService
    private final UserConvertor userConvertor;           // 注入 Convertor（BO→DO、DO→BO）
    // ❌ 禁止注入 UserRepository、UserMapper
    // ❌ 禁止入参/出参使用 Cmd 或 DTO
    // 注：包路径为 com.example.project.server.application.service.impl
}

@Service
public class UserDomainService {
    private final UserRepository userRepository;  // DomainService 访问 Repository
    private final UserConvertor userConvertor;    // Convertor（PO↔DO）
    // 注：包路径为 com.example.project.server.domain.user.service
}

// ❌ 错误：Controller 直接注入 Repository
@RestController
public class UserController {
    private final UserRepository userRepository;  // 违反四层架构！
}
```

---

## 5. 参数校验规范（应用层）

> **【核心原则】** 应用层负责对参数基本格式进行验证（如非空校验、格式校验、范围校验等），校验不通过抛出 `ParamException`。应用层不包含业务规则校验，业务规则校验由领域层（DomainService）负责。
>
> **【验证顺序】** 应用层首先进行基础参数格式验证 → 然后传递给领域层进行参数格式验证 + 业务规则验证 + 数据库查询相关验证（双重保障）。

### 5.1 校验职责划分

| 层级 | 校验范围 | 异常类型 | 示例 |
|------|---------|---------|------|
| 应用层（AppService） | 参数基本格式校验 | `ParamException` | 非空校验、字符串长度、格式正则、数值范围、枚举值 |
| 领域层（DomainService） | 参数格式校验 + 业务逻辑校验 | `ParamException` / `BusinessException` | 数据存在性、业务规则、权限校验、唯一性校验 |

### 5.2 校验方法封装规范

1. **【强制】** 参数校验必须单独封装为独立的 `validateXxx` 方法，**禁止**在业务逻辑中混合校验代码
2. **【强制】** 校验方法命名格式：`validate` + 业务动作，如 `validateCreate`、`validateUpdate`、`validateQuery`
3. **【强制】** 校验方法必须放在方法体最前面，先校验再执行业务逻辑
4. **【强制】** 校验失败统一抛出 `ParamException`，错误信息使用明确的中文描述
5. **【强制】** 应用层与领域层均需进行参数格式校验，形成双重保障

### 5.3 代码示例

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAppServiceImpl implements UserAppService {

	private final UserDomainService userDomainService;
	private final UserConvertor userConvertor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createUser(UserCreateBO bo) {
		// 1. 应用层参数校验（基本格式）
		validateCreate(bo);

		// 2. BO → DO（MapStruct 转换）
		UserDO user = userConvertor.toDomainFromCreateBO(bo);
		user.setStatus(1);

		// 3. 保存用户（调用领域服务，领域层负责业务规则校验）
		userDomainService.save(user);

		log.info("[UserAppService] 用户创建成功, userId={}, username={}", user.getId(), bo.getUsername());
		return user.getId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUser(Long id, UserCreateBO bo) {
		// 1. 应用层参数校验
		validateUpdate(id, bo);

		// 2. 业务逻辑（领域层校验数据存在性）
		UserDO existingUser = userDomainService.getById(id);
		// ... 更新逻辑
	}

	/**
	 * 创建用户参数校验（应用层：基本格式校验）
	 *
	 * <p>仅校验参数基本格式，业务规则校验由 DomainService 负责。</p>
	 */
	private void validateCreate(UserCreateBO bo) {
		if (bo == null) {
			throw new ParamException("创建参数不能为空");
		}
		if (bo.getUsername() == null || bo.getUsername().isBlank()) {
			throw new ParamException("用户名不能为空");
		}
		if (bo.getUsername().length() < 2 || bo.getUsername().length() > 20) {
			throw new ParamException("用户名长度必须在2-20个字符之间");
		}
		if (bo.getPhone() != null && !bo.getPhone().matches("^1[3-9]\\d{9}$")) {
			throw new ParamException("手机号格式不正确");
		}
	}

	/**
	 * 更新用户参数校验（应用层：基本格式校验）
	 */
	private void validateUpdate(Long id, UserCreateBO bo) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		validateCreate(bo); // 格式校验与创建相同
	}
}
```

### 5.4 应用层校验强制规范

1. **【强制】** AppService 所有对外提供的公共方法必须包含参数校验
2. **【强制】** 校验逻辑封装为 `private validateXxx` 方法，禁止在业务方法中直接内联校验代码
3. **【强制】** 应用层仅做基本格式校验（非空、长度、格式、范围），**禁止**在此层做数据库查询校验
4. **【强制】** 数据存在性校验、唯一性校验、业务规则校验交由 DomainService 的 `validateXxx` 方法处理
5. **【强制】** 参数格式校验不通过抛出 `ParamException`，**禁止**在应用层抛出 `BusinessException`
6. **【推荐】** 同一实体的创建和更新共享格式校验逻辑时，更新方法可复用创建的校验方法

---

> **交叉引用** :
> - 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 适配层规范详见 [03a-adapter-layer.md](./03a-adapter-layer.md)
> - 领域层规范详见 [03c-domain-layer.md](./03c-domain-layer.md)
> - 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
