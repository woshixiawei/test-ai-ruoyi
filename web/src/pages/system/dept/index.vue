<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createDeptApi, deleteDeptApi, getDeptListApi, updateDeptApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Dept" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.DeptDTO[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.DeptCmd>({ deptName: "", parentId: 0, sort: 0, leader: "", phone: "", email: "", status: 1 })
const formRules = { deptName: [{ required: true, message: "请输入部门名称", trigger: "blur" }] }

function getList() {
  loading.value = true
  getDeptListApi().then(({ data }) => { tableData.value = data }).finally(() => { loading.value = false })
}

function handleAdd(parentId?: number) {
  dialogTitle.value = "新增部门"
  Object.assign(form, { id: undefined, deptName: "", parentId: parentId || 0, sort: 0, leader: "", phone: "", email: "", status: 1 })
  dialogVisible.value = true
}

function handleEdit(row: Business.DeptDTO) {
  dialogTitle.value = "编辑部门"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateDeptApi(form) : createDeptApi(form)
    apiCall.then(() => { ElMessage.success(form.id ? "修改成功" : "新增成功"); dialogVisible.value = false; getList() })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该部门？", "提示", { type: "warning" }).then(() => {
    deleteDeptApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:dept:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd()">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" default-expand-all>
      <el-table-column prop="deptName" label="部门名称" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="leader" label="负责人" width="100" />
      <el-table-column prop="phone" label="联系电话" width="120" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:dept:add')" link type="primary" @click="handleAdd(row.id)">新增</el-button>
          <el-button v-if="hasPermission('system:dept:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:dept:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="上级部门">
          <el-tree-select v-model="form.parentId" :data="tableData" :props="{ label: 'deptName', children: 'children' }" node-key="id" check-strictly :render-after-expand="false" placeholder="顶级部门" clearable />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName"><el-input v-model="form.deptName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="form.leader" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
