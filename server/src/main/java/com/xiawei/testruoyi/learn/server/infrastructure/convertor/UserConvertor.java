package com.xiawei.testruoyi.learn.server.infrastructure.convertor;

import com.xiawei.testruoyi.learn.sdk.cmd.user.LoginCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.ProfileUpdateCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.UserCreateCmd;
import com.xiawei.testruoyi.learn.sdk.cmd.user.UserUpdateCmd;
import com.xiawei.testruoyi.learn.sdk.dto.user.LoginDTO;
import com.xiawei.testruoyi.learn.sdk.dto.user.UserDTO;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginBO;
import com.xiawei.testruoyi.learn.server.application.bo.auth.LoginResultBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.UserCreateBO;
import com.xiawei.testruoyi.learn.server.application.bo.user.ProfileBO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 用户对象转换器（MapStruct）
 *
 * <p>定义在infrastructure.convertor包下，由Spring管理。</p>
 * <p>命名规范：XxxConvertor（英式拼写）。</p>
 * <p>所有对象间转换必须通过本接口，禁止手动setter转换。</p>
 */
@Mapper(componentModel = "spring")
public interface UserConvertor {

	// ========== PO → DO（基础设施层 → 领域层） ==========

	/**
	 * PO -> DO
	 */
	UserDO toDomain(UserPO po);

	/**
	 * PO列表 -> DO列表
	 */
	List<UserDO> toDomainList(List<UserPO> poList);

	/**
	 * DO -> PO
	 */
	UserPO toPO(UserDO domain);

	// ========== DO → DTO（领域层 → SDK层） ==========

	/**
	 * DO -> DTO
	 */
	UserDTO toDTO(UserDO domain);

	/**
	 * DO列表 -> DTO列表
	 */
	List<UserDTO> toDTOList(List<UserDO> domainList);

	// ========== Cmd → BO（SDK层 → 应用层） ==========

	/**
	 * LoginCmd -> LoginBO
	 */
	LoginBO toLoginBO(LoginCmd cmd);

	/**
	 * UserCreateCmd -> UserCreateBO
	 */
	UserCreateBO toCreateBO(UserCreateCmd cmd);

	/**
	 * UserUpdateCmd -> UserCreateBO（更新场景复用BO）
	 *
	 * <p>忽略username、password字段（更新时不允许修改）。</p>
	 */
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	UserCreateBO toCreateBOFromUpdateCmd(UserUpdateCmd cmd);

	// ========== DO → BO（领域层 → 应用层） ==========

	/**
	 * DO -> UserBO（查询结果）
	 */
	UserBO toUserBO(UserDO domain);

	/**
	 * DO列表 -> UserBO列表
	 */
	List<UserBO> toUserBOList(List<UserDO> domainList);

	// ========== BO → DTO（应用层 → SDK层，Controller调用） ==========

	/**
	 * UserBO -> UserDTO
	 */
	UserDTO toDTOFromBO(UserBO bo);

	/**
	 * UserBO列表 -> UserDTO列表
	 */
	List<UserDTO> toDTOFromBOList(List<UserBO> boList);

	/**
	 * LoginResultBO -> LoginDTO
	 */
	LoginDTO toDTOFromLoginBO(LoginResultBO bo);

	/**
	 * ProfileBO -> UserDTO
	 */
	UserDTO toDTOFromProfileBO(ProfileBO bo);

	// ========== Cmd → BO（SDK层 → 应用层，个人中心专用） ==========

	/**
	 * ProfileUpdateCmd -> UserCreateBO
	 *
	 * <p>仅映射nickname、email、phone字段。</p>
	 */
	@Mapping(target = "username", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "sex", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "deptId", ignore = true)
	@Mapping(target = "roleIds", ignore = true)
	@Mapping(target = "postIds", ignore = true)
	@Mapping(target = "remark", ignore = true)
	UserCreateBO toProfileUpdateBO(ProfileUpdateCmd cmd);

	// ========== DO → ProfileBO（领域层 → 应用层，个人中心专用） ==========

	/**
	 * DO -> ProfileBO（个人中心，包含roles/permissions）
	 */
	ProfileBO toProfileBO(UserDO domain);

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
	 * UserCreateBO -> 更新已有UserDO（更新场景，仅覆盖可编辑字段）
	 *
	 * <p>使用@MappingTarget将BO字段覆盖到已有的DO对象上。</p>
	 * <p>id、username、password、avatar、status、roleIds、createTime、updateTime保持不变。</p>
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
