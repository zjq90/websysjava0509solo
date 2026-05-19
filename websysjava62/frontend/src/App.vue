<template>
  <el-container style="height: 100vh; border: 1px solid #eee">
    <el-aside width="220px" style="background-color: #2f4050">
      <div class="logo">
        <h3 style="color: #fff; text-align: center; padding: 20px 0">
          <el-icon><Medical /></el-icon>
          宠物问诊系统
        </h3>
      </div>
      <el-menu
        background-color="#2f4050"
        text-color="#fff"
        active-text-color="#1890ff"
        :default-active="activeMenu"
        router
      >
        <el-menu-item index="/emergency-symptoms">
          <el-icon><Warning /></el-icon>
          <span>紧急症状库</span>
        </el-menu-item>
        <el-menu-item index="/medicine">
          <el-icon><MedicineBox /></el-icon>
          <span>药品管理</span>
        </el-menu-item>
        <el-menu-item index="/review-rules">
          <el-icon><Document /></el-icon>
          <span>审核规则</span>
        </el-menu-item>
        <el-menu-item index="/smart-device">
          <el-icon><Monitor /></el-icon>
          <span>智能设备</span>
        </el-menu-item>
        <el-menu-item index="/consultation">
          <el-icon><ChatDotRound /></el-icon>
          <span>问诊管理</span>
        </el-menu-item>
        <el-menu-item index="/insurance">
          <el-icon><Wallet /></el-icon>
          <span>保险理赔</span>
        </el-menu-item>
        <el-menu-item index="/ai-diagnosis">
          <el-icon><Cpu /></el-icon>
          <span>AI诊断建议</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="text-align: right; font-size: 12px; background: #fff; border-bottom: 1px solid #e6e6e6">
        <div style="line-height: 60px; margin-right: 20px">
          <el-tag type="success">管理员</el-tag>
          <span style="margin-left: 10px">欢迎使用宠物问诊管理系统</span>
        </div>
      </el-header>
      <el-main style="background-color: #f5f7fa">
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
</script>

<style scoped>
.logo {
  border-bottom: 1px solid #19aa8d;
}

.el-menu-item {
  height: 50px;
  line-height: 50px;
}
</style>
