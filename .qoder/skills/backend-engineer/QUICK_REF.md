# 后端编码规范速查表（AI编码强制遵守）

> **本文件为AI编码时的强制速查参考，提取自完整编码规范文档。**
> **AI在编码前必须阅读本文件，编码过程中必须遵守所有规则。**
> 完整规范详见 `.qoder/skills/backend-engineer/guides/` 目录。

---

## 1. 架构规则（违反即拒绝）

| 规则 | 正确 | 禁止 |
|------|------|------|
| 模块架构 | base / sdk / server 三模块 | 自创模块 |
| 四层架构 | adapter → application → domain → infrastructure | 跨层调用、跳层调用 |
| 领域分包 | domain/user/, domain/config/, domain/log/ 等 | 所有DO放在一个包 |
| DO包名 | `domain/user/do_/`（注意do_下划线） | `domain/user/do/` 或 `domain/user/entity/` |
| BO包名 | `domain/user/bo/` | application/bo/ |
| Repository接口 | `domain/user/repository/` | infrastructure/repository/ |
| Repository实现 | `infrastructure/repository/impl/` | domain/user/repository/impl/ |
| Convertor | `infrastructure/convertor/` | adapter/convertor/ |
| Controller继承 | `extends BaseController` | 不继承 |
| 统一响应 | `ApiResponse<T>` + `succ()` / `fail()` | 自定义Result/R类 |
| API路径 | `/backend/system/{module}/...` | 其他前缀 |

## 2. 对象类型与命名（违反即拒绝）

| 用途 | 类型后缀 | 所在模块 | 示例 |
|------|---------|---------|------|
| Controller入参 | `Cmd` | sdk | `UserCreateCmd`, `LoginCmd` |
| Controller出参 | `DTO` | sdk | `UserDTO`, `LoginDTO` |
| AppService入参(创建) | `CreateBO` | server | `UserCreateBO`, `DeptCreateBO` |
| AppService出参 | `BO` | server | `UserBO`, `DeptBO` |
| 领域层数据对象 | `DO` | server | `UserDO`, `DeptDO` |
| 基础设施层持久化 | `PO` | server | `UserPO`, `DeptPO` |
| 分页请求 | `PageBO<T>` | base | AppService返回,Controller转换 |
| 分页响应 | `PageDTO<T>` | base | Controller返回给前端 |
| 查询参数 | `Query` | server | `UserQuery` (infrastructure/query/) |

## 3. MapStruct对象映射（违反即拒绝）

**【强制】所有对象间转换必须通过MapStruct Convertor，严禁手动setter。**

### 3.1 新建场景
```java
// ✅ 正确
UserDO user = userConvertor.toDomainFromCreateBO(bo);

// ❌ 禁止：手动new + setter
UserDO user = new UserDO();
user.setUsername(bo.getUsername());
```

### 3.2 更新场景（最常违反！）
```java
// ✅ 正确：使用@MappingTarget更新已有DO
UserDO user = userDomainService.validateUserExists(id);
userConvertor.updateDomainFromBO(bo, user);

// ❌ 禁止：创建新DO后setId（丢失createTime等数据库字段）
UserDO user = userConvertor.toDomainFromCreateBO(bo);
user.setId(id);

// ❌ 禁止：手动逐字段setter
user.setNickname(bo.getNickname());
user.setEmail(bo.getEmail());
```

### 3.3 分页对象转换
```java
// ✅ 正确：使用工厂方法
// DomainService中
return PageDO.from(poPage, convertor.toDomainList(poPage.getRecords()));
// AppService中
return PageBO.from(doPage, convertor.toBOList(doPage.getRecords()));
// Controller中
return succ(PageDTO.from(boPage, convertor.toDTOFromBOList(boPage.getRecords())));

// ❌ 禁止：手动new + setter
PageBO<UserBO> result = new PageBO<>();
result.setCurrent(doPage.getCurrent());
result.setSize(doPage.getSize());
result.setTotal(doPage.getTotal());
result.setRecords(records);
```

### 3.4 允许手动setter的例外（仅以下6种）
1. 设置默认值：`user.setStatus(1)`
2. 设置计算属性：`user.setPassword(BCrypt.hashpw(password))`
3. 多源组装结果：`result.setToken(...); result.setUserInfo(convertor.toUserBO(user))`
4. 树形结构组装：`depts.forEach(d -> d.setChildren(groupByParent.getOrDefault(...)))`
5. 关联数据填充：`user.setRoleIds(roleIds)`
6. 自增ID回写：`user.setId(po.getId())`

## 4. 参数校验（违反即拒绝）

**验证顺序：AppService先格式校验 → DomainService格式校验+业务规则校验**

| 校验类型 | 执行层 | 异常类型 | 示例 |
|---------|-------|---------|------|
| 参数格式校验 | AppService + DomainService | **ParamException(50001)** | 非空、长度、范围、枚举值 |
| 业务规则校验 | DomainService | **BusinessException(50002)** | 唯一性、存在性、权限不足 |

### 4.1 AppService校验模板
```java
@Override
@Transactional(rollbackFor = Exception.class)
public Long createUser(UserCreateBO bo) {
    // 1. 应用层参数校验（基本格式）
    validateCreate(bo);
    // 2. 领域层校验（格式+业务规则）
    userDomainService.validateUsernameNotExists(bo.getUsername());
    // 3. 业务逻辑
    UserDO user = userConvertor.toDomainFromCreateBO(bo);
    user.setStatus(1);
    user.setPassword(BCrypt.hashpw(bo.getPassword()));
    userDomainService.save(user);
    return user.getId();
}

private void validateCreate(UserCreateBO bo) {
    if (bo == null) { throw new ParamException("创建参数不能为空"); }
    if (bo.getUsername() == null || bo.getUsername().isBlank()) { throw new ParamException("用户名不能为空"); }
}
```

### 4.2 DomainService校验模板
```java
public UserDO validateUserExists(Long userId) {
    // 格式校验 → ParamException
    if (userId == null || userId <= 0) { throw new ParamException("用户ID不合法"); }
    // 业务规则校验 → BusinessException
    UserPO po = userRepository.getById(userId);
    if (po == null) { throw new BusinessException("用户不存在"); }
    return toDO(po);
}
```

### 4.3 禁止规则
- **禁止在AppService抛BusinessException**（业务规则校验属于领域层）
- **禁止在领域层省略参数格式校验**（与应用层双重保障）
- 校验方法封装为`private validateXxx()`，先校验再执行业务逻辑

## 5. 日期时间类型（违反即拒绝）

```java
// ✅ 正确：使用java.util.Date
import java.util.Date;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;

/** 创建时间 */
@JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN)
private Date createTime;

// ❌ 禁止：使用LocalDateTime
private LocalDateTime createTime;  // 禁止！

// ❌ 禁止：Date字段缺少@JsonFormat
private Date createTime;  // 缺少注解，禁止！
```

### 各类型注解要求
| 对象类型 | Date类型 | @JsonFormat | @DateTimeFormat |
|---------|----------|-------------|-----------------|
| PO/DO/BO | ✅ | ✅ 强制 | ❌ 不需要 |
| DTO/Cmd（sdk模块） | ✅ | ✅ 强制 | ✅ 强制（双注解） |

## 6. 软删除（违反即拒绝）

```java
// PO实体注解
@TableLogic(value = "0", delval = "id")
private Long isDeleted;
```

- 字段类型：`bigint(20)`，`0`=未删除，删除时`is_deleted = id`（记录ID值）
- 唯一索引：`UNIQUE KEY uk_xxx_deleted(业务字段, is_deleted)` 联合唯一索引
- 禁止在BaseRepositoryImpl覆写removeById()，MyBatis-Plus原生支持

## 7. 分页模式（违反即拒绝）

```
Controller: @RequestParam current/size → AppService → DomainService
                                                          ↓
                                                     Page<UserPO> (MyBatis-Plus)
                                                          ↓ PageDO.from()
                                                     PageDO<UserDO>
                                                          ↓ PageBO.from()
Controller ← PageBO<UserBO> ← AppService
     ↓ PageDTO.from()
ApiResponse<PageDTO<UserDTO>>
```

- Controller：`@RequestParam(defaultValue = "1") long current`
- AppService：返回`PageBO<XxxBO>`
- DomainService：返回`PageDO<XxxDO>`
- 禁止Controller直接操作DomainService

## 8. 密码加密

```java
// ✅ 正确：BCrypt加密
import cn.hutool.crypto.digest.BCrypt;

user.setPassword(BCrypt.hashpw(plainPassword));  // 加密
BCrypt.checkpw(plainPassword, user.getPassword())  // 验证
```

## 9. 领域间交互

- **AppService可直接调用所有领域的DomainService**（共享层级架构）
- 禁止DomainService直接调用其他领域的DomainService
- 禁止Controller直接调用DomainService
- `infrastructure/remote/` 仅用于外部第三方系统调用

## 10. 完整AppServiceImpl代码模板

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class XxxAppServiceImpl implements XxxAppService {

    private final XxxDomainService xxxDomainService;
    private final XxxConvertor xxxConvertor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createXxx(XxxCreateBO bo) {
        validateCreate(bo);
        xxxDomainService.validateXxxCodeNotExists(bo.getXxxCode(), null);
        XxxDO xxx = xxxConvertor.toDomainFromCreateBO(bo);
        if (xxx.getStatus() == null) { xxx.setStatus(1); }
        xxxDomainService.save(xxx);
        log.info("[XxxAppService] 创建成功, id={}", xxx.getId());
        return xxx.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateXxx(Long id, XxxCreateBO bo) {
        validateUpdate(id, bo);
        XxxDO xxx = xxxDomainService.validateXxxExists(id);
        xxxConvertor.updateDomainFromBO(bo, xxx);  // MapStruct @MappingTarget
        xxxDomainService.save(xxx);
        log.info("[XxxAppService] 更新成功, id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteXxx(Long id) {
        if (id == null || id <= 0) { throw new ParamException("ID不合法"); }
        xxxDomainService.validateXxxExists(id);
        xxxDomainService.deleteById(id);
        log.info("[XxxAppService] 删除成功, id={}", id);
    }

    @Override
    public XxxBO getXxxById(Long id) {
        if (id == null || id <= 0) { throw new ParamException("ID不合法"); }
        return xxxConvertor.toBO(xxxDomainService.getById(id));
    }

    @Override
    public PageBO<XxxBO> pageXxxs(long current, long size) {
        PageDO<XxxDO> doPage = xxxDomainService.findPage(current, size);
        return PageBO.from(doPage, xxxConvertor.toBOList(doPage.getRecords()));
    }

    private void validateCreate(XxxCreateBO bo) {
        if (bo == null) { throw new ParamException("创建参数不能为空"); }
        // 按业务需要添加格式校验
    }

    private void validateUpdate(Long id, XxxCreateBO bo) {
        if (id == null || id <= 0) { throw new ParamException("ID不合法"); }
        validateCreate(bo);
    }
}
```
