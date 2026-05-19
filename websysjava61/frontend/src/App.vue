<template>
  <el-container class="app-container">
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <el-icon><Medical /></el-icon>
        <span>宠物医院</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>数据看板</span>
        </el-menu-item>
        <el-sub-menu index="hospital">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>医院管理</span>
          </template>
          <el-menu-item index="/hospital/department">科室管理</el-menu-item>
          <el-menu-item index="/hospital/doctor">医生管理</el-menu-item>
          <el-menu-item index="/hospital/schedule">排班管理</el-menu-item>
          <el-menu-item index="/hospital/medicine">药品管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="user">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user/owner">宠物主人</el-menu-item>
          <el-menu-item index="/user/doctor-audit">医生审核</el-menu-item>
          <el-menu-item index="/user/credit">信用分管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="statistics">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>统计分析</span>
          </template>
          <el-menu-item index="/statistics/disease">疾病热力图</el-menu-item>
          <el-menu-item index="/statistics/user-growth">用户增长</el-menu-item>
          <el-menu-item index="/statistics/operation">运营数据</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-title">宠物问诊管理后台</div>
        <div class="header-user">
          <el-icon><Avatar /></el-icon>
          <span>管理员</span>
        </div>
      </el-header>
      <el-main class="main-content">
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

<style lang="scss" scoped>
.app-container {
  height: 100vh;
  
  .sidebar {
    background-color: #304156;
    
    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      border-bottom: 1px solid #1f2d3d;
      
      .el-icon {
        margin-right: 8px;
        font-size: 24px;
      }
    }
  }
  
  .header {
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    
    .header-title {
      font-size: 20px;
      font-weight: bold;
      color: #303133;
    }
    
    .header-user {
      display: flex;
      align-items: center;
      color: #606266;
      
      .el-icon {
        margin-right: 8px;
      }
    }
  }
  
  .main-content {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style>
