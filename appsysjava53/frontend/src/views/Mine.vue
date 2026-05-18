<template>
  <div class="mine-container">
    <!-- 用户信息卡片 -->
    <div class="user-card" v-if="userStore.isLoggedIn">
      <div class="user-avatar">
        <el-icon :size="36"><User /></el-icon>
      </div>
      <div class="user-info">
        <div class="user-name">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</div>
        <div class="user-desc">
          <el-tag v-if="userStore.userInfo?.isVerified" type="success" size="small">已实名认证</el-tag>
          <el-tag v-else type="info" size="small">未实名认证</el-tag>
        </div>
      </div>
    </div>

    <div class="login-prompt" v-else @click="goToLogin">
      <span>点击登录账号</span>
      <el-icon><ArrowRight /></el-icon>
    </div>

    <!-- 数据统计 -->
    <div class="stats-card" v-if="userStore.isLoggedIn">
      <div class="stat-item">
        <div class="stat-value">¥{{ userStore.userInfo?.balance || '0.00' }}</div>
        <div class="stat-label">账户余额</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">12</div>
        <div class="stat-label">我的收藏</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">5</div>
        <div class="stat-label">已发布</div>
      </div>
    </div>

    <!-- 功能菜单 -->
    <div class="menu-card">
      <div class="menu-item" @click="goToCollection">
        <div class="menu-icon" style="background: linear-gradient(135deg, #667eea, #764ba2);">
          <el-icon :size="20"><Star /></el-icon>
        </div>
        <span class="menu-text">我的收藏</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>

      <div class="menu-item" @click="goToOrders">
        <div class="menu-icon" style="background: linear-gradient(135deg, #f093fb, #f5576c);">
          <el-icon :size="20"><DocumentCopy /></el-icon>
        </div>
        <span class="menu-text">我的订单</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>

      <div class="menu-item" @click="goToVerify">
        <div class="menu-icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7);">
          <el-icon :size="20"><CircleCheck /></el-icon>
        </div>
        <span class="menu-text">实名认证</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>

      <div class="menu-item">
        <div class="menu-icon" style="background: linear-gradient(135deg, #fa709a, #fee140);">
          <el-icon :size="20"><View /></el-icon>
        </div>
        <span class="menu-text">长辈模式</span>
        <el-switch
          v-model="appStore.isElderMode"
          @change="toggleElderMode"
          active-color="#8B4513"
        />
      </div>
    </div>

    <!-- 设置菜单 -->
    <div class="menu-card">
      <div class="menu-item" @click="goToSettings">
        <div class="menu-icon" style="background: linear-gradient(135deg, #a8edea, #fed6e3);">
          <el-icon :size="20"><Setting /></el-icon>
        </div>
        <span class="menu-text">设置</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>

      <div class="menu-item" @click="goToHelp">
        <div class="menu-icon" style="background: linear-gradient(135deg, #667eea, #764ba2);">
          <el-icon :size="20"><QuestionFilled /></el-icon>
        </div>
        <span class="menu-text">帮助中心</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>

      <div class="menu-item" @click="goToAbout">
        <div class="menu-icon" style="background: linear-gradient(135deg, #f093fb, #f5576c);">
          <el-icon :size="20"><InfoFilled /></el-icon>
        </div>
        <span class="menu-text">关于我们</span>
        <el-icon :size="16" color="#ccc"><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- 退出登录 -->
    <el-button
      v-if="userStore.isLoggedIn"
      type="danger"
      plain
      class="logout-btn"
      @click="handleLogout"
    >
      退出登录
    </el-button>
  </div>
</template>

<script setup>
import { ElMessage, ElModal } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAppStore, useUserStore } from '@/store/app'
import {
  User,
  ArrowRight,
  Star,
  DocumentCopy,
  CircleCheck,
  View,
  Setting,
  QuestionFilled,
  InfoFilled
} from '@element-plus/icons-vue'

const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const goToLogin = () => {
  router.push('/login')
}

const toggleElderMode = () => {
  appStore.toggleElderMode()
  ElMessage.success(appStore.isElderMode ? '已开启长辈模式' : '已关闭长辈模式')
}

const goToCollection = () => {
  ElMessage.info('功能开发中')
}

const goToOrders = () => {
  ElMessage.info('功能开发中')
}

const goToVerify = () => {
  ElMessage.info('实名认证功能开发中')
}

const goToSettings = () => {
  ElMessage.info('设置功能开发中')
}

const goToHelp = () => {
  ElMessage.info('帮助中心功能开发中')
}

const goToAbout = () => {
  ElMessage({
    message: '文物收藏APP v1.0.0\n专业的文物收藏交流与交易平台',
    type: 'info',
    duration: 3000
  })
}

const handleLogout = () => {
  ElModal.confirm({
    title: '提示',
    content: '确定要退出登录吗？',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    onConfirm: () => {
      userStore.logout()
      ElMessage.success('已退出登录')
    }
  })
}
</script>

<style scoped>
.mine-container {
  padding: 20px;
}

.user-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.user-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color: #fff;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 8px;
}

.user-desc {
  font-size: 14px;
}

.login-prompt {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 16px;
  color: #fff;
  font-size: 18px;
  cursor: pointer;
}

.stats-card {
  display: flex;
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #8B4513;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.menu-card {
  background: #fff;
  border-radius: 12px;
  margin-bottom: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  color: #fff;
}

.menu-text {
  flex: 1;
  font-size: 16px;
  color: #333;
}

.logout-btn {
  width: 100%;
  margin-top: 20px;
  padding: 14px;
  font-size: 16px;
}

.elder-mode .user-name {
  font-size: 24px;
}

.elder-mode .menu-text {
  font-size: 20px;
}

.elder-mode .stat-value {
  font-size: 26px;
}

.elder-mode .logout-btn {
  font-size: 18px;
  padding: 18px;
}
</style>
