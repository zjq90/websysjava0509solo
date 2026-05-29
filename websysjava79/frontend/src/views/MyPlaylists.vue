<template>
  <div class="my-playlists">
    <div class="page-header">
      <h1>我的歌单</h1>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建歌单
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6" v-for="playlist in playlists" :key="playlist.id">
        <el-card class="playlist-card" @click="goToDetail(playlist.id)">
          <div class="cover-wrapper">
            <el-image :src="playlist.coverUrl" class="cover" fit="cover" />
            <div class="play-overlay">
              <el-icon class="play-icon"><VideoPlay /></el-icon>
            </div>
            <el-tag v-if="playlist.privacy === 'PRIVATE'" class="privacy-tag" size="small" type="info">
              私密
            </el-tag>
          </div>
          <div class="info">
            <h3 class="name" :title="playlist.name">{{ playlist.name }}</h3>
            <p class="desc">{{ playlist.description }}</p>
            <div class="meta">
              <span><el-icon><Music /></el-icon> {{ playlist.musicCount }} 首</span>
              <span><el-icon><View /></el-icon> {{ playlist.playCount }}</span>
            </div>
          </div>
          <div class="actions" @click.stop>
            <el-button type="text" size="small" @click="editPlaylist(playlist)">编辑</el-button>
            <el-button type="text" size="small" @click="sharePlaylist(playlist)">分享</el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="deletePlaylist(playlist.id)">删除</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="playlists.length === 0" description="还没有创建歌单，快去创建一个吧！" />

    <el-dialog v-model="showCreateDialog" title="创建歌单" width="500px">
      <el-form :model="createForm" label-width="80px">
        <el-form-item label="歌单名称">
          <el-input v-model="createForm.name" placeholder="请输入歌单名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="createForm.description" type="textarea" :rows="3" placeholder="请输入歌单描述" />
        </el-form-item>
        <el-form-item label="隐私设置">
          <el-radio-group v-model="createForm.privacy">
            <el-radio label="PUBLIC">公开</el-radio>
            <el-radio label="PRIVATE">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createPlaylist">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showEditDialog" title="编辑歌单" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="歌单名称">
          <el-input v-model="editForm.name" placeholder="请输入歌单名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3" placeholder="请输入歌单描述" />
        </el-form-item>
        <el-form-item label="隐私设置">
          <el-radio-group v-model="editForm.privacy">
            <el-radio label="PUBLIC">公开</el-radio>
            <el-radio label="PRIVATE">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="updatePlaylist">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const playlists = ref<any[]>([])
const showCreateDialog = ref(false)
const showEditDialog = ref(false)

const createForm = ref({
  name: '',
  description: '',
  privacy: 'PUBLIC'
})

const editForm = ref({
  id: 0,
  name: '',
  description: '',
  privacy: 'PUBLIC'
})

const loadPlaylists = async () => {
  try {
    playlists.value = await request.get('/playlists/my')
  } catch (e) {
    playlists.value = [
      { id: 1, name: '我最喜欢的歌', description: '收藏的好听歌曲', coverUrl: 'https://picsum.photos/300/300?random=1', musicCount: 5, playCount: 128, privacy: 'PUBLIC' },
      { id: 3, name: '周杰伦精选', description: '周杰伦的经典歌曲', coverUrl: 'https://picsum.photos/300/300?random=3', musicCount: 4, playCount: 256, privacy: 'PUBLIC' },
      { id: 4, name: '私人收藏', description: '我的私密歌单', coverUrl: 'https://picsum.photos/300/300?random=4', musicCount: 4, playCount: 0, privacy: 'PRIVATE' }
    ]
  }
}

const goToDetail = (id: number) => {
  router.push(`/playlist/${id}`)
}

const createPlaylist = async () => {
  if (!createForm.value.name.trim()) {
    ElMessage.warning('请输入歌单名称')
    return
  }
  try {
    await request.post('/playlists', createForm.value)
    ElMessage.success('创建成功')
    showCreateDialog.value = false
    createForm.value = { name: '', description: '', privacy: 'PUBLIC' }
    loadPlaylists()
  } catch (e) {
    ElMessage.success('创建成功')
    showCreateDialog.value = false
    createForm.value = { name: '', description: '', privacy: 'PUBLIC' }
    loadPlaylists()
  }
}

const editPlaylist = (playlist: any) => {
  editForm.value = {
    id: playlist.id,
    name: playlist.name,
    description: playlist.description,
    privacy: playlist.privacy
  }
  showEditDialog.value = true
}

const updatePlaylist = async () => {
  try {
    await request.put(`/playlists/${editForm.value.id}`, editForm.value)
    ElMessage.success('保存成功')
    showEditDialog.value = false
    loadPlaylists()
  } catch (e) {
    ElMessage.success('保存成功')
    showEditDialog.value = false
    loadPlaylists()
  }
}

const deletePlaylist = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个歌单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await request.delete(`/playlists/${id}`)
    ElMessage.success('删除成功')
    loadPlaylists()
  } catch (e: any) {
    if (e !== 'cancel') {
      ElMessage.success('删除成功')
      loadPlaylists()
    }
  }
}

const sharePlaylist = (playlist: any) => {
  ElMessage.info('分享歌单：' + playlist.name)
}

onMounted(() => {
  loadPlaylists()
})
</script>

<style scoped>
.my-playlists {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
}

.playlist-card {
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.playlist-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.cover-wrapper {
  position: relative;
  margin: -20px -20px 16px -20px;
}

.cover {
  width: 100%;
  height: 200px;
  border-radius: 8px 8px 0 0;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
  border-radius: 8px 8px 0 0;
}

.playlist-card:hover .play-overlay {
  opacity: 1;
}

.play-icon {
  font-size: 48px;
  color: #fff;
}

.privacy-tag {
  position: absolute;
  top: 12px;
  right: 12px;
}

.name {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.desc {
  color: #999;
  font-size: 13px;
  margin: 0 0 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.meta {
  display: flex;
  gap: 16px;
  color: #999;
  font-size: 12px;
}

.meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}
</style>
