<template>
  <div id="app">
    <el-container>
      <el-header v-if="isLoggedIn">
        <div class="header-content">
          <div class="logo" @click="goHome">
            <el-icon><Music /></el-icon>
            <span>音乐平台</span>
          </div>
          <div class="search-bar">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索音乐、歌单、用户..."
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          <div class="header-right">
            <el-button type="primary" link @click="goToUpload" v-if="isArtistOrAdmin">
              <el-icon><Upload /></el-icon>
              上传音乐
            </el-button>
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :src="userStore.user?.avatar || 'https://picsum.photos/100/100'" />
                <span>{{ userStore.user?.nickname || userStore.user?.username }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToProfile">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item @click="goToArtist" v-if="isArtistOrAdmin">
                    <el-icon><TrendCharts /></el-icon>
                    音乐人中心
                  </el-dropdown-item>
                  <el-dropdown-item @click="goToAdmin" v-if="isAdmin">
                    <el-icon><Setting /></el-icon>
                    管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
    <MusicPlayer v-if="isLoggedIn" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'
import MusicPlayer from './components/MusicPlayer.vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')

const isLoggedIn = computed(() => userStore.isLoggedIn)
const isArtistOrAdmin = computed(() => {
  const role = userStore.user?.role
  return role === 'ARTIST' || role === 'ADMIN'
})
const isAdmin = computed(() => userStore.user?.role === 'ADMIN')

const goHome = () => router.push('/')
const goToProfile = () => router.push(`/user/${userStore.user?.id}`)
const goToUpload = () => router.push('/upload')
const goToArtist = () => router.push('/artist')
const goToAdmin = () => router.push('/admin')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push(`/search?keyword=${encodeURIComponent(searchKeyword.value)}`)
  }
}

const handleCommand = (command: string) => {
  console.log(command)
}

const logout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('已退出登录')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  min-height: 100vh;
  background: #f5f5f5;
}

.el-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 64px !important;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  cursor: pointer;
}

.search-bar {
  flex: 0 0 400px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 20px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f5f5;
}

.el-main {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}
</style>
