<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { cleanLoginLogApi, getLoginLogListApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "LoginLog" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.LoginLogDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })

function getList() {
  loading.value = true
  getLoginLogListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleClean() {
  ElMessageBox.confirm("确认清空所有登录日志？此操作不可逆！", "警告", { type: "warning" }).then(() => {
    cleanLoginLogApi().then(() => { ElMessage.success("清空成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:loginLog:clean')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="danger" @click="handleClean">清空日志</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="ip" label="登录IP" width="130" />
      <el-table-column prop="location" label="登录地点" />
      <el-table-column prop="browser" label="浏览器" />
      <el-table-column prop="os" label="操作系统" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? "成功" : "失败" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="msg" label="提示消息" />
      <el-table-column prop="loginTime" label="登录时间" width="120" />
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />
  </div>
</template>
