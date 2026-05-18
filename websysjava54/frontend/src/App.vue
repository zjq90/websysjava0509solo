<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu router default-active="1" class="el-menu-vertical-demo">
          <el-menu-item index="1">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-goods"></i>
              <span>文物管理</span>
            </template>
            <el-menu-item index="/heritage-audit">文物审核</el-menu-item>
            <el-menu-item index="/heritage-list">文物列表</el-menu-item>
            <el-menu-item index="/category-tags">分类标签</el-menu-item>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/user-list">用户列表</el-menu-item>
            <el-menu-item index="/realname-audit">实名认证</el-menu-item>
            <el-menu-item index="/expert-audit">专家认证</el-menu-item>
            <el-menu-item index="/suspicious-users">可疑用户</el-menu-item>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-s-finance"></i>
              <span>交易监管</span>
            </template>
            <el-menu-item index="/transaction-list">交易列表</el-menu-item>
            <el-menu-item index="/abnormal-transactions">异常交易</el-menu-item>
            <el-menu-item index="/frozen-transactions">冻结交易</el-menu-item>
            <el-menu-item index="/report-list">举报处理</el-menu-item>
          </el-submenu>
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-s-data"></i>
              <span>数据看板</span>
            </template>
            <el-menu-item index="/dashboard">数据统计</el-menu-item>
            <el-menu-item index="/heatmap">热力图</el-menu-item>
            <el-menu-item index="/user-growth">用户增长</el-menu-item>
          </el-submenu>
          <el-submenu index="6">
            <template slot="title">
              <i class="el-icon-setting"></i>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/desensitization-rules">脱敏规则</el-menu-item>
            <el-menu-item index="/monitoring-rules">监测规则</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <div style="line-height: 60px">
            <span>文物收藏管理后台</span>
            <el-dropdown>
              <i class="el-icon-setting" style="margin-left: 15px"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>查看</el-dropdown-item>
                <el-dropdown-item>新增</el-dropdown-item>
                <el-dropdown-item>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view />
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
  background-color: #409EFF;
  color: white;
  text-align: center;
  line-height: 60px;
}
.el-aside {
  color: #333;
}
</style>
