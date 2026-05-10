<template>
  <div class="layout-container">
    <header class="layout-header">
      <div class="header-left">
        <i
          class="el-icon-s-fold collapse-btn"
          :class="{ 'el-icon-s-unfold': collapsed }"
          @click="toggleSidebar"
        ></i>
        <span class="system-title">Web平台管理系统</span>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <div class="user-avatar">{{ userInfo ? userInfo.realName ? userInfo.realName.charAt(0) : 'U' : 'U' }}</div>
            <span class="user-name">{{ userInfo ? userInfo.realName || userInfo.username : '' }}</span>
            <i class="el-icon-arrow-down el-icon--right"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人信息</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </header>
    <div class="layout-body">
      <aside class="layout-sidebar" :class="{ collapsed: collapsed }">
        <el-menu
          :default-active="activeMenu"
          :collapse="collapsed"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
          router
          unique-opened
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="/agents">
            <i class="el-icon-s-custom"></i>
            <span slot="title">代理商管理</span>
          </el-menu-item>
          <el-menu-item index="/logs">
            <i class="el-icon-document"></i>
            <span slot="title">操作日志</span>
          </el-menu-item>
          <el-menu-item index="/configs">
            <i class="el-icon-setting"></i>
            <span slot="title">系统配置</span>
          </el-menu-item>
        </el-menu>
      </aside>
      <main class="layout-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'Layout',
  computed: {
    ...mapGetters(['userInfo', 'collapsed']),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    }
  },
  methods: {
    ...mapActions(['ToggleSidebar', 'Logout']),
    toggleSidebar() {
      this.ToggleSidebar()
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.Logout()
          this.$router.push('/login')
          this.$message.success('已退出登录')
        }).catch(() => {})
      }
    }
  }
}
</script>
