<template>
  <div class="report-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>检验检查报告列表</span>
        <el-button style="float: right; margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleAdd">新增报告</el-button>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="reports" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="reportNo" label="报告编号" width="150"></el-table-column>
        <el-table-column prop="reportTitle" label="报告标题" width="180"></el-table-column>
        <el-table-column prop="conclusion" label="结论" width="200"></el-table-column>
        <el-table-column prop="suggestion" label="建议"></el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="info" icon="el-icon-view" @click="viewReport">查看</el-button>
            <el-button size="mini" type="warning" icon="el-icon-check" @click="submitAudit(scope.row.id)" v-if="scope.row.status === 'DRAFT'">提交审核</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete">删除</el-button>
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
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentReport.status)" size="small">{{ getStatusText(currentReport.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="是否已查看">
            {{ currentReport.patientViewed ? '是' : '否' }}
          </el-descriptions-item>
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
  name: 'ReportList',
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
    handleAdd() {
      this.$message.info('新增报告功能待实现')
    },
    loadData() {
      this.$http.get('/api/reports').then(response => {
        this.reports = response.data
      }).catch(error => {
        console.error('加载报告失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    viewReport() {
      this.currentReport = this.reports[0]
      this.dialogVisible = true
    },
    submitAudit(id) {
      this.$http.put(`/api/reports/${id}/submit-audit`).then(() => {
        this.$message.success('已提交审核')
        this.loadData()
      }).catch(error => {
        console.error('提交审核失败:', error)
        this.$message.error('操作失败')
      })
    },
    getStatusType(status) {
      const map = {
        'DRAFT': 'info',
        'FIRST_AUDIT': 'warning',
        'SECOND_AUDIT': 'primary',
        'PASSED': 'success',
        'PUBLISHED': 'success',
        'REJECTED': 'danger'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'DRAFT': '草稿',
        'FIRST_AUDIT': '待一级审核',
        'SECOND_AUDIT': '待二级审核',
        'PASSED': '审核通过',
        'PUBLISHED': '已发布',
        'REJECTED': '已驳回'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.report-list {
  padding: 0;
}
</style>
