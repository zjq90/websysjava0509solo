<template>
  <div class="page-container">
    <div class="page-header">
      <span>日志管理</span>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="日志ID" width="80"></el-table-column>
        <el-table-column prop="username" label="操作人" width="120"></el-table-column>
        <el-table-column prop="module" label="操作模块" width="120"></el-table-column>
        <el-table-column prop="description" label="操作描述" width="150"></el-table-column>
        <el-table-column prop="ip" label="IP地址" width="130"></el-table-column>
        <el-table-column prop="url" label="请求URL"></el-table-column>
        <el-table-column prop="method" label="请求方式" width="100"></el-table-column>
        <el-table-column prop="duration" label="耗时(ms)" width="100"></el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'LogManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      request({
        url: '/system/log/list',
        method: 'get',
        params: {
          page: this.page - 1,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).finally(() => {
        this.loading = false
      })
    },
    handleSizeChange(size) {
      this.size = size
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.page = page
      this.fetchData()
    },
    handleDelete(row) {
      this.$confirm('确定要删除该日志吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/log/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    }
  }
}
</script>
