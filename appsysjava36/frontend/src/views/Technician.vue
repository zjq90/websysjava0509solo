<template>
  <div class="technician">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="float: left; font-size: 18px; font-weight: bold;">装维人员管理</span>
        <el-button style="float: right" type="primary" @click="showCreateDialog">新增人员</el-button>
      </div>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="workArea" label="工作区域" width="200"></el-table-column>
        <el-table-column prop="rating" label="评分" width="100">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900"></el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="currentLoad" label="当前负载" width="120">
          <template slot-scope="scope">
            {{ scope.row.currentLoad }} / {{ scope.row.maxLoad }}
          </template>
        </el-table-column>
        <el-table-column prop="totalOrders" label="累计工单" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'ONLINE'" type="success">在线</el-tag>
            <el-tag v-else type="info">离线</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="editTechnician(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteTechnician(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑人员' : '新增人员'" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="工作区域">
          <el-input v-model="form.workArea"></el-input>
        </el-form-item>
        <el-form-item label="最大负载">
          <el-input-number v-model="form.maxLoad" :min="1" :max="50"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option label="在线" value="ONLINE"></el-option>
            <el-option label="离线" value="OFFLINE"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTechnician">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Technician',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      isEdit: false,
      form: {
        id: null,
        name: '',
        phone: '',
        workArea: '',
        maxLoad: 10,
        status: 'ONLINE'
      }
    }
  },
  mounted() {
    this.loadTechnicians()
  },
  methods: {
    async loadTechnicians() {
      try {
        const res = await this.$http.get('/api/technicians')
        if (res.data && res.data.data) {
          this.tableData = res.data.data
        }
      } catch (e) {
        console.log('使用模拟数据')
      }
    },
    showCreateDialog() {
      this.isEdit = false
      this.form = {
        id: null,
        name: '',
        phone: '',
        workArea: '',
        maxLoad: 10,
        status: 'ONLINE'
      }
      this.dialogVisible = true
    },
    editTechnician(row) {
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    async saveTechnician() {
      try {
        if (this.isEdit) {
          await this.$http.put(`/api/technicians/${this.form.id}`, this.form)
        } else {
          await this.$http.post('/api/technicians', this.form)
        }
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadTechnicians()
      } catch (e) {
        this.$message.error('保存失败')
      }
    },
    async deleteTechnician(row) {
      this.$confirm('确认删除该人员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/technicians/${row.id}`)
          this.$message.success('删除成功')
          this.loadTechnicians()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.technician {
  padding: 0;
}
</style>
