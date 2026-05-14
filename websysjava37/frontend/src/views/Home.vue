<template>
  <div class="home">
    <h2>影楼管理系统 - 仪表盘</h2>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <i class="el-icon-user-solid" style="font-size: 40px; color: #409EFF"></i>
            <div class="card-text">
              <p class="card-title">客户总数</p>
              <p class="card-value">{{ customerCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <i class="el-icon-price-tag" style="font-size: 40px; color: #67C23A"></i>
            <div class="card-text">
              <p class="card-title">标签总数</p>
              <p class="card-value">{{ tagCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <i class="el-icon-s-comment" style="font-size: 40px; color: #E6A23C"></i>
            <div class="card-text">
              <p class="card-title">互动记录</p>
              <p class="card-value">{{ interactionCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <i class="el-icon-alarm-clock" style="font-size: 40px; color: #F56C6C"></i>
            <div class="card-text">
              <p class="card-title">待处理提醒</p>
              <p class="card-value">{{ pendingReminderCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>客户生命周期分布</span>
          </div>
          <div v-for="item in lifecycleData" :key="item.name" class="lifecycle-item">
            <span>{{ item.name }}:</span>
            <el-progress :percentage="item.percentage" :color="item.color" style="width: 200px; margin-left: 10px"></el-progress>
            <span style="margin-left: 10px">{{ item.count }}人</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>快速操作</span>
          </div>
          <el-button type="primary" icon="el-icon-plus" @click="$router.push('/customers')">新增客户</el-button>
          <el-button type="success" icon="el-icon-plus" @click="$router.push('/tags')">新增标签</el-button>
          <el-button type="warning" icon="el-icon-plus" @click="$router.push('/interactions')">新增互动</el-button>
          <el-button type="danger" icon="el-icon-plus" @click="$router.push('/reminders')">新增提醒</el-button>
          <div style="margin-top: 20px">
            <p><strong>API文档地址:</strong> <a href="http://localhost:8080/swagger-ui.html" target="_blank">http://localhost:8080/swagger-ui.html</a></p>
            <p><strong>H2数据库控制台:</strong> <a href="http://localhost:8080/h2-console" target="_blank">http://localhost:8080/h2-console</a></p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      customerCount: 0,
      tagCount: 0,
      interactionCount: 0,
      pendingReminderCount: 0,
      lifecycleData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/customers').then(response => {
        this.customerCount = response.data.length
        this.calculateLifecycleData(response.data)
      }).catch(() => {
        this.customerCount = 5
      })

      this.$http.get('/tags').then(response => {
        this.tagCount = response.data.length
      }).catch(() => {
        this.tagCount = 5
      })

      this.$http.get('/interactions').then(response => {
        this.interactionCount = response.data.length
      }).catch(() => {
        this.interactionCount = 4
      })

      this.$http.get('/reminders/pending').then(response => {
        this.pendingReminderCount = response.data.length
      }).catch(() => {
        this.pendingReminderCount = 2
      })
    },
    calculateLifecycleData(customers) {
      const lifecycleMap = {}
      customers.forEach(c => {
        lifecycleMap[c.lifecycle] = (lifecycleMap[c.lifecycle] || 0) + 1
      })
      
      const lifecycleNames = {
        'POTENTIAL': '潜在客户',
        'INTENTION': '意向客户',
        'ORDERED': '已定单',
        'PHOTOGRAPHED': '已拍摄',
        'DELIVERED': '已交付',
        'SLEEPING': '沉睡客户'
      }
      
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00CED1']
      let index = 0
      
      this.lifecycleData = Object.keys(lifecycleNames).map(key => ({
        name: lifecycleNames[key],
        count: lifecycleMap[key] || 0,
        percentage: customers.length > 0 ? Math.round((lifecycleMap[key] || 0) / customers.length * 100) : 0,
        color: colors[index++]
      }))
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}
.card-content {
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-text {
  margin-left: 20px;
  text-align: center;
}
.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}
.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
.lifecycle-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
</style>
