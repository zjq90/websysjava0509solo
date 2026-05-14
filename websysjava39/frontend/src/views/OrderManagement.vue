<template>
  <div class="order-management">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center">
            <span>订单管理</span>
            <el-button type="primary" size="small" @click="handleAdd">新增订单</el-button>
          </div>
          <el-table :data="orders" style="width: 100%" v-loading="loading" border>
            <el-table-column prop="id" label="订单ID" width="80" align="center"></el-table-column>
            <el-table-column prop="orderNo" label="订单编号" width="150" align="center"></el-table-column>
            <el-table-column prop="customer.name" label="客户姓名" width="120" align="center"></el-table-column>
            <el-table-column prop="aPackage.name" label="套餐名称" align="center"></el-table-column>
            <el-table-column prop="store.name" label="门店" width="120" align="center"></el-table-column>
            <el-table-column prop="sales.name" label="销售员" width="120" align="center"></el-table-column>
            <el-table-column prop="amount" label="订单金额" width="120" align="center">
              <template slot-scope="scope">
                <span style="color: #f56c6c; font-weight: bold">¥{{ scope.row.amount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120" align="center">
              <template slot-scope="scope">
                <el-tag size="small" :type="getStatusType(scope.row.status)">
                  {{ getStatusName(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
                <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'OrderManagement',
  data() {
    return {
      loading: false,
      orders: []
    }
  },
  mounted() {
    this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      this.loading = true
      try {
        const res = await this.$http.get('/orders')
        if (res.data.success) {
          this.orders = res.data.data || []
        }
      } catch (error) {
        console.error('获取订单失败', error)
      } finally {
        this.loading = false
      }
    },
    getStatusType(status) {
      const map = {
        'CONSULTING': 'info',
        'ORDERED': 'warning',
        'SHOOTING': 'primary',
        'SHOOT_COMPLETED': 'success',
        'SELECTING': 'primary',
        'EDITING': 'warning',
        'DELIVERED': 'success',
        'CANCELLED': 'danger'
      }
      return map[status] || ''
    },
    getStatusName(status) {
      const map = {
        'CONSULTING': '咨询中',
        'ORDERED': '已下单',
        'SHOOTING': '拍摄中',
        'SHOOT_COMPLETED': '拍摄完成',
        'SELECTING': '选片中',
        'EDITING': '修片中',
        'DELIVERED': '已交付',
        'CANCELLED': '已取消'
      }
      return map[status] || status
    },
    handleAdd() {
      this.$message.info('新增订单功能')
    },
    handleView(row) {
      this.$message.info('查看订单: ' + row.orderNo)
    },
    handleEdit(row) {
      this.$message.info('编辑订单: ' + row.orderNo)
    },
    handleDelete(row) {
      this.$confirm('确定删除该订单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
