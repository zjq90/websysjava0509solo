<template>
  <el-container class="app-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon :size="28" color="#fff"><Gamepad /></el-icon>
        <span class="logo-text">游戏平台管理</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1f2d3d"
        text-color="#c0c4cc"
        active-text-color="#409eff"
        class="menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-sub-menu index="ad">
          <template #title>
            <el-icon><Picture /></el-icon>
            <span>广告管理</span>
          </template>
          <el-menu-item index="/ad-slots">
            <el-icon><Grid /></el-icon>
            <span>广告位配置</span>
          </el-menu-item>
          <el-menu-item index="/advertisements">
            <el-icon><Promotion /></el-icon>
            <span>广告管理</span>
          </el-menu-item>
          <el-menu-item index="/revenue-stats">
            <el-icon><TrendCharts /></el-icon>
            <span>收益统计</span>
          </el-menu-item>
          <el-menu-item index="/reward-video">
            <el-icon><VideoPlay /></el-icon>
            <span>激励视频配置</span>
          </el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="message">
          <template #title>
            <el-icon><Message /></el-icon>
            <span>消息推送管理</span>
          </template>
          <el-menu-item index="/push-tasks">
            <el-icon><ChatDotRound /></el-icon>
            <span>站内信推送</span>
          </el-menu-item>
          <el-menu-item index="/message-templates">
            <el-icon><Document /></el-icon>
            <span>模板管理</span>
          </el-menu-item>
          <el-menu-item index="/popup-announcements">
            <el-icon><Bell /></el-icon>
            <span>弹窗公告</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-avatar :size="32" style="background-color: #409eff">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <span class="username">管理员</span>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '首页')
</script>

<style scoped>
.app-container {
  height: 100vh;
}

.sidebar {
  background-color: #1f2d3d;
  transition: width 0.3s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid #304156;
}

.logo-text {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}

.menu {
  border-right: none;
}

.header {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  color: #606266;
  font-size: 14px;
}

.main {
  background-color: #f5f7fa;
  padding: 0;
  overflow-y: auto;
}
</style>
