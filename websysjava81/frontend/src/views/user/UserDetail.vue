<template>
  <div class="page-container">
    <div class="page-header">
      <el-button @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h3 style="margin-top: 20px">用户详情 - {{ userDetail?.user?.nickname }}</h3>
    </div>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="detail-card">
          <div class="card-header">
            <span class="card-title">基本信息</span>
            <div>
              <el-tag :class="userDetail?.user?.userStatus === 1 ? 'status-tag-banned' : 'status-tag-normal'">
                {{ userDetail?.user?.userStatus === 1 ? '已封禁' : '正常' }}
              </el-tag>
              <el-tag :class="userDetail?.user?.memberStatus === 1 ? 'member-tag-vip' : 'member-tag-free'" style="margin-left: 10px">
                {{ userDetail?.user?.memberStatus === 1 ? '付费会员' : '免费用户' }}
              </el-tag>
            </div>
          </div>
          <div class="user-info" v-if="userDetail?.user">
            <el-avatar :size="80" :src="userDetail.user.avatar">
              {{ userDetail.user.nickname?.charAt(0) }}
            </el-avatar>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">用户ID：</span>
                <span class="value">{{ userDetail.user.userId }}</span>
              </div>
              <div class="info-item">
                <span class="label">昵称：</span>
                <span class="value">{{ userDetail.user.nickname }}</span>
              </div>
              <div class="info-item">
                <span class="label">手机号：</span>
                <span class="value">{{ userDetail.user.phone }}</span>
              </div>
              <div class="info-item">
                <span class="label">邮箱：</span>
                <span class="value">{{ userDetail.user.email || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">注册时间：</span>
                <span class="value">{{ formatDate(userDetail.user.registerTime) }}</span>
              </div>
              <div class="info-item">
                <span class="label">最后登录：</span>
                <span class="value">{{ formatDate(userDetail.user.lastLoginTime) }}</span>
              </div>
              <div class="info-item">
                <span class="label">游戏总时长：</span>
                <span class="value">{{ userDetail.user.totalGameTime }} 分钟</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="游戏记录" name="game">
        <el-table :key="'game-' + activeTab" :data="userDetail?.gameRecords || []" border stripe height="300" style="width: 100%">
          <el-table-column prop="gameName" label="游戏名称" width="200" />
          <el-table-column prop="startTime" label="开始时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.startTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.endTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="duration" label="时长(分钟)" width="120" />
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="收藏列表" name="favorite">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in userDetail?.favorites || []" :key="item.id">
            <el-card :body-style="{ padding: '10px' }">
              <img :src="item.gameCover" style="width: 100%; height: 120px; object-fit: cover" />
              <div style="margin-top: 10px; text-align: center; font-weight: bold">{{ item.gameName }}</div>
              <div style="color: #909399; font-size: 12px; text-align: center">
                {{ formatDate(item.createTime) }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="评论记录" name="comment">
        <el-table :key="'comment-' + activeTab" :data="userDetail?.comments || []" border stripe height="300" style="width: 100%">
          <el-table-column prop="content" label="评论内容" />
          <el-table-column prop="auditStatus" label="审核状态" width="120">
            <template #default="{ row }">
              <el-tag :class="getAuditStatusClass(row.auditStatus)">
                {{ getAuditStatusText(row.auditStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="发布时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="登录设备" name="device">
        <el-table :key="'device-' + activeTab" :data="userDetail?.loginDevices || []" border stripe height="300" style="width: 100%">
          <el-table-column prop="deviceType" label="设备类型" width="120" />
          <el-table-column prop="deviceModel" label="设备型号" width="200" />
          <el-table-column prop="osVersion" label="系统版本" width="200" />
          <el-table-column prop="ipAddress" label="IP地址" width="150" />
          <el-table-column prop="loginTime" label="登录时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.loginTime) }}
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="行为轨迹" name="behavior">
        <el-timeline style="padding: 20px">
          <el-timeline-item
            v-for="(item, index) in userDetail?.behaviors || []"
            :key="item.id"
            :timestamp="formatDate(item.createTime)"
            :type="getBehaviorType(index)"
          >
            <div style="font-weight: bold">{{ getBehaviorText(item.behaviorType) }}</div>
            <div style="color: #606266; font-size: 12px">{{ item.behaviorDetail }}</div>
            <div style="color: #909399; font-size: 12px">
              IP: {{ item.ipAddress }} | 设备: {{ item.deviceInfo }}
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-tab-pane>

      <el-tab-pane label="封禁记录" name="ban">
        <el-table :key="'ban-' + activeTab" :data="userDetail?.banRecords || []" border stripe height="300" style="width: 100%">
          <el-table-column prop="banType" label="封禁类型" width="120">
            <template #default="{ row }">
              {{ row.banType === 1 ? '永久封禁' : '临时封禁' }}
            </template>
          </el-table-column>
          <el-table-column prop="banReason" label="封禁原因" />
          <el-table-column prop="unbanTime" label="解封时间" width="180">
            <template #default="{ row }">
              {{ row.banType === 1 ? '永久' : formatDate(row.unbanTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="operatorName" label="操作人" width="120" />
          <el-table-column prop="createTime" label="操作时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :class="row.status === 1 ? 'status-tag-banned' : 'status-tag-normal'">
                {{ row.status === 1 ? '生效中' : '已撤销' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" v-if="userDetail?.banRecords?.some(b => b.status === 1)">
            <template #default="{ row }">
              <el-button
                v-if="row.status === 1"
                type="primary"
                link
                @click="handleUnban(row.id)"
              >
                撤销
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserDetail, unbanUser } from '../../api/user'

const route = useRoute()
const router = useRouter()
const userDetail = ref(null)
const activeTab = ref('game')

const formatDate = (date) => {
  if (!date) return '-'
  return date
}

const getAuditStatusClass = (status) => {
  const classes = ['status-tag-pending', 'status-tag-passed', 'status-tag-deleted', 'status-tag-pending']
  return classes[status] || ''
}

const getAuditStatusText = (status) => {
  const texts = ['待审核', '已通过', '已删除', '已屏蔽']
  return texts[status] || '未知'
}

const getBehaviorType = (index) => {
  const types = ['primary', 'success', 'warning', 'info', 'danger']
  return types[index % types.length]
}

const getBehaviorText = (type) => {
  const map = {
    'REGISTER': '用户注册',
    'BROWSE_HOME': '浏览首页',
    'SEARCH_GAME': '搜索游戏',
    'START_GAME': '开始游戏',
    'LOGIN': '用户登录'
  }
  return map[type] || type
}

const fetchData = async () => {
  try {
    const res = await getUserDetail(route.params.userId)
    if (res.data.code === 200) {
      userDetail.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

const goBack = () => {
  router.back()
}

const handleUnban = async (id) => {
  try {
    await ElMessageBox.confirm('确定要撤销该封禁记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await unbanUser(id)
    if (res.data.code === 200) {
      ElMessage.success('撤销成功')
      fetchData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('撤销失败')
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  flex: 1;
}

.info-item {
  display: flex;
}

.info-item .label {
  color: #909399;
  min-width: 80px;
}

.info-item .value {
  color: #303133;
}
</style>
