<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">智能设备管理</h2>
      <el-button type="primary" @click="handleAddDevice">新增设备</el-button>
    </div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="设备列表" name="device">
        <el-table :data="deviceData" border stripe style="width: 100%">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="deviceCode" label="设备编号" width="120" />
          <el-table-column prop="deviceType" label="设备类型" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.deviceType === 'FEEDER' ? 'success' : 'primary'">
                {{ scope.row.deviceType === 'FEEDER' ? '喂食器' : '饮水机' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="petName" label="关联宠物" width="100" />
          <el-table-column prop="location" label="位置" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ONLINE' ? 'success' : 'danger'">
                {{ scope.row.status === 'ONLINE' ? '在线' : '离线' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="generateMockData(scope.row.id)">生成数据</el-button>
              <el-button size="small" type="danger" @click="handleDeleteDevice(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="宠物数据" name="data">
        <el-table :data="petData" border stripe style="width: 100%">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="deviceName" label="设备名称" width="150" />
          <el-table-column prop="petName" label="宠物名称" width="100" />
          <el-table-column prop="foodIntake" label="进食量(g)" width="100" />
          <el-table-column prop="waterIntake" label="饮水量(ml)" width="100" />
          <el-table-column prop="feedCount" label="进食次数" width="90" />
          <el-table-column prop="drinkCount" label="饮水次数" width="90" />
          <el-table-column prop="temperature" label="温度(℃)" width="90" />
          <el-table-column prop="humidity" label="湿度(%)" width="90" />
          <el-table-column prop="status" label="数据状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'warning'">
                {{ scope.row.status === 'NORMAL' ? '正常' : '异常' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const activeTab = ref('device')
const deviceData = ref([])
const petData = ref([])

const fetchDevices = async () => {
  try {
    const res = await axios.get('/api/smart-device/devices')
    if (res.data.code === 200) deviceData.value = res.data.data
  } catch (e) { ElMessage.error('获取设备数据失败') }
}

const fetchPetData = async () => {
  try {
    const res = await axios.get('/api/smart-device/data')
    if (res.data.code === 200) petData.value = res.data.data
  } catch (e) { ElMessage.error('获取宠物数据失败') }
}

const handleAddDevice = () => {
  ElMessage.info('新增功能开发中')
}

const generateMockData = async (id) => {
  try {
    const res = await axios.post(`/api/smart-device/data/mock/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('模拟数据已生成')
      fetchPetData()
    }
  } catch (e) { ElMessage.error('生成失败') }
}

const handleDeleteDevice = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该设备吗?', '提示')
    const res = await axios.delete(`/api/smart-device/devices/${id}`)
    if (res.data.code === 200) { ElMessage.success('删除成功'); fetchDevices() }
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => {
  fetchDevices()
  fetchPetData()
})
</script>
