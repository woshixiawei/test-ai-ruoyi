package com.xiawei.testruoyi.learn.server.config;

import cn.hutool.crypto.digest.BCrypt;
import com.xiawei.testruoyi.learn.server.infrastructure.mapper.*;
import com.xiawei.testruoyi.learn.server.infrastructure.po.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据初始化器 - 在系统首次启动时插入基础数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserMapper userMapper;
	private final RoleMapper roleMapper;
	private final MenuMapper menuMapper;
	private final DeptMapper deptMapper;
	private final PostMapper postMapper;
	private final UserRoleMapper userRoleMapper;
	private final RoleMenuMapper roleMenuMapper;
	private final DictTypeMapper dictTypeMapper;
	private final DictDataMapper dictDataMapper;
	private final ConfigMapper configMapper;
	private final DataSource dataSource;

	@Value("${app.force-reinit:false}")
	private boolean forceReinit;

	@Override
	public void run(String... args) {
		if (forceReinit) {
			log.info("[DataInitializer] 强制重新初始化模式，清理所有数据...");
			cleanAllData();
		} else if (userMapper.selectCount(null) > 0) {
			log.info("[DataInitializer] 数据库已有数据，跳过初始化");
			return;
		}

		log.info("[DataInitializer] 开始初始化基础数据...");

		// 1. 初始化部门
		initDept();

		// 2. 初始化岗位
		initPost();

		// 3. 初始化菜单
		initMenu();

		// 4. 初始化角色
		initRole();

		// 5. 初始化管理员用户
		initUser();

		// 6. 初始化用户-角色关联
		initUserRole();

		// 7. 初始化角色-菜单关联
		initRoleMenu();

		// 8. 初始化字典
		initDict();

		// 9. 初始化参数配置
		initConfig();

		log.info("[DataInitializer] 基础数据初始化完成！");
	}

	private void initDept() {
		DeptPO root = new DeptPO();
		root.setId(1L);
		root.setDeptName("总公司");
		root.setParentId(0L);
		root.setSort(1);
		root.setLeader("夏总");
		root.setPhone("13800138000");
		root.setEmail("admin@example.com");
		root.setStatus(1);
		root.setIsDeleted(0L);
		deptMapper.insert(root);

		DeptPO tech = new DeptPO();
		tech.setId(2L);
		tech.setDeptName("技术部");
		tech.setParentId(1L);
		tech.setSort(1);
		tech.setLeader("张三");
		tech.setPhone("13800138001");
		tech.setEmail("tech@example.com");
		tech.setStatus(1);
		tech.setIsDeleted(0L);
		deptMapper.insert(tech);

		DeptPO market = new DeptPO();
		market.setId(3L);
		market.setDeptName("市场部");
		market.setParentId(1L);
		market.setSort(2);
		market.setLeader("李四");
		market.setPhone("13800138002");
		market.setEmail("market@example.com");
		market.setStatus(1);
		market.setIsDeleted(0L);
		deptMapper.insert(market);

		log.info("[DataInitializer] 部门初始化完成（3条）");
	}

	private void initPost() {
		PostPO ceo = new PostPO();
		ceo.setId(1L);
		ceo.setPostCode("CEO");
		ceo.setPostName("董事长");
		ceo.setSort(1);
		ceo.setStatus(1);
		ceo.setIsDeleted(0L);
		postMapper.insert(ceo);

		PostPO manager = new PostPO();
		manager.setId(2L);
		manager.setPostCode("PM");
		manager.setPostName("项目经理");
		manager.setSort(2);
		manager.setStatus(1);
		manager.setIsDeleted(0L);
		postMapper.insert(manager);

		PostPO dev = new PostPO();
		dev.setId(3L);
		dev.setPostCode("DEV");
		dev.setPostName("开发工程师");
		dev.setSort(3);
		dev.setStatus(1);
		dev.setIsDeleted(0L);
		postMapper.insert(dev);

		log.info("[DataInitializer] 岗位初始化完成（3条）");
	}

	private void initMenu() {
		// 一级目录：系统管理
		insertMenu(1L, "系统管理", 0L, 1, "/system", "", "", 0, "Setting", 1);
		// 二级菜单
		insertMenu(2L, "用户管理", 1L, 1, "/system/user", "system/user/index", "system:user:list", 1, "User", 1);
		insertMenu(3L, "角色管理", 1L, 2, "/system/role", "system/role/index", "system:role:list", 1, "UserFilled", 1);
		insertMenu(4L, "菜单管理", 1L, 3, "/system/menu", "system/menu/index", "system:menu:list", 1, "Menu", 1);
		insertMenu(5L, "部门管理", 1L, 4, "/system/dept", "system/dept/index", "system:dept:list", 1, "OfficeBuilding", 1);
		insertMenu(6L, "岗位管理", 1L, 5, "/system/post", "system/post/index", "system:post:list", 1, "Suitcase", 1);
		insertMenu(7L, "字典管理", 1L, 6, "/system/dict", "system/dict/index", "system:dict:list", 1, "Collection", 1);
		insertMenu(8L, "参数配置", 1L, 7, "/system/config", "system/config/index", "system:config:list", 1, "Setting", 1);
		insertMenu(9L, "通知公告", 1L, 8, "/system/notice", "system/notice/index", "system:notice:list", 1, "Bell", 1);
		insertMenu(10L, "操作日志", 1L, 9, "/system/operLog", "system/operLog/index", "system:operLog:list", 1, "Document", 1);
		insertMenu(11L, "登录日志", 1L, 10, "/system/loginLog", "system/loginLog/index", "system:loginLog:list", 1, "Tickets", 1);
		insertMenu(12L, "文件管理", 1L, 11, "/system/file", "system/file/index", "system:file:list", 1, "Folder", 1);

		// 用户管理按钮权限
		insertMenu(100L, "用户新增", 2L, 1, "", "", "system:user:add", 2, "", 1);
		insertMenu(101L, "用户修改", 2L, 2, "", "", "system:user:edit", 2, "", 1);
		insertMenu(102L, "用户删除", 2L, 3, "", "", "system:user:remove", 2, "", 1);
		insertMenu(103L, "重置密码", 2L, 4, "", "", "system:user:resetPwd", 2, "", 1);
		insertMenu(104L, "修改状态", 2L, 5, "", "", "system:user:changeStatus", 2, "", 1);

		// 角色管理按钮权限
		insertMenu(110L, "角色新增", 3L, 1, "", "", "system:role:add", 2, "", 1);
		insertMenu(111L, "角色修改", 3L, 2, "", "", "system:role:edit", 2, "", 1);
		insertMenu(112L, "角色删除", 3L, 3, "", "", "system:role:remove", 2, "", 1);

		// 菜单管理按钮权限
		insertMenu(120L, "菜单新增", 4L, 1, "", "", "system:menu:add", 2, "", 1);
		insertMenu(121L, "菜单修改", 4L, 2, "", "", "system:menu:edit", 2, "", 1);
		insertMenu(122L, "菜单删除", 4L, 3, "", "", "system:menu:remove", 2, "", 1);

		// 部门管理按钮权限
		insertMenu(130L, "部门新增", 5L, 1, "", "", "system:dept:add", 2, "", 1);
		insertMenu(131L, "部门修改", 5L, 2, "", "", "system:dept:edit", 2, "", 1);
		insertMenu(132L, "部门删除", 5L, 3, "", "", "system:dept:remove", 2, "", 1);

		// 岗位管理按钮权限
		insertMenu(140L, "岗位新增", 6L, 1, "", "", "system:post:add", 2, "", 1);
		insertMenu(141L, "岗位修改", 6L, 2, "", "", "system:post:edit", 2, "", 1);
		insertMenu(142L, "岗位删除", 6L, 3, "", "", "system:post:remove", 2, "", 1);

		// 字典管理按钮权限
		insertMenu(150L, "字典新增", 7L, 1, "", "", "system:dict:add", 2, "", 1);
		insertMenu(151L, "字典修改", 7L, 2, "", "", "system:dict:edit", 2, "", 1);
		insertMenu(152L, "字典删除", 7L, 3, "", "", "system:dict:remove", 2, "", 1);

		// 参数配置按钮权限
		insertMenu(160L, "参数新增", 8L, 1, "", "", "system:config:add", 2, "", 1);
		insertMenu(161L, "参数修改", 8L, 2, "", "", "system:config:edit", 2, "", 1);
		insertMenu(162L, "参数删除", 8L, 3, "", "", "system:config:remove", 2, "", 1);

		// 通知公告按钮权限
		insertMenu(170L, "公告新增", 9L, 1, "", "", "system:notice:add", 2, "", 1);
		insertMenu(171L, "公告修改", 9L, 2, "", "", "system:notice:edit", 2, "", 1);
		insertMenu(172L, "公告删除", 9L, 3, "", "", "system:notice:remove", 2, "", 1);
		insertMenu(173L, "公告发布", 9L, 4, "", "", "system:notice:publish", 2, "", 1);
		insertMenu(174L, "公告撤回", 9L, 5, "", "", "system:notice:withdraw", 2, "", 1);

		// 文件管理按钮权限
		insertMenu(180L, "文件上传", 12L, 1, "", "", "system:file:upload", 2, "", 1);
		insertMenu(181L, "文件删除", 12L, 2, "", "", "system:file:remove", 2, "", 1);

		// 操作日志按钮权限
		insertMenu(190L, "日志清空", 10L, 1, "", "", "system:operLog:clean", 2, "", 1);

		// 登录日志按钮权限
		insertMenu(200L, "日志清空", 11L, 1, "", "", "system:loginLog:clean", 2, "", 1);

		log.info("[DataInitializer] 菜单初始化完成（{}条）", 12 + 5 + 3 + 3 + 3 + 3 + 3 + 3 + 5 + 2 + 1 + 1);
	}

	private void insertMenu(Long id, String name, Long parentId, int sort, String path,
			String component, String permission, int menuType, String icon, int status) {
		MenuPO menu = new MenuPO();
		menu.setId(id);
		menu.setMenuName(name);
		menu.setParentId(parentId);
		menu.setSort(sort);
		menu.setPath(path);
		menu.setComponent(component);
		menu.setPermission(permission);
		menu.setMenuType(menuType);
		menu.setIcon(icon);
		menu.setStatus(status);
		menu.setIsDeleted(0L);
		menuMapper.insert(menu);
	}

	private void initRole() {
		RolePO admin = new RolePO();
		admin.setId(1L);
		admin.setRoleName("超级管理员");
		admin.setRoleCode("admin");
		admin.setSort(1);
		admin.setStatus(1);
		admin.setRemark("超级管理员，拥有所有权限");
		admin.setIsDeleted(0L);
		roleMapper.insert(admin);

		RolePO common = new RolePO();
		common.setId(2L);
		common.setRoleName("普通角色");
		common.setRoleCode("common");
		common.setSort(2);
		common.setStatus(1);
		common.setRemark("普通角色，拥有基础权限");
		common.setIsDeleted(0L);
		roleMapper.insert(common);

		log.info("[DataInitializer] 角色初始化完成（2条）");
	}

	private void initUser() {
		UserPO admin = new UserPO();
		admin.setId(1L);
		admin.setUsername("admin");
		admin.setNickname("超级管理员");
		admin.setEmail("admin@example.com");
		admin.setPhone("13800138000");
		admin.setAvatar("");
		admin.setPassword(BCrypt.hashpw("admin123"));
		admin.setStatus(1);
		admin.setDeptId(1L);
		admin.setPostId(1L);
		admin.setRemark("超级管理员账号");
		admin.setIsDeleted(0L);
		userMapper.insert(admin);

		UserPO user = new UserPO();
		user.setId(2L);
		user.setUsername("zhangsan");
		user.setNickname("张三");
		user.setEmail("zhangsan@example.com");
		user.setPhone("13800138001");
		user.setAvatar("");
		user.setPassword(BCrypt.hashpw("123456"));
		user.setStatus(1);
		user.setDeptId(2L);
		user.setPostId(3L);
		user.setRemark("技术部开发人员");
		user.setIsDeleted(0L);
		userMapper.insert(user);

		log.info("[DataInitializer] 用户初始化完成（2条），admin密码: admin123，zhangsan密码: 123456");
	}

	private void initUserRole() {
		UserRolePO ur1 = new UserRolePO();
		ur1.setUserId(1L);
		ur1.setRoleId(1L);
		userRoleMapper.insert(ur1);

		UserRolePO ur2 = new UserRolePO();
		ur2.setUserId(2L);
		ur2.setRoleId(2L);
		userRoleMapper.insert(ur2);

		log.info("[DataInitializer] 用户-角色关联初始化完成（2条）");
	}

	private void initRoleMenu() {
		// admin角色拥有所有菜单权限
		List<Long> allMenuIds = List.of(
				1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L,
				100L, 101L, 102L, 103L, 104L, 110L, 111L, 112L, 120L, 121L, 122L,
				130L, 131L, 132L, 140L, 141L, 142L, 150L, 151L, 152L,
				160L, 161L, 162L, 170L, 171L, 172L, 173L, 174L,
				180L, 181L, 190L, 200L);
		for (Long menuId : allMenuIds) {
			RoleMenuPO rm = new RoleMenuPO();
			rm.setRoleId(1L);
			rm.setMenuId(menuId);
			roleMenuMapper.insert(rm);
		}

		// common角色拥有基础查看权限
		List<Long> commonMenuIds = List.of(1L, 2L, 3L, 5L, 9L, 10L, 11L, 12L);
		for (Long menuId : commonMenuIds) {
			RoleMenuPO rm = new RoleMenuPO();
			rm.setRoleId(2L);
			rm.setMenuId(menuId);
			roleMenuMapper.insert(rm);
		}

		log.info("[DataInitializer] 角色-菜单关联初始化完成");
	}

	private void initDict() {
		// 字典类型：用户状态
		DictTypePO statusType = new DictTypePO();
		statusType.setId(1L);
		statusType.setDictName("用户状态");
		statusType.setDictType("sys_user_status");
		statusType.setStatus(1);
		statusType.setRemark("用户状态列表");
		statusType.setIsDeleted(0L);
		dictTypeMapper.insert(statusType);

		insertDictData(1L, 1L, "正常", "1", 1, 1);
		insertDictData(2L, 1L, "停用", "0", 2, 1);

		// 字典类型：性别
		DictTypePO sexType = new DictTypePO();
		sexType.setId(2L);
		sexType.setDictName("性别");
		sexType.setDictType("sys_user_sex");
		sexType.setStatus(1);
		sexType.setRemark("性别列表");
		sexType.setIsDeleted(0L);
		dictTypeMapper.insert(sexType);

		insertDictData(3L, 2L, "男", "1", 1, 1);
		insertDictData(4L, 2L, "女", "2", 2, 1);
		insertDictData(5L, 2L, "未知", "0", 3, 1);

		// 字典类型：通知类型
		DictTypePO noticeType = new DictTypePO();
		noticeType.setId(3L);
		noticeType.setDictName("通知类型");
		noticeType.setDictType("sys_notice_type");
		noticeType.setStatus(1);
		noticeType.setRemark("通知类型列表");
		noticeType.setIsDeleted(0L);
		dictTypeMapper.insert(noticeType);

		insertDictData(6L, 3L, "通知", "1", 1, 1);
		insertDictData(7L, 3L, "公告", "2", 2, 1);

		log.info("[DataInitializer] 字典初始化完成（3个类型，7条数据）");
	}

	private void insertDictData(Long id, Long typeId, String label, String value, int sort, int status) {
		DictDataPO data = new DictDataPO();
		data.setId(id);
		data.setDictTypeId(typeId);
		data.setDictLabel(label);
		data.setDictValue(value);
		data.setSort(sort);
		data.setStatus(status);
		data.setIsDeleted(0L);
		dictDataMapper.insert(data);
	}

	private void initConfig() {
		insertConfig(1L, "用户初始密码", "sys.user.initPassword", "123456", 1, "用户创建时的初始密码");
		insertConfig(2L, "账号自助-是否开启用户注册", "sys.account.registerUser", "false", 1, "是否开启注册用户功能");
		insertConfig(3L, "重置密码是否强制修改", "sys.user.forceResetPassword", "false", 1, "重置密码后首次登录是否强制修改密码");

		log.info("[DataInitializer] 参数配置初始化完成（3条）");
	}

	private void insertConfig(Long id, String name, String key, String value, int isSystem, String remark) {
		ConfigPO config = new ConfigPO();
		config.setId(id);
		config.setConfigName(name);
		config.setConfigKey(key);
		config.setConfigValue(value);
		config.setIsSystem(isSystem);
		config.setRemark(remark);
		config.setIsDeleted(0L);
		configMapper.insert(config);
	}

	/**
	 * 清理所有表中的数据（包括软删除数据），用于强制重新初始化
	 */
	private void cleanAllData() {
		String[] tables = {
				"sys_role_menu", "sys_user_role", "sys_oper_log", "sys_login_log",
				"sys_file", "sys_notice", "sys_config", "sys_dict_data", "sys_dict_type",
				"sys_post", "sys_menu", "sys_dept", "sys_role", "sys_user"
		};
		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute("SET FOREIGN_KEY_CHECKS=0");
			for (String table : tables) {
				int rows = stmt.executeUpdate("DELETE FROM " + table);
				if (rows > 0) {
					log.info("[DataInitializer] 清理表 {} 中 {} 条数据", table, rows);
				}
			}
			stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			log.info("[DataInitializer] 全部数据清理完成");
		} catch (Exception e) {
			log.warn("[DataInitializer] 清理数据时出现异常: {}", e.getMessage());
		}
	}
}
