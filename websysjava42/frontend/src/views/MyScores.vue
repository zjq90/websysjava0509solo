<template>
  <div class="page-container">
    <el-card title="我的比赛成绩">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="competitionNo" label="比赛编号" width="120" />
        <el-table-column prop="competitionName" label="比赛名称" />
        <el-table-column prop="refereeName" label="评分裁判" width="100" />
        <el-table-column prop="technicalScore" label="技术分" width="100" />
        <el-table-column prop="performanceScore" label="表现分" width="100" />
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="comment" label="评语" show-overflow-tooltip />
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.auditStatus === 1 ? 'success' : scope.row.auditStatus === 2 ? 'danger' : 'warning'">
              {{ ['待审核', '已通过', '已驳回'][scope.row.auditStatus] }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const tableData = ref([])

const loadData = async () => {
  const res = await api.getScoresByAthlete(user.athleteId || user.id)
  tableData.value = res.data || []
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}
</style>
