<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">裁判管理系统</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin || isChiefReferee || isReferee" index="/dashboard/referee">
          <el-icon><User /></el-icon>
          <span>裁判管理</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin || isChiefReferee || isReferee" index="/dashboard/athlete">
          <el-icon><Trophy /></el-icon>
          <span>运动员管理</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin || isChiefReferee || isReferee" index="/dashboard/competition">
          <el-icon><Tickets /></el-icon>
          <span>比赛管理</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin || isChiefReferee || isReferee" index="/dashboard/score">
          <el-icon><Star /></el-icon>
          <span>评分管理</span>
        </el-menu-item>
        <el-menu-item v-if="isChiefReferee || isAdmin" index="/dashboard/audit">
          <el-icon><DocumentChecked /></el-icon>
          <span>评分审核</span>
        </el-menu-item>
        <el-menu-item v-if="isAthlete" index="/dashboard/my-scores">
          <el-icon><List /></el-icon>
          <span>我的成绩</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin" index="/dashboard/user">
          <el-icon><Setting /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="breadcrumb">
          <span>当前用户：{{ user.realName }} ({{ getRoleName(user.role) }})</span>
        </div>
        <div class="user-info">
          <el-button type="text" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))

const activeMenu = computed(() => route.path)

const isAdmin = computed(() => user.value.role === 'ADMIN')
const isChiefReferee = computed(() => user.value.role === 'CHIEF_REFEREE')
const isReferee = computed(() => user.value.role === 'REFEREE')
const isAthlete = computed(() => user.value.role === 'ATHLETE')

const getRoleName = (role) => {
  const roleMap = {
    'ADMIN': '管理员',
    'CHIEF_REFEREE': '裁判长',
    'REFEREE': '裁判',
    'ATHLETE': '运动员'
  }
  return roleMap[role] || role
}

const logout = () => {
  localStorage.removeItem('user')
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
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
  color: white;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
}

.header {
  background-color: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.breadcrumb {
  color: #666;
}

.user-info {
  display: flex;
  align-items: center;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>
