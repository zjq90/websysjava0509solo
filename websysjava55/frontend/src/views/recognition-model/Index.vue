<template>
  <div>
    <h1 class="page-title">🤖 文物识别模型</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>部署AI模型自动识别文物类别（如"清代官窑瓷器"）</span>
        <el-button type="primary" @click="handleAdd">部署新模型</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="modelNo" label="模型编号" />
        <el-table-column prop="modelName" label="模型名称" />
        <el-table-column prop="modelVersion" label="版本" />
        <el-table-column prop="modelType" label="类型">
          <template #default="scope">
            <el-tag :type="getModelType(scope.row.modelType)">{{ getModelTypeName(scope.row.modelType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="framework" label="框架" />
        <el-table-column prop="accuracy" label="准确率">
          <template #default="scope">
            <el-progress :percentage="scope.row.accuracy" :color="getAccuracyColor(scope.row.accuracy)" />
          </template>
        </el-table-column>
        <el-table-column prop="categoryCount" label="支持类别" />
        <el-table-column prop="isDefault" label="默认模型">
          <template #default="scope">
            <el-tag :type="scope.row.isDefault === 1 ? 'danger' : 'info'">
              {{ scope.row.isDefault === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 2 ? 'warning' : 'info'">
              {{ scope.row.status === 1 ? '已部署' : scope.row.status === 2 ? '部署中' : '未部署' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" type="success" @click="handleTest(scope.row)">测试识别</el-button>
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const tableData = ref([])

const getModelType = (type) => {
  const types = ['primary', 'success', 'warning', 'info']
  return types[type - 1] || 'info'
}

const getModelTypeName = (type) => {
  const names = ['图像分类', '目标检测', '特征提取', '其他']
  return names[type - 1] || '其他'
}

const getAccuracyColor = (percentage) => {
  if (percentage >= 90) return '#67c23a'
  if (percentage >= 80) return '#e6a23c'
  return '#f56c6c'
}

const loadData = async () => {
  try {
    const res = await request.get('/relic-recognition-model')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => ElMessage.info('部署新模型功能')
const handleTest = (row) => ElMessage.info('测试识别: ' + row.modelName)
const handleEdit = (row) => ElMessage.info('编辑模型: ' + row.modelName)
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该模型吗?', '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(() => ElMessage.success('删除成功'))
}

onMounted(() => loadData())
</script>
