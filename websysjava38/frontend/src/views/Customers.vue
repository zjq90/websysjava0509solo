<template>
  <div class="customers">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新增客户
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="客户姓名"
          style="width: 150px"
          clearable
        />
        <el-input
          v-model="searchForm.phone"
          placeholder="手机号码"
          style="width: 150px"
          clearable
        />
        <el-select v-model="searchForm.source" placeholder="客户来源" style="width: 150px" clearable>
          <el-option label="美团" value="美团" />
          <el-option label="抖音" value="抖音" />
          <el-option label="小程序" value="小程序" />
          <el-option label="门店" value="门店" />
          <el-option label="老客户推荐" value="老客户推荐" />
        </el-select>
        <el-button type="primary" @click="loadCustomers">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <el-table :data="customerList" stripe border>
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号码" width="130" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="wechat" label="微信号" width="120" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column prop="source" label="来源" width="120" />
        <el-table-column prop="vipLevel" label="VIP等级" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.vipLevel === 0" type="info">普通</el-tag>
            <el-tag v-else-if="row.vipLevel === 1" type="warning">VIP</el-tag>
            <el-tag v-else-if="row.vipLevel === 2" type="danger">SVIP</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="累计消费" width="120">
          <template #default="{ row }">
            <span style="color: #F56C6C; font-weight: bold">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="editCustomer(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="deleteCustomer(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadCustomers"
        @current-change="loadCustomers"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑客户' : '新增客户'" width="600px">
      <el-form :model="customerForm" :rules="rules" ref="customerFormRef" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="customerForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="customerForm.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="customerForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="customerForm.age" :min="0" :max="150" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="customerForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="微信号">
          <el-input v-model="customerForm.wechat" placeholder="请输入微信号" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="customerForm.address" type="textarea" :rows="2" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="来源">
          <el-select v-model="customerForm.source" placeholder="请选择来源" style="width: 100%">
            <el-option label="美团" value="美团" />
            <el-option label="抖音" value="抖音" />
            <el-option label="小程序" value="小程序" />
            <el-option label="门店" value="门店" />
            <el-option label="老客户推荐" value="老客户推荐" />
          </el-select>
        </el-form-item>
        <el-form-item label="VIP等级">
          <el-radio-group v-model="customerForm.vipLevel">
            <el-radio :label="0">普通</el-radio>
            <el-radio :label="1">VIP</el-radio>
            <el-radio :label="2">SVIP</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="customerForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCustomer">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const customerList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const customerFormRef = ref(null)

const searchForm = reactive({
  name: '',
  phone: '',
  source: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const customerForm = reactive({
  id: null,
  name: '',
  phone: '',
  gender: '男',
  age: null,
  email: '',
  wechat: '',
  address: '',
  source: '门店',
  vipLevel: 0,
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }]
}

const loadCustomers = async () => {
  try {
    const res = await request.get('/customers/page', {
      params: { page: pagination.page - 1, size: pagination.size }
    })
    customerList.value = res.data.content || []
    pagination.total = res.data.totalElements || 0
  } catch (error) {
    console.error('加载客户失败', error)
  }
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.phone = ''
  searchForm.source = ''
  loadCustomers()
}

const showCreateDialog = () => {
  isEdit.value = false
  customerForm.id = null
  customerForm.name = ''
  customerForm.phone = ''
  customerForm.gender = '男'
  customerForm.age = null
  customerForm.email = ''
  customerForm.wechat = ''
  customerForm.address = ''
  customerForm.source = '门店'
  customerForm.vipLevel = 0
  customerForm.remark = ''
  dialogVisible.value = true
}

const editCustomer = (row) => {
  isEdit.value = true
  customerForm.id = row.id
  customerForm.name = row.name
  customerForm.phone = row.phone
  customerForm.gender = row.gender || '男'
  customerForm.age = row.age
  customerForm.email = row.email || ''
  customerForm.wechat = row.wechat || ''
  customerForm.address = row.address || ''
  customerForm.source = row.source || '门店'
  customerForm.vipLevel = row.vipLevel || 0
  customerForm.remark = row.remark || ''
  dialogVisible.value = true
}

const submitCustomer = async () => {
  if (!customerFormRef.value) return
  await customerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await request.put('/customers', customerForm)
          ElMessage.success('客户更新成功')
        } else {
          await request.post('/customers', customerForm)
          ElMessage.success('客户创建成功')
        }
        dialogVisible.value = false
        loadCustomers()
      } catch (error) {
        console.error('提交客户失败', error)
      }
    }
  })
}

const deleteCustomer = (row) => {
  ElMessageBox.confirm('确定要删除该客户吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/customers/${row.id}`)
      ElMessage.success('删除成功')
      loadCustomers()
    } catch (error) {
      console.error('删除客户失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadCustomers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
