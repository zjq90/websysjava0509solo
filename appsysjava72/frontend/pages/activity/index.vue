<template>
  <view class="activity-container">
    <view class="filter-bar">
      <scroll-view class="filter-scroll" scroll-x="true" show-scrollbar="false">
        <view 
          class="filter-item" 
          :class="{ active: activeTime === 'all' }"
          @click="selectTime('all')"
        >
          全部
        </view>
        <view 
          class="filter-item" 
          :class="{ active: activeTime === 'today' }"
          @click="selectTime('today')"
        >
          今日
        </view>
        <view 
          class="filter-item" 
          :class="{ active: activeTime === 'week' }"
          @click="selectTime('week')"
        >
          本周
        </view>
        <view 
          class="filter-item" 
          :class="{ active: activeTime === 'month' }"
          @click="selectTime('month')"
        >
          本月
        </view>
      </scroll-view>
      
      <view class="status-filter">
        <picker :range="statusOptions" @change="onStatusChange">
          <view class="picker-btn">
            {{ statusOptions[statusIndex] }} ▾
          </view>
        </picker>
      </view>
    </view>
    
    <view class="type-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeType === '' }"
        @click="selectType('')"
      >
        全部类型
      </view>
      <view 
        class="tab-item" 
        v-for="type in activityTypes" 
        :key="type.value"
        :class="{ active: activeType === type.value }"
        @click="selectType(type.value)"
      >
        {{ type.label }}
      </view>
    </view>
    
    <view class="activity-list" v-if="activityList.length > 0">
      <view 
        class="activity-card" 
        v-for="activity in activityList" 
        :key="activity.id"
        @click="goDetail(activity.id)"
      >
        <view class="card-header">
          <view class="date-badge">
            <text class="date-month">{{ formatMonth(activity.startTime) }}</text>
            <text class="date-day">{{ formatDay(activity.startTime) }}</text>
          </view>
          <view class="activity-basic">
            <text class="activity-name">{{ activity.name }}</text>
            <view class="activity-tags">
              <text class="type-tag">{{ activity.activityType || '其他' }}</text>
              <text 
                class="status-tag" 
                :style="{ background: util.getActivityStatusColor(activity.status) + '20', color: util.getActivityStatusColor(activity.status) }"
              >
                {{ util.getActivityStatusText(activity.status) }}
              </text>
            </view>
          </view>
        </view>
        
        <view class="card-body">
          <view class="info-row">
            <text class="info-icon">⏰</text>
            <text class="info-text">{{ util.formatTime(activity.startTime) }} - {{ util.formatTime(activity.endTime, 'HH:mm') }}</text>
          </view>
          <view class="info-row">
            <text class="info-icon">📍</text>
            <text class="info-text">{{ activity.location || '待定' }}</text>
          </view>
          <view class="info-row" v-if="activity.description">
            <text class="info-icon">📝</text>
            <text class="info-text">{{ activity.description }}</text>
          </view>
        </view>
        
        <view class="card-footer">
          <view class="activity-stats">
            <text class="stat">👥 {{ activity.signUpCount || 0 }}/{{ activity.maxParticipants || '不限' }} 人</text>
          </view>
          <button 
            class="action-btn" 
            :class="{ 
              'signed': signUpMap[activity.id], 
              'ended': activity.status === 2,
              'full': activity.maxParticipants && activity.signUpCount >= activity.maxParticipants
            }"
            @click.stop="handleSignUp(activity)"
            :disabled="activity.status === 2"
          >
            {{ getButtonText(activity) }}
          </button>
        </view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">📅</text>
      <text class="empty-text">暂无活动数据</text>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
    
    <view class="load-more" v-if="hasMore && !loading" @click="loadMore">
      <text>加载更多</text>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activeTime: 'all',
      activeType: '',
      statusIndex: 0,
      statusOptions: ['全部状态', '未开始', '进行中', '已结束'],
      activityTypes: [
        { label: '学术讲座', value: 'lecture' },
        { label: '文体活动', value: 'cultural' },
        { label: '志愿服务', value: 'volunteer' },
        { label: '培训交流', value: 'training' },
        { label: '比赛竞赛', value: 'competition' },
        { label: '其他', value: 'other' }
      ],
      activityList: [],
      signUpMap: {},
      pageNum: 1,
      pageSize: 10,
      hasMore: true,
      loading: false
    }
  },
  onShow() {
    this.refreshList()
  },
  onPullDownRefresh() {
    this.refreshList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    util,
    
    formatMonth(time) {
      return util.formatDate(time, 'MM月')
    },
    
    formatDay(time) {
      return util.formatDate(time, 'DD')
    },
    
    getButtonText(activity) {
      if (activity.status === 2) return '已结束'
      if (this.signUpMap[activity.id]) return '已报名'
      if (activity.maxParticipants && activity.signUpCount >= activity.maxParticipants) return '已满员'
      return '立即报名'
    },
    
    selectTime(time) {
      this.activeTime = time
      this.refreshList()
    },
    
    selectType(type) {
      this.activeType = type
      this.refreshList()
    },
    
    onStatusChange(e) {
      this.statusIndex = parseInt(e.detail.value)
      this.refreshList()
    },
    
    async refreshList() {
      this.pageNum = 1
      this.hasMore = true
      this.loading = true
      try {
        const status = this.statusIndex > 0 ? this.statusIndex - 1 : null
        const res = await api.getActivityPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          activityType: this.activeType || undefined,
          status: status !== null ? status : undefined,
          timeRange: this.activeTime === 'all' ? undefined : this.activeTime
        })
        this.activityList = res.data.list || []
        this.hasMore = this.pageNum < res.data.totalPage
        this.checkSignUpStatus()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async loadMore() {
      this.pageNum++
      this.loading = true
      try {
        const status = this.statusIndex > 0 ? this.statusIndex - 1 : null
        const res = await api.getActivityPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          activityType: this.activeType || undefined,
          status: status !== null ? status : undefined,
          timeRange: this.activeTime === 'all' ? undefined : this.activeTime
        })
        this.activityList = this.activityList.concat(res.data.list || [])
        this.hasMore = this.pageNum < res.data.totalPage
        this.checkSignUpStatus()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async checkSignUpStatus() {
      for (const activity of this.activityList) {
        try {
          const res = await api.isSignedUp(activity.id)
          this.signUpMap[activity.id] = res.data
        } catch (e) {
          console.error(e)
        }
      }
    },
    
    async handleSignUp(activity) {
      if (activity.status === 2) {
        util.showToast('活动已结束')
        return
      }
      if (activity.maxParticipants && activity.signUpCount >= activity.maxParticipants) {
        util.showToast('活动已满员')
        return
      }
      if (this.signUpMap[activity.id]) {
        const confirm = await util.showModal('提示', '确定要取消报名吗？')
        if (confirm) {
          try {
            await api.cancelSignUp(activity.id)
            this.signUpMap[activity.id] = false
            activity.signUpCount--
            util.showToast('已取消报名', 'success')
          } catch (e) {
            console.error(e)
          }
        }
      } else {
        const confirm = await util.showModal('确认报名', '确定要报名参加"' + activity.name + '"吗？')
        if (confirm) {
          try {
            await api.signUpActivity(activity.id)
            this.signUpMap[activity.id] = true
            activity.signUpCount++
            util.showToast('报名成功', 'success')
          } catch (e) {
            console.error(e)
          }
        }
      }
    },
    
    goDetail(id) {
      uni.navigateTo({ url: '/pages/activity/detail?id=' + id })
    }
  }
}
</script>

<style scoped>
.activity-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 120rpx;
}

.filter-bar {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20rpx 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.filter-scroll {
  flex: 1;
  white-space: nowrap;
  padding: 0 20rpx;
}

.filter-item {
  display: inline-block;
  padding: 12rpx 30rpx;
  margin-right: 15rpx;
  background: #f5f6f8;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
}

.filter-item.active {
  background: #5677fc;
  color: #fff;
}

.status-filter {
  padding-right: 30rpx;
}

.picker-btn {
  padding: 12rpx 20rpx;
  background: #f5f6f8;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  white-space: nowrap;
}

.type-tabs {
  display: flex;
  background: #fff;
  padding: 0 20rpx 20rpx;
  overflow-x: auto;
  border-bottom: 1rpx solid #f0f0f0;
}

.tab-item {
  flex-shrink: 0;
  padding: 10rpx 25rpx;
  margin-right: 15rpx;
  font-size: 26rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab-item.active {
  color: #5677fc;
  border-bottom-color: #5677fc;
  font-weight: 500;
}

.activity-list {
  padding: 20rpx;
}

.activity-card {
  background: #fff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  padding: 25rpx;
  border-bottom: 1rpx solid #f8f8f8;
}

.date-badge {
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.date-month {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.date-day {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.activity-basic {
  flex: 1;
  overflow: hidden;
}

.activity-name {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 12rpx;
}

.activity-tags {
  display: flex;
  gap: 12rpx;
  flex-wrap: wrap;
}

.type-tag {
  padding: 4rpx 16rpx;
  background: #f0f2f5;
  color: #666;
  border-radius: 20rpx;
  font-size: 22rpx;
}

.status-tag {
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.card-body {
  padding: 20rpx 25rpx;
}

.info-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.info-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 25rpx;
  background: #fafbfc;
}

.stat {
  font-size: 24rpx;
  color: #999;
}

.action-btn {
  padding: 12rpx 40rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 26rpx;
  border: none;
}

.action-btn.signed {
  background: #4cd964;
}

.action-btn.ended,
.action-btn.full {
  background: #ccc;
}

.empty-state,
.loading-state,
.load-more {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}

.empty-icon {
  display: block;
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.load-more {
  color: #5677fc;
}
</style>
