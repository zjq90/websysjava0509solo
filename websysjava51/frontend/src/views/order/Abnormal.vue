<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">异常订单</h2>
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
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="productName" label="商品名称" min-width="180"></el-table-column>
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
        <el-table-column prop="abnormalReason" label="异常原因" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="receiverName" label="收货人" width="100"></el-table-column>
        <el-table-column prop="receiverPhone" label="电话" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" icon="el-icon-check" @click="handleResolve(scope.row)">标记解决</el-button>
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
    <el-dialog title="订单详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentOrder.productName }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)" size="small">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ currentOrder.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址">{{ currentOrder.shippingAddress }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="异常原因" :span="2">
          <el-tag type="danger">{{ currentOrder.abnormalReason }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AbnormalOrder',
  data() {
    return {
      searchForm: {
        orderNo: '',
        status: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      detailDialogVisible: false,
      currentOrder: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const mockData = []
      const statuses = ['PENDING_PAYMENT', 'PENDING_SHIPMENT', 'SHIPPED', 'COMPLETED', 'CANCELLED']
      const reasons = [
        '买家投诉商品质量问题',
        '物流信息异常，长时间未更新',
        '商品与描述不符',
        '卖家未按时发货',
        '商品损坏',
        '买家要求退款退货'
      ]
      for (let i = 1; i <= 12; i++) {
        mockData.push({
          id: i,
          orderNo: 'ORD' + Date.now() + i,
          productName: '测试商品' + i,
          quantity: 1,
          totalAmount: (100 + i * 10).toFixed(2),
          status: statuses[i % statuses.length],
          isAbnormal: true,
          abnormalReason: reasons[i % reasons.length],
          receiverName: '测试用户' + i,
          receiverPhone: '13800' + String(i).padStart(5, '0'),
          shippingAddress: '测试地址' + i + '号',
          logisticsCompany: '顺丰快递',
          trackingNumber: 'SF' + Date.now(),
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
        status: ''
      }
      this.fetchData()
    },
    handleDetail(row) {
      this.currentOrder = { ...row }
      this.detailDialogVisible = true
    },
    handleResolve(row) {
      this.$confirm('确定要标记该订单为已解决吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('标记成功')
      })
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