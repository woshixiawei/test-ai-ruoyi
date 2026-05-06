import type { RouteRecordRaw } from "vue-router"
import { createRouter } from "vue-router"
import { routerConfig } from "@/router/config"
import { registerNavigationGuard } from "@/router/guard"
import { flatMultiLevelRoutes } from "./helper"

const Layouts = () => import("@/layouts/index.vue")

/**
 * @name 常驻路由
 * @description 除了 redirect/403/404/login 等隐藏页面，其他页面建议设置唯一的 Name 属性
 */
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: Layouts,
    meta: {
      hidden: true
    },
    children: [
      {
        path: ":path(.*)",
        component: () => import("@/pages/redirect/index.vue")
      }
    ]
  },
  {
    path: "/403",
    component: () => import("@/pages/error/403.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/404",
    component: () => import("@/pages/error/404.vue"),
    meta: {
      hidden: true
    },
    alias: "/:pathMatch(.*)*"
  },
  {
    path: "/login",
    component: () => import("@/pages/login/index.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/",
    component: Layouts,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        component: () => import("@/pages/dashboard/index.vue"),
        name: "Dashboard",
        meta: {
          title: "首页",
          svgIcon: "dashboard",
          affix: true
        }
      }
    ]
  },
  {
    path: "/system",
    component: Layouts,
    redirect: "/system/user",
    name: "System",
    meta: {
      title: "系统管理",
      elIcon: "Setting",
      alwaysShow: true
    },
    children: [
      {
        path: "user",
        component: () => import("@/pages/system/user/index.vue"),
        name: "User",
        meta: {
          title: "用户管理"
        }
      },
      {
        path: "role",
        component: () => import("@/pages/system/role/index.vue"),
        name: "Role",
        meta: {
          title: "角色管理"
        }
      },
      {
        path: "menu",
        component: () => import("@/pages/system/menu/index.vue"),
        name: "Menu",
        meta: {
          title: "菜单管理"
        }
      },
      {
        path: "dept",
        component: () => import("@/pages/system/dept/index.vue"),
        name: "Dept",
        meta: {
          title: "部门管理"
        }
      },
      {
        path: "post",
        component: () => import("@/pages/system/post/index.vue"),
        name: "Post",
        meta: {
          title: "岗位管理"
        }
      },
      {
        path: "dict",
        component: () => import("@/pages/system/dict/index.vue"),
        name: "Dict",
        meta: {
          title: "字典管理"
        }
      },
      {
        path: "config",
        component: () => import("@/pages/system/config/index.vue"),
        name: "Config",
        meta: {
          title: "参数配置"
        }
      },
      {
        path: "notice",
        component: () => import("@/pages/system/notice/index.vue"),
        name: "Notice",
        meta: {
          title: "通知公告"
        }
      }
    ]
  },
  {
    path: "/log",
    component: Layouts,
    redirect: "/log/operLog",
    name: "Log",
    meta: {
      title: "日志管理",
      elIcon: "Document",
      alwaysShow: true
    },
    children: [
      {
        path: "operLog",
        component: () => import("@/pages/log/operLog/index.vue"),
        name: "OperLog",
        meta: {
          title: "操作日志"
        }
      },
      {
        path: "loginLog",
        component: () => import("@/pages/log/loginLog/index.vue"),
        name: "LoginLog",
        meta: {
          title: "登录日志"
        }
      }
    ]
  },
  {
    path: "/file",
    component: Layouts,
    children: [
      {
        path: "",
        component: () => import("@/pages/file/index.vue"),
        name: "File",
        meta: {
          title: "文件管理",
          elIcon: "FolderOpened"
        }
      }
    ]
  }
]

/**
 * @name 动态路由
 * @description 用来放置有权限 (Roles 属性) 的路由
 * @description 必须带有唯一的 Name 属性
 */
export const dynamicRoutes: RouteRecordRaw[] = [
  {
    path: "/profile",
    component: Layouts,
    children: [
      {
        path: "",
        component: () => import("@/pages/profile/index.vue"),
        name: "Profile",
        meta: {
          title: "个人中心",
          svgIcon: "dashboard",
          alwaysShow: false
        }
      }
    ]
  }
]

/** 路由实例 */
export const router = createRouter({
  history: routerConfig.history,
  routes: routerConfig.thirdLevelRouteCache ? flatMultiLevelRoutes(constantRoutes) : constantRoutes
})

/** 重置路由 */
export function resetRouter() {
  try {
    // 注意：所有动态路由路由必须带有 Name 属性，否则可能会不能完全重置干净
    router.getRoutes().forEach((route) => {
      const { name, meta } = route
      if (name && meta.roles?.length) {
        router.hasRoute(name) && router.removeRoute(name)
      }
    })
  } catch {
    // 强制刷新浏览器也行，只是交互体验不是很好
    location.reload()
  }
}

// 注册路由导航守卫
registerNavigationGuard(router)
