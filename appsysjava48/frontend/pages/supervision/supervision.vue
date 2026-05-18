<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">督导预约</text>
      <text class="subtitle">资深咨询师一对一案例督导</text>
    </view>

    <view class="section">
      <view class="section-title">选择督导</view>
      <view class="supervisor-list">
        <view 
          v-for="sv in supervisors" 
          :key="sv.id" 
          class="supervisor-card"
          :class="{ selected: selectedSupervisorId === sv.id }"
          @click="selectedSupervisorId = sv.id"
        >
          <view class="avatar">
            <text>{{ sv.name.charAt(0) }}</text>
          </view>
          <view class="info">
            <text class="name">{{ sv.name }}</text>
            <text class="title">{{ sv.title }}</text>
            <text class="specialty">擅长: {{ sv.specialty }}</text>
          </view>
          <view class="fee">
            <text class="amount">¥{{ sv.fee }}</text>
            <text class="unit">/小时</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">预约信息</view>
      
      <view class="form-item">
        <text class="label">预约日期</text>
        <picker mode="date" @change="onDateChange">
          <view class="picker-value">{{ selectedDate || '请选择日期' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">预约时段</text>
        <view class="time-slots">
          <view 
            v-for="slot in timeSlots" 
            :key="slot.time"
            class="time-slot"
            :class="{ active: selectedTime === slot.time, disabled: !slot.available }"
            @click="selectTime(slot)"
          >
            <text>{{ slot.time }}</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <text class="label">督导时长</text>
        <view class="duration-options">
          <view 
            v-for="d in durations" 
            :key="d"
            class="duration-option"
            :class="{ active: selectedDuration === d }"
            @click="selectedDuration = d"
          >
            <text>{{ d }}小时</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <text class="label">案例主题</text>
        <input class="input" v-model="form.topic" placeholder="请输入案例主题" />
      </view>

      <view class="form-item">
        <text class="label">案例描述</text>
        <textarea 
          class="textarea" 
          v-model="form.description" 
          placeholder="请详细描述案例情况，包括背景、主要问题、已采取的措施等"
        />
      </view>
    </view>

    <view class="submit-section">
      <view class="btn submit" @click="submitSupervision">
        <text>提交预约</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false,
      selectedSupervisorId: null,
      selectedDate: '',
      selectedTime: '',
      selectedDuration: 1,
      durations: [1, 1.5, 2],
      timeSlots: [
        { time: '09:00-10:00', available: true },
        { time: '10:00-11:00', available: true },
        { time: '14:00-15:00', available: true },
        { time: '15:00-16:00', available: false },
        { time: '16:00-17:00', available: true }
      ],
      form: {
        topic: '',
        description: ''
      }
    }
  },
  computed: {
    supervisors() {
      return [
        { 
          id: 1, 
          name: '王医生', 
          title: '主任医师/注册督导师', 
          specialty: '抑郁症、焦虑障碍、认知行为治疗',
          fee: '800'
        },
        { 
          id: 2, 
          name: '刘医生', 
          title: '资深心理咨询师/督导师', 
          specialty: '危机干预、创伤后应激障碍、家庭治疗',
          fee: '700'
        },
        { 
          id: 3, 
          name: '陈教授', 
          title: '心理学教授/资深督导师', 
          specialty: '精神分析、人格障碍、疑难案例',
          fee: '1000'
        }
      ]
    }
  },
  onLoad() {
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    onDateChange(e) {
      this.selectedDate = e.detail.value
    },
    selectTime(slot) {
      if (slot.available) {
        this.selectedTime = slot.time
      } else {
        uni.showToast({ title: '该时段已被预约', icon: 'none' })
      }
    },
    submitSupervision() {
      if (!this.selectedSupervisorId) {
        uni.showToast({ title: '请选择督导', icon: 'none' })
        return
      }
      if (!this.selectedDate) {
        uni.showToast({ title: '请选择预约日期', icon: 'none' })
        return
      }
      if (!this.selectedTime) {
        uni.showToast({ title: '请选择预约时段', icon: 'none' })
        return
      }
      if (!this.form.topic) {
        uni.showToast({ title: '请输入案例主题', icon: 'none' })
        return
      }
      
      const supervisor = this.supervisors.find(s => s.id === this.selectedSupervisorId)
      uni.showModal({
        title: '预约确认',
        content: `确认预约 ${supervisor.name} ${this.selectedDate} ${this.selectedTime} 的督导吗？时长: ${this.selectedDuration}小时`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '预约成功', icon: 'success' })
            setTimeout(() => {
              uni.navigateBack()
            }, 1500)
          }
        }
      })
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
  padding: 40rpx 30rpx;
}

.title {
  display: block;
  color: white;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.subtitle {
  display: block;
  color: rgba(255, 255, 255, 0.8);
  font-size: 26rpx;
}

.section {
  background: white;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.supervisor-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.supervisor-card {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
  
  &.selected {
    border-color: #667eea;
    background: #f0f4ff;
  }
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 40rpx;
  font-weight: bold;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.info {
  flex: 1;
  min-width: 0;
}

.name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 6rpx;
}

.title {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 6rpx;
}

.specialty {
  display: block;
  font-size: 22rpx;
  color: #999;
  line-height: 1.4;
}

.fee {
  text-align: right;
  margin-left: 16rpx;
  flex-shrink: 0;
}

.amount {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #ff6b6b;
}

.unit {
  display: block;
  font-size: 20rpx;
  color: #999;
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
  min-height: 240rpx;
  padding: 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.time-slots {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.time-slot {
  padding: 20rpx 30rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 26rpx;
  color: #666;
  
  &.active {
    background: #667eea;
    color: white;
  }
  
  &.disabled {
    opacity: 0.4;
  }
}

.duration-options {
  display: flex;
  gap: 16rpx;
}

.duration-option {
  flex: 1;
  padding: 20rpx;
  text-align: center;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 26rpx;
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
  
  &.submit {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
  }
}
</style>
