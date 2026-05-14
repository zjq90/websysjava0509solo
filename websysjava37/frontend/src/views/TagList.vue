<template>
  <div class="tag-list">
    <h2>标签管理</h2>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="24" style="text-align: right">
        <el-button type="primary" icon="el-icon-plus" @click="showDialog()">新增标签</el-button>
      </el-col>
    </el-row>

    <el-table :data="tags" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="标签名称" width="150"></el-table-column>
      <el-table-column prop="description" label="标签描述" show-overflow-tooltip></el-table-column>
      <el-table-column prop="autoTag" label="自动标签" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.autoTag ? 'success' : 'info'">{{ scope.row.autoTag ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteTag(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="标签描述">
          <el-input v-model="form.description" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="自动标签">
          <el-switch v-model="form.autoTag"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveTag">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TagList',
  data() {
    return {
      tags: [],
      dialogVisible: false,
      dialogTitle: '新增标签',
      isEdit: false,
      form: {
        name: '',
        description: '',
        autoTag: false
      },
      rules: {
        name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadTags()
  },
  methods: {
    loadTags() {
      this.$http.get('/tags').then(response => {
        this.tags = response.data
      }).catch(error => {
        console.error('加载标签数据失败:', error)
        this.$message.error('加载标签数据失败')
      })
    },
    showDialog(row = null) {
      this.isEdit = !!row
      this.dialogTitle = row ? '编辑标签' : '新增标签'
      if (row) {
        this.form = { ...row }
      } else {
        this.form = {
          name: '',
          description: '',
          autoTag: false
        }
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    saveTag() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            this.$http.put(`/tags/${this.form.id}`, this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadTags()
            })
          } else {
            this.$http.post('/tags', this.form).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.loadTags()
            })
          }
        }
      })
    },
    deleteTag(row) {
      this.$confirm('确定要删除该标签吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/tags/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.loadTags()
        })
      })
    }
  }
}
</script>

<style scoped>
.tag-list {
  padding: 20px;
}
</style>
