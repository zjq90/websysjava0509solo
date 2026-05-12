<template>
  <div>
    <h2>💊 药品管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-input v-model="searchKeyword" placeholder="搜索药品名称或编码" style="width: 300px" clearable @change="search"></el-input>
        <el-select v-model="searchCategory" placeholder="按分类筛选" style="width: 150px; margin-left: 10px" clearable @change="loadData">
          <el-option label="西药" value="西药"></el-option>
          <el-option label="中成药" value="中成药"></el-option>
          <el-option label="中草药" value="中草药"></el-option>
          <el-option label="特殊药品" value="特殊药品"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">新增药品</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="medicineCode" label="药品编码" width="120"></el-table-column>
        <el-table-column prop="genericName" label="通用名" width="150"></el-table-column>
        <el-table-column prop="tradeName" label="商品名" width="150"></el-table-column>
        <el-table-column prop="specification" label="规格" width="150"></el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="manufacturer" label="生产厂家" width="150"></el-table-column>
        <el-table-column prop="price" label="单价" width="100"></el-table-column>
        <el-table-column prop="stockQuantity" label="库存数量" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.stockQuantity < 10 ? 'danger' : 'success'" size="mini">{{ scope.row.stockQuantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '正常' ? 'success' : 'warning'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="updateStock(scope.row.id)">更新库存</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="药品编码">
          <el-input v-model="form.medicineCode"></el-input>
        </el-form-item>
        <el-form-item label="通用名">
          <el-input v-model="form.genericName"></el-input>
        </el-form-item>
        <el-form-item label="商品名">
          <el-input v-model="form.tradeName"></el-input>
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification"></el-input>
        </el-form-item>
        <el-form-item label="剂型">
          <el-select v-model="form.dosageForm" style="width: 100%">
            <el-option label="片剂" value="片剂"></el-option>
            <el-option label="胶囊" value="胶囊"></el-option>
            <el-option label="注射剂" value="注射剂"></el-option>
            <el-option label="颗粒" value="颗粒"></el-option>
            <el-option label="口服液" value="口服液"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="西药" value="西药"></el-option>
            <el-option label="中成药" value="中成药"></el-option>
            <el-option label="中草药" value="中草药"></el-option>
            <el-option label="特殊药品" value="特殊药品"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生产厂家">
          <el-input v-model="form.manufacturer"></el-input>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit"></el-input>
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="form.stockQuantity" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="用法用量">
          <el-input v-model="form.usage" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="适应症">
          <el-input v-model="form.indication" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="正常" value="正常"></el-option>
            <el-option label="缺货" value="缺货"></el-option>
            <el-option label="下架" value="下架"></el-option>
            <el-option label="过期" value="过期"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Medicine',
  data() {
    return {
      searchKeyword: '',
      searchCategory: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增药品',
      form: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      let url = '/medicine'
      if (this.searchCategory) {
        url = `/medicine/category/${this.searchCategory}`
      }
      this.$http.get(url).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    search() {
      if (this.searchKeyword) {
        this.$http.get(`/medicine/search?keyword=${this.searchKeyword}`).then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data
          }
        })
      } else {
        this.loadData()
      }
    },
    openDialog() {
      this.dialogTitle = '新增药品'
      this.form = {}
      this.dialogVisible = true
    },
    edit(row) {
      this.dialogTitle = '编辑药品'
      this.form = Object.assign({}, row)
      this.dialogVisible = true
    },
    save() {
      if (!this.form.medicineCode) {
        this.$message.warning('请输入药品编码')
        return
      }
      if (!this.form.genericName) {
        this.$message.warning('请输入通用名')
        return
      }
      
      const method = this.form.id ? 'put' : 'post'
      this.$http[method]('/medicine', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    updateStock(id) {
      this.$prompt('请输入更新数量(正数增加，负数减少)', '更新库存', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^-?\d+$/,
        inputErrorMessage: '请输入有效的数字'
      }).then(({ value }) => {
        this.$http.put(`/medicine/stock/${id}?quantity=${value}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('库存更新成功')
            this.loadData()
          }
        })
      }).catch(() => {
      })
    },
    remove(id) {
      this.$confirm('确定要删除该药品吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/medicine/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          }
        })
      })
    }
  }
}
</script>
