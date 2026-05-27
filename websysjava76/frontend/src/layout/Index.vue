<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon><OfficeBuilding /></el-icon>
        <span>社团管理系统</span>
      </div>
      <el-menu
        :default-active="$route.path"
        class="menu"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        
        <el-sub-menu index="club">
          <template #title>
            <el-icon><User /></el-icon>
            <span>社团审核与管理</span>
          </template>
          <el-menu-item index="/club/list">社团列表</el-menu-item>
          <el-menu-item index="/club/application">成立申请</el-menu-item>
          <el-menu-item index="/club/registration">年度注册</el-menu-item>
          <el-menu-item index="/club/violation">违规处理</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="activity">
          <template #title>
            <el-icon><Calendar /></el-icon>
            <span>活动监管</span>
          </template>
          <el-menu-item index="/activity/list">活动列表</el-menu-item>
          <el-menu-item index="/activity/approval">活动审批</el-menu-item>
          <el-menu-item index="/activity/review">内容审查</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="statistics">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>数据统计与分析</span>
          </template>
          <el-menu-item index="/statistics/club">社团数据</el-menu-item>
          <el-menu-item index="/statistics/finance">经费监管</el-menu-item>
        </el-sub-menu>
        
        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统设置</span>
          </template>
          <el-menu-item index="/system/user">权限管理</el-menu-item>
          <el-menu-item index="/system/template">消息模板</el-menu-item>
          <el-menu-item index="/system/backup">数据备份</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon><Avatar /></el-icon>
              <span>管理员</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background: linear-gradient(180deg, #304156 0%, #1f2d3d 100%);
  color: #fff;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.logo .el-icon {
  margin-right: 8px;
  font-size: 24px;
}

.menu {
  border-right: none;
  background: transparent;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #bfcbd9 !important;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover),
:deep(.el-sub-menu .el-menu-item:hover) {
  background-color: rgba(64, 158, 255, 0.9) !important;
  color: #fff !important;
  text-shadow: 0 1px 2px rgba(0,0,0,0.2) !important;
}

:deep(.el-sub-menu .el-menu-item) {
  color: #bfcbd9 !important;
}

:deep(.el-sub-menu .el-menu-item.is-active) {
  background-color: #409eff !important;
  color: #fff !important;
}

:deep(.el-menu--inline .el-menu-item) {
  min-width: 100% !important;
  box-sizing: border-box !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #409eff !important;
  color: #fff !important;
}

.header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
}

.user-info span {
  margin: 0 5px;
}

.main {
  background: #f0f2f5;
  overflow-y: auto;
}
</style>
