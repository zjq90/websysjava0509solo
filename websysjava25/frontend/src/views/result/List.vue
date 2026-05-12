<template>
  <div class="result-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>检验检查结果列表</span>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="results" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="id" label="结果ID" width="80"></el-table-column>
        <el-table-column prop="resultValue" label="结果值" width="150"></el-table-column>
        <el-table-column prop="abnormalFlag" label="异常标识" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.abnormalFlag === 'NORMAL'" type="success" size="small">正常</el-tag>
            <el-tag v-else-if="scope.row.abnormalFlag === 'HIGH'" type="danger" size="small">偏高</el-tag>
            <el-tag v-else-if="scope.row.abnormalFlag === 'LOW'" type="warning" size="small">偏低</el-tag>
            <el-tag v-else type="info" size="small">-</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resultDescription" label="结果描述" width="200"></el-table-column>
        <el-table-column prop="resultSource" label="结果来源" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.resultSource === 'DEVICE'" type="success" size="small">设备采集</el-tag>
            <el-tag v-else type="primary" size="small">人工录入</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationNotes" label="操作备注"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editResult(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" icon="el-icon-check" @click="completeResult(scope.row.id)">完成</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="编辑结果" :visible.sync="dialogVisible" width="500px">
      <el-form ref="form" :model="currentResult" label-width="100px">
        <el-form-item label="结果值">
          <el-input v-model="currentResult.resultValue"></el-input>
        </el-form-item>
        <el-form-item label="异常标识">
          <el-select v-model="currentResult.abnormalFlag" style="width: 100%">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="偏高" value="HIGH"></el-option>
            <el-option label="偏低" value="LOW"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果描述">
          <el-input type="textarea" v-model="currentResult.resultDescription"></el-input>
        </el-form-item>
        <el-form-item label="操作备注">
          <el-input v-model="currentResult.operationNotes"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveResult">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ResultList',
  data() {
    return {
      results: [],
      dialogVisible: false,
      currentResult: {
        resultValue: '',
        abnormalFlag: 'NORMAL',
        resultDescription: '',
        operationNotes: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/results').then(response => {
        this.results = response.data
      }).catch(error => {
        console.error('加载结果失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    editResult(row) {
      this.currentResult = { ...row }
      this.dialogVisible = true
    },
    saveResult() {
      this.$http.put(`/api/results/${this.currentResult.id}/complete`, {}, {
        params: {
          resultValue: this.currentResult.resultValue,
          resultDescription: this.currentResult.resultDescription,
          abnormalFlag: this.currentResult.abnormalFlag
        }
      }).then(() => {
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadData()
      }).catch(error => {
        console.error('保存结果失败:', error)
        this.$message.error('操作失败')
      })
    },
    completeResult(id) {
      this.$confirm('确认完成该结果录入?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.put(`/api/applications/${id}/complete`).then(() => {
          this.$message.success('已完成')
          this.loadData()
        }).catch(error => {
          console.error('完成失败:', error)
          this.$message.error('操作失败')
        })
      })
    }
  }
}
</script>

<style scoped>
.result-list {
  padding: 0;
}
</style>
