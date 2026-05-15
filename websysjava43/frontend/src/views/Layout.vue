<template>
  <div class="layout-container">
    <el-header class="layout-header">
      <div class="header-left">
        <h3 class="system-title">教学辅助系统</h3>
      </div>
      <div class="header-right">
        <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="message-badge">
          <el-button type="text" icon="el-icon-message" @click="$router.push('/messages')">消息</el-button>
        </el-badge>
        <el-dropdown @command="handleCommand">
          <span class="user-dropdown">
            <i class="el-icon-user"></i>
            {{ userInfo.realName || userInfo.username }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="layout-aside">
        <el-menu
          :default-active="$route.path"
          class="sidebar-menu"
          router
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/courses">
            <i class="el-icon-reading"></i>
            <span>课程列表</span>
          </el-menu-item>
          <el-menu-item index="/my-courses">
            <i class="el-icon-collection"></i>
            <span>我的课程</span>
          </el-menu-item>
          <el-menu-item index="/messages">
            <i class="el-icon-message"></i>
            <span>消息中心</span>
          </el-menu-item>
          <el-menu-item index="/profile">
            <i class="el-icon-setting"></i>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { getUnreadCount } from '@/api/message'

export default {
  name: 'Layout',
  computed: {
    ...mapState('user', ['userInfo'])
  },
  data() {
    return {
      unreadCount: 0
    }
  },
  mounted() {
    this.loadUnreadCount()
  },
  methods: {
    ...mapActions('user', ['logout']),
    async loadUnreadCount() {
      try {
        const res = await getUnreadCount()
        this.unreadCount = res.data
      } catch (error) {
        console.error(error)
      }
    },
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/profile')
      } else if (command === 'logout') {
        this.logout()
        this.$message.success('退出成功')
        this.$router.push('/login')
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.header-left .system-title {
  color: #333;
  font-size: 18px;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.message-badge {
  margin-right: 10px;
}

.user-dropdown {
  cursor: pointer;
  color: #666;
  display: flex;
  align-items: center;
  gap: 5px;
}

.user-dropdown:hover {
  color: #409eff;
}

.layout-aside {
  background: #304156;
}

.sidebar-menu {
  border: none;
}

.sidebar-menu .el-menu-item {
  color: #bfcbd9;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-menu-item.is-active {
  background: #263445;
  color: #409eff;
}

.layout-main {
  background: #f5f7fa;
  padding: 0;
}
</style>
