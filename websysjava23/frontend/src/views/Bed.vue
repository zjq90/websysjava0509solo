<template>
  <div>
    <h2>🛏️ 床位管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="searchWard" placeholder="按病区筛选" style="width: 200px" clearable @change="loadData">
          <el-option label="内科一病区" value="内科一病区"></el-option>
          <el-option label="内科二病区" value="内科二病区"></el-option>
          <el-option label="外科一病区" value="外科一病区"></el-option>
          <el-option label="外科二病区" value="外科二病区"></el-option>
          <el-option label="儿科病区" value="儿科病区"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">新增床位</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="bedNo" label="床位号" width="120"></el-table-column>
        <el-table-column prop="wardName" label="病区" width="120"></el-table-column>
        <el-table-column prop="roomNo" label="房间号" width="100"></el-table-column>
        <el-table-column prop="bedType" label="类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="scope.row.bedType === '重症监护床' ? 'danger' : 'info'" size="mini">{{ scope.row.bedType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="科室" width="100"></el-table-column>
        <el-table-column prop="patientName" label="当前患者" width="120"></el-table-column>
        <el-table-column prop="dailyRate" label="每日费用" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getBedStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="床位号">
          <el-input v-model="form.bedNo"></el-input>
        </el-form-item>
        <el-form-item label="病区">
          <el-select v-model="form.wardName" style="width: 100%">
            <el-option label="内科一病区" value="内科一病区"></el-option>
            <el-option label="内科二病区" value="内科二病区"></el-option>
            <el-option label="外科一病区" value="外科一病区"></el-option>
            <el-option label="外科二病区" value="外科二病区"></el-option>
            <el-option label="儿科病区" value="儿科病区"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="form.roomNo"></el-input>
        </el-form-item>
        <el-form-item label="床位类型">
          <el-select v-model="form.bedType" style="width: 100%">
            <el-option label="普通床" value="普通床"></el-option>
            <el-option label="重症监护床" value="重症监护床"></el-option>
            <el-option label="隔离床" value="隔离床"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属科室">
          <el-select v-model="form.department" style="width: 100%">
            <el-option label="内科" value="内科"></el-option>
            <el-option label="外科" value="外科"></el-option>
            <el-option label="儿科" value="儿科"></el-option>
            <el-option label="妇产科" value="妇产科"></el-option>
            <el-option label="骨科" value="骨科"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="每日费用">
          <el-input-number v-model="form.dailyRate" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="空闲" value="空闲"></el-option>
            <el-option label="占用" value="占用"></el-option>
            <el-option label="维修中" value="维修中"></el-option>
            <el-option label="预留" value="预留"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Bed',
  data() {
    return {
      searchWard: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增床位',
      form: {
        bedType: '普通床',
        status: '空闲',
        dailyRate: 50
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      let url = '/bed'
      if (this.searchWard) {
        url = `/bed/ward/${this.searchWard}`
      }
      this.$http.get(url).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    openDialog() {
      this.dialogTitle = '新增床位'
      this.form = {
        bedType: '普通床',
        status: '空闲',
        dailyRate: 50
      }
      this.dialogVisible = true
    },
    edit(row) {
      this.dialogTitle = '编辑床位'
      this.form = Object.assign({}, row)
      this.dialogVisible = true
    },
    save() {
      if (!this.form.bedNo) {
        this.$message.warning('请输入床位号')
        return
      }
      
      const method = this.form.id ? 'put' : 'post'
      this.$http[method]('/bed', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    remove(id) {
      this.$confirm('确定要删除该床位吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/bed/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          }
        })
      })
    },
    getBedStatusType(status) {
      const types = {
        '空闲': 'success',
        '占用': 'primary',
        '维修中': 'warning',
        '预留': 'info'
      }
      return types[status] || 'info'
    }
  }
}
</script>
