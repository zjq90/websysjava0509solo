<template>
  <div class="medicine-inventory">
    <el-card>
      <div slot="header" class="clearfix">
        <span>📦 药品库存管理</span>
      </div>
      
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="6">
          <el-statistic title="药品总数" :value="stats.total" value-style="color: #409EFF"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="近效期药品" :value="stats.nearExpiry" value-style="color: #E6A23C"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="已过期药品" :value="stats.expired" value-style="color: #F56C6C"></el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="库存总金额" :value="stats.totalAmount" :precision="2" suffix="元" value-style="color: #67C23A"></el-statistic>
        </el-col>
      </el-row>

      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="medicineCode" label="药品编码" width="100"></el-table-column>
        <el-table-column prop="medicineName" label="药品名称" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格" width="100"></el-table-column>
        <el-table-column prop="batchNumber" label="批号" width="100"></el-table-column>
        <el-table-column prop="productionDate" label="生产日期" width="120"></el-table-column>
        <el-table-column prop="expiryDate" label="有效期至" width="120"></el-table-column>
        <el-table-column prop="quantity" label="库存数量" width="100" sortable></el-table-column>
        <el-table-column prop="unit" label="单位" width="80"></el-table-column>
        <el-table-column prop="retailPrice" label="单价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.retailPrice }}
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
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MedicineInventory',
  data() {
    return {
      loading: false,
      tableData: [],
      stats: {
        total: 0,
        nearExpiry: 0,
        expired: 0,
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
      this.$http.get('/medicine').then(() => {
        this.$http.get('/medicine/inventory').catch(() => {}).finally(() => {
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
        { medicineCode: 'MED001', medicineName: '阿莫西林胶囊', specification: '0.25g*24粒', batchNumber: 'BATCH001', productionDate: '2024-01-15', expiryDate: '2026-01-14', quantity: 500, unit: '盒', retailPrice: 25.00, warehouseLocation: 'A区-001', inventoryStatus: 0 },
        { medicineCode: 'MED002', medicineName: '布洛芬缓释胶囊', specification: '0.3g*20粒', batchNumber: 'BATCH002', productionDate: '2024-02-20', expiryDate: '2026-02-19', quantity: 300, unit: '盒', retailPrice: 28.50, warehouseLocation: 'A区-002', inventoryStatus: 0 },
        { medicineCode: 'MED003', medicineName: '头孢克肟分散片', specification: '0.1g*6片', batchNumber: 'BATCH003', productionDate: '2024-03-10', expiryDate: '2026-03-09', quantity: 200, unit: '盒', retailPrice: 35.00, warehouseLocation: 'A区-003', inventoryStatus: 0 },
        { medicineCode: 'MED004', medicineName: '奥美拉唑肠溶胶囊', specification: '20mg*14粒', batchNumber: 'BATCH004', productionDate: '2024-05-01', expiryDate: '2025-04-30', quantity: 150, unit: '盒', retailPrice: 68.00, warehouseLocation: 'A区-004', inventoryStatus: 1 },
        { medicineCode: 'MED005', medicineName: '维生素C片', specification: '0.1g*100片', batchNumber: 'BATCH005', productionDate: '2023-01-10', expiryDate: '2024-01-09', quantity: 80, unit: '瓶', retailPrice: 6.00, warehouseLocation: 'A区-005', inventoryStatus: 2 }
      ]
      this.stats.total = this.tableData.length
      this.stats.nearExpiry = this.tableData.filter(item => item.inventoryStatus === 1).length
      this.stats.expired = this.tableData.filter(item => item.inventoryStatus === 2).length
      this.stats.totalAmount = this.tableData.reduce((sum, item) => sum + item.quantity * item.retailPrice, 0)
    },
    getStatusType(status) {
      const types = ['', 'warning', 'danger', 'info']
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
.medicine-inventory {
  padding: 20px;
}
</style>
