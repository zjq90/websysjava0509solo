<template>
  <div class="heritage-list">
    <h2>文物列表</h2>
    <el-table :data="heritageList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文物名称" width="180"></el-table-column>
      <el-table-column prop="dynasty" label="朝代" width="120"></el-table-column>
      <el-table-column prop="material" label="材质" width="120"></el-table-column>
      <el-table-column prop="province" label="省份" width="120"></el-table-column>
      <el-table-column prop="estimatedValue" label="预估价值" width="150"></el-table-column>
      <el-table-column prop="auditStatus" label="审核状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.auditStatus)">{{ getStatusText(scope.row.auditStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'HeritageList',
  data() {
    return {
      heritageList: []
    }
  },
  mounted() {
    this.loadHeritageList()
  },
  methods: {
    loadHeritageList() {
      this.$http.get('/heritage').then(res => {
        if (res.data.code === 200) {
          this.heritageList = res.data.data
        }
      })
    },
    getStatusType(status) {
      const map = { 'APPROVED': 'success', 'REJECTED': 'danger', 'PENDING': 'warning', 'REVIEWING': 'info' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 'APPROVED': '已通过', 'REJECTED': '已拒绝', 'PENDING': '待审核', 'REVIEWING': '审核中' }
      return map[status] || status
    },
    viewDetail(row) {
      this.$alert(JSON.stringify(row, null, 2), '文物详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    }
  }
}
</script>

<style scoped>
.heritage-list {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
