<template>
  <div class="post-detail-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>帖子详情</h2>
    </div>

    <div class="post-card" v-if="post">
      <div class="post-header">
        <div class="user-avatar">{{ post.userAvatar || '😊' }}</div>
        <div class="user-info">
          <div class="username">{{ post.username || '爱宠用户' }}</div>
          <div class="post-time">{{ post.createTime || '刚刚' }}</div>
        </div>
      </div>
      
      <div class="post-content">
        <h2>{{ post.title }}</h2>
        <p>{{ post.content }}</p>
      </div>
      
      <div class="post-actions">
        <div class="action-item" @click="likePost">
          <span>{{ post.liked ? '❤️' : '🤍' }}</span>
          <span>{{ post.likes || 0 }}</span>
        </div>
        <div class="action-item">
          <span>👁️</span>
          <span>{{ post.views || 0 }}</span>
        </div>
      </div>
    </div>

    <div class="comments-section">
      <h3>评论 ({{ comments.length }})</h3>
      <div class="comment-list">
        <div class="comment-item" v-for="comment in comments" :key="comment.id">
          <div class="comment-avatar">{{ comment.avatar || '😊' }}</div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-username">{{ comment.username }}</span>
              <span class="comment-time">{{ comment.time }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
          </div>
        </div>
      </div>

      <div class="comment-input">
        <el-input
          v-model="newComment"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
        />
        <el-button type="primary" @click="submitComment" :loading="submitting">
          发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { circleApi } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const submitting = ref(false)

const post = ref(null)
const comments = ref([])
const newComment = ref('')

const loadPostDetail = async () => {
  try {
    const res = await circleApi.getPostDetail(route.params.id)
    if (res.code === 200) {
      post.value = res.data
    }
  } catch (error) {
    post.value = {
      id: route.params.id,
      username: '金毛旺财妈',
      userAvatar: '🐕',
      title: '金毛幼犬训练经验分享',
      content: '我家旺财现在3个月了，分享一下训练经验。首先是定点排便，用零食引导效果最好。每次它在正确的地方上厕所，就马上表扬它，给它零食奖励。这样它就会记住这个地方是用来上厕所的。然后是坐下训练，用零食在它头上慢慢往下压，说坐下，它坐下就给奖励。还有不能随便咬东西，给它买咬胶，它咬别的东西就说不行，然后给它咬胶。训练要有耐心，金毛很聪明的，多教几次就会了。大家有什么训练问题可以问我~',
      likes: 128,
      views: 1024,
      liked: false,
      createTime: '2小时前'
    }
  }
}

const loadComments = async () => {
  try {
    // 实际项目中调用API
    comments.value = [
      {
        id: 1,
        username: '英短咪咪爸',
        avatar: '🐱',
        time: '1小时前',
        content: '感谢分享！我家咪咪现在也在训练，学到了很多！'
      },
      {
        id: 2,
        username: '新手铲屎官',
        avatar: '😊',
        time: '30分钟前',
        content: '请问怎么教它不咬人啊？'
      }
    ]
  } catch (error) {
    // 使用默认数据
  }
}

const likePost = async () => {
  try {
    await circleApi.likePost(post.value.id)
    post.value.liked = !post.value.liked
    post.value.likes += post.value.liked ? 1 : -1
  } catch (error) {
    post.value.liked = !post.value.liked
    post.value.likes += post.value.liked ? 1 : -1
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  submitting.value = true
  try {
    // 实际项目中调用API
    setTimeout(() => {
      comments.value.unshift({
        id: Date.now(),
        username: '我',
        avatar: '😊',
        time: '刚刚',
        content: newComment.value
      })
      ElMessage.success('评论成功')
      newComment.value = ''
      submitting.value = false
    }, 1000)
  } catch (error) {
    comments.value.unshift({
      id: Date.now(),
      username: '我',
      avatar: '😊',
      time: '刚刚',
      content: newComment.value
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    submitting.value = false
  }
}

onMounted(() => {
  loadPostDetail()
  loadComments()
})
</script>

<style scoped>
.post-detail-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 20px;
}

.post-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-bottom: 20px;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 25px;
  margin-right: 15px;
}

.user-info {
  flex: 1;
}

.username {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.post-time {
  font-size: 13px;
  color: #999;
}

.post-content h2 {
  margin: 0 0 15px;
  font-size: 20px;
  color: #333;
  line-height: 1.5;
}

.post-content p {
  margin: 0;
  font-size: 15px;
  color: #666;
  line-height: 1.8;
}

.post-actions {
  display: flex;
  gap: 30px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
}

.action-item:hover {
  color: #667eea;
}

.comments-section {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.comments-section h3 {
  margin: 0 0 20px;
  font-size: 18px;
  color: #333;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 30px;
}

.comment-item {
  display: flex;
  gap: 15px;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-username {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.comment-input {
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.comment-input .el-textarea {
  margin-bottom: 15px;
}

.comment-input .el-button {
  width: 100%;
}
</style>