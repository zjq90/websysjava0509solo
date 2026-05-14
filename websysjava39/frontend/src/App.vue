<template>
  <div id="app">
    <el-container style="height: 100vh">
      <el-aside width="200px" style="background-color: #2D3B45; color: white">
        <div style="text-align: center; padding: 20px; font-size: 18px; font-weight: bold; border-bottom: 1px solid #4A5967">
          影楼管理系统
        </div>
        <el-menu
          default-active="0"
          class="el-menu-vertical"
          background-color="#2D3B45"
          text-color="#fff"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-data-line"></i>
            <span slot="title">销售业绩看板</span>
          </el-menu-item>
          <el-menu-item index="/conversion-funnel">
            <i class="el-icon-data-analysis"></i>
            <span slot="title">客户转化漏斗</span>
          </el-menu-item>
          <el-menu-item index="/employee-performance">
            <i class="el-icon-user-solid"></i>
            <span slot="title">员工绩效排行</span>
          </el-menu-item>
          <el-menu-item index="/finance-report">
            <i class="el-icon-s-finance"></i>
            <span slot="title">财务报表</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <i class="el-icon-s-order"></i>
            <span slot="title">订单管理</span>
          </el-menu-item>
          <el-menu-item index="/reimbursements">
            <i class="el-icon-s-ticket"></i>
            <span slot="title">报销管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: space-between">
          <span style="font-size: 20px; font-weight: bold">{{ pageTitle }}</span>
          <el-button type="primary" size="small" @click="initData">初始化测试数据</el-button>
        </el-header>
        <el-main style="background-color: #f5f7fa">
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
      const titles = {
        '/dashboard': '销售业绩看板',
        '/conversion-funnel': '客户转化漏斗',
        '/employee-performance': '员工绩效排行',
        '/finance-report': '财务报表',
        '/orders': '订单管理',
        '/reimbursements': '报销管理'
      }
      return titles[this.$route.path] || '影楼管理系统'
    }
  },
  methods: {
    async initData() {
      try {
        await this.$http.post('/init/all')
        this.$message.success('测试数据初始化成功！')
      } catch (error) {
        this.$message.error('初始化失败，请确保后端服务已启动')
      }
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

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.el-header {
  padding: 0 20px;
}

.el-main {
  padding: 20px;
}
</style>
