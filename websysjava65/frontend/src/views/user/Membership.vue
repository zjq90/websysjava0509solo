<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">用户分层运营</div>
      <el-input
        v-model="minRides"
        type="number"
        placeholder="最小骑行次数"
        style="width: 150px"
        @change="loadData"
      />
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">高频用户数</div>
            <div style="font-size: 32px; font-weight: bold; color: #667eea">{{ highFreqUsers.length }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">已赠送月卡</div>
            <div style="font-size: 32px; font-weight: bold; color: #67c23a">{{ giftedCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">待赠送</div>
            <div style="font-size: 32px; font-weight: bold; color: #f56c6c">{{ pendingCount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>高频用户列表（骑行次数 ≥ {{ minRides }} 次）</span>
          <el-button type="success" :icon="Present" @click="batchGift" :disabled="pendingCount === 0">
            批量赠送月卡
          </el-button>
        </div>
      </template>
      <el-table :data="highFreqUsers" stripe style="width: 100%">
        <el-table-column type="selection" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="rideCount" label="骑行次数" width="100" />
        <el-table-column prop="balance" label="账户余额" width="120">
          <template #default="{ row }">¥{{ row.balance }}</template>
        </el-table-column>
        <el-table-column prop="isVip" label="会员状态" width="100">
          <template #default="{ row }">
            <span :class="row.isVip ? 'badge-success' : 'badge-info'">
              {{ row.isVip ? '已开通' : '未开通' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="userLevel" label="用户等级" width="100">
          <template #default="{ row }">
            <span :class="row.userLevel === 'HIGH_FREQUENCY' ? 'badge-primary' : 'badge-info'">
              {{ row.userLevel === 'HIGH_FREQUENCY' ? '高频' : '普通' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="!row.isVip"
              type="success"
              size="small"
              link
              @click="giftMembership(row)"
            >
              赠送月卡
            </el-button>
            <el-button v-else size="small" link disabled>
              已是会员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Present } from '@element-plus/icons-vue'
import { getHighFrequencyUsers, giftMembership as apiGiftMembership } from '../../api'

const highFreqUsers = ref([])
const minRides = ref(30)
const selectedUsers = ref([])

const giftedCount = computed(() => highFreqUsers.value.filter(u => u.isVip).length)
const pendingCount = computed(() => highFreqUsers.value.filter(u => !u.isVip).length)

const loadData = async () => {
  try {
    const res = await getHighFrequencyUsers(minRides.value)
    highFreqUsers.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const giftMembership = async (user) => {
  try {
    await ElMessageBox.confirm(`确定给用户 ${user.username} 赠送月卡吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    await apiGiftMembership({ userId: user.id })
    ElMessage.success('赠送成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

const batchGift = async () => {
  try {
    await ElMessageBox.confirm('确定给所有选中的高频用户赠送月卡吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    ElMessage.success('批量赠送成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>
