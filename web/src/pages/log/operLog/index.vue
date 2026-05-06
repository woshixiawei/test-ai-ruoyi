<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { cleanOperLogApi, getOperLogListApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "OperLog" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.OperLogDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })

function getList() {
  loading.value = true
  getOperLogListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleClean() {
  ElMessageBox.confirm("确认清空所有操作日志？此操作不可逆！", "警告", { type: "warning" }).then(() => {
    cleanOperLogApi().then(() => { ElMessage.success("清空成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:operLog:clean')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="danger" @click="handleClean">清空日志</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="title" label="操作模块" />
      <el-table-column prop="operType" label="操作类型" width="100" />
      <el-table-column prop="requestMethod" label="请求方式" width="100" />
      <el-table-column prop="operIp" label="操作IP" width="130" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? "正常" : "异常" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="costTime" label="耗时(ms)" width="100" />
      <el-table-column prop="operTime" label="操作时间" width="120" />
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />
  </div>
</template>
