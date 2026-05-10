<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">系统配置</div>
    </div>

    <div class="search-bar">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="配置分组">
          <el-select v-model="searchForm.configGroup" placeholder="请选择分组" clearable>
            <el-option v-for="group in configGroups" :key="group" :label="getGroupText(group)" :value="group" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置键名">
          <el-input v-model="searchForm.configKey" placeholder="请输入键名" clearable />
        </el-form-item>
        <el-form-item label="配置名称">
          <el-input v-model="searchForm.configName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select v-model="searchForm.enabled" placeholder="请选择" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-toolbar">
      <div>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增配置</el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="configGroup" label="分组" width="120">
        <template slot-scope="scope">
          <el-tag :type="getGroupTagType(scope.row.configGroup)">
            {{ getGroupText(scope.row.configGroup) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="configKey" label="配置键名" width="200" />
      <el-table-column prop="configName" label="配置名称" width="180" />
      <el-table-column prop="configValue" label="配置值" min-width="200" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-tag v-if="scope.row.configType === 'BOOLEAN'" :type="scope.row.configValue === 'true' ? 'success' : 'info'">
            {{ scope.row.configValue === 'true' ? '是' : '否' }}
          </el-tag>
          <span v-else>{{ scope.row.configValue || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="configType" label="类型" width="100">
        <template slot-scope="scope">
          <el-tag size="small">{{ getTypeText(scope.row.configType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="enabled" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag :class="scope.row.enabled === 1 ? 'status-tag status-enabled' : 'status-tag status-disabled'">
            {{ scope.row.enabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isSystem" label="系统内置" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isSystem === 1" type="warning">是</el-tag>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleView(scope.row)">详情</el-button>
          <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            type="text"
            size="small"
            :disabled="scope.row.isSystem === 1"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="配置分组" prop="configGroup">
          <el-select v-model="formData.configGroup" placeholder="请选择分组">
            <el-option v-for="group in configGroups" :key="group" :label="getGroupText(group)" :value="group" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置键名" prop="configKey">
          <el-input v-model="formData.configKey" placeholder="请输入配置键名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="formData.configName" placeholder="请输入配置名称" />
        </el-form-item>
        <el-form-item label="配置类型" prop="configType">
          <el-select v-model="formData.configType" placeholder="请选择类型" @change="handleTypeChange">
            <el-option v-for="type in configTypes" :key="type" :label="getTypeText(type)" :value="type" />
          </el-select>
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input
            v-if="formData.configType !== 'BOOLEAN'"
            v-model="formData.configValue"
            :type="formData.configType === 'NUMBER' ? 'number' : 'textarea'"
            :rows="formData.configType === 'JSON' ? 4 : 1"
            placeholder="请输入配置值"
          />
          <el-switch v-else v-model="booleanValue" active-text="是" inactive-text="否" />
        </el-form-item>
        <el-form-item label="配置描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-radio-group v-model="formData.enabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="配置详情" :visible.sync="detailVisible" width="600px">
      <el-descriptions :column="2" border v-if="currentConfig">
        <el-descriptions-item label="配置ID">{{ currentConfig.id }}</el-descriptions-item>
        <el-descriptions-item label="配置分组">{{ getGroupText(currentConfig.configGroup) }}</el-descriptions-item>
        <el-descriptions-item label="配置键名" :span="2">{{ currentConfig.configKey }}</el-descriptions-item>
        <el-descriptions-item label="配置名称" :span="2">{{ currentConfig.configName }}</el-descriptions-item>
        <el-descriptions-item label="配置类型">{{ getTypeText(currentConfig.configType) }}</el-descriptions-item>
        <el-descriptions-item label="排序号">{{ currentConfig.sortOrder }}</el-descriptions-item>
        <el-descriptions-item label="配置值" :span="2">
          <pre style="background: #f5f7fa; padding: 10px; border-radius: 4px; margin: 0; white-space: pre-wrap;">{{ currentConfig.configValue || '-' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="配置描述" :span="2">{{ currentConfig.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="是否启用">
          <el-tag :class="currentConfig.enabled === 1 ? 'status-tag status-enabled' : 'status-tag status-disabled'">
            {{ currentConfig.enabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="系统内置">
          <el-tag v-if="currentConfig.isSystem === 1" type="warning">是</el-tag>
          <span v-else>否</span>
        </el-descriptions-item>
        <el-descriptions-item label="更新人">{{ currentConfig.updateBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentConfig.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import {
  getConfigPage,
  createConfig,
  updateConfig,
  deleteConfig,
  getConfigGroups,
  getConfigTypes
} from '@/api/config'

export default {
  name: 'ConfigList',
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑配置' : '新增配置'
    },
    booleanValue: {
      get() {
        return this.formData.configValue === 'true'
      },
      set(val) {
        this.formData.configValue = val ? 'true' : 'false'
      }
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      detailVisible: false,
      isEdit: false,
      currentConfig: null,
      configGroups: [],
      configTypes: [],
      searchForm: {
        configGroup: '',
        configKey: '',
        configName: '',
        enabled: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      tableData: [],
      formData: {
        id: null,
        configGroup: 'SYSTEM',
        configKey: '',
        configName: '',
        configValue: '',
        configType: 'STRING',
        description: '',
        enabled: 1,
        isSystem: 0,
        sortOrder: 0
      },
      formRules: {
        configGroup: [{ required: true, message: '请选择配置分组', trigger: 'change' }],
        configKey: [{ required: true, message: '请输入配置键名', trigger: 'blur' }],
        configName: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
        configType: [{ required: true, message: '请选择配置类型', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
    this.loadDictData()
  },
  methods: {
    getGroupText(group) {
      const map = {
        'SYSTEM': '系统配置',
        'PAYMENT': '支付配置',
        'SMS': '短信配置',
        'OTHER': '其他配置'
      }
      return map[group] || group
    },
    getGroupTagType(group) {
      const map = {
        'SYSTEM': 'primary',
        'PAYMENT': 'success',
        'SMS': 'warning',
        'OTHER': 'info'
      }
      return map[group] || 'info'
    },
    getTypeText(type) {
      const map = {
        'STRING': '字符串',
        'NUMBER': '数字',
        'BOOLEAN': '布尔值',
        'JSON': 'JSON'
      }
      return map[type] || type
    },
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        const response = await getConfigPage(params)
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
        const [groupsRes, typesRes] = await Promise.all([
          getConfigGroups(),
          getConfigTypes()
        ])
        if (groupsRes.code === 200) {
          this.configGroups = groupsRes.data
        }
        if (typesRes.code === 200) {
          this.configTypes = typesRes.data
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
        configGroup: '',
        configKey: '',
        configName: '',
        enabled: null
      }
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
    handleAdd() {
      this.isEdit = false
      this.formData = {
        id: null,
        configGroup: 'SYSTEM',
        configKey: '',
        configName: '',
        configValue: '',
        configType: 'STRING',
        description: '',
        enabled: 1,
        isSystem: 0,
        sortOrder: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.currentConfig = row
      this.detailVisible = true
    },
    handleTypeChange() {
      if (this.formData.configType === 'BOOLEAN') {
        this.formData.configValue = 'false'
      } else if (this.formData.configType === 'JSON') {
        this.formData.configValue = '{}'
      } else if (this.formData.configType === 'NUMBER') {
        this.formData.configValue = '0'
      } else {
        this.formData.configValue = ''
      }
    },
    handleDelete(row) {
      this.$confirm(`确定要删除配置【${row.configName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteConfig(row.id)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error(error)
        }
      }).catch(() => {})
    },
    async handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.submitLoading = true
          try {
            let response
            if (this.isEdit) {
              response = await updateConfig(this.formData)
            } else {
              response = await createConfig(this.formData)
            }
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          } catch (error) {
            console.error(error)
          } finally {
            this.submitLoading = false
          }
        }
      })
    }
  }
}
</script>
