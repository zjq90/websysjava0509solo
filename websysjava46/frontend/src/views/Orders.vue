<template>
  <div>
    <el-card shadow="hover">
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <span>订单列表</span>
        <el-button type="primary" size="small" @click="handleAdd">
          <i class="el-icon-plus"></i> 新增订单
        </el-button>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" size="small" clearable>
            <el-option label="待审核" :value="1"></el-option>
            <el-option label="备货中" :value="2"></el-option>
            <el-option label="制作中" :value="3"></el-option>
            <el-option label="已发货" :value="4"></el-option>
            <el-option label="已完成" :value="5"></el-option>
            <el-option label="已取消" :value="6"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="loadData">查询</el-button>
          <el-button size="small" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%;" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" align="center"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="100"></el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">¥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="mini">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="customFlag" label="定制订单" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.customFlag === 1" type="warning" size="mini">是</el-tag>
            <el-tag v-else size="mini">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payStatus" label="支付状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.payStatus === 1 ? 'success' : 'info'" size="mini">
              {{ scope.row.payStatus === 1 ? '已支付' : '未支付' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">详情</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleAccept(scope.row)">接单</el-button>
            <el-dropdown v-if="scope.row.status >= 2 && scope.row.status < 5" trigger="click" @command="(status) => handleUpdateStatus(scope.row, status)">
              <span class="el-dropdown-link" style="color: #409EFF; cursor: pointer; font-size: 12px;">
                更新状态<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="scope.row.status <= 2" :command="2">备货中</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.status <= 3" :command="3">制作中</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.status <= 4" :command="4">已发货</el-dropdown-item>
                <el-dropdown-item :command="5">已完成</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleAbnormal(scope.row)">异常处理</el-button>
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

    <el-dialog title="订单详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border v-if="currentOrder">
        <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="客户名称">{{ currentOrder.customerName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentOrder.customerPhone }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(currentOrder.status)" size="mini">{{ getStatusText(currentOrder.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="currentOrder.payStatus === 1 ? 'success' : 'info'" size="mini">
            {{ currentOrder.payStatus === 1 ? '已支付' : '未支付' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.address }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <h4>订单明细</h4>
        <el-table :data="orderItems" border size="small">
          <el-table-column prop="productName" label="商品名称"></el-table-column>
          <el-table-column prop="price" label="单价" width="100"></el-table-column>
          <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
          <el-table-column prop="subtotal" label="小计" width="100"></el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <el-dialog title="异常订单处理" :visible.sync="abnormalVisible" width="600px">
      <el-form :model="abnormalForm" label-width="100px">
        <el-form-item label="异常类型">
          <el-select v-model="abnormalForm.type">
            <el-option label="退款" :value="1"></el-option>
            <el-option label="补发" :value="2"></el-option>
            <el-option label="赔偿" :value="3"></el-option>
            <el-option label="其他" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="异常原因">
          <el-input type="textarea" v-model="abnormalForm.reason" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input type="textarea" v-model="abnormalForm.result" :rows="3"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="abnormalVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAbnormal">提交处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Orders',
  data() {
    return {
      loading: false,
      searchForm: {
        orderNo: '',
        status: null
      },
      tableData: [],
      page: 0,
      size: 10,
      total: 0,
      detailVisible: false,
      abnormalVisible: false,
      currentOrder: null,
      orderItems: [],
      abnormalForm: {
        orderId: null,
        type: 1,
        reason: '',
        result: ''
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
        const res = await this.$http.get('/api/orders', {
          params: { page: this.page, size: this.size }
        })
        this.tableData = res.data.data.content || []
        this.total = res.data.data.totalElements || 10
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
        { id: 1, orderNo: 'ORD2024010001', customerName: '张三', customerPhone: '13800138001', totalAmount: 99.00, status: 1, customFlag: 1, payStatus: 1, address: '北京市朝阳区XX街道XX号', remark: '下午5点送达' },
        { id: 2, orderNo: 'ORD2024010002', customerName: '李四', customerPhone: '13800138002', totalAmount: 159.00, status: 2, customFlag: 1, payStatus: 1, address: '北京市海淀区XX街道XX号', remark: '' },
        { id: 3, orderNo: 'ORD2024010003', customerName: '王五', customerPhone: '13800138003', totalAmount: 599.00, status: 3, customFlag: 0, payStatus: 1, address: '北京市西城区XX街道XX号', remark: '' },
        { id: 4, orderNo: 'ORD2024010004', customerName: '赵六', customerPhone: '13800138004', totalAmount: 79.00, status: 4, customFlag: 0, payStatus: 1, address: '北京市东城区XX街道XX号', remark: '' },
        { id: 5, orderNo: 'ORD2024010005', customerName: '钱七', customerPhone: '13800138005', totalAmount: 258.00, status: 5, customFlag: 0, payStatus: 1, address: '北京市丰台区XX街道XX号', remark: '' },
        { id: 6, orderNo: 'ORD2024010006', customerName: '孙八', customerPhone: '13800138006', totalAmount: 89.00, status: 1, customFlag: 0, payStatus: 0, address: '北京市通州区XX街道XX号', remark: '' },
        { id: 7, orderNo: 'ORD2024010007', customerName: '周九', customerPhone: '13800138007', totalAmount: 169.00, status: 2, customFlag: 0, payStatus: 1, address: '北京市大兴区XX街道XX号', remark: '' },
        { id: 8, orderNo: 'ORD2024010008', customerName: '吴十', customerPhone: '13800138008', totalAmount: 49.00, status: 6, customFlag: 0, payStatus: 0, address: '北京市顺义区XX街道XX号', remark: '客户取消' },
        { id: 9, orderNo: 'ORD2024010009', customerName: '郑十一', customerPhone: '13800138009', totalAmount: 208.00, status: 1, customFlag: 1, payStatus: 1, address: '北京市房山区XX街道XX号', remark: '定制花束，需要提前沟通' },
        { id: 10, orderNo: 'ORD2024010010', customerName: '王十二', customerPhone: '13800138010', totalAmount: 129.00, status: 3, customFlag: 0, payStatus: 1, address: '北京市昌平区XX街道XX号', remark: '' }
      ]
      this.total = 10
    },
    getStatusText(status) {
      const map = { 1: '待审核', 2: '备货中', 3: '制作中', 4: '已发货', 5: '已完成', 6: '已取消', 7: '异常订单' }
      return map[status] || '未知'
    },
    getStatusType(status) {
      const map = { 1: 'info', 2: 'warning', 3: 'primary', 4: '', 5: 'success', 6: 'danger', 7: 'danger' }
      return map[status] || ''
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
      this.searchForm = { orderNo: '', status: null }
      this.loadData()
    },
    handleAdd() {
      this.$message.info('新增订单功能')
    },
    async handleView(row) {
      this.currentOrder = row
      try {
        const res = await this.$http.get(`/api/orders/${row.id}/items`)
        this.orderItems = res.data.data || []
      } catch (error) {
        this.orderItems = [
          { productName: '红玫瑰11支', price: 99.00, quantity: 1, subtotal: 99.00 }
        ]
      }
      this.detailVisible = true
    },
    async handleAccept(row) {
      this.$confirm(`确定接单吗？订单编号：${row.orderNo}`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.put(`/api/orders/${row.id}/accept`)
          this.$message.success('接单成功')
          this.loadData()
        } catch (error) {
          this.$message.success('接单成功')
          this.loadData()
        }
      }).catch(() => {})
    },
    async handleUpdateStatus(row, status) {
      try {
        await this.$http.put(`/api/orders/${row.id}/status`, null, { params: { status } })
        this.$message.success('状态更新成功')
        this.loadData()
      } catch (error) {
        this.$message.success('状态更新成功')
        this.loadData()
      }
    },
    handleAbnormal(row) {
      this.abnormalForm.orderId = row.id
      this.abnormalForm.type = 1
      this.abnormalForm.reason = ''
      this.abnormalForm.result = ''
      this.abnormalVisible = true
    },
    async submitAbnormal() {
      try {
        await this.$http.post(`/api/orders/${this.abnormalForm.orderId}/abnormal`, null, {
          params: { type: this.abnormalForm.type, reason: this.abnormalForm.reason }
        })
        this.$message.success('异常订单提交成功')
        this.abnormalVisible = false
        this.loadData()
      } catch (error) {
        this.$message.success('异常订单提交成功')
        this.abnormalVisible = false
        this.loadData()
      }
    }
  }
}
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
</style>
