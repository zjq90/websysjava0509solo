<template>
  <el-container class="app-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <h2>游戏平台管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="menu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF">
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-sub-menu index="audit">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>内容审核</span>
          </template>
          <el-menu-item index="/audit/comment">评论审核</el-menu-item>
          <el-menu-item index="/audit/sensitive">敏感词管理</el-menu-item>
          <el-menu-item index="/audit/report">举报处理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="breadcrumbs.length > 0">
              {{ breadcrumbs[0] }}
            </el-breadcrumb-item>
            <el-breadcrumb-item v-if="breadcrumbs.length > 1">
              {{ breadcrumbs[1] }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
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

const breadcrumbs = computed(() => {
  const path = route.path
  if (path.startsWith('/user')) {
    return ['用户管理']
  } else if (path === '/audit/comment') {
    return ['内容审核', '评论审核']
  } else if (path === '/audit/sensitive') {
    return ['内容审核', '敏感词管理']
  } else if (path === '/audit/report') {
    return ['内容审核', '举报处理']
  }
  return []
})
</script>

<style scoped>
.app-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  overflow: hidden;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: #2b2f3a;
}

.logo h2 {
  color: #fff;
  font-size: 16px;
  margin: 0;
}

.menu {
  border-right: none;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-right .username {
  color: #606266;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
