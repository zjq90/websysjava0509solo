<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center">
            <div style="flex: 1">
              <div style="font-size: 14px; color: #909399; margin-bottom: 10px">设备总数</div>
              <div style="font-size: 28px; font-weight: bold; color: #409EFF">{{ statistics.total }}</div>
            </div>
            <i class="el-icon-monitor" style="font-size: 40px; color: #409EFF"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center">
            <div style="flex: 1">
              <div style="font-size: 14px; color: #909399; margin-bottom: 10px">在线设备</div>
              <div style="font-size: 28px; font-weight: bold; color: #67C23A">{{ statistics.online }}</div>
            </div>
            <i class="el-icon-circle-check" style="font-size: 40px; color: #67C23A"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center">
            <div style="flex: 1">
              <div style="font-size: 14px; color: #909399; margin-bottom: 10px">离线设备</div>
              <div style="font-size: 28px; font-weight: bold; color: #E6A23C">{{ statistics.offline }}</div>
            </div>
            <i class="el-icon-circle-close" style="font-size: 40px; color: #E6A23C"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center">
            <div style="flex: 1">
              <div style="font-size: 14px; color: #909399; margin-bottom: 10px">故障设备</div>
              <div style="font-size: 28px; font-weight: bold; color: #F56C6C">{{ statistics.fault }}</div>
            </div>
            <i class="el-icon-warning-outline" style="font-size: 40px; color: #F56C6C"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>快捷入口</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8" v-for="item in quickLinks" :key="item.path">
              <div style="text-align: center; padding: 20px; cursor: pointer" @click="$router.push(item.path)">
                <i :class="item.icon" style="font-size: 32px; color: #409EFF"></i>
                <div style="margin-top: 10px; color: #606266">{{ item.name }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>系统信息</span>
          </div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统名称">Web平台管理系统</el-descriptions-item>
            <el-descriptions-item label="系统版本">1.0.0</el-descriptions-item>
            <el-descriptions-item label="技术栈">SpringBoot + Vue + H2</el-descriptions-item>
            <el-descriptions-item label="运行环境">JDK8 + Node.js</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDeviceStatistics } from '@/api/device'

export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {
        total: 0,
        online: 0,
        offline: 0,
        fault: 0
      },
      quickLinks: [
        { name: '用户管理', path: '/system/user', icon: 'el-icon-user' },
        { name: '设备列表', path: '/device/list', icon: 'el-icon-monitor' },
        { name: '远程控制', path: '/device/remote', icon: 'el-icon-s-operation' },
        { name: '商品列表', path: '/product/list', icon: 'el-icon-goods' },
        { name: '货道管理', path: '/product/slot', icon: 'el-icon-s-platform' },
        { name: '角色管理', path: '/system/role', icon: 'el-icon-s-custom' }
      ]
    }
  },
  created() {
    this.loadStatistics()
  },
  methods: {
    loadStatistics() {
      getDeviceStatistics().then(res => {
        this.statistics = res.data
      }).catch(() => {
        this.statistics = { total: 5, online: 3, offline: 1, fault: 1 }
      })
    }
  }
}
</script>
