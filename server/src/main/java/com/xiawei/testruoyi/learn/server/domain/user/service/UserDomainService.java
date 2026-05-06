package com.xiawei.testruoyi.learn.server.domain.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiawei.testruoyi.learn.base.exceptions.BusinessException;
import com.xiawei.testruoyi.learn.base.exceptions.ParamException;
import com.xiawei.testruoyi.learn.base.model.PageDO;
import com.xiawei.testruoyi.learn.server.domain.user.do_.UserDO;
import com.xiawei.testruoyi.learn.server.domain.user.repository.UserRepository;
import com.xiawei.testruoyi.learn.server.infrastructure.convertor.UserConvertor;
import com.xiawei.testruoyi.learn.server.infrastructure.po.UserPO;
import com.xiawei.testruoyi.learn.server.infrastructure.query.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户领域服务
 *
 * <p>封装用户相关的核心业务规则。</p>
 * <p>查询：通过 UserRepository 获取 PO，再通过 Convertor 转换为 DO。</p>
 * <p>增删改：通过 UserRepository（IService）的 save/removeById 等方法实现。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService {

	private final UserRepository userRepository;
	private final UserConvertor userConvertor;

	/**
	 * 校验用户名是否已存在（业务规则校验）
	 */
	public void validateUsernameNotExists(String username) {
		if (username == null || username.isBlank()) {
			throw new ParamException("用户名不能为空");
		}
		userRepository.findByUsername(username).ifPresent(po -> {
			throw new BusinessException("用户名已存在");
		});
	}

	/**
	 * 校验用户是否存在，存在则返回 DO（数据存在性校验）
	 */
	public UserDO validateUserExists(Long userId) {
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		UserPO po = userRepository.getById(userId);
		if (po == null) {
			throw new BusinessException("用户不存在");
		}
		return toDO(po);
	}

	/**
	 * 校验用户非超级管理员（业务规则校验）
	 */
	public void validateNotSuperAdmin(Long userId) {
		if (userId != null && userId == 1L) {
			throw new BusinessException("超级管理员不可操作");
		}
	}

	/**
	 * 根据用户名查询用户（返回 DO）
	 */
	public UserDO findByUsername(String username) {
		UserPO po = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessException("用户名或密码错误"));
		return toDO(po);
	}

	/**
	 * 保存用户（通过 Repository）
	 *
	 * <p>接收 DO，转换为 PO 后调用 Repository 的 saveOrUpdate 方法。</p>
	 */
	public void save(UserDO user) {
		UserPO po = userConvertor.toPO(user);
		userRepository.saveOrUpdate(po);
		user.setId(po.getId());
	}

	/**
	 * 根据ID查询用户（返回 DO）
	 */
	public UserDO getById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		UserPO po = userRepository.getById(id);
		if (po == null) {
			throw new BusinessException("用户不存在");
		}
		return toDO(po);
	}

	/**
	 * 分页查询用户（返回 PageDO<UserDO>）
	 */
	public PageDO<UserDO> findPage(long current, long size, String keyword, Integer status) {
		UserQuery query = new UserQuery();
		query.setCurrent(current);
		query.setSize(size);
		query.setKeyword(keyword);
		query.setStatus(status);

		Page<UserPO> poPage = userRepository.findPage(query);
		return PageDO.from(poPage, userConvertor.toDomainList(poPage.getRecords()));
	}

	/**
	 * 删除用户（通过 Repository）
	 */
	public void deleteById(Long id) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		userRepository.removeById(id);
	}

	/**
	 * 更新用户状态（通过 Repository）
	 */
	public void updateStatus(Long id, Integer status) {
		if (id == null || id <= 0) {
			throw new ParamException("用户ID不合法");
		}
		if (status == null || (status != 0 && status != 1)) {
			throw new ParamException("状态值不合法");
		}
		UserPO po = new UserPO();
		po.setId(id);
		po.setStatus(status);
		userRepository.updateById(po);
	}

	/**
	 * 分配角色（关联表操作，走 Repository）
	 */
	public void assignRoles(Long userId, List<Long> roleIds) {
		if (userId == null || userId <= 0) {
			throw new ParamException("用户ID不合法");
		}
		userRepository.assignRoles(userId, roleIds);
	}

	/**
	 * PO → DO 转换（含角色ID查询）
	 */
	private UserDO toDO(UserPO po) {
		UserDO user = userConvertor.toDomain(po);
		List<Long> roleIds = userRepository.findRoleIdsByUserId(po.getId());
		user.setRoleIds(roleIds);
		return user;
	}
}
