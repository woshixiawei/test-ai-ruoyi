<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { deleteFileApi, getFileListApi, uploadFileApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"
import { Upload } from "@element-plus/icons-vue"

defineOptions({ name: "File" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.FileDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })
const uploadLoading = ref(false)

function getList() {
  loading.value = true
  getFileListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

// eslint-disable-next-line ts/no-explicit-any
function handleUpload(options: any) {
  uploadLoading.value = true
  uploadFileApi(options.file as File).then(() => {
    ElMessage.success("上传成功")
    getList()
  }).catch(() => {
    ElMessage.error("上传失败")
  }).finally(() => { uploadLoading.value = false })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该文件？", "提示", { type: "warning" }).then(() => {
    deleteFileApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

function formatFileSize(bytes: number) {
  if (bytes === 0) return "0 B"
  const k = 1024
  const sizes = ["B", "KB", "MB", "GB"]
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return `${(bytes / Math.pow(k, i)).toFixed(2)} ${sizes[i]}`
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:file:upload')" :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-upload :http-request="handleUpload as any" :show-file-list="false" accept="*">
          <el-button type="primary" :icon="Upload" :loading="uploadLoading">上传文件</el-button>
        </el-upload>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="fileName" label="文件名" />
      <el-table-column prop="fileType" label="类型" width="80" />
      <el-table-column label="大小" width="100">
        <template #default="{ row }">{{ formatFileSize(row.fileSize) }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="上传时间" width="120" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }"><el-button v-if="hasPermission('system:file:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />
  </div>
</template>
