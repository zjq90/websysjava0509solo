<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">咨询记录详情</text>
    </view>

    <view class="form-section">
      <view class="section-title">基本信息</view>
      
      <view class="form-item">
        <text class="label">用户姓名</text>
        <input class="input" v-model="form.userName" placeholder="请输入用户姓名" />
      </view>

      <view class="form-item">
        <text class="label">情绪评分</text>
        <view class="rating">
          <text 
            v-for="i in 10" 
            :key="i"
            class="star"
            :class="{ active: form.moodRating >= i }"
            @click="form.moodRating = i"
          >★</text>
        </view>
      </view>

      <view class="form-item">
        <text class="label">风险等级</text>
        <picker mode="selector" :range="riskLevels" @change="onRiskChange">
          <view class="picker-value">{{ getRiskText(form.riskLevel) }}</view>
        </picker>
      </view>
    </view>

    <view class="form-section">
      <view class="section-title">咨询内容</view>
      
      <view class="form-item">
        <text class="label">用户主诉</text>
        <textarea 
          class="textarea" 
          v-model="form.chiefComplaint" 
          placeholder="请输入用户主诉（系统将自动检测敏感词）"
        />
      </view>

      <view class="form-item">
        <text class="label">干预措施</text>
        <textarea 
          class="textarea" 
          v-model="form.interventionMeasures" 
          placeholder="请输入干预措施"
        />
      </view>

      <view class="form-item">
        <text class="label">后续建议</text>
        <textarea 
          class="textarea" 
          v-model="form.followUpSuggestions" 
          placeholder="请输入后续建议"
        />
      </view>
    </view>

    <view class="form-section">
      <view class="section-title">标签分类</view>
      <view class="tag-list">
        <view 
          v-for="tag in tags" 
          :key="tag"
          class="tag-item"
          :class="{ selected: selectedTags.includes(tag) }"
          @click="toggleTag(tag)"
        >
          <text>{{ tag }}</text>
        </view>
      </view>
    </view>

    <view class="form-section">
      <view class="section-title">状态</view>
      <view class="status-options">
        <view 
          class="status-option"
          :class="{ active: form.status === 0 }"
          @click="form.status = 0"
        >
          <text>草稿</text>
        </view>
        <view 
          class="status-option"
          :class="{ active: form.status === 1 }"
          @click="form.status = 1"
        >
          <text>已完成</text>
        </view>
      </view>
    </view>

    <view class="submit-section">
      <view class="btn save" @click="saveRecord">
        <text>保存记录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false,
      riskLevels: ['低风险', '中风险', '高风险'],
      tags: ['抑郁', '焦虑', '恐慌', '婚恋情感', '工作压力', '家庭关系', '青少年心理'],
      selectedTags: [],
      form: {
        userName: '',
        moodRating: 5,
        riskLevel: 'low',
        chiefComplaint: '',
        interventionMeasures: '',
        followUpSuggestions: '',
        status: 0
      }
    }
  },
  onLoad() {
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    getRiskText(level) {
      const map = { 'low': '低风险', 'medium': '中风险', 'high': '高风险' }
      return map[level] || '低风险'
    },
    onRiskChange(e) {
      const levels = ['low', 'medium', 'high']
      this.form.riskLevel = levels[e.detail.value]
    },
    toggleTag(tag) {
      const index = this.selectedTags.indexOf(tag)
      if (index > -1) {
        this.selectedTags.splice(index, 1)
      } else {
        this.selectedTags.push(tag)
      }
    },
    saveRecord() {
      if (!this.form.chiefComplaint) {
        uni.showToast({ title: '请填写用户主诉', icon: 'none' })
        return
      }
      
      const crisisWords = ['自杀', '想死', '不想活', '结束生命', '跳楼', '割腕', '自残', '自伤']
      const hasCrisis = crisisWords.some(word => this.form.chiefComplaint.includes(word))
      
      if (hasCrisis) {
        uni.showModal({
          title: '危机预警',
          content: '检测到内容包含敏感词，系统将自动触发危机预警，是否继续保存？',
          success: (res) => {
            if (res.confirm) {
              this.doSave()
            }
          }
        })
      } else {
        this.doSave()
      }
    },
    doSave() {
      uni.showToast({ title: '保存成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
  
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

.form-section {
  background: white;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.form-item {
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.input, .picker-value {
  width: 100%;
  padding: 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.textarea {
  width: 100%;
  min-height: 200rpx;
  padding: 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.rating {
  display: flex;
  gap: 10rpx;
}

.star {
  font-size: 40rpx;
  color: #e0e0e0;
  
  &.active {
    color: #ff9800;
  }
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.tag-item {
  padding: 16rpx 32rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
  
  &.selected {
    background: #667eea;
    color: white;
  }
}

.status-options {
  display: flex;
  gap: 20rpx;
}

.status-option {
  flex: 1;
  padding: 24rpx;
  text-align: center;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #666;
  
  &.active {
    background: #667eea;
    color: white;
  }
}

.submit-section {
  padding: 20rpx;
}

.btn {
  padding: 30rpx;
  text-align: center;
  border-radius: 8rpx;
  font-size: 30rpx;
  font-weight: bold;
  
  &.save {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }
}
</style>
