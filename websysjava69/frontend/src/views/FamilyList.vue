<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">家庭管理</span>
      <el-button type="primary" @click="openDialog" v-if="!currentFamily">
        <el-icon><Plus /></el-icon>
        创建家庭
      </el-button>
    </div>

    <div class="card-shadow p-20" v-if="currentFamily">
      <div class="family-header">
        <div>
          <h2>{{ currentFamily.familyName }}</h2>
          <p class="text-muted">创建于 {{ currentFamily.createdAt }}</p>
        </div>
        <div>
          <el-button type="primary" @click="openAddMemberDialog" v-if="isAdmin">
            <el-icon><UserPlus /></el-icon>
            添加成员
          </el-button>
          <el-button type="danger" @click="deleteFamily" v-if="isAdmin">
            <el-icon><Delete /></el-icon>
            解散家庭
          </el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" class="mt-20">
        <el-tab-pane label="成员列表" name="members">
          <el-table :data="members" style="width: 100%">
            <el-table-column prop="userId" label="ID" width="80" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="role" label="角色" width="120">
              <template #default="{ row }">
                <el-tag :type="row.role === 'ADMIN' ? 'success' : 'info'" size="small">
                  {{ row.role === 'ADMIN' ? '管理员' : '成员' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="joinedAt" label="加入时间" width="180" />
            <el-table-column label="操作" width="200" v-if="isAdmin">
              <template #default="{ row }">
                <el-button type="primary" link @click="changeRole(row)" v-if="row.userId !== API.currentUserId">
                  切换角色
                </el-button>
                <el-button type="danger" link @click="removeMember(row)" v-if="row.userId !== API.currentUserId">
                  移除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="家庭资产" name="assets">
          <el-row :gutter="20" class="mb-20">
            <el-col :span="8">
              <div class="stat-card green">
                <div class="stat-label">家庭总资产</div>
                <div class="stat-value">¥{{ formatMoney(familyNetWorth?.totalAssets || 0) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-card orange">
                <div class="stat-label">家庭总负债</div>
                <div class="stat-value">¥{{ formatMoney(familyNetWorth?.totalLiabilities || 0) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-card blue">
                <div class="stat-label">家庭净资产</div>
                <div class="stat-value">¥{{ formatMoney(familyNetWorth?.netWorth || 0) }}</div>
              </div>
            </el-col>
          </el-row>

          <h3 class="mb-16">成员资产明细</h3>
          <el-table :data="familyNetWorth?.memberNetWorths || []" style="width: 100%">
            <el-table-column prop="nickname" label="成员" />
            <el-table-column prop="totalAssets" label="总资产">
              <template #default="{ row }">¥{{ formatMoney(row.totalAssets) }}</template>
            </el-table-column>
            <el-table-column prop="totalLiabilities" label="总负债">
              <template #default="{ row }">¥{{ formatMoney(row.totalLiabilities) }}</template>
            </el-table-column>
            <el-table-column prop="netWorth" label="净资产">
              <template #default="{ row }">
                <span :class="row.netWorth >= 0 ? 'positive' : 'negative'">
                  ¥{{ formatMoney(row.netWorth) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div class="card-shadow p-20 flex-center" v-else>
      <el-empty description="您还没有加入任何家庭，点击上方按钮创建" />
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑家庭' : '创建家庭'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="家庭名称" prop="familyName">
          <el-input v-model="form.familyName" placeholder="请输入家庭名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" :rows="3" placeholder="请输入描述（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="addMemberDialogVisible" title="添加家庭成员" width="500px">
      <el-form :model="addMemberForm" ref="addMemberFormRef" label-width="100px">
        <el-form-item label="选择用户">
          <el-select v-model="addMemberForm.userId" placeholder="请选择用户" filterable style="width: 100%">
            <el-option v-for="user in availableUsers" :key="user.id" :label="user.nickname" :value="user.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="addMemberForm.role">
            <el-radio value="MEMBER">成员</el-radio>
            <el-radio value="ADMIN">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addMemberDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddMember">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { familyApi, userApi, netWorthApi, API } from '@/api'

const currentFamily = ref(null)
const members = ref([])
const users = ref([])
const activeTab = ref('members')
const dialogVisible = ref(false)
const addMemberDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const addMemberFormRef = ref(null)
const familyNetWorth = ref(null)
const isAdmin = ref(false)

const form = ref({
  id: null,
  familyName: '',
  description: ''
})

const addMemberForm = ref({
  userId: null,
  role: 'MEMBER'
})

const rules = {
  familyName: [{ required: true, message: '请输入家庭名称', trigger: 'blur' }]
}

const availableUsers = computed(() => {
  const memberIds = members.value.map(m => m.userId)
  return users.value.filter(u => u.id !== API.currentUserId && !memberIds.includes(u.id))
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const loadFamily = async () => {
  try {
    const families = await familyApi.listByUser(API.currentUserId)
    if (families && families.length > 0) {
      currentFamily.value = families[0]
      API.currentFamilyId = currentFamily.value.id
      loadMembers()
      loadFamilyNetWorth()
      checkAdmin()
    }
  } catch (e) {
    console.error(e)
  }
}

const loadMembers = async () => {
  try {
    members.value = await familyApi.getMembers(currentFamily.value.id)
  } catch (e) {
    console.error(e)
  }
}

const loadUsers = async () => {
  try {
    users.value = await userApi.list()
  } catch (e) {
    console.error(e)
  }
}

const loadFamilyNetWorth = async () => {
  try {
    familyNetWorth.value = await netWorthApi.getFamily(currentFamily.value.id, API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const checkAdmin = async () => {
  try {
    isAdmin.value = await familyApi.isAdmin(currentFamily.value.id, API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const openDialog = (row = null) => {
  isEdit.value = !!row
  if (row) {
    form.value = { ...row }
  } else {
    form.value = { id: null, familyName: '', description: '' }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    await familyApi.create(form.value, API.currentUserId)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    loadFamily()
  } catch (e) {
    console.error(e)
  }
}

const deleteFamily = () => {
  ElMessageBox.confirm('确定要解散这个家庭吗？所有成员数据将被移除！', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await familyApi.delete(currentFamily.value.id)
      ElMessage.success('已解散')
      currentFamily.value = null
      API.currentFamilyId = null
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const openAddMemberDialog = () => {
  addMemberForm.value = { userId: null, role: 'MEMBER' }
  addMemberDialogVisible.value = true
}

const handleAddMember = async () => {
  try {
    await familyApi.addMember(currentFamily.value.id, addMemberForm.value.userId, addMemberForm.value.role, API.currentUserId)
    ElMessage.success('添加成功')
    addMemberDialogVisible.value = false
    loadMembers()
  } catch (e) {
    console.error(e)
  }
}

const removeMember = (row) => {
  ElMessageBox.confirm('确定要移除该成员吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await familyApi.removeMember(currentFamily.value.id, row.userId, API.currentUserId)
      ElMessage.success('移除成功')
      loadMembers()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const changeRole = (row) => {
  const newRole = row.role === 'ADMIN' ? 'MEMBER' : 'ADMIN'
  ElMessageBox.confirm(`确定将该成员角色切换为${newRole === 'ADMIN' ? '管理员' : '成员'}吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await familyApi.updateMemberRole(currentFamily.value.id, row.userId, newRole, API.currentUserId)
      ElMessage.success('角色已更新')
      loadMembers()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadFamily()
  loadUsers()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.mt-20 { margin-top: 20px; }
.mb-16 { margin-bottom: 16px; }
.mb-20 { margin-bottom: 20px; }
.flex-center { display: flex; justify-content: center; align-items: center; }

.family-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.family-header h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}

.text-muted {
  color: #909399;
  font-size: 14px;
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}
</style>
