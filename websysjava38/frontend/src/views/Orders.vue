<template>
  <div class="orders">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新建订单
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="订单编号"
          style="width: 200px"
          clearable
        />
        <el-input
          v-model="searchForm.customerName"
          placeholder="客户姓名"
          style="width: 150px"
          clearable
        />
        <el-select v-model="searchForm.status" placeholder="订单状态" style="width: 150px" clearable>
          <el-option label="待付定金" value="待付定金" />
          <el-option label="已付定金" value="已付定金" />
          <el-option label="拍摄中" value="拍摄中" />
          <el-option label="选片中" value="选片中" />
          <el-option label="修片中" value="修片中" />
          <el-option label="产品制作中" value="产品制作中" />
          <el-option label="已完成" value="已完成" />
          <el-option label="已取消" value="已取消" />
        </el-select>
        <el-button type="primary" @click="loadOrders">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <el-table :data="orderList" stripe border>
        <el-table-column prop="orderNo" label="订单编号" width="150" />
        <el-table-column prop="customerName" label="客户姓名" width="100" />
        <el-table-column prop="packageName" label="套餐名称" width="150" />
        <el-table-column prop="totalAmount" label="订单金额" width="100">
          <template #default="{ row }">
            <span style="color: #F56C6C; font-weight: bold">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="100">
          <template #default="{ row }">
            ¥{{ row.paidAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="currentStage" label="进度" width="200">
          <template #default="{ row }">
            <el-progress
              :percentage="getProgressPercentage(row.currentStage)"
              :format="() => getStageName(row.currentStage)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="channelSource" label="来源" width="100" />
        <el-table-column prop="photographer" label="摄影师" width="100" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">详情</el-button>
            <el-button type="success" link size="small" @click="updateStage(row)">更新进度</el-button>
            <el-button type="warning" link size="small" @click="assignEmployee(row)">分配人员</el-button>
            <el-button type="danger" link size="small" @click="deleteOrder(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadOrders"
        @current-change="loadOrders"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="createDialogVisible" title="新建订单" width="700px">
      <el-form :model="orderForm" label-width="100px">
        <el-form-item label="选择客户">
          <el-select v-model="orderForm.customerId" placeholder="请选择客户" style="width: 100%">
            <el-option
              v-for="customer in customerList"
              :key="customer.id"
              :label="customer.name"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择套餐">
          <el-select v-model="orderForm.packageId" placeholder="请选择套餐" style="width: 100%" @change="calculateTotal">
            <el-option
              v-for="pkg in packageList"
              :key="pkg.id"
              :label="`${pkg.name} - ¥${pkg.price}`"
              :value="pkg.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="加购项目">
          <el-checkbox-group v-model="orderForm.addOnItemIds">
            <el-checkbox
              v-for="item in addOnItemList"
              :key="item.id"
              :label="item.id"
            >
              {{ item.name }} - ¥{{ item.price }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="订单来源">
          <el-select v-model="orderForm.channelSource" placeholder="请选择订单来源" style="width: 100%">
            <el-option label="美团" value="美团" />
            <el-option label="抖音" value="抖音" />
            <el-option label="小程序" value="小程序" />
            <el-option label="门店" value="门店" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="拍摄地点">
          <el-input v-model="orderForm.shootingLocation" placeholder="请输入拍摄地点" />
        </el-form-item>
        <el-form-item label="预计金额">
          <span style="color: #F56C6C; font-size: 18px; font-weight: bold">¥{{ calculatedTotal }}</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="stageDialogVisible" title="更新订单进度" width="500px">
      <el-form :model="stageForm" label-width="100px">
        <el-form-item label="当前订单">
          <span>{{ stageOrder?.orderNo }}</span>
        </el-form-item>
        <el-form-item label="目标阶段">
          <el-select v-model="stageForm.stage" style="width: 100%">
            <el-option :label="getStageName(0)" :value="0" />
            <el-option :label="getStageName(1)" :value="1" />
            <el-option :label="getStageName(2)" :value="2" />
            <el-option :label="getStageName(3)" :value="3" />
            <el-option :label="getStageName(4)" :value="4" />
            <el-option :label="getStageName(5)" :value="5" />
            <el-option :label="getStageName(6)" :value="6" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stageDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStage">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="分配工作人员" width="500px">
      <el-form :model="assignForm" label-width="100px">
        <el-form-item label="摄影师">
          <el-select v-model="assignForm.photographer" style="width: 100%" placeholder="请选择摄影师">
            <el-option
              v-for="emp in photographerList"
              :key="emp.name"
              :label="emp.name"
              :value="emp.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="化妆师">
          <el-select v-model="assignForm.makeupArtist" style="width: 100%" placeholder="请选择化妆师">
            <el-option
              v-for="emp in makeupArtistList"
              :key="emp.name"
              :label="emp.name"
              :value="emp.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="修图师">
          <el-select v-model="assignForm.retoucher" style="width: 100%" placeholder="请选择修图师">
            <el-option
              v-for="emp in retoucherList"
              :key="emp.name"
              :label="emp.name"
              :value="emp.name"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const orderList = ref([])
const customerList = ref([])
const packageList = ref([])
const addOnItemList = ref([])
const employeeList = ref([])
const createDialogVisible = ref(false)
const stageDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const stageOrder = ref(null)

const searchForm = reactive({
  orderNo: '',
  customerName: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const orderForm = reactive({
  customerId: null,
  packageId: null,
  addOnItemIds: [],
  channelSource: '门店',
  shootingLocation: ''
})

const stageForm = reactive({
  orderId: null,
  stage: 0
})

const assignForm = reactive({
  orderId: null,
  photographer: '',
  makeupArtist: '',
  retoucher: ''
})

const calculatedTotal = computed(() => {
  let total = 0
  if (orderForm.packageId) {
    const pkg = packageList.value.find(p => p.id === orderForm.packageId)
    if (pkg) total += parseFloat(pkg.price)
  }
  orderForm.addOnItemIds.forEach(id => {
    const item = addOnItemList.value.find(i => i.id === id)
    if (item) total += parseFloat(item.price)
  })
  return total.toFixed(2)
})

const photographerList = computed(() => 
  employeeList.value.filter(e => e.position === '摄影师')
)

const makeupArtistList = computed(() => 
  employeeList.value.filter(e => e.position === '化妆师')
)

const retoucherList = computed(() => 
  employeeList.value.filter(e => e.position === '修图师')
)

const loadOrders = async () => {
  try {
    const res = await request.get('/orders/page', {
      params: { page: pagination.page - 1, size: pagination.size }
    })
    orderList.value = res.data.content || []
    pagination.total = res.data.totalElements || 0
  } catch (error) {
    console.error('加载订单失败', error)
  }
}

const loadCustomers = async () => {
  try {
    const res = await request.get('/customers')
    customerList.value = res.data || []
  } catch (error) {
    console.error('加载客户失败', error)
  }
}

const loadPackages = async () => {
  try {
    const res = await request.get('/packages')
    packageList.value = res.data || []
  } catch (error) {
    console.error('加载套餐失败', error)
  }
}

const loadAddOnItems = async () => {
  try {
    const res = await request.get('/addons')
    addOnItemList.value = res.data || []
  } catch (error) {
    console.error('加载加购项失败', error)
  }
}

const loadEmployees = async () => {
  try {
    const res = await request.get('/employees')
    employeeList.value = res.data || []
  } catch (error) {
    console.error('加载员工失败', error)
  }
}

const resetSearch = () => {
  searchForm.orderNo = ''
  searchForm.customerName = ''
  searchForm.status = ''
  loadOrders()
}

const getStatusType = (status) => {
  const typeMap = {
    '待付定金': 'warning',
    '已付定金': 'info',
    '拍摄中': 'primary',
    '选片中': 'primary',
    '修片中': 'primary',
    '产品制作中': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

const getStageName = (stage) => {
  const stageMap = {
    0: '待付定金',
    1: '已付定金',
    2: '拍摄中',
    3: '选片中',
    4: '修片中',
    5: '产品制作中',
    6: '已完成'
  }
  return stageMap[stage] || '未知'
}

const getProgressPercentage = (stage) => {
  const stages = [0, 1, 2, 3, 4, 5, 6]
  const index = stages.indexOf(stage)
  return index >= 0 ? Math.round((index / (stages.length - 1)) * 100) : 0
}

const calculateTotal = () => {
  // Trigger computed recalculation
}

const showCreateDialog = () => {
  orderForm.customerId = null
  orderForm.packageId = null
  orderForm.addOnItemIds = []
  orderForm.channelSource = '门店'
  orderForm.shootingLocation = ''
  createDialogVisible.value = true
}

const submitOrder = async () => {
  if (!orderForm.customerId) {
    ElMessage.warning('请选择客户')
    return
  }
  if (!orderForm.packageId) {
    ElMessage.warning('请选择套餐')
    return
  }
  try {
    await request.post('/orders', null, {
      params: {
        customerId: orderForm.customerId,
        packageId: orderForm.packageId,
        addOnItemIds: orderForm.addOnItemIds.join(','),
        channelSource: orderForm.channelSource,
        shootingLocation: orderForm.shootingLocation
      }
    })
    ElMessage.success('订单创建成功')
    createDialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('创建订单失败', error)
  }
}

const viewDetail = (row) => {
  ElMessage.info('查看详情功能开发中')
}

const updateStage = (row) => {
  stageOrder.value = row
  stageForm.orderId = row.id
  stageForm.stage = row.currentStage
  stageDialogVisible.value = true
}

const submitStage = async () => {
  try {
    await request.put(`/orders/${stageForm.orderId}/stage/${stageForm.stage}`, null, {
      params: { operator: '管理员' }
    })
    ElMessage.success('进度更新成功')
    stageDialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('更新进度失败', error)
  }
}

const assignEmployee = (row) => {
  assignForm.orderId = row.id
  assignForm.photographer = row.photographer || ''
  assignForm.makeupArtist = row.makeupArtist || ''
  assignForm.retoucher = row.retoucher || ''
  assignDialogVisible.value = true
}

const submitAssign = async () => {
  try {
    await request.put(`/orders/${assignForm.orderId}/assign`, null, {
      params: {
        photographer: assignForm.photographer,
        makeupArtist: assignForm.makeupArtist,
        retoucher: assignForm.retoucher
      }
    })
    ElMessage.success('人员分配成功')
    assignDialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('分配人员失败', error)
  }
}

const deleteOrder = (row) => {
  ElMessageBox.confirm('确定要删除该订单吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/orders/${row.id}`)
      ElMessage.success('删除成功')
      loadOrders()
    } catch (error) {
      console.error('删除订单失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadOrders()
  loadCustomers()
  loadPackages()
  loadAddOnItems()
  loadEmployees()
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
