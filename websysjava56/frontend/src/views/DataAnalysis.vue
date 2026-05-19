<template>
  <div class="data-analysis">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>文物推荐</span>
          </div>
          <div class="recommend-section">
            <el-form :inline="true" :model="recommendForm">
              <el-form-item label="用户ID">
                <el-input v-model="recommendForm.userId"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getRecommendations">获取推荐</el-button>
              </el-form-item>
            </el-form>
            <div v-if="recommendations.length > 0" class="recommend-list">
              <h4>为您推荐的文物</h4>
              <el-list>
                <el-list-item v-for="item in recommendations" :key="item.id">
                  <span style="flex: 1">{{ item.name }}</span>
                  <el-tag size="small">{{ item.category }}</el-tag>
                  <el-tag size="small" type="warning">{{ item.dynasty }}</el-tag>
                </el-list-item>
              </el-list>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>用户偏好画像</span>
          </div>
          <div class="preference-section">
            <el-button type="primary" @click="getUserPreferences">获取用户偏好</el-button>
            <div v-if="preferences" class="preference-content">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="浏览次数">{{ preferences.viewCount }}</el-descriptions-item>
                <el-descriptions-item label="收藏次数">{{ preferences.collectCount }}</el-descriptions-item>
                <el-descriptions-item label="购买次数">{{ preferences.purchaseCount }}</el-descriptions-item>
                <el-descriptions-item label="点击次数">{{ preferences.clickCount }}</el-descriptions-item>
              </el-descriptions>
              <div style="margin-top: 15px">
                <h5>最喜爱类别:</h5>
                <el-tag v-for="cat in preferences.topCategories" :key="cat" style="margin: 3px">{{ cat }}</el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="card-header">
            <span>物联网设备数据验证</span>
          </div>
          <el-table :data="deviceData" border stripe style="width: 100%">
            <el-table-column prop="deviceId" label="设备ID" width="120"></el-table-column>
            <el-table-column prop="deviceName" label="设备名称" width="150"></el-table-column>
            <el-table-column prop="temperature" label="温度" width="100">
              <template slot-scope="scope">
                <span :style="{color: getTempColor(scope.row.temperature)}">{{ scope.row.temperature }}°C</span>
              </template>
            </el-table-column>
            <el-table-column prop="humidity" label="湿度" width="100">
              <template slot-scope="scope">
                <span :style="{color: getHumidityColor(scope.row.humidity)}">{{ scope.row.humidity }}%</span>
              </template>
            </el-table-column>
            <el-table-column prop="location" label="位置" width="120"></el-table-column>
            <el-table-column prop="isValid" label="验证状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="scope.row.isValid ? 'success' : 'danger'">
                  {{ scope.row.isValid ? '有效' : '无效' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="validationMessage" label="验证信息" show-overflow-tooltip></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'DataAnalysis',
  data() {
    return {
      recommendForm: {
        userId: '1'
      },
      recommendations: [],
      preferences: null,
      deviceData: []
    }
  },
  mounted() {
    this.getRecommendations()
    this.getUserPreferences()
    this.loadDeviceData()
  },
  methods: {
    async getRecommendations() {
      try {
        const response = await this.$http.get(`/analysis/recommendations/user/${this.recommendForm.userId}`)
        if (response.data.code === 200) {
          this.recommendations = response.data.data.slice(0, 5)
        }
      } catch (error) {
        console.error('获取推荐失败', error)
      }
    },
    async getUserPreferences() {
      try {
        const response = await this.$http.get(`/analysis/preferences/${this.recommendForm.userId}`)
        if (response.data.code === 200) {
          this.preferences = response.data.data
        }
      } catch (error) {
        console.error('获取用户偏好失败', error)
      }
    },
    async loadDeviceData() {
      try {
        const response = await this.$http.post('/analysis/generate-iot-data', null, { params: { count: 10 } })
        if (response.data.code === 200) {
          this.deviceData = response.data.data
        }
      } catch (error) {
        console.error('加载设备数据失败', error)
      }
    },
    getTempColor(temp) {
      if (temp > 35) return '#f56c6c'
      if (temp > 30) return '#e6a23c'
      return '#67c23a'
    },
    getHumidityColor(humidity) {
      if (humidity > 70) return '#f56c6c'
      if (humidity > 60) return '#e6a23c'
      return '#67c23a'
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}
.recommend-section, .preference-section {
  padding: 20px 0;
}
.recommend-list {
  margin-top: 20px;
}
.preference-content {
  margin-top: 20px;
}
</style>
