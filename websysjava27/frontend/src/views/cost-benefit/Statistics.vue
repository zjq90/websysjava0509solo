<template>
  <div class="cost-benefit-statistics">
    <el-card><div slot="header">成本效益统计分析</div></el-card>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12"><el-card><div slot="header">收入成本对比</div><div ref="incomeCostChart" style="height: 400px;"></div></el-card></el-col>
      <el-col :span="12"><el-card><div slot="header">各科室利润排名</div><div ref="profitChart" style="height: 400px;"></div></el-card></el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  mounted() {
    const chart1 = echarts.init(this.$refs.incomeCostChart)
    chart1.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['收入', '成本', '利润'] },
      xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科'] },
      yAxis: { type: 'value' },
      series: [
        { name: '收入', type: 'bar', data: [350000, 420000, 280000, 250000, 320000, 180000] },
        { name: '成本', type: 'bar', data: [280000, 320000, 220000, 190000, 250000, 140000] },
        { name: '利润', type: 'line', data: [70000, 100000, 60000, 60000, 70000, 40000] }
      ]
    })
    const chart2 = echarts.init(this.$refs.profitChart)
    chart2.setOption({
      tooltip: { trigger: 'item' },
      series: [{ type: 'pie', data: [
        { value: 100000, name: '外科' },
        { value: 70000, name: '内科' },
        { value: 70000, name: '骨科' },
        { value: 60000, name: '妇产科' },
        { value: 60000, name: '儿科' },
        { value: 40000, name: '眼科' }
      ]}]
    })
  }
}
</script>
