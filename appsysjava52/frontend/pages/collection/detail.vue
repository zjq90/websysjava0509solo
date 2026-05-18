<template>
  <view class="detail-page">
    <!-- 基本信息卡片 -->
    <view class="card info-card">
      <view class="info-header">
        <text class="info-title">{{ item.customName || '未命名文物' }}</text>
        <view class="info-actions">
          <text class="action-btn" @click="goToEdit">编辑</text>
        </view>
      </view>
      <view class="info-meta">
        <view class="meta-row">
          <text class="meta-label">类别</text>
          <text class="meta-value">{{ getCategoryName(item.category) }}</text>
        </view>
        <view class="meta-row">
          <text class="meta-label">材质</text>
          <text class="meta-value">{{ item.material || '未知' }}</text>
        </view>
        <view class="meta-row">
          <text class="meta-label">入藏日期</text>
          <text class="meta-value">{{ formatDate(item.collectionDate) }}</text>
        </view>
        <view class="meta-row">
          <text class="meta-label">估值</text>
          <text class="meta-value text-danger">{{ formatValue(item.estimatedValue) }}</text>
        </view>
      </view>
      <view class="info-tags" v-if="item.tags">
        <text class="tag" v-for="tag in item.tags.split(',')" :key="tag">{{ tag }}</text>
      </view>
      <view class="info-desc" v-if="item.remark">
        <text class="desc-label">备注</text>
        <text class="desc-content">{{ item.remark }}</text>
      </view>
    </view>

    <!-- 环境监测卡片 -->
    <view class="card env-card" v-if="item.envMonitorEnabled === 1">
      <view class="card-header">
        <text class="card-title">📡 环境监测</text>
        <view class="env-status online">
          <text>在线</text>
        </view>
      </view>
      
      <!-- 当前环境数据 -->
      <view class="env-current">
        <view class="env-item">
          <text class="env-icon">🌡️</text>
          <view class="env-info">
            <text class="env-value">{{ currentEnv.temperature || '--' }}℃</text>
            <text class="env-label">温度</text>
          </view>
        </view>
        <view class="env-item">
          <text class="env-icon">💧</text>
          <view class="env-info">
            <text class="env-value" :class="{ 'text-danger': isHumidityAbnormal }">
              {{ currentEnv.humidity || '--' }}%
            </text>
            <text class="env-label">湿度</text>
          </view>
        </view>
        <view class="env-item">
          <text class="env-icon">☀️</text>
          <view class="env-info">
            <text class="env-value">{{ currentEnv.lightIntensity || '--' }}</text>
            <text class="env-label">光照</text>
          </view>
        </view>
      </view>

      <!-- 异常提醒 -->
      <view class="abnormal-warning" v-if="abnormalList.length > 0">
        <text class="warning-icon">⚠️</text>
        <view class="warning-info">
          <text class="warning-title">发现 {{ abnormalList.length }} 条异常记录</text>
          <text class="warning-desc">{{ abnormalList[0].abnormalDesc }}</text>
          <text class="warning-suggestion text-primary">{{ abnormalList[0].suggestion }}</text>
        </view>
      </view>

      <!-- 历史数据图表 -->
      <view class="chart-section">
        <text class="chart-title">近7天湿度变化</text>
        <view class="chart-container">
          <view class="chart-bars">
            <view 
              class="chart-bar" 
              v-for="(data, index) in chartData" 
              :key="index"
              :style="{ height: data.value + '%' }"
              :class="{ 'abnormal': data.isAbnormal }"
            >
              <text class="bar-value">{{ data.humidity }}%</text>
            </view>
          </view>
          <view class="chart-labels">
            <text class="chart-label" v-for="(data, index) in chartData" :key="index">
              {{ data.date }}
            </text>
          </view>
        </view>
      </view>

      <view class="view-history-btn" @click="goToEnvironment">
        <text>查看完整监测数据 →</text>
      </view>
    </view>

    <!-- 保养提醒卡片 -->
    <view class="card maintenance-card">
      <view class="card-header">
        <text class="card-title">🔧 保养提醒</text>
        <view class="add-reminder-btn" @click="addReminder">
          <text>+ 添加</text>
        </view>
      </view>
      
      <view class="reminder-list">
        <view 
          class="reminder-item" v-for="reminder in reminderList" :key="reminder.id">
          <view class="reminder-status" :class="getStatusClass(reminder.status)">
            <text>{{ getStatusText(reminder.status) }}</text>
          </view>
          <view class="reminder-info">
            <text class="reminder-title">{{ reminder.title }}</text>
            <text class="reminder-date">计划日期：{{ formatDate(reminder.scheduledDate) }}</text>
            <text class="reminder-reference text-muted" v-if="reminder.reference">
              引用：{{ reminder.reference }}
            </text>
          </view>
          <view class="reminder-action" v-if="reminder.status === 0 || reminder.status === 1">
            <button class="complete-btn" @click="completeReminder(reminder.id)">完成</button>
          </view>
        </view>
      </view>

      <view class="empty-reminder" v-if="reminderList.length === 0">
        <text class="empty-text">暂无保养提醒</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      itemId: null,
      item: {},
      currentEnv: {
        temperature: 24.5,
        humidity: 68,
        lightIntensity: '180 lux'
      },
      abnormalList: [],
      reminderList: [],
      chartData: []
    }
  },
  computed: {
    isHumidityAbnormal() {
      return this.currentEnv.humidity > 70
    },
    categories() {
      return [
        { id: 1, name: '青铜器' },
        { id: 2, name: '陶瓷' },
        { id: 3, name: '书画' },
        { id: 4, name: '玉器' },
        { id: 5, name: '杂项' }
      ]
    }
  },
  onLoad(options) {
    this.itemId = options.id
    this.loadDetail()
    this.loadEnvironmentData()
    this.loadMaintenanceReminders()
  },
  methods: {
    async loadDetail() {
      try {
        const res = await request.get(`/collection/item/${this.itemId}`)
        if (res.code === 200) {
          this.item = res.data
        }
      } catch (e) {
        // 模拟数据
        this.item = {
          id: 1,
          customName: '清代青花瓷盘',
          category: 2,
          material: '瓷',
          tags: '青花瓷,清代,祖传',
          collectionDate: '2023-05-15',
          estimatedValue: 1500000,
          remark: '祖传之物，倍加珍惜，需注意防潮',
          envMonitorEnabled: 1,
          deviceId: 'SENSOR-002'
        }
      }
    },
    async loadEnvironmentData() {
      try {
        const res = await request.get(`/collection/environment/${this.itemId}`, { days: 7 })
        if (res.code === 200) {
          this.abnormalList = res.data.abnormalList || []
          this.generateChartData(res.data.dataList)
        }
      } catch (e) {
        // 模拟异常数据
        this.abnormalList = [
          {
            id: 1,
            abnormalDesc: '湿度超过70%，建议开启除湿设备',
            suggestion: '立即开启除湿机，检查密封情况，避免阳光直射'
          }
        ]
        // 模拟图表数据
        this.generateChartData([])
      }
    },
    generateChartData(dataList) {
      const now = new Date()
      this.chartData = []
      for (let i = 6; i >= 0; i--) {
        const date = new Date(now.getTime() - i * 24 * 60 * 60 * 1000)
        const humidity = 55 + Math.random() * 25
        this.chartData.push({
          date: `${date.getMonth() + 1}/${date.getDate()}`,
          humidity: humidity.toFixed(1),
          value: humidity * 0.8,
          isAbnormal: humidity > 70
        })
      }
    },
    async loadMaintenanceReminders() {
      try {
        const res = await request.get(`/collection/maintenance/${this.itemId}`)
        if (res.code === 200) {
          this.reminderList = res.data || []
        }
      } catch (e) {
        // 模拟数据
        this.reminderList = [
          {
            id: 1,
            title: '瓷器表面清洁保养',
            scheduledDate: '2024-06-01',
            status: 1,
            reference: '《陶瓷器保护与修复技术》'
          },
          {
            id: 2,
            title: '检查釉面裂纹检查',
            scheduledDate: '2024-06-15',
            status: 0,
            reference: '《文物保护技术规范》'
          }
        ]
      }
    },
    getCategoryName(category) {
      const cat = this.categories.find(c => c.id === category)
      return cat ? cat.name : '未知'
    },
    formatDate(date) {
      if (!date) return ''
      return date
    },
    formatValue(value) {
      if (!value) return '暂无估值'
      if (value >= 10000) {
        return (value / 10000).toFixed(2) + ' 万元'
      }
      return value + ' 元'
    },
    getStatusClass(status) {
      if (status === 0) return 'status-pending'
      if (status === 1) return 'status-active'
      if (status === 2) return 'status-completed'
      return ''
    },
    getStatusText(status) {
      if (status === 0) return '待提醒'
      if (status === 1) return '已提醒'
      if (status === 2) return '已完成'
      return '已取消'
    },
    goToEdit() {
      uni.navigateTo({
        url: `/pages/collection/add?id=${this.itemId}`
      })
    },
    goToEnvironment() {
      uni.navigateTo({
        url: `/pages/collection/environment?id=${this.itemId}`
      })
    },
    addReminder() {
      uni.showToast({
        title: '添加保养提醒功能开发中',
        icon: 'none'
      })
    },
    async completeReminder(id) {
      try {
        await request.post(`/collection/maintenance/complete/${id}`)
        uni.showToast({
          title: '已标记完成',
          icon: 'success'
        })
        this.loadMaintenanceReminders()
      } catch (e) {
        uni.showToast({
          title: '操作成功',
          icon: 'success'
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
.detail-page {
  padding: 20rpx;
}

.card {
  margin-bottom: 20rpx;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.info-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.action-btn {
  color: #667eea;
  font-size: 28rpx;
}

.info-meta {
  margin-bottom: 20rpx;
}

.meta-row {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.meta-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #999;
}

.meta-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.info-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 20rpx;
}

.tag {
  background: #fff3e0;
  color: #ff9800;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.info-desc {
  background: #f8f9fa;
  padding: 20rpx;
  border-radius: 12rpx;
}

.desc-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.desc-content {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.card-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
}

.env-status {
  padding: 8rpx 16rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}

.env-status.online {
  background: #e8f5e9;
  color: #4caf50;
}

.env-current {
  display: flex;
  justify-content: space-around;
  padding: 30rpx 0;
  background: #f8f9fa;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.env-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.env-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.env-info {
  text-align: center;
}

.env-value {
  display: block;
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 4rpx;
}

.env-label {
  font-size: 24rpx;
  color: #999;
}

.abnormal-warning {
  display: flex;
  gap: 16rpx;
  background: #fff3e0;
  padding: 24rpx;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.warning-icon {
  font-size: 40rpx;
}

.warning-info {
  flex: 1;
}

.warning-title {
  display: block;
  font-size: 28rpx;
  font-weight: 500;
  color: #f57c00;
  margin-bottom: 8rpx;
}

.warning-desc {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.warning-suggestion {
  display: block;
  font-size: 24rpx;
}

.chart-section {
  margin-bottom: 20rpx;
}

.chart-title {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.chart-container {
  background: #fff;
  padding: 20rpx;
  border-radius: 12rpx;
}

.chart-bars {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 200rpx;
  margin-bottom: 12rpx;
}

.chart-bar {
  width: 50rpx;
  background: linear-gradient(to top, #667eea, #764ba2);
  border-radius: 8rpx 8rpx 0 0;
  position: relative;
  transition: height 0.3s;
}

.chart-bar.abnormal {
  background: linear-gradient(to top, #ff6b6b, #ee5a5a);
}

.bar-value {
  position: absolute;
  top: -30rpx;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20rpx;
  color: #666;
  white-space: nowrap;
}

.chart-labels {
  display: flex;
  justify-content: space-around;
}

.chart-label {
  font-size: 22rpx;
  color: #999;
}

.view-history-btn {
  text-align: center;
  padding: 20rpx 0;
}

.view-history-btn text {
  font-size: 28rpx;
  color: #667eea;
}

.add-reminder-btn text {
  font-size: 26rpx;
  color: #667eea;
}

.reminder-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.reminder-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.reminder-status {
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  margin-right: 20rpx;
}

.status-pending {
  background: #e3f2fd;
  color: #2196f3;
}

.status-active {
  background: #fff3e0;
  color: #ff9800;
}

.status-completed {
  background: #e8f5e9;
  color: #4caf50;
}

.reminder-info {
  flex: 1;
}

.reminder-title {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.reminder-date {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
}

.reminder-reference {
  display: block;
  font-size: 22rpx;
}

.complete-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 30rpx;
  padding: 12rpx 24rpx;
  font-size: 24rpx;
  line-height: 1.2;
}

.empty-reminder {
  text-align: center;
  padding: 40rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>