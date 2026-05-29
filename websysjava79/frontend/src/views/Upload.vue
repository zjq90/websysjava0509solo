<template>
  <div class="upload-page">
    <div class="upload-card">
      <h1>上传音乐</h1>
      <el-form :model="form" label-width="100px">
        <el-form-item label="歌曲名称">
          <el-input v-model="form.title" placeholder="请输入歌曲名称" />
        </el-form-item>
        <el-form-item label="专辑">
          <el-input v-model="form.album" placeholder="请输入专辑名称" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入歌曲简介"
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="form.tags" multiple placeholder="请选择标签">
            <el-option label="流行" value="流行" />
            <el-option label="摇滚" value="摇滚" />
            <el-option label="抒情" value="抒情" />
            <el-option label="古典" value="古典" />
            <el-option label="电子" value="电子" />
            <el-option label="民谣" value="民谣" />
            <el-option label="R&B" value="R&B" />
            <el-option label="中国风" value="中国风" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
            class="cover-uploader"
            :show-file-list="false"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="form.coverUrl" :src="form.coverUrl" class="cover-image" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="音频文件">
          <el-upload
            drag
            :auto-upload="false"
            :on-change="handleAudioChange"
            accept=".mp3"
          >
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持 MP3 格式，文件大小不超过 50MB
              </div>
            </template>
          </el-upload>
          <div v-if="audioFile" class="file-info">
            已选择: {{ audioFile.name }}
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpload">
            提交审核
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const audioFile = ref<File | null>(null)

const form = ref({
  title: '',
  album: '',
  description: '',
  tags: [] as string[],
  coverUrl: '',
  audioUrl: ''
})

const beforeCoverUpload = (file: File) => {
  form.value.coverUrl = URL.createObjectURL(file)
  return false
}

const handleAudioChange = (file: any) => {
  audioFile.value = file.raw
}

const handleUpload = async () => {
  if (!form.value.title) {
    ElMessage.warning('请输入歌曲名称')
    return
  }

  loading.value = true
  try {
    form.value.audioUrl = '/audio/sample.mp3'
    form.value.coverUrl = form.value.coverUrl || 'https://picsum.photos/200/200'
    
    await request.post('/musics', form.value)
    ElMessage.success('提交成功，等待审核')
    router.push('/')
  } catch (error) {
    console.error('Failed to upload music')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.upload-page {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 100px;
}

.upload-card {
  background: white;
  border-radius: 12px;
  padding: 32px;
}

.upload-card h1 {
  font-size: 24px;
  margin-bottom: 24px;
}

.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-uploader:hover {
  border-color: #409eff;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.cover-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

.file-info {
  margin-top: 12px;
  padding: 8px 12px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
}
</style>
