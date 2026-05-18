<template>
  <div>
    <h1 class="page-title">🎮 VR虚拟博物馆</h1>
    <div class="card-wrapper">
      <div class="table-header">
        <span>用户可"走进"虚拟博物馆观看文物</span>
        <el-button type="primary" @click="handleAdd">新建场景</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="scene in tableData" :key="scene.id">
          <el-card shadow="hover" style="margin-bottom: 20px">
            <template #header>
              <div class="card-header">
                <span>{{ scene.sceneName }}</span>
              </div>
            </template>
            <div style="height: 150px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; display: flex; align-items: center; justify-content: center; color: white; font-size: 40px;">
              🖼️
            </div>
            <div style="margin-top: 10px;">
              <p><strong>场景编号:</strong> {{ scene.sceneNo }}</p>
              <p><strong>类型:</strong> {{ getSceneTypeName(scene.sceneType) }}</p>
              <p><strong>包含文物:</strong> {{ scene.relicCount }} 件</p>
              <p><strong>支持VR:</strong> <el-tag :type="scene.supportVrDevice === 1 ? 'success' : 'info'">{{ scene.supportVrDevice === 1 ? '是' : '否' }}</el-tag></p>
            </div>
            <template #footer>
              <el-button size="small" type="primary" @click="handleEnter(scene)">进入参观</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scene)">删除</el-button>
            </template>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const tableData = ref([])

const getSceneTypeName = (type) => {
  const names = ['主展厅', '专题展馆', '虚拟漫游', '其他']
  return names[type - 1] || '其他'
}

const loadData = async () => {
  try {
    const res = await request.get('/vr-scene')
    tableData.value = res.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => ElMessage.info('新建VR场景功能')
const handleEnter = (row) => ElMessage.info('进入虚拟博物馆: ' + row.sceneName)
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该VR场景吗?', '提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(() => ElMessage.success('删除成功'))
}

onMounted(() => loadData())
</script>
