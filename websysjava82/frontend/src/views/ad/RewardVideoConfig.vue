<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">激励视频配置</h2>
    </div>

    <div class="card-wrapper">
      <el-table :data="games" style="width: 100%" border>
        <el-table-column label="游戏图标" width="80">
          <template #default="{ row }">
            <el-image v-if="row.icon" :src="row.icon" class="image-preview" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="code" label="游戏编码" width="120" />
        <el-table-column prop="name" label="游戏名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="rewardContent" label="奖励内容" width="200">
          <template #default="{ row }">
            {{ row.rewardContent || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="dailyWatchLimit" label="每日观看上限" width="120">
          <template #default="{ row }">
            {{ row.dailyWatchLimit || 0 }}次
          </template>
        </el-table-column>
        <el-table-column label="激励视频" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.rewardVideoEnabled"
              @change="handleToggle(row)"
              active-text="启用"
              inactive-text="禁用"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="openDialog(row)">配置</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="`${editData.name} - 激励视频配置`" width="500px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="启用激励视频">
          <el-switch v-model="editData.rewardVideoEnabled" />
        </el-form-item>
        <el-form-item label="奖励内容" prop="rewardContent">
          <el-input
            v-model="editData.rewardContent"
            type="textarea"
            :rows="2"
            placeholder="如：观看后获得双倍积分"
          />
        </el-form-item>
        <el-form-item label="每日观看上限" prop="dailyWatchLimit">
          <el-input-number v-model="editData.dailyWatchLimit" :min="1" :max="100" style="width: 100%" />
          <div style="color: #909399; font-size: 12px; margin-top: 5px">单用户每日最多观看次数限制</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="modal-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { gameApi } from '../../api'

const games = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const editData = reactive({
  id: null,
  name: '',
  rewardVideoEnabled: false,
  rewardContent: '',
  dailyWatchLimit: 10
})

const rules = {
  rewardContent: [{ required: true, message: '请输入奖励内容', trigger: 'blur' }],
  dailyWatchLimit: [{ required: true, message: '请输入每日观看上限', trigger: 'blur' }]
}

const loadData = async () => {
  const res = await gameApi.list()
  games.value = res.data || []
}

const handleToggle = async (row) => {
  await gameApi.updateRewardVideo(row.id, {
    enabled: row.rewardVideoEnabled
  })
  ElMessage.success(row.rewardVideoEnabled ? '已启用' : '已禁用')
}

const openDialog = (row) => {
  Object.assign(editData, {
    id: row.id,
    name: row.name,
    rewardVideoEnabled: row.rewardVideoEnabled,
    rewardContent: row.rewardContent || '',
    dailyWatchLimit: row.dailyWatchLimit || 10
  })
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  await gameApi.updateRewardVideo(editData.id, {
    enabled: editData.rewardVideoEnabled,
    rewardContent: editData.rewardContent,
    dailyLimit: editData.dailyWatchLimit
  })
  ElMessage.success('配置保存成功')
  dialogVisible.value = false
  loadData()
}

onMounted(loadData)
</script>
