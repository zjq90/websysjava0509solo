<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <div style="text-align: center; padding: 20px 0">
          <h2 style="color: #409EFF">LIMS系统</h2>
        </div>
        <el-menu router default-active="1" class="el-menu-vertical-demo">
          <el-menu-item index="1" route="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-order"></i>
              <span>申请管理</span>
            </template>
            <el-menu-item index="2-1" route="/applications">申请列表</el-menu-item>
            <el-menu-item index="2-2" route="/applications/add">新增申请</el-menu-item>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-s-cooperation"></i>
              <span>技师工作站</span>
            </template>
            <el-menu-item index="3-1" route="/technician">待处理任务</el-menu-item>
            <el-menu-item index="3-2" route="/results">结果录入</el-menu-item>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-s-check"></i>
              <span>报告审核</span>
            </template>
            <el-menu-item index="4-1" route="/reports">报告列表</el-menu-item>
            <el-menu-item index="4-2" route="/reports/audit">待审核报告</el-menu-item>
          </el-submenu>
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>基础数据</span>
            </template>
            <el-menu-item index="5-1" route="/departments">科室管理</el-menu-item>
            <el-menu-item index="5-2" route="/items">项目管理</el-menu-item>
            <el-menu-item index="5-3" route="/patients">患者管理</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>管理员</span>
        </el-header>
        <el-main>
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
* {
  margin: 0;
  padding: 0;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #eee;
}
.el-aside {
  color: #333;
}
</style>
