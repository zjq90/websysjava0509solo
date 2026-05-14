<template>
  <div class="interaction-list">
    <h2>互动记录管理</h2>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-select v-model="selectedCustomer" placeholder="选择客户" style="width: 100%" @change="filterByCustomer">
          <el-option label="全部客户" value="all"></el-option>
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id"></el-option>
        </el-select>
      </el-col>
      <el-col :span="18" style="text-align: right">
        <el-button type="primary" icon="el-icon-plus" @click="showDialog()">新增互动记录</el-button>
      </el-col>
    </el-row>

    <el-table :data="interactions" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="customerId" label="客户ID" width="100"></el-table-column>
      <el-table-column label="客户姓名" width="120">
        <template slot-scope="scope">
          {{ getCustomerName(scope.row.customerId) }}
        </template>
      </el-table-column>
      <el-table-column prop="content" label="沟通内容" show-overflow-tooltip></el-table-column>
      <el-table-column prop="followUpPerson" label="跟进人" width="120"></el-table-column>
      <el-table-column prop="nextFollowUpTime" label="下次跟进时间" width="180"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteInteraction(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="form.customerId" style="width: 100%">
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="沟通内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="跟进人">
          <el-input v-model="form.followUpPerson"></el-input>
        </el-form-item>
        <el-form-item label="下次跟进时间">
          <el-datetime-picker v-model="form.nextFollowUpTime" type="datetime" placeholder="选择时间" style="width: 100%"></el-datetime-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveInteraction">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'InteractionList',
  data() {
    return {
      interactions: [],
      customers: [],
      selectedCustomer: 'all',
      dialogVisible: false,
      dialogTitle: '新增互动记录',
      isEdit: false,
      form: {
        customerId: '',
        content: '',
        followUpPerson: '',
        nextFollowUpTime: ''
      },
      rules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        content: [{ required: true, message: '请输入沟通内容', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/customers').then(response => {
        this.customers = response.data
      })
      this.loadInteractions()
    },
    loadInteractions() {
      this.$http.get('/interactions').then(response => {
        this.interactions = response.data
      }).catch(error => {
        console.error('加载互动记录失败:', error)
        this.$message.error('加载互动记录失败')
      })
    },
    filterByCustomer() {
      if (this.selectedCustomer !== 'all') {
        this.$http.get(`/interactions/customer/${this.selectedCustomer}`).then(response => {
          this.interactions = response.data
        })
      } else {
        this.loadInteractions()
      }
    },
    showDialog(row = null) {
      this.isEdit = !!row
      this.dialogTitle = row ? '编辑互动记录' : '新增互动记录'
      if (row) {
        this.form = { ...row }
      } else {
        this.form = {
          customerId: '',
          content: '',
          followUpPerson: '',
          nextFollowUpTime: ''
        }
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    saveInteraction() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            this.$http.put(`/interactions/${this.form.id}`, this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadInteractions()
            })
          } else {
            this.$http.post('/interactions', this.form).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.loadInteractions()
            })
          }
        }
      })
    },
    deleteInteraction(row) {
      this.$confirm('确定要删除该互动记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/interactions/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.loadInteractions()
        })
      })
    },
    getCustomerName(customerId) {
      const customer = this.customers.find(c => c.id === customerId)
      return customer ? customer.name : '未知客户'
    }
  }
}
</script>

<style scoped>
.interaction-list {
  padding: 20px;
}
</style>
