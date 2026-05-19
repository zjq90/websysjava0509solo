<template>
  <div class="credit-page">
    <div class="page-card">
      <div class="page-title">信用分管理</div>
      
      <div class="toolbar">
        <div>
          <el-select v-model="userType" placeholder="用户类型" style="width: 150px; margin-right: 10px;">
            <el-option label="宠物主人" :value="1" />
            <el-option label="医生" :value="2" />
          </el-select>
          <el-input
            v-model="searchUserId"
            placeholder="请输入用户ID"
            style="width: 150px; margin-right: 10px;"
          />
          <el-button type="primary" @click="loadCreditRecords">查询</el-button>
        </div>
      </div>

      <el-table :data="creditRecords" border stripe style="width: 100%">
        <el-table-column prop="id" label="记录ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="userType" label="用户类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.userType === 1 ? '宠物主人' : '医生' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scoreChange" label="分数变化" width="120">
          <template #default="{ row }">
            <span :style="{ color: row.scoreChange > 0 ? '#67C23A' : '#F56C6C' }">
              {{ row.scoreChange > 0 ? '+' : '' }}{{ row.scoreChange }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="currentScore" label="当前分数" width="120">
          <template #default="{ row }">
            <el-tag :type="row.currentScore >= 100 ? 'success' : row.currentScore >= 80 ? 'warning' : 'danger'">
              {{ row.currentScore }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="变更原因" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </div>

    <el-dialog v-model="adjustDialogVisible" title="调整信用分" width="500px">
      <el-form label-width="100px">
        <el-form-item label="用户类型">
          <el-select v-model="adjustForm.userType">
            <el-option label="宠物主人" :value="1" />
            <el-option label="医生" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input-number v-model="adjustForm.userId" :min="1" />
        </el-form-item>
        <el-form-item label="调整分数">
          <el-input-number v-model="adjustForm.scoreChange" :min="-100" :max="100" />
        </el-form-item>
        <el-form-item label="调整原因">
          <el-input v-model="adjustForm.reason" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="adjustDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdjustSubmit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getCreditRecords, adjustCreditScore } from '@/api'

const creditRecords = ref([])
const adjustDialogVisible = ref(false)
const userType = ref(1)
const searchUserId = ref('')
const adjustForm = ref({
  userId: null,
  userType: 1,
  scoreChange: 0,
  reason: ''
})

const loadCreditRecords = async () => {
  if (!searchUserId.value) {
    ElMessage.warning('请输入用户ID')
    return
  }
  const res = await getCreditRecords(searchUserId.value, userType.value)
  creditRecords.value = res.data
}

const handleAdjustSubmit = async () => {
  await adjustCreditScore(adjustForm.value)
  ElMessage.success('调整成功')
  adjustDialogVisible.value = false
  loadCreditRecords()
}
</script>
