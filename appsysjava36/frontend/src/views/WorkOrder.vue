<template>
  <div class="workorder">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="float: left; font-size: 18px; font-weight: bold;">工单管理</span>
        <el-button style="float: right" type="primary" @click="showCreateDialog">新建工单</el-button>
      </div>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="orderNo" label="工单编号" width="150"></el-table-column>
        <el-table-column prop="userName" label="用户姓名" width="100"></el-table-column>
        <el-table-column prop="userPhone" label="用户电话" width="120"></el-table-column>
        <el-table-column prop="userAddress" label="用户地址" width="200"></el-table-column>
        <el-table-column prop="technicianName" label="装维人员" width="100"></el-table-column>
        <el-table-column prop="orderType" label="工单类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderType === 'INSTALL'" type="success">安装</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'REPAIR'" type="warning">维修</el-tag>
            <el-tag v-else-if="scope.row.orderType === 'MAINTENANCE'" type="info">维护</el-tag>
            <el-tag v-else type="primary">升级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'PENDING'" type="info">待分配</el-tag>
            <el-tag v-else-if="scope.row.status === 'ASSIGNED'" type="warning">已分配</el-tag>
            <el-tag v-else-if="scope.row.status === 'IN_PROGRESS'" type="primary">处理中</el-tag>
            <el-tag v-else type="success">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="assignOrder(scope.row)" v-if="scope.row.status === 'PENDING'">智能派单</el-button>
            <el-button size="mini" type="success" @click="acceptOrder(scope.row)" v-if="scope.row.status === 'ASSIGNED'">接单</el-button>
            <el-button size="mini" type="warning" @click="completeOrder(scope.row)" v-if="scope.row.status === 'IN_PROGRESS'">完成</el-button>
            <el-button size="mini" type="info" @click="viewDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="danger" @click="deleteOrder(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="新建工单" :visible.sync="createDialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户姓名">
          <el-input v-model="form.userName"></el-input>
        </el-form-item>
        <el-form-item label="用户电话">
          <el-input v-model="form.userPhone"></el-input>
        </el-form-item>
        <el-form-item label="用户地址">
          <el-input v-model="form.userAddress"></el-input>
        </el-form-item>
        <el-form-item label="工单类型">
          <el-select v-model="form.orderType" placeholder="请选择">
            <el-option label="安装" value="INSTALL"></el-option>
            <el-option label="维修" value="REPAIR"></el-option>
            <el-option label="维护" value="MAINTENANCE"></el-option>
            <el-option label="升级" value="UPGRADE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="form.priority" placeholder="请选择">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createOrder">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="工单详情" :visible.sync="detailDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="工单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ currentOrder.userName }}</el-descriptions-item>
        <el-descriptions-item label="用户电话">{{ currentOrder.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="用户地址">{{ currentOrder.userAddress }}</el-descriptions-item>
        <el-descriptions-item label="装维人员">{{ currentOrder.technicianName }}</el-descriptions-item>
        <el-descriptions-item label="工单类型">{{ currentOrder.orderType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentOrder.status }}</el-descriptions-item>
        <el-descriptions-item label="满意度">{{ currentOrder.satisfactionScore }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ currentOrder.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'WorkOrder',
  data() {
    return {
      tableData: [],
      createDialogVisible: false,
      detailDialogVisible: false,
      currentOrder: {},
      form: {
        userName: '',
        userPhone: '',
        userAddress: '',
        orderType: '',
        priority: 'MEDIUM',
        description: ''
      }
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      try {
        const res = await this.$http.get('/api/workorders')
        if (res.data && res.data.data) {
          this.tableData = res.data.data
        }
      } catch (e) {
        this.$message.error('加载数据失败')
      }
    },
    showCreateDialog() {
      this.createDialogVisible = true
    },
    async createOrder() {
      try {
        await this.$http.post('/api/workorders', this.form)
        this.$message.success('创建成功')
        this.createDialogVisible = false
        this.loadOrders()
        this.form = {
          userName: '',
          userPhone: '',
          userAddress: '',
          orderType: '',
          priority: 'MEDIUM',
          description: ''
        }
      } catch (e) {
        this.$message.error('创建失败')
      }
    },
    async assignOrder(row) {
      try {
        await this.$http.post(`/api/workorders/${row.id}/assign`)
        this.$message.success('派单成功')
        this.loadOrders()
      } catch (e) {
        this.$message.error('派单失败')
      }
    },
    async acceptOrder(row) {
      try {
        await this.$http.post(`/api/workorders/${row.id}/accept`)
        this.$message.success('接单成功')
        this.loadOrders()
      } catch (e) {
        this.$message.error('接单失败')
      }
    },
    async completeOrder(row) {
      try {
        await this.$http.post(`/api/workorders/${row.id}/complete`, {})
        this.$message.success('完成成功')
        this.loadOrders()
      } catch (e) {
        this.$message.error('操作失败')
      }
    },
    viewDetail(row) {
      this.currentOrder = row
      this.detailDialogVisible = true
    },
    async deleteOrder(row) {
      this.$confirm('确认删除该工单?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/workorders/${row.id}`)
          this.$message.success('删除成功')
          this.loadOrders()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.workorder {
  padding: 0;
}
</style>
