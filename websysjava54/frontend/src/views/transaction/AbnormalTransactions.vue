<template>
  <div class="abnormal-transactions">
    <h2>异常交易监测</h2>
    <el-table :data="transactionList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="transactionNo" label="交易编号" width="150"></el-table-column>
      <el-table-column prop="heritageName" label="文物名称" width="150"></el-table-column>
      <el-table-column prop="buyerName" label="买家" width="100"></el-table-column>
      <el-table-column prop="sellerName" label="卖家" width="100"></el-table-column>
      <el-table-column prop="amount" label="交易金额" width="120"></el-table-column>
      <el-table-column prop="abnormalReason" label="异常原因" width="200"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="warning" @click="freezeDialog(scope.row)">冻结资金</el-button>
          <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="冻结资金" :visible.sync="freezeVisible" width="500px">
      <el-form :model="freezeForm">
        <el-form-item label="冻结原因">
          <el-input type="textarea" v-model="freezeForm.reason" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="freezeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFreeze">申请冻结</el-button>
      </div>
    </el-dialog>

    <el-dialog title="交易详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border v-if="currentTransaction">
        <el-descriptions-item label="交易编号">{{ currentTransaction.transactionNo }}</el-descriptions-item>
        <el-descriptions-item label="文物名称">{{ currentTransaction.heritageName }}</el-descriptions-item>
        <el-descriptions-item label="买家">{{ currentTransaction.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="卖家">{{ currentTransaction.sellerName }}</el-descriptions-item>
        <el-descriptions-item label="交易金额">{{ currentTransaction.amount }}</el-descriptions-item>
        <el-descriptions-item label="预估价值">{{ currentTransaction.estimatedValue }}</el-descriptions-item>
        <el-descriptions-item label="异常原因" :span="2">{{ currentTransaction.abnormalReason }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AbnormalTransactions',
  data() {
    return {
      transactionList: [],
      freezeVisible: false,
      detailVisible: false,
      currentTransaction: null,
      freezeForm: {
        id: null,
        reason: ''
      }
    }
  },
  mounted() {
    this.loadAbnormalTransactions()
  },
  methods: {
    loadAbnormalTransactions() {
      this.$http.get('/transactions/abnormal').then(res => {
        if (res.data.code === 200) {
          this.transactionList = res.data.data
        }
      })
    },
    freezeDialog(row) {
      this.freezeForm.id = row.id
      this.freezeForm.reason = ''
      this.freezeVisible = true
    },
    submitFreeze() {
      this.$http.post(`/transactions/${this.freezeForm.id}/request-freeze`, null, {
        params: { reason: this.freezeForm.reason }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('冻结申请已提交')
          this.freezeVisible = false
          this.loadAbnormalTransactions()
        }
      })
    },
    viewDetail(row) {
      this.currentTransaction = row
      this.detailVisible = true
    }
  }
}
</script>

<style scoped>
.abnormal-transactions {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
