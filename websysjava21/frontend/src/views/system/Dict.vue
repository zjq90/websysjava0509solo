<template>
  <div class="page-container">
    <div class="page-header">
      <span>字典管理</span>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增字典</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="字典ID" width="80"></el-table-column>
        <el-table-column prop="dictType" label="字典类型" width="150"></el-table-column>
        <el-table-column prop="dictCode" label="字典编码" width="120"></el-table-column>
        <el-table-column prop="dictName" label="字典名称" width="150"></el-table-column>
        <el-table-column prop="dictValue" label="字典值" width="120"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
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
      <el-form ref="dictForm" :model="dictForm" label-width="80px">
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="dictForm.dictType" placeholder="请输入字典类型"></el-input>
        </el-form-item>
        <el-form-item label="字典编码" prop="dictCode">
          <el-input v-model="dictForm.dictCode" placeholder="请输入字典编码"></el-input>
        </el-form-item>
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictForm.dictName" placeholder="请输入字典名称"></el-input>
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="dictForm.dictValue" placeholder="请输入字典值"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="dictForm.sortOrder" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictForm.remark" type="textarea" placeholder="请输入备注"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dictForm.status">
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
  name: 'DictManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增字典',
      dictForm: {
        id: null,
        dictType: '',
        dictCode: '',
        dictName: '',
        dictValue: '',
        sortOrder: 1,
        remark: '',
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
        url: '/system/dict/list',
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
      this.dialogTitle = '新增字典'
      this.dictForm = {
        id: null,
        dictType: '',
        dictCode: '',
        dictName: '',
        dictValue: '',
        sortOrder: 1,
        remark: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑字典'
      this.dictForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该字典吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/dict/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      const method = this.dictForm.id ? 'put' : 'post'
      request({
        url: '/system/dict',
        method: method,
        data: this.dictForm
      }).then(() => {
        this.$message.success(this.dictForm.id ? '更新成功' : '新增成功')
        this.dialogVisible = false
        this.fetchData()
      })
    }
  }
}
</script>
