<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">预约咨询</text>
    </view>

    <view class="counselor-info">
      <view class="avatar">
        <text>{{ counselor.name ? counselor.name.charAt(0) : '?' }}</text>
      </view>
      <view class="info">
        <text class="name">{{ counselor.name || '王医生' }}</text>
        <text class="title">{{ counselor.title || '主任医师' }}</text>
        <text class="fee">咨询费用: ¥{{ counselor.fee || '500' }}/次</text>
      </view>
    </view>

    <view class="form-section">
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
        <text class="label">用户姓名</text>
        <input class="input" v-model="form.userName" placeholder="请输入用户姓名" />
      </view>

      <view class="form-item">
        <text class="label">联系电话</text>
        <input class="input" v-model="form.phone" placeholder="请输入联系电话" type="tel" />
      </view>

      <view class="form-item">
        <text class="label">咨询主诉</text>
        <textarea 
          class="textarea" 
          v-model="form.chiefComplaint" 
          placeholder="请简要描述咨询问题"
        />
      </view>
    </view>

    <view class="submit-section">
      <view class="btn submit" @click="submitAppointment">
        <text>确认预约</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false,
      counselor: {},
      selectedDate: '',
      selectedTime: '',
      timeSlots: [
        { time: '09:00-10:00', available: true },
        { time: '10:00-11:00', available: true },
        { time: '14:00-15:00', available: true },
        { time: '15:00-16:00', available: false },
        { time: '16:00-17:00', available: true }
      ],
      form: {
        userName: '',
        phone: '',
        chiefComplaint: ''
      }
    }
  },
  onLoad(options) {
    this.isElderMode = uni.getStorageSync('elderMode') || false
    if (options.counselorId) {
      this.loadCounselorInfo(options.counselorId)
    }
  },
  methods: {
    loadCounselorInfo(id) {
      const counselors = [
        { id: 1, name: '王医生', title: '主任医师', fee: '500' },
        { id: 2, name: '李医生', title: '副主任医师', fee: '400' },
        { id: 3, name: '张医生', title: '心理治疗师', fee: '350' },
        { id: 4, name: '刘医生', title: '资深心理咨询师', fee: '450' }
      ]
      this.counselor = counselors.find(c => c.id == id) || counselors[0]
    },
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
    submitAppointment() {
      if (!this.selectedDate) {
        uni.showToast({ title: '请选择预约日期', icon: 'none' })
        return
      }
      if (!this.selectedTime) {
        uni.showToast({ title: '请选择预约时段', icon: 'none' })
        return
      }
      if (!this.form.userName) {
        uni.showToast({ title: '请输入用户姓名', icon: 'none' })
        return
      }
      if (!this.form.phone) {
        uni.showToast({ title: '请输入联系电话', icon: 'none' })
        return
      }
      
      uni.showModal({
        title: '预约确认',
        content: `确认预约 ${this.counselor.name || '王医生'} ${this.selectedDate} ${this.selectedTime} 的咨询吗？`,
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
  padding: 30rpx;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

.counselor-info {
  display: flex;
  align-items: center;
  background: white;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
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

.info {
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

.fee {
  display: block;
  font-size: 26rpx;
  color: #ff6b6b;
}

.form-section {
  background: white;
  margin: 0 20rpx 20rpx;
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
