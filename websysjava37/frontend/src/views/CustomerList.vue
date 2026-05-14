<template>
  <div class="customer-list">
    <h2>客户管理</h2>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-input v-model="searchKey" placeholder="输入客户姓名搜索" clearable @clear="loadCustomers">
          <el-button slot="append" icon="el-icon-search" @click="searchCustomers"></el-button>
        </el-input>
      </el-col>
      <el-col :span="6">
        <el-select v-model="filterLifecycle" placeholder="按生命周期筛选" clearable @change="filterByLifecycle" style="width: 100%">
          <el-option label="潜在客户" value="POTENTIAL"></el-option>
          <el-option label="意向客户" value="INTENTION"></el-option>
          <el-option label="已定单" value="ORDERED"></el-option>
          <el-option label="已拍摄" value="PHOTOGRAPHED"></el-option>
          <el-option label="已交付" value="DELIVERED"></el-option>
          <el-option label="沉睡客户" value="SLEEPING"></el-option>
        </el-select>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-button type="primary" icon="el-icon-plus" @click="showDialog()">新增客户</el-button>
      </el-col>
    </el-row>

    <el-table :data="customers" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="姓名" width="100"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
      <el-table-column prop="birthday" label="生日" width="120"></el-table-column>
      <el-table-column prop="photoType" label="拍摄类型" width="120">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.photoType">{{ getPhotoTypeName(scope.row.photoType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="lifecycle" label="生命周期" width="120">
        <template slot-scope="scope">
          <el-tag :type="getLifecycleType(scope.row.lifecycle)">{{ getLifecycleName(scope.row.lifecycle) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="familyMembers" label="家庭成员" show-overflow-tooltip></el-table-column>
      <el-table-column prop="preference" label="消费偏好" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="success" @click="viewInteractions(scope.row)">互动</el-button>
          <el-button size="mini" type="danger" @click="deleteCustomer(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker v-model="form.birthday" type="date" placeholder="选择生日" style="width: 100%" value-format="yyyy-MM-dd"></el-date-picker>
        </el-form-item>
        <el-form-item label="家庭成员">
          <el-input v-model="form.familyMembers" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="拍摄类型">
          <el-select v-model="form.photoType" style="width: 100%">
            <el-option label="婚纱摄影" value="WEDDING"></el-option>
            <el-option label="儿童摄影" value="CHILDREN"></el-option>
            <el-option label="孕照摄影" value="MATERNITY"></el-option>
            <el-option label="全家福" value="FAMILY"></el-option>
            <el-option label="个人写真" value="PERSONAL"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="消费偏好">
          <el-input v-model="form.preference" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="生命周期">
          <el-select v-model="form.lifecycle" style="width: 100%">
            <el-option label="潜在客户" value="POTENTIAL"></el-option>
            <el-option label="意向客户" value="INTENTION"></el-option>
            <el-option label="已定单" value="ORDERED"></el-option>
            <el-option label="已拍摄" value="PHOTOGRAPHED"></el-option>
            <el-option label="已交付" value="DELIVERED"></el-option>
            <el-option label="沉睡客户" value="SLEEPING"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveCustomer">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CustomerList',
  data() {
    return {
      customers: [],
      searchKey: '',
      filterLifecycle: '',
      dialogVisible: false,
      dialogTitle: '新增客户',
      isEdit: false,
      form: {
        name: '',
        phone: '',
        birthday: '',
        familyMembers: '',
        photoType: '',
        preference: '',
        lifecycle: 'POTENTIAL'
      },
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCustomers()
  },
  methods: {
    loadCustomers() {
      this.$http.get('/customers').then(response => {
        this.customers = response.data
      }).catch(error => {
        console.error('加载客户数据失败:', error)
        this.$message.error('加载客户数据失败')
      })
    },
    searchCustomers() {
      if (this.searchKey) {
        this.$http.get(`/customers/search?name=${this.searchKey}`).then(response => {
          this.customers = response.data
        })
      } else {
        this.loadCustomers()
      }
    },
    filterByLifecycle() {
      if (this.filterLifecycle) {
        this.$http.get(`/customers/lifecycle/${this.filterLifecycle}`).then(response => {
          this.customers = response.data
        })
      } else {
        this.loadCustomers()
      }
    },
    showDialog(row = null) {
      this.isEdit = !!row
      this.dialogTitle = row ? '编辑客户' : '新增客户'
      if (row) {
        this.form = { ...row }
      } else {
        this.form = {
          name: '',
          phone: '',
          birthday: '',
          familyMembers: '',
          photoType: '',
          preference: '',
          lifecycle: 'POTENTIAL'
        }
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    saveCustomer() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const submitData = { ...this.form }
          if (!submitData.birthday) delete submitData.birthday
          if (!submitData.familyMembers) delete submitData.familyMembers
          if (!submitData.photoType) delete submitData.photoType
          if (!submitData.preference) delete submitData.preference
          
          if (this.isEdit) {
            this.$http.put(`/customers/${this.form.id}`, submitData).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadCustomers()
            }).catch(error => {
              console.error('更新客户失败:', error)
              var msg = error.response && error.response.data ? error.response.data : error.message
              this.$message.error('更新客户失败: ' + msg)
            })
          } else {
            this.$http.post('/customers', submitData).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.loadCustomers()
            }).catch(error => {
              console.error('添加客户失败:', error)
              var msg = error.response && error.response.data ? error.response.data : error.message
              this.$message.error('添加客户失败: ' + msg)
            })
          }
        }
      })
    },
    deleteCustomer(row) {
      this.$confirm('确定要删除该客户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/customers/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.loadCustomers()
        })
      })
    },
    viewInteractions(row) {
      this.$router.push(`/interactions?customerId=${row.id}`)
    },
    getLifecycleName(lifecycle) {
      const names = {
        'POTENTIAL': '潜在客户',
        'INTENTION': '意向客户',
        'ORDERED': '已定单',
        'PHOTOGRAPHED': '已拍摄',
        'DELIVERED': '已交付',
        'SLEEPING': '沉睡客户'
      }
      return names[lifecycle] || lifecycle
    },
    getLifecycleType(lifecycle) {
      const types = {
        'POTENTIAL': 'info',
        'INTENTION': 'primary',
        'ORDERED': 'warning',
        'PHOTOGRAPHED': 'success',
        'DELIVERED': 'success',
        'SLEEPING': 'danger'
      }
      return types[lifecycle] || 'info'
    },
    getPhotoTypeName(type) {
      const names = {
        'WEDDING': '婚纱摄影',
        'CHILDREN': '儿童摄影',
        'MATERNITY': '孕照摄影',
        'FAMILY': '全家福',
        'PERSONAL': '个人写真',
        'OTHER': '其他'
      }
      return names[type] || type
    }
  }
}
</script>

<style scoped>
.customer-list {
  padding: 20px;
}
</style>
