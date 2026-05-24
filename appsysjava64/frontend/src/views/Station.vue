<template>
  <div class="station-container">
    <div class="page-header">
      <h3>换电站管理</h3>
    </div>
    
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-icon">🔋</div>
        <div class="stat-content">
          <div class="stat-value">{{ totalStations }}</div>
          <div class="stat-label">换电站总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">✅</div>
        <div class="stat-content">
          <div class="stat-value">{{ availableStations }}</div>
          <div class="stat-label">营业中</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⚡</div>
        <div class="stat-content">
          <div class="stat-value">{{ availableBatteries }}</div>
          <div class="stat-label">可用电池</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🔄</div>
        <div class="stat-content">
          <div class="stat-value">{{ chargingBatteries }}</div>
          <div class="stat-label">充电中</div>
        </div>
      </div>
    </div>
    
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索换电站名称或地址"
        clearable
        prefix-icon="Search"
        @input="filterStations"
      />
    </div>
    
    <div class="station-list">
      <div 
        class="station-card" 
        v-for="station in filteredStations" 
        :key="station.id"
        @click="showStationDetail(station)"
      >
        <div class="station-header">
          <div class="station-name">{{ station.stationName }}</div>
          <el-tag :type="station.status === 'OPEN' ? 'success' : 'info'" size="small">
            {{ station.status === 'OPEN' ? '营业中' : '已关闭' }}
          </el-tag>
        </div>
        
        <div class="station-info">
          <div class="info-item">
            <span class="info-icon">📍</span>
            <span class="info-text">{{ station.address }}</span>
          </div>
          <div class="info-item">
            <span class="info-icon">🕐</span>
            <span class="info-text">{{ station.businessHours || '24小时营业' }}</span>
          </div>
          <div class="info-item" v-if="station.contactPhone">
            <span class="info-icon">📞</span>
            <span class="info-text">{{ station.contactPhone }}</span>
          </div>
        </div>
        
        <div class="battery-stats">
          <div class="battery-item">
            <div class="battery-value available">{{ station.availableBatteries || 0 }}</div>
            <div class="battery-label">可用电池</div>
          </div>
          <div class="battery-item">
            <div class="battery-value charging">{{ station.chargingBatteries || 0 }}</div>
            <div class="battery-label">充电中</div>
          </div>
          <div class="battery-item">
            <div class="battery-value total">{{ station.totalSlots || 0 }}</div>
            <div class="battery-label">总仓位</div>
          </div>
        </div>
      </div>
      
      <el-empty v-if="filteredStations.length === 0" description="暂无换电站数据" />
    </div>
    
    <el-dialog
      v-model="detailVisible"
      title="换电站详情"
      width="90%"
    >
      <div class="detail-content" v-if="currentStation">
        <div class="detail-row">
          <span class="label">站点名称</span>
          <span class="value">{{ currentStation.stationName }}</span>
        </div>
        <div class="detail-row">
          <span class="label">站点编号</span>
          <span class="value">{{ currentStation.stationNo }}</span>
        </div>
        <div class="detail-row">
          <span class="label">详细地址</span>
          <span class="value">{{ currentStation.address }}</span>
        </div>
        <div class="detail-row">
          <span class="label">营业时间</span>
          <span class="value">{{ currentStation.businessHours || '24小时营业' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">联系电话</span>
          <span class="value">{{ currentStation.contactPhone || '-' }}</span>
        </div>
        <div class="detail-row">
          <span class="label">总仓位</span>
          <span class="value">{{ currentStation.totalSlots || 0 }}</span>
        </div>
        
        <div class="battery-progress">
          <div class="progress-label">电池使用率</div>
          <el-progress 
            :percentage="batteryUsagePercent" 
            :color="progressColor"
            :stroke-width="12"
          />
        </div>
      </div>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="navigateToStation">导航前往</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { api } from '../api'

const searchKeyword = ref('')
const stations = ref([])
const detailVisible = ref(false)
const currentStation = ref(null)

const totalStations = computed(() => stations.value.length)
const availableStations = computed(() => stations.value.filter(s => s.status === 'OPEN').length)
const availableBatteries = computed(() => 
  stations.value.reduce((sum, s) => sum + (s.availableBatteries || 0), 0)
)
const chargingBatteries = computed(() => 
  stations.value.reduce((sum, s) => sum + (s.chargingBatteries || 0), 0)
)

const filteredStations = computed(() => {
  if (!searchKeyword.value) return stations.value
  const keyword = searchKeyword.value.toLowerCase()
  return stations.value.filter(s => 
    s.stationName?.toLowerCase().includes(keyword) ||
    s.address?.toLowerCase().includes(keyword)
  )
})

const batteryUsagePercent = computed(() => {
  if (!currentStation.value?.totalSlots) return 0
  const used = (currentStation.value.chargingBatteries || 0) + 
               (currentStation.value.totalSlots - (currentStation.value.availableBatteries || 0) - (currentStation.value.chargingBatteries || 0))
  return Math.round((used / currentStation.value.totalSlots) * 100)
})

const progressColor = computed(() => {
  const percent = batteryUsagePercent.value
  if (percent >= 90) return '#f56c6c'
  if (percent >= 70) return '#e6a23c'
  return '#67c23a'
})

const filterStations = () => {
}

const showStationDetail = (station) => {
  currentStation.value = station
  detailVisible.value = true
}

const navigateToStation = () => {
  ElMessage.info('导航功能开发中')
}

const loadStations = async () => {
  try {
    const data = await api.getSwapStations()
    stations.value = data || []
  } catch (e) {
    stations.value = [
      {
        id: 1,
        stationNo: 'ST001',
        stationName: '建国门换电站',
        address: '朝阳区建国路88号SOHO现代城',
        status: 'OPEN',
        businessHours: '06:00-24:00',
        contactPhone: '400-123-4567',
        totalSlots: 20,
        availableBatteries: 8,
        chargingBatteries: 10
      },
      {
        id: 2,
        stationNo: 'ST002',
        stationName: '国贸换电站',
        address: '朝阳区国贸三期B1层',
        status: 'OPEN',
        businessHours: '24小时营业',
        contactPhone: '400-123-4568',
        totalSlots: 30,
        availableBatteries: 15,
        chargingBatteries: 12
      },
      {
        id: 3,
        stationNo: 'ST003',
        stationName: '三里屯换电站',
        address: '朝阳区三里屯太古里北区',
        status: 'OPEN',
        businessHours: '08:00-22:00',
        contactPhone: '400-123-4569',
        totalSlots: 15,
        availableBatteries: 3,
        chargingBatteries: 10
      },
      {
        id: 4,
        stationNo: 'ST004',
        stationName: '望京换电站',
        address: '朝阳区望京SOHO T1',
        status: 'CLOSED',
        businessHours: '维护中',
        totalSlots: 25,
        availableBatteries: 0,
        chargingBatteries: 0
      }
    ]
  }
}

onMounted(() => {
  loadStations()
})
</script>

<style scoped>
.station-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 15px;
  
  h3 {
    margin: 0;
    font-size: 20px;
    color: #fff;
    font-weight: 600;
  }
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 10px;
  margin-top: -20px;
  position: relative;
  z-index: 10;
  
  .stat-card {
    background: #fff;
    border-radius: 12px;
    padding: 15px;
    display: flex;
    align-items: center;
    gap: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    
    .stat-icon {
      font-size: 32px;
    }
    
    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #333;
      }
      
      .stat-label {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }
  }
}

.search-bar {
  padding: 10px 15px;
}

.station-list {
  padding: 0 15px;
}

.station-card {
  background: #fff;
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  
  .station-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    
    .station-name {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
  }
  
  .station-info {
    margin-bottom: 15px;
    
    .info-item {
      display: flex;
      align-items: flex-start;
      gap: 8px;
      margin-bottom: 8px;
      font-size: 13px;
      color: #666;
      
      .info-icon {
        font-size: 14px;
        min-width: 16px;
      }
      
      .info-text {
        flex: 1;
      }
    }
  }
  
  .battery-stats {
    display: flex;
    justify-content: space-around;
    padding-top: 15px;
    border-top: 1px solid #f0f0f0;
    
    .battery-item {
      text-align: center;
      
      .battery-value {
        font-size: 20px;
        font-weight: bold;
        
        &.available { color: #67c23a; }
        &.charging { color: #e6a23c; }
        &.total { color: #909399; }
      }
      
      .battery-label {
        font-size: 12px;
        color: #999;
        margin-top: 4px;
      }
    }
  }
}

.detail-content {
  .detail-row {
    display: flex;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    
    .label {
      width: 100px;
      color: #666;
      flex-shrink: 0;
    }
    
    .value {
      flex: 1;
      color: #333;
    }
  }
  
  .battery-progress {
    margin-top: 20px;
    
    .progress-label {
      font-size: 14px;
      color: #333;
      margin-bottom: 10px;
    }
  }
}
</style>
