<template>
  <el-container style="height: 100%">
    <el-aside width="220px" style="background-color: #304156">
      <div class="logo">
        <i class="el-icon-s-home"></i>
        <span>医院管理系统</span>
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-data"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <el-submenu index="system">
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/permission">权限管理</el-menu-item>
          <el-menu-item index="/system/dict">字典管理</el-menu-item>
          <el-menu-item index="/system/log">日志管理</el-menu-item>
          <el-menu-item index="/system/config">系统配置</el-menu-item>
        </el-submenu>
        <el-submenu index="doctor">
          <template slot="title">
            <i class="el-icon-user-solid"></i>
            <span>医生工作台</span>
          </template>
          <el-menu-item index="/doctor/patient">患者管理</el-menu-item>
        </el-submenu>
        <el-submenu index="nurse">
          <template slot="title">
            <i class="el-icon-s-custom"></i>
            <span>护士工作站</span>
          </template>
          <el-menu-item index="/nurse/ward">病房管理</el-menu-item>
        </el-submenu>
        <el-submenu index="finance">
          <template slot="title">
            <i class="el-icon-s-finance"></i>
            <span>收费管理</span>
          </template>
          <el-menu-item index="/finance/charge">收费登记</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; padding: 0 20px">
        <div class="header-content">
          <div class="breadcrumb">
            <span>{{ $route.meta.title }}</span>
          </div>
          <div class="user-info">
            <span>{{ userInfo && userInfo.realName ? userInfo.realName : (userInfo && userInfo.username ? userInfo.username : '') }}</span>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main style="background-color: #f5f7fa; padding: 20px; margin: 0">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from 'vuex'
import store from '@/store'
import router from '@/router'

export default {
  name: 'Layout',
  computed: {
    ...mapGetters(['userInfo'])
  },
  methods: {
    logout() {
      this.$confirm('确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('logout')
        router.push('/login')
        this.$message.success('退出成功')
      })
    }
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.logo i {
  margin-right: 10px;
  font-size: 24px;
}

.el-menu-vertical {
  border-right: none;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.breadcrumb {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  margin-right: 15px;
  color: #666;
}
</style>
