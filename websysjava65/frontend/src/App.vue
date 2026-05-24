<template>
  <el-container style="height: 100%">
    <el-aside width="220px" style="background-color: #2f4050">
      <div style="padding: 20px; text-align: center; border-bottom: 1px solid #1f2d3d">
        <h2 style="color: white; margin: 0; font-size: 18px">🚲 共享单车管理</h2>
      </div>
      <el-menu
        background-color="#2f4050"
        text-color="#a7b1c2"
        active-text-color="#fff"
        :default-active="$route.path"
        router
        style="border: none"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>数据看板</span>
        </el-menu-item>
        <el-sub-menu index="/user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user/blacklist">黑名单管理</el-menu-item>
          <el-menu-item index="/user/tickets">客服工单</el-menu-item>
          <el-menu-item index="/user/membership">用户分层</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="/finance">
          <template #title>
            <el-icon><Money /></el-icon>
            <span>财务管理</span>
          </template>
          <el-menu-item index="/finance/reconciliation">自动对账</el-menu-item>
          <el-menu-item index="/finance/invoices">发票管理</el-menu-item>
          <el-menu-item index="/finance/cost">成本分析</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: white; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: space-between; padding: 0 20px">
        <div style="font-size: 18px; font-weight: 500; color: #333">
          {{ currentPageTitle }}
        </div>
        <div style="display: flex; align-items: center; gap: 15px">
          <el-badge :value="3" class="item">
            <el-button circle type="primary" :icon="Bell" />
          </el-badge>
          <el-avatar :size="32" style="background: #409eff">A</el-avatar>
          <span style="color: #606266">管理员</span>
        </div>
      </el-header>
      <el-main style="padding: 0; background: #f5f7fa">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Bell, DataLine, User, Money } from '@element-plus/icons-vue'

const route = useRoute()

const currentPageTitle = computed(() => {
  const titles = {
    '/dashboard': '数据看板',
    '/user/blacklist': '黑名单管理',
    '/user/tickets': '客服工单',
    '/user/membership': '用户分层运营',
    '/finance/reconciliation': '自动对账',
    '/finance/invoices': '发票管理',
    '/finance/cost': '成本分析'
  }
  return titles[route.path] || '共享单车管理后台'
})
</script>
