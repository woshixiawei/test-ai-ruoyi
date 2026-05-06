<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createPostApi, deletePostApi, getPostListApi, updatePostApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Post" })

const { hasPermission } = usePermission()

const loading = ref(false)
const tableData = ref<Business.PostDTO[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })

const dialogVisible = ref(false)
const dialogTitle = ref("")
const formRef = ref()
const form = reactive<Business.PostCmd>({ postCode: "", postName: "", sort: 0, status: 1, remark: "" })
const formRules = {
  postCode: [{ required: true, message: "请输入岗位编码", trigger: "blur" }],
  postName: [{ required: true, message: "请输入岗位名称", trigger: "blur" }]
}

function getList() {
  loading.value = true
  getPostListApi({ current: page.current, size: page.size }).then(({ data }) => {
    tableData.value = data.records; total.value = data.total
  }).finally(() => { loading.value = false })
}

function handleAdd() {
  dialogTitle.value = "新增岗位"
  Object.assign(form, { id: undefined, postCode: "", postName: "", sort: 0, status: 1, remark: "" })
  dialogVisible.value = true
}

function handleEdit(row: Business.PostDTO) {
  dialogTitle.value = "编辑岗位"
  Object.assign(form, row)
  dialogVisible.value = true
}

function handleSubmit() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = form.id ? updatePostApi(form) : createPostApi(form)
    apiCall.then(() => { ElMessage.success(form.id ? "修改成功" : "新增成功"); dialogVisible.value = false; getList() })
  })
}

function handleDelete(id: number) {
  ElMessageBox.confirm("确认删除该岗位？", "提示", { type: "warning" }).then(() => {
    deletePostApi(id).then(() => { ElMessage.success("删除成功"); getList() })
  })
}

getList()
</script>

<template>
  <div class="app-container">
    <el-row v-if="hasPermission('system:post:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleAdd">新增</el-button></el-col></el-row>
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="postCode" label="岗位编码" />
      <el-table-column prop="postName" label="岗位名称" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="120" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button v-if="hasPermission('system:post:edit')" link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="hasPermission('system:post:remove')" link type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.current" v-model:page-size="page.size" :total="total" layout="total, sizes, prev, pager, next" @current-change="getList" />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="岗位编码" prop="postCode"><el-input v-model="form.postCode" /></el-form-item>
        <el-form-item label="岗位名称" prop="postName"><el-input v-model="form.postName" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>
