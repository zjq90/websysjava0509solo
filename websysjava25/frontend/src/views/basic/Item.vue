<template>
  <div class="item-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>检验检查项目管理</span>
        <el-button style="float: right; margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleAdd">新增项目</el-button>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="items" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="itemCode" label="项目编号" width="120"></el-table-column>
        <el-table-column prop="itemName" label="项目名称" width="150"></el-table-column>
        <el-table-column prop="itemType" label="项目类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.itemType === 'LABORATORY'" type="success" size="small">检验</el-tag>
            <el-tag v-else type="warning" size="small">检查</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="referenceValue" label="参考值" width="200"></el-table-column>
        <el-table-column prop="unit" label="单位" width="100"></el-table-column>
        <el-table-column prop="price" label="价格(元)" width="100"></el-table-column>
        <el-table-column prop="estimatedTime" label="预计时长(分钟)" width="130"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'ACTIVE'" type="success" size="small">启用</el-tag>
            <el-tag v-else type="info" size="small">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="项目编号" required>
          <el-input v-model="form.itemCode" placeholder="请输入项目编号"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" required>
          <el-input v-model="form.itemName" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="项目类型" required>
          <el-select v-model="form.itemType" style="width: 100%">
            <el-option label="检验" value="LABORATORY"></el-option>
            <el-option label="检查" value="EXAMINATION"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="参考值">
          <el-input v-model="form.referenceValue" placeholder="请输入参考值"></el-input>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" placeholder="请输入单位"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :precision="2" :step="1" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="预计时长(分钟)">
          <el-input-number v-model="form.estimatedTime" :step="1" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" placeholder="请输入描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ItemList',
  data() {
    return {
      items: [],
      dialogVisible: false,
      dialogTitle: '新增项目',
      isEdit: false,
      form: {
        itemCode: '',
        itemName: '',
        itemType: 'LABORATORY',
        referenceValue: '',
        unit: '',
        price: 0,
        estimatedTime: 30,
        description: '',
        status: 'ACTIVE'
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/items').then(response => {
        this.items = response.data
      }).catch(error => {
        console.error('加载项目失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增项目'
      this.form = {
        itemCode: '',
        itemName: '',
        itemType: 'LABORATORY',
        referenceValue: '',
        unit: '',
        price: 0,
        estimatedTime: 30,
        description: '',
        status: 'ACTIVE'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑项目'
      this.form = { ...row }
      this.dialogVisible = true
    },
    saveData() {
      if (this.isEdit) {
        this.$http.put(`/api/items/${this.form.id}`, this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('更新项目失败:', error)
          this.$message.error('操作失败')
        })
      } else {
        this.$http.post('/api/items', this.form).then(() => {
          this.$message.success('新增成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('新增项目失败:', error)
          this.$message.error('操作失败')
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确认删除该项目?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/items/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(error => {
          console.error('删除项目失败:', error)
          this.$message.error('操作失败')
        })
      })
    }
  }
}
</script>

<style scoped>
.item-list {
  padding: 0;
}
</style>
