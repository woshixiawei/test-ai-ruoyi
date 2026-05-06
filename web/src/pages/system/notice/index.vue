<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createNoticeApi, deleteNoticeApi, getNoticeListApi, publishNoticeApi, updateNoticeApi, withdrawNoticeApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Notice" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.NoticeDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.NoticeCmd>({ title: "", content: "", noticeType: 1, status: 0 })
const formRules = { title: [{ required: true, message: "请输入公告标题", trigger: "blur" }], content: [{ required: true, message: "请输入公告内容", trigger: "blur" }] }

const statusMap: Record<number, { label: string, type: 'primary' | 'success' | 'warning' | 'info' | 'danger' }> = { 0: { label: "草稿", type: "info" }, 1: { label: "已发布", type: "success" }, 2: { label: "已撤回", type: "warning" } }
const noticeTypeMap: Record<number, string> = { 1: "通知", 2: "公告" }

function getList() {
  loading.value = true
  getNoticeListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleAdd() {
  dialogTitle.value = "新增公告"
  Object.assign(form, { id: undefined, title: "", content: "", noticeType: 1, status: 0 })
  dialogVisible.value = true
}

function handleEdit(row: Business.NoticeDTO) {
  dialogTitle.value = "编辑公告"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updateNoticeApi(form) : createNoticeApi(form)
    apiCall.then(() => { ElMessage.success(form.id ? "修改成功" : "新增成功"); dialogVisible.value = false; getList() })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该公告？", "提示", { type: "warning" }).then(() => {
    deleteNoticeApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

function handlePublish(id: number) {
  ElMessageBox.confirm("确认发布该公告？", "提示", { type: "info" }).then(() => {
    publishNoticeApi(id).then(() => { ElMessage.success("发布成功"); getList() })
  })
}

function handleWithdraw(id: number) {
  ElMessageBox.confirm("确认撤回该公告？", "提示", { type: "warning" }).then(() => {
    withdrawNoticeApi(id).then(() => { ElMessage.success("撤回成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:notice:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="noticeType" label="类型" width="80">
        <template #default="{ row }"><el-tag>{{ noticeTypeMap[row.noticeType] }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:notice:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 0 && hasPermission('system:notice:publish')" link type="success" @click="handlePublish(row.id)">发布</el-button>
          <el-button v-if="row.status === 1 && hasPermission('system:notice:withdraw')" link type="warning" @click="handleWithdraw(row.id)">撤回</el-button>
          <el-button v-if="hasPermission('system:notice:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="类型"><el-radio-group v-model="form.noticeType"><el-radio :value="1">通知</el-radio><el-radio :value="2">公告</el-radio></el-radio-group></el-form-item>
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="6" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
