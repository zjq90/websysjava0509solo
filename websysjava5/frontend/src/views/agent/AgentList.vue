<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">代理商管理</div>
    </div>

    <div class="search-bar">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="代理商名称">
          <el-input v-model="searchForm.agentName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="代理商编码">
          <el-input v-model="searchForm.agentCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="代理商级别">
          <el-select v-model="searchForm.level" placeholder="请选择级别" clearable>
            <el-option label="一级" :value="1" />
            <el-option label="二级" :value="2" />
            <el-option label="三级" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增代理商</el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="agentName" label="代理商名称" width="180" />
      <el-table-column prop="agentCode" label="代理商编码" width="180" />
      <el-table-column prop="level" label="级别" width="80">
        <template slot-scope="scope">
          <el-tag :type="getLevelTagType(scope.row.level)">
            {{ getLevelText(scope.row.level) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="parentId" label="父级ID" width="100" />
      <el-table-column prop="contactName" label="联系人" width="100" />
      <el-table-column prop="contactPhone" label="联系电话" width="130" />
      <el-table-column prop="contactEmail" label="联系邮箱" width="180" />
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag :class="scope.row.status === 1 ? 'status-tag status-enabled' : 'status-tag status-disabled'">
            {{ scope.row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="代理商名称" prop="agentName">
          <el-input v-model="formData.agentName" placeholder="请输入代理商名称" />
        </el-form-item>
        <el-form-item label="代理商编码" prop="agentCode">
          <el-input v-model="formData.agentCode" placeholder="请输入代理商编码" />
        </el-form-item>
        <el-form-item label="父级代理商" prop="parentId">
          <el-select v-model="formData.parentId" placeholder="请选择父级代理商（不选则为顶级）" clearable>
            <el-option v-for="agent in parentAgents" :key="agent.id" :label="agent.agentName" :value="agent.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="formData.contactName" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="formData.contactEmail" placeholder="请输入联系邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" type="textarea" :rows="2" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAgentPage, createAgent, updateAgent, deleteAgent, getTopLevelAgents } from '@/api/agent'
import { mapGetters } from 'vuex'

export default {
  name: 'AgentList',
  computed: {
    ...mapGetters(['userInfo']),
    dialogTitle() {
      return this.isEdit ? '编辑代理商' : '新增代理商'
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      parentAgents: [],
      searchForm: {
        agentName: '',
        agentCode: '',
        level: null,
        status: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      tableData: [],
      formData: {
        id: null,
        agentName: '',
        agentCode: '',
        parentId: null,
        contactName: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        status: 1
      },
      formRules: {
        agentName: [{ required: true, message: '请输入代理商名称', trigger: 'blur' }],
        agentCode: [{ required: true, message: '请输入代理商编码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
    this.loadParentAgents()
  },
  methods: {
    getLevelText(level) {
      const map = { 1: '一级', 2: '二级', 3: '三级', 4: '四级', 5: '五级' }
      return map[level] || level
    },
    getLevelTagType(level) {
      const types = ['', 'success', 'primary', 'warning', 'danger', 'info']
      return types[level] || 'info'
    },
    async fetchData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          current: this.pagination.current,
          size: this.pagination.size
        }
        const response = await getAgentPage(params)
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
    async loadParentAgents() {
      try {
        const response = await getTopLevelAgents()
        if (response.code === 200) {
          this.parentAgents = response.data
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
        agentName: '',
        agentCode: '',
        level: null,
        status: null
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
        agentName: '',
        agentCode: '',
        parentId: null,
        contactName: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确定要删除代理商【${row.agentName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteAgent(row.id)
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
              response = await updateAgent(this.formData)
            } else {
              response = await createAgent(this.formData)
            }
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.fetchData()
              this.loadParentAgents()
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
