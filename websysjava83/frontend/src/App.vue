<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-aside width="200px" style="background-color: #304156">
        <div style="height: 60px; line-height: 60px; text-align: center; color: white; font-size: 18px; font-weight: bold">
          游戏平台
        </div>
        <el-menu
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-data-line"></i>
            <span slot="title">数据概览</span>
          </el-menu-item>
          <el-menu-item index="/game-analytics">
            <i class="el-icon-s-data"></i>
            <span slot="title">游戏分析</span>
          </el-menu-item>
          <el-menu-item index="/system-config">
            <i class="el-icon-setting"></i>
            <span slot="title">系统配置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; line-height: 60px; padding: 0 20px">
          <span style="font-size: 18px; font-weight: 500">{{ pageTitle }}</span>
        </el-header>
        <el-main style="background-color: #f0f2f5; overflow: auto">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    pageTitle() {
      const route = this.$route
      const titles = {
        '/dashboard': '数据概览',
        '/game-analytics': '游戏分析',
        '/system-config': '系统配置'
      }
      return titles[route.path] || '数据概览'
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
}

#app {
  height: 100%;
}
</style>
