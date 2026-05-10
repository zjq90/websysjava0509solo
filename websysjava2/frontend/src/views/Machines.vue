<template>
  <div>
    <div class="page-header">
      <h2>设备管理</h2>
      <el-button type="primary" icon="Plus" @click="handleAdd">
        新增设备
      </el-button>
    </div>

    <div class="machine-grid">
      <el-card v-for="machine in machines" :key="machine.id" shadow="hover">
        <template #header>
          <div class="flex-between">
            <div>
              <span style="font-weight: bold;">{{ machine.name }}</span>
              <el-tag :type="getStatusType(machine.status)" style="margin-left: 10px;" size="small">
                {{ getStatusText(machine.status) }}
              </el-tag>
            </div>
            <el-dropdown @command="(cmd) => handleCommand(cmd, machine)">
              <el-icon class="el-icon--right"><MoreFilled /></el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">编辑</el-dropdown-item>
                  <el-dropdown-item command="slots">货道管理</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
        
        <div style="font-size: 14px; color: #909399; margin-bottom: 10px;">
          编号: {{ machine.machineCode }}
        </div>
        <div style="font-size: 13px; color: #606266; margin-bottom: 8px;">
          <el-icon><Location /></el-icon> {{ machine.location }}
        </div>
        <div style="font-size: 13px; color: #606266;">
          <el-icon><Monitor /></el-icon> IP: {{ machine.ipAddress || '-' }}
        </div>
        
        <el-divider />
        
        <div class="flex-between" style="font-size: 13px;">
          <span>货道: {{ machine.slotCount }} 个</span>
          <span v-if="machine.lastOnlineTime" style="color: #909399;">
            最后在线: {{ formatTime(machine.lastOnlineTime) }}
          </span>
        </div>
      </el-card>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑设备' : '新增设备'"
      width="500px"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="设备编号" prop="machineCode">
          <el-input v-model="form.machineCode" placeholder="如：VM001" />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="安装位置">
          <el-input v-model="form.location" placeholder="请输入安装位置" />
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
        </el-form-item>
        <el-form-item label="货道数量">
          <el-input-number v-model="form.slotCount" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="slotDialogVisible"
      :title="`货道管理 - ${currentMachine?.name}`"
      width="900px"
    >
      <div class="mb-20">
        <el-select v-model="selectedProductId" placeholder="选择要绑定的商品" clearable style="width: 200px;">
          <el-option
            v-for="p in products"
            :key="p.id"
            :label="p.name"
            :value="p.id"
          />
        </el-select>
        <el-button
          type="primary"
          :disabled="!selectedProductId || selectedSlots.length === 0"
          @click="handleBatchBind"
        >
          批量绑定到选中货道 ({{ selectedSlots.length }})
        </el-button>
        <el-button @click="loadSlots">刷新</el-button>
      </div>
      
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
          @click="toggleSlotSelection(slot)"
          :style="{ borderColor: selectedSlots.includes(slot.id) ? '#409eff' : '' }"
        >
          <div style="font-weight: bold; margin-bottom: 5px;">
            货道 {{ slot.slotNumber }}
          </div>
          <div v-if="slot.product" style="font-size: 12px; margin-bottom: 5px;">
            {{ slot.product.name }}
          </div>
          <div v-else style="font-size: 12px; color: #909399; margin-bottom: 5px;">
            未绑定
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
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getMachines,
  createMachine,
  updateMachine,
  deleteMachine,
  getSlotsByMachine,
  batchBindProduct
} from '../api/machine'
import { getProducts } from '../api/product'

const machines = ref([])
const products = ref([])
const slots = ref([])
const currentMachine = ref(null)
const selectedSlots = ref([])
const selectedProductId = ref(null)

const dialogVisible = ref(false)
const slotDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  machineCode: '',
  name: '',
  location: '',
  ipAddress: '',
  slotCount: 20,
  status: 'OFFLINE',
  description: ''
})

const rules = {
  machineCode: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入设备名称', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const types = { ONLINE: 'success', OFFLINE: 'danger', MAINTENANCE: 'warning' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { ONLINE: '在线', OFFLINE: '离线', MAINTENANCE: '维护中' }
  return texts[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString()
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

const isLowStock = (slot) => {
  if (!slot.product) return false
  return slot.currentStock <= (slot.product.stockThreshold || 0)
}

const toggleSlotSelection = (slot) => {
  const idx = selectedSlots.value.indexOf(slot.id)
  if (idx > -1) {
    selectedSlots.value.splice(idx, 1)
  } else {
    selectedSlots.value.push(slot.id)
  }
}

const loadMachines = async () => {
  try {
    machines.value = await getMachines()
  } catch (e) {
    console.error(e)
  }
}

const loadProducts = async () => {
  try {
    products.value = await getProducts()
  } catch (e) {
    console.error(e)
  }
}

const loadSlots = async () => {
  if (!currentMachine.value) return
  try {
    slots.value = await getSlotsByMachine(currentMachine.value.id)
    selectedSlots.value = []
  } catch (e) {
    console.error(e)
  }
}

const resetForm = () => {
  form.id = null
  form.machineCode = ''
  form.name = ''
  form.location = ''
  form.ipAddress = ''
  form.slotCount = 20
  form.status = 'OFFLINE'
  form.description = ''
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleCommand = async (cmd, machine) => {
  if (cmd === 'edit') {
    isEdit.value = true
    form.id = machine.id
    form.machineCode = machine.machineCode
    form.name = machine.name
    form.location = machine.location
    form.ipAddress = machine.ipAddress
    form.slotCount = machine.slotCount
    form.status = machine.status
    form.description = machine.description
    dialogVisible.value = true
  } else if (cmd === 'slots') {
    currentMachine.value = machine
    slotDialogVisible.value = true
    await loadSlots()
  } else if (cmd === 'delete') {
    ElMessageBox.confirm(`确定要删除设备"${machine.name}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      try {
        await deleteMachine(machine.id)
        ElMessage.success('删除成功')
        loadMachines()
      } catch (e) {
        console.error(e)
      }
    }).catch(() => {})
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    const data = { ...form }
    
    if (isEdit.value) {
      await updateMachine(form.id, data)
      ElMessage.success('更新成功')
    } else {
      await createMachine(data)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadMachines()
  } catch (e) {
    if (e !== false) {
      console.error(e)
    }
  }
}

const handleBatchBind = async () => {
  if (selectedSlots.value.length === 0) {
    ElMessage.warning('请先选择货道')
    return
  }
  
  try {
    const slotNumbers = slots.value
      .filter(s => selectedSlots.value.includes(s.id))
      .map(s => s.slotNumber)
    
    await batchBindProduct(currentMachine.value.id, slotNumbers, selectedProductId.value)
    ElMessage.success('批量绑定成功')
    selectedProductId.value = null
    selectedSlots.value = []
    loadSlots()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadMachines()
  loadProducts()
})
</script>
