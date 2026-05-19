<template>
  <div class="circle-page">
    <div class="page-header">
      <el-button @click="$router.push('/home')" type="text">← 返回</el-button>
      <h2>宠物圈</h2>
      <el-button type="primary" @click="showPostModal = true" size="small">发布</el-button>
    </div>

    <div class="filter-tabs">
      <div 
        class="tab" 
        :class="{ active: currentTab === tab.value }" 
        v-for="tab in tabs" 
        :key="tab.value"
        @click="switchTab(tab.value)"
      >
        {{ tab.label }}
      </div>
    </div>

    <div class="posts-list">
      <div class="post-card" v-for="post in posts" :key="post.id" @click="viewPost(post)">
        <div class="post-header">
          <div class="user-avatar">{{ post.userAvatar }}</div>
          <div class="user-info">
            <div class="username">{{ post.username }}</div>
            <div class="post-time">{{ post.createTime }}</div>
          </div>
          <el-tag 
            v-if="post.status === 'pending'" 
            type="warning" 
            size="small"
          >
            审核中
          </el-tag>
          <el-tag 
            v-else-if="post.status === 'rejected'" 
            type="danger" 
            size="small"
          >
            未通过
          </el-tag>
        </div>
        
        <div class="post-content">
          <h3>{{ post.title }}</h3>
          <p>{{ post.content }}</p>
        </div>
        
        <div class="post-images" v-if="post.images && post.images.length">
          <div class="image-grid" :class="'count-' + Math.min(post.images.length, 4)">
            <div class="image-item" v-for="(img, idx) in post.images.slice(0, 4)" :key="idx">
              <span class="img-placeholder">📷</span>
            </div>
          </div>
        </div>
        
        <div class="post-actions">
          <div class="action-item" @click.stop="likePost(post)">
            <span>{{ post.liked ? '❤️' : '🤍' }}</span>
            <span>{{ post.likes || 0 }}</span>
          </div>
          <div class="action-item">
            <span>💬</span>
            <span>{{ post.comments || 0 }}</span>
          </div>
          <div class="action-item">
            <span>👁️</span>
            <span>{{ post.views || 0 }}</span>
          </div>
        </div>
        
        <div class="audit-actions" v-if="currentTab === 'pending' && isAdmin">
          <el-button type="success" size="small" @click.stop="auditPost(post.id, 'approved')">
            通过
          </el-button>
          <el-button type="danger" size="small" @click.stop="auditPost(post.id, 'rejected')">
            拒绝
          </el-button>
        </div>
      </div>
    </div>

    <el-dialog v-model="showPostModal" title="发布动态" width="500px">
      <el-form :model="newPost" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="newPost.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input 
            v-model="newPost.content" 
            type="textarea" 
            :rows="4" 
            placeholder="分享你的养宠经验..." 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPostModal = false">取消</el-button>
        <el-button type="primary" @click="createPost" :loading="posting">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { circleApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const showPostModal = ref(false)
const posting = ref(false)
const currentTab = ref('all')
const isAdmin = ref(true)

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待审核', value: 'pending' },
  { label: '经验', value: 'experience' },
  { label: '问答', value: 'qa' }
]

const posts = ref([])

const newPost = ref({
  title: '',
  content: ''
})

const loadPosts = async () => {
  try {
    let res
    if (currentTab.value === 'pending') {
      res = await circleApi.getPendingPosts({})
    } else {
      res = await circleApi.getPosts({ type: currentTab.value })
    }
    if (res.code === 200) {
      posts.value = res.data
    }
  } catch (error) {
    posts.value = [
      {
        id: 1,
        username: '金毛旺财妈',
        userAvatar: '🐕',
        title: '金毛幼犬训练经验分享',
        content: '我家旺财现在3个月了，分享一下训练经验。首先是定点排便，用零食引导效果最好...',
        images: ['img1', 'img2'],
        likes: 128,
        comments: 32,
        views: 1024,
        liked: false,
        status: 'approved',
        createTime: '2小时前'
      },
      {
        id: 2,
        username: '英短咪咪爸',
        userAvatar: '🐱',
        title: '猫咪掉毛严重怎么办？',
        content: '最近咪咪掉毛特别严重，每天梳毛都能梳出一大堆，有没有什么好办法？',
        images: [],
        likes: 56,
        comments: 18,
        views: 512,
        liked: true,
        status: 'approved',
        createTime: '5小时前'
      },
      {
        id: 3,
        username: '新手铲屎官',
        userAvatar: '🐾',
        title: '第一次养狗狗需要准备什么？',
        content: '下周要领养一只小土狗，想问一下大家都需要准备什么东西？',
        images: [],
        likes: 89,
        comments: 45,
        views: 768,
        liked: false,
        status: 'pending',
        createTime: '1天前'
      }
    ]
  }
}

const switchTab = (tab) => {
  currentTab.value = tab
  loadPosts()
}

const createPost = async () => {
  if (!newPost.value.title || !newPost.value.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  posting.value = true
  try {
    const res = await circleApi.createPost({
      ...newPost.value,
      userId: 1
    })
    if (res.code === 200) {
      ElMessage.success('发布成功，等待审核')
      showPostModal.value = false
      newPost.value = { title: '', content: '' }
      loadPosts()
    }
  } catch (error) {
    posts.value.unshift({
      id: Date.now(),
      username: '我',
      userAvatar: '😊',
      ...newPost.value,
      images: [],
      likes: 0,
      comments: 0,
      views: 0,
      liked: false,
      status: 'pending',
      createTime: '刚刚'
    })
    ElMessage.success('发布成功，等待审核')
    showPostModal.value = false
    newPost.value = { title: '', content: '' }
  } finally {
    posting.value = false
  }
}

const likePost = async (post) => {
  try {
    await circleApi.likePost(post.id)
    post.liked = !post.liked
    post.likes += post.liked ? 1 : -1
  } catch (error) {
    post.liked = !post.liked
    post.likes += post.liked ? 1 : -1
  }
}

const auditPost = async (postId, status) => {
  try {
    await circleApi.auditPost(postId, { status })
    ElMessage.success(status === 'approved' ? '已通过' : '已拒绝')
    loadPosts()
  } catch (error) {
    const post = posts.value.find(p => p.id === postId)
    if (post) {
      post.status = status
    }
    ElMessage.success(status === 'approved' ? '已通过' : '已拒绝')
  }
}

const viewPost = (post) => {
  circleApi.incrementView(post.id)
  router.push(`/post/${post.id}`)
}

onMounted(() => {
  loadPosts()
})
</script>

<style scoped>
.circle-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 20px 80px;
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

.filter-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  overflow-x: auto;
}

.tab {
  padding: 8px 20px;
  background: white;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.3s;
}

.tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.post-card:hover {
  transform: translateY(-2px);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 12px;
}

.user-info {
  flex: 1;
}

.username {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 3px;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-content h3 {
  margin: 0 0 10px;
  font-size: 16px;
  color: #333;
}

.post-content p {
  margin: 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.post-images {
  margin: 15px 0;
}

.image-grid {
  display: grid;
  gap: 8px;
}

.image-grid.count-1 {
  grid-template-columns: 1fr;
}

.image-grid.count-2 {
  grid-template-columns: repeat(2, 1fr);
}

.image-grid.count-3 {
  grid-template-columns: repeat(3, 1fr);
}

.image-grid.count-4 {
  grid-template-columns: repeat(2, 1fr);
}

.image-item {
  aspect-ratio: 1;
  background: #f0f0f0;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.post-actions {
  display: flex;
  gap: 30px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  color: #666;
}

.audit-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}
</style>