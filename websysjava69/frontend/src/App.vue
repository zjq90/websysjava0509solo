<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon size="28"><Wallet /></el-icon>
        <span>个人记账系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409eff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-sub-menu index="debt">
          <template #title>
            <el-icon><CreditCard /></el-icon>
            <span>债务管理</span>
          </template>
          <el-menu-item index="/debts">债务列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="asset">
          <template #title>
            <el-icon><Money /></el-icon>
            <span>资产统计</span>
          </template>
          <el-menu-item index="/assets">资产管理</el-menu-item>
          <el-menu-item index="/investments">投资组合</el-menu-item>
          <el-menu-item index="/net-worth">净资产统计</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="family">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>家庭共享</span>
          </template>
          <el-menu-item index="/families">家庭管理</el-menu-item>
          <el-menu-item index="/budgets">预算管理</el-menu-item>
          <el-menu-item index="/virtual-accounts">虚拟账户</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/notifications">
          <el-icon><Bell /></el-icon>
          <span>消息通知</span>
          <el-badge v-if="unreadCount > 0" :value="unreadCount" class="notification-badge" />
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="user-info">
          <el-avatar :size="36" style="background-color: #409eff">
            {{ currentUser?.nickname?.charAt(0) || 'U' }}
          </el-avatar>
          <div class="user-detail">
            <div class="username">{{ currentUser?.nickname || '用户' }}</div>
            <div class="user-role">{{ isFamilyAdmin ? '家庭管理员' : '家庭成员' }}</div>
          </div>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { userApi, notificationApi, familyApi, API } from '@/api'

const route = useRoute()
const activeMenu = ref(route.path)
const currentUser = ref(null)
const unreadCount = ref(0)
const isFamilyAdmin = ref(false)

const loadCurrentUser = async () => {
  try {
    currentUser.value = await userApi.get(API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const loadUnreadCount = async () => {
  try {
    unreadCount.value = await notificationApi.getUnreadCount(API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const checkAdminRole = async () => {
  try {
    isFamilyAdmin.value = await familyApi.isAdmin(API.currentFamilyId, API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadCurrentUser()
  loadUnreadCount()
  checkAdminRole()
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #001529;
  overflow-y: auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 60px;
  padding: 0 20px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo span {
  color: #fff;
}

.sidebar :deep(.el-menu) {
  border-right: none;
}

.sidebar :deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

.sidebar :deep(.el-sub-menu__title) {
  height: 50px;
  line-height: 50px;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 24px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-detail {
  text-align: right;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.user-role {
  font-size: 12px;
  color: #909399;
}

.main-content {
  background-color: #f5f7fa;
  padding: 0;
  overflow-y: auto;
}

.notification-badge {
  margin-left: 8px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
