<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">消息通知</span>
      <div>
        <el-button type="primary" @click="markAllRead" v-if="unreadCount > 0">
          <el-icon><Check /></el-icon>
          全部已读
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">全部消息</div>
          <div class="stat-value">{{ notifications.length }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">未读消息</div>
          <div class="stat-value">{{ unreadCount }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-label">还款提醒</div>
          <div class="stat-value">{{ typeCounts.REPAYMENT_REMINDER || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-label">系统通知</div>
          <div class="stat-value">{{ typeCounts.SYSTEM || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <div class="filter-bar mb-16">
        <el-radio-group v-model="filterType" @change="filterNotifications">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button value="unread">未读</el-radio-button>
          <el-radio-button value="REPAYMENT_REMINDER">还款提醒</el-radio-button>
          <el-radio-button value="BUDGET_ALERT">预算预警</el-radio-button>
          <el-radio-button value="SYSTEM">系统通知</el-radio-button>
        </el-radio-group>
      </div>

      <div class="notification-list">
        <div 
          v-for="notification in filteredNotifications" 
          :key="notification.id" 
          class="notification-item"
          :class="{ unread: !notification.isRead }"
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-icon" :class="getIconClass(notification.notificationType)">
            <el-icon :size="20">
              <component :is="getIcon(notification.notificationType)" />
            </el-icon>
          </div>
          <div class="notification-content">
            <div class="flex-between">
              <span class="notification-title">
                {{ notification.title }}
                <el-badge v-if="!notification.isRead" is-dot class="unread-badge" />
              </span>
              <span class="notification-time">{{ notification.createdAt }}</span>
            </div>
            <div class="notification-message">{{ notification.message }}</div>
            <div class="notification-actions mt-8" v-if="notification.notificationType === 'REPAYMENT_REMINDER'">
              <el-button type="primary" size="small" link @click.stop="goToDebt">立即还款</el-button>
            </div>
          </div>
        </div>

        <el-empty v-if="filteredNotifications.length === 0" description="暂无通知消息" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { notificationApi, API } from '@/api'

const router = useRouter()
const notifications = ref([])
const filterType = ref('all')
const unreadCount = ref(0)

const typeCounts = computed(() => {
  const counts = {}
  notifications.value.forEach(n => {
    counts[n.notificationType] = (counts[n.notificationType] || 0) + 1
  })
  return counts
})

const filteredNotifications = computed(() => {
  if (filterType.value === 'all') {
    return notifications.value
  }
  if (filterType.value === 'unread') {
    return notifications.value.filter(n => !n.isRead)
  }
  return notifications.value.filter(n => n.notificationType === filterType.value)
})

const getIcon = (type) => {
  const icons = {
    REPAYMENT_REMINDER: 'CreditCard',
    BUDGET_ALERT: 'Warning',
    SYSTEM: 'Bell',
    FAMILY_INVITE: 'UserPlus',
    TRANSACTION: 'Money'
  }
  return icons[type] || 'Bell'
}

const getIconClass = (type) => {
  const classes = {
    REPAYMENT_REMINDER: 'danger',
    BUDGET_ALERT: 'warning',
    SYSTEM: 'info',
    FAMILY_INVITE: 'success',
    TRANSACTION: 'primary'
  }
  return classes[type] || 'info'
}

const loadNotifications = async () => {
  try {
    notifications.value = await notificationApi.list(API.currentUserId)
    loadUnreadCount()
  } catch (e) {
    console.error(e)
  }
}

const loadUnreadCount = async () => {
  try {
    unreadCount.value = await notificationApi.getUnreadCount(API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const handleNotificationClick = async (notification) => {
  if (!notification.isRead) {
    try {
      await notificationApi.markAsRead(notification.id)
      notification.isRead = true
      unreadCount.value--
    } catch (e) {
      console.error(e)
    }
  }
}

const markAllRead = async () => {
  try {
    await notificationApi.markAllAsRead(API.currentUserId)
    notifications.value.forEach(n => n.isRead = true)
    unreadCount.value = 0
    ElMessage.success('已全部标记为已读')
  } catch (e) {
    console.error(e)
  }
}

const goToDebt = () => {
  router.push('/debts')
}

const filterNotifications = () => {
}

onMounted(() => {
  loadNotifications()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.mb-16 { margin-bottom: 16px; }
.mb-20 { margin-bottom: 20px; }
.mt-8 { margin-top: 8px; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }

.filter-bar {
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.notification-list {
  max-height: 600px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  transition: background-color 0.2s;
}

.notification-item:hover {
  background-color: #f5f7fa;
}

.notification-item.unread {
  background-color: #f0f9ff;
}

.notification-item.unread:hover {
  background-color: #e6f2ff;
}

.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #fff;
}

.notification-icon.danger {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.notification-icon.warning {
  background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
}

.notification-icon.info {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.notification-icon.success {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.notification-icon.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.notification-time {
  font-size: 12px;
  color: #909399;
  flex-shrink: 0;
}

.notification-message {
  font-size: 13px;
  color: #606266;
  margin-top: 4px;
  line-height: 1.5;
}

.unread-badge {
  margin-left: 4px;
}
</style>
