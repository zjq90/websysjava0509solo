<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">订单列表</h2>
    </div>
    <div class="table-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 150px;" clearable>
            <el-option label="待付款" value="PENDING_PAYMENT"></el-option>
            <el-option label="待发货" value="PENDING_SHIPMENT"></el-option>
            <el-option label="已发货" value="SHIPPED"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
            <el-option label="退款中" value="REFUNDING"></el-option>
            <el-option label="已退款" value="REFUNDED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="异常订单">
          <el-select v-model="searchForm.isAbnormal" placeholder="请选择" style="width: 120px;" clearable>
            <el-option label="是" :value="true"></el-option>
            <el-option label="否" :value="false"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="mb-20" style="padding: 10px; background: #f5f7fa; border-radius: 4px;">
        <el-checkbox v-model="isAllSelected" @change="handleSelectAll">全选</el-checkbox>
        <el-button type="success" size="small" icon="el-icon-check" :disabled="selectedIds.length === 0" @click="batchShip">批量发货</el-button>
        <el-button type="warning" size="small" icon="el-icon-refresh-left" :disabled="selectedIds.length === 0" @click="batchRefund">批量退款</el-button>
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
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="productName" label="商品名称" min-width="180"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="totalAmount" label="金额" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isAbnormal" label="是否异常" width="100">
          <template slot-scope="scope">
            <i v-if="scope.row.isAbnormal" class="el-icon-warning" style="color: #F56C6C; font-size: 18px;" title="异常订单"></i>
            <span v-else style="color: #67C23A;">正常</span>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" width="100"></el-table-column>
        <el-table-column prop="receiverPhone" label="电话" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-truck" @click="handleEditLogistics(scope.row)">物流</el-button>
            <el-button size="mini" type="warning" icon="el-icon-warning" @click="handleMarkAbnormal(scope.row)">标记异常</el-button>
            <el-button size="mini" type="info" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
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
    <el-dialog title="编辑物流信息" :visible.sync="logisticsDialogVisible" width="500px">
      <el-form label-width="100px">
        <el-form-item label="物流公司">
          <el-input v-model="logisticsForm.company" placeholder="请输入物流公司"></el-input>
        </el-form-item>
        <el-form-item label="物流单号">
          <el-input v-model="logisticsForm.trackingNumber" placeholder="请输入物流单号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="logisticsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveLogistics">保存</el-button>
      </div>
    </el-dialog>
    <el-dialog title="标记异常订单" :visible.sync="abnormalDialogVisible" width="500px">
      <el-form label-width="100px">
        <el-form-item label="异常原因">
          <el-input
            v-model="abnormalForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入异常原因"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="abnormalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAbnormal">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'OrderList',
  data() {
    return {
      searchForm: {
        orderNo: '',
        status: '',
        isAbnormal: ''
      },
      tableData: [],
      selectedIds: [],
      isAllSelected: false,
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      logisticsDialogVisible: false,
      logisticsForm: {
        company: '',
        trackingNumber: ''
      },
      abnormalDialogVisible: false,
      abnormalForm: {
        reason: ''
      },
      currentOrder: null
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const mockData = []
      const statuses = ['PENDING_PAYMENT', 'PENDING_SHIPMENT', 'SHIPPED', 'COMPLETED', 'CANCELLED', 'REFUNDED']
      for (let i = 1; i <= 15; i++) {
        mockData.push({
          id: i,
          orderNo: 'ORD' + Date.now() + i,
          productName: `测试商品${i}`,
          quantity: i,
          totalAmount: (100 + i * 10).toFixed(2),
          status: statuses[i % statuses.length],
          isAbnormal: i % 7 === 0,
          abnormalReason: i % 7 === 0 ? '订单异常测试' + i : '',
          receiverName: '测试用户' + i,
          receiverPhone: '13800' + String(i).padStart(5, '0'),
          shippingAddress: '测试地址' + i + '号',
          logisticsCompany: i % 3 === 0 ? '顺丰快递' : '',
          trackingNumber: i % 3 === 0 ? 'SF' + Date.now() : '',
          createTime: '2024-01-' + String(i).padStart(2, '0') + ' 10:00:00'
        })
      }
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    getStatusType(status) {
      const map = {
        'PENDING_PAYMENT': 'warning',
        'PENDING_SHIPMENT': 'primary',
        'SHIPPED': 'info',
        'COMPLETED': 'success',
        'CANCELLED': 'info',
        'REFUNDING': 'warning',
        'REFUNDED': 'danger'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING_PAYMENT': '待付款',
        'PENDING_SHIPMENT': '待发货',
        'SHIPPED': '已发货',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消',
        'REFUNDING': '退款中',
        'REFUNDED': '已退款'
      }
      return map[status] || status
    },
    handleSearch() {
      this.$message.success('搜索成功')
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        orderNo: '',
        status: '',
        isAbnormal: ''
      }
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
    batchShip() {
      this.$confirm(`确定要批量发货选中的${this.selectedIds.length}个订单吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量发货成功')
        this.$refs.table.clearSelection()
      })
    },
    batchRefund() {
      this.$confirm(`确定要批量退款选中的${this.selectedIds.length}个订单吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('批量退款成功')
        this.$refs.table.clearSelection()
      })
    },
    handleEditLogistics(row) {
      this.currentOrder = row
      this.logisticsForm.company = row.logisticsCompany || ''
      this.logisticsForm.trackingNumber = row.trackingNumber || ''
      this.logisticsDialogVisible = true
    },
    saveLogistics() {
      if (this.currentOrder) {
        this.currentOrder.logisticsCompany = this.logisticsForm.company
        this.currentOrder.trackingNumber = this.logisticsForm.trackingNumber
      }
      this.$message.success('物流信息更新成功')
      this.logisticsDialogVisible = false
    },
    handleMarkAbnormal(row) {
      this.currentOrder = row
      this.abnormalForm.reason = row.abnormalReason || ''
      this.abnormalDialogVisible = true
    },
    saveAbnormal() {
      if (this.currentOrder) {
        this.currentOrder.isAbnormal = true
        this.currentOrder.abnormalReason = this.abnormalForm.reason
      }
      this.$message.success('标记异常成功')
      this.abnormalDialogVisible = false
    },
    handleDetail(row) {
      this.$message.info(`查看订单详情: ${row.orderNo}`)
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