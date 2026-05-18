<template>
  <div class="heritage-audit">
    <h2>文物审核</h2>
    <el-table :data="heritageList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文物名称" width="150"></el-table-column>
      <el-table-column prop="dynasty" label="朝代" width="100"></el-table-column>
      <el-table-column prop="material" label="材质" width="100"></el-table-column>
      <el-table-column prop="ownerName" label="所有者" width="120"></el-table-column>
      <el-table-column prop="riskLevel" label="风险等级" width="120">
        <template slot-scope="scope">
          <el-tag :type="getRiskType(scope.row.riskLevel)">{{ getRiskText(scope.row.riskLevel) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="estimatedValue" label="预估价值" width="120"></el-table-column>
      <el-table-column label="操作" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="verifyApi(scope.row.id)">API验证</el-button>
          <el-button size="mini" type="success" @click="auditDialog(scope.row, 'APPROVED')">通过</el-button>
          <el-button size="mini" type="danger" @click="auditDialog(scope.row, 'REJECTED')">拒绝</el-button>
          <el-button size="mini" @click="openDetailWindow(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="审核意见" :visible.sync="auditVisible" width="500px">
      <el-form :model="auditForm">
        <el-form-item label="审核备注">
          <el-input type="textarea" v-model="auditForm.remark" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确认</el-button>
      </div>
    </el-dialog>

    <div v-for="detail in detailWindows" :key="detail.id" class="detail-window">
      <el-dialog :title="'文物详情 - ' + detail.data.name" :visible.sync="detail.visible" width="700px" :modal="false" :append-to-body="true">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="文物名称">{{ detail.data.name }}</el-descriptions-item>
          <el-descriptions-item label="朝代">{{ detail.data.dynasty }}</el-descriptions-item>
          <el-descriptions-item label="材质">{{ detail.data.material }}</el-descriptions-item>
          <el-descriptions-item label="用途">{{ detail.data.usageType }}</el-descriptions-item>
          <el-descriptions-item label="预估价值">{{ detail.data.estimatedValue }}</el-descriptions-item>
          <el-descriptions-item label="来源地">{{ detail.data.origin }}</el-descriptions-item>
          <el-descriptions-item label="所有者">{{ detail.data.ownerName }}</el-descriptions-item>
          <el-descriptions-item label="所有者电话">{{ detail.data.ownerPhone }}</el-descriptions-item>
          <el-descriptions-item label="来源说明" :span="2">{{ detail.data.provenance }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ detail.data.description }}</el-descriptions-item>
        </el-descriptions>
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" type="success" @click="quickAudit(detail.data, 'APPROVED')">审核通过</el-button>
          <el-button size="mini" type="danger" @click="quickAudit(detail.data, 'REJECTED')">审核拒绝</el-button>
          <el-button size="mini" @click="closeDetailWindow(detail.id)">关闭窗口</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HeritageAudit',
  data() {
    return {
      heritageList: [],
      auditVisible: false,
      detailWindows: [],
      auditForm: {
        id: null,
        status: '',
        remark: ''
      }
    }
  },
  mounted() {
    this.loadPendingAudit()
  },
  methods: {
    loadPendingAudit() {
      this.$http.get('/heritage/pending-audit').then(res => {
        if (res.data.code === 200) {
          this.heritageList = res.data.data
        }
      })
    },
    getRiskType(level) {
      const map = { 'HIGH': 'danger', 'MEDIUM': 'warning', 'LOW': 'success' }
      return map[level] || 'info'
    },
    getRiskText(level) {
      const map = { 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return map[level] || level
    },
    verifyApi(id) {
      this.$http.post(`/heritage/${id}/verify-api`).then(res => {
        if (res.data.code === 200) {
          this.$message.success('API验证成功')
          this.loadPendingAudit()
        } else {
          this.$message.error('API验证失败')
        }
      })
    },
    auditDialog(row, status) {
      this.auditForm.id = row.id
      this.auditForm.status = status
      this.auditForm.remark = ''
      this.auditVisible = true
    },
    submitAudit() {
      this.$http.post(`/heritage/${this.auditForm.id}/audit`, null, {
        params: {
          status: this.auditForm.status,
          remark: this.auditForm.remark,
          auditorId: 1
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('审核成功')
          this.auditVisible = false
          this.loadPendingAudit()
        } else {
          this.$message.error('审核失败')
        }
      })
    },
    openDetailWindow(row) {
      const existing = this.detailWindows.find(d => d.id === row.id)
      if (existing) {
        existing.visible = true
        return
      }
      this.detailWindows.push({
        id: row.id,
        data: row,
        visible: true
      })
    },
    closeDetailWindow(id) {
      const index = this.detailWindows.findIndex(d => d.id === id)
      if (index !== -1) {
        this.detailWindows.splice(index, 1)
      }
    },
    quickAudit(row, status) {
      this.$http.post(`/heritage/${row.id}/audit`, null, {
        params: {
          status: status,
          remark: '',
          auditorId: 1
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('审核成功')
          this.closeDetailWindow(row.id)
          this.loadPendingAudit()
        } else {
          this.$message.error('审核失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.heritage-audit {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
.detail-window {
  z-index: 1000;
}
</style>
