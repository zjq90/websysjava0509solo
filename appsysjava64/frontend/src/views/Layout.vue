<template>
  <div class="layout-container">
    <div class="main-content">
      <router-view />
    </div>
    <div class="tab-bar">
      <div 
        v-for="item in tabs" 
        :key="item.path"
        class="tab-item"
        :class="{ active: activePath === item.path }"
        @click="goTo(item.path)"
      >
        <el-icon class="tab-icon"><component :is="item.icon" /></el-icon>
        <span class="tab-text">{{ item.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { List, Location, Camera, Battery, User } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const tabs = [
  { path: '/tasks', name: '任务', icon: List },
  { path: '/map', name: '地图', icon: Location },
  { path: '/scan', name: '扫码', icon: Camera },
  { path: '/battery', name: '电池', icon: Battery },
  { path: '/profile', name: '我的', icon: User }
]

const activePath = computed(() => route.path)

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.layout-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  max-width: 480px;
  margin: 0 auto;
  background: #f5f5f5;
}

.main-content {
  flex: 1;
  padding-bottom: 60px;
  overflow-y: auto;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 480px;
  height: 60px;
  background: #fff;
  display: flex;
  border-top: 1px solid #eee;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
  z-index: 1000;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  
  .tab-icon {
    font-size: 22px;
    color: #999;
    margin-bottom: 4px;
    transition: all 0.3s;
  }
  
  .tab-text {
    font-size: 12px;
    color: #999;
    transition: all 0.3s;
  }
  
  &.active {
    .tab-icon, .tab-text {
      color: #667eea;
    }
  }
}
</style>
