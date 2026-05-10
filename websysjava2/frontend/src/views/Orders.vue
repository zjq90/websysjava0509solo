<template>
  <div class="page-container">
    <div class="page-header">
      <h2>订单列表</h2>
      <div class="filter-group">
        <el-select
          v-model="filterPayment"
          placeholder="支付状态"
          clearable
          style="width: 140px; margin-right: 10px;"
          @change="handleFilterChange"
        >
          <el-option label="待支付" value="PENDING" />
          <el-option label="已支付" value="PAID" />
          <el-option label="已退款" value="REFUND" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-select
          v-model="filterPickup"
          placeholder="取货状态"
          clearable
          style="width: 140px; margin-right: 10px;"
          @change="handleFilterChange"
        >
          <el-option label="待取货" value="PENDING" />
          <el-option label="已取货" value="PICKED" />
          <el-option label="取货失败" value="FAILED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          查询
        </el-button>
        <el-button @click="handleReset">
          重置
        </el-button>
      </div>
    </div>

    <div class="table-container">
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column label="用户信息" width="180">
          <template #default="scope">
            <div>
              <div>{{ scope.row.userName || '-' }}</div>
              <div style="font-size: 12px; color: #909399;">
                {{ scope.row.userPhone || '-' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="设备" width="150">
          <template #default="scope">
            {{ scope.row.machine?.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="商品" min-width="180">
          <template #default="scope">
            <div v-for="(item, idx) in scope.row.items" :key="idx">
              {{ item.productName }} x{{ item.quantity }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="100">
          <template #default="scope">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ scope.row.totalAmount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="支付状态" width="100">
          <template #default="scope">
            <el-tag :type="getPaymentStatusType(scope.row.paymentStatus)" size="small">
              {{ getPaymentStatusText(scope.row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="取货状态" width="100">
          <template #default="scope">
            <el-tag :type="getPickupStatusType(scope.row.pickupStatus)" size="small">
              {{ getPickupStatusText(scope.row.pickupStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="showDetail(scope.row)">
              详情
            </el-button>
            <el-button
              v-if="scope.row.paymentStatus === 'PENDING'"
              type="success"
              size="small"
              link
              @click="handlePay(scope.row)"
            >
              模拟支付
            </el-button>
            <el-button
              v-if="scope.row.paymentStatus === 'PAID' && scope.row.pickupStatus === 'PENDING'"
              type="warning"
              size="small"
              link
              @click="handlePickup(scope.row)"
            >
              确认取货
            </el-button>
            <el-button
              v-if="scope.row.paymentStatus === 'PENDING'"
              type="danger"
              size="small"
              link
              @click="handleCancel(scope.row)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-wrapper">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="600px"
    >
      <el-descriptions v-if="currentOrder" border column="1">
        <el-descriptions-item label="订单号">
          {{ currentOrder.orderNo }}
        </el-descriptions-item>
        <el-descriptions-item label="用户">
          {{ currentOrder.userName || '-' }} ({{ currentOrder.userPhone || '-' }})
        </el-descriptions-item>
        <el-descriptions-item label="设备">
          {{ currentOrder.machine?.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="总金额">
          <span style="color: #f56c6c; font-weight: bold;">¥{{ currentOrder.totalAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="getPaymentStatusType(currentOrder.paymentStatus)">
            {{ getPaymentStatusText(currentOrder.paymentStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ getPaymentMethodText(currentOrder.paymentMethod) }}
        </el-descriptions-item>
        <el-descriptions-item label="取货状态">
          <el-tag :type="getPickupStatusType(currentOrder.pickupStatus)">
            {{ getPickupStatusText(currentOrder.pickupStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下单时间">
          {{ currentOrder.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="支付时间">
          {{ currentOrder.paymentTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="取货时间">
          {{ currentOrder.pickupTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="商品明细">
          <el-table :data="currentOrder.items" size="small" style="width: 100%;">
            <el-table-column prop="productName" label="商品" />
            <el-table-column label="单价">
              <template #default="scope">¥{{ scope.row.price }}</template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column label="小计">
              <template #default="scope">¥{{ scope.row.subtotal }}</template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrders, payOrder, confirmPickup, cancelOrder } from '../api/order'

const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterPayment = ref(null)
const filterPickup = ref(null)

const detailVisible = ref(false)
const currentOrder = ref(null)

const getPaymentStatusType = (status) => {
  const types = { PAID: 'success', PENDING: 'warning', REFUND: 'info', CANCELLED: 'danger' }
  return types[status] || 'info'
}

const getPaymentStatusText = (status) => {
  const texts = { PAID: '已支付', PENDING: '待支付', REFUND: '已退款', CANCELLED: '已取消' }
  return texts[status] || status
}

const getPickupStatusType = (status) => {
  const types = { PICKED: 'success', PENDING: 'warning', FAILED: 'danger', CANCELLED: 'info' }
  return types[status] || 'info'
}

const getPickupStatusText = (status) => {
  const texts = { PICKED: '已取货', PENDING: '待取货', FAILED: '取货失败', CANCELLED: '已取消' }
  return texts[status] || status
}

const getPaymentMethodText = (method) => {
  const texts = { WECHAT: '微信支付', ALIPAY: '支付宝', CASH: '现金' }
  return texts[method] || method || '-'
}

const loadOrders = async () => {
  try {
    const result = await getOrders(
      currentPage.value - 1, 
      pageSize.value, 
      filterPayment.value, 
      filterPickup.value
    )
    orders.value = result.content || []
    total.value = result.totalElements || 0
  } catch (e) {
    console.error(e)
  }
}

const handleFilterChange = () => {
  currentPage.value = 1
  loadOrders()
}

const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

const handleReset = () => {
  filterPayment.value = null
  filterPickup.value = null
  currentPage.value = 1
  loadOrders()
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadOrders()
}

const showDetail = (order) => {
  currentOrder.value = order
  detailVisible.value = true
}

const handlePay = async (order) => {
  ElMessageBox.confirm(`确定要模拟支付订单 "${order.orderNo}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      await payOrder(order.id, 'WECHAT')
      ElMessage.success('支付成功')
      loadOrders()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const handlePickup = async (order) => {
  ElMessageBox.confirm(`确定要确认取货吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await confirmPickup(order.id)
      ElMessage.success('取货确认成功')
      loadOrders()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const handleCancel = async (order) => {
  ElMessageBox.confirm(`确定要取消订单 "${order.orderNo}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelOrder(order.id)
      ElMessage.success('取消成功')
      loadOrders()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.filter-group {
  display: flex;
  align-items: center;
}

.table-container {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>
