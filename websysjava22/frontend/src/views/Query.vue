<template>
  <div class="query-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="就诊人次统计" name="visit">
        <el-card>
          <el-form :inline="true" :model="visitForm">
            <el-form-item label="开始日期">
              <el-date-picker v-model="visitForm.startDate" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item label="结束日期">
              <el-date-picker v-model="visitForm.endDate" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryVisit">查询</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="20" style="margin: 20px 0">
            <el-col :span="6">
              <el-card class="stat-card">
                <el-statistic title="总就诊人次" :value="totalVisits" value-style="color: #409EFF" />
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <el-statistic title="内科人次" :value="deptStats.internal" value-style="color: #67C23A" />
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <el-statistic title="外科人次" :value="deptStats.surgery" value-style="color: #E6A23C" />
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <el-statistic title="其他科室" :value="deptStats.other" value-style="color: #909399" />
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="医生工作量统计" name="doctor">
        <el-card>
          <el-table :data="doctorWorkload" border stripe>
            <el-table-column prop="doctorName" label="医生姓名" width="120" />
            <el-table-column prop="deptName" label="科室" width="120" />
            <el-table-column prop="visitCount" label="接诊人数" width="120" />
            <el-table-column prop="prescriptionCount" label="处方数" width="120" />
            <el-table-column prop="totalAmount" label="总金额" width="150">
              <template #default="{ row }">
                ¥{{ row.totalAmount || '0.00' }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="处方查询" name="prescription">
        <el-card>
          <el-form :inline="true" :model="prescriptionForm">
            <el-form-item label="患者姓名">
              <el-input v-model="prescriptionForm.patientName" placeholder="请输入患者姓名" />
            </el-form-item>
            <el-form-item label="医生姓名">
              <el-input v-model="prescriptionForm.doctorName" placeholder="请输入医生姓名" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryPrescription">查询</el-button>
            </el-form-item>
          </el-form>

          <el-table :data="prescriptionList" border stripe style="margin-top: 20px">
            <el-table-column prop="prescriptionNo" label="处方号" width="180" />
            <el-table-column prop="patientName" label="患者姓名" width="120" />
            <el-table-column prop="doctorName" label="医生姓名" width="120" />
            <el-table-column prop="deptName" label="科室" width="120" />
            <el-table-column prop="totalAmount" label="金额" width="120">
              <template #default="{ row }">
                ¥{{ row.totalAmount || '0.00' }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '已完成' ? 'success' : 'warning'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('visit')

const visitForm = ref({
  startDate: '',
  endDate: ''
})

const totalVisits = ref(156)
const deptStats = ref({
  internal: 68,
  surgery: 45,
  other: 43
})

const doctorWorkload = ref([
  { doctorName: '张医生', deptName: '内科', visitCount: 35, prescriptionCount: 30, totalAmount: 3500.00 },
  { doctorName: '李医生', deptName: '内科', visitCount: 33, prescriptionCount: 28, totalAmount: 3200.00 },
  { doctorName: '赵医生', deptName: '外科', visitCount: 25, prescriptionCount: 22, totalAmount: 2800.00 },
  { doctorName: '王医生', deptName: '儿科', visitCount: 20, prescriptionCount: 18, totalAmount: 2000.00 }
])

const prescriptionForm = ref({
  patientName: '',
  doctorName: ''
})

const prescriptionList = ref([
  { prescriptionNo: 'P202401010001', patientName: '张三', doctorName: '张医生', deptName: '内科', totalAmount: 128.50, status: '已完成' },
  { prescriptionNo: 'P202401010002', patientName: '李四', doctorName: '李医生', deptName: '内科', totalAmount: 89.00, status: '已完成' },
  { prescriptionNo: 'P202401010003', patientName: '王五', doctorName: '赵医生', deptName: '外科', totalAmount: 256.00, status: '已完成' }
])

const queryVisit = () => {
  ElMessage.info('查询中...')
}

const queryPrescription = () => {
  ElMessage.info('查询中...')
}
</script>

<style scoped>
.query-page {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.query-page :deep(.el-card) {
  width: 100%;
}

.query-page :deep(.el-table) {
  width: 100%;
}

.stat-card {
  text-align: center;
}
</style>
