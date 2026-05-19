<template>
  <el-container style="height: 100vh; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu router default-active="1" class="el-menu-vertical-demo">
        <el-menu-item index="/">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <el-menu-item index="/heritage">
          <i class="el-icon-s-management"></i>
          <span slot="title">文物管理</span>
        </el-menu-item>
        <el-menu-item index="/data-governance">
          <i class="el-icon-s-data"></i>
          <span slot="title">数据治理</span>
        </el-menu-item>
        <el-menu-item index="/data-analysis">
          <i class="el-icon-s-marketing"></i>
          <span slot="title">数据分析</span>
        </el-menu-item>
        <el-menu-item index="/risk-alert">
          <i class="el-icon-warning"></i>
          <span slot="title">风险预警</span>
        </el-menu-item>
        <el-menu-item index="/test-data">
          <i class="el-icon-s-tools"></i>
          <span slot="title">测试数据</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <div style="float: left; font-size: 20px; font-weight: bold; line-height: 60px;">
          文物收藏数据中台
        </div>
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>管理员</span>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
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
.el-header {
  background-color: #409EFF;
  color: #fff;
  line-height: 60px;
  padding: 0 20px !important;
}
.el-aside {
  color: #333;
}
.el-main {
  background-color: #f5f7fa;
}
</style>
