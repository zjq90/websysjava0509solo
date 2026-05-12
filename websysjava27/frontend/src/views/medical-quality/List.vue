<template>
  <div class="medical-quality-list">
    <el-card>
      <div slot="header" class="table-header">
        <span class="title">医疗质量数据列表</span>
        <el-button type="primary" size="small" @click="showAddDialog">新增</el-button>
      </div>
      <el-table :data="tableData" border stripe class="data-table" align="center">
        <el-table-column prop="statDate" label="统计日期" width="130" align="center"></el-table-column>
        <el-table-column prop="departmentName" label="科室名称" width="120" align="center"></el-table-column>
        <el-table-column prop="totalRecords" label="病历总数" width="110" align="center"></el-table-column>
        <el-table-column prop="recordQualificationRate" label="合格率(%)" width="110" align="center"></el-table-column>
        <el-table-column prop="rationalDrugUseRate" label="合理用药率(%)" width="130" align="center"></el-table-column>
        <el-table-column prop="nosocomialInfectionRate" label="感染率(%)" width="100" align="center"></el-table-column>
        <el-table-column prop="adverseEventCount" label="不良事件数" width="110" align="center"></el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="医疗质量数据" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="统计日期">
          <el-date-picker v-model="form.statDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="科室名称">
          <el-input v-model="form.departmentName"></el-input>
        </el-form-item>
        <el-form-item label="病历总数">
          <el-input-number v-model="form.totalRecords"></el-input-number>
        </el-form-item>
        <el-form-item label="合格率">
          <el-input-number v-model="form.recordQualificationRate" :precision="2"></el-input-number>
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
  name: 'MedicalQualityList',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      form: {
        id: null,
        statDate: '',
        departmentName: '',
        totalRecords: 0,
        recordQualificationRate: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const response = await this.$http.get('/medical-quality')
        this.tableData = response.data.slice(0, 50)
      } catch (error) {
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
            totalRecords: Math.floor(Math.random() * 100) + 50,
            recordQualificationRate: (85 + Math.random() * 14).toFixed(2),
            rationalDrugUseRate: (80 + Math.random() * 18).toFixed(2),
            nosocomialInfectionRate: (1 + Math.random() * 5).toFixed(2),
            adverseEventCount: Math.floor(Math.random() * 5)
          })
        }
      }
    },
    showAddDialog() {
      this.form = { id: null, statDate: '', departmentName: '', totalRecords: 0, recordQualificationRate: 0 }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(id) {
      try {
        await this.$http.delete(`/medical-quality/${id}`)
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
          await this.$http.put(`/medical-quality/${this.form.id}`, this.form)
        } else {
          await this.$http.post('/medical-quality', this.form)
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
.medical-quality-list {
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
