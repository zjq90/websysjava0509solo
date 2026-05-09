<template>
  <view class="container" v-if="batch">
    <view class="batch-info card">
      <view class="info-header">
        <text class="batch-no">{{ batch.batchNo }}</text>
        <view :class="getStatusClass(batch.status)">
          {{ getStatusText(batch.status) }}
        </view>
      </view>
      <view class="info-body">
        <view class="info-row">
          <text class="info-label">产品名称</text>
          <text class="info-value">{{ batch.productName }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">生产数量</text>
          <text class="info-value">{{ batch.quantity }} {{ batch.unit }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">保质期</text>
          <text class="info-value">{{ batch.shelfLife }}</text>
        </view>
        <view class="info-row" v-if="batch.customerName">
          <text class="info-label">客户名称</text>
          <text class="info-value">{{ batch.customerName }}</text>
        </view>
        <view class="info-row" v-if="batch.customerPhoneEncrypted">
          <text class="info-label">联系电话</text>
          <text class="info-value">{{ batch.customerPhoneEncrypted }}</text>
        </view>
        <view class="info-row" v-if="batch.remark">
          <text class="info-label">备注</text>
          <text class="info-value">{{ batch.remark }}</text>
        </view>
      </view>
    </view>

    <view class="section-title">
      <text>📋 生产进度</text>
    </view>

    <view class="progress-card card">
      <view class="progress-timeline">
        <view 
          v-for="(stage, index) in stages" 
          :key="stage.id" 
          class="timeline-item"
        >
          <view class="timeline-left">
            <view class="timeline-dot" :class="getDotClass(stage.status)"></view>
            <view v-if="index < stages.length - 1" class="timeline-line" :class="getLineClass(stage.status)"></view>
          </view>
          <view class="timeline-content" @click="handleStageClick(stage)">
            <view class="timeline-header">
              <text class="stage-name">{{ stage.stageName }}</text>
              <view :class="getStatusClass(stage.status)">
                {{ getStageStatusText(stage.status) }}
              </view>
            </view>
            <view class="timeline-details" v-if="stage.status !== 'PENDING'">
              <view class="detail-row" v-if="stage.operatorName">
                <text class="detail-label">操作人：</text>
                <text class="detail-value">{{ stage.operatorName }}</text>
              </view>
              <view class="detail-row" v-if="stage.startTime">
                <text class="detail-label">开始时间：</text>
                <text class="detail-value">{{ formatDate(stage.startTime) }}</text>
              </view>
              <view class="detail-row" v-if="stage.endTime">
                <text class="detail-label">结束时间：</text>
                <text class="detail-value">{{ formatDate(stage.endTime) }}</text>
              </view>
              <view class="detail-row" v-if="stage.processParams">
                <text class="detail-label">工艺参数：</text>
                <text class="detail-value">{{ stage.processParams }}</text>
              </view>
            </view>
            <view class="timeline-actions" v-if="stage.status === 'PENDING' || stage.status === 'IN_PROGRESS'">
              <view 
                v-if="stage.status === 'PENDING' && canStartStage(index)" 
                class="action-btn start" 
                @click.stop="startStage(stage)"
              >
                <text>开始</text>
              </view>
              <view 
                v-if="stage.status === 'IN_PROGRESS'" 
                class="action-btn complete" 
                @click.stop="completeStage(stage)"
              >
                <text>完成</text>
              </view>
              <view 
                v-if="stage.status === 'IN_PROGRESS'" 
                class="action-btn inspection" 
                @click.stop="goToInspection(stage)"
              >
                <text>质检</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="section-title">
      <text>🔬 质检记录</text>
    </view>

    <view class="inspection-card card" v-if="inspections.length > 0">
      <view v-for="inspection in inspections" :key="inspection.id" class="inspection-item">
        <view class="inspection-header">
          <text class="inspection-stage">{{ getStageName(inspection.stageCode) }}</text>
          <view :class="inspection.result === 'PASS' ? 'status-completed' : 'status-fail'">
            {{ inspection.result === 'PASS' ? '合格' : '不合格' }}
          </view>
        </view>
        <view class="inspection-data">
          <view class="data-item">
            <text class="data-label">水分</text>
            <text class="data-value">{{ inspection.moisture }}%</text>
          </view>
          <view class="data-item">
            <text class="data-label">净度</text>
            <text class="data-value">{{ inspection.purity }}%</text>
          </view>
          <view class="data-item">
            <text class="data-label">发芽率</text>
            <text class="data-value">{{ inspection.germinationRate }}%</text>
          </view>
        </view>
        <view class="inspection-footer">
          <text>质检人：{{ inspection.operatorName || '-' }}</text>
          <text>{{ formatDate(inspection.createTime) }}</text>
        </view>
      </view>
    </view>

    <view class="empty-inspection card" v-else>
      <text class="empty-icon">📝</text>
      <text class="empty-text">暂无质检记录</text>
    </view>

    <view class="action-bar">
      <view class="action-btn-primary" @click="generateReport">
        <text>生成加工报告</text>
      </view>
      <view class="action-btn-secondary" @click="viewReport">
        <text>查看报告</text>
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
      batch: null,
      stages: [],
      inspections: [],
      userInfo: null
    }
  },
  onLoad(options) {
    this.batchId = options.id
    this.userInfo = uni.getStorageSync('userInfo')
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const [batchRes, stagesRes, inspectionRes] = await Promise.all([
          api.getBatchById(this.batchId),
          api.getStagesByBatch(this.batchId),
          api.getInspectionsByBatch(this.batchId)
        ])
        this.batch = batchRes.data
        this.stages = stagesRes.data || []
        this.inspections = inspectionRes.data || []
      } catch (e) {
        console.error(e)
      }
    },
    getStatusClass(status) {
      if (status === 'IN_PROGRESS') return 'status-progress'
      if (status === 'COMPLETED') return 'status-completed'
      return 'status-pending'
    },
    getStatusText(status) {
      if (status === 'IN_PROGRESS') return '进行中'
      if (status === 'COMPLETED') return '已完成'
      if (status === 'PENDING') return '待开始'
      return status
    },
    getStageStatusText(status) {
      if (status === 'IN_PROGRESS') return '进行中'
      if (status === 'COMPLETED') return '已完成'
      if (status === 'PENDING') return '待开始'
      return status
    },
    getDotClass(status) {
      if (status === 'COMPLETED') return 'dot-done'
      if (status === 'IN_PROGRESS') return 'dot-active'
      return 'dot-pending'
    },
    getLineClass(status) {
      if (status === 'COMPLETED') return 'line-done'
      return 'line-pending'
    },
    getStageName(code) {
      const map = {
        'CLEANING': '清选',
        'COATING': '包衣',
        'PACKAGING': '分装',
        'INSPECTION': '质检'
      }
      return map[code] || code
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      return dateStr.replace('T', ' ').substring(0, 16)
    },
    canStartStage(index) {
      if (index === 0) return true
      const prevStage = this.stages[index - 1]
      return prevStage && prevStage.status === 'COMPLETED'
    },
    async startStage(stage) {
      uni.showModal({
        title: '确认开始',
        content: `确定要开始【${stage.stageName}】环节吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const data = {
                batchId: parseInt(this.batchId),
                stageCode: stage.stageCode,
                operatorId: this.userInfo && this.userInfo.id ? this.userInfo.id : null,
                operatorName: this.userInfo && this.userInfo.realName ? this.userInfo.realName : null,
                processParams: stage.stageCode === 'CLEANING' ? '{"cleaningSpeed":"500kg/h"}' : 
                                stage.stageCode === 'COATING' ? '{"coatingType":"红色包衣剂"}' :
                                stage.stageCode === 'PACKAGING' ? '{"packageType":"编织袋"}' : ''
              }
              await api.startStage(data)
              uni.showToast({
                title: '开始成功',
                icon: 'success'
              })
              this.loadData()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    async completeStage(stage) {
      uni.showModal({
        title: '确认完成',
        content: `确定要完成【${stage.stageName}】环节吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const data = {
                batchId: parseInt(this.batchId),
                stageCode: stage.stageCode,
                operatorId: this.userInfo && this.userInfo.id ? this.userInfo.id : null,
                operatorName: this.userInfo && this.userInfo.realName ? this.userInfo.realName : null
              }
              await api.completeStage(data)
              uni.showToast({
                title: '完成成功',
                icon: 'success'
              })
              this.loadData()
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    goToInspection(stage) {
      uni.navigateTo({
        url: `/pages/inspection/inspection?batchId=${this.batchId}&stageCode=${stage.stageCode}&stageName=${encodeURIComponent(stage.stageName)}`
      })
    },
    handleStageClick(stage) {
    },
    async generateReport() {
      uni.showLoading({
        title: '生成中...'
      })
      try {
        await api.generateReport(this.batchId, this.userInfo && this.userInfo.id ? this.userInfo.id : 1)
        uni.hideLoading()
        uni.showToast({
          title: '报告生成成功',
          icon: 'success'
        })
        setTimeout(() => {
          this.viewReport()
        }, 500)
      } catch (e) {
        uni.hideLoading()
        console.error(e)
      }
    },
    viewReport() {
      uni.navigateTo({
        url: `/pages/report/report?batchId=${this.batchId}`
      })
    }
  }
}
</script>

<style scoped>
.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
  margin: 24rpx 0 16rpx 8rpx;
}

.batch-info .info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.batch-no {
  font-size: 36rpx;
  font-weight: bold;
  color: #303133;
}

.info-body .info-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
}

.info-label {
  font-size: 28rpx;
  color: #909399;
}

.info-value {
  font-size: 28rpx;
  color: #303133;
}

.progress-timeline {
  padding: 10rpx 0;
}

.timeline-item {
  display: flex;
  margin-bottom: 20rpx;
}

.timeline-left {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 20rpx;
}

.timeline-dot {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  border: 4rpx solid #dcdfe6;
  background-color: #ffffff;
  z-index: 1;
}

.timeline-dot.dot-done {
  background-color: #67c23a;
  border-color: #67c23a;
}

.timeline-dot.dot-active {
  background-color: #409eff;
  border-color: #409eff;
}

.timeline-dot.dot-pending {
  background-color: #f5f7fa;
  border-color: #dcdfe6;
}

.timeline-line {
  width: 4rpx;
  flex: 1;
  min-height: 80rpx;
  background-color: #ebeef5;
  margin-top: 4rpx;
}

.timeline-line.line-done {
  background-color: #67c23a;
}

.timeline-content {
  flex: 1;
  background-color: #fafafa;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 10rpx;
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.stage-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
}

.timeline-details .detail-row {
  display: flex;
  padding: 6rpx 0;
}

.detail-label {
  font-size: 24rpx;
  color: #909399;
}

.detail-value {
  font-size: 24rpx;
  color: #606266;
}

.timeline-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 16rpx;
}

.action-btn {
  flex: 1;
  height: 64rpx;
  line-height: 64rpx;
  border-radius: 32rpx;
  text-align: center;
  font-size: 26rpx;
}

.action-btn.start {
  background-color: #409eff;
  color: #ffffff;
}

.action-btn.complete {
  background-color: #67c23a;
  color: #ffffff;
}

.action-btn.inspection {
  background-color: #e6a23c;
  color: #ffffff;
}

.inspection-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.inspection-item:last-child {
  border-bottom: none;
}

.inspection-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.inspection-stage {
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
}

.inspection-data {
  display: flex;
  justify-content: space-around;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx 0;
  margin-bottom: 12rpx;
}

.data-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.data-label {
  font-size: 24rpx;
  color: #909399;
  margin-bottom: 8rpx;
}

.data-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #409eff;
}

.inspection-footer {
  display: flex;
  justify-content: space-between;
  font-size: 24rpx;
  color: #909399;
}

.empty-inspection {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #909399;
}

.action-bar {
  display: flex;
  gap: 20rpx;
  padding: 24rpx 0;
  margin-top: 20rpx;
}

.action-btn-primary {
  flex: 1;
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
  color: #ffffff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.action-btn-secondary {
  flex: 1;
  background-color: #ffffff;
  color: #409eff;
  border: 2rpx solid #409eff;
  border-radius: 48rpx;
  height: 88rpx;
  line-height: 84rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 500;
}

.status-fail {
  color: #f56c6c;
  background-color: #fef0f0;
  padding: 4rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}
</style>
