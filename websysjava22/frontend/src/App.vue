<template>
  <el-container class="main-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h2>门诊管理系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/appointment">
          <el-icon><Calendar /></el-icon>
          <span>预约挂号</span>
        </el-menu-item>
        <el-menu-item index="/queue">
          <el-icon><Bell /></el-icon>
          <span>分诊叫号</span>
        </el-menu-item>
        <el-menu-item index="/doctor">
          <el-icon><UserFilled /></el-icon>
          <span>医生工作站</span>
        </el-menu-item>
        <el-menu-item index="/charge">
          <el-icon><Money /></el-icon>
          <span>门诊收费</span>
        </el-menu-item>
        <el-menu-item index="/pharmacy">
          <el-icon><Box /></el-icon>
          <span>药房管理</span>
        </el-menu-item>
        <el-menu-item index="/query">
          <el-icon><Search /></el-icon>
          <span>综合查询</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-title">
          <h3>医院门诊管理系统</h3>
        </div>
        <div class="header-user">
          <el-icon><User /></el-icon>
          <span>管理员</span>
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
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
}

.logo {
  padding: 20px 10px;
  text-align: center;
  border-bottom: 1px solid #1f2d3d;
}

.logo h2 {
  color: #fff;
  font-size: 18px;
  margin: 0;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-title h3 {
  margin: 0;
  color: #303133;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #606266;
}

.main {
  background-color: #f0f2f5;
}
</style>
