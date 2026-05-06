# 领域层（Domain Layer）规范

- 版本号: v1.0（从 03-four-layer-architecture.md v4.0 拆分）
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> 适配层规范详见 [03a-adapter-layer.md](./03a-adapter-layer.md)
> 应用层规范详见 [03b-application-layer.md](./03b-application-layer.md)
> 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)

---

## 1. 领域层职责边界

**职责边界**:
- 核心业务逻辑和领域规则
- 领域数据对象（贫血模型，**只包含数据属性，不包含业务方法**）
- 值对象（不可变，无 ID）
- 领域服务（**所有业务逻辑集中在此**）
- 领域事件
- **PO ↔ DO 转换的编排层**（DomainService 负责调用 Convertor）

---

## 2. 领域数据对象（贫血模型）规范

```java
/**
 * 用户领域数据对象（DO）
 *
 * <p>贫血模型：只包含数据属性，不包含任何业务逻辑方法。</p>
 * <p>业务逻辑统一由 DomainService 处理。</p>
 */
@Data
public class UserDO {

	private Long id;
	private String userName;
	private Email email;
	private Phone phone;
	private UserStatus status;
	private Date createTime;
	private Date updateTime;
}
```

---

## 3. 值对象规范

```java
/**
 * 邮箱值对象
 */
public record Email(String value) {

	private static final Pattern PATTERN = Pattern.compile(
			"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
	);

	public Email {
		if (value == null || value.isBlank()) {
			throw new BusinessException("邮箱不能为空");
		}
		if (!PATTERN.matcher(value).matches()) {
			throw new BusinessException("邮箱格式不正确");
		}
	}
}
```

---

## 4. DomainService 规范

> **【核心原则】** DomainService 是 PO ↔ DO 转换的边界层。查询时从 Repository 获取 PO 并转换为 DO；增删改时将 DO 转换为 PO 并调用 Repository 的 CRUD 方法。

```java
/**
 * 用户领域服务
 *
 * <p>查询：通过 UserRepository 获取 PO，再通过 Convertor 转换为 DO。</p>
 * <p>增删改：将 DO 转换为 PO，调用 Repository（IService）的 saveOrUpdate/removeById 等方法。</p>
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {

	private final UserRepository userRepository;  // 本领域 infrastructure 层
	private final UserConvertor userConvertor;     // 本领域 infrastructure 层
	// 注：包路径为 com.example.project.server.domain.user.service

	/**
	 * 根据ID查询用户（PO → DO 转换）
	 */
	public UserDO getById(Long id) {
		UserPO po = userRepository.getById(id);
		if (po == null) {
			throw new BusinessException("用户不存在");
		}
		return toDO(po);
	}

	/**
	 * 保存用户（DO → PO + Repository）
	 */
	public void save(UserDO user) {
		UserPO po = userConvertor.toPO(user);
		userRepository.saveOrUpdate(po);
		user.setId(po.getId());
	}

	/**
	 * 删除用户（通过 Repository）
	 */
	public void deleteById(Long id) {
		userRepository.removeById(id);
	}

	/**
	 * PO → DO 转换（含关联数据查询）
	 */
	private UserDO toDO(UserPO po) {
		UserDO user = userConvertor.toDomain(po);
		List<Long> roleIds = userRepository.findRoleIdsByUserId(po.getId());
		user.setRoleIds(roleIds);
		return user;
	}
}
```

---

## 5. 领域层强制规范

1. **贫血模型**：领域对象（DO）只包含数据属性，**严禁在 DO 中定义业务方法**
2. **业务逻辑集中**：所有业务逻辑必须写在 DomainService 中，禁止分散在 DO 中
3. **DomainService 负责 PO ↔ DO 转换**：查询时 PO→DO，增删改时 DO→PO
4. **查询走 Repository**：DomainService 通过 UserRepository（infrastructure 层）获取 PO 类型数据
5. **增删改走 Repository**：PO 继承 `Model<T>`，但增删改统一通过 Repository（IService）的 `save/saveOrUpdate/removeById/updateById` 方法实现
6. **关联表操作走 Repository**：如角色分配等无独立 PO 的关联表操作，保留在 Repository 中
7. **业务异常在领域层抛出**：`BusinessException` 携带错误码和消息

---

## 6. 参数校验规范（领域层）

> **【核心原则】** 领域层负责全量验证，包括：
> 1. **参数格式校验**：对入参进行基本格式验证（如非空、长度、范围、枚举值等），校验不通过抛出 `ParamException`
> 2. **业务规则校验**：需要查询数据库进行判断的验证逻辑（如唯一性校验、存在性校验等），以及业务规则验证（如状态校验、权限校验等），校验不通过抛出 `BusinessException`
> 3. 领域层是业务规则的最终守护者，所有校验逻辑必须内聚在领域服务内部实现

### 6.1 校验职责划分

| 校验类型 | 负责层 | 异常类型 | 示例 |
|---------|--------|---------|------|
| 非空/格式/范围校验 | 应用层（AppService）+ 领域层（DomainService） | `ParamException` | 用户名非空、手机号格式、邮箱格式、数值范围、枚举值 |
| 数据存在性校验 | 领域层（DomainService） | `BusinessException` | 用户ID是否存在、角色是否存在 |
| 唯一性校验 | 领域层（DomainService） | `BusinessException` | 用户名唯一性、角色编码唯一性、岗位编码唯一性 |
| 业务规则校验 | 领域层（DomainService） | `BusinessException` | 超级管理员禁止删除、部门循环依赖、存在子菜单禁止删除 |

### 6.2 校验方法封装规范

1. **【强制】** 业务校验必须单独封装为独立的 `validateXxx` 方法，**禁止**在业务逻辑中混合校验代码
2. **【强制】** 校验方法命名格式：`validate` + 校验内容，如 `validateUsernameNotExists`、`validateUserExists`、`validateNotSuperAdmin`
3. **【强制】** 校验方法必须放在方法体最前面，先校验再执行业务逻辑
4. **【强制】** 参数格式校验失败抛出 `ParamException`（参数异常），业务规则校验失败抛出 `BusinessException`（业务异常），均携带明确的中文描述
5. **【强制】** 领域层必须进行参数格式校验（非空、长度、范围、枚举值等），与应用层形成双重保障
6. **【推荐】** 同一实体的多个业务方法可共享校验逻辑，提取为 `private` 方法复用

### 6.3 代码示例

```java
@Service
@RequiredArgsConstructor
public class UserDomainService {

	private final UserRepository userRepository;
	private final UserConvertor userConvertor;

	/**
	 * 创建用户（含全量校验）
	 */
	public void save(UserDO user) {
		// 1. 参数格式校验（领域层：ParamException）
		validateFormatForSave(user);

		// 2. 业务规则校验（领域层：BusinessException）
		validateUsernameNotExists(user.getUserName());

		// 3. DO → PO + 保存
		UserPO po = userConvertor.toPO(user);
		userRepository.saveOrUpdate(po);
		user.setId(po.getId());
	}

	/**
	 * 删除用户（含全量校验）
	 */
	public void deleteById(Long id) {
		// 1. 参数格式校验
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}

		// 2. 业务规则校验
		validateUserExists(id);
		validateNotSuperAdmin(id);

		// 3. 执行逻辑删除
		userRepository.removeById(id);
	}

	/**
	 * 参数格式校验：保存场景
	 */
	private void validateFormatForSave(UserDO user) {
		if (user == null) {
			throw new ParamException("用户信息不能为空");
		}
		if (user.getUserName() == null || user.getUserName().isBlank()) {
			throw new ParamException("用户名不能为空");
		}
		if (user.getUserName().length() < 2 || user.getUserName().length() > 20) {
			throw new ParamException("用户名长度必须在2-20个字符之间");
		}
	}

	/**
	 * 校验用户名唯一性（业务规则校验：BusinessException）
	 */
	private void validateUsernameNotExists(String username) {
		boolean exists = userRepository.lambdaQuery()
				.eq(UserPO::getUsername, username)
				.exists();
		if (exists) {
			throw new BusinessException("用户名已存在");
		}
	}

	/**
	 * 校验用户存在性（数据存在性校验：BusinessException）
	 */
	private void validateUserExists(Long id) {
		if (id == null || !userRepository.existsById(id)) {
			throw new BusinessException("用户不存在");
		}
	}

	/**
	 * 校验非超级管理员（业务规则校验：BusinessException）
	 */
	private void validateNotSuperAdmin(Long id) {
		if (id != null && id == 1L) {
			throw new BusinessException("超级管理员禁止删除");
		}
	}
}
```

### 6.4 领域层校验强制规范

1. **【强制】** DomainService 所有对外提供的公共方法必须包含校验
2. **【强制】** 校验逻辑封装为 `private validateXxx` 方法，禁止在业务方法中直接内联校验代码
3. **【强制】** 领域层负责全量验证：参数格式校验 + 数据存在性校验 + 唯一性校验 + 业务规则校验
4. **【强制】** 参数格式校验失败抛出 `ParamException`，业务规则校验失败抛出 `BusinessException`
5. **【强制】** 领域层必须进行参数格式校验（非空、长度、范围、枚举值等），与应用层形成双重保障
6. **【强制】** 校验顺序：先参数格式校验（ParamException），再业务规则校验（BusinessException），最后执行业务逻辑
7. **【推荐】** 高频使用的校验方法可提取为工具类或基类方法

---

> **交叉引用**：
> - 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 适配层规范详见 [03a-adapter-layer.md](./03a-adapter-layer.md)
> - 应用层规范详见 [03b-application-layer.md](./03b-application-layer.md)
> - 基础设施层规范详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
