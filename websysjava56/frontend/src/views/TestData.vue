<template>
  <div class="test-data">
    <el-card>
      <div slot="header" class="card-header">
        <span>测试数据生成</span>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="test-section">
            <h3><i class="el-icon-s-management"></i> 文物数据</h3>
            <p>生成文物测试数据，包括名称、朝代、类别、材质等信息</p>
            <el-input-number v-model="heritageCount" :min="1" :max="100" label="生成数量"></el-input-number>
            <el-button type="primary" @click="generateHeritageData" style="margin-top: 10px;">
              生成文物数据
            </el-button>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="test-section">
            <h3><i class="el-icon-user"></i> 用户行为日志</h3>
            <p>生成用户点击、浏览、收藏、购买等行为日志</p>
            <el-input-number v-model="behaviorCount" :min="1" :max="500" label="生成数量"></el-input-number>
            <el-button type="success" @click="generateBehaviorData" style="margin-top: 10px;">
              生成行为日志
            </el-button>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="test-section">
            <h3><i class="el-icon-cpu"></i> 物联网设备数据</h3>
            <p>生成温湿度、光照、震动等环境监测数据</p>
            <el-input-number v-model="iotCount" :min="1" :max="200" label="生成数量"></el-input-number>
            <el-button type="warning" @click="generateIotData" style="margin-top: 10px;">
              生成设备数据
            </el-button>
          </div>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <div class="quick-actions">
        <h3>快捷操作</h3>
        <el-button type="danger" size="large" @click="generateAllData">
          <i class="el-icon-magic-stick"></i> 一键生成所有测试数据
        </el-button>
      </div>
    </el-card>

    <el-card style="margin-top: 20px;">
      <div slot="header" class="card-header">
        <span>API 开放平台 & 沙箱环境</span>
      </div>
      <el-alert
        title="沙箱环境"
        type="info"
        :closable="false"
        show-icon>
        <template slot="default">
          <p>沙箱环境提供完整的API测试功能，支持：</p>
          <ul>
            <li>文物数据查询API</li>
            <li>用户行为分析API</li>
            <li>数据订阅推送API</li>
            <li>数据脱敏处理</li>
          </ul>
        </template>
      </el-alert>
      <div style="margin-top: 20px;">
        <h4>API 文档地址</h4>
        <el-link href="http://localhost:8080/api/swagger-ui.html" target="_blank" type="primary">
          http://localhost:8080/api/swagger-ui.html
        </el-link>
      </div>
      <div style="margin-top: 20px;">
        <h4>H2 数据库控制台</h4>
        <el-link href="http://localhost:8080/api/h2-console" target="_blank" type="success">
          http://localhost:8080/api/h2-console
        </el-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'TestData',
  data() {
    return {
      heritageCount: 20,
      behaviorCount: 100,
      iotCount: 50
    }
  },
  methods: {
    async generateHeritageData() {
      try {
        const response = await this.$http.post('/heritages/generate-test-data', null, {
          params: { count: this.heritageCount }
        })
        if (response.data.code === 200) {
          this.$message.success(`成功生成 ${this.heritageCount} 条文物数据`)
        }
      } catch (error) {
        this.$message.error('生成文物数据失败')
      }
    },
    async generateBehaviorData() {
      try {
        const response = await this.$http.post('/analysis/generate-behavior-logs', null, {
          params: { count: this.behaviorCount }
        })
        if (response.data.code === 200) {
          this.$message.success(`成功生成 ${this.behaviorCount} 条行为日志`)
        }
      } catch (error) {
        this.$message.error('生成行为日志失败')
      }
    },
    async generateIotData() {
      try {
        const response = await this.$http.post('/analysis/generate-iot-data', null, {
          params: { count: this.iotCount }
        })
        if (response.data.code === 200) {
          this.$message.success(`成功生成 ${this.iotCount} 条设备数据`)
        }
      } catch (error) {
        this.$message.error('生成设备数据失败')
      }
    },
    async generateAllData() {
      this.$confirm('确认生成所有测试数据吗？这可能需要一点时间', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await Promise.all([
          this.generateHeritageData(),
          this.generateBehaviorData(),
          this.generateIotData()
        ])
        this.$message.success('所有测试数据生成完成！')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}
.test-section {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  min-height: 200px;
}
.test-section h3 {
  margin: 0 0 10px 0;
  color: #303133;
}
.test-section p {
  color: #606266;
  margin-bottom: 15px;
  font-size: 14px;
}
.quick-actions {
  text-align: center;
  padding: 20px 0;
}
.quick-actions h3 {
  margin-bottom: 20px;
  color: #303133;
}
</style>
