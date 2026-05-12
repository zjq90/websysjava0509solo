<template>
  <div class="application-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>检验检查申请列表</span>
        <el-button style="float: right; margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleAdd">新增申请</el-button>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-form :inline="true" class="demo-table">
        <el-table :data="applications" style="width: 100%" border>
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column prop="applicationNo" label="申请编号" width="150"></el-table-column>
          <el-table-column prop="applicationType" label="类型" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.applicationType === 'LABORATORY'" type="success" size="small">检验</el-tag>
              <el-tag v-else type="warning" size="small">检查</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="clinicalDiagnosis" label="临床诊断" width="150"></el-table-column>
          <el-table-column prop="remarks" label="备注"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">{{ getStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" icon="el-icon-check" @click="confirmApplication(scope.row.id)" v-if="scope.row.status === 'PENDING'">确认</el-button>
              <el-button size="mini" type="warning" icon="el-icon-user" @click="assignTechnician(scope.row.id)" v-if="scope.row.status === 'CONFIRMED'">分配</el-button>
              <el-button size="mini" type="success" icon="el-icon-video-play" @click="startProcessing(scope.row.id)" v-if="scope.row.status === 'ASSIGNED'">开始</el-button>
              <el-button size="mini" type="info" icon="el-icon-document" @click="viewDetail">查看</el-button>
              <el-button size="mini" type="danger" icon="el-icon-delete">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ApplicationList',
  data() {
    return {
      applications: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    handleAdd() {
      this.$message.info('新增申请功能待实现')
    },
    loadData() {
      this.$http.get('/api/applications').then(response => {
        this.applications = response.data
      }).catch(error => {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    confirmApplication(id) {
      this.$http.put(`/api/applications/${id}/confirm?confirmedBy=1`).then(() => {
        this.$message.success('确认成功')
        this.loadData()
      }).catch(error => {
        console.error('确认失败:', error)
        this.$message.error('操作失败')
      })
    },
    assignTechnician(id) {
      this.$prompt('请输入技师ID', '分配技师', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\d+/,
        inputErrorMessage: '请输入正确的ID格式'
      }).then(({ value }) => {
        this.$http.put(`/api/applications/${id}/assign?technicianId=${value}`).then(() => {
          this.$message.success('分配成功')
          this.loadData()
        }).catch(error => {
          console.error('分配失败:', error)
          this.$message.error('操作失败')
        })
      })
    },
    startProcessing(id) {
      this.$http.put(`/api/applications/${id}/start`).then(() => {
        this.$message.success('已开始处理')
        this.loadData()
      }).catch(error => {
        console.error('开始处理失败:', error)
        this.$message.error('操作失败')
      })
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
.application-list {
  padding: 0;
}
</style>
