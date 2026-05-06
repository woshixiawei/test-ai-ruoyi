package com.xiawei.testruoyi.learn.server.application.service;

import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuBO;
import com.xiawei.testruoyi.learn.server.application.bo.menu.MenuCreateBO;

import java.util.List;

/**
 * 菜单应用服务接口
 */
public interface MenuAppService {

	Long createMenu(MenuCreateBO bo);

	void updateMenu(Long id, MenuCreateBO bo);

	void deleteMenu(Long id);

	MenuBO getMenuById(Long id);

	List<MenuBO> listMenus();

	List<MenuBO> menuTree();
}
