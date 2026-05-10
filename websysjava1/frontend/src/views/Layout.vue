<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background-color: #2f4056">
      <div style="height: 60px; line-height: 60px; text-align: center; color: white; font-size: 18px; font-weight: bold; border-bottom: 1px solid #1f2d3d">
        管理系统
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#2f4056"
        text-color="#c0ccda"
        active-text-color="#409EFF"
        class="el-menu-vertical-demo"
      >
        <el-menu-item v-for="item in rootMenus" :key="item.id" :index="item.path">
          <i :class="item.icon ? 'el-icon-' + item.icon : 'el-icon-s-home'"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
        <el-submenu v-for="parent in parentMenus" :key="parent.id" :index="parent.path">
          <template slot="title">
            <i :class="parent.icon ? 'el-icon-' + parent.icon : 'el-icon-menu'"></i>
            <span>{{ parent.name }}</span>
          </template>
          <el-menu-item v-for="child in getChildMenus(parent.id)" :key="child.id" :index="child.path">
            <i :class="child.icon ? 'el-icon-' + child.icon : 'el-icon-document'"></i>
            <span>{{ child.name }}</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header style="text-align: right; font-size: 12px; background-color: #fff; border-bottom: 1px solid #e6e6e6">
        <span style="margin-right: 20px">{{ currentUser.realName || '管理员' }}</span>
        <el-dropdown @command="handleCommand">
          <i class="el-icon-user" style="font-size: 20px; margin-right: 15px; cursor: pointer"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      
      <el-main style="background-color: #f0f2f5; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
    
    <el-dialog title="个人中心" :visible.sync="profileVisible" width="500px">
      <el-descriptions :column="2" border v-if="currentUser.id">
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentUser.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentUser.status === 1 ? 'success' : 'danger'" size="small">
            {{ currentUser.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentUser.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="profileVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import { getUser, getUserMenus } from '@/api/user'

export default {
  name: 'Layout',
  data() {
    return {
      currentUser: {
        id: null,
        username: '',
        realName: '管理员',
        phone: '',
        email: '',
        status: 1,
        createTime: ''
      },
      profileVisible: false,
      menuList: [],
      permissions: []
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    rootMenus() {
      return this.menuList.filter(m => m.type === 2 && !m.parentId)
        .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    },
    parentMenus() {
      return this.menuList.filter(m => m.type === 1)
        .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    }
  },
  created() {
    this.loadCurrentUser()
  },
  methods: {
    loadCurrentUser() {
      const userId = localStorage.getItem('userId') || '1'
      getUser(userId).then(res => {
        if (res.data) {
          this.currentUser = res.data
        }
      }).catch(() => {
        this.currentUser = {
          id: 1,
          username: 'admin',
          realName: '系统管理员',
          phone: '13800138000',
          email: 'admin@websys.com',
          status: 1,
          createTime: '2024-01-01 10:00:00'
        }
      })
      this.loadUserMenus(userId)
    },
    loadUserMenus(userId) {
      getUserMenus(userId).then(res => {
        if (res.data) {
          this.menuList = res.data
        }
      }).catch(() => {
        this.menuList = this.getDefaultMenus()
      })
    },
    getDefaultMenus() {
      return [
        { id: 1, name: '首页', path: '/dashboard', type: 2, icon: 's-home', sortOrder: 0, parentId: null },
        { id: 2, name: '系统管理', path: '/system', type: 1, icon: 'setting', sortOrder: 1, parentId: null },
        { id: 3, name: '用户管理', path: '/system/user', type: 2, icon: 'user', sortOrder: 2, parentId: 2 },
        { id: 4, name: '角色管理', path: '/system/role', type: 2, icon: 'team', sortOrder: 3, parentId: 2 },
        { id: 5, name: '菜单管理', path: '/system/menu', type: 2, icon: 'menu', sortOrder: 4, parentId: 2 },
        { id: 6, name: '设备管理', path: '/device', type: 1, icon: 'desktop', sortOrder: 2, parentId: null },
        { id: 7, name: '设备列表', path: '/device/list', type: 2, icon: 'appstore', sortOrder: 5, parentId: 6 },
        { id: 8, name: '远程控制', path: '/device/remote', type: 2, icon: 'control', sortOrder: 6, parentId: 6 },
        { id: 9, name: '商品管理', path: '/product', type: 1, icon: 'shopping', sortOrder: 3, parentId: null },
        { id: 10, name: '商品列表', path: '/product/list', type: 2, icon: 'shop', sortOrder: 7, parentId: 9 },
        { id: 11, name: '货道管理', path: '/product/slot', type: 2, icon: 'container', sortOrder: 8, parentId: 9 }
      ]
    },
    getChildMenus(parentId) {
      return this.menuList
        .filter(m => m.parentId === parentId)
        .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    },
    handleCommand(command) {
      if (command === 'profile') {
        this.handleProfile()
      } else if (command === 'logout') {
        this.handleLogout()
      }
    },
    handleProfile() {
      this.loadCurrentUser()
      this.profileVisible = true
    },
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('userId')
        localStorage.removeItem('token')
        this.$message.success('退出成功')
        this.$router.push('/login')
      }).catch(() => {
      })
    }
  }
}
</script>

<style>
.el-header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>
