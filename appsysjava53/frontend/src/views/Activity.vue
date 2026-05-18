<template>
  <div class="activity-container">
    <!-- 分类标签 -->
    <div class="tab-bar">
      <div
        v-for="(tab, index) in tabs"
        :key="index"
        class="tab-item"
        :class="{ active: currentTab === index }"
        @click="switchTab(index)"
      >
        {{ tab }}
      </div>
    </div>

    <!-- 活动列表 -->
    <div class="activity-list">
      <div
        class="activity-card"
        v-for="activity in activityList"
        :key="activity.id"
        @click="goToDetail(activity)"
      >
        <div class="activity-image-wrapper">
          <img :src="activity.image" class="activity-image" />
          <div class="activity-status" :class="activity.statusClass">
            {{ activity.statusText }}
          </div>
        </div>

        <div class="activity-info">
          <h3 class="activity-title">{{ activity.title }}</h3>

          <div class="activity-meta">
            <div class="meta-item">
              <el-icon :size="16"><Calendar /></el-icon>
              <span>{{ activity.date }}</span>
            </div>
            <div class="meta-item">
              <el-icon :size="16"><Location /></el-icon>
              <span>{{ activity.location }}</span>
            </div>
          </div>

          <div class="activity-footer">
            <div class="participants">
              <span class="count">{{ activity.participants }}</span>
              <span>人已报名</span>
            </div>
            <el-button
              type="primary"
              size="small"
              @click.stop="joinActivity(activity)"
              :disabled="!activity.canJoin"
            >
              {{ activity.canJoin ? '立即报名' : '已结束' }}
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElModal } from 'element-plus'
import { Calendar, Location } from '@element-plus/icons-vue'

const currentTab = ref(0)
const tabs = ['全部', '鉴宝会', '展览', '讲座', '交流会']

const activityList = ref([
  {
    id: 1,
    title: '2024春季文物鉴宝大会',
    image: 'https://picsum.photos/600/300?random=40',
    date: '2024.06.15 09:00',
    location: '北京市朝阳区国际会议中心',
    participants: 328,
    statusClass: 'ongoing',
    statusText: '报名中',
    canJoin: true,
    type: '鉴宝会'
  },
  {
    id: 2,
    title: '明清瓷器精品展',
    image: 'https://picsum.photos/600/300?random=41',
    date: '2024.07.01 - 07.31',
    location: '上海博物馆',
    participants: 1256,
    statusClass: 'upcoming',
    statusText: '即将开始',
    canJoin: true,
    type: '展览'
  },
  {
    id: 3,
    title: '青铜器鉴定讲座',
    image: 'https://picsum.photos/600/300?random=42',
    date: '2024.05.20 14:00',
    location: '故宫博物院报告厅',
    participants: 186,
    statusClass: 'ended',
    statusText: '已结束',
    canJoin: false,
    type: '讲座'
  },
  {
    id: 4,
    title: '全国收藏家交流会',
    image: 'https://picsum.photos/600/300?random=43',
    date: '2024.08.10 09:00',
    location: '杭州西湖博览中心',
    participants: 568,
    statusClass: 'upcoming',
    statusText: '报名中',
    canJoin: true,
    type: '交流会'
  }
])

const switchTab = (index) => {
  currentTab.value = index
}

const goToDetail = (activity) => {
  ElMessage.info('活动详情开发中')
}

const joinActivity = (activity) => {
  if (!activity.canJoin) {
    ElMessage.warning('活动已结束')
    return
  }
  ElModal.confirm({
    title: '确认报名',
    content: `确定要报名参加"${activity.title}"吗？`,
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    onConfirm: () => {
      activity.participants++
      ElMessage.success('报名成功')
    }
  })
}
</script>

<style scoped>
.activity-container {
  padding: 20px;
}

.tab-bar {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 8px;
  margin-bottom: 20px;
}

.tab-item {
  flex-shrink: 0;
  padding: 10px 24px;
  background: #fff;
  border-radius: 8px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.tab-item.active {
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  color: #fff;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.activity-image-wrapper {
  position: relative;
  height: 180px;
}

.activity-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 6px 16px;
  border-radius: 16px;
  font-size: 12px;
  color: #fff;
}

.activity-status.ongoing {
  background: #52c41a;
}

.activity-status.upcoming {
  background: #1890ff;
}

.activity-status.ended {
  background: #999;
}

.activity-info {
  padding: 20px;
}

.activity-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 16px;
  line-height: 1.4;
}

.activity-meta {
  margin-bottom: 20px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.activity-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.participants {
  font-size: 14px;
  color: #666;
}

.participants .count {
  font-weight: bold;
  color: #8B4513;
  margin-right: 4px;
}

.elder-mode .tab-item {
  font-size: 18px;
  padding: 14px 28px;
}

.elder-mode .activity-title {
  font-size: 20px;
}

.elder-mode .meta-item {
  font-size: 16px;
}
</style>
