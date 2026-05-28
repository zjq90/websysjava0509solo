<template>
  <view class="apply-container">
    <view class="form-card">
      <view class="form-header">
        <text class="title">入团申请</text>
        <text class="subtitle">请填写以下信息，管理员将尽快审核</text>
      </view>
      
      <view class="form-item">
        <text class="label">申请社团</text>
        <view class="input-group readonly">
          <text class="input-text">{{ clubName }}</text>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">申请理由 <text class="required">*</text></text>
        <view class="input-group textarea">
          <textarea 
            class="textarea-field" 
            placeholder="请输入入团申请理由，介绍一下自己吧..." 
            v-model="form.reason"
            maxlength="500"
            :auto-height="true"
          />
        </view>
        <text class="count-text">{{ form.reason.length }}/500</text>
      </view>
      
      <view class="form-item">
        <text class="label">个人简介</text>
        <view class="input-group textarea">
          <textarea 
            class="textarea-field" 
            placeholder="简单介绍一下你的兴趣爱好、特长等..." 
            v-model="form.personalProfile"
            maxlength="300"
            :auto-height="true"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">相关作品/经历</text>
        <view class="input-group textarea">
          <textarea 
            class="textarea-field" 
            placeholder="如果有相关作品或经历，可以在这里说明..." 
            v-model="form.works"
            maxlength="300"
            :auto-height="true"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">期望加入时间</text>
        <picker mode="date" :value="form.expectedJoinDate" @change="onDateChange">
          <view class="input-group">
            <text class="input-text" :class="{ placeholder: !form.expectedJoinDate }">
              {{ form.expectedJoinDate || '请选择期望加入时间' }}
            </text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>
      
      <view class="tip-section">
        <text class="tip-title">💡 温馨提示</text>
        <text class="tip-content">提交申请后，社团管理员将在1-3个工作日内审核，请耐心等待。审核结果将通过消息通知您。</text>
      </view>
      
      <button class="submit-btn" @click="handleSubmit" :disabled="loading">
        {{ loading ? '提交中...' : '提交申请' }}
      </button>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      clubId: null,
      clubName: '',
      form: {
        clubId: null,
        reason: '',
        personalProfile: '',
        works: '',
        expectedJoinDate: ''
      },
      loading: false
    }
  },
  onLoad(options) {
    this.clubId = options.clubId
    this.form.clubId = options.clubId
    this.loadClubInfo()
  },
  methods: {
    async loadClubInfo() {
      try {
        const res = await api.getClubDetail(this.clubId)
        this.clubName = res.data.name
      } catch (e) {
        console.error(e)
      }
    },
    
    onDateChange(e) {
      this.form.expectedJoinDate = e.detail.value
    },
    
    async handleSubmit() {
      if (!this.form.reason.trim()) {
        util.showToast('请输入申请理由')
        return
      }
      if (this.form.reason.trim().length < 20) {
        util.showToast('申请理由至少20个字')
        return
      }
      
      this.loading = true
      try {
        await api.submitApplication(this.form)
        util.showToast('申请提交成功', 'success')
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.apply-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding: 30rpx;
  box-sizing: border-box;
}

.form-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
}

.form-header {
  text-align: center;
  margin-bottom: 40rpx;
}

.title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.subtitle {
  font-size: 26rpx;
  color: #999;
}

.form-item {
  margin-bottom: 35rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.required {
  color: #dd524d;
}

.input-group {
  display: flex;
  align-items: center;
  min-height: 90rpx;
  background: #f8f9fb;
  border-radius: 12rpx;
  padding: 0 24rpx;
}

.input-group.readonly {
  background: #f0f0f0;
}

.input-group.textarea {
  align-items: flex-start;
  padding: 20rpx 24rpx;
  min-height: 200rpx;
}

.input-text {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.input-text.placeholder {
  color: #999;
}

.textarea-field {
  flex: 1;
  width: 100%;
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.count-text {
  display: block;
  text-align: right;
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.tip-section {
  background: #fff8e6;
  border-radius: 12rpx;
  padding: 25rpx;
  margin-bottom: 35rpx;
}

.tip-title {
  display: block;
  font-size: 28rpx;
  font-weight: 500;
  color: #f0ad4e;
  margin-bottom: 10rpx;
}

.tip-content {
  font-size: 26rpx;
  color: #998033;
  line-height: 1.6;
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  border-radius: 45rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
}

.submit-btn[disabled] {
  opacity: 0.6;
}
</style>
