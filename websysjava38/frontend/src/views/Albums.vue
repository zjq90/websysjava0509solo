<template>
  <div class="albums">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>相册管理</span>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新增相册
          </el-button>
        </div>
      </template>

      <el-table :data="albumList" stripe border>
        <el-table-column prop="albumNo" label="相册编号" width="150" />
        <el-table-column prop="name" label="相册名称" width="150" />
        <el-table-column prop="totalPhotos" label="照片总数" width="100" />
        <el-table-column prop="selectedCount" label="已选数量" width="100" />
        <el-table-column prop="isEncrypted" label="是否加密" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isEncrypted === 1 ? 'warning' : 'info'">
              {{ row.isEncrypted === 1 ? '加密' : '公开' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewPhotos(row)">查看照片</el-button>
            <el-button type="success" link size="small" @click="editAlbum(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="deleteAlbum(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑相册' : '新增相册'" width="600px">
      <el-form :model="albumForm" label-width="100px">
        <el-form-item label="相册名称">
          <el-input v-model="albumForm.name" placeholder="请输入相册名称" />
        </el-form-item>
        <el-form-item label="关联订单ID">
          <el-input-number v-model="albumForm.orderId" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="客户ID">
          <el-input-number v-model="albumForm.customerId" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="访问密码">
          <el-input v-model="albumForm.accessCode" placeholder="请输入访问密码" />
        </el-form-item>
        <el-form-item label="是否加密">
          <el-radio-group v-model="albumForm.isEncrypted">
            <el-radio :label="0">公开</el-radio>
            <el-radio :label="1">加密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="albumForm.status">
            <el-radio :label="0">未上传</el-radio>
            <el-radio :label="1">上传中</el-radio>
            <el-radio :label="2">已上传</el-radio>
            <el-radio :label="3">选片中</el-radio>
            <el-radio :label="4">选片完成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAlbum">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="photoDialogVisible" title="相册照片" width="90%">
      <el-empty v-if="currentPhotos.length === 0" description="暂无照片" />
      <el-row :gutter="20" v-else>
        <el-col :span="6" v-for="photo in currentPhotos" :key="photo.id">
          <el-card shadow="hover" class="photo-card">
            <img :src="getPhotoUrl(photo)" class="photo-img" />
            <div class="photo-info">
              <div class="photo-name">{{ photo.fileName || '照片' }}</div>
              <div class="photo-meta">
                <el-tag size="small" v-if="photo.isSelected === 1" type="success">已选</el-tag>
                <el-tag size="small" v-if="photo.markType === 'like'" type="warning">喜欢</el-tag>
                <el-tag size="small" v-if="photo.markType === 'retouch'" type="primary">待修</el-tag>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <template #footer>
        <el-button @click="photoDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const albumList = ref([])
const dialogVisible = ref(false)
const photoDialogVisible = ref(false)
const isEdit = ref(false)
const currentPhotos = ref([])

const albumForm = reactive({
  id: null,
  name: '',
  orderId: null,
  customerId: null,
  accessCode: '',
  isEncrypted: 0,
  status: 0
})

const getStatusText = (status) => {
  const statusMap = {
    0: '未上传',
    1: '上传中',
    2: '已上传',
    3: '选片中',
    4: '选片完成',
    5: '精修中',
    6: '精修完成'
  }
  return statusMap[status] || '未知'
}

const getPhotoUrl = (photo) => {
  return photo.originalUrl || photo.thumbnailUrl || 'https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png'
}

const loadAlbums = async () => {
  try {
    const res = await request.get('/albums')
    albumList.value = res.data || []
  } catch (error) {
    console.error('加载相册失败', error)
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  albumForm.id = null
  albumForm.name = ''
  albumForm.orderId = null
  albumForm.customerId = null
  albumForm.accessCode = ''
  albumForm.isEncrypted = 0
  albumForm.status = 0
  dialogVisible.value = true
}

const editAlbum = (row) => {
  isEdit.value = true
  albumForm.id = row.id
  albumForm.name = row.name
  albumForm.orderId = row.orderId
  albumForm.customerId = row.customerId
  albumForm.accessCode = row.accessCode || ''
  albumForm.isEncrypted = row.isEncrypted ?? 0
  albumForm.status = row.status ?? 0
  dialogVisible.value = true
}

const submitAlbum = async () => {
  try {
    if (isEdit.value) {
      await request.put('/albums', albumForm)
      ElMessage.success('相册更新成功')
    } else {
      await request.post('/albums', albumForm)
      ElMessage.success('相册创建成功')
    }
    dialogVisible.value = false
    loadAlbums()
  } catch (error) {
    console.error('提交相册失败', error)
  }
}

const deleteAlbum = (row) => {
  ElMessageBox.confirm('确定要删除该相册吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/albums/${row.id}`)
      ElMessage.success('删除成功')
      loadAlbums()
    } catch (error) {
      console.error('删除相册失败', error)
    }
  }).catch(() => {})
}

const viewPhotos = async (row) => {
  try {
    const res = await request.get(`/albums/${row.id}/photos`)
    currentPhotos.value = res.data || []
    photoDialogVisible.value = true
  } catch (error) {
    console.error('加载照片失败', error)
  }
}

onMounted(() => {
  loadAlbums()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.photo-card {
  margin-bottom: 20px;
}

.photo-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.photo-info {
  padding: 10px 0;
}

.photo-name {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.photo-meta {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}
</style>
