<template>
  <div class="layout-container">
    <el-container>
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h2>二手交易系统</h2>
        </div>
        <el-menu
          :default-active="$route.path"
          class="sidebar-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          
          <el-submenu index="/product">
            <template slot="title">
              <i class="el-icon-goods"></i>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/product/list">商品列表</el-menu-item>
            <el-menu-item index="/product/low-stock">库存预警</el-menu-item>
          </el-submenu>
          
          <el-submenu index="/order">
            <template slot="title">
              <i class="el-icon-s-order"></i>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/order/list">订单列表</el-menu-item>
            <el-menu-item index="/order/abnormal">异常订单</el-menu-item>
          </el-submenu>
          
          <el-menu-item index="/dispute/list">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">纠纷管理</span>
          </el-menu-item>
          
          <el-submenu index="/statistics">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>数据统计</span>
            </template>
            <el-menu-item index="/statistics/transaction">交易额统计</el-menu-item>
            <el-menu-item index="/statistics/product-ranking">商品排行</el-menu-item>
            <el-menu-item index="/statistics/user-growth">用户增长</el-menu-item>
          </el-submenu>
          
          <el-submenu index="/system">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/user">用户管理</el-menu-item>
            <el-menu-item index="/system/operation-log">操作日志</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      
      <el-container class="main-container">
        <el-header class="header">
          <span class="page-title">{{ currentPageTitle }}</span>
          <div class="user-info">
            <el-dropdown>
              <span class="el-dropdown-link">
                <i class="el-icon-user-solid"></i> 管理员
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    currentPageTitle() {
      return this.$route.meta.title || '首页'
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100%;
}

.sidebar {
  background-color: #304156;
  height: 100vh;
  overflow-y: auto;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  border-bottom: 1px solid #1f2d3d;
}

.logo h2 {
  color: #fff;
  font-size: 18px;
  margin: 0;
  font-weight: 600;
}

.sidebar-menu {
  border-right: none;
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  flex-shrink: 0;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: #606266;
  display: flex;
  align-items: center;
}

.main-content {
  background-color: #f5f7fa;
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}
</style>