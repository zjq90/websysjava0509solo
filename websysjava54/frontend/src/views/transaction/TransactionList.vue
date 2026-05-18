<template>
  <div class="transaction-list">
    <h2>交易列表</h2>
    <el-table :data="transactionList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="transactionNo" label="交易编号" width="150"></el-table-column>
      <el-table-column prop="heritageName" label="文物名称" width="150"></el-table-column>
      <el-table-column prop="buyerName" label="买家" width="100"></el-table-column>
      <el-table-column prop="sellerName" label="卖家" width="100"></el-table-column>
      <el-table-column prop="amount" label="金额" width="120"></el-table-column>
      <el-table-column prop="isAbnormal" label="异常" width="80">
        <template slot-scope="scope">
          <el-tag size="mini" type="danger" v-if="scope.row.isAbnormal">是</el-tag>
          <el-tag size="mini" v-else>否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="fundsFrozen" label="冻结" width="80">
        <template slot-scope="scope">
          <el-tag size="mini" type="warning" v-if="scope.row.fundsFrozen">是</el-tag>
          <el-tag size="mini" v-else>否</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'TransactionList',
  data() {
    return {
      transactionList: []
    }
  },
  mounted() {
    this.loadTransactionList()
  },
  methods: {
    loadTransactionList() {
      this.$http.get('/transactions').then(res => {
        if (res.data.code === 200) {
          this.transactionList = res.data.data
        }
      })
    }
  }
}
</script>

<style scoped>
.transaction-list {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
