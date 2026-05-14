<template>
  <div class="reimbursement-management">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center">
            <span>报销管理</span>
            <el-button type="primary" size="small" @click="showAddDialog = true">申请报销</el-button>
          </div>
          <el-table :data="reimbursements" style="width: 100%" v-loading="loading" border>
            <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
            <el-table-column prop="reimburseNo" label="报销编号" width="150" align="center"></el-table-column>
            <el-table-column prop="employee.name" label="申请人" width="120" align="center"></el-table-column>
            <el-table-column prop="title" label="标题" align="center"></el-table-column>
            <el-table-column prop="category" label="分类" width="120" align="center">
              <template slot-scope="scope">
                <el-tag size="small">{{ getCategoryName(scope.row.category) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="120" align="center">
              <template slot-scope="scope">
                <span style="color: #f56c6c; font-weight: bold">¥{{ scope.row.amount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template slot-scope="scope">
                <el-tag size="small" :type="getStatusType(scope.row.status)">
                  {{ getStatusName(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="approver.name" label="审批人" width="120" align="center">
              <template slot-scope="scope">
                {{ scope.row.approver ? scope.row.approver.name : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="申请时间" width="160" align="center">
              <template slot-scope="scope">
                {{ formatDate(scope.row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleView(scope.row)">查看详情</el-button>
                <el-button v-if="scope.row.status === 'PENDING'" size="mini" type="success" @click="handleApprove(scope.row)">审批</el-button>
                <el-button v-if="scope.row.status === 'PENDING'" size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button v-if="scope.row.status === 'PENDING'" size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 报销申请弹窗 -->
    <el-dialog title="申请报销" :visible.sync="showAddDialog" width="600px">
      <el-form ref="addForm" :model="addForm" label-width="100px" :rules="addRules">
        <el-form-item label="报销标题" prop="title">
          <el-input v-model="addForm.title" placeholder="请输入报销标题"></el-input>
        </el-form-item>
        <el-form-item label="报销分类" prop="category">
          <el-select v-model="addForm.category" placeholder="请选择报销分类">
            <el-option label="差旅费" value="TRAVEL"></el-option>
            <el-option label="交通费" value="TRANSPORT"></el-option>
            <el-option label="餐费" value="MEAL"></el-option>
            <el-option label="办公费" value="OFFICE"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报销金额" prop="amount">
          <el-input type="number" v-model="addForm.amount" placeholder="请输入报销金额"></el-input>
        </el-form-item>
        <el-form-item label="报销说明">
          <el-input type="textarea" v-model="addForm.description" placeholder="请输入报销说明" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="凭证上传">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :show-file-list="true"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            accept="image/*"
            :limit="1">
            <el-button size="small" type="primary">点击上传凭证</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReimbursement">提交申请</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="报销详情" :visible.sync="showDetailDialog" width="700px">
      <div v-if="detailData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="报销编号">{{ detailData.reimburseNo }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ detailData.employee ? detailData.employee.name : '-' }}</el-descriptions-item>
          <el-descriptions-item label="报销标题">{{ detailData.title }}</el-descriptions-item>
          <el-descriptions-item label="报销分类">{{ getCategoryName(detailData.category) }}</el-descriptions-item>
          <el-descriptions-item label="报销金额"><span style="color: #f56c6c; font-weight: bold">¥{{ detailData.amount }}</span></el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(detailData.status)">{{ getStatusName(detailData.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审批人">{{ detailData.approver ? detailData.approver.name : '-' }}</el-descriptions-item>
          <el-descriptions-item label="审批备注">{{ detailData.approveRemark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="报销说明" :span="2">{{ detailData.description || '-' }}</el-descriptions-item>
          <el-descriptions-item label="申请时间" :span="2">{{ formatDate(detailData.createTime) }}</el-descriptions-item>
        </el-descriptions>
        
        <!-- 凭证预览 -->
        <div v-if="detailData.voucherUrl" style="margin-top: 20px;">
          <p style="font-weight: bold; margin-bottom: 10px;">凭证图片：</p>
          <img :src="baseUrl + detailData.voucherUrl" style="max-width: 100%; max-height: 300px;" />
        </div>

        <!-- 审批记录 -->
        <div style="margin-top: 20px;">
          <p style="font-weight: bold; margin-bottom: 10px;">审批流程：</p>
          <el-timeline>
            <el-timeline-item
              v-for="(record, index) in approvalRecords"
              :key="index"
              :timestamp="formatDate(record.createTime)"
              :type="getRecordType(record.operationType)">
              <el-card>
                <p><strong>{{ getOperationTypeName(record.operationType) }}</strong></p>
                <p>操作人：{{ record.operator ? record.operator.name : '-' }}</p>
                <p>状态变更：{{ record.previousStatus ? getStatusName(record.previousStatus) : '无' }} → {{ getStatusName(record.currentStatus) }}</p>
                <p v-if="record.remark">备注：{{ record.remark }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>

    <!-- 审批弹窗 -->
    <el-dialog title="审批报销" :visible.sync="showApproveDialog" width="500px">
      <el-form ref="approveForm" :model="approveForm" label-width="100px">
        <el-form-item label="审批意见">
          <el-input type="textarea" v-model="approveForm.remark" placeholder="请输入审批意见" :rows="3"></el-input>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="approveForm.status">
            <el-radio label="APPROVED">批准</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showApproveDialog = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">确认审批</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ReimbursementManagement',
  data() {
    return {
      loading: false,
      reimbursements: [],
      showAddDialog: false,
      showDetailDialog: false,
      showApproveDialog: false,
      detailData: null,
      approvalRecords: [],
      currentReimbursement: null,
      baseUrl: '/api',
      uploadUrl: '/api/reimbursements/upload',
      addForm: {
        title: '',
        category: '',
        amount: '',
        description: '',
        voucherUrl: ''
      },
      approveForm: {
        status: 'APPROVED',
        remark: ''
      },
      addRules: {
        title: [{ required: true, message: '请输入报销标题', trigger: 'blur' }],
        category: [{ required: true, message: '请选择报销分类', trigger: 'change' }],
        amount: [{ required: true, message: '请输入报销金额', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchReimbursements()
  },
  methods: {
    async fetchReimbursements() {
      this.loading = true
      try {
        const res = await this.$http.get('/reimbursements')
        if (res.data.success) {
          this.reimbursements = res.data.data || []
        }
      } catch (error) {
        console.error('获取报销失败', error)
      } finally {
        this.loading = false
      }
    },
    getCategoryName(category) {
      const map = {
        'TRAVEL': '差旅费',
        'TRANSPORT': '交通费',
        'MEAL': '餐费',
        'OFFICE': '办公费',
        'OTHER': '其他'
      }
      return map[category] || category
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return map[status] || ''
    },
    getStatusName(status) {
      const map = {
        'PENDING': '待审批',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝'
      }
      return map[status] || status
    },
    getOperationTypeName(type) {
      const map = {
        'APPLY': '提交申请',
        'APPROVE': '审批通过',
        'REJECT': '审批拒绝',
        'REAPPLY': '重新申请'
      }
      return map[type] || type
    },
    getRecordType(type) {
      const map = {
        'APPLY': 'primary',
        'APPROVE': 'success',
        'REJECT': 'danger',
        'REAPPLY': 'warning'
      }
      return map[type] || 'info'
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      return dateStr.replace('T', ' ').substring(0, 19)
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        this.$message.error('只能上传图片格式')
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('图片大小不能超过2MB')
        return false
      }
      return true
    },
    handleUploadSuccess(response) {
      if (response.success) {
        this.addForm.voucherUrl = response.data.url
        this.$message.success('凭证上传成功')
      }
    },
    async submitReimbursement() {
      try {
        const params = {
          employeeId: 1,
          title: this.addForm.title,
          category: this.addForm.category,
          amount: this.addForm.amount
        }
        if (this.addForm.description) {
          params.description = this.addForm.description
        }
        if (this.addForm.voucherUrl) {
          params.voucherUrl = this.addForm.voucherUrl
        }
        
        const res = await this.$http.post('/reimbursements/apply', null, { params })
        
        if (res.data.success) {
          this.$message.success('报销申请提交成功')
          this.showAddDialog = false
          this.addForm = { title: '', category: '', amount: '', description: '', voucherUrl: '' }
          this.fetchReimbursements()
        }
      } catch (error) {
        console.error('提交报销失败', error)
        this.$message.error('提交报销失败')
      }
    },
    async handleView(row) {
      this.detailData = row
      await this.fetchApprovalRecords(row.id)
      this.showDetailDialog = true
    },
    async fetchApprovalRecords(id) {
      try {
        const res = await this.$http.get(`/reimbursements/${id}/records`)
        if (res.data.success) {
          this.approvalRecords = res.data.data || []
        }
      } catch (error) {
        console.error('获取审批记录失败', error)
      }
    },
    handleApprove(row) {
      this.currentReimbursement = row
      this.approveForm = { status: 'APPROVED', remark: '' }
      this.showApproveDialog = true
    },
    async submitApproval() {
      try {
        const res = await this.$http.put(`/reimbursements/${this.currentReimbursement.id}/approve`, null, {
          params: {
            approverId: 1,
            status: this.approveForm.status,
            remark: this.approveForm.remark
          }
        })
        
        if (res.data.success) {
          this.$message.success('审批成功')
          this.showApproveDialog = false
          this.fetchReimbursements()
        }
      } catch (error) {
        console.error('审批失败', error)
        this.$message.error('审批失败')
      }
    },
    handleEdit(row) {
      this.$message.info('编辑报销功能')
    },
    async handleDelete(row) {
      try {
        await this.$http.delete(`/reimbursements/${row.id}`)
        this.$message.success('删除成功')
        this.fetchReimbursements()
      } catch (error) {
        console.error('删除失败', error)
        this.$message.error('删除失败')
      }
    }
  }
}
</script>

<style scoped>
.upload-demo {
  margin-bottom: 10px;
}
</style>
