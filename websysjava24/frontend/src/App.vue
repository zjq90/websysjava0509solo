<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu router style="height: 100%">
          <div style="text-align: center; padding: 20px 0; font-size: 16px; font-weight: bold; color: #409EFF">
            🏥 医院管理系统
          </div>
          <el-submenu index="medicine">
            <template slot="title"><i class="el-icon-medicine-box"></i>药品管理</template>
            <el-menu-item index="/medicine/list">药品字典</el-menu-item>
            <el-menu-item index="/medicine/inventory">药品库存</el-menu-item>
          </el-submenu>
          <el-submenu index="material">
            <template slot="title"><i class="el-icon-goods"></i>物资管理</template>
            <el-menu-item index="/material/list">物资字典</el-menu-item>
            <el-menu-item index="/material/inventory">物资库存</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px">
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
.el-main {
  background-color: #f5f7fa;
}
</style>
