<template>
  <div class="battery-container">
    <div class="header">
      <div class="header-title">电池管理</div>
      <div class="stats-row">
        <div class="stat-card">
          <span class="stat-icon">⚠️</span>
          <span class="stat-value">{{ warningCount }}</span>
          <span class="stat-label">低电量预警</span>
        </div>
        <div class="stat-card">
          <span class="stat-icon">🔋</span>
          <span class="stat-value">{{ totalBatteries }}</span>
          <span class="stat-label">电池总数</span>
        </div>
        <div class="stat-card">
          <span class="stat-icon">✅</span>
          <span class="stat-value">{{ healthyCount }}</span>
          <span class="stat-label">健康电池</span>
        </div>
      </div>
    </div>
    
    <div class="section">
      <div class="section-header">
        <span class="section-title">低电量预警车辆</span>
        <span class="section-more">查看全部</span>
      </div>
      <div class="warning-list">
        <div 
          class="warning-item" 
          v-for="item in lowBatteryList" 
          :key="item.id"
          @click="goToBikeDetail(item)"
        >
          <div class="bike-icon">🚲</div>
          <div class="warning-info">
            <div class="bike-no">{{ item.bikeNo }}</div>
            <div class="bike-location">{{ item.location }}</div>
          </div>
          <el-tag type="danger" size="small">🔋 {{ item.batteryLevel }}%</el-tag>
        </div>
      </div>
    </div>
    
    <div class="section">
      <div class="section-header">
        <span class="section-title">电池健康度统计</span>
      </div>
      <div class="health-stats">
        <div class="health-item">
          <div class="health-bar">
            <div class="health-fill excellent" style="width: 65%"></div>
          </div>
          <span class="health-label">优秀 (≥80%)</span>
          <span class="health-count">65块</span>
        </div>
        <div class="health-item">
          <div class="health-bar">
            <div class="health-fill good" style="width: 25%"></div>
          </div>
          <span class="health-label">良好 (60-79%)</span>
          <span class="health-count">25块</span>
        </div>
        <div class="health-item">
          <div class="health-bar">
            <div class="health-fill average" style="width: 8%"></div>
          </div>
          <span class="health-label">一般 (40-59%)</span>
          <span class="health-count">8块</span>
        </div>
        <div class="health-item">
          <div class="health-bar">
            <div class="health-fill poor" style="width: 2%"></div>
          </div>
          <span class="health-label">较差 (<40%)</span>
          <span class="health-count">2块</span>
        </div>
      </div>
    </div>
    
    <div class="quick-actions">
      <el-button type="primary" @click="goToStations">
        <el-icon><OfficeBuilding /></el-icon>
        换电站管理
      </el-button>
      <el-button type="success" @click="goToBatteryDetail">
        <el-icon><DataAnalysis /></el-icon>
        电池详情
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { OfficeBuilding, DataAnalysis } from '@element-plus/icons-vue'
import { api } from '../api'

const router = useRouter()
const batteries = ref([])
const lowBatteryList = ref([])

const warningCount = computed(() => lowBatteryList.value.length)
const totalBatteries = computed(() => 100)
const healthyCount = computed(() => 65)

const loadData = async () => {
  try {
    const [batList, warnings] = await Promise.all([
      api.getBatteries(),
      api.getLowBatteryWarning()
    ])
    batteries.value = batList || []
    lowBatteryList.value = (warnings || []).slice(0, 5)
  } catch (e) {
    lowBatteryList.value = [
      { id: 1, bikeNo: 'B001', location: '地铁站A出口', batteryLevel: 15 },
      { id: 2, bikeNo: 'B023', location: '商圈B入口', batteryLevel: 18 },
      { id: 3, bikeNo: 'B045', location: '小区C门口', batteryLevel: 12 }
    ]
  }
}

const goToBikeDetail = (item) => {
  router.push(`/bike-detail/${item.bikeId || item.id}`)
}

const goToStations = () => {
  router.push('/station')
}

const goToBatteryDetail = () => {
  ElMessage.success('电池详情页面')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.battery-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 15px;
  
  .header-title {
    font-size: 18px;
    font-weight: bold;
    color: #fff;
    margin-bottom: 15px;
  }
  
  .stats-row {
    display: flex;
    gap: 10px;
    
    .stat-card {
      flex: 1;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 12px;
      padding: 15px 10px;
      text-align: center;
      
      .stat-icon {
        font-size: 24px;
        display: block;
        margin-bottom: 5px;
      }
      
      .stat-value {
        display: block;
        font-size: 24px;
        font-weight: bold;
        color: #fff;
      }
      
      .stat-label {
        display: block;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 5px;
      }
    }
  }
}

.section {
  background: #fff;
  margin: 10px;
  border-radius: 12px;
  padding: 15px;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    
    .section-title {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
    
    .section-more {
      font-size: 12px;
      color: #667eea;
    }
  }
}

.warning-list {
  .warning-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .bike-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: #fff3e0;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      margin-right: 12px;
    }
    
    .warning-info {
      flex: 1;
      
      .bike-no {
        font-size: 14px;
        color: #333;
        margin-bottom: 5px;
      }
      
      .bike-location {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

.health-stats {
  .health-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    
    .health-bar {
      width: 120px;
      height: 12px;
      background: #f0f0f0;
      border-radius: 6px;
      overflow: hidden;
      margin-right: 12px;
      
      .health-fill {
        height: 100%;
        border-radius: 6px;
        
        &.excellent { background: #67c23a; }
        &.good { background: #409eff; }
        &.average { background: #e6a23c; }
        &.poor { background: #f56c6c; }
      }
    }
    
    .health-label {
      flex: 1;
      font-size: 13px;
      color: #666;
    }
    
    .health-count {
      font-size: 13px;
      color: #333;
      font-weight: 500;
    }
  }
}

.quick-actions {
  display: flex;
  gap: 10px;
  padding: 0 10px;
  
  .el-button {
    flex: 1;
  }
}
</style>
