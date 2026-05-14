<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee;">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246);">
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo" router>
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="/workorders">
            <i class="el-icon-s-order"></i>
            <span slot="title">工单管理</span>
          </el-menu-item>
          <el-menu-item index="/users">
            <i class="el-icon-user"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>
          <el-menu-item index="/technicians">
            <i class="el-icon-s-custom"></i>
            <span slot="title">装维人员</span>
          </el-menu-item>
          <el-menu-item index="/analytics">
            <i class="el-icon-data-line"></i>
            <span slot="title">数据分析</span>
          </el-menu-item>
          <el-menu-item index="/marketing">
            <i class="el-icon-present"></i>
            <span slot="title">营销管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px; background: #fff; border-bottom: 1px solid #eee;">
          <div style="line-height: 60px; font-size: 18px; font-weight: bold; float: left;">
            运营端后台管理系统
          </div>
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>管理员</span>
        </el-header>
        <el-main style="background: #f5f7fa;">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
