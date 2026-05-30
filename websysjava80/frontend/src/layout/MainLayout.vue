<template>
  <el-container class="main-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon :size="28"><Gamepad /></el-icon>
        <span>小游戏管理平台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :router="true"
        background-color="#1f2937"
        text-color="#9ca3af"
        active-text-color="#ffffff"
        class="menu"
      >
        <el-menu-item v-for="route in menuRoutes" :key="route.path" :index="`/${route.path}`">
          <el-icon><component :is="route.meta.icon" /></el-icon>
          <span>{{ route.meta.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="breadcrumb">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="user-info">
          <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          <span class="username">管理员</span>
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
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const menuRoutes = computed(() => {
  return router.options.routes[0].children.filter(r => r.meta && r.meta.title)
})

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '')
</script>

<style scoped lang="scss">
.main-container {
  height: 100vh;
}

.sidebar {
  background: #1f2937;
  display: flex;
  flex-direction: column;

  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    color: #fff;
    font-size: 18px;
    font-weight: 600;
    border-bottom: 1px solid #374151;
  }

  .menu {
    flex: 1;
    border-right: none;
  }
}

.header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);

  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;

    .username {
      color: #374151;
      font-size: 14px;
    }
  }
}

.main-content {
  background: #f0f2f5;
  padding: 0;
  overflow-y: auto;
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
