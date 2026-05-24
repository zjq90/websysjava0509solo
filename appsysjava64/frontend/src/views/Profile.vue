<template>
  <div class="profile-container">
    <div class="header">
      <div class="user-info">
        <div class="avatar">
          <el-icon :size="40"><User /></el-icon>
        </div>
        <div class="user-detail">
          <div class="user-name">{{ userName }}</div>
          <div class="user-role">{{ userRoleText }}</div>
        </div>
      </div>
      <div class="stats-row">
        <div class="stat-item">
          <span class="stat-value">{{ todayTasks }}</span>
          <span class="stat-label">今日任务</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ completedTasks }}</span>
          <span class="stat-label">已完成</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ workHours }}</span>
          <span class="stat-label">工时(h)</span>
        </div>
      </div>
    </div>
    
    <div class="menu-section">
      <div class="menu-title">工作管理</div>
      <div class="menu-list">
        <el-menu-item @click="goToMyTasks">
          <el-icon><List /></el-icon>
          <span>我的任务</span>
        </el-menu-item>
        <el-menu-item @click="goToWorkRecord">
          <el-icon><Document /></el-icon>
          <span>工作记录</span>
        </el-menu-item>
        <el-menu-item @click="goToPerformance">
          <el-icon><Trophy /></el-icon>
          <span>业绩统计</span>
        </el-menu-item>
      </div>
    </div>
    
    <div class="menu-section">
      <div class="menu-title">车辆管理</div>
      <div class="menu-list">
        <el-menu-item @click="goToBikeList">
          <el-icon><Van /></el-icon>
          <span>车辆列表</span>
        </el-menu-item>
        <el-menu-item @click="goToFaultReport">
          <el-icon><Warning /></el-icon>
          <span>故障上报</span>
        </el-menu-item>
        <el-menu-item @click="goToSpareParts">
          <el-icon><Tools /></el-icon>
          <span>备件库存</span>
        </el-menu-item>
      </div>
    </div>
    
    <div class="menu-section">
      <div class="menu-title">系统设置</div>
      <div class="menu-list">
        <el-menu-item @click="goToSettings">
          <el-icon><Setting /></el-icon>
          <span>设置</span>
        </el-menu-item>
        <el-menu-item @click="goToHelp">
          <el-icon><QuestionFilled /></el-icon>
          <span>帮助中心</span>
        </el-menu-item>
        <el-menu-item @click="goToAbout">
          <el-icon><InfoFilled /></el-icon>
          <span>关于我们</span>
        </el-menu-item>
      </div>
    </div>
    
    <div class="logout-section">
      <el-button type="danger" size="large" @click="logout" style="width: 100%;">
        退出登录
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  User, List, Document, Trophy, Van, Warning, 
  Tools, Setting, QuestionFilled, InfoFilled 
} from '@element-plus/icons-vue'

const router = useRouter()
const store = useStore()

const userName = computed(() => store.getters.userName || '运维人员')
const userRoleText = computed(() => {
  const roles = { ADMIN: '管理员', DISPATCHER: '调度员', MAINTENANCE: '运维人员' }
  return roles[store.state.user?.role] || '运维人员'
})

const todayTasks = ref(12)
const completedTasks = ref(8)
const workHours = ref(6.5)

const goToMyTasks = () => {
  router.push('/tasks')
}

const goToWorkRecord = () => {
  ElMessage.success('工作记录页面')
}

const goToPerformance = () => {
  ElMessage.success('业绩统计页面')
}

const goToBikeList = () => {
  ElMessage.success('车辆列表页面')
}

const goToFaultReport = () => {
  ElMessage.success('故障上报页面')
}

const goToSpareParts = () => {
  ElMessage.success('备件库存页面')
}

const goToSettings = () => {
  ElMessage.success('设置页面')
}

const goToHelp = () => {
  ElMessage.success('帮助中心')
}

const goToAbout = () => {
  ElMessageBox.alert('共享单车运维端 v1.0.0\n\n提供专业的共享单车运维管理服务', '关于我们')
}

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    store.dispatch('logout')
    router.push('/login')
    ElMessage.success('已退出登录')
  }).catch(() => {})
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30px 20px;
  
  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    
    .avatar {
      width: 70px;
      height: 70px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      margin-right: 15px;
    }
    
    .user-detail {
      .user-name {
        font-size: 20px;
        font-weight: bold;
        color: #fff;
        margin-bottom: 5px;
      }
      
      .user-role {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  .stats-row {
    display: flex;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 12px;
    padding: 15px 0;
    
    .stat-item {
      flex: 1;
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 22px;
        font-weight: bold;
        color: #fff;
      }
      
      .stat-label {
        display: block;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 5px;
      }
    }
  }
}

.menu-section {
  background: #fff;
  margin: 10px;
  border-radius: 12px;
  overflow: hidden;
  
  .menu-title {
    padding: 15px 15px 10px;
    font-size: 14px;
    font-weight: bold;
    color: #333;
  }
  
  .menu-list {
    :deep(.el-menu-item) {
      border-bottom: 1px solid #f5f5f5;
      
      &:last-child {
        border-bottom: none;
      }
    }
  }
}

.logout-section {
  margin: 20px 10px;
}
</style>
