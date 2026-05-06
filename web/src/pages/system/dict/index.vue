<script lang="ts" setup>
import type * as Business from "@@/apis/business/type"
import { createDictDataApi, createDictTypeApi, deleteDictDataApi, deleteDictTypeApi, getDictDataListApi, getDictTypeListApi, updateDictDataApi, updateDictTypeApi } from "@@/apis/business"
import { usePermission } from "@@/composables/usePermission"
import { ElMessage, ElMessageBox } from "element-plus"

defineOptions({ name: "Dict" })

const { hasPermission } = usePermission()

// === 字典类型 ===
const typeLoading = ref(false)
const typeData = ref<Business.DictTypeDTO[]>([])
const typeTotal = ref(0)
const typePage = reactive({ current: 1, size: 10 })
const selectedType = ref<Business.DictTypeDTO | null>(null)

const typeDialogVisible = ref(false)
const typeDialogTitle = ref("")
const typeFormRef = ref()
const typeForm = reactive<Business.DictTypeCmd>({ dictName: "", dictType: "", status: 1, remark: "" })
const typeFormRules = { dictName: [{ required: true, message: "请输入字典名称", trigger: "blur" }], dictType: [{ required: true, message: "请输入字典类型", trigger: "blur" }] }

function getTypeList() {
  typeLoading.value = true
  getDictTypeListApi({ current: typePage.current, size: typePage.size }).then(({ data }) => {
    typeData.value = data.records; typeTotal.value = data.total
  }).finally(() => { typeLoading.value = false })
}

function handleTypeAdd() {
  typeDialogTitle.value = "新增字典类型"
  Object.assign(typeForm, { id: undefined, dictName: "", dictType: "", status: 1, remark: "" })
  typeDialogVisible.value = true
}

function handleTypeEdit(row: Business.DictTypeDTO) {
  typeDialogTitle.value = "编辑字典类型"
  Object.assign(typeForm, row)
  typeDialogVisible.value = true
}

function handleTypeSubmit() {
  typeFormRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = typeForm.id ? updateDictTypeApi(typeForm) : createDictTypeApi(typeForm)
    apiCall.then(() => { ElMessage.success(typeForm.id ? "修改成功" : "新增成功"); typeDialogVisible.value = false; getTypeList() })
  })
}

function handleTypeDelete(id: number) {
  ElMessageBox.confirm("确认删除该字典类型？", "提示", { type: "warning" }).then(() => {
    deleteDictTypeApi(id).then(() => { ElMessage.success("删除成功"); selectedType.value = null; getTypeList(); getDataList() })
  })
}

function handleTypeSelect(row: Business.DictTypeDTO) {
  selectedType.value = row
  getDataList()
}

// === 字典数据 ===
const dataLoading = ref(false)
const dataData = ref<Business.DictDataDTO[]>([])
const dataTotal = ref(0)
const dataPage = reactive({ current: 1, size: 10 })

const dataDialogVisible = ref(false)
const dataDialogTitle = ref("")
const dataFormRef = ref()
const dataForm = reactive<Business.DictDataCmd>({ dictTypeId: 0, dictLabel: "", dictValue: "", sort: 0, status: 1, remark: "" })
const dataFormRules = { dictLabel: [{ required: true, message: "请输入数据标签", trigger: "blur" }], dictValue: [{ required: true, message: "请输入数据键值", trigger: "blur" }] }

function getDataList() {
  if (!selectedType.value) { dataData.value = []; return }
  dataLoading.value = true
  getDictDataListApi({ current: dataPage.current, size: dataPage.size, dictTypeId: selectedType.value.id }).then(({ data }) => {
    dataData.value = data.records; dataTotal.value = data.total
  }).finally(() => { dataLoading.value = false })
}

function handleDataAdd() {
  dataDialogTitle.value = "新增字典数据"
  Object.assign(dataForm, { id: undefined, dictTypeId: selectedType.value!.id, dictLabel: "", dictValue: "", sort: 0, status: 1, remark: "" })
  dataDialogVisible.value = true
}

function handleDataEdit(row: Business.DictDataDTO) {
  dataDialogTitle.value = "编辑字典数据"
  Object.assign(dataForm, row)
  dataDialogVisible.value = true
}

function handleDataSubmit() {
  dataFormRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const apiCall = dataForm.id ? updateDictDataApi(dataForm) : createDictDataApi(dataForm)
    apiCall.then(() => { ElMessage.success(dataForm.id ? "修改成功" : "新增成功"); dataDialogVisible.value = false; getDataList() })
  })
}

function handleDataDelete(id: number) {
  ElMessageBox.confirm("确认删除该字典数据？", "提示", { type: "warning" }).then(() => {
    deleteDictDataApi(id).then(() => { ElMessage.success("删除成功"); getDataList() })
  })
}

getTypeList()
</script>

<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左侧：字典类型 -->
      <el-col :span="10">
        <el-row v-if="hasPermission('system:dict:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleTypeAdd">新增类型</el-button></el-col></el-row>
        <el-table v-loading="typeLoading" :data="typeData" border highlight-current-row @current-change="handleTypeSelect">
          <el-table-column prop="dictName" label="字典名称" />
          <el-table-column prop="dictType" label="字典编码" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button v-if="hasPermission('system:dict:edit')" link type="primary" @click="handleTypeEdit(row)">编辑</el-button>
              <el-button v-if="hasPermission('system:dict:remove')" link type="danger" @click="handleTypeDelete(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination v-model:current-page="typePage.current" v-model:page-size="typePage.size" :total="typeTotal" layout="total, prev, pager, next" @current-change="getTypeList" />
      </el-col>
      <!-- 右侧：字典数据 -->
      <el-col :span="14">
        <div v-if="!selectedType" class="empty-hint"><el-empty description="请在左侧选择字典类型" /></div>
        <template v-else>
          <el-row v-if="hasPermission('system:dict:add')" :gutter="10" class="mb8"><el-col :span="1.5"><el-button type="primary" @click="handleDataAdd">新增数据</el-button></el-col></el-row>
          <el-table v-loading="dataLoading" :data="dataData" border>
            <el-table-column prop="dictLabel" label="数据标签" />
            <el-table-column prop="dictValue" label="数据键值" />
            <el-table-column prop="sort" label="排序" width="80" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? "正常" : "禁用" }}</el-tag></template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button v-if="hasPermission('system:dict:edit')" link type="primary" @click="handleDataEdit(row)">编辑</el-button>
                <el-button v-if="hasPermission('system:dict:remove')" link type="danger" @click="handleDataDelete(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination v-model:current-page="dataPage.current" v-model:page-size="dataPage.size" :total="dataTotal" layout="total, prev, pager, next" @current-change="getDataList" />
        </template>
      </el-col>
    </el-row>

    <el-dialog v-model="typeDialogVisible" :title="typeDialogTitle" width="500px">
      <el-form ref="typeFormRef" :model="typeForm" :rules="typeFormRules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName"><el-input v-model="typeForm.dictName" /></el-form-item>
        <el-form-item label="字典编码" prop="dictType"><el-input v-model="typeForm.dictType" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="typeForm.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="typeForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="typeDialogVisible = false">取消</el-button><el-button type="primary" @click="handleTypeSubmit">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="dataDialogVisible" :title="dataDialogTitle" width="500px">
      <el-form ref="dataFormRef" :model="dataForm" :rules="dataFormRules" label-width="80px">
        <el-form-item label="数据标签" prop="dictLabel"><el-input v-model="dataForm.dictLabel" /></el-form-item>
        <el-form-item label="数据键值" prop="dictValue"><el-input v-model="dataForm.dictValue" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="dataForm.sort" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="dataForm.status"><el-radio :value="1">正常</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="dataForm.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dataDialogVisible = false">取消</el-button><el-button type="primary" @click="handleDataSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<style scoped>
.empty-hint { display: flex; justify-content: center; align-items: center; min-height: 300px; }
</style>
