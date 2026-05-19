<template>
  <div class="data-governance">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>数据清洗</span>
          </div>
          <div class="clean-options">
            <el-button type="primary" @click="cleanData">
              <i class="el-icon-check"></i> 执行数据清洗
            </el-button>
            <el-button type="success" @click="batchClean">
              <i class="el-icon-document-checked"></i> 批量清洗
            </el-button>
            <div class="clean-description">
              <p>数据清洗功能包括：</p>
              <ul>
                <li>去除重复数据</li>
                <li>标准化数据格式</li>
                <li>补全缺失字段</li>
                <li>计算数据质量评分</li>
              </ul>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>主数据管理</span>
          </div>
          <div class="master-options">
            <el-button type="warning" @click="viewMasterData">
              <i class="el-icon-s-data"></i> 查看主数据
            </el-button>
            <el-button type="success" @click="generateMasterCode">
              <i class="el-icon-key"></i> 生成主数据编码
            </el-button>
            <div class="master-description">
              <p>主数据标准：</p>
              <ul>
                <li>与国家文物编码标准对齐</li>
                <li>统一的朝代和类别编码</li>
                <li>数据质量评分需达到60分以上</li>
                <li>支持主数据溯源追踪</li>
              </ul>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="card-header">
            <span>数据源追踪</span>
          </div>
          <div ref="traceChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'DataGovernance',
  mounted() {
    this.initTraceChart()
  },
  methods: {
    cleanData() {
      this.$message.success('数据清洗已执行')
    },
    batchClean() {
      this.$message.success('批量清洗完成')
    },
    viewMasterData() {
      this.$router.push('/heritage')
    },
    generateMasterCode() {
      this.$message.success('主数据编码已生成')
    },
    initTraceChart() {
      const chart = echarts.init(this.$refs.traceChart)
      chart.setOption({
        title: { text: '数据溯源流向图' },
        tooltip: { trigger: 'item', triggerOn: 'mousemove' },
        series: [{
          type: 'sankey',
          layout: 'none',
          emphasis: { focus: 'adjacency' },
          data: [
            { name: '博物馆官网' },
            { name: '拍卖行数据' },
            { name: '用户上传' },
            { name: '物联网设备' },
            { name: '数据采集层' },
            { name: '数据清洗' },
            { name: '主数据管理' },
            { name: '文物数据库' }
          ],
          links: [
            { source: '博物馆官网', target: '数据采集层', value: 100 },
            { source: '拍卖行数据', target: '数据采集层', value: 80 },
            { source: '用户上传', target: '数据采集层', value: 50 },
            { source: '物联网设备', target: '数据采集层', value: 30 },
            { source: '数据采集层', target: '数据清洗', value: 260 },
            { source: '数据清洗', target: '主数据管理', value: 200 },
            { source: '主数据管理', target: '文物数据库', value: 180 }
          ],
          lineStyle: { color: 'gradient', curveness: 0.5 }
        }]
      })
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}
.clean-options, .master-options {
  padding: 20px 0;
}
.clean-description, .master-description {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 5px;
}
.clean-description p, .master-description p {
  font-weight: bold;
  margin-bottom: 10px;
}
.clean-description ul, .master-description ul {
  margin: 0;
  padding-left: 20px;
}
.clean-description li, .master-description li {
  margin: 5px 0;
  color: #606266;
}
</style>
