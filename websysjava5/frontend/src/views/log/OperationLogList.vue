<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">操作日志</div>
    </div>

    <div class="search-bar">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="操作用户">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="searchForm.module" placeholder="请选择模块" clearable>
            <el-option v-for="module in modules" :key="module" :label="getModuleText(module)" :value="module" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.operationType" placeholder="请选择类型" clearable>
            <el-option v-for="type in operationTypes" :key="type" :label="getOperationTypeText(type)" :value="type" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            default-time="['00:00:00', '23:59:59']"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="操作用户" width="120" />
      <el-table-column prop="module" label="操作模块" width="120">
        <template slot-scope="scope">
          <el-tag>{{ getModuleText(scope.row.module) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operationType" label="操作类型" width="100">
        <template slot-scope="scope">
          <el-tag :type="getOperationTypeTag(scope.row.operationType)">
            {{ getOperationTypeText(scope.row.operationType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="操作描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="ipAddress" label="IP地址" width="140" />
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag :class="scope.row.status === 1 ? 'status-tag status-enabled' : 'status-tag status-disabled'">
            {{ scope.row.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="耗时(ms)" width="100" />
      <el-table-column prop="createTime" label="操作时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleView(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrapper">
      <el-pagination
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>

    <el-dialog title="日志详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="操作用户">{{ currentLog.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ getModuleText(currentLog.module) }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ getOperationTypeText(currentLog.operationType) }}</el-descriptions-item>
        <el-descriptions-item label="操作状态">
          <el-tag :class="currentLog.status === 1 ? 'status-tag status-enabled' : 'status-tag status-disabled'">
            {{ currentLog.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作描述" :span="2">{{ currentLog.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作对象">{{ currentLog.targetName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog.costTime || '-' }} ms</el-descriptions-item>
        <el-descriptions-item label="浏览器" :span="2">{{ currentLog.browser || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作系统" :span="2">{{ currentLog.os || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentLog.requestUrl || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ currentLog.requestMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog.createTime }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2" v-if="currentLog.errorMsg">
          <el-alert :title="currentLog.errorMsg" type="error" :closable="false" show-icon />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getLogPage, getModules, getOperationTypes } from '@/api/log'

export default {
  name: 'OperationLogList',
  data() {
    return {
      loading: false,
      detailVisible: false,
      currentLog: null,
      modules: [],
      operationTypes: [],
      dateRange: [],
      searchForm: {
        username: '',
        module: '',
        operationType: '',
        status: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      tableData: []
    }
  },
  created() {
    this.fetchData()
    this.loadDictData()
  },
  methods: {
    getModuleText(module) {
      const map = {
        'AUTH': '认证',
        'AGENT': '代理商管理',
        'USER': '用户管理',
        'CONFIG': '系统配置',
        'DEVICE': '设备管理'
      }
      return map[module] || module
    },
    getOperationTypeText(type) {
      const map = {
        'LOGIN': '登录',
        'LOGOUT': '登出',
        'CREATE': '创建',
        'UPDATE': '更新',
        'DELETE': '删除',
        'QUERY': '查询'
      }
      return map[type] || type
    },
    getOperationTypeTag(type) {
      const map = {
        'LOGIN': 'success',
        'LOGOUT': 'info',
        'CREATE': 'primary',
        'UPDATE': 'warning',
        'DELETE': 'danger',
        'QUERY': 'info'
      }
      return map[type] || 'info'
    },
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        if (this.dateRange && this.dateRange.length === 2) {
          params.startTime = this.dateRange[0]
          params.endTime = this.dateRange[1]
        }
        const response = await getLogPage(params)
        if (response.code === 200) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    async loadDictData() {
      try {
        const [modulesRes, typesRes] = await Promise.all([
          getModules(),
          getOperationTypes()
        ])
        if (modulesRes.code === 200) {
          this.modules = modulesRes.data
        }
        if (typesRes.code === 200) {
          this.operationTypes = typesRes.data
        }
      } catch (error) {
        console.error(error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        username: '',
        module: '',
        operationType: '',
        status: null
      }
      this.dateRange = []
      this.pagination.current = 1
      this.fetchData()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.fetchData()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.fetchData()
    },
    handleView(row) {
      this.currentLog = row
      this.detailVisible = true
    }
  }
}
</script>
