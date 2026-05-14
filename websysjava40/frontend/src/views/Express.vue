<template>
  <div class="express-page">
    <div class="page-header">
      <div class="search-area">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索运单号/订单号"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="loadExpress"
        />
        <el-select
          v-model="filterStatus"
          placeholder="筛选状态"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="已创建" value="CREATED" />
          <el-option label="已发货" value="SHIPPED" />
          <el-option label="配送中" value="IN_TRANSIT" />
          <el-option label="派送中" value="OUT_FOR_DELIVERY" />
          <el-option label="已签收" value="SIGNED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-button type="primary" @click="loadExpress">搜索</el-button>
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        创建快递单
      </el-button>
    </div>

    <el-card>
      <el-table :data="expressList" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="trackingNo" label="运单号" width="160" />
        <el-table-column prop="logisticsCompany" label="物流公司" width="120" :formatter="formatCompany" />
        <el-table-column prop="order.orderNo" label="关联订单" width="150" />
        <el-table-column prop="receiverName" label="收件人" width="100" />
        <el-table-column prop="receiverPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusDesc(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看详情</el-button>
            <el-button
              v-if="scope.row.status === 'CREATED'"
              size="mini"
              type="success"
              @click="handleShip(scope.row)"
            >发货</el-button>
            <el-button
              v-if="scope.row.status !== 'SIGNED' && scope.row.status !== 'CANCELLED'"
              size="mini"
              type="primary"
              @click="handleTrack(scope.row)"
            >跟踪物流</el-button>
            <el-button
              v-if="scope.row.status !== 'SIGNED'"
              size="mini"
              type="danger"
              @click="handleCancel(scope.row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      title="创建快递单"
      :visible.sync="showAddModal"
      width="500px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="关联订单" prop="orderId">
          <el-select v-model="formData.orderId" placeholder="请选择订单">
            <el-option
              v-for="order in availableOrders"
              :key="order.id"
              :label="order.orderNo + ' - ' + order.customerName"
              :value="order.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="物流公司" prop="logisticsCompany">
          <el-select v-model="formData.logisticsCompany" placeholder="请选择物流公司">
            <el-option label="顺丰速运" value="SF_EXPRESS" />
            <el-option label="京东物流" value="JD_LOGISTICS" />
            <el-option label="中通快递" value="ZTO" />
            <el-option label="圆通速递" value="YTO" />
            <el-option label="申通快递" value="STO" />
          </el-select>
        </el-form-item>
        <el-form-item label="收件人" prop="receiverName">
          <el-input v-model="formData.receiverName" placeholder="请输入收件人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input v-model="formData.receiverPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="收件地址" prop="receiverAddress">
          <el-textarea v-model="formData.receiverAddress" placeholder="请输入收件地址" rows="2" />
        </el-form-item>
        <el-form-item label="物品描述" prop="items">
          <el-input v-model="formData.items" placeholder="请输入物品描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      title="物流详情"
      :visible.sync="showDetailModal"
      width="500px"
    >
      <div v-if="selectedExpress">
        <div class="detail-info">
          <div class="info-row">
            <span class="label">运单号:</span>
            <span class="value">{{ selectedExpress.trackingNo }}</span>
          </div>
          <div class="info-row">
            <span class="label">物流公司:</span>
            <span class="value">{{ formatCompany(selectedExpress.logisticsCompany) }}</span>
          </div>
          <div class="info-row">
            <span class="label">收件人:</span>
            <span class="value">{{ selectedExpress.receiverName }}</span>
          </div>
          <div class="info-row">
            <span class="label">联系电话:</span>
            <span class="value">{{ selectedExpress.receiverPhone }}</span>
          </div>
          <div class="info-row">
            <span class="label">收件地址:</span>
            <span class="value">{{ selectedExpress.receiverAddress }}</span>
          </div>
          <div class="info-row">
            <span class="label">当前状态:</span>
            <el-tag :type="getStatusType(selectedExpress.status)">
              {{ getStatusDesc(selectedExpress.status) }}
            </el-tag>
          </div>
        </div>
        <div class="tracking-section">
          <h4>物流轨迹</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(track, index) in trackingHistory"
              :key="index"
              :timestamp="track.time"
            >
              <div>{{ track.description }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { expressApi } from '../api/express'
import { orderApi } from '../api/order'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const expressList = ref([])
const searchKeyword = ref('')
const filterStatus = ref('')
const showAddModal = ref(false)
const showDetailModal = ref(false)
const orders = ref([])

const selectedExpress = ref(null)
const trackingHistory = ref([])

const formData = ref({
  orderId: null,
  logisticsCompany: 'SF_EXPRESS',
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  items: ''
})

const availableOrders = computed(() => orders.value.filter(o => o.status === 'COMPLETED' || o.status === 'DELIVERING'))

const loadExpress = async () => {
  try {
    if (searchKeyword.value) {
      expressList.value = await expressApi.getByTrackingNo(searchKeyword.value)
    } else if (filterStatus.value) {
      expressList.value = await expressApi.getByStatus(filterStatus.value)
    } else {
      expressList.value = await expressApi.getAll()
    }
  } catch (error) {
    ElMessage.error('加载快递列表失败')
  }
}

const loadOrders = async () => {
  try {
    orders.value = await orderApi.getAll()
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  }
}

const formatCompany = (company) => {
  const companies = {
    SF_EXPRESS: '顺丰速运',
    JD_LOGISTICS: '京东物流',
    ZTO: '中通快递',
    YTO: '圆通速递',
    STO: '申通快递'
  }
  return companies[company] || company
}

const getStatusType = (status) => {
  switch (status) {
    case 'CREATED':
      return 'info'
    case 'SHIPPED':
      return 'primary'
    case 'IN_TRANSIT':
      return 'warning'
    case 'OUT_FOR_DELIVERY':
      return 'primary'
    case 'SIGNED':
      return 'success'
    case 'CANCELLED':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    CREATED: '已创建',
    SHIPPED: '已发货',
    IN_TRANSIT: '配送中',
    OUT_FOR_DELIVERY: '派送中',
    SIGNED: '已签收',
    CANCELLED: '已取消'
  }
  return statusMap[status] || status
}

const handleView = async (express) => {
  selectedExpress.value = express
  trackingHistory.value = express.trackingInfo ? JSON.parse(express.trackingInfo) : []
  showDetailModal.value = true
}

const handleShip = async (express) => {
  try {
    await expressApi.updateStatus(express.id, 'SHIPPED', '快件已发出，正在前往目的地')
    ElMessage.success('已发货')
    loadExpress()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleTrack = async (express) => {
  try {
    await expressApi.simulateTracking(express.id)
    ElMessage.success('物流信息已更新')
    loadExpress()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancel = async (express) => {
  try {
    await expressApi.updateStatus(express.id, 'CANCELLED', '快递已取消')
    ElMessage.success('已取消')
    loadExpress()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    await expressApi.create({
      order: { id: formData.value.orderId },
      logisticsCompany: formData.value.logisticsCompany,
      receiverName: formData.value.receiverName,
      receiverPhone: formData.value.receiverPhone,
      receiverAddress: formData.value.receiverAddress,
      items: formData.value.items
    })
    ElMessage.success('创建成功')
    showAddModal.value = false
    loadExpress()
  } catch (error) {
    ElMessage.error('创建失败')
  }
}

onMounted(() => {
  loadExpress()
  loadOrders()
})
</script>

<style scoped>
.express-page {
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

.detail-info {
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  margin-bottom: 10px;
}

.label {
  width: 80px;
  color: #999;
}

.value {
  flex: 1;
}

.tracking-section {
  border-top: 1px solid #eee;
  padding-top: 15px;
}
</style>
