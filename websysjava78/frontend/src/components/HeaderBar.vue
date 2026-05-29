<template>
  <header class="header">
    <div class="header-content">
      <div class="logo" @click="$router.push('/')">
        <el-icon :size="32" color="#fff"><Music /></el-icon>
        <span class="logo-text">音乐播放器</span>
      </div>
      <nav class="nav">
        <router-link to="/" class="nav-link" exact-active-class="active">首页</router-link>
        <router-link to="/music" class="nav-link" active-class="active">音乐库</router-link>
        <router-link v-if="userStore.isLoggedIn" to="/user" class="nav-link" active-class="active">个人中心</router-link>
        <router-link v-if="userStore.isLoggedIn" to="/admin" class="nav-link" active-class="active">管理</router-link>
      </nav>
      <div class="user-area">
        <template v-if="userStore.isLoggedIn">
          <span class="user-info">
            <el-tag :type="userStore.isPremium ? 'warning' : 'info'" size="small">
              {{ userStore.isPremium ? 'VIP' : '免费' }}
            </el-tag>
            <span class="username">{{ userStore.user?.nickname || userStore.user?.username }}</span>
          </span>
          <el-button type="danger" size="small" plain @click="logout">退出</el-button>
        </template>
        <template v-else>
          <el-button type="primary" size="small" @click="$router.push('/login')">登录</el-button>
          <el-button size="small" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.header {
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  padding: 0 30px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-text {
  color: white;
  font-size: 20px;
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav-link {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  font-size: 15px;
  transition: color 0.3s;
  padding: 8px 0;
  position: relative;
}

.nav-link:hover {
  color: white;
}

.nav-link.active {
  color: white;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 1px;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
}

.username {
  font-size: 14px;
}
</style>
