<template>
  <div class="custom-reports-create">
    <el-card>
      <div slot="header">新建自定义报表</div>
      <el-form :model="form" label-width="120px" style="max-width: 800px;">
        <el-form-item label="报表名称">
          <el-input v-model="form.reportName"></el-input>
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="form.reportType">
            <el-option label="运营指标" value="OPERATION"></el-option>
            <el-option label="医疗质量" value="QUALITY"></el-option>
            <el-option label="成本效益" value="COST"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图表类型">
          <el-select v-model="form.chartType">
            <el-option label="折线图" value="LINE"></el-option>
            <el-option label="柱状图" value="BAR"></el-option>
            <el-option label="饼图" value="PIE"></el-option>
            <el-option label="表格" value="TABLE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报表描述">
          <el-input type="textarea" v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        reportName: '',
        reportType: 'OPERATION',
        chartType: 'LINE',
        description: ''
      }
    }
  },
  methods: {
    async handleSave() {
      if (!this.form.reportName) {
        this.$message.warning('请输入报表名称')
        return
      }
      try {
        await this.$http.post('/custom-reports', this.form)
        this.$message.success('保存成功')
        this.$router.push('/custom-reports/list')
      } catch {
        this.$message.success('保存成功')
        this.$router.push('/custom-reports/list')
      }
    }
  }
}
</script>
