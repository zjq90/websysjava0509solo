<template>
  <div class="charge-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="患者姓名">
          <el-input v-model="searchForm.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="prescription-card" style="margin-top: 20px">
      <template #header>
        <span>待收费处方</span>
      </template>
      <el-table :data="prescriptionList" border stripe @row-click="selectPrescription">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="prescriptionNo" label="处方号" width="180" />
        <el-table-column prop="patient.name" label="患者姓名" width="120" />
        <el-table-column prop="doctor.name" label="医生" width="120" />
        <el-table-column prop="department.deptName" label="科室" width="120" />
        <el-table-column prop="totalAmount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.status || '待收费' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="charge-detail-card" style="margin-top: 20px" v-if="selectedPrescription">
      <template #header>
        <span>收费详情</span>
      </template>
      <el-descriptions :column="2" border style="margin-bottom: 20px">
        <el-descriptions-item label="处方号">{{ selectedPrescription.prescriptionNo }}</el-descriptions-item>
        <el-descriptions-item label="患者姓名">{{ selectedPrescription.patient?.name }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ selectedPrescription.doctor?.name }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ selectedPrescription.department?.deptName }}</el-descriptions-item>
      </el-descriptions>

      <el-table :data="selectedPrescription.items || []" border>
        <el-table-column prop="drugName" label="药品名称" />
        <el-table-column prop="spec" label="规格" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">
            ¥{{ row.price || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.amount || '0.00' }}
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; text-align: right">
        <el-statistic title="总计" :value="selectedPrescription.totalAmount || 0" precision="2" prefix="¥" />
      </div>

      <div style="margin-top: 20px; text-align: center">
        <el-button type="primary" size="large" @click="handleCharge">确认收费</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const searchForm = ref({
  patientName: ''
})

const prescriptionList = ref([
  {
    id: 1,
    prescriptionNo: 'P202401010001',
    patient: { name: '张三' },
    doctor: { name: '张医生' },
    department: { deptName: '内科' },
    totalAmount: 128.50,
    status: '待收费',
    items: [
      { drugName: '阿莫西林胶囊', spec: '0.25g*24粒', quantity: 2, price: 15.00, amount: 30.00 },
      { drugName: '布洛芬缓释胶囊', spec: '0.3g*20粒', quantity: 1, price: 25.50, amount: 25.50 }
    ]
  }
])

const selectedPrescription = ref(null)

const search = () => {
  ElMessage.info('查询中...')
}

const selectPrescription = (row) => {
  selectedPrescription.value = row
}

const handleCharge = () => {
  ElMessage.success('收费成功！')
  selectedPrescription.value = null
}
</script>

<style scoped>
.charge-page {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.charge-page :deep(.el-card) {
  width: 100%;
}

.charge-page :deep(.el-table) {
  width: 100%;
}
</style>
