<template>
  <div>
    <h1 class="page-title">📡 物联网设备管理</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>温湿度传感器管理，支持NFC近场通信绑定</span>
        <el-button type="primary" @click="handleAdd">新增设备</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="deviceNo" label="设备编号" />
        <el-table-column prop="deviceName" label="设备名称" />
        <el-table-column prop="nfcTagId" label="NFC标签ID" />
        <el-table-column prop="relicName" label="绑定文物" />
        <el-table-column prop="currentTemperature" label="温度(℃)">
          <template #default="scope">
            <span :style="{color: scope.row.currentTemperature > 25 ? '#f56c6c' : '#67c23a'}">
              {{ scope.row.currentTemperature }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="currentHumidity" label="湿度(%)">
          <template #default="scope">
            <span :style="{color: scope.row.currentHumidity > 70 ? '#f56c6c' : '#67c23a'}">
              {{ scope.row.currentHumidity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="bound" label="绑定状态">
          <template #default="scope">
            <el-tag :type="scope.row.bound === 1 ? 'success' : 'info'">
              {{ scope.row.bound === 1 ? '已绑定' : '未绑定' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="运行状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" @click="handleBind(scope.row)">NFC绑定</el-button>
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
    const res = await request.get('/iot-device')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => {
  ElMessage.info('新增设备功能')
}

const handleEdit = (row) => {
  ElMessage.info('编辑设备: ' + row.deviceName)
}

const handleBind = (row) => {
  ElMessage.info('NFC绑定文物: ' + row.deviceName)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该设备吗?', '提示', {
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
