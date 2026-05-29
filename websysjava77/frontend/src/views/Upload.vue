<template>
  <div class="page-container">
    <div class="page-header">
      <h1>上传音乐</h1>
      <p>分享你的音乐作品</p>
    </div>

    <div style="max-width: 700px;">
      <div class="form-group">
        <label>音频文件</label>
        <div class="upload-area" @click="audioInput?.click()" @dragover.prevent @drop.prevent="handleAudioDrop">
          <div class="upload-icon">🎵</div>
          <div class="upload-text" v-if="!audioFile">点击或拖拽上传音频文件</div>
          <div class="upload-text" v-else>{{ audioFile.name }}</div>
          <div class="upload-hint">支持 MP3/WAV/FLAC/AAC/OGG/M4A 格式，最大 100MB</div>
        </div>
        <input type="file" ref="audioInput" style="display:none" accept=".mp3,.wav,.flac,.aac,.ogg,.wma,.m4a" @change="onAudioChange" />
      </div>

      <div class="form-group">
        <label>封面图片（可选）</label>
        <div class="upload-area" @click="coverInput?.click()" style="padding: 24px;">
          <div v-if="coverPreview" style="margin-bottom: 8px;">
            <img :src="coverPreview" style="max-width: 200px; max-height: 200px; border-radius: 8px;" />
          </div>
          <div v-else class="upload-icon">🖼️</div>
          <div class="upload-text">{{ coverFile ? coverFile.name : '点击上传封面' }}</div>
          <div class="upload-hint">支持 JPG/PNG/GIF/WEBP，最大 10MB</div>
        </div>
        <input type="file" ref="coverInput" style="display:none" accept=".jpg,.jpeg,.png,.gif,.webp" @change="onCoverChange" />
      </div>

      <div class="form-group">
        <label>歌曲标题 *</label>
        <input class="input" v-model="form.title" placeholder="请输入歌曲标题" />
      </div>

      <div class="form-group">
        <label>歌手 *</label>
        <input class="input" v-model="form.artist" placeholder="请输入歌手名称" />
      </div>

      <div class="form-group">
        <label>专辑</label>
        <input class="input" v-model="form.album" placeholder="请输入专辑名称（选填）" />
      </div>

      <div class="form-group">
        <label>歌词</label>
        <textarea class="input" v-model="form.lyrics" placeholder="请输入歌词（选填）" rows="6"></textarea>
      </div>

      <div style="display: flex; gap: 12px; margin-top: 24px;">
        <button class="btn btn-primary" @click="submitUpload" :disabled="uploading">
          {{ uploading ? '上传中...' : '上传音乐' }}
        </button>
        <button class="btn btn-outline" @click="resetForm">重置</button>
      </div>

      <div v-if="uploadMessage" :style="{ color: uploadSuccess ? 'var(--accent-green)' : 'var(--accent-red)', marginTop: '16px', fontSize: '14px' }">
        {{ uploadMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../store'
import api from '../api'

const userStore = useUserStore()
const audioInput = ref(null)
const coverInput = ref(null)
const audioFile = ref(null)
const coverFile = ref(null)
const coverPreview = ref('')
const uploading = ref(false)
const uploadMessage = ref('')
const uploadSuccess = ref(false)

const form = ref({
  title: '',
  artist: '',
  album: '',
  lyrics: ''
})

function onAudioChange(e) {
  const file = e.target.files[0]
  if (file) {
    audioFile.value = file
    if (!form.value.title) {
      form.value.title = file.name.replace(/\.[^.]+$/, '')
    }
  }
}

function handleAudioDrop(e) {
  const file = e.dataTransfer.files[0]
  if (file) {
    audioFile.value = file
  }
}

function onCoverChange(e) {
  const file = e.target.files[0]
  if (file) {
    coverFile.value = file
    coverPreview.value = URL.createObjectURL(file)
  }
}

async function submitUpload() {
  if (!audioFile.value) {
    uploadMessage.value = '请选择音频文件'
    uploadSuccess.value = false
    return
  }
  if (!form.value.title || !form.value.artist) {
    uploadMessage.value = '请填写歌曲标题和歌手'
    uploadSuccess.value = false
    return
  }
  if (!userStore.isLoggedIn) {
    uploadMessage.value = '请先登录'
    uploadSuccess.value = false
    return
  }

  uploading.value = true
  uploadMessage.value = ''

  try {
    const ext = audioFile.value.name.split('.').pop().toLowerCase()
    const formData = new FormData()
    formData.append('audioFile', audioFile.value)
    if (coverFile.value) formData.append('coverFile', coverFile.value)
    formData.append('title', form.value.title)
    formData.append('artist', form.value.artist)
    formData.append('album', form.value.album || '')
    formData.append('lyrics', form.value.lyrics || '')
    formData.append('format', ext)
    formData.append('userId', userStore.currentUser.id)

    const res = await api.music.upload(formData)
    if (res.code === 200) {
      uploadMessage.value = '上传成功！'
      uploadSuccess.value = true
      resetForm()
    } else {
      uploadMessage.value = res.message || '上传失败'
      uploadSuccess.value = false
    }
  } catch (e) {
    uploadMessage.value = '上传失败: ' + (e.message || '未知错误')
    uploadSuccess.value = false
  } finally {
    uploading.value = false
  }
}

function resetForm() {
  form.value = { title: '', artist: '', album: '', lyrics: '' }
  audioFile.value = null
  coverFile.value = null
  coverPreview.value = ''
  if (audioInput.value) audioInput.value.value = ''
  if (coverInput.value) coverInput.value.value = ''
}
</script>
