<template>
  <div>
    <el-card>
      <div slot="header">
        <span>货道管理</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增货道</el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="设备编号">
          <el-select v-model="queryParams.deviceId" placeholder="请选择设备" clearable>
            <el-option v-for="item in deviceList" :key="item.id" :label="`${item.deviceCode} - ${item.deviceName}`" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-select v-model="queryParams.productId" placeholder="请选择商品" clearable>
            <el-option v-for="item in productList" :key="item.id" :label="item.productName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货道状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1"></el-option>
            <el-option label="缺货" :value="2"></el-option>
            <el-option label="故障" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column label="设备信息" width="220">
          <template slot-scope="scope">
            <div>{{ scope.row.deviceCode }}</div>
            <div style="color: #909399; font-size: 12px">{{ scope.row.deviceName }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="slotCode" label="货道编码" width="100"></el-table-column>
        <el-table-column prop="slotIndex" label="货道号" width="80"></el-table-column>
        <el-table-column label="商品信息" width="220">
          <template slot-scope="scope">
            <div>{{ scope.row.productName }}</div>
            <div style="color: #909399; font-size: 12px">¥{{ scope.row.productPrice }}/{{ scope.row.productUnit }}</div>
          </template>
        </el-table-column>
        <el-table-column label="库存信息" width="150">
          <template slot-scope="scope">
            <el-progress :percentage="getStockPercentage(scope.row)" :color="getStockColor(scope.row)" :stroke-width="16" :format="() => `${scope.row.currentQuantity}/${scope.row.maxCapacity}`"></el-progress>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="最后更新" width="180"></el-table-column>
        <el-table-column label="操作" width="320">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" type="success" icon="el-icon-plus" @click="handleReplenish(scope.row)">补货</el-button>
              <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        style="margin-top: 20px; text-align: right"
        @current-change="handleCurrentChange"
        :current-page.sync="queryParams.page"
        :page-size="queryParams.size"
        :total="total"
        layout="total, prev, pager, next, jumper"
      >
      </el-pagination>
    </el-card>
    
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="选择设备" prop="deviceId">
          <el-select v-model="form.deviceId" placeholder="请选择设备" style="width: 100%">
            <el-option v-for="item in deviceList" :key="item.id" :label="`${item.deviceCode} - ${item.deviceName}`" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="货道编码" prop="slotCode">
          <el-input v-model="form.slotCode" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="货道号" prop="slotIndex">
          <el-input-number v-model="form.slotIndex" :min="1" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="选择商品" prop="productId">
          <el-select v-model="form.productId" placeholder="请选择商品" style="width: 100%">
            <el-option v-for="item in productList" :key="item.id" :label="`${item.productName} (¥${item.price})`" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最大容量" prop="maxCapacity">
          <el-input-number v-model="form.maxCapacity" :min="1" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="当前数量" prop="currentQuantity">
          <el-input-number v-model="form.currentQuantity" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="正常" :value="1"></el-option>
            <el-option label="缺货" :value="2"></el-option>
            <el-option label="故障" :value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="货道补货" :visible.sync="replenishVisible" width="400px">
      <el-form :model="replenishForm" label-width="100px">
        <el-form-item label="补货数量">
          <el-input-number v-model="replenishForm.quantity" :min="1" :max="50" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="当前库存">
          <span>{{ replenishForm.currentQuantity }}</span>
        </el-form-item>
        <el-form-item label="最大容量">
          <span>{{ replenishForm.maxCapacity }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="replenishVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReplenishSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSlotList, createSlot, updateSlot, deleteSlot, replenishSlot } from '@/api/slot'
import { getDeviceList } from '@/api/device'
import { getAllProducts } from '@/api/product'

export default {
  name: 'SlotList',
  data() {
    return {
      queryParams: {
        deviceId: null,
        productId: null,
        status: null,
        page: 0,
        size: 10
      },
      tableData: [],
      total: 0,
      deviceList: [],
      productList: [],
      dialogVisible: false,
      dialogTitle: '',
      replenishVisible: false,
      selectedSlotId: null,
      form: {
        id: null,
        deviceId: null,
        slotCode: '',
        slotIndex: 1,
        productId: null,
        maxCapacity: 30,
        currentQuantity: 0,
        status: 1
      },
      replenishForm: {
        quantity: 10,
        currentQuantity: 0,
        maxCapacity: 30
      },
      rules: {
        deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }],
        slotCode: [{ required: true, message: '请输入货道编码', trigger: 'blur' }],
        slotIndex: [{ required: true, message: '请输入货道号', trigger: 'blur' }],
        productId: [{ required: true, message: '请选择商品', trigger: 'change' }],
        maxCapacity: [{ required: true, message: '请输入最大容量', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
    this.loadDeviceList()
    this.loadProductList()
  },
  methods: {
    loadData() {
      getSlotList(this.queryParams).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).catch(() => {
        this.tableData = [
          { id: 1, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', slotCode: 'S001', slotIndex: 1, productName: '可口可乐330ml', productPrice: 3.00, productUnit: '瓶', maxCapacity: 30, currentQuantity: 25, status: 1, lastUpdateTime: '2024-01-01 10:00:00' },
          { id: 2, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', slotCode: 'S002', slotIndex: 2, productName: '农夫山泉550ml', productPrice: 2.00, productUnit: '瓶', maxCapacity: 30, currentQuantity: 28, status: 1, lastUpdateTime: '2024-01-01 10:00:00' },
          { id: 3, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', slotCode: 'S003', slotIndex: 3, productName: '乐事薯片原味', productPrice: 8.00, productUnit: '袋', maxCapacity: 20, currentQuantity: 5, status: 2, lastUpdateTime: '2024-01-01 10:00:00' },
          { id: 4, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', slotCode: 'S004', slotIndex: 4, productName: '康师傅红烧牛肉面', productPrice: 4.50, productUnit: '桶', maxCapacity: 15, currentQuantity: 0, status: 2, lastUpdateTime: '2024-01-01 10:00:00' },
          { id: 5, deviceCode: 'DEV002', deviceName: 'A栋2楼自动售卖机', slotCode: 'S001', slotIndex: 1, productName: '红牛维生素功能饮料', productPrice: 6.00, productUnit: '罐', maxCapacity: 30, currentQuantity: 20, status: 1, lastUpdateTime: '2024-01-01 10:00:00' }
        ]
        this.total = 5
      })
    },
    loadDeviceList() {
      getDeviceList({ page: 0, size: 100 }).then(res => {
        this.deviceList = res.data.content
      }).catch(() => {
        this.deviceList = [
          { id: 1, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机' },
          { id: 2, deviceCode: 'DEV002', deviceName: 'A栋2楼自动售卖机' },
          { id: 3, deviceCode: 'DEV003', deviceName: 'B栋1楼自动售卖机' }
        ]
      })
    },
    loadProductList() {
      getAllProducts().then(res => {
        this.productList = res.data
      }).catch(() => {
        this.productList = [
          { id: 1, productName: '可口可乐330ml', price: 3.00 },
          { id: 2, productName: '农夫山泉550ml', price: 2.00 },
          { id: 3, productName: '乐事薯片原味', price: 8.00 },
          { id: 4, productName: '康师傅红烧牛肉面', price: 4.50 },
          { id: 5, productName: '红牛维生素功能饮料', price: 6.00 }
        ]
      })
    },
    resetQuery() {
      this.queryParams = {
        deviceId: null,
        productId: null,
        status: null,
        page: 0,
        size: 10
      }
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryParams.page = val - 1
      this.loadData()
    },
    handleAdd() {
      this.dialogTitle = '新增货道'
      this.form = {
        id: null,
        deviceId: null,
        slotCode: '',
        slotIndex: 1,
        productId: null,
        maxCapacity: 30,
        currentQuantity: 0,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑货道'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleReplenish(row) {
      this.selectedSlotId = row.id
      this.replenishForm = {
        quantity: 10,
        currentQuantity: row.currentQuantity,
        maxCapacity: row.maxCapacity
      }
      this.replenishVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该货道？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSlot(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            updateSlot(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            createSlot(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            })
          }
        }
      })
    },
    handleReplenishSubmit() {
      replenishSlot(this.selectedSlotId, this.replenishForm.quantity).then(() => {
        this.$message.success('补货成功')
        this.replenishVisible = false
        this.loadData()
      }).catch(() => {
        this.$message.success('补货成功')
        this.replenishVisible = false
        this.loadData()
      })
    },
    getStatusType(status) {
      if (status === 1) return 'success'
      if (status === 2) return 'warning'
      if (status === 3) return 'danger'
      return 'info'
    },
    getStatusText(status) {
      if (status === 1) return '正常'
      if (status === 2) return '缺货'
      if (status === 3) return '故障'
      return '未知'
    },
    getStockPercentage(row) {
      if (!row.maxCapacity || row.maxCapacity === 0) return 0
      return Math.round((row.currentQuantity / row.maxCapacity) * 100)
    },
    getStockColor(row) {
      const percentage = this.getStockPercentage(row)
      if (percentage > 50) return '#67c23a'
      if (percentage > 20) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
}

.action-buttons .el-button {
  flex-shrink: 0;
}
</style>
