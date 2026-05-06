import { useUserStore } from "@/pinia/stores/user"

/**
 * @name 权限组合式函数
 * @description 提供权限判断方法，用于模板中 v-if 控制元素可见性
 */
export function usePermission() {
  const userStore = useUserStore()

  /** 判断是否拥有指定权限 */
  function hasPermission(permission: string): boolean {
    return userStore.permissions.includes("*") || userStore.permissions.includes(permission)
  }

  /** 判断是否拥有任意一项权限 */
  function hasAnyPermission(permissions: string[]): boolean {
    return userStore.permissions.includes("*") || permissions.some(p => userStore.permissions.includes(p))
  }

  return { hasPermission, hasAnyPermission }
}
