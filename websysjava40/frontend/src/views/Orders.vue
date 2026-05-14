<template>
  <div class="orders-page">
    <div class="page-header">
      <div class="search-area">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索订单号/客户名称"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="loadOrders"
        />
        <el-select
          v-model="filterStatus"
          placeholder="筛选状态"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="编辑中" value="EDITING" />
          <el-option label="已确认" value="CONFIRMED" />
          <el-option label="拍摄中" value="SHOOTING" />
          <el-option label="选片中" value="SELECTING" />
          <el-option label="制作中" value="PROCESSING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="配送中" value="DELIVERING" />
          <el-option label="已交付" value="DELIVERED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-button type="primary" @click="loadOrders">搜索</el-button>
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        创建订单
      </el-button>
    </div>

    <el-card>
      <el-table :data="orders" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="orderNo" label="订单号" width="150" />
        <el-table-column prop="customerName" label="客户名称" width="120" />
        <el-table-column prop="customerPhone" label="客户电话" width="130" />
        <el-table-column prop="packageType" label="套餐类型" width="120" />
        <el-table-column prop="amount" label="订单金额" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusDesc(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.status === 'PROCESSING'"
              size="mini"
              type="success"
              @click="handleComplete(scope.row)"
            >完成</el-button>
            <el-button
              v-if="scope.row.status === 'COMPLETED'"
              size="mini"
              type="primary"
              @click="handleDelivering(scope.row)"
            >发货</el-button>
            <el-button
              v-if="scope.row.status === 'DELIVERED'"
              size="mini"
              type="success"
              @click="handleEvaluate(scope.row)"
            >评价</el-button>
            <el-button
              v-if="scope.row.status !== 'DELIVERED' && scope.row.status !== 'CANCELLED'"
              size="mini"
              type="danger"
              @click="handleCancel(scope.row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      :title="isEdit ? '编辑订单' : '创建订单'"
      :visible.sync="showAddModal"
      width="500px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="客户电话" prop="customerPhone">
          <el-input v-model="formData.customerPhone" placeholder="请输入客户电话" />
        </el-form-item>
        <el-form-item label="客户邮箱" prop="customerEmail">
          <el-input v-model="formData.customerEmail" placeholder="请输入客户邮箱" />
        </el-form-item>
        <el-form-item label="套餐类型" prop="packageType">
          <el-select v-model="formData.packageType" placeholder="请选择套餐类型">
            <el-option label="简约套餐" value="SIMPLE" />
            <el-option label="经典套餐" value="CLASSIC" />
            <el-option label="豪华套餐" value="LUXURY" />
            <el-option label="定制套餐" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单金额" prop="amount">
          <el-input v-model="formData.amount" type="number" placeholder="请输入订单金额" />
        </el-form-item>
        <el-form-item label="拍摄日期" prop="shootingDate">
          <el-date-picker v-model="formData.shootingDate" type="date" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-textarea v-model="formData.remark" placeholder="请输入备注" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      title="满意度评价"
      :visible.sync="showEvaluateModal"
      width="400px"
    >
      <el-form :model="evaluateForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="evaluateForm.score" :max="5" show-text />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-textarea v-model="evaluateForm.comment" placeholder="请输入评价内容" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEvaluateModal = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluate">提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { orderApi } from '../api/order'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const orders = ref([])
const searchKeyword = ref('')
const filterStatus = ref('')
const showAddModal = ref(false)
const isEdit = ref(false)
const showEvaluateModal = ref(false)

const currentOrder = ref(null)

const formData = ref({
  id: null,
  orderNo: '',
  customerName: '',
  customerPhone: '',
  customerEmail: '',
  packageType: 'CLASSIC',
  amount: 0,
  shootingDate: '',
  remark: ''
})

const evaluateForm = ref({
  score: 5,
  comment: ''
})

const loadOrders = async () => {
  try {
    let data
    if (searchKeyword.value) {
      data = await orderApi.search(searchKeyword.value)
    } else if (filterStatus.value) {
      data = await orderApi.getByStatus(filterStatus.value)
    } else {
      data = await orderApi.getAll()
    }
    orders.value = data
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  }
}

const getStatusType = (status) => {
  switch (status) {
    case 'EDITING':
      return 'info'
    case 'CONFIRMED':
      return 'primary'
    case 'SHOOTING':
      return 'warning'
    case 'SELECTING':
      return 'warning'
    case 'PROCESSING':
      return 'warning'
    case 'COMPLETED':
      return 'success'
    case 'DELIVERING':
      return 'primary'
    case 'DELIVERED':
      return 'success'
    case 'CANCELLED':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    EDITING: '编辑中',
    CONFIRMED: '已确认',
    SHOOTING: '拍摄中',
    SELECTING: '选片中',
    PROCESSING: '制作中',
    COMPLETED: '已完成',
    DELIVERING: '配送中',
    DELIVERED: '已交付',
    CANCELLED: '已取消'
  }
  return statusMap[status] || status
}

const handleEdit = (order) => {
  isEdit.value = true
  formData.value = {
    id: order.id,
    orderNo: order.orderNo,
    customerName: order.customerName,
    customerPhone: order.customerPhone,
    customerEmail: order.customerEmail,
    packageType: order.packageType,
    amount: order.amount,
    shootingDate: order.shootingDate,
    remark: order.remark
  }
  showAddModal.value = true
}

const handleComplete = async (order) => {
  try {
    await orderApi.complete(order.id)
    ElMessage.success('订单已完成')
    loadOrders()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelivering = async (order) => {
  try {
    await orderApi.delivering(order.id)
    ElMessage.success('已发货')
    loadOrders()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancel = async (order) => {
  try {
    await orderApi.cancel(order.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleEvaluate = (order) => {
  currentOrder.value = order
  evaluateForm.value = {
    score: 5,
    comment: ''
  }
  showEvaluateModal.value = true
}

const submitEvaluate = async () => {
  try {
    await orderApi.update(currentOrder.value.id, {
      satisfactionScore: evaluateForm.value.score,
      satisfactionComment: evaluateForm.value.comment
    })
    ElMessage.success('评价提交成功')
    showEvaluateModal.value = false
    loadOrders()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await orderApi.update(formData.value.id, formData.value)
      ElMessage.success('修改成功')
    } else {
      await orderApi.create(formData.value)
      ElMessage.success('创建成功')
    }
    showAddModal.value = false
    loadOrders()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '创建失败')
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-area {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 250px;
}

.filter-select {
  width: 150px;
}
</style>
