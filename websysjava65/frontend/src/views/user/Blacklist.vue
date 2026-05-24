<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">黑名单管理</div>
      <el-button type="primary" :icon="Plus" @click="showAddDialog = true">
        添加黑名单
      </el-button>
    </div>

    <el-card>
      <el-table :data="blacklist" stripe style="width: 100%">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="reason" label="封禁原因" />
        <el-table-column prop="banType" label="封禁类型" width="100">
          <template #default="{ row }">
            <span :class="row.banType === 'PERMANENT' ? 'badge-danger' : 'badge-warning'">
              {{ row.banType === 'PERMANENT' ? '永久封禁' : '临时封禁' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="createTime" label="封禁时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" size="small" link @click="removeBlacklist(row.userId)">
              解除封禁
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showAddDialog" title="添加黑名单" width="500px">
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="用户ID">
          <el-input v-model="addForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="封禁原因">
          <el-input type="textarea" v-model="addForm.reason" :rows="3" placeholder="请输入封禁原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="addBlacklist">确认添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getBlacklist, addToBlacklist, removeFromBlacklist } from '../../api'

const blacklist = ref([])
const showAddDialog = ref(false)
const addForm = ref({
  userId: '',
  reason: ''
})

const loadData = async () => {
  try {
    const res = await getBlacklist()
    blacklist.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const addBlacklist = async () => {
  if (!addForm.value.userId || !addForm.value.reason) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    await addToBlacklist({
      userId: addForm.value.userId,
      reason: addForm.value.reason
    })
    ElMessage.success('添加成功')
    showAddDialog.value = false
    addForm.value = { userId: '', reason: '' }
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const removeBlacklist = async (userId) => {
  try {
    await ElMessageBox.confirm('确定要解除该用户的封禁吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await removeFromBlacklist(userId)
    ElMessage.success('解除封禁成功')
    loadData()
  } catch (e) {
    if (e !== 'cancel') {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>
