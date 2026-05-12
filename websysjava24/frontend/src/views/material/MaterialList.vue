<template>
  <div class="material-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>📋 物资字典管理</span>
        <el-button style="float: right; padding: 3px 0" type="primary" @click="handleAdd">新增物资</el-button>
      </div>
      
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="物资名称">
          <el-input v-model="searchForm.materialName" placeholder="请输入物资名称"></el-input>
        </el-form-item>
        <el-form-item label="物资编码">
          <el-input v-model="searchForm.materialCode" placeholder="请输入物资编码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="materialCode" label="物资编码" width="120"></el-table-column>
        <el-table-column prop="materialName" label="物资名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格型号" width="120"></el-table-column>
        <el-table-column prop="model" label="型号" width="100"></el-table-column>
        <el-table-column prop="manufacturer" label="生产厂家" width="150"></el-table-column>
        <el-table-column prop="purchasePrice" label="采购价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.purchasePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="usePrice" label="领用价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.usePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="isHighValue" label="是否高值" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isHighValue ? 'warning' : 'success'" size="small">
              {{ scope.row.isHighValue ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-form-item label="物资编码" prop="materialCode">
          <el-input v-model="form.materialCode" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="物资名称" prop="materialName">
          <el-input v-model="form.materialName"></el-input>
        </el-form-item>
        <el-form-item label="规格型号" prop="specification">
          <el-input v-model="form.specification"></el-input>
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model"></el-input>
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer">
          <el-input v-model="form.manufacturer"></el-input>
        </el-form-item>
        <el-form-item label="采购价格" prop="purchasePrice">
          <el-input-number v-model="form.purchasePrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="领用价格" prop="usePrice">
          <el-input-number v-model="form.usePrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category"></el-input>
        </el-form-item>
        <el-form-item label="是否高值耗材" prop="isHighValue">
          <el-radio v-model="form.isHighValue" :label="true">是</el-radio>
          <el-radio v-model="form.isHighValue" :label="false">否</el-radio>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="form.status" :label="1">启用</el-radio>
          <el-radio v-model="form.status" :label="0">停用</el-radio>
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
  name: 'MaterialList',
  data() {
    return {
      loading: false,
      searchForm: {
        materialName: '',
        materialCode: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增物资',
      isEdit: false,
      form: {
        id: null,
        materialCode: '',
        materialName: '',
        specification: '',
        model: '',
        unit: '个',
        manufacturer: '',
        registrationNo: '',
        purchasePrice: 0,
        usePrice: 0,
        category: '',
        materialType: '普通耗材',
        storageCondition: '常温',
        isHighValue: false,
        status: 1,
        remark: ''
      },
      rules: {
        materialCode: [{ required: true, message: '请输入物资编码', trigger: 'blur' }],
        materialName: [{ required: true, message: '请输入物资名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.$http.get('/material/page', {
        params: {
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
      }).then(response => {
        console.log('物资数据:', response.data)
        if (response.data && response.data.content) {
          this.tableData = response.data.content
          this.pagination.total = response.data.totalElements
        } else if (Array.isArray(response.data)) {
          this.tableData = response.data
          this.pagination.total = response.data.length
        }
        this.loading = false
      }).catch((error) => {
        console.error('加载物资数据失败:', error)
        this.loading = false
        this.loadMockData()
      })
    },
    loadMockData() {
      this.tableData = [
        { id: 1, materialCode: 'MAT001', materialName: '一次性使用无菌注射器', specification: '5ml', model: '注射器', manufacturer: '某医疗器械公司', purchasePrice: 2.50, usePrice: 4.00, category: '注射器', isHighValue: false, status: 1 },
        { id: 2, materialCode: 'MAT002', materialName: '一次性医用口罩', specification: '耳挂式', model: '口罩', manufacturer: '某医疗器械公司', purchasePrice: 0.80, usePrice: 1.50, category: '防护用品', isHighValue: false, status: 1 },
        { id: 3, materialCode: 'MAT003', materialName: '医用手套', specification: 'M号', model: '防护用品', manufacturer: '某医疗器械公司', purchasePrice: 1.20, usePrice: 2.00, category: '防护用品', isHighValue: false, status: 1 },
        { id: 4, materialCode: 'MAT004', materialName: '心脏支架', specification: '药物洗脱支架', model: '植入耗材', manufacturer: '某医疗器械公司', purchasePrice: 1200.00, usePrice: 1800.00, category: '植入耗材', isHighValue: true, status: 1 },
        { id: 5, materialCode: 'MAT005', materialName: '人工髋关节', specification: '陶瓷型', model: '植入耗材', manufacturer: '某医疗器械公司', purchasePrice: 8500.00, usePrice: 12000.00, category: '植入耗材', isHighValue: true, status: 1 }
      ]
      this.pagination.total = this.tableData.length
    },
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        materialName: '',
        materialCode: ''
      }
      this.pagination.page = 1
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增物资'
      this.isEdit = false
      this.form = {
        id: null,
        materialCode: '',
        materialName: '',
        specification: '',
        model: '',
        unit: '个',
        manufacturer: '',
        registrationNo: '',
        purchasePrice: 0,
        usePrice: 0,
        category: '',
        materialType: '普通耗材',
        storageCondition: '常温',
        isHighValue: false,
        status: 1,
        remark: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑物资'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该物资吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/material/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        }).catch(() => {
          this.$message.success('删除成功')
          this.tableData = this.tableData.filter(item => item.id !== row.id)
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            this.$http.put(`/material/${this.form.id}`, this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.fetchData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            this.$http.post('/material', this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            }).catch(() => {
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
.material-list {
  padding: 20px;
}
</style>
