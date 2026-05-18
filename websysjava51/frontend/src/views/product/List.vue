<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">商品列表</h2>
      <div>
        <el-button type="primary" icon="el-icon-upload2" @click="handleImport">批量导入</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">批量导出</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增商品</el-button>
      </div>
    </div>
    <div class="table-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.category" placeholder="请选择分类" style="width: 150px;" clearable>
            <el-option label="电子产品" value="电子产品"></el-option>
            <el-option label="服装" value="服装"></el-option>
            <el-option label="家居" value="家居"></el-option>
            <el-option label="图书" value="图书"></el-option>
            <el-option label="运动" value="运动"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px;" clearable>
            <el-option label="在售" value="ON_SALE"></el-option>
            <el-option label="下架" value="OFF_SHELF"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="mb-20" style="padding: 10px; background: #f5f7fa; border-radius: 4px;">
        <el-checkbox v-model="isAllSelected" @change="handleSelectAll">全选</el-checkbox>
        <el-button type="warning" size="small" icon="el-icon-folder-remove" :disabled="selectedIds.length === 0" @click="batchOffShelf">批量下架</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="selectedIds.length === 0" @click="batchDelete">批量删除</el-button>
        <span style="margin-left: 20px; color: #909399;">已选择 <span style="color: #409EFF; font-weight: bold;">{{ selectedIds.length }}</span> 项</span>
      </div>
      <el-table
        ref="table"
        :data="tableData"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="200"></el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100">
          <template slot-scope="scope">
            <span :class="scope.row.stock < 10 ? 'text-danger' : ''">{{ scope.row.stock }}</span>
            <el-tag v-if="scope.row.stock < 10" size="mini" type="danger" style="margin-left: 5px;">预警</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="condition" label="成色" width="100">
          <template slot-scope="scope">
            {{ getConditionText(scope.row.condition) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ON_SALE' ? 'success' : 'info'" size="small">
              {{ scope.row.status === 'ON_SALE' ? '在售' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog title="导入商品" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        ref="upload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ProductList',
  data() {
    return {
      searchForm: {
        name: '',
        category: '',
        status: ''
      },
      tableData: [],
      selectedIds: [],
      isAllSelected: false,
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      importDialogVisible: false
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const params = {
          name: this.searchForm.name,
          category: this.searchForm.category,
          status: this.searchForm.status,
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
        const res = await request.get('/products', { params })
        if (res.code === 200) {
          this.tableData = res.data.content
          this.pagination.total = res.data.totalElements
        }
      } catch (error) {
        console.error('获取商品列表失败:', error)
        this.$message.error('获取商品列表失败')
      }
    },
    handleSearch() {
      this.pagination.page = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        name: '',
        category: '',
        status: ''
      }
      this.pagination.page = 1
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
      this.isAllSelected = this.selectedIds.length === this.tableData.length && this.tableData.length > 0
    },
    handleSelectAll(val) {
      if (val) {
        this.$refs.table.toggleAllSelection()
      } else {
        this.$refs.table.clearSelection()
      }
    },
    async batchOffShelf() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请先选择要下架的商品')
        return
      }
      this.$confirm(`确定要下架选中的${this.selectedIds.length}件商品吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request.post('/products/batch/off-shelf', this.selectedIds)
          if (res.code === 200) {
            this.$message.success(res.message || '批量下架成功')
            this.$refs.table.clearSelection()
            this.fetchData()
          } else {
            this.$message.error(res.message || '批量下架失败')
          }
        } catch (error) {
          console.error('批量下架失败:', error)
          this.$message.error('批量下架失败')
        }
      })
    },
    async batchDelete() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请先选择要删除的商品')
        return
      }
      this.$confirm(`确定要删除选中的${this.selectedIds.length}件商品吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await request.post('/products/batch/delete', this.selectedIds)
          if (res.code === 200) {
            this.$message.success(res.message || '批量删除成功')
            this.$refs.table.clearSelection()
            this.fetchData()
          } else {
            this.$message.error(res.message || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      })
    },
    handleAdd() {
      this.$message.info('新增商品功能')
    },
    handleEdit(row) {
      this.$message.info(`编辑商品: ${row.name}`)
    },
    handleDelete(row) {
      this.$confirm(`确定要删除商品${row.name}吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      })
    },
    handleImport() {
      this.importDialogVisible = true
    },
    confirmImport() {
      this.$message.success('批量导入成功')
      this.importDialogVisible = false
    },
    handleExport() {
      this.$message.success('批量导出成功')
    },
    handleSizeChange(size) {
      this.pagination.size = size
    },
    handleCurrentChange(page) {
      this.pagination.page = page
    },
    getConditionText(condition) {
      const conditionMap = {
        'NEW': '全新',
        'LIKE_NEW': '99新',
        'EXCELLENT': '95新',
        'GOOD': '9成新',
        'FAIR': '8成新'
      }
      return conditionMap[condition] || condition
    }
  }
}
</script>

<style scoped>
</style>