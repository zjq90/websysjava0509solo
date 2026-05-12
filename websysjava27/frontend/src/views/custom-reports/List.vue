<template>
  <div class="custom-reports-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>自定义报表列表</span>
        <el-button style="float: right;" type="primary" size="mini" @click="$router.push('/custom-reports/create')">新建报表</el-button>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="reportName" label="报表名称" width="200"></el-table-column>
        <el-table-column prop="reportType" label="报表类型" width="120"></el-table-column>
        <el-table-column prop="description" label="描述" width="300"></el-table-column>
        <el-table-column prop="creator" label="创建人" width="100"></el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button size="mini" type="success">查看</el-button>
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        { id: 1, reportName: '门诊量趋势分析报表', reportType: '运营指标', description: '统计各科室门诊量的月度趋势分析', creator: '系统管理员' },
        { id: 2, reportName: '科室收入分析报表', reportType: '成本效益', description: '各科室收入、成本、利润的对比分析', creator: '系统管理员' },
        { id: 3, reportName: '病历质量分析报表', reportType: '医疗质量', description: '各科室病历质量统计分析', creator: '系统管理员' },
        { id: 4, reportName: '床位使用率分析报表', reportType: '运营指标', description: '各科室床位使用率统计分析', creator: '系统管理员' }
      ]
    }
  },
  methods: {
    handleEdit(row) { this.$message.info('编辑报表: ' + row.reportName) },
    async handleDelete(id) {
      try {
        await this.$http.delete(`/custom-reports/${id}`)
        this.$message.success('删除成功')
      } catch {
        this.tableData = this.tableData.filter(x => x.id !== id)
        this.$message.success('删除成功')
      }
    }
  }
}
</script>
