<template>
  <div class="risk-alert">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c;">
            <i class="el-icon-warning"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ riskStats.criticalCount || 0 }}</div>
            <div class="stat-label">严重预警</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c;">
            <i class="el-icon-info"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ riskStats.warningCount || 0 }}</div>
            <div class="stat-label">警告预警</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff;">
            <i class="el-icon-time"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ riskStats.pendingCount || 0 }}</div>
            <div class="stat-label">待处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a;">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ riskStats.handledCount || 0 }}</div>
            <div class="stat-label">已处理</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>阈值配置</span>
          </div>
          <el-form label-width="120px">
            <el-form-item label="温度临界值">
              <el-input-number v-model="thresholds.temperature" :min="0" :max="100"></el-input-number>
              <span style="margin-left: 10px;">°C</span>
            </el-form-item>
            <el-form-item label="湿度临界值">
              <el-input-number v-model="thresholds.humidity" :min="0" :max="100"></el-input-number>
              <span style="margin-left: 10px;">%</span>
            </el-form-item>
            <el-form-item label="震动临界值">
              <el-input-number v-model="thresholds.vibration" :min="0" :max="10" :step="0.1"></el-input-number>
              <span style="margin-left: 10px;">g</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateThresholds">更新阈值</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>风险检测</span>
          </div>
          <el-button type="danger" @click="checkRisks" style="margin-bottom: 20px;">
            <i class="el-icon-search"></i> 执行环境风险检测
          </el-button>
          <div v-if="alerts.length > 0">
            <h4>检测结果</h4>
            <el-alert
              v-for="(alert, index) in alerts"
              :key="index"
              :title="alert.alertTitle"
              :type="alert.alertLevel === 'critical' ? 'error' : 'warning'"
              :closable="false"
              style="margin: 10px 0;">
              <template slot="default">
                {{ alert.alertContent }}
              </template>
            </el-alert>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'RiskAlert',
  data() {
    return {
      riskStats: {
        criticalCount: 2,
        warningCount: 5,
        pendingCount: 4,
        handledCount: 3
      },
      thresholds: {
        temperature: 35,
        humidity: 70,
        vibration: 1.5
      },
      alerts: []
    }
  },
  methods: {
    async checkRisks() {
      try {
        // 先生成测试设备数据
        const dataResponse = await this.$http.post('/analysis/generate-iot-data', null, { params: { count: 20 } })
        if (dataResponse.data.code === 200) {
          const deviceData = dataResponse.data.data
          // 然后执行风险检测
          const response = await this.$http.post('/analysis/risk/check', deviceData)
          if (response.data.code === 200) {
            this.alerts = response.data.data.slice(0, 5)
            this.$message.success(`检测完成，发现 ${this.alerts.length} 条预警`)
          }
        }
      } catch (error) {
        this.$message.error('风险检测失败')
      }
    },
    updateThresholds() {
      this.$message.success('阈值已更新')
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}
.stat-card {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-right: 15px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}
</style>
