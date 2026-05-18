<template>
  <div class="monitoring-rules">
    <h2>监测规则配置</h2>
    <el-table :data="ruleList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="ruleName" label="规则名称" width="150"></el-table-column>
      <el-table-column prop="ruleType" label="规则类型" width="120"></el-table-column>
      <el-table-column prop="frequencyThreshold" label="频率阈值" width="120"></el-table-column>
      <el-table-column prop="frequencyMinutes" label="时间范围(分钟)" width="150"></el-table-column>
      <el-table-column prop="priceDeviationThreshold" label="价格偏离阈值" width="150"></el-table-column>
      <el-table-column prop="enabled" label="启用" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enabled"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="primary">编辑</el-button>
          <el-button size="mini" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'MonitoringRules',
  data() {
    return {
      ruleList: []
    }
  },
  mounted() {
    this.loadMockData()
  },
  methods: {
    loadMockData() {
      this.ruleList = [
        { id: 1, ruleName: '高频交易监测', ruleType: 'FREQUENCY', frequencyThreshold: 10, frequencyMinutes: 60, priceDeviationThreshold: null, enabled: true },
        { id: 2, ruleName: '价格偏离监测', ruleType: 'PRICE_DEVIATION', frequencyThreshold: null, frequencyMinutes: null, priceDeviationThreshold: 50, enabled: true },
        { id: 3, ruleName: '低信用分监测', ruleType: 'LOW_CREDIT', frequencyThreshold: null, frequencyMinutes: null, priceDeviationThreshold: null, lowCreditScoreThreshold: 60, enabled: true }
      ]
    }
  }
}
</script>

<style scoped>
.monitoring-rules {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
