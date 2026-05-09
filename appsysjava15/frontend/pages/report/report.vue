<template>
  <view class="container">
    <view v-if="!report" class="empty-report card">
      <text class="empty-icon">📄</text>
      <text class="empty-text">暂无加工报告</text>
      <view class="btn-primary generate-btn" @click="generateReport">
        <text>生成报告</text>
      </view>
    </view>

    <view v-else>
      <view class="report-header card">
        <text class="report-title">生产加工报告</text>
        <text class="report-no">报告编号：{{ report.reportNo }}</text>
        <text class="report-batch">批次号：{{ report.batchNo }}</text>
      </view>

      <view class="report-content card">
        <view class="content-text">{{ report.content }}</view>
      </view>

      <view class="action-bar">
        <view class="btn-primary" @click="generateReport">
          <text>重新生成</text>
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
      batchId: null,
      report: null,
      userInfo: null
    }
  },
  onLoad(options) {
    this.batchId = options.batchId
    this.userInfo = uni.getStorageSync('userInfo')
    this.loadReport()
  },
  methods: {
    async loadReport() {
      try {
        const res = await api.getReportByBatch(this.batchId)
        this.report = res.data
      } catch (e) {
        console.error(e)
      }
    },
    async generateReport() {
      uni.showLoading({ title: '生成中...' })
      try {
        const res = await api.generateReport(this.batchId, this.userInfo && this.userInfo.id ? this.userInfo.id : 1)
        this.report = res.data
        uni.hideLoading()
        uni.showToast({
          title: '生成成功',
          icon: 'success'
        })
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.empty-report {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #909399;
  margin-bottom: 40rpx;
}

.generate-btn {
  width: 280rpx;
}

.report-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 30rpx;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
}

.report-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 20rpx;
}

.report-no,
.report-batch {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 8rpx;
}

.report-content {
  padding: 30rpx;
}

.content-text {
  font-size: 26rpx;
  color: #303133;
  line-height: 2;
  white-space: pre-wrap;
  font-family: 'Courier New', monospace;
}

.action-bar {
  padding: 24rpx 0;
  margin-top: 20rpx;
}
</style>
