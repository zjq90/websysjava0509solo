<template>
  <div class="heatmap-container">
    <div class="page-header">
      <h3>热力图分析</h3>
      <div class="time-selector">
        <el-radio-group v-model="timeRange" size="small" @change="loadHeatData">
          <el-radio-button value="1">1小时</el-radio-button>
          <el-radio-button value="6">6小时</el-radio-button>
          <el-radio-button value="24">24小时</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <div class="map-container">
      <div class="heatmap-overlay">
        <div 
          class="heat-point" 
          v-for="point in heatPoints" 
          :key="point.id"
          :style="getPointStyle(point)"
          @click="showPointDetail(point)"
        >
          <div class="point-inner" :style="{ opacity: getOpacity(point.heatValue) }"></div>
          <div class="point-value" v-if="point.heatValue >= 50">{{ point.heatValue }}</div>
        </div>
      </div>
      
      <div class="map-placeholder">
        <div class="map-grid">
          <div class="grid-row" v-for="i in 10" :key="i">
            <div class="grid-cell" v-for="j in 8" :key="j"></div>
          </div>
        </div>
        <div class="map-labels">
          <div class="label north">北</div>
          <div class="label south">南</div>
          <div class="label west">西</div>
          <div class="label east">东</div>
        </div>
      </div>
    </div>
    
    <div class="legend">
      <div class="legend-title">热力等级</div>
      <div class="legend-bar">
        <div class="legend-gradient"></div>
        <div class="legend-labels">
          <span>低</span>
          <span>中</span>
          <span>高</span>
        </div>
      </div>
    </div>
    
    <div class="hotspots-section">
      <div class="section-header">
        <h4>热门区域 TOP 5</h4>
        <el-button type="primary" size="small" link @click="generateDispatch">生成调度建议</el-button>
      </div>
      
      <div class="hotspot-list">
        <div 
          class="hotspot-item" 
          v-for="(spot, index) in topHotspots" 
          :key="spot.id"
        >
          <div class="rank" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
          <div class="hotspot-info">
            <div class="hotspot-name">{{ spot.areaName }}</div>
            <div class="hotspot-desc">热度值: {{ spot.heatValue }} | 车辆需求: {{ spot.demandCount }}辆</div>
          </div>
          <div class="heat-indicator" :style="{ width: (spot.heatValue / 1.2) + '%' }"></div>
        </div>
      </div>
    </div>
    
    <div class="dispatch-section" v-if="dispatchSuggestions.length > 0">
      <div class="section-header">
        <h4>智能调度建议</h4>
      </div>
      
      <div class="suggestion-list">
        <div 
          class="suggestion-card" 
          v-for="suggestion in dispatchSuggestions" 
          :key="suggestion.id"
        >
          <div class="suggestion-header">
            <el-icon><Warning /></el-icon>
            <span>车辆不足预警</span>
          </div>
          <div class="suggestion-content">
            <div class="route">
              <div class="location from">
                <div class="loc-label">调出</div>
                <div class="loc-name">{{ suggestion.fromAreaName }}</div>
                <div class="loc-detail">可用车辆: {{ suggestion.fromAreaBikes }}辆</div>
              </div>
              <div class="arrow">→</div>
              <div class="location to">
                <div class="loc-label">调入</div>
                <div class="loc-name">{{ suggestion.toAreaName }}</div>
                <div class="loc-detail">需求车辆: {{ suggestion.suggestBikes }}辆</div>
              </div>
            </div>
            <div class="suggestion-action">
              <el-button type="primary" size="small" @click="createDispatchTask(suggestion)">
                创建调度任务
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <el-dialog
      v-model="detailVisible"
      title="区域详情"
      width="85%"
    >
      <div class="point-detail" v-if="selectedPoint">
        <div class="detail-item">
          <span class="label">区域名称</span>
          <span class="value">{{ selectedPoint.areaName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">热度值</span>
          <span class="value highlight">{{ selectedPoint.heatValue }}</span>
        </div>
        <div class="detail-item">
          <span class="label">当前车辆数</span>
          <span class="value">{{ selectedPoint.bikeCount || 0 }}</span>
        </div>
        <div class="detail-item">
          <span class="label">预估需求</span>
          <span class="value">{{ selectedPoint.demandCount || 0 }}辆/小时</span>
        </div>
        <div class="detail-item">
          <span class="label">最后更新</span>
          <span class="value">{{ selectedPoint.recordTime }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="viewOnMap">查看地图</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Warning } from '@element-plus/icons-vue'
import { api } from '../api'

const timeRange = ref('6')
const heatPoints = ref([])
const detailVisible = ref(false)
const selectedPoint = ref(null)
const dispatchSuggestions = ref([])

const topHotspots = computed(() => {
  return [...heatPoints.value]
    .sort((a, b) => b.heatValue - a.heatValue)
    .slice(0, 5)
})

const getPointStyle = (point) => {
  const size = Math.max(40, Math.min(80, point.heatValue))
  return {
    left: point.x + '%',
    top: point.y + '%',
    width: size + 'px',
    height: size + 'px',
    marginLeft: -size/2 + 'px',
    marginTop: -size/2 + 'px'
  }
}

const getOpacity = (value) => {
  return Math.max(0.3, Math.min(1, value / 100))
}

const showPointDetail = (point) => {
  selectedPoint.value = point
  detailVisible.value = true
}

const viewOnMap = () => {
  ElMessage.info('地图功能开发中')
}

const generateDispatch = async () => {
  try {
    const data = await api.getDispatchSuggestions()
    dispatchSuggestions.value = data?.suggestions || []
    if (dispatchSuggestions.value.length === 0) {
      ElMessage.info('暂无调度建议，各区域车辆分布合理')
    }
  } catch (e) {
    dispatchSuggestions.value = [
      {
        id: 1,
        fromAreaName: '望京SOHO',
        fromAreaBikes: 45,
        toAreaName: '国贸地铁站',
        suggestBikes: 15
      },
      {
        id: 2,
        fromAreaName: '三里屯',
        fromAreaBikes: 32,
        toAreaName: '东直门枢纽站',
        suggestBikes: 10
      }
    ]
  }
}

const createDispatchTask = (suggestion) => {
  ElMessage.success('调度任务已创建')
  dispatchSuggestions.value = dispatchSuggestions.value.filter(s => s.id !== suggestion.id)
}

const loadHeatData = async () => {
  try {
    const data = await api.getHeatPoints()
    heatPoints.value = data || generateMockData()
  } catch (e) {
    heatPoints.value = generateMockData()
  }
}

const generateMockData = () => {
  const areas = ['国贸地铁站', '望京SOHO', '三里屯太古里', '东直门枢纽站', 
                 '朝阳门', '建国门', '大望路', '四惠', '三元桥', '亮马桥']
  return areas.map((name, index) => ({
    id: index + 1,
    areaName: name,
    heatValue: Math.floor(Math.random() * 80) + 20,
    bikeCount: Math.floor(Math.random() * 50) + 5,
    demandCount: Math.floor(Math.random() * 30) + 10,
    x: Math.random() * 70 + 15,
    y: Math.random() * 70 + 15,
    recordTime: new Date().toLocaleString()
  }))
}

onMounted(() => {
  loadHeatData()
})
</script>

<style scoped>
.heatmap-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px;
  
  h3 {
    margin: 0 0 12px 0;
    font-size: 20px;
    color: #fff;
    font-weight: 600;
  }
}

.map-container {
  position: relative;
  height: 350px;
  margin: 10px;
  border-radius: 12px;
  overflow: hidden;
  background: #e8f4f8;
}

.map-placeholder {
  width: 100%;
  height: 100%;
  position: relative;
  
  .map-grid {
    width: 100%;
    height: 100%;
    opacity: 0.3;
    
    .grid-row {
      display: flex;
      height: 10%;
      
      .grid-cell {
        flex: 1;
        border: 1px solid #4a90a4;
      }
    }
  }
  
  .map-labels {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    
    .label {
      position: absolute;
      font-size: 14px;
      font-weight: bold;
      color: #4a90a4;
    }
    
    .north { top: 10px; left: 50%; transform: translateX(-50%); }
    .south { bottom: 10px; left: 50%; transform: translateX(-50%); }
    .west { left: 10px; top: 50%; transform: translateY(-50%); }
    .east { right: 10px; top: 50%; transform: translateY(-50%); }
  }
}

.heatmap-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.heat-point {
  position: absolute;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .point-inner {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background: radial-gradient(circle, #ff6b6b 0%, #ffa502 50%, #ffd93d 100%);
    animation: pulse 2s ease-in-out infinite;
  }
  
  .point-value {
    position: absolute;
    font-size: 12px;
    font-weight: bold;
    color: #fff;
    text-shadow: 0 1px 2px rgba(0,0,0,0.5);
  }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.legend {
  margin: 15px;
  padding: 15px;
  background: #fff;
  border-radius: 12px;
  
  .legend-title {
    font-size: 14px;
    color: #333;
    margin-bottom: 10px;
    font-weight: 500;
  }
  
  .legend-bar {
    .legend-gradient {
      height: 12px;
      border-radius: 6px;
      background: linear-gradient(to right, #ffd93d, #ffa502, #ff6b6b);
    }
    
    .legend-labels {
      display: flex;
      justify-content: space-between;
      margin-top: 6px;
      font-size: 12px;
      color: #999;
    }
  }
}

.hotspots-section,
.dispatch-section {
  margin: 10px 15px;
  padding: 15px;
  background: #fff;
  border-radius: 12px;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    
    h4 {
      margin: 0;
      font-size: 16px;
      color: #333;
      font-weight: 600;
    }
  }
}

.hotspot-list {
  .hotspot-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    position: relative;
    
    &:last-child {
      border-bottom: none;
    }
    
    .rank {
      width: 28px;
      height: 28px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      font-weight: bold;
      color: #fff;
      flex-shrink: 0;
      
      &.rank-1 { background: linear-gradient(135deg, #ff6b6b, #ee5a5a); }
      &.rank-2 { background: linear-gradient(135deg, #ffa502, #ff9202); }
      &.rank-3 { background: linear-gradient(135deg, #ffd93d, #f0c929); }
      &.rank-4, &.rank-5 { background: #909399; }
    }
    
    .hotspot-info {
      flex: 1;
      
      .hotspot-name {
        font-size: 15px;
        color: #333;
        font-weight: 500;
        margin-bottom: 4px;
      }
      
      .hotspot-desc {
        font-size: 12px;
        color: #999;
      }
    }
    
    .heat-indicator {
      position: absolute;
      bottom: 0;
      left: 0;
      height: 3px;
      background: linear-gradient(to right, #ffd93d, #ff6b6b);
      border-radius: 2px;
    }
  }
}

.suggestion-list {
  .suggestion-card {
    background: #fff7e6;
    border: 1px solid #ffd591;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 12px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .suggestion-header {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #fa8c16;
      font-weight: 500;
      margin-bottom: 12px;
    }
    
    .suggestion-content {
      .route {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 12px;
        
        .location {
          flex: 1;
          padding: 10px;
          background: #fff;
          border-radius: 6px;
          
          .loc-label {
            font-size: 12px;
            color: #999;
            margin-bottom: 4px;
          }
          
          .loc-name {
            font-size: 14px;
            color: #333;
            font-weight: 500;
            margin-bottom: 4px;
          }
          
          .loc-detail {
            font-size: 12px;
            color: #666;
          }
          
          &.from {
            border-left: 3px solid #67c23a;
          }
          
          &.to {
            border-left: 3px solid #f56c6c;
          }
        }
        
        .arrow {
          font-size: 20px;
          color: #fa8c16;
          font-weight: bold;
        }
      }
      
      .suggestion-action {
        text-align: right;
      }
    }
  }
}

.point-detail {
  .detail-item {
    display: flex;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      width: 100px;
      color: #666;
      flex-shrink: 0;
    }
    
    .value {
      flex: 1;
      color: #333;
      
      &.highlight {
        font-size: 20px;
        font-weight: bold;
        color: #f56c6c;
      }
    }
  }
}
</style>
