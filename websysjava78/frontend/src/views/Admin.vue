<template>
  <div class="admin-page">
    <h1 class="page-title">管理后台</h1>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="上传音乐" name="upload">
        <div class="card">
          <el-form :model="musicForm" label-width="100px">
            <el-form-item label="歌曲名" required>
              <el-input v-model="musicForm.title" placeholder="请输入歌曲名" />
            </el-form-item>
            <el-form-item label="歌手" required>
              <el-input v-model="musicForm.artist" placeholder="请输入歌手名" />
            </el-form-item>
            <el-form-item label="专辑">
              <el-input v-model="musicForm.album" placeholder="请输入专辑名" />
            </el-form-item>
            <el-form-item label="流派">
              <el-input v-model="musicForm.genre" placeholder="请输入流派" />
            </el-form-item>
            <el-form-item label="时长(秒)">
              <el-input-number v-model="musicForm.duration" :min="0" />
            </el-form-item>
            <el-form-item label="封面URL">
              <el-input v-model="musicForm.coverUrl" placeholder="封面图片URL" />
            </el-form-item>
            <el-form-item label="会员专享">
              <el-switch v-model="musicForm.isPremium" />
            </el-form-item>
            <el-form-item label="128kbps">
              <el-upload
                v-model:file-list="file128List"
                :auto-upload="false"
                :limit="1"
                accept=".mp3"
              >
                <el-button>选择文件</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item label="320kbps">
              <el-upload
                v-model:file-list="file320List"
                :auto-upload="false"
                :limit="1"
                accept=".mp3"
              >
                <el-button>选择文件</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item label="无损FLAC">
              <el-upload
                v-model:file-list="fileFlacList"
                :auto-upload="false"
                :limit="1"
                accept=".flac"
              >
                <el-button>选择文件</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item label="歌词文件">
              <el-upload
                v-model:file-list="lrcFileList"
                :auto-upload="false"
                :limit="1"
                accept=".lrc,.txt"
              >
                <el-button>选择LRC文件</el-button>
              </el-upload>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="uploading" @click="handleUpload">
                上传音乐
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="批量导入" name="batch">
        <div class="card">
          <p class="tip">请粘贴JSON格式的音乐列表，支持批量导入音乐信息（文件路径需已存在于服务器）</p>
          <el-input
            v-model="batchJson"
            type="textarea"
            :rows="15"
            placeholder="[{&#10;  &quot;title&quot;: &quot;歌曲名&quot;,&#10;  &quot;artist&quot;: &quot;歌手&quot;,&#10;  &quot;album&quot;: &quot;专辑&quot;,&#10;  &quot;duration&quot;: 240,&#10;  &quot;isPremium&quot;: false,&#10;  &quot;filePath128&quot;: &quot;xxx.mp3&quot;,&#10;  &quot;filePath320&quot;: &quot;xxx.mp3&quot;,&#10;  &quot;filePathFlac&quot;: &quot;xxx.flac&quot;&#10;}]"
          />
          <el-button type="primary" :loading="importing" @click="handleBatchImport" style="margin-top: 15px;">
            批量导入
          </el-button>
        </div>
      </el-tab-pane>

      <el-tab-pane label="音乐管理" name="manage">
        <div class="card">
          <el-table :data="musicList" v-loading="loading" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="歌曲名" />
            <el-table-column prop="artist" label="歌手" />
            <el-table-column prop="album" label="专辑" />
            <el-table-column label="会员" width="80">
              <template #default="{ row }">
                <el-tag v-if="row.isPremium" type="warning" size="small">VIP</el-tag>
                <el-tag v-else type="info" size="small">免费</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="playCount" label="播放量" width="100" />
            <el-table-column prop="downloadCount" label="下载量" width="100" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { uploadMusic, batchImportMusic, getMusicList, deleteMusic } from '@/api/music'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('upload')
const uploading = ref(false)
const importing = ref(false)
const loading = ref(false)
const musicList = ref([])

const musicForm = reactive({
  title: '',
  artist: '',
  album: '',
  genre: '',
  duration: null,
  coverUrl: '',
  isPremium: false
})

const file128List = ref([])
const file320List = ref([])
const fileFlacList = ref([])
const lrcFileList = ref([])
const batchJson = ref('')

const handleUpload = async () => {
  if (!musicForm.title || !musicForm.artist) {
    ElMessage.warning('请填写歌曲名和歌手')
    return
  }

  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('music', new Blob([JSON.stringify(musicForm)], { type: 'application/json' }))

    if (file128List.value.length > 0) {
      formData.append('file128', file128List.value[0].raw)
    }
    if (file320List.value.length > 0) {
      formData.append('file320', file320List.value[0].raw)
    }
    if (fileFlacList.value.length > 0) {
      formData.append('fileFlac', fileFlacList.value[0].raw)
    }
    if (lrcFileList.value.length > 0) {
      formData.append('lrcFile', lrcFileList.value[0].raw)
    }

    await uploadMusic(formData)
    ElMessage.success('上传成功')

    musicForm.title = ''
    musicForm.artist = ''
    musicForm.album = ''
    musicForm.genre = ''
    musicForm.duration = null
    musicForm.coverUrl = ''
    musicForm.isPremium = false
    file128List.value = []
    file320List.value = []
    fileFlacList.value = []
    lrcFileList.value = []
  } catch (e) {
    ElMessage.error('上传失败')
  } finally {
    uploading.value = false
  }
}

const handleBatchImport = async () => {
  if (!batchJson.value.trim()) {
    ElMessage.warning('请输入JSON数据')
    return
  }

  try {
    const data = JSON.parse(batchJson.value)
    if (!Array.isArray(data)) {
      throw new Error('数据格式错误，应为数组')
    }

    importing.value = true
    const result = await batchImportMusic(data)
    ElMessage.success(`成功导入 ${result.count} 首音乐`)
    batchJson.value = ''
  } catch (e) {
    ElMessage.error('导入失败: ' + (e.message || 'JSON格式错误'))
  } finally {
    importing.value = false
  }
}

const fetchMusicList = async () => {
  loading.value = true
  try {
    const result = await getMusicList(0, 100)
    musicList.value = result.content
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定删除"${row.title}"？此操作不可恢复`,
      '删除确认',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'danger'
      }
    )
    await deleteMusic(row.id)
    ElMessage.success('删除成功')
    fetchMusicList()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(fetchMusicList)
</script>

<style scoped>
.admin-page {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  color: white;
  font-size: 28px;
  margin-bottom: 20px;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
}

.tip {
  color: #999;
  margin-bottom: 15px;
  font-size: 13px;
}

:deep(.el-tabs__nav-wrap::after) {
  background-color: rgba(255, 255, 255, 0.2);
}

:deep(.el-tabs__item) {
  color: rgba(255, 255, 255, 0.7);
}

:deep(.el-tabs__item.is-active) {
  color: white;
}

:deep(.el-tabs__active-bar) {
  background-color: white;
}
</style>
