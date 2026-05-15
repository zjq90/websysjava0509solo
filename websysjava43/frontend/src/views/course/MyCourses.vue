<template>
  <div class="page-container">
    <div class="page-header">
      <h2>我的课程</h2>
      <el-button type="primary" @click="showCreateDialog">创建课程</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="course in myCourses" :key="course.id">
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
              <el-button type="primary" size="small" @click="$router.push(`/courses/${course.id}`)">进入课程</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="myCourses.length === 0" description="暂无课程，去课程列表加入吧"></el-empty>

    <el-dialog title="创建课程" :visible.sync="createDialogVisible" width="500px">
      <el-form :model="courseForm" label-width="80px">
        <el-form-item label="课程名称">
          <el-input v-model="courseForm.name"></el-input>
        </el-form-item>
        <el-form-item label="课程描述">
          <el-input v-model="courseForm.description" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="人数上限">
          <el-input-number v-model="courseForm.capacity" :min="1" :max="1000"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createCourse">创建</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMyCourses, createCourse } from '@/api/course'

export default {
  name: 'MyCourses',
  data() {
    return {
      myCourses: [],
      createDialogVisible: false,
      courseForm: {
        name: '',
        description: '',
        capacity: 100
      }
    }
  },
  mounted() {
    this.loadMyCourses()
  },
  methods: {
    async loadMyCourses() {
      try {
        const res = await getMyCourses()
        this.myCourses = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    showCreateDialog() {
      this.createDialogVisible = true
    },
    async createCourse() {
      if (!this.courseForm.name.trim()) {
        this.$message.warning('请输入课程名称')
        return
      }
      try {
        await createCourse(this.courseForm)
        this.$message.success('课程创建成功')
        this.createDialogVisible = false
        this.courseForm = { name: '', description: '', capacity: 100 }
        this.loadMyCourses()
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
