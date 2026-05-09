<template>
  <view class="container">
    <view class="scan-header card">
      <view class="scan-icon-wrapper">
        <text class="scan-icon">📷</text>
      </view>
      <text class="scan-title">扫码查询批次</text>
      <text class="scan-desc">扫描批次二维码或手动输入批次编号</text>
    </view>

    <view class="input-section card">
      <text class="section-title">手动输入批次号</text>
      <view class="input-row">
        <input 
          class="manual-input" 
          type="text" 
          v-model="batchNo" 
          placeholder="请输入8位批次编号"
          maxlength="8"
        />
        <view class="search-btn" @click="searchByNo">
          <text>查询</text>
        </view>
      </view>
    </view>

    <view class="btn-primary scan-btn" @click="doScan">
      <text>📷 扫码查询</text>
    </view>

    <view class="history-section">
      <view class="section-header">
        <text class="section-title">近期查询</text>
        <text class="clear-btn" @click="clearHistory">清空</text>
      </view>
      <view v-if="history.length === 0" class="empty-history">
        <text>暂无查询记录</text>
      </view>
      <view v-else>
        <view 
          v-for="(item, index) in history" 
          :key="index" 
          class="history-item card"
          @click="searchHistory(item)"
        >
          <text class="history-no">{{ item.batchNo }}</text>
          <text class="history-product">{{ item.productName }}</text>
          <text class="history-date">{{ item.time }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      batchNo: '',
      history: []
    }
  },
  onShow() {
    this.loadHistory()
  },
  methods: {
    loadHistory() {
      const saved = uni.getStorageSync('scanHistory')
      this.history = saved || []
    },
    saveToHistory(batch) {
      const item = {
        batchNo: batch.batchNo,
        productName: batch.productName,
        id: batch.id,
        time: new Date().toLocaleString('zh-CN')
      }
      let history = uni.getStorageSync('scanHistory') || []
      history = history.filter(h => h.batchNo !== item.batchNo)
      history.unshift(item)
      if (history.length > 10) {
        history = history.slice(0, 10)
      }
      uni.setStorageSync('scanHistory', history)
      this.history = history
    },
    clearHistory() {
      uni.removeStorageSync('scanHistory')
      this.history = []
    },
    searchHistory(item) {
      this.batchNo = item.batchNo
      this.searchByNo()
    },
    async searchByNo() {
      if (!this.batchNo) {
        uni.showToast({
          title: '请输入批次编号',
          icon: 'none'
        })
        return
      }
      uni.showLoading({ title: '查询中...' })
      try {
        const res = await api.getBatchByNo(this.batchNo)
        uni.hideLoading()
        if (res.data) {
          this.saveToHistory(res.data)
          uni.navigateTo({
            url: `/pages/batch/detail?id=${res.data.id}`
          })
        }
      } catch (e) {
        uni.hideLoading()
      }
    },
    doScan() {
      uni.scanCode({
        onlyFromCamera: false,
        scanType: ['qrCode', 'barCode'],
        success: (res) => {
          this.batchNo = res.result
          this.searchByNo()
        },
        fail: () => {
          uni.showToast({
            title: '扫码失败',
            icon: 'none'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.scan-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 30rpx;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
}

.scan-icon-wrapper {
  width: 140rpx;
  height: 140rpx;
  background-color: rgba(255, 255, 255, 0.25);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24rpx;
}

.scan-icon {
  font-size: 70rpx;
}

.scan-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 12rpx;
}

.scan-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.85);
}

.section-title {
  font-size: 28rpx;
  color: #606266;
  margin-bottom: 20rpx;
  display: block;
}

.input-row {
  display: flex;
  gap: 20rpx;
}

.manual-input {
  flex: 1;
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 30rpx;
}

.search-btn {
  width: 140rpx;
  height: 88rpx;
  background-color: #409eff;
  color: #ffffff;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.scan-btn {
  margin-top: 24rpx;
  margin-bottom: 40rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.clear-btn {
  font-size: 26rpx;
  color: #409eff;
}

.empty-history {
  text-align: center;
  padding: 60rpx 0;
  font-size: 28rpx;
  color: #909399;
}

.history-item {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.history-no {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
}

.history-product {
  font-size: 26rpx;
  color: #606266;
}

.history-date {
  font-size: 22rpx;
  color: #c0c4cc;
}
</style>
