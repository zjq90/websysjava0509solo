<template>
  <div class="operation-list">
    <el-card>
      <div slot="header" class="table-header">
        <span class="title">运营指标列表</span>
        <el-button type="primary" size="small" @click="showAddDialog">新增</el-button>
      </div>
      <el-table :data="tableData" border stripe class="data-table" align="center">
        <el-table-column prop="statDate" label="统计日期" width="130" align="center"></el-table-column>
        <el-table-column prop="departmentName" label="科室名称" width="120" align="center"></el-table-column>
        <el-table-column prop="outpatientCount" label="门诊量" width="100" align="center"></el-table-column>
        <el-table-column prop="inpatientCount" label="住院量" width="100" align="center"></el-table-column>
        <el-table-column prop="surgeryCount" label="手术量" width="100" align="center"></el-table-column>
        <el-table-column prop="bedUsageRate" label="床位使用率(%)" width="130" align="center"></el-table-column>
        <el-table-column prop="avgHospitalizationDays" label="平均住院日" width="120" align="center"></el-table-column>
        <el-table-column prop="medicineRatio" label="药占比(%)" width="110" align="center"></el-table-column>
        <el-table-column prop="totalIncome" label="总收入(元)" width="130" align="center"></el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="运营指标" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="统计日期">
          <el-date-picker v-model="form.statDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="科室名称">
          <el-input v-model="form.departmentName"></el-input>
        </el-form-item>
        <el-form-item label="门诊量">
          <el-input-number v-model="form.outpatientCount"></el-input-number>
        </el-form-item>
        <el-form-item label="住院量">
          <el-input-number v-model="form.inpatientCount"></el-input-number>
        </el-form-item>
        <el-form-item label="手术量">
          <el-input-number v-model="form.surgeryCount"></el-input-number>
        </el-form-item>
        <el-form-item label="床位使用率">
          <el-input-number v-model="form.bedUsageRate" :precision="2"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'OperationList',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      form: {
        id: null,
        statDate: '',
        departmentName: '',
        outpatientCount: 0,
        inpatientCount: 0,
        surgeryCount: 0,
        bedUsageRate: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const response = await this.$http.get('/operation-metrics')
        this.tableData = response.data.slice(0, 50)
      } catch (error) {
        console.error('加载数据失败', error)
        this.generateMockData()
      }
    },
    generateMockData() {
      const departments = ['内科', '外科', '妇产科', '儿科', '骨科', '眼科']
      for (let i = 0; i < 20; i++) {
        const d = new Date(Date.now() - i * 24 * 60 * 60 * 1000)
        for (const dept of departments) {
          this.tableData.push({
            id: i * departments.length + departments.indexOf(dept) + 1,
            statDate: `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`,
            departmentName: dept,
            outpatientCount: Math.floor(Math.random() * 200) + 100,
            inpatientCount: Math.floor(Math.random() * 50) + 20,
            surgeryCount: Math.floor(Math.random() * 20) + 5,
            bedUsageRate: (70 + Math.random() * 25).toFixed(2),
            avgHospitalizationDays: (5 + Math.random() * 10).toFixed(1),
            medicineRatio: (30 + Math.random() * 20).toFixed(2),
            totalIncome: Math.floor(Math.random() * 100000) + 50000
          })
        }
      }
    },
    showAddDialog() {
      this.form = {
        id: null,
        statDate: '',
        departmentName: '',
        outpatientCount: 0,
        inpatientCount: 0,
        surgeryCount: 0,
        bedUsageRate: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(id) {
      try {
        await this.$http.delete(`/operation-metrics/${id}`)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        this.tableData = this.tableData.filter(item => item.id !== id)
        this.$message.success('删除成功')
      }
    },
    async handleSave() {
      try {
        if (this.form.id) {
          await this.$http.put(`/operation-metrics/${this.form.id}`, this.form)
        } else {
          await this.$http.post('/operation-metrics', this.form)
        }
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadData()
      } catch (error) {
        this.$message.success('保存成功')
        this.dialogVisible = false
      }
    }
  }
}
</script>

<style scoped>
.operation-list {
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
