import type { App, Directive } from "vue"
import { isArray } from "@@/utils/validate"
import { useUserStore } from "@/pinia/stores/user"

/**
 * @name 权限指令
 * @description 基于权限标识（如 system:user:add）控制元素可见性
 * @description 超级管理员拥有 "*" 权限，自动通过所有权限检查
 * @example v-permission="['system:user:add']"
 */
const permission: Directive = {
  mounted(el, binding) {
    const { value: requiredPermissions } = binding
    const { permissions } = useUserStore()
    if (isArray(requiredPermissions) && requiredPermissions.length > 0) {
      const hasAllPermission = permissions.includes("*")
      const hasPermission = hasAllPermission || requiredPermissions.some(p => permissions.includes(p))
      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error(`参数必须是一个数组且长度大于 0，参考：v-permission="['system:user:add']"`)
    }
  }
}

export function installPermissionDirective(app: App) {
  app.directive("permission", permission)
}
