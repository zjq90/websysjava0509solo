<template>
  <div id="app">
    <el-container style="height: 100vh; border: 1px solid #eee">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <div style="text-align: center; padding: 20px 0; border-bottom: 1px solid #ddd">
          <h3>🏥 医院管理系统</h3>
        </div>
        <el-menu :default-active="$route.path" router style="border-right: none">
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-submenu index="admission">
            <template slot="title">
              <i class="el-icon-s-custom"></i>
              <span>入院管理</span>
            </template>
            <el-menu-item index="/patient">患者信息</el-menu-item>
            <el-menu-item index="/hospitalization">住院登记</el-menu-item>
          </el-submenu>
          <el-submenu index="doctor">
            <template slot="title">
              <i class="el-icon-user-solid"></i>
              <span>医生工作站</span>
            </template>
            <el-menu-item index="/medical-record">病历管理</el-menu-item>
            <el-menu-item index="/medical-order">医嘱管理</el-menu-item>
          </el-submenu>
          <el-submenu index="nurse">
            <template slot="title">
              <i class="el-icon-s-help"></i>
              <span>护士工作站</span>
            </template>
            <el-menu-item index="/vital-signs">生命体征</el-menu-item>
            <el-menu-item index="/bed">床位管理</el-menu-item>
          </el-submenu>
          <el-menu-item index="/medicine">
            <i class="el-icon-s-goods"></i>
            <span slot="title">药品管理</span>
          </el-menu-item>
          <el-menu-item index="/fee">
            <i class="el-icon-s-finance"></i>
            <span slot="title">费用管理</span>
          </el-menu-item>
          <el-menu-item index="/staff">
            <i class="el-icon-s-cooperation"></i>
            <span slot="title">人员管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="text-align: right; font-size: 12px; background: #fff; border-bottom: 1px solid #ddd">
          <span style="margin-right: 10px">管理员</span>
          <el-dropdown>
            <i class="el-icon-setting" style="font-size: 20px; cursor: pointer"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>
        <el-main style="background: #f0f2f5">
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
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
