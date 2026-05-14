<template>
  <div class="app-container">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h2>影楼管理系统</h2>
        </div>
        <el-menu :default-active="activeMenu" mode="vertical" @select="handleMenuSelect">
          <el-menu-item index="/">
            <el-icon><component :is="icons.Home" /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="employees">
            <template #title>
              <el-icon><component :is="icons.User" /></el-icon>
              <span>人员管理</span>
            </template>
            <el-menu-item index="/employees">员工管理</el-menu-item>
            <el-menu-item index="/schedules">排班管理</el-menu-item>
            <el-menu-item index="/transfer-requests">调班申请</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="venues">
            <template #title>
              <el-icon><component :is="icons.Building" /></el-icon>
              <span>场地管理</span>
            </template>
            <el-menu-item index="/venues">场地列表</el-menu-item>
            <el-menu-item index="/venue-bookings">场地预约</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="costumes">
            <template #title>
              <el-icon><component :is="icons.Shirt" /></el-icon>
              <span>服装管理</span>
            </template>
            <el-menu-item index="/costumes">服装库存</el-menu-item>
            <el-menu-item index="/costume-bookings">服装预约</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="orders">
            <template #title>
              <el-icon><component :is="icons.Shop" /></el-icon>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/orders">订单列表</el-menu-item>
            <el-menu-item index="/express">快递管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-content">
            <span class="title">{{ currentTitle }}</span>
            <div class="header-right">
              <el-button type="text" @click="refreshPage">
                <el-icon><component :is="icons.Refresh" /></el-icon>
                刷新
              </el-button>
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
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Home, User, Building, Shirt, Shop, Refresh } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const icons = { Home, User, Building, Shirt, Shop, Refresh }

const menuTitles = {
  '/': '首页',
  '/employees': '员工管理',
  '/schedules': '排班管理',
  '/transfer-requests': '调班申请',
  '/venues': '场地列表',
  '/venue-bookings': '场地预约',
  '/costumes': '服装库存',
  '/costume-bookings': '服装预约',
  '/orders': '订单列表',
  '/express': '快递管理'
}

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => menuTitles[route.path] || '影楼管理系统')

const handleMenuSelect = (index) => {
  router.push(index)
}

const refreshPage = () => {
  router.go(0)
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.app-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.app-container .el-container {
  height: 100%;
  width: 100%;
}

.sidebar {
  background: linear-gradient(180deg, #1e3a5f 0%, #2d5a87 100%);
  color: white;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  flex-shrink: 0;
}

.logo h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.sidebar .el-menu {
  border-right: none;
  background: transparent;
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
}

.sidebar .el-menu::-webkit-scrollbar {
  width: 6px;
}

.sidebar .el-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.sidebar .el-menu::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar .el-menu-item,
.sidebar .el-sub-menu__title {
  color: rgba(255, 255, 255, 0.9) !important;
}

.sidebar .el-menu-item:hover,
.sidebar .el-sub-menu__title:hover {
  background: rgba(255, 255, 255, 0.1) !important;
}

.sidebar .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.2) !important;
  color: white !important;
}

.sidebar .el-sub-menu .el-menu {
  background: rgba(30, 58, 95, 0.95) !important;
  overflow: visible;
}

.sidebar .el-sub-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.85) !important;
  background: transparent !important;
}

.sidebar .el-sub-menu .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.15) !important;
  color: white !important;
}

.sidebar .el-sub-menu .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.25) !important;
  color: white !important;
}

.app-container .el-container.is-vertical {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  background: white;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 20px;
  flex-shrink: 0;
  height: 60px !important;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  padding: 20px;
  background: #f5f5f5;
  flex: 1;
  overflow-y: auto;
  height: calc(100vh - 60px);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
