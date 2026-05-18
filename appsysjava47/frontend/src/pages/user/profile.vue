<template>
  <div class="page-container">
    <div class="profile-header">
      <div class="avatar-large">
        <span>{{ nickname ? nickname.charAt(0) : '?' }}</span>
      </div>
      <div class="user-info">
        <span class="username">{{ nickname || username || '未登录' }}</span>
        <span class="user-status" v-if="isAnonymous">🔒 匿名模式</span>
        <span class="user-status" v-else>👤 实名认证</span>
      </div>
    </div>

    <div class="elder-mode-section" v-if="isLoggedIn">
      <div class="elder-mode-card" @click="toggleElderMode">
        <div class="elder-info">
          <span class="elder-icon">👓</span>
          <div class="elder-text">
            <span class="elder-title">长辈模式</span>
            <span class="elder-desc">放大字体，简化界面</span>
          </div>
        </div>
        <input type="checkbox" :checked="elderMode" @change="toggleElderMode" class="switch-input" />
      </div>
    </div>

    <div class="menu-section" v-if="isLoggedIn">
      <div class="menu-title">我的服务</div>
      
      <div class="menu-list">
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">💬</span>
          <span class="menu-text">咨询记录</span>
          <span class="menu-arrow">→</span>
        </div>
        
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">📋</span>
          <span class="menu-text">测评历史</span>
          <span class="menu-arrow">→</span>
        </div>
        
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">📊</span>
          <span class="menu-text">情绪分析</span>
          <span class="menu-arrow">→</span>
        </div>
      </div>
    </div>

    <div class="menu-section" v-if="isLoggedIn">
      <div class="menu-title">设置</div>
      
      <div class="menu-list">
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">🔒</span>
          <span class="menu-text">隐私设置</span>
          <span class="menu-arrow">→</span>
        </div>
        
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">🔔</span>
          <span class="menu-text">消息通知</span>
          <span class="menu-arrow">→</span>
        </div>
        
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">❓</span>
          <span class="menu-text">帮助中心</span>
          <span class="menu-arrow">→</span>
        </div>
        
        <div class="menu-item" @click="alert('功能开发中')">
          <span class="menu-icon">📝</span>
          <span class="menu-text">意见反馈</span>
          <span class="menu-arrow">→</span>
        </div>
      </div>
    </div>

    <div class="menu-section" v-if="!isLoggedIn">
      <div class="login-prompt">
        <span class="prompt-icon">💡</span>
        <span class="prompt-text">登录后可使用完整功能</span>
      </div>
      <div class="menu-list">
        <div class="menu-item" @click="goToLogin">
          <span class="menu-icon">👤</span>
          <span class="menu-text text-primary">立即登录</span>
          <span class="menu-arrow">→</span>
        </div>
      </div>
    </div>

    <div class="logout-section" v-if="isLoggedIn">
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>

    <div class="about-section">
      <span class="version">心理咨询 v1.0.0</span>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter()
    
    const isLoggedIn = ref(false)
    const username = ref('')
    const nickname = ref('')
    const isAnonymous = ref(false)
    const elderMode = ref(false)
    
    const loadUserInfo = () => {
      const token = localStorage.getItem('token')
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      
      isLoggedIn.value = !!token
      if (userInfo) {
        username.value = userInfo.username || ''
        nickname.value = userInfo.nickname || ''
        isAnonymous.value = userInfo.isAnonymous || false
      }
      
      elderMode.value = localStorage.getItem('elderMode') === 'true'
    }
    
    const toggleElderMode = () => {
      elderMode.value = !elderMode.value
      localStorage.setItem('elderMode', elderMode.value)
      if (elderMode.value) {
        document.body.classList.add('elder-mode')
      } else {
        document.body.classList.remove('elder-mode')
      }
    }
    
    const goToLogin = () => {
      router.push('/login')
    }
    
    const logout = () => {
      if (confirm('确定要退出登录吗？')) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        loadUserInfo()
        alert('已退出登录')
        router.push('/')
      }
    }
    
    onMounted(() => {
      loadUserInfo()
    })
    
    return {
      isLoggedIn,
      username,
      nickname,
      isAnonymous,
      elderMode,
      toggleElderMode,
      goToLogin,
      logout
    }
  }
}
</script>

<style scoped>
.profile-header {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 40rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  margin-bottom: 30rpx;
}

.avatar-large {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 48rpx;
  font-weight: 600;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
}

.username {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 12rpx;
}

.user-status {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
}

.elder-mode-section {
  margin-bottom: 30rpx;
}

.elder-mode-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.elder-info {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.elder-icon {
  font-size: 40rpx;
}

.elder-text {
  display: flex;
  flex-direction: column;
  gap: 6rpx;
}

.elder-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
}

.elder-desc {
  font-size: 24rpx;
  color: #909399;
}

.switch-input {
  width: 50rpx;
  height: 30rpx;
  cursor: pointer;
}

.menu-section {
  margin-bottom: 30rpx;
}

.menu-title {
  font-size: 26rpx;
  font-weight: 600;
  color: #909399;
  margin-bottom: 20rpx;
  padding: 0 10rpx;
}

.menu-list {
  background: #ffffff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f7fa;
  cursor: pointer;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
}

.menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #303133;
}

.menu-arrow {
  font-size: 24rpx;
  color: #c0c4cc;
}

.text-primary {
  color: #409EFF;
  font-weight: 500;
}

.login-prompt {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 24rpx;
  background: #ECF5FF;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.prompt-icon {
  font-size: 28rpx;
}

.prompt-text {
  font-size: 24rpx;
  color: #409EFF;
}

.logout-section {
  margin-bottom: 30rpx;
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  border-radius: 44rpx;
  background: #ffffff;
  border: 2rpx solid #F56C6C;
  color: #F56C6C;
  font-size: 28rpx;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.about-section {
  text-align: center;
  padding: 30rpx 0;
}

.version {
  font-size: 24rpx;
  color: #c0c4cc;
}
</style>
