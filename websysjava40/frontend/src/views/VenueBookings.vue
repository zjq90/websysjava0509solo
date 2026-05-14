<template>
  <div class="venue-bookings-page">
    <div class="page-header">
      <div class="date-selector">
        <span>选择日期:</span>
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
          @change="loadBookings"
        />
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        预约场地
      </el-button>
    </div>

    <div class="booking-container">
      <div class="venues-list">
        <div
          v-for="venue in venues"
          :key="venue.id"
          class="venue-item"
          :class="{ active: selectedVenue?.id === venue.id, disabled: venue.status === 'MAINTENANCE' }"
          @click="venue.status !== 'MAINTENANCE' && selectVenue(venue)"
        >
          <div class="venue-name">{{ venue.name }}</div>
          <div class="venue-status">
            <el-tag :type="getStatusType(venue.status)">
              {{ getStatusDesc(venue.status) }}
            </el-tag>
          </div>
        </div>
      </div>

      <div class="booking-content">
        <div class="time-slots">
          <div
            v-for="timeSlot in timeSlots"
            :key="timeSlot.time"
            class="time-slot"
          >
            <div class="time-label">{{ timeSlot.time }}</div>
            <div
              class="slot-content"
              :class="{ hasBooking: hasBookingAt(timeSlot.time), unavailable: !isVenueAvailable(timeSlot.time) }"
              @click="isVenueAvailable(timeSlot.time) && handleSlotClick(timeSlot)"
            >
              <div
                v-for="booking in getBookingsAt(timeSlot.time)"
                :key="booking.id"
                class="booking-block"
                :class="getBookingClass(booking)"
              >
                <div class="booking-customer">{{ booking.customerName }}</div>
                <div class="booking-purpose">{{ booking.purpose }}</div>
                <div class="booking-actions">
                  <el-button size="mini" @click.stop="handleEditBooking(booking)">编辑</el-button>
                  <el-button size="mini" type="danger" @click.stop="handleCancelBooking(booking)" v-if="booking.status === 'BOOKED'">取消</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
      :title="isEdit ? '编辑预约' : '预约场地'"
      :visible.sync="showAddModal"
      width="450px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="场地" prop="venueId">
          <el-select v-model="formData.venueId" placeholder="请选择场地">
            <el-option
              v-for="venue in availableVenues"
              :key="venue.id"
              :label="venue.name"
              :value="venue.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="bookingDate">
          <el-date-picker v-model="formData.bookingDate" type="date" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker v-model="formData.startTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker v-model="formData.endTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="formData.purpose" placeholder="请输入用途" />
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
import { venueApi } from '../api/venue'
import { venueBookingApi } from '../api/venueBooking'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const venues = ref([])
const selectedDate = ref(new Date().toISOString().split('T')[0])
const selectedVenue = ref(null)
const showAddModal = ref(false)
const isEdit = ref(false)

const timeSlots = [
  { time: '08:00', start: '08:00', end: '09:00' },
  { time: '09:00', start: '09:00', end: '10:00' },
  { time: '10:00', start: '10:00', end: '11:00' },
  { time: '11:00', start: '11:00', end: '12:00' },
  { time: '12:00', start: '12:00', end: '13:00' },
  { time: '13:00', start: '13:00', end: '14:00' },
  { time: '14:00', start: '14:00', end: '15:00' },
  { time: '15:00', start: '15:00', end: '16:00' },
  { time: '16:00', start: '16:00', end: '17:00' },
  { time: '17:00', start: '17:00', end: '18:00' },
  { time: '18:00', start: '18:00', end: '19:00' },
  { time: '19:00', start: '19:00', end: '20:00' }
]

const formData = ref({
  id: null,
  venueId: null,
  bookingDate: new Date().toISOString().split('T')[0],
  startTime: '09:00',
  endTime: '10:00',
  customerName: '',
  purpose: '',
  remark: ''
})

const bookings = ref([])

const availableVenues = computed(() => venues.value.filter(v => v.status !== 'MAINTENANCE'))

const loadVenues = async () => {
  try {
    venues.value = await venueApi.getAll()
    if (availableVenues.value.length > 0) {
      selectVenue(availableVenues.value[0])
    }
  } catch (error) {
    ElMessage.error('加载场地列表失败')
  }
}

const loadBookings = async () => {
  if (!selectedVenue.value) return
  try {
    bookings.value = await venueBookingApi.getVenueByDate(selectedVenue.value.id, selectedDate.value)
  } catch (error) {
    ElMessage.error('加载预约失败')
  }
}

const selectVenue = (venue) => {
  selectedVenue.value = venue
  loadBookings()
}

const getStatusType = (status) => {
  switch (status) {
    case 'AVAILABLE':
      return 'success'
    case 'OCCUPIED':
      return 'warning'
    case 'MAINTENANCE':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    AVAILABLE: '可预约',
    OCCUPIED: '使用中',
    MAINTENANCE: '维护中'
  }
  return statusMap[status] || status
}

const hasBookingAt = (time) => {
  return bookings.value.some(b => {
    return b.startTime <= time.start && b.endTime > time.start
  })
}

const getBookingsAt = (time) => {
  return bookings.value.filter(b => {
    return b.startTime <= time.start && b.endTime > time.start
  })
}

const isVenueAvailable = (time) => {
  return selectedVenue.value?.status !== 'MAINTENANCE' && !hasBookingAt(time)
}

const getBookingClass = (booking) => {
  switch (booking.status) {
    case 'COMPLETED':
      return 'completed'
    case 'CANCELLED':
      return 'cancelled'
    default:
      return 'booked'
  }
}

const handleSlotClick = (timeSlot) => {
  isEdit.value = false
  formData.value = {
    id: null,
    venueId: selectedVenue.value?.id,
    bookingDate: selectedDate.value,
    startTime: timeSlot.start,
    endTime: timeSlot.end,
    customerName: '',
    purpose: '',
    remark: ''
  }
  showAddModal.value = true
}

const handleEditBooking = (booking) => {
  isEdit.value = true
  formData.value = {
    id: booking.id,
    venueId: booking.venue.id,
    bookingDate: booking.bookingDate,
    startTime: booking.startTime,
    endTime: booking.endTime,
    customerName: booking.customerName,
    purpose: booking.purpose,
    remark: booking.remark
  }
  showAddModal.value = true
}

const handleCancelBooking = async (booking) => {
  try {
    await venueBookingApi.cancel(booking.id)
    ElMessage.success('已取消')
    loadBookings()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    const data = {
      venue: { id: formData.value.venueId },
      bookingDate: formData.value.bookingDate,
      startTime: formData.value.startTime,
      endTime: formData.value.endTime,
      customerName: formData.value.customerName,
      purpose: formData.value.purpose,
      remark: formData.value.remark
    }
    if (isEdit.value) {
      await venueBookingApi.update(formData.value.id, data)
      ElMessage.success('修改成功')
    } else {
      await venueBookingApi.create(data)
      ElMessage.success('预约成功')
    }
    showAddModal.value = false
    loadBookings()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '预约失败')
  }
}

onMounted(() => {
  loadVenues()
})
</script>

<style scoped>
.venue-bookings-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.date-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.booking-container {
  display: flex;
  gap: 20px;
}

.venues-list {
  width: 200px;
  background: white;
  border-radius: 8px;
  padding: 10px;
  max-height: 600px;
  overflow-y: auto;
}

.venue-item {
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
  transition: all 0.3s;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.venue-item:hover:not(.disabled) {
  background: #f5f5f5;
}

.venue-item.active {
  background: #e8f4fd;
  border: 1px solid #409eff;
}

.venue-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.venue-name {
  font-weight: 600;
}

.booking-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.time-slots {
  display: flex;
  flex-direction: column;
}

.time-slot {
  display: flex;
  height: 60px;
  border-bottom: 1px solid #eee;
}

.time-label {
  width: 60px;
  padding: 10px;
  font-size: 12px;
  color: #999;
}

.slot-content {
  flex: 1;
  padding: 5px;
  position: relative;
}

.slot-content.hasBooking {
  background: #fafafa;
}

.slot-content.unavailable {
  background: #f5f5f5;
  cursor: not-allowed;
}

.booking-block {
  padding: 8px;
  border-radius: 6px;
  margin-bottom: 4px;
  cursor: pointer;
}

.booking-block.booked {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.booking-block.completed {
  background: #e8f5e9;
  color: #388e3c;
}

.booking-block.cancelled {
  background: #fce4ec;
  color: #c2185b;
  opacity: 0.7;
}

.booking-customer {
  font-weight: 600;
  font-size: 13px;
}

.booking-purpose {
  font-size: 11px;
  opacity: 0.8;
}

.booking-actions {
  margin-top: 5px;
  display: none;
}

.booking-block:hover .booking-actions {
  display: flex;
  gap: 4px;
}

.booking-actions .el-button {
  padding: 2px 6px;
  font-size: 10px;
}
</style>
