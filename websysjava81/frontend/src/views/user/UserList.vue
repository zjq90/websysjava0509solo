<template>
  <div class="page-container">
    <div class="page-header">
      <h3>用户管理</h3>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="用户ID/昵称/手机号"
        clearable
        style="width: 200px"
        @keyup.enter="handleSearch"
      />
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD HH:mm:ss"
        style="width: 300px"
      />
      <el-select v-model="searchForm.memberStatus" placeholder="会员状态" clearable style="width: 120px">
        <el-option label="免费用户" :value="0" />
        <el-option label="付费用户" :value="1" />
      </el-select>
      <el-select v-model="searchForm.userStatus" placeholder="用户状态" clearable style="width: 120px">
        <el-option label="正常" :value="0" />
        <el-option label="已封禁" :value="1" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
      <el-button type="danger" @click="showBatchBanDialog">批量封禁</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="userId" label="用户ID" width="120" />
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="registerTime" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.registerTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="lastLoginTime" label="最后登录时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.lastLoginTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalGameTime" label="游戏总时长(分钟)" width="150" />
      <el-table-column prop="memberStatus" label="会员状态" width="100">
        <template #default="{ row }">
          <el-tag :class="row.memberStatus === 1 ? 'member-tag-vip' : 'member-tag-free'">
            {{ row.memberStatus === 1 ? '付费' : '免费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userStatus" label="用户状态" width="100">
        <template #default="{ row }">
          <el-tag :class="row.userStatus === 1 ? 'status-tag-banned' : 'status-tag-normal'">
            {{ row.userStatus === 1 ? '已封禁' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="goToDetail(row.userId)">详情</el-button>
          <el-button v-if="row.userStatus !== 1" type="danger" link @click="showBanDialog(row)">封禁</el-button>
          <el-button v-if="row.userStatus === 1" type="success" link @click="handleUnban(row)">解封</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.pageNum"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: flex-end"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />

    <el-dialog v-model="banDialogVisible" title="封禁用户" width="500px">
      <el-form :model="banForm" label-width="100px">
        <el-form-item label="封禁类型">
          <el-radio-group v-model="banForm.banType">
            <el-radio :value="0">临时封禁</el-radio>
            <el-radio :value="1">永久封禁</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="解封时间" v-if="banForm.banType === 0">
          <el-date-picker
            v-model="banForm.unbanTime"
            type="datetime"
            placeholder="选择解封时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="封禁原因">
          <el-input
            v-model="banForm.banReason"
            type="textarea"
            :rows="4"
            placeholder="请输入封禁原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="banDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBan">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="batchBanDialogVisible" title="批量封禁" width="500px">
      <el-upload
        action=""
        :auto-upload="false"
        :show-file-list="false"
        accept=".csv"
        :on-change="handleFileChange"
      >
        <el-button type="primary">选择CSV文件</el-button>
        <div style="margin-top: 10px; color: #909399; font-size: 12px">
          CSV格式：每行一个用户ID，如：U001,U002,U003
        </div>
      </el-upload>
      <el-form :model="batchBanForm" label-width="100px" style="margin-top: 20px">
        <el-form-item label="封禁类型">
          <el-radio-group v-model="batchBanForm.banType">
            <el-radio :value="0">临时封禁</el-radio>
            <el-radio :value="1">永久封禁</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="解封时间" v-if="batchBanForm.banType === 0">
          <el-date-picker
            v-model="batchBanForm.unbanTime"
            type="datetime"
            placeholder="选择解封时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="封禁原因">
          <el-input
            v-model="batchBanForm.banReason"
            type="textarea"
            :rows="4"
            placeholder="请输入封禁原因"
          />
        </el-form-item>
      </el-form>
      <div v-if="batchUserIds.length > 0">
        <div>已选择 {{ batchUserIds.length }} 个用户：</div>
        <el-tag v-for="id in batchUserIds" :key="id" style="margin: 5px">{{ id }}</el-tag>
      </div>
      <template #footer>
        <el-button @click="batchBanDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchBan" :disabled="batchUserIds.length === 0">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, banUser, unbanUser, batchBanUsers, getBanRecords } from '../../api/user'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const dateRange = ref([])
const searchForm = reactive({
  keyword: '',
  memberStatus: null,
  userStatus: null
})
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const banDialogVisible = ref(false)
const currentUser = ref(null)
const banForm = reactive({
  userId: '',
  banType: 0,
  banReason: '',
  unbanTime: '',
  operatorId: 'ADMIN001',
  operatorName: '系统管理员'
})

const batchBanDialogVisible = ref(false)
const batchBanForm = reactive({
  banType: 0,
  banReason: '',
  unbanTime: '',
  operatorId: 'ADMIN001',
  operatorName: '系统管理员'
})
const batchUserIds = ref([])

const formatDate = (date) => {
  if (!date) return '-'
  return date
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res = await getUserList(params)
    if (res.data.code === 200) {
      tableData.value = res.data.data.records
      pagination.total = res.data.data.total
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.memberStatus = null
  searchForm.userStatus = null
  dateRange.value = []
  pagination.pageNum = 1
  fetchData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

const goToDetail = (userId) => {
  router.push(`/user/detail/${userId}`)
}

const showBanDialog = (row) => {
  currentUser.value = row
  banForm.userId = row.userId
  banForm.banType = 0
  banForm.banReason = ''
  banForm.unbanTime = ''
  banDialogVisible.value = true
}

const handleBan = async () => {
  if (!banForm.banReason) {
    ElMessage.warning('请输入封禁原因')
    return
  }
  if (banForm.banType === 0 && !banForm.unbanTime) {
    ElMessage.warning('请选择解封时间')
    return
  }
  try {
    const res = await banUser(banForm)
    if (res.data.code === 200) {
      ElMessage.success('封禁成功')
      banDialogVisible.value = false
      fetchData()
    }
  } catch (error) {
    ElMessage.error('封禁失败')
  }
}

const handleUnban = async (row) => {
  try {
    const res = await getBanRecords(row.userId)
    if (res.data.code === 200 && res.data.data.length > 0) {
      const activeBan = res.data.data.find(b => b.status === 1)
      if (activeBan) {
        await ElMessageBox.confirm('确定要解封该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const unbanRes = await unbanUser(activeBan.id)
        if (unbanRes.data.code === 200) {
          ElMessage.success('解封成功')
          fetchData()
        }
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('解封失败')
    }
  }
}

const showBatchBanDialog = () => {
  batchBanForm.banType = 0
  batchBanForm.banReason = ''
  batchBanForm.unbanTime = ''
  batchUserIds.value = []
  batchBanDialogVisible.value = true
}

const handleFileChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    const content = e.target.result
    const ids = content.split(/[,\n\r]+/).filter(id => id.trim())
    batchUserIds.value = [...new Set(ids)]
  }
  reader.readAsText(file.raw)
}

const handleBatchBan = async () => {
  if (!batchBanForm.banReason) {
    ElMessage.warning('请输入封禁原因')
    return
  }
  if (batchBanForm.banType === 0 && !batchBanForm.unbanTime) {
    ElMessage.warning('请选择解封时间')
    return
  }
  try {
    const list = batchUserIds.value.map(userId => ({
      userId,
      ...batchBanForm
    }))
    const res = await batchBanUsers(list)
    if (res.data.code === 200) {
      ElMessage.success('批量封禁成功')
      batchBanDialogVisible.value = false
      fetchData()
    }
  } catch (error) {
    ElMessage.error('批量封禁失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>
