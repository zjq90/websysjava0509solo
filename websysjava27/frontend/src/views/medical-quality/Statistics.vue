<template>
  <div class="medical-quality-statistics">
    <el-card>
      <div slot="header" class="clearfix">
        <span>医疗质量统计分析</span>
      </div>
    </el-card>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix"><span>病历质量趋势</span></div>
          <div ref="recordQualityChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix"><span>合理用药率分析</span></div>
          <div ref="drugUseChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'MedicalQualityStatistics',
  mounted() {
    this.initCharts()
  },
  methods: {
    initCharts() {
      const chart1 = echarts.init(this.$refs.recordQualityChart)
      const dates = Array.from({length: 12}, (_, i) => `${i+1}月`)
      chart1.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', max: 100 },
        series: [{ name: '合格率', data: Array.from({length: 12}, () => 85 + Math.random() * 14), type: 'line', smooth: true }]
      })

      const chart2 = echarts.init(this.$refs.drugUseChart)
      chart2.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科'] },
        yAxis: { type: 'value', max: 100 },
        series: [{ name: '合理用药率', type: 'bar', data: [88, 92, 85, 90, 86, 94] }]
      })
    }
  }
}
</script>
