<template>
  <div class="frozen-transactions">
    <h2>冻结交易列表</h2>
    <el-table :data="transactionList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="transactionNo" label="交易编号" width="150"></el-table-column>
      <el-table-column prop="heritageName" label="文物名称" width="150"></el-table-column>
      <el-table-column prop="buyerName" label="买家" width="100"></el-table-column>
      <el-table-column prop="sellerName" label="卖家" width="100"></el-table-column>
      <el-table-column prop="amount" label="金额" width="120"></el-table-column>
      <el-table-column prop="freezeReason" label="冻结原因" width="200"></el-table-column>
      <el-table-column prop="freezeAuditStatus" label="审核状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.freezeAuditStatus)">{{ getStatusText(scope.row.freezeAuditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="audit(scope.row.id, 'APPROVED')">通过</el-button>
          <el-button size="mini" type="danger" @click="audit(scope.row.id, 'REJECTED')">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'FrozenTransactions',
  data() {
    return {
      transactionList: []
    }
  },
  mounted() {
    this.loadFrozenTransactions()
  },
  methods: {
    loadFrozenTransactions() {
      this.$http.get('/transactions/frozen').then(res => {
        if (res.data.code === 200) {
          this.transactionList = res.data.data
        }
      })
    },
    getStatusType(status) {
      const map = { 'APPROVED': 'success', 'REJECTED': 'danger', 'PENDING': 'warning' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 'APPROVED': '已通过', 'REJECTED': '已拒绝', 'PENDING': '待审核' }
      return map[status] || status
    },
    audit(id, status) {
      this.$http.post(`/transactions/${id}/audit-freeze`, null, {
        params: { status, auditorId: 1 }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('审核成功')
          this.loadFrozenTransactions()
        }
      })
    }
  }
}
</script>

<style scoped>
.frozen-transactions {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
