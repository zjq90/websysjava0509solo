<template>
  <div class="pharmacy-page">
    <el-card class="pending-card">
      <template #header>
        <span>待发药处方</span>
      </template>
      <el-table :data="pendingList" border stripe>
        <el-table-column prop="prescriptionNo" label="处方号" width="180" />
        <el-table-column prop="patient.name" label="患者姓名" width="120" />
        <el-table-column prop="doctor.name" label="医生" width="120" />
        <el-table-column prop="department.deptName" label="科室" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag type="success">{{ row.status || '已收费' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="viewDetail(row)">查看详情</el-button>
            <el-button size="small" type="success" @click="dispense(row)">发药</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="drug-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>药品库存</span>
          <el-button type="primary" @click="showAddDrug">新增药品</el-button>
        </div>
      </template>
      <el-table :data="drugList" border stripe>
        <el-table-column prop="drugCode" label="药品编码" width="120" />
        <el-table-column prop="drugName" label="药品名称" width="180" />
        <el-table-column prop="drugSpec" label="规格" width="150" />
        <el-table-column prop="manufacturer" label="生产厂家" width="180" />
        <el-table-column prop="stockQuantity" label="库存数量" width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">
            ¥{{ row.price || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column label="库存预警" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.stockQuantity <= 10" type="danger">库存不足</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="处方详情" width="700px">
      <el-descriptions :column="2" border style="margin-bottom: 20px">
        <el-descriptions-item label="处方号">{{ currentPrescription?.prescriptionNo }}</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ currentPrescription?.patient?.name }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ currentPrescription?.doctor?.name }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ currentPrescription?.department?.deptName }}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="currentPrescription?.items || []" border>
        <el-table-column prop="drugName" label="药品名称" />
        <el-table-column prop="spec" label="规格" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="dosage" label="剂量" />
        <el-table-column prop="usage" label="用法" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

const pendingList = ref([
  {
    id: 1,
    prescriptionNo: 'P202401010001',
    patient: { name: '张三' },
    doctor: { name: '张医生' },
    department: { deptName: '内科' },
    status: '已收费',
    items: [
      { drugName: '阿莫西林胶囊', spec: '0.25g*24粒', quantity: 2, dosage: '0.5g/次', usage: '每日3次' },
      { drugName: '布洛芬缓释胶囊', spec: '0.3g*20粒', quantity: 1, dosage: '0.3g/次', usage: '每日2次' }
    ]
  }
])

const drugList = ref([
  { drugCode: 'D001', drugName: '阿莫西林胶囊', drugSpec: '0.25g*24粒', manufacturer: '华北制药', stockQuantity: 100, unit: '盒', price: 15.00 },
  { drugCode: 'D002', drugName: '布洛芬缓释胶囊', drugSpec: '0.3g*20粒', manufacturer: '中美史克', stockQuantity: 5, unit: '盒', price: 25.50 },
  { drugCode: 'D003', drugName: '维生素C片', drugSpec: '100mg*100片', manufacturer: '东北制药', stockQuantity: 200, unit: '瓶', price: 8.00 }
])

const detailDialogVisible = ref(false)
const currentPrescription = ref(null)

const viewDetail = (row) => {
  currentPrescription.value = row
  detailDialogVisible.value = true
}

const dispense = (row) => {
  ElMessage.success('发药成功！')
  const index = pendingList.value.findIndex(item => item.id === row.id)
  if (index > -1) {
    pendingList.value.splice(index, 1)
  }
}

const showAddDrug = () => {
  ElMessage.info('新增药品功能')
}

onMounted(() => {
})
</script>

<style scoped>
.pharmacy-page {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.pharmacy-page :deep(.el-card) {
  width: 100%;
}

.pharmacy-page :deep(.el-table) {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
