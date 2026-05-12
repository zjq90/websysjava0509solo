<template>
  <div>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header">
        <span>财务报表管理</span>
      </div>

      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="generateDailyReport">生成日报表</el-button>
        <el-button type="success" @click="generateMonthlyReport">生成月报表</el-button>
        <el-date-picker
          v-model="reportDate"
          type="date"
          placeholder="选择日期"
          style="margin-left: 20px">
        </el-date-picker>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="reportNo" label="报表编号" width="150"></el-table-column>
        <el-table-column prop="reportType" label="报表类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.reportType === '日报' ? 'primary' : 'success'" size="small">
              {{ scope.row.reportType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportDate" label="报表日期" width="120"></el-table-column>
        <el-table-column prop="outpatientCount" label="门诊笔数" width="100"></el-table-column>
        <el-table-column prop="outpatientAmount" label="门诊金额" width="120">
          <template slot-scope="scope">¥{{ scope.row.outpatientAmount }}</template>
        </el-table-column>
        <el-table-column prop="inpatientCount" label="住院笔数" width="100"></el-table-column>
        <el-table-column prop="inpatientAmount" label="住院金额" width="120">
          <template slot-scope="scope">¥{{ scope.row.inpatientAmount }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template slot-scope="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="insuranceAmount" label="医保总金额" width="120">
          <template slot-scope="scope">¥{{ scope.row.insuranceAmount }}</template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自付总金额" width="120">
          <template slot-scope="scope">¥{{ scope.row.selfPayAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <div style="display: flex; gap: 8px; justify-content: center;">
              <el-button size="mini" type="warning" @click="auditReport(scope.row.id)" :disabled="scope.row.status !== '待审核'">审核</el-button>
              <el-button size="mini" type="primary" @click="closeReport(scope.row.id)" :disabled="scope.row.status !== '已审核'">结账</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Report',
  data() {
    return {
      tableData: [],
      reportDate: new Date()
    }
  },
  mounted() {
    this.loadReports()
  },
  methods: {
    getStatusType(status) {
      if (status === '已结账') return 'success'
      if (status === '已审核') return 'warning'
      return 'info'
    },
    loadReports() {
      this.$http.get('/financial-reports').then(res => {
        this.tableData = res.data
      })
    },
    formatDate(date) {
      const d = new Date(date)
      return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
    },
    generateDailyReport() {
      this.$http.post('/financial-reports/daily?reportDate=' + this.formatDate(this.reportDate)).then(() => {
        this.$message.success('日报表生成成功')
        this.loadReports()
      })
    },
    generateMonthlyReport() {
      this.$http.post('/financial-reports/monthly?reportDate=' + this.formatDate(this.reportDate)).then(() => {
        this.$message.success('月报表生成成功')
        this.loadReports()
      })
    },
    auditReport(id) {
      this.$confirm('确定要审核该报表吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/financial-reports/' + id + '/audit', {
          auditor: '管理员'
        }).then(() => {
          this.$message.success('审核成功')
          this.loadReports()
        })
      }).catch(() => {})
    },
    closeReport(id) {
      this.$confirm('确定要对该报表进行结账吗？结账后将无法修改。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/financial-reports/' + id + '/close').then(() => {
          this.$message.success('结账成功')
          this.loadReports()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
