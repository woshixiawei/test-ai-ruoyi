<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { assignRoleMenusApi, createRoleApi, deleteRoleApi, getMenuTreeApi, getRoleByIdApi, getRoleListApi, getRoleMenusApi, updateRoleApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Role" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.RoleDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })
const searchForm = reactive({ keyword: "", status: undefined as number | undefined })

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.RoleCmd>({ roleName: "", roleCode: "", sort: 0, status: 1, remark: "" })
const formRules = {
  roleName: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
  roleCode: [{ required: true, message: "请输入角色编码", trigger: "blur" }]
}

// 权限分配
const permDialogVisible = ref(false)
const currentRoleId = ref(0)
const menuTree = ref<Business.MenuDTO[]>([])
const menuTreeRef = ref()
const checkedKeys = ref<number[]>([])

function getList() {
  loading.value = true
  getRoleListApi({ current: page.current, size: page.size, keyword: searchForm.keyword || undefined, status: searchForm.status }).then(({ data }) => {
    tableData.value = data.records
    total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleSearch() { page.current = 1; getList() }
function handleReset() { searchForm.keyword = ""; searchForm.status = undefined; handleSearch() }

function handleAdd() {
  dialogTitle.value = "新增角色"
  Object.assign(form, { id: undefined, roleName: "", roleCode: "", sort: 0, status: 1, remark: "" })
  dialogVisible.value = true
}

function handleEdit(row: Business.RoleDTO) {
  dialogTitle.value = "编辑角色"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateRoleApi(form) : createRoleApi(form)
    apiCall.then(() => {
      ElMessage.success(form.id ? "修改成功" : "新增成功")
      dialogVisible.value = false
      getList()
    })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该角色？", "提示", { type: "warning" }).then(() => {
    deleteRoleApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

function handleAssignMenus(row: Business.RoleDTO) {
  currentRoleId.value = row.id
  getMenuTreeApi().then(({ data }) => { menuTree.value = data })
  getRoleMenusApi(row.id).then(({ data }) => { checkedKeys.value = data })
  permDialogVisible.value = true
}

function handleAssignSubmit() {
  const keys = menuTreeRef.value?.getCheckedKeys() || []
  const halfKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
  assignRoleMenusApi(currentRoleId.value, [...keys, ...halfKeys]).then(() => {
    ElMessage.success("权限分配成功")
    permDialogVisible.value = false
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="关键字"><el-input v-model="searchForm.keyword" placeholder="角色名称/编码" clearable @keyup.enter="handleSearch" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="全部" clearable>
          <el-option label="正常" :value="1" /><el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="handleSearch">搜索</el-button><el-button @click="handleReset">重置</el-button></el-form-item>
    </el-form>
    <el-row v-if="hasPermission('system:role:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:role:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:role:edit')" link type="warning" @click="handleAssignMenus(row)">分配权限</el-button>
          <el-button v-if="hasPermission('system:role:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName"><el-input v-model="form.roleName" /></el-form-item>
        <el-form-item label="角色编码" prop="roleCode"><el-input v-model="form.roleCode" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="permDialogVisible" title="分配菜单权限" width="500px">
      <el-tree ref="menuTreeRef" :data="menuTree" :props="{ label: 'menuName', children: 'children' }" node-key="id" show-checkbox :default-checked-keys="checkedKeys" check-strictly />
      <template #footer><el-button @click="permDialogVisible = false">取消</el-button><el-button type="primary" @click="handleAssignSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
