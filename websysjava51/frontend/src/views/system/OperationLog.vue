<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">操作日志</h2>
    </div>
    <div class="table-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="操作人">
          <el-select v-model="searchForm.operatorId" placeholder="请选择操作人" style="width: 150px;" clearable>
            <el-option label="管理员" :value="1"></el-option>
            <el-option label="卖家" :value="2"></el-option>
            <el-option label="客服" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="searchForm.module" placeholder="请选择模块" style="width: 150px;" clearable>
            <el-option label="商品管理" value="商品管理"></el-option>
            <el-option label="订单管理" value="订单管理"></el-option>
            <el-option label="纠纷管理" value="纠纷管理"></el-option>
            <el-option label="数据统计" value="数据统计"></el-option>
            <el-option label="系统管理" value="系统管理"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" style="width: 120px;" clearable>
            <el-option label="新增" value="CREATE"></el-option>
            <el-option label="修改" value="UPDATE"></el-option>
            <el-option label="删除" value="DELETE"></el-option>
            <el-option label="查询" value="QUERY"></el-option>
            <el-option label="导出" value="EXPORT"></el-option>
            <el-option label="导入" value="IMPORT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px;"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="operatorName" label="操作人" width="120"></el-table-column>
        <el-table-column prop="module" label="操作模块" width="120">
          <template slot-scope="scope">
            <el-tag :type="getModuleType(scope.row.module)" size="small">{{ scope.row.module }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="操作类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOperationType(scope.row.type)" size="small">{{ getOperationTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="操作描述" min-width="250"></el-table-column>
        <el-table-column prop="result" label="执行结果" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.result === 'SUCCESS' ? 'success' : 'danger'" size="small">
              {{ scope.row.result === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140"></el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="180"></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog title="日志详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="操作人">{{ currentLog.operatorName }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ getOperationTypeName(currentLog.type) }}</el-descriptions-item>
        <el-descriptions-item label="操作描述">{{ currentLog.description }}</el-descriptions-item>
        <el-descriptions-item label="执行结果">
          <el-tag :type="currentLog.result === 'SUCCESS' ? 'success' : 'danger'">
            {{ currentLog.result === 'SUCCESS' ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" v-if="currentLog.params">
          <pre style="margin: 0; max-height: 200px; overflow-y: auto; background: #f5f7fa; padding: 10px; border-radius: 4px;">{{ currentLog.params }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" v-if="currentLog.errorMessage">
          <pre style="margin: 0; max-height: 200px; overflow-y: auto; background: #fef0f0; color: #f56c6c; padding: 10px; border-radius: 4px;">{{ currentLog.errorMessage }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'OperationLog',
  data() {
    return {
      searchForm: {
        operatorId: '',
        module: '',
        type: '',
        dateRange: []
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      detailDialogVisible: false,
      currentLog: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const operators = ['管理员', '测试卖家', '客服小王']
      const modules = ['商品管理', '订单管理', '纠纷管理', '数据统计', '系统管理']
      const types = ['CREATE', 'UPDATE', 'DELETE', 'QUERY', 'EXPORT', 'IMPORT']
      const descriptions = [
        '新增商品: iPhone 15 Pro',
        '修改商品价格: 从 9999 改为 8999',
        '删除商品: 测试商品1',
        '批量下架商品: 共10件',
        '导出商品列表: Excel格式',
        '导入商品列表: Excel格式',
        '修改订单状态: 已发货',
        '处理纠纷: 订单ORD001',
        '生成统计报表: 2024年1月',
        '新增用户: testuser',
        '修改角色权限: 卖家角色',
        '查询操作日志'
      ]
      
      const mockData = []
      for (let i = 1; i <= 50; i++) {
        const success = Math.random() > 0.1
        mockData.push({
          id: i,
          operatorId: (i % 3) + 1,
          operatorName: operators[i % 3],
          module: modules[i % 5],
          type: types[i % 6],
          description: descriptions[i % descriptions.length] + i,
          result: success ? 'SUCCESS' : 'FAILURE',
          errorMessage: success ? '' : '网络超时，请稍后重试',
          params: success ? '{"page": 1, "size": 10}' : '',
          ipAddress: '192.168.1.' + (i % 255),
          operationTime: '2024-01-' + String((i % 30) + 1).padStart(2, '0') + ' ' + String((i % 24)).padStart(2, '0') + ':' + String((i * 7) % 60).padStart(2, '0') + ':' + String((i * 13) % 60).padStart(2, '0')
        })
      }
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    getModuleType(module) {
      const map = {
        '商品管理': 'warning',
        '订单管理': 'primary',
        '纠纷管理': 'danger',
        '数据统计': 'success',
        '系统管理': 'info'
      }
      return map[module] || 'info'
    },
    getOperationType(type) {
      const map = {
        'CREATE': 'success',
        'UPDATE': 'primary',
        'DELETE': 'danger',
        'QUERY': 'info',
        'EXPORT': 'warning',
        'IMPORT': 'warning'
      }
      return map[type] || 'info'
    },
    getOperationTypeName(type) {
      const map = {
        'CREATE': '新增',
        'UPDATE': '修改',
        'DELETE': '删除',
        'QUERY': '查询',
        'EXPORT': '导出',
        'IMPORT': '导入'
      }
      return map[type] || type
    },
    handleSearch() {
      this.$message.success('搜索成功')
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        operatorId: '',
        module: '',
        type: '',
        dateRange: []
      }
      this.fetchData()
    },
    handleDetail(row) {
      this.currentLog = { ...row }
      this.detailDialogVisible = true
    },
    handleSizeChange(size) {
      this.pagination.size = size
    },
    handleCurrentChange(page) {
      this.pagination.page = page
    }
  }
}
</script>

<style scoped>
</style>