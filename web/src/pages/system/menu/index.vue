<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createMenuApi, deleteMenuApi, getMenuListApi, updateMenuApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Menu" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.MenuDTO[]>([])

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.MenuCmd>({ menuName: "", parentId: 0, sort: 0, path: "", component: "", permission: "", menuType: 1, icon: "", status: 1 })
const formRules = { menuName: [{ required: true, message: "请输入菜单名称", trigger: "blur" }], menuType: [{ required: true, message: "请选择菜单类型", trigger: "change" }] }
const menuTypeOptions = [{ label: "目录", value: 0 }, { label: "菜单", value: 1 }, { label: "按钮", value: 2 }]

function getList() {
  loading.value = true
  getMenuListApi().then(({ data }) => { tableData.value = data }).finally(() => { loading.value = false })
}

function handleAdd(parentId?: number) {
  dialogTitle.value = "新增菜单"
  Object.assign(form, { id: undefined, menuName: "", parentId: parentId || 0, sort: 0, path: "", component: "", permission: "", menuType: 1, icon: "", status: 1 })
  dialogVisible.value = true
}

function handleEdit(row: Business.MenuDTO) {
  dialogTitle.value = "编辑菜单"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateMenuApi(form) : createMenuApi(form)
    apiCall.then(() => { ElMessage.success(form.id ? "修改成功" : "新增成功"); dialogVisible.value = false; getList() })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该菜单？", "提示", { type: "warning" }).then(() => {
    deleteMenuApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:menu:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd()">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column prop="menuName" label="菜单名称" />
      <el-table-column prop="icon" label="图标" width="80" />
      <el-table-column prop="menuType" label="类型" width="80">
        <template #default="{ row }">
          <el-tag :type="row.menuType === 0 ? 'primary' : row.menuType === 1 ? 'success' : 'warning'">{{ menuTypeOptions.find(o => o.value === row.menuType)?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="permission" label="权限标识" />
      <el-table-column prop="path" label="路由地址" />
      <el-table-column prop="component" label="组件路径" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.menuType !== 2 && hasPermission('system:menu:add')" link type="primary" @click="handleAdd(row.id)">新增</el-button>
          <el-button v-if="hasPermission('system:menu:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:menu:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单">
          <el-tree-select v-model="form.parentId" :data="tableData" :props="{ label: 'menuName', children: 'children' }" node-key="id" check-strictly :render-after-expand="false" placeholder="顶级菜单" clearable />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType"><el-radio v-for="item in menuTypeOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName"><el-input v-model="form.menuName" /></el-form-item>
        <el-form-item v-if="form.menuType !== 2" label="路由地址"><el-input v-model="form.path" /></el-form-item>
        <el-form-item v-if="form.menuType === 1" label="组件路径"><el-input v-model="form.component" /></el-form-item>
        <el-form-item v-if="form.menuType === 2" label="权限标识"><el-input v-model="form.permission" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
