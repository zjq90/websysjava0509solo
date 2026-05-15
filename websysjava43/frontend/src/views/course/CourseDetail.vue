<template>
  <div class="page-container">
    <div v-if="course" class="course-detail">
      <div class="course-header">
        <div class="course-cover">
          <div class="cover-placeholder">
            <i class="el-icon-reading"></i>
          </div>
        </div>
        <div class="course-info">
          <h1>{{ course.name }}</h1>
          <p class="teacher">授课教师: {{ course.teacherName }}</p>
          <p class="description">{{ course.description }}</p>
          <div class="course-actions">
            <el-button type="primary" @click="joinCourse" v-if="!isJoined">加入课程</el-button>
            <el-button type="danger" @click="leaveCourse" v-else>退出课程</el-button>
          </div>
        </div>
      </div>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="课程公告" name="announcements">
          <div class="tab-content">
            <div class="action-bar">
              <el-button type="primary" size="small" @click="showAnnouncementForm">发布公告</el-button>
            </div>
            <div v-for="item in announcements" :key="item.id" class="announcement-item">
              <div class="announcement-header">
                <h4>{{ item.title }}</h4>
                <span class="announcement-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div class="announcement-content">{{ item.content }}</div>
              <div class="announcement-footer">
                <span class="publisher">{{ item.publisherName }}</span>
              </div>
            </div>
            <el-empty v-if="announcements.length === 0" description="暂无公告"></el-empty>
          </div>
        </el-tab-pane>

        <el-tab-pane label="课程资料" name="materials">
          <div class="tab-content">
            <div class="action-bar">
              <el-button type="primary" size="small" @click="showMaterialForm">上传资料</el-button>
            </div>
            <el-table :data="materials" style="width: 100%">
              <el-table-column prop="title" label="资料名称"></el-table-column>
              <el-table-column prop="fileName" label="文件名"></el-table-column>
              <el-table-column prop="uploaderName" label="上传者"></el-table-column>
              <el-table-column prop="downloadCount" label="下载次数"></el-table-column>
              <el-table-column label="操作">
                <template>
                  <el-button type="text" size="small">下载</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-if="materials.length === 0" description="暂无资料"></el-empty>
          </div>
        </el-tab-pane>

        <el-tab-pane label="课程视频" name="videos">
          <div class="tab-content">
            <div class="action-bar">
              <el-button type="primary" size="small" @click="showVideoForm">上传视频</el-button>
            </div>
            <el-row :gutter="20">
              <el-col :span="8" v-for="video in videos" :key="video.id">
                <el-card class="video-card">
                  <div class="video-thumbnail">
                    <i class="el-icon-video-camera"></i>
                  </div>
                  <h4>{{ video.title }}</h4>
                  <p class="view-count">观看次数: {{ video.viewCount || 0 }}</p>
                  <el-button type="text" size="small">观看视频</el-button>
                </el-card>
              </el-col>
            </el-row>
            <el-empty v-if="videos.length === 0" description="暂无视频"></el-empty>
          </div>
        </el-tab-pane>

        <el-tab-pane label="课程留言" name="comments">
          <div class="tab-content">
            <el-card class="comment-input-box">
              <el-input
                v-model="newComment"
                type="textarea"
                :rows="3"
                placeholder="发表您的留言..."
              ></el-input>
              <div class="comment-actions">
                <el-button type="primary" size="small" @click="submitComment">发表留言</el-button>
              </div>
            </el-card>

            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-user">
                <el-avatar :size="40">
                  {{ (comment.userName || 'U').charAt(0) }}
                </el-avatar>
                <div class="comment-user-info">
                  <span class="username">{{ comment.userName }}</span>
                  <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                </div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-footer">
                <el-button type="text" size="small" icon="el-icon-goods">{{ comment.likeCount || 0 }}</el-button>
                <el-button type="text" size="small">回复</el-button>
              </div>
            </div>
            <el-empty v-if="comments.length === 0" description="暂无留言"></el-empty>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="发布公告" :visible.sync="announcementFormVisible" width="500px">
      <el-form :model="announcementForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="announcementForm.title"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="announcementForm.content" type="textarea" :rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="announcementFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAnnouncement">发布</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上传资料" :visible.sync="materialFormVisible" width="500px">
      <el-form :model="materialForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="materialForm.title"></el-input>
        </el-form-item>
        <el-form-item label="文件">
          <el-upload
            ref="upload"
            action=""
            :auto-upload="false"
            :limit="1"
          >
            <el-button slot="trigger" size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="materialFormVisible = false">取消</el-button>
        <el-button type="primary">上传</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上传视频" :visible.sync="videoFormVisible" width="500px">
      <el-form :model="videoForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="videoForm.title"></el-input>
        </el-form-item>
        <el-form-item label="视频文件">
          <el-upload
            ref="uploadVideo"
            action=""
            :auto-upload="false"
            :limit="1"
          >
            <el-button slot="trigger" size="small" type="primary">选择视频</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="videoFormVisible = false">取消</el-button>
        <el-button type="primary">上传</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCourseDetail, getCourseAnnouncements, getCourseMaterials, getCourseVideos, getCourseComments, joinCourse, leaveCourse, createAnnouncement, createComment } from '@/api/course'
import { mapState } from 'vuex'
import dayjs from 'dayjs'

export default {
  name: 'CourseDetail',
  data() {
    return {
      courseId: null,
      course: null,
      activeTab: 'announcements',
      announcements: [],
      materials: [],
      videos: [],
      comments: [],
      newComment: '',
      announcementFormVisible: false,
      announcementForm: {
        title: '',
        content: ''
      },
      materialFormVisible: false,
      materialForm: {
        title: ''
      },
      videoFormVisible: false,
      videoForm: {
        title: ''
      },
      myCourseIds: []
    }
  },
  mounted() {
    this.courseId = this.$route.params.id
    this.loadCourseDetail()
    this.loadAnnouncements()
    this.loadMaterials()
    this.loadVideos()
    this.loadComments()
  },
  computed: {
    ...mapState('user', ['userInfo']),
    isJoined() {
      return true
    }
  },
  methods: {
    async loadCourseDetail() {
      try {
        const res = await getCourseDetail(this.courseId)
        this.course = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async loadAnnouncements() {
      try {
        const res = await getCourseAnnouncements(this.courseId)
        this.announcements = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    async loadMaterials() {
      try {
        const res = await getCourseMaterials(this.courseId)
        this.materials = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    async loadVideos() {
      try {
        const res = await getCourseVideos(this.courseId)
        this.videos = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    async loadComments() {
      try {
        const res = await getCourseComments(this.courseId)
        this.comments = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    async joinCourse() {
      try {
        await joinCourse(this.courseId)
        this.$message.success('成功加入课程')
        this.isJoined = true
      } catch (error) {
        console.error(error)
      }
    },
    async leaveCourse() {
      try {
        await leaveCourse(this.courseId)
        this.$message.success('成功退出课程')
        this.isJoined = false
      } catch (error) {
        console.error(error)
      }
    },
    showAnnouncementForm() {
      this.announcementFormVisible = true
    },
    async submitAnnouncement() {
      try {
        await createAnnouncement({
          courseId: this.courseId,
          title: this.announcementForm.title,
          content: this.announcementForm.content,
          isTop: false
        })
        this.$message.success('公告发布成功')
        this.announcementFormVisible = false
        this.announcementForm = { title: '', content: '' }
        this.loadAnnouncements()
      } catch (error) {
        console.error(error)
      }
    },
    showMaterialForm() {
      this.materialFormVisible = true
    },
    showVideoForm() {
      this.videoFormVisible = true
    },
    async submitComment() {
      if (!this.newComment.trim()) {
        this.$message.warning('请输入留言内容')
        return
      }
      try {
        await createComment({
          courseId: this.courseId,
          content: this.newComment
        })
        this.$message.success('留言发布成功')
        this.newComment = ''
        this.loadComments()
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
.course-header {
  display: flex;
  gap: 30px;
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 20px;
}

.course-cover {
  width: 200px;
  height: 150px;
  border-radius: 4px;
  overflow: hidden;
  flex-shrink: 0;
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

.course-info h1 {
  margin: 0 0 15px 0;
  font-size: 24px;
  color: #333;
}

.teacher {
  color: #666;
  margin-bottom: 10px;
}

.description {
  color: #999;
  margin-bottom: 20px;
  line-height: 1.6;
}

.tab-content {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.action-bar {
  margin-bottom: 20px;
}

.announcement-item {
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 15px;
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.announcement-header h4 {
  margin: 0;
  color: #333;
}

.announcement-time {
  font-size: 12px;
  color: #999;
}

.announcement-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}

.announcement-footer {
  text-align: right;
}

.publisher {
  font-size: 12px;
  color: #999;
}

.video-card {
  margin-bottom: 20px;
}

.video-thumbnail {
  height: 120px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: -20px -20px 15px -20px;
}

.video-thumbnail i {
  font-size: 40px;
  color: #fff;
}

.video-card h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.view-count {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.comment-input-box {
  margin-bottom: 20px;
}

.comment-actions {
  text-align: right;
  margin-top: 10px;
}

.comment-item {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  margin-bottom: 15px;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.comment-user-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.username {
  font-weight: 500;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}

.comment-footer {
  display: flex;
  gap: 10px;
}
</style>
