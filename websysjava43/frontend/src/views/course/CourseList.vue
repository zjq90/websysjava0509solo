<template>
  <div class="page-container">
    <div class="page-header">
      <h2>课程列表</h2>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="course in courses" :key="course.id">
        <el-card class="course-card" shadow="hover">
          <div class="course-cover">
            <div class="cover-placeholder">
              <i class="el-icon-reading"></i>
            </div>
          </div>
          <div class="course-info">
            <h3 class="course-name">{{ course.name }}</h3>
            <p class="course-teacher">授课教师: {{ course.teacherName }}</p>
            <div class="course-stats">
              <span><i class="el-icon-user"></i> {{ course.studentCount || 0 }}人</span>
            </div>
            <div class="course-actions">
              <el-button type="primary" size="small" @click="$router.push(`/courses/${course.id}`)">查看详情</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="courses.length === 0" description="暂无课程"></el-empty>
  </div>
</template>

<script>
import { getCourseList } from '@/api/course'

export default {
  name: 'CourseList',
  data() {
    return {
      courses: []
    }
  },
  mounted() {
    this.loadCourses()
  },
  methods: {
    async loadCourses() {
      try {
        const res = await getCourseList()
        this.courses = res.data || []
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.course-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
}

.course-card:hover {
  transform: translateY(-5px);
}

.course-cover {
  height: 150px;
  margin: -20px -20px 20px -20px;
  border-radius: 4px 4px 0 0;
  overflow: hidden;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-placeholder i {
  font-size: 50px;
  color: #fff;
}

.course-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.course-teacher {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.course-stats {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  font-size: 12px;
  color: #999;
}

.course-actions {
  text-align: right;
}
</style>
