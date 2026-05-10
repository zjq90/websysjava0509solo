<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <el-icon class="header-icon"><Management /></el-icon>
      <span style="margin-left: 10px;">Web平台管理系统</span>
    </el-header>
    <el-container>
      <el-aside width="200px" class="layout-aside">
        <el-menu
          :default-active="activeMenu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#ffffff"
          router
          @select="handleMenuSelect"
        >
          <el-menu-item index="/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据概览</span>
          </el-menu-item>
          <el-menu-item index="/exception-orders">
            <el-icon><Warning /></el-icon>
            <span>异常订单</span>
          </el-menu-item>
          <el-menu-item index="/reconciliations">
            <el-icon><Tickets /></el-icon>
            <span>对账管理</span>
          </el-menu-item>
          <el-menu-item index="/invoices">
            <el-icon><Document /></el-icon>
            <span>发票管理</span>
          </el-menu-item>
          <el-menu-item index="/sales-report">
            <el-icon><TrendCharts /></el-icon>
            <span>销售报表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => route.path)

const handleMenuSelect = (index) => {
  console.log('Menu selected:', index)
}
</script>

<style scoped>
.header-icon {
  font-size: 24px;
}
</style>
