<template>
  <div class="login-container">
    <div class="login-card">
      <h2>{{ isRegister ? '注册账号' : '登录' }}</h2>

      <div class="form-group">
        <label>用户名</label>
        <input class="input" v-model="form.username" placeholder="请输入用户名" />
      </div>

      <div class="form-group">
        <label>密码</label>
        <input class="input" type="password" v-model="form.password" placeholder="请输入密码" />
      </div>

      <div v-if="isRegister" class="form-group">
        <label>邮箱</label>
        <input class="input" v-model="form.email" placeholder="请输入邮箱" />
      </div>

      <div v-if="isRegister" class="form-group">
        <label>昵称</label>
        <input class="input" v-model="form.nickname" placeholder="请输入昵称" />
      </div>

      <div v-if="errorMessage" style="color: var(--accent-red); font-size: 13px; margin-bottom: 12px;">
        {{ errorMessage }}
      </div>

      <button class="btn btn-primary" @click="submit">
        {{ isRegister ? '注册' : '登录' }}
      </button>

      <div class="switch-text">
        {{ isRegister ? '已有账号？' : '没有账号？' }}
        <a href="#" @click.prevent="isRegister = !isRegister; errorMessage = ''">
          {{ isRegister ? '去登录' : '去注册' }}
        </a>
      </div>

      <div style="margin-top: 24px; padding-top: 16px; border-top: 1px solid var(--border);">
        <p style="font-size: 12px; color: var(--text-muted); text-align: center; margin-bottom: 8px;">快速体验账号</p>
        <div style="display: flex; gap: 8px; flex-wrap: wrap; justify-content: center;">
          <button class="btn btn-sm btn-outline" v-for="u in quickUsers" :key="u.username" @click="quickLogin(u)">
            {{ u.nickname }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store'

const router = useRouter()
const userStore = useUserStore()
const isRegister = ref(false)
const errorMessage = ref('')

const form = ref({
  username: '',
  password: '',
  email: '',
  nickname: ''
})

const quickUsers = [
  { username: 'musiclover', password: '123456', nickname: '音乐爱好者' },
  { username: 'rockfan', password: '123456', nickname: '摇滚青年' },
  { username: 'classical', password: '123456', nickname: '古典乐迷' },
  { username: 'popsinger', password: '123456', nickname: '流行歌手' },
]

async function submit() {
  errorMessage.value = ''
  if (!form.value.username || !form.value.password) {
    errorMessage.value = '请填写用户名和密码'
    return
  }

  if (isRegister.value) {
    const success = await userStore.register(form.value.username, form.value.password, form.value.email, form.value.nickname)
    if (success) {
      isRegister.value = false
      errorMessage.value = '注册成功，请登录'
    } else {
      errorMessage.value = '注册失败，用户名可能已存在'
    }
  } else {
    const success = await userStore.login(form.value.username, form.value.password)
    if (success) {
      router.push('/')
    } else {
      errorMessage.value = '用户名或密码错误'
    }
  }
}

async function quickLogin(user) {
  const success = await userStore.login(user.username, user.password)
  if (success) {
    router.push('/')
  }
}
</script>
