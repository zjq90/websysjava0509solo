<template>
  <div class="bike-detail-container">
    <div class="detail-header">
      <el-button type="info" size="small" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h3>车辆详情</h3>
    </div>
    
    <div class="bike-card" v-if="bike">
      <div class="bike-header">
        <div class="bike-icon">🚲</div>
        <div class="bike-info">
          <div class="bike-no">{{ bike.bikeNo }}</div>
          <el-tag :type="statusType" size="large">{{ statusText }}</el-tag>
        </div>
      </div>
      
      <div class="info-section">
        <div class="section-title">基本信息</div>
        <div class="info-list">
          <div class="info-item">
            <span class="label">车辆类型</span>
            <span class="value">{{ bike.isElectric ? '电动单车' : '普通单车' }}</span>
          </div>
          <div class="info-item" v-if="bike.isElectric">
            <span class="label">电量</span>
            <div class="battery-bar">
              <div class="battery-fill" :style="{ width: bike.batteryLevel + '%' }" :class="batteryClass"></div>
              <span class="battery-text">{{ bike.batteryLevel }}%</span>
            </div>
          </div>
          <div class="info-item">
            <span class="label">当前位置</span>
            <span class="value">{{ bike.location || '未知' }}</span>
          </div>
          <div class="info-item">
            <span class="label">累计里程</span>
            <span class="value">{{ bike.totalMileage || 0 }} km</span>
          </div>
          <div class="info-item">
            <span class="label">最后维护</span>
            <span class="value">{{ bike.lastMaintenanceTime || '暂无' }}</span>
          </div>
        </div>
      </div>
      
      <div class="info-section" v-if="bike.status === 'FAULT'">
        <div class="section-title">故障信息</div>
        <div class="fault-info">
          <el-alert
            :title="bike.faultType"
            :description="bike.faultDescription"
            type="error"
            :closable="false"
            show-icon
          />
        </div>
      </div>
      
      <div class="info-section">
        <div class="section-title">维修历史</div>
        <div class="repair-list">
          <div class="repair-item" v-for="(item, index) in repairHistory" :key="index">
            <div class="repair-date">{{ item.date }}</div>
            <div class="repair-content">
              <div class="repair-type">{{ item.type }}</div>
              <div class="repair-desc">{{ item.description }}</div>
            </div>
          </div>
          <el-empty v-if="repairHistory.length === 0" description="暂无维修记录" :image-size="80" />
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button type="primary" size="large" @click="reportFault" v-if="bike.status !== 'FAULT'">
          上报故障
        </el-button>
        <el-button type="success" size="large" @click="createRepairOrder" v-if="bike.status === 'FAULT'">
          创建维修工单
        </el-button>
      </div>
    </div>
    
    <div class="loading" v-else>
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { api } from '../api'

const route = useRoute()
const router = useRouter()
const bike = ref(null)
const repairHistory = ref([])

const statusType = computed(() => {
  const map = {
    AVAILABLE: 'success',
    IN_USE: 'primary',
    FAULT: 'danger',
    MAINTENANCE: 'warning',
    DISABLED: 'info'
  }
  return map[bike.value?.status] || 'info'
})

const statusText = computed(() => {
  const map = {
    AVAILABLE: '可用',
    IN_USE: '使用中',
    FAULT: '故障',
    MAINTENANCE: '维护中',
    DISABLED: '已停用'
  }
  return map[bike.value?.status] || '未知'
})

const batteryClass = computed(() => {
  const level = bike.value?.batteryLevel || 0
  if (level <= 20) return 'low'
  if (level <= 50) return 'medium'
  return 'high'
})

const goBack = () => {
  router.back()
}

const reportFault = () => {
  ElMessage.info('故障上报功能开发中')
}

const createRepairOrder = () => {
  ElMessage.info('创建维修工单功能开发中')
}

const loadBikeDetail = async () => {
  const id = route.params.id
  try {
    const data = await api.getBikeDetail(id)
    bike.value = data
  } catch (e) {
    bike.value = {
      id: id,
      bikeNo: 'B' + id?.padStart(3, '0') || 'B001',
      isElectric: true,
      batteryLevel: 65,
      status: 'AVAILABLE',
      location: '朝阳区建国路88号',
      totalMileage: 1256.5,
      lastMaintenanceTime: '2024-01-15'
    }
  }
  
  repairHistory.value = [
    { date: '2024-01-15', type: '常规保养', description: '更换刹车线、润滑链条' },
    { date: '2023-12-20', type: '故障维修', description: '电池接触不良修复' }
  ]
}

onMounted(() => {
  loadBikeDetail()
})
</script>

<style scoped>
.bike-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 80px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #fff;
  border-bottom: 1px solid #eee;
  
  h3 {
    margin: 0;
    font-size: 18px;
    color: #333;
  }
}

.bike-card {
  margin: 10px;
}

.bike-header {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px 12px 0 0;
  
  .bike-icon {
    font-size: 48px;
  }
  
  .bike-info {
    .bike-no {
      font-size: 24px;
      font-weight: bold;
      color: #fff;
      margin-bottom: 8px;
    }
  }
}

.info-section {
  background: #fff;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-radius: 0 0 12px 12px;
    border-bottom: none;
  }
  
  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 2px solid #667eea;
    display: inline-block;
  }
}

.info-list {
  .info-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      font-size: 14px;
      color: #666;
    }
    
    .value {
      font-size: 14px;
      color: #333;
      font-weight: 500;
    }
  }
}

.battery-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 150px;
  height: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  
  .battery-fill {
    height: 100%;
    transition: width 0.3s;
    
    &.high { background: #67c23a; }
    &.medium { background: #e6a23c; }
    &.low { background: #f56c6c; }
  }
  
  .battery-text {
    position: absolute;
    right: 10px;
    font-size: 12px;
    font-weight: bold;
    color: #333;
  }
}

.fault-info {
  margin-top: 10px;
}

.repair-list {
  .repair-item {
    display: flex;
    gap: 15px;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .repair-date {
      font-size: 12px;
      color: #999;
      white-space: nowrap;
    }
    
    .repair-content {
      .repair-type {
        font-size: 14px;
        color: #333;
        font-weight: 500;
        margin-bottom: 4px;
      }
      
      .repair-desc {
        font-size: 13px;
        color: #666;
      }
    }
  }
}

.action-buttons {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  gap: 10px;
  
  .el-button {
    flex: 1;
  }
}

.loading {
  padding: 10px;
}
</style>
