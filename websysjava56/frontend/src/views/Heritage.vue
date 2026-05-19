<template>
  <div class="heritage">
    <el-card>
      <div slot="header" class="card-header">
        <span>文物管理</span>
        <el-button style="float: right; padding: 3px 0" type="primary" @click="showAddDialog">
          <i class="el-icon-plus"></i> 添加文物
        </el-button>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="文物名称">
          <el-input v-model="searchForm.name" placeholder="请输入文物名称"></el-input>
        </el-form-item>
        <el-form-item label="朝代">
          <el-select v-model="searchForm.dynasty" placeholder="请选择朝代">
            <el-option label="明代" value="明代"></el-option>
            <el-option label="清代" value="清代"></el-option>
            <el-option label="宋代" value="宋代"></el-option>
            <el-option label="唐代" value="唐代"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="heritageCode" label="文物编号" width="120"></el-table-column>
        <el-table-column prop="name" label="文物名称" width="150"></el-table-column>
        <el-table-column prop="category" label="类别" width="100"></el-table-column>
        <el-table-column prop="dynasty" label="朝代" width="100"></el-table-column>
        <el-table-column prop="origin" label="来源地" width="120"></el-table-column>
        <el-table-column prop="level" label="级别" width="100"></el-table-column>
        <el-table-column prop="qualityScore" label="质量评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getQualityTagType(scope.row.qualityScore)">{{ scope.row.qualityScore }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isMasterData" label="主数据" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isMasterData ? 'success' : 'info'">
              {{ scope.row.isMasterData ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="editHeritage(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="convertToMaster(scope.row)" v-if="!scope.row.isMasterData">转主数据</el-button>
            <el-button size="mini" type="danger" @click="deleteHeritage(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="heritageForm" label-width="100px">
        <el-form-item label="文物名称">
          <el-input v-model="heritageForm.name"></el-input>
        </el-form-item>
        <el-form-item label="类别">
          <el-input v-model="heritageForm.category"></el-input>
        </el-form-item>
        <el-form-item label="朝代">
          <el-input v-model="heritageForm.dynasty"></el-input>
        </el-form-item>
        <el-form-item label="材质">
          <el-input v-model="heritageForm.material"></el-input>
        </el-form-item>
        <el-form-item label="来源地">
          <el-input v-model="heritageForm.origin"></el-input>
        </el-form-item>
        <el-form-item label="级别">
          <el-input v-model="heritageForm.level"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="heritageForm.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveHeritage">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="文物详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="currentHeritage">
        <el-descriptions-item label="文物编号">{{ currentHeritage.heritageCode }}</el-descriptions-item>
        <el-descriptions-item label="文物名称">{{ currentHeritage.name }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ currentHeritage.category }}</el-descriptions-item>
        <el-descriptions-item label="朝代">{{ currentHeritage.dynasty }}</el-descriptions-item>
        <el-descriptions-item label="材质">{{ currentHeritage.material }}</el-descriptions-item>
        <el-descriptions-item label="级别">{{ currentHeritage.level }}</el-descriptions-item>
        <el-descriptions-item label="来源地">{{ currentHeritage.origin }}</el-descriptions-item>
        <el-descriptions-item label="质量评分">{{ currentHeritage.qualityScore }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentHeritage.description }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="currentHeritage && currentHeritage.estimatedValue" style="margin-top: 20px; padding: 15px; background: #f0f9eb; border-radius: 5px;">
        <h4 style="color: #67C23A; margin-bottom: 10px;">价值评估</h4>
        <p>评估价值: ¥ {{ currentHeritage.estimatedValue }}</p>
        <p>专家规则: 已应用专业估值模型</p>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="evaluateValue" v-if="currentHeritage">评估价值</el-button>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Heritage',
  data() {
    return {
      searchForm: {
        name: '',
        dynasty: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      detailDialogVisible: false,
      dialogTitle: '添加文物',
      isEdit: false,
      currentHeritage: null,
      heritageForm: {
        name: '',
        category: '',
        dynasty: '',
        material: '',
        origin: '',
        level: '',
        description: ''
      }
    }
  },
  mounted() {
    this.loadHeritages()
  },
  methods: {
    async loadHeritages() {
      try {
        const response = await this.$http.get('/heritages', {
          params: {
            page: this.pagination.page - 1,
            size: this.pagination.size
          }
        })
        if (response.data.code === 200) {
          this.tableData = response.data.data.content
          this.pagination.total = response.data.data.totalElements
        }
      } catch (error) {
        this.$message.error('加载文物列表失败')
      }
    },
    search() {
      this.pagination.page = 1
      this.loadHeritages()
    },
    resetSearch() {
      this.searchForm = { name: '', dynasty: '' }
      this.search()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadHeritages()
    },
    handleCurrentChange(page) {
      this.pagination.page = page
      this.loadHeritages()
    },
    showAddDialog() {
      this.isEdit = false
      this.dialogTitle = '添加文物'
      this.heritageForm = {
        name: '',
        category: '',
        dynasty: '',
        material: '',
        origin: '',
        level: '',
        description: ''
      }
      this.dialogVisible = true
    },
    editHeritage(row) {
      this.isEdit = true
      this.dialogTitle = '编辑文物'
      this.heritageForm = { ...row, id: row.id }
      this.dialogVisible = true
    },
    async saveHeritage() {
      try {
        let response
        if (this.isEdit) {
          response = await this.$http.put(`/heritages/${this.heritageForm.id}`, this.heritageForm)
        } else {
          response = await this.$http.post('/heritages', this.heritageForm)
        }
        if (response.data.code === 200) {
          this.$message.success(this.isEdit ? '编辑成功' : '添加成功')
          this.dialogVisible = false
          this.loadHeritages()
        }
      } catch (error) {
        this.$message.error('保存失败')
      }
    },
    async deleteHeritage(row) {
      this.$confirm('确认删除该文物吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await this.$http.delete(`/heritages/${row.id}`)
          if (response.data.code === 200) {
            this.$message.success('删除成功')
            this.loadHeritages()
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    viewDetail(row) {
      this.currentHeritage = row
      this.detailDialogVisible = true
    },
    async convertToMaster(row) {
      this.$confirm('确认将该文物转换为主数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await this.$http.post(`/heritages/${row.id}/convert-master`)
          if (response.data.code === 200) {
            this.$message.success('转换成功')
            this.loadHeritages()
          } else {
            this.$message.error(response.data.message || '转换失败')
          }
        } catch (error) {
          this.$message.error('转换失败')
        }
      }).catch(() => {})
    },
    async evaluateValue() {
      try {
        const response = await this.$http.get(`/heritages/${this.currentHeritage.id}/evaluate-value`)
        if (response.data.code === 200) {
          this.currentHeritage.estimatedValue = response.data.data.estimatedValue
          this.$message.success('价值评估完成')
        }
      } catch (error) {
        this.$message.error('价值评估失败')
      }
    },
    getQualityTagType(score) {
      if (score >= 90) return 'success'
      if (score >= 70) return 'warning'
      return 'danger'
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}
.search-form {
  margin-bottom: 20px;
}
</style>
