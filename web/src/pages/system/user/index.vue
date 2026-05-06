<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createUserApi, deleteUserApi, getUserListApi, resetPasswordApi, updateUserApi, updateUserStatusApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "User" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.UserDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })
const searchForm = reactive({ keyword: "", status: undefined as number | undefined })

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.UserCreateCmd & { id?: number }>({
  username: "", nickname: "", password: "", email: "", phone: "",
  status: 1, deptId: undefined, postId: undefined, remark: "", roleIds: []
})
const formRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  nickname: [{ required: true, message: "请输入昵称", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
}

function getList() {
  loading.value = true
  getUserListApi({ current: page.current, size: page.size, keyword: searchForm.keyword || undefined, status: searchForm.status }).then(({ data }) => {
    tableData.value = data.records
    total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleSearch() { page.current = 1; getList() }
function handleReset() { searchForm.keyword = ""; searchForm.status = undefined; handleSearch() }
function handleCurrentChange(val: number) { page.current = val; getList() }
function handleSizeChange(val: number) { page.size = val; getList() }

function handleAdd() {
  dialogTitle.value = "新增用户"
  Object.assign(form, { id: undefined, username: "", nickname: "", password: "", email: "", phone: "", status: 1, deptId: undefined, postId: undefined, remark: "", roleIds: [] })
  dialogVisible.value = true
}

function handleEdit(row: Business.UserDTO) {
  dialogTitle.value = "编辑用户"
  Object.assign(form, { ...row, password: "" })
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateUserApi(form.id, form) : createUserApi(form)
    apiCall.then(() => {
      ElMessage.success(form.id ? "修改成功" : "新增成功")
      dialogVisible.value = false
      getList()
    })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该用户？", "提示", { type: "warning" }).then(() => {
    deleteUserApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

function handleStatusChange(row: Business.UserDTO) {
  updateUserStatusApi(row.id, { status: row.status }).then(() => {
    ElMessage.success("状态修改成功")
  })
}

function handleResetPassword(row: Business.UserDTO) {
  ElMessageBox.prompt("请输入新密码", "重置密码", { confirmButtonText: "确定", inputPattern: /.+/, inputErrorMessage: "密码不能为空" }).then(({ value }) => {
    resetPasswordApi(row.id, { newPassword: value }).then(() => { ElMessage.success("密码重置成功") })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-form :inline="true" class="search-form">
      <el-form-item label="关键字">
        <el-input v-model="searchForm.keyword" placeholder="用户名/昵称" clearable @keyup.enter="handleSearch" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="全部" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row v-if="hasPermission('system:user:add')" :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增</el-button></el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch v-if="hasPermission('system:user:changeStatus')" v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          <el-tag v-else :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:user:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:user:resetPwd')" link type="warning" @click="handleResetPassword(row)">重置密码</el-button>
          <el-button v-if="hasPermission('system:user:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next" @current-change="handleCurrentChange" @size-change="handleSizeChange" />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="昵称" prop="nickname"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item v-if="!form.id" label="密码" prop="password"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
