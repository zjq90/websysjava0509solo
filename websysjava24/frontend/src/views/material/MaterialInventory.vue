<template>
  <div class="material-inventory">
    <el-card>
      <div slot="header" class="clearfix">
        <span>📦 物资库存管理</span>
      </div>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="6">
          <el-statistic title="物资总数" :value="stats.total" value-style="color: #409EFF"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="高值耗材" :value="stats.highValue" value-style="color: #E6A23C"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="近效期物资" :value="stats.nearExpiry" value-style="color: #F56C6C"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="库存总金额" :value="stats.totalAmount" :precision="2" suffix="元" value-style="color: #67C23A"></el-statistic>
        </el-col>
      </el-row>

      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="materialCode" label="物资编码" width="100"></el-table-column>
        <el-table-column prop="materialName" label="物资名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格型号" width="120"></el-table-column>
        <el-table-column prop="batchNumber" label="批号" width="100"></el-table-column>
        <el-table-column prop="uniqueIdentifier" label="唯一标识" width="150" v-if="showHighValue">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.uniqueIdentifier" size="small">{{ scope.row.uniqueIdentifier }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="productionDate" label="生产日期" width="120"></el-table-column>
        <el-table-column prop="expiryDate" label="有效期至" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.expiryDate">{{ scope.row.expiryDate }}</span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="库存数量" width="100" sortable></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="usePrice" label="领用单价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.usePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="warehouseLocation" label="库位" width="100"></el-table-column>
        <el-table-column prop="inventoryStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.inventoryStatus)" size="small">
              {{ getStatusText(scope.row.inventoryStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isHighValue" label="高值耗材" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isHighValue ? 'warning' : 'success'" size="small">
              {{ scope.row.isHighValue ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MaterialInventory',
  data() {
    return {
      loading: false,
      showHighValue: true,
      tableData: [],
      stats: {
        total: 0,
        highValue: 0,
        nearExpiry: 0,
        totalAmount: 0
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.$http.get('/material').then(() => {
        this.$http.get('/material/inventory').catch(() => {}).finally(() => {
          this.loadMockData()
          this.loading = false
        })
      }).catch(() => {
        this.loadMockData()
        this.loading = false
      })
    },
    loadMockData() {
      this.tableData = [
        { materialCode: 'MAT001', materialName: '一次性使用无菌注射器', specification: '5ml', batchNumber: 'BATCH001', uniqueIdentifier: '', productionDate: '2024-02-15', expiryDate: '2027-02-14', quantity: 2000, unit: '支', usePrice: 4.00, warehouseLocation: 'B区-001', inventoryStatus: 0, isHighValue: false },
        { materialCode: 'MAT002', materialName: '一次性医用口罩', specification: '耳挂式', batchNumber: 'BATCH002', uniqueIdentifier: '', productionDate: '2024-03-20', expiryDate: '2026-03-19', quantity: 5000, unit: '个', usePrice: 1.50, warehouseLocation: 'B区-002', inventoryStatus: 0, isHighValue: false },
        { materialCode: 'MAT003', materialName: '医用手套', specification: 'M号', batchNumber: 'BATCH003', uniqueIdentifier: '', productionDate: '2024-01-10', expiryDate: '2026-01-09', quantity: 1000, unit: '副', usePrice: 2.00, warehouseLocation: 'B区-003', inventoryStatus: 0, isHighValue: false },
        { materialCode: 'MAT004', materialName: '心脏支架', specification: '药物洗脱支架', batchNumber: 'BATCH004', uniqueIdentifier: 'UID-SN2024001', productionDate: '2024-04-01', expiryDate: '2026-03-31', quantity: 50, unit: '个', usePrice: 1800.00, warehouseLocation: 'C区-001', inventoryStatus: 0, isHighValue: true },
        { materialCode: 'MAT005', materialName: '人工髋关节', specification: '陶瓷型', batchNumber: 'BATCH005', uniqueIdentifier: 'UID-SN2024002', productionDate: '2024-03-15', expiryDate: '2029-03-14', quantity: 20, unit: '套', usePrice: 12000.00, warehouseLocation: 'C区-002', inventoryStatus: 0, isHighValue: true }
      ]
      this.stats.total = this.tableData.length
      this.stats.highValue = this.tableData.filter(item => item.isHighValue).length
      this.stats.nearExpiry = this.tableData.filter(item => item.inventoryStatus === 1).length
      this.stats.totalAmount = this.tableData.reduce((sum, item) => sum + item.quantity * item.usePrice, 0)
    },
    getStatusType(status) {
      const types = ['success', 'warning', 'danger', 'info']
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = ['正常', '近效期', '已过期', '滞销']
      return texts[status] || '未知'
    }
  }
}
</script>

<style scoped>
.material-inventory {
  padding: 20px;
}
.text-muted {
  color: #909399;
}
</style>
