<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">库存预警</h2>
      <div>
        <el-button type="primary" icon="el-icon-setting" @click="openThresholdDialog">设置预警阈值</el-button>
      </div>
    </div>
    <div class="table-container">
      <el-alert
        title="以下商品库存低于预警阈值，请及时补货"
        type="warning"
        :closable="false"
        style="margin-bottom: 20px;"
      >
      </el-alert>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="200"></el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="当前库存" width="120">
          <template slot-scope="scope">
            <el-tag type="danger" size="small">{{ scope.row.stock }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="threshold" label="预警阈值" width="100">
          <template slot-scope="scope">
            {{ scope.row.threshold || 10 }}
          </template>
        </el-table-column>
        <el-table-column prop="condition" label="成色" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEditStock(scope.row)">调整库存</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog title="设置预警阈值" :visible.sync="thresholdDialogVisible" width="400px">
      <el-form label-width="120px">
        <el-form-item label="默认预警阈值">
          <el-input-number v-model="defaultThreshold" :min="1" :max="100"></el-input-number>
          <span style="margin-left: 10px; color: #909399;">件</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="thresholdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveThreshold">保存</el-button>
      </div>
    </el-dialog>
    <el-dialog title="调整库存" :visible.sync="stockDialogVisible" width="400px">
      <el-form label-width="100px">
        <el-form-item label="商品名称">
          <span>{{ currentProduct.name }}</span>
        </el-form-item>
        <el-form-item label="当前库存">
          <span>{{ currentProduct.stock }}</span>
        </el-form-item>
        <el-form-item label="调整数量">
          <el-input-number v-model="adjustStock" :min="1" :max="1000"></el-input-number>
        </el-form-item>
        <el-form-item label="调整类型">
          <el-radio-group v-model="adjustType">
            <el-radio label="add">增加库存</el-radio>
            <el-radio label="set">设置为</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stockDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveStock">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'LowStock',
  data() {
    return {
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      thresholdDialogVisible: false,
      defaultThreshold: 10,
      stockDialogVisible: false,
      currentProduct: {},
      adjustStock: 0,
      adjustType: 'add'
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const mockData = []
      for (let i = 1; i <= 8; i++) {
        const categories = ['电子产品', '服装', '家居', '图书', '运动']
        const conditions = ['全新', '99新', '95新', '9成新', '8成新']
        mockData.push({
          id: i,
          name: `测试商品${i}`,
          category: categories[i % categories.length],
          price: (10 + i * 5).toFixed(2),
          stock: i,
          threshold: 10,
          condition: conditions[i % conditions.length],
          createTime: '2024-01-' + String(i).padStart(2, '0') + ' 10:00:00'
        })
      }
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    openThresholdDialog() {
      this.thresholdDialogVisible = true
    },
    saveThreshold() {
      this.$message.success(`预警阈值已设置为 ${this.defaultThreshold} 件`)
      this.thresholdDialogVisible = false
    },
    handleEditStock(row) {
      this.currentProduct = { ...row }
      this.adjustStock = 0
      this.adjustType = 'add'
      this.stockDialogVisible = true
    },
    saveStock() {
      if (this.adjustType === 'add') {
        this.currentProduct.stock += this.adjustStock
      } else {
        this.currentProduct.stock = this.adjustStock
      }
      const index = this.tableData.findIndex(item => item.id === this.currentProduct.id)
      if (index !== -1) {
        this.tableData[index] = { ...this.currentProduct }
      }
      this.$message.success('库存调整成功')
      this.stockDialogVisible = false
    },
    handleSizeChange(size) {
      this.pagination.size = size
    },
    handleCurrentChange(page) {
      this.pagination.page = page
    }
  }
}
</script>

<style scoped>
</style>