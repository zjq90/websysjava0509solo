<template>
  <div class="technician-tasks">
    <el-card>
      <div slot="header" class="clearfix">
        <span>技师工作站 - 待处理任务</span>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="tasks" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="applicationNo" label="申请编号" width="150"></el-table-column>
        <el-table-column prop="applicationType" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.applicationType === 'LABORATORY'" type="success" size="small">检验</el-tag>
            <el-tag v-else type="warning" size="small">检查</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="clinicalDiagnosis" label="临床诊断" width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="success" icon="el-icon-video-play" @click="startTask(scope.row.id)" v-if="scope.row.status === 'ASSIGNED'">开始</el-button>
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="inputResult(scope.row.id)" v-if="scope.row.status === 'PROCESSING'">录入结果</el-button>
            <el-button size="mini" type="info" icon="el-icon-document" @click="viewDetail">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'TechnicianTasks',
  data() {
    return {
      tasks: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/applications/technician/3').then(response => {
        this.tasks = response.data
      }).catch(() => {
        this.$http.get('/api/applications').then(response => {
          this.tasks = response.data.filter(a => a.status === 'ASSIGNED' || a.status === 'PROCESSING')
        }).catch(error => {
          console.error('加载任务失败:', error)
          this.$message.error('加载数据失败')
        })
      })
    },
    startTask(id) {
      this.$http.put(`/api/applications/${id}/start`).then(() => {
        this.$message.success('已开始处理')
        this.loadData()
      }).catch(error => {
        console.error('开始任务失败:', error)
        this.$message.error('操作失败')
      })
    },
    inputResult(id) {
      this.$router.push('/results')
    },
    viewDetail() {
      this.$message.info('查看详情功能')
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'info',
        'CONFIRMED': 'warning',
        'ASSIGNED': 'primary',
        'PROCESSING': 'warning',
        'REPORTED': 'success'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待确认',
        'CONFIRMED': '已确认',
        'ASSIGNED': '已分配',
        'PROCESSING': '处理中',
        'REPORTED': '已出报告'
      }
      return map[status] || status
    }
  }
}
</script>

<style scoped>
.technician-tasks {
  padding: 0;
}
</style>
