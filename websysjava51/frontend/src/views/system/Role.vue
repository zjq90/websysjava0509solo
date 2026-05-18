<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">角色管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
    </div>
    <div class="table-container">
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="code" label="角色编码" width="150"></el-table-column>
        <el-table-column prop="description" label="角色描述" min-width="200"></el-table-column>
        <el-table-column label="权限配置" width="400">
          <template slot-scope="scope">
            <el-checkbox-group v-model="scope.row.permissions">
              <el-checkbox label="product">商品管理</el-checkbox>
              <el-checkbox label="order">订单管理</el-checkbox>
              <el-checkbox label="dispute">纠纷管理</el-checkbox>
              <el-checkbox label="statistics">数据统计</el-checkbox>
              <el-checkbox label="system">系统管理</el-checkbox>
            </el-checkbox-group>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="权限配置">
          <el-checkbox-group v-model="form.permissions">
            <el-checkbox label="product">商品管理</el-checkbox>
            <el-checkbox label="order">订单管理</el-checkbox>
            <el-checkbox label="dispute">纠纷管理</el-checkbox>
            <el-checkbox label="statistics">数据统计</el-checkbox>
            <el-checkbox label="system">系统管理</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'RoleManagement',
  data() {
    return {
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        code: '',
        description: '',
        permissions: []
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const mockData = [
        {
          id: 1,
          name: '管理员',
          code: 'ADMIN',
          description: '系统管理员，拥有所有权限',
          permissions: ['product', 'order', 'dispute', 'statistics', 'system'],
          createTime: '2024-01-01 10:00:00'
        },
        {
          id: 2,
          name: '卖家',
          code: 'SELLER',
          description: '商品卖家，管理商品和订单',
          permissions: ['product', 'order', 'dispute', 'statistics'],
          createTime: '2024-01-01 10:00:00'
        },
        {
          id: 3,
          name: '客服',
          code: 'CUSTOMER_SERVICE',
          description: '客服人员，处理纠纷和订单问题',
          permissions: ['order', 'dispute', 'statistics'],
          createTime: '2024-01-01 10:00:00'
        },
        {
          id: 4,
          name: '买家',
          code: 'BUYER',
          description: '普通买家，购买商品',
          permissions: [],
          createTime: '2024-01-01 10:00:00'
        }
      ]
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.form = {
        id: null,
        name: '',
        code: '',
        description: '',
        permissions: []
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确定要删除角色"${row.name}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSave() {
      this.$message.success('保存成功')
      this.dialogVisible = false
      this.fetchData()
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