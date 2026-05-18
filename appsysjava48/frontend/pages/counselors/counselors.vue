<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">咨询师列表</text>
    </view>

    <view class="search-bar">
      <input class="search-input" v-model="keyword" placeholder="搜索咨询师" />
    </view>

    <view class="filter-bar">
      <view 
        v-for="cat in categories" 
        :key="cat"
        class="filter-item"
        :class="{ active: activeCategory === cat }"
        @click="activeCategory = cat"
      >
        <text>{{ cat }}</text>
      </view>
    </view>

    <view class="counselor-list">
      <view 
        v-for="counselor in filteredCounselors" 
        :key="counselor.id" 
        class="counselor-card"
        @click="goToAppointment(counselor.id)"
      >
        <view class="counselor-header">
          <view class="avatar">
            <text>{{ counselor.name.charAt(0) }}</text>
          </view>
          <view class="basic-info">
            <text class="name">{{ counselor.name }}</text>
            <text class="title">{{ counselor.title }}</text>
            <text class="expertise">{{ counselor.expertise }}</text>
          </view>
          <view class="fee">
            <text class="amount">¥{{ counselor.fee }}</text>
            <text class="unit">/次</text>
          </view>
        </view>
        
        <view class="counselor-footer">
          <view class="tags">
            <text class="tag" v-for="(tag, idx) in counselor.tags.slice(0, 3)" :key="idx">{{ tag }}</text>
          </view>
          <view class="is-senior" v-if="counselor.isSenior">
            <text>资深咨询师</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false,
      keyword: '',
      activeCategory: '全部',
      categories: ['全部', '抑郁症', '焦虑症', '婚恋情感', '青少年心理']
    }
  },
  computed: {
    counselors() {
      return [
        { id: 1, name: '王医生', title: '主任医师', expertise: '抑郁症、焦虑症、强迫症', fee: '500', isSenior: true, tags: ['抑郁', '焦虑', '强迫'] },
        { id: 2, name: '李医生', title: '副主任医师', expertise: '青少年心理、家庭关系、工作压力', fee: '400', isSenior: false, tags: ['青少年', '家庭', '职场'] },
        { id: 3, name: '张医生', title: '心理治疗师', expertise: '婚恋情感、人际关系、恐慌症', fee: '350', isSenior: false, tags: ['婚恋', '人际', '恐慌'] },
        { id: 4, name: '刘医生', title: '资深心理咨询师', expertise: '抑郁情绪、焦虑情绪、睡眠障碍', fee: '450', isSenior: true, tags: ['抑郁', '焦虑', '睡眠'] }
      ]
    },
    filteredCounselors() {
      let result = this.counselors
      if (this.keyword) {
        result = result.filter(c => c.name.includes(this.keyword) || c.expertise.includes(this.keyword))
      }
      return result
    }
  },
  onLoad() {
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    goToAppointment(id) {
      uni.navigateTo({ url: `/pages/appointment/appointment?counselorId=${id}` })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  
  &.elder {
    font-size: 36rpx !important;
  }
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

.search-bar {
  padding: 20rpx;
  background: white;
}

.search-input {
  width: 100%;
  padding: 20rpx 30rpx;
  background: #f5f5f5;
  border-radius: 40rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.filter-bar {
  display: flex;
  padding: 20rpx;
  background: white;
  margin-bottom: 20rpx;
  gap: 16rpx;
  overflow-x: auto;
}

.filter-item {
  padding: 12rpx 32rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  white-space: nowrap;
  
  &.active {
    background: #667eea;
    color: white;
  }
}

.counselor-list {
  padding: 0 20rpx;
}

.counselor-card {
  background: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.counselor-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 48rpx;
  font-weight: bold;
  margin-right: 24rpx;
}

.basic-info {
  flex: 1;
}

.name {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.title {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.expertise {
  display: block;
  font-size: 24rpx;
  color: #999;
  line-height: 1.5;
}

.fee {
  text-align: right;
}

.amount {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #ff6b6b;
}

.unit {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.counselor-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.tags {
  display: flex;
  gap: 12rpx;
}

.tag {
  padding: 8rpx 16rpx;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 20rpx;
  font-size: 22rpx;
}

.is-senior {
  padding: 8rpx 16rpx;
  background: #fff3e0;
  color: #ff9800;
  border-radius: 20rpx;
  font-size: 22rpx;
}
</style>
