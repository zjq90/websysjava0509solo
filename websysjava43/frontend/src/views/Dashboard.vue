<template>
  <div class="page-container">
    <div class="page-header">
      <h2>欢迎回来，{{ userInfo.realName || userInfo.username }}</h2>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon icon-course">
              <i class="el-icon-reading"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ courseCount }}</div>
              <div class="stat-label">我的课程</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon icon-message">
              <i class="el-icon-message"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ unreadCount }}</div>
              <div class="stat-label">未读消息</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon icon-material">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ materialCount }}</div>
              <div class="stat-label">课程资料</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon icon-video">
              <i class="el-icon-video-camera"></i>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ videoCount }}</div>
              <div class="stat-label">课程视频</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="card-wrapper">
          <div slot="header" class="card-header">
            <span>我的课程</span>
            <el-button type="text" @click="$router.push('/my-courses')">查看全部</el-button>
          </div>
          <el-table :data="myCourses.slice(0, 5)" style="width: 100%">
            <el-table-column prop="name" label="课程名称"></el-table-column>
            <el-table-column prop="teacherName" label="授课教师"></el-table-column>
            <el-table-column prop="studentCount" label="学生人数">
              <template slot-scope="scope">
                {{ scope.row.studentCount || 0 }}人
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="$router.push(`/courses/${scope.row.id}`)">进入课程</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="myCourses.length === 0" description="暂无课程"></el-empty>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="card-wrapper">
          <div slot="header">
            <span>最新消息</span>
          </div>
          <div class="message-list">
            <div v-for="msg in messages.slice(0, 5)" :key="msg.id" class="message-item">
              <div class="message-title">{{ msg.title }}</div>
              <div class="message-time">{{ formatTime(msg.createTime) }}</div>
            </div>
            <el-empty v-if="messages.length === 0" description="暂无消息"></el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { getMyCourses } from '@/api/course'
import { getMyMessages } from '@/api/message'
import dayjs from 'dayjs'

export default {
  name: 'Dashboard',
  data() {
    return {
      myCourses: [],
      messages: []
    }
  },
  mounted() {
    this.loadData()
  },
  computed: {
    ...mapState('user', ['userInfo']),
    courseCount() {
      return this.myCourses.length
    },
    unreadCount() {
      return this.messages.filter(m => !m.isRead).length
    },
    materialCount() {
      return 0
    },
    videoCount() {
      return 0
    }
  },
  methods: {
    async loadData() {
      try {
        const [coursesRes, messagesRes] = await Promise.all([
          getMyCourses(),
          getMyMessages()
        ])
        this.myCourses = coursesRes.data || []
        this.messages = messagesRes.data || []
      } catch (error) {
        console.error(error)
      }
    },
    formatTime(time) {
      return dayjs(time).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>

<style scoped>
.stat-card {
  border: none;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
}

.icon-course {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.icon-message {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.icon-material {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.icon-video {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-list {
  min-height: 300px;
}

.message-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.message-item:last-child {
  border-bottom: none;
}

.message-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-time {
  font-size: 12px;
  color: #999;
}
</style>
