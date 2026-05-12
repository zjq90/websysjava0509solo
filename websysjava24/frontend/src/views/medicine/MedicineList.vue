<template>
  <div class="medicine-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>📋 药品字典管理</span>
        <el-button style="float: right; padding: 3px 0" type="primary" @click="handleAdd">新增药品</el-button>
      </div>
      
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.medicineName" placeholder="请输入药品名称"></el-input>
        </el-form-item>
        <el-form-item label="药品编码">
          <el-input v-model="searchForm.medicineCode" placeholder="请输入药品编码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="medicineCode" label="药品编码" width="120"></el-table-column>
        <el-table-column prop="medicineName" label="药品名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格" width="120"></el-table-column>
        <el-table-column prop="dosageForm" label="剂型" width="100"></el-table-column>
        <el-table-column prop="manufacturer" label="生产厂家" width="150"></el-table-column>
        <el-table-column prop="purchasePrice" label="采购价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.purchasePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="retailPrice" label="零售价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.retailPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right">
      </el-pagination>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="药品编码" prop="medicineCode">
          <el-input v-model="form.medicineCode" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="medicineName">
          <el-input v-model="form.medicineName"></el-input>
        </el-form-item>
        <el-form-item label="通用名" prop="genericName">
          <el-input v-model="form.genericName"></el-input>
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input v-model="form.specification"></el-input>
        </el-form-item>
        <el-form-item label="剂型" prop="dosageForm">
          <el-input v-model="form.dosageForm"></el-input>
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer">
          <el-input v-model="form.manufacturer"></el-input>
        </el-form-item>
        <el-form-item label="批准文号" prop="approvalNumber">
          <el-input v-model="form.approvalNumber"></el-input>
        </el-form-item>
        <el-form-item label="采购价格" prop="purchasePrice">
          <el-input-number v-model="form.purchasePrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="零售价格" prop="retailPrice">
          <el-input-number v-model="form.retailPrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="form.status" :label="1">启用</el-radio>
          <el-radio v-model="form.status" :label="0">停用</el-radio>
        </el-form-item>
        <el-form-item label="是否处方药" prop="isPrescription">
          <el-radio v-model="form.isPrescription" :label="true">是</el-radio>
          <el-radio v-model="form.isPrescription" :label="false">否</el-radio>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MedicineList',
  data() {
    return {
      loading: false,
      searchForm: {
        medicineName: '',
        medicineCode: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增药品',
      isEdit: false,
      form: {
        id: null,
        medicineCode: '',
        medicineName: '',
        genericName: '',
        specification: '',
        dosageForm: '',
        manufacturer: '',
        approvalNumber: '',
        purchasePrice: 0,
        retailPrice: 0,
        category: '',
        storageCondition: '常温',
        status: 1,
        isPrescription: true,
        remark: ''
      },
      rules: {
        medicineCode: [{ required: true, message: '请输入药品编码', trigger: 'blur' }],
        medicineName: [{ required: true, message: '请输入药品名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.$http.get('/medicine/page', {
        params: {
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
      }).then(response => {
        console.log('药品数据:', response.data)
        if (response.data && response.data.content) {
          this.tableData = response.data.content
          this.pagination.total = response.data.totalElements
        } else if (Array.isArray(response.data)) {
          this.tableData = response.data
          this.pagination.total = response.data.length
        }
        this.loading = false
      }).catch((error) => {
        console.error('加载药品数据失败:', error)
        this.loading = false
        this.loadMockData()
      })
    },
    loadMockData() {
      this.tableData = [
        { id: 1, medicineCode: 'MED001', medicineName: '阿莫西林胶囊', specification: '0.25g*24粒', dosageForm: '胶囊剂', manufacturer: '华北制药', purchasePrice: 15.50, retailPrice: 25.00, category: '抗生素', status: 1, isPrescription: true },
        { id: 2, medicineCode: 'MED002', medicineName: '布洛芬缓释胶囊', specification: '0.3g*20粒', dosageForm: '胶囊剂', manufacturer: '中美史克', purchasePrice: 18.00, retailPrice: 28.50, category: '解热镇痛', status: 1, isPrescription: false },
        { id: 3, medicineCode: 'MED003', medicineName: '头孢克肟分散片', specification: '0.1g*6片', dosageForm: '片剂', manufacturer: '广州白云山', purchasePrice: 22.00, retailPrice: 35.00, category: '抗生素', status: 1, isPrescription: true },
        { id: 4, medicineCode: 'MED004', medicineName: '奥美拉唑肠溶胶囊', specification: '20mg*14粒', dosageForm: '胶囊剂', manufacturer: '奥赛康药业', purchasePrice: 45.00, retailPrice: 68.00, category: '消化系统', status: 1, isPrescription: true },
        { id: 5, medicineCode: 'MED005', medicineName: '维生素C片', specification: '0.1g*100片', dosageForm: '片剂', manufacturer: '东北制药', purchasePrice: 3.50, retailPrice: 6.00, category: '维生素', status: 1, isPrescription: false }
      ]
      this.pagination.total = this.tableData.length
    },
    handleSearch() {
      this.pagination.page = 1
      this.loading = true
      this.$http.get('/medicine', {
        params: this.searchForm
      }).then(response => {
        let data = response.data
        if (this.searchForm.medicineName) {
          data = data.filter(item => item.medicineName.includes(this.searchForm.medicineName))
        }
        if (this.searchForm.medicineCode) {
          data = data.filter(item => item.medicineCode.includes(this.searchForm.medicineCode))
        }
        this.tableData = data
        this.pagination.total = data.length
        this.loading = false
      })
    },
    handleReset() {
      this.searchForm = {
        medicineName: '',
        medicineCode: ''
      }
      this.pagination.page = 1
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增药品'
      this.isEdit = false
      this.form = {
        id: null,
        medicineCode: '',
        medicineName: '',
        genericName: '',
        specification: '',
        dosageForm: '',
        manufacturer: '',
        approvalNumber: '',
        purchasePrice: 0,
        retailPrice: 0,
        category: '',
        storageCondition: '常温',
        status: 1,
        isPrescription: true,
        remark: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑药品'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该药品吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/medicine/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            this.$http.put(`/medicine/${this.form.id}`, this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            this.$http.post('/medicine', this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.pagination.page = page
      this.fetchData()
    }
  }
}
</script>

<style scoped>
.medicine-list {
  padding: 20px;
}
</style>
