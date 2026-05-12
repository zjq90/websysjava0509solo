<template>
  <div class="report-audit">
    <el-card>
      <div slot="header" class="clearfix">
        <span>报告审核</span>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="reports" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="reportNo" label="报告编号" width="150"></el-table-column>
        <el-table-column prop="reportTitle" label="报告标题" width="180"></el-table-column>
        <el-table-column prop="conclusion" label="结论" width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button size="mini" type="info" icon="el-icon-view" @click="viewReport(scope.row)">查看</el-button>
            <el-button size="mini" type="success" icon="el-icon-check" @click="auditPass(scope.row.id, scope.row.status)" v-if="scope.row.status === 'FIRST_AUDIT' || scope.row.status === 'SECOND_AUDIT'">通过</el-button>
            <el-button size="mini" type="danger" icon="el-icon-close" @click="auditReject(scope.row.id, scope.row.status)" v-if="scope.row.status === 'FIRST_AUDIT' || scope.row.status === 'SECOND_AUDIT'">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="查看报告" :visible.sync="dialogVisible" width="700px">
      <div v-if="currentReport">
        <h3 style="text-align: center; margin-bottom: 20px">{{ currentReport.reportTitle }}</h3>
        <div style="background: #f5f7fa; padding: 15px; border-radius: 4px; margin-bottom: 20px">
          <p><strong>报告编号：</strong>{{ currentReport.reportNo }}</p>
          <p><strong>报告内容：</strong></p>
          <p style="white-space: pre-wrap">{{ currentReport.reportContent }}</p>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="结论">{{ currentReport.conclusion || '-' }}</el-descriptions-item>
          <el-descriptions-item label="建议">{{ currentReport.suggestion || '-' }}</el-descriptions-item>
          <el-descriptions-item label="一级审核意见">{{ currentReport.firstAuditOpinion || '-' }}</el-descriptions-item>
          <el-descriptions-item label="二级审核意见">{{ currentReport.secondAuditOpinion || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ReportAudit',
  data() {
    return {
      reports: [],
      dialogVisible: false,
      currentReport: null
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/reports').then(response => {
        this.reports = response.data.filter(r => r.status === 'FIRST_AUDIT' || r.status === 'SECOND_AUDIT')
      }).catch(error => {
        console.error('加载审核报告失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    viewReport(row) {
      this.currentReport = row
      this.dialogVisible = true
    },
    auditPass(id, status) {
      this.$prompt('请输入审核意见', '审核通过', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        if (status === 'FIRST_AUDIT') {
          this.$http.put(`/api/reports/${id}/first-audit-pass?auditorId=1&opinion=${value}`).then(() => {
            this.$message.success('一级审核通过')
            this.loadData()
          }).catch(error => {
            console.error('一级审核失败:', error)
            this.$message.error('操作失败')
          })
        } else {
          this.$http.put(`/api/reports/${id}/second-audit-publish?auditorId=1&opinion=${value}`).then(() => {
            this.$message.success('二级审核通过并发布')
            this.loadData()
          }).catch(error => {
            console.error('二级审核失败:', error)
            this.$message.error('操作失败')
          })
        }
      })
    },
    auditReject(id, status) {
      this.$prompt('请输入驳回原因', '审核驳回', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        if (status === 'FIRST_AUDIT') {
          this.$http.put(`/api/reports/${id}/first-audit-reject?auditorId=1&opinion=${value}`).then(() => {
            this.$message.success('已驳回')
            this.loadData()
          }).catch(error => {
            console.error('驳回失败:', error)
            this.$message.error('操作失败')
          })
        }
      })
    },
    getStatusType(status) {
      const map = {
        'FIRST_AUDIT': 'warning',
        'SECOND_AUDIT': 'primary'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'FIRST_AUDIT': '待一级审核',
        'SECOND_AUDIT': '待二级审核'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.report-audit {
  padding: 0;
}
</style>
