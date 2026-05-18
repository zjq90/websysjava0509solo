<template>
  <div class="desensitization-rules">
    <h2>脱敏规则配置</h2>
    <el-table :data="ruleList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="fieldName" label="字段名称" width="150"></el-table-column>
      <el-table-column prop="description" label="描述" width="200"></el-table-column>
      <el-table-column prop="regexPattern" label="正则表达式" width="250"></el-table-column>
      <el-table-column prop="replacement" label="替换规则" width="200"></el-table-column>
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
  name: 'DesensitizationRules',
  data() {
    return {
      ruleList: []
    }
  },
  mounted() {
    this.loadRules()
  },
  methods: {
    loadRules() {
      this.$http.get('/desensitization').then(res => {
        if (res.data.code === 200) {
          this.ruleList = res.data.data
        }
      })
    }
  }
}
</script>

<style scoped>
.desensitization-rules {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
