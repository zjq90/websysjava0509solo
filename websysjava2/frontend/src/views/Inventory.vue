<template>
  <div>
    <div class="page-header">
      <h2>库存监控</h2>
      <div>
        <el-select
          v-model="selectedMachineId"
          placeholder="选择设备"
          clearable
          style="width: 220px; margin-right: 10px;"
          @change="loadSlots"
        >
          <el-option
            v-for="m in machines"
            :key="m.id"
            :label="m.name"
            :value="m.id"
          />
        </el-select>
        <el-button type="primary" @click="loadData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 32px; font-weight: bold; color: #409eff;">
              {{ stats.totalSlots }}
            </div>
            <div style="color: #909399; margin-top: 5px;">货道总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 32px; font-weight: bold; color: #67c23a;">
              {{ stats.normalStock }}
            </div>
            <div style="color: #909399; margin-top: 5px;">库存正常</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 32px; font-weight: bold; color: #e6a23c;">
              {{ stats.lowStock }}
            </div>
            <div style="color: #909399; margin-top: 5px;">低库存警告</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center;">
            <div style="font-size: 32px; font-weight: bold; color: #909399;">
              {{ stats.emptySlots }}
            </div>
            <div style="color: #909399; margin-top: 5px;">未绑定/空货道</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover">
      <template #header>
        <span style="font-weight: bold;">货道库存详情</span>
      </template>
      
      <div class="slot-grid">
        <div
          v-for="slot in slots"
          :key="slot.id"
          class="slot-item"
          :class="{
            'low-stock': isLowStock(slot),
            'empty': !slot.product,
            'disabled': !slot.enabled
          }"
          @click="showSlotDetail(slot)"
        >
          <div style="font-weight: bold; margin-bottom: 5px;">
            货道 {{ slot.slotNumber }}
          </div>
          <div v-if="slot.product" style="font-size: 12px; margin-bottom: 5px;">
            {{ slot.product.name }}
          </div>
          <div v-else style="font-size: 12px; color: #909399; margin-bottom: 5px;">
            未绑定商品
          </div>
          <el-progress
            v-if="slot.product"
            :percentage="getStockPercent(slot)"
            :stroke-width="8"
            :color="getStockColor(slot)"
          />
          <div v-if="slot.product" style="font-size: 11px; margin-top: 5px;">
            {{ slot.currentStock }} / {{ slot.maxCapacity }}
          </div>
          <el-tag v-if="isLowStock(slot)" type="warning" size="small" style="margin-top: 5px;">
            预警
          </el-tag>
        </div>
      </div>
      
      <el-empty v-if="slots.length === 0" description="请选择设备查看库存" />
    </el-card>

    <el-dialog
      v-model="detailVisible"
      :title="`货道 ${currentSlot?.slotNumber} 详情`"
      width="500px"
    >
      <el-descriptions v-if="currentSlot" border column="1">
        <el-descriptions-item label="设备">
          {{ currentMachine?.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="货道编号">
          {{ currentSlot.slotNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="商品">
          {{ currentSlot.product?.name || '未绑定' }}
        </el-descriptions-item>
        <el-descriptions-item label="商品规格">
          {{ currentSlot.product?.specification || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="零售价">
          ¥{{ currentSlot.product?.retailPrice || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="当前库存">
          <el-tag :type="isLowStock(currentSlot) ? 'warning' : 'success'">
            {{ currentSlot.currentStock }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最大容量">
          {{ currentSlot.maxCapacity }}
        </el-descriptions-item>
        <el-descriptions-item label="库存阈值">
          {{ currentSlot.product?.stockThreshold || 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentSlot.enabled ? 'success' : 'info'">
            {{ currentSlot.enabled ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最近补货">
          {{ currentSlot.lastRefillTime ? new Date(currentSlot.lastRefillTime).toLocaleString() : '-' }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMachines, getSlotsByMachine } from '../api/machine'

const machines = ref([])
const slots = ref([])
const selectedMachineId = ref(null)
const currentMachine = ref(null)
const currentSlot = ref(null)
const detailVisible = ref(false)

const stats = reactive({
  totalSlots: 0,
  normalStock: 0,
  lowStock: 0,
  emptySlots: 0
})

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

const isLowStock = (slot) => {
  if (!slot.product) return false
  return slot.currentStock <= (slot.product.stockThreshold || 0)
}

const showSlotDetail = (slot) => {
  currentSlot.value = slot
  detailVisible.value = true
}

const loadMachines = async () => {
  try {
    machines.value = await getMachines()
  } catch (e) {
    console.error(e)
  }
}

const loadSlots = async () => {
  if (!selectedMachineId.value) {
    slots.value = []
    stats.totalSlots = 0
    stats.normalStock = 0
    stats.lowStock = 0
    stats.emptySlots = 0
    return
  }
  
  currentMachine.value = machines.value.find(m => m.id === selectedMachineId.value)
  
  try {
    slots.value = await getSlotsByMachine(selectedMachineId.value)
    
    stats.totalSlots = slots.value.length
    stats.normalStock = 0
    stats.lowStock = 0
    stats.emptySlots = 0
    
    slots.value.forEach(slot => {
      if (!slot.product) {
        stats.emptySlots++
      } else if (isLowStock(slot)) {
        stats.lowStock++
      } else {
        stats.normalStock++
      }
    })
  } catch (e) {
    console.error(e)
  }
}

const loadData = async () => {
  await loadMachines()
  await loadSlots()
}

onMounted(() => {
  loadData()
})
</script>
