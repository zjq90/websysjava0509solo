<script>
import { onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const currentPage = computed(() => route.path)
    
    onMounted(() => {
      console.log('App Mounted')
      checkElderMode()
    })
    
    const checkElderMode = () => {
      const elderMode = localStorage.getItem('elderMode')
      if (elderMode === 'true') {
        document.documentElement.style.setProperty('--font-size-scale', '1.3')
        document.body.classList.add('elder-mode')
      }
    }
    
    const navigateTo = (path) => {
      router.push(path)
    }
    
    return {
      currentPage,
      navigateTo
    }
  }
}
</script>

<template>
  <div id="app" class="app-container">
    <router-view />
    <div class="tab-bar" v-if="currentPage !== '/login' && currentPage !== '/register'">
      <div class="tab-item" :class="{ active: currentPage === '/' }" @click="navigateTo('/')">
        <span class="tab-icon">🏠</span>
        <span class="tab-label">首页</span>
      </div>
      <div class="tab-item" :class="{ active: currentPage === '/counselors' }" @click="navigateTo('/counselors')">
        <span class="tab-icon">👨‍⚕️</span>
        <span class="tab-label">咨询师</span>
      </div>
      <div class="tab-item" :class="{ active: currentPage === '/emergency' }" @click="navigateTo('/emergency')">
        <span class="tab-icon">🆘</span>
        <span class="tab-label">紧急求助</span>
      </div>
      <div class="tab-item" :class="{ active: currentPage === '/profile' }" @click="navigateTo('/profile')">
        <span class="tab-icon">👤</span>
        <span class="tab-label">我的</span>
      </div>
    </div>
  </div>
</template>

<style>
/* 全局样式 */
:root {
  --font-size-scale: 1;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background-color: #f5f7fa;
  font-size: calc(28rpx * var(--font-size-scale));
}

.elder-mode page {
  --font-size-scale: 1.3;
}

/* 通用容器 */
.container {
  padding: 20rpx;
}

/* 卡片样式 */
.card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

/* 按钮样式 */
.btn {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 88rpx;
  border-radius: 44rpx;
  font-size: calc(32rpx * var(--font-size-scale));
  font-weight: 500;
  border: none;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
  color: #ffffff;
}

.btn-primary:active {
  opacity: 0.85;
}

.btn-danger {
  background: linear-gradient(135deg, #F56C6C 0%, #FF8787 100%);
  color: #ffffff;
}

.btn-success {
  background: linear-gradient(135deg, #67C23A 0%, #85CE61 100%);
  color: #ffffff;
}

.btn-outline {
  background: transparent;
  border: 2rpx solid #409EFF;
  color: #409EFF;
}

.btn-large {
  height: 100rpx;
  font-size: calc(36rpx * var(--font-size-scale));
}

.btn-block {
  width: 100%;
}

/* 表单样式 */
.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: calc(28rpx * var(--font-size-scale));
  color: #606266;
  margin-bottom: 12rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 24rpx;
  border: 2rpx solid #DCDFE6;
  border-radius: 12rpx;
  font-size: calc(28rpx * var(--font-size-scale));
  background: #ffffff;
  transition: border-color 0.3s;
}

.form-input:focus {
  border-color: #409EFF;
  outline: none;
}

/* 标题样式 */
.title {
  font-size: calc(36rpx * var(--font-size-scale));
  font-weight: 600;
  color: #303133;
  margin-bottom: 16rpx;
}

.subtitle {
  font-size: calc(30rpx * var(--font-size-scale));
  font-weight: 500;
  color: #606266;
  margin-bottom: 12rpx;
}

/* 文本样式 */
.text-primary {
  color: #409EFF;
}

.text-success {
  color: #67C23A;
}

.text-warning {
  color: #E6A23C;
}

.text-danger {
  color: #F56C6C;
}

.text-secondary {
  color: #909399;
}

/* 间距 */
.mt-10 { margin-top: 10rpx; }
.mt-20 { margin-top: 20rpx; }
.mt-30 { margin-top: 30rpx; }
.mb-10 { margin-bottom: 10rpx; }
.mb-20 { margin-bottom: 20rpx; }
.mb-30 { margin-bottom: 30rpx; }

/* 弹性布局 */
.flex {
  display: flex;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

/* 头像 */
.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: calc(28rpx * var(--font-size-scale));
  font-weight: 500;
}

.avatar-large {
  width: 120rpx;
  height: 120rpx;
  font-size: calc(40rpx * var(--font-size-scale));
}

/* 标签 */
.tag {
  display: inline-block;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: calc(24rpx * var(--font-size-scale));
  margin-right: 12rpx;
  margin-bottom: 12rpx;
}

.tag-primary {
  background: #ECF5FF;
  color: #409EFF;
}

.tag-success {
  background: #F0F9EB;
  color: #67C23A;
}

.tag-warning {
  background: #FDF6EC;
  color: #E6A23C;
}

.tag-danger {
  background: #FEF0F0;
  color: #F56C6C;
}

/* 分割线 */
.divider {
  height: 2rpx;
  background: #F2F6FC;
  margin: 30rpx 0;
}

/* App容器 */
.app-container {
  min-height: 100vh;
  padding-bottom: 120rpx;
  background-color: #f5f7fa;
}

/* 底部导航栏 */
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.08);
  z-index: 1000;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  color: #909399;
  transition: all 0.3s;
  cursor: pointer;
}

.tab-item.active {
  color: #409EFF;
}

.tab-icon {
  font-size: calc(40rpx * var(--font-size-scale));
  margin-bottom: 4rpx;
}

.tab-label {
  font-size: calc(24rpx * var(--font-size-scale));
}

/* 页面内容容器 */
.page-container {
  padding: 30rpx;
}

/* 适配rem单位 */
@media (max-width: 750px) {
  :root {
    font-size: 13.3333vw;
  }
}
</style>
