<template>
  <div class="report-list">
    <h2>举报处理</h2>
    <el-table :data="reportList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="reportType" label="举报类型" width="120"></el-table-column>
      <el-table-column prop="targetName" label="目标名称" width="150"></el-table-column>
      <el-table-column prop="reporterName" label="举报人" width="100"></el-table-column>
      <el-table-column prop="reason" label="举报原因" width="250"></el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handle(scope.row.id, 'APPROVED')">通过</el-button>
          <el-button size="mini" type="danger" @click="handle(scope.row.id, 'REJECTED')">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'ReportList',
  data() {
    return {
      reportList: []
    }
  },
  mounted() {
    this.loadReportList()
  },
  methods: {
    loadReportList() {
      this.$http.get('/reports').then(res => {
        if (res.data.code === 200) {
          this.reportList = res.data.data
        }
      })
    },
    getStatusType(status) {
      const map = { 'APPROVED': 'success', 'REJECTED': 'danger', 'PENDING': 'warning' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 'APPROVED': '已处理', 'REJECTED': '已驳回', 'PENDING': '待处理' }
      return map[status] || status
    },
    handle(id, status) {
      this.$http.post(`/reports/${id}/handle`, null, {
        params: { status, handlerId: 1 }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('处理成功')
          this.loadReportList()
        }
      })
    }
  }
}
</script>

<style scoped>
.report-list {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
