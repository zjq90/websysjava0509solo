<template>
  <div>
    <h1 class="page-title">🪄 文物数字分身</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>文物3D模型管理，支持虚拟修复演示</span>
        <el-button type="primary" @click="handleAdd">新建分身</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="relicName" label="文物名称" />
        <el-table-column prop="twinName" label="分身名称" />
        <el-table-column prop="modelFormat" label="模型格式" />
        <el-table-column prop="modelSize" label="大小(MB)" />
        <el-table-column prop="precisionLevel" label="精度等级">
          <template #default="scope">
            <el-tag v-for="i in scope.row.precisionLevel" :key="i" type="primary" size="small">★</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="supportRestorationDemo" label="虚拟修复">
          <template #default="scope">
            <el-tag :type="scope.row.supportRestorationDemo === 1 ? 'success' : 'info'">
              {{ scope.row.supportRestorationDemo === 1 ? '支持' : '不支持' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : scope.row.status === 0 ? 'warning' : 'info'">
              {{ scope.row.status === 1 ? '已完成' : scope.row.status === 0 ? '创建中' : '更新中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="small" @click="handlePreview(scope.row)">3D预览</el-button>
            <el-button size="small" type="success" @click="handleRestore(scope.row)">虚拟修复</el-button>
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

const loadData = async () => {
  try {
    const res = await request.get('/digital-twin')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => ElMessage.info('新建数字分身功能')
const handlePreview = (row) => ElMessage.info('3D预览: ' + row.twinName)
const handleRestore = (row) => ElMessage.info('虚拟修复演示: ' + row.twinName)
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该数字分身吗?', '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(() => ElMessage.success('删除成功'))
}

onMounted(() => loadData())
</script>
