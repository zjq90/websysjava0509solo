<template>
  <div class="layout">
    <div class="main-content">
      <router-view />
    </div>
    <div class="tab-bar">
      <div
        v-for="item in tabs"
        :key="item.path"
        class="tab-item"
        :class="{ active: activeTab === item.path }"
        @click="goTo(item.path)"
      >
        <el-icon :size="24"><component :is="item.icon" /></el-icon>
        <span>{{ item.title }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  House,
  ChatDotRound,
  ShoppingBag,
  Calendar,
  User
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const tabs = [
  { path: '/home', title: '首页', icon: House },
  { path: '/community', title: '藏友圈', icon: ChatDotRound },
  { path: '/market', title: '市场', icon: ShoppingBag },
  { path: '/activity', title: '活动', icon: Calendar },
  { path: '/mine', title: '我的', icon: User }
]

const activeTab = computed(() => route.path)

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  padding-bottom: 70px;
}

.main-content {
  max-width: 768px;
  margin: 0 auto;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  max-width: 768px;
  margin: 0 auto;
  display: flex;
  background: #fff;
  border-top: 1px solid #e5e5e5;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  z-index: 100;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
  color: #999;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-item.active {
  color: #8B4513;
}

.tab-item span {
  margin-top: 4px;
}

.elder-mode .tab-item {
  font-size: 16px;
  padding: 16px 0;
}

.elder-mode .tab-item .el-icon {
  font-size: 28px;
}
</style>
