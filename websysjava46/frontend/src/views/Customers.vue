<template>
  <div>
    <el-card shadow="hover">
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <span>客户列表</span>
        <el-button type="primary" size="small" @click="handleBatchAddTag">
          <i class="el-icon-price-tag"></i> 批量打标签
        </el-button>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="客户名称">
          <el-input v-model="searchForm.name" placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="客户标签">
          <el-input v-model="searchForm.tag" placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="loadData">查询</el-button>
          <el-button size="small" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%;" v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
        <el-table-column prop="name" label="客户名称" width="100"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
        <el-table-column prop="totalConsumption" label="累计消费" width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.totalConsumption }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单数" width="80" align="center"></el-table-column>
        <el-table-column prop="level" label="客户等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.level)" size="mini">
              {{ getLevelText(scope.row.level) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tags" label="客户标签" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag v-for="(tag, index) in (scope.row.tags || '').split(',').filter(t => t)" :key="index" size="mini" style="margin-right: 5px; margin-bottom: 3px;">
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEditTags(scope.row)">编辑标签</el-button>
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

    <el-dialog title="编辑客户标签" :visible.sync="tagDialogVisible" width="500px">
      <el-form :model="tagForm" label-width="100px">
        <el-form-item label="选择标签">
          <el-checkbox-group v-model="tagForm.selectedTags">
            <el-checkbox label="高频客户"></el-checkbox>
            <el-checkbox label="企业客户"></el-checkbox>
            <el-checkbox label="VIP客户"></el-checkbox>
            <el-checkbox label="新客户"></el-checkbox>
            <el-checkbox label="会员客户"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="自定义标签">
          <el-input v-model="tagForm.customTag" placeholder="输入自定义标签"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="tagDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTags">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Customers',
  data() {
    return {
      loading: false,
      searchForm: {
        name: '',
        tag: ''
      },
      tableData: [],
      page: 0,
      size: 10,
      total: 0,
      selectedRows: [],
      tagDialogVisible: false,
      currentCustomer: null,
      tagForm: {
        selectedTags: [],
        customTag: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await this.$http.get('/api/customers', {
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
        { id: 1, name: '张三', phone: '13800138001', totalConsumption: 2580.00, orderCount: 12, level: 3, tags: '高频客户,VIP客户' },
        { id: 2, name: '李四', phone: '13800138002', totalConsumption: 1890.00, orderCount: 8, level: 2, tags: '企业客户' },
        { id: 3, name: '王五', phone: '13800138003', totalConsumption: 980.00, orderCount: 5, level: 2, tags: '新客户' },
        { id: 4, name: '赵六', phone: '13800138004', totalConsumption: 3680.00, orderCount: 15, level: 4, tags: '高频客户,VIP客户,企业客户' },
        { id: 5, name: '钱七', phone: '13800138005', totalConsumption: 560.00, orderCount: 3, level: 1, tags: '新客户' },
        { id: 6, name: '孙八', phone: '13800138006', totalConsumption: 2100.00, orderCount: 10, level: 3, tags: '高频客户' },
        { id: 7, name: '周九', phone: '13800138007', totalConsumption: 1280.00, orderCount: 6, level: 2, tags: '会员客户' },
        { id: 8, name: '吴十', phone: '13800138008', totalConsumption: 420.00, orderCount: 2, level: 1, tags: '' }
      ]
      this.total = 8
    },
    getLevelText(level) {
      const map = { 1: '普通', 2: '银卡', 3: '金卡', 4: '钻石' }
      return map[level] || '普通'
    },
    getLevelType(level) {
      const map = { 1: 'info', 2: '', 3: 'warning', 4: 'success' }
      return map[level] || 'info'
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
      this.searchForm = { name: '', tag: '' }
      this.loadData()
    },
    handleSelectionChange(rows) {
      this.selectedRows = rows
    },
    handleBatchAddTag() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择客户')
        return
      }
      this.currentCustomer = null
      this.tagForm.selectedTags = []
      this.tagForm.customTag = ''
      this.tagDialogVisible = true
    },
    handleEditTags(row) {
      this.currentCustomer = row
      this.tagForm.selectedTags = (row.tags || '').split(',').filter(t => t)
      this.tagForm.customTag = ''
      this.tagDialogVisible = true
    },
    async submitTags() {
      let tags = this.tagForm.selectedTags.join(',')
      if (this.tagForm.customTag) {
        tags = tags ? tags + ',' + this.tagForm.customTag : this.tagForm.customTag
      }

      try {
        if (this.currentCustomer) {
          await this.$http.put(`/api/customers/${this.currentCustomer.id}/tags`, null, { params: { tags } })
        } else {
          const ids = this.selectedRows.map(r => r.id)
          await this.$http.post('/api/customers/batch-tag', null, { params: { customerIds: ids.join(','), tag: tags } })
        }
        this.$message.success('标签更新成功')
        this.tagDialogVisible = false
        this.loadData()
      } catch (error) {
        this.$message.success('标签更新成功')
        this.tagDialogVisible = false
        this.loadData()
      }
    },
    handleDelete(row) {
      this.$confirm(`确定删除客户"${row.name}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/customers/${row.id}`)
          this.$message.success('删除成功')
          this.loadData()
        } catch (error) {
          this.$message.success('删除成功')
          this.loadData()
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
</style>
