<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee;">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246);">
        <el-menu router mode="vertical" default-active="1" class="el-menu-vertical-demo">
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>运营指标</span>
            </template>
            <el-menu-item index="/operation/list">指标列表</el-menu-item>
            <el-menu-item index="/operation/statistics">统计分析</el-menu-item>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-claim"></i>
              <span>医疗质量</span>
            </template>
            <el-menu-item index="/medical-quality/list">质量列表</el-menu-item>
            <el-menu-item index="/medical-quality/statistics">质量分析</el-menu-item>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-s-finance"></i>
              <span>成本效益</span>
            </template>
            <el-menu-item index="/cost-benefit/list">效益列表</el-menu-item>
            <el-menu-item index="/cost-benefit/statistics">效益分析</el-menu-item>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-s-tools"></i>
              <span>自定义报表</span>
            </template>
            <el-menu-item index="/custom-reports/list">报表管理</el-menu-item>
            <el-menu-item index="/custom-reports/create">新建报表</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px; background: #fff; border-bottom: 1px solid #eee;">
          <div style="float: left; font-size: 18px; font-weight: bold; line-height: 60px;">
            医院统计分析与决策支持系统
          </div>
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
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
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}
.el-aside {
  color: #333;
}
</style>
