# 后端代码风格约定

- 版本号: v3.4
- 适用范围: 所有后端模块
- 约束力: **强制**

---

> **TL;DR 核心要点**：
> 1. 缩进用 **Tab**，换行用 Unix (`\n`)，行宽 ≤120
> 2. 类名 PascalCase，方法名 camelCase，常量 UPPER_SNAKE_CASE
> 3. DO/PO/BO 字段注释用 `/** 中文注释 */`；DTO/Cmd 用 `@Schema(description="中文")`
> 4. 方法参数 ≤4个，超过必须封装为对象
> 5. 禁止 LocalDateTime，强制 java.util.Date + @JsonFormat
> 6. CMD/DTO 的 Date 字段必须加 @DateTimeFormat（双注解）
> 7. 禁止直接 new ObjectMapper()，必须用 JacksonUtil
> 8. 对象转换必须用 MapStruct Convertor，更新场景用 @MappingTarget

---

> 技术栈强制要求详见 [01-tech-stack.md](./01-tech-stack.md)
> 四层架构命名约定参见本文档第1.2节

---

## 1. 缩进与格式化
- **缩进**: 使用 **Tab 字符**
- **换行**: Unix 风格 (`\n`)
- **文件末尾**: 保留一个空行
- **最大行宽**: 120 字符
- **大括号**: K&R 风格（左大括号不换行）
```java
// 正确
public class UserService {
	public void doSomething() {
		if (condition) {
			// ...
		}
	}
}

// 错误 - Allman 风格
public class UserService
{
	public void doSomething()
	{
		// ...
	}
}
```

## 2. 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase，名词 | `UserService`, `OrderRepository` |
| 接口名 | PascalCase，形容词/名词 | `UserRepository`, `Cacheable` |
| 方法名 | camelCase，动词开头 | `getUserById()`, `createOrder()` |
| 变量名 | camelCase | `userName`, `orderList` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| 包名 | 全小写，点分隔 | `com.example.user.domain.service` |
| 布尔变量 | is/has/can 前缀 | `isActive`, `hasPermission` |
| 枚举 | PascalCase + UPPER_SNAKE_CASE 成员 | `OrderStatus.PENDING` |
| 泛型参数 | 单个大写字母 | `T`, `E`, `K`, `V`, `R` |

**四层架构命名约定**:
| 层级 | 类名后缀 | 示例 |
|------|---------|------|
| Controller | `Controller` | `UserController` |
| API 接口 | `Api` / `Facade` | `UserApi` |
| AppService | `AppService` + `AppServiceImpl` | `UserAppService`, `UserAppServiceImpl` |
| DomainService | `DomainService` / `Service` | `OrderDomainService` |
| Repository | `Repository` + `RepositoryImpl` | `UserRepository`, `UserRepositoryImpl` |
| Mapper | `Mapper` | `UserMapper` |
| DO | `DO`（领域层数据对象，贫血模型） | `UserDO`, `OrderDO` |
| PO | `PO`（基础设施层持久化对象） | `UserPO`, `OrderPO` |
| Cmd | `Cmd`（Controller 入参，sdk 模块） | `UserCreateCmd`, `LoginCmd` |
| DTO | `DTO`（Controller 出参，sdk 模块） | `UserDTO`, `LoginDTO` |
| BO | `BO`（AppService 入参/出参，server 模块） | `UserCreateBO`, `UserBO`, `LoginResultBO` |
| Config | `Config` | `RedisConfig` |
| Exception | `Exception` | `BusinessException` |
| Convertor | `Convertor` / `Mapper`（MapStruct） | `UserConvertor` |

## 3. 文件头注释
```java
/**
 * 用户应用服务实现
 *
 * <p>处理用户相关的应用层业务编排，包括用户注册、登录、信息修改等用例。</p>
 *
 * @author 后端工程师
 * @since 1.0.0
 */
```

## 4. 方法注释（Javadoc 强制）
```java
/**
 * 根据用户ID查询用户信息
 *
 * @param userId 用户唯一标识，不允许为空
 * @return 用户信息，未找到返回 {@code null}
 * @throws BusinessException 当用户ID格式无效时
 */
public UserBO getUserById(Long userId) {
	// ...
}
```

## 5. 方法参数数量规范（强制）
- **单个方法的参数数量不得超过 4 个**
- 超过 4 个参数时，**必须**将参数封装为对象传递
- 禁止使用过长的参数列表

```java
// 错误：参数超过4个
public void updateUser(Long id, String name, String email, String phone, Integer age, String address) { } // ❌

// 正确：封装为对象
public void updateUser(Long id, UserUpdateBO bo) { } // ✅
```

## 6. 实体类字段注释规范（强制）

**【强制】** 所有实体类（PO、DO、DTO、BO、Cmd）的每个字段**必须**添加中文注释，说明字段的业务含义。

注释格式统一使用行内 Javadoc 风格（`/** 注释 */`），放在字段声明上方或同一行前方：

```java
// ✅ 正确：PO 字段注释示例
@Data
@TableName("sys_user")
public class UserPO {

	/** 主键 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/** 用户名，唯一 */
	private String username;

	/** 状态：0-禁用，1-正常 */
	private Integer status;

	/** 创建时间 */
	@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
	private Date createTime;
}

// ✅ 正确：DTO 字段注释示例（使用 @Schema）
@Data
@Schema(description = "用户信息")
public class UserDTO {

	@Schema(description = "用户ID")
	private Long id;

	@Schema(description = "用户名，唯一")
	private String username;

	@Schema(description = "状态：0-禁用，1-正常")
	private Integer status;
}

// ❌ 错误：字段无注释
@Data
public class UserPO {
	private Long id;       // 缺少注释
	private String username; // 缺少注释
}
```

**不同对象类型的注释要求**：
| 对象类型 | 注释方式 | 示例 |
|---------|---------|------|
| PO（持久化对象） | `/** 中文注释 */` 行内注释 | `/** 用户名，唯一 */` |
| DO（领域对象） | `/** 中文注释 */` 行内注释 | `/** 用户名，唯一 */` |
| BO（业务对象） | `/** 中文注释 */` 行内注释 | `/** 搜索关键词 */` |
| DTO（sdk模块） | `@Schema(description = "中文")` | `@Schema(description = "用户ID")` |
| Cmd（sdk模块） | `@Schema(description = "中文")` + 校验注解 message | `@NotBlank(message = "用户名不能为空")` |

## 7. 行内注释
```java
// 正确：解释"为什么"
// 数据库中存储的是分，需要转换为元展示给用户
BigDecimal amount = dbAmount.divide(new BigDecimal(100));

// 错误：重复代码本身已说明的内容
// 设置用户名为张三
String userName = "张三";
```

---

## 8. 对象映射规范

**【强制】** 所有对象间转换必须通过 MapStruct Convertor 实现，详细规范详见 [05-object-mapping.md](./05-object-mapping.md)。

核心要点：
- 禁止手动 setter 转换，必须使用 Convertor 接口方法
- 更新场景强制使用 `updateDomainFromBO`（@MappingTarget），禁止创建新 DO
- 分页对象转换使用 PageDO.from / PageBO.from / PageDTO.from 工厂方法
- Convertor 定义在 `infrastructure.convertor` 包下，命名为 `XxxConvertor`

---

## 9. 日期时间类型规范（强制）

### 9.1 时间类型强制要求

**【强制】** 项目中所有实体类、DO、BO、PO、DTO、Cmd 等类中的时间属性，**禁止使用 `LocalDateTime` 类型**，**强制统一使用 `java.util.Date` 类型**。

### 9.2 @JsonFormat 注解强制要求

**【强制】** 对于所有使用 `Date` 类型的字段，**必须**添加 `@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)` 注解，以确保 JSON 序列化格式统一。

- `DatePattern` 来自 Hutool：`cn.hutool.core.date.DatePattern`
- `DatePattern.NORM_DATE_PATTERN` = `"yyyy-MM-dd"`
- `@JsonFormat` 来自 Jackson：`com.fasterxml.jackson.annotation.JsonFormat`

```java
// ✅ 正确：PO/DO/BO 中的 Date 字段
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/** 创建时间 */
@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
private Date createTime;

/** 更新时间 */
@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
private Date updateTime;

// ❌ 错误：使用 LocalDateTime
import java.time.LocalDateTime;

private LocalDateTime createTime;  // 禁止！
private LocalDateTime updateTime;  // 禁止！

// ❌ 错误：Date 字段缺少 @JsonFormat 注解
private Date createTime;  // 缺少 @JsonFormat，禁止！
```

### 9.3 @DateTimeFormat 注解强制要求（CMD 和 DTO 类）

**【强制】** 对于 CMD 和 DTO 类中的日期时间字段，除了 `@JsonFormat` 注解外，还**必须**额外添加 `@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)` 注解，以支持 Spring MVC 请求参数的日期格式解析。

- `@DateTimeFormat` 来自 Spring：`org.springframework.format.annotation.DateTimeFormat`

```java
// ✅ 正确：DTO/Cmd 中的 Date 字段（双注解）
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/** 创建时间 */
@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)
@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
private Date createTime;

// ❌ 错误：DTO/Cmd 中只有 @JsonFormat，缺少 @DateTimeFormat
@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
private Date createTime;  // 缺少 @DateTimeFormat，禁止！
```

### 9.4 各类型注解要求汇总

| 对象类型 | Date 类型 | @JsonFormat | @DateTimeFormat |
|---------|----------|-------------|-----------------|
| PO | ✅ 强制 | ✅ 强制 | ❌ 不需要 |
| DO | ✅ 强制 | ✅ 强制 | ❌ 不需要 |
| BO | ✅ 强制 | ✅ 强制 | ❌ 不需要 |
| DTO（sdk 模块） | ✅ 强制 | ✅ 强制 | ✅ 强制 |
| Cmd（sdk 模块） | ✅ 强制 | ✅ 强制 | ✅ 强制 |

### 9.5 强制规范清单

1. **【强制】** 禁止使用 `LocalDateTime`，所有时间属性统一使用 `java.util.Date`
2. **【强制】** 所有 Date 字段必须添加 `@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)`
3. **【强制】** CMD 和 DTO 类中的 Date 字段必须额外添加 `@DateTimeFormat(pattern = DatePattern.NORM_DATE_PATTERN)`
4. **【强制】** 注解中的 pattern 必须使用 `DatePattern.NORM_DATE_PATTERN` 常量，**禁止手写格式字符串**（如 `"yyyy-MM-dd"`）
5. **【强制】** 禁止混用 `LocalDateTime`、`LocalDate`、`java.sql.Date` 等其他日期类型

---

## 10. JSON 序列化统一规范（强制）

**【强制】** 项目中所有 JSON 序列化/反序列化操作**必须**使用 base 模块的 `JacksonUtil` 工具类，**禁止直接使用 `ObjectMapper`**。

### 10.1 为什么禁用直接使用 ObjectMapper

- `JacksonUtil` 内置了统一配置（Long→String 防止前端精度丢失、忽略未知属性、取消时间戳格式等）
- 直接 `new ObjectMapper()` 会丢失这些配置，导致序列化结果不一致
- 统一入口便于全局控制 JSON 行为（如日期格式、空值处理、Long精度等）

### 10.2 JacksonUtil 位置

- 包路径：`{公司域名}.{项目名}.base.util.JacksonUtil`（base 模块）
- 所有模块均可引用

### 10.3 使用规范

```java
import {公司域名}.{项目名}.base.util.JacksonUtil;

// ✅ 正确：对象 → JSON 字符串
String json = JacksonUtil.toJsonStr(userPO);

// ✅ 正确：JSON 字符串 → 对象
UserPO user = JacksonUtil.toBean(jsonStr, UserPO.class);

// ✅ 正确：JSON 字符串 → List
List<UserPO> list = JacksonUtil.toList(jsonStr, UserPO.class);

// ✅ 正确：JSON 字符串 → Map
Map<String, Object> map = JacksonUtil.toMap(jsonStr);

// ❌ 错误：直接 new ObjectMapper()
ObjectMapper mapper = new ObjectMapper();       // 禁止！
String json = mapper.writeValueAsString(obj);     // 禁止！

// ❌ 错误：注入 ObjectMapper
private final ObjectMapper objectMapper;          // 禁止！
```

### 10.4 强制规范清单

1. **禁止 `new ObjectMapper()`**：所有模块不得自行创建 ObjectMapper 实例
2. **禁止注入 ObjectMapper**：不得通过 `@Autowired` 或构造器注入 ObjectMapper
3. **序列化统一使用 `JacksonUtil.toJsonStr()`**：替代 `objectMapper.writeValueAsString()`
4. **反序列化统一使用 `JacksonUtil.toBean()`**：替代 `objectMapper.readValue()`
5. **如需 ObjectMapper 实例**：通过 `JacksonUtil.getMapper()` 获取已配置好的实例

---

> **交叉引用**：
> - 四层架构各层详细规范详见 [03-four-layer-architecture.md](./03-four-layer-architecture.md)
> - 项目结构详见 [02-module-architecture.md](./02-module-architecture.md)
