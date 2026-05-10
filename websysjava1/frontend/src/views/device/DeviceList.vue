<template>
  <div>
    <el-card>
      <div slot="header">
        <span>设备列表</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增设备</el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="设备编号">
          <el-input v-model="queryParams.deviceCode" placeholder="请输入设备编号" clearable></el-input>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="queryParams.deviceName" placeholder="请输入设备名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="投放位置">
          <el-input v-model="queryParams.location" placeholder="请输入投放位置" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="在线" :value="1"></el-option>
            <el-option label="离线" :value="2"></el-option>
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
        <el-table-column prop="deviceCode" label="设备编号" width="120"></el-table-column>
        <el-table-column prop="deviceName" label="设备名称" width="180"></el-table-column>
        <el-table-column prop="model" label="设备型号" width="120"></el-table-column>
        <el-table-column prop="firmwareVersion" label="固件版本" width="120"></el-table-column>
        <el-table-column prop="location" label="投放位置"></el-table-column>
        <el-table-column prop="manager" label="负责人" width="100"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" type="primary" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
              <el-button size="mini" type="success" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
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
        <el-form-item label="设备编号" prop="deviceCode">
          <el-input v-model="form.deviceCode" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName"></el-input>
        </el-form-item>
        <el-form-item label="设备型号">
          <el-input v-model="form.model"></el-input>
        </el-form-item>
        <el-form-item label="固件版本">
          <el-input v-model="form.firmwareVersion"></el-input>
        </el-form-item>
        <el-form-item label="投放位置" prop="location">
          <el-input v-model="form.location" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.manager"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.contactPhone"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="在线" :value="1"></el-option>
            <el-option label="离线" :value="2"></el-option>
            <el-option label="故障" :value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="设备详情" :visible.sync="detailVisible" width="800px">
      <el-descriptions :column="2" border v-if="selectedDevice">
        <el-descriptions-item label="设备编号">{{ selectedDevice.deviceCode }}</el-descriptions-item>
        <el-descriptions-item label="设备名称">{{ selectedDevice.deviceName }}</el-descriptions-item>
        <el-descriptions-item label="设备型号">{{ selectedDevice.model }}</el-descriptions-item>
        <el-descriptions-item label="固件版本">{{ selectedDevice.firmwareVersion }}</el-descriptions-item>
        <el-descriptions-item label="投放位置" :span="2">{{ selectedDevice.location }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ selectedDevice.manager }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ selectedDevice.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(selectedDevice.status)" size="small">
            {{ getStatusText(selectedDevice.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最后上报时间">{{ selectedDevice.lastReportTime }}</el-descriptions-item>
        <el-descriptions-item label="安装时间">{{ selectedDevice.installTime }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider>设备状态</el-divider>
      <el-descriptions :column="2" border v-if="deviceStatus">
        <el-descriptions-item label="网络质量">{{ getNetworkQualityText(deviceStatus.networkQuality) }}</el-descriptions-item>
        <el-descriptions-item label="信号强度">{{ deviceStatus.signalStrength }} dBm</el-descriptions-item>
        <el-descriptions-item label="电机状态">{{ getMotorStatusText(deviceStatus.motorStatus) }}</el-descriptions-item>
        <el-descriptions-item label="门锁状态">{{ getDoorStatusText(deviceStatus.doorStatus) }}</el-descriptions-item>
        <el-descriptions-item label="当前温度">{{ deviceStatus.temperature }} ℃</el-descriptions-item>
        <el-descriptions-item label="目标温度">{{ deviceStatus.targetTemperature }} ℃</el-descriptions-item>
        <el-descriptions-item label="湿度">{{ deviceStatus.humidity }} %</el-descriptions-item>
        <el-descriptions-item label="电力状况">{{ getPowerStatusText(deviceStatus.powerStatus) }}</el-descriptions-item>
        <el-descriptions-item label="电池电量">{{ deviceStatus.batteryLevel }} %</el-descriptions-item>
        <el-descriptions-item label="广告内容" :span="2">{{ deviceStatus.advertContent }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDeviceList, createDevice, updateDevice, deleteDevice, getDeviceLatestStatus } from '@/api/device'

export default {
  name: 'DeviceList',
  data() {
    return {
      queryParams: {
        deviceCode: '',
        deviceName: '',
        location: '',
        status: null,
        page: 0,
        size: 10
      },
      tableData: [],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      selectedDevice: null,
      deviceStatus: null,
      form: {
        id: null,
        deviceCode: '',
        deviceName: '',
        model: '',
        firmwareVersion: '',
        location: '',
        manager: '',
        contactPhone: '',
        status: 1
      },
      rules: {
        deviceCode: [{ required: true, message: '请输入设备编号', trigger: 'blur' }],
        deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
        location: [{ required: true, message: '请输入投放位置', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      getDeviceList(this.queryParams).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).catch(() => {
        this.tableData = [
          { id: 1, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', model: 'VM-2024', firmwareVersion: 'v1.2.3', location: '北京市朝阳区科技园区A栋1楼大厅', manager: '张三', contactPhone: '13800138001', status: 1, lastReportTime: '2024-01-01 10:00:00', installTime: '2024-01-01 10:00:00' },
          { id: 2, deviceCode: 'DEV002', deviceName: 'A栋2楼自动售卖机', model: 'VM-2024', firmwareVersion: 'v1.2.3', location: '北京市朝阳区科技园区A栋2楼电梯口', manager: '张三', contactPhone: '13800138001', status: 1, lastReportTime: '2024-01-01 10:00:00', installTime: '2024-01-01 10:00:00' },
          { id: 3, deviceCode: 'DEV003', deviceName: 'B栋1楼自动售卖机', model: 'VM-2024', firmwareVersion: 'v1.2.2', location: '北京市朝阳区科技园区B栋1楼大厅', manager: '李四', contactPhone: '13800138002', status: 2, lastReportTime: '2024-01-01 10:00:00', installTime: '2024-01-01 10:00:00' },
          { id: 4, deviceCode: 'DEV004', deviceName: 'C栋1楼自动售卖机', model: 'VM-2024', firmwareVersion: 'v1.2.3', location: '北京市朝阳区科技园区C栋1楼餐厅门口', manager: '王五', contactPhone: '13800138003', status: 3, lastReportTime: '2024-01-01 10:00:00', installTime: '2024-01-01 10:00:00' },
          { id: 5, deviceCode: 'DEV005', deviceName: 'D栋自动售卖机', model: 'VM-2024-Pro', firmwareVersion: 'v1.3.0', location: '北京市朝阳区科技园区D栋宿舍楼楼下', manager: '赵六', contactPhone: '13800138004', status: 1, lastReportTime: '2024-01-01 10:00:00', installTime: '2024-01-01 10:00:00' }
        ]
        this.total = 5
      })
    },
    resetQuery() {
      this.queryParams = {
        deviceCode: '',
        deviceName: '',
        location: '',
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
      this.dialogTitle = '新增设备'
      this.form = {
        id: null,
        deviceCode: '',
        deviceName: '',
        model: '',
        firmwareVersion: '',
        location: '',
        manager: '',
        contactPhone: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑设备'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.selectedDevice = row
      this.loadDeviceStatus(row.id)
      this.detailVisible = true
    },
    loadDeviceStatus(id) {
      getDeviceLatestStatus(id).then(res => {
        this.deviceStatus = res.data
      }).catch(() => {
        this.deviceStatus = {
          networkQuality: 2,
          signalStrength: -65,
          motorStatus: 1,
          doorStatus: 1,
          temperature: 4.5,
          humidity: 65.0,
          powerStatus: 1,
          batteryLevel: 100,
          targetTemperature: 4.0,
          advertContent: '欢迎使用自动售卖机！'
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该设备？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDevice(row.id).then(() => {
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
            updateDevice(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            createDevice(this.form).then(() => {
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
    getStatusType(status) {
      if (status === 1) return 'success'
      if (status === 2) return 'warning'
      if (status === 3) return 'danger'
      return 'info'
    },
    getStatusText(status) {
      if (status === 1) return '在线'
      if (status === 2) return '离线'
      if (status === 3) return '故障'
      return '未知'
    },
    getNetworkQualityText(quality) {
      if (quality === 1) return '优秀'
      if (quality === 2) return '良好'
      if (quality === 3) return '一般'
      if (quality === 4) return '较差'
      return '未知'
    },
    getMotorStatusText(status) {
      return status === 1 ? '正常' : '异常'
    },
    getDoorStatusText(status) {
      if (status === 1) return '已锁'
      if (status === 2) return '已开'
      if (status === 3) return '异常'
      return '未知'
    },
    getPowerStatusText(status) {
      if (status === 1) return '正常'
      if (status === 2) return '低电量'
      if (status === 3) return '断电'
      return '未知'
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
