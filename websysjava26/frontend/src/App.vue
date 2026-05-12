<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-aside width="200px" style="background-color: #2F4056">
        <div style="color: white; text-align: center; padding: 20px 0; font-size: 18px; font-weight: bold">
          医院收费管理系统
        </div>
        <el-menu
          background-color="#2F4056"
          text-color="#fff"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="/patients">
            <i class="el-icon-user"></i>
            <span slot="title">患者管理</span>
          </el-menu-item>
          <el-menu-item index="/outpatient">
            <i class="el-icon-s-order"></i>
            <span slot="title">门诊收费</span>
          </el-menu-item>
          <el-menu-item index="/inpatient">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">住院收费</span>
          </el-menu-item>
          <el-menu-item index="/insurance">
            <i class="el-icon-s-finance"></i>
            <span slot="title">医保结算</span>
          </el-menu-item>
          <el-menu-item index="/report">
            <i class="el-icon-s-data"></i>
            <span slot="title">财务报表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: flex-end">
          <span style="margin-right: 20px">欢迎, 管理员</span>
        </el-header>
        <el-main style="background-color: #f0f2f5">
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
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
