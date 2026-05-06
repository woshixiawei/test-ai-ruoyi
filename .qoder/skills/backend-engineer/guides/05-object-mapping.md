# 后端 MapStruct 对象映射规范

- 版本号: v4.0
- 适用范围: 所有后端 server 模块
- 约束力: **强制**

---

> **TL;DR 核心要点**：
> 1. **所有对象转换必须用MapStruct Convertor**，禁止手动setter（6种例外见第5节）
> 2. **新建场景**：`convertor.toDomainFromCreateBO(bo)` → `UserDO`
> 3. **更新场景（最常违反）**：`convertor.updateDomainFromBO(bo, @MappingTarget domain)`，**禁止创建新DO后setId**
> 4. **分页转换**：`PageDO.from()`/`PageBO.from()`/`PageDTO.from()` 工厂方法，禁止手动new+setter
> 5. **Convertor位置**：`infrastructure/convertor/XxxConvertor.java`
> 6. **允许手动setter的6种例外**：默认值、计算属性、多源组装、树形结构、关联数据填充、自增ID回写

---

**【强制】所有对象间转换必须通过 MapStruct Convertor 实现，严禁手动 setter 转换。**

MapStruct 在编译期生成转换代码，性能等同于手写，且类型安全。

---

## 1. Convertor 定义规范

- **位置**：Convertor 接口定义在 `infrastructure.convertor` 包下
- **命名**：`XxxConvertor`（英式拼写，不是 Converter）
- **注解**：`@Mapper(componentModel = "spring")`

```java
/**
 * 用户对象转换器（MapStruct）
 *
 * <p>定义在 infrastructure.convertor 包下，由 Spring 管理。</p>
 * <p>命名规范：XxxConvertor（英式拼写）。</p>
 * <p>所有对象间转换必须通过本接口，禁止手动 setter 转换。</p>
 */
@Mapper(componentModel = "spring")
public interface UserConvertor {

	// ========== PO ↔ DO（基础设施层 ↔ 领域层） ==========

	/** PO -> DO */
	UserDO toDomain(UserPO po);

	/** PO列表 -> DO列表 */
	List<UserDO> toDomainList(List<UserPO> poList);

	/** DO -> PO */
	UserPO toPO(UserDO domain);

	// ========== DO → BO（领域层 → 应用层） ==========

	/** DO -> UserBO（查询结果） */
	UserBO toUserBO(UserDO domain);

	/** DO列表 -> UserBO列表 */
	List<UserBO> toUserBOList(List<UserDO> domainList);

	// ========== BO → DTO（应用层 → SDK层，Controller 调用） ==========

	/** UserBO -> UserDTO */
	UserDTO toDTOFromBO(UserBO bo);

	/** UserBO列表 -> UserDTO列表 */
	List<UserDTO> toDTOFromBOList(List<UserBO> boList);

	/** LoginResultBO -> LoginDTO */
	LoginDTO toDTOFromLoginBO(LoginResultBO bo);

	// ========== Cmd → BO（SDK层 → 应用层，Controller 调用） ==========

	/** LoginCmd -> LoginBO */
	LoginBO toLoginBO(LoginCmd cmd);

	/** UserCreateCmd -> UserCreateBO */
	UserCreateBO toCreateBO(UserCreateCmd cmd);

	/**
	 * UserUpdateCmd -> UserCreateBO（更新场景复用BO）
	 *
	 * <p>忽略 username、password 字段（更新时不允许修改）。</p>
	 */
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	UserCreateBO toCreateBOFromUpdateCmd(UserUpdateCmd cmd);

	// ========== BO → DO（应用层 → 领域层） ==========

	/**
	 * UserCreateBO -> UserDO（新建场景）
	 *
	 * <p>忽略由系统自动管理的字段。</p>
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "avatar", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	UserDO toDomainFromCreateBO(UserCreateBO bo);

	/**
	 * UserCreateBO -> 更新已有 UserDO（更新场景，仅覆盖可编辑字段）
	 *
	 * <p>使用 @MappingTarget 将 BO 字段覆盖到已有的 DO 对象上。</p>
	 * <p>id、username、password、avatar、status、roleIds、createTime、updateTime 保持不变。</p>
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "avatar", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "roleIds", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "updateTime", ignore = true)
	void updateDomainFromBO(UserCreateBO bo, @MappingTarget UserDO domain);
}
```

---

## 2. 转换方法命名规范

| 转换方向 | 方法命名 | 示例 | 使用层 |
|---------|---------|------|-------|
| PO → DO | `toDomain` | `toDomain(UserPO po)` | DomainService |
| DO → PO | `toPO` | `toPO(UserDO domain)` | DomainService |
| DO → BO | `toUserBO` | `toUserBO(UserDO domain)` | AppService |
| BO → DTO | `toDTOFromBO` | `toDTOFromBO(UserBO bo)` | Controller |
| 列表转换 | `toXxxList` | `toUserBOList(List<UserDO>)` | AppService |
| Cmd → BO | `toCreateBO` / `toLoginBO` | 按场景区分 | Controller |
| BO → DO（新建） | `toDomainFromCreateBO` | 带 ignore 的完整映射 | AppService |
| BO → DO（更新） | `updateDomainFromBO` | 使用 @MappingTarget | AppService |

---

## 3. 各层转换职责

| 层级 | 转换职责 | 转换方向 |
|------|---------|---------|
| **Controller**（适配层） | Cmd → BO / BO → DTO | `UserCreateCmd` → `UserCreateBO` / `UserBO` → `UserDTO` |
| **AppService**（应用层） | BO → DO / DO → BO | `UserCreateBO` → `UserDO` / `UserDO` → `UserBO` |
| **DomainService**（领域层） | PO ↔ DO | `UserPO` ↔ `UserDO` |

**各层通过 Spring 自动注入 Convertor，直接调用转换方法。**

---

## 4. 使用规范（强制）

1. **禁止手动 setter 转换**：所有对象间转换必须通过 Convertor 接口方法
2. **禁止手写静态工具方法**：`toXXX` / `fromXXX` 静态方法一律禁止（分页对象工厂方法 PageDO.from / PageBO.from / PageDTO.from 除外）
3. **系统字段使用 `@Mapping` 忽略**：id、createTime、updateTime 等由数据库管理的字段
4. **更新场景强制使用 `@MappingTarget`**：禁止新建 DO 对象后逐字段覆盖，必须直接更新已有 DO 的可编辑字段
5. **特殊字段用 `@Mapping` 处理**：常量赋值（`constant`）、忽略字段（`ignore`）、表达式（`expression`）

### 4.1 更新场景规范（强制）

**更新业务对象时，必须使用 `updateDomainFromBO` 方法（@MappingTarget），禁止创建新 DO 后手动覆盖。**

```java
// ✅ 正确：使用 @MappingTarget 更新已有 DO
public void updateUser(Long id, UserCreateBO bo) {
    UserDO user = userDomainService.validateUserExists(id);
    userConvertor.updateDomainFromBO(bo, user);  // MapStruct 直接覆盖可编辑字段
    userDomainService.save(user);
}

// ❌ 错误：创建新 DO 后手动设置 ID（丢失 createTime 等数据库管理字段）
public void updateUser(Long id, UserCreateBO bo) {
    UserDO user = userConvertor.toDomainFromCreateBO(bo);
    user.setId(id);  // 禁止！会丢失 createTime/updateTime
    userDomainService.save(user);
}

// ❌ 错误：手动逐字段 setter
public void updateUser(Long id, UserCreateBO bo) {
    UserDO user = userDomainService.validateUserExists(id);
    user.setNickname(bo.getNickname());  // 禁止！必须使用 MapStruct
    user.setEmail(bo.getEmail());
    userDomainService.save(user);
}
```

### 4.2 分页对象转换规范（强制）

**分页对象之间的转换必须使用工厂方法 PageDO.from / PageBO.from / PageDTO.from，禁止手动 new + setter。**

```java
// ✅ 正确：DomainService 使用 PageDO.from()
public PageDO<UserDO> findPage(long current, long size, String keyword, Integer status) {
    Page<UserPO> poPage = userRepository.findPage(query);
    return PageDO.from(poPage, userConvertor.toDomainList(poPage.getRecords()));
}

// ✅ 正确：AppService 使用 PageBO.from()
public PageBO<UserBO> pageUsers(long current, long size, String keyword, Integer status) {
    PageDO<UserDO> doPage = userDomainService.findPage(current, size, keyword, status);
    return PageBO.from(doPage, userConvertor.toUserBOList(doPage.getRecords()));
}

// ✅ 正确：Controller 使用 PageDTO.from()
public ApiResponse<PageDTO<UserDTO>> list(...) {
    PageBO<UserBO> boPage = userAppService.pageUsers(current, size, keyword, status);
    return succ(PageDTO.from(boPage, userConvertor.toDTOFromBOList(boPage.getRecords())));
}

// ❌ 错误：手动 new + setter
PageBO<UserBO> result = new PageBO<>();
result.setCurrent(doPage.getCurrent());
result.setSize(doPage.getSize());
result.setTotal(doPage.getTotal());
result.setRecords(records);
return result;
```

### 4.3 允许手动 setter 的例外场景

以下场景允许手动 setter，不属于“对象属性复制”：

1. **设置默认值**：`user.setStatus(1)`（新建时设置默认状态）
2. **设置计算属性**：`user.setPassword(BCrypt.hashpw(password))`（加密后的密码）
3. **从多源组装结果**：`result.setToken(...); result.setUserInfo(convertor.toUserBO(user))`（LoginResultBO 从多个不同来源组装）
4. **树形结构组装**：`depts.forEach(d -> d.setChildren(groupByParent.getOrDefault(...)))`（树形构建逻辑）
5. **关联数据填充**：`user.setRoleIds(roleIds)`（DO 对象从关联表补充数据）
6. **设置自增ID回写**：`user.setId(po.getId())`（保存后回写数据库生成的 ID）

---

> **交叉引用**：
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 项目结构详见 [02-module-architecture.md](./02-module-architecture.md)
> - 代码风格与命名约定详见 [04-code-style.md](./04-code-style.md)
