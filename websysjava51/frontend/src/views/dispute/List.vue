<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">纠纷列表</h2>
    </div>
    <div class="table-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="纠纷标题">
          <el-input v-model="searchForm.title" placeholder="请输入纠纷标题" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" style="width: 150px;" clearable>
            <el-option label="商品问题" value="GOODS_ISSUE"></el-option>
            <el-option label="支付问题" value="PAYMENT_ISSUE"></el-option>
            <el-option label="物流问题" value="SHIPPING_ISSUE"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px;" clearable>
            <el-option label="待处理" value="PENDING"></el-option>
            <el-option label="处理中" value="PROCESSING"></el-option>
            <el-option label="已解决" value="RESOLVED"></el-option>
            <el-option label="已关闭" value="CLOSED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="180"></el-table-column>
        <el-table-column prop="title" label="纠纷标题" min-width="180"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            {{ getTypeText(scope.row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complainantName" label="投诉人" width="100"></el-table-column>
        <el-table-column prop="respondentName" label="被投诉人" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" icon="el-icon-upload2" @click="handleUploadEvidence(scope.row)">上传证据</el-button>
            <el-button size="mini" type="warning" icon="el-icon-edit" @click="handleArbitration(scope.row)">仲裁结果</el-button>
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
    <el-dialog title="纠纷详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="订单编号">{{ currentDispute.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="纠纷标题">{{ currentDispute.title }}</el-descriptions-item>
        <el-descriptions-item label="纠纷类型">{{ getTypeText(currentDispute.type) }}</el-descriptions-item>
        <el-descriptions-item label="纠纷状态">
          <el-tag :type="getStatusType(currentDispute.status)" size="small">
            {{ getStatusText(currentDispute.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="纠纷描述">
          <div style="white-space: pre-wrap;">{{ currentDispute.description }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="聊天记录">
          <div style="white-space: pre-wrap;">{{ currentDispute.chatHistory || '暂无' }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    <el-dialog title="上传证据" :visible.sync="evidenceDialogVisible" width="500px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :auto-upload="false"
        :limit="1"
        :on-change="handleFileChange"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传图片文件，且不超过5MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="evidenceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEvidence">上传</el-button>
      </div>
    </el-dialog>
    <el-dialog title="录入仲裁结果" :visible.sync="arbitrationDialogVisible" width="600px">
      <el-form label-width="100px">
        <el-form-item label="仲裁人">
          <el-input v-model="arbitrationForm.arbitratorName"></el-input>
        </el-form-item>
        <el-form-item label="仲裁结果">
          <el-input
            v-model="arbitrationForm.result"
            type="textarea"
            :rows="4"
            placeholder="请输入仲裁结果"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="arbitrationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveArbitration">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DisputeList',
  data() {
    return {
      searchForm: {
        title: '',
        type: '',
        status: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      detailDialogVisible: false,
      evidenceDialogVisible: false,
      arbitrationDialogVisible: false,
      currentDispute: {},
      arbitrationForm: {
        arbitratorName: '',
        result: ''
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const mockData = []
      const types = ['GOODS_ISSUE', 'PAYMENT_ISSUE', 'SHIPPING_ISSUE', 'OTHER']
      const statuses = ['PENDING', 'PROCESSING', 'RESOLVED', 'CLOSED']
      for (let i = 1; i <= 8; i++) {
        mockData.push({
          id: i,
          orderNo: 'ORD' + Date.now() + i,
          title: '纠纷标题测试' + i,
          type: types[i % types.length],
          status: statuses[i % statuses.length],
          description: '这是纠纷描述内容' + i + '，用户反馈购买的商品存在质量问题，要求退货退款。',
          chatHistory: i % 2 === 0 ? '买家：商品有问题\n卖家：请提供照片\n买家：[图片]' : '',
          complainantId: 1,
          complainantName: '测试买家',
          respondentId: 2,
          respondentName: '测试卖家',
          createTime: '2024-01-' + String(i).padStart(2, '0') + ' 10:00:00'
        })
      }
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    getTypeText(type) {
      const map = {
        'GOODS_ISSUE': '商品问题',
        'PAYMENT_ISSUE': '支付问题',
        'SHIPPING_ISSUE': '物流问题',
        'OTHER': '其他'
      }
      return map[type] || type
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'RESOLVED': 'success',
        'CLOSED': 'info'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待处理',
        'PROCESSING': '处理中',
        'RESOLVED': '已解决',
        'CLOSED': '已关闭'
      }
      return map[status] || status
    },
    handleSearch() {
      this.$message.success('搜索成功')
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        title: '',
        type: '',
        status: ''
      }
      this.fetchData()
    },
    handleDetail(row) {
      this.currentDispute = { ...row }
      this.detailDialogVisible = true
    },
    handleUploadEvidence(row) {
      this.currentDispute = row
      this.evidenceDialogVisible = true
    },
    handleFileChange(file) {
      console.log('文件选择:', file)
    },
    saveEvidence() {
      this.$message.success('证据上传成功')
      this.evidenceDialogVisible = false
    },
    handleArbitration(row) {
      this.currentDispute = row
      this.arbitrationForm.arbitratorName = ''
      this.arbitrationForm.result = ''
      this.arbitrationDialogVisible = true
    },
    saveArbitration() {
      this.$message.success('仲裁结果保存成功')
      this.arbitrationDialogVisible = false
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