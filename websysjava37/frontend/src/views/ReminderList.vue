<template>
  <div class="reminder-list">
    <h2>提醒管理</h2>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-select v-model="filterType" placeholder="按类型筛选" clearable @change="filterByType" style="width: 100%">
          <el-option label="拍摄前提醒" value="SHOOTING_BEFORE"></el-option>
          <el-option label="取件后回访" value="PICKUP_AFTER"></el-option>
          <el-option label="纪念日提醒" value="ANNIVERSARY"></el-option>
          <el-option label="跟进提醒" value="FOLLOW_UP"></el-option>
          <el-option label="营销提醒" value="MARKETING"></el-option>
          <el-option label="其他提醒" value="OTHER"></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select v-model="filterProcessed" placeholder="按状态筛选" clearable @change="loadReminders" style="width: 100%">
          <el-option label="待处理" :value="false"></el-option>
          <el-option label="已处理" :value="true"></el-option>
        </el-select>
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-button type="warning" @click="loadPendingReminders">查看待处理</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="showDialog()">新增提醒</el-button>
      </el-col>
    </el-row>

    <el-table :data="reminders" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="customerId" label="客户ID" width="100"></el-table-column>
      <el-table-column label="客户姓名" width="120">
        <template slot-scope="scope">
          {{ getCustomerName(scope.row.customerId) }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="提醒标题" width="150"></el-table-column>
      <el-table-column prop="content" label="提醒内容" show-overflow-tooltip></el-table-column>
      <el-table-column prop="type" label="提醒类型" width="120">
        <template slot-scope="scope">
          <el-tag :type="getReminderTypeTag(scope.row.type)">{{ getReminderTypeName(scope.row.type) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reminderTime" label="提醒时间" width="180"></el-table-column>
      <el-table-column prop="processed" label="处理状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.processed ? 'success' : 'warning'">{{ scope.row.processed ? '已处理' : '待处理' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showDialog(scope.row)">编辑</el-button>
          <el-button size="mini" type="success" v-if="!scope.row.processed" @click="markAsProcessed(scope.row)">标记处理</el-button>
          <el-button size="mini" type="danger" @click="deleteReminder(scope.row)">删除</el-button>
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
        <el-form-item label="提醒标题" prop="title">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="提醒内容">
          <el-input v-model="form.content" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="提醒类型">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="拍摄前提醒" value="SHOOTING_BEFORE"></el-option>
            <el-option label="取件后回访" value="PICKUP_AFTER"></el-option>
            <el-option label="纪念日提醒" value="ANNIVERSARY"></el-option>
            <el-option label="跟进提醒" value="FOLLOW_UP"></el-option>
            <el-option label="营销提醒" value="MARKETING"></el-option>
            <el-option label="其他提醒" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提醒时间" prop="reminderTime">
          <el-datetime-picker v-model="form.reminderTime" type="datetime" placeholder="选择提醒时间" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss"></el-datetime-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveReminder">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ReminderList',
  data() {
    return {
      reminders: [],
      customers: [],
      filterType: '',
      filterProcessed: '',
      dialogVisible: false,
      dialogTitle: '新增提醒',
      isEdit: false,
      form: {
        customerId: '',
        title: '',
        content: '',
        type: 'OTHER',
        reminderTime: '',
        processed: false
      },
      rules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        title: [{ required: true, message: '请输入提醒标题', trigger: 'blur' }],
        reminderTime: [{ required: true, message: '请选择提醒时间', trigger: 'change' }]
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
      this.loadReminders()
    },
    loadReminders() {
      this.$http.get('/reminders').then(response => {
        let data = response.data
        if (this.filterType) {
          data = data.filter(r => r.type === this.filterType)
        }
        if (this.filterProcessed !== '') {
          data = data.filter(r => r.processed === this.filterProcessed)
        }
        this.reminders = data
      }).catch(error => {
        console.error('加载提醒数据失败:', error)
        this.$message.error('加载提醒数据失败')
      })
    },
    loadPendingReminders() {
      this.$http.get('/reminders/pending').then(response => {
        this.reminders = response.data
        this.filterType = ''
        this.filterProcessed = ''
      })
    },
    filterByType() {
      this.loadReminders()
    },
    showDialog(row = null) {
      this.isEdit = !!row
      this.dialogTitle = row ? '编辑提醒' : '新增提醒'
      if (row) {
        this.form = { ...row }
      } else {
        this.form = {
          customerId: '',
          title: '',
          content: '',
          type: 'OTHER',
          reminderTime: '',
          processed: false
        }
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    saveReminder() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const submitData = { ...this.form }
          if (!submitData.content) delete submitData.content
          
          if (this.isEdit) {
            this.$http.put(`/reminders/${this.form.id}`, submitData).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadReminders()
            }).catch(error => {
              console.error('更新提醒失败:', error)
              var msg = error.response && error.response.data ? error.response.data : error.message
              this.$message.error('更新提醒失败: ' + msg)
            })
          } else {
            this.$http.post('/reminders', submitData).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.loadReminders()
            }).catch(error => {
              console.error('添加提醒失败:', error)
              var msg = error.response && error.response.data ? error.response.data : error.message
              this.$message.error('添加提醒失败: ' + msg)
            })
          }
        }
      })
    },
    markAsProcessed(row) {
      this.$http.patch(`/reminders/${row.id}/processed`).then(() => {
        this.$message.success('标记成功')
        this.loadReminders()
      })
    },
    deleteReminder(row) {
      this.$confirm('确定要删除该提醒吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/reminders/${row.id}`).then(() => {
          this.$message.success('删除成功')
          this.loadReminders()
        })
      })
    },
    getCustomerName(customerId) {
      const customer = this.customers.find(c => c.id === customerId)
      return customer ? customer.name : '未知客户'
    },
    getReminderTypeName(type) {
      const names = {
        'SHOOTING_BEFORE': '拍摄前提醒',
        'PICKUP_AFTER': '取件后回访',
        'ANNIVERSARY': '纪念日提醒',
        'FOLLOW_UP': '跟进提醒',
        'MARKETING': '营销提醒',
        'OTHER': '其他提醒'
      }
      return names[type] || type
    },
    getReminderTypeTag(type) {
      const types = {
        'SHOOTING_BEFORE': 'warning',
        'PICKUP_AFTER': 'info',
        'ANNIVERSARY': 'success',
        'FOLLOW_UP': 'primary',
        'MARKETING': 'danger',
        'OTHER': 'default'
      }
      return types[type] || 'default'
    }
  }
}
</script>

<style scoped>
.reminder-list {
  padding: 20px;
}
</style>
