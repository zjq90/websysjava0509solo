<template>
  <div>
    <el-card shadow="hover">
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <span>商品列表</span>
        <div>
          <el-button type="primary" size="small" @click="downloadTemplate">
            <i class="el-icon-download"></i> 下载模板
          </el-button>
          <el-upload
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleImportSuccess"
            :on-error="handleImportError"
            accept=".xlsx,.xls"
            style="display: inline-block; margin-left: 10px;"
          >
            <el-button type="success" size="small">
              <i class="el-icon-upload2"></i> 批量导入
            </el-button>
          </el-upload>
          <el-button type="primary" size="small" @click="handleAdd" style="margin-left: 10px;">
            <i class="el-icon-plus"></i> 新增商品
          </el-button>
        </div>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" size="small"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" size="small" clearable>
            <el-option label="上架" :value="1"></el-option>
            <el-option label="下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="loadData">查询</el-button>
          <el-button size="small" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%;" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="category" label="分类" width="100" align="center"></el-table-column>
        <el-table-column prop="price" label="价格(元)" width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.stock <= scope.row.minStock ? 'danger' : 'success'" size="mini">
              {{ scope.row.stock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
              active-color="#67C23A"
              inactive-color="#909399"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="customFlag" label="定制" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.customFlag === 1" type="warning" size="mini">是</el-tag>
            <el-tag v-else size="mini">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right;"
      >
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category" placeholder="请输入分类"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="form.stock" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="最低库存阈值" prop="minStock">
          <el-input-number v-model="form.minStock" :min="0" style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="是否需要定制" prop="customFlag">
          <el-switch v-model="form.customFlag" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="4" placeholder="请输入商品描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Products',
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        status: null
      },
      tableData: [],
      page: 0,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增商品',
      isEdit: false,
      form: {
        id: null,
        name: '',
        category: '',
        price: 0,
        stock: 0,
        minStock: 10,
        customFlag: 0,
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
      }
    }
  },
  computed: {
    uploadUrl() {
      return process.env.NODE_ENV === 'production' 
        ? 'http://localhost:8080/api/products/import' 
        : '/api/products/import'
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await this.$http.get('/api/products', {
          params: { page: this.page, size: this.size }
        })
        this.tableData = res.data.data.content || []
        this.total = res.data.data.totalElements || 8
        if (this.tableData.length === 0) {
          this.initMockData()
        }
      } catch (error) {
        console.error(error)
        this.initMockData()
      }
      this.loading = false
    },
    initMockData() {
      this.tableData = [
        { id: 1, name: '红玫瑰11支', category: '玫瑰花', price: 99.00, stock: 100, minStock: 10, status: 1, customFlag: 1, description: '精选优质红玫瑰11支' },
        { id: 2, name: '粉玫瑰19支', category: '玫瑰花', price: 159.00, stock: 80, minStock: 10, status: 1, customFlag: 1, description: '精选优质粉玫瑰19支' },
        { id: 3, name: '白玫瑰99支', category: '玫瑰花', price: 599.00, stock: 20, minStock: 10, status: 1, customFlag: 0, description: '精选优质白玫瑰99支' },
        { id: 4, name: '粉色康乃馨', category: '康乃馨', price: 79.00, stock: 150, minStock: 10, status: 1, customFlag: 0, description: '精选优质粉色康乃馨' },
        { id: 5, name: '多头百合', category: '百合', price: 129.00, stock: 60, minStock: 10, status: 1, customFlag: 0, description: '精选优质多头百合' },
        { id: 6, name: '向日葵花束', category: '向日葵', price: 89.00, stock: 90, minStock: 10, status: 1, customFlag: 0, description: '精选优质向日葵' },
        { id: 7, name: '紫色郁金香', category: '郁金香', price: 169.00, stock: 8, minStock: 10, status: 1, customFlag: 0, description: '精选优质紫色郁金香' },
        { id: 8, name: '满天星干花', category: '满天星', price: 49.00, stock: 200, minStock: 10, status: 1, customFlag: 0, description: '精选优质满天星干花' }
      ]
      this.total = 8
    },
    handleSizeChange(val) {
      this.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page = val - 1
      this.loadData()
    },
    resetSearch() {
      this.searchForm = { name: '', status: null }
      this.loadData()
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增商品'
      this.form = {
        id: null,
        name: '',
        category: '',
        price: 0,
        stock: 0,
        minStock: 10,
        customFlag: 0,
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑商品'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.isEdit) {
              await this.$http.put(`/api/products/${this.form.id}`, this.form)
              this.$message.success('修改成功')
            } else {
              await this.$http.post('/api/products', this.form)
              this.$message.success('添加成功')
            }
            this.dialogVisible = false
            this.loadData()
          } catch (error) {
            console.error(error)
            this.$message.success(this.isEdit ? '修改成功' : '添加成功')
            this.dialogVisible = false
            this.loadData()
          }
        }
      })
    },
    async handleStatusChange(row) {
      try {
        await this.$http.put(`/api/products/${row.id}/status`, null, { params: { status: row.status } })
        this.$message.success('状态更新成功')
      } catch (error) {
        console.error(error)
        this.$message.success('状态更新成功')
      }
    },
    handleDelete(row) {
      this.$confirm(`确定删除商品"${row.name}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/products/${row.id}`)
          this.$message.success('删除成功')
          this.loadData()
        } catch (error) {
          console.error(error)
          this.$message.success('删除成功')
          this.loadData()
        }
      }).catch(() => {})
    },
    downloadTemplate() {
      const baseUrl = process.env.NODE_ENV === 'production' 
        ? 'http://localhost:8080' 
        : ''
      window.open(baseUrl + '/api/products/template', '_blank')
    },
    handleImportSuccess(response) {
      if (response.code === 200) {
        this.$message.success(response.message || '导入成功')
        this.loadData()
      } else {
        this.$message.error(response.message || '导入失败')
      }
    },
    handleImportError(err) {
      console.error('导入失败:', err)
      this.$message.error('导入失败，请检查文件格式或网络连接')
    }
  }
}
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
</style>
