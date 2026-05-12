<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>待处理申请</span>
          </div>
          <div class="text item">
            <h1 style="color: #409EFF; text-align: center">{{ stats.pendingCount }}</h1>
            <p style="text-align: center; color: #909399">个申请待处理</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>检验中</span>
          </div>
          <div class="text item">
            <h1 style="color: #E6A23C; text-align: center">{{ stats.processingCount }}</h1>
            <p style="text-align: center; color: #909399">个项目进行中</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>待审核报告</span>
          </div>
          <div class="text item">
            <h1 style="color: #F56C6C; text-align: center">{{ stats.auditCount }}</h1>
            <p style="text-align: center; color: #909399">个报告待审核</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>已发布报告</span>
          </div>
          <div class="text item">
            <h1 style="color: #67C23A; text-align: center">{{ stats.publishedCount }}</h1>
            <p style="text-align: center; color: #909399">个报告已发布</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>最近申请</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="$router.push('/applications')">查看全部</el-button>
          </div>
          <el-table :data="recentApplications" style="width: 100%">
            <el-table-column prop="applicationNo" label="申请编号" width="150"></el-table-column>
            <el-table-column prop="applicationType" label="类型" width="100">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.applicationType === 'LABORATORY'" type="success">检验</el-tag>
                <el-tag v-else type="warning">检查</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="clinicalDiagnosis" label="诊断"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>系统功能快捷入口</span>
          </div>
          <div style="padding: 20px">
            <el-row :gutter="20">
              <el-col :span="8" style="text-align: center; margin-bottom: 20px">
                <el-button type="primary" icon="el-icon-plus" circle size="large" @click="$router.push('/applications')"></el-button>
                <p>新增申请</p>
              </el-col>
              <el-col :span="8" style="text-align: center; margin-bottom: 20px">
                <el-button type="success" icon="el-icon-edit" circle size="large" @click="$router.push('/technician')"></el-button>
                <p>结果录入</p>
              </el-col>
              <el-col :span="8" style="text-align: center; margin-bottom: 20px">
                <el-button type="warning" icon="el-icon-check" circle size="large" @click="$router.push('/reports/audit')"></el-button>
                <p>报告审核</p>
              </el-col>
              <el-col :span="8" style="text-align: center">
                <el-button type="info" icon="el-icon-document" circle size="large" @click="$router.push('/reports')"></el-button>
                <p>查看报告</p>
              </el-col>
              <el-col :span="8" style="text-align: center">
                <el-button type="danger" icon="el-icon-user" circle size="large" @click="$router.push('/patients')"></el-button>
                <p>患者管理</p>
              </el-col>
              <el-col :span="8" style="text-align: center">
                <el-button type="primary" icon="el-icon-setting" circle size="large" @click="$router.push('/items')"></el-button>
                <p>项目管理</p>
              </el-col>
            </el-row>
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
      stats: {
        pendingCount: 0,
        processingCount: 0,
        auditCount: 0,
        publishedCount: 0
      },
      recentApplications: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadRecentApplications()
  },
  methods: {
    loadStats() {
      this.$http.get('/api/applications').then(response => {
        const apps = response.data
        this.stats.pendingCount = apps.filter(a => a.status === 'PENDING' || a.status === 'CONFIRMED').length
        this.stats.processingCount = apps.filter(a => a.status === 'PROCESSING').length
      }).catch(error => {
        console.error('加载申请统计失败:', error)
      })
      this.$http.get('/api/reports').then(response => {
        const reports = response.data
        this.stats.auditCount = reports.filter(r => r.status === 'FIRST_AUDIT' || r.status === 'SECOND_AUDIT').length
        this.stats.publishedCount = reports.filter(r => r.status === 'PUBLISHED').length
      }).catch(error => {
        console.error('加载报告统计失败:', error)
      })
    },
    loadRecentApplications() {
      this.$http.get('/api/applications').then(response => {
        this.recentApplications = response.data.slice(0, 5)
      }).catch(error => {
        console.error('加载最近申请失败:', error)
      })
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'info',
        'CONFIRMED': 'warning',
        'ASSIGNED': 'primary',
        'PROCESSING': 'warning',
        'REPORTED': 'success',
        'PUBLISHED': 'success'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待确认',
        'CONFIRMED': '已确认',
        'ASSIGNED': '已分配',
        'PROCESSING': '处理中',
        'REPORTED': '已出报告',
        'PUBLISHED': '已发布'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.box-card {
  height: 100%;
}
</style>
