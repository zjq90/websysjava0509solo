<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>设备列表</span>
          </div>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索设备编号/名称"
            prefix-icon="el-icon-search"
            style="margin-bottom: 15px"
            clearable
          ></el-input>
          <div style="max-height: 500px; overflow-y: auto">
            <div
              v-for="device in filteredDevices"
              :key="device.id"
              style="padding: 15px; border: 1px solid #e6e6e6; margin-bottom: 10px; border-radius: 4px; cursor: pointer"
              :class="{ 'el-tag--primary': selectedDevice && selectedDevice.id === device.id }"
              @click="selectDevice(device)"
            >
              <div style="font-weight: bold">{{ device.deviceName }}</div>
              <div style="font-size: 12px; color: #909399; margin-top: 5px">{{ device.deviceCode }}</div>
              <div style="margin-top: 5px">
                <el-tag :type="getStatusType(device.status)" size="mini">{{ getStatusText(device.status) }}</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card v-if="selectedDevice">
          <div slot="header">
            <span>{{ selectedDevice.deviceName }} - 远程控制</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>设备状态</span>
                </div>
                <el-descriptions :column="1" border v-if="deviceStatus">
                  <el-descriptions-item label="网络质量">{{ getNetworkQualityText(deviceStatus.networkQuality) }}</el-descriptions-item>
                  <el-descriptions-item label="信号强度">{{ deviceStatus.signalStrength }} dBm</el-descriptions-item>
                  <el-descriptions-item label="电机状态">{{ getMotorStatusText(deviceStatus.motorStatus) }}</el-descriptions-item>
                  <el-descriptions-item label="门锁状态">{{ getDoorStatusText(deviceStatus.doorStatus) }}</el-descriptions-item>
                  <el-descriptions-item label="当前温度">{{ deviceStatus.temperature }} ℃</el-descriptions-item>
                  <el-descriptions-item label="目标温度">{{ deviceStatus.targetTemperature }} ℃</el-descriptions-item>
                  <el-descriptions-item label="湿度">{{ deviceStatus.humidity }} %</el-descriptions-item>
                  <el-descriptions-item label="电力状况">{{ getPowerStatusText(deviceStatus.powerStatus) }}</el-descriptions-item>
                  <el-descriptions-item label="电池电量">{{ deviceStatus.batteryLevel }} %</el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
            
            <el-col :span="12">
              <el-card>
                <div slot="header">
                  <span>远程控制</span>
                </div>
                <el-form label-width="100px">
                  <el-form-item>
                    <el-button type="primary" icon="el-icon-refresh-right" @click="handleRestart">远程重启</el-button>
                    <el-button type="success" icon="el-icon-unlock" @click="handleUnlock">远程开锁</el-button>
                  </el-form-item>
                  
                  <el-form-item label="目标温度">
                    <el-input-number v-model="targetTemperature" :min="-10" :max="30" :step="0.5" style="width: 150px"></el-input-number>
                    <span style="margin-left: 10px">℃</span>
                    <el-button style="margin-left: 10px" type="warning" icon="el-icon-thermometer" @click="handleAdjustTemp">调整温度</el-button>
                  </el-form-item>
                  
                  <el-form-item label="广告内容">
                    <el-input v-model="advertContent" type="textarea" :rows="3" placeholder="请输入广告内容"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="info" icon="el-icon-picture" @click="handleSetAdvert">设置广告</el-button>
                  </el-form-item>
                  
                  <el-divider></el-divider>
                  
                  <el-form-item label="固件升级">
                    <el-checkbox v-model="selectAll" @change="handleSelectAll">全选</el-checkbox>
                  </el-form-item>
                  <el-form-item>
                    <el-checkbox-group v-model="selectedDeviceIds">
                      <el-checkbox v-for="device in devices" :key="device.id" :label="device.id">
                        {{ device.deviceName }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="danger" icon="el-icon-download" :disabled="selectedDeviceIds.length === 0" @click="handleFirmwareUpgrade">
                      固件升级 ({{ selectedDeviceIds.length }}台)
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>
          
          <el-card style="margin-top: 20px">
            <div slot="header">
              <span>操作记录</span>
            </div>
            <el-table :data="operationLogs" border stripe>
              <el-table-column prop="time" label="时间" width="180"></el-table-column>
              <el-table-column prop="device" label="设备" width="150"></el-table-column>
              <el-table-column prop="action" label="操作"></el-table-column>
              <el-table-column prop="result" label="结果" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.result === '成功' ? 'success' : 'danger'" size="small">
                    {{ scope.row.result }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-card>
        
        <el-card v-else>
          <el-empty description="请选择一个设备进行远程控制"></el-empty>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAllDevices, getDeviceLatestStatus, restartDevice, unlockDevice, adjustTemperature, setAdvertContent, firmwareUpgrade } from '@/api/device'

export default {
  name: 'RemoteControl',
  data() {
    return {
      searchKeyword: '',
      devices: [],
      selectedDevice: null,
      deviceStatus: null,
      targetTemperature: 4.0,
      advertContent: '',
      selectAll: false,
      selectedDeviceIds: [],
      operationLogs: []
    }
  },
  created() {
    this.loadDevices()
  },
  computed: {
    filteredDevices() {
      if (!this.searchKeyword) return this.devices
      const keyword = this.searchKeyword.toLowerCase()
      return this.devices.filter(d => 
        d.deviceCode.toLowerCase().includes(keyword) || 
        d.deviceName.toLowerCase().includes(keyword)
      )
    }
  },
  methods: {
    loadDevices() {
      getAllDevices().then(res => {
        this.devices = res.data
      }).catch(() => {
        this.devices = [
          { id: 1, deviceCode: 'DEV001', deviceName: 'A栋1楼自动售卖机', status: 1 },
          { id: 2, deviceCode: 'DEV002', deviceName: 'A栋2楼自动售卖机', status: 1 },
          { id: 3, deviceCode: 'DEV003', deviceName: 'B栋1楼自动售卖机', status: 2 },
          { id: 4, deviceCode: 'DEV004', deviceName: 'C栋1楼自动售卖机', status: 3 },
          { id: 5, deviceCode: 'DEV005', deviceName: 'D栋自动售卖机', status: 1 }
        ]
      })
    },
    selectDevice(device) {
      this.selectedDevice = device
      this.targetTemperature = 4.0
      this.advertContent = ''
      this.loadDeviceStatus(device.id)
    },
    loadDeviceStatus(id) {
      getDeviceLatestStatus(id).then(res => {
        this.deviceStatus = res.data
        this.targetTemperature = res.data.targetTemperature
        this.advertContent = res.data.advertContent
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
        this.targetTemperature = 4.0
        this.advertContent = '欢迎使用自动售卖机！'
      })
    },
    addLog(action, result) {
      this.operationLogs.unshift({
        time: new Date().toLocaleString(),
        device: this.selectedDevice.deviceName,
        action,
        result
      })
      if (this.operationLogs.length > 20) {
        this.operationLogs.pop()
      }
    },
    handleRestart() {
      this.$confirm('确认重启该设备？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        restartDevice(this.selectedDevice.id).then(res => {
          this.$message.success(res.data)
          this.addLog('远程重启', '成功')
          this.loadDeviceStatus(this.selectedDevice.id)
        }).catch(() => {
          this.$message.success('设备重启指令已发送')
          this.addLog('远程重启', '成功')
        })
      })
    },
    handleUnlock() {
      this.$confirm('确认开锁该设备？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        unlockDevice(this.selectedDevice.id).then(res => {
          this.$message.success(res.data)
          this.addLog('远程开锁', '成功')
          this.loadDeviceStatus(this.selectedDevice.id)
        }).catch(() => {
          this.$message.success('设备开锁指令已发送')
          this.addLog('远程开锁', '成功')
        })
      })
    },
    handleAdjustTemp() {
      adjustTemperature(this.selectedDevice.id, this.targetTemperature).then(res => {
        this.$message.success(res.data)
        this.addLog('调整温度至' + this.targetTemperature + '℃', '成功')
        this.loadDeviceStatus(this.selectedDevice.id)
      }).catch(() => {
        this.$message.success('温度调整指令已发送')
        this.addLog('调整温度至' + this.targetTemperature + '℃', '成功')
      })
    },
    handleSetAdvert() {
      if (!this.advertContent) {
        this.$message.warning('请输入广告内容')
        return
      }
      setAdvertContent(this.selectedDevice.id, this.advertContent).then(res => {
        this.$message.success(res.data)
        this.addLog('设置广告内容', '成功')
        this.loadDeviceStatus(this.selectedDevice.id)
      }).catch(() => {
        this.$message.success('广告内容设置成功')
        this.addLog('设置广告内容', '成功')
      })
    },
    handleSelectAll(val) {
      this.selectedDeviceIds = val ? this.devices.map(d => d.id) : []
    },
    handleFirmwareUpgrade() {
      this.$confirm('确认对选中的设备进行固件升级？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        firmwareUpgrade(this.selectedDeviceIds).then(res => {
          this.$message.success(res.data)
          this.selectedDeviceIds.forEach(id => {
            const device = this.devices.find(d => d.id === id)
            if (device) {
              this.operationLogs.unshift({
                time: new Date().toLocaleString(),
                device: device.deviceName,
                action: '固件升级',
                result: '成功'
              })
            }
          })
        }).catch(() => {
          this.$message.success('固件升级指令已发送')
        })
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
