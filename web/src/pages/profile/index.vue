<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { changePasswordApi, getProfileApi, getProfileLoginLogApi, getProfileMessagesApi, markNoticeReadApi, updateProfileApi } from "@@/apis/business"
import { ElMessage } from "element-plus"

defineOptions({ name: "Profile" })

const activeTab = ref("info")
const profile = ref<Business.UserDTO>()

// 基本信息
const infoForm = reactive<Business.ProfileUpdateCmd>({ nickname: "", email: "", phone: "" })

// 修改密码
const pwdForm = reactive<Business.PasswordChangeCmd>({ oldPassword: "", newPassword: "" })
const confirmPassword = ref("")

// 登录日志
const loginLogs = ref<Business.LoginLogDTO[]>([])
const loginLogTotal = ref(0)
const loginLogPage = reactive({ current: 1, size: 10 })

// 站内消息
const messages = ref<Business.NoticeDTO[]>([])

function getProfile() {
  getProfileApi().then(({ data }) => {
    profile.value = data
    infoForm.nickname = data.nickname
    infoForm.email = data.email || ""
    infoForm.phone = data.phone || ""
  })
}

function handleUpdateProfile() {
  updateProfileApi(infoForm).then(() => { ElMessage.success("修改成功"); getProfile() })
}

function handleChangePassword() {
  if (pwdForm.newPassword !== confirmPassword.value) { ElMessage.error("两次密码不一致"); return }
  if (!pwdForm.oldPassword || !pwdForm.newPassword) { ElMessage.error("请填写完整"); return }
  changePasswordApi(pwdForm).then(() => {
    ElMessage.success("密码修改成功")
    pwdForm.oldPassword = ""; pwdForm.newPassword = ""; confirmPassword.value = ""
  })
}

function getLoginLogs() {
  getProfileLoginLogApi({ current: loginLogPage.current, size: loginLogPage.size }).then(({ data }) => {
    loginLogs.value = data.records; loginLogTotal.value = data.total
  })
}

function getMessages() {
  getProfileMessagesApi().then(({ data }) => { messages.value = data })
}

function handleMarkRead(id: number) {
  markNoticeReadApi(id).then(() => { ElMessage.success("已标记已读"); getMessages() })
}

function handleTabChange(tab: string | number) {
  if (tab === "loginLog") getLoginLogs()
  else if (tab === "messages") getMessages()
}

getProfile()
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：用户信息卡片 -->
      <el-col :span="8">
        <el-card>
          <template v-if="profile">
            <div class="user-card">
              <el-avatar :size="80" :src="profile.avatar || undefined">{{ profile.nickname?.charAt(0) }}</el-avatar>
              <h3>{{ profile.nickname }}</h3>
              <p>{{ profile.username }}</p>
              <p>{{ profile.email }}</p>
            </div>
          </template>
        </el-card>
      </el-col>
      <!-- 右侧：操作区域 -->
      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab" @tab-change="handleTabChange">
            <el-tab-pane label="基本资料" name="info">
              <el-form :model="infoForm" label-width="80px">
                <el-form-item label="昵称"><el-input v-model="infoForm.nickname" /></el-form-item>
                <el-form-item label="邮箱"><el-input v-model="infoForm.email" /></el-form-item>
                <el-form-item label="手机号"><el-input v-model="infoForm.phone" /></el-form-item>
                <el-form-item><el-button type="primary" @click="handleUpdateProfile">保存修改</el-button></el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="password">
              <el-form :model="pwdForm" label-width="100px">
                <el-form-item label="旧密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
                <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
                <el-form-item label="确认密码"><el-input v-model="confirmPassword" type="password" show-password /></el-form-item>
                <el-form-item><el-button type="primary" @click="handleChangePassword">修改密码</el-button></el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="登录日志" name="loginLog">
              <el-table :data="loginLogs" border>
                <el-table-column prop="ip" label="IP" />
                <el-table-column prop="location" label="地点" />
                <el-table-column prop="browser" label="浏览器" />
                <el-table-column prop="loginTime" label="时间" width="120" />
                <el-table-column prop="status" label="状态" width="80">
                  <template #default="{ row }"><el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? "成功" : "失败" }}</el-tag></template>
                </el-table-column>
              </el-table>
              <el-pagination v-model:current-page="loginLogPage.current" v-model:page-size="loginLogPage.size" :total="loginLogTotal" layout="total, prev, pager, next" @current-change="getLoginLogs" />
            </el-tab-pane>
            <el-tab-pane label="站内消息" name="messages">
              <el-table :data="messages" border>
                <el-table-column prop="title" label="标题" />
                <el-table-column prop="noticeType" label="类型" width="80">
                  <template #default="{ row }"><el-tag>{{ row.noticeType === 1 ? "通知" : "公告" }}</el-tag></template>
                </el-table-column>
                <el-table-column prop="createTime" label="时间" width="120" />
                <el-table-column label="操作" width="100">
                  <template #default="{ row }"><el-button link type="primary" @click="handleMarkRead(row.id)">已读</el-button></template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.user-card { text-align: center; }
.user-card h3 { margin: 12px 0 4px; }
.user-card p { color: #999; margin: 4px 0; }
</style>
