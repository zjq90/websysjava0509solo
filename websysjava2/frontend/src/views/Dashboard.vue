<template>
  <div>
    <div class="page-header">
      <h2>系统概览</h2>
    </div>

    <div class="dashboard-stats">
      <div class="stat-card">
        <el-icon :size="28"><Goods /></el-icon>
        <div class="stat-value">{{ stats.productCount }}</div>
        <div class="stat-label">商品总数</div>
      </div>
      <div class="stat-card">
        <el-icon :size="28"><Monitor /></el-icon>
        <div class="stat-value">{{ stats.machineCount }}</div>
        <div class="stat-label">设备总数</div>
      </div>
      <div class="stat-card">
        <el-icon :size="28"><Document /></el-icon>
        <div class="stat-value">{{ stats.orderCount }}</div>
        <div class="stat-label">今日订单</div>
      </div>
      <div class="stat-card">
        <el-icon :size="28"><Warning /></el-icon>
        <div class="stat-value">{{ stats.pendingRestock }}</div>
        <div class="stat-label">待补货</div>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex-between">
              <span style="font-weight: bold;">设备状态</span>
              <el-tag type="info">实时</el-tag>
            </div>
          </template>
          <el-table :data="machines" style="width: 100%">
            <el-table-column prop="machineCode" label="设备编号" width="120" />
            <el-table-column prop="name" label="设备名称" />
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="location" label="位置" width="180" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="flex-between">
              <span style="font-weight: bold;">低库存告警</span>
              <el-button type="warning" size="small" link @click="loadLowStock">
                刷新
              </el-button>
            </div>
          </template>
          <el-table :data="lowStockSlots" style="width: 100%" max-height="300">
            <el-table-column label="设备" width="140">
              <template #default="scope">
                {{ scope.row.machine?.name || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="slotNumber" label="货道" width="80" />
            <el-table-column label="商品">
              <template #default="scope">
                {{ scope.row.product?.name || '未绑定' }}
              </template>
            </el-table-column>
            <el-table-column label="库存" width="120">
              <template #default="scope">
                <el-progress 
                  :percentage="getStockPercent(scope.row)" 
                  :color="getStockColor(scope.row)"
                  :stroke-width="12"
                />
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="lowStockSlots.length === 0" description="暂无低库存商品" />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover" class="mt-20">
      <template #header>
        <div class="flex-between">
          <span style="font-weight: bold;">最新订单</span>
          <el-button type="primary" size="small" link @click="$router.push('/orders')">
            查看全部
          </el-button>
        </div>
      </template>
      <el-table :data="recentOrders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="userName" label="用户" width="100" />
        <el-table-column label="设备" width="140">
          <template #default="scope">
            {{ scope.row.machine?.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="金额" width="100">
          <template #default="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column label="支付状态" width="100">
          <template #default="scope">
            <el-tag :type="getPaymentStatusType(scope.row.paymentStatus)">
              {{ getPaymentStatusText(scope.row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="取货状态" width="100">
          <template #default="scope">
            <el-tag :type="getPickupStatusType(scope.row.pickupStatus)">
              {{ getPickupStatusText(scope.row.pickupStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMachines, getLowStockSlots } from '../api/machine'
import { getProducts } from '../api/product'
import { getOrders } from '../api/order'
import { getPendingCount } from '../api/restock'

const stats = reactive({
  productCount: 0,
  machineCount: 0,
  orderCount: 0,
  pendingRestock: 0
})

const machines = ref([])
const lowStockSlots = ref([])
const recentOrders = ref([])

const getStatusType = (status) => {
  const types = { ONLINE: 'success', OFFLINE: 'danger', MAINTENANCE: 'warning' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { ONLINE: '在线', OFFLINE: '离线', MAINTENANCE: '维护中' }
  return texts[status] || status
}

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

const getStockPercent = (slot) => {
  if (!slot.maxCapacity) return 0
  return Math.round((slot.currentStock / slot.maxCapacity) * 100)
}

const getStockColor = (slot) => {
  const percent = getStockPercent(slot)
  if (percent <= 20) return '#f56c6c'
  if (percent <= 50) return '#e6a23c'
  return '#67c23a'
}

const loadLowStock = async () => {
  try {
    lowStockSlots.value = await getLowStockSlots()
  } catch (e) {
    console.error(e)
  }
}

const loadData = async () => {
  try {
    const [products, machineList, orders, pending] = await Promise.all([
      getProducts(),
      getMachines(),
      getOrders(0, 5),
      getPendingCount()
    ])
    
    stats.productCount = products.length
    stats.machineCount = machineList.length
    stats.orderCount = orders.totalElements || 0
    stats.pendingRestock = pending || 0
    
    machines.value = machineList
    recentOrders.value = orders.content || []
    
    await loadLowStock()
  } catch (e) {
    ElMessage.error('加载数据失败')
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>
