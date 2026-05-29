<template>
  <div class="page-container">
    <div class="page-header">
      <h1>版权管理</h1>
      <p>管理音乐版权声明与侵权举报</p>
    </div>

    <div class="chart-tabs" style="margin-bottom: 24px;">
      <button class="chart-tab" :class="{ active: activeTab === 'declare' }" @click="activeTab = 'declare'">📝 版权声明</button>
      <button class="chart-tab" :class="{ active: activeTab === 'report' }" @click="activeTab = 'report'">🚨 侵权举报</button>
      <button class="chart-tab" :class="{ active: activeTab === 'list' }" @click="activeTab = 'list'; loadReports()">📋 举报列表</button>
    </div>

    <section v-if="activeTab === 'declare'" style="max-width: 600px;">
      <div class="copyright-card">
        <h3 style="margin-bottom: 16px;">发布版权声明</h3>
        <div class="form-group">
          <label>音乐ID</label>
          <input class="input" v-model="declareForm.musicId" placeholder="请输入音乐ID" type="number" />
        </div>
        <div class="form-group">
          <label>版权类型</label>
          <select class="input" v-model="declareForm.type">
            <option value="原创">原创</option>
            <option value="翻唱">翻唱</option>
            <option value="授权">授权</option>
            <option value="公共领域">公共领域</option>
          </select>
        </div>
        <div class="form-group">
          <label>版权声明</label>
          <textarea class="input" v-model="declareForm.declaration" placeholder="请输入版权声明内容" rows="3"></textarea>
        </div>
        <div class="form-group">
          <label style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
            <input type="checkbox" v-model="declareForm.isOriginal" />
            确认为原创作品
          </label>
        </div>
        <button class="btn btn-primary" @click="submitDeclare">提交声明</button>
      </div>

      <div v-if="copyrightInfo" class="copyright-card" style="margin-top: 16px;">
        <h3 style="margin-bottom: 12px;">当前版权信息</h3>
        <p><strong>类型：</strong>{{ copyrightInfo.type }}</p>
        <p><strong>声明：</strong>{{ copyrightInfo.declaration }}</p>
        <p><strong>原创认证：</strong>
          <span :class="copyrightInfo.isOriginal ? 'status-badge active' : 'status-badge reported'">
            {{ copyrightInfo.isOriginal ? '已认证' : '未认证' }}
          </span>
        </p>
        <p v-if="copyrightInfo.originalCert"><strong>证书编号：</strong>{{ copyrightInfo.originalCert }}</p>
        <p><strong>举报次数：</strong>{{ copyrightInfo.reportCount }}</p>
      </div>
    </section>

    <section v-if="activeTab === 'report'" style="max-width: 600px;">
      <div class="copyright-card">
        <h3 style="margin-bottom: 16px;">举报侵权</h3>
        <div class="form-group">
          <label>版权声明ID</label>
          <input class="input" v-model="reportForm.copyrightId" placeholder="请输入版权声明ID" type="number" />
        </div>
        <div class="form-group">
          <label>音乐ID</label>
          <input class="input" v-model="reportForm.musicId" placeholder="请输入音乐ID" type="number" />
        </div>
        <div class="form-group">
          <label>举报原因</label>
          <textarea class="input" v-model="reportForm.reason" placeholder="请详细描述侵权情况" rows="4"></textarea>
        </div>
        <div class="form-group">
          <label>证据链接</label>
          <input class="input" v-model="reportForm.evidence" placeholder="请提供证据链接（选填）" />
        </div>
        <button class="btn btn-danger" @click="submitReport">提交举报</button>
      </div>
    </section>

    <section v-if="activeTab === 'list'">
      <div v-if="reports.length > 0">
        <div class="copyright-card" v-for="report in reports" :key="report.id">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;">
            <strong>举报 #{{ report.id }}</strong>
            <span class="status-badge" :class="report.status">{{ statusText(report.status) }}</span>
          </div>
          <p><strong>音乐ID：</strong>{{ report.musicId }}</p>
          <p><strong>原因：</strong>{{ report.reason }}</p>
          <p v-if="report.evidence"><strong>证据：</strong>{{ report.evidence }}</p>
          <div v-if="report.status === 'pending'" style="margin-top: 12px; display: flex; gap: 8px;">
            <button class="btn btn-sm btn-primary" @click="handleReport(report.id, 'resolved')">通过</button>
            <button class="btn btn-sm btn-outline" @click="handleReport(report.id, 'rejected')">驳回</button>
          </div>
        </div>
      </div>
      <div class="empty-state" v-else>
        <div class="empty-icon">📋</div>
        <p>暂无举报记录</p>
      </div>
    </section>

    <div v-if="message" :style="{ color: messageSuccess ? 'var(--accent-green)' : 'var(--accent-red)', marginTop: '16px', fontSize: '14px' }">
      {{ message }}
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '../store'
import api from '../api'

const userStore = useUserStore()
const activeTab = ref('declare')
const copyrightInfo = ref(null)
const reports = ref([])
const message = ref('')
const messageSuccess = ref(false)

const declareForm = ref({
  musicId: '',
  type: '原创',
  declaration: '',
  isOriginal: true
})

const reportForm = ref({
  copyrightId: '',
  musicId: '',
  reason: '',
  evidence: ''
})

watch(() => declareForm.value.musicId, async (id) => {
  if (id) {
    try {
      const res = await api.copyright.getByMusic(id)
      if (res.code === 200) {
        copyrightInfo.value = res.data
      } else {
        copyrightInfo.value = null
      }
    } catch { copyrightInfo.value = null }
  }
})

async function submitDeclare() {
  if (!declareForm.value.musicId) {
    message.value = '请填写音乐ID'
    messageSuccess.value = false
    return
  }
  try {
    const res = await api.copyright.declare({
      ...declareForm.value,
      userId: userStore.currentUser?.id || 1
    })
    if (res.code === 200) {
      message.value = '版权声明提交成功！'
      messageSuccess.value = true
      copyrightInfo.value = res.data
    } else {
      message.value = res.message || '提交失败'
      messageSuccess.value = false
    }
  } catch (e) {
    message.value = '提交失败'
    messageSuccess.value = false
  }
}

async function submitReport() {
  if (!reportForm.value.copyrightId || !reportForm.value.musicId || !reportForm.value.reason) {
    message.value = '请填写完整信息'
    messageSuccess.value = false
    return
  }
  try {
    const res = await api.copyright.report({
      ...reportForm.value,
      reporterId: userStore.currentUser?.id || 1
    })
    if (res.code === 200) {
      message.value = '举报提交成功！'
      messageSuccess.value = true
      reportForm.value = { copyrightId: '', musicId: '', reason: '', evidence: '' }
    } else {
      message.value = res.message || '提交失败'
      messageSuccess.value = false
    }
  } catch (e) {
    message.value = '提交失败'
    messageSuccess.value = false
  }
}

async function loadReports() {
  const res = await api.copyright.pendingReports()
  if (res.code === 200) reports.value = res.data
}

async function handleReport(id, status) {
  const res = await api.copyright.updateReportStatus(id, status)
  if (res.code === 200) {
    await loadReports()
  }
}

function statusText(status) {
  const map = { pending: '待处理', resolved: '已通过', rejected: '已驳回' }
  return map[status] || status
}
</script>
