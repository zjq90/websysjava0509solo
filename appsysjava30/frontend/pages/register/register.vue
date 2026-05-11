<template>
  <view class="register-container">
    <view class="register-form card">
      <view class="form-item">
        <text class="label">用户名 *</text>
        <input v-model="form.username" class="input" placeholder="请输入用户名（4-20位）" maxlength="20" />
      </view>
      
      <view class="form-item">
        <text class="label">密码 *</text>
        <input v-model="form.password" class="input" type="password" placeholder="请输入密码（6-20位）" maxlength="20" />
      </view>
      
      <view class="form-item">
        <text class="label">真实姓名 *</text>
        <input v-model="form.realName" class="input" placeholder="请输入真实姓名" maxlength="20" />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input v-model="form.phone" class="input" placeholder="请输入手机号" maxlength="11" type="number" />
      </view>
      
      <view class="form-item">
        <text class="label">身份证号</text>
        <input v-model="form.idCard" class="input" placeholder="请输入身份证号" maxlength="18" />
      </view>
      
      <view class="form-item">
        <text class="label">性别</text>
        <view class="gender-picker">
          <view 
            :class="['gender-item', form.gender === '男' ? 'active' : '']" 
            @click="form.gender = '男'"
          >男</view>
          <view 
            :class="['gender-item', form.gender === '女' ? 'active' : '']" 
            @click="form.gender = '女'"
          >女</view>
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">年龄</text>
        <input v-model="form.age" class="input" placeholder="请输入年龄" type="number" maxlength="3" />
      </view>
      
      <view class="form-item">
        <text class="label">地址</text>
        <input v-model="form.address" class="input" placeholder="请输入地址" />
      </view>
      
      <view class="btn-primary" @click="handleRegister">注册</view>
      
      <view class="footer">
        <text class="link" @click="goBack">已有账号，立即登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { authApi } from '@/utils/api.js'

export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
        realName: '',
        phone: '',
        idCard: '',
        gender: '',
        age: null,
        address: ''
      }
    }
  },
  
  methods: {
    async handleRegister() {
      if (!this.form.username || this.form.username.length < 4) {
        uni.showToast({ title: '用户名至少4位', icon: 'none' })
        return
      }
      if (!this.form.password || this.form.password.length < 6) {
        uni.showToast({ title: '密码至少6位', icon: 'none' })
        return
      }
      if (!this.form.realName) {
        uni.showToast({ title: '请输入真实姓名', icon: 'none' })
        return
      }
      if (this.form.phone && !/^1[3-9]\d{9}$/.test(this.form.phone)) {
        uni.showToast({ title: '手机号格式不正确', icon: 'none' })
        return
      }
      
      uni.showLoading({ title: '注册中...' })
      
      try {
        const res = await authApi.register(this.form)
        uni.setStorageSync('token', res.data.token)
        uni.setStorageSync('userInfo', res.data)
        
        uni.hideLoading()
        uni.showToast({ title: '注册成功', icon: 'success' })
        
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 500)
      } catch (e) {
        uni.hideLoading()
      }
    },
    
    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.register-form {
  padding: 32rpx;
}

.form-item {
  margin-bottom: 32rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 12rpx;
  font-size: 30rpx;
  background: #fafafa;
}

.input:focus {
  border-color: #1890ff;
  background: #fff;
}

.gender-picker {
  display: flex;
  gap: 20rpx;
}

.gender-item {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  border: 2rpx solid #d9d9d9;
  border-radius: 12rpx;
  font-size: 30rpx;
  color: #666;
}

.gender-item.active {
  border-color: #1890ff;
  background: #e6f7ff;
  color: #1890ff;
}

.footer {
  text-align: center;
  margin-top: 32rpx;
}

.link {
  font-size: 28rpx;
  color: #1890ff;
}
</style>
