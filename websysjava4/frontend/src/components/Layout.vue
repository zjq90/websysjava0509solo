<template>
  <div class="app-container">
    <div class="sidebar">
      <div class="logo">管理平台</div>
      <el-menu
        :default-active="activeMenu"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#1890ff"
        @select="handleMenuSelect"
      >
        <el-menu-item index="dashboard">
          <span>📊</span>
          <span>数据大屏</span>
        </el-menu-item>
        <el-menu-item index="products">
          <span>📦</span>
          <span>商品统计</span>
        </el-menu-item>
        <el-menu-item index="devices">
          <span>⚙️</span>
          <span>设备效能</span>
        </el-menu-item>
        <el-menu-item index="users">
          <span>👥</span>
          <span>用户统计</span>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="main-content">
      <div class="header">
        <div class="header-title">{{ currentTitle }}</div>
        <div>
          <el-tag type="success">系统运行中</el-tag>
        </div>
      </div>
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'Layout',
  setup() {
    const route = useRoute()
    const router = useRouter()

    const activeMenu = computed(() => route.name?.toLowerCase() || 'dashboard')
    
    const currentTitle = computed(() => {
      const titles = {
        'dashboard': '数据大屏',
        'products': '商品统计',
        'devices': '设备效能统计',
        'users': '用户行为统计'
      }
      return titles[route.name?.toLowerCase()] || '数据大屏'
    })

    const handleMenuSelect = (index) => {
      router.push(`/${index}`)
    }

    return {
      activeMenu,
      currentTitle,
      handleMenuSelect
    }
  }
}
</script>
