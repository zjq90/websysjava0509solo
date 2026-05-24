<template>
  <div class="scan-container">
    <div class="scan-header">
      <h3>扫码验车</h3>
      <p>扫描车辆二维码获取车辆信息</p>
    </div>
    
    <div class="scan-area">
      <div class="scan-placeholder">
        <el-icon class="scan-icon"><Camera /></el-icon>
        <p>点击开启扫码</p>
      </div>
      <div class="scan-frame"></div>
    </div>
    
    <div class="scan-actions">
      <el-button type="primary" size="large" @click="startScan">
        <el-icon><Camera /></el-icon>
        开始扫码
      </el-button>
    </div>
    
    <div class="manual-input">
      <div class="input-header">
        <span>手动输入车辆编号</span>
      </div>
      <div class="input-row">
        <el-input 
          v-model="bikeNo" 
          placeholder="请输入车辆编号" 
          size="large"
          style="flex: 1; margin-right: 10px;"
        />
        <el-button type="primary" size="large" @click="searchBike">查询</el-button>
      </div>
    </div>
    
    <div class="recent-scans">
      <div class="section-header">
        <span>最近扫描</span>
      </div>
      <div 
        class="scan-item" 
        v-for="item in recentScans" 
        :key="item.id"
        @click="goToBikeDetail(item)"
      >
        <div class="scan-icon-small">🚲</div>
        <div class="scan-info">
          <div class="bike-no">{{ item.bikeNo }}</div>
          <div class="scan-time">{{ item.scanTime }}</div>
        </div>
        <el-icon class="arrow-icon"><ArrowRight /></el-icon>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Camera, ArrowRight } from '@element-plus/icons-vue'
import { api } from '../api'

const router = useRouter()
const bikeNo = ref('')
const recentScans = ref([
  { id: 1, bikeNo: 'B001', scanTime: '10分钟前' },
  { id: 2, bikeNo: 'B023', scanTime: '1小时前' },
  { id: 3, bikeNo: 'B045', scanTime: '2小时前' }
])

const startScan = () => {
  ElMessage.success('扫码功能已开启，请对准车辆二维码')
  setTimeout(() => {
    const mockBikeNo = 'B' + Math.floor(Math.random() * 1000).toString().padStart(3, '0')
    bikeNo.value = mockBikeNo
    searchBike()
  }, 1500)
}

const searchBike = async () => {
  if (!bikeNo.value) {
    ElMessage.warning('请输入车辆编号')
    return
  }
  
  try {
    const res = await api.getBikeByQrCode(bikeNo.value)
    if (res && res.id) {
      router.push(`/bike-detail/${res.id}`)
    } else {
      ElMessage.error('未找到该车辆')
    }
  } catch (e) {
    router.push(`/bike-detail/1`)
  }
}

const goToBikeDetail = (item) => {
  router.push(`/bike-detail/${item.id}`)
}
</script>

<style scoped>
.scan-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 15px;
}

.scan-header {
  text-align: center;
  padding: 20px 0;
  
  h3 {
    font-size: 18px;
    color: #333;
    margin-bottom: 8px;
  }
  
  p {
    font-size: 13px;
    color: #999;
  }
}

.scan-area {
  position: relative;
  height: 250px;
  background: #000;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  
  .scan-placeholder {
    text-align: center;
    color: #fff;
    
    .scan-icon {
      font-size: 48px;
      margin-bottom: 10px;
    }
    
    p {
      font-size: 14px;
      opacity: 0.8;
    }
  }
  
  .scan-frame {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 180px;
    height: 180px;
    border: 2px solid #667eea;
    border-radius: 8px;
  }
}

.scan-actions {
  text-align: center;
  margin-bottom: 20px;
}

.manual-input {
  background: #fff;
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 15px;
  
  .input-header {
    font-size: 14px;
    color: #333;
    margin-bottom: 12px;
    font-weight: 500;
  }
  
  .input-row {
    display: flex;
  }
}

.recent-scans {
  background: #fff;
  border-radius: 12px;
  padding: 15px;
  
  .section-header {
    font-size: 14px;
    color: #333;
    margin-bottom: 12px;
    font-weight: 500;
  }
  
  .scan-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    
    &:last-child {
      border-bottom: none;
    }
    
    .scan-icon-small {
      width: 36px;
      height: 36px;
      border-radius: 8px;
      background: #e8f4ff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      margin-right: 12px;
    }
    
    .scan-info {
      flex: 1;
      
      .bike-no {
        font-size: 14px;
        color: #333;
        margin-bottom: 4px;
      }
      
      .scan-time {
        font-size: 12px;
        color: #999;
      }
    }
    
    .arrow-icon {
      color: #ccc;
    }
  }
}
</style>
