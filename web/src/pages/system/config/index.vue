<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createConfigApi, deleteConfigApi, getConfigListApi, updateConfigApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Config" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.ConfigDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.ConfigCmd>({ configName: "", configKey: "", configValue: "", isSystem: 0, remark: "" })
const formRules = {
  configName: [{ required: true, message: "请输入参数名称", trigger: "blur" }],
  configKey: [{ required: true, message: "请输入参数键名", trigger: "blur" }],
  configValue: [{ required: true, message: "请输入参数键值", trigger: "blur" }]
}

function getList() {
  loading.value = true
  getConfigListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleAdd() {
  dialogTitle.value = "新增参数"
  Object.assign(form, { id: undefined, configName: "", configKey: "", configValue: "", isSystem: 0, remark: "" })
  dialogVisible.value = true
}

function handleEdit(row: Business.ConfigDTO) {
  dialogTitle.value = "编辑参数"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateConfigApi(form) : createConfigApi(form)
    apiCall.then(() => { ElMessage.success(form.id ? "修改成功" : "新增成功"); dialogVisible.value = false; getList() })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该参数？", "提示", { type: "warning" }).then(() => {
    deleteConfigApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:config:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="configName" label="参数名称" />
      <el-table-column prop="configKey" label="参数键名" />
      <el-table-column prop="configValue" label="参数键值" />
      <el-table-column prop="isSystem" label="系统内置" width="100">
        <template #default="{ row }"><el-tag :type="row.isSystem === 1 ? 'danger' : 'info'">{{ row.isSystem === 1 ? "是" : "否" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:config:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:config:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="参数名称" prop="configName"><el-input v-model="form.configName" /></el-form-item>
        <el-form-item label="参数键名" prop="configKey"><el-input v-model="form.configKey" /></el-form-item>
        <el-form-item label="参数键值" prop="configValue"><el-input v-model="form.configValue" /></el-form-item>
        <el-form-item label="系统内置"><el-radio-group v-model="form.isSystem"><el-radio :value="1">是</el-radio><el-radio :value="0">否</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
