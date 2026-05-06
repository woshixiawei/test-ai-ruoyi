# Repository 与数据访问规范

- 版本号: v1.0（从 03d-infrastructure-layer.md v1.0 拆分）
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> 基础设施层总览详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
> 外部服务调用规范详见 [03d2-external-service.md](./03d2-external-service.md)

---

## 1. PO 定义规范（强制）

```java
/**
 * 用户持久化对象
 *
 * <p>继承 Model<T> 支持 ActiveRecord 模式。</p>
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class UserPO extends Model<UserPO> {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 用户名 */
	private String username;

	// ... 其他字段
}
```

---

## 2. Repository 规范（继承 BaseRepository，CRUD + 查询 + 关联表操作）

> **【核心原则】** 所有 Repository 接口必须继承 `BaseRepository<T>`（base 模块），所有 Repository 实现类必须继承 `BaseRepositoryImpl<M, T>`（base 模块）。BaseRepository 继承 MyBatis-Plus 的 `IService<T>`，BaseRepositoryImpl 继承 `ServiceImpl<M, T>`，获得通用 CRUD 能力。自定义查询方法使用 `lambdaQuery()` 或 `Wrappers.lambdaQuery()` 构建条件。

### 2.1 BaseRepository 基础接口（base.basecls 包）

```java
/**
 * Repository 基础接口
 *
 * <p>所有具体的 Repository 接口都必须继承此接口。</p>
 * <p>继承 MyBatis-Plus 的 IService<T>，获得通用 CRUD 能力。</p>
 */
public interface BaseRepository<T> extends IService<T> {
}
```

### 2.2 BaseRepositoryImpl 基础实现类（base.basecls 包）

```java
/**
 * Repository 基础实现类
 *
 * <p>所有具体的 Repository 实现类都必须继承此类。</p>
 * <p>继承 MyBatis-Plus 的 ServiceImpl<M, T>，获得通用 CRUD 实现。</p>
 */
public abstract class BaseRepositoryImpl<M extends BaseMapper<T>, T>
        extends ServiceImpl<M, T> implements BaseRepository<T> {
}
```

### 2.3 Repository 接口定义（infrastructure.repository 包）

```java
/**
 * 用户仓储接口
 *
 * <p>继承 BaseRepository<UserPO>，获得 MyBatis-Plus IService 的通用 CRUD 能力。</p>
 * <p>自定义查询方法使用 LambdaQueryWrapper 构建。</p>
 */
public interface UserRepository extends BaseRepository<UserPO> {

	/** 根据用户名查询 → 返回 PO */
	Optional<UserPO> findByUsername(String username);

	/** 分页查询 → 入参超过4个时使用 Query 对象，返回 PO 分页 */
	Page<UserPO> findPage(UserQuery query);

	/** 查询用户角色ID列表（关联数据） */
	List<Long> findRoleIdsByUserId(Long userId);

	/** 分配角色（关联表操作，无独立 PO） */
	void assignRoles(Long userId, List<Long> roleIds);

	// 通用的 getById / save / saveOrUpdate / removeById / updateById 等方法
	// 由 BaseRepository（IService）继承而来，无需重复定义
}
```

### 2.4 Repository 实现规范

```java
/**
 * 用户仓储实现
 *
 * <p>继承 BaseRepositoryImpl<UserMapper, UserPO>，获得 ServiceImpl 的通用 CRUD 实现。</p>
 * <p>自定义查询方法使用 lambdaQuery() 或 Wrappers.lambdaQuery() 构建。</p>
 */
@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserMapper, UserPO> implements UserRepository {

	@Override
	public Optional<UserPO> findByUsername(String username) {
		return lambdaQuery().eq(UserPO::getUsername, username).oneOpt();
	}

	@Override
	public Page<UserPO> findPage(UserQuery query) {
		return lambdaQuery()
				.like(query.getKeyword() != null, UserPO::getUsername, query.getKeyword())
				.eq(query.getStatus() != null, UserPO::getStatus, query.getStatus())
				.orderByDesc(UserPO::getCreateTime)
				.page(new Page<>(query.getCurrent(), query.getSize()));
	}

	@Override
	public List<Long> findRoleIdsByUserId(Long userId) {
		return baseMapper.selectRoleIdsByUserId(userId);
	}

	@Override
	public void assignRoles(Long userId, List<Long> roleIds) {
		baseMapper.deleteUserRoles(userId);
		if (!CollectionUtils.isEmpty(roleIds)) {
			baseMapper.insertUserRoles(userId, roleIds);
		}
	}
}
```

### 2.5 Repository 强制规范

1. **接口定义在 `infrastructure.repository` 包下**，不在 domain 层
2. **接口必须继承 BaseRepository<T>**：获得 MyBatis-Plus IService 的通用 CRUD 能力（getById / save / saveOrUpdate / removeById / updateById 等）
3. **实现类必须继承 BaseRepositoryImpl<M, T>**：获得 ServiceImpl 的通用 CRUD 实现
4. **返回 PO 类型**：所有自定义查询方法返回 PO 或 `Optional<PO>` 或 `Page<PO>`，**严禁返回 DO 类型**
5. **自定义查询使用 Lambda 方式**：使用 `lambdaQuery()` 或 `Wrappers.lambdaQuery()` 构建查询条件，**禁止手写 XML 映射文件**
6. **通过 baseMapper 访问 Mapper**：实现类通过继承的 `baseMapper` 字段访问 Mapper，无需单独注入
7. **入参超过 4 个时使用 Query 对象**：Query 对象定义在 `infrastructure.query` 包下
8. **关联表操作可保留**：如角色分配等无独立 PO 的关联表 CUD 操作，可在 Repository 中通过 baseMapper 实现

---

## 3. Query 对象规范

> **【强制】** 当查询方法的入参超过 4 个时，**必须**将这些参数封装为 Query 对象。

```java
/**
 * 用户查询参数对象
 */
@Data
public class UserQuery {

	/** 当前页码 */
	private long current = 1;

	/** 每页大小 */
	private long size = 10;

	/** 搜索关键词 */
	private String keyword;

	/** 状态筛选 */
	private Integer status;
}
```

---

## 4. 软删除实现规范

> **【核心原则】** 软删除字段 `is_deleted` 使用 `bigint(20)` 类型，`0` 表示未删除，已删除记录将该字段设为**记录自身的 ID 值**。通过 MyBatis-Plus 原生 `@TableLogic` 注解和全局配置实现，**禁止**手动覆写 `removeById()`。

### 4.1 软删除字段定义

| 属性 | 值 | 说明 |
|------|-----|------|
| 字段名 | `is_deleted` | 统一命名 |
| 字段类型 | `bigint(20)` | 可存储 bigint unsigned 主键值 |
| 未删除值 | `0` | 所有未删除记录的 is_deleted 恒为 0 |
| 已删除值 | 记录ID | 删除时将 is_deleted 设为该记录的 id 值 |
| Java 类型 | `Long` | 映射为包装类型 |

### 4.2 唯一索引与软删除的配合

传统方案（`is_deleted = 0/1`）存在的问题：已删除记录的 `is_deleted` 全部为 `1`，导致联合唯一索引 `(业务字段, is_deleted)` 只能容纳一条同名已删除记录，无法支持删除后重建同名记录。

ID 型软删除方案优势：
- 未删除记录：`is_deleted = 0`，联合唯一索引 `(业务字段, is_deleted)` 确保同一业务字段仅一条未删除记录
- 已删除记录：`is_deleted = 记录ID`（每条唯一），联合唯一索引允许多条同名已删除记录共存
- 删除后重建同名记录：新记录 `is_deleted = 0`，与旧记录 `is_deleted = 旧ID` 不冲突

```sql
-- 正例：联合唯一索引 + ID型软删除
UNIQUE KEY uk_username_deleted (username, is_deleted)

-- 反例：单一唯一索引（删除后无法重建同名记录）
-- UNIQUE KEY uk_username (username)
```

### 4.3 MyBatis-Plus 原生配置

**application.yml 全局配置**：

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: is_deleted
      logic-delete-value: id    # ID型软删除：删除时SET is_deleted = id（MySQL列引用，非固定值）
      logic-not-delete-value: 0  # 查询时自动追加 WHERE is_deleted = 0
```

> **原理说明**：`logic-delete-value: id` 中的 `id` 是 MySQL 列引用，MyBatis-Plus 将其直接嵌入 SQL 模板生成 `SET is_deleted = id`。MySQL 执行时将每行的 `is_deleted` 字段设为该行自身的 `id` 列值，实现 ID 型软删除。

**PO 实体字段定义**：

```java
/**
 * 逻辑删除标志：0-未删除，非0-已删除（值为被删除记录的ID）
 */
@TableLogic(value = "0", delval = "id")
private Long isDeleted;
```

> **原理说明**：`@TableLogic` 注解的 `delval = "id"` 表示删除时将 `is_deleted` 设为 MySQL `id` 列的值。MyBatis-Plus 生成 SQL 时将 `id` 直接嵌入 SET 子句，而非作为参数绑定。`value = "0"` 确保查询时自动追加 `WHERE is_deleted = 0`。

### 4.4 MyBatis-Plus 生成的 SQL

| 操作 | 生成的 SQL |
|------|-----------|
| 单条删除 `removeById(id)` | `UPDATE table SET is_deleted = id WHERE id = ? AND is_deleted = 0` |
| 批量删除 `removeByIds(ids)` | `UPDATE table SET is_deleted = id WHERE id IN (?, ?, ...) AND is_deleted = 0` |
| 查询 `getById(id)` | `SELECT * FROM table WHERE id = ? AND is_deleted = 0` |
| 列表查询 `list()` | `SELECT * FROM table WHERE is_deleted = 0` |

> **核心要点**：批量删除 `removeByIds()` 也是单条 SQL 高效完成——`SET is_deleted = id` 在 MySQL 中会为每行分别设置该行 `id` 列的值，无需逐条更新。

### 4.5 软删除强制规范

1. **【强制】** 所有业务表必须包含 `is_deleted bigint(20) NOT NULL DEFAULT 0` 字段
2. **【强制】** 逻辑删除时 `is_deleted` 设为记录 ID，通过 `@TableLogic(delval = "id")` 和 `logic-delete-value: id` 原生实现，**禁止**手动覆写 `removeById()`
3. **【强制】** PO 实体必须添加 `@TableLogic(value = "0", delval = "id")` 注解到 `isDeleted` 字段
4. **【强制】** 唯一索引必须改为联合唯一索引 `(业务字段, is_deleted)`，**禁止**在有逻辑删除的表上使用单一唯一索引
5. **【强制】** 仅日志表和纯关联中间表允许物理删除，不添加 `is_deleted` 字段
6. **【推荐】** 查询条件中无需手动添加 `WHERE is_deleted = 0`，MyBatis-Plus `@TableLogic` 注解会自动追加

---

> **交叉引用**：
> - 基础设施层总览详见 [03d-infrastructure-layer.md](./03d-infrastructure-layer.md)
> - 外部服务调用规范详见 [03d2-external-service.md](./03d2-external-service.md)
> - 四层架构总览详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - MySQL 数据库设计规范中软删除字段定义详见架构师技能 01-mysql-design-spec.md
