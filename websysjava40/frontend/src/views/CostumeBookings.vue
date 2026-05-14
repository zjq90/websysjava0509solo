<template>
  <div class="costume-bookings-page">
    <div class="page-header">
      <el-select
        v-model="filterStatus"
        placeholder="筛选状态"
        class="filter-select"
        @change="loadBookings"
      >
        <el-option label="全部" value="" />
        <el-option label="已预约" value="BOOKED" />
        <el-option label="已领取" value="PICKED_UP" />
        <el-option label="已归还" value="RETURNED" />
        <el-option label="已取消" value="CANCELLED" />
      </el-select>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        预约服装
      </el-button>
    </div>

    <el-card>
      <el-table :data="bookings" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="costume.name" label="服装名称" width="150" />
        <el-table-column prop="costume.type" label="服装类型" width="120" :formatter="formatType" />
        <el-table-column prop="customerName" label="客户名称" width="120" />
        <el-table-column prop="bookingDate" label="预约日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusDesc(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pickupTime" label="领取时间" width="150" />
        <el-table-column prop="returnTime" label="归还时间" width="150" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === 'BOOKED'"
              size="mini"
              type="success"
              @click="handlePickup(scope.row)"
            >领取</el-button>
            <el-button
              v-if="scope.row.status === 'PICKED_UP'"
              size="mini"
              type="primary"
              @click="handleReturn(scope.row)"
            >归还</el-button>
            <el-button
              v-if="scope.row.status === 'BOOKED'"
              size="mini"
              type="danger"
              @click="handleCancel(scope.row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      title="预约服装"
      :visible.sync="showAddModal"
      width="450px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="服装" prop="costumeId">
          <el-select v-model="formData.costumeId" placeholder="请选择服装">
            <el-option
              v-for="costume in availableCostumes"
              :key="costume.id"
              :label="costume.name + ' (' + getTypeDesc(costume.type) + ')'"
              :value="costume.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="预约日期" prop="bookingDate">
          <el-date-picker v-model="formData.bookingDate" type="date" />
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
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { costumeBookingApi } from '../api/costumeBooking'
import { costumeApi } from '../api/costume'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const bookings = ref([])
const filterStatus = ref('')
const showAddModal = ref(false)
const costumes = ref([])

const formData = ref({
  costumeId: null,
  customerName: '',
  bookingDate: new Date().toISOString().split('T')[0],
  remark: ''
})

const availableCostumes = computed(() => costumes.value.filter(c => c.status === 'AVAILABLE' && c.cleaningStatus === 'CLEAN'))

const loadBookings = async () => {
  try {
    if (filterStatus.value) {
      bookings.value = await costumeBookingApi.getByStatus(filterStatus.value)
    } else {
      bookings.value = await costumeBookingApi.getAll()
    }
  } catch (error) {
    ElMessage.error('加载服装预约失败')
  }
}

const loadCostumes = async () => {
  try {
    costumes.value = await costumeApi.getAll()
  } catch (error) {
    ElMessage.error('加载服装列表失败')
  }
}

const formatType = (row, column) => {
  const types = {
    WEDDING_DRESS: '婚纱',
    FORMAL: '礼服',
    THEME: '主题服装',
    ACCESSORY: '配饰'
  }
  return types[row] || row
}

const getTypeDesc = (type) => {
  const types = {
    WEDDING_DRESS: '婚纱',
    FORMAL: '礼服',
    THEME: '主题服装',
    ACCESSORY: '配饰'
  }
  return types[type] || type
}

const getStatusType = (status) => {
  switch (status) {
    case 'BOOKED':
      return 'warning'
    case 'PICKED_UP':
      return 'primary'
    case 'RETURNED':
      return 'success'
    case 'CANCELLED':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    BOOKED: '已预约',
    PICKED_UP: '已领取',
    RETURNED: '已归还',
    CANCELLED: '已取消'
  }
  return statusMap[status] || status
}

const handlePickup = async (booking) => {
  try {
    await costumeBookingApi.update(booking.id, { status: 'PICKED_UP', pickupTime: new Date().toISOString() })
    ElMessage.success('已领取')
    loadBookings()
    loadCostumes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleReturn = async (booking) => {
  try {
    await costumeBookingApi.returnCostume(booking.id)
    ElMessage.success('已归还')
    loadBookings()
    loadCostumes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancel = async (booking) => {
  try {
    await costumeBookingApi.cancel(booking.id)
    ElMessage.success('已取消')
    loadBookings()
    loadCostumes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    await costumeBookingApi.create({
      costume: { id: formData.value.costumeId },
      customerName: formData.value.customerName,
      bookingDate: formData.value.bookingDate,
      remark: formData.value.remark
    })
    ElMessage.success('预约成功')
    showAddModal.value = false
    loadBookings()
    loadCostumes()
  } catch (error) {
    ElMessage.error('预约失败')
  }
}

onMounted(() => {
  loadBookings()
  loadCostumes()
})
</script>

<style scoped>
.costume-bookings-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-select {
  width: 150px;
}
</style>
