<template>
  <div class="page-container">
    <div class="page-header">
      <span>系统配置</span>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增配置</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="配置ID" width="80"></el-table-column>
        <el-table-column prop="configKey" label="配置键" width="180"></el-table-column>
        <el-table-column prop="configName" label="配置名称" width="150"></el-table-column>
        <el-table-column prop="configValue" label="配置值"></el-table-column>
        <el-table-column prop="description" label="描述" width="200"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form ref="configForm" :model="configForm" label-width="80px">
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="configForm.configKey" placeholder="请输入配置键"></el-input>
        </el-form-item>
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="configForm.configName" placeholder="请输入配置名称"></el-input>
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input v-model="configForm.configValue" type="textarea" placeholder="请输入配置值"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="configForm.description" type="textarea" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="configForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ConfigManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增配置',
      configForm: {
        id: null,
        configKey: '',
        configName: '',
        configValue: '',
        description: '',
        status: 1
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      request({
        url: '/system/config/list',
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
    handleAdd() {
      this.dialogTitle = '新增配置'
      this.configForm = {
        id: null,
        configKey: '',
        configName: '',
        configValue: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑配置'
      this.configForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该配置吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/config/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      const method = this.configForm.id ? 'put' : 'post'
      request({
        url: '/system/config',
        method: method,
        data: this.configForm
      }).then(() => {
        this.$message.success(this.configForm.id ? '更新成功' : '新增成功')
        this.dialogVisible = false
        this.fetchData()
      })
    }
  }
}
</script>
