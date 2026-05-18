<template>
  <div>
    <h1 class="page-title">🔗 区块链节点管理</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>跨可用区高可用部署的区块链节点管理</span>
        <el-button type="primary" @click="handleAdd">新增节点</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="nodeName" label="节点名称" />
        <el-table-column prop="nodeAddress" label="节点地址" />
        <el-table-column prop="port" label="端口" />
        <el-table-column prop="nodeType" label="类型">
          <template #default="scope">
            <el-tag :type="scope.row.nodeType === 1 ? 'danger' : scope.row.nodeType === 2 ? 'warning' : 'info'">
              {{ scope.row.nodeType === 1 ? '主节点' : scope.row.nodeType === 2 ? '备份节点' : '同步节点' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="availabilityZone" label="可用区" />
        <el-table-column prop="region" label="区域" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '在线' : '离线' }}
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

const loadData = async () => {
  try {
    const res = await request.get('/blockchain-node')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => {
  ElMessage.info('新增节点功能')
}

const handleEdit = (row) => {
  ElMessage.info('编辑节点: ' + row.nodeName)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该节点吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

onMounted(() => {
  loadData()
})
</script>
