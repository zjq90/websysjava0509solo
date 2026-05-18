<template>
  <div>
    <h1 class="page-title">📋 审核规则库</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>文物出入库、修复、展览等审核规则管理</span>
        <el-button type="primary" @click="handleAdd">新增规则</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="ruleNo" label="规则编号" />
        <el-table-column prop="ruleName" label="规则名称" />
        <el-table-column prop="ruleType" label="类型">
          <template #default="scope">
            <el-tag :type="getRuleType(scope.row.ruleType)">{{ getRuleTypeName(scope.row.ruleType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级">
          <template #default="scope">
            <el-tag :type="scope.row.priority === 1 ? 'danger' : scope.row.priority === 2 ? 'warning' : 'info'">
              {{ scope.row.priority === 1 ? '高' : scope.row.priority === 2 ? '中' : '低' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditorRole" label="审核角色" />
        <el-table-column prop="timeoutHours" label="超时(小时)" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
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

const getRuleType = (type) => {
  const types = ['primary', 'success', 'warning', 'danger', 'info']
  return types[type - 1] || 'info'
}

const getRuleTypeName = (type) => {
  const names = ['入库审核', '出库审核', '修复审核', '展览审核', '其他']
  return names[type - 1] || '其他'
}

const loadData = async () => {
  try {
    const res = await request.get('/audit-rule')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => ElMessage.info('新增规则功能')
const handleEdit = (row) => ElMessage.info('编辑规则: ' + row.ruleName)
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该规则吗?', '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(() => ElMessage.success('删除成功'))
}

onMounted(() => loadData())
</script>
