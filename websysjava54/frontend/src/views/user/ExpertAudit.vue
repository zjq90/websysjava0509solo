<template>
  <div class="expert-audit">
    <h2>专家认证审核</h2>
    <el-table :data="userList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="expertCertificate" label="证书" width="150">
        <template slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.expertCertificate">已上传</el-tag>
          <el-tag size="mini" type="info" v-else>未上传</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="workProof" label="工作证明" width="150">
        <template slot-scope="scope">
          <el-tag size="mini" v-if="scope.row.workProof">已上传</el-tag>
          <el-tag size="mini" type="info" v-else>未上传</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="auditDialog(scope.row, 'APPROVED')">通过</el-button>
          <el-button size="mini" type="danger" @click="auditDialog(scope.row, 'REJECTED')">拒绝</el-button>
          <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="审核意见" :visible.sync="auditVisible" width="500px">
      <el-form :model="auditForm">
        <el-form-item label="审核备注">
          <el-input type="textarea" v-model="auditForm.remark" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog title="专家详情" :visible.sync="detailVisible" width="600px">
      <el-descriptions :column="1" border v-if="currentUser">
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentUser.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentUser.email }}</el-descriptions-item>
        <el-descriptions-item label="证书文件">{{ currentUser.expertCertificate }}</el-descriptions-item>
        <el-descriptions-item label="工作证明">{{ currentUser.workProof }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ExpertAudit',
  data() {
    return {
      userList: [],
      auditVisible: false,
      detailVisible: false,
      currentUser: null,
      auditForm: {
        id: null,
        status: '',
        remark: ''
      }
    }
  },
  mounted() {
    this.loadPendingAudit()
  },
  methods: {
    loadPendingAudit() {
      this.$http.get('/users/expert-audit/pending').then(res => {
        if (res.data.code === 200) {
          this.userList = res.data.data
        }
      })
    },
    auditDialog(row, status) {
      this.auditForm.id = row.id
      this.auditForm.status = status
      this.auditForm.remark = ''
      this.auditVisible = true
    },
    submitAudit() {
      this.$http.post(`/users/${this.auditForm.id}/expert-audit`, null, {
        params: {
          status: this.auditForm.status,
          remark: this.auditForm.remark
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('审核成功')
          this.auditVisible = false
          this.loadPendingAudit()
        } else {
          this.$message.error('审核失败')
        }
      })
    },
    viewDetail(row) {
      this.currentUser = row
      this.detailVisible = true
    }
  }
}
</script>

<style scoped>
.expert-audit {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
