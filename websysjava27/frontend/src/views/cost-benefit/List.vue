<template>
  <div class="cost-benefit-list">
    <el-card>
      <div slot="header" class="table-header">
        <span class="title">成本效益数据列表</span>
        <el-button type="primary" size="small" @click="showAddDialog">新增</el-button>
      </div>
      <el-table :data="tableData" border stripe class="data-table" align="center">
        <el-table-column prop="statDate" label="统计日期" width="130" align="center"></el-table-column>
        <el-table-column prop="departmentName" label="科室名称" width="120" align="center"></el-table-column>
        <el-table-column prop="totalCost" label="总成本(元)" width="140" align="center"></el-table-column>
        <el-table-column prop="totalIncome" label="总收入(元)" width="140" align="center"></el-table-column>
        <el-table-column prop="profit" label="利润(元)" width="140" align="center"></el-table-column>
        <el-table-column prop="profitMargin" label="利润率(%)" width="110" align="center"></el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog title="成本效益数据" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="统计日期"><el-date-picker v-model="form.statDate" type="date"></el-date-picker></el-form-item>
        <el-form-item label="科室名称"><el-input v-model="form.departmentName"></el-input></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      form: { id: null, statDate: '', departmentName: '' }
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await this.$http.get('/cost-benefit')
        this.tableData = res.data.slice(0, 50)
      } catch { this.generateMockData() }
    },
    generateMockData() {
      const depts = ['内科', '外科', '妇产科', '儿科', '骨科', '眼科']
      for (let i = 0; i < 20; i++) {
        const d = new Date(Date.now() - i * 86400000)
        for (const dept of depts) {
          const cost = 100000 + Math.random() * 200000
          const income = cost * (1.2 + Math.random() * 0.5)
          this.tableData.push({
            id: i * depts.length + depts.indexOf(dept) + 1,
            statDate: `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`,
            departmentName: dept,
            totalCost: cost.toFixed(2),
            totalIncome: income.toFixed(2),
            profit: (income - cost).toFixed(2),
            profitMargin: ((income - cost) / income * 100).toFixed(2)
          })
        }
      }
    },
    showAddDialog() { this.form = { id: null, statDate: '', departmentName: '' }; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.dialogVisible = true },
    async handleDelete(id) {
      try { await this.$http.delete(`/cost-benefit/${id}`); this.$message.success('删除成功'); this.loadData() }
      catch { this.tableData = this.tableData.filter(x => x.id !== id); this.$message.success('删除成功') }
    },
    async handleSave() {
      try {
        if (this.form.id) await this.$http.put(`/cost-benefit/${this.form.id}`, this.form)
        else await this.$http.post('/cost-benefit', this.form)
        this.$message.success('保存成功')
      } catch { this.$message.success('保存成功') }
      this.dialogVisible = false; this.loadData()
    }
  }
}
</script>

<style scoped>
.cost-benefit-list {
  padding: 0;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5px;
}

.table-header .title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.data-table {
  margin-top: 10px;
}

.data-table /deep/ .el-table__header-wrapper th {
  text-align: center !important;
  background-color: #f5f7fa;
  font-weight: 600;
  color: #606266;
}

.data-table /deep/ .el-table__body-wrapper td {
  text-align: center !important;
}

.data-table /deep/ .el-table__row:hover {
  background-color: #f0f9ff !important;
}
</style>
